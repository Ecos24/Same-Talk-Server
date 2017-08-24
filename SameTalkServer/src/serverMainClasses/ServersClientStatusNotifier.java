package serverMainClasses;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;

import beanClasses.ClientStatus;
import helper.UtilClient;

/**
 * Class functions that provide functionalities to notify client about all other Client's Status.
 * @author ecos
 */
public class ServersClientStatusNotifier
{	
	private ObjectOutputStream clientOutputStream;
	private LinkedHashMap<String, LinkedHashMap<String,ArrayList<ClientStatus>>> currentClientStatusList;
	private String userId;
	private UtilClient utilClient;
	private Socket clientsSocket;
	
	/**
	 * Constructor to initiate outputStream & currentClientStatusList
	 * @param clientSocket
	 * @param clientOutputStream
	 * @param currentClientStatusList
	 * @throws IOException 
	 */
	public ServersClientStatusNotifier(ObjectOutputStream clientOutputStream, 
			LinkedHashMap<String, LinkedHashMap<String,ArrayList<ClientStatus>>> currentClientStatusList,
			String clientId, UtilClient utilClient, Socket mainsocket)
	{
		super();
		// This line creating problem But Why??????????????????????????????????????
		this.clientOutputStream = clientOutputStream;
		this.currentClientStatusList = currentClientStatusList;
		this.userId = clientId;
		this.utilClient = utilClient;
		this.clientsSocket = mainsocket;
		
		// Write currentClientStatus List to user When requested.
		try
		{
			clientOutputStream.writeObject(currentClientStatusList);
			clientOutputStream.flush();
		}
		catch(IOException e)
		{
			System.out.println(this.getClass().getName()+" Exception --> "+e.getClass().getName()+" - "+e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Function to notify Client about Current Status of other Clients.
	 */
	public void notifyClientAboutStatus()
	{
		if( clientsStatusChange() && clientsSocket.isConnected() )
		{
			try
			{
				System.out.println("Writing updated client status list to --> "+userId);
				this.currentClientStatusList = utilClient.copyLinkedHashMap(Server.clientStatusList);
				clientOutputStream.writeObject(this.currentClientStatusList);
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

	private boolean clientsStatusChange()
	{
		if( (Server.clientStatusList != null && this.currentClientStatusList != null) && 
				( Server.clientStatusList.size() > 0 && this.currentClientStatusList.size() > 0) )
		{			
			Iterator<LinkedHashMap<String, ArrayList<ClientStatus>>> itcsl = 
					this.currentClientStatusList.values().iterator();
			Iterator<LinkedHashMap<String, ArrayList<ClientStatus>>> itcslMain = 
					Server.clientStatusList.values().iterator();
			while(itcsl.hasNext() && itcslMain.hasNext())
			{
				Iterator<ArrayList<ClientStatus>> tl = itcsl.next().values().iterator();
				Iterator<ArrayList<ClientStatus>> tlMain = itcslMain.next().values().iterator();
				while( tl.hasNext() && tlMain.hasNext() )
				{
					Iterator<ClientStatus> te = tl.next().iterator();
					Iterator<ClientStatus> teMain = tlMain.next().iterator();
					while( te.hasNext() && teMain.hasNext() )
					{
						ClientStatus cs = te.next();
						ClientStatus csMain = teMain.next();
						if( !cs.getClientStatus().equals(csMain.getClientStatus()) )
						{
							return true;
						}
					}
				}
			}
			return false;
		}
		return false;
	}
}
