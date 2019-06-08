package com.example.demo;

import java.io.File;
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

@RestController
public class DataController {
	 scannerDati scan; 
	 
	@Autowired public DataController(ArrayList<Farmacia> f) {
		scan=new scannerDati(f);
	}
	
	//Ritorna i metadati
	@RequestMapping("/meta")
	public ArrayList<JSONObject> getMeta() throws ParseException{
		return scan.getMeta();
	}
	
	//Cerca la Farmacia dato il nome
	@RequestMapping("/cerca")
	public Farmacia cerca(@RequestParam(value="nome") String nome) {
		return scan.cerca(nome);
	}
	
	//Ritorna i dati della farmacia che si trova alle coordinate fornite
	@RequestMapping("/coordinate")
	public Farmacia cercaCoordinate(@RequestParam(value="lat")double lat, @RequestParam(value="long")double longi) {
		return scan.cercaCoordinate(lat, longi);
		}
	
	//Filtro:ritorna l'elenco in formato JSON delle farmacie in un determinato comune
	@RequestMapping ("/comune")
	public ArrayList<Farmacia> cercaPerComune(@RequestParam(value="nome") String Comune) {
		return scan.cercaPerComune(Comune);
	}
	
	//Filtro:ritorna l'elenco in formato JSON delle farmacie in una determinata provincia
	@RequestMapping("/provincia")
	public ArrayList<Farmacia> cercaPerProvincia(@RequestParam(value="nome") String Provincia) {
		return scan.cercaPerProvincia(Provincia);
	}
	
	//fai un metodo unico per il filtraggio in cui gli passi come RequestParam String attributo, String/Double valore, String operatore e poi implementa le funzioni di filtro come ha fatto lui
	
}
