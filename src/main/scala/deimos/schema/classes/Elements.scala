package deimos.schema.classes

import deimos.schema.classes.namespaces.xsd
import phobos.configured.ElementCodecConfig
import phobos.decoding.ElementDecoder
import phobos.syntax.{attr, default, xmlns}
import phobos.derivation

sealed trait Elements {
  val items: List[ElementItem]
}

sealed trait ElementItem

object ElementItem {
  private val elementItemConfig =
    ElementCodecConfig.default.usingElementNamesAsDiscriminator.withConstructorNamesTransformed(_.toLowerCase)
  implicit val elementItemDecoder: ElementDecoder[ElementItem] =
    derivation.semiauto.deriveElementDecoderConfigured(elementItemConfig)
}

final case class Choice(
    @attr minOccurs: Option[Int],
    @attr maxOccurs: Option[String],
    @xmlns(xsd) annotation: Option[Annotation],
    @xmlns(xsd) any: List[Any],
    @xmlns(xsd) anyAttribute: List[AnyAttribute],
    @xmlns(xsd) @default items: List[ElementItem],
) extends Elements with ElementItem

object Choice {
  implicit val choiceElementDecoder: ElementDecoder[Choice] = derivation.semiauto.deriveElementDecoder
}

final case class Sequence(
    @attr minOccurs: Option[Int],
    @attr maxOccurs: Option[String],
    @xmlns(xsd) annotation: Option[Annotation],
    @xmlns(xsd) any: List[Any],
    @xmlns(xsd) anyAttribute: List[AnyAttribute],
    @xmlns(xsd) @default items: List[ElementItem],
) extends Elements with ElementItem

object Sequence {
  implicit val sequenceElementDecoder: ElementDecoder[Sequence] = derivation.semiauto.deriveElementDecoder
}

final case class Group(
    @attr minOccurs: Option[Int],
    @attr maxOccurs: Option[String],
    @attr ref: Option[String],
    @attr name: Option[String],
    @attr id: Option[String],
    @xmlns(xsd) annotation: Option[Annotation],
    @xmlns(xsd) any: List[Any],
    @xmlns(xsd) anyAttribute: List[AnyAttribute],
    @xmlns(xsd) @default items: List[ElementItem],
) extends Global with Elements with ElementItem

object Group {
  implicit val groupElementDecoder: ElementDecoder[Group] = derivation.semiauto.deriveElementDecoder
}

final case class Element(
    @attr name: Option[String],
    @attr id: Option[String],
    @attr `type`: Option[String],
    @attr ref: Option[String],
    @attr minOccurs: Option[Int],
    @attr maxOccurs: Option[String],
    @attr nillable: Option[Boolean],
    @attr form: Option[String],
    @xmlns(xsd) simpleType: Option[SimpleType],
    @xmlns(xsd) complexType: Option[ComplexType],
) extends Global with ElementItem

object Element {
  implicit val elementElementDecoder: ElementDecoder[Element] = derivation.semiauto.deriveElementDecoder
}

final case class All(
    @attr minOccurs: Option[Int],
    @attr maxOccurs: Option[Int],
    @xmlns(xsd) annotation: Option[Annotation],
    @xmlns(xsd) any: List[Any],
    @xmlns(xsd) anyAttribute: List[AnyAttribute],
    @xmlns(xsd) @default items: List[ElementItem],
) extends Elements with ElementItem

object All {
  implicit val allElementDecoder: ElementDecoder[All] = derivation.semiauto.deriveElementDecoder
}

final case class ComplexType(
    @attr name: Option[String],
    @attr id: Option[String],
    @attr mixed: Option[Boolean],
    @xmlns(xsd) attribute: List[Attribute],
    @xmlns(xsd) simpleContent: Option[SimpleContent],
    @xmlns(xsd) complexContent: Option[ComplexContent],
    @xmlns(xsd) attributeGroup: List[AttributeGroup],
    @xmlns(xsd) annotation: Option[Annotation],
    @xmlns(xsd) any: List[Any],
    @xmlns(xsd) anyAttribute: List[AnyAttribute],
    @xmlns(xsd) @default items: List[ElementItem],
) extends Global with Elements with Attributes

object ComplexType {
  implicit val complexTypeElementDecoder: ElementDecoder[ComplexType] = derivation.semiauto.deriveElementDecoder
}

final case class Extension(
    @attr base: String,
    @xmlns(xsd) attribute: List[Attribute],
    @xmlns(xsd) attributeGroup: List[AttributeGroup],
    @xmlns(xsd) annotation: Option[Annotation],
    @xmlns(xsd) any: List[Any],
    @xmlns(xsd) anyAttribute: List[AnyAttribute],
    @xmlns(xsd) @default items: List[ElementItem],
) extends Elements with Attributes

object Extension {
  implicit val extensionElementDecoder: ElementDecoder[Extension] = derivation.semiauto.deriveElementDecoder
}
