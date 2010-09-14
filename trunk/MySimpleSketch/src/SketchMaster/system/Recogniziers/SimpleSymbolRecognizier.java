package SketchMaster.system.Recogniziers;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Observable;

import SketchMaster.Stroke.StrokeData.InkInterface;
import SketchMaster.Stroke.StrokeData.SegmentCluster;
import SketchMaster.Stroke.StrokeData.Stroke;
import SketchMaster.Stroke.features.SegmentClusterFeatureSet;
import SketchMaster.Stroke.features.StrokeRubineFeatureSet;
import SketchMaster.Stroke.graphics.shapes.GuiShape;
import SketchMaster.classifier.Classification;
import SketchMaster.classifier.Symbol.SymbolCategory;
import SketchMaster.classifier.Symbol.SymbolClassifier;
import SketchMaster.classifier.Symbol.SymbolTrainingSet;
import SketchMaster.classifier.rubine.RubineCategory;
import SketchMaster.classifier.rubine.RubineClassifier;
import SketchMaster.classifier.rubine.RubineTrainingSet;
import SketchMaster.gui.DrawingSheet;
import SketchMaster.gui.GraphWatched;
import SketchMaster.gui.Events.NewStrokeEvent;
import SketchMaster.io.FileObjectReader;
import SketchMaster.io.FileObjectWriter;
import SketchMaster.io.log.FileLog;
import SketchMaster.system.SketchSheet;
import SketchMaster.system.SystemSettings;
import SketchMaster.system.clustering.SystemClustering;
import SketchMaster.system.segmentation.SezginSegmentor;
import SketchMaster.system.segmentation.SketchSegmentors;

public class SimpleSymbolRecognizier extends RecognizierSystem {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(SimpleSymbolRecognizier.class);

	private SymbolTrainingSet trainer = null;
	private SymbolCategory currentCategory;

	private SymbolClassifier symbolClassifier;
	//SystemClustering clusturing;
	transient SketchSheet sheet;
	private SegmentCluster LastClusterDrawn;
	public void ReadTrainingSet(String FileName) {

		// set current to null
		trainer = null;
		FileObjectReader fileReader = new FileObjectReader();
		fileReader.readFile(FileName);
		Object o = fileReader.ReadObject();
		if (o instanceof SymbolTrainingSet) {
			trainer = (SymbolTrainingSet) o;
			// logger.info("setting the read ");
			trainer.ReadData();
			symbolClassifier.init(trainer);
		}
		fileReader.closeRead();
	}

	public void SaveTrainingSet(String FileName) {
	
		if (trainer != null) {
			trainer.trainAll();
			trainer.SaveData();
			// first create the file object that will rite the file

			FileObjectWriter fileWriter = new FileObjectWriter();
			fileWriter.createFile(FileName);
			fileWriter.WriteObject(trainer);
			fileWriter.closeWrite();

		}
	}

	@Override
	public void Clear() {
		LastClusterDrawn = null;

	}
	
	public void setDataDisplay(DrawingSheet drawSheet){
           if (sheet!=null){
        	  super.setDataDisplay(drawSheet);
           }
	}

