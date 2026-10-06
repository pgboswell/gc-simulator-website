package org.gcsimulator.panels;

import org.jdesktop.swingx.JXPanel;
import java.awt.Dimension;
import javax.swing.JLabel;
import java.awt.Rectangle;
import javax.swing.JComboBox;

import org.gcsimulator.Globals;
import javax.swing.JRadioButton;
import java.awt.Font;
import java.awt.event.KeyEvent;
import javax.swing.JTextField;

public class InletOutlet extends JXPanel 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JLabel jlblCarrierGas = null;
	public JComboBox jcboCarrierGas = null;
	private JLabel jlblGasViscosity = null;
	public JLabel jlblGasViscosityIndicator = null;
	private JLabel jlblGasViscosityUnit = null;
	public JRadioButton jrdoConstantFlowRate = null;
	public JRadioButton jrdoConstantPressure = null;
	public JLabel jlblFlowRate = null;
	public JTextField jtxtFlowRate = null;
	public JLabel jlblFlowRateUnit = null;
	public JLabel jlblPressure = null;
	public JTextField jtxtInletPressure = null;
	private JLabel jlblOutletPressure = null;
	public JRadioButton jrdoVacuum = null;
	public JRadioButton jrdoOtherPressure = null;
	public JTextField jtxtOtherPressure = null;
	public JLabel jlblOtherPressureUnit = null;
	private JLabel jlblSplitSplitless = null;
	public JRadioButton jrdoSplitless = null;
	public JRadioButton jrdoSplit = null;
	public JLabel jlblSplitRatio = null;
	public JTextField jtxtSplitRatio = null;
	public JLabel jlblSplitRatioUnits = null;
	private JLabel jlblLinerLength = null;
	public JTextField jtxtLinerLength = null;
	private JLabel jlblLinerLengthUnits = null;
	private JLabel jlblLinerInnerDiameter = null;
	public JTextField jtxtLinerInnerDiameter = null;
	private JLabel jlblLinerInnerDiameterUnits = null;
	private JLabel jlblLinerVolume = null;
	public JLabel jlblLinerVolumeIndicator = null;
	private JLabel jlblLinerVolumeUnits = null;
	private JLabel jlblInjectionVolume = null;
	public JTextField jtxtInjectionVolume = null;
	private JLabel jlblInjectionVolumeUnits = null;

	public JComboBox jcboInletPressureUnits = null;

	/**
	 * This method initializes 
	 * 
	 */
	public InletOutlet() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
        jlblLinerVolumeIndicator = new JLabel();
        jlblLinerVolumeIndicator.setBounds(new Rectangle(196, 344, 57, 16));
        jlblLinerVolumeIndicator.setText("400");
        jlblLinerVolumeIndicator.setFont(new Font("Dialog", Font.PLAIN, 12));
        jlblInjectionVolumeUnits = new JLabel();
        jlblInjectionVolumeUnits.setBounds(new Rectangle(256, 368, 53, 16));
        jlblInjectionVolumeUnits.setText("<html>&micro;L</html>");
        jlblInjectionVolume = new JLabel();
        jlblInjectionVolume.setBounds(new Rectangle(8, 368, 181, 16));
        jlblInjectionVolume.setDisplayedMnemonic(KeyEvent.VK_UNDEFINED);
        jlblInjectionVolume.setText("Injection volume:");
        jlblInjectionVolume.setVisible(true);
        jlblLinerVolumeUnits = new JLabel();
        jlblLinerVolumeUnits.setBounds(new Rectangle(256, 344, 53, 16));
        jlblLinerVolumeUnits.setText("mL");
        jlblLinerVolume = new JLabel();
        jlblLinerVolume.setBounds(new Rectangle(8, 344, 181, 16));
        jlblLinerVolume.setDisplayedMnemonic(KeyEvent.VK_UNDEFINED);
        jlblLinerVolume.setText("Liner volume:");
        jlblLinerVolume.setVisible(true);
        jlblLinerInnerDiameterUnits = new JLabel();
        jlblLinerInnerDiameterUnits.setBounds(new Rectangle(256, 320, 53, 16));
        jlblLinerInnerDiameterUnits.setText("mm");
        jlblLinerInnerDiameter = new JLabel();
        jlblLinerInnerDiameter.setBounds(new Rectangle(8, 320, 181, 16));
        jlblLinerInnerDiameter.setDisplayedMnemonic(KeyEvent.VK_UNDEFINED);
        jlblLinerInnerDiameter.setText("Liner inner diameter:");
        jlblLinerInnerDiameter.setVisible(true);
        jlblLinerLengthUnits = new JLabel();
        jlblLinerLengthUnits.setBounds(new Rectangle(256, 296, 53, 16));
        jlblLinerLengthUnits.setText("mm");
        jlblLinerLength = new JLabel();
        jlblLinerLength.setBounds(new Rectangle(8, 296, 181, 16));
        jlblLinerLength.setDisplayedMnemonic(KeyEvent.VK_UNDEFINED);
        jlblLinerLength.setText("Liner length:");
        jlblLinerLength.setVisible(true);
        jlblSplitRatioUnits = new JLabel();
        jlblSplitRatioUnits.setBounds(new Rectangle(256, 272, 53, 16));
        jlblSplitRatioUnits.setText(": 1");
        jlblSplitRatio = new JLabel();
        jlblSplitRatio.setBounds(new Rectangle(24, 272, 165, 16));
        jlblSplitRatio.setText("Split ratio:");
        jlblSplitRatio.setDisplayedMnemonic(KeyEvent.VK_UNDEFINED);
        jlblSplitSplitless = new JLabel();
        jlblSplitSplitless.setBounds(new Rectangle(8, 204, 237, 16));
        jlblSplitSplitless.setDisplayedMnemonic(KeyEvent.VK_UNDEFINED);
        jlblSplitSplitless.setText("Injection mode:");
        jlblSplitSplitless.setVisible(true);
        jlblOtherPressureUnit = new JLabel();
        jlblOtherPressureUnit.setBounds(new Rectangle(252, 176, 57, 16));
        jlblOtherPressureUnit.setEnabled(false);
        jlblOtherPressureUnit.setText("kPa");
        jlblOtherPressureUnit.setVisible(true);
        jlblOutletPressure = new JLabel();
        jlblOutletPressure.setBounds(new Rectangle(8, 156, 221, 16));
        jlblOutletPressure.setDisplayedMnemonic(KeyEvent.VK_UNDEFINED);
        jlblOutletPressure.setText("Column outlet pressure:");
        jlblOutletPressure.setVisible(true);
        jlblPressure = new JLabel();
        jlblPressure.setBounds(new Rectangle(24, 132, 161, 16));
        jlblPressure.setDisplayedMnemonic(KeyEvent.VK_UNDEFINED);
        jlblPressure.setText("Column inlet pressure:");
        jlblPressure.setEnabled(false);
        jlblFlowRateUnit = new JLabel();
        jlblFlowRateUnit.setBounds(new Rectangle(252, 88, 57, 16));
        jlblFlowRateUnit.setText("mL/min");
        jlblFlowRate = new JLabel();
        jlblFlowRate.setBounds(new Rectangle(24, 88, 161, 16));
        jlblFlowRate.setText("Flow rate:");
        jlblFlowRate.setDisplayedMnemonic(KeyEvent.VK_UNDEFINED);
        jlblGasViscosityUnit = new JLabel();
        jlblGasViscosityUnit.setBounds(new Rectangle(252, 40, 57, 16));
        jlblGasViscosityUnit.setPreferredSize(new Dimension(50, 16));
        jlblGasViscosityUnit.setText("<html>&micro;Pa s</html>");
        jlblGasViscosityUnit.setFont(new Font("Dialog", Font.PLAIN, 12));
        jlblGasViscosityIndicator = new JLabel();
        jlblGasViscosityIndicator.setBounds(new Rectangle(188, 40, 61, 16));
        jlblGasViscosityIndicator.setText("400");
        jlblGasViscosityIndicator.setFont(new Font("Dialog", Font.PLAIN, 12));
        jlblGasViscosity = new JLabel();
        jlblGasViscosity.setBounds(new Rectangle(8, 40, 173, 16));
        jlblGasViscosity.setText("Gas viscosity:");
        jlblCarrierGas = new JLabel();
        jlblCarrierGas.setBounds(new Rectangle(8, 12, 117, 16));
        jlblCarrierGas.setText("Carrier gas:");
        this.setLayout(null);
        this.setSize(new Dimension(314, 397));
        this.setPreferredSize(this.getSize());
        this.add(jlblCarrierGas, null);
        this.add(getJcboCarrierGas(), null);
        this.add(jlblGasViscosity, null);
        this.add(jlblGasViscosityIndicator, null);
        this.add(jlblGasViscosityUnit, null);
        this.add(getJrdoConstantFlowRate(), null);
        this.add(getJrdoConstantPressure(), null);
        this.add(jlblFlowRate, null);
        this.add(getJtxtFlowRate(), null);
        this.add(jlblFlowRateUnit, null);
        this.add(jlblPressure, null);
        this.add(getJtxtInletPressure(), null);
        this.add(jlblOutletPressure, null);
        this.add(getJrdoVacuum(), null);
        this.add(getJrdoOtherPressure(), null);
        this.add(getJtxtOtherPressure(), null);
        this.add(jlblOtherPressureUnit, null);
        this.add(jlblSplitSplitless, null);
        this.add(getJrdoSplitless(), null);
        this.add(getJrdoSplit(), null);
        this.add(jlblSplitRatio, null);
        this.add(getJtxtSplitRatio(), null);
        this.add(jlblSplitRatioUnits, null);
        this.add(jlblLinerLength, null);
        this.add(getJtxtLinerLength(), null);
        this.add(jlblLinerLengthUnits, null);
        this.add(jlblLinerInnerDiameter, null);
        this.add(getJtxtLinerInnerDiameter(), null);
        this.add(jlblLinerInnerDiameterUnits, null);
        this.add(jlblLinerVolume, null);
        this.add(jlblLinerVolumeUnits, null);
        this.add(jlblInjectionVolume, null);
        this.add(getJtxtInjectionVolume(), null);
        this.add(jlblInjectionVolumeUnits, null);
        this.add(jlblLinerVolumeIndicator, null);
        this.add(getJcboInletPressureUnits(), null);
			
	}

	/**
	 * This method initializes jcboCarrierGas	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getJcboCarrierGas() {
		if (jcboCarrierGas == null) {
			jcboCarrierGas = new JComboBox(Globals.CarrierGases);
			jcboCarrierGas.setBounds(new Rectangle(128, 8, 177, 25));
			jcboCarrierGas.setEnabled(true);
			jcboCarrierGas.setSelectedIndex(1);
			jcboCarrierGas.setActionCommand("CarrierGasChanged");
		}
		return jcboCarrierGas;
	}

	/**
	 * This method initializes jrdoConstantFlowRate	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getJrdoConstantFlowRate() {
		if (jrdoConstantFlowRate == null) {
			jrdoConstantFlowRate = new JRadioButton();
			jrdoConstantFlowRate.setBounds(new Rectangle(8, 64, 293, 20));
			jrdoConstantFlowRate.setText("Constant flow rate mode");
			jrdoConstantFlowRate.setSelected(true);
		}
		return jrdoConstantFlowRate;
	}

	/**
	 * This method initializes jrdoConstantPressure	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getJrdoConstantPressure() {
		if (jrdoConstantPressure == null) {
			jrdoConstantPressure = new JRadioButton();
			jrdoConstantPressure.setBounds(new Rectangle(8, 108, 237, 20));
			jrdoConstantPressure.setText("Constant pressure mode");
		}
		return jrdoConstantPressure;
	}

	/**
	 * This method initializes jtxtFlowRate	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJtxtFlowRate() {
		if (jtxtFlowRate == null) {
			jtxtFlowRate = new JTextField();
			jtxtFlowRate.setBounds(new Rectangle(188, 84, 61, 26));
			jtxtFlowRate.setText("1.0");
		}
		return jtxtFlowRate;
	}

	/**
	 * This method initializes jtxtInletPressure	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJtxtInletPressure() {
		if (jtxtInletPressure == null) {
			jtxtInletPressure = new JTextField();
			jtxtInletPressure.setBounds(new Rectangle(188, 128, 61, 26));
			jtxtInletPressure.setText("100");
			jtxtInletPressure.setEditable(true);
			jtxtInletPressure.setActionCommand("Inlet Pressure");
			jtxtInletPressure.setEnabled(false);
		}
		return jtxtInletPressure;
	}

	/**
	 * This method initializes jrdoVacuum	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getJrdoVacuum() {
		if (jrdoVacuum == null) {
			jrdoVacuum = new JRadioButton();
			jrdoVacuum.setBounds(new Rectangle(8, 176, 89, 20));
			jrdoVacuum.setText("Vacuum");
			jrdoVacuum.setSelected(true);
		}
		return jrdoVacuum;
	}

	/**
	 * This method initializes jrdoOtherPressure	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getJrdoOtherPressure() {
		if (jrdoOtherPressure == null) {
			jrdoOtherPressure = new JRadioButton();
			jrdoOtherPressure.setBounds(new Rectangle(104, 176, 81, 20));
			jrdoOtherPressure.setText("Other:");
			jrdoOtherPressure.setActionCommand("OtherPressure");
		}
		return jrdoOtherPressure;
	}

	/**
	 * This method initializes jtxtOtherPressure	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJtxtOtherPressure() {
		if (jtxtOtherPressure == null) {
			jtxtOtherPressure = new JTextField();
			jtxtOtherPressure.setBounds(new Rectangle(188, 172, 61, 26));
			jtxtOtherPressure.setText("101.325");
			jtxtOtherPressure.setEnabled(false);
		}
		return jtxtOtherPressure;
	}

	/**
	 * This method initializes jrdoSplitless	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getJrdoSplitless() {
		if (jrdoSplitless == null) {
			jrdoSplitless = new JRadioButton();
			jrdoSplitless.setBounds(new Rectangle(8, 224, 160, 18));
			jrdoSplitless.setSelected(false);
			jrdoSplitless.setText("Splitless injection");
		}
		return jrdoSplitless;
	}

	/**
	 * This method initializes jrdoSplit	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getJrdoSplit() {
		if (jrdoSplit == null) {
			jrdoSplit = new JRadioButton();
			jrdoSplit.setBounds(new Rectangle(8, 248, 119, 18));
			jrdoSplit.setSelected(true);
			jrdoSplit.setText("Split injection");
		}
		return jrdoSplit;
	}

	/**
	 * This method initializes jtxtSplitRatio	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJtxtSplitRatio() {
		if (jtxtSplitRatio == null) {
			jtxtSplitRatio = new JTextField();
			jtxtSplitRatio.setBounds(new Rectangle(192, 268, 61, 26));
			jtxtSplitRatio.setText("10");
		}
		return jtxtSplitRatio;
	}

	/**
	 * This method initializes jtxtLinerLength	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJtxtLinerLength() {
		if (jtxtLinerLength == null) {
			jtxtLinerLength = new JTextField();
			jtxtLinerLength.setBounds(new Rectangle(192, 292, 61, 26));
			jtxtLinerLength.setText("4");
		}
		return jtxtLinerLength;
	}

	/**
	 * This method initializes jtxtLinerInnerDiameter	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJtxtLinerInnerDiameter() {
		if (jtxtLinerInnerDiameter == null) {
			jtxtLinerInnerDiameter = new JTextField();
			jtxtLinerInnerDiameter.setBounds(new Rectangle(192, 316, 61, 26));
			jtxtLinerInnerDiameter.setText(".2");
		}
		return jtxtLinerInnerDiameter;
	}

	/**
	 * This method initializes jtxtInjectionVolume	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJtxtInjectionVolume() {
		if (jtxtInjectionVolume == null) {
			jtxtInjectionVolume = new JTextField();
			jtxtInjectionVolume.setBounds(new Rectangle(192, 364, 61, 26));
			jtxtInjectionVolume.setText("1");
		}
		return jtxtInjectionVolume;
	}

	/**
	 * This method initializes jcboInletPressureUnits	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getJcboInletPressureUnits() {
		if (jcboInletPressureUnits == null) {
			jcboInletPressureUnits = new JComboBox(new String[]{"kPag", "kPa"});
			jcboInletPressureUnits.setBounds(new Rectangle(248, 128, 61, 25));
			jcboInletPressureUnits.setActionCommand("Inlet pressure units changed");
		}
		return jcboInletPressureUnits;
	}


}  //  @jve:decl-index=0:visual-constraint="10,10"
