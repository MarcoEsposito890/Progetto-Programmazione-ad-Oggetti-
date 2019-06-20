package Eccezioni;

/**
 * Classe che gestisce eccezioni riguardanti richieste GET con un'operatore errato.
 * @author Marco Esposito
 *
 */
public class OperatorException extends Exception{
	public OperatorException(){
		super("Operatore non definito");
	}
}
