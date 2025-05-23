package folap.typingDSL

/** Type class that create a "dsl measure" from his name and type.
  *
  * @tparam T
  *   scala type (Int, Double, Long, Float)
  */
trait TypeFromString[T]:
  /** Returns the string representation of the type ("Int", "Double", "Long",
    * "Float").
    */
  def typeName: String

  /** Creates a Measure instance given a name and the associated type.
    *
    * @param name
    *   name of the measure
    * @return
    *   measure with the given name and the type name corresponding to T
    */
  def measure(name: String): Measure

/** Companion object of TypeFromString that creates a measure from his name and
  * numeric type.
  */
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

  /** Resolves a (name, typology) pair to a Measure object if a corresponding
    * TypeFromString instance exists for the provided type name.
    *
    * @param name
    *   name of the measure
    * @param typology
    *   type name as a string ("Int", "Double", "Long", "Float")
    * @return
    *   an Option containing the Measure if the type is supported; otherwise,
    *   None
    */
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
