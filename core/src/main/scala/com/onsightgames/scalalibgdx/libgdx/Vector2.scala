package com.onsightgames.scalalibgdx.libgdx

import com.badlogic.gdx.math.{Vector2 => LVector2}

object Vector2 {
  import scala.language.implicitConversions
  lazy val Zero  : Vector2 = Vector2(0f, 0f)
  lazy val Down  : Vector2 = Vector2(0f, -1f)
  lazy val Right : Vector2 = Vector2(1f, 0f)
  lazy val Left  : Vector2 = Vector2(-1f, 0f)

  implicit def Vector2toLVector2(vector2 : Vector2) : LVector2 = new LVector2(vector2.x, vector2.y)
}

case class Vector2(x: Float, y: Float) {
  def +(other : Vector2) : Vector2 = {
   copy(x + other.x, y + other.y)
  }

  def +(other : Float) : Vector2 = {
    copy(x + other, y + other)
  }

  def *(other : Float) : Vector2 = {
    copy(x * other, y * other)
  }

  def -(other : Vector2) : Vector2 = {
    copy(x - other.x, y - other.y)
  }
}