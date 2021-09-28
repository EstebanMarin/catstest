package com.estebanmarin.catstest.datamanipulation

import cats.data.Kleisli
import cats.Id

object Readers {
  final case class Configuration(
      dbUsername: String,
      email: String,
      dbPassword: String,
      host: String,
      port: Int,
      nThreads: Int,
    )
  final case class DbConnection(username: String, password: String) {
    def getStatus(orderId: Long): String = "dispatched"
    def getLastOrderId(username: String): Long = 1234
  }
  final case class HttpService(host: String, port: Int) {
    def start(): Unit = println("Server started")
  }

  val configuration = Configuration(
    dbUsername = "fuckyou@bots.%&%*",
    email = "fuckyou@bots.%",
    dbPassword = "test",
    host = "locahost",
    port = 123,
    nThreads = 8,
  )

  import cats.data.Reader
  val dbReader: Reader[Configuration, DbConnection] =
    Reader(conf => DbConnection(conf.dbUsername, conf.dbPassword))
  val serverReader: Reader[Configuration, HttpService] =
    Reader(conf => HttpService(host = conf.host, port = conf.port))
  val dbConn = dbReader.run(configuration)
  val server = serverReader.run(configuration)

  val estebanOrderStatus: Reader[Configuration, String] =
    dbReader.map((conn: DbConnection) => conn.getStatus(123))
  def getLastOrderStatus(username: String): String = {
    val userLastOrderIdReader: Reader[Configuration, String] = dbReader
      .map(_.getLastOrderId(username))
      .flatMap(lastOrderId => dbReader.map(_.getStatus(lastOrderId)))
    userLastOrderIdReader.run(configuration)
  }

  def userOrderFor(username: String): Reader[Configuration, String] = for {
    _ <- serverReader.map(_.start())
    lastOrderId <- dbReader.map(_.getLastOrderId(username))
    status <- dbReader.map(_.getStatus(lastOrderId))
  } yield status

  //TODO
  final case class EmailService(emailReplyTo: String) {
    def sendEmail(address: String, contents: String): String =
      s"From: $emailReplyTo; to >>> $address"
  }

  def emailUser(username: String, userEmail: String): String = {
    val emailServiceReader: Reader[Configuration, EmailService] =
      Reader(conf => EmailService(conf.email))
    val emailReader: Reader[Configuration, String] = for {
      _ <- serverReader.map(_.start())
      lastOrder <- dbReader.map(_.getLastOrderId(username))
      status <- dbReader.map(_.getStatus(lastOrder))
      emailService <- emailServiceReader
    } yield emailService.sendEmail(userEmail, s"your last order has the status: $status")

    emailReader.run(configuration)
  }

  // def main(args: Array[String]): Unit = {
  def run(args: Array[String]): Unit = {
    println(estebanOrderStatus(configuration))
    println(getLastOrderStatus("esteban"))
    println(userOrderFor("esteban").run(configuration))
  }
}
