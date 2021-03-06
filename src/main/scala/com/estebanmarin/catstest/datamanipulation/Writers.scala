package com.estebanmarin.catstest
package datamanipulation

import scala.concurrent.ExecutionContext
import java.util.concurrent.Executors
import scala.concurrent.Future

object Writers {
  import cats.data.Writer
  //1 - define them at the start
  val aWriter: Writer[List[String], Int] = Writer(List("Started something"), 45)
  // 2 - manioulate with pure FP
  val anIncreasedWriter = aWriter.map(_ + 1)
  val aLogWriter = aWriter.mapWritten(_ :+ "found something interesting")
  val aWritterWithBoth = aWriter.bimap(_ :+ "found something interesting", _ + 1)
  val aWritterWithBoth2 = aWriter.mapBoth { (logs, value) =>
    (logs :+ " found something interesting", value + 1)
  }
  //interesting functionality
  import cats.instances.vector._ // import Semigroup[Vector]

  val writerA = Writer(Vector("Log A1", "Log A2"), 10)
  val writerB = Writer(Vector("Log B1"), 40)
  val compositeWriter = for {
    va <- writerA
    vb <- writerB
  } yield va + vb

  // reset logs
  import cats.instances.list._ // Monoid[List]
  val anEmptyWriter = aWriter.reset
  // 3 dump either value or the logs
  val desiredValue = aWriter.value
  val log = aWriter.written
  // or
  val (l, v) = aWritterWithBoth2.run
  val valuesOfBoth = aWritterWithBoth2.run

  def countAndSay(n: Int): Unit =
    if (n <= 0) println("starting")
    else {
      countAndSay(n - 1)
      println(n)
    }

  def countAndLog(n: Int): Writer[Vector[String], Int] = Writer(Vector("Starting"), n)
  def countAndLogWithWriter(n: Int): Writer[Vector[String], Int] =
    if (n <= 0) Writer(Vector("Starting!"), 0)
    else countAndLogWithWriter(n - 1).flatMap(_ => Writer(Vector(s"$n"), n))

  val countAndLog = countAndLogWithWriter(3).run

  def naiveSum(n: Int): Int =
    if (n <= 0) 0
    else {
      println(s"now in $n")
      val lowerSum = naiveSum(n - 1)
      println(s"Computed sum (${n - 1}) = $lowerSum")
      lowerSum + n
    }

  // Benefit #2: Writers can keep logs separate on multiple threads

  implicit val ec: ExecutionContext =
    ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(8))

  def sumWithWriter(n: Int): Writer[Vector[String], Int] =
    if (n <= 0) Writer(Vector(), 0)
    else
      for {
        _ <- Writer(Vector(s"Now at $n"), n)
        lowerSum <- sumWithWriter(n - 1)
        _ <- Writer(Vector(s"Computed sum (${n - 1}) = $lowerSum"), n)
      } yield lowerSum + n

  def run(args: Array[String]): Unit = {
    // def main(args: Array[String]): Unit = {
    println("-" * 50)
    // we can keep logs from threads separate
    val sumFuture1 = Future(sumWithWriter(10))
    val sumFuture2 = Future(sumWithWriter(10))
    val logs1 = sumFuture1.map(_.written) // logs from thread 1
    val logs2 = sumFuture2.map(_.written) // logs from thread 2
    println("-" * 50)
  }
}
