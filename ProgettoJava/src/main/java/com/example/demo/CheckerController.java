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

import Utility.Checker;
import Utility.Parser;
import modelloDataSet.Farmacia;

@RestController
public class CheckerController {
	
		 Checker check; 
		
		@Autowired public CheckerController(ArrayList<Farmacia> f){
			check= new Checker(f);
		}
		

		//Scorre l'array di farmacie e verifica che il Codice di Controllo della Partita Iva sia esatto, altrimenti ritorna un Errore e la Farmacia Relativa.
		//L'algoritmo di controllo utilizza la Formula di Lunn, implementata nella classe Calcolatrice
		@RequestMapping("/checkIVA")
		public ArrayList<String> checkPartitaIva(@RequestParam(value="provincia") String provincia){
			return check.checkPartitaIva(provincia);
		}
		
		//Ritorna l'elenco delle Partite Ive con lo stesso Codice Provincia. Tiene conto dei mismatch fra Partita Iva e relativa Provincia
		@RequestMapping("/partitaIVA")
		public ArrayList<String> partitaIva(@RequestParam(value="provincia") String provincia){
			return check.partitaIva(provincia);
		}
		
		//Dato un attributo, controlla se ci siano farmacie che non hanno un valore settato per quell'attributo
		

}

