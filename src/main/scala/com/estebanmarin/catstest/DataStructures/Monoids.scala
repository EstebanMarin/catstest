package com.estebanmarin.catstest
package DataStructures

object Monoids {
  def code() = {
    import cats.Semigroup
    import cats.instances.int._ // importing implicits
    import cats.syntax.semigroup._ //import |+|

    val numbers = (1 to 1000).toList
    val sumRight = numbers.foldRight(0)(_ |+| _)
    val sumLeft = numbers.foldLeft(0)(_ |+| _)

    // def combineFold[T](list: List[T])(implicit fold: Semigroup[T]): T =
    //   list.foldLeft()(_ |+| _)
    // extend the semigroup to a monoid

    import cats.Monoid
    val intMonoid = Monoid[Int]
    val combineInt = intMonoid.combine(23, 99)
    val zero = intMonoid.empty

    import cats.instances._

    val emptyString = Monoid[String].empty

    println(emptyString)
  }
  def run(): Unit = {
    println("-" * 50)
    println("In monoids")
    code()
    println("-" * 50)

  }
}
