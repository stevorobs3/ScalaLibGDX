package com.onsightgames.scalalibgdx

import com.onsightgames.scalalibgdx.aliens.{Alien, AlienFleetData}

object Level {
  //scalastyle:off
  lazy val One : Level = Level(
    AlienFleetData(5, 10, 2f, 10f, Alien.simple)
  )
  //scalastyle:on
}

case class Level(
  alienFleet: AlienFleetData
)