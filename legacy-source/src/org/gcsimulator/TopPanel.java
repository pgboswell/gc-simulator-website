package org.gcsimulator;

import org.gcsimulator.panels.*;

import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Insets;
import java.awt.Rectangle;
import javax.media.opengl.*;
import java.awt.Point;
import javax.swing.JButton;
import java.awt.Dimension;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.table.DefaultTableModel;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.Scrollable;

import javax.swing.JToggleButton;
import javax.swing.ImageIcon;

import org.jdesktop.swingx.*;

import java.awt.GridLayout;

public class TopPanel extends JPanel implements Scrollable, ComponentListener
{
	private static final long serialVersionUID = 1L;
	public GraphControl m_GraphControl = null;
	private JScrollPane jScrollPane = null;
	public JXTaskPaneContainer taskpanecontainer = null;
	//public JXPanel jControlPanel = null;
	public JScrollPane jsclControlPanel = null;
	public JTable jtableChemicals = null;
	public JButton jbtnAddChemical = null;
	public JButton jbtnEditChemical = null;
	public JButton jbtnRemoveChemical = null;
	public DefaultTableModel tabModel;
	
	public Vector<String> vectColumnNames = new Vector<String>();
	public Vector<Vector<String>> vectChemicalRows = new Vector<Vector<String>>();  //  @jve:decl-index=0:
	
	private JPanel jpanelCompounds = null;
	public JToggleButton jbtnAutoscale = null;
	public JToggleButton jbtnZoomIn = null;
	public JToggleButton jbtnZoomOut = null;
	public JToggleButton jbtnPan = null;
	private JPanel jpanelSimulatedChromatogram = null;
	public JToggleButton jbtnAutoscaleX = null;
	public JToggleButton jbtnAutoscaleY = null;
	public JButton jbtnHelp = null;
	public JButton jbtnContextHelp = null;
	public JButton jbtnTutorials = null;
	
	public InletOutlet jxpanelInletOutlet = null;
	public JXTaskPane jxtaskInletOutlet = null;
	
	public ColumnProperties jxpanelColumnProperties = null;
	public JXTaskPane jxtaskColumnProperties = null;

	public IsothermalOptions jxpanelIsothermalOptions = null;
	public JXTaskPane jxtaskIsothermalOptions = null;

	public TemperatureProgramOptions jxpanelTemperatureProgramOptions = null;
	public JXTaskPane jxtaskTemperatureProgramOptions = null;

	public OvenOptions jxpanelOvenOptions = null;
	public JXTaskPane jxtaskOvenOptions = null;

	public PlotOptions jxpanelPlotOptions = null;
	public JXTaskPane jxtaskPlotOptions = null;
	
