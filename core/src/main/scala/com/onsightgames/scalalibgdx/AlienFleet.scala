package com.onsightgames.scalalibgdx

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch

class AlienFleet extends HasLogger {

  override val LogId: String = "AlienFormation"

  private val alienTexture = new Texture("alien.png")
  private val FleetHeight = 5
  private val FleetWidth = 11
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
              alienTexture.getWidth,
              alienTexture.getHeight
            )
            alien.x = x
            alien.y = y
        }
    }
  }

  private def calculateAlienPosition(
    rowNum        : Int,
    colNum        : Int,
    textureWidth  : Int,
    textureHeight : Int
  ) : (Int, Int)= {
    val screenWidth = Gdx.graphics.getWidth
    val screenHeight = Gdx.graphics.getHeight
    val xSpacing = (screenWidth - FleetWidth * textureWidth.toFloat) / FleetWidth
    val ySpacing = (screenHeight - FleetHeight * textureHeight) / FleetHeight
    val x = (colNum + 0.5f) * xSpacing + colNum * textureWidth
    val y = (rowNum + 0.5f) * ySpacing + rowNum * textureHeight
    (x.toInt, y.toInt)
  }
}