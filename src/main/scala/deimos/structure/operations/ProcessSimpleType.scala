package deimos.structure.operations

import cats.syntax.functor._

import deimos.schema.classes.SimpleType
import deimos.structure._

object ProcessSimpleType {
  def apply(simpleType: SimpleType): XsdMonad[String] = {
    XsdMonad.ask.map(ctx =>
      simpleType match { // TODO
        case _ if simpleType.restriction.isDefined =>
          simpleType.restriction.get.base
            .map(base => simpleTypesMap.getOrElse(ctx.toGlobalName(base), "String"))
            .getOrElse("String")
        case _ =>
          "String"
    })
  }
}
