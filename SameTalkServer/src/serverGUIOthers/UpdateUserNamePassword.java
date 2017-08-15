package serverGUIOthers;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import helper.ReadFromPropertiesFile;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class UpdateUserNamePassword
{
	public static final int UPDATE_USERNAME = 10212;
	public static final int UPDATE_PASSWORD = 21341;
	private String updateFiled = "";
	private boolean confirmPassFlag = true;
	private boolean passFlag = true;
	
	private static final int framex = 100;
	private static final int framey = 100;
	private static final int frameLength = 400;
	private static final int frameheigth = 200;
	private Color bgColor = new Color(238, 238, 238);
	
	public JFrame updateAdminDetailsFrame;
	private JLabel updateLabel;
	private JTextField updateUN;
	private JPasswordField updatePass;
	private JButton showHideUpdatePass;
	private JLabel confirmPassLabel;
	private JPasswordField confirmPass;
	private JButton showHideConfirmPass;
	private JButton updateBtn;
	
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					UpdateUserNamePassword window = new UpdateUserNamePassword(UPDATE_USERNAME);
					window.updateAdminDetailsFrame.setVisible(true);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}
	

	public UpdateUserNamePassword(int updateCode)
	{
		if(updateCode == UPDATE_PASSWORD)
			updateFiled = "Password";
		else if(updateCode == UPDATE_USERNAME)
			updateFiled = "Username";
		else
		{
			return;
		}
		
		initializeFrame();
		initComponents(updateCode);
		initListeners(updateCode);
		associateFrameComponents(updateCode);
	}
	
	private void associateFrameComponents(int code)
	{
		updateAdminDetailsFrame.getContentPane().add(updateLabel);
		if( code == UPDATE_USERNAME )
		{
			updateAdminDetailsFrame.getContentPane().add(updateUN);
		}
		else if( code == UPDATE_PASSWORD )
		{
			updateAdminDetailsFrame.getContentPane().add(updatePass);
			updateAdminDetailsFrame.getContentPane().add(showHideUpdatePass);
		}
		updateAdminDetailsFrame.getContentPane().add(confirmPassLabel);
		updateAdminDetailsFrame.getContentPane().add(confirmPass);
		updateAdminDetailsFrame.getContentPane().add(showHideConfirmPass);
		updateAdminDetailsFrame.getContentPane().add(updateBtn);
		
		updateAdminDetailsFrame.getRootPane().setDefaultButton(updateBtn);
	}
	
	private void initListeners(int code)
	{
		updateBtn.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				String value = "";
				if( code == UPDATE_USERNAME )
					value = updateUN.getText();
				else if( code == UPDATE_PASSWORD )
					value = String.valueOf(updatePass.getPassword());
				
				if( !value.equals("") )
				{
					int ans = JOptionPane.showConfirmDialog(updateAdminDetailsFrame,
							("Confirm the Change of "+updateFiled+" to :- "+value));
					
					if( ans == 1 || ans == 2 )
						return;
					
					if( String.valueOf(confirmPass.getPassword()).equals(ReadFromPropertiesFile
							.getProp("pass")) )
					{
						
						if(code == UPDATE_PASSWORD)
						{
							try
							{
								ReadFromPropertiesFile.setProp("pass", value);
							}
							catch(IOException e1)
							{
								JOptionPane.showMessageDialog(updateAdminDetailsFrame,
										"Updation failed Internal Server Error(Please try Again after some time).");
								System.out.println(e1.getMessage());
								return;
							}
						}
						else if(code == UPDATE_USERNAME)
						{
							try
							{
								ReadFromPropertiesFile.setProp("username", value);
							}
							catch(IOException e1)
							{
								JOptionPane.showMessageDialog(updateAdminDetailsFrame,
										"Updation failed Internal Server Error(Please try Again after some time).");
								System.out.println(e1.getMessage());
								return;
							}
						}
						JOptionPane.showMessageDialog(updateAdminDetailsFrame, "Updation Successfull");
						updateAdminDetailsFrame.dispose();
					}
					else
					{
						JOptionPane.showMessageDialog(updateAdminDetailsFrame,
								"Confirmation Failed( Password provided is invalid).");
						return;
					}
				}
				else
				{
					JOptionPane.showMessageDialog(updateAdminDetailsFrame,
							"Please provide the new Detail");
					return;
				}
			}
		});
		
		if( code == UPDATE_PASSWORD )
		{
			showHideUpdatePass.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					if( passFlag )
					{
						updatePass.setEchoChar((char) 0);
						showHideUpdatePass.setIcon(new ImageIcon(new ImageIcon("images/hidePass.png").getImage()
								.getScaledInstance(showHideUpdatePass.getWidth(), showHideUpdatePass.getHeight(),
										Image.SCALE_SMOOTH)));
						showHideUpdatePass.setToolTipText("Hide Password");
						passFlag = false;
					}
					else
					{
						updatePass.setEchoChar('*');
						showHideUpdatePass.setIcon(new ImageIcon(new ImageIcon("images/showPass.png").getImage()
								.getScaledInstance(showHideUpdatePass.getWidth(), showHideUpdatePass.getHeight(),
										Image.SCALE_SMOOTH)));
						showHideUpdatePass.setToolTipText("Show Password");
						passFlag = true;
					}
				}
			});
		}
		
		showHideConfirmPass.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if( confirmPassFlag )
				{
					confirmPass.setEchoChar((char) 0);
					showHideConfirmPass.setIcon(new ImageIcon(new ImageIcon("images/hidePass.png").getImage()
							.getScaledInstance(showHideConfirmPass.getWidth(), showHideConfirmPass.getHeight(),
									Image.SCALE_SMOOTH)));
					showHideConfirmPass.setToolTipText("Hide Password");
					confirmPassFlag = false;
				}
				else
				{
					confirmPass.setEchoChar('*');
					showHideConfirmPass.setIcon(new ImageIcon(new ImageIcon("images/showPass.png").getImage()
							.getScaledInstance(showHideConfirmPass.getWidth(), showHideConfirmPass.getHeight(),
									Image.SCALE_SMOOTH)));
					showHideConfirmPass.setToolTipText("Show Password");
					confirmPassFlag = true;
				}
			}
		});
	}
	
	private void initComponents(int code)
	{
		updateLabel = new JLabel("New "+updateFiled);
		updateLabel.setBounds(20, 30, 130, 20);
		if( code == UPDATE_USERNAME )
		{
			updateUN = new JTextField();
			updateUN.setBounds(170, 30, 170, 20);
			updateUN.setFont(new Font("Dialog", Font.PLAIN, 16));
		}
		else if( code == UPDATE_PASSWORD )
		{
			updatePass = new JPasswordField();
			updatePass.setBounds(170, 30, 170, 20);
			updatePass.setFont(new Font("Dialog", Font.PLAIN, 16));
			showHideUpdatePass = new JButton();
			showHideUpdatePass.setBounds(345, 30, 20, 20);
			showHideUpdatePass.setIcon(new ImageIcon(new ImageIcon("images/showPass.png").getImage()
					.getScaledInstance(showHideUpdatePass.getWidth(), showHideUpdatePass.getHeight(), Image.SCALE_SMOOTH)));
			showHideUpdatePass.setToolTipText("Show Password");
		}
		
		confirmPassLabel = new JLabel("Current Password");
		confirmPassLabel.setBounds(20, 80, 130, 20);
		confirmPass = new JPasswordField();
		confirmPass.setBounds(170, 80, 170, 20);
		confirmPass.setFont(new Font("Dialog", Font.PLAIN, 16));
		showHideConfirmPass = new JButton();
		showHideConfirmPass.setBounds(345, 80, 20, 20);
		showHideConfirmPass.setIcon(new ImageIcon(new ImageIcon("images/showPass.png").getImage()
				.getScaledInstance(showHideConfirmPass.getWidth(), showHideConfirmPass.getHeight(), Image.SCALE_SMOOTH)));
		showHideConfirmPass.setToolTipText("Show Password");
		
		updateBtn = new JButton("Update Information");
		updateBtn.setBounds(100, 140, 180, 30);
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initializeFrame()
	{		
		updateAdminDetailsFrame = new JFrame("Update Administrator "+updateFiled);
		updateAdminDetailsFrame.setBounds(framex, framey, frameLength, frameheigth);
		updateAdminDetailsFrame.setBackground(bgColor);
		updateAdminDetailsFrame.getContentPane().setLayout(null);
		updateAdminDetailsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		updateAdminDetailsFrame.setVisible(true);
		updateAdminDetailsFrame.setResizable(false);
	}
}
