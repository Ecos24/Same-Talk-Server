package hibernate;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import beanClasses.ClientStatus;
import beanClasses.User;

public class DBUtil
{
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static ArrayList<ClientStatus> getAllRegisteredClients()
	{
		SessionFactory sessionFactory = CreateDBConnection.getSessionFac();
		Session session = sessionFactory.openSession();
		
		Query query = session.createQuery("from beanClasses.User");
		List<User> userAll = query.list();
		
		if( userAll != null && userAll.size() > 0 )
		{
			ArrayList<ClientStatus> clientStatusAll = new ArrayList<>();
			for(User user : userAll)
			{
				ClientStatus cs = new ClientStatus();
				cs.setClientId(user.getUserId());
				cs.setClientStatus(ClientStatus.OFFLINE);
				clientStatusAll.add(cs);
			}
			return clientStatusAll;
		}
		else
		{
			session.close();
			return null;
		}
	}
}
