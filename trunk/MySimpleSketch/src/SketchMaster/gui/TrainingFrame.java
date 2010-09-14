package SketchMaster.gui;

import org.apache.log4j.Logger;

import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.Dimension;
import javax.swing.JScrollPane;

import java.awt.Color;
import java.awt.Rectangle;

import javax.swing.JFileChooser;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JToggleButton;

import java.awt.GridBagConstraints;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JLabel;

import SketchMaster.gui.Events.HandleFinishCluster;
import SketchMaster.gui.Events.NewClusterEvent;
import SketchMaster.gui.optionDialogs.CategoryAdd;
import SketchMaster.io.log.FileLog;
import SketchMaster.system.SystemSettings;
import SketchMaster.system.Recogniziers.RecognizierSystem;
import SketchMaster.system.Recogniziers.RubineRecognizier;
import SketchMaster.system.Recogniziers.SVMRecognizier;
import SketchMaster.system.Recogniziers.SimpleSymbolRecognizier;

import java.awt.Point;
import java.io.File;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JTextArea;
import javax.swing.JCheckBox;

public class TrainingFrame extends JFrame implements Observer,HandleFinishCluster {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(TrainingFrame.class);

	private static final long serialVersionUID = 1L;

	public static final int RUBINE_RECOGNIZER = 0;

	public static final int SYMBOL_RECOGNIZER = 1;
	public static final int SVM_RECOGNIZER = 2;
	private int Recognizier_type;

	protected JPanel jContentPane = null;

	private JScrollPane jScrollPane = null;

	protected JPanel jPanel = null;

	private JMenuBar jJMenuBar = null;

	private JMenu jProjectMenu = null;

	private DrawingSheet drawingSheet = null;

	private JComboBox jComboBoxCategoryNames = null;

	private JComboBox jComboBoxExamples = null;

	private JMenuItem jMenuOpen = null;

	private JMenuItem jMenuItemSave = null;

	private JMenuItem jMenuClose = null;

	private JLabel jLabel = null;

	private JLabel jLabel1 = null;

	private JToggleButton jButton2 = null;

	private JMenu jMenu = null;

	private JMenu jMenu1 = null;

	private JMenuItem jMenuItem = null;

	private JMenuItem jMenuItem1 = null;

	private JMenuItem jMenuItem2 = null;

	private JMenuItem jMenuItem3 = null;

	private JMenuItem jMenuItem4 = null;

	private JMenuItem jMenuItem5 = null;

	private JMenu jMenu2 = null;

	private JMenuItem jMenuItem6 = null;

	private JMenuItem jMenu3 = null;

	private JMenuItem jMenuItem7 = null;

	private JMenuItem jMenu4 = null;

	private JMenuItem jMenu5 = null;

	private JMenuItem jMenuItem8 = null;

	private JMenuItem jMenu6 = null;
	SketchMaterApplication App;
	transient RecognizierSystem recognizier = null; // @jve:decl-index=0:

	private JTextArea jTextArea = null;

	private JButton jButton = null;

	private JCheckBox jCheckBox = null;

	private Dimension oldDim;

	boolean DemoMode=false;

	/**
	 * This is the default constructor
	 */
	public TrainingFrame() {
		super();
		initialize();
	}

	public TrainingFrame(SketchMaterApplication application,
			RecognizierSystem Recognizier, int type) {

		super();
		recognizier = Recognizier;
		getRecognizier().addObserver(this);
		Recognizier_type = type;
		initialize();
		App = application;

		//  logger.trace("   ------------------------------------------------");
	}

	public TrainingFrame( RecognizierSystem Recognizier,
			int type, boolean b) {
		super();
		recognizier = Recognizier;
		getRecognizier().addObserver(this);
		Recognizier_type = type;
		DemoMode=b;
		initialize();
	
	}

	private RecognizierSystem getRecognizier() {
		if (recognizier == null) {
			if (this.getRecognizierType() == RUBINE_RECOGNIZER) {
				//  logger.trace(" initailizing  the  rubine recognizier ");
				recognizier = new RubineRecognizier();
				recognizier.init();
			} else if (this.getRecognizierType() == SYMBOL_RECOGNIZER) {
				//  logger.trace(" initailizing  the symbol  recognizier ");
				recognizier = new SimpleSymbolRecognizier();
				recognizier.init();
				recognizier.setDataDisplay(getDrawingSheet());
			}
			
			else if (this.getRecognizierType() == SVM_RECOGNIZER) {
				//  logger.trace(" initailizing  the symbol  recognizier ");
				recognizier = new SVMRecognizier();
				recognizier.init();
				recognizier.setDataDisplay(getDrawingSheet());
			}

		}

		return recognizier;
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setContentPane(getJContentPane());
		this.setJMenuBar(getJJMenuBar());
		this.setTitle("JFrame");
		this.setBounds(new Rectangle(0, 0, 594, 584));
		oldDim=this.getSize();
		this.addComponentListener(new java.awt.event.ComponentListener() {
			public void componentResized(java.awt.event.ComponentEvent e) {
				getChangedSize();
				//Dimension d = getContentPane().getSize();

				// the new size
			//	getJScrollPane().setSize(d.width - 10, d.height-2);

			}

			public void componentMoved(java.awt.event.ComponentEvent e) {
				repaint();
			}

			public void componentShown(java.awt.event.ComponentEvent e) {
				repaint();
			}

			public void componentHidden(java.awt.event.ComponentEvent e) {
			}
		});
	}
	
