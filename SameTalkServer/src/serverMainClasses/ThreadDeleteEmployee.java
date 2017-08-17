package serverMainClasses;

import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.table.DefaultTableModel;

import beanClasses.User;
import helper.Util;

public class ThreadDeleteEmployee extends Thread
{
	public boolean keepGoing = true;
	private ArrayList<User> allUsersSelfCopy = new ArrayList<>();
	private DefaultTableModel tableModel;

	public ThreadDeleteEmployee(DefaultTableModel tableModel)
	{
		super();
		copyArrayList(Util.allUsers);
		this.tableModel = tableModel;
	}

	@Override
	public void run()
	{
		while(keepGoing)
		{
			if(checkChange(allUsersSelfCopy, Util.allUsers))
			{
				updateTable(allUsersSelfCopy, tableModel);
			}
		}
	}
	
	private void copyArrayList(ArrayList<User> allU)
	{
		allUsersSelfCopy.clear();
		for(User user : allU)
		{
			User u = new User();
			u.setDepartment(user.getDepartment());
			u.setUserId(user.getUserId());
			u.setUserName(user.getUserName());
			allUsersSelfCopy.add(u);
		}
	}
	
	private void updateTable(ArrayList<User> all, DefaultTableModel model)
	{
		for(User user : all)
		{
			Object[] row = new Object[4];
			row[0] = false;
			row[1] = user.getUserId();
			row[2] = user.getUserName();
			row[3] = user.getDepartment();
			
			model.addRow(row);
		}
	}
	
	private boolean checkChange(ArrayList<User> self, ArrayList<User> main)
	{
		Iterator<User> selfIt = self.iterator();
		for(User user : main)
		{
			if( user.getUserId().toLowerCase().equals(selfIt.next().getUserId().toLowerCase()) )
				return true;
		}
		return false;
	}
}
