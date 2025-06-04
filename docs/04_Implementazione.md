## Event Constructor

È stato introdotto un alias, denominato `EventConstructor`, per rappresentare una funzione che costruisce un nuovo *evento* a partire da una collezione di *dimensioni* e una collezione di *misure*.

L’implementazione della funzione è delegata all’utente, il quale può definire in maniera personalizzata come costruire l’evento risultante, tenendo conto della logica applicativa e della struttura specifica del cubo di riferimento.

Questa astrazione consente un disaccoppiamento tra la logica generica dell’operatore e la rappresentazione concreta dei dati, rendendo l’implementazione adattabile a diversi contesti applicativi.

## Operators

L’object `Operators` raccoglie tutti gli *operatori OLAP* implementati nella libreria.  I risultati delle query possono essere visualizzati invocando la funzione `visualize`, che prende in input un iteratore di eventi.

# Slice And Dice

L'operatore `sliceAndDice` permette, a partire da una collezione di eventi appartenenti al cubo OLAP, di restituire solo quelli che contengono gli attributi specificati come criterio di filtro. Affinché un attributo dell'evento corrisponda a uno dei filtri è necessario che coincidano sia il nome sia il valore dell’attributo.

L'implementazione di questo operatore è stata sviluppata a partire dal caso base, ovvero lo *Slice*, che filtra la collezione di eventi dato un singolo attributo. In seguito, la logica è stata estesa per permettere il filtraggio secondo una collezione di attributi, realizzando così il comportamento più generale di *Slice and Dice*.

Il metodo prende in input due collezioni: la prima contiene gli eventi da filtrare, la seconda gli attributi filtro. Entrambe utilizzano tipi generici, poiché possono accettare qualsiasi sottotipo di `Attribute` per gli attributi e di `Measure` per le misure.Tuttavia, è necessario che il tipo degli attributi presenti nei filtri sia lo stesso di quello degli attributi contenuti negli eventi affinché il confronto tra di essi risulti semanticamente corretto. Il risultato è una nuova collezione che contiene esclusivamente gli eventi che presentano tutti gli attributi specificati nei filtri, con nome e valore perfettamente corrispondenti. Gli eventi risultanti mantengono inalterate le proprie misure e gli attributi originali, poiché l’operatore agisce unicamente come filtro, senza modificare o trasformare i dati.

Per l'implementazione sono state utilizzate higher-order methods, tipici della programmazione funzionale. In particolare `filter` viene applicato alla collezione di eventi per selezionare solo quelli che soddisfano i criteri. `forall` restituisce true solo se la condizione data è verificata per tutti gli elementi della collezione. `find` permette di individuare un eventuale attributo dell’evento che abbia lo stesso nome dell'attributo filtro ed `exists` verifica che, se tale attributo esiste, il suo valore coincida con quello del filtro.

L’impiego di questi metodi ha reso possibile la realizzazione di un metodo puro, conforme ai principi fondamentali della programmazione funzionale: l’output del metodo dipende unicamente dall’input fornito, senza produrre effetti collaterali né modificare lo stato esterno al metodo, nel rispetto del principio di immutabilità.

# Drill Across

L'operatore `drillAcross` consente di combinare eventi di due *cubi OLAP* differenti, restituendo una nuova collezione di eventi costruita a partire da quelli che condividono almeno un *attributo foglia* in comune, ovvero un attributo situato all’ultimo livello di una gerarchia dimensionale e che non è genitore di altri attributi.

Il metodo prende in input due collezioni di eventi, ciascuna appartenente a un cubo distinto. Entrambe le collezioni sono genericamente tipizzate: gli attributi devono estendere `Attribute` e le misure devono estendere `Measure`, ma possono essere diversi tra loro poichè due cubi distinti hanno struttura, e quindi eventi, diversa. Inoltre, viene fornita in input una funzione `createEvent` di tipo `EventConstructor`, il cui compito è costruire un nuovo evento utilizzando gli attributi foglia comuni tra i due eventi combinati e l’unione delle rispettive misure. L’individuazione degli *attributi foglia* avviene analizzando gli attributi di ciascun evento: un attributo è considerato foglia se non compare come parent di alcun altro attributo nello stesso evento. La funzione `createEvent` viene implementata dall'utente essendo il formato del nuovo evento dipendente dal contesto applicativo e dalla struttura specifica dei dati. 

Dal punto di vista implementativo, il metodo utilizza higher-order methods propri della programmazione funzionale. Nello specifico, viene impiegato `flatMap` in modo annidato: il primo `flatMap` scorre gli eventi della prima collezione, il secondo itera sugli eventi del secondo. Per ciascuna coppia di eventi generata, vengono identificati i rispettivi *attributi foglia* e determinati quelli condivisi, utilizzando `filter` e `exists`. Se esiste almeno un attributo foglia in comune, viene creato un nuovo evento, contenente solo questi attributi e tutte le misure combinate. L'output sarà dunque una collezione di nuovi eventi che avranno, come attributi, gli *attributi foglia* condivisi, e, come misure, la combinazione di tutte le misure appartenenti alla prima e alla seconda tipologia di eventi.

