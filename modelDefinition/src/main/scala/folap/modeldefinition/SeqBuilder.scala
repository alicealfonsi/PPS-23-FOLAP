package folap.modeldefinition

object SeqBuilder:
  trait ToSeq[T]:
    def toSeq: Seq[T]

  implicit class SeqableString(val s: String) extends ToSeq[String]:
    override def toSeq: Seq[String] = Seq(s)

  implicit class SeqableStringSeq(val s: Seq[String]) extends ToSeq[String]:
    override def toSeq: Seq[String] = s

  extension (head: ToSeq[String])
    /** Append a string to a Seq of strings, or generate a Seq of strings from
      * two strings
      *
      * @param tail
      *   The string to be appended
      * @return
      *   A Seq with the provided element appended
      */
    infix def -->(tail: String): Seq[String] =
      head.toSeq.appended(tail)
