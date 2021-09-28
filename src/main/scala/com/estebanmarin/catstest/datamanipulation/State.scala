package com.estebanmarin.catstest.datamanipulation

import cats.Eval
import cats.data.IndexedStateT

object States {
  type MyState[S, A] = S => (S, A)

  import cats.data.State

  val countAndStay: State[Int, String] =
    State(currentCount => (currentCount + 1, s"counted: $currentCount"))
  val (eleven, counted10) = countAndStay.run(10).value

  //pure FP
  val firstTransformation: State[Int, String] =
    State((s: Int) => (s + 1, s"Added 1 to 10,obtained ${s + 1}"))

  val secondTransformation: State[Int, String] =
    State((s: Int) => (s * 5, s"Multipled with 5, obtained ${s * 5}"))

  val iterativeComputation: State[Int, (String, String)] =
    firstTransformation
      .flatMap((x: String) => secondTransformation.map((y: String) => (x, y)))

  val iterativeFor: State[Int, (String, String)] = for {
    x <- firstTransformation
    y <- secondTransformation
  } yield (x, y)

  def main(args: Array[String]): Unit = {
    println("-" * 50)
    println(iterativeFor.run(10).value)
    println("-" * 50)
  }
}
