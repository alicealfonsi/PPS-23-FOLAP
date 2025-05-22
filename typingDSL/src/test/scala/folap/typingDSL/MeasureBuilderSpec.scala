package folap.typingDSL

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import MeasureDSL.as
import MeasureDSL.measure

class MeasureBuilderSpec extends AnyFlatSpec with Matchers:

  "The Measure DSL" should "construct a Int Measure from DSL" in:
    val m = measure named "price" as "Int"
    m.map(_.name) shouldBe Some("price")
    m shouldBe Some(Measure("price", "Int"))

  it should "construct a Double Measure from DSL" in:
    val m = measure named "price" as "Double"
    m.map(_.name) shouldBe Some("price")
    m shouldBe Some(Measure("price", "Double"))

  it should "construct a Long Measure from DSL" in:
    val m = measure named "price" as "Long"
    m.map(_.name) shouldBe Some("price")
    m shouldBe Some(Measure("price", "Long"))

  it should "construct a Float Measure from DSL" in:
    val m = measure named "price" as "Float"
    m.map(_.name) shouldBe Some("price")
    m shouldBe Some(Measure("price", "Float"))

  it should "return None for unsupported types" in:
    val m = measure named "weight" as "BigDecimal"
    m.map(_.name) shouldBe None
    m shouldBe None
