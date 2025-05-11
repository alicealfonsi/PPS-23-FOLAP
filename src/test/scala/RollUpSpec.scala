import MultidimensionalModel._
import Operators._
import org.scalatest._

import flatspec._
import matchers._

class RollUpSpec extends AnyFlatSpec with should.Matchers:
  trait SalesAttribute extends EventAttribute
  trait SalesMeasure[T] extends EventMeasure[T]
  case class TopAttribute() extends SalesAttribute:
    override val parent: Option[SalesAttribute] = None
    override val value: String = ""
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
  case class CategoryAttribute(
      override val parent: Option[TopAttribute],
      override val value: String
  ) extends SalesAttribute
  case class TypeAttribute(
      override val parent: Option[CategoryAttribute],
      override val value: String
  ) extends SalesAttribute
  case class ProductAttribute(
      override val parent: Option[TypeAttribute],
      override val value: String
  ) extends SalesAttribute
  private case class QuantitySoldMeasure(value: Int) extends SalesMeasure[Int]:
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
  val categoryAttribute12: CategoryAttribute =
    CategoryAttribute(Some(TopAttribute()), "Groceries")
  val typeAttribute12: TypeAttribute =
    TypeAttribute(Some(categoryAttribute12), "Drink")
  val productAttribute12: ProductAttribute =
    ProductAttribute(Some(typeAttribute12), "Drink1")
  val salesEvent3: SalesEvent =
    SalesEvent(List(shopAttribute1, productAttribute12), List())
  val salesEvent4: SalesEvent =
    SalesEvent(List(shopAttribute2, productAttribute12), List())

  private case class ResultEvent(
      override val dimensions: Iterable[SalesAttribute],
      override val measures: Iterable[SalesMeasure[_]]
  ) extends Event[SalesAttribute, SalesMeasure[_]]
  private def createEvent: EventConstructor[SalesAttribute, SalesMeasure[_]] =
    (
        dimensions: Iterable[SalesAttribute],
        measures: Iterable[SalesMeasure[_]]
    ) => ResultEvent(dimensions, measures)

  "RollUp" should "aggregate only if the group-by attribute matches one of the attributes of each event" in:
    val groupByAttributeName = "ClientAttribute"
    rollUp(List(salesEvent1, salesEvent2))(List(groupByAttributeName))(
      createEvent
    ) shouldEqual List(
      salesEvent1,
      salesEvent2
    )

  it should "search for the group-by attribute among the dimensions" in:
    val groupByAttributeName = "ShopAttribute"
    rollUp(List(salesEvent1, salesEvent2))(List(groupByAttributeName))(
      createEvent
    ) shouldEqual List(
      ResultEvent(salesEvent1.dimensions, List()),
      ResultEvent(salesEvent2.dimensions, List())
    )

  it should "search for the group-by attribute moving up attributes hierarchies" in:
    val groupByAttributeName = "NationAttribute"
    rollUp(List(salesEvent1, salesEvent2))(List(groupByAttributeName))(
      createEvent
    ) shouldEqual List(
      ResultEvent(List(nationAttribute12), List())
    )

  it should "aggregate along the entire hierarchy for dimensions for which no attribute is specified in the group-by set" in:
    val groupByAttributeName = "NationAttribute"
    rollUp(List(salesEvent3, salesEvent4))(List(groupByAttributeName))(
      createEvent
    ) shouldEqual
      List(ResultEvent(List(nationAttribute12, TopAttribute()), List()))
