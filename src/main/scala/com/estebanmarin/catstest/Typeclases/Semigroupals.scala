package com.estebanmarin.catstest.Typeclases

import scala.concurrent.ExecutionContext
import java.util.concurrent.Executors
import scala.concurrent.Future

object SemigroupalsMine {
  trait SemigroupalsMine[F[_]] {
    def product[A, B](fa: F[A], fb: F[B]): F[(A, B)]
  }

  import cats.Semigroupal
  import cats.instances.option._
  import cats.Monad
  val optionSemigroupal = Semigroupal[Option]
  val aTupledOption = optionSemigroupal.product(Some(123), Some("a string"))
  val aNonTuple = optionSemigroupal.product(Some(1234), None)

  import cats.instances.future._
  implicit val ec: ExecutionContext =
    ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(8))

  val aTupledFuture = Semigroupal[Future].product(Future("Meaning of life"), Future(42))

  import cats.instances.list._
  val aTupledList: List[(Int, Char)] = Semigroupal[List].product(List(1, 2, 3), List('a', 'b'))

  //TODO
  import cats.syntax.functor._ //pure is here
  import cats.syntax.flatMap._ // flatMap is here

  def productWithMonads[F[_], A, B](fa: F[A], fb: F[B])(implicit monad: Monad[F]): F[(A, B)] = for {
    a <- fa
    b <- fb
  } yield (a, b)

  def main(args: Array[String]): Unit = {

    println("-" * 50)
    println(aTupledList)
    println("-" * 50)
  }
}
