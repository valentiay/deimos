package deimos.schema.classes

import phobos.decoding.ElementDecoder
import phobos.syntax.attr
import phobos.derivation

case class Annotation(
    @attr id: scala.Option[String],
//    @xmlns(xsd) appinfo: scala.List[Appinfo],
//    @xmlns(xsd) documentation: scala.List[Documentation],
)

object Annotation {
  implicit val annotationDecoder: ElementDecoder[Annotation] = derivation.semiauto.deriveElementDecoder
}
