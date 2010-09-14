package SketchMaster.gui;

import org.apache.log4j.Logger;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.filechooser.FileFilter;

import SketchMaster.gui.Events.HandleStroke;
import SketchMaster.gui.Events.NewStrokeEvent;
import SketchMaster.gui.optionDialogs.CategoryAdd;
import SketchMaster.gui.optionDialogs.LayerSelectionDialog;
import SketchMaster.io.log.FileLog;
import SketchMaster.system.SketchRecognitionSystem;
import SketchMaster.system.SystemSettings;

public class SketchMaterApplication extends JFrame {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(SketchMaterApplication.class);

	private static final long serialVersionUID = 1L;
	final JFileChooser fc = new JFileChooser(new File("."));

	private JPanel jContentPane = null;

	private JScrollPane DrawingScrollSheet = null;

	// Create the menu bar.
	private JMenuBar ApplicationMenuBar;

	private JLabel NumberOfStrokeLabel = null;

	private JLabel TimeStrokeLabel = null;

	private JLabel CountPointStrokeLabel = null;

	private DrawingSheet DrawSheet = null;

	private static DebugMessageWindow DebugWindow;
	private int DebugWindowCount;
	SketchRecognitionSystem system; // @jve:decl-index=0:
	private JButton jClusterButton = null;
	private JButton jButtonClear = null;
	private JLabel jLabelnStroke = null;
	private JLabel jLabelTStroke = null;
	private JLabel jLabelPStroke = null;
	private int NoStrokes=0;

	private JMenuItem jMenuItemSVM = null;

	/**
	 * This is the default constructor
	 */
	public SketchMaterApplication() {
		super();
		initialize();
this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		if (SystemSettings.DEBUG_MODE) {
			SystemSettings.MODE=SystemSettings.MODE_DEBUG;
			DebugWindow = new DebugMessageWindow();
			DebugWindow.EnableTimer(false);
			DrawingDebugUtils.G_DEBUG=(Graphics2D) DebugWindow.getDrawingSheet().getGraphics();
			
			DebugWindow.setVisible(false);
			DebugWindowCount = 0;
		}
		this.system = getSystem();
	}