	public void addCategoryExample(int i, Object cluster) {
	
		if (trainer.size() < i) {
			// then this category does t exist
			// i will need to add it. till we
			for (int j = i; j < trainer.size(); j++) {
				this.addNewCategory("Category " + j);
			}
			// logger.info(" sss ");
		}

		trainer.addCategoryExample(i, (SegmentCluster) cluster);
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
	public void addNewCategory(String Name) {
		// logger.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
		// logger.info("adding a new cateogry ");
		SymbolCategory cat = new SymbolCategory();
		cat.setCategoryName(Name);
		cat.init();
		trainer.add(cat);
		setCurrentSelectedCategoryName(Name);
		currentCategory = cat;
		CurrentCat = trainer.size() - 1;// id

	}

	@Override
	public void deleteCategory(String Name) {
		trainer.deleteCategory(Name);

	}

	@Override
	public int deleteCategoryExample(String Name, int i) {
		return trainer.deleteCategoryExample(Name, i);

	}

	@Override
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

	@Override
	public int getCurrentExamplesCount() {
	
		if (currentCategory != null)
			return currentCategory.getExampleCount();
		else
			return getCategoryExamples(CurrentSelectedCategoryName).size();// takes
																			// more
																			// time
	}

	@Override
	public String getFeatureString() {
		if (LastClusterDrawn != null) {
			SegmentClusterFeatureSet temp = new SegmentClusterFeatureSet();
			temp.setSegmentCluster(LastClusterDrawn);

			temp.initAll();
			temp.computeFeatures();

			if (SystemSettings.CurrentRecognizierOperation == this.RECGONIZE_OPERATION_TRAIN) {
				return temp.toString();
			} else {
				String tempx = "/t";
				tempx += temp.toString();
				tempx += "/n/t";
				if (CurrentClassification != null)
					tempx += CurrentClassification.toString();

				// tempx+=temp.toString();
				return (tempx);
			}

		}

		return "";
	}

	@Override
	public void init() {
		Logger appLogger = Logger.getLogger("AppLogging");
		// logger.info(" i am still have to work on the symbol
		// recogniziwe system ");
		symbolClassifier = new SymbolClassifier();
		appLogger.info(" [Settings] indside symbols recognizier. settings is "+SystemSettings.getString());
		// logger.info("look ");
//		if (SystemSettings.CurrentRecognizierLoadOption == RECGONIZE_LOAD_SYS) {
//			trainer = new SymbolTrainingSet();
//			//  logger.trace("initailizing  the  symbol trainer ");
//			trainer.init();
//			// logger.info("init both classifier and rubine");
//			// Rubclassifier.init();
//			symbolClassifier.init(trainer);
//			//  logger.trace("initailizing  the  classifier trainer ");
//		} else {

			// load from file system

			trainer = new SymbolTrainingSet();
			trainer.init();
			// set the trainig set to the classifier
			symbolClassifier.init(trainer);
	//	}
		
		sheet=new SketchSheet();
		sheet.initSketch();
		sheet.initClustringAlgorithm();
//		clusturing = new SystemClustering();
//
//		clusturing.setSymbolLayer(null);
	}

	@Override
	public void setCurrentSelectedCategoryName(
			String currentSelectedCategoryName) {

		CurrentSelectedCategoryName = currentSelectedCategoryName;
		currentCategory = trainer.getCategory(CurrentSelectedCategoryName);

	}

	@Override
	public void train() {
		logger.info("############### Start training calcualtions ###########################################");
		trainer.trainAll();

		logger.info("############### Finished training calcualtions ###########################################");

	}

	public void update(Observable arg0, Object arg1) {
		if (arg0 instanceof GraphWatched) {
			GraphWatched temp = (GraphWatched) arg0;
			if (LastClusterDrawn != null)
				LastClusterDrawn.paint(temp.getG());
		}

	}

	public void HandleNewStroke(NewStrokeEvent Evt) {

		
		Stroke stroke=sheet.PreProcessStroke(Evt.getEventStroke());
		
		GuiShape segmentation = segmentStoke(stroke);
	

		// divide the stroke using algoirithms

		// add strokes in a list
		sheet.addStrokeToCluster(stroke, segmentation);
		// do nothing
 
//		setChanged();
//		notifyObservers();
		///// to add 
//
//		GuiShape segmentation = segmentStoke(Evt.getEventStroke());
//		addStrokeToCluster(Evt.getEventStroke(), segmentation);
		
	}

	private GuiShape segmentStoke(Stroke stroke) {
		
		
		return sheet.segmentStroke(stroke);
//		SketchSegmentors segment = new SketchSegmentors();
//		segment.generateDominatePoints(stroke);
//		GuiShape sol;
//
//		// // sol= segment.lineFit(stroke); // try to fit the line
//		// // this.addFitToLayer(this.LineFitname ,sol ) ;
//		//		 
//		// sol= segment.curveFit(stroke); // try to fit the ellipse
//		// // this.addFitToLayer(this.ellipseFitname ,sol ) ;
//		//		
//		// sol= segment.divideStroke(stroke); // try to fit the ellispe
//		// /// this.addFitToLayer(this.polygonName ,sol ) ;
//		// logger.info("try to find a way to determine if ellipse before
//		// getting into more swarm systems ");
//		// logger.info(" try to calcuate the square orthigonal distance
//		// from teh solution to stroke ");
//		//		  
//		// //
//		// sol=segment.divideStrokeCurves(stroke);
//		// // this.addFitToLayer(multiCircleName, sol);
//
//		SezginSegmentor segmentH = new SezginSegmentor();
//
//		sol = segmentH.runAlogrithm(stroke);
//		// this.addFitToLayer( SezignName, sol);
//
//		GuiShape BestSol = getBestSegmentationSol();
//
//		if (BestSol != null)
//			return BestSol;
//		else {
//			return sol;
//		}
	}



	public void createClusterFromStrokes() {

		// get a list of the storke
		// create symbol
		LastClusterDrawn = sheet.CreateNewSymbol();

		// add to category
		// or add the stroke to the trining set
		if (SystemSettings.CurrentRecognizierOperation == this.RECGONIZE_OPERATION_TRAIN) {
			if (CaptureStrokes) {
				// 
				if (logger.isDebugEnabled()) {
					//  logger.debug("createClusterFromStrokes() - adding an example to " + CurrentCat); //$NON-NLS-1$
				}
				addCategoryExample(CurrentCat, LastClusterDrawn);
				// CaptureStrokes=false;
			}

		}

		// either classify the stroke
		if( (SystemSettings.CurrentRecognizierOperation == this.RECGONIZE_OPERATION_TEST)||(SystemSettings.CurrentRecognizierOperation == this.RECGONIZE_OPERATION_CLASSIFY)) {
			logger.info(" !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!classify !!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			CurrentClassification = symbolClassifier.Classify(LastClusterDrawn);

		}
		setChanged();
		notifyObservers();

	}

	@Override
	public int getCategoryCount() {
		return trainer.size();
		//return 0;
	}

}
