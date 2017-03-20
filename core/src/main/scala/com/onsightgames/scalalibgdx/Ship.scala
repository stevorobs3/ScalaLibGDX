package com.onsightgames.scalalibgdx

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.Gdx

object Ship {



  lazy val atStartingPosition = Ship(
    Gdx.graphics.getWidth / 2f,
    Gdx.graphics.getHeight / 10f,
    Alien.SimpleAlienScale
  )
}

case class Ship private (x : Float, y : Float, scale : Float) {

  private val texture = new Texture("ship.png")

  private val width  = texture.getWidth * scale
  private val height = texture.getHeight * scale

  def render(batch : SpriteBatch) : Unit = {
    batch.draw(texture, x, y, width, height)
  }
}
