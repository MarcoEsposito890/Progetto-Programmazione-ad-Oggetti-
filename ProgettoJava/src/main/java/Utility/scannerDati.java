package Utility;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import Utility.Filter.Filter;
import Utility.Filter.FilterUtils;
import modelloDataSet.Comune;
import modelloDataSet.Farmacia;
import modelloDataSet.MetaData;
import modelloDataSet.Provincia;
/**Classe che si occupa di cercare e scorrere i dati in un ArrayList contente oggetti di tipo Farmacia. Viene utilizzata in particolare dai Controller per elaborare i dati
 * e ritornare i risultati delle elaborazioni. Le classi che la estendono, {@link Utility.GPS} e  {@link Utility.Checker}
 * 
 * @author Marco Esposito
 */


public class scannerDati implements Filter{

	public ArrayList<Farmacia> f;
	public Calcolatrice calc = new Calcolatrice();
	public scannerDati(ArrayList<Farmacia> f) {
		this.f = f;
	}

	/** Filtraggio generico e implementazione del metodo {@link Utility.Filter.Filter#filterField(String, String, Object)}. Si crea un'istanza di {@link Utility.Filter.FilterUtils}, poi si verifica a quale classe appartiene il campo indicato dal parametro fieldName
	 * e si richiama il metodo adeguato di filterUtilis di conseguenza.
	 * @param fieldname - campo da filtrare
	 * @param operator - operatore da utilizzare
	 * @param value - valore con cui effettuare i confronti
	 * @return  Collection - Collection contenente i risultati del filtraggio
	 * @return ArrayList<JSONObject> - Metadati in formato JSON
	 * @throws SecurityException 
	 * @throws NoSuchFieldException 
	 */
	@Override
	public Collection filterField(String fieldName, String operator, Object value) throws NoSuchFieldException, SecurityException {
		FilterUtils<Farmacia> utils = new FilterUtils<Farmacia>();
		boolean has=false;
		boolean has2=false;
		boolean has3=false;
		//i try-catch seguenti verificano a quale classe appartiene il campo indicato dal parametro fieldName (Comune, Localita, Provincia o Farmacia).
		try {
			Comune.class.getDeclaredField(fieldName);
			has=true;

		}
		catch(NoSuchFieldException e1) {
			has=false;
		}

		try {
			Comune.class.getSuperclass().getDeclaredField(fieldName);
			has=true;
		}
		catch(NoSuchFieldException e1) {
			has=false;
		}

		try {
			Provincia.class.getDeclaredField(fieldName);
			has2=true;
		}
		catch(NoSuchFieldException e1) {
			has2=false;
		}

		try {
			Farmacia.class.getDeclaredField(fieldName);
			has3=true;
		}
		catch(NoSuchFieldException e1) {
			has3=false;
		}

		if(has) { //il filtraggio è fatto su un membro di Comune (o Localita)
			String classe = "comune";
			return utils.select(f, fieldName, operator, value, classe, null, false);
		} else if(has2) {// il filtraggio è fatto su un membro di Provincia
			String classe1 = "comune";
			String classe2 = "provincia";
			return utils.select(f, fieldName, operator, value, classe1, classe2, true);
		}
		else if (has3){// il filtraggio è fatto su un membro di Farmacia che non sia uno dei precedenti
			return utils.select(f, fieldName, operator, value);
		}
		else return null;
	}

