package serverMainClasses;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Date;

import beanClasses.User;
import helper.AuthenticateUser;
import helper.ChatMessage;
import helper.Util;

public class ServersClientThread extends Thread
{
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

	public ServersClientThread(Socket clientSocket, int id)
	{
		super();
		this.clientSocket = clientSocket;
		this.clientId = id;
		
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
			Util.displayEvent("Exception occured while creating Client Thread --> "+e.getMessage());
			return;
		}
		date = new Date().toString();
	}
	
	public boolean confirmUser()
	{
		try
		{
			if( AuthenticateUser.authenticate(this.client) )
			{
				clientOutputStream.writeObject(this.client);
				// Call Authenticating user.
				Util.displayEvent(client.getUserId() + " just Connected");
				
				return true;
			}
			else
			{
				if( this.client != null )
					Util.displayEvent(client.getUserId() + " cannot be authenticated");
				
				return false;
			}
		}
		catch( IOException e )
		{
			if( this.client != null )
				Util.displayEvent(e.getClass().getName()+" Exception occured while authenticating "+
					client.getUserId() + "--> "+e.getMessage());
			
			return false;
		}
	}
	
	// What will run Forever.
	@Override
	public void run()
	{
		super.run();
		// To loop until LogOut.
		boolean keepGoing = true;
		while(keepGoing)
		{
			try
			{
				chatMessage = (ChatMessage) clientInputStream.readObject();
			}
			catch(ClassNotFoundException | IOException e)
			{
				if( e.getClass().getName().equals("java.io.EOFException") )
				{
					System.out.println("Client Closed the Application");
					close();
					break;
				}
				Util.displayEvent("Exception reading Stream --> "+e.getMessage());
				e.printStackTrace();
				break;
			}
			
			// Message
			String msg = chatMessage.getMessage();
			
			switch(chatMessage.getType())
			{
				case ChatMessage.MESSAGE:
					switch(chatMessage.getMsgTargetType())
					{
						case ChatMessage.MESSAGE_TARGET_BROADCAST:
							Server.broadCast(client.getUserId() + " : " + msg , chatMessage, clientId);
							break;
							
						case ChatMessage.MESSAGE_TARGET_GROUP:
							break;
							
						case ChatMessage.MESSAGE_TARGET_PERSONAL:
							break;

						default:
							System.out.println("Discarding Message as Type is not defined");
							break;
					}
					break;
				
				case ChatMessage.LOGOUT:
					Util.displayEvent(client.getUserId() + " disconnected with a LOGOUT message.");
					keepGoing = false;
					break;
					
				case ChatMessage.WHOSETHERE:
					writeMsg("List of the users connected at " + Util.sdf.format(new Date()) + "\n", chatMessage);
					// Scan ArrayList for users connected
					for(int i = 0; i < Server.clientsList.size(); ++i)
					{
						ServersClientThread ct = Server.clientsList.get(i);
						writeMsg((i+1) + ") " + ct.client.getUserId() + " since " + ct.date, chatMessage);
					}
					break;

				default:
					break;
			}
		}
		// Remove myself from the arrayList containing the list of the connected Clients.
		Server.remove(clientId);
		close();
	}
	
	// try to close everything
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
			Util.displayEvent(e.getClass().getName()+"Exception occured while closing "+client.getUserId()+" Connection --> "+e.getMessage());
			return;
		}
		try
		{
			if(clientInputStream != null)
				clientInputStream.close();
		}
		catch(IOException e)
		{
			Util.displayEvent(e.getClass().getName()+"Exception occured while closing "+client.getUserId()+" Connection --> "+e.getMessage());
			return;
		}
		try
		{
			if(clientSocket != null)
				clientSocket.close();
		}
		catch(IOException e)
		{
			Util.displayEvent(e.getClass().getName()+"Exception occured while closing "+client.getUserId()+" Connection --> "+e.getMessage());
			return;
		}
	}

	/*
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
			Util.displayEvent("Error sending message to " + client.getUserId());
			Util.displayEvent(e.getClass().getName()+" Exception --> "+e.getMessage());
		}
		return true;
	}
}