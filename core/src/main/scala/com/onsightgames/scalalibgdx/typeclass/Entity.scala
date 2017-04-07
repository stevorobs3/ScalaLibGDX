package com.onsightgames.scalalibgdx.typeclass

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.onsightgames.scalalibgdx.libgdx.Vector2


object Entity {

  import scala.language.existentials

  trait View[A] {
    def render(batch : SpriteBatch, a : A) : Unit
  }

  trait Reducer[A] {
    def reduce(event : Any, a : A) : A
  }

  implicit class ReducerOps[A](a : A)(implicit ev : Reducer[A]) {
    def reduce(event : Any) : A = ev.reduce(event, a)
  }

  implicit class ViewOps[A](a : A)(implicit ev : View[A]) {
    def render(batch : SpriteBatch) : Unit = ev.render(batch, a)
  }
  case class Alien(id : Long, position : Vector2, texture : Texture)

  case class Ship(name : String, position : Vector2, texture : Texture)

  object Alien {
    implicit object AlienView extends View[Alien] {
      override def render(batch : SpriteBatch, a : Alien) : Unit = {
        batch.draw(a.texture, 0, 0)
      }
    }
    implicit object AlienReducer extends Reducer[Alien] {

      override def reduce(event : Any, a : Alien) : Alien = event match {
        case _ => a.copy(position = a.position + Vector2(1,0))
      }
    }
  }
  object Ship {
    implicit object ShipView extends View[Ship] {
      override def render(batch : SpriteBatch, a : Ship) : Unit = {
        batch.draw(a.texture, 0, 0)
      }
    }

    implicit object ShipReducer extends Reducer[Ship]{

      override def reduce(event : Any, a : Ship) : Ship = {
        a.copy(position = a.position + Vector2(1,0))
      }
    }
  }

  val alien = Alien(1L, Vector2.Zero, new Texture("assets/alien.png"))

  val ship = Ship("bob", Vector2.Zero, new Texture("assets/ship.png"))


  def reduce_[A: Reducer](event : Any, a: A): A = a.reduce(event)
  def render_[A : View](batch : SpriteBatch, a : A) : Unit = a.render(batch)

  var reducers = List[(A, Reducer[A]) forSome { type A }](
    (alien, implicitly[Reducer[Alien]]),
    (ship, implicitly[Reducer[Ship]])
  )

  val views = List[(A, View[A]) forSome { type A }](
    (alien, implicitly[View[Alien]]),
    (ship, implicitly[View[Ship]])
  )

  def map[A] (component : A, ev : Reducer[A]) : (A, Reducer[A]) = {
    (reduce_(1, component)(ev), ev)
  }

  val batch = new SpriteBatch()
  reducers = reducers.map{
    case(com,ev) =>
    map(com,ev)
  }

  views.foreach{
    case (component, ev) => render_(batch, component)(ev)
  }

}