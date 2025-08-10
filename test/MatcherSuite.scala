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
    assert(matched.contains("Inside"))
    assert(matched.contains("OnEdge"))
    assert(matched.contains("OnVertex"))
    assert(!matched.contains("Outside"))
  }

  test("matchLocationsToRegions handles empty regions list") {
    val location = Location("Test", Point(1, 1))
    val results = RegionMatcher.matchLocationsToRegions(List.empty, List(location))
    assert(results.isEmpty)
  }

  test("matchLocationsToRegions handles empty locations list") {
    val region = Region("Test", List(
      Polygon(List(Point(0, 0), Point(1, 0), Point(1, 1), Point(0, 1)))
    ))
    val results = RegionMatcher.matchLocationsToRegions(List(region), List.empty)
    assertEquals(results.length, 1)
    assert(results.head.matchedLocations.isEmpty)
  }

  test("matchLocationsToRegions handles region with no polygons") {
    val emptyRegion = Region("Empty", List.empty)
    val location = Location("Test", Point(1, 1))
    val results = RegionMatcher.matchLocationsToRegions(List(emptyRegion), List(location))
    val matched = results.find(_.region == "Empty").map(_.matchedLocations).getOrElse(Nil)
    assert(matched.isEmpty)
  }

  test("matchLocationsToRegions handles multiple overlapping regions") {
    val region1 = Region("Region1", List(
      Polygon(List(Point(0, 0), Point(2, 0), Point(2, 2), Point(0, 2)))
    ))
    val region2 = Region("Region2", List(
      Polygon(List(Point(1, 1), Point(3, 1), Point(3, 3), Point(1, 3)))
    ))
    val location = Location("Overlap", Point(1.5, 1.5))
    
    val results = RegionMatcher.matchLocationsToRegions(List(region1, region2), List(location))
    
    val region1Matches = results.find(_.region == "Region1").map(_.matchedLocations).getOrElse(Nil)
    val region2Matches = results.find(_.region == "Region2").map(_.matchedLocations).getOrElse(Nil)
    
    assert(region1Matches.contains("Overlap"))
    assert(region2Matches.contains("Overlap"))
  }

  test("matchLocationsToRegions handles multipolygon region") {
    val multiRegion = Region("Multi", List(
      Polygon(List(Point(0, 0), Point(1, 0), Point(1, 1), Point(0, 1))),
      Polygon(List(Point(2, 2), Point(3, 2), Point(3, 3), Point(2, 3)))
    ))
    val loc1 = Location("InFirst", Point(0.5, 0.5))
    val loc2 = Location("InSecond", Point(2.5, 2.5))
    val loc3 = Location("InNeither", Point(5, 5))
    
    val results = RegionMatcher.matchLocationsToRegions(List(multiRegion), List(loc1, loc2, loc3))
    val matched = results.head.matchedLocations
    
    assert(matched.contains("InFirst"))
    assert(matched.contains("InSecond"))
    assert(!matched.contains("InNeither"))
  }

  test("matchLocationsToRegions handles very small regions") {
    val tinyRegion = Region("Tiny", List(
      Polygon(List(
        Point(0.0, 0.0), Point(0.0001, 0.0), 
        Point(0.0001, 0.0001), Point(0.0, 0.0001)
      ))
    ))
    val insideLoc = Location("Inside", Point(0.00005, 0.00005))
    val outsideLoc = Location("Outside", Point(0.001, 0.001))
    
    val results = RegionMatcher.matchLocationsToRegions(List(tinyRegion), List(insideLoc, outsideLoc))
    val matched = results.head.matchedLocations
    
    assert(matched.contains("Inside"))
    assert(!matched.contains("Outside"))
  }

  test("matchLocationsToRegions handles locations on polygon boundaries") {
    val region = Region("Boundary", List(
      Polygon(List(Point(0, 0), Point(2, 0), Point(2, 2), Point(0, 2)))
    ))
    val onVertex = Location("OnVertex", Point(0, 0))
    val onEdge = Location("OnEdge", Point(1, 0))
    val onCorner = Location("OnCorner", Point(2, 2))
    
    val results = RegionMatcher.matchLocationsToRegions(List(region), List(onVertex, onEdge, onCorner))
    val matched = results.head.matchedLocations
    
    assert(matched.contains("OnVertex"))
    assert(matched.contains("OnEdge"))
    assert(matched.contains("OnCorner"))
  }

  test("matchLocationsToRegions handles complex concave region") {
    val lShapeRegion = Region("LShape", List(
      Polygon(List(
        Point(0, 0), Point(2, 0), Point(2, 1), 
        Point(1, 1), Point(1, 2), Point(0, 2)
      ))
    ))
    val insideLeft = Location("InsideLeft", Point(0.5, 0.5))
    val insideRight = Location("InsideRight", Point(1.5, 0.5))
    val insideTop = Location("InsideTop", Point(0.5, 1.5))
    val inNotch = Location("InNotch", Point(1.5, 1.5))
    
    val results = RegionMatcher.matchLocationsToRegions(
      List(lShapeRegion), 
      List(insideLeft, insideRight, insideTop, inNotch)
    )
    val matched = results.head.matchedLocations
    
    assert(matched.contains("InsideLeft"))
    assert(matched.contains("InsideRight"))
    assert(matched.contains("InsideTop"))
    assert(!matched.contains("InNotch"))
  }

  test("matchLocationsToRegions handles extreme coordinate values") {
    val worldRegion = Region("World", List(
      Polygon(List(Point(-180, -90), Point(180, -90), Point(180, 90), Point(-180, 90)))
    ))
    val locations = List(
      Location("Center", Point(0, 0)),
      Location("NorthPole", Point(0, 90)),
      Location("SouthPole", Point(0, -90)),
      Location("DateLine", Point(180, 0)),
      Location("PrimeMeridian", Point(0, 0))
    )
    
    val results = RegionMatcher.matchLocationsToRegions(List(worldRegion), locations)
    val matched = results.head.matchedLocations
    
    assert(matched.size >= 4)
  }

  test("matchLocationsToRegions handles triangular region") {
    val triangle = Region("Triangle", List(
      Polygon(List(Point(0, 0), Point(2, 0), Point(1, 2)))
    ))
    val insideTriangle = Location("Inside", Point(1, 0.5))
    val outsideTriangle = Location("Outside", Point(0, 2))
    val onTriangleEdge = Location("OnEdge", Point(1, 0))
    
    val results = RegionMatcher.matchLocationsToRegions(
      List(triangle), 
      List(insideTriangle, outsideTriangle, onTriangleEdge)
    )
    val matched = results.head.matchedLocations
    
    assert(matched.contains("Inside"))
    assert(matched.contains("OnEdge"))
    assert(!matched.contains("Outside"))
  }

  test("matchLocationsToRegions handles degenerate polygon in region") {
    val regionWithLine = Region("WithLine", List(
      Polygon(List(Point(0, 0), Point(1, 1))),
      Polygon(List(Point(2, 2), Point(3, 2), Point(3, 3), Point(2, 3)))
    ))
    val location = Location("Test", Point(2.5, 2.5))
    
    val results = RegionMatcher.matchLocationsToRegions(List(regionWithLine), List(location))
    val matched = results.head.matchedLocations
    
    assert(matched.contains("Test"))
  }

  test("matchLocationsToRegions preserves region order in results") {
    val region1 = Region("First", List(Polygon(List(Point(0, 0), Point(1, 0), Point(1, 1), Point(0, 1)))))
    val region2 = Region("Second", List(Polygon(List(Point(2, 2), Point(3, 2), Point(3, 3), Point(2, 3)))))
    val region3 = Region("Third", List(Polygon(List(Point(4, 4), Point(5, 4), Point(5, 5), Point(4, 5)))))
    
    val results = RegionMatcher.matchLocationsToRegions(List(region1, region2, region3), List.empty)
    
    assertEquals(results.length, 3)
    assertEquals(results(0).region, "First")
    assertEquals(results(1).region, "Second")
    assertEquals(results(2).region, "Third")
  }

  test("matchLocationsToRegions handles duplicate location names") {
    val region = Region("Test", List(
      Polygon(List(Point(0, 0), Point(2, 0), Point(2, 2), Point(0, 2)))
    ))
    val loc1 = Location("Same", Point(1, 1))
    val loc2 = Location("Same", Point(0.5, 0.5))
    
    val results = RegionMatcher.matchLocationsToRegions(List(region), List(loc1, loc2))
    val matched = results.head.matchedLocations
    
    assertEquals(matched.count(_ == "Same"), 2)
  }