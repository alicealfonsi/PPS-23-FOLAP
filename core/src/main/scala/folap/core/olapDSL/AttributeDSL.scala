package folap.core.olapDSL

import folap.core.EventAttribute

case class AttributeDSL private (
    override val name: String,
    override val value: String
) extends EventAttribute:
  override val parent: Option[EventAttribute] = None

object AttributeDSL:
  def apply(baseName: String, value: String): AttributeDSL =
    new AttributeDSL(baseName + "Attribute", value)
