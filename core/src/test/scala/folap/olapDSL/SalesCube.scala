package folap.olapDSL

import folap.core.AggregationOp._
import folap.core.Event
import folap.core.EventConstructor
import folap.core.MultidimensionalModel._
import folap.olapDSL.AttributeDSLBuilder._
import folap.olapDSL.AttributeSeqBuilder._
import folap.olapDSL.QueryDSLBuilder._
import folap.utils.visualize
object Sales:
  case class Revenue(value: Double) extends folap.core.MultidimensionalModel.Measure:
    type T = Double
  case class Quantity(value: Int) extends folap.core.MultidimensionalModel.Measure:
    type T = Int
  type Measures = Revenue | Quantity
  sealed trait Dimension extends folap.core.MultidimensionalModel.Attribute
  object Dimension:
    sealed trait TemporalDimension extends Dimension
    object TemporalDimension:
      case class Month(value: String, quarter: Quarter) extends TemporalDimension:
        def parent = Some(quarter)
      case class Quarter(value: String, year: Year) extends TemporalDimension:
        def parent = Some(year)
      case class Year(value: String) extends TemporalDimension:
        def parent = Some(TopAttribute())
      case class TopAttribute() extends TemporalDimension:
        def parent = None
        def value = ""
    sealed trait GeographicDimension extends Dimension
    object GeographicDimension:
      case class City(value: String, country: Country) extends GeographicDimension:
        def parent = Some(country)
      case class Country(value: String, region: Region) extends GeographicDimension:
        def parent = Some(region)
      case class Region(value: String) extends GeographicDimension:
        def parent = Some(TopAttribute())
      case class TopAttribute() extends GeographicDimension:
        def parent = None
        def value = ""
    sealed trait ProductDimension extends Dimension
    object ProductDimension:
      case class Product(value: String, subCategory: SubCategory) extends ProductDimension:
        def parent = Some(subCategory)
      case class SubCategory(value: String, category: Category) extends ProductDimension:
        def parent = Some(category)
      case class Category(value: String) extends ProductDimension:
        def parent = Some(TopAttribute())
      case class TopAttribute() extends ProductDimension:
        def parent = None
        def value = ""
    type Attributes = TemporalDimension.Month | TemporalDimension.Quarter | TemporalDimension.Year | GeographicDimension.City | GeographicDimension.Country | GeographicDimension.Region | ProductDimension.Product | ProductDimension.SubCategory | ProductDimension.Category
  case class Sales(revenue: Revenue, quantity: Quantity, temporal: Dimension.TemporalDimension, geographic: Dimension.GeographicDimension, product: Dimension.ProductDimension) extends folap.core.Event[Dimension, Measures]:
    def dimensions: Iterable[Dimension] = Seq(temporal, geographic, product)
    def measures: Iterable[Measures] = Seq(revenue, quantity)
 

  given folap.core.Operational[Dimension, Measures, Sales] with
    extension (e: Sales)
      override def aggregate(groupBySet: Iterable[String]): Sales =
        Sales(e.revenue, e.quantity, e.temporal.upToLevel(e.temporal.searchCorrespondingAttributeName(groupBySet)), e.geographic.upToLevel(e.geographic.searchCorrespondingAttributeName(groupBySet)), e.product.upToLevel(e.product.searchCorrespondingAttributeName(groupBySet)))
      override def div(n: Int): Sales =
        Sales(Revenue(e.revenue.value / n), Quantity(e.quantity.value / n), e.temporal, e.geographic, e.product)
      override def min(other: Sales)(groupBySet: Iterable[String]): Sales =
        val aggregated = e.aggregate(groupBySet)
        Sales(
          Revenue(math.min(aggregated.quantity.value, other.quantity.value)),
          Quantity(math.min(aggregated.quantity.value, other.quantity.value)), 
          aggregated.temporal,
          aggregated.geographic,
          aggregated.product
        )
      override def sum(other: Sales)(groupBySet: Iterable[String]): Sales =
        val aggregated = e.aggregate(groupBySet)
        Sales(Revenue(aggregated.revenue.value + other.revenue.value), Quantity(aggregated.quantity.value + other.quantity.value), aggregated.temporal, aggregated.geographic, aggregated.product)
      override def max(other: Sales)(groupBySet: Iterable[String]): Sales =
        val aggregated = e.aggregate(groupBySet)
        Sales(Revenue(aggregated.revenue.value.max(other.revenue.value)), Quantity(aggregated.quantity.value.max(other.quantity.value)), aggregated.temporal, aggregated.geographic, aggregated.product)
