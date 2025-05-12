import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class DSLUtilsSpec  extends AnyFlatSpec with should.Matchers:
  "The DSL Utilities" should "generate a valid ID from a string (capitalisation)" in :
    val input = "sales"
    val sanitised = DSLUtils.sanitise(input)
    sanitised shouldEqual "Sales"

  it should "generate a valid ID from a string (space substitution)" in :
    val input = "sales in africa"
    val sanitised = DSLUtils.sanitise(input)
    sanitised shouldEqual "SalesInAfrica"
