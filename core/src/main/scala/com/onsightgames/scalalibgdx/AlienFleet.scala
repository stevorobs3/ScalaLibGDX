package com.onsightgames.scalalibgdx

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.onsightgames.scalalibgdx.Math.Matrix

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

  private val aliens   = {
    def alienRow = List.fill(alienFleetData.width)(alienFleetData.alien.copy())
    List.fill(alienFleetData.height)(alienRow)
  }
  private val width = alienFleetData.width
  private val height = alienFleetData.height

  setupFormation(aliens)

  def update(delta : Float) : Unit = {

  }

  def render(batch : SpriteBatch) : Unit = {
    aliens.foreach(row => row.foreach(alien => alien.render(batch)))
  }

  private def setupFormation(aliens: Matrix[Alien]) = {
    aliens.zipWithIndex.foreach{
      case (alienRow, rowNum) =>
        alienRow.zipWithIndex.foreach{
          case (alien, colNum) =>
            val (x, y) = calculateAlienPosition(
              rowNum,
              colNum,
              alien.width.toInt,
              alien.height.toInt
            )
            alien.x = x
            alien.y = y
        }
    }
  }

  private def calculateAlienPosition(
    rowNum      : Int,
    colNum      : Int,
    alienWidth  : Int,
    alienHeight : Int
  ) : (Int, Int)= {
    val xSpacing = (formationWidth - width * alienWidth.toFloat) / width
    val ySpacing = (formationHeight - height * alienHeight) / height
    val x = SideGap + (colNum + 0.5f) * xSpacing + colNum * alienWidth
    val y = BottomGap + (rowNum + 0.5f) * ySpacing + rowNum * alienHeight
    info(s"Alien($x,$y)")
    (x.toInt, y.toInt)
  }

  private def formationWidth = Gdx.graphics.getWidth - 2 * SideGap
  private def formationHeight = Gdx.graphics.getHeight - TopGap - BottomGap
}