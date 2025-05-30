package folap.core

import folap.core.AggregationStrategies.AggregationOperator._
import folap.core.CubeMockup._
import org.scalatest._

import flatspec._
import matchers._
import Operators.rollUp
import GeographicAttribute._
import ProductAttribute._

class RollUpSpec extends AnyFlatSpec with should.Matchers:
  "RollUp" should "aggregate only if at least one of the attributes in the group-by set matches all events" in:
    val events = List(event1, event2)
    val groupBySet = List("Client")
    rollUp(events)(groupBySet)(Sum) shouldEqual List(event1, event2)

  it should "search for attributes in the group-by set among the dimensions" in:
    val events = List(event1, event2)
    val groupBySet = List("Shop", "Product")
    rollUp(events)(groupBySet)(Sum) shouldEqual List(event1, event2)

  it should "search for attributes in the group-by set by moving up the attributes hierarchies" in:
    val events = List(event1, event2)
    val groupBySet = List("Nation", "TopAttribute")
    rollUp(events)(groupBySet)(Sum) shouldEqual List(
      SalesEvent(
        Nation(Some(GeographicAttribute.TopAttribute()), "Italy"),
        ProductAttribute.TopAttribute(),
        QuantitySold(3)
      )
    )

  it should "aggregate along the entire hierarchy for dimensions for which no attribute is specified in the group-by set" in:
    val events = List(event1, event2, event3)
    val groupBySet = List("Nation")
    rollUp(events)(groupBySet)(Sum) shouldEqual List(
      SalesEvent(
        Nation(Some(GeographicAttribute.TopAttribute()), "Italy"),
        ProductAttribute.TopAttribute(),
        QuantitySold(10)
      )
    )

  it should "aggregate foreach attribute specified in the group-by set" in:
    val events = List(event1, event2, event3)
    val groupBySet = List("Type", "Nation")
    rollUp(events)(groupBySet)(Sum) shouldEqual List(
      SalesEvent(
        Nation(Some(GeographicAttribute.TopAttribute()), "Italy"),
        Type(
          Some(Category(Some(ProductAttribute.TopAttribute()), "Groceries")),
          "Food"
        ),
        QuantitySold(7)
      ),
      SalesEvent(
        Nation(Some(GeographicAttribute.TopAttribute()), "Italy"),
        Type(
          Some(Category(Some(ProductAttribute.TopAttribute()), "Groceries")),
          "Drink"
        ),
        QuantitySold(3)
      )
    )
