package typeclass


class Entity() {

  import scala.language.existentials

  trait View[A] {
    def render(batch : Any, a : A) : Unit
  }

  trait Reducer[A] {
    def reduce(a : A) : PartialFunction[Any, A]
  }

  implicit class ReducerOps[A](a : A)(implicit ev : Reducer[A]) {
    def reduce(event : Any) : A = ev.reduce(event, a)
  }

  implicit class ViewOps[A](a : A)(implicit ev : View[A]) {
    def render(batch : Any) : Unit = ev.render(batch, a)
  }
  case class Alien(id : Long)

  case class Ship(name : String)

  object Alien {
    implicit object AlienView extends View[Alien] {
      override def render(batch : Any, a : Alien) : Unit = {
        println(s"rendering alien $batch")
      }
    }
    implicit object AlienReducer extends Reducer[Alien] {

      override def reduce(event : Any, a : Alien) : Alien = {
        println(s"Reducing $event, $a")
        a.copy(id = a.id + 1L)
      }
    }
  }

  val alien = Alien(1L)


  def reduce_[A: Reducer](event : Any, a: A): A = a.reduce(event)
  def render_[A : View](batch : Any, a : A) : Unit = a.render(batch)


  case class Component[A](a : A, reducers: List[Reducer[A]])
  var components = List[Component[A] forSome { type A }](
    Component(alien, List(implicitly[Reducer[Alien]], implicitly[Reducer[Alien]]))
  )

  val views = List[(A, View[A]) forSome { type A }](
    (alien, implicitly[View[Alien]])
  )

  def map[A] (component : A, evs : List[Reducer[A]]) : Component[A] = {
    Component(evs.foldLeft(component) {
      case (result, ev) =>
      reduce_(1, result)(ev)
    },evs)
  }


  def reduceMe = {
    components = components.map {
      component =>
        map(component.a, component.reducers)
    }
  }

  def viewMe = {
    views.foreach{
      case (component, ev) => render_(2, component)(ev)
    }
  }

}