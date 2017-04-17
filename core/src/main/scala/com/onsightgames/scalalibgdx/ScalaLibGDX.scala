package com.onsightgames.scalalibgdx

import akka.typed._
import akka.typed.scaladsl.Actor._
import com.badlogic.gdx.Game
import com.onsightgames.scalalibgdx.akkatyped.{Alien, AlienView}
import com.onsightgames.scalalibgdx.events.LifecycleManager

object ScalaLibGDX {
  val Width = 600
  val Height = 800
  val Title = "ScalaLibGDX"

}

class ScalaLibGDX extends Game
 with HasLogger {
  val LogId = "ScalaLibGDX"

  lazy val main: Behavior[akka.NotUsed] =
    Stateful(
      behavior = (_, _) ⇒ Unhandled,
      signal = { (ctx, sig) ⇒
        sig match {
          case PreStart ⇒
            implicit val updaterSys = ActorSystem("updater", LifecycleManager.updater())
            implicit val context = ctx

            val alien = new Alien(Alien.ActiveData(1))
            val _ = new AlienView(alien)
            val screen = new LifecycleManager(updaterSys)
            setScreen(screen)
            //val lifeCycleRef = ctx.spawn(LifecycleManager.create(updaterSys), "registry")
            info("Starting")
            //SpaceInvaders.start(ctx, lifeCycleRef)
            Same
          case _ ⇒
            Unhandled
        }
      })

  override def create(): Unit = {
    val _ = ActorSystem("SpaceInvadersGame", main)
  }
}