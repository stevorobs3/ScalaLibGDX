package com.onsightgames.scalalibgdx

import com.badlogic.gdx.graphics.g2d.SpriteBatch

trait View[State] {
  def render(state : State, batch : SpriteBatch) : Unit
}
