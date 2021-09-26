package com.estebanmarin.catstest.DataStructures

object HMMonoids {
  import cats.Monoid
  import cats.instances._
  import cats.syntax.monoid._

  def combineFold[T](list: List[T])(implicit monoid: Monoid[T]): T =
    list.foldLeft(monoid.empty)(_ |+| _)

  case class Person(name: String, age: Int)

  //TODO 2

  val phoneBooks = List(
    Map(
      "alice" -> 123,
      "Bob" -> 1235,
    ),
    Map(
      "charlie" -> 789,
      "Dick" -> 456,
    ),
    Map(
      "virginia" -> 753
    ),
  )

  def main(args: Array[String]): Unit = {
    println("-" * 50)
    println("In monooids")
    println("-" * 50)
  }
}
