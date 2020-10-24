object datastructure {

  // exercise3.24

  // List(1, 2, 3, 4)
  // List(1, 2) => true
  // List(2, 3) => true
  // List(1, 3) => false
  def hasSubSequence[A](sup: List[A], sub: List[A]): Boolean = {
    sup match {
      case Nil if sub == Nil => true
      case head :: tail if checkList(sup, sub) => true
      case head :: tail => hasSubSequence(tail, sub)
    }
  }

  def checkList[A](sup: List[A], sub: List[A]): Boolean = {
    (sup, sub) match {
      case (_, Nil) => true
      case (head :: tail, subHead :: subTail) if head == subHead => checkList(tail, subTail)
      case _ => false
    }
  }


  sealed trait Tree[+A]
  case class Leaf[A](value: A) extends Tree[A]
  case class Branch[A](left: Tree[A], right: Tree[A]) extends Tree[A]


  // exercise 2.25
  // Branch(Branch(Leaf("a"), Leaf("b")), Branch(Leaf("c"), Leaf("d")))
  // => branch 3
  // => leaf 4
  def size[A](tree: Tree[A]): Int = {
    tree match {
      case Branch(left, right) => size(left) + size(right)
      case Leaf(_) => 1
    }
  }

  // exercise 2.25 answer
  def sizeAnswer[A](tree: Tree[A]): Int = {
    tree match {
      case Branch(left, right) => 1 + size(left) + size(right)
      case Leaf(_) => 1
    }
  }

  // exercise 2.26
  def maximum(tree: Tree[Int]): Int = {
    tree match {
      case Leaf(value) => value
      case Branch(l, r) => maximum(l).max(maximum(r))
    }
  }

  // exercise 2.27
  def depth[A](tree: Tree[A]): Int = {
    tree match {
      case Leaf(_) => 0
    }
  }


  // exercise 2.27 answer
  def depthAnswer[A](t: Tree[A]): Int = t match {
    case Leaf(_) => 0
    case Branch(l,r) => 1 + (depth(l) max depth(r))
  }


  // exercise 2.28
  def map[A, B](tree: Tree[A])(f: A => B): Tree[B] = {
    tree match {
      case Leaf(value) => Leaf(f(value))
      case Branch(l, r) => Branch(map(l)(f), map(r)(f))
    }
  }

  // exercise 2.29
//  def fold[A]()


  // exercise 2.29 answer
//  def fold[A,B](t: Tree[A])(l: A => B)(b: (B,B) => B): B = {
//    t match {
//      case Leaf(value) =>
//    }
//  }


  // chain functions
  def reverse(list: List[Int]): List[Int] = {
    list.reverse
  }

  def add(list: List[Int]): List[Int] = {
    list.map(_ + 1)
  }


  val result: List[Int] => List[Int] = Function.chain(Seq(list => reverse(list), list => add(list)))

  result(List(1, 2, 3))



}
