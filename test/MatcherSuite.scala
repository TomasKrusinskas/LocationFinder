import munit.FunSuite
import models._
import utils.RegionMatcher

class MatcherSuite extends FunSuite:

  test("matchLocationsToRegions matches correctly") {
    val region = Region("Box", List(List(List(0.0, 0.0), List(0.0, 2.0), List(2.0, 2.0), List(2.0, 0.0), List(0.0, 0.0))))
    val locInside = Location("Inside", List(1.0, 1.0))
    val locOutside = Location("Outside", List(5.0, 5.0))

    val results = RegionMatcher.matchLocationsToRegions(List(region), List(locInside, locOutside))
    assertEquals(results.head.matched_locations, List("Inside"))
  }
