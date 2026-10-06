package org.gcsimulator.panels;

import org.gcsimulator.TemperatureProgramListener;
import org.jdesktop.swingx.JXPanel;

import java.awt.Dimension;
import javax.swing.JLabel;
import java.awt.Rectangle;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.ListSelectionModel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class TemperatureProgramOptions extends JXPanel implements ActionListener, ListSelectionListener, TableModelListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public SpecialTableModel2 tmTemperatureProgram;
	public JButton jbtnInsertRow = null;
	public JButton jbtnAddRow = null;
	public JButton jbtnRemoveRow = null;
	private JLabel jlblInitialTemperature = null;
	public JTextField jtxtInitialTemperature = null;
	private JLabel jlblInitialTemperatureUnit = null;
	private JLabel jlblInitialTime = null;
	public JTextField jtxtInitialTime = null;
	private JLabel jlblInitialTimeUnit = null;
	private JScrollPane jScrollPane = null;
	public JTable jtblTemperatureProgram = null;
	public boolean m_bDoNotChangeTable = false;
	
	public List<TemperatureProgramListener> listeners = new ArrayList<TemperatureProgramListener>();
	
	public void addListener(TemperatureProgramListener toAdd)
	{
		listeners.add(toAdd);
	}
	
	public void sayTemperatureProgramChanged()
	{
		for (TemperatureProgramListener tpl : listeners)
		{
			tpl.temperatureProgramChanged();
		}
	}
	
	/**
	 * This method initializes 
	 * 
	 */
	public TemperatureProgramOptions() 
	{
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() 
	{
        jlblInitialTimeUnit = new JLabel();
        jlblInitialTimeUnit.setBounds(new Rectangle(256, 36, 57, 16));
        jlblInitialTimeUnit.setText("min");
        jlblInitialTime = new JLabel();
        jlblInitialTime.setBounds(new Rectangle(8, 36, 181, 16));
        jlblInitialTime.setText("Initial time:");
        jlblInitialTime.setDisplayedMnemonic(KeyEvent.VK_UNDEFINED);
        jlblInitialTemperatureUnit = new JLabel();
        jlblInitialTemperatureUnit.setBounds(new Rectangle(256, 12, 57, 16));
        jlblInitialTemperatureUnit.setText("°C");
        jlblInitialTemperature = new JLabel();
        jlblInitialTemperature.setBounds(new Rectangle(8, 12, 181, 16));
        jlblInitialTemperature.setText("Initial oven temperature:");
        
        this.setLayout(null);
        this.setBounds(new Rectangle(0, 0, 314, 220));
        this.setPreferredSize(this.getSize());
        this.add(getJbtnInsertRow(), null);
        this.add(getJbtnRemoveRow(), null);
        this.add(jlblInitialTemperature, null);
        this.add(getJtxtInitialTemperature(), null);
        this.add(jlblInitialTemperatureUnit, null);
        this.add(jlblInitialTime, null);
        this.add(getJtxtInitialTime(), null);
        this.add(jlblInitialTimeUnit, null);
        this.add(getJScrollPane(), null);
        this.add(getJbtnAddRow(), null);

        jbtnInsertRow.addActionListener(this);
        jbtnRemoveRow.addActionListener(this);
        jbtnAddRow.addActionListener(this);
        jtblTemperatureProgram.getSelectionModel().addListSelectionListener(this);
        tmTemperatureProgram.addTableModelListener(this);
	}

	public class SpecialTableModel extends DefaultTableModel 
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 9144486981092084762L;

		public SpecialTableModel(final Object[] columnNames, final int rowCount) 
	    {
	        super(convertToVector(columnNames), rowCount);
	    }
	    
	    public SpecialTableModel(final Object[][] data, final Object[] columnNames) 
	    {
	        setDataVector(data, columnNames);
	    }

	    public boolean isCellEditable(int row, int column) 
	    {
	        //Note that the data/cell address is constant,
	        //no matter where the cell appears onscreen.
	        /*if (column < 1) {
	            return false;
	        } else {
	            return true;
	        }*/
	    	return true;
	    }
	    
	    /*
	     * JTable uses this method to determine the default renderer/
	     * editor for each cell.  If we didn't implement this method,
	     * then the last column would contain text ("true"/"false"),
	     * rather than a check box.
	     */
	    @SuppressWarnings("unchecked")
		public Class getColumnClass(int c) {
	        return getValueAt(0, c).getClass();
	    }

	}
	
	public class SpecialTableModel2 extends DefaultTableModel 
	{
		public SpecialTableModel2(final Object[] columnNames, final int rowCount) 
	    {
	        super(convertToVector(columnNames), rowCount);
	    }
	    
	    public SpecialTableModel2(final Object[][] data, final Object[] columnNames) 
	    {
	        setDataVector(data, columnNames);
	    }

	    public boolean isCellEditable(int row, int column) 
	    {
	        //Note that the data/cell address is constant,
	        //no matter where the cell appears onscreen.
	        /*if (column < 1) {
	            return false;
	        } else {
	            return true;
	        }*/
	    	return true;
	    }
	    
	    /*
	     * JTable uses this method to determine the default renderer/
	     * editor for each cell.  If we didn't implement this method,
	     * then the last column would contain text ("true"/"false"),
	     * rather than a check box.
	     */
	    public Class getColumnClass(int c) {
	        return getValueAt(0, c).getClass();
	    }

	}
	
	/**
	 * This method initializes jtableGradientProgram	
	 * 	
	 * @return javax.swing.JTable	
	 */
	private JTable getJtblTemperatureProgram() {
		if (jtblTemperatureProgram == null) 
		{
			Object[] columnNames = {"Ramp (\u00b0C/min)", "Final temp (\u00b0C)", "Hold time (min)"};
			Double[][] data = {{20.0, 260.0, 5.0}};
	        
			tmTemperatureProgram = new SpecialTableModel2(data, columnNames);

			jtblTemperatureProgram = new JTable(tmTemperatureProgram);

			jtblTemperatureProgram.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

			jtblTemperatureProgram.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);

			jtblTemperatureProgram.getTableHeader().setPreferredSize(new Dimension(22, 22));
			jtblTemperatureProgram.getColumnModel().getColumn(0).setPreferredWidth(85);
		}
		
		return jtblTemperatureProgram;
	}

	/**
	 * This method initializes jbtnInsertRow	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJbtnInsertRow() {
		if (jbtnInsertRow == null) {
			jbtnInsertRow = new JButton();
			jbtnInsertRow.setActionCommand("Insert Row");
			jbtnInsertRow.setLocation(new Point(8, 184));
			jbtnInsertRow.setSize(new Dimension(94, 29));
			jbtnInsertRow.setEnabled(false);
			jbtnInsertRow.setText("Insert");
		}
		return jbtnInsertRow;
	}

	/**
	 * This method initializes jbtnRemoveRow	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJbtnRemoveRow() {
		if (jbtnRemoveRow == null) {
			jbtnRemoveRow = new JButton();
			jbtnRemoveRow.setActionCommand("Remove Row");
			jbtnRemoveRow.setLocation(new Point(208, 184));
			jbtnRemoveRow.setSize(new Dimension(94, 29));
			jbtnRemoveRow.setEnabled(false);
			jbtnRemoveRow.setText("Remove");
		}
		return jbtnRemoveRow;
	}

	/**
	 * This method initializes jtxtInitialTemperature	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJtxtInitialTemperature() {
		if (jtxtInitialTemperature == null) {
			jtxtInitialTemperature = new JTextField();
			jtxtInitialTemperature.setBounds(new Rectangle(192, 8, 61, 26));
			jtxtInitialTemperature.setText("60.0");
		}
		return jtxtInitialTemperature;
	}

	/**
	 * This method initializes jtxtInitialTime	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJtxtInitialTime() {
		if (jtxtInitialTime == null) {
			jtxtInitialTime = new JTextField();
			jtxtInitialTime.setBounds(new Rectangle(192, 32, 61, 26));
			jtxtInitialTime.setText("5");
		}
		return jtxtInitialTime;
	}

	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setBounds(new Rectangle(8, 64, 297, 117));
			jScrollPane.setViewportView(getJtblTemperatureProgram());
		}
		return jScrollPane;
	}

	/**
	 * This method initializes jbtnAddRow	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJbtnAddRow() {
		if (jbtnAddRow == null) {
			jbtnAddRow = new JButton();
			jbtnAddRow.setText("Add");
			jbtnAddRow.setLocation(new Point(108, 184));
			jbtnAddRow.setSize(new Dimension(94, 28));
			jbtnAddRow.setActionCommand("Add Row");
		}
		return jbtnAddRow;
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if (e.getActionCommand() == "Add Row")
		{
			int lastRowIndex = this.jtblTemperatureProgram.getRowCount() - 1;

	    	Double dRowValue1, dRowValue2, dRowValue3;
	    	
	    	if (lastRowIndex == -1)
	    	{
	    		dRowValue1 = 10.0; // default ramp rate
	    		dRowValue2 = Float.valueOf(this.jtxtInitialTemperature.getText()).doubleValue();
	    		dRowValue3 = 5.0; // default hold time
	    	}
	    	else
	    	{
	    		dRowValue1 = 10.0; // default ramp rate
	    		dRowValue2 = (Double)tmTemperatureProgram.getValueAt(lastRowIndex, 1);
	    		dRowValue3 = 5.0; // default hold time
	    	}
	    	
			Double dRowData[] = {dRowValue1, dRowValue2, dRowValue3};
	    	tmTemperatureProgram.addRow(dRowData);
		}
		else if (e.getActionCommand() == "Insert Row")
		{
			int activatedRowIndex = this.jtblTemperatureProgram.getSelectedRow();
			if (activatedRowIndex < 0)
				return;

	    	Double dRowValue1, dRowValue2, dRowValue3;
	    	
			dRowValue1 = (Double) tmTemperatureProgram.getValueAt(activatedRowIndex, 0);
			dRowValue2 = (Double) tmTemperatureProgram.getValueAt(activatedRowIndex, 1);
			dRowValue3 = (Double) tmTemperatureProgram.getValueAt(activatedRowIndex, 2);

			Double dRowData[] = {dRowValue1, dRowValue2, dRowValue3};
	    	tmTemperatureProgram.insertRow(activatedRowIndex, dRowData);
		}
		else if (e.getActionCommand() == "Remove Row")
		{
			int activatedRowIndex = this.jtblTemperatureProgram.getSelectedRow();
			if (activatedRowIndex < 0)
				return;

	    	tmTemperatureProgram.removeRow(activatedRowIndex);
	    	
		    jbtnInsertRow.setEnabled(false);
		    jbtnRemoveRow.setEnabled(false);
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) 
	{
		if (e.getSource() == this.jtblTemperatureProgram.getSelectionModel())
		{
			int iSelectedRow = jtblTemperatureProgram.getSelectedRow();
			if (iSelectedRow >= 0)
			{
				this.jbtnRemoveRow.setEnabled(true);
				this.jbtnInsertRow.setEnabled(true);
			}
			else
			{
				this.jbtnRemoveRow.setEnabled(false);
				this.jbtnInsertRow.setEnabled(false);
			}
		}
	}

	@Override
	public void tableChanged(TableModelEvent e) 
	{
		if (m_bDoNotChangeTable)
		{
			m_bDoNotChangeTable = false;
			return;
		}
		
		if(e.getSource() == tmTemperatureProgram)
		{
			int row = e.getFirstRow();
			int column = e.getColumn();
			
			if (column == 0)
			{
				Double dNewValue = (Double) tmTemperatureProgram.getValueAt(row, 0);
				
				double dTemp = dNewValue;
				if (dTemp < 0)
					dTemp = 0;
				if (dTemp > 1000)
					dTemp = 1000;
				
		    	m_bDoNotChangeTable = true;
				tmTemperatureProgram.setValueAt(dTemp, row, column);
			}
			else if (column == 1)
			{
				Double dNewValue = (Double) tmTemperatureProgram.getValueAt(row, 1);
				
				double dTemp = dNewValue;
				if (dTemp < 60)
					dTemp = 60;
				if (dTemp > 320)
					dTemp = 320;
				if (row == 0)
				{
					if (dTemp < (Double)Float.valueOf(this.jtxtInitialTemperature.getText()).doubleValue())
					{
						dTemp = Float.valueOf(this.jtxtInitialTemperature.getText()).doubleValue();
					}
				}
				if (row < tmTemperatureProgram.getRowCount() - 1)
				{
					if (dTemp > (Double)tmTemperatureProgram.getValueAt(row + 1, column))
					{
						dTemp = (Double)tmTemperatureProgram.getValueAt(row + 1, column);
					}
				}
				if (row > 0)
				{
					if (dTemp < (Double)tmTemperatureProgram.getValueAt(row - 1, column))
					{
						dTemp = (Double)tmTemperatureProgram.getValueAt(row - 1, column);
					}
				}
		    	m_bDoNotChangeTable = true;
				tmTemperatureProgram.setValueAt(dTemp, row, column);
			}
			else if (column == 2)
			{
				Double dNewValue = (Double) tmTemperatureProgram.getValueAt(row, 2);
				
				double dTemp = dNewValue;
				if (dTemp < 0)
					dTemp = 0;
				if (dTemp > 1000)
					dTemp = 1000;
				
		    	m_bDoNotChangeTable = true;
				tmTemperatureProgram.setValueAt(dTemp, row, column);
			}
			
			sayTemperatureProgramChanged();
		}
		
    }

}