	/**Classe utilizzata per calcolare statistiche sui dati del data-set. Poichè gran parte dei campi sono di tipo String, si sono selezionati solo alcuni campi, come riportato nel metodo,
	 * fra quelli validi (escludendo tra i campi numerici altri codici identificativi).
	 * Sono implementati max, min, media, varianza.
	 * 
	 * @param String fieldname - campo da filtrare
	 * @param String operator - operatore da utilizzare
	 * @return
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public double Statistiche(String fieldName, String operatore) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		FilterUtils<Farmacia> utils = new FilterUtils<Farmacia>();
		if (!fieldName.equalsIgnoreCase("LATITUDINE") && !fieldName.equalsIgnoreCase("LONGITUDINE") && !fieldName.equalsIgnoreCase("PARTITAIVA")) return 0;
		boolean has=false;
		boolean has2=false;
		//i try-catch seguenti verificano a quale classe appartiene il campo indicato dal parametro fieldName (Comune, Localita, o Farmacia).
		try {
			Comune.class.getDeclaredField(fieldName);
			has=true;

		}
		catch(NoSuchFieldException e1) {
			has=false;
		}

		try {
			Comune.class.getSuperclass().getDeclaredField(fieldName);
			has=true;
		}
		catch(NoSuchFieldException e1) {
			has=false;
		}

		try {
			Farmacia.class.getDeclaredField(fieldName);
			has2=true;
		}catch(NoSuchFieldException e1) {
			has2=false;
		}
		if(has2) { //a seconda del risultato dei try-catch sopra, si chiama la versione di prelevaCampo adatta
			ArrayList<Double> d = new ArrayList<Double>();
			d=utils.prelevaCampo(f, fieldName);
			return calc.stats(operatore, d);
		}
		else if(has) {
			ArrayList<Double> d = new ArrayList<Double>();
			d=utils.prelevaCampo(f, fieldName, "comune");
			return calc.stats(operatore, d);
		} else return 0;
	}


	/** Ritorna i Metadati degli oggetti Farmacia (ossia, per ogni oggetto al suo interno, l'alias dell'oggetto, l'attributo del dataset da cui deriva e il tipo)
	 *  in formato JSON. Utilizza l'interfaccia {@link modelloDataSet.MetaData} per accedere all'informazione sui metadati. Poichè Farmacia contiene un riferimento
	 *  a Comune, che contiene a sua volta un riferimento a Provincia, sono visualizzati in automatico i metadati anche di queste classi richiamando quelli di Farmacia.
	 * 
	 * @return ArrayList<JSONObject> - Metadati in formato JSON
	 * @throws ParseException
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 */
	public ArrayList<JSONObject> getMetaFarmacia() throws ParseException, NoSuchMethodException, SecurityException {
		MetaData tmp = new Farmacia();
		return tmp.getMetaDati().getData(); //getMetaDati() ritorna una variabile di tipo MetaDataStore, per cui usiamo il metodo getData() di MetaDataStore per ricavare i metadati veri e propri
	}

	/**Ritorna i Metadati degli oggetti Comune (ossia, per ogni oggetto al suo interno, l'alias dell'oggetto, l'attributo del dataset da cui deriva e il tipo)
	 * in formato JSON. Utilizza l'interfaccia {@link modelloDataSet.MetaData} per accedere all'informazione sui metadati. Poichè Comune contiene un riferimento
	 * a Provincia, sono visualizzati in automatico anche i metadati di Provincia richiamando quelli di Comune.
	 * @return ArrayList<JSONObject> - Metadati in formato JSON
	 * @throws ParseException
	 * @throws SecurityException 
	 * @throws NoSuchMethodException
	 */
	public ArrayList<JSONObject> getMetaComune() throws ParseException, NoSuchMethodException, SecurityException {
		MetaData tmp = new Comune();
		return tmp.getMetaDati().getData(); //getMetaDati() ritorna una variabile di tipo MetaDataStore, per cui usiamo il metodo getData() di MetaDataStore per ricavare i metadati veri e propri
	}

	/**Ritorna i Metadati degli oggetti Provincia (ossia, per ogni oggetto al suo interno, l'alias dell'oggetto, l'attributo del dataset da cui deriva e il tipo)
	 * in formato JSON. Utilizza l'interfaccia {@link modelloDataSet.MetaData } per accedere all'informazione sui metadati.
	 * @return ArrayList<JSONObject> - Metadati in formato JSON
	 * @throws ParseException
	 * @throws SecurityException 
	 * @throws NoSuchMethodException
	 */
	public ArrayList<JSONObject> getMetaProvincia() throws ParseException, NoSuchMethodException, SecurityException {
		MetaData tmp = new Provincia();
		return tmp.getMetaDati().getData(); //getMetaDati() ritorna una variabile di tipo MetaDataStore, per cui usiamo il metodo getData() di MetaDataStore per ricavare i metadati veri e propri
	}

