package com.estebanmarin.catstest.DataStructures

import scala.util.Try
import cats.instances.list

object Functors {
  val aModifiedList = List(1, 2, 3, 4).map(_ + 2)
  val aModifiedOption = Option(2).map(_ + 2)
  val aModifiedTry = Try(42).map(_ + 2)

  //simplified version of a Functor
  // functor maps between categories
  // its also a Type class that we can define with a trait

  trait MyFunctor[F[_]] {
    def map[A, B](initialValue: F[A])(f: A => A): F[B]
  }

  // now that Functor is defined, lets use it to sharpen our intuition
  // part 1 -  type class definition
  import cats.Functor
  import cats.instances.list._
  // part 2 -  type class instance that we are going to use
  val listFunctor = Functor[List]

  val incrementedNumber: List[Int] = listFunctor.map(List(1, 2, 3))(_ + 1) //list(2.3.4)

  import cats.instances.option._ // Includes Functor[Option]
  val optionFunctor: Functor[Option] = Functor[Option]
  val incrementedOption = optionFunctor.map(Option(2))(_ + 2) //Some(3)

  import cats.instances.try_._

  val anIncrementedTry = Functor[Try].map(Try(42))(_ + 1)

  //generalizing API
  def do10xList(list: List[Int]): List[Int] = list.map(_ * 10)
  def do10xOption(option: Option[Int]): Option[Int] = option.map(_ * 10)
  def do10xTry(attemp: Try[Int]): Try[Int] = attemp.map(_ * 10)

  def do10x[F[_]](container: F[Int])(implicit functor: Functor[F]): F[Int] =
    functor.map(container)(_ * 10)

  //TODO - 1 define your own functor for a Binary tree
  // create your onw Functor
  // define an object which extends Functor[Tree]

  implicit object treeFunctor extends Functor[Tree] {
    override def map[A, B](fa: Tree[A])(f: A => B): Tree[B] = fa match {
      case Branch(v, left, right) => Branch(f(v), map(left)(f), map(right)(f))
      case Leaf(v) => Leaf(f(v))
    }
  }

  sealed trait Tree[+T]
  // "smart" constructor
  object Tree {
    def leaf[T](value: T): Leaf[T] = Leaf(value)
    def branch[T](
        value: T,
        left: Tree[T],
        right: Tree[T],
      ): Tree[T] = Branch(value, left, right)
  }
  final case class Leaf[+T](value: T) extends Tree[T]
  final case class Branch[+T](
      value: T,
      left: Tree[T],
      right: Tree[T],
    ) extends Tree[T]

  //extension methods - map
  // we will put in scope
  import cats.syntax.functor._

  val testTree: Tree[Int] =
    Tree.branch(40, Tree.branch(5, Tree.leaf(10), Tree.leaf(30)), Tree.leaf(20))

  val incrementedTree: Tree[Int] = testTree.map(_ + 10)

  //TODO - 3
  // write a shorter method of with extension methods
  // generalize this function
  // def do10x[F[_]](container: F[Int])(implicit functor: Functor[F]): F[Int] =
  // functor.map(container)(_ * 1)

  // Context bound Implicit
  def do10xShorter[F[_]: Functor](container: F[Int]): F[Int] =
    container.map(_ * 10)

  def run(args: Array[String]): Unit = {
//   def main(args: Array[String]): Unit = {
    println("-" * 50)
    // println(do10x(List(1, 2, 3)))
    // println(do10x(Option(20)))
    // println(do10x(Try(42)))
    // //notice that this Functor in Scope def do10x[F[_]](container: F[Int])(implicit functor: Functor[F]): F[Int]
    // // Now that we implemented the Functor for our tree, we can now apply do10x
    // println(do10x[Tree](Tree.branch(30, Tree.leaf(10), Tree.leaf(10))))

    println(do10x(this.testTree))
    println(do10xShorter(this.testTree))
    println()
    println("-" * 50)
  }
}
