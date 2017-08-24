package serverMainClasses;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import beanClasses.ClientStatus;
import beanClasses.User;
import helper.UtilClient;

public class ThreadServersClientStatusNotifier extends Thread
{
	private ServersClientStatusNotifier serverNotifier = null;
	private Socket clientsSocket;
	private boolean keepGoing = true;
	public void setKeepGoing(boolean keepGoing)
	{
		this.keepGoing = keepGoing;
	}
	
	public ThreadServersClientStatusNotifier(ObjectOutputStream clientOutputStream, User client,
			LinkedHashMap<String, LinkedHashMap<String,ArrayList<ClientStatus>>> currentClientStatusList,
			UtilClient utilClient, Socket mainsocket)
	{
		this.clientsSocket = mainsocket;
		this.serverNotifier  =
				new ServersClientStatusNotifier(clientOutputStream, currentClientStatusList,
						client.getUserId(), utilClient, mainsocket);
	}
	
	@Override
	public void run()
	{
		super.run();
		while(keepGoing && clientsSocket.isConnected())
		{
			// Notify Client about Status of others.
			if( serverNotifier != null )
			{
				serverNotifier.notifyClientAboutStatus();
			}
		}
	}
}
