package com.onsightgames.scalalibgdx

import com.badlogic.gdx.Screen

import scala.collection.mutable
import akka.actor.{FSM, Props}

abstract class Event {
  def applyTo(entity : BaseEntity) : Seq[Event]
}
abstract class StateTransition[Entity <: BaseEntity] {
  def applyTo(entity : Entity) : Entity
}
trait BaseRequest
trait BaseEvent
trait BaseData

abstract class Living extends BaseEntity {
  var health    : Int
  var alive     : Boolean = true
  val MaxHealth : Int
  val MinHealth : Int = 0
}

class Spider extends Living {
  val MaxHealth : Int = 100
  var health : Int
}

class KilledEntity[FromEntity <: BaseEntity, ToEntity <: Living]
  extends Event[FromEntity, ToEntity] {
  override def applyTo(entity : ToEntity) : Seq[Event[ToEntity, FromEntity]] = {

  }
}

class TakeDamage[FromEntity <: BaseEntity, ToEntity <: Living](damage : Int)
  extends Event[FromEntity, ToEntity] {
  override def applyTo(entity : ToEntity) : Seq[Event[ToEntity, FromEntity]] = {
    entity.health -= damage
    if (entity.health <= entity.MinHealth) {
      entity.alive = false
      Seq()
    }
    else
      Seq.empty
  }
}

abstract class EntityController[Entity <: CoreEntity, CoreEntity <: BaseEntity] {
  def updateEntity(entity : Entity) : (Seq[Event], Seq[StateTransition[Entity]])
}

case class RegisterEvent(event : Event, )

object Example {

  sealed trait ExampleRequest extends BaseRequest


  sealed trait ExampleEvent extends BaseEvent
  case class ExampleFail(x : Int) extends ExampleEvent
  case class ExampleSucceed(y : Int) extends ExampleEvent

}
class Example extends BaseEntity[Example.ExampleRequest, Example.ExampleEvent] {
  import Example._

  private var registeredFails    = mutable.MutableList.empty[ExampleFail => Unit]
  private var registeredSucceeds = mutable.MutableList.empty[ExampleSucceed => Unit]



  override protected def triggerEvent(event : ExampleEvent) : Unit = {
    event match {
      case e : ExampleFail =>
        registeredFails.foreach(func => func(e))
      case e : ExampleSucceed =>
        registeredSucceeds.foreach(func => func(e))
    }
  }
}

abstract class BaseEntity[Request <: BaseRequest, Event <: BaseEvent, Data <: BaseData] {
  protected def triggerEvent[E <: Event](event : E) : Unit = {
    registeredCallbacks.flatMap{
      case callback: (E => Unit) =>
        callback(event)
        None
      case c : (Event => Unit) => Some(c)
    }
  }

// TODO: wrap methods of FSM actor inside an Actor System, etc
  val InitialState : _ <: States
  val InitialData : _ <: Data

  trait States
  private lazy val FSMActor = Props(new FSM[States, Data]{
    startWith(InitialState, InitialData)

  })

  // TODO: Make this part of the Actor data?
  private val registeredCallbacks = mutable.MutableList.empty[Event => Unit]

  def registerEvent(callback : Event => Unit): Unit = {
    registeredCallbacks += callback
  }
}

abstract class BaseWorld[Entity <: BaseEntity] extends Screen {
  val entities : mutable.MutableList[Entity]

  override def hide() : Unit = {}
  override def resize(width: Int, height: Int): Unit = {}
  override def dispose(): Unit = {}
  override def pause(): Unit = {}
  override def show(): Unit = {}
  override def resume(): Unit = {}

  override def render(delta: Float): Unit = {
    update(delta)
  }

  def update(delta: Float): Unit = {
    val eventsById = eventsFromLastFrame.groupBy(_.toEntityId).withDefault(Seq.empty)

    val eventsToRoute = world.entities.flatMap { entity =>
      val replyEvents = entity.events.flatMap { ev =>
        ev.applyTo(entity) // take damage from others etc.
      }
      val (externalEvents, stateTransitions) = updateEntity(entity, world)
      for(t <- stateTransitions) {
        t.applyTo(entity) // change entity position etc.
      }
      replyEvents ++ externalEvents
    }
  }
}