package exceptions;

public class IvalidEmailAddressException extends ArithmeticException
{
	public IvalidEmailAddressException()
	{
		super("Invalid email address.");
	}
}
