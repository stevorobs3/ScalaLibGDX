package com.onsightgames.scalalibgdx

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode
import com.badlogic.gdx.graphics.g2d.{Animation, SpriteBatch}
import com.badlogic.gdx.utils.{Array => LArray}
import com.onsightgames.scalalibgdx.LArrayUtil._

object Alien {
  def simple : Alien = {
    //scalastyle:off
    new Alien(0f, 0f, 3f, simpleAlienTextures, 2f, 0f)
    //scalastyle:on
  }
  private lazy val simpleAlienTextures = List(
    new Texture("alienUp.png"),
    new Texture("alienDown.png")
  ).toLArray
}

// TODO: make immutable and a case class
case class Alien private (
  x              : Float,
  y              : Float,
  scale          : Float,
  textures       : LArray[Texture],
  framePerSecond : Float,
  currentTime    : Float
) {
  // TODO: wrap in a scala Animation class
  private val animation = new Animation[Texture](1f / framePerSecond, textures, PlayMode.LOOP)

  def width  : Float = textures.items.head.getWidth.toFloat
  def height : Float = textures.items.head.getHeight.toFloat

  def render(batch : SpriteBatch) : Unit = {
    val texture     = animation.getKeyFrame(currentTime)
    val width       = texture.getWidth * scale
    val height      = texture.getHeight * scale
    val extraWidth  = (width - texture.getWidth) / 2
    val extraHeight = (width - texture.getHeight) / 2
    batch.draw(texture, x - extraWidth, y - extraHeight, width, height)
  }
}