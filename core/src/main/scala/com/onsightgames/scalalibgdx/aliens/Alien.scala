package com.onsightgames.scalalibgdx.aliens

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.onsightgames.scalalibgdx.libgdx.{Animation, Vector2}

object Alien {

  val SimpleAlienScale = 3f

  def simple : Alien = {
    //scalastyle:off
    new Alien(Vector2.Zero, SimpleAlienScale, simpleAlienTextures, 2f, 0f)
    //scalastyle:on
  }
  private lazy val simpleAlienTextures = List(
    new Texture("alienUp.png"),
    new Texture("alienDown.png")
  )
}

case class Alien private (
  position        : Vector2,
  scale           : Float,
  textures        : List[Texture],
  framesPerSecond : Float,
  currentTime     : Float
) {
  private val animation = Animation(framesPerSecond, textures, PlayMode.LOOP)

  def width     : Float = animation.width
  def height    : Float = animation.height
  def rightEdge : Float = position.x + width
  def leftEdge  : Float = position.x

  def render(batch : SpriteBatch) : Unit = {
    animation.render(position, scale, currentTime)(batch)
  }
}