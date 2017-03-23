package com.onsightgames.scalalibgdx.aliens

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.onsightgames.scalalibgdx.View

object AlienFleetView extends View[AlienFleet] {

  override def render(fleet: AlienFleet, batch: SpriteBatch): Unit = {
    fleet.aliens.flatten.foreach(alien => AlienView.render(alien, batch))
  }
}