	/**Cerca la Farmacia dato il nome
	 * 
	 * @param String - nome
	 * @return Farmacia - farmacia trovata
	 */
	public Farmacia cerca(String nome) {
		for (int i = 0; i < f.size(); i++) {
			if (f.get(i).getDescrizione().contains(nome))
				return f.get(i);
		}
		return null;
	}

	/**Cerca la Farmacia date le coordinate
	 * 
	 * @param double - latitudine
	 * @param double - longitudine
	 * @return Farmacia - farmacia trovata
	 */
	public Farmacia cercaCoordinate(double lat, double longi) {
		Farmacia p = new Farmacia();
		for (int i = 0; i < f.size(); i++) {
			double tmp1 = f.get(i).getComune().getLatitudine();
			double tmp2 = f.get(i).getComune().getLongitudine();
			if (tmp1 == lat && tmp2 == longi) {
				p=f.get(i); 
				return p;
			}
		}
		return p;
	}

	/**Cerca il Comune dato il nome
	 * 
	 * @param String nome - nome comune
	 * @return Comune - comune trovato
	 */
	public Comune cercaComune(String nome) {
		Farmacia p = new Farmacia();
		for (int i = 0; i < f.size(); i++) {
			String tmp1 = f.get(i).getComune().getNomeComune();
			if (tmp1.equals(nome)) {
				p=f.get(i);
				return p.getComune();
			}
		}
		return null;
	}

	/**Cerca la Provincia dato il nome
	 * 
	 * @param String nome - nome provincia
	 * @return Provincia - Provincia trovata
	 */
	public Provincia cercaProvincia(String nome) {
		Farmacia p = new Farmacia();
		for (int i = 0; i < f.size(); i++) {
			String tmp1 = f.get(i).getComune().getProvincia().getNomeProvincia();
			if (tmp1.equals(nome)) {
				p=f.get(i);
				return p.getComune().getProvincia();
			}
		}
		return null;
	}

	/**Ritorne le coordinate, data la Farmacia
	 * 
	 * @param String - Nome Farmacia
	 * @return HashMap<String,Double> - Dupla di coordinate (latitudine, longitudine)
	 */
	public  HashMap<String, Double> trovaCoordinate(String nome){
		HashMap<String, Double> coordinate = new HashMap<String, Double> ();
		Farmacia f2 = cerca(nome);
		if(f2!=null) { //eccezione
			coordinate.put("Latitudine", f2.getComune().getLatitudine());
			coordinate.put("Longitudine", f2.getComune().getLongitudine()); 
			return coordinate;
		}
		else return null;
	}

	/**Ritorna solo le farmacie in un determinato comune
	 * 
	 * @param String - Comune
	 * @return ArrayList<Farmacia> - oggetti Farmacia che contengono un riferimento all'oggetto Comune il cui nome coincide con la stringa in ingresso
	 */
	public ArrayList<Farmacia> cercaPerComune(String Comune) {
		ArrayList<Farmacia> p = new ArrayList<Farmacia>();
		for (int i = 0; i < f.size(); i++) {
			String nome = f.get(i).getComune().getNomeComune();
			if (nome.equalsIgnoreCase(Comune)) {
				p.add(f.get(i));
			}
		}
		return p;
	}

	/**Ritorna solo le farmacie in una determinata provincia
	 * 
	 * @param String Provincia
	 * @return ArrayList<Farmacia> - oggetti Farmacia che contengono un riferimento all'oggetto Provincia (o meglio, la cui istanza di Comune contiene il riferimento a quell'oggetto Provincia) il cui nome coincide con la stringa in ingresso 
	 */
	public ArrayList<Farmacia> cercaPerProvincia(String Provincia) {
		ArrayList<Farmacia> p = new ArrayList<Farmacia>();
		for (int i = 0; i < f.size(); i++) {
			String nome = f.get(i).getComune().getProvincia().getNomeProvincia();
			if (nome.equalsIgnoreCase(Provincia)) {
				p.add(f.get(i));
			}
		}
		return p;
	}

