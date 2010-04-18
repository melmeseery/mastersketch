package settings;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import SketchMaster.lib.GeneralUtils;
import SketchMaster.system.SystemSettings;



import test.TestVertices;
import test.TrainTest;


public class PatchTestSetFromFiles implements Serializable, Runnable {
	private static transient final Logger	logger					= Logger
																			.getLogger(PatchTestSet.class);
	private static transient final Logger	appLogger				= Logger
																			.getLogger("AppLogging");
	static int								DATA_HSE				= 1;
	static int								DATA_MINE_ELECTERICAL	= 2;
	static int								DATA_MINE_DIGITAL		= 3;
	static int								DATA_MINE_ELECTERICAL_R	= 4;
	static int								DATA_MINE_DIGITAL_R		= 5;
	static int								DATA_MINE_SWARM_STROKES	= 9;
	static int								MaxRunsWithoutSave		= 50;
	private int								DataBase=DATA_HSE;
	ArrayList<TestResults>					resultList;
	ArrayList<TestSketchSetting>			testList;
	static boolean							currentStop=false;
	TrainTest								t;
	TestVertices							v;
	private static String settingFilename="set.txt";
	private ArrayList<VertixTestResult>		resultListVertices;
	static ArrayList<String>				TestFilenames			= new ArrayList<String>();
	static ArrayList<String>				TrainFilenames			= new ArrayList<String>();
	static int								averageRunNo			= 1;
	static int								CurrentFilesMaxNumber	= 8;									// subtract
																											// 1
																											// to
																											// total
	private final	 int OPERATION_RUN_ALL=0;
	private final	 int OPERATION_RUN_DEC_SIZE=1;																										// number
																											// of
																											// files
	private int								TotalFileMaxNumber=20;
	static boolean							testVertices			= false;
	static boolean							StoreOnlyFinalSheet		= true;
	private int								OperatonCode=OPERATION_RUN_ALL;
	private boolean							RunForAllDataSets		= false;
	private boolean							CountChanged=false;
	HashMap<String, Integer>				catCounts;
	
	
	private boolean							UseLeaveOneOut			= false;
	private boolean							UseSeventyPercent		= true;
	private boolean							UseFifthOne				= false;
	
	private boolean reGenerateTrainFilesNames=false;
	Random									r1Random				= new Random(
																			600);
	
	
	
	
	//private int nTestSettingFiles;
	private ArrayList<String>  confTestFiles=null;
	
	
 
	

	public PatchTestSetFromFiles() {
		logger.setLevel(Level.ALL);
		t = new TrainTest();
		v = new TestVertices();
	 
	}

	public  void saveSettings(String Filename) {
		FileOutputStream file;
		PrintStream out; // declare a print stream object
		try {
			// Create a new file output stream
			file = new FileOutputStream(Filename);
			// Connect print stream to the output stream
			out = new PrintStream(file);
			// wirte the type of database
			out.println("##  DataBase used   HSE "+this.DATA_HSE+"   dgital is "+this.DATA_MINE_DIGITAL+"   digital R  "+this.DATA_MINE_DIGITAL_R);
			out.println("##  DataBase used    MINE SWARM IS  "+ DATA_MINE_SWARM_STROKES+"   electrical is "+this.DATA_MINE_ELECTERICAL+"   electrical  R "+this.DATA_MINE_ELECTERICAL_R);
			out.println("DataBase");
			out.println(DataBase);
			
			out.println("## OperatonCode   = code of  OPERATION_RUN_ALL   "+OPERATION_RUN_ALL+"  OPERATION_RUN_DEC_SIZE    "+OPERATION_RUN_DEC_SIZE);
			out.println("OperatonCode");
			out.println(this.OperatonCode);
			
			
					
			out.println("## averageRunNo	  == >	average number of runs " );
			out.println("averageRunNo");
			out.println( 	averageRunNo		);	
			out.println("##  CurrentFilesMaxNumber  " );
			out.println("CurrentFilesMaxNumber");
			out.println(	 CurrentFilesMaxNumber);	
				out.println("##   	testVertices	" );
			out.println("testVertices");
			out.println( testVertices	);
					out.println("## StoreOnlyFinalSheet" );
			out.println("StoreOnlyFinalSheet");
			out.println(StoreOnlyFinalSheet );	
			
			
			out.println("##UseLeaveOneOut" );
			out.println("UseLeaveOneOut");
			out.println(UseLeaveOneOut);	
			out.println("##  UseSeventyPercent	 " );
			out.println("UseSeventyPercent");
			out.println( UseSeventyPercent	);	
				out.println("## UseFifthOne" );
			out.println("UseFifthOne");
			out.println( UseFifthOne);			
			out.println("##   	reGenerateTrainFilesNames" );
			out.println("reGenerateTrainFilesNames");
			out.println( reGenerateTrainFilesNames);			
		 
			
			out.println("##  MaxRunsWithoutSave " );
			out.println("MaxRunsWithoutSave");
			out.println( MaxRunsWithoutSave);	
			
			if ( confTestFiles!=null){
				out.println("##----------------Now configurations namesssssss" );
			out.println("confTestFiles");
			out.println("##first size then the  files ");
//			out.println("size");
//			out.println( confTestFiles.size());
//			out.println("####Filessss");
//			out.println("Files");
			for (int i = 0; i < confTestFiles.size(); i++) {
				//out.println("###File number "+i);
				out.println( confTestFiles.get(i));
			}
			out.println("FINISH");
			
			
			}
			out.println("### Finish....");
			out.println("## This is The end of file.......");
			
			 //  TotalFileMaxNumber=getDataBaseMaxCount();
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error in writing to file");
		}
		logger.info("Finished writing the settings..........");
		
	}

