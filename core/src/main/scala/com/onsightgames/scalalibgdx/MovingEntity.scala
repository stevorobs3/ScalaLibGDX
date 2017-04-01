package com.onsightgames.scalalibgdx

import com.onsightgames.scalalibgdx.libgdx.{BasePolygon, Vector2}

trait MovingEntity[EntityType <: MovingEntity[EntityType, Shape], Shape <: BasePolygon[Shape]]
  extends BoundedEntity[Shape] {

  val velocity : Vector2

  val acceleration : Vector2

  def withAcceleration(newAcceleration : Vector2) : EntityType

  def update(timeElapsed : Float) : EntityType = {
    val newVelocity = calculateNextVelocity()
    val newBounds = bounds translate (newVelocity * timeElapsed)
    update(newBounds, newVelocity)
  }

  protected def update(newBounds : Shape, newVelocity : Vector2) : EntityType

  protected def calculateNextVelocity() : Vector2 = {
    velocity + acceleration
  }

}
