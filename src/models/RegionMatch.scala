package models

import upickle.default.*

case class RegionMatch(region: String, matched_locations: List[String])
object RegionMatch:
  implicit val rw: ReadWriter[RegionMatch] = macroRW
