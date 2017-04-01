package com.onsightgames.scalalibgdx.aliens

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.onsightgames.scalalibgdx.View
import com.onsightgames.scalalibgdx.libgdx.Animation

object AlienView extends View[Alien] {

  private lazy val simpleAlienTextures = List(
    new Texture("alienUp.png"),
    new Texture("alienDown.png")
  )

  private val animation = Animation(2f, simpleAlienTextures, PlayMode.LOOP)

  override def render(alien : Alien, batch : SpriteBatch): Unit = {
    animation.render(alien.bounds, alien.currentTime)(batch)
  }
}
