package com.onsightgames.scalalibgdx.ship

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.onsightgames.scalalibgdx.View

object ShipView extends View[Ship] {

  private val texture = new Texture("ship.png")

  private val TextureBorder = 10f

  override def render(ship: Ship, batch: SpriteBatch): Unit = {
    val boundingBox = ship.bounds.getBoundingRectangle
    batch.draw(
      texture,
      boundingBox.x - TextureBorder,
      boundingBox.y - TextureBorder,
      boundingBox.width + 2 * TextureBorder,
      boundingBox.height + 2 * TextureBorder
    )
  }
}
