/**
 * 
 */
package SketchMaster.io.log;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author Mahi
 */
public class FileLog {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(FileLog.class);

	static FileWriter logFile;

	static String logString;

	/**
	 * 
	 */
	public FileLog() {

	}

	public static void createALogFile() {
		try {
			logFile = new FileWriter(new File("log.txt"));
			logString = "";
		} catch (IOException e) {

			logger.error("createALogFile()", e); //$NON-NLS-1$
			logString += e.getMessage();
		}
	}

	public static void createALogFile(String FileName) {
		try {
			logFile = new FileWriter(new File(FileName));
			logString = "";
		} catch (IOException e) {

			logger.error("createALogFile(String)", e); //$NON-NLS-1$
			logString += e.getMessage();
		}
	}

	public static void addString(String line) {
		// try {
		// logFile.write(line);
		//
		// logFile.write(" \n\n");
		logString += line;
		logString += "\n";
		// }
		// catch (IOException e) {

		// e.printStackTrace();
		// logString += e.getMessage();
		// }
	}

	/**
	 * @return the logString
	 * @uml.property name="logString"
	 */
	public static String getLogString() {
		return logString;
	}

	public static void closeFile() {
		try {
			logFile.write(logString);
			logFile.close();
		} catch (IOException e) {

			logger.error("closeFile()", e); //$NON-NLS-1$
			logString += e.getMessage();
		}
	}

	// / //  logger.trace("\n----------------------------------------------");
	// / //  logger.trace("");
}
