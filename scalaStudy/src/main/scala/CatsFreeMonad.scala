import cats.free.Free
import cats.free.Free.liftF
// catsのFree Monadの練習をする

// 概要

// Free Monadを利用することで、実際の処理とプログラムの実行を切り離して考えることができる
// ZIOと一緒！！
// 最後に、executeする感じ！

// これの何が良いのか？？
// -> プログラムの途中で副作用を考えずにすむ。
// 純粋関数の集まりで処理を書くことができるのでテスたぶる

object CatsFreeMonad {
  sealed trait KVStoreA[A]
  case class Put[T](key: String, value: T) extends KVStoreA[Unit]
  case class Get[T](key: String) extends KVStoreA[Option[T]]
  case class Delete(key: String) extends KVStoreA[Unit]

  type KVStore[A] = Free[KVStoreA, A]

  def put[T](key: String, value: T): KVStore[Unit] =
  // liftFは、引数の値をFree[_, _]に引き上げるメソッド
    liftF[KVStoreA, Unit](Put[T](key, value))

  def get[T](key: String): KVStore[Option[T]] ={
    // liftF[List, Option[T]](List(None))
    /// val eee: Free[Option, String] = liftF[Option, String](Some(""))
    // ↑こんなんもできる
    // つまり、引数に取った型をFree[F[_], A]と言ったように、Free型の中に格納してあげている
    // こうすることで、FreeMonadが作成できる。
    // liftFの第一型パラメータは、一つの引数を受け取る高カインド型しか渡せない（Eitherも高カインド型だが、型パラメータを二つ必要とするので
    // だめ！！）
    liftF[KVStoreA, Option[T]](Get(key))
  }

  def delete(key: String): KVStore[Unit] =
    liftF[KVStoreA, Unit](Delete(key))


  // getとputの組み合わせでupdateを作成する
  // Monadなので、flatMapで合成できる
  def update[T](key: String, f: T => T): KVStore[Unit] = {
    for {
      maybeValue <- get[T](key)
      _ <- maybeValue.map((t: T) => put(key, f(t))).getOrElse(Free.pure())
    } yield ()
  }


  // Free Monadを定義することで、monadic flowで様々な処理を実装できる
  // だた、この例はデータのシーケンスを流れるように実装しただけ
  def program: KVStore[Option[Int]] =
    for {
      _ <- put("wild-cats", 2)
      _ <- update[Int]("wild-cats", x => x + 12)
      _  <- put("tema-cats", 3)
      n <- get[Int]("wild-cats")
     _ <- delete("tema-cats")
    } yield n


  // TODO: 続き！！！！！！
  // https://typelevel.org/cats/datatypes/freemonad.html#4-write-a-compiler-for-your-program


}
