# 02 Requisiti e specifica

## 1) Requisiti di business
- La libreria che si intende realizzare deve permettere l’esecuzione di sessioni di analisi OLAP su un Data Warehouse
  (DW).
- La libreria deve fornire un DSL per utenti OLAP per facilitare l'interrogazione del DW.
- La libreria deve fornire un DSL per definire la struttura multidimensionale.
- La libreria deve essere estendibile per supportare l’aggiunta di funzionalità che permettano di fruire delle 
  informazioni contenute in un DW.

## 2) Requisiti di dominio
- Un data warehouse è una collezione di dati.
- I dati sono memorizzati in cubi secondo il modello multidimensionale.
- Un cubo comprende un insieme di eventi relativi a un fatto di interesse per l’analisi.
- Un fatto è descritto da misure e dimensioni.
- Una misura è una proprietà numerica di un fatto.
- Una dimensione è una proprietà con dominio finito di un fatto.
- Ogni dimensione può essere la radice di una gerarchia di attributi, sempre a valori discreti, usati per analizzare i 
  dati sotto diversi punti di vista.
- La gerarchia definisce i livelli crescenti di aggregazione a partire dalla dimensione in cui ha radice, che ne 
  rappresenta la granularità più fine.
- Un evento primario è una particolare occorrenza di un fatto, individuata da un valore per ciascuna dimensione. 
  A ciascun evento primario è associato un valore per ciascuna misura.
- Un evento secondario è il risultato dell’aggregazione di più eventi primari secondo un insieme di attributi (group-by 
  set).
- L’aggregazione dei valori delle misure degli eventi primari avviene applicando uno dei seguenti operatori di 
  aggregazione: somma, media, minimo, massimo.
- L'utente può formulare un'interrogazione OLAP concatenando più operatori OLAP.
- Il risultato delle interrogazioni OLAP è di tipo multidimensionale.

## 3) Requisiti funzionali
### Requisiti funzionali di utente
L’utente può:
- Visualizzare i dati.
- Applicare i seguenti operatori OLAP ai dati:
  - **roll-up**: permette di aggregare i dati lungo l'attributo specificato;
  - **drill-down**: è duale al roll-up; permette di disaggregare i dati scendendo a un livello più fine in una gerarchia;
  - **slice**: permette di selezionare un membro per una dimensione;
  - **dice**: permette di selezionare un range di membri su più dimensioni;
  - **drill-across**: permette di comparare i dati di due o più cubi che condividono almeno una dimensione.

## 4) Requisiti non funzionali
- La libreria deve fornire un DSL intuitivo per l'interrogazione del DW.
- La libreria deve fornire un DSL intuitivo per la definizione della struttura multidimensionale.

## 5) Requisiti di implementazione
- Scala 3.3.6 LTS.
- Supporto garantito a JDK 21.
- Uso del TDD nel processo di sviluppo.
- Realizzazione del DSL interno.
- Codice scritto in maniera idiomatica, organizzato e documentato.
