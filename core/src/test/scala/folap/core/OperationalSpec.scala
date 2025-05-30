package folap.core

import folap.core.CubeMockup.ProductAttribute.Category
import folap.core.CubeMockup.ProductAttribute.Type
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

  "Operational[SalesEvent]" should "return the correct aggregation of a SalesEvent based on the specified group-by set" in:
    event3.aggregate(List("Type", "Nation")) shouldEqual
      SalesEvent(
        Nation(Some(GeographicAttribute.TopAttribute()), "Italy"),
        Type(Some(category123), "Food"),
        QuantitySold(7)
      )

  "Operational[SalesEvent]" should "return the correct aggregation by sum of SalesEvents based on the specified group-by set" in:
    List(event1, event2, event3).aggregateBySum(
      List("Nation", "Category")
    ) shouldEqual SalesEvent(
      Nation(Some(TopAttribute()), "Italy"),
      Category(Some(ProductAttribute.TopAttribute()), "Groceries"),
      QuantitySold(10)
    )
