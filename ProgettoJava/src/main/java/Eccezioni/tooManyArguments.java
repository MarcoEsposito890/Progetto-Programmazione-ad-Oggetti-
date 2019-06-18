package Eccezioni;

public class tooManyArguments extends Exception{
	public tooManyArguments () {
		super("Troppi Argomenti! Inserire solo un comune o una provincia o nessun argomento");
	}
}
