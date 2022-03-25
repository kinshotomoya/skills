package Futures

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

object Futures extends App {
  println(s"${Thread.currentThread().getName}-${Thread.currentThread().getId}")

  implicit val ec = ExecutionContext.global
  def a = Future {
    Thread.sleep(5000)
    println(s"aaaaa-${Thread.currentThread().getName}-${Thread.currentThread().getId}")
    "aaaaaaaa"
  }

  def b = Future {
//    Thread.sleep(5000)
    println(s"bbbbb-${Thread.currentThread().getName}-${Thread.currentThread().getId}")
    "bbbbbbb"
  }


  // onCompleteはfutureが完了した時の挙動を書いている
  a.onComplete {
    case Success(value) => println("vfvfvfvf")
    case Failure(exception) => println("cddvvddd")
  }

  println("www")

//  for {
//    a_string <- a
//    b_string <- b
//  } yield {
//    println(s"$a_string, $b_string")
//  }

}
