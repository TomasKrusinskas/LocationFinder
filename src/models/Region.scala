package models

import upickle.default.{ReadWriter, macroRW}
import upickle.implicits.key

case class Region(
  name: String,
  @key("coordinates") geometries: List[Polygon]
)

object Region:
  implicit val rw: ReadWriter[Region] = macroRW
