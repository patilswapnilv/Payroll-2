package exceptions;

public class InvalidLoginException extends ArithmeticException
{
	public InvalidLoginException()
	{
		super("Invalid username or password.");
	}
}
