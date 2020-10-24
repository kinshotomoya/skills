import java.rmi.server.UID
import java.util.UUID

import scala.annotation.tailrec
import scala.util.Random

// 引数の部分的適応
def partial1[A, B, C](a: A, f: (A, B) => C): B => C = {
  b: B => f(a, b)
}

val rr: Int => Int = partial1(1, (x: Int, y: Int) => x + y)

// https://gist.github.com/takezoe/80b66f7b6e8fa0517139#%E5%BC%95%E6%95%B0%E3%81%AE%E9%83%A8%E5%88%86%E9%81%A9%E7%94%A8
// 部分的用の例1
// xの値を固定して、yの方を動的に変えれる
def test(x: Int, y: Int): Int = x + y
val ff: Int => Int = test(1, _: Int)
ff(3)

// 部分的用の例2
def area(pi: Double, r: Int): Double = pi * r * r
val f1: Int => Double = area(3.14, _)
f1(10)


object Exercise2s3 {
  def curry[A, B, C](f: (A, B) => C): A => (B => C) = {
    (a: A) => ((b: B) => f(a, b))
  }
}


object Exercise2s4 {
  def unCurry[A, B, C](f: A => (B => C)): (A, B) => C = {
    (a: A, b: B) => f(a)(b)
  }
}

object Exercise2s5 {
  def compose[A, B, C](f: B => C, g: A => B): A => C = {
    a: A => f(g(a))
  }
}

val f: Int => Double = (x: Int) => math.Pi / 2 - x


sealed trait List[+A]
case object Nil extends List[Nothing]
case class Cons[+A](head: A, tail: List[A]) extends List[A]

object List {

  def apply[A](as: A*): List[A] =
    if(as.isEmpty) Nil
    else Cons(as.head, apply(as.tail: _*))
  // listのtailメソッドを実装する
  def tail[T](list: List[T]): List[T] = list match {
    case Nil => sys.error("error occurred")
    case Cons(_, tail: List[T]) => tail
  }

  // 最初の要素を別の値と置き換える関数
  def setHead[T](value: T, list: List[T]) = {
    list match {
      case Nil => println("error occurred")
      case Cons(_, tail: List[T]) => Cons(value, tail)
    }
  }

  // listの先頭からn個の要素を削除する関数
  def drop[A](l: List[A], n: Int): List[A] = {
    @tailrec
    def loop(acc: List[A], n: Int): List[A] = {
      acc match {
        case Cons(_, _) if n==0 => acc
        case Cons(_, tail) => loop(tail, n-1)
        case Nil => Nil
      }
    }
    loop(l, n)
  }

  // 述語とマッチする場合に限り、Listからその要素までの要素を削除する関数
  def dropWhile[A](l: List[A], f: A => Boolean): List[A] = {
    @tailrec
    def loop(acc: List[A]): List[A] = {
      acc match {
        case Nil => Nil
        case Cons(head, tail) if f(head) => loop(tail)
        case _ => acc
      }
    }
    loop(l)
  }

  def init[A](l: List[A]): List[A] = {
    l match {
      case Cons(head, tail) => Cons(head, init(tail))
      case Cons(_, tail) =>tail
    }
  }

  def product(ds: List[Double]): Double = ds match {
    case Nil => 1.0
    case Cons(x, xs) => x * product(xs)
  }

  def foldRight[A, B](as: List[A], z: B)(f: (A, B) => B): B =
    as match {
      case Nil => z
      case Cons(x, xs) => f(x, foldRight(xs, z)(f))
    }
}

object Exercise3s7 {
  // Q: 0.0を検出した場合に、直ちに再帰を中止して0.0を返せるか？
  def product2(ns: List[Double]): Double =
    foldRight(ns, 1.0, 0.0)(_ * _)
  // A: 引数に0.0の場合の結果値（0.0）を渡してあげると、できる。

  // Q: 大きなリストでfoldRightを呼び出した場合の短絡の仕組み
  // A: 計算速度が、リストの大きさ（長さ）に比例するので、単純に計算速度が遅くなる

