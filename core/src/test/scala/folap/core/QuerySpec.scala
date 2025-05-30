package folap.core

import org.scalatest._

import flatspec._
import matchers._
import folap.core.MultidimensionalModel.Attribute

class QuerySpec
    extends AnyFlatSpec
    with should.Matchers
    with BeforeAndAfterEach:
  var q: Query[TestAttribute] = Query.create()

  override protected def beforeEach(): Unit = q = Query.create()

  "A query" should "start with an empty group-by set" in:
    val groupBySet: Set[_] = q.groupBySet
    groupBySet shouldBe empty

  "Performing a roll up" should "add the attributes to the group by set" in:
    val attribute = TestAttribute.Day
    val resultingQuery = q.rollUp(attribute)
    resultingQuery.groupBySet should contain(attribute)
