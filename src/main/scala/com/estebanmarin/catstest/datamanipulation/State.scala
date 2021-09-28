package com.estebanmarin.catstest.datamanipulation

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

  case class ShoppingCart(items: List[String], total: Double)

  def addToCart(item: String, price: Double): State[ShoppingCart, Double] =
    State((cart: ShoppingCart) =>
      (ShoppingCart(cart.items :+ item, cart.total + price), cart.total + price)
    )

  val estebanCart: State[ShoppingCart, Double] = for {
    _ <- addToCart("shoes", 15.6)
    _ <- addToCart("Kitchen Supplies", 115.6)
    total <- addToCart("Wireless mouse", 115.6)
  } yield total

  //TODO mental gymnastics
  def inspect[A, B](f: A => B): State[A, B] = State((a: A) => (a, f(a)))
  def get[A]: State[A, A] = State((a: A) => (a, a))
  def set[A](value: A): State[A, Unit] = State((_: A) => (value, ()))
  def modifiy[A](f: A => A): State[A, Unit] = State((a: A) => (f(a), ()))

  //Methods available already
  import cats.data.State._
  //were a reducing imperative computations to declarative computatins with forComprehension
  val program: State[Int, (Int, Int, Int)] = for {
    // don't modify the the state
    a <- get[Int]
    // modify the state
    _ <- set[Int](a + 10)
    // get value b from state
    b <- get[Int]
    //modift current state with function
    _ <- modify[Int](_ + 43)
    // get the state at that moment aned runs a transformation
    c <- inspect[Int, Int](_ * 2)
  } yield (a, b, c)

  def main(args: Array[String]): Unit = {
    val initialShoppingCart = ShoppingCart(List(), 0.0)
    println("-" * 50)
    // println(iterativeFor.run(10).value)
    // println(addToCart("shoes", 15.6).run(initialShoppingCart).value)
    println(estebanCart.run(initialShoppingCart).value)
    println("-" * 50)
  }
}
