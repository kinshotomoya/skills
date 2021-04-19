import java.util.concurrent.{Callable, ExecutorService, Future, TimeUnit}


type Par[A] = ExecutorService => Future[A]

// exercise7.1
def map2[A, B, C](a: => Par[A], b: => Par[B])(f: (A, B) => C): Par[C] = ???

// exercise7.2
// exercise7.3
object Par {
  def unit[A](a: A): Par[A] = (es: ExecutorService) => UnitFuture(a)
  private case class UnitFuture[A](get: A) extends Future[A] {
    def isDone = true
    def get(timeout: Long, units: TimeUnit) = get
    def isCancelled = false
    def cancel(evenIfRunning: Boolean): Boolean = false
  }
  def map2[A, B, C](a: Par[A], b: Par[B])(f: (A, B) => C): Par[C] =
    (es: ExecutorService) => {
      val af: Future[A] = a(es)
      val bf = b(es)
      UnitFuture (f(af.get, bf.get))
    }
  def fork[A](a: => Par[A]): Par[A] =
    es =>
      es.submit(new Callable[A] {
        def call = a(es).get
      })
  def lazyUnit[A](a: => A): Par[A] = fork(unit(a))

  // exercise7.4
  def asyncF[A,B](f: A => B): A => Par[B] = a => {
      lazyUnit(f(a))
    }

  def sortPar(parList: Par[List[Int]]): Par[List[Int]] = {
    map2(parList, unit(()))((a, _) => a.sorted)
  }


  def map[A, B](pa: Par[A])(f: A => B): Par[B] = {
    map2(pa, unit())((a, _) => f(a))
  }

  // exercise7.5
  def sequence_simple[A](l: List[Par[A]]): Par[List[A]] =
    l.foldRight[Par[List[A]]](unit(List()))((h: Par[A], t: Par[List[A]]) => map2(h, t)(_ :: _))

  def sequenceRight[A](as: List[Par[A]]): Par[List[A]] =
    as match {
      case Nil => unit(Nil)
      case h :: t => map2(h, fork(sequenceRight(t)))(_ :: _)
    }

  def parMap[A,B](ps: List[A])(f: A => B): Par[List[B]] = fork { val fbs: List[Par[B]] = ps.map(asyncF(f))
    sequence_simple(fbs)
  }

  // exercise7.6
  def parFilter[A](as: List[A])(f: A => Boolean): Par[List[A]] = {
    as.map(a => asyncF((s: A) => ))
    val list = as.foldRight(Nil: List[A])((a: A, b: List[A]) => if(f(a)) b.appended(a) else b)
    sequence_simple(list)
  }


}
