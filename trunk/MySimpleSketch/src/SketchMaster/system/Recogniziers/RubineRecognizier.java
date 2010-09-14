/**
 * 
 */
package SketchMaster.system.Recogniziers;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import org.apache.log4j.Logger;

import SketchMaster.Stroke.StrokeData.InkInterface;
import SketchMaster.Stroke.features.StrokeRubineFeatureSet;
import SketchMaster.classifier.Classification;
import SketchMaster.classifier.rubine.RubineCategory;
import SketchMaster.classifier.rubine.RubineClassifier;
import SketchMaster.classifier.rubine.RubineTrainingSet;
import SketchMaster.gui.GraphWatched;
import SketchMaster.gui.Events.HandleStroke;
import SketchMaster.gui.Events.NewStrokeEvent;
import SketchMaster.io.FileObjectReader;
import SketchMaster.io.FileObjectWriter;
import SketchMaster.io.log.FileLog;
import SketchMaster.swarm.Solution;
import SketchMaster.system.SystemSettings;

/**
 * @author maha
 * 
 */
public class RubineRecognizier extends RecognizierSystem {
	private static final Logger logger = Logger.getLogger(RubineRecognizier.class);
	// / the classifier defaults
	public final static int RUBINE_LOAD_FILE = 1;
	public final static int RUBINE_LOAD_SERIALIZE = 2;
	public final static int RUBINE_LOAD_SYS = 3;

	private RubineTrainingSet trainer = null;
	private StrokeRubineFeatureSet test;
	private RubineClassifier Rubclassifier = null;

	// operation
	public final static int Rubine_OPERATION_TRAINING = 0;
	public final static int Rubine_OPERATION_CLASSIFICATION = 1;

	private RubineCategory currentCategory;

	private InkInterface LastStrokeDrawn = null;

	// private currentExampleviewed=-1;
	public void init() {
		Rubclassifier = new RubineClassifier();
		Logger appLogger = Logger.getLogger("AppLogging");
		appLogger.info(" [Settings] indside rubine settings is "+SystemSettings.getString());
		// logger.info("look ");
	//	if (SystemSettings.RubineDefaultLoadOption == RUBINE_LOAD_SYS) {
			trainer = new RubineTrainingSet();
			//  logger.trace("initailizing  the  rubine trainer ");
			trainer.init();
			// logger.info("init both classifier and rubine");
			// Rubclassifier.init();
			Rubclassifier.init(trainer);
//			//  logger.trace("initailizing  the  classifier trainer ");
//		} else {
//
//			// load from file system
//
//			trainer = new RubineTrainingSet();
//			trainer.init();
//			// set the trainig set to the classifier
//			Rubclassifier.init(trainer);
//		}

	}

	public void train() {
		logger.info("############### Start training calcualtions ###########################################");
		trainer.trainAll();
		logger.info("############### Finished training calcualtions ###########################################");

	}

	public void addNewCategory(String Name) {
		RubineCategory cat = new RubineCategory();
		cat.setCategoryName(Name);
		cat.init();
		trainer.add(cat);
		setCurrentSelectedCategoryName(Name);
		currentCategory = cat;
		CurrentCat = trainer.size() - 1;// id

	}

	public void addCategoryExample(int i, Object ink) {
		if (trainer.size() < i) {
			// then this category does t exist
			// i will need to add it. till we
			for (int j = i; j < trainer.size(); j++) {
				this.addNewCategory("category " + j);
			}
			// logger.info(" sss ");
		}

		trainer.addCategoryExample(i, (InkInterface) ink);
	}

	public void addCategoryExample(String Name, InkInterface ink) {
		trainer.addCategoryExample(Name, ink);
	}

	public int deleteCategoryExample(String Name, int i) {
		return trainer.deleteCategoryExample(Name, i);
	}

	public void deleteCategory(String Name) {
		trainer.deleteCategory(Name);
		return;
	}

	public String[] getCategoryNames() {
		return trainer.getCategoryNames();
	}

