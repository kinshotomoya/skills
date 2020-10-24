import scala.collection.convert.StreamExtensions.AccumulatorFactoryInfo

object Errorhandling {

  sealed trait Option[+A] extends Options[A]
  case class Some[A](get: A) extends Option[A]
  case object None extends Option[Nothing]


  trait Animal {
    val name: String
  }
  case class Dog(override val name: String) extends Animal
  case class Cat(override val name: String) extends Animal

  val dog: Option[Animal] = Some(Dog("test"))
  val cat: Option[Cat] = Some(Cat("test2"))

  def test(animal: Option[Animal]) = println(animal)

  test(dog)
  test(cat)


  // exercise 4.1
  trait Options[+A] {
    // thisで対象のOptionオブジェクトを取得する
    // ↑as: Option[A]を受け取らなくても良くなる
    def map[B](f: A => B): Option[B] = {
      this match {
        case None => None
        case Some(value) => Some(f(value))
      }
    }

    def getOrElse[B >: A](default: => B): B = {
      this match {
        case None => default
        case Some(value) => value
      }
    }

    def flatMap[B](f: A => Option[B]): Option[B] = {
      this match {
        case None => None
        case Some(value) => f(value)
      }
    }

    def flatMap2[B](f: A => Option[B]): Option[B] = {
      this.map(f).getOrElse(None)
    }

    def orElse[B >: A](ob: => Option[B]): Option[B] = {
      this match {
        case None => ob
        case s @ Some(_) => s
      }
    }

    def orElse2[B >: A](ob: => Option[B]): Option[B] = {
      this.flatMap(_ => ob)
    }

    def filter(f: A => Boolean): Option[A] = {
      this match {
        case None => None
        case s @ Some(value) if f(value) => s
      }
    }

    def filter2(f: A => Boolean): Option[A] = {
      this.flatMap(x => if(f(x)) Some(x) else None)
    }
  }

  def mean(xs: Seq[Double]): Option[Double] = {
    if(xs.isEmpty) None
    else Some(xs.sum / xs.length)
  }

  //exercise 4.2

  def variance(xs: Seq[Double]): Option[Double] = {
    mean(xs).flatMap(m => mean(xs.map(x => scala.math.pow(x -m, 2))))
  }

  def lift[A, B](f: A => B): Option[A] => Option[B] = _.map(f)

  // exercise 4.3
  def map2[A, B, C](a: Option[A], b: Option[B])(f: (A, B) => C): Option[C] = {
    a.flatMap(x => b.map(y => f(x, y)))
  }

  // exercise 4.4
  // List(Some(a), Some(b), Some(c)) => Some(List(a, b, c))
  def sequence[A](a: List[Option[A]]): Option[List[A]] = {
    @scala.annotation.tailrec
    def loop(acc: List[A], list: List[Option[A]]): Option[List[A]] = {
      list match {
        case Nil => Some(acc)
        case head :: tail => head match {
          case None => None
          case Some(value) => loop(acc.appended(value), tail)
        }
      }
    }
    loop(Nil, a)
  }

  def sequence2[A](a: List[Option[A]]): Option[List[A]] = {
    a.foldRight[Option[List[A]]](Some(Nil))((x: Option[A], y: Option[List[A]]) => {
      // xがNoneの場合には、次の初期値がNoneになるので、foldRightListの各値を何回計算してもNoneになる
      x.flatMap(z => y.map(w => z :: w))
    })
  }


  // exercise 4.5
  // リストを一回だけ操作するように実装する
  def traverse[A, B](a: List[A])(f: A => Option[B]): Option[List[B]] = {
    a.foldRight[Option[List[B]]](Some(Nil))((x: A, y: Option[List[B]]) => {
      f(x).flatMap(z => y.map(w => z :: w))
    })
  }

}


def Try[A](a: => A): Either[Exception, A] = {
  Left(new Exception)
//  try Right(a)
//  catch { case e: Exception => Left(e) }
}


def insuranceRateQuote(age: Int, numberOfSpeedingTickets: Int): Double = ???

def parseInsuranceRateQuote( age: String, numberOfSpeedingTickets: String): Either[Exception,Double] ={
  for {
    a <- Try { age.toInt }
    tickets <- Try { numberOfSpeedingTickets.toInt }
  } yield {
    insuranceRateQuote(a, tickets)
  }
}

