package utils

import models._
import org.locationtech.jts.geom.{Coordinate, GeometryFactory, Point => JtsPoint, Polygon => JtsPolygon}

object RegionMatcher:

  private val gf = new GeometryFactory()

  def matchLocationsToRegions(regions: List[Region], locations: List[Location]): List[RegionMatch] =
    regions.map { region =>
      val jtsPolygons: List[JtsPolygon] =
        region.geometries.map { poly =>
          val coords =
            (poly.vertices :+ poly.vertices.head)
              .map(p => new Coordinate(p.lon, p.lat))
              .toArray
          gf.createPolygon(coords)
        }

    val matched: List[String] =
      locations
        .filter { loc =>
          val jtsPt: JtsPoint =
            gf.createPoint(new Coordinate(loc.point.lon, loc.point.lat))
          jtsPolygons.exists(poly =>
            poly.contains(jtsPt) || poly.touches(jtsPt)
          )
        }
        .map(_.name)

      RegionMatch(region.name, matched)
    }
