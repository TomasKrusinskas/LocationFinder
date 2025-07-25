#!/usr/bin/env scala-cli
//> using lib "com.lihaoyi::upickle:4.1.0"
//> using lib "com.github.scopt::scopt:4.0.1"

import upickle.default.*
import models._
import utils._
import java.nio.file.{Files, Paths}
import scala.util.{Try, Failure, Success}
import scopt.OParser

case class Config(regions: String = "", locations: String = "", output: String = "")

object GeoMatcherApp:

  private val builder = OParser.builder[Config]
  private val parser = {
    import builder._
    OParser.sequence(
      programName("GeoMatcherApp"),
      head("GeoMatcherApp", "1.0"),

      opt[String]("regions")
        .abbr("r")
        .required()
        .valueName("<file>")
        .action((x, c) => c.copy(regions = x))
        .text("Path to regions.json file"),

      opt[String]("locations")
        .abbr("l")
        .required()
        .valueName("<file>")
        .action((x, c) => c.copy(locations = x))
        .text("Path to locations.json file"),

      opt[String]("output")
        .abbr("o")
        .required()
        .valueName("<file>")
        .action((x, c) => c.copy(output = x))
        .text("Path where results.json will be written"),

      help("help").text("Print this help message and exit")
    )
  }

  def main(args: Array[String]): Unit =
    OParser.parse(parser, args, Config()) match
      case Some(config) =>
        val result = for
          regionsJson   <- FileUtils.loadJsonSafe(config.regions)
          locationsJson <- FileUtils.loadJsonSafe(config.locations)
          regions       <- Try(read[List[Region]](regionsJson))
          locations     <- Try(read[List[Location]](locationsJson))
          _             <- Validator.validateData(regions, locations)
        yield
          val results    = RegionMatcher.matchLocationsToRegions(regions, locations)
          val outputJson = write(results, indent = 2)
          Files.write(Paths.get(config.output), outputJson.getBytes)
          println(s"Results written to ${config.output}")

        result match
          case Failure(ex) => Console.err.println(s"Error: ${ex.getMessage}")
          case Success(_)  => ()

      case _ =>
        ()