Anche in questo caso, l’intero metodo è puro: non modifica alcun dato esistente, non produce effetti collaterali e restituisce un output deterministico in base agli input forniti.

## Typing DSL

È stato realizzato un Domain-Specific Language (DSL) per consentire all’utente la definizione di eventi in modo semplice e leggibile.


# Measure DSL 


Per poter costruire un DSL per la definizione degli eventi, è stato necessario realizzare un DSL specifico per la definizione delle *measure*. A tal fine, è stato creato l’oggetto `MeasureDSL` che consente di definire il nome e la tipologia della misura che si desidera creare.


La sintassi prevista per la definizione di una misura è:
`measure` `named` "nomeMisura" `as` `MeasureType`
Dove "nomeMisura" è la stringa che rappresenta il nome della misura e `MeasureType` è un alias di tipo che rappresenta tipi numerici Scala: `Int`, `Long`, `Float` e `Double`
Il tipo della misura (typology) è limitato ai soli tipi numerici consentiti, qualsiasi tentativo di utilizzare un tipo non compreso in questa unione genererà un errore a tempo di compilazione.


Per implementare tale DSL, è stato introdotto l’oggetto di supporto `MeasureWord`. Il metodo `measure`, esposto come punto di ingresso del DSL, restituisce un'istanza di `MeasureWord`, sulla quale è possibile invocare il metodo `named`. Questo metodo produce un oggetto intermedio `MeasureName`, che incapsula il nome della misura.


Successivamente, la chiamata al metodo `as` sull'oggetto `MeasureName` consente di completare la definizione della misura, specificandone la tipologia numerica. Il metodo `as` è implementato come estensione del tipo `MeasureName`


## DSL OLAP

È stato realizzato un Domain-Specific Language (DSL) per facilitare all'utente l’esecuzione di interrogazioni OLAP.

# Costruzione degli attributi

Per realizzare il DSL, è stato innanzitutto definito il metodo infisso `is`, che consente di creare un attributo (`AttributeDSL`) a partire da una stringa che rappresenta il nome e da un’altra stringa che rappresenta il valore dell’attributo. L'attributo può essere creato anche a partire dal solo nome; in tal caso, il valore sarà considerato assente e impostato come stringa vuota.

# Costruzione di sequenze di attributi

Il metodo infisso `and` consente di costruire sequenze di attributi in modo progressivo. Tale metodo è stato "overloaded" per poter gestire: due nomi di attributi (stringhe), una sequenza di attributi e un nome di attributo (stringa), una sequenza di attributi e un attributo, due attributi.

# Operazioni OLAP

La classe `QueryDSL` è stata progettata per rappresentare un *cubo OLAP*, ovvero una collezione di eventi. Essa costituisce l’elemento centrale del DSL, fungendo da base per l’applicazione delle operazioni OLAP.

# Slice and Dice

L’operazione *slice and dice* è resa disponibile tramite il metodo infisso `where`, definito come metodo di estensione su *QueryDSL*.
Essa consente di filtrare gli eventi del cubo, restituendo solo quelli che possiedono gli attributi specificati. Internamente, viene utilizzato il metodo *sliceAndDice*.
Il metodo `where` può accettare o un singolo attributo o una collezione di attributi e restituisce un nuovo oggetto *QueryDSL*, contenente gli eventi filtrati.

# Roll Up

Per eseguire operazioni di *roll-up* all'interno del DSL OLAP, è innanzitutto necessario selezionare l'operazione di aggregazione da applicare alle misure.

A tal fine si utilizza il metodo `of`, definito come extension method sull’enum `AggregationOp`, che rappresenta il tipo di operazione desiderata (ad esempio Max, Sum, Avg, Min). Il metodo riceve in input un *cubo OLAP* (`QueryDSL`) sul quale applicare l’aggregazione e restituisce un oggetto `QueryWithOp`, che incapsula sia il cubo che l’operazione specificata.

Per eseguire effettivamente l’aggregazione, è necessario specificare gli attributi rispetto ai quali effettuare il raggruppamento. A tal fine si utilizza il metodo `by`, disponibile come metodo di estensione su `QueryWithOp`.

Questo metodo accetta una sequenza di attributi, oppure un singolo nome di attributo (stringa), convertito internamente in un `AttributeDSL`.
In entrambi i casi, l’operazione *roll up* verrà applicata al cubo di partenza, utilizzando come groupBy gli attributi dati in input e aggregando le misure tramite l’operazione di aggregazione fornita implicitamente (`given`). Il risultato è un nuovo `QueryDSL` contenente il cubo aggregato.

# Drill Across

Il metodo `union` è un’estensione su `QueryDSL` che permette di eseguire l’operazione *drill across*, ovvero la combinazione di due cubi che condividono almeno una dimensione comune.

Il metodo accetta come parametro un secondo cubo (`QueryDSL`) e richiede implicitamente un `EventConstructor` fornito tramite il meccanismo della contextual abstraction (`given`). Questo costruttore, definito dall’utente, specifica come creare i nuovi eventi risultanti dalla combinazione dei dati dei due cubi.

Il metodo restituisce un nuovo `QueryDSL` contenente gli eventi ottenuti dalla combinazione dei due cubi.