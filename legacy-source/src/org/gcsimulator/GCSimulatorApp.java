package org.gcsimulator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.color.ColorSpace;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.awt.image.ComponentColorModel;
import java.awt.image.ComponentSampleModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Random;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JWindow;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * A Transferable able to transfer an AWT Image.
 * Similar to the JDK StringSelection class.
 */
class ImageSelection implements Transferable {
    private Image image;
   
    public static void copyImageToClipboard(Image image) {
        ImageSelection imageSelection = new ImageSelection(image);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        toolkit.getSystemClipboard().setContents(imageSelection, null);
    }
   
    public ImageSelection(Image image) {
        this.image = image;
    }
   
	@Override
    public Object getTransferData (DataFlavor flavor) throws UnsupportedFlavorException {
        if (flavor.equals(DataFlavor.imageFlavor) == false) {
            throw new UnsupportedFlavorException(flavor);
        }
        return image;
    }
   
	@Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return flavor.equals(DataFlavor.imageFlavor);
    }
   
    public DataFlavor[] getTransferDataFlavors() {
        return new DataFlavor[] {
            DataFlavor.imageFlavor
        };
    }
}

class Compound implements Serializable
{
	private static final long serialVersionUID = 1L; // Never change this
	
	String strCompoundName;
	double dConcentration;
	InterpolationFunction InterpolatedLogkvsT;
	double dMolarVolume;
	int iStationaryPhase;
	
	double dRetentionTime;
	double dSigma;
	double dW;
	int iCompoundIndex;
	
	public Compound()
	{
		
	}
	
	public void createRandomCompound(String strName, double dMinConcentration, double dMaxConcentration)
	{
		Random rand = new Random();

		iCompoundIndex = -1;
		strCompoundName = strName;
		dMolarVolume = (rand.nextDouble() * 100) + 90;
		
		//TODO: Create random logk vs. T data here
		/*dLogkwvsTIntercept[0] = rand.nextDouble() * Math.abs(-2 - 4.268938682) + -2;
		dLogkwvsTIntercept[1] = (1.036104824 * dLogkwvsTIntercept[0] + 0.498925029) + (rand.nextGaussian() * 0.219210168);

		dLogkwvsTSlope[0] = (-0.002977165 * dLogkwvsTIntercept[0] + -0.002210656) + (rand.nextGaussian() * 0.001038813);
		dLogkwvsTSlope[1] = (-0.003077331 * dLogkwvsTIntercept[1] + -0.004028345) + (rand.nextGaussian() * 0.000898516);

		dSvsTIntercept[0] = (-0.896773193 * dLogkwvsTIntercept[0] + -1.088853824) + (rand.nextGaussian() * 0.130567307);
		dSvsTIntercept[1] = (-0.735675344 * dLogkwvsTIntercept[1] + -1.407983911) + (rand.nextGaussian() * 0.147499601);
		
		dSvsTSlope[0] = (0.003334429 * dLogkwvsTIntercept[0] + 0.000190878) + (rand.nextGaussian() * 0.001400388);
		dSvsTSlope[1] = (0.002509075 * dLogkwvsTIntercept[1] + 0.004044421) + (rand.nextGaussian() * 0.001002375);
		*/
		double dLogConcentration = rand.nextDouble() * (Math.log10(dMaxConcentration / dMinConcentration)) + Math.log10(dMinConcentration);
		dConcentration = Math.pow(10, dLogConcentration);
	}
	
	public boolean loadCompoundInfo(int iStationaryPhase, int iIndex)
	{
		this.iCompoundIndex = iIndex;
		this.iStationaryPhase = iStationaryPhase;
		
		strCompoundName = Globals.CompoundNameArray[iStationaryPhase][iIndex];
		
		InterpolatedLogkvsT = new InterpolationFunction(Globals.CompoundIsothermalDataArray[0][iIndex]);

		//dMolarVolume = Globals.MolarVolumeArray[iStationaryPhase][iIndex];
		
		return true;
	}

	private void writeObject(ObjectOutputStream out) throws IOException
	{
		out.writeLong(Globals.INTERNAL_FILE_VERSION);
		
		out.writeObject(strCompoundName);
		out.writeDouble(dConcentration);
		
		out.writeObject(InterpolatedLogkvsT);
		
		out.writeDouble(dMolarVolume);
		
		out.writeInt(iStationaryPhase);
		out.writeInt(iCompoundIndex);
	}
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		long lVersion = in.readLong();
		
		if (lVersion >= 1)
		{
			strCompoundName = (String)in.readObject();
			dConcentration = in.readDouble();
			
			InterpolatedLogkvsT = (InterpolationFunction)in.readObject();
			
			dMolarVolume = in.readDouble();
			
			iStationaryPhase = in.readInt();
			iCompoundIndex = in.readInt();
		}
	}
}

public class GCSimulatorApp extends JFrame implements ActionListener, ChangeListener, KeyListener, FocusListener, ListSelectionListener, AutoScaleListener, TableModelListener, TemperatureProgramListener
{
	private static final long serialVersionUID = 1L; // Never change this
	
	private boolean m_bSliderUpdate = true;

	TopPanel contentPane = null;
	public JScrollPane jMainScrollPane = null;
	public int m_iSecondPlotType = 0;
	public int m_iCarrierGas = 1;
	public double m_dTimeConstant = 0.5;
	public double m_dTemperature = 200;
	public double m_dSignalOffset = 30;
	public double m_dNoise = 3;
	public double m_dStartTime = 0;
	public double m_dEndTime = 0;
	public int m_iNumPoints = 3000;
	public double m_dFlowRate = 1; // in mL/min
	public double m_dInletPressure = 100; // in kPa
	public double m_dOutletPressure = 0.00001; // in kPa
	public double m_dSplitRatio = 10;
	public double m_dLinerLength = .04; // in m
	public double m_dLinerInnerDiameter = 0.002; // in m
	public double m_dLinerVolume = 0; // in m^3
	public double m_dInjectionVolume = 1; // in uL
	public double m_dColumnLength = 30; // in m
	public double m_dColumnDiameter = 0.25; // in mm
	public double m_dFilmThickness = 0.25; // in um
	public double m_dInitialTemperature = 60; // in deg C
	public double m_dInitialTime = 1; // in min
	public boolean m_bTemperatureProgramMode = false;
	public boolean m_bConstantFlowRateMode = true;
	public boolean m_bColumnOutletAtVacuum = true;
	public boolean m_bSplitInjectionMode = true;
	public double m_dHoldUpVolume;
	public double m_dHoldUpTime;
	public double m_dDiffusionCoefficient = 0.00001;
	public double m_dReducedFlowVelocity;
	public double m_dReducedPlateHeight;
	public double m_dTheoreticalPlates;
	public double m_dHETP;
	public Vector<Compound> m_vectCompound = new Vector<Compound>();
	public double[][] m_dTemperatureProgramArray;
	public LinearInterpolationFunction m_lifTemperatureProgram = null;
	public int m_iChromatogramPlotIndex = -1;
	public int m_iSinglePlotIndex = -1;
	public int m_iSecondPlotIndex = -1;
	public int m_iSigma1PlotIndex = -1;
	public int m_iSigma2PlotIndex = -1;
	public Vector<double[]> m_vectSelectedRetentionFactorArray;
	public Vector<double[]> m_vectSelectedPositionArray;
	public double[][] m_dSelectedSigmaPositionArray;
	public double m_dSelectedIsocraticRetentionFactor = 0;
	public int m_iStationaryPhase = 0; // the selected stationary phase
	public boolean m_bAutomaticTimeRange = true;
	public InterpolationFunction[] InterpolatedIsothermalData;
	public double m_dPlotFlowVelocityPosition = 0;
	public double m_dPlotGasDensityPosition = 0;
	public double m_dPlotPressurePosition = 0;
	private boolean m_bDoNotMessage = false;
	
	// Menu items
    JMenuItem menuLoadSettingsAction = new JMenuItem("Load Settings");
    JMenuItem menuSaveSettingsAction = new JMenuItem("Save Settings");
    JMenuItem menuSaveSettingsAsAction = new JMenuItem("Save Settings As...");
    JMenuItem menuResetToDefaultValuesAction = new JMenuItem("Reset To Default Settings");
    JMenuItem menuExitAction = new JMenuItem("Exit");
    
    JMenuItem menuHelpTopicsAction = new JMenuItem("Help Topics");
    JMenuItem menuAboutAction = new JMenuItem("About GC Simulator");
    
    File m_currentFile = null;
    boolean m_bDocumentChangedFlag = false;

    public class JFileChooser2 extends JFileChooser
    {
		@Override
		public void approveSelection()
		{
		    File f = getSelectedFile();
		    if(f.exists() && getDialogType() == SAVE_DIALOG)
		    {
		        int result = JOptionPane.showConfirmDialog(this,"The file exists, overwrite?","Existing file",JOptionPane.YES_NO_CANCEL_OPTION);
		        switch(result){
		            case JOptionPane.YES_OPTION:
		                super.approveSelection();
		                return;
		            case JOptionPane.NO_OPTION:
		                return;
		            case JOptionPane.CANCEL_OPTION:
		                cancelSelection();
		                return;
		        }
		    }
		    super.approveSelection();
		}
	}
    
    
    // Start the app
    public static void main(String[] args) 
    {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
    
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() 
    {
        //Use the Java look and feel.
    	try {
    	    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
    	        if ("Nimbus".equals(info.getName())) {
    	            UIManager.setLookAndFeel(info.getClassName());
    	            break;
    	        }
    	    }
    	} catch (Exception e) {
    	    // If Nimbus is not available, you can set the GUI to another look and feel.
    	}
    	/*try {
            UIManager.setLookAndFeel(
                UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) { }*/

        //Make sure we have nice window decorations.
        JFrame.setDefaultLookAndFeelDecorated(true);
        JDialog.setDefaultLookAndFeelDecorated(true);

        //Instantiate the controlling class.
        GCSimulatorApp frame = new GCSimulatorApp("GC Simulator");
        
		//java.net.URL url1 = ClassLoader.getSystemResource("org/gcsimulator/images/icon.png");
		Toolkit kit = Toolkit.getDefaultToolkit();
	    Image img = kit.createImage(frame.getClass().getResource("/org/gcsimulator/images/icon.png"));
	    frame.setIconImage(img);
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set up the GC Simulator window
        frame.init();

        //Display the window.
        frame.pack();
        frame.setLocationRelativeTo(null); //center it
        frame.setVisible(true);
    }
    
