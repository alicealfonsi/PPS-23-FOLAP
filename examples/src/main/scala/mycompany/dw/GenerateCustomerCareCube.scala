package mycompany.dw

import folap.modelDefinition.Codegen
import folap.modelDefinition.DimensionBuilder.dimension
import folap.modelDefinition.EventBuilder.event
import folap.modelDefinition.MeasureDSL.measure
import folap.modelDefinition.SeqBuilder.-->

@main def generateCustomerCare: Unit =
  val geographic = "geographic" dimension "City" --> "Country" --> "Region"
  val satisfaction = measure named "SatisfactionScore" as Double
  val customerCares =
    event named "CustomerCare" having geographic and satisfaction
  println(Codegen.generate(customerCares))
