package models

import upickle.default.{ReadWriter, macroRW}
import upickle.implicits.key

case class RegionMatch(
  region: String,
  @key("matched_locations") matchedLocations: List[String]
)
object RegionMatch {
  implicit val rw: ReadWriter[RegionMatch] = macroRW
}
