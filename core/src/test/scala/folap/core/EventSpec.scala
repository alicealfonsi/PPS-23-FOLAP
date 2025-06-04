package folap.core

import folap.core.multidimensionalModel._
import org.scalatest._

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

private case class RevenueExampleMeasure(override val value: Double)
    extends ExampleEventMeasure:
  type T = Double

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

  import CubeMockup.*
  it should "return its attributes given their names" in:
    import GeographicAttribute.*, ProductAttribute.*
    event1.findAttributesByNames(List("Shop", "Product")) shouldEqual
      List(
        Shop(
          Some(
            City(
              Some(Nation(Some(GeographicAttribute.TopAttribute()), "Italy")),
              "Bologna"
            )
          ),
          "Shop1"
        ),
        Product(
          Some(
            Type(
              Some(
                Category(Some(ProductAttribute.TopAttribute()), "Groceries")
              ),
              "Drink"
            )
          ),
          "Drink1"
        )
      )

  "An Iterable of events" should "match the name of an Attribute if all of them contain the Attribute" in:
    List(event1, event2).matchAttributeByName("City") shouldBe true
