package folap.core

trait TypeFromString[T]:
  def typeName: String
  def measure(name: String): Measure[T]

object TypeFromString:
  given TypeFromString[Int] with
    def typeName: String = "Int"
    def measure(name: String): Measure[Int] = Measure[Int](name, typeName)

  given TypeFromString[Double] with
    def typeName: String = "Double"
    def measure(name: String): Measure[Double] = Measure[Double](name, typeName)

  given TypeFromString[Long] with
    def typeName: String = "Long"
    def measure(name: String): Measure[Long] = Measure[Long](name, typeName)

  given TypeFromString[Float] with
    def typeName: String = "Float"
    def measure(name: String): Measure[Float] = Measure[Float](name, typeName)

  def resolve(name: String, typology: String): Measure[_] =
    val all = List(
      summon[TypeFromString[Int]],
      summon[TypeFromString[Double]],
      summon[TypeFromString[Long]],
      summon[TypeFromString[Float]]
    )

    all
      .find(_.typeName == typology)
      .map(_.measure(name))
      .getOrElse(sys.error(s"Type not supported: $typology"))
