package com.example.demo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
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
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import Utility.Parser;
import Utility.scannerDati;
import modelloDataSet.Farmacia;

@SpringBootApplication
public class ProgettoJavaApplication {
	static ArrayList<Farmacia> f;
	public static void main(String[] args) throws ParseException {
		
		Parser pars = new Parser();
		ArrayList<String> header;
		ArrayList<HashMap<String, String>> dati;
		//eseguo il parsing del dataset-ID/download dei file csv
		//prima di tutto salvo il dataset-ID all'interno della variabile String data, copiando il contenuto dell'url assegnato
		String url = "https://www.dati.gov.it/api/3/action/package_show?id=46fd5cc3-300a-45ae-89de-98e24919e2d3";
		URLConnection openConnection;
		String data="";
		 String line=""; 
		try {
		openConnection = new URL(url).openConnection();
	    openConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
	    InputStream in = openConnection.getInputStream();
	    InputStreamReader reader = new InputStreamReader(in);
		BufferedReader buf = new BufferedReader( reader ); //unisci tutto in un'unica riga (da InputStream a BufferedReader)
			  
		   while ( ( line = buf.readLine() ) != null ) {
			   data+= line;
			   System.out.println( line );
		   }
		 in.close(); //usa i finally per chiudere le connessioni
		 } 
		catch (MalformedURLException e) {
			 System.out.println("Malformed URL: " + e.getMessage());
		}
	    catch (IOException e1) {
	    	System.out.println("I/O Exception: " + e1.getMessage());
		}
		
		//ora data contiene il dataset-ID, dal quale devo ricavare l'URL al quale si trova il file csv assegnato. Utilizzo il metodo getUrl della classe Parser
		//per fare il parsing di data (contiene un oggetto in formato JSON) e ricavare appunto l'url
		try {
			url = pars.getURL(data);
			System.out.println("URL DataSet: '"+url+"'");
			InputStream in = URI.create(url).toURL().openStream();
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
		//avvio l'applicazione Spring. All'avvio, al RestController viene iniettata automaticamente un'istanza di Parser, che sarà utilizzata dalla classe
		//scannerDati all'interno di Controller per ricavare/elaborare i dati (opportunamente inseriti da Parser negli oggetti utilizzati per modellare il dataset).
		
	
		pars.parsingCSV("Elenco-Farmacie.csv");
		f=pars.getFarmacie();
		
		SpringApplication.run(ProgettoJavaApplication.class, args);
	}

	
	@Bean
	public ArrayList<Farmacia> farmacie(){
		return f;
	}
	
}
