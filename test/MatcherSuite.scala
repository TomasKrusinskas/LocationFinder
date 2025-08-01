import munit.FunSuite
import models._
import utils.RegionMatcher

class MatcherSuite extends FunSuite:

  test("matchLocationsToRegions matches locations correctly inside and outside a region") {
    val region = Region(
      "Box",
      List(
        Polygon(List(
          Point(0.0, 0.0), Point(0.0, 2.0), Point(2.0, 2.0), Point(2.0, 0.0), Point(0.0, 0.0)
        ))
      )
    )
    val locInside = Location("Inside", Point(1.0, 1.0))
    val locOutside = Location("Outside", Point(5.0, 5.0))
    val locOnEdge = Location("OnEdge", Point(0.0, 1.0))
    val locOnVertex = Location("OnVertex", Point(0.0, 0.0))

    val results = RegionMatcher.matchLocationsToRegions(
      List(region),
      List(locInside, locOutside, locOnEdge, locOnVertex)
    )

    val matched = results.find(_.region == "Box").map(_.matchedLocations).getOrElse(Nil)
    assert(matched.contains("Inside"), "Inside point should be matched")
    assert(matched.contains("OnEdge"), "Edge point should be matched")
    assert(matched.contains("OnVertex"), "Vertex point should be matched")
    assert(!matched.contains("Outside"), "Outside point should NOT be matched")
  }
