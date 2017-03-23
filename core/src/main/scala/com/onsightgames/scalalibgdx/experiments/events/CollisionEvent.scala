package com.onsightgames.scalalibgdx.experiments.events

import com.onsightgames.scalalibgdx.experiments.CollidableObject

case class CollisionEvent(
  collider  : CollidableObject,
  collider1 : CollidableObject
)
