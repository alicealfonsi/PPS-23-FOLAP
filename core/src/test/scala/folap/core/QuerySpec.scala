package folap.core

import org.scalatest._

import flatspec._
import matchers._
import TestAttribute._

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
    val attribute = Day
    val resultingQuery = q.rollUp(attribute)
    resultingQuery.groupBySet should contain(attribute)

  it should "leave the group-by set unchanged when the attribute is lower than the attributes already in the group-by set" in:
    val resultingQuery = q
      .rollUp(Day)
      .rollUp(Hour)
    resultingQuery.groupBySet shouldBe Set(Day)

  it should "replace the attribute when requesting aggregation by parent" in:
    val resultingQuery = q
      .rollUp(Hour)
      .rollUp(Day)
    resultingQuery.groupBySet shouldBe Set(Day)

  "Performing a drill down" should "add the attribute to the group-by set" in:
    val attribute = Day
    val resultingQuery = q.drillDown(attribute)
    resultingQuery.groupBySet should contain(attribute)

  it should "leave the group-by set unchanged when the attribute in the group-by set is lower than the requested attribute" in:
    val resultingQuery = q
      .drillDown(Hour)
      .drillDown(Day)

    resultingQuery.groupBySet shouldBe Set(Hour)

  it should "replace the attribute already in the group-by when aggregating further down" in:
    val resultingQuery = q
      .drillDown(Day)
      .drillDown(Hour)

    resultingQuery.groupBySet shouldBe Set(Hour)
