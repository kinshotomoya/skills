package detastructure

object Search {

  object BinarySearchByMySelf {
    def search(nums: Array[Int], target: Int): Int = {
      def loop(left: Int, right: Int, midIndex: Int): Int = {
        if(left > right) {
          -1
        } else {
          if(nums(midIndex) == target) {
            midIndex
          } else if(target < nums(midIndex)) {
            val newRight = midIndex - 1
            loop(left = left, right = newRight, midIndex = (newRight - left) / 2)
          } else if(nums(midIndex) < target) {
            val newLeft = midIndex + 1
            loop(left = newLeft, right = right, midIndex = newLeft + (right - newLeft) / 2)
          } else {
            -1
          }
        }
      }

      val arrSize = nums.length
      val left = 0
      val right = arrSize - 1
      val midIndex = left + (right - left) / 2
      loop(left, right, midIndex)

    }
  }

  object MySqrt {
    // 平方根を求める without binarySearch
    // 0から逐次的に解を求める
    def mySqrt(x: Int): Int = {
      // input: 4
      // output: 2

      // input: 8
      // output: 2.8 -> return 2

      def loop(value: Int): Int = {
        val targetValue = value.toLong
        val SquaredValue = targetValue * targetValue
        if(x == SquaredValue) {
          value
        } else if(x < SquaredValue) {
          value - 1
        } else {
          loop(value + 1)
        }
      }

      val value = 0
      loop(value)
    }


  }

  object MySqrtWithBinarySearch {
    // binarySearchで平方根を求める
    // 解説：https://www.youtube.com/watch?v=VYtEKhxKd1Q

    // 1 ~ inputまでのデータセットを作って、binary searchで分割しながら解を求める

    def mySqrt(x: Int): Int = {
      def loop(left: Long, right: Long): Int = {
        val mid = left + (right - left) / 2
        val squaredMid = mid * mid

        if(x == squaredMid) {
          mid.toInt
        } else if(left == right) { // 最後まで探索した際
          (mid - 1L).toInt
        } else {
          if(x < squaredMid) {
            loop(left = left, right = mid)
          } else{
            loop(left = mid + 1L, right = right)
          }
        }

      }

      if(x == 0) {
        0
      } else if (x == 1)  {
        1
      } else {
        loop(left = 1L, right = x)
      }
    }


  }



}
