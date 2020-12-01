
trait RNG {
  def nextInt: (Int, RNG)
}

object PureFunction {

  type Rand[+A] = RNG => (A, RNG)

  val int: Rand[Int] = (_: RNG).nextInt

  case class Simple(seed: Long) extends RNG {
    def nextInt: (Int, RNG) = {
      val newSeed = (seed * 0x5DEECE66DL + 0xBL) & 0xFFFFFFFFFFFFL // `&` is bitwise AND. We use the current seed to generate a new seed.
      val nextRNG = Simple(newSeed) // The next state, which is an `RNG` instance created from the new seed.
      val n = (newSeed >>> 16).toInt // `>>>` is right binary shift with zero fill. The value `n` is our new pseudo-random integer.
      (n, nextRNG) // The return value is a tuple containing both a pseudo-random integer and the next `RNG` state.
    }
  }


  val s = Simple(111L)

  nonNegativeInt(s)

  // exercise6.1
  def nonNegativeInt(rng: RNG): (Int, RNG) = {
    val (i, r) = rng.nextInt
    (if (i < 0) -(i + 1) else i, r)
  }

  // exercise6.2
  def double(rng: RNG): (Double, RNG) = {
    val (i, r) = rng.nextInt

    (i.toDouble, r)
  }

  // exercise6.3
  def intDouble(rng: RNG): ((Int,Double), RNG) = {
    val (i1, r1) = nonNegativeInt(rng)
    val (i2, r2) = double(r1)
    ((i1, i2), r2)
  }

  // exercise6.3
  def doubleInt(rng: RNG): ((Double,Int), RNG) = {
    val (i1, r1) = nonNegativeInt(rng)
    val (i2, r2) = double(r1)
    ((i2, i1), r2)
  }

  // exercise6.3
  def double3(rng: RNG): ((Double,Double,Double), RNG) = {
    val (i1, r1) = double(rng)
    val (i2, r2) = double(r1)
    val (i3, r3) = double(r2)
    ((i1, i2, i3), r3)
  }

  // exercise6.4
  def ints(count: Int)(rng: RNG): (List[Int], RNG) = {
    @scala.annotation.tailrec
    def loop(n: Int, rng: RNG, acc: List[Int]): (List[Int], RNG) = {
      if(n > 0) {
        val (i, r) = rng.nextInt
        loop(n - 1, r, acc :+ i)
      } else {
        (acc, rng)
      }
    }
    loop(count, rng, Nil)
  }

  // exercise6.5
  def map[A, B](s: RNG => (A, RNG))(f: A => B): Rand[B] = {
    rng => {
      val (a, r) = s(rng)
      (f(a), r)
    }
  }

//  def doubleViaMap(rng: RNG): (Double, RNG) = {
//    map(nonNegativeInt)()
//  }


  // exercise6.6
  def map2[A, B, C](ra: Rand[A], rb: Rand[B])(f: (A, B) => C): Rand[C] = {
    rng => {
      val (a: A, r1: RNG) = ra(rng)
      val (b: B, r2: RNG) = rb(r1)
      (f(a, b), r2)
    }
  }


  def unit[A](a: A): Rand[A] =
    rng => (a, rng)

  // exercise6.7
  def sequence[A](fs: List[Rand[A]]): Rand[List[A]] = {

    fs.foldRight(unit(List[A]()))((f: Rand[A], acc: Rand[List[A]]) => map2(f, acc)(_ :: _))

//    rang => {
//      fs match {
//        case head :: tail => head(rang)
//      }
//    }
  }

  // exercise6.8
  def flatMap[A,B](f: Rand[A])(g: A => Rand[B]): Rand[B] = {
    rng => {
      val (a, r1) = f(rng)
      g(a)(r1)
    }
  }

  // exercise6.8
  def nonNegativeLessThan(n: Int): Rand[Int] =
    flatMap(nonNegativeInt)(i => {
      val mod = i % n
      if (i + (n-1) - mod >= 0) unit(mod) else nonNegativeLessThan(n)
    })



  // exercise6.9
  def mapViaFlatMap[A, B](s: RNG => (A, RNG))(f: A => B): Rand[B] = {
    flatMap(s)(a => unit(f(a)))
  }

  def map2ViaFlatMap[A, B, C](ra: Rand[A], rb: Rand[B])(f: (A, B) => C): Rand[C] = {
    flatMap(ra)(a => flatMap(rb)(b => unit(f(a, b))))
  }

}


// exercise6.10
object Test {
  type Rand[A] = State[RNG, A]

  case class State[S,+A](run: S => (A, S)) {
    def unit(a: A): Rand[A] = {
      State {
        s: RNG => (a, s)
      }
    }




  }


}
