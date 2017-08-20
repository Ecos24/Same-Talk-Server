package serverGUIOthers;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import hibernate.DBUtil;
import serverMainClasses.ThreadDeleteEmployee;

public class ViewEdit
{
	private static final int framex = 100;
	private static final int framey = 100;
	private static final int frameLength = 500;
	private static final int frameheigth = 320;
	private Color bgColor = new Color(238, 238, 238);
	
	private ThreadDeleteEmployee tDele;
	
	public JFrame viewEditFrame;
	
	private Action editAction;
	
	private JScrollPane tableScrollPane;
	private JTable empTable;
	private DefaultTableModel tableModel;

	/**
	 * Create the application.
	 */
	public ViewEdit()
	{
		initComponents();
		initListeners();
		
		@SuppressWarnings("unused")
		EditButtonColumn edit = new EditButtonColumn(empTable, editAction, 3);
		
		tDele = new ThreadDeleteEmployee(tableModel, true);
		tDele.start();
		
		initializeFrame();
		associateFrameComponents();
	}
	
	private void associateFrameComponents()
	{
		viewEditFrame.getContentPane().add(tableScrollPane);
	}
	
	private void initListeners()
	{
		editAction = new AbstractAction()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e)
			{
				EditingWindow win = new EditingWindow(DBUtil.getUser(e.getActionCommand()));
				win.editingFrame.setVisible(true);
				
				/*JTable table = (JTable)e.getSource();
		        int modelRow = Integer.valueOf( e.getActionCommand() );
		        ((DefaultTableModel)table.getModel()).removeRow(modelRow);*/
			}
		};
	}
	
	private void initComponents()
	{
		tableScrollPane = new JScrollPane();
		tableScrollPane.setBounds(10, 10, 480, 300);
		
		empTable = new JTable();
		tableModel = new DefaultTableModel()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int rowIndex, int columnIndex)
			{
				return columnIndex == 3;
			}
			
			@Override
			public Class<?> getColumnClass(int column)
			{
				switch(column)
				{
					case 0:
						return String.class;
						
					case 1:
						return String.class;
						
					case 2:
						return String.class;
						
					case 3:
						return EditButtonColumn.class;
						
					default:
						return String.class;
				}
			}
		};
		empTable.setModel(tableModel);
		
		// Set Column Names.
		tableModel.addColumn("Employee Id");
		tableModel.addColumn("Employee Name");
		tableModel.addColumn("Department");
		tableModel.addColumn("Edit Employee");
		
		// Add Table to ScrollPane
		tableScrollPane.setViewportView(empTable);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initializeFrame()
	{
		viewEditFrame = new JFrame("View/Edit Employee");
		viewEditFrame.setBounds(framex, framey, frameLength, frameheigth);
		viewEditFrame.setBackground(bgColor);
		viewEditFrame.getContentPane().setLayout(null);
		viewEditFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		viewEditFrame.setResizable(false);
		
		viewEditFrame.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosed(WindowEvent e)
			{
				if( tDele != null )
					ThreadDeleteEmployee.keepGoing = false;
				
				super.windowClosed(e);
			}
		});
	}
}