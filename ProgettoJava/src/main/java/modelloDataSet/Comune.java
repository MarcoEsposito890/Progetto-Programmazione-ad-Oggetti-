package modelloDataSet;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Comune extends Localita{
	
	private Provincia provincia;
	private int codice;
	private String descrizione;
	
	public Comune(double lat, double longi, String indirizzo) {
		super(lat, longi, indirizzo);
	}
	
	public Provincia getProvincia() {
		return provincia;
	}
	
	public int getCodice() {
		return codice;
	}
	
	public String getDescrizione() {
		return descrizione;
	}
	
	public void setProvincia(Provincia provincia) {
		this.provincia=provincia;
	}
	
	public void setCodice(int codice) {
		this.codice=codice;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione=descrizione;
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
