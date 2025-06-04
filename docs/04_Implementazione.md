# 04 Implementazione

## Multidimensional model (Alice Alfonsi)

### Attribute
Il `trait Attribute` modella il concetto di *attributo* nel modello multidimensionale. 
Definisce le caratteristiche che un *attributo* possiede: `name` con cui l'attributo viene identificato, `value` assunto dall'attributo 
per una specifica istanza di `Attribute`, `parent` che indica l'istanza di `Attribute` che precede l'attributo nella *gerarchia*.

L'ultimo attributo nella gerarchia è un'istanza di `Attribute` denominata `TopAttribute` con `parent` None.

La scelta di utilizzare un trait come contratto concettuale ha permesso l'astrazione del concetto di attributo e la separazione della
sua definizione dalle implementazioni.


L'`object Attribute` è usato come modulo funzionale che incapsula la logica per la definizione delle *gerarchie* e gli algoritmi che 
abilitano i meccanismi di *aggregazione*. Contiene extension methods per aggiungere tali funzionalità a istanze di `Attribute`.

Gli algoritmi per la costruzione delle gerarchie e per la ricerca degli attributi nelle gerarchie sono implementati usando 
funzioni annidate tail-recursive che incapsulano la logica ricorsiva delle chiamate alla funzione. 

Tali metodi helper interni sono annotati con `@tailrec` per garantire che il compilatore possa ottimizzare la ricorsione.

Nel corpo dei metodi per la ricerca degli attributi nella gerarchia è stata utilizzata `find`, funzione higher-order che 
accetta una funzione (predicato) come parametro evidenziando il ruolo delle funzioni in FP come valori di prima classe.


### Measure
Il `trait Measure` astrae il concetto di *misura* nel modello multidimensionale definendo il contratto a cui tutte le sue 
implementazioni devono conformarsi. Espone i metodi `name` e `value` che descrivono le caratteristiche che una 
*misura* possiede e un tipo astratto `T` per descrivere il tipo di `value`.

`T <: MeasureType` è un upper bound con tipo `MeasureType`, alias per il tipo unione `Int | Long | BigInt | Float | Double | BigDecimal` 
introdotto per rappresentare i tipi numerici che il valore di una misura può assumere.

In questo modo, la scelta del tipo concreto `T` è lasciata all'utente e il controllo del tipo è a compile time. Inoltre, l'uso di `T` 
come type member di `Measure` consente liste eterogenee di istanze di `Measure` aventi tipo `T` distinto.


## Event (Alice Alfonsi)
La firma `trait Event[A <: Attribute, M <: Measure]` definisce un trait generico a due parametri, con vincoli di tipo, 
che modella il concetto di *evento*.

`A` con upper bound `Attribute` e `M` con upper bound `Measure` sono i parametri che rappresentano rispettivamente il tipo degli 
*attributi* e il tipo delle *misure* di una specifica istanza di `Event`. Dunque, un'istanza di `Event` ha *dimensioni* omogenee 
su `A` e misure omogenee su `M`. A questo proposito il trait `Event` definisce i metodi astratti `dimensions` e `measures` che 
devono essere ridefiniti dalle classi che estendono il trait.

Il metodo concreto `attributes` ha un'implementazione basata sul metodo `dimensions` valida per tutte le istanze di `Event`, 
pertanto il metodo è marcato come `final`. Nel corpo del metodo si utilizza la funzione higher-order `flatMap`. 

In accordo con SRP, il trait `Event` contiene solo ciò che definisce concettualmente un *evento*. L'`object Event` aggiunge, 
mediante extension method per istanze di `Event[A, M]`, una funzionalità che viene utilizzata per implementare l'operatore di `rollUp`. 
Per l'implementazione di tale metodo sono state utilizzate le funzioni higher-order `flatMap` e `filter`.

Inoltre, l'`object Event` espone un extension method per istanze di `Iterable[Event[A, M]]`, anch'esso utilizzato 
nell'implementazione del metodo `rollUp`. Nel corpo di tale metodo sono state utilizzate le funzioni higher-order `forall` e `exists`.


# Event Constructor

È stato introdotto un alias, denominato `EventConstructor`, per rappresentare una funzione che costruisce un nuovo *evento* a partire da una collezione di *dimensioni* e una collezione di *misure*.

L’implementazione della funzione è delegata all’utente, il quale può definire in maniera personalizzata come costruire l’evento risultante, tenendo conto della logica applicativa e della struttura specifica del cubo di riferimento.