	/**Ritorna l'elenco delle Partite IVA con lo stesso
	 * Codice Provincia. Tiene conto dei mismatch fra Partita IVA e relativa
	 * Provincia usando il metodo {@link Utility.Checker#checkMismatch(String, Provincia)} di Checker
	 * @param String provincia - nome della provincia
	 * @return ArrayList<String> - Elenco Partite IVA
	 */
	// 
	public ArrayList<String> partitaIva(String provincia) {
		ArrayList<String> p = new ArrayList<String>();
		String codice;
		String iva;
		for (int i = 0; i < f.size(); i++) {
			Farmacia temp = f.get(i);
			Provincia temp2 = temp.getComune().getProvincia();
			if (temp2.getNomeProvincia().equalsIgnoreCase(provincia)) {
				int tmp=temp.getPartitaIVA();
				codice = Integer.toString(temp2.getCodiceProvincia()); //converto gli int in Stringhe, così è più semplice gestire i controlli (e il metodo checkMismatch di Checker fa appunto controlli su Stringhe)
				iva = Integer.toString(temp.getPartitaIVA());
				if (tmp==0) {
					p.add("Farmacia " + temp.getDescrizione() + " senza Partita Iva disponibile"); //gli attributi per cui non è presente un valore sono stati settati a 0 durante il parsing
					break;
				}
				else if (((Checker) this).checkMismatch(iva, temp2))
					p.add(iva); //se il controllo è andato a buon fine, aggiungo semplicemente la partita IVA
				else
					p.add(new String("Farmacia " + temp.getDescrizione() + " Presenta un mismatch fra Codice Provincia "
							+ codice + " e Partita Iva " + iva)); //altrimenti segnalo che il controllo non è andato a buon fine
			}
		}
		return p;
	}

	/**
	 * Ritorna l'elenco dei Dispensari (Farmacie con codice tipologia = 3) in una provincia.
	 * @param String -- Nome Comune
	 * @return ArrayList<Farmacia> -- Elenco Dispensari
	 */
	public ArrayList<Farmacia> getDispensariProvincia(String provincia){
		ArrayList<Farmacia> tmp = cercaDispensari();
		ArrayList<Farmacia> out = new ArrayList<Farmacia>();
		if (tmp.isEmpty()) return null;
		for(int i=0; i<tmp.size(); i++) {
			if(tmp.get(i).getComune().getProvincia().getNomeProvincia().equals(provincia)) out.add(tmp.get(i));
		}
		return out;
	}

	/**
	 * Ritorna l'elenco dei Dispensari (Farmacie con codice tipologia = 3) in un comune.
	 * @param String -- Nome Comune
	 * @return ArrayList<Farmacia> -- Elenco Dispensari
	 */
	public ArrayList<Farmacia> getDispensariComune(String comune){
		ArrayList<Farmacia> tmp = cercaDispensari();
		ArrayList<Farmacia> out = new ArrayList<Farmacia>();
		if (tmp.isEmpty()) return null;
		for(int i=0; i<tmp.size(); i++) {
			if(tmp.get(i).getComune().getNomeComune().equals(comune)) { out.add(tmp.get(i));
			}
		}
		return out;
	}

	/**
	 * Ritorna l'elenco dei Dispensari controllando il codice tipologia.
	 * @return ArrayList<Farmacia> -- Elenco Dispensari
	 */
	public ArrayList<Farmacia> cercaDispensari(){
		ArrayList<Farmacia> tmp = new ArrayList<Farmacia>();
		for(int i=0; i<f.size(); i++) {
			if(f.get(i).getCodiceTipologia()==3) tmp.add(f.get(i));
		}
		return tmp;
	}

	public ArrayList<Farmacia> getDati(){
		return f;
	}

}
	