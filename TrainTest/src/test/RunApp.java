package test;
/**
 * 
 */




import java.io.File;

import org.apache.log4j.Logger;

import SketchMaster.system.SystemSettings;

import settings.PatchTestSet;
import settings.TestResults;
import settings.TestSketchSetting;

/**
 * @author Maha
 *
 */
public class RunApp {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(RunApp.class);

	
	
	
	
	/**TrainTest
	 * @param args
	 */
	public static void main(String[] args) {
		if (logger.isDebugEnabled()) {
			//  logger.debug("main(String[]) - start"); //$NON-NLS-1$
			
			
		}
//		File temp=new File("");
//		System.out.println(temp.getAbsolutePath());

		  
		
		
		org.apache.log4j.PropertyConfigurator.configure("log4j.properties");
		
		PatchTestSet temp=new PatchTestSet();
		
		temp.run();
	 
	 
		//------------------------------------------to run a patch test train 
		
	

	}

	
	
	
	
	
	
}
