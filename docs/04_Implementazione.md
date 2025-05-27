# Event Constructor

È stato introdotto un alias, denominato EventConstructor, per rappresentare una funzione che costruisce un nuovo evento a partire da una collezione di dimensioni e una collezione di misure.

L’implementazione della funzione è delegata all’utente, il quale può definire in maniera personalizzata come costruire l’evento risultante, tenendo conto della logica applicativa e della struttura specifica del cubo di riferimento.

Questa astrazione consente un disaccoppiamento tra la logica generica dell’operatore e la rappresentazione concreta dei dati, rendendo l’implementazione adattabile a diversi contesti applicativi.
# Operators

L'object Operators contiene tutti gli operatori OLAP implementati nella libreria.

# Slice And Dice

L'operatore Slice and Dice permette, a partire da una collezione di eventi appartenenti al cubo OLAP, di restituire solo quelli che contengono gli attributi specificati come criterio di filtro. Affinché un attributo dell'evento corrisponda a uno dei filtri è necessario che coincidano sia il nome sia il valore dell’attributo.

L'implementazione di questo operatore è stata sviluppata a partire dal caso base, ovvero lo Slice, che filtra la collezione di eventi dato un singolo attributo. In seguito, la logica è stata estesa per permettere il filtraggio secondo una collezione di attributi, realizzando così il comportamento più generale di Slice and Dice.

La funzione prende in input due collezioni: la prima contiene gli eventi da filtrare, la seconda gli attributi filtro. Entrambe utilizzano tipi generici, poiché possono accettare qualsiasi sottotipo di EventAttribute per gli attributi e di EventMeasure per le misure.Tuttavia, è necessario che il tipo degli attributi presenti nei filtri sia lo stesso di quello degli attributi contenuti negli eventi affinché il confronto tra di essi risulti semanticamente corretto. Il risultato è una nuova collezione che contiene esclusivamente gli eventi che presentano tutti gli attributi specificati nei filtri, con nome e valore perfettamente corrispondenti. Gli eventi risultanti mantengono inalterate le proprie misure e gli attributi originali, poiché l’operatore agisce unicamente come filtro, senza modificare o trasformare i dati.

Per l'implementazione sono state utilizzate funzioni di ordine superiore, tipiche della programmazione funzionale. In particolare filter viene applicata alla collezione di eventi per selezionare solo quelli che soddisfano i criteri. Forall restituisce true solo se la condizione data è verificata per tutti gli elementi della collezione. Find permette di individuare un eventuale attributo dell’evento che abbia lo stesso nome dell'attributo filtro ed exists verifica che, se tale attributo esiste, il suo valore coincida con quello del filtro.

L’impiego di queste funzioni ha reso possibile la realizzazione di una funzione pura, conforme ai principi fondamentali della programmazione funzionale: l’output della funzione dipende unicamente dall’input fornito, senza produrre effetti collaterali né modificare lo stato esterno alla funzione, nel rispetto del principio di immutabilità.

# Drill Across

L'operatore Drill Across consente di combinare eventi di due cubi OLAP differenti, restituendo una nuova collezione di eventi costruita a partire da quelli che condividono almeno un attributo in comune, ovvero aventi lo stesso nome e lo stesso valore.

La funzione prende in input due collezioni di eventi, ciascuna appartenente a un cubo distinto. Entrambe le collezioni sono genericamente tipizzate: gli attributi devono estendere EventAttribute e le misure devono estendere EventMeasure, ma possono essere diversi tra loro poichè due cubi distinti hanno struttura, e quindi eventi, diversa. Inoltre, viene fornita in input una funzione createEvent il cui compito è costruire un nuovo evento utilizzando gli attributi comuni tra i due eventi combinati e l’unione delle rispettive misure. La funzione createEvent viene implementata dall'utente essendo il formato del nuovo evento dipendente dal contesto applicativo e dalla struttura specifica dei dati. L'output sarà una collezione di nuovi eventi che avranno, come attributi, gli attributi della prima collezione di eventi che sono condivisi con la seconda, e, come misure,  l'aggregazione di tutte le misure appartenenti alla prima e alla seconda tipologia di eventi.

Per l’implementazione dell’operatore Drill Across sono state utilizzate funzioni di ordine superiore, caratteristiche della programmazione funzionale. In particolare, la funzione flatMap viene utilizzata in modo annidato per confrontare tra loro tutti gli eventi appartenenti a due collezioni distinte. La prima applicazione di flatMap consente di iterare sugli eventi della prima collezione, mentre la seconda, interna alla prima, consente di esaminare, per ciascun evento della prima collezione, tutti gli eventi della seconda collezione. Questo doppio attraversamento permette di generare tutte le combinazioni possibili di coppie di eventi, una per ciascun evento della prima collezione associato a ciascun evento della seconda. Per ogni coppia, viene verificata l’esistenza di attributi comuni, cioè attributi con lo stesso nome e lo stesso valore. Questa verifica è realizzata tramite la funzione exists, mentre la selezione degli attributi comuni viene effettuata mediante filter, che restituisce solo quelli effettivamente condivisi. Grazie all’uso di flatMap, il risultato finale è una collezione "appiattita" di eventi derivati, nella quale ogni elemento rappresenta una combinazione valida tra eventi dei due cubi basata su attributi comuni.


Anche in questo caso, l’intera funzione è pura: non altera dati esistenti, non produce effetti collaterali e restituisce un output deterministico in base agli input forniti.

## DSL

È stato realizzato un Domain-Specific Language (DSL) per consentire all’utente la definizione di eventi in modo semplice e leggibile.


# Measure DSL 


Per poter costruire un DSL per la definizione degli eventi, è stato necessario realizzare un DSL specifico per la definizione delle measure. A tal fine, è stato creato l’oggetto MeasureDSL che consente di definire il nome e la tipologia della misura che si desidera creare.


La sintassi prevista per la definizione di una misura è:
measure named "nomeMisura" as MeasureType
Dove "nomeMisura" è la stringa che rappresenta il nome della misura e MeasureType è un alias di tipo che rappresenta un'unione di tipi numerici Scala: Int, Long, Float e Double
Il tipo della misura (typology) è limitato ai soli tipi numerici consentiti, qualsiasi tentativo di utilizzare un tipo non compreso in questa unione genererà un errore a tempo di compilazione, garantendo così la correttezza statica del codice.


Per implementare tale DSL, è stato introdotto l’oggetto di supporto MeasureWord. Il metodo "measure", esposto come punto di ingresso del DSL, restituisce un'istanza di MeasureWord, sulla quale è possibile invocare il metodo "named". Questo metodo produce un oggetto intermedio MeasureName, che incapsula il nome della misura.


Successivamente, la chiamata al metodo "as" sull'oggetto MeasureName consente di completare la definizione della misura, specificandone la tipologia numerica. Il metodo as è implementato come estensione del tipo MeasureName
