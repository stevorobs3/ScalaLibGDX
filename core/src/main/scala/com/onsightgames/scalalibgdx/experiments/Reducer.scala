package com.onsightgames.scalalibgdx.experiments

trait Reducer[State] {
  val initialState : State

  def reduce : PartialFunction[(State, Any), State]
}