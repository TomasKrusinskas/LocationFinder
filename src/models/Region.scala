package models

import upickle.default.*

case class Region(name: String, coordinates: List[List[List[Double]]])
object Region:
  implicit val rw: ReadWriter[Region] = macroRW
