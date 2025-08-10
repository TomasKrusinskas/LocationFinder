import munit.FunSuite
import upickle.default._
import models._
import utils._
import scala.util.{Try, Success, Failure}

class GeoUtilsEdgeCasesSuite extends FunSuite:
  test("pointInPolygon handles empty polygon") {
    val emptyPolygon = List.empty[(Double, Double)]
    assert(!GeoUtils.pointInPolygon((1.0, 1.0), emptyPolygon))
  }

  test("pointInPolygon handles single point polygon") {
    val singlePoint = List((1.0, 1.0))
    assert(!GeoUtils.pointInPolygon((1.0, 1.0), singlePoint))
  }

  test("pointInPolygon handles two point polygon (degenerate line)") {
    val line = List((0.0, 0.0), (2.0, 2.0))
    assert(!GeoUtils.pointInPolygon((1.0, 1.0), line))
  }

  test("pointInPolygon handles triangle (minimum valid polygon)") {
    val triangle = List((0.0, 0.0), (2.0, 0.0), (1.0, 2.0))
    assert(GeoUtils.pointInPolygon((1.0, 0.5), triangle))
    assert(!GeoUtils.pointInPolygon((0.0, 2.0), triangle))
  }

  test("pointInPolygon handles complex concave polygon") {
    val lShape = List(
      (0.0, 0.0), (2.0, 0.0), (2.0, 1.0), 
      (1.0, 1.0), (1.0, 2.0), (0.0, 2.0)
    )
    assert(GeoUtils.pointInPolygon((0.5, 0.5), lShape))
    assert(GeoUtils.pointInPolygon((1.5, 0.5), lShape))
    assert(GeoUtils.pointInPolygon((0.5, 1.5), lShape))
    assert(!GeoUtils.pointInPolygon((1.5, 1.5), lShape))
  }

  test("pointInPolygon handles polygon with duplicate consecutive points") {
    val polygonWithDuplicates = List(
      (0.0, 0.0), (0.0, 0.0), (0.0, 2.0), (2.0, 2.0), 
      (2.0, 0.0), (2.0, 0.0), (0.0, 0.0)
    )
    assert(GeoUtils.pointInPolygon((1.0, 1.0), polygonWithDuplicates))
  }

  test("pointInPolygon handles very small polygon (precision test)") {
    val tinyPolygon = List(
      (0.0, 0.0), (0.0001, 0.0), (0.0001, 0.0001), (0.0, 0.0001)
    )
    assert(GeoUtils.pointInPolygon((0.00005, 0.00005), tinyPolygon))
    assert(!GeoUtils.pointInPolygon((0.001, 0.001), tinyPolygon))
  }

  test("pointInPolygon handles polygon with collinear points") {
    val polygonWithCollinear = List(
      (0.0, 0.0), (1.0, 0.0), (2.0, 0.0),
      (2.0, 2.0), (0.0, 2.0)
    )
    assert(GeoUtils.pointInPolygon((1.0, 1.0), polygonWithCollinear))
    assert(GeoUtils.pointInPolygon((1.0, 0.0), polygonWithCollinear))
  }

  test("pointInPolygon handles horizontal ray intersection edge case") {
    val polygon = List((0.0, 0.0), (2.0, 1.0), (2.0, 2.0), (0.0, 1.0))
    assert(GeoUtils.pointInPolygon((1.0, 1.0), polygon))
    assert(!GeoUtils.pointInPolygon((-1.0, 1.0), polygon))
  }

  test("pointInPolygon handles polygon with repeated closing vertex") {
    val closedPolygon = List(
      (0.0, 0.0), (2.0, 0.0), (2.0, 2.0), (0.0, 2.0), (0.0, 0.0)
    )
    assert(GeoUtils.pointInPolygon((1.0, 1.0), closedPolygon))
  }

  test("pointInPolygon handles star-shaped polygon") {
    val star = List(
      (1.0, 0.0), (1.2, 0.4), (2.0, 0.4), (1.4, 0.8),
      (1.6, 1.6), (1.0, 1.2), (0.4, 1.6), (0.6, 0.8),
      (0.0, 0.4), (0.8, 0.4)
    )
    assert(GeoUtils.pointInPolygon((1.0, 0.8), star))
    assert(!GeoUtils.pointInPolygon((1.0, 0.2), star))
  }

  test("pointInPolygon handles extreme coordinate values") {
    val extremePolygon = List(
      (-180.0, -90.0), (180.0, -90.0), (180.0, 90.0), (-180.0, 90.0)
    )
    assert(GeoUtils.pointInPolygon((0.0, 0.0), extremePolygon))
    assert(!GeoUtils.pointInPolygon((200.0, 100.0), extremePolygon))
  }

  test("pointInPolygon handles point exactly on vertex of complex polygon") {
    val complexPolygon = List(
      (0.0, 0.0), (1.0, 1.0), (2.0, 0.5), (3.0, 2.0), (0.5, 2.5)
    )
    assert(GeoUtils.pointInPolygon((1.0, 1.0), complexPolygon))
    assert(GeoUtils.pointInPolygon((2.0, 0.5), complexPolygon))
  }

  test("pointInPolygon handles narrow sliver polygon") {
    val sliver = List(
      (0.0, 0.0), (10.0, 0.001), (10.0, 0.002), (0.0, 0.001)
    )
    assert(GeoUtils.pointInPolygon((5.0, 0.0015), sliver))
    assert(!GeoUtils.pointInPolygon((5.0, 0.01), sliver))
  }

  test("pointInPolygon handles self-touching polygon") {
    val bowtie = List(
      (0.0, 0.0), (2.0, 2.0), (2.0, 0.0), (0.0, 2.0)
    )
    assert(GeoUtils.pointInPolygon((1.0, 1.0), bowtie))
  }