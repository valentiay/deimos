package deimos.schema.classes

import deimos.schema.classes.namespaces.xsd
import phobos.decoding.ElementDecoder
import phobos.derivation
import phobos.syntax.{attr, xmlns}

final case class ComplexContent(
    @attr complex: Option[Boolean],
    @xmlns(xsd) extension: Option[Extension],
    @xmlns(xsd) restriction: Option[Restriction]
)

object ComplexContent {
  implicit val complexContentElementDecoder: ElementDecoder[ComplexContent] = derivation.semiauto.deriveElementDecoder
}