  def foldRight[A, B](as: List[A], z: B, zero: B)(f: (A, B) => B): B =
    as match {
      case Nil => z
      case Cons(x, _) if x == 0 => zero
      case Cons(x, xs) => f(x, foldRight(xs, z, zero)(f))
    }
}

object Exercise3s8 {
  def foldRight[A, B](as: List[A], z: B)(f: (A, B) => B): B =
    as match {
      case Nil => z
      case Cons(x, xs) => f(x, foldRight(xs, z)(f))
        // f(x, foldRight(xs, z)(f))
        // f(x, f(x, foldRight(xs, z)(f)))
    }

  foldRight(List(1, 2, 3), Nil: List[Int])(Cons(_: Int, _))
  // 1. Cons(1, Cons(2, Cons(3, Nil)))

  def length[A](as: List[A]): Int = {
    foldRight(as, 0)((_, y) => y + 1)
  }
}

object Exercise3s9 {
  def length[A](as: List[A]): Int = {

    @tailrec
    def loop(acc: Int, list: List[A]): Int = {
      list match {
        case Nil => 0
        case Cons(_, Nil) => acc + 1
        case Cons(_, tail) => loop(acc + 1, tail)
      }
    }
    loop(0, as)
  }
}

// List(1, 2, 3), 0
object Exercise3s10 {

  def foldLeft[A, B](as: List[A], z: B)(f: (B, A) => B): B = {
    as match {
      case Nil => z
      case Cons(head, tail) => foldLeft(tail, f(z, head))(f)
        // List(1, 2, 3), 0
        // foldLeft(List(2, 3), f(1, 0))
        // foldLeft(List(3), f(2, 1))
        // foldLeft(Nil, f(3, 3))
        // 6
    }
  }

  def sum(as: List[Int]) = foldLeft(as, 0)((x, y) => x + y)

  def product(as: List[Int]) = foldLeft(as, 1)(_*_)

  def length[A](as: List[A]) = foldLeft(as, 0)((x, _) => x + 1)

  // List(1, 2, 3) => List(3, 2, 1)
  // Cons(3, Cons(2, Cons(1, NIl)))
  def reverse[A](as: List[A]): List[A] = foldLeft(as, List(): List[A])((list: List[A], head: A) => Cons(head, list))
  // as = List(1, 2, 3)
  // foldLeft(List(1, 2, 3), List())((List(), 1) => Cons(1, List()))
  // foldLeft(List(2, 3), Cons(1, List()))((Cons(1, List()), 2) => Cons(2, Cons(1, List())))
  // foldLeft(List(3), Cons(2, Cons(1, List())))((Cons(2, Cons(1, List())), 3) => Cons(3, Cons(2, Cons(1, List()))))
  // foldLeft(List(), Cons(3, Cons(2, Cons(1, List()))))((Cons(2, Cons(1, List())), 3) => Cons(3, Cons(2, Cons(1, List()))))
  // => Cons(3, Cons(2, Cons(1, List())))

  // foldLeftを使ってfoldRightを実装する
  // 一回Listを逆転させてから、左から畳みめば、結果的にfoldLeftと同じ挙動になる
  def foldRightTailrec[A, B](as: List[A], z: B)(f: (A, B) => B): B = {
    foldLeft(foldLeft(as, Nil: List[A])((list, head) => Cons(head, list)) ,z)(f(_: B, _: A))
  }

  // foldRightを使って、foldLeftを実装する
  // List(1, 2, 3) => List(3, 2, 1)
  // foldRightでreverseを実装する
  // reverseListを右からたたみ込んで行く
  def foldLeftWithFoldRight[A, B](as: List[A], z: B)(f: (B, A) => B): B = {
    // Cons(3, Cons(2, Cons(1. Nil)))が欲しい
    // foldRightでは、reverseできない！！？？？
    foldRight(as, Nil: List[A])((x: A, y: List[A]) => Cons(x: A, y: List[A]))
    z
  }


