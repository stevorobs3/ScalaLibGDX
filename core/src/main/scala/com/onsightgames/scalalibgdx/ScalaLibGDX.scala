package com.onsightgames.scalalibgdx

import akkatyped.typed._
import akkatyped.typed.scaladsl.Actor._
import com.badlogic.gdx.Game
import com.onsightgames.scalalibgdx.events.LifecycleManager

object ScalaLibGDX {
  val Width = 600
  val Height = 800
  val Title = "ScalaLibGDX"

}

class ScalaLibGDX extends Game
 with HasLogger {
  val LogId = "ScalaLibGDX"

  lazy val main: Behavior[akkatyped.NotUsed] =
    Stateful(
      behavior = (_, _) ⇒ Unhandled,
      signal = { (ctx, sig) ⇒
        sig match {
          case PreStart ⇒
            val updaterSys = ActorSystem("updater", LifecycleManager.updater())
            val screen = new LifecycleManager(updaterSys)
            setScreen(screen)
            val lifeCycleRef = ctx.spawn(LifecycleManager.create(updaterSys), "registry")
            info("Starting")
            SpaceInvaders.start(ctx, lifeCycleRef)
            Same
          case _ ⇒
            Unhandled
        }
      })

  override def create(): Unit = {
    val _ = ActorSystem("SpaceInvadersGame", main)
  }
}