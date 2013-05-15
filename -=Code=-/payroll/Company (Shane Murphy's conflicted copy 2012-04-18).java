package payroll;

public class Company
{
	private String companyName;
	private String compRegNo;
	private int startWeekNo;
	
	public Company(String name, String reg, int week)
	{
		this.companyName=name;
		this.compRegNo=reg;
		this.startWeekNo=week;
	}
	
	public void setCompName(String name)
	{
		this.companyName=name;
	}
	public String getCompName()
	{
		return this.companyName;
	}
	public void setCompRegNo(String reg)
	{
		this.compRegNo=reg;
	}
	public String getCompRegNo()
	{
		return this.compRegNo;
	}
	public void setStartWeek(int week)
	{
		this.startWeekNo=week;
	}
	public int getStartWeek()
	{
		return this.startWeekNo;
	}
	public String toString()
	{
		String s = "\nCompany name:\t" + this.companyName
					+"\nRegistration no:\t" + this.compRegNo;
		return s;
	}
}
