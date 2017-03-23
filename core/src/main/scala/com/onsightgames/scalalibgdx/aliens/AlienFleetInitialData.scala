package com.onsightgames.scalalibgdx.aliens

import com.onsightgames.scalalibgdx.libgdx.Vector2

case class AlienFleetInitialData(
  height   : Int,  // number of aliens across
  width    : Int,  // number of aliens down
  velocity : Vector2, // pixels per second in x & y directions
  alien    : Alien
)