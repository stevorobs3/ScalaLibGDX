package com.onsightgames.scalalibgdx.akkatyped

import akka.typed._
import akka.typed.scaladsl.Actor._
import akka.typed.scaladsl.ActorContext
import com.onsightgames.scalalibgdx.HasLogger
import com.onsightgames.scalalibgdx.events.KeyboardEventEmitter.KeyUpEvent
import com.onsightgames.scalalibgdx.events.LifecycleManager
import com.onsightgames.scalalibgdx.events.LifecycleManager._

trait KeyUpEventEventHandler {
  def filter   : KeyUpEvent => Boolean
  def behavior : Behavior[KeyUpEvent]
}

abstract class Entity[T, D] (initialData : D)(
  implicit
  ctx                 : scaladsl.ActorContext[akka.NotUsed],
  lifecycleManagerRef : ActorRef[LifecycleManager.Event]
) {
  type Update   <: T
  type GetState <: T

  val stateRef  : ActorRef[ActorRef[D]] = ctx.spawn(getState, s"$getClass-State")
  val actorRef  : ActorRef[T] = ctx.spawn(behavior, s"$getClass-Behavior")

  def entityBehavior(data : D) : (scaladsl.ActorContext[T],T) => Behavior[T]

  def parseUpdate(update   : LifecycleManager.Update) : Update

  protected def parseGetState(replyTo : ActorRef[D])  : GetState

  // call from framework
  private def getState: Behavior[ActorRef[D]] =
    Stateless{
      (_, msg) =>
        actorRef ! parseGetState(msg)
    }

  private lazy val behavior : Behavior[T] =
    Stateful(
      behavior = entityBehavior(initialData),
      signal = { (ctx, sig) ⇒
        sig match {
          case PreStart =>
            val updateRef = ctx.spawnAdapter(parseUpdate)
            lifecycleManagerRef ! RegisterUpdate(updateRef)
            Same
          case _ ⇒
            Unhandled
        }
      }
    )
}

// T = stateEvents
// D = stateData
abstract class EntityView[T, D](
  implicit
  ctx              : scaladsl.ActorContext[akka.NotUsed],
  lifecycleManager : ActorRef[LifecycleManager.Event]
) {
  // dependency inject this!
  protected def actor            : Entity[T, D]
  // define this!
  protected def render(data : D) : RenderAction

  val view : ActorRef[Render] = ctx.spawn(render, s"$getClass-View")

  lifecycleManager ! RegisterRegister(view)

  // call this in framework
  private def render : Behavior[Render] = {
    Stateless{
      (ctx, msg) =>
        msg match {
          case Render(replyTo) =>
            val renderer = ctx.spawn(renderAction(replyTo), s"$getClass: renderer")
            actor.stateRef ! renderer
            Same
        }
    }
  }

  private def renderAction(actorRef : ActorRef[RenderAction]) : Behavior[D] =
    Stateless{
      (_, data) =>
        actorRef ! render(data)
        Same
    }
}

object Alien {
  sealed trait AlienData
  case class ActiveData(health: Float) extends AlienData
  case class DeadData(headCount: Int) extends AlienData

  sealed trait AlienEvents
  case class Fire(x: Int) extends AlienEvents
  case class Update(deltaTime: Float) extends AlienEvents
  case class GetState(replyTo : ActorRef[AlienData]) extends AlienEvents
}

class AlienView(val actor: Entity[Alien.AlienEvents, Alien.AlienData])
  extends EntityView[Alien.AlienEvents, Alien.AlienData]
  with HasLogger {
  import Alien._
  override val LogId = "AlienView"

  override def render(data: AlienData): RenderAction = RenderAction{batch =>
    info(s"Rendering $batch with $data")
  }
}

class Alien(initialData : Alien.AlienData)
  extends Entity[Alien.AlienEvents, Alien.AlienData](initialData)
  with HasLogger
{
  import Alien._

  val LogId = "Alien"
  type Update   = Alien.Update
  type GetState = Alien.GetState

  def parseUpdate(update : LifecycleManager.Update) : Update   = Alien.Update(update.timeElapsed)
  def parseGetState(replyTo : ActorRef[AlienData])  : GetState = Alien.GetState(replyTo)

  override def entityBehavior(data : AlienData) :
    (ActorContext[AlienEvents], AlienEvents) => Behavior[AlienEvents] = (_, event) => event match {
      case Fire(_) =>
        info("Firing!")
        Same
    }
}