package com.onsightgames.scalalibgdx.aliens

import com.onsightgames.scalalibgdx.{BoundedEntity, Entity}
import com.onsightgames.scalalibgdx.libgdx.{Rectangle, Vector2}

object Alien {

  val SimpleAlienScale = 3f

  def simple : Alien = Alien(Rectangle(Vector2.Zero, Vector2(40f, 40f)), 0f)
}

case class Alien(
  boundingBox : Rectangle,
  currentTime : Float,
  id          : Entity.Id = Entity.newId
) extends BoundedEntity