package deimos.schema.classes

import phobos.Namespace

object namespaces {
  val xsdUri = "http://www.w3.org/2001/XMLSchema"

  case object xsd
  implicit val xsdNamespace: Namespace[xsd.type] =
    Namespace.mkInstance(xsdUri)
}
