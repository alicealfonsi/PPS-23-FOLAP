# Testing

Questo progetto è stato sviluppato utilizzando la metodologia del Test-Driven Development (TDD).

Per scrivere le classi di test è stato utilizzato ScalaTest.

## Peculiarità
Avendo sviluppato una libreria che non contiene molte implementazioni concrete, abbiamo anche
implementato un cubo per effettuare i test (`CubeMockup.scala`), cosa non normalmente necessaria.

## Code coverage

| Package                            | Coverage (lines) |
|------------------------------------|------------------|
| `folap.modeldefinition`            | 99,5% (189/190)  |
| `folap.core`                       | 96,6% (56/58)    |
| `folap.core.multidimensionalmodel` | 96% (24/25)      |
| `folap.olapdsl`                    | 91,1% (41/45)    |

Dalla tabella sopra riportata si nota come la *code coverage* del progetto sia buona.

Queste metriche sono state raccolte solo alla fine del progetto e non hanno influenzato
il nostro processo di sviluppo.

