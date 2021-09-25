package com.estebanmarin
package catstest

object Main extends App {
  println("─" * 100)
  // implicit classes

  case class Person(name: String) {
    def greet: String = s"Hi, My name is $name"
  }
  implicit class ImpersonableString(name: String) {
    println("Hello in the implicit")
    def greet: String = s"Hello $name"
  }

  val greeting = "Petter".greet
//importing implicits conversions in Scope
  import scala.concurrent.duration._
  val oneSet = 1.second
  println(oneSet)
  //implicit arguments and values
  def increment(x: Int)(implicit amount: Int) = x + amount
  implicit val amount = 4
  println(increment(4))

  trait JSONSerializer[T] {
    def toJson(value: T): String
  }

  def listToJson[T](list: List[T])(implicit serializer: JSONSerializer[T]): String =
    list.map(value => serializer.toJson(value)).mkString("[", ",", "]")

  implicit val personSerializer: JSONSerializer[Person] = new JSONSerializer[Person] {
    def toJson(person: Person): String = person.name
  }

  val personJson = listToJson(List(Person("Alice"), Person("Bob")))
  println(personJson)

  //implicit argument is used to PROVE THE EXISTENCE of a type
  println("─" * 100)
}
