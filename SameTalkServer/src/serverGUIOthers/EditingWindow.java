package serverGUIOthers;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import beanClasses.User;
import helper.UtilClient;
import helper.DateWordFormatter;
import hibernate.DBUtil;

public class EditingWindow
{
	private static final int framex = 100;
	private static final int framey = 100;
	private static final int frameLength = 500;
	private static final int frameheigth = 300;
	private Color bgColor = new Color(238, 238, 238);
	
	private String[] defaultPos = {"Select Department First"};
	
	public JFrame editingFrame;

	private JLabel userIdLabel;
	private JTextField userId;
	private JLabel userNameLabel;
	private JTextField userName;
	private JLabel userDeptLabel;
	private JComboBox<String> userDept;
	private JLabel userPosLabel;
	private JComboBox<String> userPos;
	private JButton update;

	/**
	 * Create the application.
	 */
	public EditingWindow(User editUser)
	{
		initComponents(editUser);
		initListeners(editUser);
		initializeFrame(editUser.getUserName());
		associateFrameComponents();
		userDept.setSelectedIndex(UtilClient.getDepartmentIndex(editUser.getDepartment()));
		new Thread( new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					Thread.sleep(200);
				}
				catch(InterruptedException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				userPos.setSelectedIndex(UtilClient.getPositionsIndex(editUser.getDepartment(),editUser.getPosition()));
			}
		}).start();
	}
	
	private void associateFrameComponents()
	{
		editingFrame.getContentPane().add(userIdLabel);
		editingFrame.getContentPane().add(userId);
		editingFrame.getContentPane().add(userNameLabel);
		editingFrame.getContentPane().add(userName);
		editingFrame.getContentPane().add(userDeptLabel);
		editingFrame.getContentPane().add(userDept);
		editingFrame.getContentPane().add(userPosLabel);
		editingFrame.getContentPane().add(userPos);
		editingFrame.getContentPane().add(update);
	}
	
	private void initListeners(User editUser)
	{
		update.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if( !userName.equals("") || userDept.getSelectedIndex() != 0 ||
						userPos.getSelectedIndex() != 0)
				{
					User updatedUser = new User();
					updatedUser.setDepartment((String)userDept.getSelectedItem());
					updatedUser.setPosition((String)userPos.getSelectedItem());
					updatedUser.setUserName(DateWordFormatter.capitalizeString(userName.getText()));
					if( !editUser.getUserName().equalsIgnoreCase(updatedUser.getUserName())
							|| !editUser.getDepartment().equalsIgnoreCase(updatedUser.getDepartment())
							|| !editUser.getPosition().equalsIgnoreCase(updatedUser.getPosition()) )
					{
						int ans = JOptionPane.showConfirmDialog(editingFrame, "Do you confirm the changes\n"
								+ editUser.getUserName() + " --> " + updatedUser.getUserName() + "\n"
								+ editUser.getPosition() + " --> " + updatedUser.getPosition() + "\n"
								+ editUser.getDepartment() + " --> " + updatedUser.getDepartment() + "\n" );
						System.out.println(ans);
						if( ans == 0 )
						{
							updatedUser.setUserId(editUser.getUserId());
							updatedUser.setPassword(editUser.getPassword());
							DBUtil.updateUser(updatedUser);
							JOptionPane.showMessageDialog(editingFrame, "Changes updated");
						}
					}
					else
					{
						JOptionPane.showMessageDialog(editingFrame, "No updated information provided.");
						return;
					}
				}
				else
				{
					JOptionPane.showMessageDialog(editingFrame, "Provide complete information.");
					return;
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
	
	private void initComponents(User editUser)
	{
		userIdLabel = new JLabel("Employee Id :-");
		userIdLabel.setLabelFor(userId);
		userIdLabel.setBounds(70, 70, 180, 20);
		userId = new JTextField();
		userId.setText(editUser.getUserId());
		userId.setFocusable(false);
		userId.setEditable(false);
		userId.setBounds(270, 70, 160, 20);

		userNameLabel = new JLabel("Employee Name :-");
		userNameLabel.setBounds(70, 100, 180, 20);
		userNameLabel.setLabelFor(userName);
		userName = new JTextField();
		userName.setText(editUser.getUserName());
		userName.setBounds(270, 100, 160, 20);
		
		userDeptLabel = new JLabel("Employee Department :-");
		userDeptLabel.setBounds(70, 130, 180, 20);
		userDeptLabel.setLabelFor(userDept);
		userDept = new JComboBox<>(UtilClient.getDepartments());
		userDept.setBounds(270, 130, 160, 20);
		
		userPosLabel = new JLabel("Employee Position :-");
		userPosLabel.setBounds(70, 160, 180, 20);
		userPosLabel.setLabelFor(userPos);
		userPos = new JComboBox<>(defaultPos);
		userPos.setBounds(270, 160, 160, 20);
		
		update = new JButton("Update Employee");
		update.setBounds(165, 230, 170, 30);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initializeFrame(String name)
	{
		editingFrame = new JFrame("Editing "+name);
		editingFrame.setBounds(framex, framey, frameLength, frameheigth);
		editingFrame.setBackground(bgColor);
		editingFrame.getContentPane().setLayout(null);
		editingFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		editingFrame.setResizable(false);
	}

}
