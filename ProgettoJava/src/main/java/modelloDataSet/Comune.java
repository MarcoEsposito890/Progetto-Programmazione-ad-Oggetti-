package modelloDataSet;

import java.util.ArrayList;
/** Classe che modella un Comune del dataset. Al suo interno contiene un riferimento a un oggetto Provincia. 
 * Inoltre estende la classe Localita, che contiene coordinate ed indirizzo della Farmacia che contiene il riferimento al Comune.
 * @author Marco Esposito
 */

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import Utility.MetaDataStore;
/**
 * Classe che modella un Comune. Ogni Comune contiene un riferimento a un oggetto Provincia, e inoltre estende Localita e implementa l'interfaccia MetaDati.
 * @author Marco
 * 
 */
public class Comune extends Localita implements MetaData{

	private Provincia provincia;
	private int codiceComune;
	private String nomeComune;

	public Comune() {
		super();
		provincia=new Provincia();
	}

	public Comune(double lat, double longi, String indirizzo, String frazione) {
		super(lat, longi, indirizzo, frazione);
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

	/**
	 * Implementa il metodo getMetaDati() dell'interfaccia MetaData. Se ritornasse semplicemente un ArrayList di JSONObject, Spring visualizzerebbe
	 * i metadati ogni volta che viene ritornato un oggetto di tipo Comune. Per questo motivo si inseriscono i metadati in un oggetto {@link Utility.MetaDataStore}, da cui poi vi si può accedere facilmente con il metodo {@link Utility.MetaDataStore#getData()}.
	 * @return  MetaDataStore - oggetto contenente i metadati
	 */
	public MetaDataStore getMetaDati() throws ParseException, NoSuchMethodException, SecurityException {
		String[] campi= {"CodiceComune", "NomeComune"};
		Class<?> f = this.getClass();
		ArrayList<JSONObject> temp=MetaData.creaMetaDati(f,campi);
		temp.addAll(provincia.getMetaDati().getData()); //accedo ai metadati di provincia
		temp.addAll(super.getMetaDati().getData()); //accedo ai metadati della superclasse
		return new MetaDataStore(temp);
	}

}
