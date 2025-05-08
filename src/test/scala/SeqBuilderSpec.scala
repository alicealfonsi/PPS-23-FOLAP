import SeqBuilder.-->
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

import scala.language.postfixOps
import scala.math.Numeric.Implicits.infixNumericOps

class SeqBuilderSpec extends AnyFlatSpec with should.Matchers:
  "A SeqBuilder" should "create a Seq from two distinct strings" in:
    val first = "first"
    val second = "second"
    val seq = first --> second
    seq shouldEqual Seq(first, second)