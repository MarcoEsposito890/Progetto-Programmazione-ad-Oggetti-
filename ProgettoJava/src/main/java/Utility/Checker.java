package Utility;

import java.util.ArrayList;

import modelloDataSet.Farmacia;
import modelloDataSet.Provincia;
/**
 * Classe utilizzata per fare controlli sui valori del dataset. Estende scannerDati, dal quale eredita in particolare i metodi per lo scorrimento dell'array di Farmacie
 * e di ricerca. 
 * @author Marco
 *
 */
public class Checker extends scannerDati{
	
	Calcolatrice calc = new Calcolatrice();
	ArrayList<Farmacia> f;
	public Checker(ArrayList<Farmacia> f) {
		super(f);
	}

	/**Scorre l'array di farmacie e verifica che il Codice di Controllo della
	 * Partita Iva sia esatto, altrimenti ritorna un Errore e la Farmacia Relativa.
	 * L'algoritmo di controllo utilizza la Formula di Lunn, implementata nella
	 * classe Calcolatrice.
	 * @param String - Provincia sulla quale fare il controllo
	 * @return ArrayList<String> - Contiene i messaggi di errore in caso il controllo non vada a buon fine e il conto delle Partite IVA errate
	 * , oppure segnala che tutti i controlli sono andati a buon fine.
	 */
	public ArrayList<String> checkPartitaIva(String provincia) {
		ArrayList<String> p = new ArrayList<String>();
		String errore = "";
		int c = 0;
		for (int i = 0; i < f.size(); i++) {
			if (f.get(i).getComune().getProvincia().getNomeProvincia().equals(provincia)) {
				String iva = Integer.toString((f.get(i).getPartitaIVA()));
				if (!calc.formulaLunn(iva)) { // richiamo la funzione formulaLunn di Calcolatrice, che usa la Formula di  Lunn e ritorna vero se la verifica è andata a buon fine
					// se la verifica non è andata a buon fine, segnalo l'errore	
						errore = "Errore: " + f.get(i).getDescrizione()+ " Ha un errore nel codice di controllo della sua partita iva!"; 
						p.add(errore);
						System.out.println(p);
						c++;
					}
				}
			}
			if (c == 0)
				p.add("Controllo andato a buon fine!"); // segnala che non ci sono stati errori
			else {
				p.add("In totale " + c + " su " + f.size() + " Hanno un Errore nel codice di controllo");
			}
			return p;
		}
	
	/**controlla se c'è un mismatch fra la partita IVA e il codice Provincia (terzultima e penultima cifra della partita IVA).
	 * 
	 * @param String - Partita IVA
	 * @param Provincia - riferimento all'oggetto Provincia su cui fare il controllo
	 * @return boolean - conferma o meno che il controllo sia andato a buon fine
	 */
	public boolean checkMismatch(String iva, Provincia p) {
		String codice = Integer.toString(p.getCodiceProvincia());
		if(iva.substring(iva.length() - 3, iva.length() - 1).equals(codice)) return true;
		return false;
	}
	
	
	/*public ArrayList<Farmacia> checkNoAttributo(String attributo, ArrayList<Farmacia> f){
		ArrayList <Farmacia> temp = new ArrayList<Farmacia>();
		for(int i=0; i<)
	}*/
	
}







