package helper;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import beanClasses.User;
import hibernate.CreateDBConnection;

public class AuthenticateUser
{
	public static boolean authenticate(User user)
	{
		SessionFactory sessionFactory = CreateDBConnection.getSessionFac();
		
		Session session = sessionFactory.openSession();
		System.out.println(user.getUserId().toLowerCase());
		User authUser = (User)session.get(User.class, user.getUserId().toLowerCase());
		
		if( authUser != null )
		{
			if( user.getPassword().equals(authUser.getPassword()) )
			{
				return true;
			}
		}
		return false;
	}
}
