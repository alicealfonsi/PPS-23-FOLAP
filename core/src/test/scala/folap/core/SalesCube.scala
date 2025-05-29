package folap.core
import MultidimensionalModel._

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

case class RevenueAmount(val value: Double) extends Measure:
  type T = Double
  override def fromRaw(value: Double): RevenueAmount = RevenueAmount(value)

case class QuantitySold(val value: Int) extends Measure:
  type T = Int
  override def fromRaw(value: Int): QuantitySold = QuantitySold(value)

case class Sales(
    geographic: GeographicDimension,
    product: ProductDimension,
    date: DateDimension,
    quantity: QuantitySold,
    revenue: RevenueAmount
) extends Event[Dimension, Measures]:
  def dimensions: Iterable[Dimension] = Seq(geographic, product, date)
  def measures: Iterable[Measures] = Seq(revenue, quantity)

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
val subCategorySmartphones = SubCategoryAttribute(Some(categoryElectronics), "Smartphones")
val productIPhone = ProductAttribute(Some(subCategorySmartphones), "iPhone 14")
val categoryHomeAppliances = CategoryAttribute(Some(TopAttribute()), "Home Appliances")
val subCategoryKitchen = SubCategoryAttribute(Some(categoryHomeAppliances), "Kitchen")
val productBlender = ProductAttribute(Some(subCategoryKitchen), "Blender")
val subCategoryLaptops = SubCategoryAttribute(Some(categoryElectronics), "Laptops")
val productMacbook = ProductAttribute(Some(subCategoryLaptops), "MacBook Air")

val valueQuantity = 10
val quantitySold = QuantitySold(valueQuantity)
val valueRevenue = 12000.0
val revenueAmount = RevenueAmount(valueRevenue)
val event1 = Sales(
  cityNewYork,
  productIPhone,
  monthJanuary_2023,
  quantitySold,
  revenueAmount
)


val quantity2 = QuantitySold(5)
val revenue2 = RevenueAmount(250.0)
val event2 = Sales(
  cityBerlin,
  productBlender,
  monthApril_2023,
  quantity2,
  revenue2
)


val quantity3 = QuantitySold(2)
val revenue3 = RevenueAmount(2400.0)
val event3 = Sales(
  cityMilan,
  productMacbook,
  monthJanuary_2024,
  quantity3,
  revenue3
)


val productIPhone14 = ProductAttribute(Some(subCategorySmartphones), "iPhone 14")
val quantity4 = QuantitySold(3)
val revenue4 = RevenueAmount(3600.0)
val event4 = Sales(
  cityBerlin,
  productIPhone14,
  monthJanuary_2024,
  quantity4,
  revenue4
)

val filtered = Sales where (
  ("Product" is "iPhone 14") and
  ("Month" is "January") and
  ("Year" is "2024") and
  ("Region" is "Europe")
)

