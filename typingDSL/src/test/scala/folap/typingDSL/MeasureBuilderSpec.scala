package folap.typingDSL

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import MeasureDSL.as
import MeasureDSL.measure

class MeasureBuilderSpec extends AnyFlatSpec with Matchers:

  "The Measure DSL" should "construct a Measure[Int] from DSL" in:
    val m = "" measure "price" as "Int"
    m.map(_.name) shouldBe Some("price")
    m shouldBe Some(Measure("price", "Int"))

  it should "construct a Measure[Double] from DSL" in:
    val m = "" measure "price" as "Double"
    m.map(_.name) shouldBe Some("price")
    m shouldBe Some(Measure("price", "Double"))

  it should "construct a Measure[Long] from DSL" in:
    val m = "" measure "price" as "Long"
    m.map(_.name) shouldBe Some("price")
    m shouldBe Some(Measure("price", "Long"))

  it should "construct a Measure[Float] from DSL" in:
    val m = "" measure "price" as "Float"
    m.map(_.name) shouldBe Some("price")
    m shouldBe Some(Measure("price", "Float"))

  it should "return None for unsupported types" in:
    val m = "" measure "weight" as "BigDecimal"
    m.map(_.name) shouldBe None
    m shouldBe None
