package folap.core

import MultidimensionalModel._

trait SalesAttribute extends Attribute

type SalesMeasure = QuantitySold

case class QuantitySold(override val value: Int) extends Measure:
  type T = Int
  override def fromRaw(value: Int): QuantitySold = QuantitySold(value)

given Computable[QuantitySold] with
  extension (q: QuantitySold)
    def sum(other: QuantitySold): QuantitySold = QuantitySold(
      q.value + other.value
    )

case class SalesEvent(quantity: QuantitySold)
    extends Event[SalesAttribute, SalesMeasure]:
  override def dimensions: Iterable[SalesAttribute] = List()
  override def measures: Iterable[SalesMeasure] = List(quantity)

val quantitySoldValue1: Int = 1
val quantitySold1: QuantitySold = QuantitySold(quantitySoldValue1)
val quantitySoldValue2: Int = 2
val quantitySold2: QuantitySold = QuantitySold(quantitySoldValue2)
