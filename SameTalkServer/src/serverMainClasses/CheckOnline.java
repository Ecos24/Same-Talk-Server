package serverMainClasses;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class CheckOnline
{
	private static ServerSocket mainSocket = null;
	private static Socket acceptedSocket = null;
	
	private static Runnable clientThread = new Runnable()
	{
		@Override
		public void run()
		{
			try
			{
				mainSocket = new ServerSocket(5743);
				while(true)
				{
					System.out.println("Starting While.");
					acceptedSocket = mainSocket.accept();	//Establish Connection.
					System.out.println("Connection Created");
					DataInputStream inputStream = new DataInputStream(acceptedSocket.getInputStream());
					String str = (String) inputStream.readUTF();
					System.out.println("Client says --> "+str);
				}
			}
			catch( IOException e)
			{
				e.printStackTrace();
			}
			finally
			{
				if( mainSocket != null )
				{
					try
					{
						mainSocket.close();
					}
					catch(IOException e)
					{
						System.out.println(e.getClass().getName()+"Exception while Clossing MainSocket --> "+e.getMessage());
					}
				}
				if( acceptedSocket != null )
				{
					try
					{
						acceptedSocket.close();
					}
					catch(IOException e)
					{
						System.out.println(e.getClass().getName()+"Exception while Clossing acceptedSocket --> "+e.getMessage());
					}
				}
			}
		}
	};
	
	public static void main(String[] args)
	{
		Thread t1 = new Thread(clientThread);
		t1.start();
		System.out.println("Press 1 ");
	}

}
