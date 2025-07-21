package models

import upickle.default.*

case class Location(name: String, coordinates: List[Double])
object Location:
  implicit val rw: ReadWriter[Location] = macroRW
