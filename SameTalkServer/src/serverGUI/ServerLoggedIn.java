package serverGUI;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import serverMainClasses.Server;

import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.JTextPane;

public class ServerLoggedIn
{
	
	private static final int framex = 100;
	private static final int framey = 100;
	private static final int frameLength = 700;
	private static final int frameheigth = 500;
	private final Color bgColor = new Color(238, 238, 238);
	private final Color failedStatusbg = new Color(255,0,0);
	private final String failedStatus = "Server is not running";
	@SuppressWarnings("unused")
	private final Color readyStatusbg = new Color(0,255,0);
	@SuppressWarnings("unused")
	private final String readyStatus = "Server is running normally";
	@SuppressWarnings("unused")
	private final Color errorStatusbg = new Color(255,128,0);
	@SuppressWarnings("unused")
	private final String errorStatus = "Server encountered some problem.";
	
	public JFrame serverLoggedInframe;
	private JTextPane serverStatusTextPane;
	private JTextField serverStatusText;
	private JButton shutDownServerBtn;
	private JTextArea eventDisplay;
	private JScrollPane eventDisplayScrollPane;
	private JButton regUser;
	private JTable connectedClientsTable;
	private DefaultTableModel connectedClientsTableModel;
	private JScrollPane connectedClientsScrollPane;
	private JTextArea serverDetails;
	
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					ServerLoggedIn window = new ServerLoggedIn();
					window.serverLoggedInframe.setVisible(true);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * Create the application.
	 */
	public ServerLoggedIn()
	{
		initializeFrame();
		initComponents();
		initListeners();
		associateFrameComponents();
	}
	public ServerLoggedIn(int portNumber)
	{		
		// Initialize & Start GUI.
		initializeFrame();
		initComponents();
		initListeners();
		associateFrameComponents();

		// Start the Server.
		Server server = new Server(portNumber);
		server.start();
	}
	
	private void associateFrameComponents()
	{
		serverLoggedInframe.getContentPane().add(serverStatusTextPane);
		serverLoggedInframe.getContentPane().add(serverStatusText);
		serverLoggedInframe.getContentPane().add(shutDownServerBtn);
		serverLoggedInframe.getContentPane().add(eventDisplayScrollPane);
		serverLoggedInframe.getContentPane().add(serverDetails);
		serverLoggedInframe.getContentPane().add(regUser);
		serverLoggedInframe.getContentPane().add(connectedClientsScrollPane);
	}
	
	private void initListeners()
	{
		
	}
	
	private void initComponents()
	{
		serverStatusTextPane = new JTextPane();
		serverStatusTextPane.setEditable(false);
		serverStatusTextPane.setFocusable(false);
		serverStatusTextPane.setBackground(bgColor);
		serverStatusTextPane.setText("Server Status:");
		serverStatusTextPane.setFont(new Font("Dialog", Font.PLAIN, 16));
		serverStatusTextPane.setBounds(10, 12, 120, 20);
		serverStatusText = new JTextField();
		serverStatusText.setEditable(false);
		serverStatusText.setFocusable(false);
		serverStatusText.setHorizontalAlignment(JTextField.CENTER);
		serverStatusText.setBounds(140, 10, 330, 30);
		serverStatusText.setFont(new Font("Dialog", Font.BOLD, 16));
		serverStatusText.setText(failedStatus);
		serverStatusText.setBackground(failedStatusbg);
		
		
		shutDownServerBtn = new JButton("ShutDown Server");
		shutDownServerBtn.setBounds((frameLength-170), 10, 160, 30);
		
		eventDisplay = new JTextArea();
		eventDisplay.setEditable(false);
		eventDisplay.setFocusable(false);
		eventDisplay.setFont(new Font("Dialog", Font.PLAIN, 15));
		eventDisplayScrollPane = new JScrollPane(eventDisplay);
		eventDisplayScrollPane.setBounds(10, 80, 370, 410);
		
		serverDetails = new JTextArea();
		serverDetails.setFocusable(false);
		serverDetails.setEditable(false);
		serverDetails.setBounds(400, 80, 288, 20);
		
		regUser = new JButton("Register a New User");
		regUser.setBounds(440, 120, 200, 30);
		
		connectedClientsTable = new JTable();
		connectedClientsScrollPane = new JScrollPane(connectedClientsTable);
		connectedClientsScrollPane.setBounds(394, 162, 294, 326);
		String[] columns = {"Client Name", "Connection Time"};
		connectedClientsTableModel = new DefaultTableModel()
				{
					private static final long serialVersionUID = 1L;

					@Override
					public boolean isCellEditable(int arg0, int arg1)
					{
						return false;
						//return super.isCellEditable(arg0, arg1);
					}
				};
		connectedClientsTableModel.setColumnIdentifiers(columns);
		connectedClientsTable.setModel(connectedClientsTableModel);
		connectedClientsTable.setRowHeight(30);
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initializeFrame()
	{
		serverLoggedInframe = new JFrame("Same Talk LogIn");
		serverLoggedInframe.setBounds(framex, framey, frameLength, frameheigth);
		serverLoggedInframe.setBackground(bgColor);
		serverLoggedInframe.getContentPane().setLayout(null);
		serverLoggedInframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		serverLoggedInframe.setVisible(true);
		serverLoggedInframe.setResizable(false);
		
		serverLoggedInframe.addWindowListener( new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent arg0)
			{
				new ServerLogIn();
				super.windowClosing(arg0);
			}
		});
	}
}
