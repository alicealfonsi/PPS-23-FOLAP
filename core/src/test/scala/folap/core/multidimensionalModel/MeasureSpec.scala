package folap.core.multidimensionalModel

import folap.core.multidimensionalmodel.Measure
import org.scalatest._
import org.scalatest.flatspec._
import org.scalatest.matchers._

class MeasureSpec extends AnyFlatSpec with should.Matchers:
  private case class IntMeasure(override val value: Int) extends Measure:
    type T = Int

  private case class LongMeasure(override val value: Long) extends Measure:
    type T = Long

  private case class BigIntMeasure(override val value: BigInt) extends Measure:
    type T = BigInt

  private case class FloatMeasure(override val value: Float) extends Measure:
    type T = Float

  private case class DoubleMeasure(override val value: Double) extends Measure:
    type T = Double

  private case class BigDecimalMeasure(override val value: BigDecimal)
      extends Measure:
    type T = BigDecimal

  "A Measure" should "have a name equal to the class name" in:
    val value: Int = 10
    val measure: IntMeasure = IntMeasure(value)
    measure.name shouldEqual "IntMeasure"

  it should "accept an Int value" in:
    val value: Int = 42
    val measure: IntMeasure = IntMeasure(value)
    measure.value shouldEqual value

  it should "accept a Long value" in:
    val value: Long = 4242
    val measure: LongMeasure = LongMeasure(value)
    measure.value shouldEqual value

  it should "accept a BigInt value" in:
    val value: BigInt = BigInt("424242")
    val measure: BigIntMeasure = BigIntMeasure(value)
    measure.value shouldEqual value

  it should "accept a Float value" in:
    val value: Float = 42.42
    val measure: FloatMeasure = FloatMeasure(value)
    measure.value shouldEqual value

  it should "accept a Double value" in:
    val value: Double = 424242.424242
    val measure: DoubleMeasure = DoubleMeasure(value)
    measure.value shouldEqual value

  it should "accept a BigDecimal value" in:
    val value: BigDecimal = BigDecimal("42424242.42424242")
    val measure: BigDecimalMeasure = BigDecimalMeasure(value)
    measure.value shouldEqual value
