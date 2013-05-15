package payroll;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.MutableComboBoxModel;
import javax.swing.event.ListDataListener;

public class ListComboBoxModel implements MutableComboBoxModel {
	protected ArrayList data;
	
	public ListComboBoxModel(ArrayList list) {
		this.listeners = new ArrayList();
		this.data = list;
		if(list.size() > 0) {
			selected = list.get(0);
		}
	}
	
	protected Object selected;
	public void setSelectedItem(Object item) {
		this.selected = item;
	}
	public Object getSelectedItem() {
		return this.selected;
	}

	public Object getElementAt(int index) {
		return data.get(index);
	}
	public int getSize() {
		return data.size();
	}

	protected ArrayList listeners;
	public void addListDataListener(ListDataListener l) {
		listeners.add(l);
	}
	public void removeListDataListener(ListDataListener l) {
		this.listeners.remove(l);
	}
	@Override
	public void addElement(Object arg0)
	{
		data.add(arg0);
		
	}
	@Override
	public void insertElementAt(Object arg0, int arg1)
	{
		// TODO Auto-generated method stub
		
	}
	@Override
	public void removeElement(Object arg0)
	{
		// TODO Auto-generated method stub
		
	}
	@Override
	public void removeElementAt(int arg0)
	{
		// TODO Auto-generated method stub
		
	}
}
