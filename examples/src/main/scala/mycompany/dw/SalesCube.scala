package mycompany.dw

object Sales:
  case class Revenue(value: Double)
      extends folap.core.multidimensionalmodel.Measure:
    type T = Double
  case class Quantity(value: Int)
      extends folap.core.multidimensionalmodel.Measure:
    type T = Int

  type Measures = Revenue | Quantity
  sealed trait Dimension extends folap.core.multidimensionalmodel.Attribute
  object Dimension:
    sealed trait TemporalDimension extends Dimension
    object TemporalDimension:
      case class Month(value: String, quarter: Quarter)
          extends TemporalDimension:
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
      case class City(value: String, country: Country)
          extends GeographicDimension:
        def parent = Some(country)
      case class Country(value: String, region: Region)
          extends GeographicDimension:
        def parent = Some(region)
      case class Region(value: String) extends GeographicDimension:
        def parent = Some(TopAttribute())
      case class TopAttribute() extends GeographicDimension:
        def parent = None
        def value = ""
    sealed trait ProductDimension extends Dimension
    object ProductDimension:
      case class Product(value: String, subCategory: SubCategory)
          extends ProductDimension:
        def parent = Some(subCategory)
      case class SubCategory(value: String, category: Category)
          extends ProductDimension:
        def parent = Some(category)
      case class Category(value: String) extends ProductDimension:
        def parent = Some(TopAttribute())
      case class TopAttribute() extends ProductDimension:
        def parent = None
        def value = ""
    type Attributes = TemporalDimension.Month | TemporalDimension.Quarter |
      TemporalDimension.Year | GeographicDimension.City |
      GeographicDimension.Country | GeographicDimension.Region |
      ProductDimension.Product | ProductDimension.SubCategory |
      ProductDimension.Category
  case class Sales(
      revenue: Revenue,
      quantity: Quantity,
      temporal: Dimension.TemporalDimension,
      geographic: Dimension.GeographicDimension,
      product: Dimension.ProductDimension
  ) extends folap.core.Event[Dimension, Measures]:
    def dimensions: Iterable[Dimension] = Seq(temporal, geographic, product)
    def measures: Iterable[Measures] = Seq(revenue, quantity)

  given folap.core.Computable[Dimension, Measures, Sales] with
    extension (e: Sales)
      override def aggregate(groupBySet: Iterable[String]): Sales =
        Sales(
          e.revenue,
          e.quantity,
          e.temporal.upToLevel(
            e.temporal.searchCorrespondingAttributeName(groupBySet)
          ),
          e.geographic.upToLevel(
            e.geographic.searchCorrespondingAttributeName(groupBySet)
          ),
          e.product.upToLevel(
            e.product.searchCorrespondingAttributeName(groupBySet)
          )
        )
      override def div(n: Int): Sales =
        Sales(
          Revenue(e.revenue.value / n),
          Quantity(e.quantity.value / n),
          e.temporal,
          e.geographic,
          e.product
        )
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
        Sales(
          Revenue(aggregated.revenue.value + other.revenue.value),
          Quantity(aggregated.quantity.value + other.quantity.value),
          aggregated.temporal,
          aggregated.geographic,
          aggregated.product
        )
      override def max(other: Sales)(groupBySet: Iterable[String]): Sales =
        val aggregated = e.aggregate(groupBySet)
        Sales(
          Revenue(aggregated.revenue.value.max(other.revenue.value)),
          Quantity(aggregated.quantity.value.max(other.quantity.value)),
          aggregated.temporal,
          aggregated.geographic,
          aggregated.product
        )
