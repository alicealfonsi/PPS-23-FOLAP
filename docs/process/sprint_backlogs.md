# Sprint Backlogs
## Sprint Backlog 0 (start on 31/03/2025, deadline 11/04/2025)
| **Product Backlog Item**                               | **Sprint Task**                                       | **Volunteer** | **Initial Estimate of Effort (h)** | **Measured effort (h)** | **Remaining effort for the next sprint (h)** |
|--------------------------------------------------------|-------------------------------------------------------|---------------|------------------------------------|-------------------------|----------------------------------------------|
| Identificazione elementi base del processo di sviluppo | Assegnazione dei ruoli                                | Team          | 0.25                               | 0.25                    | 0                                            |
| Identificazione elementi base del processo di sviluppo | "Definition of done"                                  | Team          | 0.25                               | 0.25                    | 0                                            |
| Identificazione elementi base del processo di sviluppo | Definizione delle attività di uno sprint              | Team          | 0.25                               | 0.25                    | 0                                            |
| Identificazione elementi base del processo di sviluppo | Tipologie e cadenza di meeting                        | Team          | 0.25                               | 0.25                    | 0                                            |
| Preparazione CI e set up repo                          | Controlli e azioni da effettuare a ogni "commit"      | Eugenio       | 1                                  | 0.5                     | 0                                            |
| Preparazione CI e set up repo                          | Regole per accettazione "pull request"                | Team          | 0.5                                | 0.25                    | 0                                            |
| Preparazione CI e set up repo                          | Regole sui branch                                     | Team          | 0.5                                | 0.25                    | 0                                            |
| Requisiti e specifica                                  | Requisiti di business                                 | Team          | 1                                  | 1                       | 0                                            |
| Requisiti e specifica                                  | Requisiti di dominio                                  | Team          | 1                                  | 1                       | 0                                            |
| Requisiti e specifica                                  | Requisiti funzionali                                  | Team          | 1                                  | 1                       | 0                                            |
| Requisiti e specifica                                  | Requisiti non funzionali                              | Team          | 1                                  | 1                       | 0                                            |
| Requisiti e specifica                                  | Requisiti di implementazione                          | Team          | 1                                  | 1                       | 0                                            |
| Architettura                                           | Identificazione componenti del sistema                | Team          | 1                                  | 3                       | 0                                            |
| Architettura                                           | Scelta pattern architetturali                         | Team          | 1                                  | 1                       | 0                                            |
| Design di dettaglio                                    | Identificazione entità del sistema e loro interazioni | Team          | 2                                  | 0.5                     | 1.5                                          |
| Design di dettaglio                                    | Organizzazione del codice                             | Team          | 1                                  | 0                       | 1                                            |
| Design di dettaglio                                    | Diagrammi UML                                         | Team          | 1                                  | 0                       | 1                                            |

### Sprint retrospective 0

Il team si è dovuto soffermare su aspetti di dominio essenziali per la modellazione che hanno richiesto più tempo del
previsto per essere approfonditi.
La valutazione dei vantaggi e degli svantaggi delle varie soluzioni individuate si è rivelata lunga ed è stato difficile
convergere verso la scelta finale.


## Sprint Backlog 1 (start on 12/04/2025, deadline 29/04/2025)
| **Product Backlog Item**           | **Sprint Task**                                       | **Volunteer**  | **Initial Estimate of Effort (h)** | **Measured effort (h)** | **Remaining effort for the next sprint (h)** |
|------------------------------------|-------------------------------------------------------|----------------|------------------------------------|-------------------------|----------------------------------------------|
| Definizione cubo multidimensionale | Definizione specifiche cubo                           | Team           | 3                                  | 3                       | 0                                            |
| Implementazione Event              |                                                       | Eugenio, Alice | 1                                  | 1 + 3                   | 0.5                                          |
| Implementazione Attribute          |                                                       | Eugenio, Alice | 1                                  | 0.5 + 1                 | 0.25                                         |
| Implementazione Measure            |                                                       | Eugenio, Alice | 1                                  | 0.5 + 0.5               | 0.25                                         |
| Implementazione Cube               |                                                       | Alice          | 1                                  | 0.5                     | 0.25                                         |
| Implementazione EventConstructor   |                                                       | Alice, Claudia | 1                                  | 1                       | 0.5                                          |
| Implementazione EventAttribute     |                                                       | Alice          | 2                                  | 3                       | 0.5                                          |
| Implementazione EventMeasure       |                                                       | Alice          | 1                                  | 1                       | 0.25                                         |
| Definizione operatori OLAP         | Definizione specifiche operatori OLAP                 | Team           | 3                                  | 3                       | 0                                            |
| Implementazione roll-up            |                                                       | Alice          | 10                                 | 6                       | 4                                            |
| Implementazione drill-down         |                                                       | Eugenio        | 3                                  | 0                       | 3                                            |
| Implementazione slice-and-dice     |                                                       | Claudia        | 7                                  | 7                       | 1                                            |
| Implementazione drill-across       |                                                       | Claudia        | 7                                  | 9                       | 1                                            |
| Design di dettaglio                | Identificazione entità del sistema e loro interazioni | Team           | 2                                  | 1                       | 1                                            |
| Design di dettaglio                | Organizzazione del codice                             | Team           | 1                                  | 0.5                     | 0.5                                          |
| Design di dettaglio                | Diagrammi UML                                         | Team           | 1                                  | 0                       | 1                                            |

