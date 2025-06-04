package folap.modeldefinition

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

  /** Indent a string by a given amount of spaces
    * @param input
    *   The string to be indented
    * @param spaces
    *   The number of spaces to add to the left of each line
    * @return
    *   The indented string
    */
  def indent(input: String, spaces: Int): String =
    input
      .split("\n")
      .map(" ".repeat(spaces) + _)
      .mkString("\n")

  /** Transform a PascalCase string to camelCase
    * @param input
    *   The string to be converted
    * @return
    *   The string as camelCase
    */
  def toCamelCase(input: String): String =
    Seq(input.head.toLower, input.tail).mkString("")
