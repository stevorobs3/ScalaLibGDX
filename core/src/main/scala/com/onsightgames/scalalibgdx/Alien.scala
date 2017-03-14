package com.onsightgames.scalalibgdx

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch

class Alien(texture : Texture) {
  var x : Int = _
  var y : Int = _

  def render(batch : SpriteBatch) : Unit = {
    batch.draw(texture, x.toFloat, y.toFloat)
  }
}