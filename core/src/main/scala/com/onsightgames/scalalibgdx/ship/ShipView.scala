package com.onsightgames.scalalibgdx.ship

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.onsightgames.scalalibgdx.View

object ShipView extends View[Ship] {

  private val texture = new Texture("ship.png")

  override def render(ship: Ship, batch: SpriteBatch): Unit = {
    batch.draw(
      texture,
      ship.boundingBox.bottomLeft.x,
      ship.boundingBox.bottomLeft.y,
      ship.boundingBox.dimensions.x,
      ship.boundingBox.dimensions.y
    )
  }
}
