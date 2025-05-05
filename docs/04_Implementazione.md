# Event Constructor

È stato introdotto un alias, denominato EventConstructor, per rappresentare una funzione che costruisce un nuovo evento a partire da una collezione di dimensioni e una collezione di misure.

L’implementazione della funzione è delegata all’utente, il quale può definire in maniera personalizzata come costruire l’evento risultante, tenendo conto della logica applicativa e della struttura specifica del cubo di riferimento.

Questa astrazione consente un disaccoppiamento tra la logica generica dell’operatore e la rappresentazione concreta dei dati, rendendo l’implementazione adattabile a diversi contesti applicativi.