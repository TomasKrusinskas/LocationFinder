package utils

import models._
import scala.util.Try

object Validator:

  def validateData(regions: List[Region], locations: List[Location]): Try[Unit] = Try {
    regions.foreach { region =>
      if region.coordinates.exists(_.exists(_.length != 2)) then
        throw new Exception(s"Invalid coordinates in region: ${region.name}")
    }

    locations.foreach { loc =>
      if loc.coordinates.length != 2 then
        throw new Exception(s"Location ${loc.name} has invalid coordinates: ${loc.coordinates}")
    }
  }
