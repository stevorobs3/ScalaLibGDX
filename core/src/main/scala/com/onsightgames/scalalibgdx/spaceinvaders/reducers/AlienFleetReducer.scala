package com.onsightgames.scalalibgdx.spaceinvaders.reducers

import com.onsightgames.scalalibgdx.Logging
import com.onsightgames.scalalibgdx.experiments.events.CoreGameEvents.Update
import com.onsightgames.scalalibgdx.experiments.{GameObject, Reducer}
import com.onsightgames.scalalibgdx.libgdx.Vector2
import com.onsightgames.scalalibgdx.spaceinvaders.objects.{Alien, AlienFleet}

object AlienFleetReducer extends Reducer[AlienFleet] with Logging {
  val LogId = "AlienFleetReducer"
  val SideGap = 20
  val BottomGap = 200
  val TopGap = 100

  override def reduce : PartialFunction[(GameObject, Any), GameObject] = {
    case (alienFleet : AlienFleet, Update(delta)) =>
      lazy val allAliens      = alienFleet.aliens.flatten
      lazy val rightMostAlien = rightMost(allAliens)
      lazy val leftMostAlien  = leftMost(allAliens)
      lazy val tooFarLeft     = leftMostAlien.leftEdge - SideGap <= 0
      lazy val tooFarRight    = rightMostAlien.rightEdge + SideGap >= AlienFleet.screenWidth
      val movingRight = (alienFleet.movingRight && !tooFarRight) || tooFarLeft
      val horizontalAcceleration = alienFleet.horizontalAcceleration
      val descentAcceleration = alienFleet.descentAcceleration

      val aliens = alienFleet.aliens.map{alienRow =>
        alienRow.map(alien =>
          transformAlien(alien, delta, movingRight, descentAcceleration, horizontalAcceleration)
        )
      }
      alienFleet.copy(
        aliens = aliens,
        movingRight = movingRight
      )
  }

  private def rightMost(aliens: List[Alien]) = {
    find(aliens)((alien1, alien2) => alien1.position.x > alien2.position.x)
  }

  private def leftMost(aliens: List[Alien]) = {
    find(aliens)((alien1, alien2) => alien1.position.x < alien2.position.x)
  }

  private def find(aliens: List[Alien])(f : (Alien, Alien) => Boolean) = {
    aliens.tail.foldLeft(aliens.head){
      case (result, alien) =>
        if (f(alien, result)) alien
        else                  result
    }
  }


  private def transformAlien(
    alien     : Alien,
    delta     : Float,
    moveRight : Boolean,
    descentAcceleration : Float,
    horizontalAcceleration : Float
  ) : Alien = {
    val downDelta = Vector2.Down * descentAcceleration * delta
    val direction = if (moveRight) Vector2.Right else Vector2.Left
    val acrossDelta = direction * horizontalAcceleration * delta
    alien.copy(
      currentTime =  alien.currentTime + delta,
      position = alien.position + downDelta + acrossDelta
    )
  }
}