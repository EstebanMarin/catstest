package com.estebanmarin.catstest.DataStructures

import cats.instances.order
import cats.kernel.Comparison

object UsingMonads {
  import cats.Monad
  import cats.instances.list._
  val monadList: Monad[List] = Monad[List]
  val aSimpleList: List[Int] = monadList.pure(2)

  //below applicative to a types
  val anExtendedList: List[Int] = for {
    a <- aSimpleList
  } yield (a + 1)

  val anExtendedList2: List[Int] = aSimpleList.flatMap(x => List(x + 1))

  //either
  val aManualEither: Either[String, Int] = Right(42)

  type LoadingOr[T] = Either[String, T]
  type ErrorOr[T] = Either[Throwable, T]

  import cats.instances.either._
  val loadingMonad = Monad[LoadingOr]
  val anEither = loadingMonad.pure(45)
  val aChangedLoading = loadingMonad.flatMap(anEither)(n =>
    if (n % 2 == 0) Right(n + 1) else Left("Loading meaning of life")
  )

  //imaginary online store
  case class OrderStatus(orderId: Long, status: String)
  def getOrderStatus(orderId: Long): LoadingOr[OrderStatus] = Right(
    OrderStatus(orderId, "ready to ship")
  )
  def trackLocation(orderStatus: OrderStatus): LoadingOr[String] =
    if (orderStatus.orderId > 1000) Left("Not available yet")
    else Right("Amsterdam, NL")

  //say that you want to get the the order status and the track the location of the package
  val orderID = 4567

  def orderLocation(orderId: Long): LoadingOr[String] =
    loadingMonad.flatMap(getOrderStatus(orderId))(orderStatus => trackLocation(orderStatus))

  //use the extension methods
  import cats.syntax.applicative._
  import cats.syntax.functor._
  import cats.syntax.flatMap._
  val orderLocationBetter: LoadingOr[String] = for {
    status <- getOrderStatus(orderID)
    location <- trackLocation(status)
  } yield location

  //TODO: Service layer API of a web APP
  case class Connection(host: String, port: String)
  type Port = String
  type Host = String
  type Config = Map[Host, Port]
  val conf: Config = Map(
    "host" -> "localhost",
    "port" -> "4040",
  )

  trait HttpService[M[_]] {
    def getConnection(cif: Config): M[Connection]
    def issueRequest(connection: Connection, payload: String): M[String]
  }
  /*
  TODO provide a real implementatio of HTTP service using either a TRy Option Future Either
   */
  object OptionHttpService extends HttpService[Option] {
    override def getConnection(cif: Config): Option[Connection] = for {
      h <- conf.get("host")
      p <- conf.get("port")
    } yield Connection(h, p)

    override def issueRequest(connection: Connection, payload: String): Option[String] =
      if (payload.length > 50) None else Some(s"Request $payload has been accpeted")
  }

  // TODO implement another HTTPServive with LoadingOr or ErrorOr
  object LoadingOrHttpService extends HttpService[LoadingOr] {
    override def getConnection(cif: Config): LoadingOr[Connection] =
      if (!cif.contains("host") || !cif.contains("port"))
        Left("Error Connexion")
      else
        Right(Connection(conf("host"), conf("port")))
    override def issueRequest(connection: Connection, payload: String): LoadingOr[String] =
      if (payload.length > 50) Left("Paila") else Right(s"Request $payload has been accpeted")
  }

  def main(args: Array[String]): Unit = {
    println("-" * 50)
    val response: Option[String] = OptionHttpService
      .getConnection(conf)
      .flatMap(connexion => OptionHttpService.issueRequest(connexion, "Hello Esteban"))

    val secondResponse: Option[String] = for {
      conn: Connection <- OptionHttpService.getConnection(conf)
      response: String <- OptionHttpService.issueRequest(conn, "Hello Second Time")
    } yield response

    println(response)
    println(secondResponse)
    println("-" * 50)
  }
}
