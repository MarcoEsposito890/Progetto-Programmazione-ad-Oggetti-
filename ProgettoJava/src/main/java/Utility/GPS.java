package Utility;

import java.util.ArrayList;
import java.util.HashMap;

import modelloDataSet.Farmacia;
/**
 * Classe utilizzata per gestire le informazioni relative a latitudine e longitudine. Estende la classe scannerDati, con cui condivide in particolare i metodi
 * di scorrimento dell'array di Farmacie e di ricerca.
 * @author Marco
 *
 */
public class GPS extends scannerDati{
	
	Calcolatrice calc = new Calcolatrice();
	ArrayList<Farmacia> f;
	
	public GPS(ArrayList<Farmacia> f) {
		super(f);
		this.f=f;
	}
	
	/// Funzioni di GPS

		/**Trova la distanza fra due farmacie, dati i nomi di entrambe
		 * 
		 * @param String - nome1
		 * @param String - nome2
		 * @return double - distanza fra le farmacie
		 */
		public double distanza(String nome1, String nome2) {
			double lat1 = 0;
			double lon1 = 0;
			double lat2 = 0;
			double lon2 = 0;

			for (int i = 0; i < f.size(); i++) {
				if (f.get(i).getDescrizione().equals(nome1)) { //fai direttamente una verifica con cercaComune?
					lat1 = f.get(i).getComune().getLatitudine();
					lon1 = f.get(i).getComune().getLongitudine();
				}
				if (f.get(i).getDescrizione().equals(nome2)) {
					lat2 = f.get(i).getComune().getLatitudine();
					lon2 = f.get(i).getComune().getLongitudine();
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
		

		/**Ritorna la farmacia più vicina a quella indicata
		 * 
		 * @param String - nome della farmacia
		 * @return Farmacia - riferimento alla farmacia più vicina
		 */
		public Farmacia vicina(String nome) {
			ArrayList<Double> latitudini = new ArrayList<Double>();
			ArrayList<Double> longitudini = new ArrayList<Double>();
			double lat = trovaCoordinate(nome).get("Latitudine");
			double lon = trovaCoordinate(nome).get("Longitudine");
			for(int i=0; i<f.size(); i++) {
				latitudini.add(f.get(i).getComune().getLatitudine());
				longitudini.add(f.get(i).getComune().getLongitudine());
			}
			HashMap<String, Double> coordinate = trovaVicina(latitudini, longitudini, lat, lon);
			return cercaCoordinate(coordinate.get("Latitudine"), coordinate.get("Longitudine"));
		}
		
	
	/**
	 * Calcola la distanza fra due punti, date le rispettive latitudini e longitudini. In particolare, utilizza due metodi dell'oggetto {@link Utility.Calcolatrice} che contiene,
	 * ossia confronto, utilizzato per ordinare gli ingressi, e sphericalLawofCosines, che ritorna appunto la distanza una volta che ha ricevuto gli ingressi ordinati.
	 * @param double - lat1
	 * @param double - lat2
	 * @param double - lon1
	 * @param double - lon2
	 * @return distanza
	 */
		
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
	
	/*//la velocità è in km/h. Ritorda il tempo in minuti che serve per andare da una Farmacia all'altra
	public double calcolaTempo (double velocita, double distanza) {
		//converto in metri/sec
		velocita= velocita/(3.6);
		//converto in metri
		distanza= distanza/1000;
		//ottengo il tempo in secondi
		double tempo = distanza/velocita;
		//ritorno il tempo in minuti
		return tempo/60;
	}*/
	
	/**Scorre gli ArrayList contenenti le latitudini e le longitudini, e ritorna le coordinate della località più vicina a quella corrispondente alle
	 * coordinate in ingresso.
	 * @param ArrayList<Double> - latitudini
	 * @param ArrayList<Double> - longitudini
	 * @param double - latitudine della località per cui voglio trovare la più vicina
	 * @param double - longitutine della località per cui voglio trovare la più vicina
	 * @return
	 */
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
