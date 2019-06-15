package modelloDataSet;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import Utility.MetaDataStore;
import modelloDataSet.MetaData.metadati;
/**
 * Classe che contiene informazioni sulla provincia dove si trova una Farmacia (riga del dataset)
 * @author Marco
 *
 */
public class Provincia implements MetaData{
	
	private int codiceProvincia;
	private String sigla;
	private String nomeProvincia;
	
	@metadati(alias="codiceProvincia", sourcefield="CODICE PROVINCIA ISTAT", type="String")
	public int getCodiceProvincia() {
		return codiceProvincia;
	}
	
	@metadati(alias="sigla", sourcefield="SIGLA PROVINCIA", type="String")
	public String getSigla() {
		return sigla;
	}
	
	@metadati(alias="nomeProvincia", sourcefield="DESCRIZIONE PROVINCIA", type="String")
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
	
	public MetaDataStore getMetaDati() throws ParseException, NoSuchMethodException, SecurityException {
		String[] campi= {"CodiceProvincia", "NomeProvincia", "Sigla"};
		Class<?> f = this.getClass();
		ArrayList<JSONObject> temp=MetaData.creaMetaDati(f,campi);
		return new MetaDataStore(temp);
	}

}
