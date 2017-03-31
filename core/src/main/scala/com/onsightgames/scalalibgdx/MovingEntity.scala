package com.onsightgames.scalalibgdx

import com.onsightgames.scalalibgdx.libgdx.{Rectangle, Vector2}

trait MovingEntity[EntityType <: MovingEntity[EntityType]] extends BoundedEntity {

  val velocity : Vector2

  val acceleration : Vector2

  def withAcceleration(newAcceleration : Vector2) : EntityType

  def update() : EntityType = {
    val newVelocity = calculateNextVelocity()
    val newBounds = boundingBox translate newVelocity
    update(newBounds, newVelocity)
  }

  protected def update(newBounds : Rectangle, newVelocity : Vector2) : EntityType

  protected def calculateNextVelocity() : Vector2 = {
    velocity + acceleration
  }

}
