package serverMainClasses;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;

import helper.ChatMessage;
import helper.Util;

public class Server
{
	// a unique ID for each connection
	private static int uniqueId;
	// an ArrayList to keep the list of the Client
	public static ArrayList<ServersClientThread> clientsList;
	// the port number to listen for connection
	private int port;
	// the boolean that will be turned of to stop the server
	private boolean keepGoing;
	
	public Server(int port)
	{
		// the port
		this.port = port;
		// ArrayList for the Client list
		clientsList = new ArrayList<ServersClientThread>();
	}
	
	/**
	 * To run as a console application just open a console window and:
	 * > java Server
	 * > java Server portNumber
	 * If the port number is not specified 4501 is used
	 * @param args
	 */
	public static void main(String[] args)
	{
		// start server on port 1500 unless a PortNumber is specified
		int portNumber = 4501;
		
		switch(args.length)
		{
			case 1:
				try
				{
					portNumber = Integer.parseInt(args[0]);
				}
				catch(NumberFormatException e)
				{
					System.out.println("Invalid port number.");
					System.out.println("Usage is: > java Server [portNumber]");
					return;
				}
			case 0:
				break;
			default:
				System.out.println("Usage is: > java Server [portNumber]");
				return;
		}
		// create a server object and start it
		Server server = new Server(portNumber);
		server.start();
	}
	
	public void start()
	{
		keepGoing = true;
		/* create socket server and wait for connection requests */
		try
		{
			// the socket used by the server
			ServerSocket serverSocket = new ServerSocket(port);
			
			// infinite loop to wait for connections
			while(keepGoing)
			{
				// format message saying we are waiting
				Util.displayEvent("Server waiting for Clients on Port --> "+port+".");
				
				Socket socket = serverSocket.accept();      // accept connection
				// if I was asked to stop
				if(!keepGoing)
					break;
				ServersClientThread clientThread = new ServersClientThread(socket,++uniqueId);  // make a thread of it
				if( clientThread.confirmUser() )
					clientsList.add(clientThread);	// save it in the ArrayList
				else
				{
					socket.close();
					continue;
				}
				clientThread.start();
			}
			
			// I was asked to stop
			serverSocket.close();
			for(int i = 0; i < clientsList.size(); ++i)
			{
				ServersClientThread gettingClientThread = clientsList.get(i);
				gettingClientThread.close();
			}
		}
		// something went bad
		catch(IOException e)
		{
			Util.displayEvent(e.getClass().getName()+" Exception on new ServerSocket: "+e.getMessage());
		}
	}
	
	/**
	 * To broadcast a message to all Clients
	 * @param message
	 */
	public static synchronized void broadCast(String message, ChatMessage chat, int id)
	{
		// add HH:mm:ss and \n to the message
		String time = Util.sdf.format(new Date());
		String messageLf = time + "   " + message + "\n";
		// display message
		System.out.print(messageLf);
		
		// we loop in reverse order in case we would have to remove a Client because it has disconnected.
		for(int i = clientsList.size(); --i >= 0;)
		{
			ServersClientThread ct = clientsList.get(i);
			// try to write to the Client if it fails remove it from the list
			if(!ct.writeMsg(messageLf, chat))
			{
				clientsList.remove(i);
				Util.displayEvent("Disconnected Client " + ct.getClient().getUserId()+ " removed from list.");
			}
		}
	}

	/**
	 * For a client who log's off using the LOGOUT message
	 * @param id
	 */
	public static synchronized void remove(int id)
	{
		// Scan the array list until we found the Id
		for(int i = 0; i < clientsList.size(); ++i)
		{
			ServersClientThread ct = clientsList.get(i);
			// found it
			if(ct.getClientId() == id)
			{
				clientsList.remove(i);
				return;
			}
		}
	}
}