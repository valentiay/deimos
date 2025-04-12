package deimos.schema.classes

import deimos.schema.classes.namespaces.xsd
import phobos.decoding.ElementDecoder
import phobos.derivation
import phobos.syntax.{attr, xmlns}

final case class Attribute(
    @attr name: Option[String],
    @attr id: Option[String],
    @attr ref: Option[String],
    @attr `type`: Option[String],
    @attr use: Option[String],
    @attr fixed: Option[String],
    @attr form: Option[String],
    @xmlns(xsd) simpleType: Option[SimpleType],
) extends Global

object Attribute {
  implicit val attributeElementDecoder: ElementDecoder[Attribute] = derivation.semiauto.deriveElementDecoder
}
