package folap.core.olapDSLSpec

import folap.core._
import folap.core.olapDSL.AttributeDSLBuilder._
import folap.core.olapDSL.AttributeSeqBuilder._
import folap.core.olapDSL.QueryDSLBuilder.by
import folap.core.olapDSL.QueryDSLBuilder.of
import folap.core.olapDSL._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import AggregationOp._

class RollUpDSLSpec extends AnyFlatSpec with Matchers:

  trait SalesAttribute extends EventAttribute
  trait SalesMeasure[T] extends EventMeasure[T]

  case class NationAttribute(
      override val parent: Option[TopAttribute],
      override val value: String
  ) extends SalesAttribute
  case class CityAttribute(
      override val parent: Option[NationAttribute],
      override val value: String
  ) extends SalesAttribute
  case class ShopAttribute(
      override val parent: Option[CityAttribute],
      override val value: String
  ) extends SalesAttribute

  case class QuantitySoldMeasure(value: Int) extends SalesMeasure[Int]:
    override def fromRaw(value: Int): QuantitySoldMeasure = QuantitySoldMeasure(
      value
    )

  case class SalesEvent(
      override val dimensions: Iterable[SalesAttribute],
      override val measures: Iterable[SalesMeasure[_]]
  ) extends Event[SalesAttribute, SalesMeasure[_]]

  case class ResultEvent[A <: EventAttribute, M <: EventMeasure[_]](
      override val dimensions: Iterable[A],
      override val measures: Iterable[M]
  ) extends Event[A, M]

  given EventConstructor[A <: EventAttribute, M <: EventMeasure[_]]
      : EventConstructor[A, M] =
    (
        attributes: Iterable[A],
        measures: Iterable[M]
    ) => ResultEvent(attributes, measures)

  val nationAttr = NationAttribute(Some(TopAttribute()), "Italy")
  val city1 = CityAttribute(Some(nationAttr), "Bologna")
  val city2 = CityAttribute(Some(nationAttr), "Cesena")
  val shop1 = ShopAttribute(Some(city1), "Shop1")
  val shop2 = ShopAttribute(Some(city2), "Shop2")

  val event1 = SalesEvent(List(shop1), List(QuantitySoldMeasure(40)))
  val event2 = SalesEvent(List(shop2), List(QuantitySoldMeasure(25)))

  val Sales = QueryDSL(Seq(event1, event2))

  "The rollUp DSL" should "work with more attributes" in:
    val Client = "Client"
    val Year = "Year"
    val result = Max of Sales by (Client and Year)
    result.cube shouldEqual Seq(event1, event2)

  it should "work with a single attribute" in:
    val Client = "Client"
    val result = Max of Sales by Client
    result.cube shouldEqual Seq(event1, event2)