	public void readFromFile(String filename) {
		// i will use same configration files as in the digit recognition ....

		
		try {
			logger.info("reading the file................ wait");
			File afile = new File(filename);
			Scanner input = new Scanner(new BufferedReader(
					new FileReader(afile)));
			String inputString = "", inputStringInner;
			 int inputInt;
			// boolean finishClassifier=false,finishRegion=false;

			while (input.hasNext()) {
				inputString = input.nextLine();
				if (inputString.startsWith("##"))
					continue;

				// now check for the following
				if (inputString.trim().startsWith("DataBase")) {
					inputInt = input.nextInt();
	 
				 this.DataBase=inputInt;
				 logger.info("Reading database "+DataBase);
					
				}
				
				
				if (inputString.trim().startsWith("OperatonCode")) {
					inputInt = input.nextInt();
					OperatonCode=inputInt;
					 logger.info(	"	OperatonCode  = "+OperatonCode);
				}
				
				 
						if (inputString.trim().startsWith("averageRunNo")) {
					inputInt = input.nextInt();
	 
				 	averageRunNo	=inputInt;	
				 	 logger.info("	averageRunNo  "+	averageRunNo);
						}
				if (inputString.trim().startsWith("CurrentFilesMaxNumber")) {
					inputInt = input.nextInt();
					CurrentFilesMaxNumber=inputInt ;	 logger.info("  CurrentFilesMaxNumber  "+CurrentFilesMaxNumber );
				}
				
					if (inputString.trim().startsWith("testVertices")) {
					testVertices	=input.nextBoolean();	 logger.info("   testVertices  " +testVertices);
				}
					 
						if (inputString.trim().startsWith("StoreOnlyFinalSheet")) {
					StoreOnlyFinalSheet	=input.nextBoolean();	 logger.info(" StoreOnlyFinalSheet "+StoreOnlyFinalSheet);
						}
				
		 
				if (inputString.trim().startsWith("UseLeaveOneOut")) {
				 	UseLeaveOneOut	=input.nextBoolean();		 logger.info(" UseLeaveOneOut "+UseLeaveOneOut);
				}
				 
				if (inputString.trim().startsWith("UseSeventyPercent")) {
					 UseSeventyPercent	=input.nextBoolean();	 logger.info(" UseSeventyPercent "+UseSeventyPercent);
				}
			 
					if (inputString.trim().startsWith("UseFifthOne")) {
				 UseFifthOne	=input.nextBoolean();			 logger.info(" UseFifthOne "+UseFifthOne);	
					}
			 
				if (inputString.trim().startsWith("reGenerateTrainFilesNames")) {
				 	reGenerateTrainFilesNames	=input.nextBoolean();		 logger.info(" reGenerateTrainFilesNames "+reGenerateTrainFilesNames);		
				}
				
				
				if (inputString.trim().startsWith("MaxRunsWithoutSave")) {
					inputInt = input.nextInt();
	                 MaxRunsWithoutSave=inputInt;	 logger.info(" MaxRunsWithoutSave "+MaxRunsWithoutSave);
				
				}
				if (inputString.trim().startsWith("confTestFiles")) {
			 
				confTestFiles=new ArrayList<String>();
				String innerInput="";
				int size=0;
				while (input.hasNext()&& !innerInput.equalsIgnoreCase("Finish")){
					
					innerInput=input.nextLine();
					if (innerInput.startsWith("##"))
						continue;


					
					if (innerInput.equalsIgnoreCase("Finish")){
						
						break;
					}
					if (innerInput.trim().equalsIgnoreCase("size")){
						size=input.nextInt();
					}//esle if not comment or finish then a file nameee.... 
					
					confTestFiles.add(innerInput);
						 logger.info(" file   "+innerInput);
					
				}
				
				
				}
				
			
				
//				if ( confTestFiles!=null){
//					out.println("##----------------Now configurations namesssssss" );
//				out.println("confTestFiles");
//				out.println("##first size then the  files ");
//				out.println("size");
//				out.println( confTestFiles.size());
//				out.println("####Filessss");
//				out.println("Files");
//				for (int i = 0; i < confTestFiles.size(); i++) {
//					//out.println("###File number "+i);
//					out.println( confTestFiles.get(i));
//				}
//				out.println("FINISH");
				
				
				
				
				
			}
				input.close();
				

			} catch (FileNotFoundException e) {

				e.printStackTrace();
				logger.error("Setting File not FOUNDDDD USE defualt settings.........");
				
			} catch (IOException e) {

				e.printStackTrace();
			}
		 
			logger.info("Finished reading settings.........");
		
//		try {
//			File afile = new File(filename);
//			Scanner input = new Scanner(new BufferedReader(
//					new FileReader(afile)));
//			while (input.hasNext("##*")) {
//				// remove ths commpent s
//				logger.error(input.nextLine());
//			}
//			
//			
//			if (input.hasNextInt()) {
//				// first
//				OperatonCode = input.nextInt();
//				logger.error(" operation is " + OperatonCode);
//			}
//			if (input.hasNextInt()) {
//				DataBase = input.nextInt();
//				if (DataBase == 6 || DataBase == -1) {
//					// test all data base on the sets
//					RunForAllDataSets = true;
//					DataBase = DATA_HSE;
//					logger.info(" new database is " + DataBase);
//				}
//			}
//			if (input.hasNextInt()) {
//				averageRunNo = input.nextInt();
//				logger.info(" average run number is  " + averageRunNo);
//			}
//			if (input.hasNextInt()) {
//				CurrentFilesMaxNumber = input.nextInt();
//				CountChanged = true;
//				if (CurrentFilesMaxNumber <= 0) {
//					CountChanged = false;
//					CurrentFilesMaxNumber = getDataBaseMaxCount();
//				}
//				logger.info(" current file max number (database) "
//						+ CurrentFilesMaxNumber);
//			}
//			if (input.hasNextInt()) {
//				int temp = input.nextInt();
//				if (temp == 0) {
//					StoreOnlyFinalSheet = false;
//				} else if (temp == 1) {
//					StoreOnlyFinalSheet = true;
//				}
//				logger.info("   the store only to finnal sheet is "
//						+ StoreOnlyFinalSheet);
//			}
//			if (input.hasNextInt()) {
//				int temp = input.nextInt();
//				if (temp == 0) {
//					testVertices = false;
//				} else if (temp == 1) {
//					testVertices = true;
//				}
//				logger.info(" test vertices   " + testVertices);
//			}
//			if (input.hasNextInt()) {
//				int temp = input.nextInt();
//				MaxRunsWithoutSave = temp;
//				logger.info(" maximum number of runs withour saves is "
//						+ MaxRunsWithoutSave);
//			}
//			if (input.hasNextInt()) {
//				int temp = input.nextInt();
//				if (temp == 0) {
//					UseLeaveOneOut = true;
//					UseSeventyPercent = false;
//					UseFifthOne = false;
//				} else if (temp == 1) {
//					UseSeventyPercent = true;
//					UseLeaveOneOut = false;
//					UseFifthOne = false;
//				} else if (temp == 2) {
//					UseFifthOne = true;
//					UseSeventyPercent = false;
//					UseLeaveOneOut = false;
//				}
//				logger.info("  Test type " + temp);
//			}
//		} catch (FileNotFoundException e) {
//			logger.error("  no files found ");
//			// e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

	private int getDataBaseMaxCount() {
		int path = 20;
		if (DataBase == DATA_HSE)
			path = 20;
		else if (DataBase == DATA_MINE_ELECTERICAL)
			// F:\sketch\Data Sets\Mine\elect
			path = 16;
		else if (DataBase == DATA_MINE_DIGITAL)
			// F:\sketch\Data Sets\Mine\logic
			path = 9;
		else if (DataBase == DATA_MINE_ELECTERICAL_R)
			// F:\sketch\Data Sets\Mine\elect
			path = 11; // 8 file last is skew
		else if (DataBase == DATA_MINE_DIGITAL_R)
			// F:\sketch\Data Sets\Mine\logic
			path = 11; // 7 file last is sketw
		else if (DataBase == DATA_MINE_SWARM_STROKES)
			// F:\sketch\Data Sets\Mine\logic
			path = 3; // 7 file last is sketw
		return path;
	}

	public static void main(String[] args) {
		if (logger.isDebugEnabled()) {
			//  logger.debug("main(String[]) - start"); //$NON-NLS-1$
		}
		// File temp=new File("");
		// System.out.println(temp.getAbsolutePath());
		org.apache.log4j.PropertyConfigurator.configure("log4j.properties");
		PatchTestSetFromFiles temp = new PatchTestSetFromFiles();
		temp.run();
		// ------------------------------------------to run a patch test train
	}

	@Override
	public void run() {
		 confTestFiles=new ArrayList<String>();
		 confTestFiles.add("TestSettingsForOP1_DB_1.txt.txt");
		readFromFile(settingFilename);
		TotalFileMaxNumber = getDataBaseMaxCount();  
		ArrayList<TestSketchSetting> 	TemptestList=new ArrayList<TestSketchSetting>();
	         for (int i = 0; i < confTestFiles.size(); i++) {
			ArrayList<TestSketchSetting> temp = TestSketchSetting.ReadClassifiersDetails(confTestFiles.get(i));
	        	 
			TemptestList.addAll(temp);
			}
	         
	         if (reGenerateTrainFilesNames){
	        	 
	        	testList= reRecreateTheFileName(TemptestList);
	         }
	         else {
	        	 testList=TemptestList;
	         }
	         
	          
	         TestSketchSetting.SaveClassifiersDetails(testList, "confTestFiles_"+confTestFiles.size());
	         runTests ();
			 saveResults();
				
			saveSettings(settingFilename);	
				
	         
		
	}
	
	public ArrayList<TestSketchSetting>	 reRecreateTheFileName(ArrayList<TestSketchSetting> tests){
		// generate a train and a test set 
		ArrayList<TestSketchSetting>	 FinalTestSet=new ArrayList<TestSketchSetting>	();
		
		
		int increment=1;
		
//		if (OperatonCode==6)
//		   increment=1;
//		else 
//			increment=2;
		 
		 // get percentage of max number of files... ..
		 TotalFileMaxNumber=getDataBaseMaxCount();
		
		for(int c=3;c<=TotalFileMaxNumber;c+=increment){
	
			if (OperatonCode==OPERATION_RUN_ALL)
			{
			   	c=TotalFileMaxNumber;
			}	
			else if (c>=TotalFileMaxNumber ){
			   	c=TotalFileMaxNumber;
			}
			//System.out.println( " number of j ="+j+" repeat i "+i);
			 CurrentFilesMaxNumber=c;
			 
			 
		
		for (int k = 0; k <  averageRunNo; k++) {
			
		GenerateRandomTestSet();
		
		
		//if use all data then generate only train set...
		for (int i = 0; i <  tests.size(); i++) {
			TestSketchSetting temp = (TestSketchSetting) tests.get(i).clone();
			temp.setFilesNames(null);
			
			for (int j = 0; j < TrainFilenames.size(); j++) {
				temp.addDataSetFileName(TrainFilenames.get(j));
			}
			for (int j = 0; j < TestFilenames.size(); j++) {
				temp.addDTestSetFileName(TestFilenames.get(j));
			}
			temp.setTestSize(TestFilenames.size());
			
			FinalTestSet.add(temp);
			
		}// the test set that is read....
		
 
		}// the average number of runsss.... 
	
		}	
		return FinalTestSet; 
	}
	
	public  void runTests (){
		resultList=new ArrayList<TestResults>();
		int k=0;
		          /// loop on list  of test 		
		for (int i = 0; i <  testList.size(); i++) {
			k++;
			 	// check if pauseandsave = true. stop and return. 
				// changes setting in system setttinds 
				//run test 
				TestResults TestResults = runPatchTest(testList.get(i));
				TestResults.setTestID(i+1);
				// if stop thread return to save. 
				if ( currentStop) 
					return ;
				// add thre reult to list
				resultList.add(TestResults);
//				System.out.println(  "Result of "+i+"  test ==>  "+ TestResults+" (" + this.getClass().getSimpleName()
//						+ "    "
//						+ (new Throwable()).getStackTrace()[0].getLineNumber()
//						+ "  )  ");
				System.out.println("  Result of  "+i+"  test ==>  "+ TestResults);
				appLogger.info( "  Result of  "+i+"  test ==>  "+ TestResults );
				if (k>=MaxRunsWithoutSave){
					
				k=0;	
				
				   saveTempReults();
				}
		}
	}
	
	public TestResults runPatchTest(TestSketchSetting set)
	{  
		System.out.println("  running test "+" (" + this.getClass().getSimpleName()
			+ "    " + (new Throwable()).getStackTrace()[0].getLineNumber()
				+ "  )  ");
		// create a settings object 
	//	TestSketchSetting set=new TestSketchSetting();
		// changes setting in system setttinds 
		t=new TrainTest();
		//detecting the main 
		
		set.setOSLinux( GeneralUtils.getCurrentOs());
		
		set.CorrectPaths();
		
		set.ModifyForTrain();
		
		// now modefy set 
		// now initalize train set 
		
			if (set.isDoTrain()){
	
					   t.initRunPatch(set);
					
						t.run();
						currentStop=t.isPauseAndSave();
			}
			
		   //change setting for test
			appLogger.info("   FINISH TRAIN now going to start testing "+" (" + this.getClass().getSimpleName()
					+ "    "
					+ (new Throwable()).getStackTrace()[0].getLineNumber()
					+ "  )  ");
			System.out.println("   FINISH TRAIN now going to start testing "+" (" + this.getClass().getSimpleName()
					+ "    "
					+ (new Throwable()).getStackTrace()[0].getLineNumber()
					+ "  )  ");
			
			
		set.ModifyForTest();
		set.setPauseSave(currentStop);
		//	now initalize agin for run . 
		t.initRunPatch(set);
	
		t.run();
		currentStop=t.isPauseAndSave();
			
		 
		 return t.getResult();
		 
		 
	
		 
		 
	
		
	}

		public void saveTempReults(){
			
			
			  // firstly try to display all reasult. 
				
				for (int i = 0; i < resultList.size(); i++) {
			
					if (logger.isInfoEnabled())
					{
						logger.info( "   i "+i+"   "+resultList.get(i));
					}
					else
					{
						logger.error( "   i "+i+"   "+resultList.get(i));
						logger.error("  cat   "+resultList.get(i).CategoriesString());
					}
					
				}
				 Date d=new Date();
			     SimpleDateFormat formatter=new SimpleDateFormat("dd-MM-yyyy_hh-mm");
			    // formatter.applyPattern("y");
			     formatter.format(d);
			     String dat=formatter.format(d);
				SaveToFile("Results_"+dat+"Saving_"+resultList.size()+"result"+".xls");
				
			
				
			}
		private void saveResults() {
			  // firstly try to display all reasult. 
				
				for (int i = 0; i < resultList.size(); i++) {
			
					if (logger.isInfoEnabled())
					{
						logger.info( "   i "+i+"   "+resultList.get(i));
					}
					else
					{
						logger.error( "   i "+i+"   "+resultList.get(i));
						logger.error("  cat   "+resultList.get(i).CategoriesString());
					}
					
				}
				 Date d=new Date();
			     SimpleDateFormat formatter=new SimpleDateFormat("dd-MM-yyyy_hh-mm");
			    // formatter.applyPattern("y");
			     formatter.format(d);
			     String dat=formatter.format(d);
				SaveToFile("Results_"+dat+".xls");
				
				
			}
	
	
	
	private void SaveToFile(String filename ){
		
		if (resultList.size()==0)
			return ;
			 HSSFWorkbook wb = new HSSFWorkbook();
			    HSSFSheet sheet = wb.createSheet("Results");
			    HSSFRow row = sheet.createRow((short)0);
			    
			    row.createCell((short)0).setCellValue( new HSSFRichTextString ( "Test Name"));
			    row.createCell((short)1).setCellValue( new HSSFRichTextString ( "Correct Sample Results"));
			    row.createCell((short)2).setCellValue( new HSSFRichTextString ( "False Sample "));
			    row.createCell((short)3).setCellValue(new HSSFRichTextString ( "TaskTimeSeg"));
			    row.createCell((short)4).setCellValue( new HSSFRichTextString ("SegCount"));
			    row.createCell((short)5).setCellValue(new HSSFRichTextString ("TimeMsFeatures"));
			    row.createCell((short)6).setCellValue(new HSSFRichTextString ("TaskTimeRec"));
			    row.createCell((short)7).setCellValue(new HSSFRichTextString ( "ExampleCount")); 
			    //HSSFSheet sheet2 = wb.createSheet("second sheet");
			    for (int i = 0; i < resultList.size(); i++) {
			    // Create a row and put some cells in it. Rows are 0 based.
			    row = sheet.createRow((short)i+1);
			    
		
			    row.createCell((short)0).setCellValue( new HSSFRichTextString (resultList.get(i).TestName));
			    row.createCell((short)1).setCellValue( resultList.get(i).correctSamples *100);
			    row.createCell((short)2).setCellValue(resultList.get(i).falseSamples *100);
			    row.createCell((short)3).setCellValue( resultList.get(i).getTaskTimeSeg());
			    row.createCell((short)4).setCellValue( resultList.get(i).getSegCount());
			    row.createCell((short)5).setCellValue(resultList.get(i).getTaskTimeMsFeatures());
			    row.createCell((short)6).setCellValue(resultList.get(i).getTaskTimeRec());
			    row.createCell((short)7).setCellValue( resultList.get(i).getExampleCount());
			    
			 
			    }
			    
			    
			    //------------------------------------now categoryies 
			    
			    HSSFSheet sheet2 = wb.createSheet("Category");
			    HSSFRow row2 = sheet2.createRow((short)0);
			    //this is the header  row 
			    row2.createCell((short)0).setCellValue( new HSSFRichTextString ( "Test Name"));
			    //now get the cateories 
			    Set<String> categryNames = resultList.get(0).categorySamples.keySet();
			    int j=1;
			    for (Iterator iterator = categryNames.iterator(); iterator
						.hasNext();) {
					String sname = (String) iterator.next();
					  row2.createCell((short)j).setCellValue( new HSSFRichTextString ( sname));
					  j++;
					
				}
			    //HSSFSheet sheet2 = wb.createSheet("second sheet");
			    for (int i = 0; i < resultList.size(); i++) {
			    // Create a row and put some cells in it. Rows are 0 based.
			    row2 = sheet2.createRow((short)i+1);
			    
			    // the name of row 
			    row2.createCell((short)0).setCellValue( new HSSFRichTextString (resultList.get(i).TestName));
			    
			    // now the 
			    Set<String> categryN = resultList.get(i).categorySamples.keySet();
			    int k=1;
			    for (Iterator iterator = categryN.iterator(); iterator
						.hasNext();) {
			    	String sname = (String) iterator.next();
			    	ArrayList<Integer> temp = resultList.get(i).categorySamples.get(sname);
			    	
			    	  double correct=((double)temp.get(1)/(double)temp.get(0))*100.0;
			    	  
			    	  
			    	  row2.createCell((short)k).setCellValue( correct);
					    k++;
			    }
			    
			    
			    
			    }
			    
			    SaveConfusionMatrix(wb);
			    FileOutputStream fileOut;
				try {
					fileOut = new FileOutputStream(filename);
					 wb.write(fileOut);
					    fileOut.close();
				} catch (FileNotFoundException e) {
					logger.error(e.getMessage(), e);
					//e.printStackTrace();
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
					//e.printStackTrace();
				}
			   
			
			
		}
	private void SaveConfusionMatrix(	 HSSFWorkbook wb){
		if (resultList.size()==0)
			return ;
		if (StoreOnlyFinalSheet)
			return ;
		for (int i = 0; i < resultList.size(); i++) {
			 HSSFSheet sheet = wb.createSheet(resultList.get(i).TestName+i);
			 
			 resultList.get(i).getConfusion().writeToWorkbook(sheet);
		}
		
		    
	}
	
	
	
	
	private TestSketchSetting AllAlgAllFeat() {
		 TestSketchSetting set;
			set=new TestSketchSetting(); // initalize a new set.
			set.GetStoredSetting();
			set.setRecognizierType( TestSketchSetting.SVM_RECOGNIZER);
			// all, no swarm,algorithms ( 1 2 3 ), circle.  
			set.setAlgorithmRunnings(false, false,true, true,false, true);
			//(150 iteration and 15 agent) with 3 training files and 2 test file
			set.setTestName("Syst_");
			// all, no swarm,algorithms ( 1 2 3 ), circle.  
			//set.setAlgorithmRunnings(false, false,false, false, true, false);
			set.setDataSetType(set.DATA_SET_TYPE_XML);
			
			set.setMaxCatSize(-1);  // all data 
			
			for (int i = 0; i < TrainFilenames.size(); i++) {
				set.addDataSetFileName(TrainFilenames.get(i));
			}
			
			set.setSwarmSettings(15,120);
			set.swarmSettings(2,2,1.5,6);
//			// resample  minn segment (for segmentations) and Max iterpolation length then min stroke pixel 
//			set.setStrokeLengthSettings(false, -1,-1, -1);
//			set.setSettingsForPolygon(polygonSolution.POLYGON_ADJUST_NONE);
//			// bezire error, curve test, hybired tol( no use), solution error, solution intial tol, second pass
//			set.SetErrorThresholds( -1.0,-1.0,-1,-1,-1,false);
//			//swarm cettings c1, c2, w, vmax
			
//			// now for features. 
//			// those are the defaults 
//		//  primitive,count, count relations, centroid, convex hul, raions, area, logstatics, density, rubine, zenerik
//				//true,false,true,false,false,false,false,false,true,false,true 
				set.SymbolFeaturesSettings(true,true,true,true,true,true,true,true,true,true,true );
				set.setSymbolFeaturesSettingsCOUNTCURVE(true);
			set.setOSLinux(SystemSettings.OS);
			for (int i = 0; i < TestFilenames.size(); i++) {
				set.addDTestSetFileName(TestFilenames.get(i));
			}
			set.setTestSize(TestFilenames.size());
	 
           //set.ResetAllSettings();
		    return set;
	}
	
	private void GenerateRandomTestSet(){
		 String path =getDataBaseMainPath();
		 String extention=getDataBaseExtention();
		// CurrentFilesMaxNumber=getDataBaseMaxCount();
//			Random  r1Random;
//			r1Random=new Random();
		 
		  double seed=r1Random.nextDouble();
		  //now i need to get 4 values 
		  
		   ArrayList<String> AllFiles = new ArrayList<String>();
		   String filename;
		   if (CurrentFilesMaxNumber<=2){
			   
			   TestFilenames=new ArrayList<String>();
			   TrainFilenames=new ArrayList<String>();
			   int i= r1Random.nextInt(TotalFileMaxNumber )+1;
			   //int i=(int) Math.floor(Math.random()*TotalFileMaxNumber)+1;
			   if (i>=TotalFileMaxNumber)
				   i=TotalFileMaxNumber-1;
			   
			   filename=path+"user"+i+extention;
			   TrainFilenames.add( filename);
			   i= r1Random.nextInt(TotalFileMaxNumber )+1;
			   //i=(int) Math.floor(Math.random()*TotalFileMaxNumber)+1;
			   if (i>=TotalFileMaxNumber)
				   i=TotalFileMaxNumber-1;
			   filename=path+"user"+i+extention;
			   TestFilenames.add( filename);
				
			   logger.info("inside the case of only 2 ");
			   return; 
		   }
		   
		   
		   
		   
		   for (int i = 1; i <  CurrentFilesMaxNumber; i++) {
			
			   filename=path+"user"+i+extention;
			   if (DataBase==DATA_HSE){
			   if (i!=15)
			    AllFiles.add(filename);
			   }
			   else {
				    AllFiles.add(filename);
			   }
			 //  logger.trace(" filename "+i+" =   " +  filename);
			   
			   
		}
		   AllFiles=shuffleFiles(AllFiles);
		   TestFilenames=new ArrayList<String>();
		   TrainFilenames=new ArrayList<String>();
		   if (DataBase==DATA_HSE){
		   filename=path+"user"+15+extention;
			TrainFilenames.add(filename);
		   }
		   for (int i = 0; i < AllFiles.size(); i++) {
			   if (UseLeaveOneOut){
				   if (i<AllFiles.size()-2)
				   {TrainFilenames.add( AllFiles.get(i));
				   }
				   else 
					   TestFilenames.add( AllFiles.get(i));
				   
				   
			   }
			   else if (UseSeventyPercent){
			   
			if (i%4==0)
			{
				TestFilenames.add( AllFiles.get(i));
				
			}
			else {
			
				TrainFilenames.add( AllFiles.get(i));
			}
			   }
			   else if (UseFifthOne){
				   
					if (i%5==0)
					{
						TestFilenames.add( AllFiles.get(i));
						
					}
					else {
					
						TrainFilenames.add( AllFiles.get(i));
					}
					   }
			// logger.trace(" Randomize  "+i+" =   " +  AllFiles.get(i));
			
		}
		   
		 
		   
		 
	}
	private void GenerateAllTestSet(){
		 String path =getDataBaseMainPath();
		 String extention=getDataBaseExtention();
		// CurrentFilesMaxNumber=getDataBaseMaxCount();
		  //double seed=Math.random();
		 
			
		 
		  double seed=r1Random.nextDouble();
		  //now i need to get 4 values 
		  
		   ArrayList<String> AllFiles = new ArrayList<String>();
		   String filename;
		   if (CurrentFilesMaxNumber<=2){
			   
			   TestFilenames=new ArrayList<String>();
			   TrainFilenames=new ArrayList<String>();
			   int i= r1Random.nextInt(TotalFileMaxNumber )+1;
			//   int i=(int) Math.floor(Math.random()*TotalFileMaxNumber)+1;
			   if (i>=TotalFileMaxNumber)
				   i=TotalFileMaxNumber-1;
			   
			   filename=path+"user"+i+extention;
			   TrainFilenames.add( filename);
			   i= r1Random.nextInt(TotalFileMaxNumber )+1;   
		//	   i=(int) Math.floor(Math.random()*TotalFileMaxNumber)+1;
			   if (i>=TotalFileMaxNumber)
				   i=TotalFileMaxNumber-1;
			   filename=path+"user"+i+extention;
			   TestFilenames.add( filename);
				
			   logger.info(" inside the case of only 2 ");
			   return; 
		   }
		   
		   
		   
		   
		   for (int i = 1; i <  CurrentFilesMaxNumber; i++) {
			
			   filename=path+"user"+i+extention;
			  
			    AllFiles.add(filename);
			  
			   
			 //  logger.trace(" filename "+i+" =   " +  filename);
			   
			   
		}
		   AllFiles=shuffleFiles(AllFiles);
		   TestFilenames=new ArrayList<String>();
		   TrainFilenames=new ArrayList<String>();
//		   if (DataBase==DATA_HSE){
//		   filename=path+"user"+15+extention;
//			TrainFilenames.add(filename);
//		   }
		   for (int i = 0; i < AllFiles.size(); i++) {
			 
				 TrainFilenames.add( AllFiles.get(i));
			TestFilenames.add( AllFiles.get(i));

		}
		   
		 
		   
		 
	}
	
	private String getDataBaseMainPath(){
		 String path="";
		if (DataBase==DATA_HSE )
		  path ="D:\\sketch\\Data Sets\\hhreco\\data\\data\\";
		else if (DataBase==DATA_MINE_ELECTERICAL)
			//F:\sketch\Data Sets\Mine\elect
			path ="D:\\sketch\\Data Sets\\Mine\\elect\\";
		else if (DataBase==DATA_MINE_DIGITAL)
			//F:\sketch\Data Sets\Mine\logic
			path ="D:\\sketch\\Data Sets\\Mine\\logic\\";
		
		
		
		else if (DataBase==DATA_MINE_ELECTERICAL_R)
			//F:\sketch\Data Sets\Mine\elect
			path ="D:\\sketch\\Data Sets\\Mine\\e\\";
		else if (DataBase==DATA_MINE_DIGITAL_R)
			//F:\sketch\Data Sets\Mine\logic
			path ="D:\\sketch\\Data Sets\\Mine\\d\\";
		

		
		else if (DataBase==DATA_MINE_SWARM_STROKES)
			//F:\sketch\Data Sets\Mine\logic
			path ="D:\\sketch\\Data Sets\\Mine\\s\\";
		
		return path;
	}
	private String getDataBaseExtention(){
		 String path="";
		if (DataBase==DATA_HSE )
		  path =".sml";
		else if (DataBase==DATA_MINE_ELECTERICAL)
			path =".xml";
		else if (DataBase==DATA_MINE_DIGITAL)
			path =".xml";
		else if (DataBase==DATA_MINE_ELECTERICAL_R)
			//F:\sketch\Data Sets\Mine\elect
			path =".xml";
		else if (DataBase==DATA_MINE_DIGITAL_R)
			//F:\sketch\Data Sets\Mine\logic
			path =".xml";
		else if (DataBase==DATA_MINE_SWARM_STROKES)
			//F:\sketch\Data Sets\Mine\logic
			path =".xml";
		return path;
	}
	/**
	 * @param files
	 * @return
	 */
	private ArrayList<String> shuffleFiles(
			ArrayList<String> files) {
		
		//create an emypt array to hold the shuffeld files 
		ArrayList<String>  RandomSet=new ArrayList<String>(files.size());
		RandomSet.ensureCapacity(files.size());
		
	ArrayList<Integer> indeces=new ArrayList<Integer>(files.size());
	
	
	for (int i = 0; i < files.size(); i++) {
		indeces.add(new Integer(i));
		
	}
	//int randomi=(int) Math.floor(Math.random()*files.size());
	
	int randomi=r1Random.nextInt(indeces.size());//(int) Math.floor(Math.random()*files.size());
	int index;
	for (int i = 0; i < indeces.size(); i++) {
		
		//randomi=(int) Math.floor(Math.random()*indeces.size());
		randomi=r1Random.nextInt(indeces.size());
	// now move files from he input to the ouptut array . 
		index=indeces.get(randomi);
		
		RandomSet.add( files.get(index));
		indeces.remove(randomi);
		
	}
	 
		 //check that all indeces are added 
	if(indeces.size()>0)
	{
		for (int i = 0; i <indeces.size(); i++) {
			index=indeces.get(i);
			RandomSet.add( files.get(index));
		}
	}
	
	
//	if (logger.isTraceEnabled()){
//		
//		for (int i = 0; i < 100 && i<RandomSet.size(); i++) {
//			//logger.trace(" -- "+RandomSet.get(i));
//		}
//	
//	}
		
		
		
		
		
		return RandomSet;
	}
	
	
	
}
