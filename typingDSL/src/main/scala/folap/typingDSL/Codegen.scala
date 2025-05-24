package folap.typingDSL

import folap.typingDSL.DSLUtils.sanitise
import folap.typingDSL.DSLUtils.indent

object Codegen:
  def generate(dimension: Dimension): String =
    val sanitised = sanitise(dimension.name)
    s"sealed trait ${sanitised}Dimension\nobject ${sanitised}Dimension:" +
      dimension.attributes
        .zip(dimension.attributes.tail)
        .map((current, parent) => (sanitise(current), sanitise(parent)))
        .map((current, parent) =>
          s"case class ${current}(value: String, parent: ${parent})"
        )
        .map(indent(_, 2))
        .mkString("\n") +
      s"case class ${{ sanitise(dimension.attributes.last) }}(value: String)"
