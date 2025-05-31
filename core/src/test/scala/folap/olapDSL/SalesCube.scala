package folap.olapDSL

import folap.core.AggregationOp._
import folap.core.Event
import folap.core.EventConstructor
import folap.core.MultidimensionalModel._
import folap.olapDSL.AttributeDSLBuilder._
import folap.olapDSL.AttributeSeqBuilder._
import folap.olapDSL.QueryDSLBuilder._
import folap.utils.visualize

trait Dimension extends Attribute
trait DateDimension extends Dimension
trait GeographicDimension extends Dimension
trait ProductDimension extends Dimension
type Measures = RevenueAmount | QuantitySold

private case class YearAttribute(
    override val parent: Option[TopAttribute],
    override val value: String
) extends DateDimension

private case class QuarterAttribute(
    override val parent: Option[YearAttribute],
    override val value: String
) extends DateDimension

private case class MonthAttribute(
    override val parent: Option[QuarterAttribute],
    override val value: String
) extends DateDimension

private case class RegionAttribute(
    override val parent: Option[TopAttribute],
    override val value: String
) extends GeographicDimension

private case class CountryAttribute(
    override val parent: Option[RegionAttribute],
    override val value: String
) extends GeographicDimension

private case class CityAttribute(
    override val parent: Option[CountryAttribute],
    override val value: String
) extends GeographicDimension

private case class CategoryAttribute(
    override val parent: Option[TopAttribute],
    override val value: String
) extends ProductDimension

private case class SubCategoryAttribute(
    override val parent: Option[CategoryAttribute],
    override val value: String
) extends ProductDimension

private case class ProductAttribute(
    override val parent: Option[SubCategoryAttribute],
    override val value: String
) extends ProductDimension

case class RevenueAmount(override val value: Double) extends Measure:
  type T = Double

case class QuantitySold(override val value: Int) extends Measure:
  type T = Int

case class SalesEvent(
    geographic: GeographicDimension,
    product: ProductDimension,
    date: DateDimension,
    quantity: QuantitySold,
    revenue: RevenueAmount
) extends Event[Dimension, Measures]:
  def dimensions: Iterable[Dimension] = Seq(geographic, product, date)
  def measures: Iterable[Measures] = Seq(quantity, revenue)

val year2023 = YearAttribute(Some(TopAttribute()), "2023")
val year2024 = YearAttribute(Some(TopAttribute()), "2024")

val quarterQ1_2023 = QuarterAttribute(Some(year2023), "Q1")
val quarterQ2_2023 = QuarterAttribute(Some(year2023), "Q2")
val quarterQ1_2024 = QuarterAttribute(Some(year2024), "Q1")

val monthJanuary_2023 = MonthAttribute(Some(quarterQ1_2023), "January")
val monthJanuary_2024 = MonthAttribute(Some(quarterQ1_2024), "January")
val monthApril_2023 = MonthAttribute(Some(quarterQ2_2023), "April")

val regionNorthAmerica = RegionAttribute(Some(TopAttribute()), "North America")
val countryUSA = CountryAttribute(Some(regionNorthAmerica), "USA")
val cityNewYork = CityAttribute(Some(countryUSA), "New York")
val regionEurope = RegionAttribute(Some(TopAttribute()), "Europe")
val countryGermany = CountryAttribute(Some(regionEurope), "Germany")
val cityBerlin = CityAttribute(Some(countryGermany), "Berlin")
val countryItaly = CountryAttribute(Some(regionEurope), "Italy")
val cityMilan = CityAttribute(Some(countryItaly), "Milan")

val categoryElectronics = CategoryAttribute(Some(TopAttribute()), "Electronics")
val subCategorySmartphones =
  SubCategoryAttribute(Some(categoryElectronics), "Smartphones")
val productIPhone = ProductAttribute(Some(subCategorySmartphones), "iPhone 14")
val categoryHomeAppliances =
  CategoryAttribute(Some(TopAttribute()), "Home Appliances")
val subCategoryKitchen =
  SubCategoryAttribute(Some(categoryHomeAppliances), "Kitchen")
val productBlender = ProductAttribute(Some(subCategoryKitchen), "Blender")
val subCategoryLaptops =
  SubCategoryAttribute(Some(categoryElectronics), "Laptops")
val productMacbook = ProductAttribute(Some(subCategoryLaptops), "MacBook Air")

val valueQuantity = 10
val quantitySold = QuantitySold(valueQuantity)
val valueRevenue = 12000.0
val revenueAmount = RevenueAmount(valueRevenue)
val salesEvent1 = SalesEvent(
  cityNewYork,
  productIPhone,
  monthJanuary_2023,
  quantitySold,
  revenueAmount
)

val quantity2 = QuantitySold(5)
val revenue2 = RevenueAmount(250.0)
val salesEvent2 = SalesEvent(
  cityBerlin,
  productBlender,
  monthApril_2023,
  quantity2,
  revenue2
)

val quantity3 = QuantitySold(2)
val revenue3 = RevenueAmount(2400.0)
val salesEvent3 = SalesEvent(
  cityMilan,
  productMacbook,
  monthJanuary_2024,
  quantity3,
  revenue3
)

val productIPhone14 =
  ProductAttribute(Some(subCategorySmartphones), "iPhone 14")
val quantity4 = QuantitySold(3)
val revenue4 = RevenueAmount(3600.0)
val salesEvent4 = SalesEvent(
  cityBerlin,
  productIPhone14,
  monthJanuary_2024,
  quantity4,
  revenue4
)
val events = Iterable(salesEvent1, salesEvent2, salesEvent3, salesEvent4)
val Sales = QueryDSL(events)

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
  val filtered = Sales where ("Product" is "iPhone 14" and ("City" is "Berlin"))
  visualize(filtered.cube)
  val union = Sales union CustomerCare
  visualize(union.cube)
  Max of Sales
