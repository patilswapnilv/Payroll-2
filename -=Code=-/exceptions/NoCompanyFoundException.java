package exceptions;

public class NoCompanyFoundException extends ArithmeticException
{
	public NoCompanyFoundException()
	{
		super("No company found. Add new company first.");
	}
}
