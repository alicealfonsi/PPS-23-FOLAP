package folap.modelDefinition

/** This object contains utility functions for the DSL
  */
object CodegenUtils:
  /** Transform a string into a valid class name
    * @param input
    *   the untrusted input
    * @return
    *   the PascalCase version of that string
    */
  def sanitise(input: String): String =
    input.split(" ").map(_.capitalize).foldLeft("")(_ + _)

  def indent(input: String, spaces: Int): String =
    input
      .split("\n")
      .map(" ".repeat(spaces) + _)
      .mkString("\n")

  def toCamelCase(input: String): String =
    Seq(input.head.toLower, input.tail).mkString("")
