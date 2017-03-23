package com.onsightgames.scalalibgdx.aliens

import com.badlogic.gdx.Gdx
import com.onsightgames.scalalibgdx.Component
import com.onsightgames.scalalibgdx.Math.Matrix
import com.onsightgames.scalalibgdx.libgdx.Vector2

object AlienFleetFactory {
  private val BottomGap = 200
  private val TopGap = 100

  def buildComponent(alienFleetData: AlienFleetInitialData) : Component[AlienFleet] = {
    Component(
      state   = buildState(alienFleetData),
      reducer = AlienFleetReducer,
      views   = Set(AlienFleetView)
    )
  }

  def buildState(alienFleetData : AlienFleetInitialData) : AlienFleet = {
    AlienFleet(
      aliens      = setupFormation(buildAliens(alienFleetData), alienFleetData),
      velocity    = alienFleetData.velocity,
      movingRight = true
    )
  }

  private def buildAliens(alienFleetData : AlienFleetInitialData) : Matrix[Alien] = {
    List.fill(alienFleetData.height)(
      List.fill(alienFleetData.width)(alienFleetData.alien)
    )
  }

  private def setupFormation(aliens: Matrix[Alien], alienFleetData : AlienFleetInitialData) = {
    aliens.zipWithIndex.map{
      case (alienRow, rowNum) =>
        alienRow.zipWithIndex.map{
          case (alien, colNum) =>
            val position = calculateAlienPosition(
              rowNum,
              colNum,
              alien.width.toInt,
              alien.height.toInt,
              alienFleetData
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
    fleetData   : AlienFleetInitialData
  ) : Vector2 = {
    val xSpacing = (formationWidth - fleetData.width * alienWidth.toFloat) / fleetData.width
    val ySpacing = (formationHeight - fleetData.height * alienHeight) / fleetData.height
    val x = AlienFleet.SideGap + (colNum + 0.5f) * xSpacing + colNum * alienWidth
    val y = BottomGap + (rowNum + 0.5f) * ySpacing + rowNum * alienHeight
    Vector2(x, y)
  }

  private def screenWidth = Gdx.graphics.getWidth
  private def screenHeight = Gdx.graphics.getHeight
  private def formationWidth = screenWidth - 2 * AlienFleet.SideGap
  private def formationHeight = screenHeight - TopGap - BottomGap
}