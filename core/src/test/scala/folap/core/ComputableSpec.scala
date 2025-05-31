package folap.core

import org.scalatest._

import flatspec._
import matchers._
import CubeMockup._

class ComputableSpec extends AnyFlatSpec with should.Matchers:
  "Computable[QuantitySold]" should "return the correct division of a QuantitySold by an integer" in:
    val n = 3
    quantitySold3 div n shouldEqual QuantitySold(2)
