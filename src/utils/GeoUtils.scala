package utils

object GeoUtils:
  def pointInPolygon(point: (Double, Double), polygon: List[(Double, Double)]): Boolean =
    val (x, y) = point
    var inside = false
    var j = polygon.length - 1
    for i <- polygon.indices do
      val (xi, yi) = polygon(i)
      val (xj, yj) = polygon(j)
      val denom = yj - yi
      val intersect =
        (yi > y) != (yj > y) &&
        (denom != 0) && (x < (xj - xi) * (y - yi) / denom + xi)
      if intersect then inside = !inside
      j = i
    inside
