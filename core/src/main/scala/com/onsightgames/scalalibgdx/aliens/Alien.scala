package com.onsightgames.scalalibgdx.aliens

import com.onsightgames.scalalibgdx.{Entity, HasBoundingBox}
import com.onsightgames.scalalibgdx.libgdx.{Rectangle, Vector2}

object Alien {

  val SimpleAlienScale = 3f

  def simple : Alien = Alien(Rectangle(Vector2.Zero, Vector2(40f, 40f)), 0f)
}

case class Alien(boundingBox : Rectangle, currentTime : Float)
  extends Entity
  with HasBoundingBox {

  def width     : Float = boundingBox.dimensions.x
  def height    : Float = boundingBox.dimensions.y
  def rightEdge : Float = boundingBox.position.x + width
  def leftEdge  : Float = boundingBox.position.x
}