package com.onsightgames.scalalibgdx.experiments

import com.onsightgames.scalalibgdx.Logging

trait Reducer[State <: GameObject] extends Logging {
  def reduce : PartialFunction[(GameObject, Any), GameObject]

  def create : Reducer[GameObject] = this.asInstanceOf[Reducer[GameObject]]
}