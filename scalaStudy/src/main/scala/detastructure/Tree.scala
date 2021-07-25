package detastructure

import scala.collection.mutable

object Tree {
  class TreeNode(_value: Int = 0, _left: TreeNode = null, _right: TreeNode = null) {
    var value: Int = _value
    var left: TreeNode = _left
    var right: TreeNode = _right
  }

  object SameTree {
    def isSameTree(p: TreeNode, q: TreeNode): Boolean = {
      if(p == null && q == null) {
        true
      } else if(p == null || q == null) {
        false
      }else if(p.value != q.value){
        false
      } else if(p.value == q.value) {
        isSameTree(p.left, q.left) && isSameTree(p.right, q.right)
      } else {
        false
      }
    }
  }



  object BinarySearchTree {
    class Node(_data: Int = 0, _left: Node = null, _right: Node = null) {
      var data: Int = _data
      var left: Node = _left
      var right: Node = _right
    }


    def check(node: Node, min: Int, max: Int): Boolean = {
      if(node == null) {
        true
      } else if(node.data > min && node.data < max) {
        true
      } else {
        check(node.left, min, node.data) && check(node.right, node.data, max)
      }
    }

    def checkBST(root: Node): Boolean = {
      check(root, Int.MinValue, Int.MaxValue)
    }



  }


  object Tyies {


    // time complexity: O(MN) M・・・dictionaryの大きさ、N・・・sentenceの大きさ
    // space complexity: O(N) N・・・sentenceの大きさ
    def replaceWords(dictionary: List[String], sentence: String): String = {
      val sentenceList = sentence.split(" ").toList

      @scala.annotation.tailrec
      def loop(dict: List[String], sentenceList: List[String]): List[String] = {
        dict match {
          case Nil => sentenceList
          case head :: Nil => sentenceList.map(s => if(s.startsWith(head)) head else s)
          case head :: tail =>
            val tmpSentenceList = sentenceList.map(s => if(s.startsWith(head)) head else s)
            loop(tail, tmpSentenceList)
        }
      }

      loop(dictionary, sentenceList).mkString(" ")

    }
  }


  object Tries2 {
    class Trie(_word: String, _children: Array[Trie] = Array.fill('a' - 'z' + 1)(null)) {
      var word: String = _word
      val children = _children
    }

    def replaceWords(dictionary: List[String], sentence: String): String = {
      val trie: Trie = new Trie(_word = "")
      dictionary.foreach(dict => {
        var cur: Trie = trie
        dict.toCharArray.foreach(d => {
          if(cur.children(d - 'a') == null) {
            cur.children(d - 'a') = new Trie(_word = "")
          }
          cur = cur.children(d - 'a')
        })
        cur.word = dict
      })


      val stringBuilder: mutable.Seq[Char] = new StringBuilder()
      val sentenceArray: Array[String] = sentence.split(" ")
      sentenceArray.foreach(word => {
        if(stringBuilder.nonEmpty) {
          stringBuilder.appended(" ")
        }
        var cur = trie
        word.toCharArray.foreach(w => {
          if(cur.children(w - 'a') == null || cur.word != null) {

          }
          cur = cur.children(w - 'a')
        })
        if(cur.word != null) {
          stringBuilder.appended(cur.word)
        } else {
          word
        }
      })
      stringBuilder.toString()
    }

  }




}
