import upickle.default.*
import models._
import utils._
import java.nio.file.{Files, Paths}
import scala.util.{Try, Failure, Success}

object GeoMatcherApp:

  def main(args: Array[String]): Unit =
    val argMap = args.flatMap { arg =>
      arg.stripPrefix("--").split("=", 2) match
        case Array(k, v) => Some(k -> v)
        case _ => None
    }.toMap

    (argMap.get("regions"), argMap.get("locations"), argMap.get("output")) match
      case (Some(regionsPath), Some(locationsPath), Some(outputPath)) =>
        val result = for
          regionsJson <- FileUtils.loadJsonSafe(regionsPath)
          locationsJson <- FileUtils.loadJsonSafe(locationsPath)
          regions <- Try(read[List[Region]](regionsJson))
          locations <- Try(read[List[Location]](locationsJson))
          _ <- Validator.validateData(regions, locations)
        yield
          val results = RegionMatcher.matchLocationsToRegions(regions, locations)
          val outputJson = write(results, indent = 2)
          Files.write(Paths.get(outputPath), outputJson.getBytes)
          println(s"Results written to $outputPath")

        result match
          case Failure(ex) =>
            Console.err.println(s"Error: ${ex.getMessage}")
          case Success(_) => ()

      case _ =>
        Console.err.println("Missing arguments. Usage:\n--regions=... --locations=... --output=...")
