import Cube.Measure
import org.scalatest._

import flatspec._
import matchers._

case class ExampleMeasure[T: Numeric](override val value: T)
    extends Measure[T]

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
