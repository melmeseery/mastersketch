/**
 * 
 */
package SketchMaster.system.Recogniziers;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Observable;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import SketchMaster.Stroke.StrokeData.InkInterface;
import SketchMaster.Stroke.StrokeData.SegmentCluster;
import SketchMaster.Stroke.StrokeData.Stroke;
import SketchMaster.Stroke.features.SVMFeatureSet;
import SketchMaster.Stroke.features.SegmentClusterFeatureSet;
import SketchMaster.Stroke.graphics.shapes.GuiShape;
import SketchMaster.classifier.Classification;
import SketchMaster.classifier.Symbol.SymbolClassifier;
import SketchMaster.classifier.Symbol.SymbolTrainingSet;
import SketchMaster.classifier.rubine.RubineTrainingSet;
import SketchMaster.classifier.svmTrainable.*;
import SketchMaster.gui.GraphWatched;
import SketchMaster.gui.Events.NewStrokeEvent;
import SketchMaster.io.FileObjectReader;
import SketchMaster.io.FileObjectWriter;
import SketchMaster.system.SketchSheet;
import SketchMaster.system.SystemSettings;
import SketchMaster.system.clustering.SystemClustering;

/**
 * @author Maha
 *
 */
public class SVMRecognizier extends RecognizierSystem {
	private static final Logger logger = Logger.getLogger(SVMRecognizier.class);
	
private static String CurrentCategory = null;
//SystemClustering clusturing;
transient SketchSheet sheet;
private SegmentCluster LastClusterDrawn;
//	
//	protected String FeatureString;
//	 
//	protected int CurrentCat = 0;
//
//	protected String CurrentSelectedCategoryName = "";
//	protected boolean CaptureStrokes = false;
	
	// i will need to add all trian dat a
	

int CurrentStrokesCount=0;
	
	TrainingSet trainSet;
	boolean trained=false;
	
	SVMClassifier  svm;
	SVMClassification svmClass;
	
	boolean xmlRead=false;
	boolean Interactive=true;
  //hashtable;
	   Hashtable<Integer, String> Categories = new Hashtable<Integer, String>();

private SVMClassification SVMCurrentClassification;

private SVMFeatureSet LastDrawnfeats;

private TrainSet TrainSetStrokes;
	   
	
	
	/* (non-Javadoc)
	 * @see SketchMaster.system.Recogniziers.RecognizierSystem#Clear()
	 */
	@Override
	public void Clear() {
		//clear any unfinished strokes  
		sheet.clearAllSketch();
		
		
		// current displayed set null 
		this.LastClusterDrawn=null;
		//set changed need to redraw and clear text box
		setChanged();
		notifyObservers();


	}
	

	/* (non-Javadoc)
	 * @see SketchMaster.system.Recogniziers.RecognizierSystem#ReadTrainingSet(java.lang.String)
	 */
	@Override
	public void ReadTrainingSet(String FileName) {
	
		logger.info("reading the training model ......");
		// set current to null
		svm = null;
		FileObjectReader fileReader = new FileObjectReader();
		fileReader.readFile(FileName);
		Object o = fileReader.ReadObject();
		if (o instanceof SVMClassifier) {
			svm= (SVMClassifier) o;
			//trainer.ReadData();
			Categories =new Hashtable<Integer, String>(svm.getLabels());			
		}

		fileReader.closeRead();

	}

	/* (non-Javadoc)
	 * @see SketchMaster.system.Recogniziers.RecognizierSystem#SaveTrainingSet(java.lang.String)
	 */
	@Override	  
	public void SaveTrainingSet(String FileName) {
		if (svm != null) {
			if (!trained)
			 this.train();
			
			String s=FileName;
			if (FileName.contains("\\"))
			{
				
			
				
				int temp=FileName.lastIndexOf(".");
				int temp1=FileName.lastIndexOf("\\");
					s=FileName.substring(temp1+1, temp);
					
				
			}
			else 
			{
				int temp=FileName.lastIndexOf(".");
				s=FileName.substring(0, temp);
				
			}
		//	int temp=FileName.lastIndexOf(".");
			// remove all the extention from current file name to it form model 
			
			String mod=		s+"_model.svt";
			
				svm.setModelFileName(mod);
			// first create the file object that will rite the file

			FileObjectWriter fileWriter = new FileObjectWriter();
			fileWriter.createFile(FileName);
			fileWriter.WriteObject(svm);
			fileWriter.closeWrite();

		}

	}

