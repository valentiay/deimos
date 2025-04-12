package deimos.schema.classes

import phobos.decoding.ElementDecoder
import phobos.derivation

final case class Union()

object Union {
  implicit val unionElementDecoder: ElementDecoder[Union] = derivation.semiauto.deriveElementDecoder
}