import scala.annotation.tailrec

def fibo(goal: Int): Int = {
  @tailrec
  def loop(index: Int, now: Int, next: Int): Int = {
    if (goal == index) {
      now
    } else {
      loop(index+1, next, now+next)
    }
  }
  loop(1, 0, 1)
}

val result: Int = fibo(10)
println(result)
