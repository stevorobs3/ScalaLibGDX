package com.onsightgames.scalalibgdx.spaceinvaders.objects

object Level {
  //scalastyle:off
  lazy val One : Level = Level(
    AlienFleet(5, 10, 2f, 10f, Alien.simple)
  )
  //scalastyle:on
}

case class Level(
  alienFleet: AlienFleet
)