package utils

import models._

object RegionMatcher:

  def matchLocationsToRegions(regions: List[Region], locations: List[Location]): List[RegionMatch] =
    regions.map { region =>
      val polygons = region.coordinates.map(_.map {
        case List(lon, lat) => (lon, lat)
        case _ => throw new Exception("Invalid coordinate in region")
      })
      val matched = locations.filter { loc =>
        val point = (loc.coordinates(0), loc.coordinates(1))
        polygons.exists(polygon => GeoUtils.pointInPolygon(point, polygon))
      }.map(_.name)

      RegionMatch(region.name, matched)
    }
