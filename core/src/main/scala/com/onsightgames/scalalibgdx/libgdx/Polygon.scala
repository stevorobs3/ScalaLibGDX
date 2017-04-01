package com.onsightgames.scalalibgdx.libgdx


case class Polygon(centre : Vector2, vertices : Seq[Vector2]) extends BasePolygon[Polygon] {

  override def translate(vector : Vector2) : Polygon = {
    copy(centre + vector, vertices map (_ + vector))
  }

}
