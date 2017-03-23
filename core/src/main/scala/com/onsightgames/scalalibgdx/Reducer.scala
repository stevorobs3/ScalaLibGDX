package com.onsightgames.scalalibgdx

import com.onsightgames.scalalibgdx.events.Event

trait Reducer[State <: Entity] {
  def reduce : PartialFunction[(State, Event), State]
}
