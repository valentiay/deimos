package deimos.schema.classes

import deimos.schema.classes.namespaces.xsd
import phobos.decoding.ElementDecoder
import phobos.derivation
import phobos.syntax.{attr, xmlns}

final case class Extension(
  @attr base: String,
  @xmlns(xsd) choice: List[Choice],
  @xmlns(xsd) sequence: List[Sequence],
  @xmlns(xsd) group: List[Group],
  @xmlns(xsd) element: List[Element],
  @xmlns(xsd) all: List[All],
  @xmlns(xsd) attribute: List[Attribute],
  @xmlns(xsd) attributeGroup: List[AttributeGroup],
) extends Elements with Attributes

object Extension {
  implicit val extensionElementDecoder: ElementDecoder[Extension] = derivation.semiauto.deriveElementDecoder
}