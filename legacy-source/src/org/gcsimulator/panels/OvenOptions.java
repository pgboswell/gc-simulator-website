package org.gcsimulator.panels;

import org.jdesktop.swingx.JXPanel;
import java.awt.Rectangle;

import javax.swing.JRadioButton;

public class OvenOptions extends JXPanel 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public JRadioButton jrdoIsothermalElution = null;
	public JRadioButton jrdoProgrammedTemperatureElution = null;

	/**
	 * This method initializes 
	 * 
	 */
	public OvenOptions() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
        this.setLayout(null);
        this.setBounds(new Rectangle(0, 0, 314, 57));
        this.setPreferredSize(this.getSize());
        this.add(getJrdoIsothermalElution(), null);
        this.add(getJrdoProgrammedTemperatureElution(), null);
			
	}

	/**
	 * This method initializes jrdoIsothermalElution	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getJrdoIsothermalElution() {
		if (jrdoIsothermalElution == null) {
			jrdoIsothermalElution = new JRadioButton();
			jrdoIsothermalElution.setBounds(new Rectangle(8, 8, 233, 17));
			jrdoIsothermalElution.setText("Isothermal elution mode");
			jrdoIsothermalElution.setSelected(true);
		}
		return jrdoIsothermalElution;
	}

	/**
	 * This method initializes jrdoProgrammedTemperatureElution	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getJrdoProgrammedTemperatureElution() {
		if (jrdoProgrammedTemperatureElution == null) {
			jrdoProgrammedTemperatureElution = new JRadioButton();
			jrdoProgrammedTemperatureElution.setBounds(new Rectangle(8, 32, 297, 17));
			jrdoProgrammedTemperatureElution.setSelected(false);
			jrdoProgrammedTemperatureElution.setText("Programmed-temperature elution mode");
			jrdoProgrammedTemperatureElution.setActionCommand("Programmed temperature elution mode");
			jrdoProgrammedTemperatureElution.setRolloverEnabled(true);
		}
		return jrdoProgrammedTemperatureElution;
	}


}  //  @jve:decl-index=0:visual-constraint="10,10"
