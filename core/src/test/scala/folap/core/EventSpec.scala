package folap.core

import org.scalatest._

import MultidimensionalModel._
import flatspec._
import matchers._

trait ExampleEventAttribute extends Attribute
trait ExampleEventMeasure extends Measure
private case class DimensionExampleAttribute(
    override val parent: Option[TopAttribute],
    override val value: String
) extends ExampleEventAttribute
private case class QuantityExampleMeasure(override val value: Int)
    extends ExampleEventMeasure:
  type T = Int
  override def fromRaw(value: Int): QuantityExampleMeasure =
    QuantityExampleMeasure(value)
private case class RevenueExampleMeasure(override val value: Double)
    extends ExampleEventMeasure:
  type T = Double
  override def fromRaw(value: Double): RevenueExampleMeasure =
    RevenueExampleMeasure(value)
private val valueQ: Int = 10
private val valueR: Double = 17.5
private case class ExampleEvent()
    extends Event[ExampleEventAttribute, ExampleEventMeasure]:
  override def dimensions: Iterable[ExampleEventAttribute] =
    List(DimensionExampleAttribute(Some(TopAttribute()), ""))
  override def measures: Iterable[ExampleEventMeasure] = List(
    QuantityExampleMeasure(valueQ),
    RevenueExampleMeasure(valueR)
  )

class EventSpec extends AnyFlatSpec with should.Matchers:
  val dim: DimensionExampleAttribute =
    DimensionExampleAttribute(Some(TopAttribute()), "")
  val event: Event[ExampleEventAttribute, ExampleEventMeasure] =
    ExampleEvent()

  "An Event" should "have a list of dimensions" in:
    event.dimensions shouldEqual List(dim)

  it should "have a list of attributes" in:
    event.attributes shouldEqual List(dim, TopAttribute())

  it should "have a list of measures which can be of different types" in:
    event.measures shouldEqual List(
      QuantityExampleMeasure(10),
      RevenueExampleMeasure(17.5)
    )
