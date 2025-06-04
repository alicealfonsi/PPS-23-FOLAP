package folap.utils
import folap.core.Event
import folap.core.multidimensionalModel._

private def extractAllDimensions(attr: Attribute): List[Attribute] =
  attr match
    case TopAttribute() => Nil
    case a =>
      a.parent.toList.flatMap(extractAllDimensions) ++ List(a)

def visualize(events: Iterable[Event[_, _]]): Unit =
  println("===== Cube =====")
  events.zipWithIndex.foreach { (event, idx) =>
    println(s"--- Event ${idx + 1} ---")

    val expanded = event.attributes
      .flatMap(extractAllDimensions)
      .filter(_.name != "TopAttribute")

    val seen = scala.collection.mutable.LinkedHashSet[String]()
    val orderedDims = expanded.filter { a =>
      val key = a.name + ":" + a.value
      if seen.contains(key) then false else { seen += key; true }
    }

    orderedDims.foreach { attr =>
      val name = attr.name
      println(s"$name: ${attr.value}")
    }

    event.measures.foreach { m =>
      val name = m.name
      val value = m.value
      println(s"$name: $value")
    }

    println()
  }
