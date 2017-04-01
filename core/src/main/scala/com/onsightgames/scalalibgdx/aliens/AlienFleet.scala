package com.onsightgames.scalalibgdx.aliens

import com.onsightgames.scalalibgdx.MovingEntity
import com.onsightgames.scalalibgdx.Math.Matrix
import com.onsightgames.scalalibgdx.libgdx.{Rectangle, Vector2}

case class AlienFleet(
  aliens       : Matrix[Alien],
  bounds       : Rectangle,
  velocity     : Vector2,
  acceleration : Vector2
) extends MovingEntity[AlienFleet, Rectangle] {

  override def update(timeElapsed : Float) : AlienFleet = {
    val movement = calculateNextVelocity() * timeElapsed

    val newAliens = aliens.map { alienRow =>
      alienRow.map(alien => transformAlien(alien, timeElapsed, movement))
    }

    super.update(timeElapsed).copy(aliens = newAliens)
  }

  override def withAcceleration(newAcceleration : Vector2): AlienFleet = {
    copy(acceleration = newAcceleration)
  }

  override protected def update(newBounds : Rectangle, newVelocity : Vector2) : AlienFleet = {
    copy(bounds = newBounds, velocity = newVelocity)
  }

  private def transformAlien(alien : Alien, timeElapsed : Float, movement : Vector2) : Alien = {
    alien.copy(
      currentTime = alien.currentTime + timeElapsed,
      bounds = alien.bounds.translate(movement)
    )
  }
}
