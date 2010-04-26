package test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Observable;

import javax.swing.JOptionPane;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import settings.Accuracy;
import settings.TestResults;
import settings.TestSketchSetting;

import SketchMaster.Stroke.StrokeData.Stroke;
import SketchMaster.classifier.Category;
import SketchMaster.classifier.Classification;
import SketchMaster.classifier.svmTrainable.SVMClassification;
import SketchMaster.classifier.svmTrainable.TrainingSet;
import SketchMaster.gui.Events.NewStrokeEvent;
import SketchMaster.io.log.FileLog;
import SketchMaster.io.xml.XmlManager;
import SketchMaster.io.xml.XmlParser;
import SketchMaster.system.SystemSettings;
import SketchMaster.system.Recogniziers.RecognizierSystem;
import SketchMaster.system.Recogniziers.RubineRecognizier;
import SketchMaster.system.Recogniziers.SVMRecognizier;
import SketchMaster.system.Recogniziers.SimpleSymbolRecognizier;

import gui.TrainSketchSystem;



/**
 * 
 */




/**
 * @author Maha
 *
 */
public class TrainTest extends Observable implements Runnable {
	//
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(TrainTest.class);
	Logger appLogger = Logger.getLogger("AppLogging");
	
	/*
	 * 
	 * 
	 ** FATAL
    * ERROR
    * WARN
    * INFO
    * DEBUG
    * TRACE

	 * 
	 * **/

	/**
	 * @param args
	 */
	/***
	0) choose test set 
	1) read symbols from test data 
	2) now enter this data to the svm recognizier through the program 
	3) next symbol 
	4) train
	5) save training module
	6) now choose the test set
	7) apply sample see if it get the correct category. 

	****/
	
	static boolean USE_False_Samples=false;
	
	int falseSampleID=0;
	long startTimeMsFeature;// = System.nanoTime( );
	 
	long taskTimeMsFeatures=0 ; //= System.nanoTime( )- startTimeMs;
	long tempTime=0;
	long startTimeRec;
	long taskTimeRec=0;
	long startTimeSeg;
	long taskTimeSeg=0;
	int SegCount=0;
	int ExampleCount=0;
//	long 
	//startTimeMsFeature = System.nanoTime( );
//	 
//	long 
	//taskTimeMsFeatures  = System.nanoTime( )- startTimeMs;
	
	

	
	//will compute  the following data 
	int nTotalCorrect=0,nTotalFalse=0;
		//ArrayList<Integer>  nSampleCorrect=new ArrayList<Integer>();
		//ArrayList<Integer>  nSampleCount=new ArrayList<Integer>();
		//ArrayList<Integer>  nSampleFalse=new ArrayList<Integer>();
		HashMap<String, ArrayList<Integer>> categorySamples=new HashMap<String, ArrayList<Integer>>();
		HashMap<String, Accuracy> categoryAccuracy=new HashMap<String, Accuracy>();
		int nTotalCorrectThreeBest=0; // if in first thrre best.
		int nSamples=0;
		double correctPercent=0;
		double falsePercent=0;

		private int nCategory;
	// get 
	ArrayList<String>  SymbolsTypes;
	
	String FinalMessage=" ";
	
	static  boolean PauseAndSave=false;
	TestResults  result=null;
	
	String TrainingFileName;
	
	boolean PatchMode=false;
	 HandleDataSetEvents dataSet;
	 HandleDataSetEvents   errors;
	 HandleDataSetEvents  corrects;
	int errorCount=0;
	 int MaxerrorCount=600;
	 int correctCount=0;
	 int Maxcorrect=500;
	 
	

//	private Object recoglizier;
	  RecognizierSystem  recognizier=null;
	  private int Recognizier_type;
 
	
	int currentFile=0;
	int currentCat=0;
	int MaxCurrentCat=30; 
	String CurrentCatString="";
	int currentExample=0;
	int MaxCurExample=30;
	// data set type 

	
	ConfusionMatrix conf;
	
	
	static boolean displayOn=false;
	
	
	
	static int DataSetType=0  ;//   0 xml files of shapes 1 psql db  
	static int RunMode=0; //0 train 1 test.
	static int PrevTrain=0;  //0 menaing no 1 mening yes 
	boolean SaveToFile=true;
	private boolean SaveErrorCorrect=false;
	// boolean OS_Linux=false;
		private int MAX_CAT_SIZE=-1;
		
	  TrainingSet TestSet;

	private int CountTrainExamples;
	private ArrayList<String>	TrainSetfiles=null;
	ArrayList<String>  TestSetfiles=null;
	
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
			TrainSetfiles=new ArrayList<String>();
			//OS_Linux=settings.isOSLinux();
			DataSetType=settings.getDataSet();
			MAX_CAT_SIZE=settings.getMaxCatSize();
			SaveErrorCorrect=settings.isSaveErrorCorrect();
			SaveToFile=settings.isSaveToFile();
			int sizeTest=settings.getTestSize();
			ArrayList<String> files=settings.getFilesNames();
			
