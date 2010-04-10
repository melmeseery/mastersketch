package gui;

import java.awt.GridBagLayout;
import javax.swing.JPanel;
import javax.swing.JSlider;
import java.awt.GridBagConstraints;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JCheckBox;
import java.awt.Dimension;

public class SketchSystemOptions extends JPanel {

	private static final long serialVersionUID = 1L;
	private JSlider jSliderSwarmMax = null;
	private JButton jButtonSave = null;
	private JLabel jLabel = null;
	private JRadioButton jRadioButtonSegmentor = null;
	private JRadioButton jRadioButtonRecognizier = null;
	private JCheckBox jCheckBoxDisplay = null;
	private JSlider jSliderErrorThreshold = null;
	private JCheckBox jCheckBoxSwarmLoop = null;
	private JCheckBox jCheckBoxErrorLoop = null;
	private JLabel jLabelSwarm = null;
	private JRadioButton jRadioButtonSwarmAlgorithmTest = null;
	private JLabel jLabelErrorValue = null;
	private JLabel jLabelSwarmValue = null;
	private JCheckBox jCheckBoxOnlineCalculations = null;
	private JCheckBox jCheckBoxCurveSwarm = null;
	private JCheckBox jCheckBoxDivideSwarm = null;
	private JCheckBox jCheckBoxPolygonSwarm = null;
	private JCheckBox jCheckBoxHybird = null;
	private JCheckBox jCheckBoxSezgin = null;
	private JPanel jPanelAlgorithms = null;
	private JPanel jPanelRadios = null;
	private JPanel jPanelSliders = null;
	/**
	 * This is the default constructor
	 */
	public SketchSystemOptions() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints161 = new GridBagConstraints();
		gridBagConstraints161.gridx = 1;  // Generated
		gridBagConstraints161.gridy = 0;  // Generated
		gridBagConstraints161.gridheight = 4;  // Generated
		gridBagConstraints161.fill=GridBagConstraints.BOTH;
		GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
		gridBagConstraints14.gridx = 0;  // Generated
		gridBagConstraints14.gridy = 11;  // Generated
		GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
		gridBagConstraints13.gridx = 1;  // Generated
		gridBagConstraints13.gridy = 11;  // Generated
//		GridBagConstraints gridBagConstraints121 = new GridBagConstraints();
//		gridBagConstraints121.gridx = 5;  // Generated
//		gridBagConstraints121.gridy = 13;  // Generated
//		GridBagConstraints gridBagConstraints111 = new GridBagConstraints();
//		gridBagConstraints111.gridx = 5;  // Generated
//		gridBagConstraints111.gridy = 14;  // Generated
//		GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
//		gridBagConstraints10.gridx = 5;  // Generated
//		gridBagConstraints10.gridy = 12;  // Generated
//		GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
//		gridBagConstraints9.gridx = 5;  // Generated
//		gridBagConstraints9.gridy = 11;  // Generated
//		GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
//		gridBagConstraints8.gridx = 5;  // Generated
//		gridBagConstraints8.gridy = 6;  // Generated
		GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
		gridBagConstraints7.gridx = 0;  // Generated
		gridBagConstraints7.gridy = 4;  // Generated
		jLabelSwarmValue = new JLabel();
		jLabelSwarmValue.setText("000");  // Generated
		this.setSize(659, 320);
		this.setLayout(new GridBagLayout());
		this.setName("Error");  // Generated
		this.setToolTipText("Error thresold in the sketch");  // Generated
		
		
		///----------------------RadioButtonSwarmAlgorithmTest
		
		//---------------------label swarm ---------
		jLabelSwarm = new JLabel();
		jLabelSwarm.setText("swarm max iteration");  // Generated
		///-----------------------------CheckBoxErrorLoop----
		GridBagConstraints gridBagConstraints22 = new GridBagConstraints();
		gridBagConstraints22.gridx = 0;  // Generated
		gridBagConstraints22.gridy = 0;  // Generated
		
	
		//-/--------------------------CheckBoxSwarmLoop-----------------------------
		GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
		gridBagConstraints12.gridx = 0;  // Generated
		gridBagConstraints12.gridy = 3;  // Generated
	
		//------------------------SliderErrorThreshold-----------------------------------
		////-------------------------CheckBoxDisplay-----------------------------------
		GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
		gridBagConstraints3.gridx = 0;  // Generated
		gridBagConstraints3.gridy = 5;  // Generated
		///------------------------RadioButtonRecognizier------------------------------
	

		///-------------------------RadioButtonSegmentor-----------------------------
//		-------------------------error label value 
		jLabelErrorValue = new JLabel();
		jLabelErrorValue.setText("000");  // Generated
		///-----------------------jlabel error thresold-------------------------------
		jLabel = new JLabel();
		jLabel.setText("Error thresold");  // Generated
		
