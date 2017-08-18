package hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class CreateDBConnection
{
	private static SessionFactory sessionFactory;
	
	static
	{
		sessionFactory = new Configuration().configure("hibernate.cfg.xml")
				.buildSessionFactory();
	}
	
	public static Session getSession()
	{
		return sessionFactory.openSession();
	}
}
