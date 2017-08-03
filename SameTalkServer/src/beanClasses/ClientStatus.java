package beanClasses;

import java.io.Serializable;

public class ClientStatus implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3020750501590947316L;
	
	public final static String AWAY = "away";
	public final static String ONLINE = "online";
	public final static String OFFLINE = "offline";
	
	private String clientId;
	private String clientStatus;
	
	public String getClientId()
	{
		return clientId;
	}
	public void setClientId(String clientId)
	{
		this.clientId = clientId;
	}
	public String getClientStatus()
	{
		return clientStatus;
	}
	public void setClientStatus(String clientStatus)
	{
		this.clientStatus = clientStatus;
	}
}
