package serverMainClasses;

import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.table.DefaultTableModel;

import beanClasses.User;
import helper.Util;

public class ThreadDeleteEmployee extends Thread
{
	public static boolean keepGoing = true;
	private ArrayList<User> allUsersSelfCopy = new ArrayList<>();
	private DefaultTableModel tableModel;
	private boolean viewEditFlag;

	public ThreadDeleteEmployee(DefaultTableModel tableModel, boolean flag)
	{
		super();
		copyArrayList(Util.allUsers);
		this.tableModel = tableModel;
		this.viewEditFlag = flag;
	}

	@Override
	public void run()
	{
		updateTable(allUsersSelfCopy, tableModel);
		
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
		model.setRowCount(0);
		
		for(User user : all)
		{
			Object[] row = new Object[4];;
			if( viewEditFlag == false )
			{
				row[0] = false;
				row[1] = user.getUserId();
				row[2] = user.getUserName();
				row[3] = user.getDepartment();
			}
			else
			{
				row[0] = user.getUserId();
				row[1] = user.getUserName();
				row[2] = user.getDepartment();
				row[3] = row[0];
			}
			
			model.addRow(row);
		}
	}
	
	private boolean checkChange(ArrayList<User> self, ArrayList<User> main)
	{
		Iterator<User> selfIt = self.iterator();
		
		if( self.size() > 0 && main.size() > 0 )
		{
			if( self.size() != main.size() )
				return true;
			
			for(User user : main)
			{
				if( selfIt.hasNext() )
				{
					if( !user.getUserId().equalsIgnoreCase(selfIt.next().getUserId()) )
						return true;
				}
			}
		}
		return false;
	}
}
