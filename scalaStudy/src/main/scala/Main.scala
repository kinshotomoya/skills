object Main extends App {

  // AnyValのサブクラスにすると、実行時のオブジェクトの割り当てを回避できる
  // つまり、以下の場合はコンパイル時にはUserIdという型だが、実行時にはString型になる
  class UserId(val value: String) extends AnyVal


  // value classはただひつつのパラメータしか持てない
  // case class UserIds(value: String, value2: String) extends AnyVal


  // value classはdefだけ定義できる
  class UserId2(val value: String) extends AnyVal {
    def userId2 = new UserId2("userId2")
    def printValue = println(value)
  }

  // value classは汎用トレイトのみを拡張することができる
  trait Printable extends Any{
    def print(): Unit
  }

  class UserId3(val value: String) extends AnyVal with Printable {
    override def print(): Unit = println(value)
}

  // インスタンス化する必要がある
  val userId3 = new UserId3("userId3")
  userId3.print()

  // 他のclassなどは継承できない
  class User(val name: String)

  //　まあtrait以外はmixinできないから
  // class UserId4(val value: String) extends AnyVal with User


  //汎用トレイトによっては、value classにメモリ割り当てをするようになることもある


  // value classの用例
  // 拡張メソッド
  // implicitと組み合わせることで メモリ割り当てのない拡張メソッドを作成できる
  // RichIntのメソッド（toHexString）を使うために、RichIntのインスタンスを作る必要がない
  // ので、実行時のオーバーヘッドを無くす事ができる
  implicit class RichInt(val self: Int) extends AnyVal {
    def toHexString: String = java.lang.Integer.toHexString(self)
  }

  // 3.toHexString

  // 正当性
  // 実行時のオーバーヘッドを無くしながら、型安全を得ることがある。

  case class Meter(value: Double) extends AnyVal {
    def +(m: Meter): Meter = Meter(value + m.value)
  }

  // Meterはvalue classなので、実行時にはMeterオブジェクトは消えて、メモリ割り当てはしなくて良い
  val x = Meter(3.0)
  val y = Meter(4.0)
  val z = x + y


  // メモリ割り当てが必要になる場合
  // （１）他の型として扱われる場合

  trait Distance extends Any

  case class Meter2(value: Double) extends AnyVal with Distance {
  }

  // 親型のDistanceを受け取るようになっている
  def add(x: Distance, y: Distance): Distance = ???
  // この時は、Meter2はインスタンス化される
  add(Meter2(3.0), Meter2(4.0))

  // 以下のようなシグネチャならば、インスタンス化の必要はない
  def add2(x: Meter2, y: Meter2): Meter2 = ???

  // 型パラメータの場合もインスタンス化する必要がある
  def add3[A](x: A, y: A): A = ???

  add3(Meter2(3.0), Meter2(4.0))


  //（２） 配列の中に組み込まれる場合
  // 配列の内部の型がMeterだとしても、この場合内部表現のDoubleを格納するのではなく、
  // Meter実体を格納することになる
  val m = Meter(3.0)
  val array = Array[Meter](m)


  // （３）パターンマッチで型でマッチさせる場合
  // インスタンスの実体が必要になる
  val m1 = Meter(3.0)
  m1 match {
    case Meter(3.0) => println("3.0です")
    case Meter(_) => println("3.0以外です")
  }

  // その他の制約
  // 名前渡しパラメータを使うことはできない
  // value classはvalしか持つことはできないから

  // class Name(val value: => String) extends AnyVal

  // 複数のコンストラクタを持つことができない
//  class Secondary(val name: String) extends AnyVal {
//    def this(x: String) = this(x)
//  }


  // applyメソッドをオーバーライドして、Test値クラスを作成しても、
  // 実行時にはインスタンス化されない
  case class Test1(name: String)

  object Test1 {
     def apply(value: String): Test1 = Test1(value)
  }


}
