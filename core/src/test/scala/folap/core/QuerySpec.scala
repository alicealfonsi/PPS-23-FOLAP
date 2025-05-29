package folap.core

import org.scalatest._

import flatspec._
import matchers._

class QuerySpec extends AnyFlatSpec with should.Matchers:

  "A query" should "start with an empty group-by set" in:
    val q = Query.create()
    val groupBySet: Set[_] = q.groupBySet
    groupBySet shouldBe empty
