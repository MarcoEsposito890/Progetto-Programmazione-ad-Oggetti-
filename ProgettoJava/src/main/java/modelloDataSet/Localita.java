package modelloDataSet;

import java.util.ArrayList;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import Utility.MetaDataStore;
/**
 * Classe che contiene coordinate ed indirizzo di una Farmacia (riga del dataset)
 * @author Marco
 *
 */
public class Localita implements MetaData{
	
	private double latitudine;
	private double longitudine;
	private String indirizzo;

	//private Comune comune;
	
	/*public Comune getComune() {
		return comune;
	}*/
	
	public Localita() {
		
	}
	
	public Localita(double latitudine, double longitudine, String indirizzo) {
		this.latitudine=latitudine;
		this.longitudine=longitudine;
		this.indirizzo=indirizzo;
	}
	
	@metadati(alias="latitudine", sourcefield="LATITUDINE", type="double")
	public double getLatitudine() {
		return latitudine;
	}
	
	@metadati(alias="longitutine", sourcefield="LONGITUDINE", type="double")
	public double getLongitudine() {
		return longitudine;
	}
	
	@metadati(alias="indirizzo", sourcefield="INDIRIZZO", type="String")
	public String getIndirizzo() {
		return indirizzo;
	}
	
	public void setLat(double latitudine) {
		this.latitudine=latitudine;
	}

	public void setLong(double longitudine) {
		this.longitudine=longitudine;
	}
	
	
	public MetaDataStore getMetaDati() throws ParseException, NoSuchMethodException, SecurityException {
		String[] campi= {"Latitudine", "Longitudine", "Indirizzo"};
		Class<?> f = this.getClass();
		ArrayList<JSONObject> temp=MetaData.creaMetaDati(f,campi);
		return new MetaDataStore(temp);
	}
}
