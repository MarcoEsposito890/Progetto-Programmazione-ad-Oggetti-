package modelloDataSet;

import java.lang.annotation.Retention;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import javax.persistence.Entity;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.tools.javac.code.Attribute.RetentionPolicy;

import Utility.MetaDataStore;
import modelloDataSet.MetaData.metadati;

/**
 * Classe che modella una Farmacia (una riga del dataset). Al suo interno, oltre ai vari oggetti utilizzati per 
 * rappresentare i campi del dataset, abbiamo un oggetto di tipo Comune che raccoglie le informazioni geografiche sulla Farmacia.
 * @author Marco
 *
 */
public class Farmacia implements MetaData{
	
	private Comune c;
	private int codiceID;
	private String descrizione;
	private String tipologia;
	private int codiceTipologia;
	private int partitaIVA;
	public MetaDataStore dati;
	
	public Farmacia() {
		c=new Comune();
	}
	
	public Comune getComune() {
		return c;
	}
	
	@metadati(alias="codiceID", sourcefield="CODICE IDENTIFICATIVO FARMACIA", type="int")
	public int getCodiceID() {
		return codiceID;
	}
	
	@metadati(alias="descrizione", sourcefield="DESCRIZIONE FARMACIA", type="String")
	public String getDescrizione() {
		return descrizione;
	}
	
	@metadati(alias="tipologia", sourcefield="DESCRIZIONE TIPOLOGIA", type="String")
	public String getTipologia() {
		return tipologia;
	}
	
	@metadati(alias="codiceTipologia", sourcefield="CODICE TIPOLOGIA", type="String")
	public int getCodiceTipologia() {
		return codiceTipologia;
	}
	
	@metadati(alias="tipologia", sourcefield="PARTITA IVA", type="int")
	public int getPartitaIVA() {
		return partitaIVA;
	}
	
	@metadati(alias="descrizione", sourcefield="CODICE IDENTIFICATIVO FARMACIA", type="String")
	public double getLatitudine() {
		return c.getLatitudine();
	}
	
	public double getLongitudine() {
		return c.getLongitudine();
	}
	
	public void setID(int codiceID) {
		this.codiceID=codiceID;
	}
	
	public void setDescrizione(String descrizione) {
		this.descrizione=descrizione;
	}
	
	public void setTipologia(String tipologia) {
		this.tipologia=tipologia;
	}
	
	public void setCodiceTipologia(int codiceTipologia) {
		this.codiceTipologia=codiceTipologia;
	}
	
	public void setIVA(int partitaIVA) {
		this.partitaIVA=partitaIVA;
	}
	
	public void setComune(Comune c) {
		this.c=c;
	}
	
/**
 * Implementa il metodo getMetaDati() dell'interfaccia MetaData. Se ritornasse semplicemente un ArrayList di JSONObject, Spring visualizzerebbe
 * i metadati ogni volta che viene ritornato un oggetto di tipo Farmacia. Per questo motivo si inseriscono i metadati in un oggetto MetaDataStore.
 * @return 
 */
	
		public MetaDataStore getMetaDati() throws NoSuchMethodException, SecurityException, ParseException {
		JSONParser parser = new JSONParser();
		ArrayList<JSONObject> temp = new ArrayList<JSONObject>();
		Class<?> f = this.getClass();
		String[] campi= {"Descrizione", "Tipologia", "CodiceTipologia", "PartitaIVA", "CodiceID"};
		temp=MetaData.creaMetaDati(f,campi);
		temp.addAll(c.getMetaDati().getData());
		return new MetaDataStore(temp);
	}

	

	

}
