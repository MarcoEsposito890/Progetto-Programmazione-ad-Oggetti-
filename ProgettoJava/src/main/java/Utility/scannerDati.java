package Utility;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import modelloDataSet.Farmacia;
import modelloDataSet.Provincia;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//viene utilizzata da Controller per cercare e filtrare i dati nel suo ArrayList di Farmacia
//viene estesa dalle classi GPS e Checker che fanno elaborazioni sull'ArrayList di Farmacia (rispettivamente su latitudine/longitudine e sulle partiteIVA).

public class scannerDati {

	public ArrayList<Farmacia> f;
	public Calcolatrice calc = new Calcolatrice();
	 // vedi meglio per le ereditarietà se puoi instanziare solo uno fra GPS e
							// calcolatrice o devi mettere entrambi o puoi accedere in qualche modo ai metodi di GPS dalla superclasse (non credo)

	public scannerDati(ArrayList<Farmacia> f) {
		this.f = f;
	}

	// Ritorna i metadati
	public ArrayList<JSONObject> getMeta() throws ParseException {
		return f.get(0).getMetaDati();
	}

	// Cerca la Farmacia dato il nome
	public Farmacia cerca(String nome) {
		for (int i = 0; i < f.size(); i++) {
			if (f.get(i).getDescrizione().contains(nome))
				return f.get(i);
		}
		return null;
	}

	// Cerca la Farmacia date le coordinate
	public Farmacia cercaCoordinate(double lat, double longi) {
		Farmacia p = new Farmacia();
		Parser prs = new Parser();
		for (int i = 0; i < f.size(); i++) {
			double tmp1 = f.get(i).getComune().getLat();
			double tmp2 = f.get(i).getComune().getLong();
			if (tmp1 == lat && tmp2 == longi) {
				return p;
			}
		}
		return p;
	}
	
	//Ritorne le coordinate, data la Farmacia
	public  HashMap<String, Double> trovaCoordinate(String nome){
		HashMap<String, Double> coordinate = new HashMap<String, Double> ();
		Farmacia f2 = cerca(nome);
		if(f2!=null) { //eccezione
		coordinate.put("Latitudine", f2.getComune().getLat());
		coordinate.put("Longitudine", f2.getComune().getLong()); 
		return coordinate;
	}
		else return null;
	}

	// Filtra le farmacie, ritornando solo quelle in un determinato comune
	public ArrayList<Farmacia> cercaPerComune(String Comune) {
		ArrayList<Farmacia> p = new ArrayList<Farmacia>();
		for (int i = 0; i < f.size(); i++) {
			String nome = f.get(i).getComune().getDescrizione();
			if (nome.equalsIgnoreCase(Comune)) {
				p.add(f.get(i));
			}
		}
		return p;
	}

	/// Filtra le farmacie, ritornando solo quelle in una determinata provincia
	public ArrayList<Farmacia> cercaPerProvincia(String Provincia) {
		ArrayList<Farmacia> p = new ArrayList<Farmacia>();
		Parser prs = new Parser();
		for (int i = 0; i < f.size(); i++) {
			String nome = f.get(i).getComune().getProvincia().getDescrizione();
			if (nome.equalsIgnoreCase(Provincia)) {
				p.add(f.get(i));
			}
		}
		return p;
	}

	// Filtra le Partite IVA, ritornando l'elenco delle Partite IVA con lo stesso
	// Codice Provincia. Tiene conto dei mismatch fra Partita IVA e relativa
	// Provincia usando il metodo checkMismatch di Checker
	public ArrayList<String> partitaIva(String provincia) {
		ArrayList<String> p = new ArrayList<String>();
		String codice;
		String iva;
		for (int i = 0; i < f.size(); i++) {
			Farmacia temp = f.get(i);
			Provincia temp2 = temp.getComune().getProvincia();
			if (temp2.getDescrizione().equalsIgnoreCase(provincia)) {
				codice = temp2.getCodice();
				iva = temp.getPartitaIVA();
				if (iva.contentEquals("0"))
					p.add("Farmacia " + temp.getDescrizione() + " senza Partita Iva disponibile");
				else if (((Checker) this).checkMismatch(iva, temp2))
					p.add(iva);
				else
					p.add(new String("Farmacia " + temp.getDescrizione() + " Presenta un mismatch fra Codice Provincia "
							+ codice + " e Partita Iva " + iva));
			}
		}
		return p;
	}
}

	
