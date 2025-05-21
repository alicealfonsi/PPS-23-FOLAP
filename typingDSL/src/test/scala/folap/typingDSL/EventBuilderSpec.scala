package folap.typingDSL

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

import EventBuilder.event

class EventBuilderSpec extends AnyFlatSpec with should.Matchers:
  val eventName: String = "TestEvent"

  "An EventBuilder" should "build an Event from a name with the word named" in:
    val e = event named eventName
    e shouldEqual Event("TestEvent", Seq(), Seq())