	public JButton jbtnCopyImage = null;
	private JPanel jPanelGraphControl = null;
	/**
	 * This is the default constructor
	 */
	public TopPanel() {
		super();
		initialize();
	}

	
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() 
	{
		this.setLayout(null);
		
		this.setVisible(true);
		this.setPreferredSize(new Dimension(900, 500));
        this.setMinimumSize(new Dimension(900, 500));
		//this.setSize(new Dimension(1000, 900));
		
        GLCapabilities caps = new GLCapabilities(GLProfile.get(GLProfile.GL2));
        caps.setDoubleBuffered(true);
        caps.setHardwareAccelerated(true);
        
		m_GraphControl = new GraphControl(caps);
		m_GraphControl.setYAxisTitle("Signal");
		m_GraphControl.setYAxisBaseUnit("moles/liter", "mol/L");

	    this.add(getJpanelCompounds(), null);
	    this.add(getJpanelSimulatedChromatogram(), null);
	    this.add(getControlPanel(), null);

	    // TODO: Help connections
	    /*CSH.setHelpIDString(jxpanelInletOutlet, "panel.mobilephasecomposition");
	    CSH.setHelpIDString(jxpanelInletOutlet.jlblSolventA, "controls.solventa");
	    CSH.setHelpIDString(jxpanelInletOutlet.jcboSolventA, "controls.solventa");
	    CSH.setHelpIDString(jxpanelInletOutlet.jlblSolventB, "controls.solventb");
	    CSH.setHelpIDString(jxpanelInletOutlet.jcboSolventB, "controls.solventb");
	    CSH.setHelpIDString(jxpanelInletOutlet.jrdoIsocraticElution, "controls.isocraticelutionmode");
	    CSH.setHelpIDString(jxpanelInletOutlet.jrdoGradientElution, "controls.gradientelutionmode");
	    CSH.setHelpIDString(jxpanelIsocraticOptions.jtxtSolventBFraction, "controls.solventbfraction");
	    CSH.setHelpIDString(jxpanelIsocraticOptions.jlblSolventBFraction, "controls.solventbfraction");
	    CSH.setHelpIDString(jxpanelIsocraticOptions.jsliderSolventBFraction, "controls.solventbfraction");
	    CSH.setHelpIDString(jxpanelGradientOptions.jtableGradientProgram, "controls.gradientprogramtable");
	    CSH.setHelpIDString(jxpanelGradientOptions.jbtnInsertRow, "controls.gradientprogramtable");
	    CSH.setHelpIDString(jxpanelGradientOptions.jbtnRemoveRow, "controls.gradientprogramtable");
	    CSH.setHelpIDString(jxpanelGradientOptions.jtxtMixingVolume, "controls.mixingvolume");
	    CSH.setHelpIDString(jxpanelGradientOptions.jlblMixingVolume, "controls.mixingvolume");
	    CSH.setHelpIDString(jxpanelGradientOptions.jlblMixingVolumeUnit, "controls.mixingvolume");
	    CSH.setHelpIDString(jxpanelGradientOptions.jlblNonMixingVolume, "controls.nonmixingvolume");
	    CSH.setHelpIDString(jxpanelGradientOptions.jtxtNonMixingVolume, "controls.nonmixingvolume");
	    CSH.setHelpIDString(jxpanelGradientOptions.jlblNonMixingVolumeUnit, "controls.nonmixingvolume");
	    CSH.setHelpIDString(jxpanelGradientOptions.jlblDwellVolumeIndicator, "controls.totaldwellvolume");
	    CSH.setHelpIDString(jxpanelGradientOptions.jlblDwellVolume, "controls.totaldwellvolume");
	    CSH.setHelpIDString(jxpanelGradientOptions.jlblDwellVolumeUnit, "controls.totaldwellvolume");
	    CSH.setHelpIDString(jxpanelGradientOptions.jlblDwellTimeIndicator, "controls.dwelltime");
	    CSH.setHelpIDString(jxpanelGradientOptions.jlblDwellTime, "controls.dwelltime");
	    CSH.setHelpIDString(jxpanelGradientOptions.jlblDwellTimeUnit, "controls.dwelltime");
	    CSH.setHelpIDString(jxpanelChromatographyProperties, "panel.chromatographicproperties");
	    CSH.setHelpIDString(jxpanelChromatographyProperties.jtxtTemp, "controls.temperature");
	    CSH.setHelpIDString(jxpanelChromatographyProperties.jlblTemperature, "controls.temperature");
	    CSH.setHelpIDString(jxpanelChromatographyProperties.jsliderTemp, "controls.temperature");
	    CSH.setHelpIDString(jxpanelChromatographyProperties.jlblInjectionVolume, "controls.injectionvolume");
	    CSH.setHelpIDString(jxpanelChromatographyProperties.jtxtInjectionVolume, "controls.injectionvolume");
	    CSH.setHelpIDString(jxpanelChromatographyProperties.jlblInjectionVolume2, "controls.injectionvolume");
	    CSH.setHelpIDString(jxpanelChromatographyProperties.jlblFlowRate, "controls.flowrate");
	    CSH.setHelpIDString(jxpanelChromatographyProperties.jtxtFlowRate, "controls.flowrate");
	    CSH.setHelpIDString(jxpanelChromatographyProperties.jlblFlowRate2, "controls.flowrate");
	    CSH.setHelpIDString(jxpanelChromatographyProperties.jlblOpenTubeVelocity, "controls.flowvelocity");
	    CSH.setHelpIDString(jxpanelChromatographyProperties.jlblOpenTubeVelocity2, "controls.flowvelocity");
	    CSH.setHelpIDString(jxpanelChromatographyProperties.jlblOpenTubeVelocity3, "controls.flowvelocity");
	    CSH.setHelpIDString(jxpanelChromatographyProperties.jlblInterstitialVelocity, "controls.flowvelocity");
	    CSH.setHelpIDString(jxpanelChromatographyProperties.jlblInterstitialVelocity2, "controls.flowvelocity");
	    CSH.setHelpIDString(jxpanelChromatographyProperties.jlblInterstitialVelocityUnits, "controls.flowvelocity");
	    CSH.setHelpIDString(jxpanelChromatographyProperties.jlblChromatographicVelocity, "controls.flowvelocity");
	    CSH.setHelpIDString(jxpanelChromatographyProperties.jlblChromatographicVelocity2, "controls.flowvelocity");
	    CSH.setHelpIDString(jxpanelChromatographyProperties.jlblChromatographicVelocityUnits, "controls.flowvelocity");
	    CSH.setHelpIDString(jxpanelChromatographyProperties.jlblReducedVelocity, "controls.flowvelocity");
	    CSH.setHelpIDString(jxpanelChromatographyProperties.jlblReducedVelocity2, "controls.flowvelocity");
	    CSH.setHelpIDString(jxpanelChromatographyProperties.jlblHETP, "controls.hetp");
	    CSH.setHelpIDString(jxpanelChromatographyProperties.jlblHETP2, "controls.hetp");
	    CSH.setHelpIDString(jxpanelChromatographyProperties.jlblHETP3, "controls.hetp");
	    CSH.setHelpIDString(jxpanelChromatographyProperties.jlblTheoreticalPlates, "controls.theoreticalplates");
	    CSH.setHelpIDString(jxpanelChromatographyProperties.jlblTheoreticalPlates2, "controls.theoreticalplates");
	    CSH.setHelpIDString(jxpanelChromatographyProperties.jlblBackpressure, "controls.backpressure");
	    CSH.setHelpIDString(jxpanelChromatographyProperties.jlblBackpressure2, "controls.backpressure");
	    CSH.setHelpIDString(jxpanelChromatographyProperties.jlblBackpressure3, "controls.backpressure");
	    CSH.setHelpIDString(jxpanelGeneralProperties, "panel.generalproperties");
	    CSH.setHelpIDString(jxpanelGeneralProperties.jlblEluentViscosity, "controls.eluentviscosity");
	    CSH.setHelpIDString(jxpanelGeneralProperties.jlblEluentViscosity2, "controls.eluentviscosity");
	    CSH.setHelpIDString(jxpanelGeneralProperties.jlblEluentViscosity3, "controls.eluentviscosity");
	    CSH.setHelpIDString(jxpanelGeneralProperties.jlblDiffusionCoefficient, "controls.diffusioncoefficient");
	    CSH.setHelpIDString(jxpanelGeneralProperties.jlblDiffusionCoefficient2, "controls.diffusioncoefficient");
	    CSH.setHelpIDString(jxpanelGeneralProperties.jlblDiffusionCoefficient3, "controls.diffusioncoefficient");
	    CSH.setHelpIDString(jxpanelGeneralProperties.jtxtTimeConstant, "controls.timeconstant");
	    CSH.setHelpIDString(jxpanelGeneralProperties.jlblTimeConstant, "controls.timeconstant");
	    CSH.setHelpIDString(jxpanelGeneralProperties.jlblTimeConstant2, "controls.timeconstant");
	    CSH.setHelpIDString(jxpanelGeneralProperties.jtxtSignalOffset, "controls.signaloffset");
	    CSH.setHelpIDString(jxpanelGeneralProperties.jlblSignalOffset, "controls.signaloffset");
	    CSH.setHelpIDString(jxpanelGeneralProperties.jlblSignalOffset2, "controls.signaloffset");
	    CSH.setHelpIDString(jxpanelGeneralProperties.jtxtNoise, "controls.noise");
	    CSH.setHelpIDString(jxpanelGeneralProperties.jlblNoise, "controls.noise");
	    CSH.setHelpIDString(jxpanelGeneralProperties.jlblNoise2, "controls.noise");
	    CSH.setHelpIDString(jxpanelGeneralProperties.jchkAutoTimeRange, "controls.automatictimespan");
	    CSH.setHelpIDString(jxpanelGeneralProperties.jtxtInitialTime, "controls.initialtime");
	    CSH.setHelpIDString(jxpanelGeneralProperties.jlblInitialTime, "controls.initialtime");
	    CSH.setHelpIDString(jxpanelGeneralProperties.jlblInitialTime2, "controls.initialtime");
	    CSH.setHelpIDString(jxpanelGeneralProperties.jtxtFinalTime, "controls.finaltime");
	    CSH.setHelpIDString(jxpanelGeneralProperties.jlblFinalTime, "controls.finaltime");
	    CSH.setHelpIDString(jxpanelGeneralProperties.jlblFinalTime2, "controls.finaltime");
	    CSH.setHelpIDString(jxpanelGeneralProperties.jtxtNumPoints, "controls.numdatapoints");
	    CSH.setHelpIDString(jxpanelGeneralProperties.jlblNumPoints, "controls.numdatapoints");
	    CSH.setHelpIDString(jxpanelColumnProperties, "panel.columnproperties");
	    CSH.setHelpIDString(jxpanelColumnProperties.jtxtColumnLength, "controls.columnlength");
	    CSH.setHelpIDString(jxpanelColumnProperties.jlblColumnLength, "controls.columnlength");
	    CSH.setHelpIDString(jxpanelColumnProperties.jlblColumnLength2, "controls.columnlength");
	    CSH.setHelpIDString(jxpanelColumnProperties.jtxtColumnDiameter, "controls.columndiameter");
	    CSH.setHelpIDString(jxpanelColumnProperties.jlblColumnDiameter, "controls.columndiameter");
	    CSH.setHelpIDString(jxpanelColumnProperties.jlblColumnDiameter2, "controls.columndiameter");
	    CSH.setHelpIDString(jxpanelColumnProperties.jtxtParticleSize, "controls.particlesize");
	    CSH.setHelpIDString(jxpanelColumnProperties.jlblParticleSize, "controls.particlesize");
	    CSH.setHelpIDString(jxpanelColumnProperties.jlblParticleSize2, "controls.particlesize");
	    CSH.setHelpIDString(jxpanelColumnProperties.jtxtInterparticlePorosity, "controls.interparticleporosity");
	    CSH.setHelpIDString(jxpanelColumnProperties.jlblInterparticlePorosity, "controls.interparticleporosity");
	    CSH.setHelpIDString(jxpanelColumnProperties.jtxtIntraparticlePorosity, "controls.intraparticleporosity");
	    CSH.setHelpIDString(jxpanelColumnProperties.jlblIntraparticlePorosity, "controls.intraparticleporosity");
	    CSH.setHelpIDString(jxpanelColumnProperties.jlblTotalPorosity, "controls.totalporosity");
	    CSH.setHelpIDString(jxpanelColumnProperties.jlblTotalPorosityOut, "controls.totalporosity");
	    CSH.setHelpIDString(jxpanelColumnProperties.jlblVoidVolume, "controls.voidvolume");
	    CSH.setHelpIDString(jxpanelColumnProperties.jlblVoidVolume2, "controls.voidvolume");
	    CSH.setHelpIDString(jxpanelColumnProperties.jlblVoidVolume3, "controls.voidvolume");
	    CSH.setHelpIDString(jxpanelColumnProperties.jlblVoidTime, "controls.voidtime");
	    CSH.setHelpIDString(jxpanelColumnProperties.jlblVoidTime2, "controls.voidtime");
	    CSH.setHelpIDString(jxpanelColumnProperties.jlblVoidTime3, "controls.voidtime");
	    CSH.setHelpIDString(jxpanelColumnProperties.jlblVanDeemter, "controls.vandeemter");
	    CSH.setHelpIDString(jxpanelColumnProperties.jlblATerm, "controls.vandeemter");
	    CSH.setHelpIDString(jxpanelColumnProperties.jtxtATerm, "controls.vandeemter");
	    CSH.setHelpIDString(jxpanelColumnProperties.jlblBTerm, "controls.vandeemter");
	    CSH.setHelpIDString(jxpanelColumnProperties.jtxtBTerm, "controls.vandeemter");
	    CSH.setHelpIDString(jxpanelColumnProperties.jlblCTerm, "controls.vandeemter");
	    CSH.setHelpIDString(jxpanelColumnProperties.jtxtCTerm, "controls.vandeemter");
	    CSH.setHelpIDString(jxpanelColumnProperties.jlblReducedPlateHeight, "controls.reducedplateheight");
	    CSH.setHelpIDString(jxpanelColumnProperties.jlblReducedPlateHeight2, "controls.reducedplateheight");
	*/
	    this.addComponentListener(this);
	}

	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			jScrollPane.setSize(new Dimension(624, 117));
			jScrollPane.setLocation(new Point(1, 1));
			jScrollPane.setViewportView(getJtableChemicals());
			jScrollPane.setBorder(null);
		}
		return jScrollPane;
	}
	
	public class JChemicalTable extends JTable implements Scrollable
	{
		private static final long serialVersionUID = 1L;
		
		public JChemicalTable(DefaultTableModel tabModel) {
			super(tabModel);
		}

		public boolean isCellEditable(int row, int column)
		{
			return false;
		}
		
		@Override
		public boolean getScrollableTracksViewportWidth() 
		{
			return true;
		}
	}

	/**
	 * This method initializes jtableChemicals	
	 * 	
	 * @return javax.swing.JTable	
	 */
	private JTable getJtableChemicals() 
	{
		if (jtableChemicals == null) 
		{
			String[] columnNames = 
			{ 
			"Compound", 
			"Conc. (\u03BCM)",
			"k'",
			"tR (min)",
			"<html>&#x03C3<sub>total</sub> (s)</html>",
			"W (pmol)"
			};
			
			for (int i = 0; i < columnNames.length; i++)
			{
				vectColumnNames.add(columnNames[i]);
			}
			
			tabModel = new DefaultTableModel();
			tabModel.setDataVector(vectChemicalRows, vectColumnNames);

			jtableChemicals = new JChemicalTable(tabModel);
			jtableChemicals.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			jtableChemicals.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			jtableChemicals.getTableHeader().setPreferredSize(new Dimension(jtableChemicals.getColumnModel().getTotalColumnWidth(), 22));
			jtableChemicals.getColumnModel().getColumn(0).setPreferredWidth(170);
			jtableChemicals.getColumnModel().getColumn(1).setPreferredWidth(85);
			jtableChemicals.getColumnModel().getColumn(2).setPreferredWidth(80);
			jtableChemicals.getColumnModel().getColumn(3).setPreferredWidth(80);
			jtableChemicals.getColumnModel().getColumn(4).setPreferredWidth(80);
			jtableChemicals.getColumnModel().getColumn(5).setPreferredWidth(80);
		}
		return jtableChemicals;
	}
	
	/**
	 * This method initializes jbtnAddChemical	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJbtnAddChemical() {
		if (jbtnAddChemical == null) {
			jbtnAddChemical = new JButton();
			jbtnAddChemical.setActionCommand("Add Chemical");
			jbtnAddChemical.setBounds(new Rectangle(12, 132, 81, 30));
			jbtnAddChemical.setText("Add");
		}
		return jbtnAddChemical;
	}

	/**
	 * This method initializes jbtnEditChemical	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJbtnEditChemical() {
		if (jbtnEditChemical == null) {
			jbtnEditChemical = new JButton();
			jbtnEditChemical.setActionCommand("Edit Chemical");
			jbtnEditChemical.setBounds(new Rectangle(100, 132, 81, 30));
			jbtnEditChemical.setText("Edit");
		}
		return jbtnEditChemical;
	}

	/**
	 * This method initializes jbtnRemoveChemical	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJbtnRemoveChemical() {
		if (jbtnRemoveChemical == null) {
			jbtnRemoveChemical = new JButton();
			jbtnRemoveChemical.setActionCommand("Remove Chemical");
			jbtnRemoveChemical.setBounds(new Rectangle(188, 132, 81, 30));
			jbtnRemoveChemical.setText("Remove");
		}
		return jbtnRemoveChemical;
	}

	/**
	 * This method initializes jbtnAutoscale	
	 * 	
	 * @return javax.swing.JToggleButton	
	 */
	private JToggleButton getJbtnAutoscale() {
		if (jbtnAutoscale == null) {
			jbtnAutoscale = new JToggleButton();
			jbtnAutoscale.setIcon(new ImageIcon(getClass().getResource("/org/gcsimulator/images/autoscale.png")));
			jbtnAutoscale.setRolloverEnabled(false);
			jbtnAutoscale.setSelected(true);
			jbtnAutoscale.setMargin(new Insets(0, 0, 0, 0));
			jbtnAutoscale.setText("Autoscale");
			jbtnAutoscale.setBounds(new Rectangle(8, 444, 105, 30));
			jbtnAutoscale.setToolTipText("");
		}
		return jbtnAutoscale;
	}

	/**
	 * This method initializes jbtnAutoscaleX	
	 * 	
	 * @return javax.swing.JToggleButton	
	 */
	private JToggleButton getJbtnAutoscaleX() 
	{
		if (jbtnAutoscaleX == null) {
			jbtnAutoscaleX = new JToggleButton();
			jbtnAutoscaleX.setIcon(new ImageIcon(getClass().getResource("/org/gcsimulator/images/autoscaleX.png")));
			jbtnAutoscaleX.setRolloverEnabled(false);
			jbtnAutoscaleX.setSelected(true);
			jbtnAutoscaleX.setText("");
			jbtnAutoscaleX.setActionCommand("Autoscale X");
			jbtnAutoscaleX.setBounds(new Rectangle(120, 444, 33, 30));
			jbtnAutoscaleX.setToolTipText("");
		}
		return jbtnAutoscaleX;
	}

	/**
	 * This method initializes jbtnAutoscaleY	
	 * 	
	 * @return javax.swing.JToggleButton	
	 */
	private JToggleButton getJbtnAutoscaleY() {
		if (jbtnAutoscaleY == null) {
			jbtnAutoscaleY = new JToggleButton();
			jbtnAutoscaleY.setActionCommand("Autoscale Y");
			jbtnAutoscaleY.setIcon(new ImageIcon(getClass().getResource("/org/gcsimulator/images/autoscaleY.png")));
			jbtnAutoscaleY.setRolloverEnabled(false);
			jbtnAutoscaleY.setSelected(true);
			jbtnAutoscaleY.setText("");
			jbtnAutoscaleY.setBounds(new Rectangle(160, 444, 33, 30));
			jbtnAutoscaleY.setToolTipText("");
		}
		return jbtnAutoscaleY;
	}

	/**
	 * This method initializes jbtnZoomIn	
	 * 	
	 * @return javax.swing.JToggleButton	
	 */
	private JToggleButton getJbtnZoomIn() {
		if (jbtnZoomIn == null) {
			jbtnZoomIn = new JToggleButton();
			jbtnZoomIn.setIcon(new ImageIcon(getClass().getResource("/org/gcsimulator/images/zoomin.png")));
			jbtnZoomIn.setRolloverEnabled(false);
			jbtnZoomIn.setSelected(false);
			jbtnZoomIn.setMargin(new Insets(0, 0, 0, 0));
			jbtnZoomIn.setText("Zoom in");
			jbtnZoomIn.setBounds(new Rectangle(312, 444, 105, 30));
			jbtnZoomIn.setToolTipText("");
		}
		return jbtnZoomIn;
	}

	/**
	 * This method initializes jbtnZoomOut	
	 * 	
	 * @return javax.swing.JToggleButton	
	 */
	private JToggleButton getJbtnZoomOut() {
		if (jbtnZoomOut == null) {
			jbtnZoomOut = new JToggleButton();
			jbtnZoomOut.setIcon(new ImageIcon(getClass().getResource("/org/gcsimulator/images/zoomout.png")));
			jbtnZoomOut.setRolloverEnabled(false);
			jbtnZoomOut.setSelected(false);
			jbtnZoomOut.setText("Zoom out");
			jbtnZoomOut.setMargin(new Insets(0, 0, 0, 0));
			jbtnZoomOut.setBounds(new Rectangle(424, 444, 105, 30));
			jbtnZoomOut.setToolTipText("");
		}
		return jbtnZoomOut;
	}

	/**
	 * This method initializes jbtnPan	
	 * 	
	 * @return javax.swing.JToggleButton	
	 */
	private JToggleButton getJbtnPan() 
	{
		if (jbtnPan == null) 
		{
			jbtnPan = new JToggleButton();
			jbtnPan.setIcon(new ImageIcon(getClass().getResource("/org/gcsimulator/images/pan.png")));
			jbtnPan.setRolloverEnabled(false);
			jbtnPan.setSelected(true);
			jbtnPan.setText("Pan");
			jbtnPan.setMargin(new Insets(0, 0, 0, 0));
			jbtnPan.setBounds(new Rectangle(200, 444, 105, 30));
			jbtnPan.setToolTipText("");
		}
		return jbtnPan;
	}

	/**
	 * This method initializes jpanelCompounds	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJpanelCompounds() 
	{
		if (jpanelCompounds == null) {
			jpanelCompounds = new JPanel();
			jpanelCompounds.setLayout(null);
			jpanelCompounds.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
			jpanelCompounds.setBounds(new Rectangle(284, 480, 625, 170));
			jpanelCompounds.add(getJScrollPane(), null);
			jpanelCompounds.add(getJbtnAddChemical(), null);
			jpanelCompounds.add(getJbtnEditChemical(), null);
			jpanelCompounds.add(getJbtnRemoveChemical(), null);
			jpanelCompounds.add(getJbtnTutorials(), null);
			jpanelCompounds.add(getJbtnContextHelp(), null);
			jpanelCompounds.add(getJbtnHelp(), null);
		}
		return jpanelCompounds;
	}

	/**
	 * This method initializes jSimulatedChromatogram	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJpanelSimulatedChromatogram() 
	{
		if (jpanelSimulatedChromatogram == null) 
		{
			jpanelSimulatedChromatogram = new JPanel();
			jpanelSimulatedChromatogram.setLayout(null);
			jpanelSimulatedChromatogram.setBounds(new Rectangle(284, 0, 625, 481));
			jpanelSimulatedChromatogram.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
			jpanelSimulatedChromatogram.setPreferredSize(new Dimension(0, 0));
			
			
			jpanelSimulatedChromatogram.add(getJbtnAutoscale(), null);
			jpanelSimulatedChromatogram.add(getJbtnPan(), null);
			jpanelSimulatedChromatogram.add(getJbtnAutoscaleX(), null);
			jpanelSimulatedChromatogram.add(getJbtnAutoscaleY(), null);
			jpanelSimulatedChromatogram.add(getJbtnZoomOut(), null);
			jpanelSimulatedChromatogram.add(getJbtnZoomIn(), null);
			jpanelSimulatedChromatogram.add(getJbtnCopyImage(), null);
			jpanelSimulatedChromatogram.add(getJPanelGraphControl(), null);
		}
		return jpanelSimulatedChromatogram;
	}

	/**
	 * This method initializes jControlPanel	
	 * 	
	 * @return org.desktop.swingx.JXPanel	
	 */
	private JScrollPane getControlPanel() 
	{
		if (this.jsclControlPanel == null) 
		{
			//jControlPanel = new JXPanel();
		    
			
		    VerticalLayout verticalLayout = new VerticalLayout();
		    verticalLayout.setGap(0);
		    		    
		    // create a taskpanecontainer
		    taskpanecontainer = new JXTaskPaneContainer();
		    // create a taskpane, and set it's title and icon
		    taskpanecontainer.setBorder(BorderFactory.createEmptyBorder());

		    taskpanecontainer.setPaintBorderInsets(true);
		    taskpanecontainer.setLayout(verticalLayout);

		    // Inlet/Outlet
		    jxtaskInletOutlet = new JXTaskPane();
		    jxtaskInletOutlet.setAnimated(false);
		    jxtaskInletOutlet.setTitle("Inlet/Outlet");
		    ((JComponent)jxtaskInletOutlet.getContentPane()).setBorder(BorderFactory.createEmptyBorder());
		    
		    jxpanelInletOutlet = new InletOutlet();
		    jxtaskInletOutlet.add(jxpanelInletOutlet);
		    
		    // Oven options
		    jxtaskOvenOptions = new JXTaskPane();
		    jxtaskOvenOptions.setAnimated(false);
		    jxtaskOvenOptions.setTitle("Oven Properties");
		    ((JComponent)jxtaskOvenOptions.getContentPane()).setBorder(BorderFactory.createEmptyBorder());
		    
		    jxpanelOvenOptions = new OvenOptions();
		    jxtaskOvenOptions.add(jxpanelOvenOptions);
		    
		    jxpanelIsothermalOptions = new IsothermalOptions();
		    jxtaskOvenOptions.add(jxpanelIsothermalOptions);

		    jxpanelTemperatureProgramOptions = new TemperatureProgramOptions();
		    // Don't add yet. Just create the gradient options panel.
		    //jxtaskOvenOptions.add(jxpanelTemperatureProgramOptions);

		    // Plot Options
		    jxtaskPlotOptions = new JXTaskPane();
		    jxtaskPlotOptions.setAnimated(false);
		    jxtaskPlotOptions.setTitle("Plot Options");
		    ((JComponent)jxtaskPlotOptions.getContentPane()).setBorder(null);
		    
		    jxpanelPlotOptions = new PlotOptions();
		    jxtaskPlotOptions.add(jxpanelPlotOptions);

		    // Column Options
		    jxtaskColumnProperties = new JXTaskPane();
		    jxtaskColumnProperties.setAnimated(false);
		    jxtaskColumnProperties.setTitle("Column Properties");
		    ((JComponent)jxtaskColumnProperties.getContentPane()).setBorder(null);

		    jxpanelColumnProperties = new ColumnProperties();
		    jxtaskColumnProperties.add(jxpanelColumnProperties);

		    // add the task pane to the taskpanecontainer
		    taskpanecontainer.add(jxtaskOvenOptions, null);
		    taskpanecontainer.add(jxtaskInletOutlet, null);
		    taskpanecontainer.add(jxtaskColumnProperties, null);
		    taskpanecontainer.add(jxtaskPlotOptions,null);
		    
		    jsclControlPanel = new JScrollPane(taskpanecontainer);
		    //jsclControlPanel.setLayout(new ScrollPaneLayout());
		    
		    jsclControlPanel.setBorder(BorderFactory.createEmptyBorder());
		    jsclControlPanel.setLocation(new Point(0, 0));
		    //jsclControlPanel.setBorder(null);
		    jsclControlPanel.setSize(new Dimension(281, 665));
		    //jControlPanel.setBorder(BorderFactory.createTitledBorder(null, "Controls", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
		    //jControlPanel.add(jsclControlPanel);
		}
		return jsclControlPanel;
	}

	/**
	 * This method initializes jbtnHelp	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJbtnHelp() {
		if (jbtnHelp == null) {
			jbtnHelp = new JButton();
			jbtnHelp.setText("Help");
			jbtnHelp.setForeground(Color.blue);
			jbtnHelp.setBounds(new Rectangle(536, 132, 69, 30));
		}
		return jbtnHelp;
	}

	/**
	 * This method initializes jbtnContextHelp	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJbtnContextHelp() {
		if (jbtnContextHelp == null) {
			jbtnContextHelp = new JButton();
			jbtnContextHelp.setIcon(new ImageIcon(getClass().getResource("/org/gcsimulator/images/help.gif")));
			jbtnContextHelp.setBounds(new Rectangle(496, 132, 33, 30));
		}
		return jbtnContextHelp;
	}

	/**
	 * This method initializes jbtnTutorials	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJbtnTutorials() {
		if (jbtnTutorials == null) {
			jbtnTutorials = new JButton();
			jbtnTutorials.setText("Tutorials");
			jbtnTutorials.setForeground(Color.blue);
			jbtnTutorials.setBounds(new Rectangle(396, 132, 93, 30));
			jbtnTutorials.setVisible(false);
		}
		return jbtnTutorials;
	}


	/**
	 * This method initializes jbtnCopyImage	
	 * 	
	 * @return javax.swing.JToggleButton	
	 */
	private JButton getJbtnCopyImage() {
		if (jbtnCopyImage == null) {
			jbtnCopyImage = new JButton();
			jbtnCopyImage.setBounds(new Rectangle(536, 444, 81, 30));
			jbtnCopyImage.setActionCommand("Copy Image");
			jbtnCopyImage.setIcon(new ImageIcon(getClass().getResource("/org/gcsimulator/images/clipboard.png")));
			jbtnCopyImage.setRolloverEnabled(false);
			jbtnCopyImage.setMargin(new Insets(0, 0, 0, 0));
			jbtnCopyImage.setSelected(false);
			jbtnCopyImage.setText("Copy");
			jbtnCopyImage.setToolTipText("");
		}
		return jbtnCopyImage;
	}


	@Override
	public Dimension getPreferredScrollableViewportSize() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public int getScrollableBlockIncrement(Rectangle arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public boolean getScrollableTracksViewportHeight() {
		Dimension minSize = this.getMinimumSize();
		Dimension portSize = null;
		if (getParent() instanceof JViewport) 
		{
			JViewport port = (JViewport)getParent();
			portSize = port.getSize();
		}
		else
			return false;
		
		if (portSize.height < minSize.height)
			return false;
		else
			return true;
	}


	@Override
	public boolean getScrollableTracksViewportWidth() {
		Dimension minSize = this.getMinimumSize();
		Dimension portSize = null;
		if (getParent() instanceof JViewport) 
		{
			JViewport port = (JViewport)getParent();
			portSize = port.getSize();
		}
		else
			return false;
		
		if (portSize.width < minSize.width)
			return false;
		else
			return true;
	}


	@Override
	public int getScrollableUnitIncrement(Rectangle arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public void componentHidden(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void componentMoved(ComponentEvent arg0) {
		this.revalidate();
	}


	@Override
	public void componentResized(ComponentEvent arg0) {
		// Respond to window resize
		if (arg0.getComponent() == this)
		{
			Dimension size = this.getSize();
			int iControlPanelWidth = 332;
			this.jsclControlPanel.setSize(iControlPanelWidth - 4, size.height);
			int divide = (size.height * 3) / 4;
			this.jpanelSimulatedChromatogram.setLocation(iControlPanelWidth, 0);
			this.jpanelSimulatedChromatogram.setSize(size.width - iControlPanelWidth, divide + 1);
			this.jPanelGraphControl.setBounds(1, 1, jpanelSimulatedChromatogram.getWidth() - 2, jpanelSimulatedChromatogram.getHeight() - 44);
			this.jpanelCompounds.setLocation(iControlPanelWidth, divide);
			this.jpanelCompounds.setSize(size.width - iControlPanelWidth, size.height - divide);
			this.jbtnAutoscale.setLocation(jbtnAutoscale.getLocation().x, jpanelSimulatedChromatogram.getHeight() - 37);
			this.jbtnAutoscaleX.setLocation(jbtnAutoscaleX.getLocation().x, jpanelSimulatedChromatogram.getHeight() - 37);
			this.jbtnAutoscaleY.setLocation(jbtnAutoscaleY.getLocation().x, jpanelSimulatedChromatogram.getHeight() - 37);
			this.jbtnPan.setLocation(jbtnPan.getLocation().x, jpanelSimulatedChromatogram.getHeight() - 37);
			this.jbtnZoomIn.setLocation(jbtnZoomIn.getLocation().x, jpanelSimulatedChromatogram.getHeight() - 37);
			this.jbtnZoomOut.setLocation(jbtnZoomOut.getLocation().x, jpanelSimulatedChromatogram.getHeight() - 37);
			this.jbtnCopyImage.setLocation(jbtnCopyImage.getLocation().x, jpanelSimulatedChromatogram.getHeight() - 37);
			this.jScrollPane.setSize(jpanelCompounds.getWidth() - 2, jpanelCompounds.getHeight() - 44);
			this.jbtnAddChemical.setLocation(jbtnAddChemical.getLocation().x, jpanelCompounds.getHeight() - 37);
			this.jbtnEditChemical.setLocation(jbtnEditChemical.getLocation().x, jpanelCompounds.getHeight() - 37);
			this.jbtnRemoveChemical.setLocation(jbtnRemoveChemical.getLocation().x, jpanelCompounds.getHeight() - 37);
			this.jbtnTutorials.setLocation(jpanelCompounds.getWidth() - 220, jpanelCompounds.getHeight() - 37);
			this.jbtnContextHelp.setLocation(jpanelCompounds.getWidth() - 120, jpanelCompounds.getHeight() - 37);
			this.jbtnHelp.setLocation(jpanelCompounds.getWidth() - 80, jpanelCompounds.getHeight() - 37);

			int xpos = 0;
			int diff;
			diff = ((jScrollPane.getViewport().getWidth()) * 170) / 575;
			xpos += diff;
			jtableChemicals.getColumnModel().getColumn(0).setPreferredWidth(diff);
			diff = ((jScrollPane.getViewport().getWidth()) * 85) / 575;
			xpos += diff;
			jtableChemicals.getColumnModel().getColumn(1).setPreferredWidth(diff);
			diff = ((jScrollPane.getViewport().getWidth()) * 80) / 575;
			xpos += diff;
			jtableChemicals.getColumnModel().getColumn(2).setPreferredWidth(diff);
			diff = ((jScrollPane.getViewport().getWidth()) * 80) / 575;
			xpos += diff;
			jtableChemicals.getColumnModel().getColumn(3).setPreferredWidth(diff);
			diff = ((jScrollPane.getViewport().getWidth()) * 80) / 575;
			xpos += diff;
			jtableChemicals.getColumnModel().getColumn(4).setPreferredWidth(diff);
			jtableChemicals.getColumnModel().getColumn(5).setPreferredWidth(jScrollPane.getViewport().getWidth() - xpos);
			jtableChemicals.revalidate();
			this.m_GraphControl.repaint();
		}
	}


	@Override
	public void componentShown(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	/**
	 * This method initializes jPanelGraphControl	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelGraphControl() {
		if (jPanelGraphControl == null) {
			GridLayout gridLayout = new GridLayout();
			gridLayout.setRows(1);
			jPanelGraphControl = new JPanel();
			jPanelGraphControl.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
			jPanelGraphControl.setLayout(gridLayout);
			jPanelGraphControl.setBounds(new Rectangle(4, 2, 620, 432));
			jPanelGraphControl.add(m_GraphControl, null);
		}
		return jPanelGraphControl;
	}
}  //  @jve:decl-index=0:visual-constraint="-257,33"