			for (int i = 0; i < files.size(); i++) {
				TrainSetfiles.add(files.get(i)+"");
			}
			
			 files=settings.getTestFileNames();
			for (int i = 0; i < files.size(); i++) {
				TestSetfiles.add(files.get(i)+"");
			}
			 PauseAndSave=settings.getPauseSave();
		     setRecognizier_type(settings.getRecognizierType());
		     SystemSettings.CurrentRecognizierOperation=settings.getCurrentRecognizierOperation(); 
		     RunMode=settings.getRunMode();
		     PrevTrain=settings.getPreviousTrain();
		     TrainingFileName=settings.getTestName().replace(" ", "_")+ ".smt";
		     displayOn=settings.isDisplaySketch();
		     if (RunMode==TestSketchSetting.RUN_MODE_TEST)
		     {
		    	 System.out.println("intalize the reslut. "+" ("
						+ this.getClass().getSimpleName() + "    "
						+ (new Throwable()).getStackTrace()[0].getLineNumber()
						+ "  )  ");
		    	 result=new TestResults();
		    	result.setTestName(settings.getTestName()) ;
		    	result.setNFiles(settings.getTestSize());
		    	
		  	  if (SaveToFile)
			  {
				   TestSet=new 	  TrainingSet() ;
				  }
		    	
		    	 
		     }
		}
	
		else 
		Defaults();
		
	}
	
	
	public void Defaults(){
		//  logger.debug(" L("  	+ (new Throwable()).getStackTrace()[0].getLineNumber() + ")"+" - "	+"Loading the defaults");
//		logger.info("Loading the defaults ");
//		logger.warn("this is a warinng ");
//		logger.error("this is a error ");
		TrainSetfiles=new ArrayList<String>();
		TestSetfiles=new ArrayList<String>();
//		if (OS_Linux){
//			TestSetfiles.add("/windows/D/sketch/other systems/source/hhreco/data/data/user1.sml");
//			TestSetfiles.add("/windows/D/sketch/other systems/source/hhreco/data/data/user2.sml");
////			TestSetfiles.add("/windows/E/sketch/other systems/source/hhreco/data/data/user3.sml");
////			TestSetfiles.add("/windows/E/sketch/other systems/source/hhreco/data/data/user4.sml");
////			
//	}
//		else{ 
		TrainSetfiles.add("F:\\sketch\\other systems\\source\\hhreco\\data\\data\\user13.sml");
//		TestSetfiles.add("F:\\sketch\\other systems\\source\\hhreco\\data\\data\\user2.sml");
//		TestSetfiles.add("F:\\sketch\\other systems\\source\\hhreco\\data\\data\\user3.sml");
//		TestSetfiles.add("F:\\sketch\\other systems\\source\\hhreco\\data\\data\\user4.sml");
//		TestSetfiles.add("F:\\sketch\\other systems\\source\\hhreco\\data\\data\\user5.sml");
//		TestSetfiles.add("F:\\sketch\\other systems\\source\\hhreco\\data\\data\\user11.sml");
//		TestSetfiles.add("F:\\sketch\\other systems\\source\\hhreco\\data\\data\\user7.sml");
//		TestSetfiles.add("F:\\sketch\\other systems\\source\\hhreco\\data\\data\\user8.sml");
//		TestSetfiles.add("F:\\sketch\\other systems\\source\\hhreco\\data\\data\\user9.sml");
		TestSetfiles.add("F:\\sketch\\other systems\\source\\hhreco\\data\\data\\user10.sml");
//	
//		}
		//TestSetfiles.add("F:\\sketch\\other systems\\source\\hhreco\\data\\data\\user5.sml");
		//TestSetfiles.add("F:\\sketch\\other systems\\source\\hhreco\\data\\data\\user6.sml");
		DataSetType=TestSketchSetting.DATA_SET_TYPE_XML;  //xml files 
		//RunMode=0;
    
     PauseAndSave=false;
     setRecognizier_type(TestSketchSetting.SYMBOL_RECOGNIZER);
		if (PrevTrain==TestSketchSetting.PREV_NO_TRAIN){
			     Date d=new Date();
			     SimpleDateFormat formatter=new SimpleDateFormat("dd-MM-yyyy_hh-mm");
			    // formatter.applyPattern("y");
			     formatter.format(d);
			     String dat=formatter.format(d);
			     String fileName="train_Symbol_"+dat+"_Size_"+TrainSetfiles.size();
			     TrainingFileName=fileName+".smt";
		
		}
		logger.info(" L("
				+ (new Throwable()).getStackTrace()[0].getLineNumber() + ")"
				+ " - " + " [TRAIN] The training will be saved into  "+TrainingFileName);
		
	}

	public void initRunPatch(TestSketchSetting settings){
	//	 logger.setLevel(Level.INFO);
		 CountTrainExamples=0;
		readSettings(settings);
		// now dipslay all setting 
		
		appLogger.info(" [Settings]  inside run the settings is "+settings.toString() );
		 getRecognizier().init();
		 getRecognizier().setCapturesStroke(true);
		 
		 if (PrevTrain==1){
			 // read the training files if there is one. 
			 getRecognizier().ReadTrainingSet(TrainingFileName);
			 }
		 
			dataSet=new HandleDataSetEvents(); 
		// this.run();

		 
	}
	
	public void initSystem(){
		readSettings (null);
		// init classifier and recognizier system 
		 getRecognizier().init();
		 getRecognizier().setCapturesStroke(true);
		 if (PrevTrain==TestSketchSetting.PREV_TRAINING){
		 // read the training files if there is one. 
		 getRecognizier().ReadTrainingSet(TrainingFileName);
		 }
		 
		 if (RunMode==TestSketchSetting.RUN_MODE_TRAIN)
		 {
			 SystemSettings.CurrentRecognizierOperation=RecognizierSystem.RECGONIZE_OPERATION_TRAIN; 
			
			 
		 }
		 else {
			  SystemSettings.CurrentRecognizierOperation=RecognizierSystem.RECGONIZE_OPERATION_CLASSIFY;
				
		 }
		// init drawing sheet if display out
		logger.info(" L("
				+ (new Throwable()).getStackTrace()[0].getLineNumber() + ")"
				+ " - " + " [TRAIN] do the following [init drawing sheet if display out ] ");
		
		//
		// init the event handler (that will invoke that will simulate the stroke and cluster addition)
		
		// set any other settings used in testing 
		dataSet=new HandleDataSetEvents(); 
		
		// if test not train ==> load the train data to recognizier. 

	}
	
	public boolean TrainFile(String file){
		dataSet=new HandleDataSetEvents(); 
		
		boolean stop=false;
		// open the file using xml paraser 
		dataSet.readXML(file);
		   logger.error("reading file "+file);
		ArrayList<String> Cat = dataSet.getCategoriesList();
		if (Cat==null)
			
		return stop;
		//if (MAX_CAT_SIZE==-1) // set to max of file if not set 
		MAX_CAT_SIZE=Cat.size();
		
		updateMaxCounters(MAX_CAT_SIZE, -1);
		//  for each type/catgory  do the following
		logger.error(" L("
				+ (new Throwable()).getStackTrace()[0].getLineNumber() + ")"
				+ " - " + "  [TRAIN] the number of cateogies is  "+MAX_CAT_SIZE);
		for (int i = 0; i < MAX_CAT_SIZE; i++) {
			if (stop==true)
				return stop;
			
			
			 updateCounters(-1,i,0);
		     // if not alread an added category add it 
		      getRecognizier().checkAddCategory(Cat.get(i));
		  ArrayList<ArrayList<Stroke>> Examples = dataSet.getExampleForCat(Cat.get(i));
		//  logger.debug(" L(" 	+ (new Throwable()).getStackTrace()[0].getLineNumber() + ")"+" - "+"  adding the samples for cateogry  [TRAIN] "+Cat.get(i));
		   //get all example into == array list of (arrylist of storkes )
		  
		logger.info(" L("
				+ (new Throwable()).getStackTrace()[0].getLineNumber() + ")"
				+ " - " + "  [TRAIN]  the cateogry  "+Cat.get(i)+  "  number  "+i+" of  "+ MAX_CAT_SIZE+"  has "+Examples.size()+" Examples");
		CurrentCatString=Cat.get(i);
		updateMaxCounters(MAX_CAT_SIZE,Examples.size());
		
		  for (int j = 0; j <Examples.size(); j++) {
			  CountTrainExamples++;
			// for each example do the following
			  updateCounters(-1,i,j);
			  stop=CheckSave();
			  if (stop==true)
				return stop;
			  logger.info(" L("
						+ (new Throwable()).getStackTrace()[0].getLineNumber()
						+ ")" + " - " + "   [TRAIN]  ----------  Example number  "+j+"  total number of count is "+CountTrainExamples);
					 
					   
		

			  for (int k = 0; k < Examples.get(j).size(); k++) {

				  // read all storkes for this example and do the following
				   // add stroke to system 
			      // segment stroke (compute initial features and run algorithms)
			        // add stroke to cluster
				  NewStrokeEvent newStroke = new NewStrokeEvent(this);
					newStroke.setEventStroke(Examples.get(j).get(k));
					newStroke.setFlagConsumed(false);
					getRecognizier().HandleNewStroke(newStroke);
					// compute features of the clusters 
					// add to the dataset in recognizier for this category. 
			}// after all strokes in examples
			// create a new cluster 
			if (getRecognizier()instanceof  SimpleSymbolRecognizier) {
				  ((SimpleSymbolRecognizier)(getRecognizier())).createClusterFromStrokes();
				
			}
			  if (getRecognizier() instanceof SVMRecognizier) {
				  if (USE_False_Samples){
					  if (falseSampleID==0){
					  // add a negtive sample for all other categoires 
					  ((SVMRecognizier)(getRecognizier())).createClusterFromStrokes(1);
					  }
					  else {
						  ((SVMRecognizier)(getRecognizier())).createClusterFromStrokes();
					  }
				  }
				  else 
					  ((SVMRecognizier)(getRecognizier())).createClusterFromStrokes();
				
			}
				 
		}
	
		}
		
		dataSet.closeFile();
		return stop;
	}
	
	private boolean CheckSave() {
	 if(this.PauseAndSave==true)
	 {
		 System.out.println("save the file and stop all the running "+" (" + this.getClass().getSimpleName()
				+ "    " + (new Throwable()).getStackTrace()[0].getLineNumber()
				+ "  )  ");
		 return true;
	 }
		return false;
	}


	public void TrainFiles(){
		String Messg;
		
		int catSize=0;
		int avgExampleSize=0;
		 logger.setLevel(Level.INFO);
		logger.info("   Training using   "+TrainSetfiles.size()+"  files.. ");
		// loop on the list of file strings  TestSetfiles
		for (int i = 0; i < TrainSetfiles.size(); i++) {
			 Messg=" L("  
					+ (new Throwable()).getStackTrace()[0].getLineNumber() + ")"+" - "
					+"  [TRAIN]  traning file number   "+i;
			System.out.println(Messg );
			appLogger.info(Messg);
			logger.info(Messg);
			//for each file do the following 
			// trainFile
			updateCounters(i,0,0);
			 logger.setLevel(Level.ERROR);
			boolean stop=TrainFile(TrainSetfiles.get(i));
			 logger.setLevel(Level.INFO);
			catSize+=MAX_CAT_SIZE;
			// // if train individually then train 
			// else loop again 
			if (falseSampleID==0)
				falseSampleID=1;
			else if (falseSampleID==1)
				falseSampleID=0;
			if (stop==true)
				break;
			// after all train all 
			// save train data to disk in save location (from settings)
		}
		Messg=" L("  
			+ (new Throwable()).getStackTrace()[0].getLineNumber() + ")"+" - "
			+" Now will train the system.     [TRAIN] ";
		System.out.println(Messg );
		appLogger.info(Messg);
		logger.info(Messg);
		catSize/=(double)TrainSetfiles.size();
		appLogger.info("  average number of categories is "+catSize+"  with total number of samples is "+CountTrainExamples);
		
		//logger.error();
		getRecognizier().SaveTrainingSet(TrainingFileName);
	//	if (g)
		if (SaveToFile){
		if (getRecognizier() instanceof SVMRecognizier) {
			SVMRecognizier reg = (SVMRecognizier) getRecognizier();
			reg.SaveTrainingSetArffFile(TrainingFileName);
		}
		}
		 
//		getRecognizier()
		
		FinalMessage="Finished training all files and saved into system.     [TRAIN] ";
		
		
		 if (!PatchMode)
			 JOptionPane.showMessageDialog(null, FinalMessage);
		 else {
			 System.out.println(FinalMessage );
				appLogger.info(FinalMessage);
				logger.info(FinalMessage);
		 }
			
	}

	
	public boolean TestFile(String filename){
		
		boolean stop=false;
		
	
		dataSet=new HandleDataSetEvents(); 
 		// i will do this algorithm 
		
	// open the file using xml paraser 
 	// open the file using xml paraser 
		dataSet.readXML(filename);
		   logger.error("reading file "+filename);
		ArrayList<String> Cat = dataSet.getCategoriesList();
		if (!conf.isInit()){
			conf.initClases(Cat);
		}
		if (Cat==null)
			return stop;
		
		//if (MAX_CAT_SIZE==-1) // set to max of file if not set 
			MAX_CAT_SIZE=Cat.size();
		
		updateMaxCounters(MAX_CAT_SIZE, -1);
		// for each cateogry do the following 
		for (int i = 0; i < MAX_CAT_SIZE; i++) {
			if (stop==true)
				return stop;
			 updateCounters(-1,i,0);
			  ArrayList<ArrayList<Stroke>> TestSamples = dataSet.getExampleForCat(Cat.get(i));	  
			  
			  
//			  logger.error(" ggGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG" + "  [TEST]  the cateogry  "+Cat.get(i)+  "  number  "+i+" of  "+ MAX_CAT_SIZE+"  has "+
//						TestSamples.size()+" Examples");
			  CurrentCatString=Cat.get(i);
			  
			  // for each catgory add a correct and false sample ==0
			  int nSampleCorrectTemp=0;
			  int nSampleFalseTemp=0;
		
			  // add the count of samples of this category. 
			//  nSampleCount.add(new Integer(TestSamples.size()));
				updateMaxCounters(MAX_CAT_SIZE,TestSamples.size());
				
			  
		   //get all example into == array list of (arrylist of storkes )
		      // for each example do the following 
			  for (int j = 0; j < TestSamples.size(); j++){
				  
				  
				  logger.info(" L("
							+ (new Throwable()).getStackTrace()[0].getLineNumber()
							+ ")" + " - " + "   [TEST] %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%Example number  "+j);
		            // increase sample count
				  updateCounters(-1,i,j);
				  nSamples++;
				  
				  stop=CheckSave();
				  if (stop==true)
					  return stop;
				  for (int k = 0; k < TestSamples.get(j).size(); k++) {
		           // read all storkes for this example and do the following
		                // add stroke to system 
					  NewStrokeEvent newStroke = new NewStrokeEvent(this);
						newStroke.setEventStroke(TestSamples.get(j).get(k));
						newStroke.setFlagConsumed(false);
						  startTimeSeg = System.nanoTime( );
						getRecognizier().HandleNewStroke(newStroke);
						tempTime= System.nanoTime()- startTimeSeg ;
						taskTimeSeg+=tempTime;
						SegCount++;
		                // segment stroke (compute initial features and run algorithms)
		                  // add stroke to cluster
		          // compute features of the clusters 
					  
					  
				  }
			        String firstName="";
				  if (getRecognizier() instanceof SimpleSymbolRecognizier) {
					  SimpleSymbolRecognizier syReg = (SimpleSymbolRecognizier)getRecognizier();
					  syReg.createClusterFromStrokes();
					  Classification classification =  syReg.getCurrentClassification();
					  
						// create a new cluster 
					    //try to classify the sample 
					 			          
			               Category first=(Category) classification.getLastKey();
			               firstName=first.getCategoryName();
			               logger.warn("   the first key is "+ classification.getFirstKey()+ "   get the last key "+classification.getLastKey()+  "   the largest key is "+ classification.getLargestKey()+   "     smallest  "+classification.getSmallestKey() );
				           logger.warn("   the first key is "+ classification.getFirstValue()+ "   get the last key "+classification.getLastValue()+  "   the largest key is "+ classification.getLargestValue()+   "     smallest  "+classification.getSmallestValue() );
				}
				  else if (getRecognizier() instanceof SVMRecognizier){
					  
					  SVMRecognizier syReg = (SVMRecognizier)getRecognizier();
					  startTimeMsFeature = System.nanoTime( );
					  syReg.createClusterFromStrokes();
					  tempTime= System.nanoTime()-startTimeMsFeature;
					  taskTimeMsFeatures+=tempTime;
					  ExampleCount++;
					  
					  if (SaveToFile)
					  {
						 // TrainingSet TestSet=new 	  TrainingSet() ;
						  TestSet.addExample(Cat.get(i), syReg.getLastDrawnfeats(), 0);
						  
					  }
					  startTimeRec= System.nanoTime( );
					  SVMClassification classification = (SVMClassification) syReg.getCurrentClassification();
					  tempTime= System.nanoTime()-startTimeRec;
					  taskTimeRec+=tempTime;
					  firstName=classification.getHighestConfidenceType();
					  logger.warn("   Here is the classiffication    "+classification);
					  
					  
				  }
		               ////now if supposed to be cat.get and it is firstname
				  
				  ProcessAccuarcy(Cat,firstName,i);
				  ////now if supposed to be cat.get and it is firstname
				  
				  //
		             //  logger.error("   CurrentCatString   "+ CurrentCatString  + " and it was classified as "+first.getCategoryName());
		               if (firstName.equals(Cat.get(i))){
		            	  
		            	   //correct classification 
		            	   nSampleCorrectTemp++;
		            	   nTotalCorrect++;
		            	   if (SaveErrorCorrect){
		         			  
		         			if(correctCount<Maxcorrect){
		         				//corrects.a
		         				corrects.AddListToCat(Cat.get(i),  TestSamples.get(j));
		         				correctCount++;
		         			}  
		         		  }
		               }
		               else{
		            //	     logger.error(" [TEST]  " + " - " + "  classification is "+classification.toString());
				            //   logger.warn("   the first key is "+ classification.getFirstKey()+ "   get the last key "+classification.getLastKey()+  "   the largest key is "+ classification.getLargestKey()+   "     smallest  "+classification.getSmallestKey() );
				             //  logger.warn("   the first key is "+ classification.getFirstValue()+ "   get the last key "+classification.getLastValue()+  "   the largest key is "+ classification.getLargestValue()+   "     smallest  "+classification.getSmallestValue() );
		             	   if (SaveErrorCorrect){
			         			  
			         			if(errorCount<MaxerrorCount){
			         				//corrects.a
			         				errors.AddListToCat(Cat.get(i),  TestSamples.get(j));
			         				errorCount++;
			         			}  
			         		  }
		            	   nTotalFalse++;
		            	   nSampleFalseTemp++;
		               }
		             //check is result is same result as type 
		           // if correct increase number of correcct recognitios
	       				// increase  correct number of recognition for this type 
		           // if not try to check if correct is in first 3 solutions  and count that number 
		    			//increase number of false recgnotion as total and for this category. 
				  
				  
			  }
			  
			
			  if (	  categorySamples.containsKey(Cat.get(i) ))
			  {
				//  System.out.println(" Repeatedd................."+" ("
//						+ this.getClass().getSimpleName() + "    "
//						+ (new Throwable()).getStackTrace()[0].getLineNumber()
//						+ "  )  ");
				
				  ArrayList<Integer> tempSamples= categorySamples.get(Cat.get(i));
				  int temp1=tempSamples.get(0);
				  tempSamples.set(0, new Integer(tempSamples.get(0)+TestSamples.size()));
				  tempSamples.set(1, new Integer(tempSamples.get(1)+nSampleCorrectTemp));
				  tempSamples.set(2, new Integer(tempSamples.get(2)+nSampleFalseTemp));
				  categorySamples.put(Cat.get(i), tempSamples);
				  
			  }
			  else {
//				  System.out.println(Cat.get(i)+" ("
//							+ this.getClass().getSimpleName() + "    "
//							+ (new Throwable()).getStackTrace()[0].getLineNumber()
//							+ "  )  ");
				  ArrayList<Integer> tempSamples=new ArrayList<Integer>();
				  tempSamples.add(new Integer(TestSamples.size()));
				  tempSamples.add(new Integer(nSampleCorrectTemp) );
				  tempSamples.add(new Integer(nSampleFalseTemp));
				  
				  categorySamples.put(Cat.get(i), tempSamples);
			  }
//			  nSampleCorrect.add(new Integer(nSampleCorrectTemp));
//			  nSampleFalse.add(new Integer(nSampleFalseTemp));
			  
			  
		              
		}
		correctPercent=	(double)	nTotalCorrect/    (double)nSamples;       
		falsePercent=  (double)nTotalFalse/    (double)nSamples;   
		return stop;
	}
