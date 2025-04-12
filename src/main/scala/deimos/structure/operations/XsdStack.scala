package deimos.structure.operations

import deimos.structure.GlobalName

class XsdStack private (val history: Set[GlobalName]) {
  def push(globalName: GlobalName): XsdStack = new XsdStack(history + globalName)
  def contains(globalName: GlobalName): Boolean = history.contains(globalName)
}

object XsdStack {
  def empty = new XsdStack(Set())
}