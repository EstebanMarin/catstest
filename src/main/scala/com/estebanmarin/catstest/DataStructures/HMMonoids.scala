package com.estebanmarin.catstest.DataStructures

object HMMonoids {
  def code() = {
    import cats.Monoid
    import cats.instances._
    import cats.syntax.monoid._

    def combineFold[T](list: List[T])(implicit monoid: Monoid[T]): T =
      list.foldLeft(monoid.empty)(_ |+| _)

    println(combineFold(List(1, 2, 3, 4, 5)))

    // case class Person(name: String, age: Int)
    // implicit lazy val monoid = Monoid.instance[Person](
    //   Person("", 0),
    //   (p1, p2) => Person(s"${p1.name} loves ${p2.name}", p1.age + p2.age)
    // )
  }

  def run(): Unit = {
    println("-" * 50)
    println("In monooids")
    code()
    println("-" * 50)
  }
}
