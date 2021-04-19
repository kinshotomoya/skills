import Monoid.foldRight

import scala.concurrent.Future

object Monoid {

  trait Monoid[A] {
    def op(a1: A, a2: A): A
    def zero: A
  }

  // string monoidの場合
  def stringMonoid = new Monoid[String] {
    override def op(a1: String, a2: String): String = a1 + a2

    override def zero: String = ""
  }

  // list monoidの場合
  def listMonoid[A] = new Monoid[List[A]] {
    override def op(a1: List[A], a2: List[A]): List[A] = a1 ++ a2

    override def zero: List[A] = Nil
  }

  // exercise 10.1
  // int addの場合
  val intAddition: Monoid[Int] = new Monoid[Int] {
    override def op(a1: Int, a2: Int): Int = a1 + a2

    override def zero: Int = 0
  }

  // int multipleの場合
  val intMultiple: Monoid[Int] = new Monoid[Int] {
    override def op(a1: Int, a2: Int): Int = a1 * a2

    override def zero: Int = 1
  }

  // boolean orの場合
  val booleanOr: Monoid[Boolean] = new Monoid[Boolean] {
    override def op(a1: Boolean, a2: Boolean): Boolean = a1 || a2

    override def zero: Boolean = false
  }

  // boolean andの場合
  val booleanAnd: Monoid[Boolean] = new Monoid[Boolean] {
    override def op(a1: Boolean, a2: Boolean): Boolean = a1 && a2

    override def zero: Boolean = true
  }

  // exercise 10.2
  // optionを結合するためのMonoidインスタンスを考える
  // TODO
//  def optionMonoid[A]: Monoid[Option[A]] = new Monoid[Option[A]] {
//    override def op(a1: Option[A], a2: Option[A]): Option[A] = (a1 ++ a2).headOption
//
//    override def zero: Option[A] = None
//  }


  // 二つのOptionに関しては、二つのモノイドが存在するということ
  // firstOptionMonoid
  def optionMonoid[A]: Monoid[Option[A]] = new Monoid[Option[A]] {
    def op(x: Option[A], y: Option[A]) = x orElse y
    val zero = None
  }

  // We can get the dual of any monoid just by flipping the `op`.
  def dual[A](m: Monoid[A]): Monoid[A] = new Monoid[A] {
    def op(x: A, y: A): A = m.op(y, x)
    val zero = m.zero
  }

  // Now we can have both monoids on hand:
  def firstOptionMonoid[A]: Monoid[Option[A]] = optionMonoid[A]
  def lastOptionMonoid[A]: Monoid[Option[A]] = dual(firstOptionMonoid)



  // exercise 10.3
  // 引数の型と戻り値の型が同じ関数 = endo function
  def endoMonoid[A]: Monoid[A => A] = new Monoid[A => A] {
    override def op(a1: A => A, a2: A => A): A => A = {
      a1.compose(a2)
    }

    override def zero: A => A = (a: A) => a
  }

  // exercise 10.4
  def monoidLaws[A](m: Monoid[A], gen: Gen[A]): Prop


  // exercise 10.5
  // foldRightで実装
  def foldMap[A, B](as: List[A], m: Monoid[B])(f: A => B): B = {
    as.foldRight(m.zero)((x: A, y: B) => m.op(f(x), y))
  }

  // foldLeftで実装
  def foldMap2[A, B](as: List[A], m: Monoid[B])(f: A => B): B = {
    as.foldLeft(m.zero)((x: B, y: A) => m.op(f(y), x))
  }

  // exercise 10.6
  // foldMapを使って、foldLeftとfoldRightを作成する
  def foldRightViaFoldMap[A, B](z: B)(op: (A, B) => B): B = {
    foldMap()
  }



  def foldRight[A, B](as: List[A])(z: B)(f: (A, B) => B): B = {
    as.map(a => {
      val aaa: A => B => B = f.curried
      val rr: B => B = aaa(a)
      val www: B = rr(z)
      val zzzz: B => B = (f.curried)(z)
      val qqq = (aaa)(z)
    })
    val aaa: A => B => B = f.curried
    val bbb: B => B = (f.curried)(z)
    foldMap(as, endoMonoid[B])(aaa)(z)
  }

