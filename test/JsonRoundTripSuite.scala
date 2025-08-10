import munit.FunSuite
import upickle.default._
import models._
import scala.util.Try

class JsonParsingEdgeCasesSuite extends FunSuite:
  test("Point parsing handles malformed JSON") {
    val malformedJson = """[1.0]"""
    val result = Try(read[Point](malformedJson))
    assert(result.isFailure)
  }

  test("Point parsing handles invalid types") {
    val invalidJson = """["not_a_number", 2.0]"""
    val result = Try(read[Point](invalidJson))
    assert(result.isFailure)
  }

  test("Point parsing handles extreme values") {
    val extremeJson = """[1.7976931348623157E308, -1.7976931348623157E308]"""
    val result = Try(read[Point](extremeJson))
    assert(result.isSuccess)
  }

  test("Polygon parsing handles empty vertices list") {
    val emptyPolygonJson = """[]"""
    val result = Try(read[Polygon](emptyPolygonJson))
    assert(result.isSuccess)
    assertEquals(result.get.vertices.length, 0)
  }

  test("Region parsing handles missing required fields") {
    val incompleteJson = """{"name": "TestRegion"}"""
    val result = Try(read[Region](incompleteJson))
    assert(result.isFailure)
  }

  test("Region parsing handles empty coordinates array") {
    val emptyCoordinatesJson = """{"name": "TestRegion", "coordinates": []}"""
    val result = Try(read[Region](emptyCoordinatesJson))
    assert(result.isSuccess)
    assertEquals(result.get.geometries.length, 0)
  }

  test("Location parsing handles extreme coordinate values") {
    val extremeLocationJson = """{"name": "Extreme", "coordinates": [180.0, 90.0]}"""
    val result = Try(read[Location](extremeLocationJson))
    assert(result.isSuccess)
    assertEquals(result.get.point, Point(180.0, 90.0))
  }

  test("Point parsing handles null values") {
    val nullJson = """[null, 2.0]"""
    val result = Try(read[Point](nullJson))
    assert(result.isFailure)
  }

  test("Point parsing handles empty array") {
    val emptyArrayJson = """[]"""
    val result = Try(read[Point](emptyArrayJson))
    assert(result.isFailure)
  }

  test("Point parsing handles array with too many elements") {
    val tooManyJson = """[1.0, 2.0, 3.0, 4.0]"""
    val result = Try(read[Point](tooManyJson))
    assert(result.isSuccess)
    assertEquals(result.get, Point(1.0, 2.0))
  }

  test("Point parsing handles scientific notation") {
    val scientificJson = """[1.5e2, -3.2E-4]"""
    val result = Try(read[Point](scientificJson))
    assert(result.isSuccess)
    assertEquals(result.get, Point(150.0, -0.00032))
  }

  test("Point parsing handles integer coordinates") {
    val integerJson = """[10, 20]"""
    val result = Try(read[Point](integerJson))
    assert(result.isSuccess)
    assertEquals(result.get, Point(10.0, 20.0))
  }

  test("Polygon parsing handles single point") {
    val singlePointJson = """[[1.0, 2.0]]"""
    val result = Try(read[Polygon](singlePointJson))
    assert(result.isSuccess)
    assertEquals(result.get.vertices.length, 1)
  }

  test("Polygon parsing handles malformed coordinate arrays") {
    val malformedJson = """[[1.0], [2.0, 3.0]]"""
    val result = Try(read[Polygon](malformedJson))
    assert(result.isFailure)
  }

  test("Region parsing handles null name") {
    val nullNameJson = """{"name": null, "coordinates": []}"""
    val result = Try(read[Region](nullNameJson))
    assert(result.isFailure)
  }

  test("Region parsing handles empty name") {
    val emptyNameJson = """{"name": "", "coordinates": []}"""
    val result = Try(read[Region](emptyNameJson))
    assert(result.isSuccess)
    assertEquals(result.get.name, "")
  }

  test("Location parsing handles missing name") {
    val missingNameJson = """{"coordinates": [1.0, 2.0]}"""
    val result = Try(read[Location](missingNameJson))
    assert(result.isFailure)
  }

  test("Location parsing handles missing coordinates") {
    val missingCoordsJson = """{"name": "Test"}"""
    val result = Try(read[Location](missingCoordsJson))
    assert(result.isFailure)
  }

  test("RegionMatch serialization preserves snake_case key") {
    val match = RegionMatch("TestRegion", List("Loc1", "Loc2"))
    val json = write(match)
    assert(json.contains("matched_locations"))
    assert(!json.contains("matchedLocations"))
  }

  test("RegionMatch parsing handles empty matched locations") {
    val emptyMatchJson = """{"region": "Test", "matched_locations": []}"""
    val result = Try(read[RegionMatch](emptyMatchJson))
    assert(result.isSuccess)
    assertEquals(result.get.matchedLocations.length, 0)
  }

  test("Complex nested structure round-trip") {
    val complexRegion = Region("Complex", List(
      Polygon(List(Point(0, 0), Point(1, 0), Point(1, 1))),
      Polygon(List(Point(2, 2), Point(3, 2), Point(3, 3), Point(2, 3)))
    ))
    val json = write(complexRegion)
    val restored = read[Region](json)
    assertEquals(restored, complexRegion)
  }

  test("Invalid JSON structure") {
    val invalidStructure = """{"invalid": "structure"}"""
    val result = Try(read[Point](invalidStructure))
    assert(result.isFailure)
  }

  test("Deeply nested array parsing") {
    val deeplyNested = """[[[1.0, 2.0], [3.0, 4.0]], [[5.0, 6.0]]]"""
    val result = Try(read[List[Polygon]](deeplyNested))
    assert(result.isSuccess)
  }