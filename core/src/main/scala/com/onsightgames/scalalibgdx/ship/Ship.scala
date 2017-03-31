package com.onsightgames.scalalibgdx.ship

import com.onsightgames.scalalibgdx.libgdx.{Rectangle, Vector2}
import com.onsightgames.scalalibgdx.MovingEntity

object Ship {

  val Dampening = 0.03f
  val AccelerationScale = 0.3f
}

case class Ship(
  boundingBox  : Rectangle,
  velocity     : Vector2,
  acceleration : Vector2
)
  extends MovingEntity[Ship] {

  import Ship._

  override def calculateNextVelocity() : Vector2 = {
    dampen(velocity + acceleration)
  }

  override protected def update(newBounds: Rectangle, newVelocity: Vector2): Ship = {
    copy(boundingBox = newBounds, velocity = newVelocity)
  }

  def withAcceleration(vector: Vector2): Ship = {
    copy(acceleration = acceleration + vector)
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
