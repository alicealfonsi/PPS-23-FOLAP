package folap.core

import folap.core.GeographicAttribute._
import org.scalatest._

import flatspec._
import matchers._

class OperationalSpec extends AnyFlatSpec with should.Matchers:
  "Operational[SalesEvent]" should "return the correct sum of SalesEvent" in:
    event1.sum(event2)("Nation") shouldEqual SalesEvent(
      Nation(Some(TopAttribute()), "Italy"),
      QuantitySold(3)
    )
