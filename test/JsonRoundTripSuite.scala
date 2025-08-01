import munit.FunSuite
import upickle.default._
import models._

class JsonRoundTripSuite extends FunSuite {

  test("Point JSON round-trip") {
    val pt = Point(3.14, 2.72)
    val json = write(pt)
    val back = read[Point](json)
    assertEquals(back, pt)
  }

  test("Polygon JSON round-trip") {
    val poly = Polygon(List(Point(0.0,0.0), Point(0.0,1.0), Point(1.0,1.0)))
    val json = write(poly)
    val back = read[Polygon](json)
    assertEquals(back, poly)
  }

  test("Region JSON round-trip") {
    val region = Region("TestRegion", List(
      Polygon(List(Point(0,0), Point(1,0), Point(1,1), Point(0,1), Point(0,0)))
    ))
    val json = write(region)
    val back = read[Region](json)
    assertEquals(back, region)
  }

  test("Location JSON round-trip") {
    val loc = Location("SampleLoc", Point(10.0, 20.0))
    val json = write(loc)
    val back = read[Location](json)
    assertEquals(back, loc)
  }

  test("RegionMatch JSON round-trip (with camelCase key)") {
    val matchResult = RegionMatch("Test", List("Loc1", "Loc2"))
    val json = write(matchResult)
    val back = read[RegionMatch](json)
    assertEquals(back, matchResult)
    assert(json.contains("matched_locations"))
  }
}