import Sales._, Dimension._, Dimension.TemporalDimension._, Dimension.GeographicDimension._, Dimension.ProductDimension._, Sales._
val year2023 = Year("2023")
val year2024 = Year("2024")

val quarterQ1_2023 = Quarter("Q1", year2023)
val quarterQ2_2023 = Quarter("Q2", year2023)
val quarterQ1_2024 = Quarter("Q1", year2024)

val monthJanuary_2023 = Month("January", quarterQ1_2023)
val monthJanuary_2024 = Month("January", quarterQ1_2024)
val monthApril_2023 = Month("April", quarterQ2_2023)

val regionNorthAmerica = Region("North America")
val countryUSA = Country("USA", regionNorthAmerica)
val cityNewYork = City("New York", countryUSA)
val regionEurope = Region("Europe")
val countryGermany = Country("Germany", regionEurope)
val cityBerlin = City("Berlin", countryGermany)
val countryItaly = Country("Italy", regionEurope)
val cityMilan = City("Milan", countryItaly)

val categoryElectronics = Category("Electronics")
val subCategorySmartphones =
  SubCategory("Smartphones", categoryElectronics)
val productIPhone = Product("iPhone 14", subCategorySmartphones)
val categoryHomeAppliances =
  Category("Home Appliances")
val subCategoryKitchen =
  SubCategory("Kitchen", categoryHomeAppliances)
val productBlender = Product("Blender", subCategoryKitchen)
val subCategoryLaptops =
  SubCategory("Laptops", categoryElectronics)
val productMacbook = Product("MacBook Air", subCategoryLaptops)

val valueQuantity = 10
val quantitySold = Quantity(valueQuantity)
val valueRevenue = 12000.0
val revenueAmount = Revenue(valueRevenue)
val salesEvent1 = Sales.Sales(
  revenueAmount,
  quantitySold,
  monthJanuary_2023,
  cityNewYork,
  productIPhone
  
)

val quantity2 = Quantity(5)
val revenue2 = Revenue(250.0)
val salesEvent2 = Sales.Sales(
  revenue2,
  quantity2,
  monthApril_2023,
  cityBerlin,
  productBlender
)

val quantity3 = Quantity(2)
val revenue3 = Revenue(2400.0)
val salesEvent3 = Sales.Sales(
  revenue3,
  quantity3,
  monthJanuary_2024,
  cityMilan,
  productMacbook
)


val quantity4 = Quantity(3)
val revenue4 = Revenue(3600.0)
val salesEvent4 = Sales.Sales(
  revenue4,
  quantity4,
  monthJanuary_2024,
  cityBerlin,
  productIPhone
)
val events = Iterable(salesEvent1, salesEvent2, salesEvent3, salesEvent4)
val SalesCube = QueryDSL(events)

case class SatisfactionScore(value: Double) extends Measure:
  type T = Double

type CareMeasures = SatisfactionScore

case class CustomerCareEvent(
    location: GeographicDimension,
    satisfaction: SatisfactionScore
) extends Event[Dimension, CareMeasures]:
  def dimensions: Iterable[Dimension] = Seq(location)
  def measures: Iterable[CareMeasures] = Seq(satisfaction)

val careEvent1 = CustomerCareEvent(
  cityBerlin,
  SatisfactionScore(4.5)
)

val careEvent2 = CustomerCareEvent(
  cityMilan,
  SatisfactionScore(3.8)
)
case class ResultEvent[A <: Attribute, M <: Measure](
    override val dimensions: Iterable[A],
    override val measures: Iterable[M]
) extends Event[A, M]
given EventConstructor[A <: Attribute, M <: Measure, E <: Event[A, M]]: EventConstructor[A, M, E] =
  (
      attributes: Iterable[A],
      measures: Iterable[M]
  ) => ResultEvent(attributes, measures).asInstanceOf[E]

val careEvents = Iterable(careEvent1, careEvent2)
val CustomerCare = QueryDSL(careEvents)

@main def main(): Unit =
  //visualize(SalesCube.cube)
  val attr = "City" is "Berlin"
  val filtered = SalesCube where ("City" is "Berlin" and ("Month" is "January"))
  //visualize(filtered.cube)
  val union = SalesCube union CustomerCare
  //visualize(union.cube)
  val aggregated = Sum of SalesCube by "City"
  visualize(aggregated.cube)
