package com.estebanmarin.catstest.DataStructures

object HMMonoids {
  import cats.Monoid
  import cats.syntax.monoid._

  def combineFold[T](list: List[T])(implicit monoid: Monoid[T]): T =
    list.foldLeft(monoid.empty)(_ |+| _)

  case class Person(name: String, age: Int)

  //TODO 2 combine a list of phonebooks as Maps[String, Int]

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

  import cats.instances.map._

  val massivePhonebook = combineFold(phoneBooks)

  //TODO 3
  // implement your own monoid
  case class ShoppingCart(item: List[String], total: Double)
  def checkout(shoppingCarts: List[ShoppingCart]): ShoppingCart = ???

  implicit val shopppingCardMonoid = Monoid.instance(
    ShoppingCart(List(), 0),
    (sh1: ShoppingCart, sh2: ShoppingCart) =>
      ShoppingCart(sh1.item |+| sh2.item, sh1.total |+| sh2.total),
  )

  val shoppingCards = List(
    ShoppingCart(List("Car", "Toy", "Fish"), 95),
    ShoppingCart(List("Dippers", "Bandana"), 75),
    ShoppingCart(List("Guitar"), 65),
  )

  def main(args: Array[String]): Unit = {
    println("-" * 50)
    println("In monooids")

    println(combineFold(shoppingCards))
    println("-" * 50)
  }
}