	private SketchRecognitionSystem getSystem() {
		if (system == null) {
			system = new SketchRecognitionSystem();
			system.addDrawingSheet(this.getDrawSheet());
			// getDrawSheet().addDataDisplay(system);
			system.getSystemOptions();
		}

		return system;
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(600, 614);
		this.setContentPane(getJContentPane());
		this.setTitle("Sketch Recognition Master");
		// logger.info("int the program ");
		this.setJMenuBar(getApplicationMenuBar());
		this.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				if (SystemSettings.MODE!=SystemSettings.MODE_DEMO)
				FileLog.closeFile();
				// logger.info("windowClosing()");
			}
		});
		this.addComponentListener(new java.awt.event.ComponentListener() {
			public void componentResized(java.awt.event.ComponentEvent e) {
				Dimension d = getContentPane().getSize();

				// the new size
				getDrawingScrollSheet().setSize(d.width - 10, d.height);

			}

			public void componentMoved(java.awt.event.ComponentEvent e) {
			}

			public void componentShown(java.awt.event.ComponentEvent e) {
			}

			public void componentHidden(java.awt.event.ComponentEvent e) {
			}
		});

		//  logger.trace("\n frame reaady for drawing");

	}

	private void close() {
		dispose();
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jLabelPStroke = new JLabel();
			jLabelPStroke.setBounds(new Rectangle(192, 89, 38, 27));  // Generated
			jLabelPStroke.setText(" ");  // Generated
			jLabelTStroke = new JLabel();
			jLabelTStroke.setBounds(new Rectangle(162, 47, 49, 27));  // Generated
			jLabelTStroke.setText(" ");  // Generated
			jLabelnStroke = new JLabel();
			jLabelnStroke.setBounds(new Rectangle(157, 14, 46, 23));  // Generated
			jLabelnStroke.setText(" ");  // Generated
			CountPointStrokeLabel = new JLabel();
			CountPointStrokeLabel.setBounds(new Rectangle(18, 88, 169, 28));
			CountPointStrokeLabel.setText("No of points in current stroke");
			TimeStrokeLabel = new JLabel();
			TimeStrokeLabel.setBounds(new Rectangle(20, 47, 134, 29));
			TimeStrokeLabel.setText("Time of current stroke");
			NumberOfStrokeLabel = new JLabel();
			NumberOfStrokeLabel.setBounds(new Rectangle(20, 13, 118, 25));
			NumberOfStrokeLabel.setText("Number of strokes");
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			// jContentPane.add(getApplicationMenuBar(),null);
			jContentPane.add(getDrawingScrollSheet(), null);
			jContentPane.add(NumberOfStrokeLabel, null);
			jContentPane.add(TimeStrokeLabel, null);
			jContentPane.add(CountPointStrokeLabel, null);
			jContentPane.add(getJClusterButton(), null);

			jContentPane.add(getJButtonClear(), null);  // Generated
			jContentPane.add(jLabelnStroke, null);  // Generated
			jContentPane.add(jLabelTStroke, null);  // Generated
			jContentPane.add(jLabelPStroke, null);  // Generated
			jContentPane.addComponentListener(new java.awt.event.ComponentAdapter() {
				public void componentResized(java.awt.event.ComponentEvent e) {
		 					Dimension temp=new Dimension(getSize());
					temp.height-=120;
					temp.width-=30;
					getDrawingScrollSheet().setSize(temp);
					getDrawSheet().setSize(temp);
				}
			});
		}
		return jContentPane;
	}

	private JMenuBar getApplicationMenuBar() {
		if (ApplicationMenuBar == null) {

			ApplicationMenuBar = new JMenuBar();
			// ApplicationMenuBar.setBounds(10, 10, 200, 10);
			// project menu the first menu.
			JMenu menuP = new JMenu("Project");
			menuP.setMnemonic(KeyEvent.VK_P);
			menuP.getAccessibleContext().setAccessibleDescription(
					"The only menu in this program that has menu items");
			ApplicationMenuBar.add(menuP);

			// save project
			JMenuItem menuItemSave = new JMenuItem("Save Project",
					KeyEvent.VK_S);
			menuItemSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
					ActionEvent.CTRL_MASK));
			menuItemSave.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {

					fc.setFileFilter(new SketchFileFilter());
					// In response to a button click:
					if (fc.showSaveDialog(getJContentPane().getParent()) == fc.APPROVE_OPTION) { // .showOpenDialog(this);
						// logger.info("actionPerformed open ()");

						getSystem().saveStrokes(
								fc.getSelectedFile().getAbsolutePath());
						// getRubineRecognizier().SaveTrainingSet();
						JOptionPane.showMessageDialog(getJContentPane()
								.getParent(),
								"Finish the process of saving file ");

					}

					// logger.info("actionPerformed()");

				}
			});
			menuItemSave.getAccessibleContext().setAccessibleDescription(
					"Save Project");
			menuP.add(menuItemSave);
			// / open project

			JMenuItem menuItemOpen = new JMenuItem("Open Project",
					KeyEvent.VK_O);
			menuItemOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
					ActionEvent.CTRL_MASK));
			menuItemOpen.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					fc.setFileFilter(new SketchFileFilter());
					// In response to a button click:
					if (fc.showOpenDialog(getJContentPane().getParent()) == fc.APPROVE_OPTION) { // .showOpenDialog(this);
						// logger.info("actionPerformed open ()");
						getSystem().readStrokes(
								fc.getSelectedFile().getAbsolutePath());
						JOptionPane.showMessageDialog(getJContentPane()
								.getParent(),
								"Finish the process of open file ");

					}

				}
			});
			menuItemOpen.getAccessibleContext().setAccessibleDescription(
					"Open Project");
			menuP.add(menuItemOpen);

			// close
			JMenuItem menuItemClose = new JMenuItem("Close", KeyEvent.VK_C);
			menuItemClose.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,
					ActionEvent.CTRL_MASK));
			menuItemClose
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							// logger.info("actionPerformed()");
							close();

						}

					});
			menuItemClose.getAccessibleContext().setAccessibleDescription(
					"Close System");
			menuP.add(menuItemClose);

			// The view menu

			JMenu menuView = new JMenu("View");
			menuView.setMnemonic(KeyEvent.VK_V);
			menuView.getAccessibleContext().setAccessibleDescription(
					"The only menu in this view ");

			// STATISTICAL_POINTS_DRAW

			// / item view all

			JCheckBoxMenuItem menuItemViewPoints = new JCheckBoxMenuItem(
					"View stats points");
			menuItemViewPoints.setAccelerator(KeyStroke.getKeyStroke(
					KeyEvent.VK_P, ActionEvent.CTRL_MASK));
			menuItemViewPoints
					.setSelected(SystemSettings.STATISTICAL_POINTS_DRAW);
			menuItemViewPoints.getAccessibleContext().setAccessibleDescription(
					"View dominate points..");
			menuItemViewPoints
					.addActionListener(new java.awt.event.ActionListener() {

						public void actionPerformed(java.awt.event.ActionEvent e) {
							JCheckBoxMenuItem temp = (JCheckBoxMenuItem) e
									.getSource();
							SystemSettings.STATISTICAL_POINTS_DRAW = temp
									.isSelected();
							getDrawSheet().repaint();

						}
					});

			menuView.add(menuItemViewPoints);

			// / item view all

			JCheckBoxMenuItem menuItemViewAll = new JCheckBoxMenuItem(
					"View All");
			menuItemViewAll.setAccelerator(KeyStroke.getKeyStroke(
					KeyEvent.VK_A, ActionEvent.CTRL_MASK));
			menuItemViewAll.getAccessibleContext().setAccessibleDescription(
					"View all views of draws sheet..");
			menuItemViewAll
					.addActionListener(new java.awt.event.ActionListener() {

						public void actionPerformed(java.awt.event.ActionEvent e) {
							JCheckBoxMenuItem temp = (JCheckBoxMenuItem) e
									.getSource();
							getSystem().ViewAll(temp.isSelected());
							getDrawSheet().repaint();

						}
					});

			menuView.add(menuItemViewAll);

			// view intial
			JCheckBoxMenuItem menuItemViewFirst = new JCheckBoxMenuItem(
					"View Intial");
			menuItemViewFirst.getAccessibleContext().setAccessibleDescription(
					"View intial  views of draws sheet..");

			menuItemViewFirst
					.addActionListener(new java.awt.event.ActionListener() {

						// boolean active=true;
						public void actionPerformed(java.awt.event.ActionEvent e) {
							JCheckBoxMenuItem temp = (JCheckBoxMenuItem) e
									.getSource();
							getSystem().setActiveLayer(0, temp.isSelected());
							getDrawSheet().repaint();
							// getSystem().getLayers().length

						}
					});

			menuView.add(menuItemViewFirst);

			// item more view option
			JMenuItem menuItemViewOptions = new JMenuItem("View Options",
					KeyEvent.VK_O);

			menuItemViewOptions
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							DisplayLayerDialog();

					if (logger.isDebugEnabled()) {
						//  logger.debug("$java.awt.event.ActionListener.actionPerformed(java.awt.event.ActionEvent) - actionPerformed() of view options line 303 in sketch master application.java"); //$NON-NLS-1$
					}
						}
					});
			menuItemViewOptions.getAccessibleContext()
					.setAccessibleDescription("ViewOptions ");
			menuView.add(menuItemViewOptions);
			ApplicationMenuBar.add(menuView);

			JMenu menuOptions = new JMenu("Options");
			menuOptions.setMnemonic(KeyEvent.VK_T);
			menuOptions.getAccessibleContext().setAccessibleDescription(
					"opTions ");
			// view intial
			JMenuItem menuItemSystemOptions = new JMenuItem("System Options",
					KeyEvent.VK_Y);
			menuItemSystemOptions
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
					if (logger.isDebugEnabled()) {
						//  logger.debug("$java.awt.event.ActionListener.actionPerformed(java.awt.event.ActionEvent) - actionPerformed()   355 in sketchmaster application "); //$NON-NLS-1$
					}
						}
					});
			menuItemSystemOptions.getAccessibleContext()
					.setAccessibleDescription("Main Options");
			menuOptions.add(menuItemSystemOptions);

			// view intial
			JMenuItem menuItemDebugSetting = new JMenuItem("DebuG window",
					KeyEvent.VK_D);
			menuItemDebugSetting
					.addActionListener(new java.awt.event.ActionListener() {

						public void actionPerformed(java.awt.event.ActionEvent e) {
					if (logger.isDebugEnabled()) {
						//  logger.debug("$java.awt.event.ActionListener.actionPerformed(java.awt.event.ActionEvent) - actionPerformed()  debug window  line 335 sketch master application.java"); //$NON-NLS-1$
					}
							if (SystemSettings.DEBUG_MODE) {
								if (DebugWindowCount == 0) {
									if (DebugWindow==null)
									DebugWindow = new DebugMessageWindow();
									DebugWindow.EnableTimer(true);
									DrawingDebugUtils.G_DEBUG=(Graphics2D) DebugWindow.getDrawingSheet().getGraphics();
									DebugWindow.setVisible(true);
									DebugWindowCount = 1;

								} else {
									DebugWindow.EnableTimer(false);
									DebugWindow.setVisible(false);
									DebugWindowCount = 0;
								
									
								}
							}
						}
					});
			menuItemDebugSetting.getAccessibleContext()
					.setAccessibleDescription("Debug window");
			menuOptions.add(menuItemDebugSetting);

			ApplicationMenuBar.add(menuOptions);

			// ///////////////////now adding the recognizier menu

			// recogizer add
			// / recognizer train again
			// recognizer options

			JMenu menuRecognizer = new JMenu("Recognizer Options");
			menuRecognizer.setMnemonic(KeyEvent.VK_R);
			menuRecognizer.getAccessibleContext().setAccessibleDescription(
					"Recognizer Options ");

			// recogizer add

			// ////////////////////////
			JMenuItem menuItemRecognizerAdd = new JMenuItem("Add Category",
					KeyEvent.VK_A);
			menuItemRecognizerAdd
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							// logger.info("actionPerformed()");
							DisplayCategoryAddDialog();
						}
					});
			menuItemRecognizerAdd.getAccessibleContext()
					.setAccessibleDescription("Add a new Category");
			menuRecognizer.add(menuItemRecognizerAdd);

			// / recognizer train again

			JMenuItem menuItemRecognizerTrain = new JMenuItem("Train",
					KeyEvent.VK_T);
			menuItemRecognizerTrain
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
					if (logger.isDebugEnabled()) {
						//  logger.debug("$java.awt.event.ActionListener.actionPerformed(java.awt.event.ActionEvent) - actionPerformed( train  line 395 sketch master application.java )"); //$NON-NLS-1$
					}
						}
					});
			menuItemRecognizerTrain.getAccessibleContext()
					.setAccessibleDescription("Train Recognizer");
			menuRecognizer.add(menuItemRecognizerTrain);

			// recognizer options
			JMenu menuItemRecognizerOptions = new JMenu("Options");

			menuItemRecognizerOptions.getAccessibleContext()
					.setAccessibleDescription("options of  the Recognizer");

			JMenuItem menuItemRecognizeOptionMain = new JMenuItem(
					"Symbol Recognizier", KeyEvent.VK_M);

			menuItemRecognizerOptions.add(getJMenuItemSVM());  // Generated
			menuItemRecognizeOptionMain
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {

							DisplayTrainingSymbolFrame();

							// logger.info("actionPerformed()");
						}
					});

			menuItemRecognizerOptions.add(menuItemRecognizeOptionMain);

			JMenuItem menuItemRecognizeOptionRubine = new JMenuItem(
					"Rubine Recognizier", KeyEvent.VK_R);

			menuItemRecognizeOptionRubine
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {

							DisplayTrainingFrame();

							// logger.info("actionPerformed()");
						}
					});

			menuItemRecognizerOptions.add(menuItemRecognizeOptionRubine);

			menuRecognizer.add(menuItemRecognizerOptions);
			// initMenu();
			ApplicationMenuBar.add(menuRecognizer);
			// ApplicationMenuBar= initMenu();
		}

		return ApplicationMenuBar;
	}

	private void DisplayTrainingSymbolFrame() {
		TrainingFrame frame = new TrainingFrame(this, this.getSystem()
				.getSymbolRecognizier(), TrainingFrame.SYMBOL_RECOGNIZER);
		frame.setVisible(true);
	}
	private void DisplaySVMTrainingFrame(){
		TrainingFrame frame = new TrainingFrame(this, this.getSystem().getSVMRecognizier(), TrainingFrame.SVM_RECOGNIZER);
		frame.setVisible(true);
	}

	private void DisplayTrainingFrame() {

		TrainingFrame frame = new TrainingFrame(this, this.getSystem()
				.getRubineRecognizier(), TrainingFrame.RUBINE_RECOGNIZER);
		frame.setVisible(true);
		// there subposed to be a list of data or stores.

	}

	private void DisplayCategoryAddDialog() {
		CategoryAdd cat = new CategoryAdd(this);

		cat.setVisible(true);

	//	String cateogoryString = cat.getNewCategory();
		// if null then cancel
		// else correct and add cateogry

	}

	private void DisplayLayerDialog() {
		LayerSelectionDialog layD = new LayerSelectionDialog(this, this
				.getSystem().getLayers(), getSystem().getLayerSelections());
		layD.setVisible(true);
		// logger.info("in main ");

		boolean[] selections = layD.getlayersSelections();
		for (int i = 0; i < selections.length; i++) {
			getSystem().setActiveLayer(i, selections[i]);
		}
		this.getDrawSheet().repaint();
		// layD.setVisible(true);
	}

	/**
	 * This method initializes DrawingScrollSheet
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getDrawingScrollSheet() {
		if (DrawingScrollSheet == null) {
			DrawingScrollSheet = new JScrollPane();
			DrawingScrollSheet.setBounds(new Rectangle(6, 148, 586, 437));
			getDrawSheet().setBounds(DrawingScrollSheet.getBounds());
			DrawingScrollSheet.setViewportView(getDrawSheet());
		}
		return DrawingScrollSheet;
	}

	/**
	 * This method initializes DrawSheet
	 * 
	 * @return SketchMaster.gui.DrawingSheet
	 */
	public DrawingSheet getDrawSheet() {
		if (DrawSheet == null) {
			if (SystemSettings.DEBUG_MODE)
			{
				DrawSheet = new DrawingSheetTesting();
			}
			else{
			   DrawSheet = new DrawingSheet();
			}
			DrawSheet.setBorder(null);
			DrawSheet.setBounds(new Rectangle(7, 130, 583, 434));
			DrawSheet.addstrokeListener(new HandleStroke() {

				public void HandleNewStroke(NewStrokeEvent Evt) {
					long time = Evt.getEventStroke().getStrokeTime();
					int noPoints=Evt.getEventStroke().getPointsCount();
					if (time==0&&noPoints==0){
						// let time ==1
						time=1;
					}
					
					UpdateLabels(time,noPoints);
					
				}

			
			});
			
		}
		return DrawSheet;
	}
	private void UpdateLabels(long time, int points) {
		NoStrokes++;
		
		
		 this.jLabelnStroke.setText(""+NoStrokes);
		 this.jLabelPStroke.setText(""+points);
		 this.jLabelTStroke.setText(""+time);
		 if (time==0&& points==0){
			 NoStrokes=0;
				// i did clear that mean  "  " in the text label s
				 this.jLabelnStroke.setText("" );
				 this.jLabelPStroke.setText("" );
				 this.jLabelTStroke.setText("" );
			}
	}
	
	
	public class SketchFileFilter extends javax.swing.filechooser.FileFilter {
		public boolean accept(File file) {
			// Convert to lower case before checking extension
			return file.getName().toLowerCase().endsWith(".smd")
					|| file.getName().toLowerCase().endsWith(".SDM");
		}

		public String getDescription() {
			return "Sketch Master Data (*.smd)";
		}
	}

	/**
	 * This method initializes jClusterButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJClusterButton() {
		if (jClusterButton == null) {
			jClusterButton = new JButton();
			jClusterButton.setBounds(new Rectangle(423, 75, 122, 28));
			jClusterButton.setText("Finish Symbol");
			jClusterButton
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							getSystem().CreateCluster();
							// logger.info("actionPerformed()");

						}
					});
		}
		return jClusterButton;
	}
	private void clearAll(){
		
		this.system.clearAllSketch();
		
		UpdateLabels(0,0);
	}
	/**
	 * This method initializes jButtonClear	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonClear() {
		if (jButtonClear == null) {
			jButtonClear = new JButton();
			jButtonClear.setBounds(new Rectangle(319, 76, 93, 25));  // Generated
			jButtonClear.setText("Clear ");  // Generated
			jButtonClear.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					
					clearAll();
				}
			});
		}
		return jButtonClear;
	}

	/**
	 * This method initializes jMenuItemSVM	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getJMenuItemSVM() {
		if (jMenuItemSVM == null) {
			jMenuItemSVM = new JMenuItem();
			jMenuItemSVM.setText("SVM trainner");
			jMenuItemSVM.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					//logger.info("actionPerformed()");  
					
					DisplaySVMTrainingFrame();
					
				}
			});
		}
		return jMenuItemSVM;
	}

} // @jve:decl-index=0:visual-constraint="10,10"
