import cats.data.ValidatedNel
import cats.implicits._
import eu.timepit.refined.api._
import eu.timepit.refined.auto._
import eu.timepit.refined.boolean.{And, Not}
import eu.timepit.refined.collection.{MaxSize, MinSize, NonEmpty}
import eu.timepit.refined.numeric.Positive
import eu.timepit.refined.refineV
import eu.timepit.refined.string.{MatchesRegex, StartsWith}
import io.estatico.newtype.macros.newtype


object Refined {

  case class UserId(value: String) extends AnyVal

  // スマートコンストラクタ
  object UserId {
    def apply(value: String): Option[UserId] = {
      Some(value).filter(isValidUserId).map(new UserId(_))
    }

    def isValidUserId(userId: String): Boolean = {
      userId.startsWith("@") && userId.length > 8
    }
  }

  val i1: Int Refined Positive = 5

  type UserIdRole = StartsWith["@"] And MinSize[3] And Not[MatchesRegex["(?i)@admin"]]

  val userId1: String Refined UserIdRole = "@sss"


  type Id = String Refined NonEmpty
  type Password = String Refined NonEmpty

  case class LogInUser(id: Id, password: Password)
  val id: Id = "id"
  val password: Password = "password"

  // コンパイルが通ってしまう。
  LogInUser(password, id)


  // これはできない
  // value classは、自前で定義した他のvalue classをラップできないから
//  case class Id2(value: Id) extends AnyVal
//  case class Password2(value: Password) extends AnyVal

  // case classならできるが、boxing unboxingのコストがある。-> つまり、オーバーヘッドがある
  // boxing -> 値型を参照型（オブジェクト）に変換する（ラップする）こと
  // unboxing -> オブジェクトを値型に変換すること
  case class Id3(value: Id)
  case class Password3(value: Password)
  case class LoginUser2(id: Id3, password3: Password3)

  // @newtypeが使える
  // 実行時のオーバーヘッドなしに値をラップできる
  @newtype case class Id4(value: Id)
  @newtype case class Password4(value: Password)

  case class LoginUser3(id: Id4, password: Password4)

  // val invalidId = Id4("")  コンパイル通らない
  val validId = Id4("id")
  val validPassword = Password4("password")

  LoginUser3(id = validId, password = validPassword)

}

object Example extends App {
  // 練習
  type NameRule = MaxSize[10] And NonEmpty
  type EmailRule = MatchesRegex["""[a-z0-9]+@[a-z0-9]+\.[a-z0-9]{2,}"""] And NonEmpty

  type NameString = String Refined NameRule
  type EmailString = String Refined EmailRule

  @newtype case class Name(value: NameString)
  object Name {
    // applyでバリデーション処理をかまして、Either型で返すようにすればいい
    // refineVでバリデーションかける
    def apply(value: String): Either[String, Name] = {
      refineV[NameRule](value).map((x: Refined[String, NameRule]) => Name(x))
    }
  }

  @newtype case class Email(value: EmailString)
  object Email {
    def apply(value: String): Either[String, Email] = {
      refineV[EmailRule](value).map((x: Refined[String, EmailRule]) => Email(x))
    }
  }

  case class UserInfo(name: Name, email: Email)

  def validateUserInfo(nameInput: String, emailInput: String): ValidatedNel[String, UserInfo] = {

    val a: ValidatedNel[String, Name] = Name(nameInput).toValidatedNel
    val b: ValidatedNel[String, Email] = Email(emailInput).toValidatedNel

    val c = a.map(x => b.map(y => UserInfo(x, y)))
    // flatMapは使えない
//    val result = for {
//      n <- Name(nameInput).toValidatedNel
//      e <- Email(emailInput).toValidatedNel
//    } yield UserInfo(n, e)

    // TODO: バリデーションメッセージがややこしいので、カスタムできないか？
    val result: ValidatedNel[String, UserInfo] = (a, b).mapN((name, email) => UserInfo(name, email))
    result
  }

  println(validateUserInfo("tomoya", "tmoya"))



}
