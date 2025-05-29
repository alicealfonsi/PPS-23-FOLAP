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