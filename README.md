# Progetto Programmazione ad Oggetti
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

L'applicazione consente il download dei dati specificati e il loro parsing e modelizzazione. Inoltre, come già detto, scopo principale dell'applicazione è di implementare API (*Application Programming Interface*) tramite il modello REST (*Representational State Transfer*) che consente a un Client di inviare richieste verso l'applicazione, che risponde con la risposta appropriata. Le richieste vengono fatte tramite il metodo HTTP GET e specificano il tipo di richiesta, la risorsa a cui si è interessati e il tipo di azione da svolgere sulla risorsa (ex. *http://localhost:8080/filtro?campo=latitudine&operatore=>&valore=40* specifica un'operazione di filtraggio sull'attributo latitudine e con l'operatore e il valore indicati).

Sono quindi consentite diverse richieste che consentono principalmente la visualizzazione del data-set o di parte di esso, il filtraggio dei dati, operazioni di controllo (ad esempio, sui codici di controllo delle Partite IVA) e di elaborazione (calcolo della distanza fra due farmacie, statistiche sul numero di farmacie o dispensari etc.).

## Modello del Dataset

![modello](https://user-images.githubusercontent.com/48209182/59630775-7816a900-9146-11e9-93b1-6ddd2a37507f.png)

Come già detto, il data-set assegnato rappresenta le Farmacie della regione Campania. Il modello usato quindi rappresenta ogni Farmacia (una riga del file "Elenco-Farmacie.csv") come un oggetto omonimo contenente gli attributi riportati nel dataset come nome, partita IVA, tipologia etc. Ogni Farmacia contiene inoltre un riferimento ad un oggetto Comune, che rappresenta appunto il comune in cui si trova la Farmacia. La classe Comune estende la classe Localita, contenente informazioni sulla latitudine e la longitudine. Infine, ogni oggetto
Comune contiene un riferimento a un oggetto Provincia, che indica appunto la provincia in cui si trova il comune.

Le classi sono quindi organizzate in maniera tale che ogni oggetto Farmacia contenga tutti i riferimenti necessari ad estrarre i dati ad esso relativi, inclusi quelli contenuti negli oggetti Comune (che ha accesso ai metodi di Localita) e Provincia, creando quindi una sorta di incapsulamento dai dati più specifici a quelli più generali. La scelta di utilizzare un incapsulamento di questo tipo sta nel fatto che in questa maniera si può utilizzare un ArrayList contenente oggetti Farmacia per gestire facilmente tutti i dati tramite le classi del Package Utility, riportate in seguito.

MetaData infine è un'interfaccia che si occupa di definire il metodo getMetaDati() che viene implementato da tutte le classi precedenti per fornire i propri metadati (che includono l'alias dell'attributo, il campo del file .csv da cui deriva e il tipo) incapsulandoli in un oggetti di tipo MetaDataStore. MetaData contiene un metodo statico che consente di ricavare dai metodi di ogni classe le stringhe contenenti i metadati e convertirle in oggetti di tipo JSON che possono poi essere ritornati da Spring quando viene ricevuta una richiesta di GET per i metadati. Il metodo è generico (prende in ingresso un oggetto Classe generico e un elenco di campi generico) e viene utilizzato da tutte le classi che implementano l'interfaccia.

Ulteriori delucidazioni sui singoli metodi usati sono nel JavaDoc relativo.

## Il Package Utility

![utility](https://user-images.githubusercontent.com/48209182/59623705-23b6fd80-9135-11e9-80df-55720ff6eb75.png)

Il Package Utility presenta diverse classi utilizzate per effettuare elaborazioni, filtraggio e controlli sui dati. 
In particolare sono presenti le seguenti classi:
  * Parser, utilizzata per fare operazioni di parsing, sia per ricavare (come descritto meglio in seguito) l'url del dataset in formato     .csv, sia per fare il parsing di quest'ultimo e ricavare header e dati. Inoltre si occupa di utilizzare i dati ricavati dal parsing     per creare gli oggetti che modellano il dataset
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
I Rest Controller gestiscono quindi le richieste che mappano, ritornando oggetti in formato JSON contenenti le risposte. Sono definite eccezioni nel caso in cui sia passato un numero errato di attributi in una richiesta, se un certo attributo non esiste etc.

![diagrammadisequenza](https://user-images.githubusercontent.com/48209182/59589469-4c65d580-90ea-11e9-9a14-a7fbfe21f1f2.png)

Ogni Rest Controller utilizza l'istanza della classe di Utility che le è stata iniettata per ottenere i dati necessari a rispondere alla richiesta. 
Ad esempio, quando il Rest Controller DataController riceva una richiesta /metaF, si ha la seguente sequenza per l'ottenimento dei metadati degli oggetti Farmacia

![meta](https://user-images.githubusercontent.com/48209182/59592571-96ea5080-90f0-11e9-99d5-6b0bc2d094d9.png)