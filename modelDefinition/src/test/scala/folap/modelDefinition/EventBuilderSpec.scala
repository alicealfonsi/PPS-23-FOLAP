package folap.modelDefinition

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

import EventBuilder.event
import DimensionBuilder.dimension
import MeasureDSL.measure

class EventBuilderSpec extends AnyFlatSpec with should.Matchers:
  val eventName: String = "TestEvent"
  val d1: Dimension = "dimension1" dimension Seq("")
  val d2: Dimension = "dimension2" dimension Seq("")
  val m1: Measure = measure named "measure1" as Int
  val m2: Measure = measure named "measure2" as Int

  "An EventBuilder" should "build an empty Event from a name with the word named" in:
    val e = event named eventName
    e shouldEqual Event("TestEvent", Seq(), Seq())

  it should "build an Event from a dimension with the word having" in:
    val e = event named eventName having d1
    e shouldEqual Event(eventName, Seq(d1), Seq())

  it should "build an Event from more than one dimension with the word having followed by the word and" in:
    val e = event named eventName having d1 and d2
    e shouldEqual Event(eventName, Seq(d1, d2), Seq())

  it should "build an Event from a measure with the word having" in:
    val e = event named eventName having m1
    e shouldEqual Event(eventName, Seq(), Seq(m1))

  it should "build an Event from more than one measure with the word having followed by the word and" in:
    val e = event named eventName having m1 and m2
    e shouldEqual Event(eventName, Seq(), Seq(m1, m2))

  it should "build an Event from name, dimensions and measures" in:
    val e = event named eventName having d1 and d2 and m1 and m2
    e shouldEqual Event(eventName, Seq(d1, d2), Seq(m1, m2))
