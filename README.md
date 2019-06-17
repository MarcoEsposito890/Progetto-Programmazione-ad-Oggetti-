# Progetto-Programmazione-ad-Oggetti-
Progetto Programmazione ad Oggetti A.A. 2018/2019 

Il progetto consiste nel modellare un data-set creando un insieme di classi e sfruttando i principi della Programmazione ad Oggetti, e 
realizzare un'applicazione che renda poi accessibile il data-set mediante API REST GET, ad esempio per ottenere statistiche, dati filtrati o
altre elaborazioni sui dati e sulle classi che li modellano.

In particolare, il data-set assegnato è un elenco in formato .csv delle Farmacie nella Regione Campania. Sono stati quindi create classi
che modellano le suddette e gli attributi che le caratterizzano (corrispondenti ai campi dell'header del file .csv). 
Per creare l'API REST si è utilizzato Spring Boot, un framework di Spring () usato per creare microservizi e che fornisce funzioni di
autoconfigurazione e di *dependencies management* semplificate. Tramite richieste GET è possibile visualizzare i dati in formato JSON, filtrarli ed ottenere varie informazioni su di essi. In particolare, è possibile fare controlli e verifiche
sui dati forniti (ad esempio, sulle Partite IVA) nonchè ottenere informazioni sulle Farmacie più vicine a una data Farmacia etc.

Di seguito si riportano i casi d'uso dell'applicazione, le scelte fatte per la modellazione del data-set e la sequenza 

## Casi D'Uso 

![casi d'uso (1)](https://user-images.githubusercontent.com/48209182/59587173-f7739080-90e4-11e9-9ed7-12a1d33ceec7.png)

## Modello del Dataset

## Il Package Utility

![utility](https://user-images.githubusercontent.com/48209182/59595841-df0c7180-90f6-11e9-8043-a4c7d283f413.png)

Il Package Utility presenta diverse classi utilizzate per effettuare elaborazioni, filtraggio e controlli sui dati. 
In particolare sono presenti le seguenti classi:
  * Calcolatrice, utilizzata per effettuare calcoli, medie, confronti e per il calcolo della Formula di Lunn e della *Spherical Law of Cosines* utilizzate rispettivamente in Checker e GPS;
  * scannerDati, classe per il filtraggio, lo scorrimento e la ricerca di elementi. Viene inizializzata dal costruttore con un ArrayList di oggetti Farmacia, 
    sui quali effettua direttamente le operazioni elencate. Implementa l'interfaccia Filters ed utilizza un'istanza di FilterUtils per le operazioni di filtraggio;
  * GPS, classe utilizzata per elaborare i campi latitudine e longitudine del data-set. In particolare, permette di trovare la distanza fra due date farmacie (tramite la *Spherical Law of Cosines*) e consente di trovare
    la Farmacia più vicina ad un'altra. Estende scannerDati, di cui utilizza in particolare i metodi per la ricerca entro l'ArrayList di oggetti Farmacia.
  * Checker, classe che fa controlli sui dati. In particolare controlla possibili mismatch nella Partite IVA utilizzando la Formula di Lunn, che consente di calcolare il codice di controllo
    di una Partita IVA per confrontarlo con quello della Farmacia, per verificare se è esatto. Inoltre controlla eventuali mismatch fra Provincia e codice Provincia della partita IVA, oltre che controllare
    se una riga del data-set presenta campi vuoti o non definiti.
  * MetaDataStore, classe utilizzata per conservare i metadati.
Il package Filters contenuto in Utility contiene infine:
  *Filter, interfaccia implementata da scannerDati contenente il metodo filterField;
  *FilterUtils, classe che contiene metodi utilizzati per il filtraggio, in particolare per effettuare confronti e per costruire le Collection contenenti i valori filtrati. I metodi sono volutamente generici per
   permettere di filtrare diversi tipi di Collection e con diversi operatori.
  
Maggiori dettagli sui singoli metodi delle varie classi sono contenuti nel JavaDoc del progetto.

## Fasi dell'Applicazione

Come specifica, è stato fornito l'URL del data-set ID contenente a sua volta l'URL del data-set in formato csv. All'avvio, l'applicazione (nel metodo main) esegue
quindi il download del data-set ID (formato JSON) all'URL specificato, dopodichè utilizza un'istanza della classe Parser per effettuare il parsing del data-set ID.
In particolare, utilizza il metodo parsJSON() di Parser per ottenere l'URL del file *Elenco-Farmacie.csv*. Da questo effettua quindi il
download del file e , sempre tramite Parser, realizza il parsing del file .csv, da cui vengono estratti l'header e i dati.

Questi vengono poi utilizzati da Parser per creare il modello del data-set come riportato sopra. In particolare, creerà quindi tanti oggetti Farmacia
quante sono le righe del file (ognuna corrispondente appunto ad una farmacia), creando ovviamente anche gli oggetti Comune e Provincia relativi.
Gli oggetti Farmacia sono quindi inseriti in un ArrayList e ritornati al metodo main, che è stato marcato come @SpringBootApplication e rappresenta quindi
il punto d'accesso di Spring Boot. 
Il main crea quindi i @Bean che vengono utilizzati dai RestController per rispondere alle richieste GET. In particolare,
sono creati @Bean per le classi principali del Package Utility, inizializzandoli con l'ArrayList contenente gli oggetti Farmacia.
All'avvio dell'applicazione, Spring Boot esamina i Bean e le annotazioni inserite, e inietta nei RestController i Bean configurati. Sono stati creati
3 Rest Controller, per gestire richieste riguardanti rispettivamente la scansione e il filtraggio dei dati, il controllo sui dati e le funzioni della classe GPS.
I Rest Controller gestiscono quindi le richieste che mappano, ritornando oggetti in formato JSON contenenti le risposte.

![diagrammadisequenza](https://user-images.githubusercontent.com/48209182/59589469-4c65d580-90ea-11e9-9a14-a7fbfe21f1f2.png)

Ogni Rest Controller utilizza l'istanza della classe di Utility che le è stata iniettata per ottenere i dati necessari a rispondere alla richiesta. 
Ad esempio, quando il Rest Controller DataController riceva una richiesta /metaF, si ha la seguente sequenza per l'ottenimento dei metadati degli oggetti Farmacia

![meta](https://user-images.githubusercontent.com/48209182/59592571-96ea5080-90f0-11e9-99d5-6b0bc2d094d9.png)
