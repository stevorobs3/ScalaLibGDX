package com.onsightgames.scalalibgdx.libgdx

import com.badlogic.gdx.math.{Polygon => LPolygon}

object BasePolygon {
  import scala.language.implicitConversions

  implicit def PolygonToPolygon(polygon : BasePolygon[_]) : LPolygon = {
    val lPoly = new LPolygon(
      polygon
        .vertices
        .flatMap(vertex => Seq(vertex.x, vertex.y))
        .toArray
    )
    lPoly
  }
}

trait BasePolygon[Shape <: BasePolygon[Shape]] {
  val centre   : Vector2
  val vertices : Seq[Vector2]

  def translate(vector : Vector2) : Shape
}
