package Utility.Filter;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

import modelloDataSet.Comune;
import modelloDataSet.Provincia;


public class FilterUtils<T> {
	/**
	 * Confronta i due oggetti in ingresso secondo l'operatore indicato
	 * @param value
	 * @param operator
	 * @param th
	 * @return boolean - conferma o meno se il confronto fatto risulta vero o no
	 */
	public static boolean check(Object value, String operator, Object th) {
		try{ 
		Double thC = Double.parseDouble((String) th);
		Double valuec;
		if(value instanceof Integer) { //converto i valori numerici in double
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
		
		}catch (NumberFormatException e) {
				
				if(th instanceof String && value instanceof String) { //per le stringhe si implementa semplicemente l'uguaglianza
				return value.equals(th);
				}
		}
		return false;		
	}
	
	/**
	 * Metodo di filtraggio generico utilizzato per filtrare attributi di un classe (in particolare, lo useremo per Farmacia). Si passa al metodo il campo su cui eseguire il filtraggio,
	 * l'operatore che si desidera usare e il valore su cui effettuare il confronto. Il metodo preleva iterativamente il metodo getter corrispondente al parametro fieldName dalla classe degli oggetti
	 * della Collection<T> in ingresso, e poi li passa al metodo checker per verificare se soddisfano o meno la condizione indicata. Se il risultato di checker è true, allora l'oggetto viene aggiunto alla
	 * Collection<T> in uscita.
	 * @param src
	 * @param fieldName
	 * @param operator
	 * @param value
	 * @return Collection<T> - Collection contenente gli oggetti che soddisfano la relazione data da operator e value
	 */
	
	public Collection<T> select(Collection<T> src, String fieldName, String operator, Object value) {
		Collection<T> out = new ArrayList<T>();
		for(T item:src) {
			try {
				
				Method m = item.getClass().getMethod("get"+fieldName.substring(0, 1).toUpperCase()+fieldName.substring(1),null);
				
				try {
					Object tmp = m.invoke(item); 
					if(FilterUtils.check(tmp, operator, value))
						out.add(item);
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
	 * viene dato come parametro in ingresso anche il nome della variabile di Farmacia (o, più in generale, contenuta nella classe generica T) su cui eseguiamo il filtraggio. In questa maniera prima si crea un oggetto di quella classe accedendovi tramite il metodo get relativo (creato runtime), poi
	 * tramite quell'oggetto si procede come nel metodo precedente. La classe ritorna comunque una collezione di tipo Farmacia (in generale di tipo T), essendo un filtraggio di questo genere più significativo (ad esempio, permette di filtrare tutte le Farmacie nello stesso comune, provincia etc.).
	 * @param src - Collection di oggetti di tipo generico T
	 * @param fieldName - Campo su cui eseguire il filtraggio
	 * @param operator - Operatore di filtraggio
	 * @param value - Valore con cui effettuare i confronti
	 * @param classe1 - Classe dell'oggetto contenuto nella classe T sui cui campi eseguiamo il filtraggio
	 * @param classe2 - Classe contenuta in classe1, nel caso di doppio annidamento
	 * @param annidato -- Flag usato per indicare si fa filtraggio su un oggetto contenuto in un oggetto contenuto in un membro della classe della Collection src.
	 * @return Collection<T> - Collection contenente gli oggetti che soddisfano la relazione data da operator e value
	 */
	public Collection<T> select(Collection<T> src, String fieldName, String operator, Object value, String classe1, String classe2, boolean annidato) {
		Collection<T> out = new ArrayList<T>();
		for(T item:src) {
			try {
				
				Method m1= item.getClass().getMethod("get"+classe1.substring(0,1).toUpperCase()+classe1.substring(1), null); //si preleva il metodo get della classe incapsulata nella classe T. Ad esempio per Farmacia, se sottoclasse="comune", si ottiene il metodo getComune)
				Method m2;
				try {
					Object tmp = m1.invoke(item); 
					//si crea un oggetto della classe contenuta nella classe di tipo T (di nuovo per Farmacia, ad esempio, si crea un oggetto di tipo Comune)
					if (annidato) { //se annidato è vero, significa che l'attributo appartiene a una classe contenuta nell'oggetto appena creato, quindi dobbiamo creare un ulteriore getter (ad esempio, per prelevare oggetti di tipo Provincia da oggetti di tipo Comune)
						m2=tmp.getClass().getMethod("get"+classe2.substring(0,1).toUpperCase()+classe2.substring(1), null);
						Object tmp2=m2.invoke(tmp);
						Method m = tmp2.getClass().getMethod("get"+fieldName.substring(0, 1).toUpperCase()+fieldName.substring(1),null); //si ottiene il metodo getFieldName dell'oggetto contenuto in T (ad esempio, un getter della classe Provincia)
						Object tmp3 = m.invoke(tmp2); //si estrae l'informazione tramite il metodo getter appena creato
						if(FilterUtils.check(tmp3, operator, value))//si procede con il filtraggio vero e proprio
							out.add(item); //a seconda del risultato del filtraggio, si aggiunge o meno un oggetto di tipo T alla collezione in uscita
					}
					//nel caso in cui non ci sia bisogno di estrarre un'ulteriore classe, si procede alla stessa maniera ma con un unico getter
					else if(!annidato) { //se annidato è falso, procedo con il metodo getter (m1) per la classe istanziata in T
					Method m = tmp.getClass().getMethod("get"+fieldName.substring(0, 1).toUpperCase()+fieldName.substring(1),null); //si ottiene il metodo getFieldName dell'oggetto contenuto in T (ad esempio, un getter della classe Comune)
					Object tmp2 = m.invoke(tmp); //si estrae l'informazione tramite il metodo getter appena creato
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


}
