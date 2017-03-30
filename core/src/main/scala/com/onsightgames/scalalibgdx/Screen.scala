package com.onsightgames.scalalibgdx

import com.onsightgames.scalalibgdx.libgdx.{Rectangle, Vector2}


case class Screen(
  width  : Int,
  height : Int
) {
  val origin : Vector2 = Vector2.Zero

  val rectangle : Rectangle = Rectangle(origin, Vector2(width.toFloat, height.toFloat))
}