package Utility.Filter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

/**Classe che implementa metodi di utilità per il filtraggio dei dati (confronti, estrazione di oggetti dalle Collection da filtrare o di attributi).
 * 
 * @author Marco Esposito
 *
 * @param <T>
 */
public class FilterUtils<T> {
	/**
	 * Confronta i due oggetti in ingresso secondo l'operatore indicato
	 * @param value - valore con cui si effettuano i confronti
	 * @param operator - operatore scelto per i confronti
	 * @param th - valore da confrontare
	 * @return boolean - conferma o meno se il confronto fatto risulta vero o no
	 */
	public static boolean check(Object value, String operator, Object th) {
		try{ 
			Double thC = Double.parseDouble((String) th); //il Rest Controller passa a scannerDati.filterField delle stringhe (provenienti dalla richiesta HTTP), per cui si effettua una prima conversione
			Double valuec;
			if(value instanceof Integer) { //converto tutti i valori numerici in double per gestire solamente un tipo numerico
				valuec= ((Integer) value).doubleValue();
			} else valuec = (Double) value;

			if (operator.equals("==")) {
				return (valuec.equals(thC));
			}
			else if (operator.equals(">")) {
				return valuec > thC;
			}
			else if (operator.equals("<"))
				return valuec < thC;

		}catch (NumberFormatException e) { //se non è posisbile convertire in double, significa che sto gestendo una stringa
			if(th instanceof String && value instanceof String) { //per le stringhe si implementa semplicemente l'uguaglianza
				return value.equals(th);
			}
		}
		return false;		
	}

	/**
	 * Metodo di filtraggio generico utilizzato per filtrare attributi di un classe (in particolare, lo useremo per Farmacia). Si passa al metodo il campo su cui eseguire il filtraggio,
	 * l'operatore che si desidera usare e il valore con cui effettuare il confronto. Il metodo preleva iterativamente il metodo getter corrispondente al parametro fieldName dalla classe degli oggetti
	 * della Collection<T> in ingresso, e poi li passa al metodo checker per verificare se soddisfano o meno la condizione indicata. Se il risultato di checker è true, allora l'oggetto viene aggiunto alla
	 * Collection<T> in uscita.
	 * @param Collection<T> src - collection da filtrare
	 * @param fieldName - campo da filtrare
	 * @param value - valore con cui si effettuano i confronti
	 * @param operator - operatore scelto per i confronti
	 * @return Collection<T> - Collection contenente gli oggetti che soddisfano la relazione data da operator e value
	 */

