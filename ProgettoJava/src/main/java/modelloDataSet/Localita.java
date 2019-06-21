package modelloDataSet;

import java.util.ArrayList;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import Utility.MetaDataStore;
/**
 * Classe che rappresenta una Localita e contiene quindi informazioni su latitudine, longitudine e indirizzo. Implementa l'interfaccia MetaDati.
 * @author Marco
 *
 */
public class Localita implements MetaData{

	private double latitudine;
	private double longitudine;
	private String indirizzo;
	private String frazione;

	public Localita() {

	}

	public Localita(double latitudine, double longitudine, String indirizzo, String frazione) {
		this.latitudine=latitudine;
		this.longitudine=longitudine;
		this.indirizzo=indirizzo;
		this.frazione=frazione;
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

	@metadati(alias="frazione", sourcefield="FRAZIONE", type="String")
	public String getFrazione() {
		return frazione;
	}

	public void setLat(double latitudine) {
		this.latitudine=latitudine;
	}

	public void setLong(double longitudine) {
		this.longitudine=longitudine;
	}

	/**
	 * Implementa il metodo getMetaDati() dell'interfaccia MetaData. Se ritornasse semplicemente un ArrayList di JSONObject, Spring visualizzerebbe
	 * i metadati ogni volta che viene ritornato un oggetto di tipo Localita. Per questo motivo si inseriscono i metadati in un oggetto {@link Utility.MetaDataStore}, da cui poi vi si può accedere facilmente con il metodo {@link Utility.MetaDataStore#getData()}.
	 * @return  MetaDataStore - oggetto contenente i metadati
	 */
	public MetaDataStore getMetaDati() throws ParseException, NoSuchMethodException, SecurityException {
		String[] campi= {"Latitudine", "Longitudine", "Indirizzo", "Frazione"};
		Class<?> f = this.getClass();
		ArrayList<JSONObject> temp=MetaData.creaMetaDati(f,campi);
		return new MetaDataStore(temp);
	}
}
