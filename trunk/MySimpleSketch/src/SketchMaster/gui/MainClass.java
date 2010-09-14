package SketchMaster.gui;

import org.apache.log4j.Logger;

import java.awt.BorderLayout;
import java.awt.Label;

import javax.swing.JFrame;

import SketchMaster.io.log.FileLog;
import SketchMaster.system.SystemSettings;

/**
 * <p>
 * Title: SketchMasterProject
 * </p>
 * 
 * <p>
 * Description: sketch recognition
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * 
 * <p>
 * Company: engineer
 * </p>
 * 
 * @author maha el meseery
 * @version 1.0
 */
public class MainClass {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(MainClass.class);

	private static Label emptyLabel = null;

	public MainClass() {
		super();
		try {
			jbInit();
		} catch (Exception ex) {
			logger.error("MainClass()", ex); //$NON-NLS-1$
		}
	}
	public static void RunApplication(){
		FileLog.createALogFile();
		org.apache.log4j.PropertyConfigurator.configure("log4j.properties");
		//  logger.trace("Start program");

		//  logger.trace(" before sketch");
	//	MainClass main = new MainClass();

		emptyLabel = new Label("Draw what you want ");
		// 1. Optional: Specify who draws the window decorations.
		// SketchFrame.setDefaultLookAndFeelDecorated(true);
		SystemSettings.MODE=SystemSettings.MODE_NORMAL;
		// 2. Create the frame.
		SketchMaterApplication frame = new SketchMaterApplication();

		// 3. Optional: What happens when the frame closes?
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// 4. Create components and put them in the frame.
		// ...create emptyLabel...
		// frame.getContentPane().add(emptyLabel, BorderLayout.CENTER);

		// 5. Size the frame.
		// / frame.pack();
		// frame.getContentPane().setLayout(null);

		// frame.setSize(1000, 1000);
		// 6. Show it.

		// frame.init();
		// frame.addlistnerToFrame();

		frame.setVisible(true);

	}

	public static void main(String[] args) {
		
		RunApplication();
		
//		FileLog.createALogFile();
//		//  logger.trace("Start program");
//
//		//  logger.trace(" before sketch");
//		MainClass main = new MainClass();
//
//		emptyLabel = new Label("Draw what you want ");
//		// 1. Optional: Specify who draws the window decorations.
//		// SketchFrame.setDefaultLookAndFeelDecorated(true);
//
//		// 2. Create the frame.
//		SketchMaterApplication frame = new SketchMaterApplication();
//
//		// 3. Optional: What happens when the frame closes?
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//		// 4. Create components and put them in the frame.
//		// ...create emptyLabel...
//		// frame.getContentPane().add(emptyLabel, BorderLayout.CENTER);
//
//		// 5. Size the frame.
//		// / frame.pack();
//		// frame.getContentPane().setLayout(null);
//
//		// frame.setSize(1000, 1000);
//		// 6. Show it.
//
//		// frame.init();
//		// frame.addlistnerToFrame();
//
//		frame.setVisible(true);

	}

	private void jbInit() throws Exception {
	}

}
