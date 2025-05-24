package folap.typingDSL

import folap.typingDSL.DSLUtils.sanitise

object Codegen:
  def generate(dimension: Dimension): String =
    val sanitised = sanitise(dimension.name)
    s"""sealed trait ${sanitised}Dimension
object ${sanitised}Dimension:"""
