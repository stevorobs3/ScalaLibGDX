package com.onsightgames.scalalibgdx.spaceinvaders.objects

import java.util.UUID

import com.badlogic.gdx.Gdx
import com.onsightgames.scalalibgdx.Math.Matrix
import com.onsightgames.scalalibgdx.experiments.GameObject
import com.onsightgames.scalalibgdx.libgdx.Vector2

object AlienFleet {
  val SideGap = 20
  val BottomGap = 200
  val TopGap = 100

  def apply(
    width                  : Int,
    height                 : Int,
    descentAcceleration    : Float,
    horizontalAcceleration : Float,
    alien                  : Alien
  ) : AlienFleet = {
    val aliens = {
      val alienRow = List.fill(width)(alien)
      setupFormation(width, height, List.fill(height)(alienRow))
    }
    AlienFleet(
      aliens,
      movingRight = true,
      descentAcceleration,
      horizontalAcceleration,
      UUID.randomUUID()
    )
  }

  private def setupFormation(width : Int, height : Int, aliens: Matrix[Alien]) = {
    aliens.zipWithIndex.map{
      case (alienRow, rowNum) =>
        alienRow.zipWithIndex.map{
          case (alien, colNum) =>
            val position = calculateAlienPosition(
              rowNum,
              colNum,
              alien.width.toInt,
              alien.height.toInt,
              width,
              height
            )
            alien.copy(position = position)
        }
    }
  }

  private def calculateAlienPosition(
    rowNum      : Int,
    colNum      : Int,
    alienWidth  : Int,
    alienHeight : Int,
    width       : Int,
    height      : Int
  ) : Vector2 = {
    val xSpacing = (formationWidth - width * alienWidth.toFloat) / width
    val ySpacing = (formationHeight - height * alienHeight) / height
    val x = SideGap + (colNum + 0.5f) * xSpacing + colNum * alienWidth
    val y = BottomGap + (rowNum + 0.5f) * ySpacing + rowNum * alienHeight
    Vector2(x, y)
  }

  def screenWidth     : Int = Gdx.graphics.getWidth
  def screenHeight    : Int = Gdx.graphics.getHeight
  def formationWidth  : Int = screenWidth - 2 * SideGap
  def formationHeight : Int = screenHeight - TopGap - BottomGap
}

case class AlienFleet private (
  aliens                 : Matrix[Alien],
  movingRight            : Boolean,
  descentAcceleration    : Float, // pixels per second^2
  horizontalAcceleration : Float, // pixels per second^2
  id                     : UUID
) extends GameObject