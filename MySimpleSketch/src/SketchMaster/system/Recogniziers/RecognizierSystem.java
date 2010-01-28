package SketchMaster.system.Recogniziers;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import SketchMaster.Stroke.StrokeData.InkInterface;
import SketchMaster.classifier.Classification;
import SketchMaster.classifier.rubine.RubineTrainingSet;
import SketchMaster.gui.DrawingSheet;
import SketchMaster.gui.GraphWatched;
import SketchMaster.gui.Events.HandleStroke;
import SketchMaster.io.FileObjectReader;
import SketchMaster.io.FileObjectWriter;

abstract public class RecognizierSystem extends Observable implements Observer,
		HandleStroke {
	protected Classification CurrentClassification = null;

	protected String FeatureString;
 
	protected int CurrentCat = 0;

	protected String CurrentSelectedCategoryName = "";
	// / the classifier defaults
	public final static int RECOGNIZE_LOAD_FILE = 1;
	public final static int RECOGNIZE_LOAD_SERIALIZE = 2;
	public final static int RECGONIZE_LOAD_SYS = 3;
	public final static int RECGONIZE_OPERATION_TRAIN = 4;
	public final static int RECGONIZE_OPERATION_TEST = 5;
	public final static int RECGONIZE_OPERATION_CLASSIFY = 6;
	public final static int  EXAMPLE_NEG=0;
	public final static int  EXAMPLE_POS=0;
	public static int ExmapleType=EXAMPLE_POS;
		

	/**
	 * @param currentCat
	 *            the currentCat to set
	 */
	public void setCurrentCat(int currentCat) {
		CurrentCat = currentCat;
	}

	public abstract void train();

	public abstract void addNewCategory(String Name);

	public abstract void addCategoryExample(int i, Object ink);

	// public abstract void addCategoryExample(String Name,InkObject ink);

	public abstract int deleteCategoryExample(String Name, int i);

	public abstract void deleteCategory(String Name);
 
	public abstract int getCategoryCount();
	public abstract String[] getCategoryNames();

	// public abstract ArrayList getCategoryExamples(String Name);
	public abstract ArrayList getCategoryExamples(int Name);

	public abstract ArrayList getCategoryExamplesInk(String Name);

	public abstract String getFeatureString();

	protected boolean CaptureStrokes = false;

	public void setCapturesStroke(boolean b) {

		CaptureStrokes = b;
	}

	public boolean getCapturesStroke() {
		return CaptureStrokes;

	}

	/**
	 * @param currentSelectedCategoryName
	 *            the currentSelectedCategoryName to set
	 */
	public abstract void setCurrentSelectedCategoryName(
			String currentSelectedCategoryName);

	public abstract int getCurrentExamplesCount();

	/**
	 * @return the currentClassification
	 */
	public Classification getCurrentClassification() {
		return CurrentClassification;
	}

	public abstract void SaveTrainingSet(String FileName);

	public abstract void ReadTrainingSet(String FileName);

	public abstract void init();
	public void setDataDisplay(DrawingSheet drawSheet){

	}

	public abstract void Clear();

	public void checkAddCategory(String Name) {
	
		addNewCategory(Name);
	}

	public void setExampleType(int selected) {
	 
		this.ExmapleType=selected;
		
	}

}
