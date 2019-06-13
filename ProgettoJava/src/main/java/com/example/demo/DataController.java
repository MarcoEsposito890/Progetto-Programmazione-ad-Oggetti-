package com.example.demo;

import java.io.File;
import Eccezioni.RESTErrorHandler;
import org.springframework.boot.json.BasicJsonParser;
import org.springframework.boot.json.JsonParser;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicLong;

import javax.validation.Valid;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import Utility.Parser;
import Utility.scannerDati;
import Utility.GPS;
import Utility.Checker;
import modelloDataSet.Farmacia;
/** Rest Controller che si occupa delle richieste per la ricerca, il filtraggio e lo scorrimento dei dati. Utilizza un'istanza di scannerDati in tal senso.
 * 
 * @author Marco Esposito
 */
@RestController
public class DataController {
	 scannerDati scan; 
	 
	@Autowired public DataController(ArrayList<Farmacia> f) {
		scan= new scannerDati(f);
	}
	
	/**
	 * Filtro Generico per Oggetti Farmacia. Filtra i dati numerici (latitudine e longitudine) a seconda dell'operatore. Per le stringhe ritorna, se c'è una corrispondenza, l'uguaglianza fra il valore immesso e quello nel campo corrispondente indicato
	 * Operatori definiti: 
	 * = uguaglianza fra numeri o stringhe
	 * > maggiore di
	 * < minore di
	 * ( contiene la stringa/sottostringa indicata
	 * @param fieldname
	 * @param operator
	 * @param value
	 * @return
	 * @throws SecurityException 
	 * @throws NoSuchFieldException 
	 */
	@RequestMapping ("/filtro") 
	public Collection filtro (@RequestParam(value="campo") String fieldname, @RequestParam(value="operatore") String operator, @RequestParam(value="valore") Object value) throws NoSuchFieldException, SecurityException{ 
		return scan.filterField(fieldname, operator, value);
	}
	
	//Ritorna i metadati
	@RequestMapping("/meta")
	public ArrayList<JSONObject> getMeta() throws ParseException{
		return scan.getMeta();
	}
	
	/**Cerca la Farmacia dato il nome
	 * 
	 * @param nome
	 * @return
	 * @throws RESTErrorHandler
	 */
	@RequestMapping("/cerca")
	public Farmacia cerca(@RequestParam(value="nome") String nome) throws RESTErrorHandler {
		if(scan.cerca(nome)==null) throw new RESTErrorHandler();
		return scan.cerca(nome);
	}
	
	/**Ritorna i dati della farmacia che si trova alle coordinate fornite
	 * 
	 * @param lat
	 * @param longi
	 * @return
	 * @throws RESTErrorHandler
	 */
	@RequestMapping("/coordinate")
	public Farmacia cercaCoordinate(@RequestParam(value="lat")double lat, @RequestParam(value="long")double longi) throws RESTErrorHandler{
		if(scan.cercaCoordinate(lat, longi)==null) throw new RESTErrorHandler();
		return scan.cercaCoordinate(lat, longi);
		}
	

	/**Ritorna l'elenco in formato JSON delle farmacie in un determinato comune
	 * 
	 * @param Comune
	 * @return
	 * @throws RESTErrorHandler
	 */
	@RequestMapping ("/comune")
	public ArrayList<Farmacia> cercaPerComune(@RequestParam(value="nome") String Comune) throws RESTErrorHandler {
		if (scan.cercaComune(Comune)==null) throw new RESTErrorHandler();
		return scan.cercaPerComune(Comune);
	}
	
	/**Ritorna l'elenco in formato JSON delle farmacie in una determinata provincia
	 * 
	 * @param Provincia
	 * @return
	 * @throws RESTErrorHandler
	 */
	@RequestMapping("/provincia")
	public ArrayList<Farmacia> cercaPerProvincia(@RequestParam(value="nome") String Provincia) throws RESTErrorHandler {
		if (scan.cercaProvincia(Provincia)==null) throw new RESTErrorHandler();
		return scan.cercaPerProvincia(Provincia);
	}
	
	//fai un metodo unico per il filtraggio in cui gli passi come RequestParam String attributo, String/Double valore, String operatore e poi implementa le funzioni di filtro come ha fatto lui
	
}
