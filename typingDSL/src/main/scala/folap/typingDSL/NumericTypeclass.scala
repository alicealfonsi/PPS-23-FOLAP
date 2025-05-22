package folap.typingDSL

trait TypeFromString[T]:
  def typeName: String
  def measure(name: String): Measure

object TypeFromString:
  given TypeFromString[Int] with
    def typeName: String = "Int"
    def measure(name: String): Measure = Measure(name, typeName)

  given TypeFromString[Double] with
    def typeName: String = "Double"
    def measure(name: String): Measure = Measure(name, typeName)

  given TypeFromString[Long] with
    def typeName: String = "Long"
    def measure(name: String): Measure = Measure(name, typeName)

  given TypeFromString[Float] with
    def typeName: String = "Float"
    def measure(name: String): Measure = Measure(name, typeName)

  def resolve(name: String, typology: String): Option[Measure] =
    val all = List(
      summon[TypeFromString[Int]],
      summon[TypeFromString[Double]],
      summon[TypeFromString[Long]],
      summon[TypeFromString[Float]]
    )

    all
      .find(_.typeName == typology)
      .map(_.measure(name))
