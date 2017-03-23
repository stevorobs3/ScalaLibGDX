package com.onsightgames.scalalibgdx.aliens

import com.onsightgames.scalalibgdx.Component
import com.onsightgames.scalalibgdx.Math.Matrix
import com.onsightgames.scalalibgdx.libgdx.{Rectangle, Vector2}

object AlienFleetFactory {
  private val BottomGap = 200
  private val TopGap = 100

  def buildComponent(
    alienFleetData : AlienFleetInitialData,
    screen         : Rectangle
  ) : Component[AlienFleet] = {
    Component(
      state   = buildState(alienFleetData, screen),
      reducer = AlienFleetReducer,
      views   = Set(AlienFleetView)
    )
  }

  def buildState(alienFleetData : AlienFleetInitialData, screen : Rectangle) : AlienFleet = {
    AlienFleet(
      aliens      = setupFormation(buildAliens(alienFleetData), alienFleetData, screen),
      velocity    = alienFleetData.velocity,
      movingRight = true
    )
  }

  private def buildAliens(alienFleetData : AlienFleetInitialData) : Matrix[Alien] = {
    List.fill(alienFleetData.height)(
      List.fill(alienFleetData.width)(alienFleetData.alien)
    )
  }

  private def setupFormation(
    aliens         : Matrix[Alien],
    alienFleetData : AlienFleetInitialData,
    screen         : Rectangle
  ) = {
    aliens.zipWithIndex.map{
      case (alienRow, rowNum) =>
        alienRow.zipWithIndex.map{
          case (alien, colNum) =>
            val position = calculateAlienPosition(
              rowNum,
              colNum,
              alien.boundingBox.width.toInt,
              alien.boundingBox.height.toInt,
              alienFleetData,
              screen
            )
            alien.copy(boundingBox = alien.boundingBox.copy(bottomLeft = position))
        }
    }
  }

  private def calculateAlienPosition(
    rowNum      : Int,
    colNum      : Int,
    alienWidth  : Int,
    alienHeight : Int,
    fleetData   : AlienFleetInitialData,
    screen      : Rectangle
  ) : Vector2 = {
    val xSpacing = (formationWidth(screen) - fleetData.width * alienWidth.toFloat) / fleetData.width
    val ySpacing = (formationHeight(screen) - fleetData.height * alienHeight) / fleetData.height
    val x = AlienFleet.SideGap + (colNum + 0.5f) * xSpacing + colNum * alienWidth
    val y = BottomGap + (rowNum + 0.5f) * ySpacing + rowNum * alienHeight
    Vector2(x, y)
  }

  private def formationWidth(screen : Rectangle) = screen.width - 2 * AlienFleet.SideGap
  private def formationHeight(screen : Rectangle) = screen.height - TopGap - BottomGap
}