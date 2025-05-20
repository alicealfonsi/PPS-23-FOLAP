import MeasureDSL.as
import MeasureDSL.measure
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class MeasureBuilderSpec extends AnyFlatSpec with Matchers:

  "The Measure DSL" should "construct a Measure[Int] from DSL" in:
    val m = "" measure "price" as "Int"
    m shouldBe Measure[Int]("price", "Int")

  it should "construct a Measure[Double] from DSL" in:
    val m = "" measure "price" as "Double"
    m shouldBe Measure[Double]("price", "Double")

  it should "construct a Measure[Long] from DSL" in:
    val m = "" measure "price" as "Long"
    m.name shouldBe "price"
    m shouldBe Measure[Double]("price", "Long")

  it should "construct a Measure[Float] from DSL" in:
    val m = "" measure "price" as "Float"
    m.name shouldBe "price"
    m shouldBe Measure[Float]("price", "Float")

  it should "throw an exception for unsupported types" in:
    an[RuntimeException] should be thrownBy {
      "" measure "weight" as "String"
    }
