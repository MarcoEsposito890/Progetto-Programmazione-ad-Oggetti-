package modelloDataSet;

import java.util.ArrayList;
/** Classe che modella un Comune del dataset. Al suo interno contiene un riferimento a un oggetto Provincia. 
 * Inoltre estende la classe Localita, che contiene coordinate ed indirizzo della Farmacia che contiene il riferimento al Comune.
 * @author Marco Esposito
 */

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import Utility.MetaDataStore;

public class Comune extends Localita implements MetaData{
	
	private Provincia provincia;
	private int codiceComune;
	private String nomeComune;
	
	public Comune() {
		super();
		provincia=new Provincia();
	}
	
	public Comune(double lat, double longi, String indirizzo) {
		super(lat, longi, indirizzo);
		provincia=new Provincia();
	}
	
	public Provincia getProvincia() {
		return provincia;
	}
	@metadati(alias="codiceComune", sourcefield="CODICE COMUNE ISTAT", type="int")
	public int getCodiceComune() {
		return codiceComune;
	}
	
	@metadati(alias="NomeComune", sourcefield="DESCRIZIONE COMUNE", type="String")
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
	
	 public MetaDataStore getMetaDati() throws ParseException, NoSuchMethodException, SecurityException {
		String[] campi= {"CodiceComune", "NomeComune"};
		Class<?> f = this.getClass();
		ArrayList<JSONObject> temp=MetaData.creaMetaDati(f,campi);
		temp.addAll(provincia.getMetaDati().getData());
		temp.addAll(super.getMetaDati().getData());
		return new MetaDataStore(temp);
	}

}
