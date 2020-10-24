
object ErrorHandlingEither {


//  sealed trait Either[+E, +A]
  case class Left[+E](value: E) extends Either[E, Nothing] {
    override def equals(that: Any): Boolean = ???
  }
  case class Right[+A](value: A) extends Either[Nothing, A] {
    override def equals(that: Any): Boolean = ???
  }

  // exercise 4.6
  sealed trait Either[+E, +A] {
    def map[B](f: A => B): Either[E, B] = {
      this match {
        case l @ Left(_) => l
        case Right(value) => Right(f(value))
      }
    }

    def flatMap[EE >: E, B](f: A => Either[EE, B]): Either[EE, B] = {
      this match {
        case l @ Left(_) => l
        case Right(value) => f(value)
      }
    }

    def orElse[EE >: E, B >: A](b: => Either[EE, B]): Either[EE, B] = {
      this match {
        case Left(_) => b
        case r @ Right(_) => r
      }
    }

    def map2[EE >: E, B , C](b: Either[EE, B])(f: (A, B) => C): Either[EE, C] = {
      this match {
        case l @ Left(_) => l
        case Right(value1: A) => b match {
          case l @ Left(_) => l
          case Right(value2: B) => Right(f(value1, value2))
        }
      }
    }

    def map22[EE >: E, B , C](b: Either[EE, B])(f: (A, B) => C): Either[EE, C] = {
      this.flatMap((x: A) => b.map((y: B) => f(x, y)))
      for {
        x <- this
        y <- b
      } yield f(x, y)
    }

    // exercise 4.7
    def sequence[E, A](es: List[Either[E, A]]): Either[E, List[A]] = {
      es.map((x: Either[E, A]) => x.map((y: A) => List(y))).head
    }

    def sequence[E, A](es: List[Either[E, A]]): Either[E, List[A]] = {
      es.foldRight(Right(List.empty[A]): Either[E, List[A]])((x: Either[E, A], y: Either[E, List[A]]) => {
        y.flatMap(l => x.map(w => l.appended(w)))
      })
    }

    def traverse[E, A, B](as: List[A])(f: A => Either[E, B]): Either[E, List[B]] = {
      as.foldRight(Right(List.empty[B]): Either[E, List[B]])((x: A, y: Either[E, List[B]]) => {
        y.flatMap(l => f(x).map(w => l.appended(w)))
      })
    }

    def traverse2[E,A,B](es: List[A])(f: A => Either[E, B]): Either[E, List[B]] =
      es match {
        case Nil => Right(Nil)
        case h::t => (f(h) map2 traverse(t)(f))(_ :: _)
      }

  }

}
