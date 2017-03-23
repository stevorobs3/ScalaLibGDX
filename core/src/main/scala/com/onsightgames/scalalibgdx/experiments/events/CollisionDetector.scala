package com.onsightgames.scalalibgdx.experiments.events

import com.onsightgames.scalalibgdx.experiments.CollidableObject

trait CollisionDetector {
  val collisionsToCheckFor : List[(Any,Any)]
  def checkForCollisions(collidableObjects : Iterator[CollidableObject] ) : Iterator[CollisionEvent]
}
