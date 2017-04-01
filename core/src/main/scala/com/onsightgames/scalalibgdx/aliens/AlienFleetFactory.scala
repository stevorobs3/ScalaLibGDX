package com.onsightgames.scalalibgdx.aliens

import com.onsightgames.scalalibgdx.{Component, Entity}
import com.onsightgames.scalalibgdx.Math.Matrix
import com.onsightgames.scalalibgdx.libgdx.{Rectangle, Vector2}

object AlienFleetFactory {
  def buildComponent(alienFleetData : AlienFleetInitialData) : Component[AlienFleet] = {
    Component(
      state         = buildState(alienFleetData),
      reducer       = AlienFleetReducer,
      views         = Set(AlienFleetView),
      mapToEntities = mapFleetToEntities
    )
  }

  def buildState(alienFleetData : AlienFleetInitialData) : AlienFleet = {
    AlienFleet(
      aliens       = setupFormation(buildAliens(alienFleetData), alienFleetData),
      boundingBox  = alienFleetData.boundingBox,
      velocity     = alienFleetData.velocity,
      acceleration = alienFleetData.acceleration
    )
  }

  private def buildAliens(alienFleetData : AlienFleetInitialData) : Matrix[Alien] = {
    List.fill(alienFleetData.height)(
      List.fill(alienFleetData.width)(alienFleetData.alien)
    )
  }

  private def setupFormation(aliens : Matrix[Alien], alienFleetData : AlienFleetInitialData) = {
    aliens.zipWithIndex.map{
      case (alienRow, rowNum) =>
        alienRow.zipWithIndex.map{
          case (alien, colNum) =>
            val position = calculateAlienPosition(
              rowNum,
              colNum,
              alien.boundingBox,
              alienFleetData
            )
            alien.copy(boundingBox = alien.boundingBox.copy(bottomLeft = position))
        }
    }
  }

  private def calculateAlienPosition(
    rowNum      : Int,
    colNum      : Int,
    alienBounds : Rectangle,
    fleetData   : AlienFleetInitialData
  ) : Vector2 = {
    val fleetBounds = fleetData.boundingBox
    val xSpacing = (fleetBounds.width - fleetData.width * alienBounds.width) / fleetData.width
    val ySpacing = (fleetBounds.height - fleetData.height * alienBounds.height) / fleetData.height
    val x = fleetBounds.bottomLeft.x + (colNum + 0.5f) * xSpacing + colNum * alienBounds.width
    val y = fleetBounds.bottomLeft.y + (rowNum + 0.5f) * ySpacing + rowNum * alienBounds.height
    Vector2(x, y)
  }

  private def mapFleetToEntities(fleet : AlienFleet) : Iterable[Entity] = {
    ((Nil : Seq[Entity]) :+ fleet) ++ fleet.aliens.flatten
  }

}