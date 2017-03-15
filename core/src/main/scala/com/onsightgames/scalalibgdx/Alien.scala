package com.onsightgames.scalalibgdx

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.{Sprite, SpriteBatch}

class Alien(texture : Texture) {
  var x : Int = _
  var y : Int = _
  private val AlienScale = 3f

  private val sprite = new Sprite(texture)
  sprite.setScale(AlienScale, AlienScale)
  def width : Float  = sprite.getWidth
  def height : Float = sprite.getHeight



  def render(batch : SpriteBatch) : Unit = {
    sprite.setPosition(x.toFloat, y.toFloat)
    sprite.draw(batch)
  }
}