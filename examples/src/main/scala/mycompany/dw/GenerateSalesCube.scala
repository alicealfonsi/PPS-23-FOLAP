package mycompany.dw

import folap.modeldefinition.Codegen
import folap.modeldefinition.DimensionBuilder.dimension
import folap.modeldefinition.EventBuilder.event
import folap.modeldefinition.MeasureDSL.measure
import folap.modeldefinition.SeqBuilder.-->

@main def generateSales: Unit =
  val temporal = "temporal" dimension "Month" --> "Quarter" --> "Year"
  val geographic = "geographic" dimension "City" --> "Country" --> "Region"
  val product = "product" dimension "Product" --> "SubCategory" --> "Category"
  val revenue = measure named "Revenue" as Double
  val quantity = measure named "Quantity" as Int
  val sales =
    event named "Sales" having temporal and geographic and product and revenue and quantity
  println(Codegen.generate(sales))
