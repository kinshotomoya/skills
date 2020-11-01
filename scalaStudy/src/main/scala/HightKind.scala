object HighKind {

  // value
  val name = "name"


  // proper type
  // Intがproper type
  // valueよりも一段階抽象化されている
  val one: Int = 1


  // first-order type
  // Listでいうと、１つスロットがある
  // List[_]は、type constructorという
  // List[String], List[Int]のようになるという事
  val list: List[_] = List(1, 2)

  // second-order type
  // second-order typeを定義する事で、以下のように
  // mapメソッドを共通かできる。
  // F[_]は、List、Option, Array, Futureでも１つのスロットを持つ型ならなんでもok
  trait WithMap[F[_]] {
    def map[A, B](fa: F[A])(f: A => B): F[B]
    def flatMap[A, B](fa: F[A])(f: A => F[B]): F[B]
    def OrElse[A, B](fa: F[B]): F[B]
    def filter[A](fa: F[A])(f: A => Boolean): F[A]

  }


}
