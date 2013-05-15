package exceptions;

public class WrongPpsFormatException extends ArithmeticException
{
	public WrongPpsFormatException()
	{
		super("Wrong Pps.\nCorrect format: 1234567A");
	}
}
