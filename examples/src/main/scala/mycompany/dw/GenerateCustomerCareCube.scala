package mycompany.dw

import folap.modeldefinition.Codegen
import folap.modeldefinition.DimensionBuilder.dimension
import folap.modeldefinition.EventBuilder.event
import folap.modeldefinition.MeasureDSL.measure
import folap.modeldefinition.SeqBuilder.-->

@main def generateCustomerCare: Unit =
  val geographic = "geographic" dimension "City" --> "Country" --> "Region"
  val satisfaction = measure named "SatisfactionScore" as Double
  val customerCares =
    event named "CustomerCare" having geographic and satisfaction
  println(Codegen.generate(customerCares))
