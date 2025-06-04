package folap.olapdsl

import folap.core.AggregationOp._
import folap.core.CubeMockup._
import folap.core._
import folap.olapdsl.AttributeSeqBuilder._
import folap.olapdsl.QueryDSLBuilder._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import GeographicAttribute._
import ProductAttribute._
import folap.olapdsl.QueryDSL

class RollUpDSLSpec extends AnyFlatSpec with Matchers:

  "The DSL syntax for roll up" should "aggregate only if at least one of the attributes in the group-by set matches all events" in:
    val Sales = QueryDSL(Seq(event1, event2))
    val result = Max of Sales by "Client"
    result.cube shouldEqual Seq(event1, event2)

  it should "search for attributes in the group-by set among the dimensions" in:
    val Sales = QueryDSL(Seq(event1, event2))
    val result = Max of Sales by ("Shop" and "Product")
    result.cube shouldEqual Seq(event1, event2)

  it should "search for attributes in the group-by set by moving up the attributes hierarchies" in:
    val Sales = QueryDSL(Seq(event1, event2))
    val attribute = "Nation" and "Top"
    val n = attribute.head.name
    val n1 = attribute.tail.head.name
    print(n + n1)
    val result = Sum of Sales by ("Nation" and "Top")
    result.cube shouldEqual Seq(
      SalesEvent(
        Nation(Some(GeographicAttribute.TopAttribute()), "Italy"),
        ProductAttribute.TopAttribute(),
        QuantitySold(3)
      )
    )

  it should "aggregate along the entire hierarchy for dimensions for which no attribute is specified in the group-by set" in:
    val Sales = QueryDSL(Seq(event1, event2, event3))
    val result = Sum of Sales by "Nation"
    result.cube shouldEqual List(
      SalesEvent(
        Nation(Some(GeographicAttribute.TopAttribute()), "Italy"),
        ProductAttribute.TopAttribute(),
        QuantitySold(10)
      )
    )
  it should "aggregate foreach attribute specified in the group-by set" in:
    val Sales = QueryDSL(Seq(event1, event2, event3))
    val result = Sum of Sales by ("Type" and "Nation")
    result.cube shouldEqual List(
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

  it should "return a set of secondary events resulting from the aggregation using the sum operator of multiple primary events according to the attributes in the group-by set" in:
    val Sales = QueryDSL(Seq(event1, event2, event3))
    val result = Sum of Sales by ("Nation" and "Product")
    result.cube shouldEqual List(
      SalesEvent(
        Nation(Some(GeographicAttribute.TopAttribute()), "Italy"),
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
        QuantitySold(7)
      ),
      SalesEvent(
        Nation(Some(GeographicAttribute.TopAttribute()), "Italy"),
        Product(
          Some(
            Type(
              Some(
                Category(Some(ProductAttribute.TopAttribute()), "Groceries")
              ),
              "Drink"
            )
          ),
          "Drink1"
        ),
        QuantitySold(3)
      )
    )
  it should "return a set of secondary events resulting from the aggregation using the avg operator of multiple primary events according to the attributes in the group-by set" in:
    val Sales = QueryDSL(Seq(event1, event2, event3))
    val result = Avg of Sales by ("Nation" and "Product")
    result.cube shouldEqual List(
      SalesEvent(
        Nation(Some(GeographicAttribute.TopAttribute()), "Italy"),
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
        QuantitySold(7)
      ),
      SalesEvent(
        Nation(Some(GeographicAttribute.TopAttribute()), "Italy"),
        Product(
          Some(
            Type(
              Some(
                Category(Some(ProductAttribute.TopAttribute()), "Groceries")
              ),
              "Drink"
            )
          ),
          "Drink1"
        ),
        QuantitySold(1)
      )
    )
  it should "return a set of secondary events resulting from the aggregation using the min operator of multiple primary events according to the attributes in the group-by set" in:
    val Sales = QueryDSL(Seq(event1, event2, event3))
    val result = Min of Sales by ("Nation" and "Product")
    result.cube shouldEqual List(
      SalesEvent(
        Nation(Some(GeographicAttribute.TopAttribute()), "Italy"),
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
        QuantitySold(7)
      ),
      SalesEvent(
        Nation(Some(GeographicAttribute.TopAttribute()), "Italy"),
        Product(
          Some(
            Type(
              Some(
                Category(Some(ProductAttribute.TopAttribute()), "Groceries")
              ),
              "Drink"
            )
          ),
          "Drink1"
        ),
        QuantitySold(1)
      )
    )
  it should "return a set of secondary events resulting from the aggregation using the max operator of multiple primary events according to the attributes in the group-by set" in:
    val Sales = QueryDSL(Seq(event1, event2, event3))
    val result = Max of Sales by ("Nation" and "Product")
    result.cube shouldEqual List(
      SalesEvent(
        Nation(Some(GeographicAttribute.TopAttribute()), "Italy"),
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
        QuantitySold(7)
      ),
      SalesEvent(
        Nation(Some(GeographicAttribute.TopAttribute()), "Italy"),
        Product(
          Some(
            Type(
              Some(
                Category(Some(ProductAttribute.TopAttribute()), "Groceries")
              ),
              "Drink"
            )
          ),
          "Drink1"
        ),
        QuantitySold(2)
      )
    )
