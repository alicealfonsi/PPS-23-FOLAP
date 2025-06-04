# 07 Retrospettiva

Gli obiettivi del progetto sono stati raggiunti. Come previsto, la libreria implementata consente l’interrogazione di DW 
tramite DSL, fornisce una rappresentazione dei dati secondo il modello multidimensionale ed espone un DSL per la 
generazione di codice conforme a tale modello.

Siamo soddisfatti del risultato che si ottiene con l’utilizzo dei DSL, in quanto aumentano l'espressività e, nel caso
del DSL di definizione del modello, consente di esprimere in modo molto conciso le entità rispetto al codice generato.

Abbiamo cercato di attenerci il più possibile alla metodologia Scrum. La maggiore difficoltà che abbiamo riscontrato è 
stata rispettare i tempi stabiliti per gli sprint. Ciò può essere dipeso dalla nostra inesperienza nell’utilizzare tale 
metodologia di progetto, ma sicuramente anche dalle stime ottimistiche fatte all’inizio. Nella fase iniziale del 
progetto, infatti, non abbiamo considerato adeguatamente la complessità insita nel dominio.

Durante le prime settimane di sviluppo, sebbene lo scopo del progetto fosse chiaramente definito, non siamo riusciti a 
focalizzare nel dettaglio come implementare la soluzione desiderata.

Controllando gli sprint backlog ci siamo accorti di non aver compilato la colonna Product Backlog Item in modo che 
referenzi gli elementi del Product Backlog.

La maggiore criticità riscontrata durante l’esecuzione riguarda la mancata individuazione delle dipendenze tra le 
diverse parti del progetto. Ciò ha rallentato il processo di sviluppo non permettendo sufficiente parallelizzazione dei 
task per come suddivisi tra i membri del team.

Siamo consapevoli di alcuni refactor ed estensioni che non abbiamo realizzato per mancanza di tempo.


## Considerazioni personali
### Eugenio Tampieri
Eugenio Tampieri avrebbe voluto esprimere i livelli delle gerarchie con gli oggetti associati ai tipi
(ad es. `City.type`), in modo da eliminare alcuni input non validi a compile time.
Questo refactor avrebbe consentito di esprimere le interrogazioni nel seguente modo (si noti che `City` e `Month` non sono 
tra virgolette):
```scala
SalesCube where (City is "Berlin" and (Month is "January"))
```
Ciò avrebbe portato a lasciare i valori stringa solo per rappresentare i valori dimensionali, e non il nome dell'attributo.

Questo non è stato possibile per mancanza di tempo.

Tampieri avrebbe anche voluto realizzare la code generation o come plugin di *Dotty* o come task di `sbt`.

Secondo Tampieri, dover aggregare sull'intero insieme di dati vanifica la lazyness e porta a una cattiva scalabilità sulla
memoria, e questo è un peccato, però non abbiamo trovato un modo per evitarlo.

Infine, Tampieri ha potuto apprezzare l'utilità del TDD, che gli ha permesso di intercettare molti errori compiuti in fase
di code generation.

### Alice Alfonsi
La prima modellazione della struttura multidimensionale prevedeva l’uso di `scala.math.Numeric` per il tipo del valore 
delle misure. In un secondo momento, Alfonsi si è scontrata con il fatto che Scala è invariante nei generici; pertanto, 
in questo modo non era possibile creare una lista eterogenea di misure con valori di tipo diverso.

Ripensare a come modellare le misure ha ritardato l’esecuzione dei task ad esse connessi.
Tra questi, l’implementazione del metodo `rollUp` che inizialmente era stata sviluppata lavorando direttamente sui valori 
delle misure, è stata modificata in modo da spostare la logica di aggregazione nell’evento, lasciandola definire all’utente.
