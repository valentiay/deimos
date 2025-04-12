package deimos.schema.classes

import phobos.decoding.ElementDecoder
import phobos.derivation
import phobos.syntax.attr

final case class Restriction(@attr base: Option[String])

object Restriction {
  implicit val restrictionElementDecoder: ElementDecoder[Restriction] = derivation.semiauto.deriveElementDecoder
}
