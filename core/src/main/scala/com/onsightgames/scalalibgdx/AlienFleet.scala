package com.onsightgames.scalalibgdx

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.onsightgames.scalalibgdx.Math.Matrix
import com.onsightgames.scalalibgdx.libgdx.Vector2

object AlienFleet {
  val SideGap = 20
  val BottomGap = 200
  val TopGap = 100
}

class AlienFleet(
  alienFleetData : AlienFleetData
) extends HasLogger {
  import AlienFleet._
  override val LogId: String = "AlienFleet"

  private var aliens = {
    val alienRow = List.fill(alienFleetData.width)(alienFleetData.alien)
    List.fill(alienFleetData.height)(alienRow)
  }
  private val width = alienFleetData.width
  private val height = alienFleetData.height

  private var movingRight = true

  aliens = setupFormation(aliens)

 def update(delta : Float) : Unit = {
    lazy val allAliens      = aliens.flatten
    lazy val rightMostAlien = rightMost(allAliens)
    lazy val leftMostAlien  = leftMost(allAliens)
    lazy val tooFarLeft     = leftMostAlien.leftEdge - SideGap <= 0
    lazy val tooFarRight    = rightMostAlien.rightEdge + SideGap >= screenWidth
    movingRight = (movingRight && !tooFarRight) || tooFarLeft

    aliens = aliens.map{alienRow =>
      alienRow.map(alien => transformAlien(alien, delta, movingRight))
    }
  }

  def render(batch : SpriteBatch) : Unit = {
    aliens.foreach(row => row.foreach(alien => alien.render(batch)))
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



  private def setupFormation(aliens: Matrix[Alien]) = {
    aliens.zipWithIndex.map{
      case (alienRow, rowNum) =>
        alienRow.zipWithIndex.map{
          case (alien, colNum) =>
            val position = calculateAlienPosition(
              rowNum,
              colNum,
              alien.width.toInt,
              alien.height.toInt
            )
            alien.copy(position = position)
        }
    }
  }

  private def transformAlien(
    alien     : Alien,
    delta     : Float,
    moveRight : Boolean
  ) : Alien = {
    val downDelta = Vector2.Down * alienFleetData.descentAcceleration * delta
    val direction = if (moveRight) Vector2.Right else Vector2.Left
    val acrossDelta = direction * alienFleetData.horizontalAcceleration * delta
    alien.copy(
      currentTime =  alien.currentTime + delta,
      position = alien.position + downDelta + acrossDelta
    )
  }

  private def calculateAlienPosition(
    rowNum      : Int,
    colNum      : Int,
    alienWidth  : Int,
    alienHeight : Int
  ) : Vector2 = {
    val xSpacing = (formationWidth - width * alienWidth.toFloat) / width
    val ySpacing = (formationHeight - height * alienHeight) / height
    val x = SideGap + (colNum + 0.5f) * xSpacing + colNum * alienWidth
    val y = BottomGap + (rowNum + 0.5f) * ySpacing + rowNum * alienHeight
    Vector2(x, y)
  }

  private def screenWidth = Gdx.graphics.getWidth
  private def screenHeight = Gdx.graphics.getHeight
  private def formationWidth = screenWidth - 2 * SideGap
  private def formationHeight = screenHeight - TopGap - BottomGap
}