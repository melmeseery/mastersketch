/**
 * 
 */
package SketchMaster.io;

import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import SketchMaster.Stroke.StrokeData.Stroke;

/**
 * @author maha
 * 
 */
public class StrokeReaderWriter {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(StrokeReaderWriter.class);

	// File StrokeFile;
	FileInputStream f_in;
	FileOutputStream f_out;
	ObjectInputStream obj_in;

	/**
	 * 
	 */
	public StrokeReaderWriter() {

	}

	public Stroke ReadStroke() {
		Stroke st = null;
		// Read from disk using FileInputStream
		// FileInputStream f_in;
		try {
			// f_in = new
			// FileInputStream("myStroke.data");

			obj_in = new ObjectInputStream(f_in);

			// Read an object
			Object obj = obj_in.readObject();

			if (obj instanceof Stroke) {
				// Cast object to a Vector
				st = (Stroke) obj;

				// Do something with vector....
			}
		} catch (FileNotFoundException e) {
			// e.printStackTrace();
		} catch (IOException e) {

			// e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// e.printStackTrace();
		} catch (Exception e) {
			// e.printStackTrace();
		}

		return st;

	}

	public void writeStrokeCounts(int [] counts){
		try{
		// Write object with ObjectOutputStream
		ObjectOutputStream obj_out = new ObjectOutputStream(f_out);

		// Write object out to disk
		obj_out.writeObject(counts);

		obj_out.flush();

		f_out.flush();

	} catch (FileNotFoundException e) {

		logger.error("WriteStroke(count)", e); //$NON-NLS-1$
	} catch (IOException e) {

		logger.error("WriteStroke(count)", e); //$NON-NLS-1$
	}

	// if nothing go wrongs then i will wrie all the date in the stroke
	}
	public int[] readStrokeCounts(){
		int[] count=null;
		
		Stroke st = null;
		// Read from disk using FileInputStream
		// FileInputStream f_in;
		try {
			// f_in = new
			// FileInputStream("myStroke.data");

			obj_in = new ObjectInputStream(f_in);

			// Read an object
			Object obj = obj_in.readObject();

			if (obj instanceof int[]) {
				// Cast object to a Vector
				count = (int[]) obj;

				// Do something with vector....
			}
		} catch (FileNotFoundException e) {
			// e.printStackTrace();
		} catch (IOException e) {

			// e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// e.printStackTrace();
		} catch (Exception e) {
			// e.printStackTrace();
		}

		return count;
		
		//return null;
	}
	
	public void WriteStroke(Stroke stroke) {
		try {

			// Write object with ObjectOutputStream
			ObjectOutputStream obj_out = new ObjectOutputStream(f_out);

			// Write object out to disk
			obj_out.writeObject(stroke);

			obj_out.flush();

			f_out.flush();

		} catch (FileNotFoundException e) {

			logger.error("WriteStroke(Stroke)", e); //$NON-NLS-1$
		} catch (IOException e) {

			logger.error("WriteStroke(Stroke)", e); //$NON-NLS-1$
		}

		// if nothing go wrongs then i will wrie all the date in the stroke
	}

	public boolean readFile(String FileName) {
		try {
			f_in = new FileInputStream(FileName);
			// obj_in =
			// new ObjectInputStream (f_in);
			//			

		} catch (FileNotFoundException e) {

			logger.error("readFile(String)", e); //$NON-NLS-1$
			return false;
		} catch (IOException e) {
			logger.error("readFile(String)", e); //$NON-NLS-1$
		}
		return true;
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

	public void closeRead() {
		try {
			f_in.close();
		} catch (IOException e) {

			logger.error("closeRead()", e); //$NON-NLS-1$
		}
	}
}
