package com.estebanmarin.catstest.DataStructures

import cats.instances.order
import cats.kernel.Comparison

object UsingMonads {
  import cats.Monad
  import cats.instances.list._
  val monadList: Monad[List] = Monad[List]
  val aSimpleList: List[Int] = monadList.pure(2)
  import cats.syntax.applicative._
  import cats.syntax.functor._
  import cats.syntax.flatMap._

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

  def main(args: Array[String]): Unit = {
    println("-" * 50)
    // println(this.anEither)
    println("-" * 50)
  }
}
