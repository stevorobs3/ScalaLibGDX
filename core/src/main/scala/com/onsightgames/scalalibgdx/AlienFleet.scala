package com.onsightgames.scalalibgdx

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch

object AlienFleet {
  val SideGap = 20
  val BottomGap = 200
  val TopGap = 100
}

class AlienFleet extends HasLogger {
  import AlienFleet._
  override val LogId: String = "AlienFormation"

  private val alienTexture = new Texture("alien.png")
  private val FleetHeight = 5
  private val FleetWidth = 10
  private val aliens = List.fill(FleetHeight)(List.fill(FleetWidth)(new Alien(alienTexture)))
  setupFormation(aliens)



  def update(delta : Float) : Unit = {

  }

  def render(batch : SpriteBatch) : Unit = {
    aliens.foreach(row => row.foreach(alien => alien.render(batch)))
  }

  private def setupFormation(aliens: List[List[Alien]]) = {
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
    val xSpacing = (formationWidth - FleetWidth * alienWidth.toFloat) / FleetWidth
    val ySpacing = (formationHeight - FleetHeight * alienHeight) / FleetHeight
    val x = SideGap + (colNum + 0.5f) * xSpacing + colNum * alienWidth
    val y = BottomGap + (rowNum + 0.5f) * ySpacing + rowNum * alienHeight
    (x.toInt, y.toInt)
  }

  private def formationWidth = Gdx.graphics.getWidth - 2 * SideGap
  private def formationHeight = Gdx.graphics.getHeight - TopGap - BottomGap
}