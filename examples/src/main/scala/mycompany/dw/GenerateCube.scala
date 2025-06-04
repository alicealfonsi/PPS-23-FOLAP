package mycompany.dw

import folap.modelDefinition.Codegen
import folap.modelDefinition.DimensionBuilder.dimension
import folap.modelDefinition.EventBuilder.event
import folap.modelDefinition.MeasureDSL.measure
import folap.modelDefinition.SeqBuilder.-->

@main def generate =
  val geoDimension = "geographic" dimension "shop" --> "town"
  val quantity = measure named "quantity" as Int
  val e = event named "sales" having geoDimension and quantity
  println(Codegen.generate(e))
