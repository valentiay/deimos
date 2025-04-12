package deimos.schema.classes

import deimos.schema.classes.namespaces.xsd
import phobos.decoding.ElementDecoder
import phobos.derivation
import phobos.syntax.{attr, xmlns}

final case class SimpleType(
    @attr name: Option[String],
    @attr id: Option[String],
    @xmlns(xsd) restriction: Option[Restriction],
    @xmlns(xsd) union: Option[Union],
    @xmlns(xsd) list: Option[XsdList],
) extends Global

object SimpleType {
  implicit val simpleTypeElementDecoder: ElementDecoder[SimpleType] = derivation.semiauto.deriveElementDecoder
}
