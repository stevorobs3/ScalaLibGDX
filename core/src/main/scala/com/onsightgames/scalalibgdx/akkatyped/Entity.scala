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

object Entity {

}


abstract class Entity[T, D] (initialData : D)(
  implicit
  ctx                 : scaladsl.ActorContext[akka.NotUsed],
  lifecycleManagerRef : ActorRef[LifecycleManager.Register]
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
            info(s"Registsr $updateRef")
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
  lifecycleManager : ActorRef[LifecycleManager.Register]
) {
  // dependency inject this!
  protected def entity            : Entity[T, D]
  // define this!
  protected def render(data : D) : RenderAction

  val view : ActorRef[Render] = ctx.spawn(render, s"$getClass-View")

  println(s"view $view")
  lifecycleManager ! RegisterRender(view)

  // call this in framework
  private def render : Behavior[Render] = {
    Stateless{
      (ctx, msg) =>
        msg match {
          case Render(replyTo) =>
            val renderer = ctx.spawn(renderAction(replyTo), s"$getClass: renderer")
            entity.stateRef ! renderer
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
  case class AlienData(health: Float)

  sealed trait AlienEvents
  case class Fire(x: Int) extends AlienEvents
  case class Update(deltaTime: Float) extends AlienEvents
  case class GetState(replyTo : ActorRef[AlienData]) extends AlienEvents
}

class AlienView(val entity: Entity[Alien.AlienEvents, Alien.AlienData])(
  implicit
  ctx                 : scaladsl.ActorContext[akka.NotUsed],
  lifecycleManagerRef : ActorRef[LifecycleManager.Register]
)
  extends EntityView[Alien.AlienEvents, Alien.AlienData]
  with HasLogger {
  import Alien._
  override val LogId = "AlienView"

  override def render(data: AlienData): RenderAction = RenderAction{batch =>
    info(s"Rendering $batch with $data")
  }
}

class Alien(initialData : Alien.AlienData)(
  implicit
  ctx                 : scaladsl.ActorContext[akka.NotUsed],
  lifecycleManagerRef : ActorRef[LifecycleManager.Register]
)
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
      case Update(deltaTime) =>
        Stateless(entityBehavior(data.copy(health = data.health + deltaTime)))
      case GetState(replyTo) =>
        replyTo ! data
        Same
    }
}