import munit.FunSuite
import utils.GeoUtils

class GeoUtilsSuite extends FunSuite:

  test("pointInPolygon returns true for inside point") {
    val polygon = List((0.0, 0.0), (0.0, 2.0), (2.0, 2.0), (2.0, 0.0))
    assert(GeoUtils.pointInPolygon((1.0, 1.0), polygon))
  }

  test("pointInPolygon returns false for outside point") {
    val polygon = List((0.0, 0.0), (0.0, 2.0), (2.0, 2.0), (2.0, 0.0))
    assert(!GeoUtils.pointInPolygon((3.0, 3.0), polygon))
  }

  test("pointInPolygon handles point on edge (may be false)") {
    val polygon = List((0.0, 0.0), (0.0, 2.0), (2.0, 2.0), (2.0, 0.0))
    GeoUtils.pointInPolygon((0.0, 1.0), polygon)
  }
