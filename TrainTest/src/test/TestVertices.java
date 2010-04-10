package test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Observable;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import settings.TestResults;
import settings.TestSketchSetting;
import settings.VertixTestResult;
import settings.VertixTestResultNode;
import SketchMaster.Stroke.StrokeData.SegmentCluster;
import SketchMaster.Stroke.StrokeData.Stroke;
import SketchMaster.Stroke.graphics.shapes.GuiShape;
import SketchMaster.gui.Events.NewStrokeEvent;
import SketchMaster.swarm.curvefit.StrokeCurveSolution;
import SketchMaster.swarm.polygonApproximations.DigitalCurveDivideSolution;
import SketchMaster.swarm.polygonApproximations.polygonSolution;
import SketchMaster.system.SketchSheet;
import SketchMaster.system.SystemSettings;
import SketchMaster.system.Recogniziers.RecognizierSystem;
import SketchMaster.system.Recogniziers.RubineRecognizier;
import SketchMaster.system.Recogniziers.SVMRecognizier;
import SketchMaster.system.Recogniziers.SimpleSymbolRecognizier;
import SketchMaster.system.segmentation.HybirdFitSolution;


public class TestVertices extends Observable implements Runnable  {
	/**
	 * Logger for this class
	 */
	// get 
	ArrayList<String>  SymbolsTypesToTest;
	
	private static final Logger logger = Logger.getLogger( TestVertices.class);
	
	ArrayList<String>  TestSetfiles=null;	
	
	String FinalMessage=" ";
	String TrainingFileName;
	 HandleDataSetEvents dataSet;
	 
	 transient SketchSheet sheet;
	 private SegmentCluster LastClusterDrawn;	                             

	// boolean OS_Linux=false;
	 private int MAX_CAT_SIZE=-1;
		
		// data set type 
		static int DataSetType=0  ;//   0 xml files of shapes 1 psql db  

		boolean PatchMode=false;

//		private Object recoglizier;
		  RecognizierSystem  recognizier=null;
		 // private int Recognizier_type;
		
	//	static boolean displayOn=false;
		  GuiShape Lastsegmentation ;
  
		  VertixTestResult result;	
	
	
public void readSettings ( TestSketchSetting settings){
		
		//read the settingds file 
		
			// if error get default settinds
		
		// if setting passed not an object. 
		//else  i will need to read the data set type 
		if (settings!=null)
		{
			PatchMode=true;
		    // if database then read connection string 
			 
			  //if file then read the names of the files 
			
			// now get the mode 
			
			// if train use the file to train system 
			// if test use data to test system.  
			          //get the name of the train file that will be tested. 
			TestSetfiles=new ArrayList<String>();
			
			//OS_Linux=settings.isOSLinux();
			DataSetType=settings.getDataSet();
		
			
			int sizeTest=settings.getTestSize();
			
			ArrayList<String> files=settings.getFilesNames();
			
			for (int i = 0; i < sizeTest; i++) {
				TestSetfiles.add(files.get(i));
			}
			
			result=new VertixTestResult();
			result.setTestName(settings.getTestName()) ;
	    	result.setNFiles(settings.getTestSize());
		
			
		  //   this.Recognizier_type=settings.getRecognizierType();
		    // PrevTrain=settings.getPreviousTrain();
		  //   TrainingFileName=settings.getTestName().replace(" ", "_")+ ".smv";
		   //  displayOn=settings.isDisplaySketch();
		    
		}
	
		else 
		Defaults();
		
	}
public void initRunPatch(TestSketchSetting settings){
	readSettings(settings);

    // SystemSettings.CurrentRecognizierOperation=settings.getCurrentRecognizierOperation(); 
	sheet=new SketchSheet();
	sheet.initSketch();
	sheet.initClustringAlgorithm();
	
		dataSet=new HandleDataSetEvents(); 
		
		
	// this.run();

	 
}
	
