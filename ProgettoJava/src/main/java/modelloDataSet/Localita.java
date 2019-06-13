package modelloDataSet;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
/**
 * Classe che contiene coordinate ed indirizzo di una Farmacia (riga del dataset)
 * @author Marco
 *
 */
public class Localita {
	
	private double latitudine;
	private double longitudine;
	private String indirizzo;
	//private Comune comune;
	
	/*public Comune getComune() {
		return comune;
	}*/
	
	public Localita(double latitudine, double longitudine, String indirizzo) {
		this.latitudine=latitudine;
		this.longitudine=longitudine;
		this.indirizzo=indirizzo;
	}
	
	public double getLatitudine() {
		return latitudine;
	}
	
	public double getLongitudine() {
		return longitudine;
	}
	
	/*public void setComune(Comune comune) {
		this.comune=comune;
	}*/
	
	public void setLat(double latitudine) {
		this.latitudine=latitudine;
	}

	public void setLong(double longitudine) {
		this.longitudine=longitudine;
	}
	
	
	public ArrayList<JSONObject> getMetaDati() throws ParseException {
		JSONParser parser = new JSONParser();
		JSONArray jsonArr = new JSONArray();
		ArrayList<JSONObject> temp = new ArrayList<JSONObject>();
		temp.add((JSONObject) parser.parse("{\"Alias\":\"Latitudine\",\"Source Field\":\"LATITUDINE\",\"Type\":\"double\"}") );
		temp.add((JSONObject) parser.parse("{\"Alias\":\"Longitudine\",\"Source Field\":\"LONGITUDINE\",\"Type\":\"double\"}") );
		temp.add((JSONObject) parser.parse("{\"Alias\":\"indirizzo\",\"Source Field\":\"INDIRIZZO\",\"Type\":\"String\"}") );
		return temp;
	}
}
