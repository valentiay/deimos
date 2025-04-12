package deimos.schema.classes

import deimos.schema.classes.namespaces.xsd
import phobos.decoding.ElementDecoder
import phobos.derivation
import phobos.syntax.{attr, xmlns}

final case class Schema(
    @attr targetNamespace: Option[String],
    @attr elementFormDefault: Option[String],
    @attr attributeFormDefault: Option[String],
    @xmlns(xsd) include: List[Include],
    @xmlns(xsd) `import`: List[Import],
    @xmlns(xsd) element: List[Element],
    @xmlns(xsd) complexType: List[ComplexType],
    @xmlns(xsd) simpleType: List[SimpleType],
    @xmlns(xsd) attribute: List[Attribute],
    @xmlns(xsd) group: List[Group],
    @xmlns(xsd) attributeGroup: List[AttributeGroup],
)

object Schema {
  implicit val schemaElementDecoder: ElementDecoder[Schema] = derivation.semiauto.deriveElementDecoder
}
