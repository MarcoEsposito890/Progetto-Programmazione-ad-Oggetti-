package Utility;

import java.util.ArrayList;

import org.json.simple.JSONObject;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/**
 * La classe viene utilizzata per immagazzinare i metadati, altrimenti questi vengono visualizzati da Spring ogni volta che 
 * viene ritornato da un RestController, come risposta a una richiesta, un oggetto che implementa MetaData. Si usano quindi le annotazioni
 * JsonIgnoreProperties e JsonIgnore per evitare che Spring serializzi i metadati ogni qualvolta che viene
 * ritornato un oggetto o una lista di oggetti che implementano l'interfaccia {@link modelloDataSet.MetaData}
 * @author Marco Esposito
 *
 */
@JsonIgnoreProperties("dati")
public class MetaDataStore {
	
	private ArrayList<JSONObject> dati;
	public MetaDataStore(ArrayList<JSONObject> dati) {
		this.dati=dati;
	}
	@JsonIgnore
	public ArrayList<JSONObject> getData() {
		return dati;
	}
	
			
}
