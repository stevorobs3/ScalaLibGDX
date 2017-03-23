package com.onsightgames.scalalibgdx.ship

import com.onsightgames.scalalibgdx.libgdx.Vector2
import com.onsightgames.scalalibgdx.Entity

object Ship {

  val Dampening = 0.03f
  val AccelerationScale = 0.3f
}

case class Ship(
  dimensions   : Vector2,
  position     : Vector2,
  velocity     : Vector2,
  acceleration : Vector2
)
  extends Entity {

  import Ship._

  def update() : Ship = {
    val newVelocity = dampen(velocity + acceleration)

    val newPosition = position + newVelocity
    copy(position = newPosition, velocity = newVelocity)
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
