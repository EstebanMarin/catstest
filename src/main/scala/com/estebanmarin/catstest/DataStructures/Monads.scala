package com.estebanmarin.catstest.DataStructures

import java.util.concurrent.Executors
import scala.concurrent.ExecutionContext
import scala.concurrent.ExecutionContextExecutorService
import scala.concurrent.Future

object MyMonads {

  //list
  val numberList = List(1, 2, 3)

  val charList = List('a', 'b', 'c')

  val combinations = for {
    n <- numberList
    p <- charList
  } yield (n, p)

  // option

  val numberOption = Option(2)
  val charOption = Option('d')

  val combinationChar: Option[(Int, Char)] = for {
    n <- numberOption
    c <- charOption
  } yield (n, c)

  //futures
  implicit val ec: ExecutionContextExecutorService =
    ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(8))

  val numberFuture = Future(42)
  val charFuture = Future('a')

  val futureCombinator: Future[(Int, Char)] = for {
    n <- numberFuture
    c <- charFuture
  } yield (n, c)

  /*
  Pattern
  - wrappting a value into a Monadic value
  - the flatMap mechanism
  MONAD
   */

  trait MyMonad[M[_]] {
    def pure[A](value: A): M[A]
    def flatMap[A, B](ma: M[A])(f: A => M[B]): M[B]
  }

  import cats.Monad
  import cats.instances.option._ // implicit Monad[Option]
  val optionMonad = Monad[Option]

  val anOption: Option[Int] = optionMonad.pure(4) // Some (4)

//   val anOptionTransformed = aTransformedOption(anOption)
  val aTransformedOption: Option[Int] =
    optionMonad.flatMap(anOption)(x => if (x % 3 == 0) Some(x + 1) else None)

  import cats.instances.list._
  val listMonads: Monad[List] = Monad[List]
  val aList: List[Int] = listMonads.pure(3)
  val aTransformedList: List[Int] = listMonads.flatMap(aList)(x => List(x + 1))

  def main(args: Array[String]): Unit = {
    println("-" * 50)
    println("In Monads")
    println(this.aList)
    println("-" * 50)
  }
}
