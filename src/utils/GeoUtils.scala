package utils

object GeoUtils:
  def pointInPolygon(point: (Double, Double), polygon: List[(Double, Double)]): Boolean =
    val (x, y) = point
    
    if polygon.length < 3 then return false
    
    // Check if point is exactly on a vertex
    if polygon.exists { case (vx, vy) => math.abs(x - vx) < 1e-10 && math.abs(y - vy) < 1e-10 } then
      return true
    
    // Check if point is on any edge
    for i <- polygon.indices do
      val j = (i + 1) % polygon.length
      if isPointOnLineSegment(point, polygon(i), polygon(j)) then
        return true
    
    // Ray casting algorithm with proper vertex handling
    var intersections = 0
    for i <- polygon.indices do
      val j = (i + 1) % polygon.length
      if rayIntersectsSegment(point, polygon(i), polygon(j)) then
        intersections += 1
    
    intersections % 2 == 1
  
  private def isPointOnLineSegment(
    point: (Double, Double), 
    start: (Double, Double), 
    end: (Double, Double)
  ): Boolean =
    val (px, py) = point
    val (x1, y1) = start
    val (x2, y2) = end
    val epsilon = 1e-10
    
    val minX = math.min(x1, x2) - epsilon
    val maxX = math.max(x1, x2) + epsilon
    val minY = math.min(y1, y2) - epsilon  
    val maxY = math.max(y1, y2) + epsilon
    
    if px < minX || px > maxX || py < minY || py > maxY then
      return false
    
    val crossProduct = (x2 - x1) * (py - y1) - (y2 - y1) * (px - x1)
    math.abs(crossProduct) < epsilon
  
  private def rayIntersectsSegment(
    point: (Double, Double), 
    segStart: (Double, Double), 
    segEnd: (Double, Double)
  ): Boolean =
    val (px, py) = point
    val (x1, y1) = segStart
    val (x2, y2) = segEnd
    
    val ((xa, ya), (xb, yb)) = if y1 <= y2 then ((x1, y1), (x2, y2)) else ((x2, y2), (x1, y1))
    
    if py < ya || py >= yb then
      return false
    
    if py == ya then
      return yb > py
    
    val intersectionX = if yb == ya then
      math.max(xa, xb)
    else
      xa + (py - ya) * (xb - xa) / (yb - ya)
    
    intersectionX > px