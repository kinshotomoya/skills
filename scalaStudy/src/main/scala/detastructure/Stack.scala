package detastructure

object Stack {
  class MinStack() {

    /** initialize your data structure here. */
    val stack = scala.collection.mutable.ArrayBuffer.empty[Int]


    def push(`val`: Int): Unit = {
      stack.addOne(`val`)
    }

    def pop(): Unit = {
      stack.remove(stack.size - 1)
      // resizeしたかったら、以下のメソッドを読んであげる
      // しかし、一回一回リサイズすると計算量が大きくなるので、どのタイミングでリサイズすべきか。。。
      stack.trimToSize()
    }

    def top(): Int = {
      stack(stack.size - 1)
    }

    def getMin(): Int = {
      var loop = true
      var index = 0
      var min = stack(index)
      while(loop) {
        if(stack.size < index + 1) {
          loop = false
        } else {
          val cur = stack(index)
          if(cur < min) {
            min = cur
          }
          index += 1
        }
      }
      min
    }

  }


}
