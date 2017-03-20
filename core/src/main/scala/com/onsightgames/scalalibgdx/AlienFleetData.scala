package com.onsightgames.scalalibgdx

case class AlienFleetData(
  height                 : Int,  // number of aliens across
  width                  : Int,  // number of aliens down
  descentAcceleration    : Float, // pixels per second^2
  horizontalAcceleration : Float, // pixels per second^2
  alien                  : Alien
)