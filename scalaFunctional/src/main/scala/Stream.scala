

object StreamProblem {

  // if2関数は、非正格関数と言う
  // onTrue, onFalseは遅延評価されるので、onTrue(), onFalse()とするまで評価されない
  def if2[A](cond: Boolean, onTrue: () => A, onFalse: () => A): A = {
    if(cond) onTrue() else onFalse()
  }


  // if2は以下のように書き換えることができる
  def if3[A](cond: Boolean, onTrue: => A, onFalse: => A): A = {
    if(cond) onTrue else onFalse
  }


  if2(true, () => 4, () => 3)
  if3(true, 4, 3)


  // 引数の遅延評価をキャッシュしないので、呼ばれるたびに式を評価する
  // なので、iは呼ばれる毎に評価される
  def maybeTwice(b: Boolean, i: => Int): Int = if(b) i + i else 0

  maybeTwice(true, {println("hello"); 1+2})
  // => hello, hello, 6

  // lazyをつけるとlazy val宣言の右辺が参照されるまで遅延され、右辺が参照されるとキャッシュする
  def maybeTwice2(b: Boolean, i: => Int): Int = {
    lazy val j = i
    if(b) j + j else 0
  }

}


trait Stream[+A] {

  def cons[A](hd: => A, tl: => Stream[A]): Stream[A] = {
    lazy val head = hd
    lazy val tail = tl
    Cons(() => head, () => tail)
  }

  def empty[A]: Stream[A] = Empty

  def apply[A](as: A*): Stream[A] = {
    if(as.isEmpty) empty else cons(as.head, apply(as.tail: _*))
  }

  def headOption: Option[A] = this match {
    case Empty => None
    case Cons(h, _) => Some(h())
  }

  def toList: List[A] = {
//    this.foldRight(Nil: List[A])((x: A, y: List[A]) => y.appended(x))
    this match {
      case Cons(h, t) => h() :: t().toList
      case _ => Nil
    }
  }


  def take(n: Int): Stream[A] = {
    this match {
      case Cons(h, t) if n > 1 => cons(h(), t().take(n - 1))
      case Cons(h, _) if n== 1 => cons(h(), empty)
      case _ => empty
    }
  }

  // exercise5.5
  def takeWhile(p: A => Boolean): Stream[A] = {
    this match {
      case Cons(h, t) => if(p(h())) cons(h(), t().takeWhile(p)) else t().takeWhile(p)
      case _ => Empty
    }
  }

  // exercise5.4
  // 指定された述語とマッチするものを全てチェックする
  // マッチされない値が検出されるとチェックを終了させる
  def forAll(p: A => Boolean): Boolean = {
    this match {
      case Cons(h, t) if p(h()) => t().forAll(p)
      case Cons(h, _) if !p(h()) => false
      case Empty => true
    }
  }

  def foldRight[B](z: => B)(f: (A, =>B) => B): B = {
    this match {
      case Cons(h, t) => f(h(), t().foldRight(z)(f))
      case _ => z
    }
  }

  def exists(p: A => Boolean): Boolean = {
    foldRight(false)((a: A, b: Boolean) => p(a) || b)
  }

  // bは、foldRightの中身で言うとt().foldRight(z)(f)
  def forAllFoldRight(p: A => Boolean): Boolean = {
    foldRight(false)((a: A, b: Boolean) => if(p(a)) b else false)
  }

  // exercise5.5
  def takeWhileViaFoldRight(p: A => Boolean): Stream[A] = {
    foldRight(Empty: Stream[A])((x, y) => if(p(x)) cons(x, y) else y)
  }

  // exercise5.6
  def headOptionViaFoldRight: Option[A] = {
    foldRight(None: Option[A])((x, _) => Option(x))
  }

  // exercise5.7
  def map[B](f: A => B): Stream[B] = {
    foldRight(Empty: Stream[B])((a, b) => cons(f(a), b))
  }

  def filter(f: A => Boolean): Stream[A] = {
    foldRight(Empty: Stream[A])((a, b) => if(f(a)) cons(a, b) else b)
  }

  def append[A](h: => A): Stream[A] = {

  }


}
case object Empty extends Stream[Nothing] {
  override def equals(that: Any): Boolean = ???
}
case class Cons[+A](h: () => A, t: () => Stream[A]) extends Stream[A] {
  override def equals(that: Any): Boolean = ???
}

object Stream {
  // スマートコンストラクタ
  // hd, tl共に最初に評価されたときの値をキャッシュする
  def cons[A](hd: => A, tl: => Stream[A]): Stream[A] = {
    lazy val head = hd
    lazy val tail = tl
    Cons(() => head, () => tail)
  }

  def empty[A]: Stream[A] = Empty

  def apply[A](as: A*): Stream[A] = {
    if(as.isEmpty) empty else cons(as.head, apply(as.tail: _*))
  }

}