	public void SaveTrainingSetArffFile(String Filename){
	       trainSet.SaveToArrffFile(Filename);
		
	}
	
	
	public void SaveTrainingSetXML(String filename){
		
		TrainSetStrokes.saveXML(filename);
		
	}
	
	public void ReadTrainingSetXML(String filename) {
		
		logger.info("Reading the xml data ");
		 xmlRead=true;
		TrainSetStrokes.readXML(filename);
		 Categories =new Hashtable<Integer, String>();
		Iterator< String>  iterator=TrainSetStrokes.types();
		int i=0;
	 for (; iterator.hasNext();) {
		String cat = iterator.next();
		
		Categories.put(i, cat);
		
		
		
		i++;
	}
		 //Categories.put(i, )
		 
	}
	
//	public void AddCategoryExamples(String temp,ArrayList<Stroke> inkStrokes){
//		
//		
//		SystemClustering tempClus=new 	SystemClustering();
//		 
//		
//		 SegmentCluster segment =  (SegmentCluster)ink;
//		 SegmentClusterFeatureSet example = new SegmentClusterFeatureSet();
//			example.setSegmentCluster(segment);
//			example.initAll();
//			SVMFeatureSet feats = example.computeSVMFeatures();
//		  trainSet.addPositiveExample(temp, feats);
//		
//		  logger.info("  SVM  adding example "+temp);
//		 //segment.
//		  trained=false;
//		
//	}
	
	
	/* (non-Javadoc)
	 * @see SketchMaster.system.Recogniziers.RecognizierSystem#addCategoryExample(int, java.lang.Object)
	 */
	@Override
	public void addCategoryExample(int i, Object ink) {
	      String temp ;
		if (Categories.contains(i))
	
			temp = Categories.get(i);
		else
		   temp=CurrentCategory;
	  //    (InkInterface)
	//	logger.info("addint the  exmaple of "+i+"  "+CurrentCategory);
	   if (ink instanceof  SegmentCluster) {
//		   logger.info("addding ............"+ink);
		 SegmentCluster segment =  (SegmentCluster)ink;
		 SegmentClusterFeatureSet example = new SegmentClusterFeatureSet();
			example.setSegmentCluster(segment);
			example.initAll();
			SVMFeatureSet feats = example.computeSVMFeatures();
		  trainSet.addPositiveExample(temp, feats);
		  if (Interactive)
		  TrainSetStrokes.addPositiveExample(temp,segment.getStrokeInSymbol());
	//	  logger.info("  SVM  adding example "+temp);
		 //segment.
		  trained=false;
	}
	   
	   
	
	    
	}
	public void checkAddCategory(String Name) {
		
		addNewCategory(Name);
	}
	/* (non-Javadoc)
	 * @see SketchMaster.system.Recogniziers.RecognizierSystem#addNewCategory(java.lang.String)
	 */
	@Override
	public void addNewCategory(String Name) {
		this.CurrentCategory=Name;
		// check if exist 
		 if (Categories.containsValue(Name))
			 return ;
		 else {
			 // it does not exist 
		//get size 
		int temp=Categories.size();
		  Categories.put(temp, Name);
		 }
		 trained=false;
	}

	/* (non-Javadoc)
	 * @see SketchMaster.system.Recogniziers.RecognizierSystem#deleteCategory(java.lang.String)
	 */
	@Override
	public void deleteCategory(String Name) {
	
//		 check if exist 
		 if (Categories.containsValue(Name))
		 {
			 Set<Entry<Integer, String>> collection = Categories.entrySet();
			 for (Iterator iter = collection.iterator(); iter.hasNext();) {
				 Entry element = (Entry) iter.next();
				if (element.getValue().equals(Name))
				{
					iter.remove();
					break;
				}
			}
			 // now remove all entries of this category from the train set. 
			 trainSet.removeType(Name);
			  if (Interactive)
			 TrainSetStrokes.removeType(Name);
			 
			 return ;
		 }
		 trained=false;
		
	}

