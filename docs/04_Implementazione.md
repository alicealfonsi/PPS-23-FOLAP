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