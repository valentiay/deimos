package deimos.schema

import phobos.decoding.{Cursor, DecodingError, ElementDecoder}

final case class WithNamespaces[T](value: T, namespaces: Map[String, String])

object WithNamespaces {
  implicit def withNamespacesElementDecoder[T](
      implicit itemDecoder: ElementDecoder[T]): ElementDecoder[WithNamespaces[T]] =
    new ElementDecoder[WithNamespaces[T]] {
      override def decodeAsElement(c: Cursor,
                                   localName: String,
                                   namespaceUri: Option[String]): ElementDecoder[WithNamespaces[T]] = {
        println(s"Current state: ${c.getEventType}")
        val namespaces =
          (for (i <- 0 until c.getNamespaceCount) yield c.getNamespacePrefix(i) -> c.getNamespaceURI(i)).toMap

        itemDecoder.decodeAsElement(c, localName, namespaceUri).map(res => WithNamespaces(res, namespaces))
      }

      override def result(history: => List[String]): Either[DecodingError, WithNamespaces[T]] =
        Left(ElementDecoder.decodingNotCompleteError(history))

      override def isCompleted: Boolean = false
    }
}
