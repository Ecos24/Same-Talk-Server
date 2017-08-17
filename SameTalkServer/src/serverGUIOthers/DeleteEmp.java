package serverGUIOthers;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import serverMainClasses.ThreadDeleteEmployee;

public class DeleteEmp
{
	private static final int framex = 100;
	private static final int framey = 100;
	private static final int frameLength = 440;
	private static final int frameheigth = 370;
	private Color bgColor = new Color(238, 238, 238);
	
	private ThreadDeleteEmployee tDele;
	
	public JFrame deleteFrame;
	
	private JScrollPane tableScrollPane;
	private JTable empTable;
	private DefaultTableModel tableModel;
	private JButton deleteBtn;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DeleteEmp window = new DeleteEmp();
					window.deleteFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public DeleteEmp()
	{
		initComponents();
		initListeners();
		
		tDele = new ThreadDeleteEmployee(tableModel);
		tDele.start();
		
		initializeFrame();
		associateFrameComponents();
	}

	private void associateFrameComponents()
	{
		deleteFrame.getContentPane().add(tableScrollPane);
		deleteFrame.getContentPane().add(deleteBtn);
		
		deleteFrame.getRootPane().setDefaultButton(deleteBtn);
	}
	
	private void initListeners()
	{
		deleteBtn.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				
			}
		});
	}
	
	private void initComponents()
	{
		tableScrollPane = new JScrollPane();
		tableScrollPane.setBounds(10, 10, 420, 300);
		
		empTable = new JTable();
		tableModel = new DefaultTableModel()
		{
			private static final long serialVersionUID = 1L;

			public Class<?> getColumnClass(int column)
			{
				switch(column)
				{
					case 0:
						return Boolean.class;
						
					case 1:
						return String.class;
						
					case 2:
						return String.class;
						
					default:
						return String.class;
				}
			}
		};
		empTable.setModel(tableModel);
		
		// Set Column Names.
		tableModel.addColumn("Select");
		tableModel.addColumn("Employee Id");
		tableModel.addColumn("Employee Name");
		tableModel.addColumn("Department");
		
		// Add Table to ScrollPane
		tableScrollPane.setViewportView(empTable);
		
		deleteBtn = new JButton("Delete");
		deleteBtn.setBounds(((frameLength/2) - 50), 320, 100, 30);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initializeFrame()
	{
		deleteFrame = new JFrame("Delete Employee");
		deleteFrame.setBounds(framex, framey, frameLength, frameheigth);
		deleteFrame.setBackground(bgColor);
		deleteFrame.getContentPane().setLayout(null);
		deleteFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		deleteFrame.setResizable(false);
		
		deleteFrame.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosed(WindowEvent e)
			{
				if( tDele != null )
					tDele.keepGoing = false;
				
				super.windowClosed(e);
			}
		});
	}
}
