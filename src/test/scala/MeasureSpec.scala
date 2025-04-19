import Cube.Measure
import org.scalatest._

import flatspec._
import matchers._

case class ExampleMeasure[T: Numeric](override val value: T)
    extends Measure[T] {
  override def fromRaw(value: T): Measure[T] = ExampleMeasure(value)
}

class MeasureSpec
    extends AnyFlatSpec
    with should.Matchers
    with BeforeAndAfterEach:
  "A Measure" should "accept an integer value" in:
    val value: Int = 42
    val measure = ExampleMeasure(value)
    measure.value shouldEqual value

  it should "accept a long value" in:
    val value: Long = 4242
    val measure = ExampleMeasure(value)
    measure.value shouldEqual value

  it should "accept a float value" in:
    val value: Float = 42.42
    val measure = ExampleMeasure(value)
    measure.value shouldEqual value

  it should "accept a double value" in:
    val value: Double = 424242.424242
    val measure = ExampleMeasure(value)
    measure.value shouldEqual value

  it should "be comparable: less than" in:
    val measureA = ExampleMeasure(10)
    val measureB = ExampleMeasure(20)
    measureA should be < measureB

  it should "be comparable: greater than" in:
    val measureA = ExampleMeasure(20)
    val measureB = ExampleMeasure(10)
    measureA should be > measureB

  it should "be comparable: equal" in:
    val measureA = ExampleMeasure(10)
    val measureB = ExampleMeasure(10)
    measureA shouldEqual measureB

  it should "return a correct sum" in:
    val valueA = 10
    val valueB = 20
    val measureA = ExampleMeasure(valueA)
    val measureB = ExampleMeasure(valueB)
    (measureA + measureB).value shouldEqual valueA + valueB

  it should "return a correct difference" in:
    val valueA = 10
    val valueB = 20
    val measureA = ExampleMeasure(valueA)
    val measureB = ExampleMeasure(valueB)
    (measureA - measureB).value shouldEqual valueA - valueB
