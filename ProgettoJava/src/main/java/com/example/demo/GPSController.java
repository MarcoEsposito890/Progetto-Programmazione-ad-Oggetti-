package com.example.demo;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import Eccezioni.RESTErrorHandler;
import Utility.Checker;
import Utility.GPS;
import Utility.Parser;
import modelloDataSet.Farmacia;
/**
 * Rest Controller che si occupa delle richieste riguardanti latitudine e longitudine. Utilizza un'istanza di GPS in tal senso.
 * @author Marco
 *
 */
@RestController
public class GPSController{
		 GPS gps; 
		
		@Autowired public GPSController(@Qualifier("gps")GPS gps)
		{
		this.gps=gps;
		}
	
	
	//Trova la distanza fra due farmacie, dato il nome
		@RequestMapping("/Distanza")
		public String distanza(@RequestParam(value="nome1")String nome1, @RequestParam(value="nome2")String nome2) throws RESTErrorHandler {
			if(gps.cerca(nome1)==null && gps.cerca(nome2)==null) throw new RESTErrorHandler();
			return "Distanza in km: "+gps.distanza(nome1, nome2);
		}
	
	//Trova la farmacia più vicina, dato il nome
		@RequestMapping("/PiuVicina")
		public Farmacia piuVicina(@RequestParam(value="nome")String nome) throws RESTErrorHandler {
			if(gps.cerca(nome)==null) throw new RESTErrorHandler("Farmacia");
			return gps.vicina(nome);
		}
		

		/*@RequestMapping("/Tempo")
		public String tempo(@RequestParam(value="Nome1") String nome1, @RequestParam(value="Nome2") String nome2, @RequestParam(value="Velocita") double velocita) {
			return "Tempo Necessario (in minuti):"+gps.tempoNecessario(nome1, nome2, velocita);
		}
		
		@RequestMapping("/Tempo2")
		public String tempo(@RequestParam(value="Destinazione")String destinazione, @RequestParam(value="Longitudine")double lon,@RequestParam(value="Latitudine") double lat, @RequestParam(value="Velocita") double velocita) {
			return "Tempo Necessario (in minuti):"+gps.tempoNecessario(destinazione, lon, lat, velocita);
		}*/

}
