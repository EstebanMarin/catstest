package com.estebanmarin.catstest.DataStructures

import cats.data.EitherT
import scala.concurrent.ExecutionContext
import java.util.concurrent.Executors
import scala.concurrent.Future

object MonadTransformers {
  def sumAllOptions(values: List[Option[Int]]): Int = ???

  //OptionTransforme
  import cats.data.OptionT
  import cats.instances.list._ // fetch implicit ListT[Int]
  import cats.instances.future._

  val listOfInt: OptionT[List, Int] = OptionT(List(Option(1), Option(2)))
  val listOfCharacters: OptionT[List, Char] = OptionT(List(Option('a'), Option('b')))

  val listOfTuples: OptionT[List, (Int, Char)] = for {
    number <- listOfInt
    char <- listOfCharacters
  } yield (number, char)

  //either transformer
  import cats.data.EitherT
  val listOfEithers: EitherT[List, String, Int] = EitherT(
    List(Left("Something"), Right(42), Right(8))
  )
  implicit val ex: ExecutionContext =
    ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(8))

  val futureOfEither: EitherT[Future, Nothing, Int] = EitherT.right(Future(45))

  val bandWidths = Map(
    "server1.estebanmarin.com" -> 50,
    "server2.estebanmarin.com" -> 100,
    "server3.estebanmarin.com" -> 175,
  )

  type AsyncResponse[T] = EitherT[Future, String, T]

  def getBandWidth(server: String): AsyncResponse[Int] = bandWidths.get(server) match {
    case None => EitherT.left(Future(s"server $server unreachable"))
    case Some(value) => EitherT.right(Future(value))
  }

  def canWithStandSurge(s1: String, s2: String): AsyncResponse[Boolean] = for {
    bandS1: Int <- getBandWidth(s1)
    bandS2: Int <- getBandWidth(s2)
  } yield bandS1 + bandS2 > 250

  def generateTrafficSpikeReport(s1: String, s2: String): AsyncResponse[String] =
    canWithStandSurge(s1, s2).transform {
      case Left(reason) => Left(s"s1 and s2 cannot withstand reason: $reason")
      case Right(false) => Left(s"not enough bandwidth")
      case Right(true) => Right(s"no problemo")
    }

  def main(args: Array[String]): Unit = {
    println("-" * 50)
    val resultFuture: Future[Either[String, String]] =
      generateTrafficSpikeReport("server1.estebanmarin.com", "server5.estebanmarin.com").value
    resultFuture.foreach(println)
    println("-" * 50)
  }
}
