package Eccezioni;
/**
 * Classe che gestisce eccezioni riguarda il passaggio di troppi parametri ad una richiesta GET.
 * @author Marco Esposito
 *
 */
public class tooManyArguments extends Exception{
	public tooManyArguments (String s) {
		super("Troppi Argomenti!"+s);
	}
}
