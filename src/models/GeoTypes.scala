package models

import upickle.default.{ReadWriter, macroRW, readwriter}
import ujson.Value

case class Point(lon: Double, lat: Double)
object Point:
  implicit val rw: ReadWriter[Point] =
    readwriter[Value].bimap[Point](
      p => ujson.Arr(p.lon, p.lat),
      json =>
        val arr = json.arr
        Point(arr(0).num, arr(1).num)
    )

case class Polygon(vertices: List[Point])
object Polygon:
  implicit val rw: ReadWriter[Polygon] =
    readwriter[Value].bimap[Polygon](
      poly => ujson.Arr.from(
        poly.vertices.map(p => ujson.Arr(p.lon, p.lat))
      ),
      json =>
        val verts = json.arr.toList.map { coordJson =>
          val arr = coordJson.arr
          Point(arr(0).num, arr(1).num)
        }
        Polygon(verts)
    )