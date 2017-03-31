package com.onsightgames.scalalibgdx.aliens

import com.onsightgames.scalalibgdx.libgdx.{Rectangle, Vector2}

case class AlienFleetInitialData(
  boundingBox  : Rectangle,
  width        : Int,  // number of aliens down
  height       : Int,  // number of aliens across
  velocity     : Vector2, // pixels per second in x & y directions
  acceleration : Vector2,
  alien        : Alien
)