### Sprint retrospective 1
Le dipendenze fra i task hanno rallentato, soprattutto nella fase iniziale dello sprint, il lavoro del team.


## Sprint Backlog 2 (start on 05/05/2025, deadline 16/05/2025)
| **Product Backlog Item**           | **Sprint Task**                                           | **Volunteer**  | **Initial Estimate of Effort (h)** | **Measured effort (h)** | **Remaining effort for the next sprint (h)** |
|------------------------------------|-----------------------------------------------------------|----------------|------------------------------------|-------------------------|----------------------------------------------|
| Implementazione Event              | Relazione                                                 | Eugenio, Alice | 0.5                                | 0                       | 0.5                                          |
| Implementazione Attribute          | Relazione                                                 | Eugenio, Alice | 0.25                               | 0                       | 0.25                                         |
| Implementazione Measure            | Relazione                                                 | Eugenio, Alice | 0.25                               | 0                       | 0.25                                         |
| Implementazione Cube               | Relazione                                                 | Alice          | 0.25                               | 0                       | 0.25                                         |
| Implementazione EventConstructor   | Relazione                                                 | Claudia        | 0.5                                | 0.5                     | 0                                            |
| Implementazione EventAttribute     | Relazione                                                 | Alice          | 0.5                                | 0                       | 0.5                                          |
| Implementazione EventMeasure       | Relazione                                                 | Alice          | 0.25                               | 0                       | 0.25                                         |
| Implementazione roll-up            | Aggregazione misure, group-by su più attributi, relazione | Alice          | 4                                  | 10                      | 6                                            |
| Implementazione drill-down         | Implementazione, relazione                                | Eugenio        | 3                                  | 0                       | 3                                            |
| Implementazione slice-and-dice     | Relazione                                                 | Claudia        | 1                                  | 1                       | 0                                            |
| Implementazione drill-across       | Relazione                                                 | Claudia        | 1                                  | 1                       | 0                                            |
| Design di dettaglio                | Relazione                                                 | Team           | 1                                  | 0                       | 1                                            |
| Design di dettaglio                | Organizzazione del codice                                 | Team           | 0.5                                | 0                       | 0.5                                          |
| Design di dettaglio                | Diagrammi UML                                             | Team           | 1                                  | 0                       | 1                                            |
| Refactoring operatori              |                                                           | Eugenio        | 3                                  | 0                       | 3                                            |
| Studio di fattibilità delle macro  |                                                           | Eugenio        | 3                                  | 6                       | 0                                            |
| Definizione DSL per definizione DW |                                                           | Team           | 2                                  | 2                       | 0                                            |
| Implementazione DSL gerarchie      |                                                           | Eugenio        | 5                                  | 1                       | 1                                            |
| Implementazione DSL misure         |                                                           | Claudia        | 5                                  | 3                       | 1                                            |
| Implementazione DSL evento         |                                                           | Alice          | 5                                  | 0                       | 5                                            |

### Sprint retrospective 2


## Sprint Backlog 3 (start on 16/05/2025, deadline 28/05/2025)
| **Product Backlog Item**              | **Sprint Task**                | **Volunteer** | **Initial Estimate of Effort (h)** | **Measured effort (h)** | **Remaining effort for the next sprint (h)** |
|---------------------------------------|--------------------------------|---------------|------------------------------------|-------------------------|----------------------------------------------|
| Implementazione Event                 | Relazione                      | Alice         | 0.5                                |                         |                                              |
| Implementazione Attribute             | Relazione                      | Alice         | 0.25                               |                         |                                              |
| Implementazione Measure               | Relazione                      | Alice         | 0.25                               |                         |                                              |
| Implementazione Cube                  | Relazione                      | Alice         | 0.25                               |                         |                                              |
| Implementazione EventAttribute        | Relazione                      | Alice         | 0.5                                |                         |                                              |
| Implementazione EventMeasure          | Relazione                      | Alice         | 0.25                               |                         |                                              |
| Implementazione roll-up               | Aggregazione misure, relazione | Alice         | 6                                  |                         |                                              |
| Refactor Measure                      |                                | Alice         | 3                                  |                         |                                              |
| Implementazione drill-down            | Implementazione, relazione     | Eugenio       | 3                                  |                         |                                              |
| Design di dettaglio                   | Relazione                      | Team          | 1                                  |                         |                                              |
| Design di dettaglio                   | Organizzazione del codice      | Team          | 0.5                                |                         |                                              |
| Design di dettaglio                   | Diagrammi UML                  | Team          | 1                                  |                         |                                              |
| Refactoring operatori                 |                                | Eugenio       | 3                                  |                         |                                              |
| Implementazione DSL gerarchie         | Relazione                      | Eugenio       | 1                                  |                         |                                              |
| Implementazione DSL misure            | Relazione                      | Claudia       | 1                                  |                         |                                              |
| Implementazione DSL evento            | Implementazione, relazione     | Alice         | 5                                  |                         |                                              |
| Task sbt generazione codice           |                                | Eugenio       | 3                                  |                         |                                              |
| Definizione DSL per interrogazione DW |                                | Team          | 3                                  |                         |                                              |
| Implementazione DW di esempio         |                                |               |                                    |                         |                                              |
