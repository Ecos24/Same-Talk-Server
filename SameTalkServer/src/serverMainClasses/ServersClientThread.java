package serverMainClasses;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;

import beanClasses.ClientStatus;
import beanClasses.User;
import helper.AuthenticateUser;
import helper.ChatMessage;
import helper.Util;

public class ServersClientThread extends Thread
{
	public boolean keepGoing = true;
	
	private Socket clientSocket;
	private ObjectInputStream clientInputStream;
	private ObjectOutputStream clientOutputStream;
	
	// Unique id for Client.
	int clientId;
	// Clients name.
	User client;
	// Chat Message from client
	ChatMessage chatMessage;
	// Date of connection
	String date;
	// Reference for UtilObject.
	Util util;
	
	public int getClientId()
	{
		return clientId;
	}
	public void setClientId(int id)
	{
		this.clientId = id;
	}
	public User getClient()
	{
		return client;
	}
	public void setClient(User user)
	{
		this.client = user;
	}

	public ServersClientThread(Socket clientSocket, int id, Util util)
	{
		super();
		this.clientSocket = clientSocket;
		this.clientId = id;
		this.util = util;
		
		// Creating both Data Stream
		try
		{
			// Creating Input Stream
			clientInputStream = new ObjectInputStream(clientSocket.getInputStream());
			// Creating Output Stream
			clientOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
			
			// Read the UserName
			System.out.println("Reading User");
			this.client = (User) clientInputStream.readObject();
		}
		catch(IOException | ClassNotFoundException e)
		{
			util.displayEvent("Exception occured while creating Client Thread --> "+e.getMessage());
			return;
		}
		date = new Date().toString();
	}
	
	/**
	 * Confirms the User details received from Client.
	 * @return
	 */
	public boolean confirmUser()
	{
		try
		{
			if( AuthenticateUser.authenticate(this.client, this.util) )
			{
				clientOutputStream.writeObject(this.client);
				clientOutputStream.flush();
				
				updateClientStatus(client.getUserId());
				
				util.displayEvent(client.getUserId() + " just Connected");
				util.updateConnectedClientsTable(client.getUserId());
				return true;
			}
			else
			{
				if( this.client != null )
					util.displayEvent(client.getUserId() + " cannot be authenticated");
				
				return false;
			}
		}
		catch( IOException e )
		{
			if( this.client != null )
				util.displayEvent(e.getClass().getName()+" Exception occured while authenticating "+
					client.getUserId() + "--> "+e.getMessage());
			
			return false;
		}
	}
	
	private void updateClientStatus(String userId)
	{
		for(ClientStatus clientStatus : Server.clientStatusList)
		{
			if( clientStatus.getClientId().equals(userId) )
			{
				ClientStatus cs = Server.clientStatusList.get(Server.clientStatusList.indexOf(clientStatus));
				cs.setClientStatus(ClientStatus.ONLINE);
				Server.clientStatusList.set(Server.clientStatusList.indexOf(clientStatus), cs);
			}
		}
	}
	
