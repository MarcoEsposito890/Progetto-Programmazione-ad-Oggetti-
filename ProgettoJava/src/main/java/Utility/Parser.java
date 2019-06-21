package Utility;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import modelloDataSet.Comune;
import modelloDataSet.Farmacia;
import modelloDataSet.Provincia;
/** La classe Parser contiene metodi utili per eseguire il parsing del dataset-ID fornito (in formato JSON) e del file csv assegnato.
 *	In particolare, viene utilizzata all'avvio dell'applicazione per ricavare l'URL per scaricare il file CSV e poi per effettuare il
 *	parsing di quest'ultimo. Si occupa inoltre di creare gli oggetti che modellano il dataset, utilizzando i dati provenienti dal parsing.
 */
public class Parser{

	private ArrayList<String> campi= new ArrayList<String>();
	private ArrayList<HashMap<String, String>> dati = new ArrayList<HashMap<String, String>>();
	//esegue il parsing del file csv

	/**
	 * Metodo utilizzato per effettuare il parsing del file. In particolare, inizialmente salva l'header del file in un ArrayList; poi crea 
	 * un ArrayList di HashMap<String, String>, in cui, per ogni riga (farmacia) del dataset 
	 * salva le coppie attributo-valore corrispondenti agli attributi dell'header e al loro corrispondente valore per quella riga. Al termine del parsing, si ha quindi a disposizione l'ArrayList campi,
	 * che contiene gli attributi dell'header, ed è accessibili con il metodo getter della classe Parser, e l'ArrayList dati, contentente un HashMap<Attributo, Valore> per ogni riga
	 * del file CSV, anch'essa accessibile con il corrispondente metodo getter.
	 * @param String filename - nome del file in formato CSV di cui fare il parsing
	 */
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
				else campi.add("LONGITUDINE"); //altrimenti legge un carattere in più nell'ultimo campo 
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
	/**
	 * Metodo che fa il parsing della stringa in formato JSON contenente il dataset-ID e cerca l'URL utile per scaricare il file, cercando quella che
	 * porta ad un file CSV tra le URL disponibili nel dataset-ID.
	 * @param String data - Stringa contenente il dataset-ID in formato JSON (opportunamento scaricato all'avvio dell'applicazione {@link com.example.demo.ProgettoJavaApplication}
	 * @return String URL da cui scaricare il file CSV
	 * @throws ParseException
	 */
	public String getURL(String data) { 
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObj = new JSONObject();
		Object obj;
		try {
			obj = jsonParser.parse(data);
			jsonObj = (JSONObject) obj;
			JSONObject result = (JSONObject) jsonObj.get("result");
			JSONArray resource = (JSONArray) result.get("resources");
			for(Object o : resource) {
				JSONObject o1 = (JSONObject) o;
				if(o1.get("format").equals("csv")) {
					return (String) o1.get("url");
				}	
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**Ritorna l'ArrayList contente i campi dell'header del file
	 * 
	 * @return ArrayList<String> - Lista degli header del file csv
	 */
	public ArrayList<String> getHeader(){
		System.out.println("Header File CSV: "+campi.toString());
		System.out.println("Numero campi header: "+campi.size());
		return campi;
	}


	/**Ritorna l'ArrayList contente le HashMap rappresentanti ogni riga del file.
	 * 
	 * @return ArrayList<HashMap<String, String>> -- ArrayList contenente una HashMap per ogni riga del file, ognuna contente le coppie attributi/valori per quella riga
	 */
	public ArrayList<HashMap<String, String>> getDati(){
		return dati;
	}


	/**
	 * A partire dall'ArrayList dati presente nella classe (da riempire tramite il metodo ParsingCSV),
	 * crea un ArrayList di oggetti Farmacia, organizzando i dati nelle classi opportune (vedere diagrammi UML) ed effettuando le opportune conversioni se necessario
	 * (in particolare, converte stringhe in numeri tenendo conto di campi vuoti e altre eccezioni relative ai formati numerici).
	 * @return ritorna un ArrayList<Farmacia>
	 * @throws NumberFormatException -- Controlla che tutti i campi siano convertibili in valori numerici e gestisce i campi vuoti
	 */
	public ArrayList<Farmacia> getFarmacie(){
		ArrayList<Farmacia> farmacie = new ArrayList<Farmacia>(); 
		ArrayList<Comune> comuni = new ArrayList<Comune>();
		ArrayList<Provincia> province = new ArrayList<Provincia>();
		String dummy="";
		String dummy2="";
		for(int i=0; i<dati.size()-1; i++) { 
			Farmacia temp = new Farmacia();

			//Seguendo lo schema di incapsulamento dei dati, creo prima un oggetto Provincia, che andrà inserito poi nell'oggetto Comune. Aggiungo un controllo per non avere oggetti Provincia duplicati (più comuni possono fare riferimento allo stesso oggetto Provincia)
			Provincia p = new Provincia();
			p = new Provincia();
			p.setDescrizione(dati.get(i).get("DESCRIZIONE PROVINCIA"));
			int tmp=Integer.parseInt(dati.get(i).get("CODICE PROVINCIA ISTAT"));
			p.setCodice(tmp);
			p.setSigla(dati.get(i).get("SIGLA PROVINCIA")); 
			if (province.contains(p)) p=province.get(province.indexOf(p));
			else province.add(p);

			//Si crea un oggetto Comune, il quale estende la classe Localita (quindi estraiamo prima dai dati i costruttori della superclasse). Come sopra, controllo prima di non averne creato uno uguale (più farmacie possono fare riferimento allo stesso oggetto Comune)
			dummy= dati.get(i).get("LATITUDINE").replace(',', '.');
			if(dummy.contains("-")) dummy = "0"; //il simbolo "-" è usato nel dataset quando un campo è vuoto. Visto che i campi numerici sono di tipo int/double, quando si incontra "-" si mette a 0 quel campo.
			dummy2=dati.get(i).get("LONGITUDINE").replace(',', '.');
			if(dummy2.contains("-")) dummy2 = "0";
			String indirizzo= dati.get(i).get("INDIRIZZO");
			String frazione = dati.get(i).get("FRAZIONE");
			if(frazione.equals("-")) frazione = "Nessuna";
			Comune c = new Comune(Double.parseDouble(dummy), Double.parseDouble(dummy2), indirizzo, frazione);
			c.setProvincia(p);
			c.setCodice(Integer.parseInt(dati.get(i).get("CODICE COMUNE ISTAT")));
			c.setDescrizione(dati.get(i).get("DESCRIZIONE COMUNE"));
			if (comuni.contains(c)) c=comuni.get(comuni.indexOf(p));
			else comuni.add(c);

			//Infine si popola l'oggetto Farmacia, passandogli il riferimento all'oggetto Comune creato prima oltre gli altri dati prelevati dal DataSet
			temp.setCodiceTipologia(Integer.parseInt(dati.get(i).get("CODICE TIPOLOGIA")));
			temp.setDescrizione(dati.get(i).get("DESCRIZIONE FARMACIA"));
			temp.setID(Integer.parseInt(dati.get(i).get("CODICE IDENTIFICATIVO FARMACIA")));
			temp.setTipologia(dati.get(i).get("DESCRIZIONE TIPOLOGIA"));
			Double IVA;
			try {
				IVA=Double.parseDouble(dati.get(i).get("PARTITA IVA")); 
			} catch(NumberFormatException e) {
				IVA=0.0;
			}
			temp.setIVA(((Double) IVA).intValue());
			temp.setComune(c);
			farmacie.add(temp);

		}
		return farmacie;
	}
}