	/* (non-Javadoc)
	 * @see SketchMaster.system.Recogniziers.RecognizierSystem#deleteCategoryExample(java.lang.String, int)
	 */
	@Override
	@Deprecated
	public int deleteCategoryExample(String Name, int i) {
		  
		return 0;
	}

	/* (non-Javadoc)
	 * @see SketchMaster.system.Recogniziers.RecognizierSystem#getCategoryCount()
	 */
	@Override
	public int getCategoryCount() {
		
		return Categories.size();
	}

	/* (non-Javadoc)
	 * @see SketchMaster.system.Recogniziers.RecognizierSystem#getCategoryExamples(int)
	 */
	@Override
	@Deprecated
	public ArrayList getCategoryExamples(int Name) {
		  
		return null;
	}

	/* (non-Javadoc)
	 * @see SketchMaster.system.Recogniziers.RecognizierSystem#getCategoryExamplesInk(java.lang.String)
	 */
	@Override
	@Deprecated
	public ArrayList getCategoryExamplesInk(String Name) {
		  
		return null;
	}

	/* (non-Javadoc)
	 * @see SketchMaster.system.Recogniziers.RecognizierSystem#getCategoryNames()
	 */
	@Override
	public String[] getCategoryNames() {
		
//		logger.info("CATTTTTTTTTTTTNAMMMMMMMMMMMMESSSSSSSSSSSSSSSSSs"+" (" + this.getClass().getSimpleName()
//				+ "    " + (new Throwable()).getStackTrace()[0].getLineNumber()
//				+ "  )  ");
		Collection<String> val = Categories.values();
		String [] temp =new String[val.size()];
		int i=0;
		for (Iterator iter = val.iterator(); iter.hasNext();) {
			String str = (String) iter.next();
			temp[i]=str;
			//logger.error("str " +str);
			i++;
		}
	
		return 	 temp;
	}

	/* (non-Javadoc)
	 * @see SketchMaster.system.Recogniziers.RecognizierSystem#getCurrentExamplesCount()
	 */
	@Override
	public int getCurrentExamplesCount() {
	
		return trainSet.examplesSize(CurrentCategory);
	}

	/* (non-Javadoc)
	 * @see SketchMaster.system.Recogniziers.RecognizierSystem#getFeatureString()
	 */
	@Override
	public String getFeatureString() {
		if (LastClusterDrawn != null) {
			SegmentClusterFeatureSet temp = new SegmentClusterFeatureSet();
			temp.setSegmentCluster(LastClusterDrawn);

			temp.initAll();
			temp.computeFeatures();
			SVMFeatureSet feat = temp.computeSVMFeatures();
		

			if (SystemSettings.CurrentRecognizierOperation == this.RECGONIZE_OPERATION_TRAIN) {
				
				
				
				return feat.getFullString();
			} else {
				String tempx = "/t";
				tempx += temp.toString();
				tempx += "/n/t";
				//SVMCurrentClassification
				if (SVMCurrentClassification!= null)
					tempx += "     "+SVMCurrentClassification.getHighestConfidenceType()+"   with confidance "+SVMCurrentClassification.getHighestConfidence();
				tempx+=SVMCurrentClassification;
				logger.info(tempx);
				//tempx += "     "+SVMCurrentClassification.getLargestKey()+"   with confidance "+SVMCurrentClassification.getFirstValue();
				

				// tempx+=temp.toString();
				return (tempx);
			}

		}
		return "";

	
	}

