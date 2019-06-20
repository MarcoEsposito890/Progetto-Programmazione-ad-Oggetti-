package Utility;

import java.util.ArrayList;

import modelloDataSet.Farmacia;
import modelloDataSet.Provincia;
/**
 * Classe utilizzata per fare controlli sui valori del dataset. Estende scannerDati, dal quale utilizza in particolare i metodi per lo scorrimento dell'array di Farmacie e il filtraggio. 
 * @author Marco Esposito
 *
 */
public class Checker extends scannerDati{
	
	Calcolatrice calc = new Calcolatrice();
	ArrayList<Farmacia> f;
	public Checker(ArrayList<Farmacia> f) {
		super(f);
		this.f=f;
	}

	/**Scorre l'array di farmacie e verifica che il Codice di Controllo della
	 * Partita Iva sia esatto, altrimenti ritorna un Errore e la Farmacia Relativa.
	 * L'algoritmo di controllo utilizza la Formula di Lunn, tramite il metodo {@link Utility.Calcolatrice#formulaLunn(String)} della
	 * classe Calcolatrice.
	 * @param String provincia - Provincia sulla quale fare il controllo
	 * @return ArrayList<String> - Contiene i messaggi di errore in caso il controllo non vada a buon fine e il conteggio delle Partite IVA errate
	 * , oppure segnala che tutti i controlli sono andati a buon fine.
	 */
	public ArrayList<String> checkPartitaIva(String provincia) {
		ArrayList<String> p = new ArrayList<String>();
		String errore = "";
		int c = 0;
		int c2= 0;
		for (int i = 0; i < f.size(); i++) {
			if (f.get(i).getComune().getProvincia().getNomeProvincia().equals(provincia)) {
				c2++;
				String iva = Integer.toString((f.get(i).getPartitaIVA()));
				if (!calc.formulaLunn(iva)) { // richiamo la funzione formulaLunn di Calcolatrice, che usa la Formula di  Lunn e ritorna vero se la verifica è andata a buon fine
					// altrimenti, se la verifica non è andata a buon fine, segnalo l'errore	
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
				p.add("In totale " + c + " su " + c2 + " Hanno un Errore nel codice di controllo");
			}
			return p;
		}
	
	/**Controlla se c'è un mismatch fra la partita IVA e il codice Provincia (terzultima e penultima cifra della partita IVA).
	 * Le Farmacie di cui non è disponibile la partita IVA non vengono considerate.
	 * @param String iva - Partita IVA
	 * @param Provincia p - riferimento all'oggetto Provincia su cui fare il controllo
	 * @return boolean - conferma o meno che il controllo sia andato a buon fine
	 */
	public boolean checkMismatch(String iva, Provincia p) {
		if (iva.equals("0")) return true;
		String codice = Integer.toString(p.getCodiceProvincia());
		if(iva.substring(iva.length()-3, iva.length()-1).equals(codice)) return true;
		return false;
	}
	/**
	 * Dato un attributo, controlla se ci siano farmacie che non hanno un valore settato per quell'attributo (ossia conta le righe di file in cui non ho un valore per l'attributo).
	 * Utilizza un filtraggio tramite {@link Utility.scannerDati#filterField(String, String, Object)} settando l'operatore "==" e il valore 0 per il confronto (nel momento in cui si è fatto parsing, si è inserito uno 0
	 * dove mancavano valori per certi campi, eventualità segnalata nel file .csv assegnato con il simbolo "-").
	 * @param String fieldName - campo su cui effettuare il controllo
	 * @return int - numero di righe del file in cui il campo non è presente
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 */
	public int attributoMancante(String fieldName) throws NoSuchFieldException, SecurityException {
		ArrayList<Farmacia> tmp = new ArrayList<Farmacia>();
		tmp=(ArrayList<Farmacia>) super.filterField(fieldName, "==", "0");
		return tmp.size();
		
	}

	
}







