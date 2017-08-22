package hibernate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import javax.swing.table.DefaultTableModel;

import org.hibernate.Session;
import org.hibernate.query.Query;

import beanClasses.ClientStatus;
import beanClasses.User;
import helper.Util;
import serverMainClasses.ThreadDeleteEmployee;

public class DBUtil
{	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static LinkedHashMap<String, LinkedHashMap<String, ArrayList<ClientStatus>>> getAllRegisteredClients(boolean usersOnly)
	{
		Session session = CreateDBConnection.getSession();
		
		Query query = session.createQuery("from beanClasses.User");
		List<User> userAll = query.list();
		
		session.close();
		
		Util.allUsers.addAll(userAll);
		
		if( usersOnly )
			return null;
		
		if( userAll != null && userAll.size() > 0 )
		{
			LinkedHashMap<String, LinkedHashMap<String, ArrayList<ClientStatus>>> clientStatusMap = 
					new LinkedHashMap<>();
			
			for(User user : userAll)
			{
				ClientStatus cs = new ClientStatus();
				cs.setClientId(user.getUserId());
				cs.setClientName(user.getUserName());
				cs.setDepartment(user.getDepartment().toLowerCase());
				cs.setPosition(user.getPosition().toLowerCase());
				cs.setClientStatus(ClientStatus.OFFLINE);
				
				if( !clientStatusMap.containsKey(cs.getDepartment()) )
				{
					ArrayList<ClientStatus> arcs = new ArrayList<>();
					arcs.add(cs);
					LinkedHashMap<String, ArrayList<ClientStatus>> lkhm = new LinkedHashMap<>();
					lkhm.put(cs.getPosition(), arcs);
					clientStatusMap.put(cs.getDepartment(), lkhm);
				}
				else
				{
					LinkedHashMap<String, ArrayList<ClientStatus>> lhm = 
							clientStatusMap.get(cs.getDepartment());
					if( !lhm.containsKey(cs.getPosition()) )
					{
						ArrayList<ClientStatus> arcs = new ArrayList<>();
						arcs.add(cs);
						lhm.put(cs.getPosition(), arcs);
					}
					else
					{
						ArrayList<ClientStatus> ar = lhm.get(cs.getPosition());
						ar.add(cs);
					}
				}
			}
			return clientStatusMap;
		}
		else
			return null;
	}
	
	public static boolean removeUsers(ArrayList<String> all, DefaultTableModel tableModel)
	{
		boolean deletedFalg = false;
		
		System.out.println("Inside function removeUsers");
		Session session = CreateDBConnection.getSession();
		session.beginTransaction();
		
		for(User user : Util.allUsers)
		{
			Iterator<String> idIt = all.iterator();
			
			System.out.println("Checking "+user.getUserId());
			while( idIt.hasNext() )
			{
				System.out.println("idIt contains user");
				String id = idIt.next();
				if( user.getUserId().equalsIgnoreCase(id) )
				{
					session.delete(user);
					System.out.println("Removed "+user.getUserId());
					deletedFalg = true;
					ThreadDeleteEmployee.keepGoing = false;
					break;
				}
			}
		}
		
		session.getTransaction().commit();
		session.close();
		
		if( deletedFalg == true )
		{
			Util.allUsers.clear();
			DBUtil.getAllRegisteredClients(true);
			ThreadDeleteEmployee tDele = new ThreadDeleteEmployee(tableModel, false);
			tDele.start();
		}
		
		return deletedFalg;
	}

	public static User getUser(String id)
	{
		Session session = CreateDBConnection.getSession();
		User reUser = (User)session.get(User.class, id);
		session.close();
		return reUser;
	}

	public static void updateUser(User updatedUser)
	{
		Session session = CreateDBConnection.getSession();
		session.beginTransaction();
		session.update(updatedUser);
		session.getTransaction().commit();
		System.out.println("Commited changes");
		session.close();
	}
}