package exceptions;

public class WeekOutOfRangeException extends ArithmeticException
{
	public WeekOutOfRangeException()
	{
		super("Week no has to be between 1 and 52");
	}
}
