package hibernate;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import beanClasses.ClientStatus;
import beanClasses.User;

public class DBUtil
{
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static LinkedHashMap<String, LinkedHashMap<String, ArrayList<ClientStatus>>> getAllRegisteredClients()
	{
		SessionFactory sessionFactory = CreateDBConnection.getSessionFac();
		Session session = sessionFactory.openSession();
		
		Query query = session.createQuery("from beanClasses.User");
		List<User> userAll = query.list();
		
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
			session.close();
			return clientStatusMap;
		}
		else
		{
			session.close();
			return null;
		}
	}
}
