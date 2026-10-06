package org.gcsimulator;

import javax.swing.JPanel;
import java.awt.Frame;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JDialog;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;

import java.awt.Rectangle;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.Color;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Vector;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import java.awt.GridBagConstraints;

public class ChemicalDialog extends JDialog implements ActionListener, KeyListener, FocusListener, ChangeListener{

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JComboBox jcboCompound = null;
	private JLabel jLabel1 = null;
	private JTextField jtxtConcentration1 = null;
	private JLabel jLabel2 = null;
	private JButton jbtnOk = null;
	private JButton jbtnCancel = null;
	private JPanel jPanel = null;
	
	public Vector<Integer> m_vectCompoundsUsed = new Vector<Integer>();  //  @jve:decl-index=0:
	public boolean m_bOk = false;
	public int m_iCompound = 0;
	public String m_strCompoundName = "";  //  @jve:decl-index=0:
	public double m_dConcentration1 = 50;
	public int m_iStationaryPhase = 0;
	public InterpolationFunction InterpolatedLogkvsT;
	private JLabel jLabel31 = null;
	private JLabel jlblCompound = null;
	/**
	 * @param owner
	 */
	public ChemicalDialog(Frame owner, boolean bEditDialog) 
	{
		super(owner);
		initialize();
		
		if (bEditDialog)
		{
			this.setTitle("Edit Compound");
		}
		
		jcboCompound.addActionListener(this);
		jbtnOk.addActionListener(this);
		jbtnCancel.addActionListener(this);
        jtxtConcentration1.addKeyListener(this);
        jtxtConcentration1.addFocusListener(this);
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(441, 172);
		this.setContentPane(getJContentPane());
		this.setModal(true);
		this.setResizable(false);
		this.setTitle("Add New Compound");
		this.setCompoundVariables();
		this.performValidations();
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jlblCompound = new JLabel();
			jlblCompound.setBounds(new Rectangle(12, 8, 138, 16));
			jlblCompound.setText("Compound:");
			jLabel2 = new JLabel();
			jLabel2.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLabel2.setBounds(new Rectangle(212, 64, 57, 17));
			jLabel2.setText("\u00b5M");
			jLabel1 = new JLabel();
			jLabel1.setText("Concentration:");
			jLabel1.setBounds(new Rectangle(12, 64, 105, 17));
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.add(getJbtnOk(), null);
			jContentPane.add(getJbtnCancel(), null);
			jContentPane.add(jLabel1, null);
			jContentPane.add(getJtxtConcentration1(), null);
			jContentPane.add(jLabel2, null);
			//jContentPane.add(jLabel31, null);
			jContentPane.add(getJcboCompound(), null);
			jContentPane.add(jlblCompound, null);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jcboCompound	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getJcboCompound() 
	{
		if (jcboCompound == null) 
		{
			jcboCompound = new JComboBox(Globals.CompoundNameArray[m_iStationaryPhase]);
			jcboCompound.setFont(new Font("Dialog", Font.PLAIN, 12));
			jcboCompound.setBounds(new Rectangle(8, 28, 417, 26));
		}
		
		return jcboCompound;
	}

	/**
	 * This method initializes jtxtConcentration1	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJtxtConcentration1() {
		if (jtxtConcentration1 == null) {
			jtxtConcentration1 = new JTextField();
			jtxtConcentration1.setText("50");
			jtxtConcentration1.setBounds(new Rectangle(120, 60, 85, 26));
		}
		return jtxtConcentration1;
	}

	/**
	 * This method initializes jbtnOk	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJbtnOk() {
		if (jbtnOk == null) {
			jbtnOk = new JButton();
			jbtnOk.setBounds(new Rectangle(176, 104, 125, 31));
			jbtnOk.setActionCommand("OK");
			jbtnOk.setText("OK");
		}
		return jbtnOk;
	}

	/**
	 * This method initializes jbtnCancel	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJbtnCancel() {
		if (jbtnCancel == null) {
			jbtnCancel = new JButton();
			jbtnCancel.setBounds(new Rectangle(308, 104, 124, 31));
			jbtnCancel.setText("Cancel");
		}
		return jbtnCancel;
	}

	public void setSelectedCompound(int iCompoundIndex)
	{
		jcboCompound.setSelectedIndex(iCompoundIndex);
	}

	public void setSelectedStationaryPhase(int iStationaryPhase)
	{
		this.m_iStationaryPhase = iStationaryPhase;
		DefaultComboBoxModel newModel = new DefaultComboBoxModel(Globals.CompoundNameArray[m_iStationaryPhase]); 
		this.jcboCompound.setModel(newModel);
	}
	
	
	public void setCompoundConcentration(double dConcentration)
	{
		this.m_dConcentration1 = dConcentration;
		this.jtxtConcentration1.setText(Float.toString((float)dConcentration));
	}
	
	private void setCompoundVariables()
	{
		this.m_iCompound = this.jcboCompound.getSelectedIndex();
		
		Compound thisCompound = new Compound();
		thisCompound.loadCompoundInfo(this.m_iStationaryPhase, m_iCompound);
		
		m_strCompoundName = thisCompound.strCompoundName;

		InterpolatedLogkvsT = thisCompound.InterpolatedLogkvsT;
		
		NumberFormat formatter = new DecimalFormat("#0.0000");

		jtxtConcentration1.setText(formatter.format(m_dConcentration1));
	}
	
	//@Override
	public void actionPerformed(ActionEvent arg0) 
	{
		if (arg0.getActionCommand() == "comboBoxChanged")
		{
			setCompoundVariables();
		}
		else if (arg0.getActionCommand() == "OK")
		{
			for (int i = 0; i < this.m_vectCompoundsUsed.size(); i++)
			{
				if (m_iCompound == m_vectCompoundsUsed.get(i).intValue())
				{
					
					JOptionPane.showMessageDialog(this,
							"That compound was already added. Please select a different compound.",
							"Compound already added",
							JOptionPane.INFORMATION_MESSAGE,
							null);
					return;
				}
			}
			
			m_bOk = true;
			this.setVisible(false);
			this.dispose(); 
		}
		else if (arg0.getActionCommand() == "Cancel")
		{
			this.setVisible(false);
			this.dispose(); 			
		}
	}

	@Override
	public void keyPressed(KeyEvent arg0) 
	{

	}

	@Override
	public void keyReleased(KeyEvent arg0) 
	{

	}

	@Override
	public void keyTyped(KeyEvent e) {
		if (!((Character.isDigit(e.getKeyChar()) ||
				(e.getKeyChar() == KeyEvent.VK_BACK_SPACE) ||
				(e.getKeyChar() == KeyEvent.VK_DELETE) ||
				(e.getKeyChar() == KeyEvent.VK_PERIOD))))
		{
	        e.consume();
		}
		
		if (e.getKeyChar() == KeyEvent.VK_ENTER)
		{
			performValidations();
		}
		
	}

	@Override
	public void focusGained(FocusEvent arg0) 
	{

	}

	@Override
	public void focusLost(FocusEvent arg0) 
	{
		performValidations();		
	}

	public void performValidations()
	{
		validateConcentration1();
	}

	public void validateConcentration1()
	{
		if (jtxtConcentration1.getText().length() == 0)
			jtxtConcentration1.setText("0");

    	double dTemp = (double)Float.valueOf(jtxtConcentration1.getText());
		
		if (dTemp < .000001)
			dTemp = .000001;
		if (dTemp > 100000)
			dTemp = 100000;
		
		this.m_dConcentration1 = dTemp;
		jtxtConcentration1.setText(Float.toString((float)m_dConcentration1));    	
	}

	@Override
	public void stateChanged(ChangeEvent arg0) 
	{
		performValidations();
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
