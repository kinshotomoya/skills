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

  object GuessGame {
    // 問題: https://leetcode.com/explore/learn/card/binary-search/125/template-i/951/
    /**
     * The API guess is defined in the parent class.
     * @return 	     -1 if num is lower than the guess number
     *			      1 if num is higher than the guess number
     *               otherwise return 0
     * def guess(num: Int): Int = {}
     */


    def guess(num: Int): Int = ???
    def guessNumber(n: Int): Int = {
      def loop(left: Int, right: Int): Int = {
        val mid = left + (right - left) / 2
        if(left == right) {
          mid
        } else {
          val guessResult = guess(mid)
          if(guessResult == -1) {
            // pickedNumberがmidより小さい場合
            loop(left = left, right = mid - 1)
          } else if(guessResult == 1) {
            // pickedNumberがmidより大きい場合
            loop(left = mid + 1, right = right)
          } else if(guessResult == 0) {
            // pickedNumberがmidと同じ場合
            mid
          } else {
            -1
          }
        }
      }
      val left = 1
      val right = n
      loop(left, right)
    }
  }

  object RotatedSortedArray {
    // 配列の中にtargetが存在していればそのindexを返す。
    // 存在していないならば-1を返す
    // 問題: https://leetcode.com/explore/learn/card/binary-search/125/template-i/952/
    // 解説: https://www.youtube.com/watch?v=QdVrY3stDD4

    //binary-searchを利用しない場合
    def searchWithoutBinarySearch(nums: Array[Int], target: Int): Int = {
      nums.indexOf(target)
    }


    //binary-searchを利用する場合
    def search(nums: Array[Int], target: Int): Int = {
      def getStartIndex(leftIndex: Int, rightIndex: Int): Int = {
        val midIndex = leftIndex + (rightIndex - leftIndex) / 2
        val midValue = nums(midIndex)
        if(leftIndex == rightIndex) {
          leftIndex
        } else if(nums(rightIndex) < midValue) {
          getStartIndex(leftIndex = midIndex + 1, rightIndex = rightIndex)
        } else {
          getStartIndex(leftIndex = leftIndex, rightIndex = midIndex)
        }
      }


      val leftIndex = 0
      val rightIndex = nums.length - 1
      val startIndex = getStartIndex(leftIndex, rightIndex)

      def binarySearch(leftIndex: Int, rightIndex: Int): Int = {
        val midIndex = leftIndex + (rightIndex - leftIndex) / 2
        val midValue = nums(midIndex)
        // 最後まで探索して（leftIndex == rightIndex）さらに、最後探索valueがtargetと違う場合には、numsには存在しないと言うことなので
        // -1を返す
        if(leftIndex == rightIndex && target != midValue) {
          -1
        } else {
          if(midValue == target) {
            midIndex
          } else if(target < midValue) {
            binarySearch(leftIndex = leftIndex, rightIndex = midIndex)
          } else if(midValue < target) {
            binarySearch(leftIndex = midIndex + 1, rightIndex = rightIndex)
          } else {
            -1
          }

        }
      }

      // 完全sorted array（rotatedされていない）の場合には、leftIndex = startIndex, rightIndex = 一番右端のindexで計算する
      if(nums(startIndex) <= target && target <= nums(rightIndex)) {
        binarySearch(leftIndex = startIndex, rightIndex = rightIndex)
      } else {
        binarySearch(leftIndex = leftIndex, rightIndex = startIndex)
      }
    }
  }


  object BadVersion {
    // 問題: https://leetcode.com/explore/learn/card/binary-search/126/template-ii/947/
    def isBadVersion(version: Int): Boolean = ???
    def firstBadVersion(n: Int): Int = {
      val left = 0
      val right = n

      def loop(left: Int, right: Int): Int = {
        val mid = left + (right - left) / 2
        if(left == right) {
          mid
        } else {
          if(isBadVersion(mid)) {
            // midがtrueの場合は、midの左だけ探索する
            loop(left = left, right = mid)
          } else if(!isBadVersion(mid)) {
            // midがfalseの場合は、midの右だけを探索する
            loop(left = mid + 1, right = right)
          } else {
            mid
          }
        }
      }

      loop(left, right)

    }
  }

}