private void AddAccurcayToCat(String cat,int TP,int TN,int FN,int FP ){
	
	
	  if (	  categoryAccuracy.containsKey(cat))
	  {
		  Accuracy tempACC= categoryAccuracy.get(cat);
			tempACC.FalseNegative+=FN;
			tempACC.FalsePositive+=FP;
			tempACC.TrueNegative+=TN;
			tempACC.TruePositive+=TP;
		  
	  }
	  else {
 
		  Accuracy tempACC=new Accuracy();
		tempACC.setClassName(cat);
		tempACC.FalseNegative=FN;
		tempACC.FalsePositive=FP;
		tempACC.TrueNegative=TN;
		tempACC.TruePositive=TP;
		
		categoryAccuracy.put(cat, tempACC);
	  }
	
	
}
	
   private void ProcessAccuarcy(ArrayList<String> cat, String firstName,int i) {
	   
	   /// this confusion matrix..........
	   
	   

	//if firstname == cat.geti
	   if (firstName.equals(cat.get(i))){
		conf.AddCorrectSample(firstName);   
		 
		   AddAccurcayToCat(cat.get(i),1,0,0,0);
		   
		   for (int j = 0; j < cat.size(); j++) {
			if (i!=j){
				AddAccurcayToCat(cat.get(j),0,1,0,0);
				
				
			}
		}
		   
		   
	   }
	   else {
		   conf.AddSample(firstName,cat.get(i));
		   for (int j = 0; j < cat.size(); j++) {
			   
			   if (i==j){ //suposed be i  but it is not then it is wrong not to recognized. 
				   
				   //if firename is reccongized as cat not i then 
				   //then is is a  False Negative 
				   AddAccurcayToCat(cat.get(j),0,0,1,0);
				   
			   }
			   else   if (j!=i){
				   
				   if (firstName.equals(cat.get(j))){
					   //false postive a wrong recognition reslut 
					   AddAccurcayToCat(cat.get(j),0,0,0,1);
				   }
				   else {
					   //ture negative. 
					   AddAccurcayToCat(cat.get(j),0,1,0,0);
				   }
				   
				   
			   }
			
			
		}
	 
	   
	   }
	}