	public Collection<T> select(Collection<T> src, String fieldName, String operator, Object value) {
		Collection<T> out = new ArrayList<T>();
		for(T item:src) {
			try {

				Method m = item.getClass().getMethod("get"+fieldName.substring(0, 1).toUpperCase()+fieldName.substring(1),null); //si ricava il metodo getter per il campo indicato da fieldName(dalla classe T)

				try {
					Object tmp = m.invoke(item); //si invoca il metodo getter così creato, ottenendo l'oggetto su cui fare il confronto
					if(FilterUtils.check(tmp, operator, value))
						out.add(item); //se il risultato di check è true, si aggiunge l'oggetto alla collezione in uscita 
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			}					
		}
		return out;
	}

	/**
	 * Metodo di filtraggio utilizzato nel caso in cui si debbano filtrare oggetti di classi istanziate in Farmacia che non siano Stringhe o tipi primitivi (ad esempio, Comune o Farmacia). Oltre al nome del campo da filtrare,
	 * viene dato come parametro in ingresso anche il nome della classe (istanziata negli oggetti della Collection<T>) su cui eseguiamo il filtraggio. In questa maniera prima si crea un oggetto di quella classe accedendovi tramite il metodo getter relativo, poi
	 * tramite quell'oggetto si procede come nel metodo precedente. Si utilizza il parametro formale annidato per sapere se l'operazione sopra deve essere iterata ulteriormente (ad esempio, se vogliamo prelevare oggetti Provincia da oggetti Comune).
	 * La classe ritorna comunque una collezione di tipo Farmacia (più in generale, di tipo generico T), essendo un filtraggio di questo genere più significativo (ad esempio, permette di filtrare tutte le Farmacie nello stesso comune, provincia etc.).
	 * @param Collection<T> src - collection da filtrare
	 * @param fieldName - campo da filtrare
	 * @param value - valore con cui si effettuano i confronti
	 * @param operator - operatore scelto per i confronti
	 * @param classe1 - Classe dell'oggetto contenuto nella classe T sui cui campi eseguiamo il filtraggio
	 * @param classe2 - Classe contenuta in classe1, nel caso dobbiamo prendere un suo attributo e iterare il procedimento
	 * @param annidato -- Flag usato per indicare che è necessario iterare il procedimento descritto sopra per prelevare un oggetto di classe2 (contenuto nell'oggetto di classe1).
	 * @return Collection<T> - Collection contenente gli oggetti che soddisfano la relazione data da operator e value
	 */
	public Collection<T> select(Collection<T> src, String fieldName, String operator, Object value, String classe1, String classe2, boolean annidato) {
		Collection<T> out = new ArrayList<T>();
		for(T item:src) {
			try {

				Method m1= item.getClass().getMethod("get"+classe1.substring(0,1).toUpperCase()+classe1.substring(1), null); ///si ricava il metodo getter getFieldName per il campo indicato da fieldName (dalla classe T). Ad esempio per Farmacia, se classe1="comune", si ottiene il metodo getComune)
				Method m2;
				try {
					//si crea un oggetto della classe istanziata nella classe di tipo T (per Farmacia, ad esempio, si crea un oggetto di tipo Comune)
					Object tmp = m1.invoke(item); 

					if (annidato) { //se annidato è vero, significa che l'attributo appartiene a una classe istanziata nell'oggetto appena creato, quindi dobbiamo prelevare un ulteriore metodo getter e ripetere il procedimento sopra (ad esempio, per prelevare oggetti di tipo Provincia da oggetti di tipo Comune)
						m2=tmp.getClass().getMethod("get"+classe2.substring(0,1).toUpperCase()+classe2.substring(1), null);
						Object tmp2=m2.invoke(tmp);
						Method m = tmp2.getClass().getMethod("get"+fieldName.substring(0, 1).toUpperCase()+fieldName.substring(1),null); //si ottiene il metodo getter getFieldName dell'oggetto contenuto in T (ad esempio, un getter della classe Provincia)
						Object tmp3 = m.invoke(tmp2); //si invoca il metodo getter così creato, ottenendo l'oggetto su cui fare il confronto
						if(FilterUtils.check(tmp3, operator, value))//si procede con il filtraggio vero e proprio
							out.add(item); //a seconda del risultato del filtraggio, si aggiunge o meno un oggetto di tipo T alla collezione in uscita
					}
					//nel caso in cui non ci sia bisogno di estrarre un'ulteriore classe, si procede alla stessa maniera ma con un unico getter
					else if(!annidato) { 
						Method m = tmp.getClass().getMethod("get"+fieldName.substring(0, 1).toUpperCase()+fieldName.substring(1),null); //si ottiene il metodo getFieldName dell'oggetto contenuto in T (ad esempio, un getter della classe Comune)
						Object tmp2 = m.invoke(tmp); //si invoca il metodo getter così creato, ottenendo l'oggetto su cui fare il confronto
						if(FilterUtils.check(tmp2, operator, value)) //si procede con il filtraggio vero e proprio
							out.add(item); }//a seconda del risultato del filtraggio, si aggiunge o meno un oggetto di tipo T alla collezione in uscita
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			}					
		}
		return out;
	}

	/**
	 * Metodo utilizzato per filtrare un intero campo del dataset(una colonna). Poichè viene utilizzato per operazioni statistiche, si è implementato
	 * direttamente per gestire dati numerici. Il funzionamento è analogo ai metodi di filtraggio (si crea il metodo getFieldName per prelevare gli oggetti e poi si inseriscono in un ArrayList).
	 * @param Collectio<T> src - collezione di partenza (ArrayList di oggetti Farmacia).
	 * @param String fieldName - nome del campo da prelevare.
	 * @param String classe - classe dell'oggetto contenuto nella classe T di cui preleviamo un campo.
	 * @return ArrayList<Double> - ArrayList in cui si inseriscono i valori per quell'attributo (colonna del data-set).
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public ArrayList<Double> prelevaCampo(Collection<T> src, String fieldName) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		ArrayList<Double> out = new ArrayList<Double>();
		for(T item:src) {
			try {
				Method m = item.getClass().getMethod("get"+fieldName.substring(0, 1).toUpperCase()+fieldName.substring(1),null); //si ricava il metodo getter per il campo indicato da fieldName(dalla classe T)
				Object tmp = m.invoke(item); 
				double tmpout;
				if(tmp instanceof Integer) { //converto tutti i valori numerici in double per gestire solamente un tipo numerico
					tmpout = ((Integer) tmp).doubleValue();
				} else tmpout = (Double) tmp;
				if (tmpout!=0) out.add(tmpout); //i campi posti a 0 sono quelli in cui c'era un attributo mancante, quindi li escludiamo
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			}			
		}
		return out;
	}
	/**
	 * Metodo utilizzato per filtrare un intero campo del dataset(una colonna). Analogamente al secondo metodo di filtraggio, utilizziamo questo metodo per prelevare colonne che si riferiscono a Comune (ad esempio, latitudine e longitudine). 
	 * Poichè viene utilizzato per operazioni statistiche, si è implementato direttamente per gestire dati numerici. Il funzionamento è analogo ai metodi di filtraggio (si crea il metodo getFieldName per prelevare gli oggetti e poi si inseriscono in un ArrayList).
	 * @param Collectio<T> src - collezione di partenza (ArrayList di oggetti Farmacia).
	 * @param String fieldName - nome del campo da prelevare.
	 * @param String classe - classe dell'oggetto contenuto nella classe T di cui preleviamo un campo.
	 * @return ArrayList<Double> - ArrayList in cui si inseriscono i valori per quell'attributo (colonna del data-set).
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public ArrayList<Double> prelevaCampo(Collection<T> src, String fieldName, String classe) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		ArrayList<Double> out = new ArrayList<Double>();
		for(T item:src) {
			try {
				Method m = item.getClass().getMethod("get"+classe.substring(0, 1).toUpperCase()+classe.substring(1),null); //si ricava il metodo getter per il campo indicato da fieldName(dalla classe T)
				Object tmp = m.invoke(item); 
				Method m1 = tmp.getClass().getMethod("get"+fieldName.substring(0, 1).toUpperCase()+fieldName.substring(1),null);
				Object tmp2= m1.invoke(tmp);
				double tmpout;
				if(tmp2 instanceof Integer) { //converto tutti i valori numerici in double per gestire solamente un tipo numerico
					tmpout = ((Integer) tmp2).doubleValue();
				} else tmpout = (Double) tmp2;
				if (tmpout!=0) out.add(tmpout); //i campi posti a 0 sono quelli in cui c'era un attributo mancante, quindi li escludiamo
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			}			
		}
		return out;
	}

}
