package models

import upickle.default.{ReadWriter, macroRW}
import upickle.implicits.key

case class Location(
  name: String,
  @key("coordinates") point: Point
)

object Location:
  implicit val rw: ReadWriter[Location] = macroRW
