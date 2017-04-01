package com.onsightgames.scalalibgdx.libgdx

import com.badlogic.gdx.math.{Rectangle => LRectangle}

object Rectangle {
  import scala.language.implicitConversions

  implicit def RectangleToRectangle(rectangle : Rectangle) : LRectangle = {
    new LRectangle(
      rectangle.bottomLeft.x,
      rectangle.bottomLeft.y,
      rectangle.dimensions.x,
      rectangle.dimensions.y
    )
  }
}

case class Rectangle(bottomLeft : Vector2, dimensions : Vector2) extends BasePolygon[Rectangle] {

  override def translate(vector : Vector2) : Rectangle = {
    copy(bottomLeft = bottomLeft + vector)
  }

  lazy val leftEdge   : Float = bottomLeft.x
  lazy val rightEdge  : Float = bottomLeft.x + dimensions.x
  lazy val bottomEdge : Float = bottomLeft.y
  lazy val topEdge    : Float = bottomLeft.y + dimensions.y

  lazy val centre : Vector2 = Vector2(leftEdge + dimensions.x / 2, bottomEdge + dimensions.y / 2)

  lazy val vertices : Seq[Vector2] = Seq(
    bottomLeft,
    bottomLeft.copy(x = rightEdge),
    Vector2(rightEdge, topEdge),
    bottomLeft.copy(y = topEdge)
  )
}