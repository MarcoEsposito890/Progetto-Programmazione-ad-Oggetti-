package Utility;

import java.util.ArrayList;
import java.util.HashMap;

import modelloDataSet.Farmacia;

public class GPS extends scannerDati{
	
	Calcolatrice calc = new Calcolatrice();
	
	public GPS(ArrayList<Farmacia> f) {
		super(f);
	}
	
	/// Funzioni di GPS

		// trova la distanza fra due farmacie, dato il nome
		public double distanza(String nome1, String nome2) {
			double lat1 = 0;
			double lon1 = 0;
			double lat2 = 0;
			double lon2 = 0;

			for (int i = 0; i < f.size(); i++) {
				if (f.get(i).getDescrizione().equals(nome1)) {
					lat1 = f.get(i).getComune().getLat();
					lon1 = f.get(i).getComune().getLong();
				}
				if (f.get(i).getDescrizione().equals(nome2)) {
					lat2 = f.get(i).getComune().getLat();
					lon2 = f.get(i).getComune().getLong();
				}
			}
			if (lat1 > 0 && lon1 > 0 && lat2 > 0 && lon2 > 0) {
				return calcolaDistanza(lat1, lat2, lon1, lon2);
			}
			return 0;
		}
	
		
		//funzioni che trovano il tempo necessario a raggiungere una Farmacia a partire da un'altra o dalle proprie coordinate
		
		/*public double tempoNecessario(String nome1, String nome2, double velocita) {
			double dist = distanza(nome1, nome2);
			return calcolaTempo(velocita, dist);
		}
		
		public double tempoNecessario(String destinazione, double lon, double lat, double velocita) {
			double lat1 = trovaCoordinate(destinazione).get("Latitudine");
			double lon1 = trovaCoordinate(destinazione).get("Longitudine");
			double dist = calcolaDistanza(lat1, lon, lon1, lat);
			return calcolaTempo(velocita, dist);
		}*/
		

		//ritorna la farmacia più vicina a quella indicata
		public Farmacia vicina(String nome) {
			ArrayList<Double> latitudini = new ArrayList<Double>();
			ArrayList<Double> longitudini = new ArrayList<Double>();
			double lat = trovaCoordinate(nome).get("Latitudine");
			double lon = trovaCoordinate(nome).get("Longitudine");
			for(int i=0; i<f.size(); i++) {
				latitudini.add(f.get(i).getComune().getLat());
				longitudini.add(f.get(i).getComune().getLat());
			}
			HashMap<String, Double> coordinate = trovaVicina(latitudini, longitudini, lat, lon);
			return cercaCoordinate(coordinate.get("Latitudine"), coordinate.get("Longitudine"));
		}
		
	
	//Funzioni di utilità di GPS
		
	//dist = arccos( sin(minlat) * sin(maxlat) + cos(minlat) * cos(maxlat) * cos(maxlon – minlon) ) * 6371 ("Spherical Law of Cosines")
	public double calcolaDistanza(double lat1, double lat2, double lon1, double lon2) {
		double maxlat = calc.confronto(lat1, lat2).get(0);
		double maxlon = calc.confronto(lon1, lon2).get(0);
		double minlat = calc.confronto(lat1, lat2).get(1);
		double minlon = calc.confronto(lon1, lon2).get(1);
		//"Spherical Law of Cosines"
		double dist = calc.sphericalLawofCosines(maxlat, minlat, maxlon, minlon);
		//distanza in chilometri		
		return dist;
	}
	
	//la velocità è in km/h. Ritorda il tempo in minuti che serve per andare da una Farmacia all'altra
	public double calcolaTempo (double velocita, double distanza) {
		//converto in metri/sec
		velocita= velocita/(3.6);
		//converto in metri
		distanza= distanza/1000;
		//ottengo il tempo in secondi
		double tempo = distanza/velocita;
		//ritorno il tempo in minuti
		return tempo/60;
	}
	
	//scorre gli arraylist forniti e confronta le distanze rispetto alle latitudini fornite per trovare quella minima
	public HashMap<String, Double> trovaVicina(ArrayList<Double> latitudini, ArrayList<Double> longitudini, double lat, double lon) {
		HashMap<String, Double> coordinate = new HashMap<String, Double> ();
		double lat2=0;
		double lon2=0;
		double minDistanza=calcolaDistanza(latitudini.get(0), lat, longitudini.get(0), lon);
		if(latitudini.size()!=longitudini.size()) return null;
		for(int i=1; i<latitudini.size();i++) {
			if(calcolaDistanza(latitudini.get(i), lat, longitudini.get(i), lon)<minDistanza) {
				lat2=latitudini.get(i);
				lon2=longitudini.get(i);
			}
		}
		coordinate.put("Latitudine", lat2);
		coordinate.put("Longitudine", lon2); 
		return coordinate;
	}
	
}
