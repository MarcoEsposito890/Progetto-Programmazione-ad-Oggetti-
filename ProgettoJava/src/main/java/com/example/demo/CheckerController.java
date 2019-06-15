package com.example.demo;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import Eccezioni.RESTErrorHandler;
import Utility.Checker;
import Utility.Parser;
import modelloDataSet.Farmacia;
/**
 * Rest Controller che si occupa delle richieste per il controllo dei dataset. Utilizza un istanza di Checker in tal senso.
 * @author Marco
 *
 */
@RestController
public class CheckerController {
	
		 Checker check; 
		
		@Autowired public CheckerController(@Qualifier("check")Checker check){
			this.check=check;
		}
		

		/**Scorre l'array di farmacie e verifica che il Codice di Controllo della Partita Iva sia esatto, altrimenti ritorna un Errore e la Farmacia Relativa.
		 * L'algoritmo di controllo utilizza la Formula di Lunn, implementata nella classe Calcolatrice.
		 * @param String - provincia
		 * @return ArrayList<String> - Elenco delle Farmacie che hanno un errore nella partita IVA (mismatch fra ultima cifra e codice di controllo calcolato con Lunn).
		 * @throws RESTErrorHandler 
		 */
		
		@RequestMapping("/checkIVA")
		public ArrayList<String> checkPartitaIva(@RequestParam(value="provincia") String provincia) throws RESTErrorHandler{
			if (check.cercaProvincia(provincia)==null) throw new RESTErrorHandler();
			return check.checkPartitaIva(provincia);
		}
		
		/**Ritorna l'elenco delle Partite Ive con lo stesso Codice Provincia. Tiene conto dei mismatch fra Partita Iva e relativa Provincia (terzultima e penultima
		 * cifra della Partita IVA devono coincidere con il Codice Provincia).
		 * 
		 * @param String provincia
		 * @return ArrayList<String> partitaIva - Elenco Partite IVA con segnalati gli Errori.
		 * @throws RESTErrorHandler 
		 */
		@RequestMapping("/partitaIVA")
		public ArrayList<String> partitaIva(@RequestParam(value="provincia") String provincia) throws RESTErrorHandler{
			if (check.cercaProvincia(provincia)==null) throw new RESTErrorHandler();
			return check.partitaIva(provincia);
		}
		
		//Dato un attributo, controlla se ci siano farmacie che non hanno un valore settato per quell'attributo
		

}