	/**
	 * This is the xxx default constructor
	 */
	public GCSimulatorApp(String str) 
	{
	    super(str);
	    
		this.setPreferredSize(new Dimension(1000, 700));
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	public void init() 
	{
        // Load the JavaHelp
		/*String helpHS = "org/gcsimulator/help/HPLCSimHelp.hs";
		ClassLoader cl = TopPanel.class.getClassLoader();
		try {
			URL hsURL = HelpSet.findHelpSet(cl, helpHS);
			Globals.hsMainHelpSet = new HelpSet(null, hsURL);
		} catch (Exception ee) {
			System.out.println( "HelpSet " + ee.getMessage());
			System.out.println("HelpSet "+ helpHS +" not found");
			return;
		}
		Globals.hbMainHelpBroker = Globals.hsMainHelpSet.createHelpBroker();
*/
		//Execute a job on the event-dispatching thread; creating this applet's GUI.
        try {
        //    SwingUtilities.invokeAndWait(new Runnable() 
        //    {
        //        public void run() {
                	createGUI();
        //        }
        //    });
        } catch (Exception e) { 
            System.err.println("createGUI didn't complete successfully");
            System.err.println(e.getMessage());
            System.err.println(e.getLocalizedMessage());
            System.err.println(e.toString());
            System.err.println(e.getStackTrace());
            System.err.println(e.getCause());
        }
        
        loadDefaultsForStationaryPhase(0);
        loadControlsWithStoredValues();
    	calculateTemperatureProgram();
        performCalculations();
    }
    
    private void createGUI()
    {
    	// Creates a menubar for a JFrame
        JMenuBar menuBar = new JMenuBar();
        
        // Add the menubar to the frame
        setJMenuBar(menuBar);
        
        // Define and add two drop down menu to the menubar
        JMenu fileMenu = new JMenu("File");
        JMenu helpMenu = new JMenu("Help");
        
        menuBar.add(fileMenu);
        menuBar.add(helpMenu);
        
        // Create and add simple menu item to one of the drop down menu
        menuLoadSettingsAction.addActionListener(this);
        menuSaveSettingsAction.addActionListener(this);
        menuSaveSettingsAsAction.addActionListener(this);
        menuResetToDefaultValuesAction.addActionListener(this);
        menuExitAction.addActionListener(this);

        menuHelpTopicsAction.addActionListener(this);
        menuAboutAction.addActionListener(this);
        
        fileMenu.add(menuLoadSettingsAction);
        fileMenu.add(menuSaveSettingsAction);
        fileMenu.add(menuSaveSettingsAsAction);
        fileMenu.addSeparator();
        fileMenu.add(menuResetToDefaultValuesAction);
        fileMenu.addSeparator();
        fileMenu.add(menuExitAction);
        
        menuHelpTopicsAction.setEnabled(false);
        helpMenu.add(menuHelpTopicsAction);
        helpMenu.addSeparator();
        helpMenu.add(menuAboutAction);

        //Create and set up the content pane
    	jMainScrollPane = new JScrollPane();
    	jMainScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    	jMainScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

    	contentPane = new TopPanel();
        contentPane.setOpaque(true);
        jMainScrollPane.setViewportView(contentPane);
    	setContentPane(jMainScrollPane);
    	jMainScrollPane.revalidate();
        
        contentPane.jbtnAddChemical.addActionListener(this);
        contentPane.jbtnEditChemical.addActionListener(this);
        contentPane.jbtnRemoveChemical.addActionListener(this);

        contentPane.jxpanelPlotOptions.jrdoNoPlot.addActionListener(this);
        contentPane.jxpanelPlotOptions.jrdoTemperature.addActionListener(this);
        contentPane.jxpanelPlotOptions.jrdoHoldUpTime.addActionListener(this);
        contentPane.jxpanelPlotOptions.jrdoMobilePhaseViscosity.addActionListener(this);
        contentPane.jxpanelPlotOptions.jrdoFlowVelocity.addActionListener(this);
        contentPane.jxpanelPlotOptions.jsliderFlowVelocityPosition.addChangeListener(this);
        contentPane.jxpanelPlotOptions.jtxtFlowVelocityColumnPosition.addFocusListener(this);
        contentPane.jxpanelPlotOptions.jtxtFlowVelocityColumnPosition.addKeyListener(this);
        contentPane.jxpanelPlotOptions.jrdoMobilePhaseDensity.addActionListener(this);
        contentPane.jxpanelPlotOptions.jsliderDensityPosition.addChangeListener(this);
        contentPane.jxpanelPlotOptions.jtxtDensityColumnPosition.addFocusListener(this);
        contentPane.jxpanelPlotOptions.jtxtDensityColumnPosition.addKeyListener(this);
        contentPane.jxpanelPlotOptions.jrdoPressure.addActionListener(this);
        contentPane.jxpanelPlotOptions.jsliderPressurePosition.addChangeListener(this);
        contentPane.jxpanelPlotOptions.jtxtPressurePosition.addFocusListener(this);
        contentPane.jxpanelPlotOptions.jtxtPressurePosition.addKeyListener(this);
        contentPane.jxpanelPlotOptions.jrdoRetentionFactor.addActionListener(this);
        contentPane.jxpanelPlotOptions.jcboRetentionFactorCompounds.addActionListener(this);
        contentPane.jxpanelPlotOptions.jrdoPosition.addActionListener(this);
        contentPane.jxpanelPlotOptions.jcboPositionCompounds.addActionListener(this);
        contentPane.jxpanelPlotOptions.jtxtTimeConstant.addKeyListener(this);
        contentPane.jxpanelPlotOptions.jtxtTimeConstant.addFocusListener(this);
        contentPane.jxpanelPlotOptions.jtxtSignalOffset.addKeyListener(this);
        contentPane.jxpanelPlotOptions.jtxtSignalOffset.addFocusListener(this);
        contentPane.jxpanelPlotOptions.jtxtNoise.addKeyListener(this);
        contentPane.jxpanelPlotOptions.jtxtNoise.addFocusListener(this);
        contentPane.jxpanelPlotOptions.jtxtInitialTime.addKeyListener(this);
        contentPane.jxpanelPlotOptions.jtxtInitialTime.addFocusListener(this);
        contentPane.jxpanelPlotOptions.jtxtFinalTime.addKeyListener(this);
        contentPane.jxpanelPlotOptions.jtxtFinalTime.addFocusListener(this);
        contentPane.jxpanelPlotOptions.jtxtNumPoints.addKeyListener(this);
        contentPane.jxpanelPlotOptions.jtxtNumPoints.addFocusListener(this);
        contentPane.jxpanelPlotOptions.jchkAutoTimeRange.addActionListener(this);

        contentPane.jxpanelInletOutlet.jcboCarrierGas.addActionListener(this);
        contentPane.jxpanelInletOutlet.jrdoConstantFlowRate.addActionListener(this);
        contentPane.jxpanelInletOutlet.jrdoConstantPressure.addActionListener(this);
        contentPane.jxpanelInletOutlet.jtxtFlowRate.addKeyListener(this);
        contentPane.jxpanelInletOutlet.jtxtFlowRate.addFocusListener(this);
        contentPane.jxpanelInletOutlet.jtxtInletPressure.addKeyListener(this);
        contentPane.jxpanelInletOutlet.jtxtInletPressure.addFocusListener(this);
        contentPane.jxpanelInletOutlet.jrdoVacuum.addActionListener(this);
        contentPane.jxpanelInletOutlet.jrdoOtherPressure.addActionListener(this);
        contentPane.jxpanelInletOutlet.jtxtOtherPressure.addKeyListener(this);
        contentPane.jxpanelInletOutlet.jtxtOtherPressure.addFocusListener(this);
        contentPane.jxpanelInletOutlet.jrdoSplitless.addActionListener(this);
        contentPane.jxpanelInletOutlet.jrdoSplit.addActionListener(this);
        contentPane.jxpanelInletOutlet.jtxtSplitRatio.addKeyListener(this);
        contentPane.jxpanelInletOutlet.jtxtSplitRatio.addFocusListener(this);
        contentPane.jxpanelInletOutlet.jtxtLinerLength.addKeyListener(this);
        contentPane.jxpanelInletOutlet.jtxtLinerLength.addFocusListener(this);
        contentPane.jxpanelInletOutlet.jtxtLinerInnerDiameter.addKeyListener(this);
        contentPane.jxpanelInletOutlet.jtxtLinerInnerDiameter.addFocusListener(this);
        contentPane.jxpanelInletOutlet.jtxtInjectionVolume.addKeyListener(this);
        contentPane.jxpanelInletOutlet.jtxtInjectionVolume.addFocusListener(this);
        contentPane.jxpanelInletOutlet.jcboInletPressureUnits.addActionListener(this);

        contentPane.jxpanelColumnProperties.jtxtColumnLength.addKeyListener(this);
        contentPane.jxpanelColumnProperties.jtxtColumnLength.addFocusListener(this);
        contentPane.jxpanelColumnProperties.jtxtColumnDiameter.addKeyListener(this);
        contentPane.jxpanelColumnProperties.jtxtColumnDiameter.addFocusListener(this);
        contentPane.jxpanelColumnProperties.jtxtFilmThickness.addKeyListener(this);
        contentPane.jxpanelColumnProperties.jtxtFilmThickness.addFocusListener(this);
        contentPane.jxpanelColumnProperties.jcboStationaryPhase.addActionListener(this);
        
        contentPane.jxpanelIsothermalOptions.jsliderTemperature.addChangeListener(this);
        contentPane.jxpanelIsothermalOptions.jtxtTemperature.addKeyListener(this);
        contentPane.jxpanelIsothermalOptions.jtxtTemperature.addFocusListener(this);

        contentPane.jxpanelTemperatureProgramOptions.jtxtInitialTemperature.addKeyListener(this);
        contentPane.jxpanelTemperatureProgramOptions.jtxtInitialTemperature.addFocusListener(this);
        contentPane.jxpanelTemperatureProgramOptions.jtxtInitialTime.addKeyListener(this);
        contentPane.jxpanelTemperatureProgramOptions.jtxtInitialTime.addFocusListener(this);
        //contentPane.jxpanelTemperatureProgramOptions.tmTemperatureProgram.addTableModelListener(this);
        contentPane.jxpanelTemperatureProgramOptions.addListener(this);
        
        contentPane.jxpanelOvenOptions.jrdoIsothermalElution.addActionListener(this);
        contentPane.jxpanelOvenOptions.jrdoProgrammedTemperatureElution.addActionListener(this);

        contentPane.jtableChemicals.getSelectionModel().addListSelectionListener(this);
        contentPane.jbtnPan.addActionListener(this);
        contentPane.jbtnZoomIn.addActionListener(this);
        contentPane.jbtnZoomOut.addActionListener(this);
        contentPane.jbtnAutoscale.addActionListener(this);
        contentPane.jbtnAutoscaleX.addActionListener(this);
        contentPane.jbtnAutoscaleY.addActionListener(this);
        contentPane.jbtnHelp.addActionListener(this);
        contentPane.jbtnTutorials.addActionListener(this);
        contentPane.jbtnCopyImage.addActionListener(this);
        contentPane.m_GraphControl.addAutoScaleListener(this);
        contentPane.m_GraphControl.setSecondYAxisVisible(false);
        contentPane.m_GraphControl.setSecondYAxisRangeLimits(0, 100);
        //contentPane.jbtnContextHelp.addActionListener(new CSH.DisplayHelpAfterTracking(Globals.hbMainHelpBroker));
    }

    private void validateTimeConstant()
    {
    	if (contentPane.jxpanelPlotOptions.jtxtTimeConstant.getText().length() == 0)
    		contentPane.jxpanelPlotOptions.jtxtTimeConstant.setText("0");

    	double dTemp = (double)Float.valueOf(contentPane.jxpanelPlotOptions.jtxtTimeConstant.getText());
		
		if (dTemp < .000001)
			dTemp = .000001;
		if (dTemp > 999999)
			dTemp = 999999;
		
		this.m_dTimeConstant = dTemp;
		contentPane.jxpanelPlotOptions.jtxtTimeConstant.setText(Float.toString((float)m_dTimeConstant));    	
    } 

    private void validateSignalOffset()
    {
    	if (contentPane.jxpanelPlotOptions.jtxtSignalOffset.getText().length() == 0)
    		contentPane.jxpanelPlotOptions.jtxtSignalOffset.setText("0");

    	double dTemp = (double)Float.valueOf(contentPane.jxpanelPlotOptions.jtxtSignalOffset.getText());
		
		if (dTemp < 0)
			dTemp = 0;
		if (dTemp > 999999)
			dTemp = 999999;
		
		this.m_dSignalOffset = dTemp;
		contentPane.jxpanelPlotOptions.jtxtSignalOffset.setText(Float.toString((float)m_dSignalOffset));    	
    } 

    private void validateNoise()
    {
    	if (contentPane.jxpanelPlotOptions.jtxtNoise.getText().length() == 0)
    		contentPane.jxpanelPlotOptions.jtxtNoise.setText("0");

    	double dTemp = (double)Float.valueOf(contentPane.jxpanelPlotOptions.jtxtNoise.getText());
		
		if (dTemp < .000001)
			dTemp = .000001;
		if (dTemp > 999999)
			dTemp = 999999;
		
		this.m_dNoise = dTemp;
		contentPane.jxpanelPlotOptions.jtxtNoise.setText(Float.toString((float)m_dNoise));    	
    } 

    private void validateStartTime()
    {
    	if (contentPane.jxpanelPlotOptions.jtxtInitialTime.getText().length() == 0)
    		contentPane.jxpanelPlotOptions.jtxtInitialTime.setText("0");

    	double dTemp = (double)Float.valueOf(contentPane.jxpanelPlotOptions.jtxtInitialTime.getText());
		
		if (dTemp < 0)
			dTemp = 0;
		if (dTemp > m_dEndTime)
			dTemp = m_dEndTime - .000001;
		
		this.m_dStartTime = dTemp;
		contentPane.jxpanelPlotOptions.jtxtInitialTime.setText(Float.toString((float)m_dStartTime));    	
    } 

    private void validateEndTime()
    {
    	if (contentPane.jxpanelPlotOptions.jtxtFinalTime.getText().length() == 0)
    		contentPane.jxpanelPlotOptions.jtxtFinalTime.setText("0");

    	double dTemp = (double)Float.valueOf(contentPane.jxpanelPlotOptions.jtxtFinalTime.getText());
		
		if (dTemp < m_dStartTime)
			dTemp = m_dStartTime + .000001;
		if (dTemp > 99999999)
			dTemp = 99999999;
		
		this.m_dEndTime = dTemp;
		contentPane.jxpanelPlotOptions.jtxtFinalTime.setText(Float.toString((float)m_dEndTime));    	
    } 

    private void validateNumPoints()
    {
    	if (contentPane.jxpanelPlotOptions.jtxtNumPoints.getText().length() == 0)
    		contentPane.jxpanelPlotOptions.jtxtNumPoints.setText("0");

    	int iTemp = Integer.valueOf(contentPane.jxpanelPlotOptions.jtxtNumPoints.getText());
		
		if (iTemp < 2)
			iTemp = 2;
		if (iTemp > 100000)
			iTemp = 100000;
		
		this.m_iNumPoints = iTemp;
		contentPane.jxpanelPlotOptions.jtxtNumPoints.setText(Integer.toString(m_iNumPoints));    	
    } 

    private void validateFlowRate()
    {
    	if (contentPane.jxpanelInletOutlet.jtxtFlowRate.getText().length() == 0)
    		contentPane.jxpanelInletOutlet.jtxtFlowRate.setText("0");

    	double dTemp = (double)Float.valueOf(contentPane.jxpanelInletOutlet.jtxtFlowRate.getText());
		
		if (dTemp < 0.000000001)
			dTemp = 0.000000001;
		if (dTemp > 10000)
			dTemp = 10000;
		
		this.m_dFlowRate = dTemp;
		contentPane.jxpanelInletOutlet.jtxtFlowRate.setText(Float.toString((float)dTemp));    	
    }

    private void validateInletPressure()
    {
    	if (contentPane.jxpanelInletOutlet.jtxtInletPressure.getText().length() == 0)
    		contentPane.jxpanelInletOutlet.jtxtInletPressure.setText("0");

    	double dTemp = (double)Float.valueOf(contentPane.jxpanelInletOutlet.jtxtInletPressure.getText());
		
		if (dTemp < 0.000000001)
			dTemp = 0.000000001;
		if (dTemp > 100000)
			dTemp = 100000;
	
		if (this.contentPane.jxpanelInletOutlet.jcboInletPressureUnits.getSelectedIndex() == 0)
			this.m_dInletPressure = (dTemp * 1000) + 101325;
		else
			this.m_dInletPressure = dTemp * 1000;
			
		contentPane.jxpanelInletOutlet.jtxtInletPressure.setText(Float.toString((float)Globals.roundToSignificantFigures(dTemp, 5)));    	
    }

    private void validateOtherOutletPressure()
    {
    	if (contentPane.jxpanelInletOutlet.jtxtOtherPressure.getText().length() == 0)
    		contentPane.jxpanelInletOutlet.jtxtOtherPressure.setText("0");

    	if (contentPane.jxpanelInletOutlet.jrdoOtherPressure.isSelected() == false)
    		return;

		double dTemp = (double)Float.valueOf(contentPane.jxpanelInletOutlet.jtxtOtherPressure.getText());
		
		if (dTemp < 0.000001)
			dTemp = 0.000001;
		if (dTemp > 101.325)
			dTemp = 101.325;
		
		this.m_dOutletPressure = dTemp * 1000;
		contentPane.jxpanelInletOutlet.jtxtOtherPressure.setText(Float.toString((float)dTemp));    	
    }

    private void validateSplitRatio()
    {
    	if (contentPane.jxpanelInletOutlet.jtxtSplitRatio.getText().length() == 0)
    		contentPane.jxpanelInletOutlet.jtxtSplitRatio.setText("0");

    	double dTemp = (double)Float.valueOf(contentPane.jxpanelInletOutlet.jtxtSplitRatio.getText());
		
		if (dTemp < 0.000000001)
			dTemp = 0.000000001;
		if (dTemp > 1000000)
			dTemp = 1000000;
	
		this.m_dSplitRatio = dTemp;			
		contentPane.jxpanelInletOutlet.jtxtSplitRatio.setText(Float.toString((float)dTemp));    	
    }

    private void validateLinerLength()
    {
    	if (contentPane.jxpanelInletOutlet.jtxtLinerLength.getText().length() == 0)
    		contentPane.jxpanelInletOutlet.jtxtLinerLength.setText("0");

    	double dTemp = (double)Float.valueOf(contentPane.jxpanelInletOutlet.jtxtLinerLength.getText());
		
		if (dTemp < 0.000000001)
			dTemp = 0.000000001;
		if (dTemp > 1000000)
			dTemp = 1000000;
	
		this.m_dLinerLength = dTemp / 1000;			
		contentPane.jxpanelInletOutlet.jtxtLinerLength.setText(Float.toString((float)dTemp));    	
    }

    private void validateLinerInnerDiameter()
    {
    	if (contentPane.jxpanelInletOutlet.jtxtLinerInnerDiameter.getText().length() == 0)
    		contentPane.jxpanelInletOutlet.jtxtLinerInnerDiameter.setText("0");

    	double dTemp = (double)Float.valueOf(contentPane.jxpanelInletOutlet.jtxtLinerInnerDiameter.getText());
		
		if (dTemp < 0.000000001)
			dTemp = 0.000000001;
		if (dTemp > 1000000)
			dTemp = 1000000;
	
		this.m_dLinerInnerDiameter = dTemp / 1000;			
		contentPane.jxpanelInletOutlet.jtxtLinerInnerDiameter.setText(Float.toString((float)dTemp));    	
    }

    private void validateInjectionVolume()
    {
    	if (contentPane.jxpanelInletOutlet.jtxtInjectionVolume.getText().length() == 0)
    		contentPane.jxpanelInletOutlet.jtxtInjectionVolume.setText("0");

    	double dTemp = (double)Float.valueOf(contentPane.jxpanelInletOutlet.jtxtInjectionVolume.getText());
		
		if (dTemp < .000001)
			dTemp = .000001;
		if (dTemp > 999999)
			dTemp = 999999;
		
		this.m_dInjectionVolume = dTemp;
		contentPane.jxpanelInletOutlet.jtxtInjectionVolume.setText(Float.toString((float)m_dInjectionVolume));    	
    } 

    private void validateColumnLength()
    {
    	if (contentPane.jxpanelColumnProperties.jtxtColumnLength.getText().length() == 0)
    		contentPane.jxpanelColumnProperties.jtxtColumnLength.setText("0");

    	double dTemp = (double)Float.valueOf(contentPane.jxpanelColumnProperties.jtxtColumnLength.getText());
		
		if (dTemp < .01)
			dTemp = .01;
		if (dTemp > 999999)
			dTemp = 999999;
		
		this.m_dColumnLength = dTemp;
		contentPane.jxpanelColumnProperties.jtxtColumnLength.setText(Float.toString((float)m_dColumnLength));    	
    }    

    private void validateColumnDiameter()
    {
    	if (contentPane.jxpanelColumnProperties.jtxtColumnDiameter.getText().length() == 0)
    		contentPane.jxpanelColumnProperties.jtxtColumnDiameter.setText("0");

    	double dTemp = (double)Float.valueOf(contentPane.jxpanelColumnProperties.jtxtColumnDiameter.getText());
		
		if (dTemp < .0001)
			dTemp = .0001;
		if (dTemp > 999999)
			dTemp = 999999;
		
		this.m_dColumnDiameter = dTemp / 1000; // convert to m
		contentPane.jxpanelColumnProperties.jtxtColumnDiameter.setText(Float.toString((float)dTemp));    	
    }    
    
    private void validateFilmThickness()
    {
    	if (contentPane.jxpanelColumnProperties.jtxtFilmThickness.getText().length() == 0)
    		contentPane.jxpanelColumnProperties.jtxtFilmThickness.setText("0");

    	double dTemp = (double)Float.valueOf(contentPane.jxpanelColumnProperties.jtxtFilmThickness.getText());
		
		if (dTemp < .001)
			dTemp = .001;
		if (dTemp > 999999)
			dTemp = 999999;
		
		this.m_dFilmThickness = dTemp / 1000000; // convert to m
		contentPane.jxpanelColumnProperties.jtxtFilmThickness.setText(Float.toString((float)dTemp));    	
    }    
    
    private void validateTemperature()
    {
    	if (contentPane.jxpanelIsothermalOptions.jtxtTemperature.getText().length() == 0)
    		contentPane.jxpanelIsothermalOptions.jtxtTemperature.setText("0");

		double dTemp = (double)Float.valueOf(contentPane.jxpanelIsothermalOptions.jtxtTemperature.getText());
		dTemp = Math.floor(dTemp);
		
		if (dTemp < 60)
			dTemp = 60;
		if (dTemp > 320)
			dTemp = 320;
		
		m_dTemperature = dTemp;
		m_bSliderUpdate = false;
		contentPane.jxpanelIsothermalOptions.jsliderTemperature.setValue((int)m_dTemperature);
		m_bSliderUpdate = true;
		contentPane.jxpanelIsothermalOptions.jtxtTemperature.setText(Integer.toString((int)m_dTemperature));    	
    }

    private void validateFlowVelocityAtColumnPosition()
    {
    	if (contentPane.jxpanelPlotOptions.jtxtFlowVelocityColumnPosition.getText().length() == 0)
    		contentPane.jxpanelPlotOptions.jtxtFlowVelocityColumnPosition.setText("0");

		double dTemp = (double)Float.valueOf(contentPane.jxpanelPlotOptions.jtxtFlowVelocityColumnPosition.getText());
		
		if (dTemp < 0)
			dTemp = 0;
		if (dTemp > 1)
			dTemp = 1;
		
		m_dPlotFlowVelocityPosition = dTemp;
		m_bSliderUpdate = false;
		contentPane.jxpanelPlotOptions.jsliderFlowVelocityPosition.setValue((int)(dTemp * 1000));
		m_bSliderUpdate = true;
		contentPane.jxpanelPlotOptions.jtxtFlowVelocityColumnPosition.setText(Float.toString((float)dTemp));    	
    }

    private void validateGasDensityAtColumnPosition()
    {
    	if (contentPane.jxpanelPlotOptions.jtxtDensityColumnPosition.getText().length() == 0)
    		contentPane.jxpanelPlotOptions.jtxtDensityColumnPosition.setText("0");

		double dTemp = (double)Float.valueOf(contentPane.jxpanelPlotOptions.jtxtDensityColumnPosition.getText());
		
		if (dTemp < 0)
			dTemp = 0;
		if (dTemp > 1)
			dTemp = 1;
		
		m_dPlotGasDensityPosition = dTemp;
		m_bSliderUpdate = false;
		contentPane.jxpanelPlotOptions.jsliderDensityPosition.setValue((int)(dTemp * 1000));
		m_bSliderUpdate = true;
		contentPane.jxpanelPlotOptions.jtxtDensityColumnPosition.setText(Float.toString((float)dTemp));    	
    }
    
    private void validatePressureAtColumnPosition()
    {
    	if (contentPane.jxpanelPlotOptions.jtxtPressurePosition.getText().length() == 0)
    		contentPane.jxpanelPlotOptions.jtxtPressurePosition.setText("0");

		double dTemp = (double)Float.valueOf(contentPane.jxpanelPlotOptions.jtxtPressurePosition.getText());
		
		if (dTemp < 0)
			dTemp = 0;
		if (dTemp > 1)
			dTemp = 1;
		
		m_dPlotPressurePosition = dTemp;
		m_bSliderUpdate = false;
		contentPane.jxpanelPlotOptions.jsliderPressurePosition.setValue((int)(dTemp * 1000));
		m_bSliderUpdate = true;
		contentPane.jxpanelPlotOptions.jtxtPressurePosition.setText(Float.toString((float)dTemp));    	
    }

    private void validateInitialTemperature()
    {
    	if (contentPane.jxpanelTemperatureProgramOptions.jtxtInitialTemperature.getText().length() == 0)
    		contentPane.jxpanelTemperatureProgramOptions.jtxtInitialTemperature.setText("0");

    	double dTemp = (double)Float.valueOf(contentPane.jxpanelTemperatureProgramOptions.jtxtInitialTemperature.getText());
		
		if (dTemp < 60)
			dTemp = 60;
		if (dTemp > 320)
			dTemp = 320;
		
		if (contentPane.jxpanelTemperatureProgramOptions.tmTemperatureProgram.getRowCount() > 0)
		{
			double dFirstTemp = (Double)contentPane.jxpanelTemperatureProgramOptions.tmTemperatureProgram.getValueAt(0, 1);
			if (dTemp > dFirstTemp)
				dTemp = dFirstTemp;
		}
		
		this.m_dInitialTemperature = dTemp;
		contentPane.jxpanelTemperatureProgramOptions.jtxtInitialTemperature.setText(Float.toString((float)dTemp));    	
    }    
    
    private void validateInitialTime()
    {
    	if (contentPane.jxpanelTemperatureProgramOptions.jtxtInitialTime.getText().length() == 0)
    		contentPane.jxpanelTemperatureProgramOptions.jtxtInitialTime.setText("0");

    	double dTemp = (double)Float.valueOf(contentPane.jxpanelTemperatureProgramOptions.jtxtInitialTime.getText());
		
		if (dTemp < 0)
			dTemp = 0;
		if (dTemp > 1000)
			dTemp = 1000;
		
		this.m_dInitialTime = dTemp;
		contentPane.jxpanelTemperatureProgramOptions.jtxtInitialTime.setText(Float.toString((float)dTemp));    	
    }

    public boolean writeToOutputStream()
    {
    	try 
		{
            FileOutputStream fos = new FileOutputStream(m_currentFile);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

	    	oos.writeLong(Globals.INTERNAL_FILE_VERSION);
	    	oos.writeObject(contentPane.jxpanelTemperatureProgramOptions.tmTemperatureProgram.getDataVector());
            oos.writeInt(m_iCarrierGas);
	        oos.writeDouble(m_dFlowRate);
	        oos.writeDouble(m_dInletPressure);
	        oos.writeDouble(m_dOutletPressure);
	        oos.writeDouble(m_dSplitRatio);
	        oos.writeDouble(m_dLinerLength);
	        oos.writeDouble(m_dLinerInnerDiameter);
	        oos.writeDouble(m_dInjectionVolume);
	        oos.writeBoolean(m_bTemperatureProgramMode);
	    	oos.writeBoolean(m_bAutomaticTimeRange);
	        oos.writeDouble(m_dTemperature);
	        oos.writeDouble(m_dColumnLength);
	        oos.writeDouble(m_dColumnDiameter);
	        oos.writeDouble(m_dFilmThickness);
	        oos.writeDouble(m_dInitialTemperature);
	        oos.writeDouble(m_dInitialTime);
	        oos.writeBoolean(m_bConstantFlowRateMode);
	        oos.writeBoolean(m_bColumnOutletAtVacuum);
	        oos.writeBoolean(m_bSplitInjectionMode);
	        oos.writeDouble(m_dTimeConstant);
	        oos.writeDouble(m_dStartTime);
	        oos.writeDouble(m_dEndTime);
	        oos.writeDouble(m_dNoise);
	        oos.writeDouble(m_dSignalOffset);
	        oos.writeInt(m_iNumPoints);
	        oos.writeObject(m_vectCompound);
	        oos.writeInt(m_iStationaryPhase);
	        
            oos.flush();
			oos.close();
			this.m_bDocumentChangedFlag = false;
    	}
    	catch (IOException e) 
		{
			e.printStackTrace();
	        JOptionPane.showMessageDialog(this, "The file could not be saved.", "Error saving file", JOptionPane.ERROR_MESSAGE);
	        return false;
		}
    	
    	return true;
    }
    
    public boolean saveFile(boolean bSaveAs)
    {
		if (bSaveAs == false && m_currentFile != null)
		{
			if (writeToOutputStream())
				return true;
		}
		else
		{	
			JFileChooser2 fc = new JFileChooser2();
			
			FileNameExtensionFilter filter = new FileNameExtensionFilter("GC Simulator Files (*.gcsim)", "gcsim");
			fc.setFileFilter(filter);
			fc.setDialogTitle("Save As...");
			int returnVal = fc.showSaveDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) 
            {
                m_currentFile = fc.getSelectedFile();
                String path = m_currentFile.getAbsolutePath();
                if (path.lastIndexOf(".") >= 0)
                	path = path.substring(0, path.lastIndexOf("."));
                	
                m_currentFile = new File(path + ".gcsim");

               	if (writeToOutputStream())
               		return true;
				
            }
		}
		return false;
    }
    