	public void Defaults(){
		//  logger.debug(" L("  	+ (new Throwable()).getStackTrace()[0].getLineNumber() + ")"+" - "	+"Loading the defaults");
//		logger.info("Loading the defaults ");
//		logger.warn("this is a warinng ");
//		logger.error("this is a error ");
		TestSetfiles=new ArrayList<String>();
//		if (OS_Linux){
//			TestSetfiles.add("/windows/E/sketch/other systems/source/hhreco/data/data/user1.sml");
//			TestSetfiles.add("/windows/E/sketch/other systems/source/hhreco/data/data/user2.sml");
////			TestSetfiles.add("/windows/E/sketch/other systems/source/hhreco/data/data/user3.sml");
////			TestSetfiles.add("/windows/E/sketch/other systems/source/hhreco/data/data/user4.sml");
////			
//	}
//		else{ 
		TestSetfiles.add("F:\\sketch\\other systems\\source\\hhreco\\data\\data\\user13.sml");
//		TestSetfiles.add("F:\\sketch\\other systems\\source\\hhreco\\data\\data\\user2.sml");
//		TestSetfiles.add("F:\\sketch\\other systems\\source\\hhreco\\data\\data\\user3.sml");
//		TestSetfiles.add("F:\\sketch\\other systems\\source\\hhreco\\data\\data\\user4.sml");
//		TestSetfiles.add("F:\\sketch\\other systems\\source\\hhreco\\data\\data\\user5.sml");
//		TestSetfiles.add("F:\\sketch\\other systems\\source\\hhreco\\data\\data\\user11.sml");
//		TestSetfiles.add("F:\\sketch\\other systems\\source\\hhreco\\data\\data\\user7.sml");
//		TestSetfiles.add("F:\\sketch\\other systems\\source\\hhreco\\data\\data\\user8.sml");
//		TestSetfiles.add("F:\\sketch\\other systems\\source\\hhreco\\data\\data\\user9.sml");
//		TestSetfiles.add("F:\\sketch\\other systems\\source\\hhreco\\data\\data\\user10.sml");
//	
//		}
		//TestSetfiles.add("F:\\sketch\\other systems\\source\\hhreco\\data\\data\\user5.sml");
		//TestSetfiles.add("F:\\sketch\\other systems\\source\\hhreco\\data\\data\\user6.sml");
		DataSetType=TestSketchSetting.DATA_SET_TYPE_XML;  //xml files 
		//RunMode=0;
    
//     PauseAndSave=false;
//    // setRecognizier_type(TestSketchSetting.SYMBOL_RECOGNIZER);
//		if (PrevTrain==TestSketchSetting.PREV_NO_TRAIN){
//			     Date d=new Date();
//			     SimpleDateFormat formatter=new SimpleDateFormat("dd-MM-yyyy_hh-mm");
//			    // formatter.applyPattern("y");
//			     formatter.format(d);
//			     String dat=formatter.format(d);
//			     String fileName="train_Symbol_"+dat+"_Size_"+TestSetfiles.size();
//			     TrainingFileName=fileName+".smt";
//		
//		}
		logger.info(" L("
				+ (new Throwable()).getStackTrace()[0].getLineNumber() + ")"
				+ " - " + " [TRAIN] The training will be saved into  "+TrainingFileName);
		
	}

	
public void RunAllFiles(){
//		
//		// loop on the list of file strings  TestSetfiles
		for (int i = 0; i < TestSetfiles.size(); i++) {
			logger.error(" L("  
					+ (new Throwable()).getStackTrace()[0].getLineNumber() + ")"+" - "
					+"  [TRAIN]  traning file number   "+i);
//			//for each file do the following 
//			// trainFile
//			updateCounters(i,0,0);
			boolean stop=RunFile(TestSetfiles.get(i));
//			// // if train individually then train 
//			// else loop again 
//			if (falseSampleID==0)
//				falseSampleID=1;
//			else if (falseSampleID==1)
//				falseSampleID=0;
			if (stop==true)
				break;
//			// after all train all 
//			// save train data to disk in save location (from settings)
		}
		logger.error(" L("  
				+ (new Throwable()).getStackTrace()[0].getLineNumber() + ")"+" - "
				+" Now will train the system.     [TRAIN] ");
//		getRecognizier().SaveTrainingSet(TrainingFileName);
		
		FinalMessage="Finished training all files and saved into system.     [TRAIN] ";
		
		
		 if (!PatchMode)
			 JOptionPane.showMessageDialog(null, FinalMessage);
		 else 
				logger.warn(FinalMessage); 
			
}
public boolean RunFile(String file){
	boolean stop=false;
	// open the file using xml paraser 
	dataSet.readXML(file);
	ArrayList<String> Cat = dataSet.getCategoriesList();
	if (Cat==null)
		
	return stop;
int	MAX_CAT_SIZE=Cat.size();
	
//	updateMaxCounters(MAX_CAT_SIZE, -1);
//	//  for each type/catgory  do the following
	logger.info(" L("
			+ (new Throwable()).getStackTrace()[0].getLineNumber() + ")"
			+ " - " + "  [TRAIN] the number of cateogies is  "+MAX_CAT_SIZE);
	for (int i = 0; i < MAX_CAT_SIZE; i++) {
		if (stop==true)
			return stop;
	  ArrayList<ArrayList<Stroke>> Examples = dataSet.getExampleForCat(Cat.get(i));
  //logger.debug(" L(" 	+ (new Throwable()).getStackTrace()[0].getLineNumber() + ")"+" - "+"  adding the samples for cateogry  [TRAIN] "+Cat.get(i));
//	   //get all example into == array list of (arrylist of storkes )
//	  
	logger.info(" L("
			+ (new Throwable()).getStackTrace()[0].getLineNumber() + ")"
			+ " - " + "  [TRAIN]  the cateogry  "+Cat.get(i)+  "  number  "+i+" of  "+ MAX_CAT_SIZE+"  has "+Examples.size()+" Examples");
//	CurrentCatString=Cat.get(i);
//	updateMaxCounters(MAX_CAT_SIZE,Examples.size());
//	
	  for (int j = 0; j <Examples.size(); j++) {
		  
//		// for each example do the following
//		  updateCounters(-1,i,j);
//		  stop=CheckSave();
		  if (stop==true)
			return stop;
		  logger.info(" L("
					+ (new Throwable()).getStackTrace()[0].getLineNumber()
					+ ")" + " - " + "   [TRAIN]  ----------  Example number  "+j);
//				 
//				   
//	
//
		  for (int k = 0; k < Examples.get(j).size(); k++) {
//
//			  // read all storkes for this example and do the following
//			   // add stroke to system 
//		      // segment stroke (compute initial features and run algorithms)
//		        // add stroke to cluster
			  NewStrokeEvent newStroke = new NewStrokeEvent(this);
				newStroke.setEventStroke(Examples.get(j).get(k));
				newStroke.setFlagConsumed(false);
				this.HandleNewStroke(newStroke);
				
				 VertixTestResultNode exampleResult=getDataFromSegmentation();
					  
					result.AddCategoryResult(Cat.get(i),exampleResult);
				
//				// compute features of the clusters 
//				// add to the dataset in recognizier for this category. 
		}
		  this.createClusterFromStrokes();
		  
		 
		 //now i needd to create the results .... 
	}

	  
//
	}
//	
	dataSet.closeFile();
	return stop;
	
	
	
	
	//return true;
}
/**
 * @return the recognizier_type
 */
//public int getRecognizierType() {
//	return this.Recognizier_type;
//}
//private RecognizierSystem getRecognizier() {
//	if (recognizier == null) {
//		if (this.getRecognizierType() == TestSketchSetting.RUBINE_RECOGNIZER) {
//			//  logger.trace(" initailizing  the  rubine recognizier ");
//			recognizier = new RubineRecognizier();
//			//recognizier.init();
//			//  logger.debug(" L("	+ (new Throwable()).getStackTrace()[0].getLineNumber()+ ")" + " - " + " Rubine recognizer started");
//		} 
//		else if (this.getRecognizierType() == TestSketchSetting.SYMBOL_RECOGNIZER) {
//			
//			//  logger.trace(" initailizing  the symbol  recognizier ");
//			recognizier = new SimpleSymbolRecognizier();
//		//	recognizier.init();
//			//recognizier.setDataDisplay(getDrawingSheet());
//			//  logger.debug(" L("		+ (new Throwable()).getStackTrace()[0].getLineNumber()		+ ")" + " - " + " Symbol recognizer started");
//		}
//		else if (this.getRecognizierType() == TestSketchSetting.SVM_RECOGNIZER){
//			
//			//  logger.trace(" initailizing  the  SVM   recognizier ");
//			recognizier = new SVMRecognizier(); 
//			
//			//  logger.debug(" L("	+ (new Throwable()).getStackTrace()[0].getLineNumber()	+ ")" + " - " + " SVM recognizer started");
//		}
//
//	}
//	return recognizier;
//}
//	
//public boolean TrainFile(String file){
//		   
//		
//		boolean stop=false;
//		// open the file using xml paraser 
//		dataSet.readXML(file);
//		ArrayList<String> Cat = dataSet.getCategoriesList();
//		if (Cat==null)
//			
//		return stop;
//		if (MAX_CAT_SIZE==-1) // set to max of file if not set 
//		MAX_CAT_SIZE=Cat.size();
//		
//		updateMaxCounters(MAX_CAT_SIZE, -1);
//		//  for each type/catgory  do the following
//		logger.info(" L("
//				+ (new Throwable()).getStackTrace()[0].getLineNumber() + ")"
//				+ " - " + "  [TRAIN] the number of cateogies is  "+MAX_CAT_SIZE);
//		for (int i = 0; i < MAX_CAT_SIZE; i++) {
//			if (stop==true)
//				return stop;
//			 updateCounters(-1,i,0);
//		     // if not alread an added category add it 
//		      getRecognizier().checkAddCategory(Cat.get(i));
//		  ArrayList<ArrayList<Stroke>> Examples = dataSet.getExampleForCat(Cat.get(i));
//		//  logger.debug(" L(" 	+ (new Throwable()).getStackTrace()[0].getLineNumber() + ")"+" - "+"  adding the samples for cateogry  [TRAIN] "+Cat.get(i));
//		   //get all example into == array list of (arrylist of storkes )
//		  
//		logger.info(" L("
//				+ (new Throwable()).getStackTrace()[0].getLineNumber() + ")"
//				+ " - " + "  [TRAIN]  the cateogry  "+Cat.get(i)+  "  number  "+i+" of  "+ MAX_CAT_SIZE+"  has "+Examples.size()+" Examples");
//		CurrentCatString=Cat.get(i);
//		updateMaxCounters(MAX_CAT_SIZE,Examples.size());
//		
//		  for (int j = 0; j <Examples.size(); j++) {
//			  
//			// for each example do the following
//			  updateCounters(-1,i,j);
//			  stop=CheckSave();
//			  if (stop==true)
//				return stop;
//			  logger.info(" L("
//						+ (new Throwable()).getStackTrace()[0].getLineNumber()
//						+ ")" + " - " + "   [TRAIN]  ----------  Example number  "+j);
//					 
//					   
//		
//
//			  for (int k = 0; k < Examples.get(j).size(); k++) {
//
//				  // read all storkes for this example and do the following
//				   // add stroke to system 
//			      // segment stroke (compute initial features and run algorithms)
//			        // add stroke to cluster
//				  NewStrokeEvent newStroke = new NewStrokeEvent(this);
//					newStroke.setEventStroke(Examples.get(j).get(k));
//					newStroke.setFlagConsumed(false);
//					getRecognizier().HandleNewStroke(newStroke);
//					// compute features of the clusters 
//					// add to the dataset in recognizier for this category. 
//			}
//			// create a new cluster 
//			if (getRecognizier()instanceof  SimpleSymbolRecognizier) {
//				  ((SimpleSymbolRecognizier)(getRecognizier())).createClusterFromStrokes();
//				
//			}
//			  if (getRecognizier() instanceof SVMRecognizier) {
//				  if (USE_False_Samples){
//					  if (falseSampleID==0){
//					  // add a negtive sample for all other categoires 
//					  ((SVMRecognizier)(getRecognizier())).createClusterFromStrokes(1);
//					  }
//					  else {
//						  ((SVMRecognizier)(getRecognizier())).createClusterFromStrokes();
//					  }
//				  }
//				  else 
//					  ((SVMRecognizier)(getRecognizier())).createClusterFromStrokes();
//				
//			}
//				 
//		}
//	
//		}
//		
//		dataSet.closeFile();
//		return stop;
//	}
	
