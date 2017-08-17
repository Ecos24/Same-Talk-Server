package helper;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import beanClasses.User;

public class Util
{
	private JTextArea displayEvent;
	private DefaultTableModel connectedClientTableModel;
	private JTextField serverdetails;
	private JTextField serverStatus;
	
	// List of All Users.
	public static ArrayList<User> allUsers = new ArrayList<>();

	// Status final objects.
	public final static String STATUS_BUSY = "busy";
	public final static String STATUS_FAILED = "failed";
	public final static String STATUS_READY = "ideal";
	public final static String STATUS_ERROR = "error";
	
	private final static Color failedStatusbg = new Color(255,0,0);
	private final static String failedStatus = "Server is not running";
	private final static Color readyStatusbg = new Color(0,255,0);
	private final static String readyStatus = "Server is running Ideally";
	private final static Color errorStatusbg = new Color(255,128,0);
	private final static String errorStatus = "Server encountered some problem";
	private final static Color busyStatusbg = new Color(214, 255, 0);
	private final static String busyStatus = "Server is busy with normal conditions";
	
	// To display time in hh:mm:ss
	public static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm a");
	
	/**
	 * Constructor to Initialize local references with objects from GUI Class.
	 * @param displayEvent
	 * @param connectedClientTableModel
	 * @param serverdetails
	 */
	public Util(JTextArea displayEvent, DefaultTableModel connectedClientTableModel,
			JTextField serverdetails, JTextField serverStatusText)
	{
		super();
		this.displayEvent = displayEvent;
		this.connectedClientTableModel = connectedClientTableModel;
		this.serverdetails = serverdetails;
		this.serverStatus = serverStatusText;
	}
	
	public void updateConnectedClientsTable(String userId)
	{
		String row[] = new String[2];
		row[0] = userId;
		row[1] = sdf.format(new Date());
		connectedClientTableModel.addRow(row);
	}
	
	public void removeClientFromTable(String userId)
	{
		for( int i = 0; i < connectedClientTableModel.getRowCount() ; i++ )
		{
			System.out.println(connectedClientTableModel.getValueAt(i, 0).toString());
			if(connectedClientTableModel.getValueAt(i, 0).toString().equals(userId.toLowerCase()))
				connectedClientTableModel.removeRow(i);
		}
	}

	/**
	 * Display an event (not a message) to the console or the GUI
	 * @param msg
	 */
	public void displayEvent(String msg)
	{
		String time = sdf.format(new Date()) + " " + msg + "\n";
		displayEvent.append(time);
	}
	
	/**
	 * Display an message to the console or the GUI
	 * @param msg
	 */
	public void displayChat(String msg)
	{
		String time = sdf.format(new Date()) + " " + msg;
		
		/*StyleContext sc = StyleContext.getDefaultStyleContext();
	    AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, Color.GREEN);
	    aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console");
	    aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

	    int len = displayEvent.getDocument().getLength();
	    displayEvent.setCaretPosition(len);
	    displayEvent.setCharacterAttributes(aset, false);
	    System.out.println(time);
	    displayEvent.replaceSelection(time);*/
		
		displayEvent.append(time);
	}
	
	/**
	 * Function is useful to display server Connection Details.
	 * @param msg
	 */
	public void updateServerDetails(String msg)
	{
		serverdetails.setText(msg);
	}
	
	/**
	 * Function helps in updating ServerStatus.
	 * @param status
	 */
	public void updateServerStatus(String status)
	{
		switch(status)
		{
			case "busy":	
				serverStatus.setBackground(busyStatusbg);
				serverStatus.setText(busyStatus);
				break;
			
			case "ideal":
				serverStatus.setBackground(readyStatusbg);
				serverStatus.setText(readyStatus);
				break;
				
			case "failed":
				serverStatus.setBackground(failedStatusbg);
				serverStatus.setText(failedStatus);
				break;
				
			case "error":
				serverStatus.setBackground(errorStatusbg);
				serverStatus.setText(errorStatus);
				break;
		}
	}
}
