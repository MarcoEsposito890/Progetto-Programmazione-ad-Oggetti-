package com.example.demo;
import java.util.ArrayList;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import Utility.Parser;
import Utility.scannerDati;
import Utility.Filter.FilterUtils;
import modelloDataSet.Farmacia;
@Configuration
public class myConfig {
	private final String p = "Elenco-Farmacie.csv";
	
	@Bean (name="nomeFile") //prova a metterlo nel main direttamente?
	public String nomeFile() {
		return p;
	}
	
	@Bean
	public Parser pars() {
		return new Parser();
	}
	
	@Bean FilterUtils<Farmacia> utils(){
		return new FilterUtils<Farmacia>();
	}
}
