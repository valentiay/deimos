package deimos.schema.classes

import deimos.schema.classes.namespaces.xsd
import phobos.decoding.ElementDecoder
import phobos.derivation
import phobos.syntax.{attr, xmlns}

final case class AttributeGroup(
    @attr name: Option[String],
    @attr id: Option[String],
    @attr ref: Option[String],
    @xmlns(xsd) attribute: List[Attribute],
    @xmlns(xsd) attributeGroup: List[AttributeGroup],
) extends Global with Attributes

object AttributeGroup {
  implicit val attributeGroupElementDecoder: ElementDecoder[AttributeGroup] = derivation.semiauto.deriveElementDecoder
}