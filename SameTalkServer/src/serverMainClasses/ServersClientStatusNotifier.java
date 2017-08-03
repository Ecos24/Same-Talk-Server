package serverMainClasses;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

import beanClasses.ClientStatus;

/**
 * Class functions that provide functionalities to notify client about all other Client's Status.
 * @author ecos
 */
public class ServersClientStatusNotifier
{	
	private ObjectOutputStream clientOutputStream;
	private ArrayList<ClientStatus> currentClientStatusList;
	
	/**
	 * Constructor to initiate outputStream & currentClientStatusList
	 * @param clientSocket
	 * @param clientOutputStream
	 * @param currentClientStatusList
	 * @throws IOException 
	 */
	public ServersClientStatusNotifier(Socket clientSocket, ObjectOutputStream clientOutputStream,
			ArrayList<ClientStatus> currentClientStatusList) throws IOException
	{
		super();
		this.clientOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
		this.currentClientStatusList = currentClientStatusList;
		
		// Write currentClientStatus List to user When requested.
		while(true)
		{
			try
			{
				System.out.println("Writing currentClientStatus List to Stream for first time!! by "+this.getClass().getName());
				clientOutputStream.writeObject(currentClientStatusList);
				clientOutputStream.flush();
				break;
			}
			catch(IOException e)
			{
				System.out.println(this.getClass().getName()+" Exception --> "+e.getClass().getName()+" - "+e.getMessage());
				e.printStackTrace();
				continue;
			}
		}
	}

	/**
	 * Function to notify Client about Current Status of other Clients.
	 */
	public void notifyClientAboutStatus()
	{
		if( clientsStatusChange() )
		{
			try
			{
				System.out.println("Writing new Client Status List!");
				clientOutputStream.writeObject(currentClientStatusList);
				clientOutputStream.flush();
			}
			catch(IOException e)
			{
				System.out.println(this.getClass().getName()+" Exception "+e.getClass().getName()+
						" writing Status List to outputStream --> "+e.getMessage());
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings("rawtypes")
	private boolean clientsStatusChange()
	{
		if( (Server.clientStatusList != null && currentClientStatusList != null) && 
				( Server.clientStatusList.size() > 0 && currentClientStatusList.size() > 0) )
		{
			Iterator currentListIterator = currentClientStatusList.iterator();
			Iterator clientStatusListIterator = Server.clientStatusList.iterator();
			while( currentListIterator.hasNext() && clientStatusListIterator.hasNext() )
			{
				ClientStatus currentClientStatus = (ClientStatus) currentListIterator.next();
				ClientStatus clientStatus = (ClientStatus) clientStatusListIterator.next();
				if( currentClientStatus.getClientStatus().equals(clientStatus.getClientStatus()))
				{
					System.out.println(this.getClass().getName()+" returning false in clientsStatusChange. ");
					return false;
				}
			}
			System.out.println(this.getClass().getName()+" returning true in clientsStatusChange. ");
			return true;
		}
		System.out.println(this.getClass().getName()+" returning false in clientsStatusChange. ");
		return false;
	}
}
