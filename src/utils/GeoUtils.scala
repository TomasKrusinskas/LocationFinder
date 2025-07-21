package utils

object GeoUtils:
  def pointInPolygon(point: (Double, Double), polygon: List[(Double, Double)]): Boolean =
    val (x, y) = point
    var inside = false
    var j = polygon.length - 1
    for i <- polygon.indices do
      val (xi, yi) = polygon(i)
      val (xj, yj) = polygon(j)
      val intersect = (yi > y) != (yj > y) &&
        x < (xj - xi) * (y - yi) / (yj - yi + 1e-10) + xi
      if intersect then inside = !inside
      j = i
    inside
