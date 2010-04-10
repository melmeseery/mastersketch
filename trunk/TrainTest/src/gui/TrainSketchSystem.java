package gui;

import javax.swing.SwingUtilities;
import java.awt.BorderLayout;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.GridBagLayout;
import javax.swing.JButton;

import org.apache.log4j.Logger;

import test.TrainTest;

import java.awt.GridBagConstraints;
import java.io.File;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JProgressBar;
import javax.swing.JLabel;
import java.awt.Dimension;

/**
 * 
 */

/**
 * @author Maha
 *
 */
public class TrainSketchSystem extends JFrame implements Observer{
	private static final Logger logger = Logger.getLogger(TrainSketchSystem.class);
	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JButton jButtonStopSave = null;
	private JButton jButtonStartAgain = null;
	private JButton jButtonRestart = null;
	 TrainTest train;  //  @jve:decl-index=0:
	private JProgressBar jProgressBarCategory = null;
	private JProgressBar jProgressBarFiles = null;
	private JProgressBar jProgressBarExamples = null;
	private JLabel jLabelFiles = null;
	private JLabel jLabelSpace = null;
	private JLabel jLabelCategory = null;
	private JLabel jLabelExamples = null;
	private JLabel jLabelCategoryname = null;
	
	 Thread thread;
	private JLabel jLabelMaxExamples = null;
	private JLabel jLabelMaxCat = null;
	private JLabel jLabelMaxFile = null;
	private JButton jButtonTest = null;

