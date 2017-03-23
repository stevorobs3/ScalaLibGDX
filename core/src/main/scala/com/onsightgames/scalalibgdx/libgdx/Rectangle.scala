package com.onsightgames.scalalibgdx.libgdx

import com.badlogic.gdx.math.{Rectangle => LRectangle}

object Rectangle {
  import scala.language.implicitConversions

  implicit def RectangleToRectangle(rectangle : Rectangle) : LRectangle = {
    new LRectangle(
      rectangle.position.x,
      rectangle.position.y,
      rectangle.dimensions.x,
      rectangle.dimensions.y
    )
  }
}

case class Rectangle(position : Vector2, dimensions : Vector2) {

  def translate(vector : Vector2) : Rectangle = {
    copy(position = position + vector)
  }
}