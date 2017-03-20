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

  aliens = setupFormation(aliens)

  def update(delta : Float) : Unit = {
    aliens = aliens.map{alienRow =>
      alienRow.map(alien => transformAlien(alien, delta))
    }
  }

  def render(batch : SpriteBatch) : Unit = {
    aliens.foreach(row => row.foreach(alien => alien.render(batch)))
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

  private def transformAlien(alien : Alien, delta : Float) : Alien = {
    alien.copy(currentTime =  alien.currentTime + delta)
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

  private def formationWidth = Gdx.graphics.getWidth - 2 * SideGap
  private def formationHeight = Gdx.graphics.getHeight - TopGap - BottomGap
}