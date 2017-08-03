package serverMainClasses;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import beanClasses.ClientStatus;
import helper.AuthenticateUser;
import helper.ChatMessage;
import helper.Util;
import hibernate.DBUtil;

public class Server
{
	// Reference to store ServerConnection with specified socket.
	ServerSocket serverSocket;
	
	// a unique ID for each connection
	private static int uniqueId;
	// an ArrayList to keep the list of the Client
	static ArrayList<ServersClientThread> clientsList;
	// ArrayList to hold all client Status.
	static ArrayList<ClientStatus> clientStatusList;
	// the port number to listen for connection
	private int port;
	// the boolean that will be turned of to stop the server
	private boolean keepGoing;
	// Object of Util To display Events & Other
	private static Util util;
	public static Util getUtil()
	{
		return util;
	}

	public Server(int port, JTextArea displayEvent, DefaultTableModel connectedClientTableModel,
			JTextField serverDetails, JTextField serverStatusText)
	{
		// the port
		this.port = port;
		// ArrayList for the Client list
		clientsList = new ArrayList<ServersClientThread>();
		// Pass GUI elements references to Util class.
		util = new Util(displayEvent, connectedClientTableModel, serverDetails, serverStatusText);
	}
	
	/**
	 * This function create a connection on given port number of class constructors.
	 * @throws IOException
	 */
	public void initServer() throws IOException
	{
		// Create socket server
		serverSocket = new ServerSocket(port);
		util.updateServerStatus(Util.STATUS_READY);
		
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				AuthenticateUser.initHibernate(util);
				clientStatusList = DBUtil.getAllRegisteredClients();
			}
		}).start();
	}
	
	/**
	 * Main function to Start the Server.
	 * @throws IOException
	 */
	public void start() throws IOException
	{
		keepGoing = true;

		// infinite loop to wait for connections
		while(keepGoing)
		{
			// format message saying we are waiting
			util.updateServerDetails("Server waiting for Clients on Port --> " + port + ".");

			Socket socket = serverSocket.accept(); // accept connection
			// if I was asked to stop
			if(!keepGoing)
				break;
			// make a thread of it
			ServersClientThread clientThread = new ServersClientThread(socket, ++uniqueId, util);
			if(clientThread.confirmUser())
			{
				clientsList.add(clientThread); // save it in the ArrayList
			}
			else
			{
				socket.close();
				continue;
			}
			clientThread.start();
		}

		// I was asked to stop
		util.updateServerStatus(Util.STATUS_FAILED);
		serverSocket.close();
		for(int i = 0; i < clientsList.size(); ++i)
		{
			ServersClientThread gettingClientThread = clientsList.get(i);
			gettingClientThread.close();
		}
	}
	
	/**
	 * To broadcast a message to all Clients
	 * @param message
	 */
	public static synchronized void broadCast(String message, ChatMessage chat, int id)
	{
		// Displaying busy Server Status.
		util.updateServerStatus(Util.STATUS_BUSY);
		
		// add HH:mm:ss and \n to the message
		String time = Util.sdf.format(new Date());
		String messageLf = time + "   " + message + "\n";
		// display message
		util.displayChat(messageLf);
		
		// we loop in reverse order in case we would have to remove a Client because it has disconnected.
		for(int i = clientsList.size(); --i >= 0;)
		{
			ServersClientThread ct = clientsList.get(i);
			// try to write to the Client if it fails remove it from the list
			if(!ct.writeMsg(messageLf, chat))
			{
				clientsList.remove(i);
				util.displayEvent("Disconnected Client " + ct.getClient().getUserId()+ " removed from list.");
			}
		}

		// Change Status back to Ideal.
		util.updateServerStatus(Util.STATUS_READY);
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
	
	/**
	 * For all client's when closing server.
	 */
	public synchronized void destroy()
	{
		// Scan the array list until we found the Id
		for(int i = 0; i < clientsList.size(); ++i)
		{
			ServersClientThread ct = clientsList.get(i);
			ct.keepGoing = false;
			clientsList.remove(i);
		}
	}
}