/**
 * 
 */
package SketchMaster.io;

import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

import SketchMaster.io.log.FileLog;

/**
 * @author maha
 * 
 */
public class FileObjectReader {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(FileObjectReader.class);

	// File StrokeFile;
	FileInputStream f_in;

	ObjectInputStream obj_in;

	/**
	 * 
	 */
	public FileObjectReader() {

	}

	public void closeRead() {
		try {
			f_in.close();
		} catch (IOException e) {

			logger.error("closeRead()", e); //$NON-NLS-1$
		}
	}

	public boolean readFile(String FileName) {
		try {
			f_in = new FileInputStream(FileName);

		} catch (FileNotFoundException e) {

			logger.error("readFile(String)", e); //$NON-NLS-1$
			return false;
		}

		return true;
	}

	public Object ReadObject() {
		Object obj = null;

		try {

			obj_in = new ObjectInputStream(f_in);

			obj = obj_in.readObject();
			//  logger.trace("REading ");

		} catch (FileNotFoundException e) {

			logger.error("ReadObject()", e); //$NON-NLS-1$
		} catch (IOException e) {

			// System.out.println("in exception");
			logger.error("ReadObject()", e); //$NON-NLS-1$

		} catch (ClassNotFoundException e) {
			logger.error("ReadObject()", e); //$NON-NLS-1$
		} catch (Exception e) {
			logger.error("ReadObject()", e); //$NON-NLS-1$
		}

		return obj;
	}

	public ObjectInputStream getOpenedObjectStream() {
		try {

			// Write object with ObjectOutputStream
			obj_in = new ObjectInputStream(f_in);

			// Write object out to disk

			return obj_in;

		} catch (FileNotFoundException e) {

			logger.error("getOpenedObjectStream()", e); //$NON-NLS-1$
		} catch (IOException e) {

			logger.error("getOpenedObjectStream()", e); //$NON-NLS-1$
		}
		return null;
	}

}
