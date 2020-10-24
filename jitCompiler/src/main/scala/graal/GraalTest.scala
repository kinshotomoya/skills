package graal

import java.util.concurrent.TimeUnit

import org.openjdk.jmh.annotations._

import scala.annotation.tailrec
import scala.io.BufferedSource

// フォーク数を指定
// デフォルトは5
@Fork(1)
// ウォームアップ回数を指定（対象のメソッドを計測前に、10000回を1回実行する）
@Warmup(iterations = 1, batchSize = 10000)
// 実際の計測を、10000回を20回実行する。その結果を測定値をして出す
// 公式に、10~20のイテレーションを行うと、現実的な数字が出ると書いている
@Measurement(iterations = 30, batchSize = 10000)
// 1回あたりの実行時間を測定する
@BenchmarkMode(Array(Mode.SingleShotTime))
// 計測単位をms
@OutputTimeUnit(TimeUnit.MILLISECONDS)
class GraalTest {

  // このメソッドを測定対象にする
  @Benchmark
  def run = {
    val fileName: String = "GraalTest.txt"
    val encode: String = "UTF-8"

    // scala2.13.xでは、resources配下のファイルを以下のコマンド、相対パスで取得できる
    // scala.io.Source.fromResource(fileName)
    val source: BufferedSource = scala.io.Source.fromResource(fileName)
    val lines: Iterator[String] = source.getLines()
    val sortedTextList = lines.toList.mkString(" ").split(" ").sorted.toList
    val value = createMap(sortedTextList)
    val top10Words = value.toList.sortBy(_._2).reverse.take(10)
  }

  private def createMap(wordList: List[String]): Map[String, Long] = {
    @tailrec
    def loop(list: List[String], acc: Map[String, Long]): Map[String, Long] = {
      list match {
        // 初めの時
        case head :: tail if acc.isEmpty => {
          loop(tail, acc + (head -> 1L))
        }
        // 2回目以降
        case head :: tail => {
          acc.get(head) match {
            case Some(value) => {
              loop(tail, acc.updated(head, value + 1L))
            }
            case None => {
              loop(tail, acc + (head -> 1L))
            }
          }
        }
        case head :: Nil => {
          acc.get(head) match {
            case Some(value) => {
              acc.updated(head, value + 1L)
            }
            case None => {
              acc + (head -> 1L)
            }
          }
        }
        case Nil => acc
      }
    }
    loop(wordList, Map.empty[String, Long])
  }
}

