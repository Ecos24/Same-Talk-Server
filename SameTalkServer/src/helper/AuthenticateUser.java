package helper;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import beanClasses.User;
import hibernate.CreateDBConnection;

public class AuthenticateUser
{
	public static void initHibernate(Util util)
	{
		util.updateServerStatus(Util.STATUS_BUSY);
		SessionFactory sessionFactory = CreateDBConnection.getSessionFac();
		Session session = sessionFactory.openSession();
		session.close();
		util.updateServerStatus(Util.STATUS_READY);
	}
	
	public static User authenticate(User user, Util util)
	{
		util.updateServerStatus(Util.STATUS_BUSY);
		SessionFactory sessionFactory = CreateDBConnection.getSessionFac();
		
		Session session = sessionFactory.openSession();
		User authUser = (User)session.get(User.class, user.getUserId().toLowerCase());
		
		if( authUser != null )
		{
			authUser.setDepartment(authUser.getDepartment().toLowerCase());
			authUser.setPosition(authUser.getPosition().toLowerCase());
			if( user.getPassword().equals(authUser.getPassword()) )
			{
				session.close();
				util.updateServerStatus(Util.STATUS_READY);
				return authUser;
			}
		}
		session.close();
		util.updateServerStatus(Util.STATUS_READY);
		return null;
	}
}