Questa astrazione consente un disaccoppiamento tra la logica generica dell’operatore e la rappresentazione concreta dei dati, rendendo l’implementazione adattabile a diversi contesti applicativi.


# Computable (Alice Alfonsi)
Il `trait Computable[A <: Attribute, M <: Measure, E <: Event[A, M]]` è generico sui parametri `A`, `M`, `E` con vincoli
`A` sottotipo di `Attribute`, `M` sottotipo di `Measure`, `E` sottotipo di `Event[A, M]` e prende il nome di type class.

La type class `Computable[A, M, E]` definisce nuovi comportamenti per i tipi che estendono `Event[A, M]` senza modificarli, come promosso dall'OCP. 
I metodi astratti `sum`, `div`, `min`, `max` e `aggregate`, definiti come extension methods per istanze di tipo `E`, rappresentano operazioni 
per qualunque tipo `E` per cui esista una given instance `Computable[A, M, E]` in scope e dovranno essere implementati per ciascun tipo specifico.

Grazie al polimorfismo ad-hoc è possibile fornire implementazioni (given instance) della type class `Computable[A, M, E]` per tipi concreti che 
estendono `Event[A, M]`, aggiungendo anche a posteriori un potenziamento a tipi esistenti e senza ereditarietà. 

In questo modo la type class favorisce riusabilità e rimane aperta a future estensioni. L'utente può definire implementazioni ad-hoc dei metodi
`sum`, `div`, `min`, `max` e `aggregate` solo per i tipi `E` di interesse.

Allo stesso modo, la type class `Computable[A, M, E]` definisce il metodo concreto `aggregateBy` come extension method per istanze di 
`Iterable[E]`, che sarà disponibile per ogni `E` per cui esiste un'istanza di `Computable[A, M, E]` in scope e il cui comportamento cambia in base 
all'implementazione specifica di `Computable[A, M, E]`. 

Il metodo `aggregateBy` incapsula le logiche di *aggregazione* degli *eventi*. Nel caso in cui l'evento da aggregare sia soltanto uno chiama 
il metodo `aggregate`, nel caso di più eventi da aggregare chiama il metodo astratto della type class che rappresenta l'operazione corrispondente 
all'*operatore di aggregazione* specificato. A tal proposito è stato introdotto il tipo `AggregationOp` costituito dalla seguente enumerazione 
di tipi: `Sum`, `Avg`, `Min`, `Max`.

Il metodo `aggregateBy` è implementato utilizzando `foldleft` come funzione curried higher-order che nel caso specifico accetta come parametro una 
funzione tra `sum`, `min` e `max`. In Scala il metodo `foldleft` è implementato tramite ricorsione tail ed è quindi ottimizzato dal compilatore.


# Operators (Claudia Giannelli, Alice Alfonsi)
L'`object Operators` contiene tutti gli *operatori OLAP* implementati nella libreria.

# Slice And Dice

L'operatore `sliceAndDice` permette, a partire da una collezione di eventi appartenenti al cubo OLAP, di restituire solo quelli che contengono gli attributi specificati come criterio di filtro. Affinché un attributo dell'evento corrisponda a uno dei filtri è necessario che coincidano sia il nome sia il valore dell’attributo.

L'implementazione di questo operatore è stata sviluppata a partire dal caso base, ovvero lo *Slice*, che filtra la collezione di eventi dato un singolo attributo. In seguito, la logica è stata estesa per permettere il filtraggio secondo una collezione di attributi, realizzando così il comportamento più generale di *Slice and Dice*.

La funzione prende in input due collezioni: la prima contiene gli eventi da filtrare, la seconda gli attributi filtro. Entrambe utilizzano tipi generici, poiché possono accettare qualsiasi sottotipo di `EventAttribute` per gli attributi e di `EventMeasure` per le misure.Tuttavia, è necessario che il tipo degli attributi presenti nei filtri sia lo stesso di quello degli attributi contenuti negli eventi affinché il confronto tra di essi risulti semanticamente corretto. Il risultato è una nuova collezione che contiene esclusivamente gli eventi che presentano tutti gli attributi specificati nei filtri, con nome e valore perfettamente corrispondenti. Gli eventi risultanti mantengono inalterate le proprie misure e gli attributi originali, poiché l’operatore agisce unicamente come filtro, senza modificare o trasformare i dati.

