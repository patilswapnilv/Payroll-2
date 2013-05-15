package exceptions;

public class EmptyFieldException extends ArithmeticException
{
	public EmptyFieldException()
	{
		super("All fields must be filled in.");
	}
}
