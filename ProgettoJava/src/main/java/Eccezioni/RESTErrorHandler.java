package Eccezioni;
/**
 * Classe che gestisce eccezioni riguardanti i Rest Controller. Il messaggio di default si presenta quando un oggetto/informazioni cercati non sono presenti nel dataset, altrimenti è possibile anche specificare cosa non è presente.
 * @author Marco Esposito
 *
 */
@SuppressWarnings("serial")
public class RESTErrorHandler extends Exception{
	
	public RESTErrorHandler(String nome){
		super(""+nome+" non presente nel dataset!");
	}
	
	public RESTErrorHandler(){
		super("Non presente nel dataset!");
	}

}