	public ArrayList getCategoryExamples(String Name) {
		return trainer.getCategoryExamples(Name);

	}

	public ArrayList getCategoryExamples(int Name) {
		return trainer.getCategoryExamples(Name);
	}

	public ArrayList getCategoryExamplesInk(String Name) {
		return trainer.getCategoryExamplesInk(Name);

	}

	public void HandleNewStroke(NewStrokeEvent Evt) {

		// logger.info("===================================================================");
		LastStrokeDrawn = Evt.getEventStroke();

		// either classify the stroke
		if (SystemSettings.CurrentRubineOperation == Rubine_OPERATION_CLASSIFICATION) {
			CurrentClassification = Rubclassifier
					.Classify(Evt.getEventStroke());

		}
		// or add the stroke to the trining set
		if (SystemSettings.CurrentRubineOperation == Rubine_OPERATION_TRAINING) {
			if (CaptureStrokes) {
				// 
				// logger.info("adding an example to "+CurrentCat);
				addCategoryExample(CurrentCat, Evt.getEventStroke());
				// CaptureStrokes=false;
			}

		}

		setChanged();
		notifyObservers();

	}

	public String getFeatureString() {
		// if (CaptureStrokes)

		// {
		if (LastStrokeDrawn != null) {
			StrokeRubineFeatureSet temp = new StrokeRubineFeatureSet(
					LastStrokeDrawn);
			temp.initAll();
			temp.computeFeatures();
			if (SystemSettings.CurrentRubineOperation == Rubine_OPERATION_TRAINING) {
				return temp.toString();
			} else {
				String tempx = "  ";
				if (CurrentClassification != null)
					tempx += CurrentClassification.toString();

				// tempx+=temp.toString();
				return (tempx);
			}

		}

		return "";
	}

	/**
	 * @param currentSelectedCategoryName
	 *            the currentSelectedCategoryName to set
	 */
	public void setCurrentSelectedCategoryName(
			String currentSelectedCategoryName) {
		CurrentSelectedCategoryName = currentSelectedCategoryName;
		currentCategory = trainer.getCategory(CurrentSelectedCategoryName);
		// logger.info(" selecting " + CurrentSelectedCategoryName);
	}

	public int getCurrentExamplesCount() {

		if (currentCategory != null)
			return currentCategory.getExampleCount();
		else
			return getCategoryExamples(CurrentSelectedCategoryName).size();// takes
																			// more
																			// time
	}

	public void update(Observable arg0, Object arg1) {

		if (arg0 instanceof GraphWatched) {
			GraphWatched temp = (GraphWatched) arg0;
			if (LastStrokeDrawn != null)
				LastStrokeDrawn.paint(temp.getG());
		}

	}

	public void Clear() {

		LastStrokeDrawn = null;
	}

	// /**
	// * @return the currentClassification
	// */
	// public Classification getCurrentClassification() {
	// return CurrentClassification;
	// }

	public void SaveTrainingSet(String FileName) {

		if (trainer != null) {
			trainer.SaveData();
			// first create the file object that will rite the file

			FileObjectWriter fileWriter = new FileObjectWriter();
			fileWriter.createFile(FileName);
			fileWriter.WriteObject(trainer);
			fileWriter.closeWrite();

		}

	}

	public void ReadTrainingSet(String FileName) {
		// set current to null
		trainer = null;
		FileObjectReader fileReader = new FileObjectReader();
		fileReader.readFile(FileName);
		Object o = fileReader.ReadObject();
		if (o instanceof RubineTrainingSet) {
			trainer = (RubineTrainingSet) o;

			trainer.ReadData();
			Rubclassifier.init(trainer);
		}
		fileReader.closeRead();

	}
	
	
	public void checkAddCategory(String name) {
		
		if (!trainer.CategoryExist(name))
		{
		 addNewCategory(name);	
		}
		else 
		{
			//currentCategory=trainer.getCategory(name);
			CurrentCat = trainer.getCategoryID(name);
		}
			
		}

	@Override
	public int getCategoryCount() {
		
		return trainer.size();
	}

}
