package models

import upickle.default.{ReadWriter, readwriter}
import upickle.implicits.key
import ujson.Value

case class Region(
  name: String,
  @key("coordinates") geometries: List[Polygon]
) {
  if name == null then
    throw new IllegalArgumentException("Region name cannot be null")
}

object Region:
  implicit val rw: ReadWriter[Region] =
    readwriter[Value].bimap[Region](
      region => ujson.Obj(
        "name" -> ujson.Str(region.name),
        "coordinates" -> ujson.Arr.from(
          region.geometries.map { poly =>
            ujson.Arr.from(poly.vertices.map(p => ujson.Arr(p.lon, p.lat)))
          }
        )
      ),
      json => {
        val nameValue = json("name")
        if nameValue.isNull then
          throw new IllegalArgumentException("Region name cannot be null")
        Region(
          name = nameValue.str,
          geometries = json("coordinates").arr.toList.map { polyJson =>
            val verts = polyJson.arr.toList.map { coordJson =>
              val arr = coordJson.arr
              Point(arr(0).num, arr(1).num)
            }
            Polygon(verts)
          }
        )
      }
    )