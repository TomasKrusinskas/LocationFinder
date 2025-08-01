#!/usr/bin/env scala-cli
//> using lib "com.lihaoyi::upickle:4.1.0"
//> using lib "com.github.scopt::scopt:4.0.1"
//> using lib "org.slf4j:slf4j-api:1.7.36"
//> using lib "ch.qos.logback:logback-classic:1.2.11"

import upickle.default.*
import models._
import utils._
import java.nio.file.{Files, Paths}
import scala.util.{Try, Failure, Success}
import scopt.OParser
import org.slf4j.LoggerFactory

case class Config(
  regions: String = "",
  locations: String = "",
  output: String = "",
  demo: Boolean = false
)

object GeoMatcherApp:

  private val logger = LoggerFactory.getLogger(getClass)

  private val builder = OParser.builder[Config]
  private val parser = {
    import builder._
    OParser.sequence(
      programName("GeoMatcherApp"),
      head("GeoMatcherApp", "1.0"),

      opt[String]("regions")
        .abbr("r")
        .optional()
        .valueName("<file>")
        .action((x, c) => c.copy(regions = x))
        .text("Path to regions.json file"),

      opt[String]("locations")
        .abbr("l")
        .optional()
        .valueName("<file>")
        .action((x, c) => c.copy(locations = x))
        .text("Path to locations.json file"),

      opt[String]("output")
        .abbr("o")
        .optional()
        .valueName("<file>")
        .action((x, c) => c.copy(output = x))
        .text("Path where results.json will be written"),

      opt[Unit]("demo")
        .action((_, c) => c.copy(demo = true))
        .text("Run with built-in demo data (no files required)"),

      help("help").text("Print this help message and exit")
    )
  }

  def loadJsonEither(path: String): Either[String, String] =
    FileUtils.loadJsonSafe(path).toEither.left.map(_.getMessage)

  def main(args: Array[String]): Unit =
    OParser.parse(parser, args, Config()) match
      case Some(config) if config.demo =>
        logger.info("Running GeoMatcherApp in DEMO mode (no files needed)")
        val sampleRegions = List(
          Region("DemoBox", List(
            Polygon(List(
              Point(0, 0), Point(0, 2), Point(2, 2), Point(2, 0), Point(0, 0)
            ))
          ))
        )
        val sampleLocations = List(
          Location("Inside", Point(1, 1)),
          Location("OnEdge", Point(0, 1)),
          Location("Outside", Point(5, 5))
        )
        val results = RegionMatcher.matchLocationsToRegions(sampleRegions, sampleLocations)
        val json = write(results, indent = 2)
        if (config.output.nonEmpty) {
          Files.write(Paths.get(config.output), json.getBytes)
          println(s"Demo results written to ${config.output}")
        } else {
          println(json)
        }

      case Some(config) =>
        logger.info(
          "Starting GeoMatcherApp with regions={}, locations={}, output={}",
          config.regions,
          config.locations,
          config.output
        )

        val result: Either[String, Unit] = for {
          regionsJson   <- loadJsonEither(config.regions)
          locationsJson <- loadJsonEither(config.locations)
          regions       <- try { Right(read[List[Region]](regionsJson)) } catch { case ex: Exception => Left(ex.getMessage) }
          locations     <- try { Right(read[List[Location]](locationsJson)) } catch { case ex: Exception => Left(ex.getMessage) }
          _             <- Validator.validateData(regions, locations)
                              .left.map(_.mkString("; "))
        } yield {
          val results    = RegionMatcher.matchLocationsToRegions(regions, locations)
          val outputJson = write(results, indent = 2)
          if (config.output.nonEmpty) {
            Files.write(Paths.get(config.output), outputJson.getBytes)
            logger.info(s"Results written to ${config.output}")
          } else {
            println(outputJson)
          }
        }

        result match
          case Left(err) => logger.error("Error during processing: {}", err)
          case Right(_)  => ()

      case _ => ()

