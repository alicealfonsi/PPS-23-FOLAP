package folap.typingDSL

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import MeasureDSL.as
import MeasureDSL.measure

class MeasureBuilderSpec extends AnyFlatSpec with Matchers:

  "The Measure DSL" should "construct a Measure from DSL" in:
    val m = measure named "price" as Int
    m shouldBe Measure("price", Int)

  it should "construct a Measure from DSL" in:
    val m = measure named "price" as Double
    m shouldBe Measure("price", Double)

  it should "construct a Measure from DSL" in:
    val m = measure named "price" as Long
    m.name shouldBe "price"
    m shouldBe Measure("price", Long)

  it should "construct a Measure from DSL" in:
    val m = measure named "price" as Float
    m.name shouldBe "price"
    m shouldBe Measure("price", Float)
