package exceptions;

public class InputLengthExceededException extends ArithmeticException
{
	public InputLengthExceededException(int length, String field)
	{
		super("Value too long.\n Maximum length of " + field 
				+ " field is " + length + " characters.");
	}

}
