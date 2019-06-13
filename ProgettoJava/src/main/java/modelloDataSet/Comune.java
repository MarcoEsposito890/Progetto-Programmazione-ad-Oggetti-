package modelloDataSet;

import java.util.ArrayList;
/** Classe che modella un Comune del dataset. Al suo interno contiene un riferimento a un oggetto Provincia. 
 * Inoltre estende la classe Localita, che contiene coordinate ed indirizzo della Farmacia che contiene il riferimento al Comune.
 * @author Marco Esposito
 */

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Comune extends Localita{
	
	private Provincia provincia;
	private int codiceComune;
	private String nomeComune;
	
	public Comune(double lat, double longi, String indirizzo) {
		super(lat, longi, indirizzo);
	}
	
	public Provincia getProvincia() {
		return provincia;
	}
	
	public int getCodiceComune() {
		return codiceComune;
	}
	
	public String getNomeComune() {
		return nomeComune;
	}
	
	public void setProvincia(Provincia provincia) {
		this.provincia=provincia;
	}
	
	public void setCodice(int codiceComune) {
		this.codiceComune=codiceComune;
	}

	public void setDescrizione(String nomeComune) {
		this.nomeComune=nomeComune;
	}
	
	public ArrayList<JSONObject> getMetaDati() throws ParseException {
		JSONParser parser = new JSONParser();
		JSONArray jsonArr = new JSONArray();
		ArrayList<JSONObject> temp = new ArrayList<JSONObject>();
		temp.add((JSONObject) parser.parse("{\"Alias\":\"codice\",\"Source Field\":\"CODICE COMUNE ISTAT\",\"Type\":\"String\"}") );
		temp.add((JSONObject) parser.parse("{\"Alias\":\"descrizione\",\"Source Field\":\"DESCRIZIONE COMUNE\",\"Type\":\"String\"}") );
		temp.addAll(provincia.getMetaDati());
		temp.addAll(super.getMetaDati());
		return temp;
	}

}
