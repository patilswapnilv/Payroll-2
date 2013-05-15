package exceptions;

public class WrongDateFormatException extends ArithmeticException
{
	public WrongDateFormatException()
	{
		super("Wrong date.\nCorrect format: dd/mm/yyyy");
	}

}
