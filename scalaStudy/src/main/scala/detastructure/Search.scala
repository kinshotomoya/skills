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

}
