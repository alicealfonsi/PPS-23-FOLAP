package folap.typingDSL

object SeqBuilder:
  trait ToSeq[T]:
    def toSeq: Seq[T]

  implicit class SeqableAttribute(val a: String)
      extends ToSeq[DimensionAttribute]:
    override def toSeq: Seq[DimensionAttribute] = Seq(
      DimensionAttribute(a, false)
    )

  implicit class SeqableAttributeSeq(val s: Seq[DimensionAttribute])
      extends ToSeq[DimensionAttribute]:
    override def toSeq: Seq[DimensionAttribute] = s

  extension (head: ToSeq[DimensionAttribute])
    infix def -->(tail: String): Seq[DimensionAttribute] =
      head.toSeq.appended(DimensionAttribute(tail, false))
    infix def ~->(tail: String): Seq[DimensionAttribute] =
      head.toSeq.appended(DimensionAttribute(tail, true))
