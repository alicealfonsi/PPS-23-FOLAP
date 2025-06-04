package mycompany.dw

import folap.core.AggregationOp._
import folap.core.Event
import folap.core.EventConstructor
import folap.core.multidimensionalmodel._
import folap.olapDSL.AttributeDSLBuilder._
import folap.olapDSL.AttributeSeqBuilder._
import folap.olapDSL.QueryDSL
import folap.olapDSL.QueryDSLBuilder._
import folap.utils.visualize

val SalesCube = QueryDSL(SalesEvents.salesEvents)
val CustomerCareCube = QueryDSL(CustomerCareEvents.customerCareEvents)

case class ResultEvent[A <: Attribute, M <: Measure](
    override val dimensions: Iterable[A],
    override val measures: Iterable[M]
) extends Event[A, M]
given EventConstructor[A <: Attribute, M <: Measure, E <: Event[A, M]]
    : EventConstructor[A, M, E] =
  (
      attributes: Iterable[A],
      measures: Iterable[M]
  ) => ResultEvent(attributes, measures).asInstanceOf[E]

@main def main(): Unit =
  val filtered = SalesCube where ("City" is "Berlin" and ("Month" is "January"))
  println("- SLICE AND DICE RESULT -")
  visualize(filtered.cube)
  val union = SalesCube union CustomerCareCube
  println("- DRILL ACROSS RESULT -")
  visualize(union.cube)
  val aggregated = Sum of SalesCube by "City"
  println("- ROLL UP RESULT -")
  visualize(aggregated.cube)
