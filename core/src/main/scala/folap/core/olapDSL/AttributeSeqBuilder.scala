package folap.core.olapDSL

object AttributeSeqBuilder:

  trait ToAttributeSeq:
    def toSeq: Seq[AttributeDSL]

  implicit class SingleAttribute(val attr: AttributeDSL) extends ToAttributeSeq:
    override def toSeq: Seq[AttributeDSL] = Seq(attr)

  implicit class AttributeSeq(val attrs: Seq[AttributeDSL])
      extends ToAttributeSeq:
    override def toSeq: Seq[AttributeDSL] = attrs

  extension (head: ToAttributeSeq)
    infix def and(tail: AttributeDSL): Seq[AttributeDSL] =
      head.toSeq.appended(tail)
