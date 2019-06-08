package Utility;

import java.util.ArrayList;
import java.math.*;

public class Calcolatrice {
	
	public boolean formulaLunn(String cs) {
		double X=0; double Y=0; double Z=0; double T=0; 
		double C=Double.parseDouble(cs.substring(cs.length()-1, cs.length()));
		for(int i=0; i<cs.length()-1; i=i+2) {
			X+=Double.parseDouble(cs.substring(i, i+1)); //X=somma delle cifre di ordine dispari
		}
		
		for(int i=1; i<cs.length()-1; i=i+2) {
			Y+=2*Double.parseDouble(cs.substring(i, i+1)); //Y=somma delle cifre di ordine dispari
		}

		for(int i=1; i<cs.length()-1; i=i+2) { 
			if(Double.parseDouble(cs.substring(i, i+1))>=5) Z++; //Z=numero delle volte in cui nei numeri in posizione pari c'è un numero maggiore o uguale a 5
		}
		T=(X+Y+Z)%10;
		if(C==(10-T)%10) return true;
		else return false;
	}
	
	public double calcolaMedia(ArrayList<Double> d) {
		int sum=0;
		for(int i=0; i<d.size(); i++) {
			sum+=d.get(i);
		}
		return sum/d.size();
	}
	
	public double sphericalLawofCosines(double maxlat, double minlat, double maxlon, double minlon) {
		return arccos(seno(minlat)*seno(maxlat) +coseno(minlat)*coseno(maxlat)*coseno(maxlon-minlon))*6371;
	}
	
	//funzioni di utilità
	
	//confronta due numeri in ingresso e li ritorna in un ArrayList in ordine (prima il più grande, poi il più piccolo)
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
	

	
	
}
