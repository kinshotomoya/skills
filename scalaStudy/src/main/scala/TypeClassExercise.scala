object TypeClassExercise {
  def executeTwice[A](num: A)(implicit calculator: Calculator[A]): A = {
    calculator.twice(num)
  }

  // executeTwiceのimplicitを糖衣構文で書いた
  def executeTwice2[A: Calculator](num: A): A = {
    implicitly[Calculator[A]].twice(num)
  }

  def executeUnwrapped[F[_], A](value: F[A])(implicit objectUnwrapper: ObjectUnwrapper[F]): A = {
    objectUnwrapper.unwrape(value)
  }
}


trait Calculator[A] {
  def twice(value: A): A
}

trait ObjectUnwrapper[F[_]] {
  def unwrape[A](x: F[A]): A
}


object Calculator {
  implicit def intCalculator = new Calculator[Int] {
    override def twice(value: Int): Int = {
      println("Int")
      value * value
    }
  }

  implicit def doubleCalculator = new Calculator[Double] {
    override def twice(value: Double): Double = {
      println("Double")
      value * value
    }
  }
}

object ObjectUnwrapper {
  implicit def listUnwrapper = new ObjectUnwrapper[List] {
    override def unwrape[A](list: List[A]): A = list.head
  }

  implicit def optionUnwrapper = new ObjectUnwrapper[Option] {
    override def unwrape[A](option: Option[A]): A = option.get
  }
}


object Run extends App {
  val calculateResult = TypeClassExercise.executeTwice(3.0)
  val objectUnwrappedListResult = TypeClassExercise.executeUnwrapped(List(1, 2))
  val objectUnwrappedOptionResult = TypeClassExercise.executeUnwrapped(Option(33))
  println(calculateResult)
  println(objectUnwrappedListResult)
  println(objectUnwrappedOptionResult)
}
