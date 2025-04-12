package deimos.schema.classes

import phobos.decoding.ElementDecoder
import phobos.derivation
import phobos.syntax.attr

final case class Include(@attr schemaLocation: String)

object Include {
  implicit val includeElementDecoder: ElementDecoder[Include] = derivation.semiauto.deriveElementDecoder
}