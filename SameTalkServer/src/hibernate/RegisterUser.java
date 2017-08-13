package hibernate;

import javax.persistence.PersistenceException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import beanClasses.User;

public class RegisterUser
{
	public static void registerNewUser(User user) throws PersistenceException
	{
		SessionFactory sessionFactory = CreateDBConnection.getSessionFac();
		
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		session.save(user);
		session.getTransaction().commit();
		session.close();
	}
}
