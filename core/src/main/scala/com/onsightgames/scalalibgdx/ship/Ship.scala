package com.onsightgames.scalalibgdx.ship

import com.onsightgames.scalalibgdx.{BoundedEntity, Entity}
import com.onsightgames.scalalibgdx.libgdx.{Rectangle, Vector2}

object Ship {

  val Dampening = 0.03f
  val AccelerationScale = 0.3f
}

case class Ship(
  boundingBox  : Rectangle,
  velocity     : Vector2,
  acceleration : Vector2,
  id           : Entity.Id = Entity.newId
)
  extends BoundedEntity {

  import Ship._

  def update() : Ship = {
    val newVelocity = dampen(velocity + acceleration)

    val newBounds = boundingBox translate newVelocity
    copy(boundingBox = newBounds, velocity = newVelocity)
  }

  def accelerate(vector: Vector2): Ship = {
    copy(acceleration = acceleration + vector)
  }

  def positiveModulus(dividend : Float, divisor : Float) : Float = {
    ((dividend % divisor) + divisor) % divisor
  }

  private def dampen(vector : Vector2) : Vector2 = {
    Vector2(dampen(vector.x), dampen(vector.y))
  }

  private def dampen(scalar : Float) : Float = {
    val unsignedDampened = math.abs(scalar) * (1f - Dampening)
    if (unsignedDampened < Dampening) {
      0
    } else if (scalar > 0) {
      unsignedDampened
    } else {
      -unsignedDampened
    }
  }
}
