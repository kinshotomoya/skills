package jacksonExample

import akka.util.ByteString
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule

import java.util

object Jackson extends App {

  case class InputResult(document: Document, meta: Meta, key: Option[String] = None)
  case class Meta(hits: String)

  case class OutputResult(document: Document, meta: Meta)

  val document = new Document("kinsho", "1111", util.Arrays.asList("hoge","fuga","bar"), 2221L, 0.22f)
  val meta = Meta("1222")

  val result = InputResult(document, meta)


  val mapper = JsonMapper.builder().addModule(DefaultScalaModule).build()
  val json: String = mapper.writeValueAsString(result)

  val value = ByteString(json)
  println(value.utf8String)
//  val fromJsonValue = mapper.readValue(value.utf8String, classOf[OutputResult])
//  println(fromJsonValue.document.ctr)

}
