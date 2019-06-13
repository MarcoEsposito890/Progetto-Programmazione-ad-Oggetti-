package modelloDataSet;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
/**
 * Classe che contiene informazioni sulla provincia dove si trova una Farmacia (riga del dataset)
 * @author Marco
 *
 */
public class Provincia {
	
	private int codiceProvincia;
	private String sigla;
	private String nomeProvincia;
	
	
	public int getCodiceProvincia() {
		return codiceProvincia;
	}
	
	public String getSigla() {
		return sigla;
	}
	
	public String getNomeProvincia() {
		return nomeProvincia;
	}
	
	public void setSigla(String sigla) {
		this.sigla=sigla;
	}
	
	public void setCodice(int codice) {
		this.codiceProvincia=codiceProvincia;
	}

	public void setDescrizione(String nomeProvincia) {
		this.nomeProvincia=nomeProvincia;
	}
	
	public ArrayList<JSONObject> getMetaDati() throws ParseException {
		JSONParser parser = new JSONParser();
		ArrayList<JSONObject> temp = new ArrayList<JSONObject>();
		temp.add((JSONObject) parser.parse("{\"Alias\":\"codiceProvincia\",\"Source Field\":\"CODICE PROVINCIA ISTAT\",\"Type\":\"String\"}") );
		temp.add((JSONObject) parser.parse("{\"Alias\":\"sigla\",\"Source Field\":\"SIGLA PROVINCIA\",\"Type\":\"String\"}") );
		temp.add((JSONObject) parser.parse("{\"Alias\":\"nomeProvincia\",\"Source Field\":\"DESCRIZIONE PROVINCIA\",\"Type\":\"String\"}") );
		return temp;
	}

}
