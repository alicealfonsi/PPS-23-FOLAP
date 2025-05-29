package folap.core

import MultidimensionalModel._

trait SalesAttribute extends Attribute

type SalesMeasure = QuantitySold

case class QuantitySold(override val value: Int) extends Measure:
  type T = Int
  override def fromRaw(value: Int): QuantitySold = QuantitySold(value)

case class SalesEvent(quantity: QuantitySold)
    extends Event[SalesAttribute, SalesMeasure]:
  override def dimensions: Iterable[SalesAttribute] = List()
  override def measures: Iterable[SalesMeasure] = List(quantity)
