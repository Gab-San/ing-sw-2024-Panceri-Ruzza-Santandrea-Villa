Questo README contiene il workflow che vorrei ognuno di voi utilizzasse.

Nella sezione di GitHub _Projects_ è presente un elemento, "Ingegneria del Software". 
Se non avete iniziato a lavorare su nulla, guardate nella sezione task di tale elemento e controllate tutte le task che non sono ancora state assegnate. 
Potete liberamente scegliere di inventarvene una nuova (se non è presente) oppure sceglierne una tra quelle già definite.
Una volta scelta una task, **datevi una deadline**: nella sezione _Project Roadmap_ è possibile selezionare una data di inizio e una data di fine. Almeno indicativamente **cerchiamo di darci una tempistica**,
di modo che possiamo tutti lavorare in maniera più coordinata.

Un volta scelta una task, si utilizzi il workflow dei _feature branch_:
1. Creazione del branch: il nome da dare al branch è nella colonna _branch id_ delle tasks
2. Sviluppo del branch: potete liberamente lavorare su tale branch, magari condividendolo con qualcun altro nel corso dello sviluppo; di base sarà **personale**.
3. Push del branch: **NON** fate mai una merge senza aver consultato il team. Pushate il branch su cui state lavorando, anche in mezzo allo sviluppo e utilizzate il pull&merge request su GitHub per far notare agli altri
      su cosa state lavorando. Possiamo organizzarci per lavorare a coppie: una persona che sviluppa una cosa e un'altro che fa la review.
4. Step Finali: una volta che avete testato e sviluppato il vostro branch e il team ha dato il permesso di mergare allora si arriva agli step finali;
   1. Rebase: se il vostro branch non è nasce dall'ultimo commit del main, si utilizza il comando `git rebase` per poter spostare il vostro branch sull'ultimo commit del main e gestire tutti 
        i conflitti all'interno del vostro branch.  
   2. Squash: utilizzate il comando di `git rebase --interactive` per poter fare lo squashing e riscrivere la history in modo tale da renderla più corta e leggibile;
   3. Merge&Push: alla fine potete mergare e pushare sulla remote repo.

Segnate sul _Project_ "Ingegneria del Software" che avete finito di sviluppare tale feature (spostare in done). 

Qualora sorgano dubbi o problemi, o qualsiasi, utilizzate la task nel project per creare una issue e descrivete il vostro problema: basta aprire la vostra task e premere in basso a destra _Convert to issue_.