Per l'implementazione sono state utilizzate higher-order functions, tipiche della programmazione funzionale. In particolare `filter` viene applicata alla collezione di eventi per selezionare solo quelli che soddisfano i criteri. `Forall` restituisce true solo se la condizione data è verificata per tutti gli elementi della collezione. `Find` permette di individuare un eventuale attributo dell’evento che abbia lo stesso nome dell'attributo filtro ed `exists` verifica che, se tale attributo esiste, il suo valore coincida con quello del filtro.

L’impiego di queste funzioni ha reso possibile la realizzazione di una funzione pura, conforme ai principi fondamentali della programmazione funzionale: l’output della funzione dipende unicamente dall’input fornito, senza produrre effetti collaterali né modificare lo stato esterno alla funzione, nel rispetto del principio di immutabilità.

# Drill Across

L'operatore `drillAcross` consente di combinare eventi di due *cubi OLAP* differenti, restituendo una nuova collezione di eventi costruita a partire da quelli che condividono almeno un attributo in comune, ovvero un attributo situato all’ultimo livello di una gerarchia dimensionale e che non è genitore di altri attributi.

La funzione prende in input due collezioni di eventi, ciascuna appartenente a un cubo distinto. Entrambe le collezioni sono genericamente tipizzate: gli attributi devono estendere `EventAttribute` e le misure devono estendere `EventMeasure`, ma possono essere diversi tra loro poichè due cubi distinti hanno struttura, e quindi eventi, diversa. Inoltre, viene fornita in input una funzione `createEvent` il cui compito è costruire un nuovo evento utilizzando gli **attributi foglia* comuni tra i due eventi combinati e l’unione delle rispettive misure. L’individuazione degli attributi foglia avviene analizzando gli attributi di ciascun evento: un attributo è considerato foglia se non compare come parent di alcun altro attributo nello stesso evento. La funzione `createEvent` viene implementata dall'utente essendo il formato del nuovo evento dipendente dal contesto applicativo e dalla struttura specifica dei dati. 

Dal punto di vista implementativo, la funzione utilizza higher-order functions proprie della programmazione funzionale. Nello specifico, viene impiegata `flatMap` in modo annidato: la prima `flatMap` scorre gli eventi della prima collezione, la seconda itera sugli eventi della seconda. Per ciascuna coppia di eventi generata, vengono identificati i rispettivi *attributi foglia* e determinati quelli condivisi, utilizzando `filter` e `exists`. Se esiste almeno un attributo foglia in comune, viene creato un nuovo evento, contenente solo questi attributi e tutte le misure combinate. L'output sarà dunque una collezione di nuovi eventi che avranno, come attributi, gli attributi foglia condivisi, e, come misure, la combinazione di tutte le misure appartenenti alla prima e alla seconda tipologia di eventi.

Anche in questo caso, l’intera funzione è pura: non modifica alcun dato esistente, non produce effetti collaterali e restituisce un output deterministico in base agli input forniti.


# Roll-up (Alice Alfonsi)
L'operatore `rollUp` permette di aggregare gli *eventi primari* memorizzati nel *cubo OLAP* negli *eventi secondari* corrispondenti secondo un 
insieme di *attributi* che costituiscono il *group-by set*. I valori delle *misure* che caratterizzano gli eventi primari vengono aggregati in 
valori da abbinare a ciascun evento secondario secondo l'*operatore di aggregazione* specificato.

Dunque, il metodo `rollUp` prende in input la collezione di eventi primari da aggregare, la collezione di stringhe che rappresentano i nomi degli 
attributi del group-by set e l'operatore di aggregazione di tipo `AggregationOp`.

Il metodo `rollUp` è parametrizzato su `A` vincolato ad `Attribute`, `M` vincolato a `Measure` ed `E` vincolato a `Event[A, M]`, dove `E` 
rappresenta il tipo degli eventi in input al `rollUp` e il tipo degli eventi che il metodo restituisce. 

Affinchè sia garantito a compile time che il tipo degli eventi primari e il tipo degli eventi secondari sia lo stesso, al metodo `rollUp` viene 
passato come ultimo parametro `using computable: Computable[A, M, E]` dove la type class `Computable` funge da context bound per il metodo. 
Ciò implica che il metodo `rollUp` possa essere chiamato solo se c'è una `given Computable[A, M, E]` disponibile in scope. 
Inoltre, il context bound rende disponibili i metodi della type class `Computable` sugli eventi primari, abilitando su di essi i meccanismi 
di aggregazione mediante la chiamata al metodo `aggregateBy`.

