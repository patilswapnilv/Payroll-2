package payroll;

import java.util.ArrayList;
import java.util.List;

import javax.swing.MutableComboBoxModel;
import javax.swing.event.ListDataListener;

public class CbModel implements MutableComboBoxModel
{
	private List<String> emps;
	private Object o = null;
	
	public CbModel(List<String> al)
	{
		this.emps = al;
	}

	@Override
	public void addListDataListener(ListDataListener arg0)
	{
	
	}

	@Override
	public Object getElementAt(int index)
	{
		return this.emps.get(index);
	}

	@Override
	public int getSize()
	{
		return this.emps.size();
	}

	@Override
	public void removeListDataListener(ListDataListener arg0)
	{
		
	}

	@Override
	public Object getSelectedItem()
	{
		return this.o;
	}

	@Override
	public void setSelectedItem(Object anItem)
	{
		this.o=anItem;
	}

	@Override
	public void addElement(Object anItem)
	{
		this.emps.add((String)anItem);
	}

	@Override
	public void insertElementAt(Object anItem, int index)
	{
		this.emps.add(index, (String)anItem);
	}

	@Override
	public void removeElement(Object arg0)
	{
		
	}

	@Override
	public void removeElementAt(int index)
	{
		
	}
}