	// What will run Forever.
	@Override
	public void run()
	{
		super.run();
		
		// Start ListenerResponser for this client.
		ArrayList<ClientStatus> currentClientStatusList = null;
		if( Server.clientStatusList != null )
		{
			currentClientStatusList = new ArrayList<>();
			currentClientStatusList.addAll(Server.clientStatusList);
			for(ClientStatus clientStatus : currentClientStatusList)
			{
				System.out.println(clientStatus.getClientId() + " --> " + clientStatus.getClientStatus());
			}
		}
		// Initialize object for Status Notifier.
		ServersClientStatusNotifier serverNotifier = null;
		try
		{
			serverNotifier = new ServersClientStatusNotifier(clientSocket, clientOutputStream, currentClientStatusList);
		}
		catch(IOException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		// To loop until LogOut.
		Object obj;
		while(keepGoing)
		{
			try
			{
				if( !clientSocket.isConnected() )
				{
					break;
				}
				obj = clientInputStream.readObject();
				if( obj instanceof ChatMessage )
				{
					chatMessage = (ChatMessage) obj;
					instanceOfChatMessage(chatMessage);
				}
				else
					continue;
			}
			catch( ClassNotFoundException e )
			{
				continue;
			}
			catch( IOException e)
			{
				if( e.getClass().getName().equals("java.io.EOFException") )
				{
					util.displayEvent("Client Closed the Application");
					close();
					break;
				}
				util.displayEvent("Exception reading Stream --> "+e.getMessage());
				e.printStackTrace();
				break;
			}
			
			// Notify Client about Status of others.
			if( serverNotifier != null )
				serverNotifier.notifyClientAboutStatus();
		}
		// Remove myself from the arrayList containing the list of the connected Clients.
		Server.remove(clientId);
		close();
	}
	
	/**
	 * Function is called if the read object is instanceOf ChatMessage Class.
	 * @param chatMessage
	 */
	private void instanceOfChatMessage(ChatMessage chatMessage)
	{
		// Message
		String msg = chatMessage.getMessage();

		switch(chatMessage.getType())
		{
			case ChatMessage.MESSAGE:
				switch(chatMessage.getMsgTargetType())
				{
					case ChatMessage.MESSAGE_TARGET_BROADCAST:
						Server.broadCast(client.getUserId() + " : " + msg, chatMessage, clientId);
						break;

					case ChatMessage.MESSAGE_TARGET_GROUP:
						break;

					case ChatMessage.MESSAGE_TARGET_PERSONAL:
						break;

					default:
						util.displayEvent("Discarding Message as Type is not defined");
				}
				break;

			case ChatMessage.LOGOUT:
				util.displayEvent(client.getUserId() + " disconnected with a LOGOUT message.");
				keepGoing = false;
				break;

			case ChatMessage.WHOSETHERE:
				writeMsg("List of the users connected at " + Util.sdf.format(new Date()) + "\n", chatMessage);
				// Scan ArrayList for users connected
				for(int i = 0; i < Server.clientsList.size(); ++i)
				{
					ServersClientThread ct = Server.clientsList.get(i);
					writeMsg((i + 1) + ") " + ct.client.getUserId() + " since " + ct.date, chatMessage);
				}
				break;

			default:
				break;
		}
	}
	
	/**
	 * Try to close everything.
	 */
	public void close()
	{
		// try to close the connection
		try
		{
			if(clientOutputStream != null)
				clientOutputStream.close();
		}
		catch(IOException e)
		{
			util.displayEvent(e.getClass().getName()+"Exception occured while closing "+client.getUserId()+" Connection --> "+e.getMessage());
			return;
		}
		try
		{
			if(clientInputStream != null)
				clientInputStream.close();
		}
		catch(IOException e)
		{
			util.displayEvent(e.getClass().getName()+"Exception occured while closing "+client.getUserId()+" Connection --> "+e.getMessage());
			return;
		}
		try
		{
			if(clientSocket != null)
				clientSocket.close();
		}
		catch(IOException e)
		{
			util.displayEvent(e.getClass().getName()+"Exception occured while closing "+client.getUserId()+" Connection --> "+e.getMessage());
			return;
		}
	}

	/**
	 * Write a String to the Client output stream
	 */
	public boolean writeMsg(String msg, ChatMessage chat)
	{
		// If Client is still connected send the message to it
		if( !clientSocket.isConnected() )
		{
			close();
			return false;
		}
		// write the message to the stream
		try
		{
			chat.setMessage(msg);
			clientOutputStream.writeObject(chat);
			clientOutputStream.flush();
		}
		// if an error occurs, do not abort just inform the user
		catch(IOException e)
		{
			util.displayEvent("Error sending message to " + client.getUserId());
			util.displayEvent(e.getClass().getName()+" Exception --> "+e.getMessage());
		}
		return true;
	}
}