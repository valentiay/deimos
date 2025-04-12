package deimos.schema.classes

import deimos.schema.classes.namespaces.xsd
import phobos.decoding.ElementDecoder
import phobos.derivation
import phobos.syntax.xmlns

final case class SimpleContent(
    @xmlns(xsd) extension: Option[Extension],
    @xmlns(xsd) restriction: Option[Restriction],
)

object SimpleContent {
  implicit val simpleContentElementDecoder: ElementDecoder[SimpleContent] = derivation.semiauto.deriveElementDecoder
}
