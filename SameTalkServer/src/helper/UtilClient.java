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
					cs.setClientName(csOr.getClientName());
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
	
	
	public static String[] getDepartments()
	{
		String[] dept = {"Select Department", "Accounting", "Developer", "Finance", "Human Resource", "Quality Assurance"}; 
		return dept;
	}
	
	public static int getDepartmentIndex(String dept)
	{
		String[] deptArr = {"Select Department", "Accounting", "Developer", "Finance", "Human Resource", "Quality Assurance"};
		for( int i=1 ; i<deptArr.length ; i++ )
		{
			if( dept.equalsIgnoreCase(deptArr[i]) )
				return i;
		}
		return -1;
	}
	
	public static String[] getPositions( String dept )
	{
		ArrayList<String> pos = new ArrayList<>();
		switch(dept)
		{
			case "Accounting":
				pos.add("Select");
				pos.add("Staff Accountant");
				pos.add("Accounts Receivable Specialist");
				pos.add("Analyst/Associate (Forensic Accounting)");
				pos.add("Accounting Associate");
				pos.add("Tax Manager");
				pos.add("Internal Audit Manager");
				break;
				
			case "Developer":
				pos.add("Select");
				pos.add("Project Manager");
				pos.add("Team Lead");
				pos.add("Senior Developer");
				pos.add("Junior Developer");
				pos.add("Designer");
				pos.add("DB Manager");
				break;
				
			case "Finance":
				pos.add("Select");
				pos.add("Financial Analyst");
				pos.add("Credit Manager");
				pos.add("Cash Management");
				pos.add("Investor Relations");
				break;
				
			case "Human Resource":
				pos.add("Select");
				pos.add("Manager");
				pos.add("Talent Manager");
				pos.add("Assistant Manager");
				break;
				
			case "Quality Assurance":
				pos.add("Select");
				pos.add("Team Leader");
				pos.add("Senior Testor");
				pos.add("Junior Testor");
				break;

			default:
				String[] defaultPos = {"Select Department First"};
				return defaultPos;
		}
		if( pos.size() > 0 )
		{
			String[] returnPos = new String[pos.size()];
			returnPos = pos.toArray(returnPos);
			return returnPos;
		}
		else
			return null;
	}

	public static int getPositionsIndex(String dept, String position)
	{
		switch(WordUtil.capitalizeString(dept))
		{
			case "Accounting":
				String[] posAc = {"Staff Accountant", "Accounts Receivable Specialist",
						"Analyst/Associate (Forensic Accounting)", "Accounting Associate",
						"Tax Manager", "Internal Audit Manager"};
				for( int i=0 ; i<posAc.length ; i++ )
				{
					if( position.equalsIgnoreCase(posAc[i]) )
					{
						return (i+1);
					}
				}
				return -1;
				
			case "Developer":
				String[] posDe = {"Project Manager", "Team Lead",
						"Senior Developer", "Junior Developer",
						"Designer", "DB Manager"};
				for( int i=0 ; i<posDe.length ; i++ )
				{
					if( position.equalsIgnoreCase(posDe[i]) )
					{
						return (i+1);
					}
				}
				return -1;
				
			case "Finance":
				String[] posFi = {"Financial Analyst", "Credit Manager",
						"Cash Management", "Investor Relations"};
				for( int i=0 ; i<posFi.length ; i++ )
				{
					if( position.equalsIgnoreCase(posFi[i]) )
					{
						return (i+1);
					}
				}
				return -1;
				
			case "Human Resource":
				String[] posHr = {"Manager", "Talent Manager",
						"Assistant Manager"};
				for( int i=0 ; i<posHr.length ; i++ )
				{
					if( position.equalsIgnoreCase(posHr[i]) )
					{
						return (i+1);
					}
				}
				return -1;
				
			case "Quality Assurance":
				String[] posQa = {"Team Leader", "Senior Testor",
						"Junior Testor"};
				for( int i=1 ; i<posQa.length ; i++ )
				{
					if( position.equalsIgnoreCase(posQa[i]) )
					{
						return (i+1);
					}
				}
				return -1;

			default:
				return -1;
		}
	}
}
