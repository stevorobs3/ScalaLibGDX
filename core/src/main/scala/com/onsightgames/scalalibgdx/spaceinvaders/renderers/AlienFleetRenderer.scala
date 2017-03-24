package com.onsightgames.scalalibgdx.spaceinvaders.renderers

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.onsightgames.scalalibgdx.experiments.Renderer
import com.onsightgames.scalalibgdx.spaceinvaders.objects.{Alien, AlienFleet}

object AlienFleetRenderer extends Renderer[AlienFleet] {
  override def render(alienFleet: AlienFleet, batch: SpriteBatch): Unit = {
    alienFleet.aliens.foreach{row =>
      row.foreach(alien => render(alien, batch))
    }
  }
  private def render(alien : Alien, batch : SpriteBatch)  = alien match {
    case Alien(position, scale, _, _, _) =>
      val sprite = alien.currentFrame
      sprite.setPosition(position.x, position.y)
      sprite.setScale(scale)
      sprite.draw(batch)
  }
}