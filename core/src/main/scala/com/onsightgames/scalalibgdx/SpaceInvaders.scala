package com.onsightgames.scalalibgdx

import akka.NotUsed
import akka.typed.scaladsl.Actor._
import akka.typed.scaladsl.ActorContext
import akka.typed._
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.onsightgames.scalalibgdx.events.LifecycleManager
import com.onsightgames.scalalibgdx.events.LifecycleManager._

object SpaceInvaders extends HasLogger {
  override val LogId : String = "SpaceInvaders"

  /*private val screen = Rectangle(
    bottomLeft   = Vector2.Zero,
    dimensions = Vector2(
      x = Gdx.graphics.getWidth.toFloat,
      y = Gdx.graphics.getHeight.toFloat
    )
  )*/

  // entity to specify its own events, register against :
  //  -- lifecycle events
  //  -- input events


  trait EntityEvents
  case class UpdateEntity(
    deltaTime : Float,
    replyTo   : ActorRef[RenderAction]
  ) extends EntityEvents



  private def noOp(batch : SpriteBatch) : Unit = {

  }
  private def entity(
    lifecycleManagerRef : ActorRef[LifecycleManager.Register],
    data                : Int
  ) : Behavior[EntityEvents] = Stateful[EntityEvents] (
    behavior = { (_, msg) =>
      msg match {
        case UpdateEntity(deltaTime, replyTo) =>
          println(s"Render entity! ${1/ deltaTime}")
          replyTo ! RenderAction(noOp)
          Same
        case _ =>
          Unhandled
      }
    },
    signal = { (ctx, sig) =>
      sig match {
        case PreStart =>
          info("PreStarting")
          val updateRef = ctx.spawnAdapter{
            update : Update => UpdateEntity(update.timeElapsed, update.replyTo)
          }
          lifecycleManagerRef ! LifecycleManager.RegisterUpdate(updateRef)
          Same
        case _ =>
          Unhandled
      }
    }
  )


//private val keyboardEventEmitter      = new KeyboardEventEmitter(store.dispatch)
//  private val boundaryCollisionDetector = new BoundaryCollisionDetector
//  private val collisionDetector         = new CollisionDetector

  def start(
    ctx              : ActorContext[NotUsed],
    lifecycleManager : ActorRef[LifecycleManager.Register]
  ) : Unit = {
    val _ = ctx.spawn(entity(lifecycleManager, 1), "Entity")
  }
}