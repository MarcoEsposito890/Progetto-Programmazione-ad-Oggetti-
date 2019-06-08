package modelloDataSet;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Provincia {
	
	private String codice;
	private String sigla;
	private String descrizione;
	
	
	public String getCodice() {
		return codice;
	}
	
	public String getSigla() {
		return sigla;
	}
	
	public String getDescrizione() {
		return descrizione;
	}
	
	public void setSigla(String sigla) {
		this.sigla=sigla;
	}
	
	public void setCodice(String codice) {
		this.codice=codice;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione=descrizione;
	}
	
	public ArrayList<JSONObject> getMetaDati() throws ParseException {
		JSONParser parser = new JSONParser();
		JSONArray jsonArr = new JSONArray();
		ArrayList<JSONObject> temp = new ArrayList<JSONObject>();
		temp.add((JSONObject) parser.parse("{\"Alias\":\"codice\",\"Source Field\":\"CODICE PROVINCIA ISTAT\",\"Type\":\"String\"}") );
		temp.add((JSONObject) parser.parse("{\"Alias\":\"sigla\",\"Source Field\":\"SIGLA PROVINCIA\",\"Type\":\"String\"}") );
		temp.add((JSONObject) parser.parse("{\"Alias\":\"descrizione\",\"Source Field\":\"DESCRIZIONE PROVINCIA\",\"Type\":\"String\"}") );
		return temp;
	}

}
