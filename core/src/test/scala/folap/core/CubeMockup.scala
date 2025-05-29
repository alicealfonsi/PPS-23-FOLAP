package folap.core

import MultidimensionalModel._

trait SalesAttribute extends Attribute
trait GeographicAttribute extends SalesAttribute
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
  case class Shop(override val parent: Option[City], override val value: String)
      extends GeographicAttribute

type SalesMeasure = QuantitySold

case class QuantitySold(override val value: Int) extends Measure:
  type T = Int
  override def fromRaw(value: Int): QuantitySold = QuantitySold(value)

given Computable[QuantitySold] with
  extension (q: QuantitySold)
    def sum(other: QuantitySold): QuantitySold = QuantitySold(
      q.value + other.value
    )

case class SalesEvent(where: GeographicAttribute, quantity: QuantitySold)
    extends Event[SalesAttribute, SalesMeasure]:
  override def dimensions: Iterable[SalesAttribute] = List(where)
  override def measures: Iterable[SalesMeasure] = List(quantity)

val quantitySoldValue1: Int = 1
val quantitySold1: QuantitySold = QuantitySold(quantitySoldValue1)
val quantitySoldValue2: Int = 2
val quantitySold2: QuantitySold = QuantitySold(quantitySoldValue2)
