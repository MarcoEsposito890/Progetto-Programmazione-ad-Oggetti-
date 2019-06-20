package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import Eccezioni.RESTErrorHandler;
import Utility.GPS;
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
	
	
	/**Trova la distanza fra due farmacie, dato il nome. Utilizza il metodo di GPS {@link Utility.GPS#distanza(double, double, double, double)
	 * 
	 * @param String nome1 - nome della prima farmacia
	 * @param String nome2 - nome della seconda farmacia
	 * @return String - stringa in cui è riportata la distanza in km
	 * @throws RESTErrorHandler
	 */
		@RequestMapping("/Distanza")
		public String distanza(@RequestParam(value="nome1")String nome1, @RequestParam(value="nome2")String nome2) throws RESTErrorHandler {
			if(gps.cerca(nome1)==null && gps.cerca(nome2)==null) throw new RESTErrorHandler();
			return "Distanza in km: "+gps.distanza(nome1, nome2);
		}
	
	/**Trova la farmacia più vicina, dato il nome. Utilizza il metodo di GPS {@link Utility.GPS#trovaVicina(java.util.ArrayList, java.util.ArrayList, double, double)}
	 * 
	 * @param String nome - nome della farmacia
	 * @return Farmacia - farmacia più vicina
	 * @throws RESTErrorHandler
	 */
		@RequestMapping("/PiuVicina")
		public Farmacia piuVicina(@RequestParam(value="nome")String nome) throws RESTErrorHandler {
			if(gps.cerca(nome)==null) throw new RESTErrorHandler("Farmacia");
			return gps.vicina(nome);
		}
		
}