  // exercise3.14
  // List(1, 2, 3)
  // List(4, 5, 6)
  // => List(1, 2, 3, 4, 5, 6)
  // 初期値をマージ先のa2 Listにしておく
  // a1 Listの先頭から順次a2にまーじされていくので、a2の順番を逆にする必要がある
  def foldLeftAppend[A](a1: List[A], a2: List[A]): List[A] = {
    foldLeft(reverse(a1), a2)((a2List, head) => Cons(head, a2List))
  }

  // exercise3.14
  // List(1, 2, 3)
  // List(4, 5, 6)
  // => List(1, 2, 3, 4, 5, 6)
  // 右からたたみ込んで行くので、3, 2, 1の順番でa2にマージされていく。
  def foldRightAppend[A](a1: List[A], a2: List[A]): List[A] = {
    foldRight(a1, a2)((head: A, tail: List[A]) => Cons(head, tail))
    // Cons(1, foldRight(List(2, 3), List(4, 5, 6)))
    // Cons(1, Cons(2, foldRight(List(3), List(4, 5, 6))))
    // Cons(1, Cons(2, Cons(3, List(4, 5, 6))))
  }

  // exercise3.15
  // 複数のリストからなるリストを１つのリストにまとめる関数
  // List(List(1, 2), List(4, 7), List(5, 9)) => List(1, 2, 4, 7, 5, 9)
  def combineListV1[A](as: List[List[A]]): List[A] = {
    as match {
      case Nil => Nil
      case Cons(head: List[A], tail: List[List[A]]) => {
        foldLeft(head, combineListV1(tail): List[A])((x: List[A], y: A) => foldLeftAppend(List(y), x))
      }
    }
  }

  def foldRight[A, B](as: List[A], z: B)(f: (A, B) => B): B =
    as match {
      case Nil => z
      case Cons(x, xs) => f(x, foldRight(xs, z)(f))
        // f(x, foldRight(xs, z)(f))
        // f(x, f(x, foldRight(xs, z)(f)))

        // combineListV2が呼ばれた際に、以下のような順番で処理される
        // f (List(1, 2, 3), foldRight(List(List(4, 5, 6), List(7, 8, 9))))
        // f(List(1, 2, 3), f(List(4, 5, 6), foldRight(List(7, 8, 9), Nil)))
        // f(List(1, 2, 3), f(List(4, 5, 6), f(List(7, 8, 9), foldRight(Nil, Nil))))
        // f(List(1, 2, 3), f(List(4, 5, 6), f(List(7, 8, 9), Nil)))
        // こっから、内側のfunctionから順番に処理される
        // f: (x: List[A], y: List[A]) => foldRight(x, y)((head, tail) => Cons(head, tail))
        // f(List(1, 2, 3), f(List(4, 5, 6), Cons(7, foldRight(List(8, 9), Nil))))
        // f(List(1, 2, 3), f(List(4, 5, 6), Cons(7, Cons(8, foldRight(List(9), Nil)))))
        // f(List(1, 2, 3), f(List(4, 5, 6), Cons(7, Cons(8, Cons(9, Nil)))))
        // f(List(1, 2, 3), Cons(4, foldRight(List(5, 6), Cons(7, Cons(8, Cons(9, Nil))))))
        // f(List(1, 2, 3), Cons(4, Cons(5, foldRight(List(6), Cons(7, Cons(8, Cons(9, Nil)))))))
        // f(List(1, 2, 3), Cons(4, Cons(5, Cons(6, foldRight(Nil, Cons(7, Cons(8, Cons(9, Nil))))))))
        // f(List(1, 2, 3), Cons(4, Cons(5, Cons(6, Cons(7, Cons(8, Cons(9, Nil)))))))
        // Cons(1, foldRight(List(2, 3), Cons(4, Cons(5, Cons(6, Cons(7, Cons(8, Cons(9, Nil))))))))
        // Cons(1, Cons(2, foldRight(List(3, Cons(4, Cons(5, Cons(6, Cons(7, Cons(8, Cons(9, Nil))))))))))
        // Cons(1, Cons(2, Cons(3, foldRight(Nil, Cons(4, Cons(5, Cons(6, Cons(7, Cons(8, Cons(9, Nil))))))))))
        // Cons(1, Cons(2, Cons(3, Cons(4, Cons(5, Cons(6, Cons(7, Cons(8, Cons(9, Nil)))))))))
    }

