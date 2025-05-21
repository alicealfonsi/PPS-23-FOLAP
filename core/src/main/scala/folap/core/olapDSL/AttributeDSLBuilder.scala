package folap.core.olapDSL

object AttributeDSLBuilder:

  extension (name: String)
    infix def is(value: String): AttributeDSL =
      AttributeDSL(name, value)
