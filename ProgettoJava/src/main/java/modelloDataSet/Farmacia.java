package modelloDataSet;

import java.util.ArrayList;

import javax.persistence.Entity;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.context.annotation.Bean;


public class Farmacia {
	
	private Comune c;
	private long ID;
	private String descrizione;
	private String tipologia;
	private int codiceTipologia;
	private String partitaIVA;
	
	public Farmacia() {
		
	}
	
	public Comune getComune() {
		return c;
	}
	
	public long getID() {
		return ID;
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
	
	public String getPartitaIVA() {
		return partitaIVA;
	}
	
	public void setID(long ID) {
		this.ID=ID;
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
	
	public void setIVA(String partitaIVA) {
		this.partitaIVA=partitaIVA;
	}
	
	public void setComune(Comune c) {
		this.c=c;
	}
	
	public ArrayList<JSONObject> getMetaDati() throws ParseException {
		JSONParser parser = new JSONParser();
		ArrayList<JSONObject> temp = new ArrayList<JSONObject>();
		temp.add((JSONObject) parser.parse("{\"Alias\":\"descrizione\",\"Source Field\":\"DESCRIZIONE FARMACIA\",\"Type\":\"String\"}"));
		temp.add((JSONObject) parser.parse("{\"Alias\":\"ID\",\"Source Field\":\"CODICE IDENTIFICATIVO FARMACIA\",\"Type\":\"long\"}"));
		temp.add((JSONObject) parser.parse("{\"Alias\":\"tipologia\",\"Source Field\":\"DESCRIZIONE TIPOLOGIA\",\"Type\":\"String\"}"));
		temp.add((JSONObject) parser.parse("{\"Alias\":\"codiceTipologia\",\"Source Field\":\"CODICE TIPOLOGIA\",\"Type\":\"int\"}"));
		temp.addAll(c.getMetaDati());
		return temp;
	}

}
