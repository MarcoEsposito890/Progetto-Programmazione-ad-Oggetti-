package modelloDataSet;

import java.util.ArrayList;

import javax.persistence.Entity;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.context.annotation.Bean;

/**
 * Classe che modella una Farmacia (una riga del dataset). Al suo interno, oltre ai vari oggetti utilizzati per 
 * rappresentare i campi del dataset, abbiamo un oggetto di tipo Comune che raccoglie le informazioni geografiche sulla Farmacia.
 * @author Marco
 *
 */
public class Farmacia {
	
	private long id; //prova a creare un bean e repository per Farmacia
	private Comune c;
	private int codiceID;
	private String descrizione;
	private String tipologia;
	private int codiceTipologia;
	private int partitaIVA;
	
	public Farmacia() {
		
	}
	
	public Comune getComune() {
		return c;
	}
	
	public int getId() {
		return codiceID;
	}
	
	public String getDescrizione() {
		return descrizione;
	}
	
	public String getTipologia() {
		return tipologia;
	}
	
	public int getCodiceTipologia() {
		return codiceTipologia;
	}
	
	public int getPartitaIVA() {
		return partitaIVA;
	}
	
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
	
	public void setTipologia(String Tipologia) {
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
	
	public ArrayList<JSONObject> getMetaDati() throws ParseException {
		JSONParser parser = new JSONParser();
		ArrayList<JSONObject> temp = new ArrayList<JSONObject>();
		temp.add((JSONObject) parser.parse("{\"Alias\":\"descrizione\",\"Source Field\":\"DESCRIZIONE FARMACIA\",\"Type\":\"String\"}"));
		temp.add((JSONObject) parser.parse("{\"Alias\":\"codiceID\",\"Source Field\":\"CODICE IDENTIFICATIVO FARMACIA\",\"Type\":\"long\"}"));
		temp.add((JSONObject) parser.parse("{\"Alias\":\"tipologia\",\"Source Field\":\"DESCRIZIONE TIPOLOGIA\",\"Type\":\"String\"}"));
		temp.add((JSONObject) parser.parse("{\"Alias\":\"codiceTipologia\",\"Source Field\":\"CODICE TIPOLOGIA\",\"Type\":\"int\"}"));
		temp.addAll(c.getMetaDati());
		return temp;
	}

}
