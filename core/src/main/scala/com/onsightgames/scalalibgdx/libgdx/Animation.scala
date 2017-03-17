package com.onsightgames.scalalibgdx.libgdx

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode
import com.badlogic.gdx.graphics.g2d.{SpriteBatch, Animation => LAnimation}
import com.onsightgames.scalalibgdx.LArrayUtil._

case class Animation (
  framesPerSecond : Float,
  textures        : List[Texture],
  playMode        : PlayMode
) {
  textures.tail.foldLeft(textures.head){
    case (previousTexture, texture) =>
      val sameWidth  = texture.getWidth  == previousTexture.getWidth
      val sameHeight = texture.getHeight == previousTexture.getHeight
      require(sameWidth && sameHeight)
      texture
  }

  private lazy val lAnimation = new LAnimation[Texture](
    1f / framesPerSecond,
    textures.toLArray,
    playMode
  )

  def width  : Float = textures.head.getWidth.toFloat
  def height : Float = textures.head.getHeight.toFloat

  def render(
    x           : Float,
    y           : Float,
    scale       : Float,
    currentTime : Float
  )(batch : SpriteBatch) : Unit = {
    val texture     = lAnimation.getKeyFrame(currentTime)
    val width       = texture.getWidth * scale
    val height      = texture.getHeight * scale
    val extraWidth  = (width - texture.getWidth) / 2
    val extraHeight = (width - texture.getHeight) / 2
    batch.draw(texture, x - extraWidth, y - extraHeight, width, height)
  }
}