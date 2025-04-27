package deimos.schema.classes

import deimos.schema.classes.namespaces.xsd
import phobos.decoding.ElementDecoder
import phobos.syntax._
import phobos.derivation

case class Any(
    @attr minOccurs: scala.Option[Int],
    @attr maxOccurs: scala.Option[String],
    @attr namespace: scala.Option[String],
    @attr processContents: scala.Option[String],
    @attr id: scala.Option[String],
    @xmlns(xsd) annotation: scala.Option[Annotation]
)

object Any {
  implicit val anyDecoder: ElementDecoder[Any] = derivation.semiauto.deriveElementDecoder
}

case class AnyAttribute(
    @attr namespace: scala.Option[String],
    @attr processContents: scala.Option[String],
    @attr id: scala.Option[String],
    @xmlns(xsd) annotation: scala.Option[Annotation]
)

object AnyAttribute {
  implicit val anyAttributeDecoder: ElementDecoder[AnyAttribute] = derivation.semiauto.deriveElementDecoder
}
