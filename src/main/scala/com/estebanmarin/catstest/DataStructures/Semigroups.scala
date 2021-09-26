package com.estebanmarin.catstest
package DataStructures

object Semigroups {
  //Semigroups combine
  import cats.Semigroup

  val naturalIntSemigroup: Semigroup[Int] = Semigroup[Int]
  val intCombinations: Int = naturalIntSemigroup.combine(42, 6)

  val naturalStringSemigroup: Semigroup[String] = Semigroup[String]
  val stringCombination: String = naturalStringSemigroup.combine("Esteban", "Gisellita")

  def reduceInts(list: List[Int]): Int = list.reduce(naturalIntSemigroup.combine)
  def reduceStrings(list: List[String]): String = list.reduce(naturalStringSemigroup.combine)

  //lets define a generalization
  def reduceThings[T](list: List[T])(implicit semigroup: Semigroup[T]): T =
    list.reduce(semigroup.combine)

  case class Expense(id: Long, amount: Double)

  implicit val expense: Semigroup[Expense] =
    Semigroup.instance[Expense]((ex1, ex2) =>
      Expense(Math.max(ex1.id, ex2.id), ex1.amount + ex2.amount)
    )

  // lets extend the methods of the Semgroup to our Type
  import cats.syntax.semigroup._
  val combineInts = 2 |+| 3
  val combineStrings = "Hello " |+| "World"
  val combineExpenses = Expense(123, 1234) |+| Expense(1235, 50)

  // implement reduce things without the implicit by adding a context to the Generic and using the \+\ operator

  def reduceThings2[T: Semigroup](list: List[T]): T =
    list.reduce(_ |+| _)

  // def main(args: Array[String]): Unit = {
  def run(args: Array[String]): Unit = {
    println("-" * 50)
    reduceInts((1 to 10).toList)
    reduceStrings(List("Esteban ", "ama", " a Gisellita"))
    reduceThings[String](List("Esteban ", "ama", " a Gisellita"))

    import cats.instances.option._
    val numberOptions: List[Option[Int]] = (1 to 10).toList.map(Option(_))

    reduceThings(numberOptions)
    reduceThings(List(Option("Esteban "), Option("ama a Gisellita")))

    val expense1 = Expense(1234, 50)
    val expense2 = Expense(1235, 50)

    println(reduceThings2(List(expense1, expense2)))

    // lest implement the extension methods

    println("-" * 50)
  }
}
