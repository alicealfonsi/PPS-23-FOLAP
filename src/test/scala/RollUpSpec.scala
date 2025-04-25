import MultidimensionalModel._
import Operators._
import org.scalatest._

import flatspec._
import matchers._

class RollUpSpec extends AnyFlatSpec with should.Matchers:

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
    override def fromRaw(value: Int): Measure[Int] = QuantitySoldMeasure(value)

  case class SalesEvent(
      override val dimensions: Iterable[SalesAttribute],
      override val measures: Iterable[SalesMeasure[_]]
  ) extends Event[SalesAttribute, SalesMeasure[_]]

  val nationAttribute12: NationAttribute =
    NationAttribute(Some(TopAttribute()), "Italy")

  val cityAttribute1: CityAttribute =
    CityAttribute(Some(nationAttribute12), "Bologna")
  val shopAttribute1: ShopAttribute =
    ShopAttribute(Some(cityAttribute1), "Shop1")
  val quantitySoldValue1: Int = 40
  val salesEvent1: SalesEvent =
    SalesEvent(
      List(shopAttribute1),
      List(QuantitySoldMeasure(quantitySoldValue1))
    )

  val cityAttribute2: CityAttribute =
    CityAttribute(Some(nationAttribute12), "Cesena")
  val shopAttribute2: ShopAttribute =
    ShopAttribute(Some(cityAttribute2), "Shop2")
  val quantitySoldValue2: Int = 25
  val salesEvent2: SalesEvent =
    SalesEvent(
      List(shopAttribute2),
      List(QuantitySoldMeasure(quantitySoldValue2))
    )

  "RollUp" should "aggregate only if the group-by attribute matches one of the attributes of each event" in:
    val groupByAttribute = "ClientAttribute"
    rollUp(List(salesEvent1, salesEvent2))(groupByAttribute) shouldEqual List(
      salesEvent1,
      salesEvent2
    )

  it should "search for the group-by attribute among the dimensions" in:
    val groupByAttribute = "ShopAttribute"
    rollUp(List(salesEvent1, salesEvent2))(groupByAttribute) shouldEqual List()
