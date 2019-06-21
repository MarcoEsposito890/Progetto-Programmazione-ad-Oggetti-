package Utility;

import java.util.ArrayList;
/**
 * Classe che contiene diversi metodi utili per gestire dati numerici. Oltre che funzioni di utilità come quelle trigonometriche, implementa alcuni metodi
 * necessari per altre classi del package Utility.
 * @author Marco
 *
 */
public class Calcolatrice {

	/**
	 * Realizza l'algoritmo di controllo per verificare il codice di controllo delle Partite IVA. In particolare, utilizza la Formula di Lunn
	 * per calcolare il codice di controllo (una cifra), poi lo confronta con quello della Partita IVA in ingresso (ultima cifra).
	 * @param String - Stringa che contiene una PartitaIVA 
	 * @return boolean - conferma o meno che il controllo sia andato a buon fine
	 */
	public boolean formulaLunn(String cs) {
		double X=0; double Y=0; double Z=0; double T=0; 
		double C=Double.parseDouble(cs.substring(cs.length()-1, cs.length()));
		for(int i=0; i<cs.length()-1; i=i+2) {
			X+=Double.parseDouble(cs.substring(i, i+1)); //X=somma delle cifre di indice dispari
		}

		for(int i=1; i<cs.length()-1; i=i+2) {
			Y+=2*Double.parseDouble(cs.substring(i, i+1)); //Y=somma delle cifre di indice pari
		}

		for(int i=1; i<cs.length()-1; i=i+2) { 
			if(Double.parseDouble(cs.substring(i, i+1))>=5) Z++; //Z=numero delle volte in cui nei numeri in posizione pari c'è un numero maggiore o uguale a 5
		}

		T=(X+Y+Z)%10;
		if(C==(10-T)%10) return true;
		else return false;
	}
	/**
	 * Calcola la statistica (indicata dall'operatore) sull'ArrayList in ingresso.
	 * @param String - indica quale statistica calcolare fra media, varianza, minimo e massimo.
	 * @return double - risultato della statistica
	 * 
	 */
	public double stats(String operator, ArrayList<Double> d) {
		if(operator.equalsIgnoreCase("media")) return calcolaMedia(d);
		else if(operator.equalsIgnoreCase("varianza")) return calcolaVarianza(d);
		else if((operator.equalsIgnoreCase("max"))) return max(d);
		else if((operator.equalsIgnoreCase("min"))) return min(d);
		return 0;
	}

	/**
	 * Calcola la media aritmetica.
	 * @param ArrayList<Double> - ArrayList contenente i dati su cui calcolare la media
	 * @return double - media dei dati 
	 */
	public double calcolaMedia(ArrayList<Double> d) {
		int sum=0;
		for(int i=0; i<d.size(); i++) {
			sum+=d.get(i);
		}
		return sum/d.size();
	}

	/**
	 * Calcola la varianza.
	 * @param ArrayList<Double> - ArrayList contenente i dati su cui calcolare la varianza
	 * @return double - varianza dei dati 
	 */
	public double calcolaVarianza(ArrayList<Double> d) {
		double m = calcolaMedia(d);
		double sommaScartiQuad = 0;
		for(int i=0; i<d.size(); i++)
			sommaScartiQuad += (d.get(i)-m)*(d.get(i)+m);
		return sommaScartiQuad/d.size();
	}

	/**
	 * Trova il massimo dell'ArrayList in ingresso
	 * @param ArrayList<Double> - ArrayList contenente i dati su cui trovare il massimo
	 * @return double - massimo dei dati 
	 */
	public double max(ArrayList<Double> d) {
		double m=d.get(0);
		for(int i=0; i<d.size(); i++) {
			if (d.get(i)>m) m=d.get(i);
		}
		return m;
	}

	/**
	 * Trova il minimo dell'ArrayList in ingresso
	 * @param ArrayList<Double> - ArrayList contenente i dati su cui trovare il minimo
	 * @return double - minimo dei dati 
	 */
	public double min(ArrayList<Double> d) {
		double m=d.get(0);
		for(int i=0; i<d.size(); i++) {
			if (d.get(i)<m) m=d.get(i);
		}
		return m;
	}

	/**
	 * Utilizza la "Spherical Law of Cosines" per calcolare la distanza fra due punti note longitudine e latitudine dei due punti.
	 * @param double - maxlat
	 * @param double - minlat
	 * @param double - maxlon
	 * @param double - minlon
	 * @return double -distanza fra i due punti descritti dalle coordinate (longitudine, latitudine) in ingresso
	 */
	public double sphericalLawofCosines(double maxlat, double minlat, double maxlon, double minlon) {
		return arccos(seno(rad(minlat))*seno(rad(maxlat)) +coseno(rad(minlat))*coseno(rad(maxlat))*coseno(rad(maxlon-minlon)))*6371;
	}

	//funzioni di utilità

	/**confronta due numeri in ingresso e li ritorna in un ArrayList in ordine (prima il più grande, poi il più piccolo)
	 * 
	 * @param double a
	 * @param double b
	 * @return ArrayList<Double> - array contenente a e b in ordine
	 */
	public ArrayList<Double> confronto(double a, double b) {
		ArrayList<Double> d = new ArrayList<Double>();
		if (a>b) {
			d.add(a);
			d.add(b);
			return d;
		}
		d.add(b);
		d.add(a);
		return d;
	}

	public double seno(double a) {
		return Math.sin(a);
	}

	public double coseno(double a) {
		return Math.cos(a);
	}

	public double arccos(double a) {
		return Math.acos(a);
	}

	public double arcsin(double a) {
		return Math.asin(a);
	}

	public double rad(double angle) {
		return angle*3.14/180;
	}



}
