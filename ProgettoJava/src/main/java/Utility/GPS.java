package Utility;

import java.util.ArrayList;
import java.util.HashMap;

import modelloDataSet.Comune;
import modelloDataSet.Farmacia;
/**
 * Classe utilizzata per gestire le informazioni relative a latitudine e longitudine. Estende la classe scannerDati, di cui utilizza in particolare i metodi
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
			Farmacia f1= cerca(nome1);
			Farmacia f2= cerca(nome2);
			Comune c1 = f1.getComune();
			Comune c2=  f2.getComune();
			lat1=c1.getLatitudine();
			lat2=c2.getLatitudine();
			lon1=c1.getLongitudine();
			lon2=c2.getLongitudine();
			if (lat1 > 0 && lon1 > 0 && lat2 > 0 && lon2 > 0) {
				return calcolaDistanza(lat1, lat2, lon1, lon2);
			}
			return 0;
		}
	

		/**Ritorna la farmacia più vicina a quella indicata
		 * 
		 * @param String nome - nome della farmacia
		 * @return Farmacia - riferimento alla farmacia più vicina
		 */
		public Farmacia vicina(String nome) {
			ArrayList<Double> latitudini = new ArrayList<Double>();
			ArrayList<Double> longitudini = new ArrayList<Double>();
			double lat = trovaCoordinate(nome).get("Latitudine"); //prelevo le coordinate della farmacia corrispondente al nome in ingresso
			double lon = trovaCoordinate(nome).get("Longitudine");
			for(int i=0; i<f.size(); i++) {
				if(!f.get(i).getDescrizione().equals(nome)) { //evito di copiare le sue coordinate
				latitudini.add(f.get(i).getComune().getLatitudine()); //creo degli array con latitudini e longitudini degli altri comuni
				longitudini.add(f.get(i).getComune().getLongitudine());
			}
			}
			HashMap<String, Double> coordinate = trovaVicina(latitudini, longitudini, lat, lon);
			return cercaCoordinate(coordinate.get("Latitudine"), coordinate.get("Longitudine"));
		}
		
	
	/**
	 * Calcola la distanza fra due punti, date le rispettive latitudini e longitudini. In particolare, utilizza due metodi dell'oggetto {@link Utility.Calcolatrice} che contiene,
	 * ossia {@link Utility.Calcolatrice#confronto(double, double)}, utilizzato per ordinare gli ingressi, e {@link Utility.Calcolatrice#sphericalLawofCosines(double, double, double, double)}, che ritorna appunto la distanza una volta che ha ricevuto gli ingressi ordinati.
	 * @param double lat1
	 * @param double lat2
	 * @param double lon1
	 * @param double lon2
	 * @return double - distanza
	 */
	public double calcolaDistanza(double lat1, double lat2, double lon1, double lon2) {
		double maxlat = calc.confronto(lat1, lat2).get(0);
		double maxlon = calc.confronto(lon1, lon2).get(0);
		double minlat = calc.confronto(lat1, lat2).get(1);
		double minlon = calc.confronto(lon1, lon2).get(1);
		//dist = arccos( sin(minlat) * sin(maxlat) + cos(minlat) * cos(maxlat) * cos(maxlon – minlon) ) * 6371 ("Spherical Law of Cosines")
		double dist = calc.sphericalLawofCosines(maxlat, minlat, maxlon, minlon);
		//distanza in chilometri		
		return dist;
	}
	
	/**Scorre gli ArrayList contenenti le latitudini e le longitudini, e ritorna le coordinate della località più vicina a quella corrispondente alle
	 * coordinate in ingresso.
	 * @param ArrayList<Double> latitudini 
	 * @param ArrayList<Double> longitudini
	 * @param double - latitudine della località per cui voglio trovare la farmacia più vicina
	 * @param double - longitutine della località per cui voglio trovare la farmacia più vicina
	 * @return HashMap<String, Double> - coordinate della farmacia più vicina
	 */
	public HashMap<String, Double> trovaVicina(ArrayList<Double> latitudini, ArrayList<Double> longitudini, double lat, double lon) {
		HashMap<String, Double> coordinate = new HashMap<String, Double> ();
		double lat2=0;
		double lon2=0;
		double minDistanza=calcolaDistanza(latitudini.get(0), lat, longitudini.get(0), lon); //inizializzo la distanza minima con la distanza fra farmacia in ingresso e prima farmacia dell'ArrayList
		if(latitudini.size()!=longitudini.size()) return null;
		for(int i=1; i<latitudini.size();i++) {
			double tmp =calcolaDistanza(latitudini.get(i), lat, longitudini.get(i), lon);
			if(tmp<minDistanza) {
				lat2=latitudini.get(i);
				lon2=longitudini.get(i);
				minDistanza=tmp;
			}
		}
		coordinate.put("Latitudine", lat2);
		coordinate.put("Longitudine", lon2); 
		return coordinate;
	}
	
}
