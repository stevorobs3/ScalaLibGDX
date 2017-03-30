package com.onsightgames.scalalibgdx.aliens

import com.badlogic.gdx.Gdx
import com.onsightgames.scalalibgdx.Entity
import com.onsightgames.scalalibgdx.Math.Matrix
import com.onsightgames.scalalibgdx.libgdx.Vector2

object AlienFleet {
  val SideGap = 20
}

case class AlienFleet(
  aliens      : Matrix[Alien],
  movingRight : Boolean,
  velocity    : Vector2,
  id          : Entity.Id = Entity.newId
)
  extends Entity {
  import AlienFleet._

  def update(timeElapsed : Float) : AlienFleet = {
    lazy val allAliens      = aliens.flatten
    lazy val rightMostAlien = rightMost(allAliens)
    lazy val leftMostAlien  = leftMost(allAliens)
    lazy val tooFarLeft     = leftMostAlien.boundingBox.leftEdge - SideGap <= 0
    lazy val tooFarRight    = rightMostAlien.boundingBox.rightEdge + SideGap >= Gdx.graphics.getWidth
    val newMovingRight = (movingRight && !tooFarRight) || tooFarLeft

    val newAliens = aliens.map { alienRow =>
      alienRow.map(alien => transformAlien(alien, timeElapsed, movingRight))
    }

    copy(movingRight = newMovingRight, aliens = newAliens)
  }

  private def rightMost(aliens: List[Alien]) = {
    find(aliens)((alien1, alien2) => alien1.isRightOf(alien2))
  }

  private def leftMost(aliens: List[Alien]) = {
    find(aliens)((alien1, alien2) => alien1.isLeftOf(alien2))
  }

  private def find(aliens: List[Alien])(f : (Alien, Alien) => Boolean) = {
    aliens.tail.foldLeft(aliens.head){
      case (result, alien) =>
        if (f(alien, result)) alien
        else                  result
    }
  }

  private def transformAlien(alien : Alien, timeElapsed : Float, moveRight : Boolean) : Alien = {
    alien.copy(
      currentTime = alien.currentTime + timeElapsed,
      boundingBox = alien.boundingBox.translate(calculatePositionChange(timeElapsed, moveRight))
    )
  }

  def calculatePositionChange(timeElapsed : Float, movingRight : Boolean) : Vector2 = {
    val positionChange = velocity * timeElapsed
    if ((movingRight && positionChange.x < 0) || (!movingRight && positionChange.x > 0)) {
      Vector2(positionChange.x * -1f, positionChange.y)
    }
    else {
      positionChange
    }
  }

}
