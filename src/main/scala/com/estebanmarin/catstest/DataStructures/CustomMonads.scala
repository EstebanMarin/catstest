package com.estebanmarin.catstest.DataStructures

import scala.annotation.tailrec

object CustomMonads {
  import cats.Monad
  implicit object OptionMonad extends Monad[Option] {
    override def flatMap[A, B](fa: Option[A])(f: A => Option[B]): Option[B] =
      fa.flatMap(f)
    //Ensure that does not Stack Overflow
    @tailrec
    override def tailRecM[A, B](a: A)(f: A => Option[Either[A, B]]): Option[B] = f(a) match {
      case None => None
      case Some(Left(v)) => tailRecM(v)(f)
      case Some(Right(v)) => Some(v)
    }
    override def pure[A](x: A): Option[A] =
      Option(x)
  }
  //TODO - 1: definie a monad for the identity type

  type Identity[T] = T
  val aNumber: Identity[Int] = 42

  implicit object IndentyMonad extends Monad[Identity] {
    import cats.syntax.applicative._ //pure is here
    import cats.syntax.flatMap._ // flatMap is here
    override def flatMap[A, B](fa: Identity[A])(f: A => Identity[B]): Identity[B] = f(fa)
    @tailrec
    override def tailRecM[A, B](a: A)(f: A => Identity[Either[A, B]]): Identity[B] = f(a) match {
      case Left(value) => tailRecM(value)(f)
      case Right(value) => value
    }
    override def pure[A](x: A): Identity[A] =
      Monad[Identity].pure(x)
  }

  //Harder Example
  sealed trait Tree[+A]
  final case class Leaf[+A](value: A) extends Tree[A]
  final case class Branch[+A](left: Tree[A], right: Tree[A]) extends Tree[A]
  //TODO 2 Define a monad for this tree

  implicit object TreeMonad extends Monad[Tree] {
    override def flatMap[A, B](ta: Tree[A])(f: A => Tree[B]): Tree[B] = ta match {
      case Branch(left, right) => Branch(flatMap(left)(f), flatMap(right)(f))
      case Leaf(value) => f(value)
    }

    override def tailRecM[A, B](a: A)(f: A => Tree[Either[A, B]]): Tree[B] = {
      def stackRect(t: Tree[Either[A, B]]): Tree[B] = t match {
        case Leaf(Left(v)) => stackRect(f(v))
        case Leaf(Right(v)) => Leaf(v)
        case Branch(left, right) => Branch(stackRect(left), stackRect(right))
      }
      stackRect(f(a))
    }

    override def pure[A](x: A): Tree[A] =
      Leaf(x)
  }

  def main(args: Array[String]): Unit = {
    val testTree: Tree[Int] =
      Branch(Leaf(10), Leaf(20))
    val changedTree = TreeMonad.flatMap(testTree)(v => Branch(Leaf(v + 1), Leaf(v + 2)))
    println(changedTree)
  }
}
