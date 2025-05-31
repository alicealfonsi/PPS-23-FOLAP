package folap.core

trait Computable[A]:
  extension (a: A) def div(n: Int): A
