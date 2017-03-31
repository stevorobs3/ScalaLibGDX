package com.onsightgames.scalalibgdx

import com.onsightgames.scalalibgdx.events.Event

object Reducer {
  type PartialReducerFunc[State] =
    PartialFunction[(State, Event), State]
  type FullReducerFunc[State] =
    PartialFunction[(State, Event), (_ <: Entity, Set[Component[_ <: Entity]])]
}

trait Reducer[State <: Entity] {
  import Reducer._

  def reduce : FullReducerFunc[State]
}