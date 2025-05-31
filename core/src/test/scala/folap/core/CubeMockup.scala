package folap.core

import MultidimensionalModel._

object CubeMockup:
  trait SalesAttribute extends Attribute
  trait GeographicAttribute extends SalesAttribute
  trait ProductAttribute extends SalesAttribute
  object GeographicAttribute:
    case class TopAttribute() extends GeographicAttribute:
      override val parent: Option[Attribute] = None
      override val value: String = ""
    case class Nation(
        override val parent: Option[TopAttribute],
        override val value: String
    ) extends GeographicAttribute
    case class City(
        override val parent: Option[Nation],
        override val value: String
    ) extends GeographicAttribute
    case class Shop(
        override val parent: Option[City],
        override val value: String
    ) extends GeographicAttribute
  object ProductAttribute:
    case class TopAttribute() extends ProductAttribute:
      override val parent: Option[Attribute] = None
      override val value: String = ""
    case class Category(
        override val parent: Option[TopAttribute],
        override val value: String
    ) extends ProductAttribute
    case class Type(
        override val parent: Option[Category],
        override val value: String
    ) extends ProductAttribute
    case class Product(
        override val parent: Option[Type],
        override val value: String
    ) extends ProductAttribute

  type SalesMeasure = QuantitySold

  case class QuantitySold(override val value: Int) extends Measure:
    type T = Int
    override def fromRaw(value: Int): QuantitySold = QuantitySold(value)

  given Computable[QuantitySold] with
    extension (q: QuantitySold)
      def div(n: Int): QuantitySold = QuantitySold(q.value / n)

  case class SalesEvent(
      where: GeographicAttribute,
      what: ProductAttribute,
      quantity: QuantitySold
  ) extends Event[SalesAttribute, SalesMeasure]:
    override def dimensions: Iterable[SalesAttribute] = List(where, what)
    override def measures: Iterable[SalesMeasure] = List(quantity)

  given Operational[SalesAttribute, SalesMeasure, SalesEvent] with
    extension (e: SalesEvent)
      def sum(other: SalesEvent)(groupBySet: Iterable[String]): SalesEvent =
        val aggregated = e.aggregate(groupBySet)
        SalesEvent(
          aggregated.where,
          aggregated.what,
          QuantitySold(aggregated.quantity.value + other.quantity.value)
        )
      def div(n: Int): SalesEvent =
        SalesEvent(e.where, e.what, e.quantity div n)
      def min(other: SalesEvent)(groupBySet: Iterable[String]): SalesEvent =
        val aggregated = e.aggregate(groupBySet)
        SalesEvent(aggregated.where, aggregated.what, QuantitySold(math.min(e.quantity.value, other.quantity.value)))
      def aggregate(groupBySet: Iterable[String]): SalesEvent =
        SalesEvent(
          e.where.upToLevel(
            e.where.searchCorrespondingAttributeName(groupBySet)
          ),
          e.what.upToLevel(e.what.searchCorrespondingAttributeName(groupBySet)),
          e.quantity
        )

  import GeographicAttribute.*, ProductAttribute.*
  val nation123: Nation =
    Nation(Some(GeographicAttribute.TopAttribute()), "Italy")
  val city1: City = City(Some(nation123), "Bologna")
  val shop1: Shop = Shop(Some(city1), "Shop1")
  val category123: Category =
    Category(Some(ProductAttribute.TopAttribute()), "Groceries")
  val type12: Type = Type(Some(category123), "Drink")
  val product12: ProductAttribute = Product(Some(type12), "Drink1")
  val quantitySoldValue1: Int = 1
  val quantitySold1: QuantitySold = QuantitySold(quantitySoldValue1)
  val city23: City = City(Some(nation123), "Cesena")
  val shop2: Shop = Shop(Some(city23), "Shop2")
  val quantitySoldValue2: Int = 2
  val quantitySold2: QuantitySold = QuantitySold(quantitySoldValue2)
  val shop3: Shop = Shop(Some(city23), "Shop3")
  val type3: Type = Type(Some(category123), "Food")
  val product3: ProductAttribute = Product(Some(type3), "Food1")
  val quantitySoldValue3: Int = 7
  val quantitySold3: QuantitySold = QuantitySold(quantitySoldValue3)

  val event1: SalesEvent = SalesEvent(shop1, product12, quantitySold1)
  val event2: SalesEvent = SalesEvent(shop2, product12, quantitySold2)
  val event3: SalesEvent = SalesEvent(shop3, product3, quantitySold3)
