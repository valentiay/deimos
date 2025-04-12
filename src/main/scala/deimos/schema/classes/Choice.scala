package deimos.schema.classes

import deimos.schema.classes.namespaces.xsd
import phobos.decoding.ElementDecoder
import phobos.derivation
import phobos.syntax.{attr, xmlns}

final case class Choice(
    @attr minOccurs: Option[Int],
    @attr maxOccurs: Option[String],
    @xmlns(xsd) element: List[Element],
    @xmlns(xsd) group: List[Group],
    @xmlns(xsd) choice: List[Choice],
    @xmlns(xsd) sequence: List[Sequence],
    @xmlns(xsd) all: List[All],
) extends Elements

object Choice {
  implicit val choiceElementDecoder: ElementDecoder[Choice] = derivation.semiauto.deriveElementDecoder
}