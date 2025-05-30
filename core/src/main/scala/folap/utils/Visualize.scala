package folap.utils
import folap.core.Event
import folap.core.MultidimensionalModel._


private def extractAllDimensions(attr: Attribute): List[Attribute] =
  attr match
    case TopAttribute() => Nil
    case a =>
      a.parent.toList.flatMap(extractAllDimensions) ++ List(a)

def visualize(events: Iterable[Event[_, _]]): Unit =
  println("===== Cube Result =====")
  events.zipWithIndex.foreach { (event, idx) =>
    println(s"--- Event ${idx + 1} ---")

    val expanded = event.dimensions
      .flatMap(extractAllDimensions)
      .filter(_ != TopAttribute())

    val seen = scala.collection.mutable.LinkedHashSet[String]()
    val orderedDims = expanded.filter { a =>
      val key = a.getClass.getName + ":" + a.value
      if seen.contains(key) then false else { seen += key; true }
    }

    orderedDims.foreach { attr =>
      val name = attr.getClass.getSimpleName.replace("Attribute", "")
      println(s"$name: ${attr.value}")
    }

    event.measures.foreach { m =>
      val name = m.getClass.getSimpleName
      val value = m.value
      println(s"$name: $value")
    }

    println()
  }