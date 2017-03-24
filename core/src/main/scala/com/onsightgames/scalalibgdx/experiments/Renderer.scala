package com.onsightgames.scalalibgdx.experiments

import com.badlogic.gdx.graphics.g2d.SpriteBatch

trait Renderer[State <: GameObject] {
  def render(state : State, batch : SpriteBatch) : Unit

  def create : Renderer[GameObject] = this.asInstanceOf[Renderer[GameObject]]
}