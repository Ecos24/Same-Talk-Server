package hibernate;

import javax.persistence.PersistenceException;

import org.hibernate.Session;

import beanClasses.User;

public class RegisterUser
{
	public static void registerNewUser(User user) throws PersistenceException
	{
		Session session = CreateDBConnection.getSession();
		session.beginTransaction();
		session.save(user);
		session.getTransaction().commit();
		session.close();
	}
}
