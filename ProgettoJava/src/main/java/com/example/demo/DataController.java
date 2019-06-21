package com.example.demo;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import Eccezioni.OperatorException;
import Eccezioni.RESTErrorHandler;
import Eccezioni.tooManyArguments;
import Utility.scannerDati;
import modelloDataSet.Farmacia;
/** Rest Controller che si occupa delle richieste per la ricerca, il filtraggio e lo scorrimento dei dati. Utilizza un'istanza di scannerDati in tal senso, che gli viene iniettata automaticamente da Spring.
 * 
 * @author Marco Esposito
 */
@RestController
public class DataController {
	scannerDati scan; 

	@Autowired public DataController(@Qualifier("scanner")scannerDati scan) {
		this.scan=scan;
	}

	/**
	 * Richiesta di filtraggio generica per oggetti Farmacia. Filtra i dati numerici a seconda dell'operatore. Per le stringhe ritorna, se c'è una corrispondenza, l'uguaglianza fra il valore immesso e quello nel campo corrispondente indicato.
	 * Operatori definiti: 
	 * = uguaglianza fra numeri o stringhe
	 * > maggiore di
	 * < minore di
	 * Utilizza il metodo {@link Utility.scannerDati#filterField}
	 * @param fieldname - campo da filtrare
	 * @param operator - operatore da utilizzare
	 * @param value - valore con cui effettuare i confronti
	 * @return  Collection - Collection contenente i risultati del filtraggio
	 * @throws SecurityException 
	 * @throws NoSuchFieldException 
	 * @throws RESTErrorHandler 
	 * @throws OperatorException 
	 */
	@RequestMapping ("/filtro") 
	public Collection filtro (@RequestParam(value="campo") String fieldname, @RequestParam(value="operatore") String operator, @RequestParam(value="valore") Object value) throws NoSuchFieldException, SecurityException, RESTErrorHandler, OperatorException{ 
		if ((!operator.equals(">")) && (!operator.equals("<")) && (!operator.equals("=="))) throw new OperatorException();
		if(scan.filterField(fieldname, operator, value)==null) throw new RESTErrorHandler("Attributo");
		else return scan.filterField(fieldname, operator, value);
	}

	/**
	 * Ritorna tutti i dati (organizzati in oggetti Farmacia).
	 * @return ArrayList<Farmacia> - ArrayList contenente tutti gli oggetti Farmacia
	 */
	@RequestMapping ("/dati")
	public ArrayList<Farmacia> getDati(){
		return scan.getDati();
	}

	/**Ritorna i metadati degli oggetti Farmacia
	 * Utilizza il metodo {@link Utility.scannerDati#getMetaFarmacia}
	 * @return ArrayList<JSONObject> - ArrayList contenente i metadati in formato JSON
	 * @throws ParseException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	@RequestMapping("/metaF")
	public ArrayList<JSONObject> getMetaFarmacia() throws ParseException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		return scan.getMetaFarmacia();
	}

	/**Ritorna i metadati degli oggetti Comune
	 * Utilizza il metodo {@link Utility.scannerDati#getMetaComune}
	 * @return ArrayList<JSONObject> - ArrayList contenente i metadati in formato JSON
	 * @throws ParseException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	@RequestMapping("/metaC")
	public ArrayList<JSONObject> getMetaComune() throws ParseException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		return scan.getMetaComune();
	}

	/**Ritorna i metadati degli oggetti Provincia
	 * Utilizza il metodo {@link Utility.scannerDati#getMetaProvincia}
	 * @return ArrayList<JSONObject> - ArrayList contenente i metadati in formato JSON
	 * @throws ParseException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	@RequestMapping("/metaP")
	public ArrayList<JSONObject> getMetaProvincia() throws ParseException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		return scan.getMetaProvincia();
	}


	/**Cerca l'oggetto Farmacia dato il nome
	 * Utilizza il metodo {@link Utility.scannerDati#cerca(String)}
	 * @param String - nome della farmacia
	 * @return Farmacia
	 * @throws RESTErrorHandler
	 */
	@RequestMapping("/cerca")
	public Farmacia cerca(@RequestParam(value="nome") String nome) throws RESTErrorHandler {
		if(scan.cerca(nome)==null) throw new RESTErrorHandler(nome);
		return scan.cerca(nome);
	}