    public void loadDefaultsForStationaryPhase(int iStationaryPhase)
    {
    	if (iStationaryPhase == 0)
    	{
        	int iNumRows = contentPane.jxpanelTemperatureProgramOptions.tmTemperatureProgram.getRowCount();
        	for (int i = 0; i < iNumRows; i++)
        	{
        		contentPane.jxpanelTemperatureProgramOptions.m_bDoNotChangeTable = true;
        		contentPane.jxpanelTemperatureProgramOptions.tmTemperatureProgram.removeRow(0);
        	}

        	contentPane.jxpanelTemperatureProgramOptions.m_bDoNotChangeTable = true;
        	contentPane.jxpanelTemperatureProgramOptions.tmTemperatureProgram.addRow(new Double[] {20.0, 260.0, 5.0});
        	
        	m_bTemperatureProgramMode = false;
        	m_bAutomaticTimeRange = true;
        	m_bSplitInjectionMode = true;
        	m_bConstantFlowRateMode = true;
        	m_bColumnOutletAtVacuum = true;
        	m_iCarrierGas = 1;
        	m_dInletPressure = 50000 + 101325;
        	m_dOutletPressure = 0.00001;
        	m_dSplitRatio = 100.0;
        	m_dLinerLength = .078;
        	m_dLinerInnerDiameter = 0.002;
        	m_dFilmThickness = 0.25 / 1000000;
        	m_dInitialTemperature = 60;
        	m_dInitialTime = 1;
        	m_dTemperature = 200;
        	m_dColumnLength = 30;
        	m_dColumnDiameter = 0.25 / 1000;
        	m_dFlowRate = 1; // in mL/min
        	m_dInjectionVolume = 1; //(in uL)
        	m_dTimeConstant = 0.1;
        	m_dStartTime = 0;
        	m_dEndTime = 277;
        	m_dNoise = 0.5;
        	m_dSignalOffset = 0;
        	m_iNumPoints = 3000;
        	m_iStationaryPhase = 0;
        	m_vectCompound.clear();
        	
        	double[][] compoundsToAdd = {
            		{2, 30},
            		{5, 60},
            		{12, 30},
            		{14, 50},
            		{17, 100},
            		{18, 20},
            		{19, 50},
            		{24, 40},
            		{30, 30},
            		{35, 10},
            		{40, 100},
            		{45, 30},
            		{50, 60},
            		{55, 40},
            		{60, 20}
            };
            
            for (int i = 0; i < compoundsToAdd.length; i++)
            {
            	Compound compound = new Compound();
            	compound.loadCompoundInfo(m_iStationaryPhase, (int)compoundsToAdd[i][0]);
            	compound.dConcentration = compoundsToAdd[i][1];
            	this.m_vectCompound.add(compound);
            }
        	
    	}
    	
    	loadControlsWithStoredValues();
    	updateCompoundComboBoxes();
    }
    
    public boolean loadFile(File fileToLoad)
    {
    	// Set all variables to default values here
    	loadDefaultsForStationaryPhase(0);
    	
    	if (fileToLoad != null)
    	{
    		try 
            {
                FileInputStream fis = new FileInputStream(m_currentFile);
                ObjectInputStream ois = new ObjectInputStream(fis);
                
                long lFileVersion = ois.readLong();

                if (lFileVersion >= 1)
                {
                	int iNumRows = contentPane.jxpanelTemperatureProgramOptions.tmTemperatureProgram.getRowCount();
                	for (int i = 0; i < iNumRows; i++)
                	{
                		contentPane.jxpanelTemperatureProgramOptions.m_bDoNotChangeTable = true;
                		contentPane.jxpanelTemperatureProgramOptions.tmTemperatureProgram.removeRow(0);
                	}
              
                	Vector<Vector<Double>> rowVector = (Vector<Vector<Double>>)ois.readObject();

                	for (int i = 0; i < rowVector.size(); i++)
                	{
                		if (rowVector.elementAt(i) != null)
                		{
                			contentPane.jxpanelTemperatureProgramOptions.m_bDoNotChangeTable = true;
                			contentPane.jxpanelTemperatureProgramOptions.tmTemperatureProgram.addRow(rowVector.elementAt(i));
                		}
                	}
                	m_iCarrierGas = ois.readInt();
                	m_dFlowRate = ois.readDouble();
                	m_dInletPressure = ois.readDouble();
                	m_dOutletPressure = ois.readDouble();
                	m_dSplitRatio = ois.readDouble();
                	m_dLinerLength = ois.readDouble();
                	m_dLinerInnerDiameter = ois.readDouble();
                	m_dInjectionVolume = ois.readDouble();
                	m_bTemperatureProgramMode = ois.readBoolean();
                	m_bAutomaticTimeRange = ois.readBoolean();
                	m_dTemperature = ois.readDouble();
                	m_dColumnLength = ois.readDouble();
                	m_dColumnDiameter = ois.readDouble();
                	m_dFilmThickness = ois.readDouble();
                	m_dInitialTemperature = ois.readDouble();
                	m_dInitialTime = ois.readDouble();
                	m_bConstantFlowRateMode = ois.readBoolean();
                	m_bColumnOutletAtVacuum = ois.readBoolean();
                	m_bSplitInjectionMode = ois.readBoolean();
                	m_dTimeConstant = ois.readDouble();
                	m_dStartTime = ois.readDouble();
                	m_dEndTime = ois.readDouble();
                	m_dNoise = ois.readDouble();
                	m_dSignalOffset = ois.readDouble();
                	m_iNumPoints = ois.readInt();
                	m_vectCompound = (Vector<Compound>) ois.readObject();
                	m_iStationaryPhase = ois.readInt();
                }
                else
                {
    		        JOptionPane.showMessageDialog(this, "Sorry! This file is no longer compatible with GC Simulator after the latest update. However, new files you save in this version WILL be compatible with future versions.", "Error opening file", JOptionPane.ERROR_MESSAGE);
                    ois.close();
    		        m_currentFile = null;
    		        return false;
                }
    	        
                ois.close();
			} 
            catch (IOException e) 
            {
				e.printStackTrace();
		        JOptionPane.showMessageDialog(this, "The file is not a valid GC Simulator file.", "Error opening file", JOptionPane.ERROR_MESSAGE);
		        m_currentFile = null;
		        return false;
			} 
            catch (ClassNotFoundException e) 
            {
				e.printStackTrace();
		        JOptionPane.showMessageDialog(this, "The file is not a valid GC Simulator file.", "Error opening file", JOptionPane.ERROR_MESSAGE);
		        m_currentFile = null;
		        return false;
            }
    	}
    	
    	loadControlsWithStoredValues();
    	
    	return true;
    }
    
    public void loadControlsWithStoredValues()
    {
    	// Now set each parameter in the controls
    	if (m_bTemperatureProgramMode)
	    {
    		contentPane.jxpanelOvenOptions.jrdoIsothermalElution.setSelected(false);
	    	contentPane.jxpanelOvenOptions.jrdoProgrammedTemperatureElution.setSelected(true);

	    	contentPane.jxtaskOvenOptions.remove(contentPane.jxpanelIsothermalOptions);
	    	contentPane.jxtaskOvenOptions.add(contentPane.jxpanelTemperatureProgramOptions);

	    	contentPane.jxtaskOvenOptions.validate();
	    	contentPane.jsclControlPanel.validate();
	    }
	    else
	    {
    		contentPane.jxpanelOvenOptions.jrdoIsothermalElution.setSelected(true);
	    	contentPane.jxpanelOvenOptions.jrdoProgrammedTemperatureElution.setSelected(false);

	    	contentPane.jxtaskOvenOptions.remove(contentPane.jxpanelTemperatureProgramOptions);
	    	contentPane.jxtaskOvenOptions.add(contentPane.jxpanelIsothermalOptions);

	    	contentPane.jxtaskOvenOptions.validate();
	    	contentPane.jsclControlPanel.validate();
	    }
    	
    	m_bDoNotMessage = true;
		contentPane.jxpanelInletOutlet.jcboCarrierGas.setSelectedIndex(this.m_iCarrierGas);
    	m_bDoNotMessage = false;

    	contentPane.jxpanelTemperatureProgramOptions.jtxtInitialTemperature.setText(Float.toString((float)this.m_dInitialTemperature));
    	contentPane.jxpanelTemperatureProgramOptions.jtxtInitialTime.setText(Float.toString((float)this.m_dInitialTime));
    	
    	contentPane.jxpanelInletOutlet.jtxtFlowRate.setText(Float.toString((float)m_dFlowRate));    	
		contentPane.jxpanelInletOutlet.jtxtInletPressure.setText(Float.toString((float)(m_dInletPressure - 101325.0) / 1000));    	
		contentPane.jxpanelInletOutlet.jtxtSplitRatio.setText(Float.toString((float)this.m_dSplitRatio));
		contentPane.jxpanelInletOutlet.jtxtLinerLength.setText(Float.toString((float)this.m_dLinerLength * 1000));
		contentPane.jxpanelInletOutlet.jtxtLinerInnerDiameter.setText(Float.toString((float)this.m_dLinerInnerDiameter * 1000));
		contentPane.jxpanelInletOutlet.jtxtInjectionVolume.setText(Float.toString((float)this.m_dInjectionVolume));
		if (m_bConstantFlowRateMode)
			switchToConstantFlowRateMode();
		else
			switchToConstantPressureMode();
		
		if (m_bColumnOutletAtVacuum)
			vacuumOutletPressure();
		else
			otherOutletPressure();
		
		if (m_bSplitInjectionMode)
			this.switchToSplitMode();
		else
			this.switchToSplitlessMode();
		
    	m_bDoNotMessage = true;
    	contentPane.jxpanelColumnProperties.jcboStationaryPhase.setSelectedIndex(m_iStationaryPhase);
    	m_bDoNotMessage = false;
		contentPane.jxpanelColumnProperties.jtxtColumnLength.setText(Float.toString((float)m_dColumnLength));    	
		contentPane.jxpanelColumnProperties.jtxtColumnDiameter.setText(Float.toString((float)m_dColumnDiameter * 1000));    	
		contentPane.jxpanelColumnProperties.jtxtFilmThickness.setText(Float.toString((float)this.m_dFilmThickness * 1000000));
		
    	m_bSliderUpdate = false;
    	contentPane.jxpanelIsothermalOptions.jtxtTemperature.setText(Integer.toString((int)m_dTemperature));
    	contentPane.jxpanelIsothermalOptions.jsliderTemperature.setValue((int)m_dTemperature);
		m_bSliderUpdate = true;
		
		contentPane.jxpanelPlotOptions.jtxtTimeConstant.setText(Float.toString((float)m_dTimeConstant));    	
		contentPane.jxpanelPlotOptions.jtxtSignalOffset.setText(Float.toString((float)m_dSignalOffset));    	
		contentPane.jxpanelPlotOptions.jtxtNoise.setText(Float.toString((float)m_dNoise));    	
		contentPane.jxpanelPlotOptions.jtxtInitialTime.setText(Float.toString((float)m_dStartTime));    	
		contentPane.jxpanelPlotOptions.jtxtFinalTime.setText(Float.toString((float)m_dEndTime));    	
		contentPane.jxpanelPlotOptions.jtxtNumPoints.setText(Integer.toString(m_iNumPoints));    	
		
		contentPane.jxpanelPlotOptions.jchkAutoTimeRange.setSelected(m_bAutomaticTimeRange);
    	if (m_bAutomaticTimeRange)
    	{
    		contentPane.jxpanelPlotOptions.jtxtInitialTime.setEnabled(false);
    		contentPane.jxpanelPlotOptions.jtxtFinalTime.setEnabled(false);
    	}
    	else
    	{
    		contentPane.jxpanelPlotOptions.jtxtInitialTime.setEnabled(true);
    		contentPane.jxpanelPlotOptions.jtxtFinalTime.setEnabled(true);	    		
    	}
    	
    	// Add the table space for the compound. Fill it in later with performCalculations().
		contentPane.vectChemicalRows.clear();
		for (int i = 0; i < m_vectCompound.size(); i++)
		{
			Vector<String> vectNewRow = new Vector<String>();
	    	vectNewRow.add(m_vectCompound.get(i).strCompoundName);
	    	vectNewRow.add(Float.toString((float)m_vectCompound.get(i).dConcentration));
	    	vectNewRow.add("");
	    	vectNewRow.add("");
	    	vectNewRow.add("");
	    	vectNewRow.add("");
	    	vectNewRow.add("");
	    	contentPane.vectChemicalRows.add(vectNewRow);
		}

		updateCompoundComboBoxes();
    
		performCalculations();
    }
    