  // ex) List(List(1, 2, 3), List(4, 5, 6), List(7, 8, 9)) => List(1, 2, 3, 4, 5, 6, 7, 8, 9)
  def combineListV2[A](as: List[List[A]]): List[A] = {
    foldRight(as, Nil: List[A])((x: List[A], y: List[A]) => foldRightAppend(x, y))
    // foldRight(as, Nil: List[A])((x: List[A], y: List[A]) => foldRight(x, y)((head: A, tail: List[A]) => Cons(head, tail)))
    // foldRightAppend()
  }

  // exercise3.16
  // List(1, 2, 3) => List(2, 3, 4)
  def addMap(as: List[Int]): List[Int] = {
    as match {
      case Nil => Nil
      case Cons(head, tail) => Cons(head + 1, addMap(tail))
    }
  }

  // exercise3.16
  // List(1, 2, 3) => List(2, 3, 4)
  // foldRightを使う
  def addMapViaFoldRight(as: List[Int]): List[Int] = {
    foldRight(as, Nil: List[Int])((head: Int, tail: List[Int]) => Cons(head + 1, tail))
  }

  // exercise3.17
  def convertDoubleToString(as: List[Double]): List[String] = {
    as match {
      case Nil => Nil
      case Cons(head, tail) => Cons(head.toString, convertDoubleToString(tail))
    }
  }

  // exercise3.17
  // foldRightを使う
  def convertDoubleToStringViaFoldRight(as: List[Double]): List[String] = {
    foldRight(as, Nil: List[String])((head: Double, tail: List[String]) => Cons(head.toString, tail))
  }

  // exercise3.18
  def map[A, B](as: List[A])(f: A => B): List[B] = {
    as match {
      case Nil => Nil
      case Cons(head, tail) => Cons(f(head), map(tail)(f))
    }
  }


  // exercise3.18
  // foldRightを使う
  def mapViaFoldRight[A, B](as: List[A])(f: A => B): List[B] = {
    foldRight(as, Nil: List[B])((head: A, tail: List[B]) => Cons(f(head), tail))
  }

  // exercise3.19
  // List(1, 2, 3, 4, 5) => List(2, 4)
  def filter[A](as: List[A])(f: A => Boolean): List[A] = {
    as match {
      case Nil => Nil
      case Cons(head, Cons(head2, tail)) if f(head) => Cons(head2, filter(tail)(f))
      case Cons(head, tail) => Cons(head, filter(tail)(f))
    }
  }


  // exercise3.19
  // foldRightを使う
  // List(1, 2, 3, 4) => List(2, 4)
   def filterViaFoldRight[A](as: List[A])(f: A => Boolean): List[A] = {
    foldRight(as, Nil: List[A])((x: A, y: List[A]) => if(f(x)) y else Cons(x, y))
  }


  def foldRight[A, B](as: List[A], z: B)(f: (A, B) => B): B =
    as match {
      case Nil => z
      case Cons(x, xs) => f(x, foldRight(xs, z)(f))
    }
  // f(1, foldRight(List(2, 3, 4), Nil))
  // f(1, f(2, foldRight(List(3, 4), Nil)))
  // f(1, f(2, f(3, foldRight(List(4), Nil)))
  // f(1, f(2, f(3, f(4, foldRight(Nil, Nil)))))
  // f(1, f(2, f(3, f(4, Nil))))
  // f(1, f(2, f(3, Cons(4, Nil))))
  // f(1, f(2, Cons(4, Nil)))
  // f(1, Cons(2, Cons(4, Nil)))
  // Cons(2, Cons(4, Nil))

//  def flatMap[A, B](as: List[A])(f: A => List[B]): List[B] = {
//
//  }

}
