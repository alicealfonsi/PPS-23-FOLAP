# 01 Processo di sviluppo

Per la realizzazione della libreria si intende seguire il processo di sviluppo consigliato SCRUM-inspired.

Il team di sviluppo è composto da:
- Alice Alfonsi
- Claudia Giannelli Taccarino
- Eugenio Tampieri

Tra i membri del team si è scelto di assegnare il ruolo di committente ed esperto di dominio a Eugenio Tampieri e il ruolo di product 
owner a Alice Alfonsi.

## "Definition of done"
- software che ha superato i test
- software la cui documentazione è opportunamente aggiornata
- software con commenti per i componenti che hanno visibilità pubblica
- software formattato secondo la configurazione di scalafmt

## Sprint
Si prevede di realizzare un primo sprint organizzativo e tre sprint successivi indicativamente di durata 10 giorni ciascuno.

Per ogni sprint si stima un carico di lavoro pari a 15 ore circa a testa.

### Attività di uno sprint
1. Sprint Planning
2. Mid-Sprint Meeting
3. Sprint Review
4. Sprint Retrospective
5. Product Backlog Refinement

## Gestione del repo
Si è scelto di utilizzare GitFlow ad esclusione dei branch `release`.
### Continuous Integration
A seconda del tipo di evento (commit o pull request) vengono effettuati dei controlli differenti.

Nel caso dei commit, vengono eseguiti gli unit test.

Nel caso delle pull request, vengono eseguiti i test su Windows (ultima versione), Ubuntu 22.04 e macOS
(ultima versione).
Inoltre, vengono effettuati i controlli di `scalafmt` (preset di default) e di `scalafix`.
Di quest'ultimo strumento sono stati abilitati:
 - `DisableSyntax`: verifica che non vengano utilizzate alcune sintassi non idiomatiche;
 - `OrganizeImports`: verifica che gli `import` siano ordinati e ben organizzati;
 - `RedundantSyntax`: verifica che non siano presenti qualificatori non necessari o altri elementi sintattici superflui;
 - `RemoveUnused`: verifica che non siano presenti `import` non utilizzati.
