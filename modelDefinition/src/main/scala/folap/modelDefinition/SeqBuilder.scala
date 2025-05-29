package folap.modelDefinition

object SeqBuilder:
  trait ToSeq[T]:
    def toSeq: Seq[T]

  implicit class SeqableString(val s: String) extends ToSeq[String]:
    override def toSeq: Seq[String] = Seq(s)

  implicit class SeqableStringSeq(val s: Seq[String]) extends ToSeq[String]:
    override def toSeq: Seq[String] = s

  extension (head: ToSeq[String])
    infix def -->(tail: String): Seq[String] =
      head.toSeq.appended(tail)
