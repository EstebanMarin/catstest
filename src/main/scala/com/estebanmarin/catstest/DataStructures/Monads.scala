package com.estebanmarin.catstest.DataStructures

import java.util.concurrent.Executors
import scala.concurrent.ExecutionContext
import scala.concurrent.ExecutionContextExecutorService
import scala.concurrent.Future

object MyMonads {

  //list
  val numberList = List(1, 2, 3)

  val charList = List('a', 'b', 'c')

  val combinations = for {
    n <- numberList
    p <- charList
  } yield (n, p)

  // option

  val numberOption = Option(2)
  val charOption = Option('d')

  val combinationChar: Option[(Int, Char)] = for {
    n <- numberOption
    c <- charOption
  } yield (n, c)

  //futures
  implicit val ec: ExecutionContextExecutorService =
    ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(8))

  val numberFuture = Future(42)
  val charFuture = Future('a')

  val futureCombinator: Future[(Int, Char)] = for {
    n <- numberFuture
    c <- charFuture
  } yield (n, c)

  def main(args: Array[String]): Unit = {
    println("-" * 50)
    println("In Monads")
    println("-" * 50)
  }
}