	/**
	 * This method initializes jButtonStopSave	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonStopSave() {
		if (jButtonStopSave == null) {
			jButtonStopSave = new JButton();
			jButtonStopSave.setText("Pause Training");  // Generated
			jButtonStopSave.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					train.setPauseAndSave(true);
					 thread.interrupt();
				}
			});
			
		}
		return jButtonStopSave;
	}

	/**
	 * This method initializes jButtonStartAgain	
	 * 	
	 * @return javax.swing.JButton	
	 */ 
	private JButton getJButtonStartAgain() {
		if (jButtonStartAgain == null) {
			jButtonStartAgain = new JButton();
			jButtonStartAgain.setText("   Start Train    ");  // Generated
			jButtonStartAgain.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
				//	System.out.println("actionPerformed()"); 
					train.setRunMode(0);
					train.setPrevTrain(0);
					 train.initSystem();
				
					 setProgressBars();
					thread=new Thread(train);
					 thread.start();
			
					 
				      //train.TrainFiles();
				      
				      
				      
				     logger.info("finished "+" ("  + "    "
							+ (new Throwable()).getStackTrace()[0].getLineNumber() + "  )  ");

						if (logger.isDebugEnabled()) {
							//  logger.debug("main(String[]) - end"); //$NON-NLS-1$
						}
				}
			});
		}
		return jButtonStartAgain;
	}

	public void setProgressBars(){
		 train.addObserver(this);
		this.getJProgressBarFiles().setMaximum(train.getNoOfRunFiles()-1);
		jLabelMaxFile.setText(""+train.getNoOfRunFiles());
		this.getJProgressBarFiles().setMaximum(0);
		getJProgressBarCategory().setMaximum(30);
		getJProgressBarCategory().setMinimum(0);
		getJProgressBarExamples().setMaximum(30);
		getJProgressBarExamples().setMinimum(0);
		getJProgressBarFiles().setStringPainted(true);
		getJProgressBarCategory().setStringPainted(true);
		getJProgressBarExamples().setStringPainted(true);
	}
	
	/**
	 * This method initializes jButtonRestart	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonRestart() {
		if (jButtonRestart == null) {
			jButtonRestart = new JButton();
			jButtonRestart.setText(" Restart Again ");  // Generated
			jButtonRestart.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					JFileChooser fc = new JFileChooser(new File("."));
					if (fc.showOpenDialog(getJContentPane().getParent()) == JFileChooser.APPROVE_OPTION) { // .showOpenDialog(this);
						{
							
							train.setPrevTrain(1);
							train.setRunMode(0);
							
							train.setTrainingFileName(	fc.getSelectedFile().getAbsolutePath());
							 train.initSystem();
						      train.TrainFiles();
						     logger.info("finished "+" ("  + "    "
									+ (new Throwable()).getStackTrace()[0].getLineNumber() + "  )  ");

								if (logger.isDebugEnabled()) {
									//  logger.debug("main(String[]) - end"); //$NON-NLS-1$
								}
						}
							
						}
						
					
					
					
					
				}
			});
		}
		return jButtonRestart;
	}

	/**
	 * This method initializes jProgressBarCategory	
	 * 	
	 * @return javax.swing.JProgressBar	
	 */
	private JProgressBar getJProgressBarCategory() {
		if (jProgressBarCategory == null) {
			jProgressBarCategory = new JProgressBar();
		}
		return jProgressBarCategory;
	}

	/**
	 * This method initializes jProgressBarFiles	
	 * 	
	 * @return javax.swing.JProgressBar	
	 */
	private JProgressBar getJProgressBarFiles() {
		if (jProgressBarFiles == null) {
			jProgressBarFiles = new JProgressBar();
		}
		return jProgressBarFiles;
	}

	/**
	 * This method initializes jProgressBarExamples	
	 * 	
	 * @return javax.swing.JProgressBar	
	 */
	private JProgressBar getJProgressBarExamples() {
		if (jProgressBarExamples == null) {
			jProgressBarExamples = new JProgressBar();
		}
		return jProgressBarExamples;
	}

	/**
	 * This method initializes jButtonTest	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonTest() {
		if (jButtonTest == null) {
			jButtonTest = new JButton();
			jButtonTest.setText("Run Test");  // Generated
			jButtonTest.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					
					JFileChooser fc = new JFileChooser(new File("."));
					if (fc.showOpenDialog(getJContentPane().getParent()) == JFileChooser.APPROVE_OPTION) { // .showOpenDialog(this);
						
						
							train.setPrevTrain(1);
							train.setTrainingFileName(	fc.getSelectedFile().getAbsolutePath());
							train.setRunMode(1);
							 train.initSystem();
							 setProgressBars();
							thread=new Thread(train);
							 thread.start();
							 
					
					}   //if 
				
			}//function 
				
			
			
		}); 
		}
		return jButtonTest;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		  
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				TrainSketchSystem thisClass = new TrainSketchSystem();
				thisClass.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				thisClass.setVisible(true);
			}
		});
	}

	/**
	 * This is the default constructor
	 */
	public TrainSketchSystem() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(608, 280);
		this.setContentPane(getJContentPane());
		this.setTitle("JFrame");
		org.apache.log4j.PropertyConfigurator.configure("log4j.properties");
		 train=new TrainTest();
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
			gridBagConstraints13.gridx = 0;  // Generated
			gridBagConstraints13.gridy = 7;  // Generated
			GridBagConstraints gridBagConstraints31 =new GridBagConstraints();
			gridBagConstraints31.gridx = 4;  // Generated
			gridBagConstraints31.gridy = 1;  // Generated
			jLabelMaxFile = new JLabel();
			jLabelMaxFile.setText("MaxFiles");  // Generated
			GridBagConstraints gridBagConstraints21 =new GridBagConstraints() ;
			gridBagConstraints21.gridx = 4;  // Generated
			gridBagConstraints21.gridy = 3;  // Generated
			jLabelMaxCat = new JLabel();
			jLabelMaxCat.setText("MaxCat");  // Generated
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.gridx = 4;  // Generated
			gridBagConstraints12.gridy = 5;  // Generated
			jLabelMaxExamples = new JLabel();
			jLabelMaxExamples.setText("Maxexample");  // Generated
			GridBagConstraints gridBagConstraints9 =  new GridBagConstraints();
			gridBagConstraints9.gridx = 4;  // Generated
			gridBagConstraints9.gridy = 2;  // Generated
			jLabelCategoryname = new JLabel();
			jLabelCategoryname.setText("Category name under study");  // Generated
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.gridx = 3;  // Generated
			gridBagConstraints8.gridy = 4;  // Generated
			jLabelExamples = new JLabel();
			jLabelExamples.setText("Examples ");  // Generated
			GridBagConstraints gridBagConstraints7 =new GridBagConstraints() ;
			gridBagConstraints7.gridx = 3;  // Generated
			gridBagConstraints7.gridy = 2;  // Generated
			jLabelCategory = new JLabel();
			jLabelCategory.setText("Category");  // Generated
			GridBagConstraints gridBagConstraints6 =new GridBagConstraints() ;
			gridBagConstraints6.gridx = 1;  // Generated
			gridBagConstraints6.gridy = 3;  // Generated
			jLabelSpace = new JLabel();
			jLabelSpace.setText("                ");  // Generated
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.gridx = 3;  // Generated
			gridBagConstraints5.gridy = 0;  // Generated
			jLabelFiles = new JLabel();
			jLabelFiles.setText("Files progress");  // Generated
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints() ;
			gridBagConstraints4.gridx = 3;  // Generated
			gridBagConstraints4.gridy = 5;  // Generated
			GridBagConstraints gridBagConstraints3 =  new GridBagConstraints() ;
			gridBagConstraints3.gridx = 3;  // Generated
			gridBagConstraints3.gridy = 1;  // Generated
			GridBagConstraints gridBagConstraints2 =  new GridBagConstraints() ;
			gridBagConstraints2.gridx = 3;  // Generated
			gridBagConstraints2.gridy = 3;  // Generated
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints()  ;
			gridBagConstraints11.gridx = 0;  // Generated
			gridBagConstraints11.gridy = 5;  // Generated
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints() ;
			gridBagConstraints1.gridx = 0;  // Generated
			gridBagConstraints1.gridy = 1;  // Generated
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;  // Generated
			gridBagConstraints.gridwidth = 1;  // Generated
			gridBagConstraints.gridy = 3;  // Generated
			jContentPane = new JPanel();
			jContentPane.setLayout(new GridBagLayout());
			jContentPane.add(getJButtonStopSave(), gridBagConstraints);  // Generated
			jContentPane.add(getJButtonStartAgain(), gridBagConstraints1);  // Generated
			jContentPane.add(getJButtonRestart(), gridBagConstraints11);  // Generated
			jContentPane.add(getJProgressBarCategory(), gridBagConstraints2);  // Generated
			jContentPane.add(getJProgressBarFiles(), gridBagConstraints3);  // Generated
			jContentPane.add(getJProgressBarExamples(), gridBagConstraints4);  // Generated
			jContentPane.add(jLabelFiles, gridBagConstraints5);  // Generated
			jContentPane.add(jLabelSpace, gridBagConstraints6);  // Generated
			jContentPane.add(jLabelCategory, gridBagConstraints7);  // Generated
			jContentPane.add(jLabelExamples, gridBagConstraints8);  // Generated
			jContentPane.add(jLabelCategoryname, gridBagConstraints9);  // Generated
			jContentPane.add(jLabelMaxExamples, gridBagConstraints12);  // Generated
			jContentPane.add(jLabelMaxCat, gridBagConstraints21);  // Generated
			jContentPane.add(jLabelMaxFile, gridBagConstraints31);  // Generated
			jContentPane.add(getJButtonTest(), gridBagConstraints13);  // Generated
		}
		return jContentPane;
	}

	public void update(Observable ob, Object arg1) {
		if (ob instanceof TrainTest) {
			TrainTest temp = (TrainTest) ob;
			// set current file 
			getJProgressBarFiles().setValue(temp.getCurrentFile());
			getJProgressBarFiles().setString(""+temp.getCurrentFile());
			
			//get category 
			
			  getJProgressBarCategory().setMaximum(temp.getMaxCurrentCat()-1);
			 jLabelMaxCat.setText(""+temp.getMaxCurrentCat());
			// update the progress bars
		   getJProgressBarCategory().setValue(temp.getCurrentCat());
		   getJProgressBarCategory().setString(""+temp.getCurrentCat());
		   jLabelCategoryname.setText(temp.getCurrentCatString());
		   
		   // now for example 
		   
		   getJProgressBarExamples().setMaximum(temp.getMaxCurExample()-1);
		   getJProgressBarExamples().setValue(temp.getCurrentExample());
		   getJProgressBarExamples().setString(""+temp.getCurrentExample());
		   jLabelMaxExamples.setText(""+temp.getMaxCurExample());
			
		}
		
	}



}  //  @jve:decl-index=0:visual-constraint="10,10"
