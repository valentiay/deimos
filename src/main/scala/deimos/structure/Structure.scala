package deimos.structure

import java.nio.file.{Path, Paths}

import deimos.schema.WithNamespaces
import deimos.schema.classes._
import deimos.structure.operations.{Indices, ProcessGlobalElement, XsdContext, XsdMonad, XsdStack}

object Structure {
  def process(schemas: Map[Path, WithNamespaces[Schema]]): GeneratedPackageWrapper = {
    // URI -> Prefix
    val namespacesPrefixes: Map[String, String] =
      schemas.values
        .flatMap(schema => schema.namespaces.toList)
        .map { case (prefix, uri) => (uri, prefix) }
        .groupBy(_._1)
        .foldLeft((Map.empty[String, String], Set.empty[String], 0)) {
          case ((res, used, count), (uri, prefixes)) =>
            val possiblePrefixes =
              prefixes.collect { case (_, prefix) if prefix.nonEmpty => prefix }.toList.distinct
                .filterNot(used.contains)
            possiblePrefixes match {
              case p :: _ => (res.updated(uri, p), used + p, count)
              case Nil =>
                val p = s"ans$count"
                (res.updated(uri, p), used + p, count + 1)
            }
        }
        ._1

    val availableFiles = schemas.map {
      case (path, WithNamespaces(schema, _)) =>
        val importedFiles = // TODO: Namespace in `import`
          (schema.include.map(_.schemaLocation) ++ schema.`import`.map(_.schemaLocation)).map { schemaLocation =>
            val includedPath = Paths.get(schemaLocation)
            path.getParent.resolve(includedPath).normalize()
          }
        path -> (path :: importedFiles)
    }

    val schemasIndex = schemas.map { case (path, fileInfo) => path -> fileInfo.value }

    val indices = Indices(schemasIndex, availableFiles, namespacesPrefixes)

    schemas.foreach {
      case (path, WithNamespaces(schema, namespaces)) =>
        val targetNamespace = schema.targetNamespace.getOrElse("")

        def addToIndex[V <: Global](index: ImportsIndex[Path, GlobalName, V], typ: String)(v: V): Unit = {
          val name =
            v.name.orElse(v.id).getOrElse(throw InvalidSchema(s"Neither name nor id was provided for global $typ", ???))
          index.add(path, GlobalName(targetNamespace, name), v)
        }

        schema.complexType.foreach(addToIndex(indices.complexTypes, "complexType"))
        schema.simpleType.foreach(addToIndex(indices.simpleTypes, "simpleType"))
        schema.element.foreach(addToIndex(indices.elements, "element"))
        schema.attribute.foreach(addToIndex(indices.attributes, "attribute"))
        schema.group.foreach(addToIndex(indices.groups, "group"))
        schema.attributeGroup.foreach(addToIndex(indices.attributeGroups, "attributeGroup"))
        for { (prefix, uri) <- namespaces } { indices.namespaces.add(path, prefix, uri) }
    }

    val ctx = new XsdContext(indices, schemas.keys.head, XsdStack.empty)
    val generatedPackage = XsdMonad
      .traverse(schemas.toList) {
        case (path, WithNamespaces(schema, _)) =>
          XsdMonad
            .traverse(schema.element) { element =>
              ProcessGlobalElement(element)
            }
            .local(_.copy(currentPath = path))
      }
      .run(ctx, GeneratedPackage.empty)
      .value
      .fold(err => throw err, result => result._2)

    val imports =
      availableFiles.transform(
        (path, imports) => imports.filter(imprt => generatedPackage.files.contains(imprt) && imprt != path)
      )

    GeneratedPackageWrapper(
      namespacesPrefixes,
      generatedPackage.files,
      imports,
    )
  }
}
