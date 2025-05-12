object DSLUtils:
  def sanitise(input: String): String =
    input.split(" ").map(_.capitalize).foldLeft("")(_ + _)