La dichiarazione del parametro `using` nella firma del metodo ha richiesto di implementare il metodo `rollUp` in forma curried.

Nel corpo del metodo `rollUp` sono state utilizzate le funzioni higher-order `exists`, `groupBy` e `map`.


## DSL

È stato realizzato un Domain-Specific Language (DSL) per consentire all’utente la definizione di eventi in modo semplice e leggibile.

La sintassi per la definizione di un evento è la seguente: `event` `named` "nomeEvento" `having` dimension1 `and` dimension2 `and` measure1 
`and` measure2.


# Measure DSL 


Per poter costruire un DSL per la definizione degli eventi, è stato necessario realizzare un DSL specifico per la definizione delle *measure*. A tal fine, è stato creato l’oggetto `MeasureDSL` che consente di definire il nome e la tipologia della misura che si desidera creare.


La sintassi prevista per la definizione di una misura è:
`measure` `named` "nomeMisura" `as` `MeasureType`
Dove "nomeMisura" è la stringa che rappresenta il nome della misura e `MeasureType` è un alias di tipo che rappresenta un'unione di tipi numerici Scala: `Int`, `Long`, `Float` e `Double`
Il tipo della misura (typology) è limitato ai soli tipi numerici consentiti, qualsiasi tentativo di utilizzare un tipo non compreso in questa unione genererà un errore a tempo di compilazione, garantendo così la correttezza statica del codice.


Per implementare tale DSL, è stato introdotto l’oggetto di supporto `MeasureWord`. Il metodo `measure`, esposto come punto di ingresso del DSL, restituisce un'istanza di `MeasureWord`, sulla quale è possibile invocare il metodo `named`. Questo metodo produce un oggetto intermedio `MeasureName`, che incapsula il nome della misura.


Successivamente, la chiamata al metodo `as` sull'oggetto `MeasureName` consente di completare la definizione della misura, specificandone la tipologia numerica. Il metodo `as` è implementato come estensione del tipo `MeasureName`


# Event Builder (Alice Alfonsi)
L'*evento* è rappresentato dalla `case class Event(name: String, dimensions: Seq[Dimension], measures: Seq[Measure])`.

L'`object EventBuilder` costituisce il builder dell'*evento* ed espone il metodo `event` come punto di accesso al DSL. Tale metodo 
restituisce un oggetto intermedio `EventWord()` che rappresenta un'istanza di `EventBuilder`.

Sull'oggetto appena creato è possibile invocare il metodo `named` passando in input una stringa che rappresenta il nome dell'evento.
Il metodo `named` crea un'istanza di `Event` con il nome passato in input e *dimensioni* e *misure* vuote.

Su tale istanza di `Event` è possibile chiamare il metodo `having` per aggiungere una `Dimension` o una `Measure` rispettivamente 
all'insieme delle dimensioni o all'insieme delle misure dell'evento attualmente vuoti.

Infine, su un'istanza di `Event` è disponibile il metodo `and` per aggiungere successivamente `Dimension` o `Measure` alle dimensioni o 
misure dell'evento.


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

In entrambi i casi, l’operazione *roll up* verrà applicata al cubo di partenza, utilizzando come groupBy gli attributi dati in input e aggregando le misure tramite l'operazione specificata. Il risultato è un nuovo `QueryDSL` contenente il cubo aggregato.

L’esecuzione dell’aggregazione si basa sull’uso implicito di un `EventConstructor` fornito dall'utente tramite il meccanismo di contextual abstraction, il quale definisce la modalità con cui costruire i nuovi eventi aggregati.

# Drill Across
Il metodo `union` è un’estensione su `QueryDSL` che permette di eseguire l’operazione *drill across*, ovvero la combinazione di due cubi che condividono almeno una dimensione comune.

Il metodo accetta come parametro un secondo cubo (`QueryDSL`) e richiede implicitamente un `EventConstructor` fornito tramite il meccanismo della contextual abstraction. Questo costruttore, definito dall’utente, specifica come creare i nuovi eventi risultanti dalla combinazione dei dati dei due cubi.

Il metodo restituisce un nuovo `QueryDSL` contenente gli eventi ottenuti dalla combinazione dei due cubi.