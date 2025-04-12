package deimos.schema.classes

import phobos.decoding.ElementDecoder
import phobos.derivation

final case class XsdList()

object XsdList {
  implicit val xsdListElementDecoder: ElementDecoder[XsdList] = derivation.semiauto.deriveElementDecoder
}