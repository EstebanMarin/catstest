package com.estebanmarin.catstest
package datamanipulation

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

  def main(args: Array[String]): Unit = {
    println("-" * 50)
    println(countAndLog)
    println("-" * 50)
  }
}
