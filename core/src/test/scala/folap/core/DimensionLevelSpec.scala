package folap.core

import org.scalatest._

import flatspec._
import matchers._
import folap.core.TestAttribute.{Month, Quarter}

private enum TestAttribute extends DimensionLevel:
  case Hour
  case Day
  case Month
  case Quarter
  override def parent: Option[DimensionLevel] = this match
    case Hour    => Some(Day)
    case Day     => Some(Month)
    case Month   => Some(Quarter)
    case Quarter => None

class DimensionLevelSpec
    extends AnyFlatSpec
    with should.Matchers
    with BeforeAndAfterEach:
  "A DimensionLevel" should "have an optional parent" in:
    val a: DimensionLevel = Month
    a.parent should be equals (Some(Quarter))
