package deimos.schema.classes

import deimos.schema.WithNamespaces
import deimos.schema.classes.namespaces.{wsdl, xsd}
import phobos.decoding.ElementDecoder
import phobos.syntax.xmlns
import phobos.derivation

case class Definitions(
    @xmlns(wsdl) types: Option[Definitions.Types],
)

object Definitions {
  case class Types(@xmlns(xsd) schema: List[WithNamespaces[Schema]])

  implicit val typesElementDecoder: ElementDecoder[Types]             = derivation.semiauto.deriveElementDecoder
  implicit val definitionsElementDecoder: ElementDecoder[Definitions] = derivation.semiauto.deriveElementDecoder
}
