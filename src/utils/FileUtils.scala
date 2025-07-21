package utils

import scala.io.Source
import scala.util.{Try, Using}

object FileUtils:

  /** Safely read the entire file as a String */
  def loadJsonSafe(path: String): Try[String] =
    Using(Source.fromFile(path)) { source =>
      source.getLines().mkString("\n")
    }