	private void getChangedSize() {
		//  
		Dimension dim = this.getSize();
		
		//change
		int deltax=dim.width-oldDim.width;
		int deltay=dim.height-oldDim.height;
	
		 Rectangle b=getJScrollPane().getBounds();
		
		 getJScrollPane().setBounds(b.x, b.y, b.width+deltax, b.height+deltay);
		oldDim=dim;
		
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.add(getJScrollPane());
		//	if(!DemoMode){
			  jContentPane.add(getJPanel());
			//}
			jContentPane.addComponentListener(new java.awt.event.ComponentAdapter() {
				public void componentResized(java.awt.event.ComponentEvent e) {
	
					getChangedSize();
				}
			});
			// jContentPane.setLayout(new BorderLayout());
			// jContentPane.add(getJScrollPane(),BorderLayout.SOUTH);
			// jContentPane.add(getJPanel(), BorderLayout.NORTH);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			// jScrollPane.setLayout(null);
			jScrollPane.setBounds(new Rectangle(14, 249, 552, 261));
			jScrollPane.setViewportView(getDrawingSheet());  // Generated
			jScrollPane.setBorder(null);
		}
		return jScrollPane;
	}

	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	protected JPanel getJPanel() {
		if (jPanel == null) {

			jLabel2 = new JLabel();
			jLabel2.setBounds(new Rectangle(542, 13, 39, 39));
			jLabel2.setText("  0");
			jLabel1 = new JLabel();
			jLabel1.setText("Examples");
			jLabel1.setBounds(new Rectangle(320, 28, 68, 15));
			jLabel = new JLabel();
			jLabel.setText("Category");
			jLabel.setBounds(new Rectangle(22, 22, 81, 22));
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints1.weightx = 1.0;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.weightx = 1.0;
			jPanel = new JPanel();
			jPanel.setLayout(null);
			jPanel.setBounds(new Rectangle(2, 16, 591, 223));
			if(!DemoMode){
			jPanel.add(getJComboBoxCategoryNames());
			jPanel.add(jLabel);
			jPanel.add(jLabel1);
			jPanel.add(getJButton2(), null); //capture strokes 
			// jPanel.add(jLabel2, null);
			}
			
			if (DemoMode){
				
				getRecognizier().setCapturesStroke(DemoMode);
				
			}
			
			
			jPanel.add(getJTextArea(), null);
		
			jPanel.add(getJButton(), null);  ///clear sheet
			jPanel.add(getJCheckBox(), null);

			if (getRecognizierType() == SYMBOL_RECOGNIZER||getRecognizierType() ==SVM_RECOGNIZER) {
				jPanel.add(getJButtonSymbol());
				if(!DemoMode){
				jPanel.add(getJComboBoxExamples(), null);  // Generated
				jPanel.add(jLabel2, null);
				jPanel.add(getJToggleButton(), null);  // Generated
				}
				
				
			}

		}
		return jPanel;
	}

	/**
	 * This method initializes jJMenuBar
	 * 
	 * @return javax.swing.JMenuBar
	 */
	private JMenuBar getJJMenuBar() {
		if (jJMenuBar == null) {
			jJMenuBar = new JMenuBar();
			jJMenuBar.add(getJProjectMenu());
			jJMenuBar.add(getJMenu());
			jJMenuBar.add(getJMenu1());
			jJMenuBar.add(getJMenu2());
		}
		return jJMenuBar;
	}

	/**
	 * This method initializes jProjectMenu
	 * 
	 * @return javax.swing.JMenu
	 */
	private JMenu getJProjectMenu() {
		if (jProjectMenu == null) {
			jProjectMenu = new JMenu();
			jProjectMenu.setText("Data");

			jProjectMenu.add(getJMenuOpen());
			jProjectMenu.add(getJMenuItemLoadStrokes());  // Generated
			jProjectMenu.add(getJMenuItemSave());
			jProjectMenu.add(getJMenuItemSaveStrokes());  // Generated
			jProjectMenu.add(getJMenuClose());
		}
		return jProjectMenu;
	}

	/**
	 * This method initializes drawingSheet
	 * 
	 * @return SketchMaster.gui.DrawingSheet
	 */
	private DrawingSheet getDrawingSheet() {
		if (drawingSheet == null) {
			drawingSheet = new DrawingSheet();
			// then add the sheet to the recognizier
			drawingSheet.setBackground(Color.white);
			drawingSheet.addstrokeListener(getRecognizier());
			drawingSheet.addClusterListener(this);
			drawingSheet.watched.addObserver(getRecognizier());
			

		}
		return drawingSheet;
	}

	// implements HandleStroke
	/**
	 * This method initializes jComboBoxCategoryNames
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getJComboBoxCategoryNames() {
		if (jComboBoxCategoryNames == null) {
			jComboBoxCategoryNames = new JComboBox();
			jComboBoxCategoryNames.setSize(new Dimension(182, 26));
			jComboBoxCategoryNames.setName("CategoryNames");
			jComboBoxCategoryNames
					.setToolTipText("To  add a new    cateogry just choose add a ne w ");
			jComboBoxCategoryNames.setLocation(new Point(121, 22));
			initCategoryNames(jComboBoxCategoryNames);

			jComboBoxCategoryNames
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {

							// logger.info("traing frame add cateogry ");

							if (jComboBoxCategoryNames.getSelectedItem() == null)
								return;
							if (jComboBoxCategoryNames.getSelectedItem()
									.equals("Add new Category")) {
								String newCat = DisplayCategoryAddDialog();

								if (newCat != null) {
									getRecognizier().addNewCategory(newCat);
									initCategoryNames(getJComboBoxCategoryNames());
									// jComboBoxCategoryNames.addItem(newCat);

									getJComboBoxCategoryNames()
											.setSelectedItem(newCat);
									getJComboBoxExamples().removeAllItems();
									getAddExampleComboItems(
											getJComboBoxExamples(), 0);
								}
							} else {

								getRecognizier().setCurrentCat(
										jComboBoxCategoryNames
												.getSelectedIndex());
								getRecognizier()
										.setCurrentSelectedCategoryName(
												(String) jComboBoxCategoryNames
														.getSelectedItem());

								int examplesSize = 0;

								examplesSize = getRecognizier()
										.getCurrentExamplesCount();
								getJComboBoxExamples().removeAllItems();

								getAddExampleComboItems(getJComboBoxExamples(),
										examplesSize);
							}
						}

					});

		}
		return jComboBoxCategoryNames;
	}

	private void getAddExampleComboItems(JComboBox combo, int count) {
		for (int i = 0; i < count; i++) {

			combo.addItem("Example " + i);
		}
		
		jLabel2.setText("  "+count);
		combo.addItem("Add new Example");
		
		

	}

	private void initCategoryNames(JComboBox comboBoxCategoryNames) {

		comboBoxCategoryNames.removeAllItems();
		String[] cat = getRecognizier().getCategoryNames();
		// logger.info("cateogry name s at beginign is "+cat.length);
		for (int i = 0; i < cat.length; i++) {
			comboBoxCategoryNames.addItem(cat[i]);
		}
		if (cat.length == 0)

			comboBoxCategoryNames.addItem("   ");
		comboBoxCategoryNames.addItem("Add new Category");
		// make it first choice

	}

	/**
	 * This method initializes jComboBoxExamples
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getJComboBoxExamples() {
		if (jComboBoxExamples == null) {
			jComboBoxExamples = new JComboBox();
			getAddExampleComboItems(jComboBoxExamples, 0);
			jComboBoxExamples.setVisible(true);

			jComboBoxExamples.setBounds(new Rectangle(394, 16, 140, 35));  // Generated
		}
		return jComboBoxExamples;
	}

	/**
	 * This method initializes jMenuOpen
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getJMenuOpen() {
		if (jMenuOpen == null) {
			jMenuOpen = new JMenuItem();
			jMenuOpen.setText("Load Training Data");
			jMenuOpen.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					JFileChooser fc = new JFileChooser(new File("."));
					// logger.info("actionPerformed()");
					fc.setFileFilter(new SketchFileFilter());

					// In response to a button click:
					if (fc.showOpenDialog(getJContentPane().getParent()) == JFileChooser.APPROVE_OPTION) { // .showOpenDialog(this);
						// logger.info("actionPerformed open ()");
						getRecognizier().ReadTrainingSet(
								fc.getSelectedFile().getAbsolutePath());
						// jComboBoxCategoryNames=null;
						// getJComboBoxCategoryNames();
						initCategoryNames(getJComboBoxCategoryNames());
						getJComboBoxCategoryNames().setSelectedIndex(0);

						getRecognizier().setCurrentCat(
								getJComboBoxCategoryNames().getSelectedIndex());
						getRecognizier().setCurrentSelectedCategoryName(
								(String) getJComboBoxCategoryNames()
										.getSelectedItem());
						int examplesSize = 0;
						examplesSize = getRecognizier()
								.getCurrentExamplesCount();
						
						getJComboBoxExamples().removeAllItems();
						getAddExampleComboItems(getJComboBoxExamples(),
								examplesSize);

						JOptionPane.showMessageDialog(getJContentPane()
								.getParent(),
								"Finish the process of open file ");

					}
				}
			});
		}
		return jMenuOpen;
	}

	/**
	 * This method initializes jMenuItemSave
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getJMenuItemSave() {
		if (jMenuItemSave == null) {
			jMenuItemSave = new JMenuItem();
			jMenuItemSave.setText("Save Data");
			jMenuItemSave
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							JFileChooser fc = new JFileChooser(new File("."));
							// logger.info("actionPerformed()");
							fc.setFileFilter(new SketchFileFilter());
							// In response to a button click:
							if (fc
									.showSaveDialog(getJContentPane()
											.getParent()) == JFileChooser.APPROVE_OPTION) { // .showOpenDialog(this);
								// logger.info("actionPerformed open
								// ()");
								getRecognizier().SaveTrainingSet(
										fc.getSelectedFile().getAbsolutePath());
								JOptionPane.showMessageDialog(getJContentPane()
										.getParent(),
										"Finish the process of saving file "+fc.getSelectedFile().getAbsolutePath());

							}
						}
					});
		}
		return jMenuItemSave;
	}

	/**
	 * 
	 * This method initializes jMenuClose
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getJMenuClose() {
		if (jMenuClose == null) {
			jMenuClose = new JMenuItem();
			jMenuClose.setText("Close");
		}
		return jMenuClose;
	}

	/**
	 * This method initializes jButton2
	 * 
	 * @return javax.swing.JButton
	 */
	private JToggleButton getJButton2() {
		if (jButton2 == null) {
			jButton2 = new JToggleButton();
			jButton2.setBounds(new Rectangle(341, 61, 181, 24));
			jButton2.setText("capture strokes");

			jButton2.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					getRecognizier().setCapturesStroke(
							getJButton2().isSelected());
					// logger.info("actionPerformed()");
				}
			});
		}
		return jButton2;
	}

	/**
	 * 
	 * This method initializes jMenu
	 * 
	 * @return javax.swing.JMenu
	 */
	private JMenu getJMenu() {
		if (jMenu == null) {
			jMenu = new JMenu();
			jMenu.setText("Category");
			jMenu.add(getJMenuAddAllCat());  // Generated
			jMenu.add(getJMenuItem());
			jMenu.add(getJMenuItem1());
			jMenu.add(getJMenuItem2());
			jMenu.add(getJMenuItem7());
			jMenu.add(getJMenu6());
		}
		return jMenu;
	}

	/**
	 * This method initializes jMenu1
	 * 
	 * @return javax.swing.JMenu
	 */
	private JMenu getJMenu1() {
		if (jMenu1 == null) {
			jMenu1 = new JMenu();
			jMenu1.setText("Examples");
			jMenu1.add(getJMenuItem3());
			jMenu1.add(getJMenuItem4());
			jMenu1.add(getJMenuItem5());
			jMenu1.add(getJMenu3());
			jMenu1.add(getJMenuItem8());
		}
		return jMenu1;
	}

	/**
	 * This method initializes jMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getJMenuItem() {
		if (jMenuItem == null) {
			jMenuItem = new JMenuItem();
			jMenuItem.setText("Add Category");
			jMenuItem.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					// String newcat= DisplayCategoryAddDialog();
					getJComboBoxCategoryNames().setSelectedIndex(
							getJComboBoxCategoryNames().getItemCount() - 1);
					// jComboBoxCategoryNames

				}
			});
		}
		return jMenuItem;
	}

	private String DisplayCategoryAddDialog() {
		CategoryAdd cat = new CategoryAdd(this);

		cat.setVisible(true);

		String cateogoryString = cat.getNewCategory();
		// if null then cancel
		// else correct and add cateogry
		return cateogoryString;

	}

	/**
	 * This method initializes jMenuItem1
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getJMenuItem1() {
		if (jMenuItem1 == null) {
			jMenuItem1 = new JMenuItem();
			jMenuItem1.setText("Delete Selected Category");
			jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					// logger.info("actionPerformed()");
					if (!((String) getJComboBoxCategoryNames()
							.getSelectedItem()).startsWith("Add new")) {
						getRecognizier().deleteCategory(
								((String) getJComboBoxCategoryNames()
										.getSelectedItem()));
						initCategoryNames(getJComboBoxCategoryNames());
					}
				}
			});
		}
		return jMenuItem1;
	}

	/**
	 * This method initializes jMenuItem2
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getJMenuItem2() {
		if (jMenuItem2 == null) {
			jMenuItem2 = new JMenuItem();
			jMenuItem2.setText("Delete All");
		}
		return jMenuItem2;
	}

	/**
	 * This method initializes jMenuItem3
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getJMenuItem3() {
		if (jMenuItem3 == null) {
			jMenuItem3 = new JMenuItem();
			jMenuItem3.setText("Add Example");
		}
		return jMenuItem3;
	}

	/**
	 * This method initializes jMenuItem4
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getJMenuItem4() {
		if (jMenuItem4 == null) {
			jMenuItem4 = new JMenuItem();
			jMenuItem4.setText("Delete current ");
			jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					// logger.info("actionPerformed()");
					if (!((String) getJComboBoxExamples().getSelectedItem())
							.startsWith("Add new")) {
						int examplesSize = 0;
						examplesSize = getRecognizier().deleteCategoryExample(
								((String) getJComboBoxCategoryNames()
										.getSelectedItem()),
								getJComboBoxExamples().getSelectedIndex());

						getJComboBoxExamples().removeAllItems();
						getAddExampleComboItems(getJComboBoxExamples(),
								examplesSize);
						getJComboBoxExamples().setSelectedIndex(0);

					}
				}

			});
		}
		return jMenuItem4;
	}

	/**
	 * This method initializes jMenuItem5
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getJMenuItem5() {
		if (jMenuItem5 == null) {
			jMenuItem5 = new JMenuItem();
			jMenuItem5.setText("Delete All");
		}
		return jMenuItem5;
	}

	/**
	 * This method initializes jMenu2
	 * 
	 * @return javax.swing.JMenu
	 */
	private JMenu getJMenu2() {
		if (jMenu2 == null) {
			jMenu2 = new JMenu();
			jMenu2.setText("Options");
			jMenu2.add(getJMenuItem6());
			jMenu2.add(getJMenu4());
			jMenu2.add(getJMenu5());
		}
		return jMenu2;
	}

	/**
	 * This method initializes jMenuItem6
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getJMenuItem6() {
		if (jMenuItem6 == null) {
			jMenuItem6 = new JMenuItem();
			jMenuItem6.setText("Train Again'");
			jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					getRecognizier().train();
					JOptionPane.showMessageDialog(
							getJContentPane().getParent(), "Finish training ");

				}
			});
		}
		return jMenuItem6;
	}

	/**
	 * This method initializes jMenu3
	 * 
	 * @return javax.swing.JMenu
	 */
	private JMenuItem getJMenu3() {
		if (jMenu3 == null) {
			jMenu3 = new JMenuItem();
			jMenu3.setText("Recompute Features");
		}
		return jMenu3;
	}

	/**
	 * This method initializes jMenuItem7
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getJMenuItem7() {
		if (jMenuItem7 == null) {
			jMenuItem7 = new JMenuItem();
			jMenuItem7.setText("Recompute Category");
		}
		return jMenuItem7;
	}

	/**
	 * This method initializes jMenu4
	 * 
	 * @return javax.swing.JMenu
	 */
	private JMenuItem getJMenu4() {
		if (jMenu4 == null) {
			jMenu4 = new JMenuItem();
			jMenu4.setText("Options");
		}
		return jMenu4;
	}

	/**
	 * This method initializes jMenu5
	 * 
	 * @return javax.swing.JMenu
	 */
	private JMenuItem getJMenu5() {
		if (jMenu5 == null) {
			jMenu5 = new JMenuItem();
			jMenu5.setText("Clear ");
		}
		return jMenu5;
	}

	/**
	 * This method initializes jMenuItem8
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getJMenuItem8() {
		if (jMenuItem8 == null) {
			jMenuItem8 = new JMenuItem();
			jMenuItem8.setText("View All Examples");
		}
		return jMenuItem8;
	}

	/**
	 * This method initializes jMenu6
	 * 
	 * @return javax.swing.JMenu
	 */
	private JMenuItem getJMenu6() {
		if (jMenu6 == null) {
			jMenu6 = new JMenuItem();
			jMenu6.setText("View All Categories(Main eXample)");
		}
		return jMenu6;
	}

	public void update(Observable arg0, Object arg1) {

		// if (arg0 instanceof RubineRecognizier) {
		// RubineRecognizier new_name = (RubineRecognizier) arg0;
		//		
		//			
		// }

		getJTextArea().setText(this.getRecognizier().getFeatureString());
		//getRecognizier().Clear();
		getDrawingSheet().repaint();
		 //getJButton().doClick();
		jLabel2.setText("   "+this.getRecognizier().getCurrentExamplesCount());
	}

	/**
	 * This method initializes jTextArea
	 * 
	 * @return javax.swing.JTextArea
	 */
	private JTextArea getJTextArea() {
		if (jTextArea == null) {
			jTextArea = new JTextArea();

			jTextArea.setBounds(new Rectangle(25, 90, 550, 125));
			jTextArea.setEditable(false);
			jTextArea.setWrapStyleWord(true);
			jTextArea.setLineWrap(true);
			jTextArea.setText("Current Storke Feature Values");

			// jTextArea.setBounds(new Rectangle(25, 90, 550, 125));
		}
		return jTextArea;
	}

	/**
	 * This method initializes jButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButton() {
		if (jButton == null) {
			jButton = new JButton();
			jButton.setText("Clear sheet");
			jButton.setBounds(new Rectangle(26, 60, 104, 23));
			jButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					getRecognizier().Clear();

					getDrawingSheet().repaint();
				}
			});
		}
		return jButton;
	}

	/**
	 * This method initializes jCheckBox
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getJCheckBox() {
		if (jCheckBox == null) {
			jCheckBox = new JCheckBox();
			jCheckBox.setBounds(new Rectangle(140, 67, 85, 16));
			jCheckBox.setText("Test ");
			jCheckBox.setActionCommand("Test Train");
			jCheckBox.setName("TryTestCheckBox");
			jCheckBox.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					// logger.info("actionPerformed()"); stub
					// actionPerformed()
					// first if checked then disable thhe togle buttom (doesn't
					// mattter)
					// and the category ies
					// other wise disable

					setTrainSystem(!getJCheckBox().isSelected());
					if (getRecognizierType() == RUBINE_RECOGNIZER) {
						if (getJCheckBox().isSelected())
							SystemSettings.CurrentRubineOperation = RubineRecognizier.Rubine_OPERATION_CLASSIFICATION;
						else
							SystemSettings.CurrentRubineOperation = RubineRecognizier.Rubine_OPERATION_TRAINING;
					} else {
						if (getJCheckBox().isSelected())
							SystemSettings.CurrentRecognizierOperation = RecognizierSystem.RECGONIZE_OPERATION_TEST;
						else
							SystemSettings.CurrentRecognizierOperation = RecognizierSystem.RECGONIZE_OPERATION_TRAIN;

					}
				}
			});
		}
		return jCheckBox;
	}

	private void setTrainSystem(boolean test) {
		this.getJComboBoxCategoryNames().setEnabled(test);
		this.getJComboBoxExamples().setEnabled(test);
		this.getJButton2().setEnabled(true);

	}

	public class SketchFileFilter extends javax.swing.filechooser.FileFilter {
		public boolean accept(File file) {
			// Convert to lower case before checking extension
			return file.getName().toLowerCase().endsWith(".smt")|| file.getName().toLowerCase().endsWith(".xml")
					|| file.getName().toLowerCase().endsWith(".SDM")|| file.getName().toLowerCase().endsWith(".sml");
		}

		public String getDescription() {
			return "Sketch Master Files (*.smt,*.sdm,*.xml,*.sml)";
		}
	}

	JButton jBSymbol;

	private JMenuItem jMenuItemSaveStrokes = null;

	private JToggleButton jToggleButton = null;

	private JLabel jLabel2 = null;

	private JMenuItem jMenuItemElect = null;

	private JMenu jMenuAddAllCat = null;

	private JMenuItem jMenuItemMechnical = null;

	private JMenuItem jMenuItemUmL = null;

	private JMenuItem jMenuItemLoadStrokes = null;

	private int getRecognizierType() {
		return this.Recognizier_type;
	}

	private JButton getJButtonSymbol() {
		if (jBSymbol == null) {
			jBSymbol = new JButton();
			jBSymbol.setText("Symbol end");
			jBSymbol.setBounds(new Rectangle(226, 60, 104, 23));
			jBSymbol.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					FinishTheSymbol();
				
				}
			});
		}
		return jBSymbol;
	}

	
	public void FinishTheSymbol(){
		
		if (logger.isDebugEnabled()) {
			//  logger.debug("$java.awt.event.ActionListener.actionPerformed(java.awt.event.ActionEvent) - action preformed to finish the symbol of the this 952 trainingframe.java "); //$NON-NLS-1$
		}
		if (recognizier instanceof SimpleSymbolRecognizier) {
			SimpleSymbolRecognizier SymbolRecogni = (SimpleSymbolRecognizier) recognizier;
			SymbolRecogni.createClusterFromStrokes();
		}
		if (recognizier instanceof SVMRecognizier) {
			SVMRecognizier SVMrecognizier= (SVMRecognizier) recognizier;
			SVMrecognizier.createClusterFromStrokes();
			
		}
		if (!DemoMode)
		getRecognizier().Clear();
	}
	
	/**
	 * This method initializes jMenuItemSaveStrokes	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getJMenuItemSaveStrokes() {
		if (jMenuItemSaveStrokes == null) {
			jMenuItemSaveStrokes = new JMenuItem();
			jMenuItemSaveStrokes.setText("Save Strokes Data");  // Generated
			jMenuItemSaveStrokes.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					//logger.info("actionPerformed()");  
					
					
					JFileChooser fc = new JFileChooser(new File("."));
					// logger.info("actionPerformed()");
					fc.setFileFilter(new SketchFileFilter());
					// In response to a button click:
					if (fc
							.showSaveDialog(getJContentPane()
									.getParent()) == JFileChooser.APPROVE_OPTION) { // .showOpenDialog(this);
						// logger.info("actionPerformed open
						// ()");
						RecognizierSystem reg = getRecognizier();
						
						if (reg instanceof SVMRecognizier){
							SVMRecognizier svmreg = (SVMRecognizier) reg;
							svmreg.SaveTrainingSetXML( fc.getSelectedFile().getAbsolutePath());
						}
						 
						
						
						JOptionPane.showMessageDialog(getJContentPane()
								.getParent(),
								"Finish the process of saving file ");

					}
				}
		
			});
		}
		return jMenuItemSaveStrokes;
	}

	/**
	 * This method initializes jToggleButton	
	 * 	
	 * @return javax.swing.JToggleButton	
	 */
	private JToggleButton getJToggleButton() {
		if (jToggleButton == null) {
			jToggleButton = new JToggleButton();
			jToggleButton.setBounds(new Rectangle(527, 59, 45, 18));  // Generated
			jToggleButton.setText("+");  // Generated
			jToggleButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					//logger.info("actionPerformed()");  
					
					//if (){
						if(jToggleButton.isSelected())
						getRecognizier().setExampleType(	getRecognizier().EXAMPLE_POS );
						else
							getRecognizier().setExampleType(getRecognizier().EXAMPLE_NEG );
							
					//}
				}
			});
		}
		return jToggleButton;
	}

	public void HandleFinishedCluster(NewClusterEvent Evt) {
	//	 getJButtonSymbol().doClick();
		// getJButton().doClick();
		FinishTheSymbol();
	}

	/**
	 * This method initializes jMenuItemElect	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getJMenuItemElect() {
		if (jMenuItemElect == null) {
			jMenuItemElect = new JMenuItem();
			jMenuItemElect.setText("Electrical");  // Generated
			jMenuItemElect.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					//logger.info("actionPerformed()");  
					
					AddCategoryToListOfcat("Battery");
					
					AddCategoryToListOfcat("Capacitor");
					AddCategoryToListOfcat("Ground");
				
					AddCategoryToListOfcat("Clock");
					AddCategoryToListOfcat("Oscillator");
					
					AddCategoryToListOfcat("Resistor");
					AddCategoryToListOfcat("Diode");
					AddCategoryToListOfcat("Amplifier");
					AddCategoryToListOfcat("Inductor");
					AddCategoryToListOfcat("Transducer");
					
					AddCategoryToListOfcat("Divider");
					AddCategoryToListOfcat("Mulitplier");

					AddCategoryToListOfcat("Integrator");
					
					
					AddCategoryToListOfcat("Mulitplier");
					AddCategoryToListOfcat("Loop Antena");
					AddCategoryToListOfcat("Crystal");
				}
			});
		}
		return jMenuItemElect;
	}

	
	
	
	private void AddCategoryToListOfcat(String newCat){
		
		
		getRecognizier().addNewCategory(newCat);
		initCategoryNames(getJComboBoxCategoryNames());
		// jComboBoxCategoryNames.addItem(newCat);

		getJComboBoxCategoryNames()
				.setSelectedItem(newCat);
		getJComboBoxExamples().removeAllItems();
		getAddExampleComboItems(
				getJComboBoxExamples(), 0);
	}
	
	
	/**
	 * This method initializes jMenuAddAllCat	
	 * 	
	 * @return javax.swing.JMenu	
	 */
	private JMenu getJMenuAddAllCat() {
		if (jMenuAddAllCat == null) {
			jMenuAddAllCat = new JMenu();
			jMenuAddAllCat.setText("Add Categories of ");  // Generated
			jMenuAddAllCat.add(getJMenuItemElect());  // Generated
			jMenuAddAllCat.add(getJMenuItemMechnical());  // Generated
			jMenuAddAllCat.add(getJMenuItemUmL());  // Generated
		}
		return jMenuAddAllCat;
	}

	/**
	 * This method initializes jMenuItemMechnical	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getJMenuItemMechnical() {
		if (jMenuItemMechnical == null) {
			jMenuItemMechnical = new JMenuItem();
			jMenuItemMechnical.setText("Mechanical");  // Generated
			jMenuItemMechnical.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					//logger.info("actionPerformed()");  
					AddCategoryToListOfcat("Gate Valve");
					AddCategoryToListOfcat("Flow Control");
					AddCategoryToListOfcat("Sealed reservoir");
					AddCategoryToListOfcat("Filter");
					AddCategoryToListOfcat("Actuator");
					AddCategoryToListOfcat("Energy source");
					AddCategoryToListOfcat("Pump");
				}
			});
		}
		return jMenuItemMechnical;
	}

	/**
	 * This method initializes jMenuItemUmL	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getJMenuItemUmL() {
		if (jMenuItemUmL == null) {
			jMenuItemUmL = new JMenuItem();
			jMenuItemUmL.setText("Digital Logic");  // Generated
			jMenuItemUmL.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
				  //	logger.info("actionPerformed()");  
					AddCategoryToListOfcat("And Gate");
					AddCategoryToListOfcat("Or Gate");
					AddCategoryToListOfcat("Nor Gate");
					AddCategoryToListOfcat("Nand Gate");
					AddCategoryToListOfcat("Xor Gate");
					
					AddCategoryToListOfcat("Not Gate");
					
					AddCategoryToListOfcat("D flip Flop");
					AddCategoryToListOfcat("Four bit counter");
					
					 
					AddCategoryToListOfcat("Light indicator");
					
					
				}
			});
		}
		return jMenuItemUmL;
	}

	/**
	 * This method initializes jMenuItemLoadStrokes	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getJMenuItemLoadStrokes() {
		if (jMenuItemLoadStrokes == null) {
			jMenuItemLoadStrokes = new JMenuItem();
			jMenuItemLoadStrokes.setText("Load stroke data ");  // Generated
			jMenuItemLoadStrokes.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					//logger.info(" load stroke data ");  

					JFileChooser fc = new JFileChooser(new File("."));
					// logger.info("actionPerformed()");
					fc.setFileFilter(new SketchFileFilter());

					// In response to a button click:
					if (fc.showOpenDialog(getJContentPane().getParent()) == JFileChooser.APPROVE_OPTION) { // .showOpenDialog(this);
						// logger.info("actionPerformed open ()");
//						getRecognizier().(
//								fc.getSelectedFile().getAbsolutePath());
						
							RecognizierSystem reg = getRecognizier();
						
						if (reg instanceof SVMRecognizier){
							SVMRecognizier svmreg = (SVMRecognizier) reg;
							svmreg.ReadTrainingSetXML( fc.getSelectedFile().getAbsolutePath());
						}
						 
						
						// jComboBoxCategoryNames=null;
						// getJComboBoxCategoryNames();
						initCategoryNames(getJComboBoxCategoryNames());
						getJComboBoxCategoryNames().setSelectedIndex(0);

						getRecognizier().setCurrentCat(
								getJComboBoxCategoryNames().getSelectedIndex());
						getRecognizier().setCurrentSelectedCategoryName(
								(String) getJComboBoxCategoryNames()
										.getSelectedItem());
						int examplesSize = 0;
						examplesSize = getRecognizier()
								.getCurrentExamplesCount();
						
						getJComboBoxExamples().removeAllItems();
						getAddExampleComboItems(getJComboBoxExamples(),
								examplesSize);

						JOptionPane.showMessageDialog(getJContentPane()
								.getParent(),
								"Finish the process of open file ");

					}
				
				}
			});
		}
		return jMenuItemLoadStrokes;
	}

	public void setTestMode(boolean b) {
		 
		
	}

	public void setLoadTrainData(String string) {
		getRecognizier().ReadTrainingSet(string);
		JOptionPane.showMessageDialog(getJContentPane()
				.getParent(),
				"Finish reading Training Model of  "+string);
	}

	public void setDemoMode(boolean b) {
		 DemoMode=true;
		 getJCheckBox().setSelected(true);
			SystemSettings.CurrentRecognizierOperation = RecognizierSystem.RECGONIZE_OPERATION_TEST;
			
	}

}
