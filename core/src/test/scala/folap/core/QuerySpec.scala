package folap.core

import org.scalatest._

import flatspec._
import matchers._

class QuerySpec
    extends AnyFlatSpec
    with should.Matchers
    with BeforeAndAfterEach:
  var q: Query[String] = Query.create()

  override protected def beforeEach(): Unit = q = Query.create()

  "A query" should "start with an empty group-by set" in:
    val groupBySet: Set[_] = q.groupBySet
    groupBySet shouldBe empty

  "Performing a roll up" should "add the attributes to the group by set" in:
    val attribute = "a"
    val resultingQuery = q.rollUp(attribute)
    resultingQuery.groupBySet should contain(attribute)
