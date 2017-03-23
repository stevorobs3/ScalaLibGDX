package com.onsightgames.scalalibgdx

import com.badlogic.gdx.Gdx
import com.onsightgames.scalalibgdx.aliens.{Alien, AlienFleet, AlienFleetInitialData, AlienFleetFactory}
import com.onsightgames.scalalibgdx.libgdx.Vector2
import com.onsightgames.scalalibgdx.ship.{Ship, ShipReducer, ShipView}

object Level {
  lazy val One : Level = Level(
    Component[Ship](
      state = Ship(
        dimensions   = Vector2(40f, 40f),
        position     = Vector2(Gdx.graphics.getWidth / 2f, Gdx.graphics.getHeight / 10f),
        velocity     = Vector2.Zero,
        acceleration = Vector2.Zero
      ),
      reducer = ShipReducer,
      views   = Set(ShipView)
    ),
    AlienFleetFactory.buildComponent(AlienFleetInitialData(5, 10, Vector2(10f, -2f), Alien.simple))
  )
}

case class Level(ship: Component[Ship], alienFleet: Component[AlienFleet]) {

  lazy val components : Set[Component[_ <: Entity]] = Set(ship, alienFleet)

}