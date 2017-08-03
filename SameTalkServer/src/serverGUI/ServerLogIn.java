package serverGUI;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.UIManager;

import helper.ReadFromPropertiesFile;

public class ServerLogIn
{
	private static final int framex = 100;
	private static final int framey = 100;
	private static final int frameLength = 450;
	private static final int frameheigth = 350;
	private Color bgColor = new Color(238, 238, 238);
	
	private JFrame serverLogInframe;
	private JLabel bgLabel;
	private JTextField serverPort;
	private JTextPane serverPortTextPane;
	private JPasswordField password;
	private JTextField userName;
	private JTextPane userNameTextPane;
	private JTextPane passwordTextPane;
	private JButton LogIn;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					ServerLogIn window = new ServerLogIn();
					window.serverLogInframe.setVisible(true);
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
	public ServerLogIn()
	{
		initializeFrame();
		initComponents();
		initListeners();
		associateFrameComponents();
	}
	
	private void associateFrameComponents()
	{
		//frame.setJMenuBar(menuBar);
		serverLogInframe.getContentPane().add(bgLabel);
		serverLogInframe.getContentPane().add(serverPort);
		serverLogInframe.getContentPane().add(serverPortTextPane);
		serverLogInframe.getContentPane().add(LogIn);
		serverLogInframe.getContentPane().add(userName);
		serverLogInframe.getContentPane().add(password);
		serverLogInframe.getContentPane().add(userNameTextPane);
		serverLogInframe.getContentPane().add(passwordTextPane);

		serverLogInframe.getRootPane().setDefaultButton(LogIn);
	}
	
	private void initComponents()
	{
		// Background Label.
		bgLabel = new JLabel();
		bgLabel.setBounds(((frameLength/2)-30), 20, 60, 85);
		bgLabel.setIcon(new ImageIcon(new ImageIcon("images/sameTime.png").getImage()
				.getScaledInstance(bgLabel.getWidth(), bgLabel.getHeight(), Image.SCALE_SMOOTH)));
		
		serverPortTextPane = new JTextPane();
		serverPortTextPane.setText("Server Port");
		serverPortTextPane.setFocusable(false);
		serverPortTextPane.setEditable(false);
		serverPortTextPane.setBackground(UIManager.getColor("Button.background"));
		serverPortTextPane.setBounds(87, 120, 80, 20);
		serverPort = new JTextField();
		serverPort.setBounds(187, 120, 154, 20);
		
		userNameTextPane = new JTextPane();
		userNameTextPane.setBackground(bgColor);
		userNameTextPane.setBounds(87, 155, 80, 20);
		userNameTextPane.setText("User Id");
		userNameTextPane.setEditable(false);
		userNameTextPane.setFocusable(false);
		userName = new JTextField();
		userName.setBounds(187, 155, 154, 20);
		userName.setText("admin");
		userName.setEditable(false);
		userName.setFocusable(false);

		passwordTextPane = new JTextPane();
		passwordTextPane.setBackground(bgColor);
		passwordTextPane.setBounds(87, 190, 80, 20);
		passwordTextPane.setText("Password");
		passwordTextPane.setEditable(false);
		passwordTextPane.setFocusable(false);
		password = new JPasswordField();
		password.setBounds(187, 190, 154, 20);
		
		LogIn = new JButton("LogIn");
		LogIn.setBounds(((frameLength/2)-65), 260, 130, 20);
	}
	

	/////////////////////////////////////////////////////////////////////////
	//////////////////////////Listeners//////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////
	private void initListeners()
	{
		LogIn.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				if( !String.valueOf(password.getPassword()).equals("") )
				{
					if( String.valueOf(password.getPassword()).equals(ReadFromPropertiesFile.getProp("pass")) )
					{
						// start server on port 1500 unless a PortNumber is specified
						int portNumber = 4501;
						
						try
						{
							portNumber = Integer.parseInt(serverPort.getText());
						}
						catch(NumberFormatException e)
						{
							JOptionPane.showMessageDialog(serverLogInframe, "Invalid port number.");
							return;
						}
						// create a server object and start it
						ServerLoggedIn server = new ServerLoggedIn();
						try
						{
							server.startServer(portNumber);
						}
						catch(IOException e)
						{
							JOptionPane.showMessageDialog(serverLogInframe, "Server cannot be Started!!\n" +
										e.getClass().getName() + " --> " + e.getMessage() + "\n");
							return;
						}
						server.displayFrame();
						server.serverLoggedInframe.setVisible(true);
						serverLogInframe.dispose();
					}
					else
					{
						JOptionPane.showMessageDialog(serverLogInframe, "Password Incorrect!!!");
						return;
					}
				}
				else
				{
					JOptionPane.showMessageDialog(serverLogInframe, "Please provide Password to LogIn.");
					return;
				}
			}
		});
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initializeFrame()
	{
		serverLogInframe = new JFrame("Same Talk LogIn");
		serverLogInframe.setBounds(framex, framey, frameLength, frameheigth);
		serverLogInframe.setBackground(bgColor);
		serverLogInframe.getContentPane().setLayout(null);
		serverLogInframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		serverLogInframe.setVisible(true);
		serverLogInframe.setResizable(false);
	}
}
