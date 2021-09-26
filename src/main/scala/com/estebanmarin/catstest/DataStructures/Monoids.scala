package com.estebanmarin.catstest
package DataStructures

object Monoids {
  def run(): Unit = {
    import cats.Semigroup
    import cats.instances.int._
    import cats.syntax.semigroup._ //import |+|

    val numbers = (1 to 1000).toList
    val sumRight = numbers.foldRight(0)(_ |+| _)
    val sumLeft = numbers.foldLeft(0)(_ |+| _)

  }
}