public void TestAllFiles(){
	long startTimeMsFeature;// = System.nanoTime( );
	 
	 taskTimeMsFeatures=0 ; //= System.nanoTime( )- startTimeMs;
	tempTime=0;
	 startTimeRec=0;
	taskTimeRec=0;
startTimeSeg=0;
	taskTimeSeg=0;
	SegCount=0;
	ExampleCount=0;
		 nTotalCorrect=0;nTotalFalse=0;
//	 nSampleCorrect=new ArrayList<Integer>();
//	  nSampleCount=new ArrayList<Integer>();
//  nSampleFalse=new ArrayList<Integer>();
		 conf=new ConfusionMatrix();
		 categorySamples=new HashMap<String, ArrayList<Integer>>();
		 categoryAccuracy=new HashMap<String, Accuracy>();
		 if (SaveErrorCorrect)
	  {
			 errors=new HandleDataSetEvents ();
			 corrects=new HandleDataSetEvents ();
				correctCount=0;
				errorCount=0;
				errors.initData();
				corrects.initData();
	  }
	

		 
		 
	 nTotalCorrectThreeBest=0; // if in first thrre best.
		 nSamples=0;
		 correctPercent=0;
	 falsePercent=0;
	   //check train data is all loadded and trained.
	   
	
	//   getRecognizier().train();
	   nCategory= getRecognizier().getCategoryCount();
	   
	   //looad any extra settind for the system. 
	
//	  SystemSettings.CurrentRecognizierOperation=RecognizierSystem.RECGONIZE_OPERATION_CLASSIFY;
	
		 	 logger.setLevel(Level.INFO);
				logger.info("    %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%   ");
				logger.info(" [TEST] testing using    "+TestSetfiles.size()+"    files   ");
		for (int i = 0; i < TestSetfiles.size(); i++) {
			// loop on the list of file strings  TestSetfiles
			
			//for each file do the following 
		 
			logger.info(" [TEST] "+" - "+"  testing file number   "+i);
			//test the file 
			//  teste file
			updateCounters(i,0,0);	 
			logger.setLevel(Level.ERROR);
			boolean stop=TestFile(TestSetfiles.get(i));
						 logger.setLevel(Level.INFO);
			if (stop==true)
				break;
			logger.info(" [TEST] [RESULT]  " + " - " + "  the test result till now is correctPercent  "+correctPercent
					+"  where number of sampels is  "+nSamples);
			
		}
			
		
		if (SaveToFile){
			if (getRecognizier() instanceof SVMRecognizier) {
				
				   TestSet.SaveToArrffFile(TrainingFileName+"_Test_set");
				
			}
			}
		 if (SaveErrorCorrect)
		  {
			 if (errors!=null)
			 errors.SaveXML(TrainingFileName+"ErrorOfTest"+".xml");
			 if (corrects!=null)
			 corrects.SaveXML(TrainingFileName+"CorrectsOfTest"+".xml");
		  }
	   
		if (result!=null)
		{
			result.setCorrectSamples(correctPercent);
			result.setFalseSamples(falsePercent);
			result.setNSamples(nSamples); //samples in category
			result.setNCategory(nCategory);
			result.setCategoryResults(this.categorySamples);
			Collection<Accuracy> acc= this.categoryAccuracy.values();
			for (Iterator iterator = acc.iterator(); iterator.hasNext();) {
				Accuracy accitem= (Accuracy) iterator.next();
				 accitem.computeStat();
				 logger.info(" The accuracy of the category " + accitem.getClassName() +" is ");
				 logger.info(accitem.toString());
				
				
			}
			result.setCategoryAccuracies(categoryAccuracy  );
			result.setConfusion(conf);
			result.setSegCount(SegCount);
			result.setTaskTimeMsFeatures(taskTimeMsFeatures);
			result.setTaskTimeRec(taskTimeRec);
			result.setTaskTimeSeg(taskTimeSeg);
		    result.setExampleTimeCount(ExampleCount);
		}
	
	
	   //finally display final test recognition. 
		FinalMessage="*******************Final RESULT is      correct percent is   "+(correctPercent*100)+"       with nsamples   "+nSamples  +" and false sample is    "+(falsePercent*100);
	
	 if (!PatchMode)
		 JOptionPane.showMessageDialog(null, FinalMessage);
	 else 
			logger.warn(FinalMessage); 
		
   }
 
   private RecognizierSystem getRecognizier() {
		if (recognizier == null) {
			if (this.getRecognizierType() == TestSketchSetting.RUBINE_RECOGNIZER) {
				//  logger.trace(" initailizing  the  rubine recognizier ");
				recognizier = new RubineRecognizier();
				//recognizier.init();
				//  logger.debug(" L("	+ (new Throwable()).getStackTrace()[0].getLineNumber()+ ")" + " - " + " Rubine recognizer started");
			} 
			else if (this.getRecognizierType() == TestSketchSetting.SYMBOL_RECOGNIZER) {
				
				//  logger.trace(" initailizing  the symbol  recognizier ");
				recognizier = new SimpleSymbolRecognizier();
			//	recognizier.init();
				//recognizier.setDataDisplay(getDrawingSheet());
				//  logger.debug(" L("		+ (new Throwable()).getStackTrace()[0].getLineNumber()		+ ")" + " - " + " Symbol recognizer started");
			}
			else if (this.getRecognizierType() == TestSketchSetting.SVM_RECOGNIZER){
				
				//  logger.trace(" initailizing  the  SVM   recognizier ");
				recognizier = new SVMRecognizier(); 
				
				//  logger.debug(" L("	+ (new Throwable()).getStackTrace()[0].getLineNumber()	+ ")" + " - " + " SVM recognizer started");
			}

		}

		return recognizier;
	}
   private int getRecognizierType() {
		return this.getRecognizier_type();
	}


