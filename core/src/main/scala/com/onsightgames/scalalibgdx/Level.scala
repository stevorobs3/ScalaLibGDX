package com.onsightgames.scalalibgdx

import com.onsightgames.scalalibgdx.aliens._
import com.onsightgames.scalalibgdx.libgdx.{Rectangle, Vector2}
import com.onsightgames.scalalibgdx.ship.{Ship, ShipReducer, ShipView}

object Level {
  def first(screen : Rectangle) : Level = Level(
    Component[Ship](
      state = Ship(
        boundingBox = Rectangle(
          dimensions = Vector2(40f, 40f),
          bottomLeft   = Vector2(screen.width / 2f, screen.height / 10f)
        ),
        velocity     = Vector2.Zero,
        acceleration = Vector2.Zero
      ),
      reducer = ShipReducer,
      views   = Set(ShipView)
    ),
    AlienFleetFactory.buildComponent(
      AlienFleetInitialData(5, 10, Vector2(10f, -2f), Alien.simple),
      screen
    )
  )
}

case class Level(ship: Component[Ship], alienFleet: Component[AlienFleet]) {

  lazy val components : Set[Component[_ <: Entity]] = Set(ship, alienFleet)

}