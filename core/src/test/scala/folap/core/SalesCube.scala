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

case class SalesEvent(
    geographic: GeographicDimension,
    product: ProductDimension,
    date: DateDimension,
    quantity: QuantitySold,
    revenue: RevenueAmount
) extends Event[Dimension, Measures]:
  def dimensions: Iterable[Dimension] = Seq(geographic, product, date)
  def measures: Iterable[Measures] = Seq(revenue, quantity)

val year2023 = YearAttribute(Some(TopAttribute()), "2023")
val quarterQ1 = QuarterAttribute(Some(year2023), "Q1")
val monthJanuary = MonthAttribute(Some(quarterQ1), "January")
val regionNorthAmerica = RegionAttribute(Some(TopAttribute()), "North America")
val countryUSA = CountryAttribute(Some(regionNorthAmerica), "USA")
val cityNewYork = CityAttribute(Some(countryUSA), "New York")
val categoryElectronics = CategoryAttribute(Some(TopAttribute()), "Electronics")
val subCategorySmartphones =
  SubCategoryAttribute(Some(categoryElectronics), "Smartphones")
val productIPhone = ProductAttribute(Some(subCategorySmartphones), "iPhone 14")
val value = 10
val quantitySold = QuantitySold(value)
val revenueAmount = RevenueAmount(12000.0)
val event1 = SalesEvent(
  cityNewYork,
  productIPhone,
  monthJanuary,
  quantitySold,
  revenueAmount
)
