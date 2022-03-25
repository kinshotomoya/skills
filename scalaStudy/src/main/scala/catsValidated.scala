//import cats.data.{NonEmptyChain, Validated, ValidatedNec}
//
//object catsValidated {
//  // cats validatedのサンプル
//
//  final case class RegistrationData(userName: String, password: String, firstName: String, lastName: String, age: Int)
//
//  sealed trait DomainValidation {
//    def errorMessage: String
//  }
//
//  case object UsernameHasSpecialCharacters extends DomainValidation {
//    def errorMessage: String = "Username cannot contain special characters."
//  }
//
//  case object PasswordDoesNotMeetCriteria extends DomainValidation {
//    def errorMessage: String = "Password must be at least 10 characters long, including an uppercase and a lowercase letter, one number and one special character."
//  }
//
//  case object FirstNameHasSpecialCharacters extends DomainValidation {
//    def errorMessage: String = "First name cannot contain spaces, numbers or special characters."
//  }
//
//  case object LastNameHasSpecialCharacters extends DomainValidation {
//    def errorMessage: String = "Last name cannot contain spaces, numbers or special characters."
//  }
//
//  case object AgeIsInvalid extends DomainValidation {
//    def errorMessage: String = "You must be aged 18 and not older than 75 to use our services."
//  }
//
//  sealed trait FormValidator {
//    def validateUserName(userName: String): Either[DomainValidation, String] =
//      Either.cond(
//        userName.matches("^[a-zA-Z0-9]+$"),
//        userName,
//        UsernameHasSpecialCharacters
//      )
//
//    def validatePassword(password: String): Either[DomainValidation, String] =
//      Either.cond(
//        password.matches("(?=^.{10,}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$"),
//        password,
//        PasswordDoesNotMeetCriteria
//      )
//
//    def validateFirstName(firstName: String): Either[DomainValidation, String] =
//      Either.cond(
//        firstName.matches("^[a-zA-Z]+$"),
//        firstName,
//        FirstNameHasSpecialCharacters
//      )
//
//    def validateLastName(lastName: String): Either[DomainValidation, String] =
//      Either.cond(
//        lastName.matches("^[a-zA-Z]+$"),
//        lastName,
//        LastNameHasSpecialCharacters
//      )
//
//    def validateAge(age: Int): Either[DomainValidation, Int] =
//      Either.cond(
//        age >= 18 && age <= 75,
//        age,
//        AgeIsInvalid
//      )
//
//    def validateForm(username: String, password: String, firstName: String, lastName: String, age: Int): Either[DomainValidation, RegistrationData] = {
//      for {
//        // これだと、エラーの場合には１つ目のエラーしか返せない
//        validatedUserName <- validateUserName(username)
//        validatedPassword <- validatePassword(password)
//        validatedFirstName <- validateFirstName(firstName)
//        validatedLastName <- validateLastName(lastName)
//        validatedAge <- validateAge(age)
//      } yield RegistrationData(validatedUserName, validatedPassword, validatedFirstName, validatedLastName, validatedAge)
//    }
//  }
//
//  object FormValidator extends FormValidator
//  import cats.implicits._
//
//
//  def validateUserName(userName: String): Validated[DomainValidation, String] = FormValidator.validateUserName(userName).toValidated
//
//  def validatePassword(password: String): Validated[DomainValidation, String] = FormValidator.validatePassword(password).toValidated
//
//  def validateFirstName(firstName: String): Validated[DomainValidation, String] = FormValidator.validateFirstName(firstName).toValidated
//
//  def validateLastName(lastName: String): Validated[DomainValidation, String] = FormValidator.validateLastName(lastName).toValidated
//
//  def validateAge(age: Int): Validated[DomainValidation, Int] = FormValidator.validateAge(age).toValidated
//
//  // Vlidatedはモナドではないので、flatMap使われへん
//  // Validatedは、applicativeFunctorである
//  def validateForm(username: String, password: String, firstName: String, lastName: String, age: Int): Validated[DomainValidation, RegistrationData] = {
//    for {
//      validatedUserName <- validateUserName(username)
//      validatedPassword <- validatePassword(password)
//      validatedFirstName <- validateFirstName(firstName)
//      validatedLastName <- validateLastName(lastName)
//      validatedAge <- validateAge(age)
//    } yield RegistrationData(validatedUserName, validatedPassword, validatedFirstName, validatedLastName, validatedAge)
//  }
//
//
//  sealed trait FormValidatorNec {
//    // ValidatedNecは、left値として、nonEmptyChainを使用しているので、
//    // エラーの場合は、DomainValidationのchainを取得できる
//    type ValidationResult[A] = ValidatedNec[DomainValidation, A]
//
//    private def validateUserName(userName: String): ValidationResult[String] =
//    // userName.validNecで、ValidatedNec[Nothing, String]を返す
//    // つまり、ValidatedNecで値をラップしている
//    // invalidNecも同じ
//      if(userName.matches("^[a-zA-Z0-9]+$")) userName.validNec else UsernameHasSpecialCharacters.invalidNec
//
//    private def validatePassword(password: String): ValidationResult[String] =
//      if (password.matches("(?=^.{10,}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$")) password.validNec
//      else PasswordDoesNotMeetCriteria.invalidNec
//
//    private def validateFirstName(firstName: String): ValidationResult[String] =
//      if (firstName.matches("^[a-zA-Z]+$")) firstName.validNec else FirstNameHasSpecialCharacters.invalidNec
//
//    private def validateLastName(lastName: String): ValidationResult[String] =
//      if (lastName.matches("^[a-zA-Z]+$")) lastName.validNec else LastNameHasSpecialCharacters.invalidNec
//
//    private def validateAge(age: Int): ValidationResult[Int] =
//      if (age >= 18 && age <= 75) age.validNec else AgeIsInvalid.invalidNec
//
//    def validateForm(username: String, password: String, firstname: String, lastname: String, age: Int): ValidationResult[RegistrationData] =
//    {
//      // mapNでタプルの中身をRegistrationDataに詰めている
//      (validateUserName(username), validatePassword(password), validateFirstName(firstname), validateLastName(lastname), validateAge(age)).mapN(RegistrationData)
//    }
//
//
//  }
//
//  object FormValidatorNec extends FormValidatorNec
//
//  val result1: FormValidatorNec.ValidationResult[RegistrationData] = FormValidatorNec.validateForm(
//    username = "Joe",
//    password = "Passw0r$1234",
//    firstname = "John",
//    lastname = "Doe",
//    age = 21
//  )
//
//  case class Response(message: List[DomainValidation], data: Option[RegistrationData])
//  // まあ、こんな感じでNonEmptyChainでエラーケースが格納されている
//  result1.fold(
//    (e: NonEmptyChain[DomainValidation]) =>  Response(message = e.toList, data = None),
//    (data: RegistrationData) => Response(Nil, data.some)
//  )
//
//  // 続きは、ここから
//  // https://typelevel.org/cats/datatypes/validated.html#a-short-detour
//
//  NonEmptyChain.one[DomainValidation](UsernameHasSpecialCharacters) |+| NonEmptyChain[DomainValidation](FirstNameHasSpecialCharacters, LastNameHasSpecialCharacters)
//
//  FormValidatorNec.validateForm(
//    username = "Joe",
//    password = "Passw0r$1234",
//    firstname = "John",
//    lastname = "Doe",
//    age = 21
//  ).toEither
//
//  // つまり、データを蓄積したいならvalidatedを使う
//  // それ以外なら、Eitherを使う
//}