	/**
	 * @param args
	 */



	public void run() {
		RunAllFiles();
		
	}
	public void HandleNewStroke(NewStrokeEvent Evt) {
		
		Stroke stroke=sheet.PreProcessStroke(Evt.getEventStroke());
		Lastsegmentation = sheet.segmentStroke(stroke);
		sheet.addStrokeToCluster(stroke, Lastsegmentation);

		
	}
	public void createClusterFromStrokes() {

		// get a list of the storke
		// create symbol
		LastClusterDrawn = sheet.CreateNewSymbol();
		}
	
	public 	 VertixTestResultNode getDataFromSegmentation(){
		
	//	Lastsegmentation
		
		 VertixTestResultNode data=new VertixTestResultNode();
		if (Lastsegmentation instanceof StrokeCurveSolution) {
			
			StrokeCurveSolution LastsegmentationCurve =  (StrokeCurveSolution)Lastsegmentation;
			 
			
			if (LastsegmentationCurve.getPolygonVertices()!=null) 
		data.setVertixCount( 	LastsegmentationCurve.getPolygonVertices().size());
			else 
				data.setVertixCount(1);
			data.setError( LastsegmentationCurve.error() );
			data.setStrokePointsCount(LastsegmentationCurve.getProblemStroke().getPointsCount());
			data.setCountOfSegments(LastsegmentationCurve.getSegmentsCount());
			
		}
		else if (Lastsegmentation instanceof  polygonSolution){
			polygonSolution LastsegmentationCurve =  (polygonSolution)Lastsegmentation;
			;
			data.setVertixCount( 	LastsegmentationCurve.getPolygonVertices().size());
			data.setError( LastsegmentationCurve.getError());
			data.setStrokePointsCount(LastsegmentationCurve.getProblemStroke().getPointsCount());
			data.setCountOfSegments(LastsegmentationCurve.getSegmentsCount());
		}
//	else if (Lastsegmentation instanceof  polygonSolution){
//			
//			
//		}
		
		// HybirdFitSolution   //   DigitalCurveDivideSolution 
		
		return data;
	}
	public VertixTestResult getResult() {
		 
		return result;
	}


}
