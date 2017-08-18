package hibernate;

import org.hibernate.Session;

import beanClasses.User;
import helper.Util;

public class AuthenticateUser
{
	public static void initHibernate(Util util)
	{
		util.updateServerStatus(Util.STATUS_BUSY);
		
		Session session = CreateDBConnection.getSession();
		session.close();
		
		util.updateServerStatus(Util.STATUS_READY);
	}
	
	public static User authenticate(User user, Util util)
	{
		util.updateServerStatus(Util.STATUS_BUSY);
		
		Session session = CreateDBConnection.getSession();
		User authUser = (User)session.get(User.class, user.getUserId().toLowerCase());
		
		if( authUser != null )
		{
			authUser.setDepartment(authUser.getDepartment().toLowerCase());
			authUser.setPosition(authUser.getPosition().toLowerCase());
			if( user.getPassword().equals(authUser.getPassword()) )
			{
				util.updateServerStatus(Util.STATUS_READY);
				System.out.println(authUser.getUserName());
				return authUser;
			}
		}
		util.updateServerStatus(Util.STATUS_READY);
		return null;
	}
}
