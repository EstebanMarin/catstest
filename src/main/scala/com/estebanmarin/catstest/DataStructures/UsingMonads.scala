package com.estebanmarin.catstest.DataStructures

object UsingMonads {
  import cats.Monad
  import cats.instances.list._
  val monadList = Monad[List]
  val aSimpleList = monadList.pure(2)

  def main(args: Array[String]): Unit = {
    println("-" * 50)
    println("-" * 50)
  }
}
