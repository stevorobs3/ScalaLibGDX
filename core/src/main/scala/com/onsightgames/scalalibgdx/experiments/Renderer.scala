package com.onsightgames.scalalibgdx.experiments

import com.badlogic.gdx.graphics.g2d.SpriteBatch

trait Renderer[State] {
  def render(state : State, batch : SpriteBatch) : Unit
}
