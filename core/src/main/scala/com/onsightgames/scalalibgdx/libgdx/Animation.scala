package com.onsightgames.scalalibgdx.libgdx

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode
import com.badlogic.gdx.graphics.g2d.{Sprite, SpriteBatch, Animation => LAnimation}
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

  private lazy val lAnimation = new LAnimation[Sprite](
    1f / framesPerSecond,
    textures.map(texture => new Sprite(texture)).toLArray,
    playMode
  )

  def width  : Float = lAnimation.getKeyFrames.head.getWidth
  def height : Float = lAnimation.getKeyFrames.head.getHeight

  def render(
    position    : Vector2,
    scale       : Float,
    currentTime : Float
  )(batch : SpriteBatch) : Unit = {
    val sprite = lAnimation.getKeyFrame(currentTime)
    sprite.setPosition(position.x, position.y)
    sprite.setScale(scale)
    sprite.draw(batch)
  }
}