  // exercise 10.7
  def foldMapV[A,B](v: IndexedSeq[A], m: Monoid[B])(f: A => B): B = {
    if(v.isEmpty)
      m.zero
    else if(v.length == 1)
      f(v.head)
    else {
      val (one, two) = v.splitAt(v.length / 2)
      m.op(foldMapV(one, m)(f), foldMapV(two, m)(f))
    }
  }

  // exercise 10.8



  // exercise 10.9
  // IndexSeq[Int]が順序付けされているかを判断するメソッドを作成する
  // 良い感じのmonoidを作成する必要がある
  val indexSeq = IndexedSeq(1, 2, 3, 4, 5)

  def ordered(ints: IndexedSeq[Int]): Boolean = {
    def mon = new Monoid[Option[(Int, Int, Boolean)]] {
      override def op(a1: Option[(Int, Int, Boolean)], a2: Option[(Int, Int, Boolean)]): Option[(Int, Int, Boolean)] = {
        (a1, a2) match {
          case (Some((x1, y1, p)), Some((x2, y2, q))) => Some((x1.min(x2), y1.max(y2), p && q && y1 <= x2))
          case (x, None) => x
          case (None, x) => x
        }
      }

      override def zero: Option[(Int, Int, Boolean)] = None
    }

    foldMapV(ints, mon)((i: Int) => Some((i, i, true))).forall(_._3)

  }

  sealed trait WC
  case class Stub(chars: String) extends WC
  case class Part(lStub: String, words: Int, rStub: String) extends WC

  // exercise 10.10
  // monoid則を満たすようにする
  // 結合律・同一律
//  val wcMonoid: Monoid[WC] = new Monoid[WC] {
//    override def op(a1: WC, a2: WC): WC = {
//      (a1, a2) match {
//        case (Part(l1, words1, r1), Part(l2, words2, r2)) =>
//          val numOfWords = words1 + words2
//          if(l1 == " " && r2 != " ")
//            Part(l2, numOfWords + 1, r1)
//          else if(r1 == " " && l2 != " ")
//            Part(l1, numOfWords + 1, r2)
//          else
//            Part("", 0, "")
//      }
//    }
//
//    override def zero: WC = Part("", 0, "")
//  }


  val wcMonoid: Monoid[WC] = new Monoid[WC] {
    // The empty result, where we haven't seen any characters yet.
    val zero = Stub("")

    def op(a: WC, b: WC) = (a, b) match {
      case (Stub(c), Stub(d)) => Stub(c + d)
      case (Stub(c), Part(l, w, r)) => Part(c + l, w, r)
      case (Part(l, w, r), Stub(c)) => Part(l, w, r + c)
      case (Part(l1, w1, r1), Part(l2, w2, r2)) =>
        Part(l1, w1 + (if ((r1 + l2).isEmpty) 0 else 1) + w2, r2)
    }
  }


  // exercise 10.11
  def count(s: String): Int = {
    def wc(c: Char): WC =
      if (c.isWhitespace)
        Part("", 0, "")
      else
        Stub(c.toString)
    def unstub(s: String) = s.length min 1
    foldMapV(s.toIndexedSeq, wcMonoid)(wc) match {
      case Stub(s) => unstub(s)
      case Part(l, w, r) => unstub(l) + w + unstub(r)
    }
  }


  // exercise 10.12
  // F[_]のことを、型コンストラクタと言う
  // [_]のように、一つの型引数を受け取る記法になっている
  trait Foldable[F[_]] {
    def foldRight[A,B](as: F[A])(z: B)(f: (A,B) => B): B
    def foldLeft[A,B](as: F[A])(z: B)(f: (B,A) => B): B
    def foldMap[A,B](as: F[A])(f: A => B)(mb: Monoid[B]): B
    def concatenate[A](as: F[A])(m: Monoid[A]): A

    // exercise 10.15
    def toList[A](fa: F[A]): List[A] = {
      // どっちもいける？？
      foldMap(fa)(a => List(a))(listMonoid)

      foldRight(fa)(listMonoid.zero)(_ :: _)
    }
  }

