package folap.core

import org.scalatest._

import flatspec._
import matchers._

class ComputableSpec extends AnyFlatSpec with should.Matchers:
  "Computable[QuantitySold]" should "return the correct sum of QuantitySold" in:
    quantitySold1 sum quantitySold2 shouldEqual QuantitySold(3)
