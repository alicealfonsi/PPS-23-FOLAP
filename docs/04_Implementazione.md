## DSL

È stato realizzato un Domain-Specific Language (DSL) per consentire all’utente la definizione di eventi in modo semplice e leggibile.....

# Measure DSL

Per poter costruire un DSL per la definizione degli eventi, è stato necessario realizzare un DSL specifico per la definizione delle measure. A tal fine, è stato creato l’oggetto MeasureDSL, il quale consente di definire il nome e la tipologia della misura che si desidera creare.

La sintassi prevista per la definizione di una misura è:
measure named "nomeMisura" as "numType"
Dove "nomeMisura" è la stringa che rappresenta il nome della misura e "numType" è la stringa che rappresenta il tipo numerico. Il tipo della misura può essere "Int", "Double", "Float" oppure "Long".

Per implementare tale DSL, è stato introdotto l’oggetto di supporto MeasureWord. Il metodo measure, esposto come punto di ingresso del DSL, restituisce un'istanza di MeasureWord, sulla quale è possibile invocare il metodo named. Questo metodo produce un oggetto intermedio MeasureName, che incapsula il nome della misura.

Successivamente, l’invocazione del metodo as su MeasureName consente di completare la definizione della misura, specificandone la tipologia. Tale metodo è implementato come un’estensione del tipo MeasureName ed effettua un lookup della stringa del tipo attraverso il metodo resolve dell’oggetto TypeFromString.

L’oggetto TypeFromString ha il compito di mappare i nomi dei tipi numerici ("Int", "Double", "Long", "Float") ai corrispondenti tipi Scala, utilizzando istanze given del type class TypeFromString[T]. Ogni istanza fornisce sia il nome del tipo in formato stringa sia un metodo per costruire un oggetto Measure a partire da un nome.


Il metodo resolve, chiamato all'interno del metodo as, scorre tutte le istanze note di TypeFromString e, se trova una corrispondenza tra il tipo "typeName" di un'istanza e quello fornito dall’utente, restituisce l’oggetto Measure corrispondente; in caso contrario, restituisce None.
