package com.onsightgames.scalalibgdx.experiments

trait Reducer[State] extends LObject {
  val initialState : State

  def reduce : PartialFunction[(State, Any), State]
}