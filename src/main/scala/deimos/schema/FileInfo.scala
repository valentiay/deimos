package deimos.schema

import deimos.schema.classes.Schema

final case class FileInfo(schema: Schema, namespaces: Map[String, String])