package org.gcsimulator.panels;

import org.jdesktop.swingx.JXPanel;

import java.awt.Color;
import javax.swing.JLabel;
import java.awt.Rectangle;
import javax.swing.JComboBox;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import java.awt.Font;
import java.util.Hashtable;

import javax.swing.JCheckBox;
import javax.swing.JSlider;

public class PlotOptions extends JXPanel 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel jlblSecondPlot = null;
	public JRadioButton jrdoNoPlot = null;
	public JRadioButton jrdoFlowVelocity = null;
	public JRadioButton jrdoMobilePhaseViscosity = null;
	public JRadioButton jrdoMobilePhaseDensity = null;
	public JRadioButton jrdoRetentionFactor = null;
	public JRadioButton jrdoPosition = null;
	public JRadioButton jrdoPressure = null;
	public JComboBox jcboRetentionFactorCompounds = null;
	public JComboBox jcboPositionCompounds = null;
	private JLabel jlblTimeConstant = null;
	public JTextField jtxtTimeConstant = null;
	private JLabel jlblTimeConstantUnits = null;
	private JLabel jlblSignalOffset = null;
	public JTextField jtxtSignalOffset = null;
	private JLabel jlblSignalOffsetUnits = null;
	private JLabel jlblNoise = null;
	public JTextField jtxtNoise = null;
	public JCheckBox jchkAutoTimeRange = null;
	private JLabel jlblInitialTime = null;
	public JTextField jtxtInitialTime = null;
	private JLabel jlblInitialTimeUnits = null;
	private JLabel jlblFinalTime = null;
	public JTextField jtxtFinalTime = null;
	private JLabel jlblFinalTimeUnits = null;
	private JLabel jlblNumPoints = null;
	public JTextField jtxtNumPoints = null;
	public JRadioButton jrdoTemperature = null;
	public JRadioButton jrdoHoldUpTime = null;
	public JSlider jsliderFlowVelocityPosition = null;
	public JTextField jtxtFlowVelocityColumnPosition = null;
	public JSlider jsliderDensityPosition = null;
	public JTextField jtxtDensityColumnPosition = null;
	public JSlider jsliderPressurePosition = null;
	public JTextField jtxtPressurePosition = null;
	/**
	 * This method initializes 
	 * 
	 */
	public PlotOptions() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
        jlblNumPoints = new JLabel();
        jlblNumPoints.setBounds(new Rectangle(8, 600, 181, 16));
        jlblNumPoints.setText("Plot points:");
        jlblFinalTimeUnits = new JLabel();
        jlblFinalTimeUnits.setBounds(new Rectangle(256, 576, 49, 16));
        jlblFinalTimeUnits.setText("s");
        jlblFinalTimeUnits.setFont(new Font("Dialog", Font.PLAIN, 12));
        jlblFinalTime = new JLabel();
        jlblFinalTime.setBounds(new Rectangle(8, 576, 181, 16));
        jlblFinalTime.setText("Final time:");
        jlblInitialTimeUnits = new JLabel();
        jlblInitialTimeUnits.setBounds(new Rectangle(256, 552, 49, 16));
        jlblInitialTimeUnits.setText("s");
        jlblInitialTimeUnits.setFont(new Font("Dialog", Font.PLAIN, 12));
        jlblInitialTime = new JLabel();
        jlblInitialTime.setBounds(new Rectangle(8, 552, 181, 16));
        jlblInitialTime.setText("Initial time:");
        jlblNoise = new JLabel();
        jlblNoise.setBounds(new Rectangle(8, 500, 181, 16));
        jlblNoise.setText("Noise:");
        jlblSignalOffsetUnits = new JLabel();
        jlblSignalOffsetUnits.setBounds(new Rectangle(256, 476, 49, 16));
        jlblSignalOffsetUnits.setText("munits");
        jlblSignalOffsetUnits.setFont(new Font("Dialog", Font.PLAIN, 12));
        jlblSignalOffset = new JLabel();
        jlblSignalOffset.setBounds(new Rectangle(8, 476, 181, 16));
        jlblSignalOffset.setText("Signal offset:");
        jlblTimeConstantUnits = new JLabel();
        jlblTimeConstantUnits.setBounds(new Rectangle(256, 452, 50, 16));
        jlblTimeConstantUnits.setText("s");
        jlblTimeConstantUnits.setFont(new Font("Dialog", Font.PLAIN, 12));
        jlblTimeConstant = new JLabel();
        jlblTimeConstant.setBounds(new Rectangle(8, 452, 181, 16));
        jlblTimeConstant.setText("Time constant:");
        jlblSecondPlot = new JLabel();
        jlblSecondPlot.setBounds(new Rectangle(8, 8, 129, 16));
        jlblSecondPlot.setText("Second Plot:");
        this.setLayout(null);
        this.setBounds(new Rectangle(0, 0, 314, 630));
        this.setPreferredSize(this.getSize());
        this.setBackground(new Color(214, 217, 223));
        this.add(jlblSecondPlot, null);
        this.add(getJrdoNoPlot(), null);
        this.add(getJrdoFlowVelocity(), null);
        this.add(getJrdoMobilePhaseViscosity(), null);
        this.add(getJrdoMobilePhaseDensity(), null);
        this.add(getJrdoRetentionFactor(), null);
        this.add(getJrdoPosition(), null);
        this.add(getJcboRetentionFactorCompounds(), null);
        this.add(getJcboPositionCompounds(), null);
        this.add(getJrdoPressure(), null);
        this.add(jlblTimeConstant, null);
        this.add(getJtxtTimeConstant(), null);
        this.add(jlblTimeConstantUnits, null);
        this.add(jlblSignalOffset, null);
        this.add(getJtxtSignalOffset(), null);
        this.add(jlblSignalOffsetUnits, null);
        this.add(jlblNoise, null);
        this.add(getJtxtNoise(), null);
        this.add(getJchkAutoTimeRange(), null);
        this.add(jlblInitialTime, null);
        this.add(getJtxtInitialTime(), null);
        this.add(jlblInitialTimeUnits, null);
        this.add(jlblFinalTime, null);
        this.add(getJtxtFinalTime(), null);
        this.add(jlblFinalTimeUnits, null);
        this.add(jlblNumPoints, null);
        this.add(getJtxtNumPoints(), null);
        this.add(getJrdoTemperature(), null);
        this.add(getJrdoHoldUpTime(), null);
        this.add(getJsliderFlowVelocityPosition(), null);
        this.add(getJtxtFlowVelocityColumnPosition(), null);
        this.add(getJsliderDensityPosition(), null);
        this.add(getJtxtDensityColumnPosition(), null);
        this.add(getJsliderPressurePosition(), null);
        this.add(getJtxtPressurePosition(), null);
			
	}

	/**
	 * This method initializes jrdoNoPlot	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getJrdoNoPlot() {
		if (jrdoNoPlot == null) {
			jrdoNoPlot = new JRadioButton();
			jrdoNoPlot.setBounds(new Rectangle(8, 28, 297, 17));
			jrdoNoPlot.setText("No plot");
			jrdoNoPlot.setSelected(true);
		}
		return jrdoNoPlot;
	}

	/**
	 * This method initializes jrdoFlowVelocity	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getJrdoFlowVelocity() {
		if (jrdoFlowVelocity == null) {
			jrdoFlowVelocity = new JRadioButton();
			jrdoFlowVelocity.setBounds(new Rectangle(8, 124, 297, 17));
			jrdoFlowVelocity.setText("Flow velocity at column position (z/L):");
			jrdoFlowVelocity.setActionCommand("Plot flow velocity");
		}
		return jrdoFlowVelocity;
	}

	/**
	 * This method initializes jrdoMobilePhaseViscosity	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getJrdoMobilePhaseViscosity() {
		if (jrdoMobilePhaseViscosity == null) {
			jrdoMobilePhaseViscosity = new JRadioButton();
			jrdoMobilePhaseViscosity.setBounds(new Rectangle(8, 100, 297, 17));
			jrdoMobilePhaseViscosity.setText("Gas viscosity");
			jrdoMobilePhaseViscosity.setActionCommand("Plot mobile phase viscosity");
		}
		return jrdoMobilePhaseViscosity;
	}

	/**
	 * This method initializes jrdoMobilePhaseDensity	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getJrdoMobilePhaseDensity() {
		if (jrdoMobilePhaseDensity == null) {
			jrdoMobilePhaseDensity = new JRadioButton();
			jrdoMobilePhaseDensity.setBounds(new Rectangle(8, 200, 301, 17));
			jrdoMobilePhaseDensity.setText("Gas density at column position (z/L):");
			jrdoMobilePhaseDensity.setActionCommand("Plot mobile phase density");
		}
		return jrdoMobilePhaseDensity;
	}

	/**
	 * This method initializes jrdoRetentionFactor	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getJrdoRetentionFactor() {
		if (jrdoRetentionFactor == null) {
			jrdoRetentionFactor = new JRadioButton();
			jrdoRetentionFactor.setBounds(new Rectangle(8, 348, 301, 17));
			jrdoRetentionFactor.setText("Retention factor of:");
			jrdoRetentionFactor.setActionCommand("Plot retention factor");
		}
		return jrdoRetentionFactor;
	}

	/**
	 * This method initializes jrdoPosition	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getJrdoPosition() {
		if (jrdoPosition == null) {
			jrdoPosition = new JRadioButton();
			jrdoPosition.setBounds(new Rectangle(8, 396, 233, 17));
			jrdoPosition.setActionCommand("Plot position");
			jrdoPosition.setText("Position along column of:");
		}
		return jrdoPosition;
	}

	/**
	 * This method initializes jcboRetentionFactorCompounds	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getJcboRetentionFactorCompounds() {
		if (jcboRetentionFactorCompounds == null) {
			jcboRetentionFactorCompounds = new JComboBox();
			jcboRetentionFactorCompounds.setBounds(new Rectangle(32, 368, 273, 25));
			jcboRetentionFactorCompounds.setEnabled(false);
			jcboRetentionFactorCompounds.setActionCommand("RetentionFactorCompoundChanged");
		}
		return jcboRetentionFactorCompounds;
	}

	/**
	 * This method initializes jcboPositionCompounds	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getJcboPositionCompounds() {
		if (jcboPositionCompounds == null) {
			jcboPositionCompounds = new JComboBox();
			jcboPositionCompounds.setBounds(new Rectangle(32, 416, 273, 25));
			jcboPositionCompounds.setEnabled(false);
			jcboPositionCompounds.setActionCommand("PositionCompoundChanged");
		}
		return jcboPositionCompounds;
	}

	/**
	 * This method initializes jrdoPressure	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getJrdoPressure() {
		if (jrdoPressure == null) {
			jrdoPressure = new JRadioButton();
			jrdoPressure.setBounds(new Rectangle(8, 272, 301, 18));
			jrdoPressure.setText("Pressure at column position (z/L):");
			jrdoPressure.setActionCommand("Plot pressure");
		}
		return jrdoPressure;
	}

	/**
	 * This method initializes jtxtTimeConstant	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJtxtTimeConstant() {
		if (jtxtTimeConstant == null) {
			jtxtTimeConstant = new JTextField();
			jtxtTimeConstant.setBounds(new Rectangle(192, 448, 61, 26));
			jtxtTimeConstant.setText("0.1");
		}
		return jtxtTimeConstant;
	}

	/**
	 * This method initializes jtxtSignalOffset	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJtxtSignalOffset() {
		if (jtxtSignalOffset == null) {
			jtxtSignalOffset = new JTextField();
			jtxtSignalOffset.setBounds(new Rectangle(192, 472, 61, 26));
			jtxtSignalOffset.setText("0");
		}
		return jtxtSignalOffset;
	}

	/**
	 * This method initializes jtxtNoise	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJtxtNoise() {
		if (jtxtNoise == null) {
			jtxtNoise = new JTextField();
			jtxtNoise.setBounds(new Rectangle(192, 496, 61, 26));
			jtxtNoise.setText("2");
		}
		return jtxtNoise;
	}

	/**
	 * This method initializes jchkAutoTimeRange	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getJchkAutoTimeRange() {
		if (jchkAutoTimeRange == null) {
			jchkAutoTimeRange = new JCheckBox();
			jchkAutoTimeRange.setBounds(new Rectangle(8, 524, 301, 20));
			jchkAutoTimeRange.setSelected(true);
			jchkAutoTimeRange.setText("Automatically determine time span");
			jchkAutoTimeRange.setName("jchkAutoTimeRange");
		}
		return jchkAutoTimeRange;
	}

	/**
	 * This method initializes jtxtInitialTime	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJtxtInitialTime() {
		if (jtxtInitialTime == null) {
			jtxtInitialTime = new JTextField();
			jtxtInitialTime.setBounds(new Rectangle(192, 548, 61, 26));
			jtxtInitialTime.setText("0");
			jtxtInitialTime.setEnabled(false);
		}
		return jtxtInitialTime;
	}

	/**
	 * This method initializes jtxtFinalTime	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJtxtFinalTime() {
		if (jtxtFinalTime == null) {
			jtxtFinalTime = new JTextField();
			jtxtFinalTime.setBounds(new Rectangle(192, 572, 61, 26));
			jtxtFinalTime.setText("0");
			jtxtFinalTime.setEnabled(false);
		}
		return jtxtFinalTime;
	}

	/**
	 * This method initializes jtxtNumPoints	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJtxtNumPoints() {
		if (jtxtNumPoints == null) {
			jtxtNumPoints = new JTextField();
			jtxtNumPoints.setBounds(new Rectangle(192, 596, 61, 26));
			jtxtNumPoints.setText("3000");
		}
		return jtxtNumPoints;
	}

	/**
	 * This method initializes jrdoTemperature	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getJrdoTemperature() {
		if (jrdoTemperature == null) {
			jrdoTemperature = new JRadioButton();
			jrdoTemperature.setBounds(new Rectangle(8, 52, 297, 17));
			jrdoTemperature.setText("Temperature");
			jrdoTemperature.setActionCommand("Plot temperature");
		}
		return jrdoTemperature;
	}

	/**
	 * This method initializes jrdoHoldUpTime	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getJrdoHoldUpTime() {
		if (jrdoHoldUpTime == null) {
			jrdoHoldUpTime = new JRadioButton();
			jrdoHoldUpTime.setBounds(new Rectangle(8, 76, 297, 17));
			jrdoHoldUpTime.setText("Hold-up time");
			jrdoHoldUpTime.setActionCommand("Plot hold-up time");
		}
		return jrdoHoldUpTime;
	}

	/**
	 * This method initializes jsliderFlowVelocityPosition	
	 * 	
	 * @return javax.swing.JSlider	
	 */
	private JSlider getJsliderFlowVelocityPosition() {
		if (jsliderFlowVelocityPosition == null) {
			jsliderFlowVelocityPosition = new JSlider();
			jsliderFlowVelocityPosition.setBounds(new Rectangle(28, 148, 221, 45));
			jsliderFlowVelocityPosition.setName("Plot flow velocity position slider");
			jsliderFlowVelocityPosition.setMajorTickSpacing(500);
			jsliderFlowVelocityPosition.setMaximum(1000);
			jsliderFlowVelocityPosition.setMinimum(0);
			jsliderFlowVelocityPosition.setMinorTickSpacing(100);
			jsliderFlowVelocityPosition.setPaintLabels(true);
			jsliderFlowVelocityPosition.setPaintTicks(true);
			jsliderFlowVelocityPosition.setPaintTrack(true);
			jsliderFlowVelocityPosition.setEnabled(false);
			jsliderFlowVelocityPosition.setValue(0);
			Hashtable labelTable = new Hashtable();
			labelTable.put( new Integer( 0 ), new JLabel("0.0") );
			labelTable.put( new Integer( 500 ), new JLabel("0.5") );
			labelTable.put( new Integer( 1000 ), new JLabel("1.0") );
			jsliderFlowVelocityPosition.setLabelTable( labelTable );
			jsliderFlowVelocityPosition.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return jsliderFlowVelocityPosition;
	}

	/**
	 * This method initializes jtxtFlowVelocityColumnPosition	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJtxtFlowVelocityColumnPosition() {
		if (jtxtFlowVelocityColumnPosition == null) {
			jtxtFlowVelocityColumnPosition = new JTextField();
			jtxtFlowVelocityColumnPosition.setBounds(new Rectangle(252, 152, 57, 26));
			jtxtFlowVelocityColumnPosition.setEnabled(false);
			jtxtFlowVelocityColumnPosition.setText("0");
		}
		return jtxtFlowVelocityColumnPosition;
	}

	/**
	 * This method initializes jsliderDensityPosition	
	 * 	
	 * @return javax.swing.JSlider	
	 */
	private JSlider getJsliderDensityPosition() {
		if (jsliderDensityPosition == null) {
			jsliderDensityPosition = new JSlider();
			jsliderDensityPosition.setBounds(new Rectangle(28, 220, 221, 45));
			jsliderDensityPosition.setName("Plot gas density position slider");
			jsliderDensityPosition.setMajorTickSpacing(500);
			jsliderDensityPosition.setMaximum(1000);
			jsliderDensityPosition.setMinimum(0);
			jsliderDensityPosition.setMinorTickSpacing(100);
			jsliderDensityPosition.setPaintLabels(true);
			jsliderDensityPosition.setPaintTicks(true);
			jsliderDensityPosition.setPaintTrack(true);
			jsliderDensityPosition.setEnabled(false);
			jsliderDensityPosition.setValue(0);
			Hashtable labelTable = new Hashtable();
			labelTable.put( new Integer( 0 ), new JLabel("0.0") );
			labelTable.put( new Integer( 500 ), new JLabel("0.5") );
			labelTable.put( new Integer( 1000 ), new JLabel("1.0") );
			jsliderDensityPosition.setLabelTable( labelTable );
			jsliderDensityPosition.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return jsliderDensityPosition;
	}

	/**
	 * This method initializes jtxtDensityColumnPosition	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJtxtDensityColumnPosition() {
		if (jtxtDensityColumnPosition == null) {
			jtxtDensityColumnPosition = new JTextField();
			jtxtDensityColumnPosition.setBounds(new Rectangle(252, 224, 57, 26));
			jtxtDensityColumnPosition.setEnabled(false);
			jtxtDensityColumnPosition.setText("0");
		}
		return jtxtDensityColumnPosition;
	}

	/**
	 * This method initializes jsliderPressurePosition	
	 * 	
	 * @return javax.swing.JSlider	
	 */
	private JSlider getJsliderPressurePosition() {
		if (jsliderPressurePosition == null) {
			jsliderPressurePosition = new JSlider();
			jsliderPressurePosition.setBounds(new Rectangle(28, 296, 221, 45));
			jsliderPressurePosition.setName("Plot pressure position slider");
			jsliderPressurePosition.setMajorTickSpacing(500);
			jsliderPressurePosition.setMaximum(1000);
			jsliderPressurePosition.setMinimum(0);
			jsliderPressurePosition.setMinorTickSpacing(100);
			jsliderPressurePosition.setPaintLabels(true);
			jsliderPressurePosition.setPaintTicks(true);
			jsliderPressurePosition.setPaintTrack(true);
			jsliderPressurePosition.setEnabled(false);
			jsliderPressurePosition.setValue(0);
			Hashtable labelTable = new Hashtable();
			labelTable.put( new Integer( 0 ), new JLabel("0.0") );
			labelTable.put( new Integer( 500 ), new JLabel("0.5") );
			labelTable.put( new Integer( 1000 ), new JLabel("1.0") );
			jsliderPressurePosition.setLabelTable( labelTable );
			jsliderPressurePosition.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return jsliderPressurePosition;
	}

	/**
	 * This method initializes jtxtPressurePosition	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJtxtPressurePosition() {
		if (jtxtPressurePosition == null) {
			jtxtPressurePosition = new JTextField();
			jtxtPressurePosition.setBounds(new Rectangle(252, 300, 57, 28));
			jtxtPressurePosition.setEnabled(false);
			jtxtPressurePosition.setText("0");
		}
		return jtxtPressurePosition;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
