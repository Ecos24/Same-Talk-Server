package serverGUI;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import helper.Util;
import serverGUIOthers.UpdateUserNamePassword;
import serverMainClasses.Server;

import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JTextPane;
import javax.swing.border.TitledBorder;

import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.awt.event.ActionEvent;

public class ServerLoggedIn
{
	private Server server;
	
	private static final int framex = 100;
	private static final int framey = 100;
	private static final int frameLength = 700;
	private static final int frameheigth = 530;
	private final Color bgColor = new Color(238, 238, 238);
	
	public JFrame serverLoggedInframe;
	private JMenuBar topBar;
	private JMenu menuHeaders, subMenu;
	private JMenuItem menuItems;
	private JTextPane serverStatusTextPane;
	private JTextField serverStatusText;
	private JButton shutDownServerBtn;
	private JTextArea eventDisplay;
	private JScrollPane eventDisplayScrollPane;
	private JLabel connectedClientsLabel;
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
		serverLoggedInframe.setJMenuBar(topBar);
		serverLoggedInframe.getContentPane().add(serverStatusTextPane);
		serverLoggedInframe.getContentPane().add(serverStatusText);
		serverLoggedInframe.getContentPane().add(shutDownServerBtn);
		serverLoggedInframe.getContentPane().add(eventDisplayScrollPane);
		serverLoggedInframe.getContentPane().add(serverDetails);
		serverLoggedInframe.getContentPane().add(connectedClientsLabel);
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
		addMenuBar();
		
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
		
		connectedClientsLabel = new JLabel("Active Employees");
		connectedClientsLabel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK),
                null, TitledBorder.CENTER,
                TitledBorder.TOP));
		connectedClientsLabel.setFont(new Font("Dialog", Font.BOLD, 16));
		connectedClientsLabel.setHorizontalAlignment(JLabel.CENTER);
		connectedClientsLabel.setBounds(394, 120, 294, 22);
		connectedClientsTable = new JTable();
		connectedClientsScrollPane = new JScrollPane(connectedClientsTable);
		connectedClientsScrollPane.setBounds(394, 142, 294, 346);
		connectedClientsScrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK),
                null, TitledBorder.CENTER,
                TitledBorder.TOP));
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
	
	private void addMenuBar()
	{
		ActionListener menuItemListener = new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				switch (e.getActionCommand())
				{
					case "Change User Name":
						UpdateUserNamePassword windowUn = new UpdateUserNamePassword(UpdateUserNamePassword.UPDATE_USERNAME);
						windowUn.updateAdminDetailsFrame.setVisible(true);
						break;
						
					case "Change Password":
						UpdateUserNamePassword windowPass = new UpdateUserNamePassword(UpdateUserNamePassword.UPDATE_PASSWORD);
						windowPass.updateAdminDetailsFrame.setVisible(true);
						break;
						
					case "Quit":
						Server.getUtil().updateServerStatus(Util.STATUS_FAILED);
						server.destroy();
						serverLoggedInframe.dispose();
						System.exit(1);
						break;
						
					case "View All":
						break;
						
					case "Edit":
						break;
						
					case "Register":
						RegisterUserGUI win = new RegisterUserGUI();
						win.regFrame.setVisible(true);
						break;
						
					case "Delete":
						break;
						
					case "Help":
						break;
						
					case "About":
						break;

					default:
						break;
				}
			}
		};
		
		topBar = new JMenuBar();
		
		// First Menu.
		menuHeaders = new JMenu("Administrator");
		menuHeaders.setMnemonic(KeyEvent.VK_A);
		menuHeaders.getAccessibleContext().setAccessibleDescription("Provide Menus for Administrative services.");
		menuItems = new JMenuItem("Change User Name");
		menuItems.addActionListener(menuItemListener);
		menuItems.setMnemonic(KeyEvent.VK_U);
		menuItems.getAccessibleContext().setAccessibleDescription("Change password for Administrative LogIn.");
		menuHeaders.add(menuItems);
		menuItems = new JMenuItem("Change Password");
		menuItems.addActionListener(menuItemListener);
		menuItems.setMnemonic(KeyEvent.VK_P);
		menuItems.getAccessibleContext().setAccessibleDescription("Change password for Administrative LogIn.");
		menuHeaders.add(menuItems);
		menuHeaders.addSeparator();
		menuItems = new JMenuItem("Quit");
		menuItems.addActionListener(menuItemListener);
		menuItems.setMnemonic(KeyEvent.VK_Q);
		menuItems.getAccessibleContext().setAccessibleDescription("Exit the Server.");
		menuHeaders.add(menuItems);
		topBar.add(menuHeaders);
		
		// Second Menu.
		menuHeaders = new JMenu("Employees");
		menuHeaders.setMnemonic(KeyEvent.VK_E);
		menuHeaders.getAccessibleContext().setAccessibleDescription("Menu for Employee Management.");
		subMenu = new JMenu("View/Edit Registered");
		subMenu.setMnemonic(KeyEvent.VK_V);
		menuItems = new JMenuItem("View All");
		menuItems.addActionListener(menuItemListener);
		menuItems.setMnemonic(KeyEvent.VK_V);
		menuItems.getAccessibleContext().setAccessibleDescription("View all Registered Employees");
		subMenu.add(menuItems);
		menuItems = new JMenuItem("Edit");
		menuItems.addActionListener(menuItemListener);
		menuItems.setMnemonic(KeyEvent.VK_E);
		menuItems.getAccessibleContext().setAccessibleDescription("Edit Registered Employees");
		subMenu.add(menuItems);
		menuHeaders.add(subMenu);
		menuHeaders.addSeparator();
		menuItems = new JMenuItem("Register");
		menuItems.addActionListener(menuItemListener);
		menuItems.setMnemonic(KeyEvent.VK_R);
		menuHeaders.add(menuItems);
		menuItems = new JMenuItem("Delete");
		menuItems.addActionListener(menuItemListener);
		menuItems.setMnemonic(KeyEvent.VK_D);
		menuHeaders.add(menuItems);
		topBar.add(menuHeaders);
		
		menuHeaders = new JMenu("Help");
		menuItems = new JMenuItem("Help");
		menuItems.addActionListener(menuItemListener);
		menuItems.setMnemonic(KeyEvent.VK_H);
		menuItems.getAccessibleContext().setAccessibleDescription("Documentation on Software");
		menuHeaders.add(menuItems);
		menuItems = new JMenuItem("About");
		menuItems.addActionListener(menuItemListener);
		menuItems.setMnemonic(KeyEvent.VK_A);
		menuHeaders.add(menuItems);
		topBar.add(menuHeaders);
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
		serverLoggedInframe.setResizable(false);
	}
}
