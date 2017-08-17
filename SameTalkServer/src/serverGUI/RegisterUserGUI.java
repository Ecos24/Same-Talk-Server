package serverGUI;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.persistence.PersistenceException;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JTextField;

import beanClasses.User;
import helper.Util;
import helper.UtilClient;
import hibernate.DBUtil;
import hibernate.RegisterUser;
import serverMainClasses.Server;

import javax.swing.JPasswordField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class RegisterUserGUI
{
	private static final int framex = 100;
	private static final int framey = 100;
	private static final int frameLength = 500;
	private static final int frameheigth = 400;
	private final Color bgColor = new Color(238, 238, 238);
	private String[] defaultPos = {"Select Department First"};
	private boolean passFlag = true;
	
	public JFrame regFrame;
	
	private JLabel logo;
	private JLabel userIdLabel;
	private JTextField userId;
	private JLabel userNameLabel;
	private JTextField userName;
	private JLabel userDeptLabel;
	private JComboBox<String> userDept;
	private JLabel userPosLabel;
	private JComboBox<String> userPos;
	private JLabel passwordLabel;
	private JPasswordField password;
	private JButton showHidePass;
	private JButton register;

	/**
	 * Create the application.
	 */
	public RegisterUserGUI()
	{
		initializeFrame();
		initComponents();
		initListeners();
		associateFrameComponents();
	}
	
	private void associateFrameComponents()
	{
		regFrame.getContentPane().add(logo);
		regFrame.getContentPane().add(userIdLabel);
		regFrame.getContentPane().add(userId);
		regFrame.getContentPane().add(userNameLabel);
		regFrame.getContentPane().add(userName);
		regFrame.getContentPane().add(userDeptLabel);
		regFrame.getContentPane().add(userDept);
		regFrame.getContentPane().add(userPosLabel);
		regFrame.getContentPane().add(userPos);
		regFrame.getContentPane().add(passwordLabel);
		regFrame.getContentPane().add(password);
		regFrame.getContentPane().add(showHidePass);
		regFrame.getContentPane().add(register);
		
		regFrame.getRootPane().setDefaultButton(register);
	}
	
	private void initListeners()
	{
		register.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				System.out.println((String)userDept.getSelectedItem());
				if( !userId.getText().equals("") || !userName.getText().equals("") ||
						userDept.getSelectedIndex() != 0 || userPos.getSelectedIndex() != 0 ||
								String.valueOf(password.getPassword()).equals(""))
				{
					User newUser = new User();
					newUser.setUserId(userId.getText().toLowerCase());
					newUser.setUserName(userName.getText());
					newUser.setDepartment( ((String)userDept.getSelectedItem()).toLowerCase() );
					newUser.setPosition( ((String)userPos.getSelectedItem()).toLowerCase() );
					newUser.setPassword(String.valueOf(password.getPassword()));
					
					try
					{
						RegisterUser.registerNewUser(newUser);
					}
					catch(PersistenceException ex)
					{
						if( ex.getMessage().toLowerCase().contains("constraintviolationexception") )
							JOptionPane.showMessageDialog(regFrame, "UserId already registered");
						return;
					}
					
					Server.clientStatusList = DBUtil.getAllRegisteredClients();
					
					userId.setText("");
					userName.setText("");
					userDept.setSelectedIndex(0);
					userPos.setSelectedIndex(0);
					password.setText("");
					showHidePass.doClick();
					JOptionPane.showMessageDialog(regFrame, "User Registered.");
					
					Util.allUsers.clear();
					Util.allUsers.addAll(DBUtil.getAllUsers());
				}
				else
				{
					JOptionPane.showMessageDialog(regFrame, "Please provide complete information.");
				}
			}
		});
		
		showHidePass.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if( passFlag )
				{
					password.setEchoChar((char) 0);
					showHidePass.setIcon(new ImageIcon(new ImageIcon("images/hidePass.png").getImage()
							.getScaledInstance(showHidePass.getWidth(), showHidePass.getHeight(),
									Image.SCALE_SMOOTH)));
					showHidePass.setToolTipText("Hide Password");
					passFlag = false;
				}
				else
				{
					password.setEchoChar('*');
					showHidePass.setIcon(new ImageIcon(new ImageIcon("images/showPass.png").getImage()
							.getScaledInstance(showHidePass.getWidth(), showHidePass.getHeight(),
									Image.SCALE_SMOOTH)));
					showHidePass.setToolTipText("Show Password");
					passFlag = true;
				}
			}
		});
		
		userDept.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if( userDept.getSelectedIndex() != 0 )
				{
					userPos.setModel( new DefaultComboBoxModel<String>( 
							UtilClient.getPositions((String)userDept.getSelectedItem()) ));
				}
				else
				{
					userPos.setModel( new DefaultComboBoxModel<String>(defaultPos) );
				}
			}
		});
	}
	
	private void initComponents()
	{
		logo = new JLabel();
		logo.setBounds(((frameLength/2)-50), 15, 100, 100);
		logo.setIcon(new ImageIcon(new ImageIcon("images/sameTime.png").getImage()
				.getScaledInstance(logo.getWidth(), logo.getHeight(), Image.SCALE_SMOOTH)));
		logo.setFocusable(false);
		
		userIdLabel = new JLabel("Employee Id :-");
		userIdLabel.setLabelFor(userId);
		userIdLabel.setBounds(70, 135, 180, 20);
		userId = new JTextField();
		userId.setBounds(270, 135, 160, 20);
		userId.setColumns(10);

		userNameLabel = new JLabel("Employee Name :-");
		userNameLabel.setBounds(70, 165, 180, 20);
		userNameLabel.setLabelFor(userName);
		userName = new JTextField();
		userName.setBounds(270, 165, 160, 20);
		userName.setColumns(10);
		
		userDeptLabel = new JLabel("Employee Department :-");
		userDeptLabel.setBounds(70, 195, 180, 20);
		userDeptLabel.setLabelFor(userDept);
		userDept = new JComboBox<String>(UtilClient.getDepartments());
		userDept.setBounds(270, 195, 160, 20);
		
		userPosLabel = new JLabel("Employee Position :-");
		userPosLabel.setBounds(70, 225, 180, 20);
		userPosLabel.setLabelFor(userPos);
		userPos = new JComboBox<>(defaultPos);
		userPos.setBounds(270, 225, 160, 20);
		
		passwordLabel = new JLabel("Password :-");
		passwordLabel.setBounds(70, 255, 180, 20);
		passwordLabel.setLabelFor(password);
		password = new JPasswordField();
		password.setBounds(270, 255, 160, 20);
		password.setEchoChar('*');
		showHidePass = new JButton();
		showHidePass.setBounds(435, 255, 20, 20);
		showHidePass.setIcon(new ImageIcon(new ImageIcon("images/showPass.png").getImage()
				.getScaledInstance(showHidePass.getWidth(), showHidePass.getHeight(), Image.SCALE_SMOOTH)));
		showHidePass.setToolTipText("Show Password");
		register = new JButton("Register Employee");
		register.setBounds(165, 310, 170, 30);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initializeFrame()
	{		
		regFrame = new JFrame("Register New User");
		regFrame.setBounds(framex, framey, frameLength, frameheigth);
		regFrame.setBackground(bgColor);
		regFrame.getContentPane().setLayout(null);
		regFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		regFrame.setResizable(false);
	}
}