/**
 * @return the prevTrain
 * 
 */
public static int getPrevTrain() {
	return PrevTrain;
}


/**
 * @param prevTrain the prevTrain to set
 */
public static void setPrevTrain(int prevTrain) {
	PrevTrain = prevTrain;
}


/**
 * @return the trainingFileName
 */
public String getTrainingFileName() {
	return TrainingFileName;
}


/**
 * @param trainingFileName the trainingFileName to set
 */
public void setTrainingFileName(String trainingFileName) {
	TrainingFileName = trainingFileName;
}


/**
 * @return the pauseAndSave
 */
public static boolean isPauseAndSave() {
	return PauseAndSave;
}


/**
 * @param pauseAndSave the pauseAndSave to set
 */
public static void setPauseAndSave(boolean pauseAndSave) {
	PauseAndSave = pauseAndSave;
}

 


	

public void run() {
	if (RunMode== TestSketchSetting.RUN_MODE_TRAIN)
	{
		TrainFiles();
	}
	else {
		TestAllFiles();
	}
	
	
	
}


private void updateMaxCounters(int Cat,int Ex){
	if (Ex!=-1)
	MaxCurExample=Ex;
	if (Cat!=-1)
	MaxCurrentCat=Cat;
	this.setChanged();
	this.notifyObservers();
	
}
private void updateCounters(int file,int Cat,int Ex){
	currentCat=Cat;
	if (file!=-1)
	currentFile=file;
	currentExample=Ex;
	
	this.setChanged();
	this.notifyObservers();
}
public int getNoOfRunFiles() {
	if (RunMode== TestSketchSetting.RUN_MODE_TRAIN)
	{
	return TrainSetfiles.size();
	}
	else {
		
		return TestSetfiles.size();
	}
}


