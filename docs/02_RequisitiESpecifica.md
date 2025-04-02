# 02 Requisiti e specifica

## 1) Requisiti di business
- La libreria che si intende realizzare deve permettere l’esecuzione di sessioni di analisi OLAP su un data warehouse esistente.
- La libreria deve fornire un DSL per utenti OLAP per facilitare l'interrogazione del data warehouse.
- Il framework deve essere estendibile per supportare l’aggiunta di funzionalità che permettano di fruire delle informazioni contenute in un data warehouse.

## 2) Requisiti di dominio
- Un data warehouse è una collezione di dati.
- I dati sono memorizzati in cubi secondo il modello multidimensionale.
- Un cubo comprende un insieme di eventi relativi a un fatto di interesse per l’analisi.
- Un fatto è descritto da misure numeriche e dimensioni.
- Ogni dimensione può essere la radice di una gerarchia di attributi usati per analizzare i dati sotto diversi punti di vista.
- La gerarchia definisce i livelli crescenti di aggregazione a partire dalla dimensione in cui ha radice, che ne rappresenta la granularità più fine.
- Un evento primario è una particolare occorrenza di un fatto, individuata da un valore per ciascuna dimensione. A ciascun evento primario è associato un valore per ciascuna misura.
- Un evento secondario è il risultato dell’aggregazione di più eventi primari secondo un insieme di attributi dimensionali (group-by set).
- L’additività è una proprietà che descrive se e come una misura può essere aggregata (somma, media, minimo, massimo) lungo una dimensione.
- Ogni passo della sessione di analisi è scandito dall’applicazione di un operatore OLAP ai dati.
- Il risultato delle interrogazioni OLAP è di tipo multidimensionale.

## 3) Requisiti funzionali
### 3.1) Requisiti funzionali di utente
L’utente può:
- Visualizzare i dati multidimensionali in formato tabellare.
- Applicare i seguenti operatori OLAP ai dati:
  - **roll-up**: permette di aggregare i dati lungo l'attributo dimensionale specificato;
  - **drill-down**: è duale al roll-up; permette di disaggregare i dati scendendo a un livello più fine in una gerarchia;
  - **slice**: permette di selezionare un membro per una dimensione;
  - **dice**: permette di selezionare un range di membri su più dimensioni;
  - **pivot**: permette una diversa modalità di organizzazione dei dati;
  - **drill-across**: permette di comparare i dati di due o più cubi che condividono almeno una dimensione. 

### 3.2) Requisiti funzionali di sistema


## 4) Requisiti non funzionali
- La libreria deve fornire un DSL intuitivo per l'interrogazione del DW.

## 5) Requisiti di implementazione
- Scala 3.3.5 LTS.
- Supporto garantito a JDK 21.
- Uso del TDD nel processo di sviluppo.
- Realizzazione del DSL interno.
- Codice scritto in maniera idiomatica, organizzato e documentato.
