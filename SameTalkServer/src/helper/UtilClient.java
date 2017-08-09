package helper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;

import beanClasses.ClientStatus;
import beanClasses.User;
import serverMainClasses.Server;

public class UtilClient
{
	/**
	 * This function update the Given User status on the Server.
	 * @param usr
	 */
	public void updateClientStatus(User usr, boolean onlineFlag)
	{
		for(ClientStatus clientStatus : Server.clientStatusList.get(usr.getDepartment()).get(usr.getPosition()) )
		{
			if( clientStatus.getClientId().equals(usr.getUserId()) )
			{
				int indexOfClient = Server.clientStatusList.get(usr.getDepartment())
							.get(usr.getPosition()).indexOf(clientStatus);
					ClientStatus cs = Server.clientStatusList.get(usr.getDepartment())
							.get(usr.getPosition()).get(indexOfClient);
				if( onlineFlag )
					cs.setClientStatus(ClientStatus.ONLINE);
				else
					cs.setClientStatus(ClientStatus.OFFLINE);
				
				// Update The Client in LinkedHashMap.
				Server.clientStatusList.get(usr.getDepartment()).get(usr.getPosition())
					.set(indexOfClient, cs);
			}
		}
	}

	public LinkedHashMap<String, LinkedHashMap<String, ArrayList<ClientStatus>>> copyLinkedHashMap(
			LinkedHashMap<String, LinkedHashMap<String, ArrayList<ClientStatus>>> origionalMap )
	{
		LinkedHashMap<String, LinkedHashMap<String, ArrayList<ClientStatus>>> copy = new LinkedHashMap<>();
		Iterator<LinkedHashMap<String, ArrayList<ClientStatus>>> origIt = origionalMap.values().iterator();
		Iterator<String> origKeysIt = origionalMap.keySet().iterator();
		while(origIt.hasNext() && origKeysIt.hasNext())
		{
			LinkedHashMap<String, ArrayList<ClientStatus>> value1 = origIt.next();
			Iterator<ArrayList<ClientStatus>> values1 = value1.values().iterator();
			Iterator<String> keys1 = value1.keySet().iterator();
			String key1 = origKeysIt.next();
			LinkedHashMap<String, ArrayList<ClientStatus>> lhm = new LinkedHashMap<>();
			while( values1.hasNext() && keys1.hasNext() )
			{
				ArrayList<ClientStatus> value2 = values1.next();
				String key2 = keys1.next();
				Iterator<ClientStatus> values2 = value2.iterator();
				ArrayList<ClientStatus> valuesToPut2 = new ArrayList<>();
				while( values2.hasNext() )
				{
					ClientStatus cs = new ClientStatus();
					ClientStatus csOr = values2.next();
					cs.setClientId(csOr.getClientId());
					cs.setClientStatus(csOr.getClientStatus());
					cs.setDepartment(csOr.getDepartment());
					cs.setPosition(csOr.getPosition());
					valuesToPut2.add(cs);
				}
				lhm.put(key2, valuesToPut2);
			}
			copy.put(key1, lhm);
		}
		if( copy.size() == 0 )
			return null;
		else
			return copy;
	}
}
