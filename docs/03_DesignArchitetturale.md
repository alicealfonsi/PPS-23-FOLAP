# 03 Design architetturale

La libreria segue il pattern architetturale a layer. L'utente ne ha la seguente visione a strati:
- layer DSL OLAP;
- layer OLAP;
- layer DSL DW;
- layer della struttura multidimensionale.

## Layer DSL OLAP

Espone il DSL che permette la formulazione delle query in maniera intuitiva.
A tal proposito il layer DSL OLAP traduce la query in una sequenza di operazioni OLAP come catena di chiamate ai metodi 
del layer OLAP.

## Layer OLAP

Espone i metodi che implementano le operazioni OLAP.

## Layer DSL DW

Espone il DSL per la generazione del codice che permette di modellare il DW sottostante secondo il modello 
multidimensionale.

## Layer della struttura multidimensionale

Espone la struttura multidimensionale dei dati forniti dall'utente.
