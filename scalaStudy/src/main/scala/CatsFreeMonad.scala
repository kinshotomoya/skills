//import FreeMonadExample.Logo.{
//  Backward,
//  Forward,
//  Instruction,
//  RotateLeft,
//  RotateRight,
//  ShowPosition
//}
//import cats.arrow.FunctionK
//import cats.free.Free
//import cats.free.Free.liftF
//import cats.{Id, ~>}
//import scala.collection.mutable
//// catsのFree Monadの練習をする
//
//// 概要
//
//// Free Monadを利用することで、実際の処理とプログラムの実行を切り離して考えることができる
//// ZIOと一緒！！
//// 最後に、executeする感じ！
//
//// これの何が良いのか？？
//// -> プログラムの途中で副作用を考えずにすむ。
//// 純粋関数の集まりで処理を書くことができるのでテストがしやすくなる
//
//object CatsFreeMonad {
//  // DSLを定義する
//  sealed trait KVStoreA[A]
//  case class Put[T](key: String, value: T) extends KVStoreA[Unit]
//  case class Get[T](key: String) extends KVStoreA[Option[T]]
//  case class Delete(key: String) extends KVStoreA[Unit]
//
//  type KVStore[A] = Free[KVStoreA, A]
//
//  def put[T](key: String, value: T): KVStore[Unit] =
//    // liftFは、引数の値をFree[_, _]に引き上げるメソッド
//    liftF[KVStoreA, Unit](Put[T](key, value))
//
//  def get[T](key: String): KVStore[Option[T]] = {
//    // liftF[List, Option[T]](List(None))
//    /// val eee: Free[Option, String] = liftF[Option, String](Some(""))
//    // ↑こんなんもできる
//    // つまり、引数に取った型をFree[F[_], A]と言ったように、Free型の中に格納してあげている
//    // こうすることで、FreeMonadが作成できる。
//    // liftFの第一型パラメータは、一つの引数を受け取る高カインド型しか渡せない（Eitherも高カインド型だが、型パラメータを二つ必要とするので
//    // だめ！！）
//    liftF[KVStoreA, Option[T]](Get(key))
//  }
//
//  def delete(key: String): KVStore[Unit] =
//    liftF[KVStoreA, Unit](Delete(key))
//
//  // getとputの組み合わせでupdateを作成する
//  // Monadなので、flatMapで合成できる
//  def update[T](key: String, f: T => T): KVStore[Unit] = {
//    for {
//      maybeValue <- get[T](key)
//      _ <- maybeValue.map((t: T) => put(key, f(t))).getOrElse(Free.pure())
//    } yield ()
//  }
//
//  // Free Monadを定義することで、monadic flowで様々な処理を実装できる
//  // だた、この例はデータのシーケンスを流れるように実装しただけ
//  def program: KVStore[Option[Int]] =
//    for {
//      _ <- put("wild-cats", 2)
//      _ <- update[Int]("wild-cats", x => x + 12)
//      _ <- put("tema-cats", 3)
//      n <- get[Int]("wild-cats")
//      _ <- delete("tema-cats")
//    } yield n
//
//  // TODO: 続き！！！！！！
//  // https://typelevel.org/cats/datatypes/freemonad.html#4-write-a-compiler-for-your-program
//
//  // KVStoreA ~> Id = FunctionK[KVStoreA, Id]
//  // FunctionK[KVStoreA, Id]は、KVStoreAをIdに変換するFunctor
//
//  // このimpureCompilerメソッドは、副作用を含んでおり不純なメソッドである
//  // kvsを変更するし、printlnもしているので。
//  // 関数型プログラミングは、副作用を防ぐのではなくて、副作用を分離させることを目的にしている。
//
//  // 例えば、このメソッドでDBにアクセスする処理を書くなどする（副作用）
//
//  // インタープリターを作成している。（実際の実行基盤みたいなもの）
//  // 様々なインタープリターを作成することで、様々な処理を行うことができる。
//  def impureCompiler: KVStoreA ~> Id =
//    new (KVStoreA ~> Id) {
//      val kvs = mutable.Map.empty[String, Any]
//      override def apply[A](fa: KVStoreA[A]): Id[A] = {
//        fa match {
//          case Put(k: String, v: Any) =>
//            println(s"put($k, $v)")
//            kvs(k) = v
//            ()
//          case Get(k) =>
//            println(s"get($k")
//            kvs.get(k).map(_.asInstanceOf[A])
//          case Delete(k) =>
//            println(s"delete($k")
//            kvs.remove(k)
//            ()
//        }
//      }
//    }
//
//  // programは、実際に処理を実装している
//  // impureCompilerは、副作用のある実際の処理を実装している
//  // foldMapをすることで、プログラムの実行を行っている
//  program.foldMap(impureCompiler)
//
//}
//
//// 以下のサイトのサンプルを実装したもの
//// https://scalac.io/free-monad-cats-overview/
//
//// インタプリンタは、実装の詳細を気にする
//// インタプリンタは、ビジネスロジックから分離されている
//object FreeMonadExample {
//
//  trait Container[T]
//  case class StringContainer(value: String) extends Container[String]
//  // liftFメソッドで、引数に渡した高カインド型を、Free[M[_], A]に分解してくれる
//  val lifted = Free.liftF[Container, String](StringContainer("foo"))
//
//  object Logo {
//    // まず、命令を定義している
//    // この時点では、まだ命令の型だけを指定しているだけで、実際の詳細命令を定義しているわけではないので、まだ抽象的になっている（ATS）
//    sealed trait Instruction[A]
//    case class Forward(position: Position, length: Int)
//        extends Instruction[Position]
//    case class Backward(position: Position, length: Int)
//        extends Instruction[Position]
//    case class RotateLeft(position: Position, length: Degree)
//        extends Instruction[Position]
//    case class RotateRight(position: Position, degree: Degree)
//        extends Instruction[Position]
//    case class ShowPosition(position: Position) extends Instruction[Unit]
//  }
//
//  // こいつらは単なるドメインモデル
//  case class Position(x: Double, y: Double, heading: Degree)
//  case class Degree(private val d: Int) {
//    val value = d % 360
//  }
//
//  // 次に、DSLを作る
//  //　DSL　・・・　Domain Specific Language
//  // 特定のタスクに向けに設計された言語のこと
//  // これを定義することで、このアプリケーションにおいてコードを読みやすくする！
//  // Instruction[A]をFee[Instruction, A]に変換するメソッドを定義する
//  def forward(pos: Position, l: Int): Free[Instruction, Position] =
//    Free.liftF[Instruction, Position](Forward(pos, l))
//  def backward(pos: Position, l: Int): Free[Instruction, Position] =
//    Free.liftF[Instruction, Position](Backward(pos, l))
//  def left(pos: Position, degree: Degree): Free[Instruction, Position] =
//    Free.liftF[Instruction, Position](RotateLeft(pos, degree))
//  def right(pos: Position, degree: Degree): Free[Instruction, Position] =
//    Free.liftF[Instruction, Position](RotateRight(pos, degree))
//  def showPosition(pos: Position): Free[Instruction, Unit] =
//    Free.liftF[Instruction, Unit](ShowPosition(pos))
//
//  // 実際にロジックを書いていく
//  // 受け取ったポジションから (10, 10)進むプログラム
//
//  // Free Monadを返しているので、他の関数と合成もできる
//  // ここでは、実際の実装詳細は気にしていない
//  // ただロジックをビジネス書いているだけ
//  val program: Position => Free[Instruction, Position] = { start: Position =>
//    for {
//      p1 <- forward(start, 10)
//      p2 <- right(p1, Degree(90))
//      p3 <- forward(p2, 10)
//    } yield p3
//  }
//
//  // では、実際に上で実装したプログラムを実行していく！
//  // そこで必要なのが、NaturalTransformation。
//  // これは、型コンストラクタから別の型コンストラクタに変換できる関数のこと
//  // F[_] => G[_]
//  // Instruction ~> Id = FunctionK[Instruction, Id]
//  // TODO: なぜ、Idに変換しないといけないのか！！？？
//  object InterpreterId extends (Instruction ~> Id) {
//    import cats.implicits._
//    override def apply[A](fa: Instruction[A]): Id[A] = fa match {
//      case Forward(p, length) => forward(p, length)
//    }
//  }
//
//  program(Position(4.0, 3.0, Degree(9)))
//
//}