	/* (non-Javadoc)
	 * @see SketchMaster.system.Recogniziers.RecognizierSystem#init()
	 */
	@Override
	public void init() {
		// logger.info(" i am still have to work on the symbol
		// recogniziwe system ");
		
	
		  // svmClass = new SymbolClassifier();

		// logger.info("look ");
		Logger appLogger = Logger.getLogger("AppLogging");
		//logger.setLevel(Level.INFO);
		appLogger.info(" [Settings] indside svm settings is "+SystemSettings.getString());
		if (SystemSettings.CurrentRecognizierLoadOption == RECGONIZE_LOAD_SYS) {
			
			trainSet=new TrainingSet();
			TrainSetStrokes=new TrainSet();
			trained=false;	
			svm=new SVMClassifier();

	  //hashtable;
          Categories = new Hashtable<Integer, String>();
          
          
	
			//  logger.trace("initailizing  the  symbol trainer ");
			
			// logger.info("init both classifier and rubine");
			// Rubclassifier.init();
		
			//  logger.trace("initailizing  the  classifier trainer ");
		} else {

			// load from file system

			logger.error("ERRRROR un implemented code for load recognizeir from ");
//			trainer = new SymbolTrainingSet();
//			trainer.init();
//			// set the trainig set to the classifier
//			symbolClassifier.init(trainer);
		}
		
		sheet=new SketchSheet();
		sheet.initSketch();
		sheet.initClustringAlgorithm();
//		clusturing = new SystemClustering();

	}

	/* (non-Javadoc)
	 * @see SketchMaster.system.Recogniziers.RecognizierSystem#setCurrentSelectedCategoryName(java.lang.String)
	 */
	@Override
	public void setCurrentSelectedCategoryName(
			String currentSelectedCategoryName) {
		CurrentCategory=currentSelectedCategoryName;

	}

	/* (non-Javadoc)
	 * @see SketchMaster.system.Recogniziers.RecognizierSystem#train()
	 */
	@Override
	public void train() {
		
		if (xmlRead){
			logger.info("----------Reading from xml so segment and create cluster first. -------------");
			//cluster all the reading to file 
			SegmentTrainSetStrokes();
			xmlRead=false;
			
		}
		
	       if (!trained){
	    	   logger.info("starting to train..........................");
		 SegmentClusterFeatureSet example = new SegmentClusterFeatureSet();
			example.setSegmentCluster(LastClusterDrawn);
			example.initAll();
		svm.train(trainSet,example.getCountOfFeatures() );
		trained=true;
	       }

	}
	private void SegmentTrainSetStrokes(){
		
		
	SystemSettings.CurrentRecognizierOperation =this.RECGONIZE_OPERATION_TRAIN;
			CaptureStrokes=true;
		
			ExmapleType=EXAMPLE_POS;
		
		
		if (TrainSetStrokes!=null){
			
			 Interactive=false;
			 int count=0;
			int catCount=0;
			   for (Iterator iterator = TrainSetStrokes.types(); iterator.hasNext();) {
		   			String type = (String) iterator.next();
		   			logger.info(" segmeting the type "+type);
		   			CurrentCategory=type;
		   		CurrentCat=getCategoryide(type);
			ArrayList<ArrayList<Stroke>> catArray = TrainSetStrokes.getExamples(CurrentCategory);
			count=0;
			catCount=0;
			
			for (int i = 0; i < catArray.size(); i++) {
				Stroke stroke=null;
				
//				catCount++;
//				if (catCount>20){
//					break;
//				}
				// 
				if (count%50==0){
					logger.info(" Example number "+count);
					logger.info("  example count in strokes trains set is  "+trainSet.getExamplesCount());
				}
				count++;
				ArrayList<Stroke> strokeArray = catArray.get(i);
				
				for (int j = 0; j < strokeArray.size() ; j++) {
					stroke=strokeArray.get(j);
					//for strokes do 
					  NewStrokeEvent newStroke = new NewStrokeEvent(this);
						newStroke.setEventStroke(stroke);
						newStroke.setFlagConsumed(false);
					HandleNewStroke(newStroke);
						
				}// 
				/// after stroke do the follwoing 
					createClusterFromStrokes();
			}
			
			
				
			   }
				
		}
	}

