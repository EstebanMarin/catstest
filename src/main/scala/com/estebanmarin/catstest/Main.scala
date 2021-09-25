package com.estebanmarin
package catstest

object TypeClasses {

  //Type Classes: A programming pattern to add capabiilties to exisiting types
  case class Person(name: String, age: Int)

  //part 1 type class definition
  trait JSONSerializer[T] {
    def toJson(value: T): String
  }

  //part 2 - create implicit type class INSTANCES
  implicit object StringSerializer extends JSONSerializer[String] {
    override def toJson(value: String) = "\"" + value + "\""
  }

  implicit object IntSerializer extends JSONSerializer[Int] {
    override def toJson(value: Int): String = value.toString()
  }

  implicit object PersonSerializer extends JSONSerializer[Person] {
    override def toJson(value: Person): String =
      s"""
      |{"name": ${value.name}}, "age": ${value.age}
      """.stripMargin.trim()
  }

  //part 3 - offer some API
  def convertListToJSON[T](list: List[T])(implicit serializer: JSONSerializer[T]): String =
    list.map(value => serializer.toJson(value)).mkString("[", ",", "]")

  //part 4 extending types
  object JSONSyntax {
    implicit class JSONSerializable[T](value: T)(implicit serializer: JSONSerializer[T]) {
      def toJson: String = serializer.toJson(value)
    }
  }

  def main(args: Array[String]): Unit = {
    import JSONSyntax._
    println(Person("Esteban", 35).toJson)
  }
}