	/**Ritorna i dati della farmacia che si trova alle coordinate fornite
	 * Utilizza il metodo {@link Utility.scannerDati#cercaCoordinate(double, double)}
	 * @param double - latitudine
	 * @param double - longitudine
	 * @return Farmacia
	 * @throws RESTErrorHandler
	 */
	@RequestMapping("/coordinate")
	public Farmacia cercaCoordinate(@RequestParam(value="lat")double lat, @RequestParam(value="long")double longi) throws RESTErrorHandler{
		if(scan.cercaCoordinate(lat, longi)==null) throw new RESTErrorHandler();
		return scan.cercaCoordinate(lat, longi);
	}


	/**Ritorna l'elenco in formato JSON delle farmacie in un determinato comune (equivale ad un filtraggio del tipo http://localhost:8080/filtro?campo=comune&operatore===&valore=nomeComune, ma è riportato per semplicità).
	 * Utilizza il metodo {@link Utility.scannerDati#cercaPerComune(String)}.
	 * @param String - Comune
	 * @return ArrayList<Farmacia> - Farmacie di quel Comune
	 * @throws RESTErrorHandler
	 */
	@RequestMapping ("/comune")
	public ArrayList<Farmacia> cercaPerComune(@RequestParam(value="nome") String Comune) throws RESTErrorHandler {
		if (scan.cercaComune(Comune)==null) throw new RESTErrorHandler("Comune");
		return scan.cercaPerComune(Comune);
	}

	/**Ritorna l'elenco in formato JSON delle farmacie in una determinata provincia (equivale ad un filtraggio del tipo http://localhost:8080/filtro?campo=provincia&operatore===&valore=nomeProvincia, ma è riportato per semplicità).
	 * Utilizza il metodo {@link Utility.scannerDati#cercaPerProvincia(String)}.
	 * String - Provincia
	 * @return ArrayList<Farmacia> - Farmacie di quella Provincia
	 * @throws RESTErrorHandler
	 */
	@RequestMapping("/provincia")
	public ArrayList<Farmacia> cercaPerProvincia(@RequestParam(value="nome") String Provincia) throws RESTErrorHandler {
		if (scan.cercaProvincia(Provincia)==null) throw new RESTErrorHandler("Provincia");
		return scan.cercaPerProvincia(Provincia);
	}

	/** Ritorna l'elenco in formato JSON dei dispensari in un Comune, in una Provincia o nell'intero DataSet.
	 * Utilizza i metodi {@link Utility.scannerDati#getDispensariComune(String)}, {@link Utility.scannerDati#getDispensariProvincia(String)}, {@link Utility.scannerDati#getDispensari()}
	 * @param String - Provincia
	 * @param String - Comune
	 * @return ArrayList<Farmacia> - Elenco dei dispensari
	 * @throws RESTErrorHandler
	 * @throws tooManyArguments 
	 */
	@RequestMapping("/dispensari")
	public ArrayList<Farmacia> cercaDispensari(@RequestParam(value="provincia", defaultValue="") String Provincia, @RequestParam(value="comune", defaultValue="") String Comune) throws RESTErrorHandler, tooManyArguments {

		if(!Provincia.equals("") && !Comune.equals("")) throw new tooManyArguments("Inserire solo un comune o una provincia o nessun argomento");
		else if (!Provincia.equals("")) {
			if (scan.cercaProvincia(Provincia)==null) throw new RESTErrorHandler("Provincia");
			return scan.getDispensariProvincia(Provincia);
		}
		else if (!Comune.equals("")) {
			if (scan.cercaComune(Comune)==null) throw new RESTErrorHandler("Comune");
			return scan.getDispensariComune(Comune);
		}
		else return scan.cercaDispensari();
	}

	/**
	 * Ritorna il risultato dell'operazione statistica richiesta indicata dal parametro operator, sul campo richiesto indicato dal parametro fieldname.
	 * @param fieldname
	 * @param operator
	 * @return
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws OperatorException
	 * @throws RESTErrorHandler
	 */
	@RequestMapping ("/stat") 
	public double stat (@RequestParam(value="campo") String fieldname, @RequestParam(value="operatore") String operator) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, OperatorException, RESTErrorHandler {
		if ((!operator.equals("max")) && (!operator.equals("min")) && (!operator.equals("media")) && (!operator.equals("varianza"))) throw new OperatorException();
		else if (scan.Statistiche(fieldname, operator)==0) throw new RESTErrorHandler("Campo");
		else return scan.Statistiche(fieldname, operator);
	}

}




