package serverGUI;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import helper.Util;
import serverMainClasses.Server;

import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JTextPane;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.io.IOException;
import java.awt.event.ActionEvent;

public class ServerLoggedIn
{
	private Server server;
	
	
	private static final int framex = 100;
	private static final int framey = 100;
	private static final int frameLength = 700;
	private static final int frameheigth = 500;
	private final Color bgColor = new Color(238, 238, 238);
	
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
	private JTextField serverDetails;
	
	/**
	 * Create the application.
	 */
	public ServerLoggedIn()
	{		
		// Initialize GUI components.
		initComponents();
	}
	
	public void startServer(int portNumber) throws IOException
	{
		server = new Server(portNumber,eventDisplay,connectedClientsTableModel,serverDetails, serverStatusText);
		server.initServer();
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					server.start();
				}
				catch(IOException e)
				{
					Server.getUtil().updateServerStatus(Util.STATUS_FAILED);
					JOptionPane.showMessageDialog(null, "Server cannot be started!!\n" +
								e.getClass().getName() + " --> " + e.getMessage());
					e.printStackTrace();
					System.exit(1);
				}
			}
		}).start();
	}
	
	public void displayFrame()
	{
		initializeFrame();
		initListeners();
		associateFrameComponents();
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
		// Shutdown Listener.
		shutDownServerBtn.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				if( server != null )
				{
					Server.getUtil().updateServerStatus(Util.STATUS_FAILED);
					server.destroy();
					serverLoggedInframe.dispose();
					System.exit(1);
				}
			}
		});
		
		eventDisplayScrollPane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener()
		{	
			@Override
			public void adjustmentValueChanged(AdjustmentEvent e)
			{
				e.getAdjustable().setValue(e.getAdjustable().getMaximum());
			}
		});
		
		connectedClientsScrollPane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener()
		{	
			@Override
			public void adjustmentValueChanged(AdjustmentEvent e)
			{
				e.getAdjustable().setValue(e.getAdjustable().getMaximum());
			}
		});
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
		serverStatusText.setBounds(140, 10, 370, 30);
		serverStatusText.setFont(new Font("Dialog", Font.BOLD, 16));
		serverStatusText.setText("Server is not running");
		serverStatusText.setBackground(new Color(255,0,0));
		
		
		shutDownServerBtn = new JButton("ShutDown Server");
		shutDownServerBtn.setBounds((frameLength-170), 10, 160, 30);
		
		eventDisplay = new JTextArea();
		eventDisplay.setEditable(false);
		eventDisplay.setFocusable(false);
		eventDisplay.setFont(new Font("Dialog", Font.PLAIN, 15));
		eventDisplayScrollPane = new JScrollPane(eventDisplay);
		eventDisplayScrollPane.setBounds(10, 80, 370, 410);
		
		serverDetails = new JTextField();
		serverDetails.setFocusable(false);
		serverDetails.setEditable(false);
		serverDetails.setBounds(392, 80, 296, 20);
		
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
		serverLoggedInframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		serverLoggedInframe.setVisible(true);
		serverLoggedInframe.setResizable(false);
	}
}
