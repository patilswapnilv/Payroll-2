package payroll;

import javax.swing.table.AbstractTableModel;

public class TimeSheetModel extends AbstractTableModel
{
	String[] columnNames = {null,"Mon",
            "Tue",
            "Wed",
            "Thu",
            "Fri",
            "Sat",
            "Sun"};
	
	Object[][] data = {
			{"Start time", null,null,null,null,null, null,null},
			{"Finish time",null,null,null,null,null, null, null},
			{"Holiday", null,null,null,null,null, null, null},
			{"Expenses", null,null,null,null,null, null,null}
			};
	
	
	@Override
	public int getColumnCount()
	{
		return columnNames.length;
	}

	@Override
	public int getRowCount()
	{
		return data.length;
	}

	@Override
	public Object getValueAt(int row, int col)
	{
		return data[row][col];
	}
	
	public void setValueAt(Object o, int row, int col)
	{
		data[row][col] =o;
		fireTableCellUpdated(row,col);
	}
	public String getColumnName(int col)
	{
		return columnNames[col];
	}
	public boolean isCellEditable(int row, int col)
	{
		return true;
	}
}
