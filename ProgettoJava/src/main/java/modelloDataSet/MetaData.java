package modelloDataSet;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import Utility.MetaDataStore;
/** 
 * Interfaccia per accedere ai metadati. Implementa il metodo statico creaMetaDati, che prende in ingresso la Classe e i campi derivati dal DataSet e costruisce un ArrayList di oggetti JSON che rappresentano i metadati per quella classe. 
 * Implementando l'accesso ai metadati come interfaccia è possibile facilmente accedere ai metadati di tutte le classi che implementano MetaData.
 * @author Marco
 *
 */
public interface MetaData{
	
public MetaDataStore getMetaDati() throws NoSuchMethodException, SecurityException, ParseException;

/**
 * Realizza l'ArrayList contenente i metadati in formato JSON. Poichè il metodo è comune a tutte le classi che implementano
 * l'interfaccia, è stato dichiarato static. Prende in ingresso la Classe e i Campi che costituiscono i metadati.
 * A partire da questi poi ricava il metodo relativo al campo e la sua annotazione, in cui sono appunto dichiarati i metadati.
 * @param f
 * @param campi
 * @return
 * @throws NoSuchMethodException
 * @throws SecurityException
 * @throws ParseException
 */
static ArrayList<JSONObject> creaMetaDati(Class<?> f, String...campi) throws NoSuchMethodException, SecurityException, ParseException{
	ArrayList<JSONObject> temp = new ArrayList<JSONObject>();
	JSONParser parser = new JSONParser();
	for(int i=0; i<campi.length; i++){
		String tmp = "get"+campi[i];
		Method m = f.getMethod(tmp);
		metadati meta = m.getAnnotation(metadati.class);
		String s =("{\"Alias\":\""+meta.alias()+"\",\"Source Field\":\""+meta.sourcefield()+"\",\"Type\":\""+meta.type()+"\"}");
		JSONObject obj = (JSONObject) parser.parse(s);
		temp.add(obj);
		}
	return temp;
}
/**
 * Annotazione utilizzata per i metadati
 * @author Marco
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@interface metadati{
	String alias();
	String sourcefield();
	String type();
}

}
