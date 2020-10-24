object Sort {
  // 指定された比較関数に従って、asがソートされているかを判断する関数
  def isSorted[A](as: Array[A], ordered: (A, A) => Boolean): Boolean = {
    def loop[A](head: A, tail: List[A], acc: Boolean): Boolean = {
      if(!acc) {
        false
      } else {
        tail match {
          case head::tail => loop(tail.head, tail.tail, ordered(head, tail.head))
          case _::tail if tail.isEmpty => true
          case Nil => true
        }
      }
    }
    loop(as.head, as.tail.toList, ordered(as.head, as.tail.head))
  }
}

// Sort.isSorted(Array(1, 2, 3, 4))
