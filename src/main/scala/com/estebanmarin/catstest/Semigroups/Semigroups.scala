package com.estebanmarin.catstest
package Semigroups

object Semigroups {
  //Semigroups combine
  import cats.Semigroup
  import cats.instances._

  val naturalIntSemigroup: Semigroup[Int] = Semigroup[Int]
  val intCombinations: Int = naturalIntSemigroup.combine(42, 6)

  val naturalStringSemigroup: Semigroup[String] = Semigroup[String]
  val stringCombination: String = naturalStringSemigroup.combine("Esteban", "Gisellita")

  def reduceInts(list: List[Int]): Int = list.reduce(naturalIntSemigroup.combine)
  def reduceStrings(list: List[String]): String = list.reduce(naturalStringSemigroup.combine)

  //lets define a generalization
  def reduceThings[T](list: List[T])(implicit semigroup: Semigroup[T]): T =
    list.reduce(semigroup.combine)

  def run(): Unit = {
    println("-" * 50)
    reduceInts((1 to 10).toList)
    reduceStrings(List("Esteban ", "ama", " a Gisellita"))
    reduceThings[String](List("Esteban ", "ama", " a Gisellita"))

    import cats.instances.option._
    val numberOptions: List[Option[Int]] = (1 to 10).toList.map(Option(_))

    reduceThings(numberOptions)
    reduceThings(List(Option("Esteban "), Option("ama a Gisellita")))

    case class Expense(id: Long, amount: Double)
    implicit val expense: Semigroup[Expense] =
      Semigroup.instance[Expense]((ex1, ex2) => Expense(ex1.id + ex2.id, ex1.amount + ex2.amount))

    val expense1 = Expense(1234, 50)
    val expense2 = Expense(1235, 50)

    println(reduceThings(List(expense1, expense2)))

    println("-" * 50)
  }
}