    public void actionPerformed(ActionEvent evt) 
	{
	    String strActionCommand = evt.getActionCommand();
	    
	    if (strActionCommand == "Add Chemical")
	    {
	    	//Frame[] frames = Frame.getFrames();
	    	ChemicalDialog dlgChemical = new ChemicalDialog(this, false);
	    	
	    	dlgChemical.setSelectedStationaryPhase(this.m_iStationaryPhase);
	    	
	    	// Make a list of the chemical indices already used
	    	for (int i = 0; i < m_vectCompound.size(); i++)
	    	{
	    		Integer k = new Integer(m_vectCompound.get(i).iCompoundIndex);
	    		dlgChemical.m_vectCompoundsUsed.add(k);
	    	}
	    	
	    	// Show the dialog.
	    	dlgChemical.setVisible(true);
	    	
	    	if (dlgChemical.m_bOk == false)
	    		return;
	    	
	    	// Add the compound properties to the m_vectCompound array
	    	Compound newCompound = new Compound();
		    	
    		newCompound.loadCompoundInfo(m_iStationaryPhase, dlgChemical.m_iCompound);
    		newCompound.dConcentration = dlgChemical.m_dConcentration1;
	    	
	    	this.m_vectCompound.add(newCompound);
	    	
	    	// Add the table space for the compound. Fill it in later with performCalculations().
	    	Vector<String> vectNewRow = new Vector<String>();
	    	vectNewRow.add(newCompound.strCompoundName);
	    	vectNewRow.add(Float.toString((float)newCompound.dConcentration));
	    	vectNewRow.add("");
	    	vectNewRow.add("");
	    	vectNewRow.add("");
	    	vectNewRow.add("");
	    	vectNewRow.add("");
	    	
	    	contentPane.vectChemicalRows.add(vectNewRow);
	    	
	    	updateCompoundComboBoxes();
	    	
	    	performCalculations();
	    }
	    else if (strActionCommand == "Edit Chemical")
	    {
	    	int iRowSel = contentPane.jtableChemicals.getSelectedRow();
	    	if (iRowSel < 0 || iRowSel >= contentPane.vectChemicalRows.size())
	    		return;

	    	//Frame[] frames = Frame.getFrames();
	    	ChemicalDialog dlgChemical = new ChemicalDialog(this, true);
	    	
	    	dlgChemical.setSelectedStationaryPhase(this.m_iStationaryPhase);
	    	
	    	dlgChemical.setSelectedCompound(this.m_vectCompound.get(iRowSel).iCompoundIndex);
	    	dlgChemical.setCompoundConcentration(this.m_vectCompound.get(iRowSel).dConcentration);
	    	
	    	// Make a list of the chemical indices already used
	    	for (int i = 0; i < m_vectCompound.size(); i++)
	    	{
	    		// Don't add the currently selected row to the list
	    		if (i == iRowSel)
	    			continue;
	    		
	    		Integer k = new Integer(m_vectCompound.get(i).iCompoundIndex);
	    		dlgChemical.m_vectCompoundsUsed.add(k);
	    	}

	    	// Show the dialog.
	    	dlgChemical.setVisible(true);
	    	
	    	if (dlgChemical.m_bOk == false)
	    		return;
	    	
	    	// Add the compound properties to the m_vectCompound array
	    	Compound newCompound = new Compound();
	    	
    		newCompound.loadCompoundInfo(m_iStationaryPhase, dlgChemical.m_iCompound);
    		newCompound.dConcentration = dlgChemical.m_dConcentration1;
	    	
	    	this.m_vectCompound.set(iRowSel, newCompound);
	    	
	    	// Add the table space for the compound. Fill it in later with performCalculations().
	    	Vector<String> vectNewRow = new Vector<String>();
	    	vectNewRow.add(newCompound.strCompoundName);
	    	vectNewRow.add(Float.toString((float)newCompound.dConcentration));
	    	vectNewRow.add("");
	    	vectNewRow.add("");
	    	vectNewRow.add("");
	    	vectNewRow.add("");
	    	vectNewRow.add("");
	    	
	    	contentPane.vectChemicalRows.set(iRowSel, vectNewRow);

	    	updateCompoundComboBoxes();

	    	performCalculations();
	    }
	    else if (strActionCommand == "Remove Chemical")
	    {
	    	int iRowSel = contentPane.jtableChemicals.getSelectedRow();
	    	if (iRowSel < 0 || iRowSel >= contentPane.vectChemicalRows.size())
	    		return;
	    	
	    	contentPane.vectChemicalRows.remove(iRowSel);
	    	contentPane.jtableChemicals.addNotify();
	    	
	    	if (iRowSel >= m_vectCompound.size())
	    		return;
	    	
	    	m_vectCompound.remove(iRowSel);	    		
	    	updateCompoundComboBoxes();

	    	performCalculations();
	    }
	    else if (strActionCommand == "Automatically determine time span")
	    {
	    	if (contentPane.jxpanelPlotOptions.jchkAutoTimeRange.isSelected() == true)
	    	{
	    		m_bAutomaticTimeRange = true;
	    		contentPane.jxpanelPlotOptions.jtxtInitialTime.setEnabled(false);
	    		contentPane.jxpanelPlotOptions.jtxtFinalTime.setEnabled(false);
	    	}
	    	else
	    	{
	    		m_bAutomaticTimeRange = false;
	    		contentPane.jxpanelPlotOptions.jtxtInitialTime.setEnabled(true);
	    		contentPane.jxpanelPlotOptions.jtxtFinalTime.setEnabled(true);	    		
	    	}
	    	
	    	performCalculations();
	    }
	    else if (strActionCommand == "Autoscale")
	    {
	    	if (contentPane.jbtnAutoscale.isSelected() == true)
	    	{
	    		contentPane.m_GraphControl.setAutoScaleX(true);
	    		contentPane.m_GraphControl.setAutoScaleY(true);
	    		contentPane.m_GraphControl.repaint();
	    	}
	    	else
	    	{
	    		contentPane.m_GraphControl.setAutoScaleX(false);
    			contentPane.m_GraphControl.setAutoScaleY(false);
	    	}
	    }
	    else if (strActionCommand == "Autoscale X")
	    {
	    	if (contentPane.jbtnAutoscaleX.isSelected() == true)
	    	{
	    		contentPane.m_GraphControl.setAutoScaleX(true);
	    		contentPane.m_GraphControl.repaint();
	    	}
	    	else
	    	{
	    		contentPane.m_GraphControl.setAutoScaleX(false);
	    	}
	    }
	    else if (strActionCommand == "Autoscale Y")
	    {
	    	if (contentPane.jbtnAutoscaleY.isSelected() == true)
	    	{
	    		contentPane.m_GraphControl.setAutoScaleY(true);
	    		contentPane.m_GraphControl.repaint();
	    	}
	    	else
	    	{
	    		contentPane.m_GraphControl.setAutoScaleY(false);
	    	}
	    }
	    else if (strActionCommand == "Pan")
	    {
	    	contentPane.jbtnPan.setSelected(true);
	    	contentPane.jbtnZoomIn.setSelected(false);
	    	contentPane.jbtnZoomOut.setSelected(false);
	    	contentPane.m_GraphControl.selectPanMode();
	    }
	    else if (strActionCommand == "Zoom in")
	    {
	    	contentPane.jbtnPan.setSelected(false);
	    	contentPane.jbtnZoomIn.setSelected(true);
	    	contentPane.jbtnZoomOut.setSelected(false);	    	
	    	contentPane.m_GraphControl.selectZoomInMode();
	    }
	    else if (strActionCommand == "Zoom out")
	    {
	    	contentPane.jbtnPan.setSelected(false);
	    	contentPane.jbtnZoomIn.setSelected(false);
	    	contentPane.jbtnZoomOut.setSelected(true);	    		    	
	    	contentPane.m_GraphControl.selectZoomOutMode();
	    }
	    else if (strActionCommand == "Help")
	    {
			//Globals.hbMainHelpBroker.setCurrentID("getting_started");
			//Globals.hbMainHelpBroker.setDisplayed(true);
	    }
	    else if (strActionCommand == "Tutorials")
	    {
			//Globals.hbMainHelpBroker.setCurrentID("tutorials");
			//Globals.hbMainHelpBroker.setDisplayed(true);
	    }
	    else if (strActionCommand == "CarrierGasChanged")
	    {
    		if (this.m_iCarrierGas == contentPane.jxpanelInletOutlet.jcboCarrierGas.getSelectedIndex())
    			return;

    		m_iCarrierGas = contentPane.jxpanelInletOutlet.jcboCarrierGas.getSelectedIndex();
    		
    		performCalculations();
	    }
	    else if (strActionCommand == "StationaryPhaseComboBoxChanged")
	    {
	    	if (!m_bDoNotMessage)
	    	{	
	    		if (m_iStationaryPhase == contentPane.jxpanelColumnProperties.jcboStationaryPhase.getSelectedIndex())
	    			return;

	    		int retval = JOptionPane.showConfirmDialog(null, "If you select a new stationary phase, the selected compounds will reset to the default ones.\nAre you sure you want to choose a new stationary phase?", "GC Simulator", JOptionPane.YES_NO_OPTION);
		    	if (retval == JOptionPane.YES_OPTION)
		    	{
		    		m_iStationaryPhase = contentPane.jxpanelColumnProperties.jcboStationaryPhase.getSelectedIndex();
		    		loadDefaultsForStationaryPhase(m_iStationaryPhase);
		    	}
		    	else
		    	{
		    		contentPane.jxpanelColumnProperties.jcboStationaryPhase.setSelectedIndex(m_iStationaryPhase);
		    	}
	    	}
	    }
	    else if (strActionCommand == "Isothermal elution mode")
	    {
	    	contentPane.jxpanelOvenOptions.jrdoProgrammedTemperatureElution.setSelected(false);
	    	contentPane.jxpanelOvenOptions.jrdoIsothermalElution.setSelected(true);
	    	
	    	contentPane.jxtaskOvenOptions.remove(contentPane.jxpanelTemperatureProgramOptions);
	    	contentPane.jxtaskOvenOptions.add(contentPane.jxpanelIsothermalOptions);

	    	contentPane.jxpanelInletOutlet.validate();
	    	contentPane.jsclControlPanel.validate();
	    	
	    	this.m_bTemperatureProgramMode = false;
	    	performCalculations();
	    }
	    else if (strActionCommand == "Programmed temperature elution mode")
	    {
	    	contentPane.jxpanelOvenOptions.jrdoIsothermalElution.setSelected(false);
	    	contentPane.jxpanelOvenOptions.jrdoProgrammedTemperatureElution.setSelected(true);

	    	contentPane.jxtaskOvenOptions.remove(contentPane.jxpanelIsothermalOptions);
	    	contentPane.jxtaskOvenOptions.add(contentPane.jxpanelTemperatureProgramOptions);

	    	contentPane.jxpanelInletOutlet.validate();
	    	contentPane.jsclControlPanel.validate();

	    	this.m_bTemperatureProgramMode = true;
	    	performCalculations();
	    }
	    else if (strActionCommand == "Constant flow rate mode")
	    {
	    	switchToConstantFlowRateMode();
	    	performCalculations();
	    }
	    else if (strActionCommand == "Constant pressure mode")
	    {
	    	switchToConstantPressureMode();
	    	performCalculations();
	    }
	    else if (strActionCommand == "Vacuum")
	    {
	    	vacuumOutletPressure();
	    	performCalculations();
	    }
	    else if (strActionCommand == "OtherPressure")
	    {
	    	otherOutletPressure();
	    	performCalculations();
	    }
	    else if (strActionCommand == "Split injection")
	    {
	    	switchToSplitMode();
	    	performCalculations();
	    }
	    else if (strActionCommand == "Splitless injection")
	    {
	    	switchToSplitlessMode();
	    	performCalculations();
	    }
	    else if (strActionCommand == "No plot")
	    {
	    	m_iSecondPlotType = 0;
	    	contentPane.jxpanelPlotOptions.jrdoNoPlot.setSelected(true);
	    	contentPane.jxpanelPlotOptions.jrdoTemperature.setSelected(false);
	    	contentPane.jxpanelPlotOptions.jrdoHoldUpTime.setSelected(false);
	    	contentPane.jxpanelPlotOptions.jrdoMobilePhaseViscosity.setSelected(false);
	    	contentPane.jxpanelPlotOptions.jrdoFlowVelocity.setSelected(false);
	    	contentPane.jxpanelPlotOptions.jrdoMobilePhaseDensity.setSelected(false);
	    	contentPane.jxpanelPlotOptions.jrdoPressure.setSelected(false);
	    	contentPane.jxpanelPlotOptions.jrdoRetentionFactor.setSelected(false);
	    	contentPane.jxpanelPlotOptions.jrdoPosition.setSelected(false);
	    	contentPane.jxpanelPlotOptions.jsliderFlowVelocityPosition.setEnabled(false);
	    	contentPane.jxpanelPlotOptions.jtxtFlowVelocityColumnPosition.setEnabled(false);
	    	contentPane.jxpanelPlotOptions.jsliderDensityPosition.setEnabled(false);
	    	contentPane.jxpanelPlotOptions.jtxtDensityColumnPosition.setEnabled(false);
	    	contentPane.jxpanelPlotOptions.jsliderPressurePosition.setEnabled(false);
	    	contentPane.jxpanelPlotOptions.jtxtPressurePosition.setEnabled(false);
	    	contentPane.jxpanelPlotOptions.jcboRetentionFactorCompounds.setEnabled(false);
	    	contentPane.jxpanelPlotOptions.jcboPositionCompounds.setEnabled(false);
	    	
	    	contentPane.m_GraphControl.setSecondYAxisVisible(false);
	    	contentPane.m_GraphControl.RemoveSeries(m_iSecondPlotIndex);
	    	m_iSecondPlotIndex = -1;
	    	
	    	performCalculations();
	    }
	    else if (strActionCommand == "Plot temperature")
	    {
	    	m_iSecondPlotType = 1;
	    	contentPane.jxpanelPlotOptions.jrdoNoPlot.setSelected(false);
	    	contentPane.jxpanelPlotOptions.jrdoTemperature.setSelected(true);
	    	contentPane.jxpanelPlotOptions.jrdoHoldUpTime.setSelected(false);
	    	contentPane.jxpanelPlotOptions.jrdoMobilePhaseViscosity.setSelected(false);
	    	contentPane.jxpanelPlotOptions.jrdoFlowVelocity.setSelected(false);
	    	contentPane.jxpanelPlotOptions.jrdoMobilePhaseDensity.setSelected(false);
	    	contentPane.jxpanelPlotOptions.jrdoPressure.setSelected(false);
	    	contentPane.jxpanelPlotOptions.jrdoRetentionFactor.setSelected(false);
	    	contentPane.jxpanelPlotOptions.jrdoPosition.setSelected(false);
	    	contentPane.jxpanelPlotOptions.jsliderFlowVelocityPosition.setEnabled(false);
	    	contentPane.jxpanelPlotOptions.jtxtFlowVelocityColumnPosition.setEnabled(false);
	    	contentPane.jxpanelPlotOptions.jsliderDensityPosition.setEnabled(false);
	    	contentPane.jxpanelPlotOptions.jtxtDensityColumnPosition.setEnabled(false);
	    	contentPane.jxpanelPlotOptions.jsliderPressurePosition.setEnabled(false);
	    	contentPane.jxpanelPlotOptions.jtxtPressurePosition.setEnabled(false);
	    	contentPane.jxpanelPlotOptions.jcboRetentionFactorCompounds.setEnabled(false);
	    	contentPane.jxpanelPlotOptions.jcboPositionCompounds.setEnabled(false);
	    	
	    	contentPane.m_GraphControl.setSecondYAxisVisible(true);
	    	contentPane.m_GraphControl.setSecondYAxisTitle("Oven temperature");
	    	contentPane.m_GraphControl.setSecondYAxisBaseUnit("\u00b0C", "\u00b0C");
	    	
	    	performCalculations();
	    }
	    else if (strActionCommand == "Plot hold-up time")
	    {
	    	m_iSecondPlotType = 2;
	    	contentPane.jxpanelPlotOptions.jrdoNoPlot.setSelected(false);
	    	contentPane.jxpanelPlotOptions.jrdoTemperature.setSelected(false);
	    	contentPane.jxpanelPlotOptions.jrdoHoldUpTime.setSelected(true);
	    	contentPane.jxpanelPlotOptions.jrdoMobilePhaseViscosity.setSelected(false);
	    	contentPane.jxpanelPlotOptions.jrdoFlowVelocity.setSelected(false);
	    	contentPane.jxpanelPlotOptions.jrdoMobilePhaseDensity.setSelected(false);
	    	contentPane.jxpanelPlotOptions.jrdoPressure.setSelected(false);
	    	contentPane.jxpanelPlotOptions.jrdoRetentionFactor.setSelected(false);
	    	contentPane.jxpanelPlotOptions.jrdoPosition.setSelected(false);
	    	contentPane.jxpanelPlotOptions.jsliderFlowVelocityPosition.setEnabled(false);
	    	contentPane.jxpanelPlotOptions.jtxtFlowVelocityColumnPosition.setEnabled(false);
	    	contentPane.jxpanelPlotOptions.jsliderDensityPosition.setEnabled(false);
	    	contentPane.jxpanelPlotOptions.jtxtDensityColumnPosition.setEnabled(false);
	    	contentPane.jxpanelPlotOptions.jsliderPressurePosition.setEnabled(false);
	    	contentPane.jxpanelPlotOptions.jtxtPressurePosition.setEnabled(false);
	    	contentPane.jxpanelPlotOptions.jcboRetentionFactorCompounds.setEnabled(false);
	    	contentPane.jxpanelPlotOptions.jcboPositionCompounds.setEnabled(false);

	    	contentPane.m_GraphControl.setSecondYAxisVisible(true);
	    	contentPane.m_GraphControl.setSecondYAxisTitle("Hold-up time");
	    	contentPane.m_GraphControl.setSecondYAxisBaseUnit("seconds", "s");

	    	performCalculations();
	    }
	    else if (strActionCommand == "Plot mobile phase viscosity")
	    {
	    	m_iSecondPlotType = 3;
	    	contentPane.jxpanelPlotOptions.jrdoNoPlot.setSelected(false);
	    	contentPane.jxpanelPlotOptions.jrdoTemperature.setSelected(false);
	    	contentPane.jxpanelPlotOptions.jrdoHoldUpTime.setSelected(false);
	    	contentPane.jxpanelPlotOptions.jrdoMobilePhaseViscosity.setSelected(true);
	    	contentPane.jxpanelPlotOptions.jrdoFlowVelocity.setSelected(false);
	    	contentPane.jxpanelPlotOptions.jrdoMobilePhaseDensity.setSelected(false);
	    	contentPane.jxpanelPlotOptions.jrdoPressure.setSelected(false);
	    	contentPane.jxpanelPlotOptions.jrdoRetentionFactor.setSelected(false);
	    	contentPane.jxpanelPlotOptions.jrdoPosition.setSelected(false);
	    	contentPane.jxpanelPlotOptions.jsliderFlowVelocityPosition.setEnabled(false);
	    	contentPane.jxpanelPlotOptions.jtxtFlowVelocityColumnPosition.setEnabled(false);
	    	contentPane.jxpanelPlotOptions.jsliderDensityPosition.setEnabled(false);
	    	contentPane.jxpanelPlotOptions.jtxtDensityColumnPosition.setEnabled(false);
	    	contentPane.jxpanelPlotOptions.jsliderPressurePosition.setEnabled(false);
	    	contentPane.jxpanelPlotOptions.jtxtPressurePosition.setEnabled(false);
	    	contentPane.jxpanelPlotOptions.jcboRetentionFactorCompounds.setEnabled(false);
	    	contentPane.jxpanelPlotOptions.jcboPositionCompounds.setEnabled(false);
	    	
	    	contentPane.m_GraphControl.setSecondYAxisVisible(true);
	    	contentPane.m_GraphControl.setSecondYAxisTitle("Mobile phase viscosity");
	    	contentPane.m_GraphControl.setSecondYAxisBaseUnit("pascal seconds", "Pa s");
	    	
	    	performCalculations();
	    }
	    else if (strActionCommand == "Plot flow velocity")
	    {
	    	m_iSecondPlotType = 4;
	    	contentPane.jxpanelPlotOptions.jrdoNoPlot.setSelected(false);
	    	contentPane.jxpanelPlotOptions.jrdoTemperature.setSelected(false);
	    	contentPane.jxpanelPlotOptions.jrdoHoldUpTime.setSelected(false);
	    	contentPane.jxpanelPlotOptions.jrdoMobilePhaseViscosity.setSelected(false);
	    	contentPane.jxpanelPlotOptions.jrdoFlowVelocity.setSelected(true);
	    	contentPane.jxpanelPlotOptions.jrdoMobilePhaseDensity.setSelected(false);
	    	contentPane.jxpanelPlotOptions.jrdoPressure.setSelected(false);
	    	contentPane.jxpanelPlotOptions.jrdoRetentionFactor.setSelected(false);
	    	contentPane.jxpanelPlotOptions.jrdoPosition.setSelected(false);
	    	contentPane.jxpanelPlotOptions.jsliderFlowVelocityPosition.setEnabled(true);
	    	contentPane.jxpanelPlotOptions.jtxtFlowVelocityColumnPosition.setEnabled(true);
	    	contentPane.jxpanelPlotOptions.jsliderDensityPosition.setEnabled(false);
	    	contentPane.jxpanelPlotOptions.jtxtDensityColumnPosition.setEnabled(false);
	    	contentPane.jxpanelPlotOptions.jsliderPressurePosition.setEnabled(false);
	    	contentPane.jxpanelPlotOptions.jtxtPressurePosition.setEnabled(false);
	    	contentPane.jxpanelPlotOptions.jcboRetentionFactorCompounds.setEnabled(false);
	    	contentPane.jxpanelPlotOptions.jcboPositionCompounds.setEnabled(false);
	    	
	    	contentPane.m_GraphControl.setSecondYAxisVisible(true);
	    	contentPane.m_GraphControl.setSecondYAxisTitle("Flow velocity");
	    	contentPane.m_GraphControl.setSecondYAxisBaseUnit("meters/second", "m/s");
	    	
	    	performCalculations();
	    }
	    else if (strActionCommand == "Plot mobile phase density")
	    {
	    	m_iSecondPlotType = 5;
	    	contentPane.jxpanelPlotOptions.jrdoNoPlot.setSelected(false);
	    	contentPane.jxpanelPlotOptions.jrdoTemperature.setSelected(false);
	    	contentPane.jxpanelPlotOptions.jrdoHoldUpTime.setSelected(false);
	    	contentPane.jxpanelPlotOptions.jrdoMobilePhaseViscosity.setSelected(false);
	    	contentPane.jxpanelPlotOptions.jrdoFlowVelocity.setSelected(false);
	    	contentPane.jxpanelPlotOptions.jrdoMobilePhaseDensity.setSelected(true);
	    	contentPane.jxpanelPlotOptions.jrdoPressure.setSelected(false);
	    	contentPane.jxpanelPlotOptions.jrdoRetentionFactor.setSelected(false);
	    	contentPane.jxpanelPlotOptions.jrdoPosition.setSelected(false);
	    	contentPane.jxpanelPlotOptions.jsliderFlowVelocityPosition.setEnabled(false);
	    	contentPane.jxpanelPlotOptions.jtxtFlowVelocityColumnPosition.setEnabled(false);
	    	contentPane.jxpanelPlotOptions.jsliderDensityPosition.setEnabled(true);
	    	contentPane.jxpanelPlotOptions.jtxtDensityColumnPosition.setEnabled(true);
	    	contentPane.jxpanelPlotOptions.jsliderPressurePosition.setEnabled(false);
	    	contentPane.jxpanelPlotOptions.jtxtPressurePosition.setEnabled(false);
	    	contentPane.jxpanelPlotOptions.jcboRetentionFactorCompounds.setEnabled(false);
	    	contentPane.jxpanelPlotOptions.jcboPositionCompounds.setEnabled(false);
	    	
	    	contentPane.m_GraphControl.setSecondYAxisVisible(true);
	    	contentPane.m_GraphControl.setSecondYAxisTitle("Mobile phase density");
	    	contentPane.m_GraphControl.setSecondYAxisBaseUnit("grams/liter", "g/L");
	    	
	    	performCalculations();
	    }
	    else if (strActionCommand == "Plot pressure")
	    {
	    	m_iSecondPlotType = 6;
	    	contentPane.jxpanelPlotOptions.jrdoNoPlot.setSelected(false);
	    	contentPane.jxpanelPlotOptions.jrdoTemperature.setSelected(false);
	    	contentPane.jxpanelPlotOptions.jrdoHoldUpTime.setSelected(false);
	    	contentPane.jxpanelPlotOptions.jrdoMobilePhaseViscosity.setSelected(false);
	    	contentPane.jxpanelPlotOptions.jrdoFlowVelocity.setSelected(false);
	    	contentPane.jxpanelPlotOptions.jrdoMobilePhaseDensity.setSelected(false);
	    	contentPane.jxpanelPlotOptions.jrdoPressure.setSelected(true);
	    	contentPane.jxpanelPlotOptions.jrdoRetentionFactor.setSelected(false);
	    	contentPane.jxpanelPlotOptions.jrdoPosition.setSelected(false);
	    	contentPane.jxpanelPlotOptions.jsliderFlowVelocityPosition.setEnabled(false);
	    	contentPane.jxpanelPlotOptions.jtxtFlowVelocityColumnPosition.setEnabled(false);
	    	contentPane.jxpanelPlotOptions.jsliderDensityPosition.setEnabled(false);
	    	contentPane.jxpanelPlotOptions.jtxtDensityColumnPosition.setEnabled(false);
	    	contentPane.jxpanelPlotOptions.jsliderPressurePosition.setEnabled(true);
	    	contentPane.jxpanelPlotOptions.jtxtPressurePosition.setEnabled(true);
	    	contentPane.jxpanelPlotOptions.jcboRetentionFactorCompounds.setEnabled(false);
	    	contentPane.jxpanelPlotOptions.jcboPositionCompounds.setEnabled(false);
	    	
	    	contentPane.m_GraphControl.setSecondYAxisVisible(true);
	    	contentPane.m_GraphControl.setSecondYAxisTitle("Pressure");
	    	contentPane.m_GraphControl.setSecondYAxisBaseUnit("pascals", "Pa");
	    	
	    	performCalculations();
	    }
	    else if (strActionCommand == "Plot retention factor")
	    {
	    	m_iSecondPlotType = 7;
	    	contentPane.jxpanelPlotOptions.jrdoNoPlot.setSelected(false);
	    	contentPane.jxpanelPlotOptions.jrdoTemperature.setSelected(false);
	    	contentPane.jxpanelPlotOptions.jrdoHoldUpTime.setSelected(false);
	    	contentPane.jxpanelPlotOptions.jrdoMobilePhaseViscosity.setSelected(false);
	    	contentPane.jxpanelPlotOptions.jrdoFlowVelocity.setSelected(false);
	    	contentPane.jxpanelPlotOptions.jrdoMobilePhaseDensity.setSelected(false);
	    	contentPane.jxpanelPlotOptions.jrdoPressure.setSelected(false);
	    	contentPane.jxpanelPlotOptions.jrdoRetentionFactor.setSelected(true);
	    	contentPane.jxpanelPlotOptions.jrdoPosition.setSelected(false);
	    	contentPane.jxpanelPlotOptions.jsliderFlowVelocityPosition.setEnabled(false);
	    	contentPane.jxpanelPlotOptions.jtxtFlowVelocityColumnPosition.setEnabled(false);
	    	contentPane.jxpanelPlotOptions.jsliderDensityPosition.setEnabled(false);
	    	contentPane.jxpanelPlotOptions.jtxtDensityColumnPosition.setEnabled(false);
	    	contentPane.jxpanelPlotOptions.jsliderPressurePosition.setEnabled(false);
	    	contentPane.jxpanelPlotOptions.jtxtPressurePosition.setEnabled(false);
	    	contentPane.jxpanelPlotOptions.jcboRetentionFactorCompounds.setEnabled(true);
	    	contentPane.jxpanelPlotOptions.jcboPositionCompounds.setEnabled(false);
	    	
	    	contentPane.m_GraphControl.setSecondYAxisVisible(true);
	    	contentPane.m_GraphControl.setSecondYAxisBaseUnit("k", "");
	    	int iSelectedCompound = contentPane.jxpanelPlotOptions.jcboRetentionFactorCompounds.getSelectedIndex();
	    	String strCompoundName = "";
	    	
	    	if (iSelectedCompound < m_vectCompound.size() && iSelectedCompound >= 0)
	    		strCompoundName = this.m_vectCompound.get(iSelectedCompound).strCompoundName;
	    	
	    	contentPane.m_GraphControl.setSecondYAxisTitle("Retention factor of " + strCompoundName);

	    	performCalculations();
	    }
	    else if (strActionCommand == "Plot position")
	    {
	    	m_iSecondPlotType = 8;
	    	contentPane.jxpanelPlotOptions.jrdoNoPlot.setSelected(false);
	    	contentPane.jxpanelPlotOptions.jrdoTemperature.setSelected(false);
	    	contentPane.jxpanelPlotOptions.jrdoHoldUpTime.setSelected(false);
	    	contentPane.jxpanelPlotOptions.jrdoMobilePhaseViscosity.setSelected(false);
	    	contentPane.jxpanelPlotOptions.jrdoFlowVelocity.setSelected(false);
	    	contentPane.jxpanelPlotOptions.jrdoMobilePhaseDensity.setSelected(false);
	    	contentPane.jxpanelPlotOptions.jrdoPressure.setSelected(false);
	    	contentPane.jxpanelPlotOptions.jrdoRetentionFactor.setSelected(false);
	    	contentPane.jxpanelPlotOptions.jrdoPosition.setSelected(true);
	    	contentPane.jxpanelPlotOptions.jsliderFlowVelocityPosition.setEnabled(false);
	    	contentPane.jxpanelPlotOptions.jtxtFlowVelocityColumnPosition.setEnabled(false);
	    	contentPane.jxpanelPlotOptions.jsliderDensityPosition.setEnabled(false);
	    	contentPane.jxpanelPlotOptions.jtxtDensityColumnPosition.setEnabled(false);
	    	contentPane.jxpanelPlotOptions.jsliderPressurePosition.setEnabled(false);
	    	contentPane.jxpanelPlotOptions.jtxtPressurePosition.setEnabled(false);
	    	contentPane.jxpanelPlotOptions.jcboRetentionFactorCompounds.setEnabled(false);
	    	contentPane.jxpanelPlotOptions.jcboPositionCompounds.setEnabled(true);
	    	
	    	contentPane.m_GraphControl.setSecondYAxisVisible(true);
	    	int iSelectedCompound = contentPane.jxpanelPlotOptions.jcboPositionCompounds.getSelectedIndex();
	    	String strCompoundName = "";
	    	
	    	if (iSelectedCompound < m_vectCompound.size() && iSelectedCompound >= 0)
	    		strCompoundName = this.m_vectCompound.get(iSelectedCompound).strCompoundName;
	    	
	    	contentPane.m_GraphControl.setSecondYAxisTitle("Position of " + strCompoundName + " along column");
	    	contentPane.m_GraphControl.setSecondYAxisBaseUnit("meters", "m");
	    	contentPane.m_GraphControl.setSecondYAxisRangeLimits(0.0, m_dColumnLength);
	    	
	    	performCalculations();
	    }
	    else if (strActionCommand == "RetentionFactorCompoundChanged")
	    {
	    	if (this.m_iSecondPlotType == 7)
	    	{
	    		int iSelectedCompound = contentPane.jxpanelPlotOptions.jcboRetentionFactorCompounds.getSelectedIndex();
	    		String strCompoundName = "";
	    		
	    		if (iSelectedCompound < m_vectCompound.size() && iSelectedCompound >= 0)
		    		strCompoundName = this.m_vectCompound.get(iSelectedCompound).strCompoundName;
	    		
		    	contentPane.m_GraphControl.setSecondYAxisTitle("Retention factor of " + strCompoundName);
	    	}

	    	performCalculations();  	
	    }
	    else if (strActionCommand == "PositionCompoundChanged")
	    {
	    	if (this.m_iSecondPlotType == 8)
	    	{
	    		int iSelectedCompound = contentPane.jxpanelPlotOptions.jcboPositionCompounds.getSelectedIndex();
	    		String strCompoundName = "";
	    		
	    		if (iSelectedCompound < m_vectCompound.size() && iSelectedCompound >= 0)
	    			strCompoundName = this.m_vectCompound.get(iSelectedCompound).strCompoundName;
	    		
		    	contentPane.m_GraphControl.setSecondYAxisTitle("Position of " + strCompoundName + " along column");
	    	}
	    	
	    	performCalculations();  	
	    }
	    else if (strActionCommand == "Inlet pressure units changed")
	    {
	    	if (this.contentPane.jxpanelInletOutlet.jcboInletPressureUnits.getSelectedIndex() == 0)
	    	{
	    		this.contentPane.jxpanelInletOutlet.jtxtInletPressure.setText(Float.toString((float)(this.m_dInletPressure - 101325) / 1000));
	    	}
	    	else
	    	{
	    		this.contentPane.jxpanelInletOutlet.jtxtInletPressure.setText(Float.toString((float)(this.m_dInletPressure) / 1000));
	    	}
	    }
	    else if (strActionCommand == "Copy Image")
	    {
	    	ByteBuffer bytePixels = contentPane.m_GraphControl.getPixels();
	    	Image image;
	    	int h = contentPane.m_GraphControl.getHeight();
	    	int w = contentPane.m_GraphControl.getWidth();
	    	if (w % 4 > 0)
        		w += 4 - (w % 4);
	    	
	    	byte[] flippedPixels = new byte[bytePixels.array().length];

	    	for (int y = 0; y < h; y++)
	    	{
	    		for (int x = 0; x < w * 4; x++)
	    		{
	    			flippedPixels[(y * w * 4) + x] = bytePixels.array()[((h - y - 1) * w * 4) + x];
	    		}
	    	}

	    	DataBuffer dbuf = new DataBufferByte(flippedPixels, flippedPixels.length, 0);

	    	int[] bandOffsets = {0,1,2,3};
	    	SampleModel sampleModel = new ComponentSampleModel(DataBuffer.TYPE_BYTE, contentPane.m_GraphControl.getWidth(), contentPane.m_GraphControl.getHeight(), 4, w * 4, bandOffsets);
	    	WritableRaster raster = Raster.createWritableRaster(sampleModel, dbuf, null);
	    	ComponentColorModel colorModel = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB),
	    					 new int[] {8,8,8,8},
	    					 true,
	    					 false,
	    					 ComponentColorModel.OPAQUE,
	    					 DataBuffer.TYPE_BYTE);
	    	image = new BufferedImage(colorModel, raster, false, null);
	        
