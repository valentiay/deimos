package deimos.structure.operations

import cats.syntax.flatMap._
import cats.syntax.functor._

import deimos.schema.classes.{All, Choice, Element, Elements, Group, Sequence}
import deimos.structure._

object ProcessElements {
  def processElement(element: Element): XsdMonad[List[Tag]] =
    ProcessLocalElement.apply(element).map(_.toList)

  def processChoice(choice: Choice): XsdMonad[List[Tag]] =
    for {
      rawNewTags <- ProcessElements(choice)
      minOccurs  = choice.minOccurs
      maxOccurs  = choice.maxOccurs
      newTags = (minOccurs, maxOccurs) match {
        case (_, Some("unbounded"))      => rawNewTags.map(element => element.copy(typ = element.typ.toListing))
        case (_, Some(a)) if a.toInt > 1 => rawNewTags.map(element => element.copy(typ = element.typ.toListing))
        case (Some(0), _)                => rawNewTags.map(element => element.copy(typ = element.typ.toOptional))
        case _                           => rawNewTags.map(element => element.copy(typ = element.typ.toOptional))
      }
    } yield newTags

  def processGroup(group: Group): XsdMonad[List[Tag]] =
    for {
      ctx <- XsdMonad.ask
      (newFile, realGroup) = group.ref match {
        case Some(ref) =>
          ctx.indices.groups
            .getItem(ctx.availableFiles, ctx.toGlobalName(ref))
            .getOrElse(throw InvalidSchema(s"$ref refrences to nothing", ctx.currentPath))
        case None => (ctx.currentPath, group)
      }
      rawNewTags <- ProcessElements(realGroup).local(_.copy(currentPath = newFile))
      minOccurs  = group.minOccurs.orElse(realGroup.minOccurs)
      maxOccurs  = group.maxOccurs.orElse(realGroup.maxOccurs)
      newTags = (minOccurs, maxOccurs) match {
        case (_, Some("unbounded"))      => rawNewTags.map(element => element.copy(typ = element.typ.toListing))
        case (_, Some(a)) if a.toInt > 1 => rawNewTags.map(element => element.copy(typ = element.typ.toListing))
        case (Some(0), _)                => rawNewTags.map(element => element.copy(typ = element.typ.toOptional))
        case _                           => rawNewTags
      }
    } yield newTags

  def processSequence(sequence: Sequence): XsdMonad[List[Tag]] =
    for {
      rawNewTags <- ProcessElements(sequence)
      minOccurs  = sequence.minOccurs
      maxOccurs  = sequence.maxOccurs
      newTags = (minOccurs, maxOccurs) match {
        case (_, Some("unbounded"))      => rawNewTags.map(element => element.copy(typ = element.typ.toListing))
        case (_, Some(a)) if a.toInt > 1 => rawNewTags.map(element => element.copy(typ = element.typ.toListing))
        case (Some(0), _)                => rawNewTags.map(element => element.copy(typ = element.typ.toOptional))
        case _                           => rawNewTags
      }
    } yield newTags

  def processAll(all: All): XsdMonad[List[Tag]] =
    for {
      rawNewTags <- ProcessElements(all)
      newTags = if (all.minOccurs.contains(0)) rawNewTags.map(element => element.copy(typ = element.typ.toOptional))
      else rawNewTags
    } yield newTags

  def apply(block: Elements): XsdMonad[List[Tag]] = {
    for {
      ctx <- XsdMonad.ask
      params <- XsdMonad.traverse(block.items) {
                 case c: Choice   => processChoice(c)
                 case s: Sequence => processSequence(s)
                 case g: Group    => processGroup(g)
                 case e: Element  => processElement(e)
                 case a: All      => processAll(a)
               }
    } yield ctx.deduplicateParams(params.flatten)
  }
}