  val foldableList = new Foldable[List] {
    override def foldRight[A, B](as: List[A])(z: B)(f: (A, B) => B): B = {
      as match {
        case head :: tail => foldRight(tail)(f(head, z))(f)
        case head :: Nil => f(head, z)
        case Nil => z
      }
    }

    override def foldLeft[A, B](as: List[A])(z: B)(f: (B, A) => B): B = {
      as match {
        case head :: tail => foldLeft(tail)(f(z, head))(f)
        case head :: Nil => f(z, head)
        case Nil => z
      }
    }

    override def foldMap[A, B](as: List[A])(f: A => B)(mb: Monoid[B]): B = {
      as.foldRight(mb.zero)((x, y) => mb.op(f(x), y))
    }

    override def concatenate[A](as: List[A])(m: Monoid[A]): A = {
      as.foldRight(m.zero)(m.op)
    }
  }

  // exercise 10.13
  sealed trait Tree[+A]
  case class Leaf[A](value: A) extends Tree[A]
  case class Branch[A](left: Tree[A], right: Tree[A]) extends Tree[A]


  new Foldable[Tree] {
    override def foldRight[A, B](as: Tree[A])(z: B)(f: (A, B) => B): B = {
      as match {
        case Leaf(v) => f(v, z)
        case Branch(t1, t2) => foldRight(t1)(foldRight(t2)(z)(f))(f)
      }
    }

    override def foldLeft[A, B](as: Tree[A])(z: B)(f: (B, A) => B): B = {
      as match {
        case Leaf(v) => f(z, v)
        case Branch(t1, t2) => foldLeft(t1)(foldLeft(t2)(z)(f))(f)
      }
    }

    override def foldMap[A, B](as: Tree[A])(f: A => B)(mb: Monoid[B]): B = {
      as match {
        case Leaf(v) => f(v)
        case Branch(t1, t2) => mb.op(foldMap(t1)(f)(mb), foldMap(t2)(f)(mb))
      }
    }

    override def concatenate[A](as: Tree[A])(m: Monoid[A]): A = {
      as match {
        case Leaf(v) => v
        case Branch(t1, t2) => m.op(concatenate(t1)(m), concatenate(t2)(m))
      }
    }
  }

  // exercise 10.14
  new Foldable[Option] {
    override def foldRight[A, B](as: Option[A])(z: B)(f: (A, B) => B): B = {
      as match {
        case None => z
        case Some(v) => f(v, z)
      }
    }

    override def foldLeft[A, B](as: Option[A])(z: B)(f: (B, A) => B): B = {
      as match {
        case None => z
        case Some(v) => f(z, v)
      }
    }

    override def foldMap[A, B](as: Option[A])(f: A => B)(mb: Monoid[B]): B = {
      as match {
        case None => mb.zero
        case Some(v) => f(v)
      }
    }

    override def concatenate[A](as: Option[A])(m: Monoid[A]): A = {
      as match {
        case None => m.zero
        case Some(v) => v
      }
    }
  }

  // exercise 10.15


  // exercise 10.16
  def productMonoid[A,B](A: Monoid[A], B: Monoid[B]): Monoid[(A,B)] = new Monoid[(A, B)] {
    override def op(a1: (A, B), a2: (A, B)): (A, B) = {
      (A.op(a1._1, a2._1), B.op(a1._2, a2._2))
    }

    override def zero: (A, B) = (A.zero, B.zero)
  }


  def mapMergeMonoid[K,V](V: Monoid[V]): Monoid[Map[K, V]] = new Monoid[Map[K, V]] {
    def zero = Map[K,V]()
    def op(a: Map[K, V], b: Map[K, V]) =
      (a.keySet ++ b.keySet).foldLeft(zero) { (acc,k) =>
        acc.updated(k, V.op(a.getOrElse(k, V.zero), b.getOrElse(k, V.zero)))
      } }


  // exercise 10.17
  def functionMonoid[A,B](B: Monoid[B]): Monoid[A => B] = {
    new Monoid[A => B] {
      override def op(a1: A => B, a2: A => B): A => B = {
        a => B.op(a1(a), a2(a))
      }

      override def zero: A => B = _ => B.zero
    }
  }

  // exercise 10.18
  def bag[A](as: IndexedSeq[A]): Map[A, Int] = {
    as.foldRight(Map[A, Int]())((x: A, y: Map[A, Int]) => mapMergeMonoid(intAddition).op(Map(x -> 1), y))
  }
}
