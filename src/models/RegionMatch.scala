package models

import upickle.default.*
import upickle.implicits.key

case class RegionMatch(
  region: String,
  @key("matched_locations") matchedLocations: List[String]
)
object RegionMatch:
  implicit val rw: ReadWriter[RegionMatch] = macroRW
