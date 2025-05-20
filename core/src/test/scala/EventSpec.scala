package folap.core

import MultidimensionalModel._
import org.scalatest._

import flatspec._
import matchers._

trait ExampleEventAttribute extends EventAttribute
trait ExampleEventMeasure[T] extends EventMeasure[T]
private case class DimensionExampleAttribute(
    override val parent: Option[TopAttribute],
    override val value: String
) extends ExampleEventAttribute
private case class QuantityExampleMeasure(value: Int)
    extends ExampleEventMeasure[Int]:
  override def fromRaw(value: Int): Measure[Int] = QuantityExampleMeasure(value)
private val value: Int = 10
private case class ExampleEvent()
    extends Event[ExampleEventAttribute, ExampleEventMeasure[_]]:
  override def dimensions: Iterable[ExampleEventAttribute] =
    List(DimensionExampleAttribute(Some(TopAttribute()), ""))
  override def measures: Iterable[ExampleEventMeasure[_]] = List(
    QuantityExampleMeasure(value)
  )

class EventSpec extends AnyFlatSpec with should.Matchers:
  val dim: DimensionExampleAttribute =
    DimensionExampleAttribute(Some(TopAttribute()), "")
  val event: Event[ExampleEventAttribute, ExampleEventMeasure[_]] =
    ExampleEvent()

  "An Event" should "have a list of dimensions" in:
    event.dimensions shouldEqual List(dim)

  it should "have a list of attributes" in:
    event.attributes shouldEqual List(dim, TopAttribute())

  it should "have a list of measures" in:
    event.measures shouldEqual List(QuantityExampleMeasure(10))
