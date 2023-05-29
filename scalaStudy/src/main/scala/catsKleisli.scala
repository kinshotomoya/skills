//import cats.data.Kleisli
//import org.checkerframework.checker.units.qual.s
//import shapeless.PolyDefns.->
//
//object catsKleisli {
//  // OptionやEitherなどのようなMonadを合成できる
//
//  // 以下のように、andThenで関数同士を合成できる
//
//  val twice: Int => Int = x => x * 2
//
//  val countCats: Int => String =
//    x => if(x == 1) "1 cat" else s"$x cats"
//
//  val twiceAsManyCats: Int => String = twice andThen countCats
//
//
//  // 時には、モナドを返す関数もある
//  // この場合には、parse andThen reciprocalはできない
//  // parseはOption[Int]を返し、reciprocalはIntを受け取るので
//  val parse: String => Option[Int] = s => if(s.matches("-?[0-9]+")) Some(s.toInt) else None
//
//  val reciprocal: Int => Option[Double] = i => if(i != 0) Some(1.0 / i) else None
//
//  for {
//    a <- parse("ss")
//    b <- reciprocal(a)
//  } yield b
//
//  // こんな時に、Kleisliが使える
//
//  // Kleisli[F[_], A, B]は、単なるA => F[B]のラッパー
//
//  import cats.implicits._
//
//  def parseViaKleisli: Kleisli[Option, String, Int] =
//    Kleisli((s: String) => if(s.matches("-?[0-9]+")) Some(s.toInt) else None)
//
//  def reciprocalViaKleisli: Kleisli[Option, Int, Double] =
//    Kleisli((i: Int) => if(i != 0) Some(1.0 / i) else None)
//
//
//  val composedMethod: Kleisli[Option, String, Double] = parseViaKleisli.andThen(reciprocalViaKleisli)
//
//
//
//
//  // Kleisli[F[_], A, B]のF[_]は、Monadである必要はない。
//  // ただ、Functorであれば良い（つまり、mapメソッドがあればMonadである必要はない）
//
//  //　以下は、F[_]がどのタイプかで使えるメソッドをまとめたもの
//  // 今回の例で使ったF[_] = OptionはMonadなので、以下の表のどのメソッドも使える
//
////  Method    | Constraint on `F[_]`
////  --------- | -------------------
////  andThen   | FlatMap
////  compose   | FlatMap
////  flatMap   | FlatMap
////  lower     | Monad
////  map       | Functor
////  traverse  | Applicative
//
//
//  reciprocalViaKleisli
//
//
//  // Kleisliは、
//
//
//  type Id[A] = A
//
//  type Reader[A, B] = Kleisli[Id, A, B]
//  object Reader {
//    def apply[A, B](f: A => B): Reader[A, B] = Kleisli[Id, A, B](f)
//  }
//
//  type ReaderT[F[_], A, B] = Kleisli[F, A, B]
//  val ReaderT: Kleisli.type = Kleisli
//
//}
//
//
//// まとめ！！！！！
//
//// 正直、Kleisliの使い所がわからない
//// 関数を合成したいが、モナドの戻り値があるので合成できない時にのみ使う？
//// scalaでは関数をオブジェクトとして持てるので
//// 関数を合成することで、変数で持ちまわったり、メソッドの引数に渡したり、メソッドの戻り値として返却したりなどできるようになる
//
//import cats.implicits._
//
//def parseViaKleisli: Kleisli[Option, String, Int] =
//  Kleisli((s: String) => if(s.matches("-?[0-9]+")) Some(s.toInt) else None)
//
//def reciprocalViaKleisli: Kleisli[Option, Int, Double] =
//  Kleisli((i: Int) => if(i != 0) Some(1.0 / i) else None)
//
//
//val composedMethod: Kleisli[Option, String, Double] = parseViaKleisli.andThen(reciprocalViaKleisli)
//
//def calucurate(f: Kleisli[Option, String, Double], str: String): Double = {
//  f(str).getOrElse(1.0)
//}