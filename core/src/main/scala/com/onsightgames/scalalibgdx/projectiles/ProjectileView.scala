package com.onsightgames.scalalibgdx.projectiles

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.onsightgames.scalalibgdx.{Logging, View}

object ProjectileView extends View[Projectile] with Logging {
  lazy val texture = new Texture("alienUp.png")

  override def render(state : Projectile, batch : SpriteBatch) : Unit = {
    info("Drawing ")
    batch.draw(texture, state.x, state.y)
  }
}