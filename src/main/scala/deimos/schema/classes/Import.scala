package deimos.schema.classes

import phobos.decoding.ElementDecoder
import phobos.derivation
import phobos.syntax.attr

final case class Import(@attr namespace: String, @attr schemaLocation: String)

object Import {
  implicit val importElementDecoder: ElementDecoder[Import] = derivation.semiauto.deriveElementDecoder
}