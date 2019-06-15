package Utility;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import modelloDataSet.Comune;
import modelloDataSet.Farmacia;
import modelloDataSet.MetaData;
import modelloDataSet.Provincia;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import Utility.Filter.Filter;
import Utility.Filter.FilterUtils;
/**Classe che si occupa di cercare e scorrere i dati in un ArrayList contente oggetti di tipo Farmacia. Viene utilizzata in particolare dai Controller per elaborare i dati
 * e ritornare i risultati delle elaborazioni, assieme alle classi che la estendono, GPS e Checker, che fanno ulteriori elaborazioni sull'ArrayList di Farmacia.
 * 
 * @author Marco Esposito
 */


public class scannerDati implements Filter{

	public ArrayList<Farmacia> f;
	public Calcolatrice calc = new Calcolatrice();
	public scannerDati(ArrayList<Farmacia> f) {
		this.f = f;
	}
	
	
	@Override
	public Collection filterField(String fieldName, String operator, Object value) throws NoSuchFieldException, SecurityException {
		FilterUtils<Farmacia> utils = new FilterUtils<Farmacia>();
		boolean has=false;
		boolean has2=false;
		try {
			Comune.class.getDeclaredField(fieldName);
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
		System.out.println(has);
		if(has) {
			System.out.println("YAS");
			String classe = "comune";
			return utils.select(f, fieldName, operator, value, classe, null, false);
		} else if(has2) {
			String classe1 = "comune";
			String classe2 = "provincia";
			return utils.select(f, fieldName, operator, value, classe1, classe2, true);
		}
		else {
			return utils.select(f, fieldName, operator, value);
		}
	}
	

	

	/** Ritorna i Metadati degli oggetti Farmacia (ossia, per ogni oggetto al suo interno, l'alias dell'oggetto, l'attributo del dataset da cui deriva e il tipo)
	 *  in formato JSON. Utilizza l'interfaccia {@link modelloDataSet.MetaData } per accedere all'informazione sui metadati. Poichè Farmacia contiene un riferimento
	 *  a Comune, che contiene a sua volta un riferimento a Provincia, sono visualizzati in automatico i metadati anche di queste classi.
	 * 
	 * @return ArrayList<JSONObject> - Metadati in formato JSON
	 * @throws ParseException
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 */
	public ArrayList<JSONObject> getMetaFarmacia() throws ParseException, NoSuchMethodException, SecurityException {
		MetaData tmp = new Farmacia();
		return tmp.getMetaDati().getData();
	}
	
	/**Ritorna i Metadati degli oggetti Comune (ossia, per ogni oggetto al suo interno, l'alias dell'oggetto, l'attributo del dataset da cui deriva e il tipo)
	 *  in formato JSON. Utilizza l'interfaccia {@link modelloDataSet.MetaData } per accedere all'informazione sui metadati. Poichè Comune contiene un riferimento
	 *   a Provincia, sono visualizzati in automatico anche i metadati di Provincia.
	 * @return ArrayList<JSONObject> - Metadati in formato JSON
	 * @throws ParseException
	 * @throws SecurityException 
	 * @throws NoSuchMethodException
	 */
	public ArrayList<JSONObject> getMetaComune() throws ParseException, NoSuchMethodException, SecurityException {
		MetaData tmp = new Comune();
		return tmp.getMetaDati().getData();
	}
	
	/**Ritorna i Metadati degli oggetti Provincia (ossia, per ogni oggetto al suo interno, l'alias dell'oggetto, l'attributo del dataset da cui deriva e il tipo)
	 *  in formato JSON. Utilizza l'interfaccia {@link modelloDataSet.MetaData } per accedere all'informazione sui metadati.
	 * @return ArrayList<JSONObject> - Metadati in formato JSON
	 * @throws ParseException
	 * @throws SecurityException 
	 * @throws NoSuchMethodException
	 */
	public ArrayList<JSONObject> getMetaProvincia() throws ParseException, NoSuchMethodException, SecurityException {
		MetaData tmp = new Provincia();
		return tmp.getMetaDati().getData();
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
	 * @param String - nome
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
	 * @param String - nome
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
	 * @return ArrayList<Farmacia> - oggetti Farmacia che contengono un riferimento all'oggetto Comune la cui descrizione coincide con la stringa in ingresso
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
	 * @param String - Provincia
	 * @return ArrayList<Farmacia> - oggetti Farmacia che contengono un riferimento all'oggetto Provincia la cui descrizione coincide con la stringa in ingresso (contenuto in particolare nell'istanza di Comune presente in Farmacia)
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
	 * Provincia usando il metodo checkMismatch di Checker
	 * @param String provincia
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
				codice = Integer.toString(temp2.getCodiceProvincia());
				iva = Integer.toString(temp.getPartitaIVA());
				if (iva.contentEquals("0"))
					p.add("Farmacia " + temp.getDescrizione() + " senza Partita Iva disponibile");
				else if (((Checker) this).checkMismatch(iva, temp2))
					p.add(iva);
				else
					p.add(new String("Farmacia " + temp.getDescrizione() + " Presenta un mismatch fra Codice Provincia "
							+ codice + " e Partita Iva " + iva));
			}
		}
		return p;
	}
	
	/**
	 * Ritorna l'elenco dei Dispensari in una provincia.
	 * @param String -- Nome Comune
	 * @return ArrayList<Farmacia> -- Elenco Dispensari
	 */
	public ArrayList<Farmacia> getDispensariProvincia(String provincia){
		ArrayList<Farmacia> tmp = cercaDispensari();
		if (tmp.isEmpty()) return null;
		for(int i=0; i<tmp.size(); i++) {
			if(!tmp.get(i).getComune().getProvincia().getNomeProvincia().equalsIgnoreCase(provincia)) tmp.remove(i);
		}
		return tmp;
	}
	
	/**
	 * Ritorna l'elenco dei Dispensari in un comune.
	 * @param String -- Nome Comune
	 * @return ArrayList<Farmacia> -- Elenco Dispensari
	 */
	public ArrayList<Farmacia> getDispensariComune(String comune){
		ArrayList<Farmacia> tmp = cercaDispensari();
		if (tmp.isEmpty()) return null;
		for(int i=0; i<tmp.size(); i++) {
			if(!tmp.get(i).getComune().getNomeComune().equalsIgnoreCase(comune)) tmp.remove(i);
		}
		return tmp;
	}
	
	/**
	 * Ritorna l'elenco dei Dispensari tramite il campo Descrizione Tipologia (oggetto String tipologia di Farmacia).
	 * @return ArrayList<Farmacia> -- Elenco Dispensari
	 */
	public ArrayList<Farmacia> cercaDispensari(){
		ArrayList<Farmacia> tmp = new ArrayList<Farmacia>();
		for(int i=0; i<f.size(); i++) {
			System.out.println(f.get(i).getCodiceTipologia());
			if(f.get(i).getCodiceTipologia()==3) tmp.add(f.get(i));
		}
	return tmp;
	}

}
	

	
