package folap.typingDSL
import folap.typingDSL.MeasureDSL._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class MeasureBuilderSpec extends AnyFlatSpec with Matchers:

  "The Measure DSL" should "construct a Int Measure from DSL" in:
    val m = measure named "price" as Int
    m.name shouldBe "price"
    m.typology shouldBe Int
    m shouldBe Measure("price", Int)

  it should "construct a Double Measure from DSL" in:
    val m = measure named "price" as Double
    m.name shouldBe "price"
    m.typology shouldBe Double
    m shouldBe Measure("price", Double)

  it should "construct a Long Measure from DSL" in:
    val m = measure named "price" as Long
    m.name shouldBe "price"
    m.typology shouldBe Long
    m shouldBe Measure("price", Long)

  it should "construct a Float Measure from DSL" in:
    val m = measure named "price" as Float
    m.name shouldBe "price"
    m.typology shouldBe Float
    m shouldBe Measure("price", Float)
