package mycompany.dw

object CustomerCare:
  case class SatisfactionScore(value: Double)
      extends folap.core.multidimensionalModel.Measure:
    type T = Double
  type Measures = SatisfactionScore
  sealed trait Dimension extends folap.core.multidimensionalModel.Attribute
  object Dimension:
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
    type Attributes = GeographicDimension.City | GeographicDimension.Country |
      GeographicDimension.Region
  case class CustomerCare(
      satisfactionScore: SatisfactionScore,
      geographic: Dimension.GeographicDimension
  ) extends folap.core.Event[Dimension, Measures]:
    def dimensions: Iterable[Dimension] = Seq(geographic)
    def measures: Iterable[Measures] = Seq(satisfactionScore)
  given folap.core.Computable[Dimension, Measures, CustomerCare] with
    extension (e: CustomerCare)
      override def aggregate(groupBySet: Iterable[String]): CustomerCare =
        CustomerCare(
          e.satisfactionScore,
          e.geographic.upToLevel(
            e.geographic.searchCorrespondingAttributeName(groupBySet)
          )
        )
      override def sum(other: CustomerCare)(
          groupBySet: Iterable[String]
      ): CustomerCare =
        val aggregated = e.aggregate(groupBySet)
        CustomerCare(
          SatisfactionScore(
            aggregated.satisfactionScore.value + other.satisfactionScore.value
          ),
          aggregated.geographic.lowestCommonAncestor(other.geographic)
        )
      override def div(n: Int): CustomerCare =
        CustomerCare(
          SatisfactionScore(e.satisfactionScore.value / n),
          e.geographic
        )
      override def min(other: CustomerCare)(
          groupBySet: Iterable[String]
      ): CustomerCare =
        val aggregated = e.aggregate(groupBySet)
        CustomerCare(
          SatisfactionScore(
            aggregated.satisfactionScore.value
              .min(other.satisfactionScore.value)
          ),
          aggregated.geographic.lowestCommonAncestor(other.geographic)
        )
      override def max(other: CustomerCare)(
          groupBySet: Iterable[String]
      ): CustomerCare =
        val aggregated = e.aggregate(groupBySet)
        CustomerCare(
          SatisfactionScore(
            aggregated.satisfactionScore.value
              .max(other.satisfactionScore.value)
          ),
          aggregated.geographic.lowestCommonAncestor(other.geographic)
        )