	        new javax.swing.ImageIcon(image); // Force load.
	        BufferedImage newImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
	        newImage.createGraphics().drawImage(image, 0, 0, null);
	        image = newImage;
	        
	        ImageSelection imageSelection = new ImageSelection(image);
	        Toolkit toolkit = Toolkit.getDefaultToolkit();
	        toolkit.getSystemClipboard().setContents(imageSelection, null);
	    }
	    else if (evt.getSource() == this.menuResetToDefaultValuesAction)
		{
			if (this.m_bDocumentChangedFlag)
			{
				String fileName;
				if (this.m_currentFile == null)
					fileName = "Untitled";
				else
					fileName = m_currentFile.getName();
				
		        int result = JOptionPane.showConfirmDialog(this,"Do you want to save changes to " + fileName + "?", "GC Simulator", JOptionPane.YES_NO_CANCEL_OPTION);
		        
		        if (result == JOptionPane.YES_OPTION)
		        {
		        	if (!saveFile(false))
		        		return;
		        }
		        else if (result == JOptionPane.CANCEL_OPTION)
		        {
		        	return;
		        }
			}
			m_currentFile = null;
			
			loadFile(null);
		}
		else if (evt.getSource() == this.menuLoadSettingsAction)
		{
			JFileChooser fc = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter("GC Simulator Files (*.gcsim)", "gcsim");
			fc.setFileFilter(filter);
			fc.setDialogTitle("Open");
			int returnVal = fc.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) 
            {
                m_currentFile = fc.getSelectedFile();

                loadFile(m_currentFile);
            }
		}
		else if (evt.getSource() == this.menuSaveSettingsAction)
		{
			saveFile(false);
		}
		else if (evt.getSource() == this.menuSaveSettingsAsAction)
		{
			saveFile(true);
		}
		else if (evt.getSource() == this.menuExitAction)
		{
			if (this.m_bDocumentChangedFlag)
			{
				String fileName;
				if (this.m_currentFile == null)
					fileName = "Untitled";
				else
					fileName = m_currentFile.getName();
				
		        int result = JOptionPane.showConfirmDialog(this,"Do you want to save changes to " + fileName + "?", "GC Simulator", JOptionPane.YES_NO_CANCEL_OPTION);
		        
		        if (result == JOptionPane.YES_OPTION)
		        {
		        	if (!saveFile(false))
		        		return;
		        }
		        else if (result == JOptionPane.CANCEL_OPTION)
		        {
		        	return;
		        }
			}
			
			this.setVisible(false);
			System.exit(0); 
		}
		else if (evt.getSource() == this.menuAboutAction)
		{
			//Frame[] frames = Frame.getFrames();
			AboutDialog aboutDialog = new AboutDialog(this);
	    	Point dialogPosition = new Point(this.getSize().width / 2, this.getSize().height / 2);
	    	dialogPosition.x -= aboutDialog.getWidth() / 2;
	    	dialogPosition.y -= aboutDialog.getHeight() / 2;
	    	aboutDialog.setLocation(dialogPosition);
	    	
	    	// Show the dialog.
	    	aboutDialog.setVisible(true);

		}
		else if (evt.getSource() == this.menuHelpTopicsAction)
		{
			//Globals.hbMainHelpBroker.setCurrentID("getting_started");
			//Globals.hbMainHelpBroker.setDisplayed(true);
		}
	}

	//@Override
	public void stateChanged(ChangeEvent e) 
	{
		JSlider source = (JSlider)e.getSource();
		if (source.getName() == "Temperature Slider")
		{
			if (m_bSliderUpdate == false)
				return;
			
			m_dTemperature = contentPane.jxpanelIsothermalOptions.jsliderTemperature.getValue();
			contentPane.jxpanelIsothermalOptions.jtxtTemperature.setText(Integer.toString((int)m_dTemperature));
			performCalculations();
		}
		else if (source.getName() == "Plot flow velocity position slider")
		{
			if (m_bSliderUpdate == false)
				return;
			
			this.m_dPlotFlowVelocityPosition = (double)contentPane.jxpanelPlotOptions.jsliderFlowVelocityPosition.getValue() / 1000d;
			contentPane.jxpanelPlotOptions.jtxtFlowVelocityColumnPosition.setText(Float.toString((float)m_dPlotFlowVelocityPosition));
			performCalculations();
		}
		else if (source.getName() == "Plot gas density position slider")
		{
			if (m_bSliderUpdate == false)
				return;
			
			this.m_dPlotGasDensityPosition = (double)contentPane.jxpanelPlotOptions.jsliderDensityPosition.getValue() / 1000d;
			contentPane.jxpanelPlotOptions.jtxtDensityColumnPosition.setText(Float.toString((float)m_dPlotGasDensityPosition));
			performCalculations();
		}
		else if (source.getName() == "Plot pressure position slider")
		{
			if (m_bSliderUpdate == false)
				return;
			
			this.m_dPlotPressurePosition = (double)contentPane.jxpanelPlotOptions.jsliderPressurePosition.getValue() / 1000d;
			contentPane.jxpanelPlotOptions.jtxtPressurePosition.setText(Float.toString((float)m_dPlotPressurePosition));
			performCalculations();
		}

	}

	//@Override
	public void keyPressed(KeyEvent arg0) 
	{
		
	}

	//@Override
	public void keyReleased(KeyEvent e) 
	{

	}

	//@Override
	public void keyTyped(KeyEvent e) 
	{
		//JTextField source = (JTextField)e.getSource();
		
		if (!((Character.isDigit(e.getKeyChar()) ||
				(e.getKeyChar() == KeyEvent.VK_BACK_SPACE) ||
				(e.getKeyChar() == KeyEvent.VK_DELETE) ||
				(e.getKeyChar() == KeyEvent.VK_PERIOD))))
		{
	        e.consume();
		}
		
		if (e.getKeyChar() == KeyEvent.VK_ENTER)
		{
			performCalculations();
		}
		
	}

	/**
	 * Returns the viscosity of a gas with a specified temperature. Gas
	 * viscosity is virtually independent of pressure (Blumberg pg. 72,85) 
	 *
	 * @param  dTempK the temperature of the gas (in Kelvin)
	 * @param  dGasType the type of gas (0 = H2, 1 = He, 2 = N2, 3 = Ar)
	 * @return      the gas viscosity in Pa s
	 */
	double calcGasViscosity (double dTempK, double dGasType)
	{
		// Determine gas viscosity parameters (pg. 72 Blumberg)
		double dEtast = 18.63; // (in uPa s)
		double dE0 = 0.6958; // (dimensionless)
		double dE1 = -0.0071; // (dimensionless)
		double dRefTempK = 273.15;
		
		if (dGasType == 0)
		{
			// Hydrogen
			dEtast = 8.382;
			dE0 = 0.6892;
			dE1 = 0.005;
		}
		else if (dGasType == 1)
		{
			// Helium
			dEtast = 18.63;
			dE0 = 0.6958;
			dE1 = -0.0071;
		}
		else if (dGasType == 2)
		{
			// Nitrogen
			dEtast = 16.62;
			dE0 = 0.7665;
			dE1 = -0.0378;
		}
		else if (dGasType == 3)
		{
			// Argon
			dEtast = 21.04;
			dE0 = 0.8131;
			dE1 = -0.0426;
		}
		
		double dEta = (dEtast * 0.000001) * Math.pow(dTempK / dRefTempK, dE0 + dE1 * ((dTempK - dRefTempK) / dRefTempK));
		return dEta;
	}
	
	/**
	 * Returns the gas self diffusion coefficient of a gas for a
	 * specified temperature and pressure. (Blumberg pg. 74) 
	 *
	 * @param  dTempK the temperature of the gas (in Kelvin)
	 * @param  dPressure the pressure of the gas (in Pa)
	 * @param  dGasType the type of gas (0 = H2, 1 = He, 2 = N2, 3 = Ar)
	 * @return      the gas diffusion coefficient in m^2 s^-1
	 */
	double calcGasSelfDiffusionCoefficient (double dTempK, double dPressure, double dGasType)
	{
		double dR = 8.3144621; // (in m^3?Pa?K^-1?mol^-1)
		double dM = 4.003;
		double dE = 0.685;
		
		if (dGasType == 0)
		{
			// Hydrogen
			dM = 2.016;
			dE = 0.698;
		}
		else if (dGasType == 1)
		{
			// Helium
			dM = 4.003;
			dE = 0.685;
		}
		else if (dGasType == 2)
		{
			// Nitrogen
			dM = 28.01;
			dE = 0.710;
		}
		else if (dGasType == 3)
		{
			// Argon
			dM = 39.95;
			dE = 0.750;
		}
		
		double dDgst = (6 * dR * 273.15 * this.calcGasViscosity(273.15, dGasType)) / (5 * (dM / 1000) * 101325);
		double dDg = dDgst * (101325 / dPressure) * Math.pow(dTempK / 273.15, 1 + dE);
		return dDg;
	}
	
	/**
	 * Returns the diffusion coefficient for a solute in a given gas at a
	 * specified temperature and pressure. Uses the Fuller-Giddings empirical formula. (Blumberg pg. 75) 
	 *
	 * @param  dTempK the temperature of the gas (in Kelvin)
	 * @param  dPressure the pressure of the gas (in Pa)
	 * @param  dGasType the type of gas (0 = H2, 1 = He, 2 = N2, 3 = Ar)
	 * @return      the gas diffusion coefficient in m^2 s^-1
	 */
	double calcSoluteDiffusivityInGas (double dTempK, double dPressure, double dGasType)
	{
		double dR = 8.3144621; // (in m^3?Pa?K^-1?mol^-1)
		double dMg = 4.003;
		double dVg = 2.67;
		
		if (dGasType == 0)
		{
			// Hydrogen
			dMg = 2.016;
			dVg = 6.12;
		}
		else if (dGasType == 1)
		{
			// Helium
			dMg = 4.003;
			dVg = 2.67;
		}
		else if (dGasType == 2)
		{
			// Nitrogen
			dMg = 28.01;
			dVg = 18.5;
		}
		else if (dGasType == 3)
		{
			// Argon
			dMg = 39.95;
			dVg = 16.2;
		}
		
		// TODO: Change these to actual values for the solutes
		double dMsol = 100;
		double dVsol = 200;
		// in cm^2/s
		double dDsol = ((100 * Math.sqrt((1 / dMg) + (1 / dMsol))) / Math.pow(Math.pow(dVg, 1.0/3.0) + Math.pow(dVsol, 1.0/3.0), 2)) * (1 / dPressure) * Math.pow(dTempK, 1.75);
		
		return dDsol * 0.0001; // in m^2/s
	}
	
	/**
	 * Returns the gas pressure at a given point along the column,
	 * with a given inlet and outlet pressure (Blumberg pg. 103) 
	 *
	 * @param  dZPos the z position along the column, 0 (inlet) to 1 (outlet)
	 * @param  dInletPressure the pressure at the column inlet (in Pa)
	 * @param  dOutletPressure the pressure at the column outlet (in Pa)
	 * @return      the gas pressure (in Pa)
	 */
	double calcGasPressure (double dZPos, double dInletPressure, double dOutletPressure)
	{
		double dLext = Math.pow(dInletPressure, 2) / (Math.pow(dInletPressure, 2) - Math.pow(dOutletPressure, 2));
		double dPressure = dInletPressure * Math.sqrt(1 - (dZPos / dLext));
		return dPressure;
	}
	
	/**
	 * Returns the hold-up time for a given temperature, inlet pressure,
	 * and outlet pressure.
	 *
	 * @param  dTempK the temperature of the gas (in Kelvin)
	 * @param  dGasType the type of gas (0 = H2, 1 = He, 2 = N2, 3 = Ar)
	 * @param  dInletPressure the pressure at the column inlet (in Pa)
	 * @param  dOutletPressure the pressure at the column outlet (in Pa)
	 * @param  dColumnLength the column length (in m)
	 * @param  dInnerDiameter the column inner diameter (in m)
	 * @return      the hold-up time (in s)
	 */
	double calcHoldUpTime (double dTempK, double dGasType, double dInletPressure, double dOutletPressure, double dColumnLength, double dInnerDiameter)
	{
		double dOmega = dColumnLength * calcGasViscosity(dTempK, dGasType) * (32.0 / Math.pow(dInnerDiameter, 2));
		double dDeadTime = (4 * dOmega * this.m_dColumnLength * (Math.pow(dInletPressure, 3) - Math.pow(dOutletPressure, 3))) / (3 * Math.pow(Math.pow(dInletPressure, 2) - Math.pow(dOutletPressure, 2), 2));				
		return dDeadTime;
	}

	/**
	 * Returns the velocity of a gas at a specific point along the column.
	 * (Blumberg pg. 103, 7.67, 7.50, 7.38) 
	 *
	 * @param  dZPos the z position along the column, 0 (inlet) to 1 (outlet)
	 * @param  dTempK the temperature of the gas (in Kelvin)
	 * @param  dGasType the type of gas (0 = H2, 1 = He, 2 = N2, 3 = Ar)
	 * @param  dInletPressure the pressure at the column inlet (in Pa)
	 * @param  dOutletPressure the pressure at the column outlet (in Pa)
	 * @param  dInnerDiameter the column inner diameter (in m)
	 * @param  dColumnLength the column length (in m)
	 * @return      the gas velocity in m/s
	 */
	double calcFlowVelocity (double dZPos, double dTempK, double dGasType, double dInletPressure, double dOutletPressure, double dInnerDiameter, double dColumnLength)
	{
		//if (dZPos > 0.99)
		//	dZPos = dZPos;
		double dOmega = dColumnLength * calcGasViscosity(dTempK, dGasType) * (32.0 / Math.pow(dInnerDiameter, 2));
		double dInletVelocity = (Math.pow(dInletPressure, 2) - Math.pow(dOutletPressure, 2)) / (2 * dOmega * dInletPressure);
		//BigDecimal bdInletPressure = BigDecimal.valueOf(dInletPressure).pow(2);
		//BigDecimal bdOutletPressure = BigDecimal.valueOf(dOutletPressure).pow(2);
		// Difference between inlet pressure and the difference between the inlet and outlet pressure is too small
		// It's ok if dLext rounds to 1. It doesn't cause significant error in dZPos/dLext.
		double dLext = (double)(Math.pow(dInletPressure, 2) / (Math.pow(dInletPressure, 2) - Math.pow(dOutletPressure, 2)));
		//BigDecimal bdLext = (bdInletPressure.divide(bdInletPressure.subtract(bdOutletPressure), BigDecimal.ROUND_HALF_UP));
		//double dShort = BigDecimal.valueOf(1).subtract(BigDecimal.valueOf(dZPos).divide(bdLext, BigDecimal.ROUND_HALF_UP)).doubleValue();
		double dVelocityAtZ = dInletVelocity / Math.sqrt(1 - (dZPos / dLext));
		//double dVelocityAtZ = dInletVelocity / Math.sqrt(dShort);
		return dVelocityAtZ;
	}
	
	/**
	 * Returns the inlet pressure for a specified flow rate.
	 * (Blumberg pg. 101-102, 7.60 and 7.53) 
	 *
	 * @param  dFlowRate the normalized volumetric flow rate (in mL/min)
	 * @param  dTempK the temperature of the gas (in Kelvin)
	 * @param  dGasType the type of gas (0 = H2, 1 = He, 2 = N2, 3 = Ar)
	 * @param  dOutletPressure the pressure at the column outlet (in Pa)
	 * @param  dInnerDiameter the column inner diameter (in m)
	 * @param  dColumnLength the column length (in m)
	 * @return      the inlet pressure in Pa
	 */
	double calcInletPressureAtConstFlowRate (double dFlowRate, double dTempK, double dGasType, double dOutletPressure, double dInnerDiameter, double dColumnLength)
	{
		double dRefTempK = 298.15;
		double dPst = 101325; // (in Pa)
		double dOmega = dColumnLength * calcGasViscosity(dTempK, dGasType) * (32.0 / Math.pow(dInnerDiameter, 2));
		double dSpecificFlowRate = (dTempK / dRefTempK) * ((dFlowRate / (1000000 * 60)) / dInnerDiameter); // in m^2 / min
		double dInletPressure = Math.sqrt(((dSpecificFlowRate * 8 * dPst * dOmega) / (Math.PI * dInnerDiameter)) + Math.pow(dOutletPressure, 2));
		return dInletPressure;
	}

	/**
	 * Returns the diffusion coefficient for a solute in the stationary
	 * phase at a given temperature. This is only a rough approximation.
	 * It is calculated by a fit to data from C.A. Cramers, C.E. Van Tilburg, C.P.M. Schutjes, J.A. Rijks, G.A. Rutten, R. De Nijs, Journal of Chromatography A 279 (1983) 83–89.
	 * It does not account for differences between solutes nor between stationary phases.
	 *
	 * @param  dTempK the temperature of the phase (in Kelvin)
	 * @return      the solute diffusion coefficient in m^2 s^-1
	 */
	double calcSoluteDiffusivityInStationaryPhase (double dTempK)
	{
		double dR = 8.3144621; // (in m^3?Pa?K^-1?mol^-1)
		// dK and dE were fit parameters from data in C.A. Cramers, C.E. Van Tilburg, C.P.M. Schutjes, J.A. Rijks, G.A. Rutten, R. De Nijs, Journal of Chromatography A 279 (1983) 83–89.

		double dK = -14.36381977;
		double dE = 22101.91968;
		double dDs = Math.exp(dK - (dE/(dR * dTempK)));
		
		return dDs; // in m^2/s
	}

	/**
	 * Returns the plate height using the Golay equation. (Blumberg pg. 225)
	 *
	 * @param  dSoluteDiffusionCoefficientInGas the solute diffusion coefficient in the mobile phase (in m^2/s)
	 * @param  dSoluteDiffusionCoefficientInStationaryPhase the solute diffusion coefficient in the stationary phase (in m^2/s)
	 * @param  dFlowVelocity the flow velocity at the position of the solute (in m/s)
	 * @param  dInnerDiameter the inner diameter of the column (in m)
	 * @param  dRetentionFactor the instantaneous retention factor of the solute
	 * @param  dFilmThickness the thickness of the stationary phase film (in m)
	 *
	 * @return      the plate height in m^2/m
	 */
	double calcPlateHeight (double dSoluteDiffusionCoefficientInGas, double dSoluteDiffusionCoefficientInStationaryPhase, double dFlowVelocity, double dInnerDiameter, double dRetentionFactor, double dFilmThickness)
	{
		double dTerm1 = (2 * dSoluteDiffusionCoefficientInGas) / dFlowVelocity;
		double dTerm2 = ((1 + 6 * dRetentionFactor + 11 * Math.pow(dRetentionFactor,2)) * Math.pow(dInnerDiameter, 2) * dFlowVelocity) / (96 * Math.pow(1 + dRetentionFactor, 2) * dSoluteDiffusionCoefficientInGas);
		double dTerm3 = (2 * Math.pow(dFilmThickness, 2) * dRetentionFactor * dFlowVelocity) / (3 * Math.pow(1 + dRetentionFactor, 2) * dSoluteDiffusionCoefficientInStationaryPhase);

		return dTerm1 + dTerm2 + dTerm3; // in m^2/m
	}
	
	public void performCalculations()
	{
		NumberFormat formatter = new DecimalFormat("#0.0000");
		
		validateTemperature();
		validateColumnDiameter();
		validateColumnLength();
		validateStartTime();
		validateEndTime();
		validateFilmThickness();
		validateFlowRate();
		validateInitialTemperature();
		validateInitialTime();
		validateInjectionVolume();
		validateInletPressure();
		validateLinerInnerDiameter();
		validateLinerLength();
		validateNoise();
		validateNumPoints();
		validateOtherOutletPressure();
		validateSignalOffset();
		validateSplitRatio();
		validateTimeConstant();
		validateFlowVelocityAtColumnPosition();
		validateGasDensityAtColumnPosition();
		validatePressureAtColumnPosition();
		
		calculateTemperatureProgram();
		
		// Subtract film thickness out of the inner diameter (in m)
		double dInnerDiameter = (this.m_dColumnDiameter) - (2 * this.m_dFilmThickness);
		
		// Calculate total volume inside of column (in m^3)
		double dV0 = Math.PI * Math.pow(dInnerDiameter / 2, 2) * this.m_dColumnLength;

		// Calculate phase ratio
    	double dBeta1 = Math.pow((0.25 / 2) - (0.25 / 1000), 2) / (Math.pow(0.25 / 2, 2) - Math.pow((0.25 / 2) - (0.25 / 1000), 2));
    	double dBeta2 = Math.pow((this.m_dColumnDiameter / 2) - (m_dFilmThickness), 2) / (Math.pow(m_dColumnDiameter / 2, 2) - Math.pow((m_dColumnDiameter / 2) - (m_dFilmThickness), 2));
    	double dBeta1Beta2 = dBeta1 / dBeta2;
		contentPane.jxpanelColumnProperties.jlblPhaseRatioIndicator.setText(formatter.format(dBeta2));
	
		// Calculate gas viscosity
		if (this.m_bTemperatureProgramMode)
		{
			this.contentPane.jxpanelInletOutlet.jlblGasViscosityIndicator.setText("--");
		}
		else
		{
			double dViscosity = this.calcGasViscosity(this.m_dTemperature + 273.15, this.m_iCarrierGas);
			this.contentPane.jxpanelInletOutlet.jlblGasViscosityIndicator.setText(formatter.format(dViscosity * 1000000));
		}
		
		// Calculate hold-up time
		if (this.m_bTemperatureProgramMode)
		{
			this.contentPane.jxpanelColumnProperties.jlblHoldUpTimeIndicator.setText("--");
		}
		else
		{
			double dInletPressure;
			if (this.m_bConstantFlowRateMode)
				dInletPressure = this.calcInletPressureAtConstFlowRate(this.m_dFlowRate, this.m_dTemperature + 273.15, this.m_iCarrierGas, this.m_dOutletPressure, dInnerDiameter, this.m_dColumnLength);
			else
				dInletPressure = this.m_dInletPressure;
			
			double dHoldUpTime = this.calcHoldUpTime(this.m_dTemperature + 273.15, this.m_iCarrierGas, dInletPressure, this.m_dOutletPressure, this.m_dColumnLength, dInnerDiameter);
			this.contentPane.jxpanelColumnProperties.jlblHoldUpTimeIndicator.setText(formatter.format(dHoldUpTime / 60));
		}
		
		this.m_dLinerVolume = Math.PI * Math.pow(this.m_dLinerInnerDiameter / 2, 2) * this.m_dLinerLength;
		contentPane.jxpanelInletOutlet.jlblLinerVolumeIndicator.setText(formatter.format(m_dLinerVolume * 1000000));
		
		/*		
		m_dVoidVolume = Math.PI * Math.pow(((m_dColumnDiameter / 10) / 2), 2) * (m_dColumnLength / 10) * m_dTotalPorosity;
		contentPane.jxpanelColumnProperties.jlblVoidVolume.setText(formatter.format(m_dVoidVolume));
		
		m_dVoidTime = (m_dVoidVolume / m_dFlowRate) * 60;
		contentPane.jxpanelColumnProperties.jlblVoidTime.setText(formatter.format(m_dVoidTime));
		
		m_dOpenTubeVelocity = (m_dFlowRate / 60) / (Math.PI * Math.pow(((m_dColumnDiameter / 10) / 2), 2));
		contentPane.jxpanelChromatographyProperties.jlblOpenTubeVelocity.setText(formatter.format(m_dOpenTubeVelocity));

		m_dInterstitialVelocity = m_dOpenTubeVelocity / this.m_dInterparticlePorosity;
		contentPane.jxpanelChromatographyProperties.jlblInterstitialVelocity.setText(formatter.format(m_dInterstitialVelocity));
		
		m_dChromatographicVelocity = m_dOpenTubeVelocity / this.m_dTotalPorosity;
		contentPane.jxpanelChromatographyProperties.jlblChromatographicVelocity.setText(formatter.format(m_dChromatographicVelocity));
			
		NumberFormat bpFormatter = new DecimalFormat("#0.00");
		contentPane.jxpanelGradientOptions.jlblDwellVolumeIndicator.setText(bpFormatter.format(m_dMixingVolume + m_dNonMixingVolume));
		contentPane.jxpanelGradientOptions.jlblDwellTimeIndicator.setText(bpFormatter.format(((m_dMixingVolume + m_dNonMixingVolume) / 1000) / m_dFlowRate));

		double dTempKelvin = m_dTemperature + 273.15;

		if (!m_bGradientMode)
			contentPane.jxpanelGeneralProperties.jlblEluentViscosity.setText(formatter.format(m_dEluentViscosity));
		else
			contentPane.jxpanelGeneralProperties.jlblEluentViscosity.setText("--");
			
		// Calculate the average diffusion coefficient using Wilke-Chang empirical determination
		// See Wilke, C. R.; Chang, P. AICHE J. 1955, 1, 264-270.
		
		// First, determine association parameter
		double dAssociationParameter = ((1 - m_dSolventBFraction) * (2.6 - 1.9)) + 1.9;
		
		// Determine weighted average molecular weight of solvent
		double dSolventBMW;
		if (this.m_iSolventB == 0)
			dSolventBMW = 41;
		else
			dSolventBMW = 32;
		
		double dSolventMW = (m_dSolventBFraction * (dSolventBMW - 18)) + 18;
		
		// Determine the average molar volume
		double dAverageMolarVolume = 0;
		for (int i = 0; i < m_vectCompound.size(); i++)
		{
			dAverageMolarVolume += m_vectCompound.get(i).dMolarVolume;
		}
		dAverageMolarVolume = dAverageMolarVolume / m_vectCompound.size();
		
		// Now determine the average diffusion coefficient
		m_dDiffusionCoefficient = 0.000000074 * (Math.pow(dAssociationParameter * dSolventMW, 0.5) * dTempKelvin) / (m_dEluentViscosity * Math.pow(dAverageMolarVolume, 0.6));
		DecimalFormat df = new DecimalFormat("0.000E0");
		contentPane.jxpanelGeneralProperties.jlblDiffusionCoefficient.setText(df.format(m_dDiffusionCoefficient));
		
		// Determine the reduced flow velocity
		m_dReducedFlowVelocity = ((m_dParticleSize / 10000) * m_dInterstitialVelocity) / m_dDiffusionCoefficient;
		contentPane.jxpanelChromatographyProperties.jlblReducedVelocity.setText(formatter.format(m_dReducedFlowVelocity));
		
		// Calculate reduced plate height
		m_dReducedPlateHeight = m_dATerm + (m_dBTerm / m_dReducedFlowVelocity) + (m_dCTerm * m_dReducedFlowVelocity);
		contentPane.jxpanelColumnProperties.jlblReducedPlateHeight.setText(formatter.format(m_dReducedPlateHeight));
    	
		// Calculate HETP
		m_dHETP = (m_dParticleSize / 10000) * m_dReducedPlateHeight;
		contentPane.jxpanelChromatographyProperties.jlblHETP.setText(df.format(m_dHETP));
		
		// Calculate number of theoretical plates
		NumberFormat NFormatter = new DecimalFormat("#0");
		m_dTheoreticalPlates = (m_dColumnLength / 10) / m_dHETP;
		contentPane.jxpanelChromatographyProperties.jlblTheoreticalPlates.setText(NFormatter.format(m_dTheoreticalPlates));

		// TODO: Account for changes in eluent viscosity during a gradient
		// Calculate post-column tubing volume (in uL)
		double dTubingVolume = (this.m_dTubingLength / 100) * (Math.PI * Math.pow((this.m_dTubingDiameter * 0.0000254) / 2, 2) * 1000000000);
		this.contentPane.jxpanelExtraColumnTubing.jlblTubingVolume.setText(formatter.format(dTubingVolume));

		// Calculate retention time delay from post-column tubing in seconds
		double dTubingDelay = ((dTubingVolume / 1000) / m_dFlowRate) * 60;
		
		// Get extra-column tubing radius in units of cm
		double dTubingRadius = (this.m_dTubingDiameter * 0.00254) / 2;

		// Open tube velocity in cm/s
		double dTubingOpenTubeVelocity = (m_dFlowRate / 60) / (Math.PI * Math.pow(dTubingRadius, 2));

		// Calculate dispersion that will result from extra-column tubing
		// in cm^2
		double dTubingZBroadening = (2 * m_dDiffusionCoefficient * this.m_dTubingLength / dTubingOpenTubeVelocity) + ((Math.pow(dTubingRadius, 2) * m_dTubingLength * dTubingOpenTubeVelocity) / (24 * m_dDiffusionCoefficient));
		
		// convert to mL^2
		double dTubingVolumeBroadening = Math.pow(Math.sqrt(dTubingZBroadening) * Math.PI * Math.pow(dTubingRadius, 2), 2);
		
		// convert to s^2
		double dTubingTimeBroadening = Math.pow((Math.sqrt(dTubingVolumeBroadening) / m_dFlowRate) * 60, 2);
		
		// Get flow rate in m^3/s
		// 1 m^3 = 1000000 cm^3
		double dFlowRateInUnits = (m_dFlowRate / 1000000) / 60;

		// Calculate backpressure from tubing
		// Gives pressure drop in Pa
		m_dTubingPressureDrop = (8 * (m_dEluentViscosity / 1000) * (m_dTubingLength / 100) * dFlowRateInUnits) / (Math.PI * Math.pow(dTubingRadius / 100, 4));
		
		if (!m_bGradientMode)
			contentPane.jxpanelChromatographyProperties.jlblBackpressure.setText(bpFormatter.format((m_dBackpressure + m_dTubingPressureDrop) / 100000));
		else
			contentPane.jxpanelChromatographyProperties.jlblBackpressure.setText("--");
*/
    	
		int iNumCompounds = m_vectCompound.size();

		this.m_vectSelectedRetentionFactorArray = new Vector<double[]>();
		this.m_vectSelectedPositionArray = new Vector<double[]>();
		int iNumZPoints = 500;
		m_dSelectedSigmaPositionArray = new double[iNumZPoints][2];
		
		double dSumHETP = 0;
		
		for (int iCompound = 0; iCompound < iNumCompounds; iCompound++)
		{
			// First, scout out the approximate retention time of this compound
			double dTcA = 0;
			double dTcB = 0;
			double dt = 60; // in s
			double t = 0; // in s
			double z = 0; // in m
			double lastz = 0;
			boolean bIsEluted = false;
			double tR = 0;
			
			// Grab the first temp
			if (this.m_bTemperatureProgramMode)
				dTcA = this.m_lifTemperatureProgram.getAt(0);

			while (bIsEluted == false)// (double t = 0; t <= (Double) m_vectCompound.get(m_vectCompound.size() - 1)[1] * 1.5; t += dtstep)
			{
				t += dt;
				
				double dTc;
				if (this.m_bTemperatureProgramMode)
				{
					// Grab the second temp
					dTcB = m_lifTemperatureProgram.getAt(t / 60);

					// Find the average of the two temps
					dTc = (dTcA + dTcB) / 2;
				}
				else
					dTc = this.m_dTemperature;
				
				// Get inlet pressure at this time
				double dInletPressure;
				if (m_bConstantFlowRateMode)
					dInletPressure = this.calcInletPressureAtConstFlowRate(m_dFlowRate, dTc + 273.15, m_iCarrierGas, m_dOutletPressure, dInnerDiameter, m_dColumnLength);
				else
					dInletPressure = this.m_dInletPressure;
				
				double dFlowVelocity = calcFlowVelocity(z, dTc + 273.15, m_iCarrierGas, dInletPressure, m_dOutletPressure, dInnerDiameter, m_dColumnLength);						

				// Get k at this temperature
				double k = Math.pow(10, this.m_vectCompound.get(iCompound).InterpolatedLogkvsT.getAt(dTc)) * dBeta1Beta2;

				// Determine how far this compound will move in the time alloted at this flow velocity
				double dz = (dt / (1 + k)) * dFlowVelocity;
				dz = dz / this.m_dColumnLength;
				
				lastz = z;
				z += dz;
				
				if (z >= 1)
				{
					tR = (((1 - lastz)/(z - lastz)) * dt) + t;
					bIsEluted = true;
					break;
				}
				
				dTcA = dTcB;
			}

			// tR gives us a rough idea of when it will come out
			// Now get a more accurate retention time.
			int numSlices = 1000;
			dt = tR / (double)numSlices;
			t = 0;
			z = 0;
			lastz = 0;
			bIsEluted = false;
			
			// Keep track of t as a function of z
			double[][] zvst = new double[numSlices * 2][2];
			int zvstSize = 0;
			
			boolean bRecordRetentionFactor = false;
			boolean bRecordPosition = false;
			
			if (contentPane.jxpanelPlotOptions.jcboRetentionFactorCompounds.getSelectedIndex() == iCompound)
				bRecordRetentionFactor = true;
			
			if (contentPane.jxpanelPlotOptions.jcboPositionCompounds.getSelectedIndex() == iCompound)
				bRecordPosition = true;

			// Holds k vs z
			// Is there any way possible that the array could need to be bigger than numSlices * 2?
			double[][] kvsz = new double[numSlices * 2][2];
			int kvszSize = 0;
			
			// Grab the first temp
			if (this.m_bTemperatureProgramMode)
				dTcA = this.m_lifTemperatureProgram.getAt(0);
			
			while (bIsEluted == false)// (double t = 0; t <= (Double) m_vectCompound.get(m_vectCompound.size() - 1)[1] * 1.5; t += dtstep)
			{
				t += dt;
				
				double dTc;
				if (this.m_bTemperatureProgramMode)
				{
					// Grab the second temp
					dTcB = m_lifTemperatureProgram.getAt(t / 60);

					// Find the average of the two temps
					dTc = (dTcA + dTcB) / 2;
				}
				else
					dTc = this.m_dTemperature;

				// Get inlet pressure at this time
				double dInletPressure;
				if (m_bConstantFlowRateMode)
					dInletPressure = this.calcInletPressureAtConstFlowRate(m_dFlowRate, dTc + 273.15, m_iCarrierGas, m_dOutletPressure, dInnerDiameter, m_dColumnLength);
				else
					dInletPressure = this.m_dInletPressure;
				
				double dFlowVelocity = calcFlowVelocity(z, dTc + 273.15, m_iCarrierGas, dInletPressure, m_dOutletPressure, dInnerDiameter, m_dColumnLength);						

				// Get k at this temperature
				double k = Math.pow(10, this.m_vectCompound.get(iCompound).InterpolatedLogkvsT.getAt(dTc)) * dBeta1Beta2;
				
				// Record k at this position in the column
				kvsz[kvszSize][0] = z;
				kvsz[kvszSize][1] = k;
				kvszSize++;
				
				// Determine how far this compound will move in the time alloted at this flow velocity
				double dz = (dt / (1 + k)) * dFlowVelocity;
				dz = dz / this.m_dColumnLength;
				
				lastz = z;
				z += dz;
				
				if (z >= 1)
				{
					tR = (((1 - lastz)/(z - lastz)) * dt) + t;
					bIsEluted = true;
					break;
				}
				
				if (bRecordRetentionFactor)
				{
					double[] temp = {t, k};
					m_vectSelectedRetentionFactorArray.add(temp);
				}

				zvst[zvstSize][0] = z;
				zvst[zvstSize][1] = t;
				zvstSize++;
				
				if (bRecordPosition)
				{
					double[] temp = {t, z * m_dColumnLength};
					m_vectSelectedPositionArray.add(temp);
				}
				
				dTcA = dTcB;
			}
			
			// Create an interpolated function of kvsz
			LinearInterpolationFunction lifKvsZ = new LinearInterpolationFunction(kvsz);

			// Create an interpolated function of tvsz
			LinearInterpolationFunction liftvsz = new LinearInterpolationFunction(zvst);

			// Now determine peak width
			t = 0;
			double dz = 1.0 / (double)iNumZPoints;
			double k = 1;
	    	double dSigmaSquaredPlateHeight = 0;
	    	double dSigmaSquaredTotal = 0;
			double dFlowVelocity = 0;
			double dLastFlowVelocity = -1;
			int iIteration = 0;

			// Start with the broadening from injection
			// Figure out how step function will be (variance)
			
			double dTemperature;
			if (this.m_bTemperatureProgramMode)
				dTemperature = this.m_lifTemperatureProgram.getAt(0);
			else
				dTemperature = this.m_dTemperature;

			double dInletPressure;
			if (m_bConstantFlowRateMode)
				dInletPressure = this.calcInletPressureAtConstFlowRate(m_dFlowRate, dTemperature + 273.15, m_iCarrierGas, m_dOutletPressure, dInnerDiameter, m_dColumnLength);
			else
				dInletPressure = this.m_dInletPressure;
			
			dFlowVelocity = calcFlowVelocity(0, dTemperature + 273.15, m_iCarrierGas, dInletPressure, m_dOutletPressure, dInnerDiameter, m_dColumnLength);						
			
			k = lifKvsZ.getAt(0);
			
			// Calculate flow rate (in m^3/m)
			double dFlowRate = (Math.PI * Math.pow(dInnerDiameter / 2, 2));
			double dLengthTakenByInletGas = m_dLinerVolume / dFlowRate;
			
			// Remove the split
			if (this.m_bSplitInjectionMode)
				dLengthTakenByInletGas = dLengthTakenByInletGas / (this.m_dSplitRatio + 1);
			
			double dLengthTakenBySample = dLengthTakenByInletGas / (1 + k);
			double dInjectionVarianceZ = (1.0/12.0) * Math.pow(dLengthTakenBySample, 2);
			dSigmaSquaredTotal = dInjectionVarianceZ;
			
			for (z = 0; z < 1; z += dz)
			{
				// Get the current time
				t = liftvsz.getAt(z);
				
				if (this.m_bTemperatureProgramMode)
					dTemperature = this.m_lifTemperatureProgram.getAt(t / 60.0);
				else
					dTemperature = this.m_dTemperature;

				if (m_bConstantFlowRateMode)
					dInletPressure = this.calcInletPressureAtConstFlowRate(m_dFlowRate, dTemperature + 273.15, m_iCarrierGas, m_dOutletPressure, dInnerDiameter, m_dColumnLength);
				else
					dInletPressure = this.m_dInletPressure;
				
				dFlowVelocity = calcFlowVelocity(z, dTemperature + 273.15, m_iCarrierGas, dInletPressure, m_dOutletPressure, dInnerDiameter, m_dColumnLength);						

				// How long does it take for gas to travel dz?
				//double tg = (dz * m_dColumnLength) / dFlowVelocity;
				
				// Calculate k
				//k = Math.pow(10, this.m_vectCompound.get(iCompound).InterpolatedLogkvsT.getAt(dTemperature)) * dBeta1Beta2;

				// How long does it take for the solute to travel to z + dz?
				//dt = tg * (1 + k);
				
				// Add to the total time it's taking to get the compound through the column
				//t += dt;
				
				// Get k from the interpolation function
				k = lifKvsZ.getAt(z);
				
				// Determine how much to add to the peak width using Golay equation
				double dPressure = calcGasPressure(z, dInletPressure, this.m_dOutletPressure);
				double dSoluteDiffusivityInGas = calcSoluteDiffusivityInGas(dTemperature + 273.15, dPressure, this.m_iCarrierGas);
				double dSoluteDiffusivityInStationaryPhase = this.calcSoluteDiffusivityInStationaryPhase(dTemperature + 273.15);
				// In m^2/m
				double dPlateHeight = calcPlateHeight(dSoluteDiffusivityInGas, dSoluteDiffusivityInStationaryPhase, dFlowVelocity, dInnerDiameter, k, this.m_dFilmThickness);
				dSigmaSquaredPlateHeight += dPlateHeight * (dz * this.m_dColumnLength);
				
				// Add to total sigma^2
				dSigmaSquaredTotal += dPlateHeight * (dz * this.m_dColumnLength);;
				
				// Now spread out the band from decompression (Blumberg pg. 243)
				double dSigmaSquaredFromDecompression = 0;
				if (dLastFlowVelocity > 0)
					dSigmaSquaredFromDecompression = ((2 * dSigmaSquaredTotal) / dLastFlowVelocity) * (dFlowVelocity - dLastFlowVelocity);
				dSigmaSquaredTotal += dSigmaSquaredFromDecompression;

				if (bRecordPosition)
				{
					m_dSelectedSigmaPositionArray[iIteration][0] = t;
					m_dSelectedSigmaPositionArray[iIteration][1] = Math.sqrt(dSigmaSquaredTotal);
				}

				dLastFlowVelocity = dFlowVelocity;
				
				iIteration++;
			}

			if (this.m_bTemperatureProgramMode)
				contentPane.vectChemicalRows.get(iCompound).set(2, "--");
			else
			{
		    	contentPane.vectChemicalRows.get(iCompound).set(2, formatter.format(k));
		    	if (contentPane.jxpanelPlotOptions.jcboRetentionFactorCompounds.getSelectedIndex() == iCompound)
		    	{
		    		m_dSelectedIsocraticRetentionFactor = k;	
		    	}
			}
			
	    	m_vectCompound.get(iCompound).dRetentionTime = tR;
	    	contentPane.vectChemicalRows.get(iCompound).set(3, formatter.format(tR / 60));
	    	
	    	// Use the final value of k to determine the peak width.
			double dSigmaL = Math.sqrt(dSigmaSquaredTotal);
	    	double dSigmaT = dSigmaL / (dFlowVelocity / (1 + k));
	    	double dHETP = dSigmaSquaredPlateHeight / this.m_dColumnLength;
	    	dSumHETP += dHETP;
	    	
	    	// Add in broadening from the time constant
	    	dSigmaT = Math.sqrt(Math.pow(dSigmaT, 2) + Math.pow(m_dTimeConstant, 2));//Math.sqrt(Math.pow((dt0 * (1 + dk)) / Math.sqrt(m_dTheoreticalPlates), 2) + Math.pow(m_dTimeConstant, 2));
	    	m_vectCompound.get(iCompound).dSigma = dSigmaT;	    	
	    	contentPane.vectChemicalRows.get(iCompound).set(4, formatter.format(dSigmaT));
	    	
	    	double dW = (m_dInjectionVolume / 1000000) * m_vectCompound.get(iCompound).dConcentration;
	    	if (this.m_bSplitInjectionMode)
	    		dW /= (this.m_dSplitRatio + 1);
	    	m_vectCompound.get(iCompound).dW = dW;
	    	contentPane.vectChemicalRows.get(iCompound).set(5, formatter.format(dW * 1000000));		    	
		}

		double dHETP = (dSumHETP / (double)iNumCompounds);
		contentPane.jxpanelColumnProperties.jlblHETPIndicator.setText(formatter.format(dHETP * 1000));
		contentPane.jxpanelColumnProperties.jlblTheoreticalPlatesIndicator.setText(Integer.toString((int)(this.m_dColumnLength / dHETP)));
    	
		// Now calculate the time period we're going to be looking at:
    	if (m_bAutomaticTimeRange == true)
    	{
	    	// Find the compound with the longest tR
	    	double dLongestRetentionTime = 0;
	    	
	    	for (int i = 0; i < m_vectCompound.size(); i++)
	    	{
	    		if (m_vectCompound.get(i).dRetentionTime > dLongestRetentionTime)
	    		{
	    			dLongestRetentionTime = m_vectCompound.get(i).dRetentionTime;
	    		}
	    	}
	    	
	    	m_dStartTime = 0;
	    	m_dEndTime = dLongestRetentionTime * 1.1;
	    	
	    	contentPane.jxpanelPlotOptions.jtxtFinalTime.setText(Float.toString((float)m_dEndTime));
	    	contentPane.jxpanelPlotOptions.jtxtInitialTime.setText("0");
	    }

    	// Clear the old chromatogram
    	contentPane.m_GraphControl.RemoveAllSeries();

    	// Draw the second plot
		if (m_iSecondPlotType == 1)
			plotTemperatureProgram();
		else if (m_iSecondPlotType == 2)
			plotHoldUpTime();
		else if (m_iSecondPlotType == 3)
			plotViscosity();
		else if (m_iSecondPlotType == 4)
			plotFlowVelocity();
		else if (m_iSecondPlotType == 5)
			plotGasDensity();
		else if (m_iSecondPlotType == 6)
			plotGasPressure();
		else if (m_iSecondPlotType == 7)
			plotRetentionFactor();
		else if (m_iSecondPlotType == 8)
			plotPosition();

    	// Calculate each data point
    	Random random = new Random();
    	
    	//contentPane.m_GraphControl.RemoveSeries(m_iChromatogramPlotIndex);
    	m_iChromatogramPlotIndex = -1;
    	
    	// Clear the single plot if it exists (the red plot that shows up if you click on a compound)
    	//contentPane.m_GraphControl.RemoveSeries(m_iSinglePlotIndex);
    	m_iSinglePlotIndex = -1;
    	
		Color clrOrange = new Color(240,90,40);
		Color clrOrange2 = new Color(132,48,2);
		Color clrBlack = new Color(0,0,0);
		Color clrBlue = new Color(98, 101, 214);
		Color clrRed = new Color(206, 70, 70);
    	if (this.m_vectCompound.size() > 0)
    	{
    		m_iChromatogramPlotIndex = contentPane.m_GraphControl.AddSeries("Chromatogram", clrBlack, 1, false, false);
	    	// Find if a chemical is selected
	    	int iRowSel = contentPane.jtableChemicals.getSelectedRow();
	    	if (iRowSel >= 0 && iRowSel < contentPane.vectChemicalRows.size())
	    	{
	    		m_iSinglePlotIndex = contentPane.m_GraphControl.AddSeries("Single", clrOrange, 1, false, false);	    		
	    	}
	    	
	    	for (int i = 0; i < this.m_iNumPoints; i++)
	    	{
	    		double dTime = m_dStartTime + (double)i * ((m_dEndTime - m_dStartTime) / (double)this.m_iNumPoints);
	    		double dNoise = random.nextGaussian() * (m_dNoise / 1000000000);
	    		double dCTotal = (dNoise / Math.sqrt(m_dTimeConstant)) + m_dSignalOffset;
	    		
	    		// Add the contribution from each compound to the peak
	    		for (int j = 0; j < m_vectCompound.size(); j++)
	    		{
	    			Compound curCompound = m_vectCompound.get(j);
	    			//double dCthis = ((curCompound.dW / 1000000) / (curCompound.dSigma * (m_dFlowRate / (60 * 1000)))) * Math.exp(-0.5*Math.pow((dTime - curCompound.dRetentionTime) / (curCompound.dSigma), 2));
	    			double dCthis = ((curCompound.dW / 1000000) / (Math.sqrt(2 * Math.PI) * curCompound.dSigma * (m_dFlowRate / (60 * 1000)))) * Math.exp(-Math.pow(dTime - curCompound.dRetentionTime, 2) / (2 * Math.pow(curCompound.dSigma, 2)));
	    			dCTotal += dCthis;
	    			
	    			// If a compound is selected, then show it in a different color and without noise.
	    			if (m_iSinglePlotIndex >= 0 && j == iRowSel)
	    		    	contentPane.m_GraphControl.AddDataPoint(m_iSinglePlotIndex, dTime, (dCthis + m_dSignalOffset));
	    		}
	    		
		    	contentPane.m_GraphControl.AddDataPoint(m_iChromatogramPlotIndex, dTime, dCTotal);
	    	}
	    	
	    	if (contentPane.jbtnAutoscaleX.isSelected() == true)
	    		contentPane.m_GraphControl.AutoScaleX();	//AutoScaleToSeries(iTotalPlotIndex);
	    	if (contentPane.jbtnAutoscaleY.isSelected() == true)
	    		contentPane.m_GraphControl.AutoScaleY();
    	}
    	
    	contentPane.m_GraphControl.repaint();   	
    	contentPane.jtableChemicals.addNotify();
	}

	//@Override
	public void focusGained(FocusEvent e) 
	{
		
	}

	//@Override
	public void focusLost(FocusEvent e) 
	{
		performCalculations();
	}

	//@Override
	public void valueChanged(ListSelectionEvent arg0) 
	{
		performCalculations();
	}

	//@Override
	public void autoScaleChanged(AutoScaleEvent event) 
	{
		if (event.getAutoScaleXState() == true)
			contentPane.jbtnAutoscaleX.setSelected(true);
		else
			contentPane.jbtnAutoscaleX.setSelected(false);
		
		if (event.getAutoScaleYState() == true)
			contentPane.jbtnAutoscaleY.setSelected(true);
		else
			contentPane.jbtnAutoscaleY.setSelected(false);
		
		if (event.getAutoScaleXState() == true && event.getAutoScaleYState() == true)
			contentPane.jbtnAutoscale.setSelected(true);			
		else
			contentPane.jbtnAutoscale.setSelected(false);						
	}

	@Override
	public void tableChanged(TableModelEvent e) 
	{

		/*if (e.getSource() == contentPane.jxpanelTemperatureProgramOptions.tmTemperatureProgram)
		{
			if (contentPane.jxpanelTemperatureProgramOptions.m_bDoNotChangeTable)
			{
				contentPane.jxpanelTemperatureProgramOptions.m_bDoNotChangeTable = false;
				return;
			}

			performCalculations();
		}		*/
	}
	
	public void calculateTemperatureProgram()
	{
    	this.m_dTemperatureProgramArray = new double[(contentPane.jxpanelTemperatureProgramOptions.tmTemperatureProgram.getRowCount() * 2) + 3][2];
    	int iPointCount = 0;

    	this.m_dTemperatureProgramArray[iPointCount][0] = 0.0;
    	this.m_dTemperatureProgramArray[iPointCount][1] = m_dInitialTemperature;
		iPointCount++;
		
		if (m_dInitialTime > 0)
		{
	    	this.m_dTemperatureProgramArray[iPointCount][0] = m_dInitialTime;
	    	this.m_dTemperatureProgramArray[iPointCount][1] = m_dInitialTemperature;
			iPointCount++;
		}

		double dTotalTime = m_dInitialTime;
    	double dLastTemp = m_dInitialTemperature;
    	double dFinalTemp = m_dInitialTemperature;
    	
    	// Go through the temperature program table and create an array that contains temp vs. time
		for (int i = 0; i < contentPane.jxpanelTemperatureProgramOptions.tmTemperatureProgram.getRowCount(); i++)
		{
			double dRamp = (Double)contentPane.jxpanelTemperatureProgramOptions.tmTemperatureProgram.getValueAt(i, 0);
			dFinalTemp = (Double)contentPane.jxpanelTemperatureProgramOptions.tmTemperatureProgram.getValueAt(i, 1);
			double dFinalTime = (Double)contentPane.jxpanelTemperatureProgramOptions.tmTemperatureProgram.getValueAt(i, 2);

			if (dRamp != 0)
			{
				dTotalTime += (dFinalTemp - dLastTemp) / dRamp;
				this.m_dTemperatureProgramArray[iPointCount][0] = dTotalTime;
				this.m_dTemperatureProgramArray[iPointCount][1] = dFinalTemp;
				iPointCount++;
			}
			
			if (dFinalTime != 0)
			{
				if (i < contentPane.jxpanelTemperatureProgramOptions.tmTemperatureProgram.getRowCount() - 1)
				{
					dTotalTime += dFinalTime;
					this.m_dTemperatureProgramArray[iPointCount][0] = dTotalTime;
					this.m_dTemperatureProgramArray[iPointCount][1] = dFinalTemp;
					iPointCount++;						
				}
			}
			
			dLastTemp = dFinalTemp;
		}
		
		// Now add a data point way out there for the last time point
		this.m_dTemperatureProgramArray[iPointCount][0] = dTotalTime + 1;
		this.m_dTemperatureProgramArray[iPointCount][1] = this.m_dTemperatureProgramArray[iPointCount - 1][1];
		iPointCount++;

		// Ideal series finished
		// Now cut it down to the correct size
		double tempArray[][] = new double[iPointCount][2];
		for (int i = 0; i < iPointCount; i++)
		{
			tempArray[i][0] = this.m_dTemperatureProgramArray[i][0];
			tempArray[i][1] = this.m_dTemperatureProgramArray[i][1];
		}
		this.m_dTemperatureProgramArray = tempArray;
		
		// Make the interpolated temperature profile
    	this.m_lifTemperatureProgram = new LinearInterpolationFunction(this.m_dTemperatureProgramArray);
	}
	
	public void plotTemperatureProgram()
	{
		contentPane.m_GraphControl.RemoveSeries(m_iSecondPlotIndex);
		m_iSecondPlotIndex = -1;
		
		m_iSecondPlotIndex = contentPane.m_GraphControl.AddSeries("SecondPlot", new Color(130, 130, 130), 1, false, true);	    		
    	
		double dTemperatureMin = 999999999;
		double dTemperatureMax = 0;
		
		if (this.m_bTemperatureProgramMode)
		{
	    	for (int i = 0; i < m_dTemperatureProgramArray.length; i++)
	    	{
	    		double dTemperature = m_dTemperatureProgramArray[i][1];
	    		
				if (dTemperature < dTemperatureMin)
					dTemperatureMin = dTemperature;
				if (dTemperature > dTemperatureMax)
					dTemperatureMax = dTemperature;
				
		    	contentPane.m_GraphControl.AddDataPoint(m_iSecondPlotIndex, m_dTemperatureProgramArray[i][0] * 60, m_dTemperatureProgramArray[i][1]);
	    	}
	    	
	    	contentPane.m_GraphControl.AddDataPoint(m_iSecondPlotIndex, 9999999999d, m_dTemperatureProgramArray[m_dTemperatureProgramArray.length - 1][1]);
		
	    	if (dTemperatureMin == dTemperatureMax)
			{
	    		dTemperatureMin = dTemperatureMin * 0.8;
	    		dTemperatureMax = dTemperatureMax * 1.2;
			}
		}
		else
		{
			// Isocratic mode
	    	contentPane.m_GraphControl.AddDataPoint(m_iSecondPlotIndex, 0, this.m_dTemperature);			
	    	contentPane.m_GraphControl.AddDataPoint(m_iSecondPlotIndex, 9999999999d, this.m_dTemperature);			
	    	dTemperatureMin = 60;
	    	dTemperatureMax = 320;
		}
		
		contentPane.m_GraphControl.setSecondYAxisRangeLimits(dTemperatureMin, dTemperatureMax);
	}
	
	public void plotHoldUpTime()
	{
		contentPane.m_GraphControl.RemoveSeries(m_iSecondPlotIndex);
		m_iSecondPlotIndex = -1;
		
		m_iSecondPlotIndex = contentPane.m_GraphControl.AddSeries("SecondPlot", new Color(130, 130, 130), 1, false, true);	    		
    	
		double dHoldUpTimeMin = 999999999;
		double dHoldUpTimeMax = 0;
		
		// Subtract film thickness out of the inner diameter (in m)
		double dInnerDiameter = (this.m_dColumnDiameter) - (2 * this.m_dFilmThickness);

		if (this.m_bTemperatureProgramMode)
		{
			double dFinalTime = m_dTemperatureProgramArray[m_dTemperatureProgramArray.length - 1][0] * 60;
			double dt = dFinalTime / 100d;
			
			double dHoldUpTime = 0;
			
	    	for (double t = 0; t <= dFinalTime; t += dt)
	    	{
	    		double dTemperature = this.m_lifTemperatureProgram.getAt(t / 60);
	    		
				double dInletPressure;
				if (m_bConstantFlowRateMode)
					dInletPressure = this.calcInletPressureAtConstFlowRate(m_dFlowRate, dTemperature + 273.15, m_iCarrierGas, m_dOutletPressure, dInnerDiameter, m_dColumnLength);
				else
					dInletPressure = this.m_dInletPressure;
	    		
				dHoldUpTime = this.calcHoldUpTime(dTemperature + 273.15, m_iCarrierGas, dInletPressure, m_dOutletPressure, m_dColumnLength, dInnerDiameter);
				
				if (dHoldUpTime < dHoldUpTimeMin)
					dHoldUpTimeMin = dHoldUpTime;
				if (dHoldUpTime > dHoldUpTimeMax)
					dHoldUpTimeMax = dHoldUpTime;
				
		    	contentPane.m_GraphControl.AddDataPoint(m_iSecondPlotIndex, t, dHoldUpTime);
	    	}
	    	
	    	contentPane.m_GraphControl.AddDataPoint(m_iSecondPlotIndex, 9999999999d, dHoldUpTime);
		
	    	if (dHoldUpTimeMin == dHoldUpTimeMax)
			{
	    		dHoldUpTimeMin = dHoldUpTimeMin * 0.8;
	    		dHoldUpTimeMax = dHoldUpTimeMax * 1.2;
			}
		}
		else
		{
			// Isothermal mode
			double dInletPressure;
			if (m_bConstantFlowRateMode)
				dInletPressure = this.calcInletPressureAtConstFlowRate(m_dFlowRate, this.m_dTemperature + 273.15, m_iCarrierGas, m_dOutletPressure, dInnerDiameter, m_dColumnLength);
			else
				dInletPressure = this.m_dInletPressure;
    		
			double dHoldUpTime = this.calcHoldUpTime(this.m_dTemperature + 273.15, m_iCarrierGas, dInletPressure, m_dOutletPressure, m_dColumnLength, dInnerDiameter);

	    	contentPane.m_GraphControl.AddDataPoint(m_iSecondPlotIndex, 0, dHoldUpTime);			
	    	contentPane.m_GraphControl.AddDataPoint(m_iSecondPlotIndex, 9999999999d, dHoldUpTime);			
	    	dHoldUpTimeMin = dHoldUpTime * 0.8;
	    	dHoldUpTimeMax = dHoldUpTime * 1.2;
		}
		
		contentPane.m_GraphControl.setSecondYAxisRangeLimits(dHoldUpTimeMin, dHoldUpTimeMax);
	}
	
	public void plotViscosity()
	{
		contentPane.m_GraphControl.RemoveSeries(m_iSecondPlotIndex);
		m_iSecondPlotIndex = -1;
		
		m_iSecondPlotIndex = contentPane.m_GraphControl.AddSeries("SecondPlot", new Color(130, 130, 130), 1, false, true);	    		
    	
		double dViscosityMin = 999999999;
		double dViscosityMax = 0;
		
		if (this.m_bTemperatureProgramMode)
		{
			double dFinalTime = m_dTemperatureProgramArray[m_dTemperatureProgramArray.length - 1][0] * 60;
			double dt = dFinalTime / 100;
			
			double dViscosity = 0;
			
	    	for (double t = 0; t <= dFinalTime; t += dt)
	    	{
	    		double dTemperature = this.m_lifTemperatureProgram.getAt(t / 60.0);
	    		
	    		dViscosity = this.calcGasViscosity(dTemperature + 273.15, this.m_iCarrierGas);
				
				if (dViscosity < dViscosityMin)
					dViscosityMin = dViscosity;
				if (dViscosity > dViscosityMax)
					dViscosityMax = dViscosity;
				
		    	contentPane.m_GraphControl.AddDataPoint(m_iSecondPlotIndex, t, dViscosity);
	    	}
	    	
	    	contentPane.m_GraphControl.AddDataPoint(m_iSecondPlotIndex, 9999999999d, dViscosity);
		
	    	if (dViscosityMin == dViscosityMax)
			{
	    		dViscosityMin = dViscosityMin * 0.8;
	    		dViscosityMax = dViscosityMax * 1.2;
			}
		}
		else
		{
			// Isothermal mode
    		double dViscosity = this.calcGasViscosity(this.m_dTemperature + 273.15, this.m_iCarrierGas);

	    	contentPane.m_GraphControl.AddDataPoint(m_iSecondPlotIndex, 0, dViscosity);			
	    	contentPane.m_GraphControl.AddDataPoint(m_iSecondPlotIndex, 9999999999d, dViscosity);			
	    	dViscosityMin = dViscosity * 0.8;
	    	dViscosityMax = dViscosity * 1.2;
		}
		
		contentPane.m_GraphControl.setSecondYAxisRangeLimits(dViscosityMin, dViscosityMax);
	}
	
	public void plotFlowVelocity()
	{
		contentPane.m_GraphControl.RemoveSeries(m_iSecondPlotIndex);
		m_iSecondPlotIndex = -1;
		
		m_iSecondPlotIndex = contentPane.m_GraphControl.AddSeries("SecondPlot", new Color(130, 130, 130), 1, false, true);	    		
    	
		double dFlowVelocityMin = 999999999;
		double dFlowVelocityMax = 0;
		
		double dPlotFlowVelocityPosition = m_dPlotFlowVelocityPosition;
		if (this.m_dPlotFlowVelocityPosition == 1.0)
			dPlotFlowVelocityPosition = 0.9999999;

		// Subtract film thickness out of the inner diameter (in m)
		double dInnerDiameter = (this.m_dColumnDiameter) - (2 * this.m_dFilmThickness);

		if (this.m_bTemperatureProgramMode)
		{
			double dFinalTime = m_dTemperatureProgramArray[m_dTemperatureProgramArray.length - 1][0] * 60;
			double dt = dFinalTime / 100;
			
			double dFlowVelocity = 0;
			
	    	for (double t = 0; t <= dFinalTime; t += dt)
	    	{
	    		double dTemperature = this.m_lifTemperatureProgram.getAt(t / 60.0);
	    		
				double dInletPressure;
				if (m_bConstantFlowRateMode)
					dInletPressure = this.calcInletPressureAtConstFlowRate(m_dFlowRate, dTemperature + 273.15, m_iCarrierGas, m_dOutletPressure, dInnerDiameter, m_dColumnLength);
				else
					dInletPressure = this.m_dInletPressure;
	    		
				dFlowVelocity = this.calcFlowVelocity(dPlotFlowVelocityPosition, dTemperature + 273.15, m_iCarrierGas, dInletPressure, m_dOutletPressure, dInnerDiameter, m_dColumnLength);
				
				if (dFlowVelocity < dFlowVelocityMin)
					dFlowVelocityMin = dFlowVelocity;
				if (dFlowVelocity > dFlowVelocityMax)
					dFlowVelocityMax = dFlowVelocity;
				
		    	contentPane.m_GraphControl.AddDataPoint(m_iSecondPlotIndex, t, dFlowVelocity);
	    	}
	    	
	    	contentPane.m_GraphControl.AddDataPoint(m_iSecondPlotIndex, 9999999999d, dFlowVelocity);
			
	    	if (dFlowVelocityMin == dFlowVelocityMax)
			{
	    		dFlowVelocityMin = dFlowVelocityMin * 0.8;
	    		dFlowVelocityMax = dFlowVelocityMax * 1.2;
			}
		}
		else
		{
			// Isothermal mode
			double dInletPressure;
			if (m_bConstantFlowRateMode)
				dInletPressure = this.calcInletPressureAtConstFlowRate(m_dFlowRate, this.m_dTemperature + 273.15, m_iCarrierGas, m_dOutletPressure, dInnerDiameter, m_dColumnLength);
			else
				dInletPressure = this.m_dInletPressure;
    		
			double dFlowVelocity = this.calcFlowVelocity(dPlotFlowVelocityPosition, m_dTemperature + 273.15, m_iCarrierGas, dInletPressure, m_dOutletPressure, dInnerDiameter, m_dColumnLength);

	    	contentPane.m_GraphControl.AddDataPoint(m_iSecondPlotIndex, 0, dFlowVelocity);			
	    	contentPane.m_GraphControl.AddDataPoint(m_iSecondPlotIndex, 9999999999d, dFlowVelocity);			
	    	dFlowVelocityMin = dFlowVelocity * 0.8;
	    	dFlowVelocityMax = dFlowVelocity * 1.2;
		}
		
		contentPane.m_GraphControl.setSecondYAxisRangeLimits(dFlowVelocityMin, dFlowVelocityMax);
	}
	
	public void plotGasDensity()
	{
		contentPane.m_GraphControl.RemoveSeries(m_iSecondPlotIndex);
		m_iSecondPlotIndex = -1;
		
		m_iSecondPlotIndex = contentPane.m_GraphControl.AddSeries("SecondPlot", new Color(130, 130, 130), 1, false, true);	    		
    	
		double dDensityMin = 999999999;
		double dDensityMax = 0;
		
		// Subtract film thickness out of the inner diameter (in m)
		double dInnerDiameter = (this.m_dColumnDiameter) - (2 * this.m_dFilmThickness);

		if (this.m_bTemperatureProgramMode)
		{
			double dFinalTime = m_dTemperatureProgramArray[m_dTemperatureProgramArray.length - 1][0] * 60;
			double dt = dFinalTime / 100;
			
			double dDensity = 0;
			
	    	for (double t = 0; t <= dFinalTime; t += dt)
	    	{
	    		double dTemperature = this.m_lifTemperatureProgram.getAt(t / 60.0);
	    		
				double dInletPressure;
				if (m_bConstantFlowRateMode)
					dInletPressure = this.calcInletPressureAtConstFlowRate(m_dFlowRate, dTemperature + 273.15, m_iCarrierGas, m_dOutletPressure, dInnerDiameter, m_dColumnLength);
				else
					dInletPressure = this.m_dInletPressure;
	    		
				double dPressure;
				if (this.m_dPlotGasDensityPosition == 1.0)
					dPressure = this.m_dOutletPressure;
				else
					dPressure = this.calcGasPressure(this.m_dPlotGasDensityPosition, dInletPressure, this.m_dOutletPressure);
				
				// n/V = P/RT
				double dMolarity = dPressure / (8.3144621 * (dTemperature + 273.15));
				double dMg = 2.016;
				if (this.m_iCarrierGas == 0)
				{
					// Hydrogen
					dMg = 2.016;
				}
				else if (this.m_iCarrierGas == 1)
				{
					// Helium
					dMg = 4.003;
				}
				else if (this.m_iCarrierGas == 2)
				{
					// Nitrogen
					dMg = 28.01;
				}
				else if (this.m_iCarrierGas == 3)
				{
					// Argon
					dMg = 39.95;
				}
				
				dDensity = dMolarity * dMg; // in g/L

				if (dDensity < dDensityMin)
					dDensityMin = dDensity;
				if (dDensity > dDensityMax)
					dDensityMax = dDensity;
				
		    	contentPane.m_GraphControl.AddDataPoint(m_iSecondPlotIndex, t, dDensity);
	    	}
	    	
	    	contentPane.m_GraphControl.AddDataPoint(m_iSecondPlotIndex, 9999999999d, dDensity);
		
			if (dDensityMin == dDensityMax)
			{
				dDensityMin = dDensityMin * 0.8;
				dDensityMax = dDensityMax * 1.2;
			}
		}
		else
		{
			// Isothermal mode
			double dInletPressure;
			if (m_bConstantFlowRateMode)
				dInletPressure = this.calcInletPressureAtConstFlowRate(m_dFlowRate, m_dTemperature + 273.15, m_iCarrierGas, m_dOutletPressure, dInnerDiameter, m_dColumnLength);
			else
				dInletPressure = this.m_dInletPressure;
    		
			double dPressure;
			if (this.m_dPlotGasDensityPosition == 1.0)
				dPressure = this.m_dOutletPressure;
			else
				dPressure = this.calcGasPressure(this.m_dPlotGasDensityPosition, dInletPressure, this.m_dOutletPressure);
			
			// n/V = P/RT
			double dMolarity = dPressure / (8.3144621 * (m_dTemperature + 273.15));
			double dMg = 2.016;
			if (this.m_iCarrierGas == 0)
			{
				// Hydrogen
				dMg = 2.016;
			}
			else if (this.m_iCarrierGas == 1)
			{
				// Helium
				dMg = 4.003;
			}
			else if (this.m_iCarrierGas == 2)
			{
				// Nitrogen
				dMg = 28.01;
			}
			else if (this.m_iCarrierGas == 3)
			{
				// Argon
				dMg = 39.95;
			}
			
			double dDensity = dMolarity * dMg; // in g/L
	    	contentPane.m_GraphControl.AddDataPoint(m_iSecondPlotIndex, 0, dDensity);			
	    	contentPane.m_GraphControl.AddDataPoint(m_iSecondPlotIndex, 9999999999d, dDensity);			
	    	dDensityMin = dDensity * 0.8;
	    	dDensityMax = dDensity * 1.2;
		}
		
		contentPane.m_GraphControl.setSecondYAxisRangeLimits(dDensityMin, dDensityMax);
	}
	
	public void plotGasPressure()
	{
		contentPane.m_GraphControl.RemoveSeries(m_iSecondPlotIndex);
		m_iSecondPlotIndex = -1;
		
		m_iSecondPlotIndex = contentPane.m_GraphControl.AddSeries("SecondPlot", new Color(130, 130, 130), 1, false, true);	    		
    	
		double dPressureMin = 999999999;
		double dPressureMax = 0;

		if (m_dPlotPressurePosition == 1.0)
		{
	    	contentPane.m_GraphControl.AddDataPoint(m_iSecondPlotIndex, 0, m_dOutletPressure);			
	    	contentPane.m_GraphControl.AddDataPoint(m_iSecondPlotIndex, 9999999999d, m_dOutletPressure);
	    	dPressureMin = m_dOutletPressure * 0.8;
	    	dPressureMax = m_dOutletPressure * 1.2;
		}
		else
		{
			// Subtract film thickness out of the inner diameter (in m)
			double dInnerDiameter = (this.m_dColumnDiameter) - (2 * this.m_dFilmThickness);
	
			if (this.m_bTemperatureProgramMode)
			{
				double dFinalTime = m_dTemperatureProgramArray[m_dTemperatureProgramArray.length - 1][0] * 60;
				double dt = dFinalTime / 100;
				
				double dPressure = 0;
				
		    	for (double t = 0; t <= dFinalTime; t += dt)
		    	{
		    		double dTemperature = this.m_lifTemperatureProgram.getAt(t / 60.0);
		    		
					double dInletPressure;
					if (m_bConstantFlowRateMode)
						dInletPressure = this.calcInletPressureAtConstFlowRate(m_dFlowRate, dTemperature + 273.15, m_iCarrierGas, m_dOutletPressure, dInnerDiameter, m_dColumnLength);
					else
						dInletPressure = this.m_dInletPressure;
		    		
					dPressure = this.calcGasPressure(this.m_dPlotPressurePosition, dInletPressure, this.m_dOutletPressure);
	
					if (dPressure < dPressureMin)
						dPressureMin = dPressure;
					if (dPressure > dPressureMax)
						dPressureMax = dPressure;
					
			    	contentPane.m_GraphControl.AddDataPoint(m_iSecondPlotIndex, t, dPressure);
		    	}
		    	
		    	contentPane.m_GraphControl.AddDataPoint(m_iSecondPlotIndex, 9999999999d, dPressure);
		    	
				if (dPressureMin == dPressureMax)
				{
					dPressureMin = dPressureMin * 0.8;
					dPressureMax = dPressureMax * 1.2;
				}
			}
			else
			{
				// Isothermal mode
				double dInletPressure;
				if (m_bConstantFlowRateMode)
					dInletPressure = this.calcInletPressureAtConstFlowRate(m_dFlowRate, m_dTemperature + 273.15, m_iCarrierGas, m_dOutletPressure, dInnerDiameter, m_dColumnLength);
				else
					dInletPressure = this.m_dInletPressure;
	    		
				double dPressure = this.calcGasPressure(this.m_dPlotPressurePosition, dInletPressure, this.m_dOutletPressure);
	
		    	contentPane.m_GraphControl.AddDataPoint(m_iSecondPlotIndex, 0, dPressure);			
		    	contentPane.m_GraphControl.AddDataPoint(m_iSecondPlotIndex, 9999999999d, dPressure);			
		    	dPressureMin = dPressure * 0.8;
		    	dPressureMax = dPressure * 1.2;
			}
		}

		contentPane.m_GraphControl.setSecondYAxisRangeLimits(dPressureMin, dPressureMax);
	}
	
	public void plotRetentionFactor()
	{
		contentPane.m_GraphControl.RemoveSeries(m_iSecondPlotIndex);
		m_iSecondPlotIndex = -1;
		
		m_iSecondPlotIndex = contentPane.m_GraphControl.AddSeries("SecondPlot", new Color(130, 130, 130), 1, false, true);	    		
		
		double dRetentionFactorMin = 999999999;
		double dRetentionFactorMax = 0;
   	
		if (this.m_bTemperatureProgramMode)
		{
	    	for (int i = 0; i < this.m_vectSelectedRetentionFactorArray.size(); i++)
	    	{
	    		double dRetentionFactor = m_vectSelectedRetentionFactorArray.get(i)[1];
	    		
				if (dRetentionFactor < dRetentionFactorMin)
					dRetentionFactorMin = dRetentionFactor;
				if (dRetentionFactor > dRetentionFactorMax)
					dRetentionFactorMax = dRetentionFactor;
	
		    	contentPane.m_GraphControl.AddDataPoint(m_iSecondPlotIndex, m_vectSelectedRetentionFactorArray.get(i)[0], dRetentionFactor);
	    	}
	    	
			if (dRetentionFactorMin == dRetentionFactorMax)
			{
				dRetentionFactorMin = dRetentionFactorMin * 0.8;
				dRetentionFactorMax = dRetentionFactorMax * 1.2;
			}
		}
		else
		{
			// Isocratic Mode
	    	contentPane.m_GraphControl.AddDataPoint(m_iSecondPlotIndex, m_dStartTime, m_dSelectedIsocraticRetentionFactor);
	    	contentPane.m_GraphControl.AddDataPoint(m_iSecondPlotIndex, m_dEndTime, m_dSelectedIsocraticRetentionFactor);
			dRetentionFactorMin = m_dSelectedIsocraticRetentionFactor * 0.8;
			dRetentionFactorMax = m_dSelectedIsocraticRetentionFactor * 1.2;
		}

		contentPane.m_GraphControl.setSecondYAxisRangeLimits(dRetentionFactorMin, dRetentionFactorMax);
	}
	
	public void plotPosition()
	{
		contentPane.m_GraphControl.RemoveSeries(m_iSecondPlotIndex);
		m_iSecondPlotIndex = -1;
		
		m_iSecondPlotIndex = contentPane.m_GraphControl.AddSeries("SecondPlot", new Color(130, 130, 130), 1, false, true);	    		
		//m_iSigma1PlotIndex = contentPane.m_GraphControl.AddSeries("Sigma1Plot", new Color(200, 200, 200), 1, false, true);	    		
		//m_iSigma2PlotIndex = contentPane.m_GraphControl.AddSeries("Sigma2Plot", new Color(200, 200, 200), 1, false, true);	    		

		// Create linear interpolation of m_vectSelectedPositionArray
		LinearInterpolationFunction lifSigmaPosition = new LinearInterpolationFunction(m_dSelectedSigmaPositionArray);
		
		if (this.m_bTemperatureProgramMode)
		{
	    	for (int i = 0; i < m_vectSelectedPositionArray.size(); i++)
	    	{
	    		double z = m_vectSelectedPositionArray.get(i)[1];
	    		double t = m_vectSelectedPositionArray.get(i)[0]; // in seconds
	    		
	    		contentPane.m_GraphControl.AddDataPoint(m_iSecondPlotIndex, t, z);
	    	
	    		//contentPane.m_GraphControl.AddDataPoint(m_iSigma1PlotIndex, t, z + lifSigmaPosition.getAt(t));
	    		//contentPane.m_GraphControl.AddDataPoint(m_iSigma2PlotIndex, t, z - lifSigmaPosition.getAt(t));
	    	}
		}
		else
		{
			// Isocratic Mode
			int iSelectedCompound = contentPane.jxpanelPlotOptions.jcboPositionCompounds.getSelectedIndex();
			if (iSelectedCompound < m_vectCompound.size() && iSelectedCompound >= 0)
			{
				double dRetentionTime = m_vectCompound.get(iSelectedCompound).dRetentionTime;
			
				contentPane.m_GraphControl.AddDataPoint(m_iSecondPlotIndex, m_dStartTime, m_dStartTime * (m_dColumnLength / dRetentionTime));
				contentPane.m_GraphControl.AddDataPoint(m_iSecondPlotIndex, m_dEndTime, m_dEndTime * (m_dColumnLength / dRetentionTime));
			}
		}
    	
		contentPane.m_GraphControl.setSecondYAxisRangeLimits(0, m_dColumnLength);
	}
	
	public void updateCompoundComboBoxes()
	{
		int iNumCompounds = m_vectCompound.size();
		
		contentPane.jxpanelPlotOptions.jcboRetentionFactorCompounds.removeAllItems();
		contentPane.jxpanelPlotOptions.jcboPositionCompounds.removeAllItems();
		
		for (int i = 0; i < iNumCompounds; i++)
		{
			contentPane.jxpanelPlotOptions.jcboRetentionFactorCompounds.addItem(m_vectCompound.get(i).strCompoundName);
			contentPane.jxpanelPlotOptions.jcboPositionCompounds.addItem(m_vectCompound.get(i).strCompoundName);
		}
	}
	
    public void vacuumOutletPressure()
    {
    	contentPane.jxpanelInletOutlet.jrdoVacuum.setSelected(true);
    	contentPane.jxpanelInletOutlet.jrdoOtherPressure.setSelected(false);
    	
    	contentPane.jxpanelInletOutlet.jtxtOtherPressure.setEnabled(false);
    	contentPane.jxpanelInletOutlet.jlblOtherPressureUnit.setEnabled(false);
    	
    	this.m_dOutletPressure = .000001;
    }
    
    public void otherOutletPressure()
    {
    	contentPane.jxpanelInletOutlet.jrdoVacuum.setSelected(false);
    	contentPane.jxpanelInletOutlet.jrdoOtherPressure.setSelected(true);
    	
    	contentPane.jxpanelInletOutlet.jtxtOtherPressure.setEnabled(true);
    	contentPane.jxpanelInletOutlet.jlblOtherPressureUnit.setEnabled(true);
    	
    	this.m_dOutletPressure = 100; // 100 kPa
    }
    
    public void switchToConstantPressureMode()
    {
    	contentPane.jxpanelInletOutlet.jrdoConstantPressure.setSelected(true);
    	contentPane.jxpanelInletOutlet.jrdoConstantFlowRate.setSelected(false);
    	contentPane.jxpanelInletOutlet.jlblFlowRate.setEnabled(false);
    	contentPane.jxpanelInletOutlet.jlblFlowRateUnit.setEnabled(false);
    	contentPane.jxpanelInletOutlet.jtxtFlowRate.setEnabled(false);
    	contentPane.jxpanelInletOutlet.jlblPressure.setEnabled(true);
    	contentPane.jxpanelInletOutlet.jtxtInletPressure.setEnabled(true);
    	contentPane.jxpanelInletOutlet.jcboInletPressureUnits.setEnabled(true);
    	m_bConstantFlowRateMode = false;
    }
    
    public void switchToConstantFlowRateMode()
    {
    	contentPane.jxpanelInletOutlet.jrdoConstantFlowRate.setSelected(true);
    	contentPane.jxpanelInletOutlet.jrdoConstantPressure.setSelected(false);
    	contentPane.jxpanelInletOutlet.jlblFlowRate.setEnabled(true);
    	contentPane.jxpanelInletOutlet.jlblFlowRateUnit.setEnabled(true);
    	contentPane.jxpanelInletOutlet.jtxtFlowRate.setEnabled(true);
    	contentPane.jxpanelInletOutlet.jlblPressure.setEnabled(false);
    	contentPane.jxpanelInletOutlet.jtxtInletPressure.setEnabled(false);
    	contentPane.jxpanelInletOutlet.jcboInletPressureUnits.setEnabled(false);
    	m_bConstantFlowRateMode = true;
    }
    
    public void switchToSplitMode()
    {
    	contentPane.jxpanelInletOutlet.jrdoSplit.setSelected(true);
    	contentPane.jxpanelInletOutlet.jrdoSplitless.setSelected(false);
    	contentPane.jxpanelInletOutlet.jlblSplitRatio.setEnabled(true);
    	contentPane.jxpanelInletOutlet.jlblSplitRatioUnits.setEnabled(true);
    	contentPane.jxpanelInletOutlet.jtxtSplitRatio.setEnabled(true);
    	m_bSplitInjectionMode = true;
    }
    
    public void switchToSplitlessMode()
    {
    	contentPane.jxpanelInletOutlet.jrdoSplit.setSelected(false);
    	contentPane.jxpanelInletOutlet.jrdoSplitless.setSelected(true);
    	contentPane.jxpanelInletOutlet.jlblSplitRatio.setEnabled(false);
    	contentPane.jxpanelInletOutlet.jlblSplitRatioUnits.setEnabled(false);
    	contentPane.jxpanelInletOutlet.jtxtSplitRatio.setEnabled(false);
    	m_bSplitInjectionMode = false;
    }

	@Override
	public void temperatureProgramChanged() 
	{
		performCalculations();
	}
}
