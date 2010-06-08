/**
 * 
 */
package SketchMaster.io;

import org.apache.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import SketchMaster.Stroke.StrokeData.Stroke;
import SketchMaster.io.log.FileLog;

/**
 * @author maha
 * 
 */
public class FileObjectWriter {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(FileObjectWriter.class);

	FileOutputStream f_out;

	ObjectOutputStream obj_out;

	/**
	 * 
	 */
	public FileObjectWriter() {

	}

	public void createFile(String string) {

		try {
			// Write to disk with FileOutputStream
			f_out = new FileOutputStream(string);

		} catch (FileNotFoundException e) {

			logger.error("createFile(String)", e); //$NON-NLS-1$
		}
	}

	public void closeWrite() {
		try {
			f_out.close();
		} catch (IOException e) {

			logger.error("closeWrite()", e); //$NON-NLS-1$
		}
	}

	public void WriteObject(Object obj) {
		try {

			// Write object with ObjectOutputStream
			obj_out = new ObjectOutputStream(f_out);

			// Write object out to disk
			obj_out.writeObject(obj);
			//  logger.trace("writing the object");

		} catch (FileNotFoundException e) {

			logger.error("WriteObject(Object)", e); //$NON-NLS-1$
		} catch (IOException e) {

			logger.error("WriteObject(Object)", e); //$NON-NLS-1$
		}

		// if nothing go wrongs then i will wrie all the date in the stroke
	}

	public ObjectOutputStream getOpenedObjectStream() {
		try {

			// Write object with ObjectOutputStream
			obj_out = new ObjectOutputStream(f_out);

			// Write object out to disk

			return obj_out;

		} catch (FileNotFoundException e) {

			logger.error("getOpenedObjectStream()", e); //$NON-NLS-1$
		} catch (IOException e) {

			logger.error("getOpenedObjectStream()", e); //$NON-NLS-1$
		}
		return null;
	}

}
