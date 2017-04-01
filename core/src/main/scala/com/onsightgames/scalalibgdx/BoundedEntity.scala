package com.onsightgames.scalalibgdx

import com.onsightgames.scalalibgdx.libgdx.BasePolygon

trait BoundedEntity[Shape <: BasePolygon[Shape]] extends Entity {

  val bounds : BasePolygon[Shape]

  def isLeftOf(hasBoundingBox : BoundedEntity[_]) : Boolean = {
    bounds.centre.x < hasBoundingBox.bounds.centre.x
  }

  def isRightOf(hasBoundingBox : BoundedEntity[_]) : Boolean = {
    bounds.centre.x > hasBoundingBox.bounds.centre.x
  }
}
