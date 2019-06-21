package com.example.demo;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import Utility.Checker;
import Utility.GPS;
import Utility.Parser;
import Utility.scannerDati;
import modelloDataSet.Farmacia;
/**Progetto Programmazione ad Oggetti
 *@author Marco Esposito
 *
 */
@SpringBootApplication
public class ProgettoJavaApplication {
	static ArrayList<Farmacia> Farmacie;
	/**
	 *  *Applicazione che, all'avvio, effettua il  download del data-set che contiene i dati in formato CSV
	 *partendo dall’indirizzo fornito, dopo opportuna decodifica del data-set ID (formato JSON) che contiene appunto l'indirizzo. 
	 *Per effetuare le operazioni di Parsing viene utilizzata la classe {@link Utility.Parser}, che effettua
	 *sia il parsing del JSON che del file .csv, usando inoltre i dati ottenuti dal parsing per creare gli
	 *oggetti che modellano il dataset. Una volta ricavati questi ultimi, si crea un'ArrayList contenente gli oggetti di
	 *tipo Farmacia (rappresentanti una riga del file .csv) che viene usato per creare le classi di {@link Utility} che vengono iniettate all'avvio di Spring nei Rest Controller.
	 * @param args
	 * @throws ParseException
	 * @throws IOException
	 */
	public static void main(String[] args) throws ParseException, IOException {

		Parser pars = new Parser();
		//eseguo il parsing del data-set ID e il download dei file csv

		//prima di tutto salvo il data-set ID all'interno della variabile String data, copiando il contenuto dall'url assegnato
		String url = "https://www.dati.gov.it/api/3/action/package_show?id=46fd5cc3-300a-45ae-89de-98e24919e2d3";
		URLConnection openConnection;
		String data="";
		String line=""; 
		InputStream in= null;
		try {
			openConnection = new URL(url).openConnection();
			openConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
			in = openConnection.getInputStream();
			InputStreamReader reader = new InputStreamReader(in);
			BufferedReader buf = new BufferedReader(reader); 

			while ((line = buf.readLine())!= null ) {
				data+= line;
				System.out.println( line );
			}

		} 
		catch (MalformedURLException e) {
			System.out.println("Malformed URL: " + e.getMessage());
		}
		catch (IOException e1) {
			System.out.println("I/O Exception: " + e1.getMessage());
		}
		finally {
			if(in!=null) in.close();
		}

		//ora data contiene il dataset-ID, dal quale devo ricavare l'URL al quale si trova il file csv assegnato. Utilizzo il metodo getURL della classe Parser
		//per fare il parsing di data (stringa che contiene un oggetto in formato JSON) e ricavare appunto l'URL
		try {
			url = pars.getURL(data);
			System.out.println("URL DataSet: '"+url+"'");
			in = URI.create(url).toURL().openStream();
			//trovato l'URL e aperta la connessione, procedo a copiare il file csv
			Path targetPath = new File("Elenco-Farmacie.csv").toPath(); 
			Files.copy(in, targetPath, StandardCopyOption.REPLACE_EXISTING); 
			in.close();
		}
		catch (MalformedURLException e) {
			System.out.println("Malformed URL: " + e.getMessage());
		}
		catch (IOException e) {
			System.out.println("I/O Exception: " + e.getMessage());
		} 
		finally {
			if(in!=null) in.close();
		}

		//eseguo il parsing del file .csv e creo gli oggetti che modellano il data-set
		pars.parsingCSV("Elenco-Farmacie.csv");
		Farmacie=pars.getFarmacie();

		//avvio l'applicazione Spring. All'avvio, ai RestController viene iniettata automaticamente un'istanza dei Bean definiti sotto, che saranno utilizzati
		//per ricavare/elaborare i dati (già inseriti da Parser negli oggetti creati per modellare il dataset).

		SpringApplication.run(ProgettoJavaApplication.class, args);
	}

	//in seguito l'inizializzazione dei Bean usati dai Rest Controller

	@Bean
	public ArrayList<Farmacia> farmacie(){
		return Farmacie;
	}

	@Bean
	@Qualifier("scanner")
	public scannerDati scanner() {
		return new scannerDati(Farmacie);
	}

	@Bean
	@Qualifier("gps")
	public GPS gps() {
		return new GPS(Farmacie);
	}

	@Bean
	@Qualifier("check")
	public Checker check() {
		return new Checker(Farmacie);
	}

}
