package payroll;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import exceptions.NoEmployeeFoundException;
import exceptions.NoUserFoundException;

import oracle.jdbc.pool.OracleDataSource;

public class dbConn
{
	private Connection conn;
	private ResultSet rs;
	private ResultSet tsRs;
	private ResultSet uRs;
	private ResultSet wpRs;
	private PreparedStatement ps;
	static int TOT_EMPLOYEE;
	
	private void connect()
	{
		try
		{	
			OracleDataSource ods = new OracleDataSource();
			ods.setURL("jdbc:oracle:thin:@127.0.0.1:1521:xe");
		    ods.setUser("payroll");
		    ods.setPassword("payroll");
		    
		    conn = ods.getConnection();
		    System.out.println("Connected");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void disconnect()
	{
		try
		{
			conn.close();
			System.out.println("Connection closed");
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	public PayrollUser getPayrollUser(String alias)
	{
		PayrollUser user=null;
		String queryString = "select * from payroll_user where user_alias=?";
		try
		{
			connect();
			ps = conn.prepareStatement(queryString,ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			ps.setString(1, alias);
			rs = ps.executeQuery();
			
			if(rs.next())
			{
				user = new PayrollUser(rs.getString("user_fname"),
													rs.getString("user_lname"),
													rs.getString("user_alias"),
													rs.getString("user_password"));
			}
			//disconnect();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return user;
	}
	
	public ResultSet getCompany()
	{
		try
		{
			connect();
			rs=null;
			ps = conn.prepareStatement("select * from company",ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			rs = ps.executeQuery();
		}
		catch (SQLException e)
		{
			return rs;
			//e.printStackTrace();
		}
		return rs;
	}
	
	public ResultSet getAllEmployees()
	{
		try
		{
			String queryString = "select * from employee, revdetails, bankdetails " +
									"where employee.emp_no=revdetails.emp_no " +
									"and employee.emp_no=bankdetails.emp_no order by emp_surname";
			
			ps = conn.prepareStatement(queryString, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			rs = ps.executeQuery();
			rs.last();
			TOT_EMPLOYEE=rs.getRow();
			rs.beforeFirst();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return rs;
	}
	
	public void insertTimesheet(TimeSheet ts)
	{
		String insertString = "insert into timesheet values(?,?,?,?,?,?,?,?,?,?)";
		try
		{
			System.out.println("Inserting timesheet...");
			ps = conn.prepareStatement(insertString);
			ps.setInt(1, ts.getEmpNo());
			ps.setInt(2, ts.getWeekNo());
			ps.setString(3, ts.getMon());
			ps.setString(4, ts.getTue());
			ps.setString(5, ts.getWed());
			ps.setString(6, ts.getThu());
			ps.setString(7, ts.getFri());
			ps.setString(8, ts.getSat());
			ps.setString(9, ts.getSun());
			ps.setDouble(10, ts.getTotHours());
			ps.executeUpdate();
			System.out.println("Timesheet inserted");
			
			ps = conn.prepareStatement("update company set start_week=?");
			ps.setInt(1, ts.getWeekNo());
			ps.executeUpdate();
			System.out.println("Start week changed to " + ts.getWeekNo());
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	public TimeSheet getTimesheet(Employee emp, int week)
	{
		TimeSheet ts = null;
		String queryString = "select * from timesheet where emp_no=? and week_no=?";
		try
		{
			System.out.println("Retriving timesheet...");
			ps = conn.prepareStatement(queryString,ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			ps.setInt(1, emp.getEmpNo());
			ps.setInt(2, week);
			tsRs = ps.executeQuery();
			while(tsRs.next())
			{
				ts = new TimeSheet(emp, tsRs.getInt("week_no"),
									tsRs.getString("monday"),
									tsRs.getString("tuesday"),
									tsRs.getString("wednsday"),
									tsRs.getString("thursday"),
									tsRs.getString("friday"),
									tsRs.getString("saturday"),
									tsRs.getString("sunday"),
									tsRs.getDouble("tot_hours"));
			}
			System.out.println("Timesheet retrived.");
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return ts;
	}
	
	public void insertTotalPay(WeeklyPay wp)
	{
		try
		{
			ps = conn.prepareStatement("update totalpay set tot_gross_pay=tot_gross_pay+?,"
										+"tot_net_pay=tot_net_pay+?,"
										+"tot_tax_paid=tot_tax_paid+?,"
										+"tot_prsi_paid=tot_prsi_paid+?,"
										+"tot_usc_paid=tot_usc_paid+? where emp_no=?");
			ps.setDouble(1, wp.getGrossPay());
			ps.setDouble(2, wp.getNetPay());
			ps.setDouble(3, wp.getTaxPaid());
			ps.setDouble(4, wp.getPrsiPaid());
			ps.setDouble(5, wp.getUscPaid());
			ps.setInt(6, wp.getEmpNo());
			ps.executeUpdate();
			System.out.println("TotalPay updated");
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	public void insertWeeklyPay(WeeklyPay wp)
	{
		String insertString = "insert into weeklypay values(?,?,?,?,?,?,?)";
		try
		{
			System.out.println("Inserting weeklypay...");
			ps = conn.prepareStatement(insertString);
			ps.setInt(1, wp.getEmpNo());
			ps.setInt(2, wp.getWeekNo());
			ps.setDouble(3, wp.getGrossPay());
			ps.setDouble(4, wp.getNetPay());
			ps.setDouble(5, wp.getTaxPaid());
			ps.setDouble(6, wp.getPrsiPaid());
			ps.setDouble(7, wp.getUscPaid());
			ps.executeUpdate();
			System.out.println("Weeklypay inserted");
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	public WeeklyPay getWeeklyPay(Employee emp, int week)
	{
		WeeklyPay wp = null;
		String queryString = "select * from weeklypay where emp_no=? and week_no=?";
		try
		{
			//connect();
			System.out.println("Retriving weeklypay...");
			ps = conn.prepareStatement(queryString,ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			ps.setInt(1, emp.getEmpNo());
			ps.setInt(2, week);
			wpRs = ps.executeQuery();
			while(wpRs.next())
			{
				wp = new WeeklyPay(emp, wpRs.getInt("week_no"),
									wpRs.getDouble("gross_pay"),
									wpRs.getDouble("net_pay"),
									wpRs.getDouble("tax_paid"),
									wpRs.getDouble("prsi_paid"),
									wpRs.getDouble("usc_paid"));
			}
			System.out.println("Weeklypay retrived.");
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return wp;
		
	}
	
	public void updateEmpDetails(Employee emp, double cutOff, double taxCredit, String prsi,
									String bankName, String accountNo, String sortCode)
	{
		try
		{
			System.out.println("Updating...");
			ps = conn.prepareStatement("update employee set emp_surname=?, "
										+"emp_fname=?, emp_dob=?, emp_address_l1=?,emp_address_l2=?,emp_address_l3=?,"
										+" emp_email=?, emp_phone=?,emp_pps=?, emp_start_date=?,"
										+" emp_department=?, emp_pay_type=?, emp_rate=? where emp_no=?");
			
			ps.setString(1,emp.getSurname());
			ps.setString(2,emp.getFirstName());
			ps.setString(3,emp.getDob());
			ps.setString(4,emp.getAddressL1());
			ps.setString(5,emp.getAddressL2());
			ps.setString(6,emp.getAddressL3());
			ps.setString(7,emp.getEmail());
			ps.setString(8,emp.getPhone());
			ps.setString(9,emp.getPps());
			ps.setString(10, emp.getStartDate());
			ps.setString(11, emp.getDepartment());
			ps.setString(12, emp.getPaymentType());
			if(emp instanceof hourlyEmployee)
			{
				hourlyEmployee he=(hourlyEmployee)emp;
				ps.setDouble(13, he.getRate());
			}
			else if(emp instanceof salariedEmployee)
			{
				salariedEmployee se=(salariedEmployee)emp;
				ps.setDouble(13, se.getSalary());
			}
			ps.setInt(14,emp.getEmpNo());
			ps.executeUpdate();
			System.out.println("Updated employee");
			
			ps = conn.prepareStatement("update revdetails set cut_off=?, tax_credit=?, prsi_class=? where emp_no=?");
			
			ps.setDouble(1, cutOff);
			ps.setDouble(2, taxCredit);
			ps.setString(3, prsi);
			ps.setInt(4,emp.getEmpNo());
			ps.executeUpdate();
			System.out.println("Updated revenue details");
			
			ps = conn.prepareStatement("update bankdetails set bank_name=?, account_no=?, sort_code=? where emp_no=?");
			
			ps.setString(1, bankName);
			ps.setString(2, accountNo);
			ps.setString(3, sortCode);
			ps.setInt(4, emp.getEmpNo());
			ps.executeUpdate();
			System.out.println("Updated bank details");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}	
	}
	
	public void insertEmployee(Employee emp)
	{
		try
		{
			ps = conn.prepareStatement("insert into employee(emp_no, emp_surname, emp_fname," +
										"emp_address_l1,emp_address_l2,emp_address_l3, emp_email, emp_phone," +
										"emp_dob,emp_pps,emp_start_date,emp_pay_type, emp_rate, emp_hourly) " +
										"values(emp_no_seq.nextval,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		
			ps.setString(1,emp.getSurname());
			ps.setString(2,emp.getFirstName());
			ps.setString(3,emp.getAddressL1());
			ps.setString(4,emp.getAddressL2());
			ps.setString(5,emp.getAddressL3());
			ps.setString(6,emp.getEmail());
			ps.setString(7,emp.getPhone());
			ps.setString(8,emp.getDob());
			ps.setString(9,emp.getPps());
			ps.setString(10, emp.getStartDate());
			ps.setString(11, emp.getPaymentType());
			if(emp instanceof hourlyEmployee)
			{
				hourlyEmployee he = (hourlyEmployee)emp;
				ps.setDouble(12,he.getRate());
				ps.setString(13, "Y");
			}
			else if(emp instanceof salariedEmployee)
			{
				salariedEmployee se = (salariedEmployee)emp;
				ps.setDouble(12, se.getSalary());
				ps.setString(13, "N");
			}
			ps.executeUpdate();
			System.out.println("Employee inserted.");
			
		}
		catch(SQLException sqle)
		{
			sqle.printStackTrace();
		}
	}
	
	public void ceaseEmployment(Employee emp)
	{
		try
		{
			ps = conn.prepareStatement("update employee set emp_finish_date=? where emp_no=?");
			ps.setString(1, emp.getFinishDate());
			ps.setInt(2, emp.getEmpNo());
			ps.executeUpdate();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			
		}
	}
	
	public void insertCompany(Company c)
	{
		try
		{
			//connect();
			ps = conn.prepareStatement("create table company(company_name varchar2(30),reg_no varchar2(20)," 
										+"start_week number(2))");
			ps.executeUpdate();
			System.out.println("Company table OK");
			
			ps = conn.prepareStatement("insert into company values(?,?,?)");
			ps.setString(1, c.getCompName());
			ps.setString(2, c.getCompRegNo());
			ps.setInt(3, c.getStartWeek());
			ps.executeUpdate();
			
			ps = conn.prepareStatement("create sequence user_seq start with 1 increment by 1");
			ps.executeUpdate();
			System.out.println("User sequence OK");
			
			ps = conn.prepareStatement("create table payroll_user (user_id varchar2(5),user_fname varchar2(20),"
										+"user_lname varchar2(30),user_alias varchar2(20),user_password varchar2(20),"
										+"primary key (user_id))");
			ps.executeUpdate();
			System.out.println("PayrollUser table OK");
			
			ps = conn.prepareStatement("insert into payroll_user values (user_seq.nextval,?,?,?,?)");
			ps.setString(1, "Maciej");
			ps.setString(2, "Macierzynski");
			ps.setString(3, "magic");
			ps.setString(4, "mac2012");
			ps.executeUpdate();
			System.out.println("User1 OK");
			
			ps.setString(1, "James");
			ps.setString(2, "Madden");
			ps.setString(3, "seamus");
			ps.setString(4, "mad2012");
			ps.executeUpdate();
			System.out.println("User2 OK");
			
			ps.setString(1, "Shane");
			ps.setString(2, "Murphy");
			ps.setString(3, "shane");
			ps.setString(4, "mur2012");
			ps.executeUpdate();
			System.out.println("User3 OK");
			
			ps = conn.prepareStatement("create sequence emp_no_seq start with 1 increment by 1");
			ps.executeUpdate();
			System.out.println("Employee sequence OK");
			
			ps = conn.prepareStatement("create table employee(emp_no  number(5) not null,emp_surname varchar2(30) not null,"
										+" emp_fname varchar2(30) not null,emp_address_l1 varchar2(50),"
										+"emp_address_l2 varchar2(50),emp_address_l3 varchar2(50),"
										+"emp_email varchar2(10),emp_phone varchar2(30),"
										+"emp_dob varchar2(10),emp_pps varchar2(10) not null,"
										+"emp_start_date varchar2(10),emp_finish_date varchar2(10),"
										+"emp_department varchar2(10),emp_pay_type varchar2(10),"
										+"emp_rate number(5) not null, emp_hourly char(1),"
										+"primary key (emp_no))");
			ps.executeUpdate();
			System.out.println("Employee table OK");
			
			ps = conn.prepareStatement("create table timesheet(emp_no number(5) not null,week_no number(2) not null,"
										+"monday varchar2(13),tuesday varchar2(13),wednsday varchar2(13),"
										+"thursday varchar2(13),friday varchar2(13),saturday varchar2(13),"
										+"sunday varchar2(13),tot_hours number(2),"
										+"foreign key(emp_no) references employee(emp_no))");
			ps.executeUpdate();
			System.out.println("Timesheet table OK");
			
			ps = conn.prepareStatement("create table weeklyPay(emp_no number(5),week_no number(2),"
										+"gross_pay number(10),net_pay number(10),tax_paid number(10),"
										+"prsi_paid number(10),usc_paid number(10)," 
										+"foreign key(emp_no) references employee(emp_no))");
			ps.executeUpdate();
			System.out.println("Weeklypay table OK");
			
			ps = conn.prepareStatement("create table totalPay(emp_no number(5),tot_gross_pay number(10),"
										+"tot_net_pay number(10),tot_tax_paid number(10),"
										+"tot_prsi_paid number(10),tot_usc_paid number(10),"
										+"foreign key (emp_no) references employee(emp_no))");
			ps.executeUpdate();
			System.out.println("TotalPay table OK");
			
			ps = conn.prepareStatement("create table revDetails(emp_no number(5),cut_Off number,"
										+"tax_Credit number,prsi_Class varchar2(10),"
										+"foreign key (emp_no) references employee(emp_no))");
			ps.executeUpdate();
			System.out.println("RevDetails table OK");
			
			ps = conn.prepareStatement("create table bankDetails(emp_no number(5),bank_name varchar2(20),"
										+"account_no varchar2(20),sort_code varchar2(20),"
										+"foreign key (emp_no) references employee(emp_no))");
			ps.executeUpdate();
			System.out.println("BankDetails table OK");
			
			ps = conn.prepareStatement("create or replace trigger details_trigger after insert on employee"
										+" for each row "
										+" begin "
			+"insert into revdetails values (emp_no_seq.currval, 0.0,0.0, 'A');"
			+"insert into bankdetails values (emp_no_seq.currval, '','','');"
			+"insert into totalpay values (emp_no_seq.currval, 0.0, 0.0,0.0,0.0,0.0);"
										+" end;");
			ps.executeUpdate();
			System.out.println("Trigger OK");
		}
		catch (SQLException e)
		{
			//disconnect();
			e.printStackTrace();
		}
	}
	
	public void deleteCompany()
	{
		try
		{
			connect();
			ps = conn.prepareStatement("drop table timesheet");
			ps.executeUpdate();
			System.out.println("Timesheet table dropped.");
			
			ps = conn.prepareStatement("drop table revdetails");
			ps.executeUpdate();
			System.out.println("RevDetails table dropped.");
			
			ps = conn.prepareStatement("drop table bankdetails");
			ps.executeUpdate();
			System.out.println("BankDetails table dropped.");
			
			ps = conn.prepareStatement("drop table weeklypay");
			ps.executeUpdate();
			System.out.println("WeeklyPay table dropped.");
			
			ps = conn.prepareStatement("drop table totalpay");
			ps.executeUpdate();
			System.out.println("TotalPay table dropped.");
			
			ps = conn.prepareStatement("drop table company");
			ps.executeUpdate();
			System.out.println("Company table dropped.");
			
			ps = conn.prepareStatement("drop table employee");
			ps.executeUpdate();
			System.out.println("Employee table dropped.");
			
			ps = conn.prepareStatement("drop sequence emp_no_seq");
			ps.executeUpdate();
			System.out.println("Employee sequence dropped.");
			
			ps = conn.prepareStatement("drop table payroll_user");
			ps.executeUpdate();
			System.out.println("User table dropped.");
			
			ps = conn.prepareStatement("drop sequence user_seq");
			ps.executeUpdate();
			System.out.println("User sequence dropped.");
			
//			ps = conn.prepareStatement("drop trigger details_trigger");
//			ps.executeUpdate();
//			System.out.println("Trigger dropped.");
		}
		catch (SQLException e)
		{
			//disconnect();
			e.printStackTrace();
		}
	}
	
	public ResultSet getReport(String type, int num)
	{
		if(type.equalsIgnoreCase("w"))
		{
			try
			{
				//connect();
				ps = conn.prepareStatement("select week_no, sum(gross_pay), " +
											"sum(net_pay), sum(tax_paid), sum(prsi_paid), " +
											"sum(usc_paid) from weeklypay where week_no=? group by week_no");
				ps.setInt(1, num);
				rs = ps.executeQuery();
				return rs;
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
		else if(type.equalsIgnoreCase("p45"))
		{
			try
			{
				//connect();
				ps = conn.prepareStatement("select count(week_no), sum(gross_pay), " +
											"sum(net_pay), sum(tax_paid), sum(prsi_paid), " +
											"sum(usc_paid) from weeklypay where emp_no=? group by emp_no");
				ps.setInt(1, num);
				rs = ps.executeQuery();
				return rs;
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
		else if(type.equalsIgnoreCase("p60"))
		{
			try
			{
				ps = conn.prepareStatement("select count(week_no), sum(gross_pay), " +
											"sum(net_pay), sum(tax_paid), sum(prsi_paid), " +
											"sum(usc_paid) from weeklypay where emp_no=? group by emp_no");
				ps.setInt(1, num);
				rs = ps.executeQuery();
				return rs;
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
		else if(type.equalsIgnoreCase("p30"))
		{
			try
			{
				ps = conn.prepareStatement("select sum(tot_tax_paid), sum(tot_prsi_paid)" +
											"from totalpay");
				rs = ps.executeQuery();
				return rs;
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
		else if(type.equalsIgnoreCase("p35"))
		{
			try
			{
				ps = conn.prepareStatement("select sum(tot_tax_paid), sum(tot_prsi_paid)" +
											"from totalpay");
				rs = ps.executeQuery();
				return rs;
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
		return rs;
	}
}
