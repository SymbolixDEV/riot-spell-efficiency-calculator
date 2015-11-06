package exception;

/**
 * An exception to be thrown whenever a negative number is present in the set* methods of
 * SpellEfficiencyCalculator.
 * 
 * @author Leonard Kerr
 */
public class NegativeNumberException extends Exception {

	/** ID number used for object serialization. */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Parameterless constructor
	 */
	public NegativeNumberException() {}
	
	/**
	 * Constructor that accepts a message with the exception.
	 * @param message String to be added to the exception
	 */
	public NegativeNumberException(String message){
		super(message);
	}

}
