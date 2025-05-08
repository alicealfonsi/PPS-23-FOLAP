object SeqBuilder:
  extension (head: String)
    infix def -->(tail: String): Seq[String] =
      Seq(head, tail)