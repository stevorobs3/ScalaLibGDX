package com.onsightgames.scalalibgdx.aliens

import com.onsightgames.scalalibgdx.Entity
import com.onsightgames.scalalibgdx.libgdx.Vector2

object Alien {

  val SimpleAlienScale = 3f

  def simple : Alien = Alien(Vector2.Zero, Vector2(40f, 40f), 0f)
}

case class Alien(
  position        : Vector2,
  dimensions      : Vector2,
  currentTime     : Float
) extends Entity {

  def width     : Float = dimensions.x
  def height    : Float = dimensions.y
  def rightEdge : Float = position.x + width
  def leftEdge  : Float = position.x
}