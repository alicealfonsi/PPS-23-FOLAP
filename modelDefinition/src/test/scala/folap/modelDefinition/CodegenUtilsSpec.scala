package folap.modelDefinition

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class CodegenUtilsSpec extends AnyFlatSpec with should.Matchers:
  "The DSL Utilities" should "generate a valid ID from a string (capitalisation)" in:
    val input = "sales"
    val sanitised = CodegenUtils.sanitise(input)
    sanitised shouldEqual "Sales"

  it should "generate a valid ID from a string (space substitution)" in:
    val input = "sales in africa"
    val sanitised = CodegenUtils.sanitise(input)
    sanitised shouldEqual "SalesInAfrica"

  it should "indent by a set number of spaces, single line" in:
    val input = "test"
    val indented = CodegenUtils.indent(input, 4)
    indented shouldEqual "    test"

  it should "indent by a set number of spaces, multiple lines" in:
    val input = "test\ndata"
    val indented = CodegenUtils.indent(input, 4)
    indented shouldEqual "    test\n    data"

  it should "indent by a set number of spaces, multiple lines, different indentations" in:
    val input = "test\n    data"
    val indented = CodegenUtils.indent(input, 4)
    indented shouldEqual "    test\n        data"

  it should "transform a string from PascalCase to camelCase" in:
    val pascalCase = "TestString"
    val camelCase = CodegenUtils.toCamelCase(pascalCase)
    camelCase shouldEqual "testString"
