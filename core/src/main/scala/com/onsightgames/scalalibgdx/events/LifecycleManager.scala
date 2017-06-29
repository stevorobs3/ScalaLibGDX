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


object LifecycleManager extends HasLogger {
  override val LogId = "LifecycleManager"

  sealed trait Event
  case class Update(
    timeElapsed : Float,
    scheduler   : Scheduler,
    replyTo     : ActorRef[Boolean]
  ) extends Event
  sealed trait Register extends Event

  case class RegisterUpdate(entity : ActorRef[Update]) extends Register
  case class RegisterRender(entity : ActorRef[Render]) extends Register

  case class Render(scheduler : Scheduler, replyTo : ActorRef[RenderAction]) extends Event

  case class RenderAction(renderAction : SpriteBatch => Unit) extends Event

  implicit val timeout = Timeout(5.seconds)


 // TODO: extend for hide, show, etc
  def looper(
    updatables  : List[ActorRef[Update]] = List.empty,
    renderables : List[ActorRef[Render]] = List.empty
  ) : Behavior[Event] = {
  import scala.concurrent.ExecutionContext.Implicits.global
    Stateful{ (_, msg) =>
      msg match {
        case update : Update =>
          info(s"getting update with ${updatables.length} entities")
          implicit val scheduler = update.scheduler
          val future = updatables
            .map(_ ? (Update(update.timeElapsed, update.scheduler, _ : ActorRef[Boolean])))
            .reduceOption {
            (leftF, rightF) =>
              leftF.flatMap(left => rightF.map{right => left && right})
          }.getOrElse(Future.successful(true))
          future.map(result => update.replyTo ! result)
          Same
        case render : Render =>
          implicit val scheduler = render.scheduler
          val future = renderables
            .map(_ ? (Render(render.scheduler, _ : ActorRef[RenderAction])))
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
                 future.map(action => render.replyTo ! RenderAction(action))
          Same
        case RegisterUpdate(entity) =>
          info(s"Reg in updater! $entity")
          looper(entity :: updatables)
        case RegisterRender(entity) =>
          info(s"Reg in rendering! $entity")
          Same
        case _ =>
          Unhandled
      }
    }
  }
  // only expose register, internally trigger events from some methods!
  def create(updaterRef : ActorRef[LifecycleManager.Event]) : Behavior[Register] = {
    Stateful[Register] { (_, msg) =>
      msg match {
        case r @ RegisterUpdate(entity) =>
          info(s"Registering Update $entity")
          updaterRef ! r
          Same
        case r @ RegisterRender(view) =>
          info(s"Registering Render $view")
          updaterRef ! r
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
    val future : Future[Boolean] = actorSystem ? (Update(delta, scheduler, _))
    val result = Try(Await.result(future, 5.seconds)).getOrElse(false)
    if (!result)
      error(s"Failed to update in 5 seconds!")

    val renderFuture : Future[RenderAction] = actorSystem ? (Render(scheduler, _))
    val renderResult = Try(Await.result(renderFuture, 5.seconds))
      .map(_.renderAction)
      .getOrElse((_ : SpriteBatch) => {}) // no-op
    renderResult(batch)
    batch.end()
  }
}