package folap.core

import folap.core.CubeMockup._
import org.scalatest._

import GeographicAttribute._
import flatspec._
import matchers._

class OperationalSpec extends AnyFlatSpec with should.Matchers:
  "Operational[SalesEvent]" should "return the correct sum of SalesEvents based on the specified group-by set" in:
    event1.sum(event2)(List("Nation", "Category")) shouldEqual SalesEvent(
      Nation(Some(TopAttribute()), "Italy"),
      Category(Some(ProductAttribute.TopAttribute()), "Groceries"),
      QuantitySold(3)
    )

  "Operational[SalesEvent]" should "return the correct aggregation by sum of SalesEvent" in:
    List(event1, event2, event3).aggregateBySum(
      "Nation"
    ) shouldEqual SalesEvent(
      Nation(Some(TopAttribute()), "Italy"),
      QuantitySold(10)
    )
