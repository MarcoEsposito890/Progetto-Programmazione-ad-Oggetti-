package modelloDataSet;

import java.util.ArrayList;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import Utility.MetaDataStore;
/**
 * Classe che contiene informazioni sulla provincia dove si trovano una Farmacia/Comune. Implementa l'interfaccia MetaDati.
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

	public void setCodice(int codiceProvincia) {
		this.codiceProvincia=codiceProvincia;
	}

	public void setDescrizione(String nomeProvincia) {
		this.nomeProvincia=nomeProvincia;
	}

	/**
	 * Implementa il metodo getMetaDati() dell'interfaccia MetaData. Se ritornasse semplicemente un ArrayList di JSONObject, Spring visualizzerebbe
	 * i metadati ogni volta che viene ritornato un oggetto di tipo Provincia. Per questo motivo si inseriscono i metadati in un oggetto {@link Utility.MetaDataStore}, da cui poi vi si può accedere facilmente con il metodo {@link Utility.MetaDataStore#getData()}.
	 * @return  MetaDataStore - oggetto contenente i metadati
	 */
	public MetaDataStore getMetaDati() throws ParseException, NoSuchMethodException, SecurityException {
		String[] campi= {"CodiceProvincia", "NomeProvincia", "Sigla"};
		Class<?> f = this.getClass();
		ArrayList<JSONObject> temp=MetaData.creaMetaDati(f,campi);
		return new MetaDataStore(temp);
	}

}
