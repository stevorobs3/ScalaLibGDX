package com.onsightgames.scalalibgdx.aliens

import com.onsightgames.scalalibgdx.Collidable
import com.onsightgames.scalalibgdx.libgdx.{Rectangle, Vector2}

object Alien {

  val SimpleAlienScale = 3f

  def simple : Alien = Alien(Rectangle(Vector2.Zero, Vector2(40f, 40f)), 0f)
}

case class Alien(boundingBox : Rectangle, currentTime : Float)
  extends Collidable {

  val collisionLayer = Collidable.Layers.Alien
}