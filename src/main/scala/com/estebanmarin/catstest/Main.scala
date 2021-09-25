package com.estebanmarin
package catstest

import scala.util.Try

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

  println("─" * 100)
}
