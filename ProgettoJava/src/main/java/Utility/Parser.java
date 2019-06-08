package Utility;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import modelloDataSet.Comune;
import modelloDataSet.Farmacia;
import modelloDataSet.Provincia;


public class Parser{
	
	private ArrayList<String> campi= new ArrayList<String>();
	private ArrayList<HashMap<String, String>> dati = new ArrayList<HashMap<String, String>>();
	//esegue il parsing del file csv
	
	public void parsingCSV(String filename) {
		try {
			Scanner in =  new Scanner(new BufferedInputStream (new FileInputStream (filename))).useDelimiter("\n");
			//salvo l'header del file csv (contente i campi della tabella) nell'ArrayList campi
			String header = "";
	        header=in.next();
			String[] columns = header.split(";");
			for(int i=0; i<columns.length; i++) {
				if(i!=columns.length-1) {
				campi.add(columns[i]);
				}
				else campi.add("LONGITUDINE"); //altrimenti inserisce un carattere in più nell'ultimo campo 
			}
			String temp="";
			in.useDelimiter("\n|;");
			//Per ogni riga del file successiva all'header, salvo i dati corrispondenti ad ogni campo in un'HashMap (<Campo, Valore>). 
			//Le HashMap vengono poi salvate in un ArrayList (contente un'HashMap per ogni riga del file, quindi una per ogni farmacia)
			boolean EOF = false;
			int counter=0;
			while(!EOF){
				HashMap<String, String> mappa = new HashMap<String, String>();
				for (int i=0; i<campi.size(); i++) {
					temp=in.next();
					mappa.put(campi.get(i), temp);
					if (!in.hasNext()) {
						EOF=true;
						break;
					}
				}
				dati.add(mappa);
				counter++;
			}
		System.out.println("Numero righe file: "+counter);	
		in.close();
		}
		catch (IOException e) {
	        System.out.println("I/O Error: " + e.getMessage());
			}
	}
	
	public String getURL(String data) throws ParseException {
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObj = new JSONObject();
		Object obj = jsonParser.parse(data);
		jsonObj = (JSONObject) obj;
		JSONObject result = (JSONObject) jsonObj.get("result");
		JSONArray resource = (JSONArray) result.get("resources");
		for(Object o : resource) {
			JSONObject o1 = (JSONObject) o;
			if(o1.get("format").equals("csv")) {
				return (String) o1.get("url");
			}	
		}
		return null;
	}
	
	//ritorna l'ArrayList contente i campi dell'header del file
	public ArrayList<String> getHeader(){
		System.out.println("Header File CSV: "+campi.toString());
		System.out.println("Numero campi header: "+campi.size());
		return campi;
	}
	
	//ritorna l'ArrayList contente le HashMap rappresentanti ogni riga del file (che a loro volta contengono i dati corrispodenti a un campo dell'header)
	public ArrayList<HashMap<String, String>> getDati(){
		return dati;
	}
	
	

	//crea un ArrayList di oggetti Farmacia, organizzando i dati nelle classi opportune (vedere diagrammi UML) ed effettuando le opportune conversioni se necessario
	public ArrayList<Farmacia> getFarmacie(){
		ArrayList<Farmacia> farmacie = new ArrayList<Farmacia>();
		for(int i=0; i<dati.size()-1; i++) { //rivedi qui dati.size()-1
			Farmacia temp = new Farmacia();
			
			//Seguendo lo schema di incapsulamento dei dati, creo prima un oggetto Provincia, che andrà inserito nell'oggetto Comune
			Provincia p = new Provincia();
			p.setDescrizione(dati.get(i).get("DESCRIZIONE PROVINCIA")); 
			p.setCodice(dati.get(i).get("CODICE PROVINCIA ISTAT"));
			p.setSigla(dati.get(i).get("SIGLA PROVINCIA"));
			
			//Si crea un oggetto Comune, il quale estende la classe Localita (quindi estraiamo prima dai dati i costruttori della superclasse)
			String dummy = dati.get(i).get("LATITUDINE").replace(',', '.');
			if(dummy.contains("-")) dummy = "0";
			String dummy2=dati.get(i).get("LONGITUDINE").replace(',', '.');
			if(dummy2.contains("-")) dummy2 = "0";
			String indirizzo= dati.get(i).get("INDIRIZZO");
			Comune c = new Comune(Double.parseDouble(dummy), Double.parseDouble(dummy2), indirizzo);
			c.setProvincia(p);
			c.setCodice(Integer.parseInt(dati.get(i).get("CODICE COMUNE ISTAT")));
			c.setDescrizione(dati.get(i).get("DESCRIZIONE COMUNE"));
			
			//Infine si popola l'oggetto Farmacia, passandogli il riferimento all'oggetto Comune creato prima oltre i dati prelevati dal DataSet
			temp.setCodiceTipologia(Integer.parseInt(dati.get(i).get("CODICE TIPOLOGIA")));
			temp.setDescrizione(dati.get(i).get("DESCRIZIONE FARMACIA"));
			temp.setID(Integer.parseInt(dati.get(i).get("CODICE IDENTIFICATIVO FARMACIA")));
			temp.setTipologia(dati.get(i).get("DESCRIZIONE TIPOLOGIA"));
			dummy=dati.get(i).get("PARTITA IVA");
			if(dummy.contains("-")) dummy = "0";
			temp.setIVA(dummy);
			temp.setComune(c);
			farmacie.add(temp);
		}
		return farmacie;
	}
}
