package exceptions;

public class NoEmployeeFoundException extends ArithmeticException
{
	public NoEmployeeFoundException()
	{
		super("No employees in database");
	}
}
