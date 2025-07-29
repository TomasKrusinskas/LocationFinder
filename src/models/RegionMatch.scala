package models

import upickle.default.*
import upickle.implicits.key

case class RegionMatch(region: String, matched_locations: List[String])
object RegionMatch:
  implicit val rw: ReadWriter[RegionMatch] = macroRW

