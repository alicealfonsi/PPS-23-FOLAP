package folap.typingDSL

import _root_.folap.typingDSL.SeqBuilder.~->
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

import scala.language.postfixOps

import SeqBuilder.-->

class SeqBuilderSpec extends AnyFlatSpec with should.Matchers:
  "A SeqBuilder" should "create a Seq from two distinct strings" in:
    val first = "first"
    val second = "second"
    val seq = first --> second
    seq shouldEqual Seq(
      DimensionAttribute(first, false),
      DimensionAttribute(second, false)
    )

  "A SeqBuilder" should "create a Seq from a list and a string" in:
    val first = "first"
    val second = "second"
    val third = "third"
    val seq = first --> second --> third
    seq shouldEqual Seq(
      DimensionAttribute(first, false),
      DimensionAttribute(second, false),
      DimensionAttribute(third, false)
    )

  "A SeqBuilder" should "create a Seq from a list and a string, with an optional second level" in:
    val first = "first"
    val second = "second"
    val third = "third"
    val seq = first ~-> second --> third
    seq shouldEqual Seq(
      DimensionAttribute(first, false),
      DimensionAttribute(second, true),
      DimensionAttribute(third, false)
    )