/**
 * @return the currentFile
 */
public int getCurrentFile() {
	return currentFile;
}


/**
 * @return the currentCat
 */
public int getCurrentCat() {
	return currentCat;
}


/**
 * @return the maxCurrentCat
 */
public int getMaxCurrentCat() {
	return MaxCurrentCat;
}


/**
 * @return the currentCatString
 */
public String getCurrentCatString() {
	return CurrentCatString;
}


/**
 * @return the currentExample
 */
public int getCurrentExample() {
	return currentExample;
}


/**
 * @return the maxCurExample
 */
public int getMaxCurExample() {
	return MaxCurExample;
}


public void setRunMode(int i) {
 this.RunMode=i;
	
}


/**
 * @return the finalMessage
 */
public String getFinalMessage() {
	return FinalMessage;
}


public TestResults getResult() {
	
	return   result;
}


/**
 * @return the mAX_CAT_SIZE
 */
public int getMAX_CAT_SIZE() {
	return MAX_CAT_SIZE;
}


/**
 * @param max_cat_size the mAX_CAT_SIZE to set
 */
public void setMAX_CAT_SIZE(int max_cat_size) {
	MAX_CAT_SIZE = max_cat_size;
}


/**
 * @param recognizier_type the recognizier_type to set
 */
public void setRecognizier_type(int recognizier_type) {
	Recognizier_type = recognizier_type;
}


/**
 * @return the recognizier_type
 */
public int getRecognizier_type() {
	return Recognizier_type;
}
}
