object ScalaTest {
  trait Prop {
    def check: Boolean
    def &&(p: Prop): Prop = new Prop {
      override def check: Boolean = this.check && p.check
    }
  }

  case class Gen[A](sample: State[RNG, A]) {
    def flatMap[B](f: A => Gen[B]): Gen[B] = {
      sample match {
        case (rng, a) => f(a)
      }
    }

    def listOfN(size: Gen[Int]): Gen[List[A]] = {}

    // exercise8.10
    def unsized: SGen[A] = {
      SGen(_ => this)
    }
  }

  def union[A](g1: Gen[A], g2: Gen[A]): Gen[A] = {
    g1 match {
      case  =>
    }
  }

  def weighted[A](g1: (Gen[A],Double), g2: (Gen[A],Double)): Gen[A] = {
  }

  def &&(p: Prop): Prop = {

  }


  case class SGen[+A](forSize: Int => Gen[A])


  // exercise8.12
  def listOf[A](g: Gen[A]): SGen[List[A]] = {
    g.unsized.
  }

}
