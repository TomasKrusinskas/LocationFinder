import munit.FunSuite
import utils.GeoUtils

class GeoUtilsSuite extends FunSuite:

  test("pointInPolygon returns true for a point inside the polygon") {
    val polygon = List((0.0, 0.0), (0.0, 2.0), (2.0, 2.0), (2.0, 0.0))
    assert(GeoUtils.pointInPolygon((1.0, 1.0), polygon))
  }

  test("pointInPolygon returns false for a point outside the polygon") {
    val polygon = List((0.0, 0.0), (0.0, 2.0), (2.0, 2.0), (2.0, 0.0))
    assert(!GeoUtils.pointInPolygon((3.0, 3.0), polygon))
  }

  test("pointInPolygon returns true for a point exactly on a polygon edge") {
    val polygon = List((0.0, 0.0), (0.0, 2.0), (2.0, 2.0), (2.0, 0.0))
    assert(GeoUtils.pointInPolygon((0.0, 1.0), polygon), "Point on edge should return true")
  }

  test("pointInPolygon returns true for a point exactly on a polygon vertex") {
    val polygon = List((0.0, 0.0), (0.0, 2.0), (2.0, 2.0), (2.0, 0.0))
    assert(GeoUtils.pointInPolygon((0.0, 0.0), polygon), "Point on vertex should return true")
  }

  test("pointInPolygon returns false for a point far outside") {
    val polygon = List((0.0, 0.0), (0.0, 2.0), (2.0, 2.0), (2.0, 0.0))
    assert(!GeoUtils.pointInPolygon((100.0, 100.0), polygon))
  }
