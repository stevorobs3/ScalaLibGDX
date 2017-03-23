package com.onsightgames.scalalibgdx.experiments.events

import com.onsightgames.scalalibgdx.experiments.LObject

trait EventDetector extends LObject {
  def generate : List[Any]
}