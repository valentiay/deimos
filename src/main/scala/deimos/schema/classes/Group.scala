package deimos.schema.classes

import deimos.schema.classes.namespaces.xsd
import phobos.decoding.ElementDecoder
import phobos.derivation
import phobos.syntax.{attr, xmlns}

final case class Group(
    @attr minOccurs: Option[Int],
    @attr maxOccurs: Option[String],
    @attr ref: Option[String],
    @attr name: Option[String],
    @attr id: Option[String],
    @xmlns(xsd) all: List[All],
    @xmlns(xsd) choice: List[Choice],
    @xmlns(xsd) sequence: List[Sequence],
    @xmlns(xsd) group: List[Group],
    @xmlns(xsd) element: List[Element],
) extends Global with Elements

object Group {
  implicit val groupElementDecoder: ElementDecoder[Group] = derivation.semiauto.deriveElementDecoder
}