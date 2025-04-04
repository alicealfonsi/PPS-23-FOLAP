# 03 Design architetturale

La libreria segue il pattern architetturale a layer. L'utente ne ha la seguente visione a strati:
- layer DSL;
- layer OLAP;
- layer della struttura multidimensionale.

## Layer DSL

Espone il DSL, permettendo la formulazione delle query in maniera intuitiva.
A tal proposito il layer DSL utilizza i metodi del layer OLAP.

## Layer OLAP

Espone i metodi che implementano la logica di dominio.

## Layer della struttura multidimensionale

Espone la struttura multidimensionale dei dati a cui la sorgente dati deve adattarsi tramite un connettore.
