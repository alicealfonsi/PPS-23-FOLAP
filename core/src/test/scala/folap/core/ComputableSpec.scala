package folap.core

import folap.core.CubeMockup.ProductAttribute.Category
import folap.core.CubeMockup.ProductAttribute.Product
import folap.core.CubeMockup.ProductAttribute.Type
import folap.core.CubeMockup._
import org.scalatest._

import GeographicAttribute._
import AggregationOp._
import flatspec._
import matchers._

class ComputableSpec extends AnyFlatSpec with should.Matchers:
  "Computable[SalesEvent]" should "return the correct sum of SalesEvents based on the specified group-by set" in:
    event1.sum(event2)(List("Nation", "Category")) shouldEqual SalesEvent(
      Nation(Some(TopAttribute()), "Italy"),
      Category(Some(ProductAttribute.TopAttribute()), "Groceries"),
      QuantitySold(3)
    )

  "Computable[SalesEvent]" should "return the correct division of a SalesEvent by an integer" in:
    val n = 3
    event3 div n shouldEqual
      SalesEvent(
        Shop(
          Some(
            City(
              Some(Nation(Some(GeographicAttribute.TopAttribute()), "Italy")),
              "Cesena"
            )
          ),
          "Shop3"
        ),
        Product(
          Some(
            Type(
              Some(
                Category(Some(ProductAttribute.TopAttribute()), "Groceries")
              ),
              "Food"
            )
          ),
          "Food1"
        ),
        QuantitySold(2)
      )

  "Computable[SalesEvent]" should "return the correct minimum of SalesEvents based on the specified group-by set" in:
    event1.min(event2)(List("Nation", "Category")) shouldEqual SalesEvent(
      Nation(Some(GeographicAttribute.TopAttribute()), "Italy"),
      Category(Some(ProductAttribute.TopAttribute()), "Groceries"),
      QuantitySold(1)
    )

  "Computable[SalesEvent]" should "return the correct maximum of SalesEvents based on the specified group-by set" in:
    event1.max(event2)(List("Nation", "Category")) shouldEqual SalesEvent(
      Nation(Some(GeographicAttribute.TopAttribute()), "Italy"),
      Category(Some(ProductAttribute.TopAttribute()), "Groceries"),
      QuantitySold(2)
    )

  "Computable[SalesEvent]" should "return the correct aggregation of a SalesEvent based on the specified group-by set" in:
    event3.aggregate(List("Type", "Nation")) shouldEqual
      SalesEvent(
        Nation(Some(GeographicAttribute.TopAttribute()), "Italy"),
        Type(
          Some(Category(Some(ProductAttribute.TopAttribute()), "Groceries")),
          "Food"
        ),
        QuantitySold(7)
      )

  "Computable[SalesEvent]" should "return the correct aggregation by sum of a SalesEvent based on the specified group-by set" in:
    List(event3).aggregateBy(Sum)(
      List("Nation", "Category")
    ) shouldEqual SalesEvent(
      Nation(Some(GeographicAttribute.TopAttribute()), "Italy"),
      Category(Some(ProductAttribute.TopAttribute()), "Groceries"),
      QuantitySold(7)
    )

  "Computable[SalesEvent]" should "return the correct aggregation by sum of SalesEvents based on the specified group-by set" in:
    List(event1, event2, event3).aggregateBy(Sum)(
      List("Nation", "Category")
    ) shouldEqual SalesEvent(
      Nation(Some(TopAttribute()), "Italy"),
      Category(Some(ProductAttribute.TopAttribute()), "Groceries"),
      QuantitySold(10)
    )

  "Computable[SalesEvent]" should "return the correct aggregation by average of a SalesEvent based on the specified group-by set" in:
    List(event3).aggregateBy(Avg)(
      List("Nation", "Category")
    ) shouldEqual SalesEvent(
      Nation(Some(GeographicAttribute.TopAttribute()), "Italy"),
      Category(Some(ProductAttribute.TopAttribute()), "Groceries"),
      QuantitySold(7)
    )

  "Computable[SalesEvent]" should "return the correct aggregation by average of SalesEvents based on the specified group-by set" in:
    List(event1, event2, event3).aggregateBy(Avg)(
      List("Nation", "Category")
    ) shouldEqual SalesEvent(
      Nation(Some(TopAttribute()), "Italy"),
      Category(Some(ProductAttribute.TopAttribute()), "Groceries"),
      QuantitySold(3)
    )

  "Computable[SalesEvent]" should "return the correct aggregation by minimum of a SalesEvent based on the specified group-by set" in:
    List(event3).aggregateBy(Min)(
      List("Nation", "Category")
    ) shouldEqual SalesEvent(
      Nation(Some(GeographicAttribute.TopAttribute()), "Italy"),
      Category(Some(ProductAttribute.TopAttribute()), "Groceries"),
      QuantitySold(7)
    )

  "Computable[SalesEvent]" should "return the correct aggregation by minimum of SalesEvents based on the specified group-by set" in:
    List(event1, event2, event3).aggregateBy(Min)(
      List("Nation", "Category")
    ) shouldEqual SalesEvent(
      Nation(Some(TopAttribute()), "Italy"),
      Category(Some(ProductAttribute.TopAttribute()), "Groceries"),
      QuantitySold(1)
    )

  "Computable[SalesEvent]" should "return the correct aggregation by maximum of a SalesEvent based on the specified group-by set" in:
    List(event3).aggregateBy(Max)(
      List("Nation", "Category")
    ) shouldEqual SalesEvent(
      Nation(Some(GeographicAttribute.TopAttribute()), "Italy"),
      Category(Some(ProductAttribute.TopAttribute()), "Groceries"),
      QuantitySold(7)
    )

  "Computable[SalesEvent]" should "return the correct aggregation by maximum of SalesEvents based on the specified group-by set" in:
    List(event1, event2, event3).aggregateBy(Max)(
      List("Nation", "Category")
    ) shouldEqual SalesEvent(
      Nation(Some(TopAttribute()), "Italy"),
      Category(Some(ProductAttribute.TopAttribute()), "Groceries"),
      QuantitySold(7)
    )
