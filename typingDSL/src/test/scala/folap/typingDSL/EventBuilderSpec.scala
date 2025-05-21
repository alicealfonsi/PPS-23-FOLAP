package folap.typingDSL

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

import EventBuilder.event
import DimensionBuilder.dimension

class EventBuilderSpec extends AnyFlatSpec with should.Matchers:
  val eventName: String = "TestEvent"
  val d1: Dimension = "dimension1" dimension Seq("")
  val d2: Dimension = "dimension2" dimension Seq("")

  "An EventBuilder" should "build an Event from a name with the word named" in:
    val e = event named eventName
    e shouldEqual Event("TestEvent", Seq(), Seq())

  it should "build an Event from a dimension with the word having" in:
    val e = event named eventName having d1
    e shouldEqual Event(eventName, Seq(d1), Seq())

  it should "build an Event from more than one dimension with the word having followed by the word and" in:
    val e = event named eventName having d1 and d2
    e shouldEqual Event(eventName, Seq(d1, d2), Seq())
