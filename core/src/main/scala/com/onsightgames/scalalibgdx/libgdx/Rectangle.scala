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

case class Rectangle(bottomLeft : Vector2, dimensions : Vector2) {

  def translate(vector : Vector2) : Rectangle = {
    copy(bottomLeft = bottomLeft + vector)
  }

  def leftEdge   : Float = bottomLeft.x
  def rightEdge  : Float = bottomLeft.x + dimensions.x
  def bottomEdge : Float = bottomLeft.y
  def topEdge    : Float = bottomLeft.y + dimensions.y

  def center    : Vector2 = bottomLeft + (dimensions / 2)
  def topCenter : Vector2 = center + Vector2(0, dimensions.y / 2)
}