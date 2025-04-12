package deimos.structure.operations

import cats.syntax.flatMap._
import cats.syntax.functor._

import deimos.schema.classes.Attributes
import deimos.structure.Attr

object ProcessAttributes {
  def apply(attributes: Attributes): XsdMonad[List[Attr]] =
    for {
      attributes1 <- XsdMonad.traverse(attributes.attribute)(ProcessAttribute.apply)
      attributes2 <- XsdMonad.traverse(attributes.attributeGroup)(ProcessAttributeGroup.apply)
    } yield attributes1 ++ attributes2.flatten
}
