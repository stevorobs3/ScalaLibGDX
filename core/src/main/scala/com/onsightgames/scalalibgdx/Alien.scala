package com.onsightgames.scalalibgdx

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.onsightgames.scalalibgdx.libgdx.Animation

object Alien {
  def simple : Alien = {
    //scalastyle:off
    new Alien(0f, 0f, 3f, simpleAlienTextures, 2f, 0f)
    //scalastyle:on
  }
  private lazy val simpleAlienTextures = List(
    new Texture("alienUp.png"),
    new Texture("alienDown.png")
  )
}

case class Alien private (
  x               : Float,
  y               : Float,
  scale           : Float,
  textures        : List[Texture],
  framesPerSecond : Float,
  currentTime     : Float
) {
  // TODO: wrap in a scala Animation class
  private val animation = Animation(framesPerSecond, textures, PlayMode.LOOP)

  def width  : Float = animation.width
  def height : Float = animation.height

  def render(batch : SpriteBatch) : Unit = {
    animation.render(x, y, scale, currentTime)(batch)
  }
}