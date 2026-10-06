package org.gcsimulator.panels;

import org.gcsimulator.Globals;
import org.jdesktop.swingx.JXPanel;
import java.awt.Dimension;
import javax.swing.JLabel;
import java.awt.Rectangle;
import java.awt.Font;
import java.awt.Point;
import javax.swing.JTextField;
import javax.swing.JComboBox;

public class ColumnProperties extends JXPanel 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JLabel jlblColumnLength = null;
	private JLabel jlblColumnDiameter = null;
	private JLabel jlblParticleSize = null;
	private JLabel jlblHoldUpTime = null;
	public JTextField jtxtColumnLength = null;
	public JTextField jtxtColumnDiameter = null;
	public JTextField jtxtFilmThickness = null;
	public JLabel jlblHoldUpTimeIndicator = null;
	private JLabel jlblColumnLengthUnits = null;
	private JLabel jlblColumnDiameterUnits = null;
	private JLabel jlblParticleSizeUnits = null;
	private JLabel jlblHoldUpTimeUnits = null;
	private JLabel jlblStationaryPhase = null;
	public JComboBox jcboStationaryPhase = null;
	private JLabel jlblTotalPorosity = null;
	public JLabel jlblPhaseRatioIndicator = null;
	private JLabel jlblHETP = null;
	public JLabel jlblHETPIndicator = null;
	private JLabel jlblHETPUnits = null;
	private JLabel jlblTheoreticalPlates = null;
	public JLabel jlblTheoreticalPlatesIndicator = null;

	/**
	 * This method initializes 
	 * 
	 */
	public ColumnProperties() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
        jlblTheoreticalPlatesIndicator = new JLabel();
        jlblTheoreticalPlatesIndicator.setBounds(new Rectangle(192, 204, 117, 16));
        jlblTheoreticalPlatesIndicator.setText("0.9987");
        jlblTheoreticalPlatesIndicator.setFont(new Font("Dialog", Font.PLAIN, 12));
        jlblTheoreticalPlates = new JLabel();
        jlblTheoreticalPlates.setBounds(new Rectangle(8, 204, 177, 16));
        jlblTheoreticalPlates.setText("Theoretical plates:");
        jlblHETPUnits = new JLabel();
        jlblHETPUnits.setBounds(new Rectangle(252, 180, 57, 16));
        jlblHETPUnits.setText("mm");
        jlblHETPUnits.setFont(new Font("Dialog", Font.PLAIN, 12));
        jlblHETPIndicator = new JLabel();
        jlblHETPIndicator.setBounds(new Rectangle(192, 180, 57, 16));
        jlblHETPIndicator.setText("0.9987");
        jlblHETPIndicator.setFont(new Font("Dialog", Font.PLAIN, 12));
        jlblHETP = new JLabel();
        jlblHETP.setBounds(new Rectangle(8, 180, 177, 16));
        jlblHETP.setText("HETP:");
        jlblPhaseRatioIndicator = new JLabel();
        jlblPhaseRatioIndicator.setBounds(new Rectangle(192, 132, 57, 17));
        jlblPhaseRatioIndicator.setText("0.64");
        jlblPhaseRatioIndicator.setFont(new Font("Dialog", Font.PLAIN, 12));
        jlblTotalPorosity = new JLabel();
        jlblTotalPorosity.setBounds(new Rectangle(8, 132, 177, 16));
        jlblTotalPorosity.setText("Phase ratio:");
        jlblStationaryPhase = new JLabel();
        jlblStationaryPhase.setText("Stationary phase:");
        jlblStationaryPhase.setSize(new Dimension(125, 16));
        jlblStationaryPhase.setLocation(new Point(8, 8));
        jlblHoldUpTimeUnits = new JLabel();
        jlblHoldUpTimeUnits.setText("min");
        jlblHoldUpTimeUnits.setLocation(new Point(252, 156));
        jlblHoldUpTimeUnits.setSize(new Dimension(57, 16));
        jlblHoldUpTimeUnits.setFont(new Font("Dialog", Font.PLAIN, 12));
        jlblParticleSizeUnits = new JLabel();
        jlblParticleSizeUnits.setText("\u00b5m");
        jlblParticleSizeUnits.setLocation(new Point(252, 108));
        jlblParticleSizeUnits.setSize(new Dimension(57, 16));
        jlblParticleSizeUnits.setFont(new Font("Dialog", Font.PLAIN, 12));
        jlblColumnDiameterUnits = new JLabel();
        jlblColumnDiameterUnits.setText("mm");
        jlblColumnDiameterUnits.setLocation(new Point(252, 84));
        jlblColumnDiameterUnits.setSize(new Dimension(57, 16));
        jlblColumnDiameterUnits.setFont(new Font("Dialog", Font.PLAIN, 12));
        jlblColumnLengthUnits = new JLabel();
        jlblColumnLengthUnits.setText("m");
        jlblColumnLengthUnits.setBounds(new Rectangle(252, 60, 57, 16));
        jlblColumnLengthUnits.setFont(new Font("Dialog", Font.PLAIN, 12));
        jlblHoldUpTimeIndicator = new JLabel();
        jlblHoldUpTimeIndicator.setText("0.9987");
        jlblHoldUpTimeIndicator.setLocation(new Point(192, 156));
        jlblHoldUpTimeIndicator.setSize(new Dimension(57, 16));
        jlblHoldUpTimeIndicator.setFont(new Font("Dialog", Font.PLAIN, 12));
        jlblHoldUpTime = new JLabel();
        jlblHoldUpTime.setText("Hold-up time:");
        jlblHoldUpTime.setSize(new Dimension(177, 16));
        jlblHoldUpTime.setLocation(new Point(8, 156));
        jlblParticleSize = new JLabel();
        jlblParticleSize.setText("Film thickness:");
        jlblParticleSize.setSize(new Dimension(177, 16));
        jlblParticleSize.setLocation(new Point(8, 108));
        jlblColumnDiameter = new JLabel();
        jlblColumnDiameter.setText("Inner diameter:");
        jlblColumnDiameter.setSize(new Dimension(177, 16));
        jlblColumnDiameter.setLocation(new Point(8, 84));
        jlblColumnLength = new JLabel();
        jlblColumnLength.setText("Length:");
        jlblColumnLength.setSize(new Dimension(177, 16));
        jlblColumnLength.setLocation(new Point(8, 60));
        this.setLayout(null);
        this.setBounds(new Rectangle(0, 0, 314, 227));
        this.setPreferredSize(this.getSize());
        this.add(jlblColumnLength, null);
        this.add(jlblColumnDiameter, null);
        this.add(jlblParticleSize, null);
        this.add(jlblHoldUpTime, null);
        this.add(getJtxtColumnLength(), null);
        this.add(getJtxtColumnDiameter(), null);
        this.add(getJtxtFilmThickness(), null);
        this.add(jlblHoldUpTimeIndicator, null);
        this.add(jlblColumnLengthUnits, null);
        this.add(jlblColumnDiameterUnits, null);
        this.add(jlblParticleSizeUnits, null);
        this.add(jlblHoldUpTimeUnits, null);
        this.add(jlblStationaryPhase, null);
        this.add(getJcboStationaryPhase(), null);
        this.add(jlblTotalPorosity, null);
        this.add(jlblPhaseRatioIndicator, null);
        this.add(jlblHETP, null);
        this.add(jlblHETPIndicator, null);
        this.add(jlblHETPUnits, null);
        this.add(jlblTheoreticalPlates, null);
        this.add(jlblTheoreticalPlatesIndicator, null);
			
	}

	/**
	 * This method initializes jtxtColumnLength	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJtxtColumnLength() {
		if (jtxtColumnLength == null) {
			jtxtColumnLength = new JTextField();
			jtxtColumnLength.setText("30");
			jtxtColumnLength.setBounds(new Rectangle(188, 56, 61, 26));
		}
		return jtxtColumnLength;
	}

	/**
	 * This method initializes jtxtColumnDiameter	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJtxtColumnDiameter() {
		if (jtxtColumnDiameter == null) {
			jtxtColumnDiameter = new JTextField();
			jtxtColumnDiameter.setText("0.25");
			jtxtColumnDiameter.setBounds(new Rectangle(188, 80, 61, 26));
		}
		return jtxtColumnDiameter;
	}

	/**
	 * This method initializes jtxtFilmThickness	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJtxtFilmThickness() {
		if (jtxtFilmThickness == null) {
			jtxtFilmThickness = new JTextField();
			jtxtFilmThickness.setText("0.25");
			jtxtFilmThickness.setBounds(new Rectangle(188, 104, 61, 26));
		}
		return jtxtFilmThickness;
	}

	/**
	 * This method initializes jcboStationaryPhase	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getJcboStationaryPhase() {
		if (jcboStationaryPhase == null) {
			jcboStationaryPhase = new JComboBox(Globals.StationaryPhaseArray);
			jcboStationaryPhase.setSelectedIndex(0);
			jcboStationaryPhase.setLocation(new Point(8, 26));
			jcboStationaryPhase.setActionCommand("StationaryPhaseComboBoxChanged");
			jcboStationaryPhase.setSize(new Dimension(297, 27));
		}
		return jcboStationaryPhase;
	}

}  //  @jve:decl-index=0:visual-constraint="-34,10"
