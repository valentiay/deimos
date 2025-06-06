package deimos.structure.operations

import cats.syntax.flatMap._
import cats.syntax.functor._

import deimos.schema.classes.Element
import deimos.structure.{GeneratedPackage, GlobalName, InvalidSchema, Pure, Tag, XmlCodecInfo}

object ProcessGlobalElement {
  def apply(element: Element): XsdMonad[Option[Tag]] = {

    def generateTypeByName(name: String): String = name.updated(0, name.head.toUpper)

    def name: XsdMonad[String] =
      element.name match {
        case Some(name) => XsdMonad.pure(name)
        case None       => XsdMonad.raiseError("Global element name is missing")
      }

    def namespace: XsdMonad[Option[String]] =
      for {
        ctx    <- XsdMonad.ask
        schema = ctx.indices.schemas(ctx.currentPath)
      } yield
        (schema.targetNamespace, schema.elementFormDefault, element.form) match {
          case (Some(uri), _, Some("qualified"))    => ctx.indices.namespacePrefixes.get(uri)
          case (Some(uri), Some("qualified"), None) => ctx.indices.namespacePrefixes.get(uri)
          case _                                    => None
        }

    element match {
      case _ if element.simpleType.isDefined =>
        for {
          ctx       <- XsdMonad.ask
          typ       <- ProcessSimpleType(element.simpleType.get)
          name      <- name
          namespace <- namespace
          xmlCodec  = XmlCodecInfo(typ, name, namespace)
          _         <- XsdMonad.addXmlCodec(ctx.currentPath, xmlCodec)
        } yield Some(Tag(name, Pure(typ), namespace, None, None))

      case _ if element.complexType.isDefined =>
        for {
          ctx          <- XsdMonad.ask
          name         <- name
          realTypeName = generateTypeByName(name) // TODO: Children classes here
          globalName   = GlobalName(ctx.targetNamespace.getOrElse(""), name)
          clazz <- ProcessComplexType(element.complexType.get, realTypeName, Some(globalName))
                    .local(_.copy(stack = ctx.stack.push(globalName)))
          typ       = Pure(clazz.name)
          namespace <- namespace
          xmlCodec  = XmlCodecInfo(clazz.name, name, namespace)
          _         <- XsdMonad.addXmlCodec(ctx.currentPath, xmlCodec)
        } yield Some(Tag(name, typ, namespace, None, None))

      case _ if element.`type`.isDefined =>
        for {
          ctx            <- XsdMonad.ask
          globalTypeName = element.`type`.map(ctx.toGlobalName).get
          typ <- XsdMonad.getSimpleTypeByName(globalTypeName).flatMap {
                  case Some(simpleType) =>
                    XsdMonad.pure(Pure(simpleType))
                  case None =>
                    XsdMonad.getOrProcessClassName(globalTypeName).map(Pure)
                }
          name      <- name
          namespace <- namespace
          xmlCodec  = XmlCodecInfo(typ.typ, name, namespace)
          _         <- XsdMonad.addXmlCodec(ctx.currentPath, xmlCodec)
        } yield Some(Tag(name, typ, namespace, None, None))
    }
  }
}
