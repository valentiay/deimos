package deimos.schema

import java.io.File
import java.nio.file.{Files, Path}
import com.fasterxml.aalto.stax.InputFactoryImpl

import javax.xml.stream.XMLStreamConstants
import deimos.schema.classes.{Definitions, Schema}
import deimos.schema.classes.namespaces.{wsdl, xsd}
import phobos.Namespace
import phobos.decoding.{Cursor, ElementDecoder, XmlDecoder, XmlStreamReader}
import cats.syntax.option._

import scala.collection.JavaConverters._
import scala.util.{Failure, Success, Try}

object Parser {
  def parseRecursive(root: Path, prefix: Option[String] = None): Map[Path, WithNamespaces[Schema]] = {
    def prefixed(f: File) = {
      val name = f.getName
      prefix.fold(name)(p => s"$p/${f.getName}")
    }

    val current = prefix.fold(root)(root.resolve)

    val (dirs, files) = Files.newDirectoryStream(current).iterator.asScala.map(_.toFile).partition(_.isDirectory)

    val schema = files.flatMap { file =>
      val name = prefixed(file)
      if (name.endsWith(".xsd")) List(parseSingleXsd(root, name))
      else if (name.endsWith(".wsdl")) parseSingleWsdl(root, name)
      else Nil
    }

    dirs.flatMap(dir => parseRecursive(root, Some(prefixed(dir)))).toMap ++ schema
  }

  def parseSingleXsd(root: Path, name: String): (Path, WithNamespaces[Schema]) = {
    Try {
      val string = Files.readString(root.resolve(name))
      val decoder =
        XmlDecoder.fromElementDecoderNs[WithNamespaces[Schema], xsd.type]("schema", xsd)
      val schema = decoder.decode(clearString(string)).fold(e => throw e, identity)
      root.resolve(name).normalize() -> schema
    } match {
      case Failure(exception) => throw new Exception(s"Error happened while decoding ${root.resolve(name)}", exception)
      case Success(value)     => value
    }
  }

  def parseSingleWsdl(root: Path, name: String): List[(Path, WithNamespaces[Schema])] =
    Try {
      val string = Files.readString(root.resolve(name))
      val decoder =
        XmlDecoder.fromElementDecoderNs[WithNamespaces[Definitions], wsdl.type]("definitions", wsdl)
      val definitions =
        decoder.decode(clearString(string)).fold(e => throw e, identity)
      definitions.value.types match {
        case Some(Definitions.Types(List(schema))) =>
          List(root.resolve(name).normalize() -> schema.copy(namespaces = definitions.namespaces ++ schema.namespaces))
        case Some(Definitions.Types(schemas)) =>
          schemas.zipWithIndex.map {
            case (schema, idx) =>
              root.resolve(name).resolve(s"schema$idx").normalize() ->
                schema.copy(namespaces = definitions.namespaces ++ schema.namespaces)
          }
        case _ => Nil
      }
    } match {
      case Failure(exception) => throw new Exception(s"Error happened while decoding ${root.resolve(name)}", exception)
      case Success(value)     => value
    }

  def clearString(string: String): String =
    string.filterNot(c => c == '\uffef' || c == '\ufeff')
}
