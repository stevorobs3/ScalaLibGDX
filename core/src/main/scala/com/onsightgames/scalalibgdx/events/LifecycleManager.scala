package com.onsightgames.scalalibgdx.events

import akka.actor.Scheduler
import akka.typed._
import akka.typed.scaladsl.Actor._
import akka.typed.scaladsl.AskPattern._
import akka.util.Timeout
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.{Gdx, Screen}
import com.onsightgames.scalalibgdx.HasLogger

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.util.Try


object LifecycleManager {

  sealed trait Event
  case class Update(
    timeElapsed : Float,
    scheduler   : Scheduler,
    replyTo     : ActorRef[RenderAction]
  ) extends Event
  sealed trait Register extends Event

  case class RegisterUpdate(entity : ActorRef[Update]) extends Register
  case class RegisterRegister(entity : ActorRef[Render]) extends Register

  case class Render(replyTo : ActorRef[RenderAction]) extends Event

  case class RenderAction(renderAction : SpriteBatch => Unit) extends Event

  implicit val timeout = Timeout(5.seconds)


 // TODO: extend for hide, show, etc
  def updater(entities : List[ActorRef[Update]] = List.empty) : Behavior[Event] = {
  import scala.concurrent.ExecutionContext.Implicits.global
    Stateful{ (_, msg) =>
      msg match {
        case update : Update =>
          println(s"getting update with ${entities.length} entities")
          implicit val scheduler = update.scheduler
          val future = entities
            .map(_ ? (Update(update.timeElapsed, update.scheduler, _ : ActorRef[RenderAction])))
            .map(_.map(_.renderAction))
            .reduceOption {
            (leftF, rightF) =>
              leftF.flatMap(left =>
                rightF.map{right => batch : SpriteBatch =>
                  right(batch)
                  left(batch)
                }
              )
          }.getOrElse(Future.successful{
            (_ : SpriteBatch) =>
              // intentional no-op
          })
          future.map(action => update.replyTo ! RenderAction(action))
          Same
        case RegisterUpdate(entity) =>
          println("Reg in updater!")
          updater(entity :: entities)
        case _ =>
          Unhandled
      }
    }
  }
  // only expose register, internally trigger events from some methods!
  def create(updaterRef : ActorRef[LifecycleManager.Event]) : Behavior[Register] = {
    Stateful[Register] { (_, msg) =>
      msg match {
        case RegisterUpdate(entity) =>
          println(s"Registering Update")
          updaterRef ! RegisterUpdate(entity)
          Same
      }
    }
  }
}

class LifecycleManager(actorSystem : ActorSystem[LifecycleManager.Event]) extends Screen
  with HasLogger {

  override val LogId: String = "LifecycleEventEmitter"

  import LifecycleManager._

  override def hide(): Unit = {
    info("Hiding")
  }

  override def resize(width: Int, height: Int): Unit = {
    info(s"Resizing to $width*$height")
  }

  override def dispose(): Unit = {
    info("Disposing")
  }

  override def pause(): Unit = {
    info("Pausing")
  }

  override def show(): Unit = {
    info("Showing")
  }

  override def resume(): Unit = {
    info("Resuming")
  }

  private lazy val batch  = new SpriteBatch
  override def render(delta: Float): Unit = {
    Gdx.gl.glClearColor(0,0,0,1)
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
    batch.begin()
    implicit val timeout = Timeout(5.seconds)
    implicit val scheduler = actorSystem.scheduler
    val future : Future[RenderAction] = actorSystem ? (Update(delta, scheduler, _))
    val result = Try(Await.result(future, 5.seconds))
      .map(_.renderAction)
      .getOrElse((_ : SpriteBatch) => Unit)
    result(batch)
    batch.end()
  }
}