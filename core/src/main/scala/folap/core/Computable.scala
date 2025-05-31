package folap.core

trait Computable[A]:
  extension (a: A)
    def sum(other: A): A
    def div(n: Int): A
