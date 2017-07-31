package beanClasses;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table (name="REGISTERED_USERS")
public class User implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	private String userId;
	private String password;
	@Column( name = "Token")
	private String uniqueToken;
	
	
	public String getUniqueToken()
	{
		return uniqueToken;
	}
	public void setUniqueToken(String uniqueToken)
	{
		this.uniqueToken = uniqueToken;
	}
	public String getUserId()
	{
		return userId;
	}
	public void setUserId(String userId)
	{
		this.userId = userId;
	}
	public String getPassword()
	{
		return password;
	}
	public void setPassword(String password)
	{
		this.password = password;
	}
}