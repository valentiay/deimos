package deimos

import java.nio.file.Paths

import deimos.codegen.Codegen
import deimos.schema.Parser
import deimos.structure.Structure

object Main {
  def main(args: Array[String]): Unit = {
    args match {
      case Array(src, dest) =>
        val schemas = Parser.parseRecursive(Paths.get(src))
        val struct = Structure.process(schemas)
        val destDir = Paths.get(dest)
        Codegen.generate(struct, destDir)
      case _ =>
        System.err.println("[Error] Pass two params: source dir and destination dir")
    }
  }
}