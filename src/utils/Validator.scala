package utils

import models._

object Validator:

  def validateData(regions: List[Region], locations: List[Location]): Either[List[String], Unit] = {
    // Region errors: each polygon must have at least 3 vertices
    val regionErrs = regions.zipWithIndex.flatMap { (region, ridx) =>
      region.geometries.zipWithIndex.collect {
        case (poly, pidx) if poly.vertices.length < 3 =>
          s"Region '${region.name}' polygon #$pidx has fewer than 3 points"
      }
    }

    val locErrs = locations.collect {
      case loc if loc.point.lon < -180 || loc.point.lon > 180 ||
                    loc.point.lat < -90  || loc.point.lat >  90 =>
        s"Location '${loc.name}' has invalid coords: ${loc.point}"
    }

    val allErrs = regionErrs ++ locErrs
    if allErrs.isEmpty then Right(())
    else Left(allErrs)
  }