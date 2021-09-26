package com.estebanmarin.catstest.DataStructures

import java.util.concurrent.Executors
import scala.concurrent.ExecutionContext
import scala.concurrent.ExecutionContextExecutorService
import scala.concurrent.Future

object MyMonads {

  //list
  val numberList: List[Int] = List(1, 2, 3)

  val charList: List[Char] = List('a', 'b', 'c')

  val combinations: List[(Int, Char)] = for {
    n <- numberList
    p <- charList
  } yield (n, p)

  // option

  val numberOption: Option[Int] = Option(2)
  val charOption: Option[Char] = Option('d')

  val combinationChar: Option[(Int, Char)] = for {
    n <- numberOption
    c <- charOption
  } yield (n, c)

  //futures
  implicit val ec: ExecutionContextExecutorService =
    ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(8))

  val numberFuture: Future[Int] = Future(42)
  val charFuture: Future[Char] = Future('a')

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
    // Notice that as with functor mondas provide .map
    //Monads extends functors
    def map[A, B](ma: M[A])(f: A => B): M[B] = flatMap(ma)(a => pure(f(a)))
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
  val aTransformedList: List[Int] = listMonads.flatMap(aList)(x => List(x + 1)) //List(3, 4)

  //TODO 2: Use a monad of a Future
  import cats.instances.future._
  val futureMonad: Monad[Future] = Monad[Future]
  val aFuture: Future[Int] = futureMonad.pure(42)
  val aTransformedFuture: Future[Int] = futureMonad.flatMap(aFuture)(x => Future(x + 50))

  //specialized API
  def getPairsList(number: List[Int], chars: List[Char]): List[(Int, Char)] =
    for {
      n <- number
      c <- chars
    } yield (n, c)

  def getPairOption(number: Option[Int], chars: Option[Char]): Option[(Int, Char)] =
    for {
      n <- number
      c <- chars
    } yield (n, c)

  def getPairFuture(number: Future[Int], chars: Future[Char]): Future[(Int, Char)] =
    for {
      n <- number
      c <- chars
    } yield (n, c)

  // let generalize the concept
//   def do10x[F[_]](container: F[Int])(implicit functor: Functor[F]): F[Int] =
//     functor.map(container)(_ * 10
  def generalizePairs[M[_], A, B](a: M[A], b: M[B])(implicit monad: Monad[M]): M[(A, B)] =
    monad.flatMap(a)(a => monad.map(b)(b => (a, b)))

  // extension methods have wierd imports - pure and flatmaps
  import cats.syntax.applicative._ //pure is here
  val oneOption: Option[Int] = 1.pure[Option] // implicit Monad[Option] will be some(1)
  val oneList: List[Int] = 1.pure[List] // implicit Monad[Option] will be List(1)

  import cats.syntax.flatMap._ // flatMap is here
  val oneOptionTransformed2: Option[Int] = oneOption.flatMap(x => (x + 1).pure[Option])

// TODO 3
// implement map method in MyMonad, meaning a monads is a functor as well
  //monads extends Functors
  val oneOptionMapped: Option[Int] = Monad[Option].map(Option(2))(_ + 1)
  import cats.syntax.functor._
  val oneOptionMapped2: Option[Int] = oneOption.map(_ + 1)

  //for comprehensions

  val composedOptionFor: Option[Int] = for {
    one <- 1.pure[Option]
    two <- 2.pure[Option]
  } yield (one + two)

  //implement a shoter version of get pairs

  def generalizePairs2[M[_], A, B](a: M[A], b: M[B])(implicit monad: Monad[M]): M[(A, B)] =
    for {
      a <- a
      b <- b
    } yield (a, b)

  def main(args: Array[String]): Unit = {
    println("-" * 50)
    println("In Monads")
    println(generalizePairs(numberList, charList))
    println(generalizePairs(numberOption, charOption))
    println(generalizePairs(numberFuture, charFuture).foreach(println))
    println(this.oneOptionMapped)
    println("-" * 50)
  }
}
