package deimos.schema.classes

import deimos.schema.classes.namespaces.xsd
import phobos.decoding.ElementDecoder
import phobos.derivation
import phobos.syntax.{attr, xmlns}

final case class Sequence(
    @attr minOccurs: Option[Int],
    @attr maxOccurs: Option[String],
    @xmlns(xsd) element: List[Element],
    @xmlns(xsd) choice: List[Choice],
    @xmlns(xsd) group: List[Group],
    @xmlns(xsd) sequence: List[Sequence],
    @xmlns(xsd) all: List[All],
) extends Elements

object Sequence {
  implicit val sequenceElementDecoder: ElementDecoder[Sequence] = derivation.semiauto.deriveElementDecoder
}
