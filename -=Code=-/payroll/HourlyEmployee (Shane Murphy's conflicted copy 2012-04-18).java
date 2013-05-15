package payroll;

public final class hourlyEmployee extends Employee
{
	private double hourlyRate;
	
	public hourlyEmployee(String sn,String fn, String addL1, String addL2, String addL3, String email, String phone,
							String dob, String pps, String sDate,String fDate,String dept,String pType, double rate)
	{
		super(sn,fn,addL1, addL2, addL3,email, phone, dob,pps, sDate, fDate, dept, pType);
		this.hourlyRate=rate;
	}
	
	public hourlyEmployee(int no,String sn,String fn, String addL1, String addL2, String addL3, String email, String phone,
							String dob, String pps, String sDate, String fDate, String dept,String pType, double rate)
	{
		super(no,sn,fn,addL1, addL2, addL3, email,phone,  dob,pps, sDate, fDate, dept, pType);
		this.hourlyRate=rate;
	}
	
	public double calcGrossPay(double hours)
	{
		return this.hourlyRate*hours;
	}
	
	public double getRate()
	{
		return this.hourlyRate;
	}
	public void setRate(double newRate)
	{
		this.hourlyRate=newRate;
	}
}
