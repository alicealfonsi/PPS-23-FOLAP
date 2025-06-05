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
Durante l'implementazione del roll-up sono emerse complessità impreviste che non erano state affrontate in precedenza.

Ciò ha richiesto di ripensare la soluzione e di conseguenza ha ritardato lo svolgimento di alcuni task.

#### Note (Tampieri)

Gran parte del mio *effort* durante questo sprint è stato dedicato ad approfondire i meccanismi di code generation di Scala.

L'obiettivo che mi ero prefissato era quello di sostituire dei sottoalberi dell'_AST_
in modo da rimpiazzare ciò che l'utente definiva con il codice generato.

Mi sono documentato in primo luogo sulle [macro](https://docs.scala-lang.org/scala3/guides/macros/macros.html), pensando fossero simili a [quelle presenti in Rust](https://doc.rust-lang.org/book/ch20-05-macros.html).
In realtà, consentono di trasformare codice a runtime ma sono spesso utilizzate per
espandere codice o effettuare l'*inlining* delle funzioni (vedi esempio dell'elevamento a potenza).
Non sono state utilizzate le *macro*, in quanto senza usare `@experimental` possono solo generare all'interno dello scope della funzione.

Successivamente, ho approfondito la libreria [Scalameta](https://scalameta.org/docs/trees/guide.html).
Dalle mie indagini, veniva usata in Scala 2 per effettuare metaprogrammazione.
Questa libreria fornisce un parser di Scala, ed è idonea alla generazione di codice,
ma da quanto ho capito questa generazione viene effettuata a runtime.

Infine, ho letto la documentazione dei [plugin del compilatore](https://docs.scala-lang.org/scala3/reference/changed-features/compiler-plugins.html).
Questi consentirebbero la modifica a compile time dell'*AST* ma, data la complessità
dell'approccio, che i plugin non possono influenzare il *type-checking* (non ho capito se è
permesso modificare l'*AST* prima della fase di *type-checking*) e che lo sprint si avvicinava alla conclusione, ho abbandonato questa strada.

Considerata la scarsa complessità di questa *code generation*, ho optato per generare a
runtime la stringa corrispondente alla definizione in Scala degli eventi, delle gerarchie e
degli attributi.


## Sprint Backlog 3 (start on 16/05/2025, deadline 04/06/2025)
| **Product Backlog Item**                  | **Sprint Task**                | **Volunteer** | **Initial Estimate of Effort (h)** | **Measured effort (h)** | **Remaining effort for the next sprint (h)** |
|-------------------------------------------|--------------------------------|---------------|------------------------------------|-------------------------|----------------------------------------------|
| Implementazione Event                     | Relazione                      | Alice         | 0.5                                | 1                       | 0                                            |
| Implementazione Attribute                 | Relazione                      | Alice         | 0.25                               | 1                       | 0                                            |
| Implementazione Measure                   | Relazione                      | Alice         | 0.25                               | 0.5                     | 0                                            |
| Implementazione Cube                      | Relazione                      | Alice         | 0.25                               | 0                       | 0                                            |
| Implementazione EventAttribute            | Relazione                      | Alice         | 0.5                                | 0                       | 0                                            |
| Implementazione EventMeasure              | Relazione                      | Alice         | 0.25                               | 0                       | 0                                            |
| Implementazione roll-up                   | Aggregazione misure, relazione | Alice         | 6                                  | 16                      | 0                                            |
| Refactor Measure                          |                                | Alice         | 3                                  | 8                       | 0                                            |
| Refactor MultidimensionalModel            |                                |               |                                    | 3                       | 0                                            |
| Implementazione drill-down                | Implementazione, relazione     | Eugenio       | 3                                  | 2                       | 0                                            |
| Design di dettaglio                       | Relazione                      | Team          | 1                                  | 1                       | 0                                            |
| Design di dettaglio                       | Organizzazione del codice      | Team          | 0.5                                | 0.5                     | 0                                            |
| Design di dettaglio                       | Diagrammi UML                  | Eugenio       | 1                                  | 1                       | 0                                            |
| Refactoring operatori                     |                                | Eugenio       | 3                                  | 3                       | 0                                            |
| Implementazione DSL gerarchie             | Relazione                      | Eugenio       | 1                                  | 0.5                     | 0                                            |
| Implementazione DSL misure                | Relazione                      | Claudia       | 1                                  | 1                       | 0                                            |
| Implementazione DSL evento                | Implementazione, relazione     | Alice         | 5                                  | 5                       | 0                                            |
| Task sbt generazione codice               |                                | Eugenio       | 3                                  | 6                       | 0                                            |
| Definizione DSL per interrogazione DW     |                                | Team          | 3                                  | 1                       | 0                                            |
| Implementazione DSL per interrogazione DW |                                | Claudia       |                                    | 12                      | 0                                            |
| Implementazione DW di esempio             |                                | Claudia       |                                    | 5                       | 0                                            |
| Conclusione documentazione                |                                | Team          |                                    | 4                       | 0                                            |

### Sprint retrospective 3
#### Note (Giannelli)
Le dipendenze dei miei task da quelli degli altri mi hanno costretta ad aspettare la loro conclusione prima di poter procedere. 
I numerosi refactor a fine progetto mi hanno costretta a modificare codice già concluso.

#### Note (Alfonsi)
Date le dipendenze tra la mia parte di codice e quella degli altri, anche solo piccoli refactor del mio codice avevano 
un impatto non trascurabile sull'intero progetto di cui ho dovuto tenere conto per decidere se apportare le modifiche.
In particolare modifiche al core dovevano essere riportate nella code generation e utilizzate nella parte di 
implementazione del DW di esempio.

#### Note (Tampieri)
In questo sprint sono riuscito a completare la code generation e ad ottenere qualcosa di utilizzabile e funzionante.