		///------------------------Buttton save------------------------------
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.gridx = 1;  // Generated
		gridBagConstraints1.gridheight = 2;  // Generated
		gridBagConstraints1.gridy = 17;  // Generated
		this.add(getJCheckBoxErrorLoop(), gridBagConstraints22);  // Generated
		this.add(getJCheckBoxSwarmLoop(), gridBagConstraints12);  // Generated
		this.add(getJCheckBoxDisplay(), gridBagConstraints3);  // Generated
		this.add(getJButtonSave(), gridBagConstraints1);  // Generated
		this.add(getJCheckBoxOnlineCalculations(), gridBagConstraints7);  // Generated
	
	
		///-------------------------SliderSwarmMax-----------------------------
		
		this.add(getJPanelAlgorithms(), gridBagConstraints13);  // Generated
		this.add(getJPanelRadios(), gridBagConstraints14);  // Generated
		this.add(getJPanelSliders(), gridBagConstraints161);  // Generated
		
		
		
	}

	/**
	 * This method initializes jSliderSwarmMax	
	 * 	
	 * @return javax.swing.JSlider	
	 */
	private JSlider getJSliderSwarmMax() {
		if (jSliderSwarmMax == null) {
			jSliderSwarmMax = new JSlider();
			jSliderSwarmMax.setMaximum(500);  // Generated
			jSliderSwarmMax.setValue(350);  // Generated
			jSliderSwarmMax.setMinimum(10);  // Generated
			jSliderSwarmMax.setPaintTicks(true);  // Generated
			jSliderSwarmMax.setMajorTickSpacing(100);
			jSliderSwarmMax.setMinorTickSpacing(50);
			jSliderSwarmMax.setPaintLabels(true);  // Generated
			jSliderSwarmMax.setName("swarm");  // Generated
			jSliderSwarmMax.setEnabled(false);
			jSliderSwarmMax.addChangeListener(new javax.swing.event.ChangeListener() {
				public void stateChanged(javax.swing.event.ChangeEvent e) {
				//	System.out.println("stateChanged()"); // TODO Auto-generated Event stub stateChanged()
					jLabelSwarmValue.setText(jSliderSwarmMax.getValue()+"");
				}
			});
		}
		return jSliderSwarmMax;
	}

	/**
	 * This method initializes jButtonSave	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonSave() {
		if (jButtonSave == null) {
			jButtonSave = new JButton();
			jButtonSave.setText("Save");  // Generated
		}
		return jButtonSave;
	}

	/**
	 * This method initializes jRadioButtonSegmentor	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getJRadioButtonSegmentor() {
		if (jRadioButtonSegmentor == null) {
			jRadioButtonSegmentor = new JRadioButton();
			jRadioButtonSegmentor.setText("Segmentation Test");  // Generated
		}
		return jRadioButtonSegmentor;
	}

	/**
	 * This method initializes jRadioButtonRecognizier	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getJRadioButtonRecognizier() {
		if (jRadioButtonRecognizier == null) {
			jRadioButtonRecognizier = new JRadioButton();
			jRadioButtonRecognizier.setText("Recognizier Test   ");  // Generated
		}
		return jRadioButtonRecognizier;
	}

	/**
	 * This method initializes jCheckBoxDisplay	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getJCheckBoxDisplay() {
		if (jCheckBoxDisplay == null) {
			jCheckBoxDisplay = new JCheckBox();
			jCheckBoxDisplay.setText(" Display sketch ouput ");
		}
		return jCheckBoxDisplay;
	}

	/**
	 * This method initializes jSliderErrorThreshold	
	 * 	
	 * @return javax.swing.JSlider	
	 */
	private JSlider getJSliderErrorThreshold() {
		if (jSliderErrorThreshold == null) {
			jSliderErrorThreshold = new JSlider();
			jSliderErrorThreshold.setMaximum(100);  // Generated
			jSliderErrorThreshold.setMinimum(0);
			jSliderErrorThreshold.setValue(25);  // Generated
			//jSliderErrorThreshold.setSnapToTicks(true);  // Generated
			jSliderErrorThreshold.setPaintTicks(true);  // Generated
			jSliderErrorThreshold.setPaintLabels(true);  // Generated
		
			jSliderErrorThreshold.setMinorTickSpacing((int)Math.ceil(0.125*jSliderErrorThreshold.getMaximum()));
			jSliderErrorThreshold.setMajorTickSpacing((int)Math.ceil(0.25*jSliderErrorThreshold.getMaximum()));
			
			jSliderErrorThreshold.addChangeListener(new javax.swing.event.ChangeListener() {
				public void stateChanged(javax.swing.event.ChangeEvent e) {
				//	System.out.println("stateChanged()"); // TODO Auto-generated Event stub stateChanged()
					
					jLabelErrorValue.setText(jSliderErrorThreshold.getValue()+"");
					 
				}
			});
		
		}
		return jSliderErrorThreshold;
	}

	/**
	 * This method initializes jCheckBoxSwarmLoop	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getJCheckBoxSwarmLoop() {
		if (jCheckBoxSwarmLoop == null) {
			jCheckBoxSwarmLoop = new JCheckBox();
			jCheckBoxSwarmLoop.setText("Loop on max swarm  ");  // Generated
			jCheckBoxSwarmLoop.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
				//	System.out.println("actionPerformed()"); // TODO Auto-generated Event stub actionPerformed()
					getJSliderSwarmMax().setEnabled(jCheckBoxSwarmLoop.isSelected());
				}
			});
		}
		return jCheckBoxSwarmLoop;
	}

	/**
	 * This method initializes jCheckBoxErrorLoop	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getJCheckBoxErrorLoop() {
		if (jCheckBoxErrorLoop == null) {
			jCheckBoxErrorLoop = new JCheckBox();
			jCheckBoxErrorLoop.setText("Loop on Error Thresold");
			jCheckBoxErrorLoop.setSelected(true);
			jCheckBoxErrorLoop.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					//System.out.println("actionPerformed()"); // TODO Auto-generated Event stub actionPerformed()
					
					getJSliderErrorThreshold().setEnabled(jCheckBoxErrorLoop.isSelected());
				}
			});
		}
		return jCheckBoxErrorLoop;
	}

	/**
	 * This method initializes jRadioButtonSwarmAlgorithmTest	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getJRadioButtonSwarmAlgorithmTest() {
		if (jRadioButtonSwarmAlgorithmTest == null) {
			jRadioButtonSwarmAlgorithmTest = new JRadioButton();
			jRadioButtonSwarmAlgorithmTest.setText("Swarm Alg. Test  ");  // Generated
		}
		return jRadioButtonSwarmAlgorithmTest;
	}

	/**
	 * This method initializes jCheckBoxOnlineCalculations	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getJCheckBoxOnlineCalculations() {
		if (jCheckBoxOnlineCalculations == null) {
			jCheckBoxOnlineCalculations = new JCheckBox();
			jCheckBoxOnlineCalculations.setText("Online Calculations");  // Generated
		}
		return jCheckBoxOnlineCalculations;
	}

	/**
	 * This method initializes jCheckBoxCurveSwarm	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getJCheckBoxCurveSwarm() {
		if (jCheckBoxCurveSwarm == null) {
			jCheckBoxCurveSwarm = new JCheckBox();
			jCheckBoxCurveSwarm.setText("Use Curve Swarm     ");  // Generated
		}
		return jCheckBoxCurveSwarm;
	}

	/**
	 * This method initializes jCheckBoxDivideSwarm	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getJCheckBoxDivideSwarm() {
		if (jCheckBoxDivideSwarm == null) {
			jCheckBoxDivideSwarm = new JCheckBox();
			jCheckBoxDivideSwarm.setText("Use Divide Curve swarm");  // Generated
		}
		return jCheckBoxDivideSwarm;
	}

	/**
	 * This method initializes jCheckBoxPolygonSwarm	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getJCheckBoxPolygonSwarm() {
		if (jCheckBoxPolygonSwarm == null) {
			jCheckBoxPolygonSwarm = new JCheckBox();
			jCheckBoxPolygonSwarm.setText("Use polygon swram");  // Generated
		}
		return jCheckBoxPolygonSwarm;
	}

	/**
	 * This method initializes jCheckBoxHybird	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getJCheckBoxHybird() {
		if (jCheckBoxHybird == null) {
			jCheckBoxHybird = new JCheckBox();
			jCheckBoxHybird.setText(" hybird of all            ");  // Generated
		}
		return jCheckBoxHybird;
	}

	/**
	 * This method initializes jCheckBoxSezgin	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getJCheckBoxSezgin() {
		if (jCheckBoxSezgin == null) {
			jCheckBoxSezgin = new JCheckBox();
			jCheckBoxSezgin.setText("sezgin algorithm  ");  // Generated
		}
		return jCheckBoxSezgin;
	}

	/**
	 * This method initializes jPanelAlgorithms	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelAlgorithms() {
		if (jPanelAlgorithms == null) {
			jPanelAlgorithms = new JPanel();
			GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
			gridBagConstraints13.gridx = 1;  // Generated
			gridBagConstraints13.gridy = 1;  // Generated
			GridBagConstraints gridBagConstraints121 = new GridBagConstraints();
			gridBagConstraints121.gridx = 1;  // Generated
			gridBagConstraints121.anchor = GridBagConstraints.WEST;  // Generated
			gridBagConstraints121.gridy = 2;  // Generated
			GridBagConstraints gridBagConstraints111 = new GridBagConstraints();
			gridBagConstraints111.gridx = 1;  // Generated
			gridBagConstraints111.anchor = GridBagConstraints.WEST;  // Generated
			gridBagConstraints111.gridy = 3;  // Generated
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.gridx = 1;  // Generated
			gridBagConstraints10.anchor = GridBagConstraints.WEST;  // Generated
			gridBagConstraints10.gridy = 1;  // Generated
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.gridx = 2;  // Generated
			gridBagConstraints9.anchor = GridBagConstraints.WEST;  // Generated
			gridBagConstraints9.gridy = 1;  // Generated
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.gridx = 2;  // Generated
			gridBagConstraints8.anchor = GridBagConstraints.WEST;  // Generated
			gridBagConstraints8.gridy = 2;  // Generated
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.gridx = 3;  // Generated
			gridBagConstraints7.gridy = 1;  // Generated
			
			
			jPanelAlgorithms.setLayout(new GridBagLayout());  // Generated
			jPanelAlgorithms.add(getJCheckBoxCurveSwarm(), gridBagConstraints8);  // Generated
			jPanelAlgorithms.add(getJCheckBoxDivideSwarm(), gridBagConstraints9);  // Generated
			jPanelAlgorithms.add(getJCheckBoxPolygonSwarm(), gridBagConstraints10);  // Generated
			
			jPanelAlgorithms.add(getJCheckBoxHybird(), gridBagConstraints111);  // Generated
			jPanelAlgorithms.add(getJCheckBoxSezgin(), gridBagConstraints121);  // Generated
			
			
			
		}
		return jPanelAlgorithms;
	}

	/**
	 * This method initializes jPanelRadios	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelRadios() {
		if (jPanelRadios == null) {
			GridBagConstraints gridBagConstraints16 = new GridBagConstraints();
			gridBagConstraints16.gridx = 1;  // Generated
			gridBagConstraints16.gridy = 1;  // Generated
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.gridx = 1;  // Generated
			gridBagConstraints11.gridy = 2;  // Generated
			GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
			gridBagConstraints15.gridx = 1;  // Generated
			gridBagConstraints15.gridy = 3;  // Generated
			jPanelRadios = new JPanel();
			jPanelRadios.setLayout(new GridBagLayout());  // Generated
			jPanelRadios.add(getJRadioButtonRecognizier(), gridBagConstraints15);  // Generated
			jPanelRadios.add(getJRadioButtonSegmentor(), gridBagConstraints11);  // Generated
			jPanelRadios.add(getJRadioButtonSwarmAlgorithmTest(), gridBagConstraints16);  // Generated
		}
		return jPanelRadios;
	}

	/**
	 * This method initializes jPanelSliders	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelSliders() {
		if (jPanelSliders == null) {
			GridBagConstraints gridBagConstraints17 = new GridBagConstraints();
			gridBagConstraints17.gridx = 1;  // Generated
			gridBagConstraints17.gridy = 4;  // Generated
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 1;  // Generated
			gridBagConstraints2.gridy = 2;  // Generated
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.gridx = 2;  // Generated
			gridBagConstraints6.gridy = 1;  // Generated
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.gridx = 2;  // Generated
			gridBagConstraints5.gridy = 3;  // Generated
			
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = GridBagConstraints.VERTICAL;  // Generated
			gridBagConstraints.gridx = 1;  // Generated
			gridBagConstraints.gridy = 3;  // Generated
			gridBagConstraints.weightx = 1.0;  // Generated
		
			
			
			
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.fill = GridBagConstraints.VERTICAL;  // Generated
			gridBagConstraints4.gridx = 1;  // Generated
			gridBagConstraints4.gridy = 1;  // Generated
			gridBagConstraints4.weightx = 1.0;  // Generated
			gridBagConstraints4.gridwidth = 2;  // Generated
			jPanelSliders = new JPanel();
			jPanelSliders.setLayout(new GridBagLayout());  // Generated
			jPanelSliders.add(getJSliderErrorThreshold(), gridBagConstraints4);  // Generated
			jPanelSliders.add(getJSliderSwarmMax(), gridBagConstraints);  // Generated
			jPanelSliders.add(jLabelSwarmValue, gridBagConstraints5);  // Generated
			jPanelSliders.add(jLabelErrorValue, gridBagConstraints6);  // Generated
			jPanelSliders.add(jLabel, gridBagConstraints2);  // Generated
			jPanelSliders.add(jLabelSwarm, gridBagConstraints17);  // Generated
		}
		return jPanelSliders;
	}

}  //  @jve:decl-index=0:visual-constraint="17,24"
