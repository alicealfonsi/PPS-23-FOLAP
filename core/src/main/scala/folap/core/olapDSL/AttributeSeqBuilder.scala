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

  extension (name: String)
    infix def and(other: String): Seq[AttributeDSL] =
      Seq(AttributeDSL(name), AttributeDSL(other))

  extension (seq: Seq[AttributeDSL])
    infix def and(next: String): Seq[AttributeDSL] =
      seq :+ AttributeDSL(next)

  extension (seq: Seq[AttributeDSL])
    infix def and(attr: AttributeDSL): Seq[AttributeDSL] =
      seq :+ attr
