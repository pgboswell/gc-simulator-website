package org.gcsimulator.panels;

import org.jdesktop.swingx.JXPanel;
import javax.swing.JLabel;
import java.awt.Rectangle;
import javax.swing.JSlider;
import java.awt.Font;
import javax.swing.JTextField;

public class IsothermalOptions extends JXPanel 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JLabel jlblTemperature = null;
	public JSlider jsliderTemperature = null;
	public JTextField jtxtTemperature = null;
	/**
	 * This method initializes 
	 * 
	 */
	public IsothermalOptions() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
        jlblTemperature = new JLabel();
        jlblTemperature.setBounds(new Rectangle(8, 0, 189, 16));
        jlblTemperature.setText("<html>Temperature (&deg;C):</html>");
        this.setLayout(null);
        this.setBounds(new Rectangle(0, 0, 314, 63));
        this.setPreferredSize(this.getSize());
        this.add(jlblTemperature, null);
        this.add(getJsliderTemperature(), null);
        this.add(getJtxtTemperature(), null);
			
	}

	/**
	 * This method initializes jsliderTemperature	
	 * 	
	 * @return javax.swing.JSlider	
	 */
	private JSlider getJsliderTemperature() {
		if (jsliderTemperature == null) {
			jsliderTemperature = new JSlider();
			jsliderTemperature.setBounds(new Rectangle(8, 16, 237, 43));
			jsliderTemperature.setFont(new Font("Dialog", Font.PLAIN, 12));
			jsliderTemperature.setName("Temperature Slider");
			jsliderTemperature.setMajorTickSpacing(40);
			jsliderTemperature.setMaximum(320);
			jsliderTemperature.setMinorTickSpacing(10);
			jsliderTemperature.setPaintLabels(true);
			jsliderTemperature.setPaintTicks(true);
			jsliderTemperature.setPaintTrack(true);
			jsliderTemperature.setMinimum(60);
			jsliderTemperature.setValue(40);
		}
		return jsliderTemperature;
	}

	/**
	 * This method initializes jtxtTemperature	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJtxtTemperature() {
		if (jtxtTemperature == null) {
			jtxtTemperature = new JTextField();
			jtxtTemperature.setText("60");
			jtxtTemperature.setBounds(new Rectangle(252, 16, 57, 26));
		}
		return jtxtTemperature;
	}


}  //  @jve:decl-index=0:visual-constraint="10,10"
