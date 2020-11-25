

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

  // BがAのスーパークラス
  def append[B >: A](s: => Stream[B]): Stream[B] = {
    foldRight(s)((x, y) => cons(x, y))
  }

  def flatMap[A, B](f: A => Stream[B]): Stream[B] = {
    foldRight(Empty: Stream[B])((a, b) => f(a) match {
      case Cons(h, _) => cons(h(), b)
      case Empty => empty
    })
  }



  def constant[A](a: A): Stream[A] = cons(a, constant(a))

  // より効率的にリファクタリング
  def constantR[A](a: A): Stream[A] = {
    lazy val tail: Stream[A] = Cons(() => a, () => tail)
    tail
  }

  def from(n: Int): Stream[Int] = cons(n, from(n + 1))

  def fibs(x: Int, y: Int): Stream[Int] = cons(x, fibs(y, x + y))

  def unfold[A, S](z: S)(f: S => Option[(A, S)]): Stream[A] = {
    f(z) match {
      case Some((a, s)) => cons(a, unfold(s)(f))
      case None => empty
    }
  }


  def fibsViaUnfold: Stream[Int] = unfold((0, 1)){case (f1, f2) => Some(f1, (f2, f1 + f2))}

  def fromViaUnfold(n: Int): Stream[Int] = unfold(n)(n => Some((n, n+1)))

  def constantViaUnfold[A](a: A): Stream[A] = unfold(a)(_ => Some(a, a))

  def oneViaUnfold: Stream[Int] = unfold(1)(_ => Some((1, 1)))

  def mapViaUnfold[A, B](f: A => B): Stream[B] = {
    unfold(this){
      case Cons(h, t) => Some((f(h()), t()))
      case _ => None
    }
  }

  def takeViaUnfold(n: Int): Stream[A] = unfold(this){
    case Cons(h, t) => Some((h(), t()))
    case _ => None
  }

  def takeWhileViaUnfold(f: A => Boolean): Stream[A] =
    unfold(this) {
      case Cons(h, t) if f(h()) => Some((h(), t()))
      case _ => None
    }

  def zipWithAll[B, C](s2: Stream[B])(f: (Option[A], Option[B]) => C): Stream[C] =
    unfold((this, s2)) {
      case (Empty, Empty) => None
      case (Cons(h, t), Empty) => Some(f(Some(h()), Option.empty[B]).->((t(), empty[B])))
      case (Empty, Cons(h, t)) => Some(f(Option.empty[A], Some(h())) -> (empty[A] -> t()))
      case (Cons(h1, t1), Cons(h2, t2)) => Some(f(Some(h1()), Some(h2())) -> (t1() -> t2()))
    }


  // Stream(1, 2, 3)  Stream(1, 2) => true
  def startsWith[A](s: Stream[A]): Boolean = {
    unfold((this, s)){
      case (Cons(h1, t1), Cons(h2, t2)) if h1 == h2 => Some((h1(), (t1(), t2())))
      case _ => None
  } match {
      case Empty => false
      case _ => true
    }
  }


  def tails: Stream[Stream[A]] =
    unfold(this){
      case Empty => None
      case s @ Cons(h, t) => Some((s, t()))
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