	private int getCategoryide(String type) {

		Categories.containsValue(type);
	
	 for (Iterator iterator = 	Categories.entrySet().iterator(); iterator.hasNext();) {
	Entry<Integer,String> cat = (Entry<Integer,String>) iterator.next();
		if (cat.getValue().equals(type)){
			return cat.getKey();
			
		}
	}
		
		return -1;
	}


	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	public void update(Observable arg0, Object arg1) {
		if (arg0 instanceof GraphWatched) {
			GraphWatched temp = (GraphWatched) arg0;
			if (LastClusterDrawn != null)
				LastClusterDrawn.paint(temp.getG());
		}

	}

	/* (non-Javadoc)
	 * @see SketchMaster.gui.Events.HandleStroke#HandleNewStroke(SketchMaster.gui.Events.NewStrokeEvent)
	 */
	public void HandleNewStroke(NewStrokeEvent Evt) {
		
		Stroke stroke=sheet.PreProcessStroke(Evt.getEventStroke());
		
		
		GuiShape segmentation = segmentStoke(stroke);
		

		// divide the stroke using algoirithms

		// add strokes in a list
		sheet.addStrokeToCluster(stroke, segmentation);
		CurrentStrokesCount++;
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

		//  only run if strok cound > 0 
		
	//	sheet.getClusterStrokeCounts()
		if (CurrentStrokesCount>0){
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
				if (ExmapleType==EXAMPLE_POS){
	
				 addCategoryExample(CurrentCat, LastClusterDrawn);
				 
				}
				else if (ExmapleType==EXAMPLE_NEG)
					addCategoryNegativeExample(CurrentCat, LastClusterDrawn);
					
					
				trained=false;
				// CaptureStrokes=false;
			}

		}

		// either classify the stroke
		if( (SystemSettings.CurrentRecognizierOperation == this.RECGONIZE_OPERATION_TEST)||(SystemSettings.CurrentRecognizierOperation == this.RECGONIZE_OPERATION_CLASSIFY)) {
			logger.info(" !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!classify !!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			
			
			 SegmentClusterFeatureSet example = new SegmentClusterFeatureSet();
				example.setSegmentCluster(LastClusterDrawn);
				example.initAll();
				LastDrawnfeats = example.computeSVMFeatures();
				SVMCurrentClassification = svm.classify(LastDrawnfeats);
			

		}
		
		
		CurrentStrokesCount=0;
		setChanged();
		notifyObservers();
		}

	}
	@Deprecated
	public void createClusterFromStrokes(int i) {
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
				addCategoryNegativeExample(CurrentCat, LastClusterDrawn);
				trained=false;
				// CaptureStrokes=false;
			}

		}
		setChanged();
		notifyObservers();
	}
 
	private void addCategoryNegativeExample(int i,
			SegmentCluster ink) {
		   String currentCategory ;
			if (Categories.contains(i))
		
				currentCategory = Categories.get(i);
			else
			   currentCategory=CurrentCategory;
		  //    (InkInterface)
		//   if (ink instanceof  SegmentCluster) {
			 SegmentCluster segment =  (SegmentCluster)ink;
			 SegmentClusterFeatureSet example = new SegmentClusterFeatureSet();
				example.setSegmentCluster(segment);
				example.initAll();
				SVMFeatureSet feats = example.computeSVMFeatures();
				String temp;
				for (int j = 0; j < Categories.size(); j++) {
					temp=Categories.get(j);
					if (!temp.equals(currentCategory))
					  trainSet.addNegativeExample(temp, feats);
					  if (Interactive)
					 TrainSetStrokes.addNegativeExample(temp,ink.getStrokeInSymbol());
				}
				
				
		
			  logger.info("  SVM  adding -ve example for  "+ currentCategory);
			 //segment.
			  trained=false;
		 //  }
	}

	@Override
	public  Classification getCurrentClassification() {
    
		return SVMCurrentClassification;
	}

	/**
	 * @return the lastDrawnfeats
	 */
	public SVMFeatureSet getLastDrawnfeats() {
		return LastDrawnfeats;
	}

	
}
