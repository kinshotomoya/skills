package detastructure

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

object CourseScheduleQuestion {
  object Solution {
    // ex) Input: numCourses = 4, prerequisites = [[1,0],[2,0],[3,1],[3,2]]
    val visited: mutable.Set[Int] = scala.collection.mutable.Set.empty[Int]
    val order: mutable.Stack[Int] = scala.collection.mutable.Stack.empty[Int]
    val nodeStack: mutable.Stack[Int] = scala.collection.mutable.Stack.empty[Int]
    val allNodesSet = scala.collection.mutable.Set.empty[Int]

    def findOrder(numCourses: Int, prerequisites: Array[Array[Int]]): Array[Int] = {
      val newPrerequisites: Array[Array[Int]] = changeGraphDataStructure(numCourses, prerequisites)
      for(i <- 0 until numCourses) {
        allNodesSet.addOne(i)
      }

      def dfs(target: Int, children: Array[Int]): Unit = {
        if(!visited.contains(target)) visited.add(target)
        // 小要素がなければorderに追加と、それ自身をstackから削除
        if(children.isEmpty) {
          order.push(target)
          nodeStack.pop()
        } else {
          nodeStack.pushAll(children)
        }

        if(nodeStack.nonEmpty) {
          // stackにまだ要素が存在する場合（まだ小要素の探索が末尾で終わっていない場合）は、
          // 次の小要素を探索対象にする
          val nextTarget = nodeStack.head
          dfs(nextTarget, newPrerequisites(nextTarget).filterNot(ele => visited.contains(ele)))
        } else if(visited.size != numCourses) {
          // stackにはもうどの要素もないが、まだ全ての要素を探索していない場合（末尾まで探索したが、別のルートは探索していない場合）
          // 他のnodeについて探索を行う
          val nextTarget = allNodesSet.diff(visited).head
          nodeStack.push(nextTarget)
          dfs(nextTarget, newPrerequisites(nextTarget).filterNot(ele => visited.contains(ele)))
        }
      }

      // mainロジック
      if(numCourses == 1) {
        // そもそもコースが一個しかない場合は、0を返す
        Array(0)
      } else {
        if(isCircle(numCourses, prerequisites)) {
          // circleである場合（どのnodeも祖先を持っている場合）
          Array.empty[Int]
        } else {
          // directed graphになっている場合（正規な場合）
          nodeStack.push(0)
          dfs(0, newPrerequisites(0))
          order.toArray
        }
      }
    }

    // directed graphかどうか判断している
    def isCircle(numCourses: Int, prerequisites: Array[Array[Int]]): Boolean = {
      prerequisites.map(array => array(0)).distinct.length == numCourses
    }

    // [[1,0],[2,0],[3,1],[3,2]] => [[1, 2], [3], [3], []]と
    // indexをnode、そのindexに紐づくarrayを小要素としてデータを持つようにする
    def changeGraphDataStructure(numCourses: Int, prerequisites: Array[Array[Int]]): Array[Array[Int]] = {
      // Course数と同じlengthを持つArrayを構築
      val newCourseStructure = new Array[ArrayBuffer[Int]](numCourses)

      // まず側だけ作る
      newCourseStructure.zipWithIndex.foreach((arrayBufferWithIndex: (ArrayBuffer[Int], Int)) => {
        val index = arrayBufferWithIndex._2
        val innerArray: ArrayBuffer[Int] = ArrayBuffer.empty[Int]
        newCourseStructure.update(index, innerArray)
      })

      // 親子を考慮したgraph data structureを構築
      prerequisites.foreach(course => {
        val parent = course(1)
        val child = course(0)

        newCourseStructure.apply(parent).addOne(child)
      })

      newCourseStructure.map(_.toArray)
    }

  }
}
