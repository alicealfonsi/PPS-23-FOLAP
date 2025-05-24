package folap.typingDSL

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import MeasureDSL.measure

class MeasureBuilderSpec extends AnyFlatSpec with Matchers:

  "The Measure DSL" should "construct a Measure[Int] from DSL" in:
    val m = Int measure "price"
    m shouldBe Measure("price", Int)

  it should "construct a Measure[Double] from DSL" in:
    val m = Double measure "price"
    m shouldBe Measure("price", Double)

  it should "construct a Measure[Long] from DSL" in:
    val m = Long measure "price"
    m.name shouldBe "price"
    m shouldBe Measure("price", Long)

  it should "construct a Measure[Float] from DSL" in:
    val m = Float measure "price"
    m.name shouldBe "price"
    m shouldBe Measure("price", Float)
