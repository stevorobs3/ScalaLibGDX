package com.onsightgames.scalalibgdx.experiments

trait Reducer[State] extends Object {
  val initialState : State

  def reduce : PartialFunction[(State, Any), State]
}