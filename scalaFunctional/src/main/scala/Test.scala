import scala.annotation.tailrec

object Test {
  // これをIntでもしたいどうするのか？
  // 型パラメータにして、
  def findFirst[T](ss: Array[T], key: T, f: (T, T) => Boolean): Int = {
    @tailrec
    def loop(index: Int): Int = {
      if(ss.length < index) -1
      else if(f(ss(index), key)) index
      else loop(index + 1)
    }
    loop(0)
  }

}

def handle[T](value: T, aa: T): Boolean = {
  value == aa
}

Test.findFirst(Array("a"), "a", handle[String])
Test.findFirst(Array(1, 2, 3), 4, handle[Int])
