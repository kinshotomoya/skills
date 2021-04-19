import Test.State

object Monad {

  trait Functor[F[_]] {
    def map[A,B](fa: F[A])(f: A => B): F[B]
  }

  trait Mon[F[_]] {
    def map[A,B](fa: F[A])(f: A => B): F[B]
    def flatMap[A,B](fa: F[A])(f: A => F[B]): F[B]
    def map2[A,B,C](fa: F[A], fb: F[B])(f: (A, B) => C): F[C] =
      flatMap(fa)(a => map(fb)(b => f(a, b)))
  }

  // 全てのモナドはファンクターである
  // 型コンストラクタのことをファンクタと呼ぶ
  trait Monad[F[_]] extends Functor[F] {
    // Monのmap関数は、unitとflatMapから構成される
    // unitとflatMapを使えば、map, map2が手に入る
    def unit[A](a: => A): F[A]
    def flatMap[A,B](ma: F[A])(f: A => F[B]): F[B]

    def map[A,B](ma: F[A])(f: A => B): F[B] = flatMap(ma)(a => unit(f(a)))
    def map2[A,B,C](ma: F[A], mb: F[B])(f: (A, B) => C): F[C] = flatMap(ma)(a => map(mb)(b => f(a, b)))
    def sequence[A](lma: List[F[A]]): F[List[A]] = {

      lma.foldRight(unit(List[A]()))((ma: F[A], mla: F[List[A]]) => map2(ma, mla)(_ :: _))


      lma match {
        case List(a) => flatMap(a)(e => unit(List(e)))
        case Nil => unit(Nil)
      }
    }

    def traverse[A,B](la: List[A])(f: A => F[B]): F[List[B]] = {

      la.foldRight(unit(List[B]()))((a: A, mlb: F[List[B]]) => map2(f(a), mlb)((_: B) :: _))



      la match {
        case List(a) => map(f(a))(b => List(b))
        case Nil => unit(Nil)
      }
    }

    // exercise11.4
    def replicateM[A](n: Int, ma: F[A]): F[List[A]] = {
      if (n <= 0) unit(List[A]()) else map2(ma, replicateM(n - 1, ma))(_ :: _)
    }

    // exercise11.6
    def filterM[A](ms: List[A])(f: A => F[Boolean]): F[List[A]] =
      ms match {
        case Nil => unit(Nil)
        case h :: t => flatMap(f(h))((b: Boolean) =>
          if (!b) filterM(t)(f)
          else map(filterM(t)(f))(h :: _))
      }

    // exercise 11.7
    def compose[A,B,C](f: A => F[B], g: B => F[C]): A => F[C] = { a =>
      flatMap(f(a))(b => g(b))
    }

    // exercise 11.8

    def flatMap[A,B](ma: F[A])(f: A => F[B]): F[B] ={
      val ee: Unit = ()
      val aa: F[B] = compose((_:Unit) => ma, f)((): Unit)
      aa
    }


    def flatMapViaCompose[A,B](ma: F[A])(f: A => F[B]): F[B] = {
      ma
    }

    // exercise 11.9


    def compose[A,B,C](f: A => F[B], g: B => F[C]): A => F[C]

    def unit[A](a: => A): F[A]

  }

  // Genのモナドインスタンスを作ってみる

  // exercise11.1
  // Par 、Parser 、Option 、Stream 、Listはモナドなので、モナドインスタンスにできる
  // option
  val optionMonad = new Monad[Option] {
    override def unit[A](a: => A): Option[A] = Option(a)

    override def flatMap[A, B](ma: Option[A])(f: A => Option[B]): Option[B] = {
      ma flatMap f
    }
  }

  val listMonad = new Monad[List] {
    override def unit[A](a: => A): List[A] = List(a)

    override def flatMap[A, B](ma: List[A])(f: A => List[B]): List[B] = {
      ma flatMap f
    }
  }

  val streamMonad = new Monad[Stream] {
    override def unit[A](a: => A): Stream[A] = Stream(a)

    override def flatMap[A, B](ma: Stream[A])(f: A => Stream[B]): Stream[B] = {
      ma flatMap f
    }
  }

  // exercise 11.2
  // 分からん
  new Monad[State] {
    override def unit[A](a: => A): State[A] =

    override def flatMap[A, B](ma: State[A])(f: A => State[B]): State[B] = ???
  }


  // exercise 11.10


  // exercise 11.11
  // Listでやってみる


  // exercise 11.12


  // exercise 11.18



  // exercise 11.19
  // getState 、setState 、unit 、flatMap において相互に適用される法則としてどのようなものが考えられるか。
  // => 全ての戻り型がStateになる
  


}
