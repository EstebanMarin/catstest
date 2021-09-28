package com.estebanmarin.catstest.datamanipulation

object Readers {
  final case class Configuration(
      dbUsername: String,
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
    dbUsername = "esteban",
    dbPassword = "test",
    host = "locahost",
    port = 123,
    nThreads = 8,
  )

  import cats.data.Reader
  val dbReader: Reader[Configuration, DbConnection] =
    Reader(conf => DbConnection(conf.dbUsername, conf.dbPassword))
  val dbConn = dbReader.run(configuration)
  val estebanOrderStatus: Reader[Configuration, String] =
    dbReader.map((conn: DbConnection) => conn.getStatus(123))
  def getLastOrderStatus(username: String): String = {
    val userLastOrderIdReader: Reader[Configuration, String] = dbReader
      .map(_.getLastOrderId(username))
      .flatMap(lastOrderId => dbReader.map(_.getStatus(lastOrderId)))
    userLastOrderIdReader.run(configuration)
  }
  def main(args: Array[String]): Unit =
    println(estebanOrderStatus(configuration))
}
