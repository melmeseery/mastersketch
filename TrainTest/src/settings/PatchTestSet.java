package settings;



import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;
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

//import SketchMaster.swarm.polygonApproximations.polygonSolution;
import SketchMaster.Stroke.StrokeData.Stroke;
import SketchMaster.swarm.polygonApproximations.polygonSolution;
import SketchMaster.system.SystemSettings;

import test.HandleDataSetEvents;
import test.RunApp;
import test.TestVertices;
import test.TrainTest;

public class PatchTestSet implements Serializable ,Runnable{
	private static transient final Logger logger = Logger.getLogger(PatchTestSet.class);
	private static transient final Logger appLogger = Logger.getLogger("AppLogging");
	static int DATA_HSE=1;
	static int  DATA_MINE_ELECTERICAL=2;
	static int  DATA_MINE_DIGITAL=3;
	static int  DATA_MINE_ELECTERICAL_R=4;
	static int  DATA_MINE_DIGITAL_R=5;
	static int DATA_MINE_SWARM_STROKES=9;
	static int MaxRunsWithoutSave=50;
	private int DataBase;
	ArrayList <TestResults> resultList;
	ArrayList <TestSketchSetting> testList;
	 static boolean currentStop;
	 TrainTest t;
	 TestVertices v;
	 private ArrayList<VertixTestResult> resultListVertices;
	 static ArrayList<String> TestFilenames=new ArrayList<String>();
	 static ArrayList<String> TrainFilenames=new ArrayList<String>();
	 static int averageRunNo = 3;
	 static int CurrentFilesMaxNumber=8; //  subtract 1 to total number of files 
	 private int  TotalFileMaxNumber;
	 static boolean testVertices=false;
	 static boolean StoreOnlyFinalSheet=true;
	 private int OperatonCode;
	private boolean RunForAllDataSets=false;
	private boolean CountChanged;
	
	Random  r1Random=new Random(600);
	public PatchTestSet() {
            logger.setLevel(Level.ALL);
		   t=new TrainTest();
		   v=new TestVertices();
		   
		   readFromFile();
		 
		   
		   TotalFileMaxNumber=getDataBaseMaxCount();
	}
	public void readFromFile(){
		
		  DataBase=  DATA_MINE_DIGITAL_R;
		   OperatonCode=3;
		   averageRunNo =1;
		   CurrentFilesMaxNumber=getDataBaseMaxCount();
		
		try{
		File afile = new File("settings.txt");
		Scanner input = new Scanner(new BufferedReader(
				new FileReader(afile)));
	while (input.hasNext("##*")){
		//remove ths commpent s
		logger.error(input.nextLine());
	}
		 if (input.hasNextInt()) {
			// first
			 OperatonCode=input.nextInt();
			 logger.error(" operation is "+OperatonCode);
			}
		 
		 if (input.hasNextInt()) {
			 DataBase=input.nextInt();
			 
			 if(DataBase==6||DataBase==-1)
			 {
				 
				 //test all data base on the sets
			 RunForAllDataSets = true;
				 DataBase=DATA_HSE;
				 logger.info(" new database is "+DataBase);
			 }
				 
				 
			 }
			 if (input.hasNextInt()) {
			 averageRunNo=input.nextInt();
			 
			 logger.info(" average run number is  "+averageRunNo);
			 }
			 if (input.hasNextInt()) {
				 CurrentFilesMaxNumber=input.nextInt();
				 CountChanged=true;
				 if (CurrentFilesMaxNumber<=0){
					 CountChanged=false;
					 CurrentFilesMaxNumber=getDataBaseMaxCount();
				 }
				 
				 logger.info(" current file max number (database) "+CurrentFilesMaxNumber);
		 }
			 if (input.hasNextInt()) {
				 int temp=input.nextInt();
				 if (temp==0)
				 {
					 StoreOnlyFinalSheet=false;
				 }else if (temp==1){
					 StoreOnlyFinalSheet=true;
				 }
				 
		    logger.info( "   the store only to finnal sheet is "+StoreOnlyFinalSheet);
		 }
			 
			
			 if (input.hasNextInt()) {
				 int temp=input.nextInt();
				 if (temp==0)
				 {
					 testVertices=false;
				 }else if (temp==1){
					 testVertices=true;
				 }
				 
		    logger.info(" test vertices   "+testVertices);
		 }
			 
			 
			 		 if (input.hasNextInt()) {
					 int temp=input.nextInt();
					
						MaxRunsWithoutSave=temp;
						
						logger.info(" maximum number of runs withour saves is "+ MaxRunsWithoutSave);
						
			 		 }
			 		 
			 		 
			 		 
			 		 if (input.hasNextInt()) {
						 int temp=input.nextInt();
						 if (temp==0)
						 {
							 UseLeaveOneOut=true;
						
							 
							 UseSeventyPercent=false;
							 UseFifthOne=false;
							 
						 }else if (temp==1){
							 UseSeventyPercent=true;
								 UseLeaveOneOut=false;
								 
								 UseFifthOne=false;
						 }
						 else if (temp==2){
							 UseFifthOne=true;
							 
							 UseSeventyPercent=false;
							 UseLeaveOneOut=false;
						 }
						 
				      logger.info("  Test type "+temp);
				 }
					 
		}catch (FileNotFoundException e) {
			logger.error("  no files found ");
			//e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}


	 //
	
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

	public void runVerticesTest(){
		resultListVertices=new ArrayList<VertixTestResult>();
		int k=0;
        /// loop on list  of test 		
for (int i = 0; i <  testList.size(); i++) {
	k++;
	 	// check if pauseandsave = true. stop and return. 
		// changes setting in system setttinds 
		//run test 
	  VertixTestResult TestResults = runPatchVerticesTest(testList.get(i));
		TestResults.setNTestId(i+1);
		// if stop thread return to save. 
		if ( currentStop) 
			return ;
		// add thre reult to list
		resultListVertices.add(TestResults);
//		System.out.println(  "Result of "+i+"  test ==>  "+ TestResults+" (" + this.getClass().getSimpleName()
//				+ "    "
//				+ (new Throwable()).getStackTrace()[0].getLineNumber()
//				+ "  )  ");
		System.out.println("  Result of  "+i+"  test ==>  "+ TestResults);
		appLogger.info( "  Result of  "+i+"  test ==>  "+ TestResults );
		
		
		if (k>=MaxRunsWithoutSave){
			
			k=0;
			saveResultsTempVertices();
		//	saveTempReults();
			}
}

	
	}
	public void setOperationSettings(int op, int db, int max, int repeat){
		//t.operation, t.DB, t.MaxFiles, t.repeatTimes
		DataBase=db;
		 TotalFileMaxNumber=getDataBaseMaxCount();
		CurrentFilesMaxNumber=max;
		averageRunNo=repeat;
		OperatonCode=op;
		
		
	}
	
    public VertixTestResult runPatchVerticesTest(  TestSketchSetting set  )
    {
    	set.ModifyForTrain();
    	//logger.info(" the settings is " +set);
    //logger.info(" the infor in set is "+set.getString());
					   v.initRunPatch(set);
				//	   logger.info( " this exam is testing "+		   SystemSettings.getString());
						v.run();
		

		   //change setting for test
			appLogger.info("   FINISH run test of the swarm now going to start testing "+" (" + this.getClass().getSimpleName()
					+ "    "
					+ (new Throwable()).getStackTrace()[0].getLineNumber()
					+ "  )  ");
//			System.out.println("   FINISH TRAIN now going to start testing "+" (" + this.getClass().getSimpleName()
//					+ "    "
//					+ (new Throwable()).getStackTrace()[0].getLineNumber()
//					+ "  )  ");
		 
		 
		 return v.getResult();
    	
    }	
	public TestResults runPatchTest(TestSketchSetting set)
	{  
//		System.out.println("  running test "+" (" + this.getClass().getSimpleName()
//				+ "    " + (new Throwable()).getStackTrace()[0].getLineNumber()
//				+ "  )  ");
		// create a settings object 
	//	TestSketchSetting set=new TestSketchSetting();
		// changes setting in system setttinds 
		set.ModifyForTrain();
		// now modefy set 
		// now initalize train set 
		
			if (set.isDoTrain()){
					   t.initRunPatch(set);
					
						t.run();
						currentStop=t.isPauseAndSave();
			}
			else {
				
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

	public void run() {
		  testList=new ArrayList<TestSketchSetting>();
		   if (testVertices){
		    	  RunVertices();
		    	
		      }
		   else if    (OperatonCode==22){
			   
			   
				  DataBase=DATA_HSE;
				  TotalFileMaxNumber=getDataBaseMaxCount();
				  CurrentFilesMaxNumber=getDataBaseMaxCount();
				  RunStoreAllData();
			   DataBase=DATA_MINE_ELECTERICAL_R;
			   TotalFileMaxNumber=getDataBaseMaxCount();
				  CurrentFilesMaxNumber=getDataBaseMaxCount();
			   RunStoreAllData();
				  DataBase=DATA_MINE_DIGITAL_R;
				  TotalFileMaxNumber=getDataBaseMaxCount();
				  CurrentFilesMaxNumber=getDataBaseMaxCount();
				  RunStoreAllData();
				  
				  
				  runTests ();
					saveResults();
		   }
		      else {
		               
		    	  RunForDataSet();
		    	  if (RunForAllDataSets){
		    		  DataBase=DATA_MINE_ELECTERICAL_R;
		    		  TotalFileMaxNumber=getDataBaseMaxCount();
		    		  if (! CountChanged)
		    		  CurrentFilesMaxNumber=getDataBaseMaxCount();
		    		  RunForDataSet();
		    		  DataBase=DATA_MINE_DIGITAL_R;
		    		  TotalFileMaxNumber=getDataBaseMaxCount();
		    		  if (! CountChanged)
			    		  CurrentFilesMaxNumber=getDataBaseMaxCount();
		    		  RunForDataSet();
		    	  }
				runTests ();
				saveResults();
		      }
	}
	
	public void RunForDataSet(){
		if (OperatonCode==11){
			intializeTrainAll();	
			
		}
		
		else if (OperatonCode==15){
		  //run single test for all the data 
		  InitErrorCorrectTest();
		  
	  }
  else if (OperatonCode>=3 && OperatonCode<11) {
	  //different number of symobl in each train 
		  intializeTestSymbolsCount();
	  } 
	  else {
	  
		  intializeTest();
	  }
		
	}
	public void RunVertices(){
	 	intializeTestVertices();
	 	
		  if (RunForAllDataSets){
    		  
			  DataBase=DATA_MINE_ELECTERICAL_R;
    		  TotalFileMaxNumber=getDataBaseMaxCount();
    		  if (! CountChanged)
    		  CurrentFilesMaxNumber=getDataBaseMaxCount();
    		  
    		  
    		  	intializeTestVertices();
    		  	
    		  	
    		  DataBase=DATA_MINE_DIGITAL_R;
    		  TotalFileMaxNumber=getDataBaseMaxCount();
    		  if (! CountChanged)
	    		  CurrentFilesMaxNumber=getDataBaseMaxCount();
    		//  RunForDataSet();
    		  	intializeTestVertices();
    		  
    	  }
	
		runVerticesTest();
		saveResultsVertices();
		
	}
	
	
	public void Stop(){
		t.setPauseAndSave(true);
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
		   private void saveResultsTempVertices(){
			   // firstly try to display all reasult. 
			  		
			  		for (int i = 0; i < resultListVertices.size(); i++) {
			  	
			  			if (logger.isInfoEnabled())
			  			{
			  				logger.info( "   i "+i+"   "+resultListVertices.get(i));
			  			}
//			  			else
//			  			{
//			  				//logger.error( "   i "+i+"   "+resultListVertices.get(i));
//			  				//logger.error("  cat   "+resultListVertices.get(i).CategoriesString());
//			  			}
			  			
			  		}
			  		 Date d=new Date();
			  	     SimpleDateFormat formatter=new SimpleDateFormat("dd-MM-yyyy_hh-mm");
			  	    // formatter.applyPattern("y");
			  	     formatter.format(d);
			  	     String dat=formatter.format(d);
			  		SaveToFileVertices("Results_"+dat+"Saving_"+resultListVertices.size()+"result"+".xls");
			  		
			      	
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
    private void saveResultsVertices(){
 // firstly try to display all reasult. 
		
		for (int i = 0; i < resultListVertices.size(); i++) {
	
			if (logger.isInfoEnabled())
			{
				logger.info( "   i "+i+"   "+resultListVertices.get(i));
			}
//			else
//			{
//				//logger.error( "   i "+i+"   "+resultListVertices.get(i));
//				//logger.error("  cat   "+resultListVertices.get(i).CategoriesString());
//			}
			
		}
		 Date d=new Date();
	     SimpleDateFormat formatter=new SimpleDateFormat("dd-MM-yyyy_hh-mm");
	    // formatter.applyPattern("y");
	     formatter.format(d);
	     String dat=formatter.format(d);
		SaveToFileVertices("Results_Vertices_"+dat+".xls");
		
    	
    }
    
    
    public void SaveToFileVertices(String filename ){
		
    	if (resultListVertices.size()==0)
    		return ;
    		 HSSFWorkbook wb = new HSSFWorkbook();
    		    HSSFSheet sheet = wb.createSheet("Results");
    		    HSSFRow row = sheet.createRow((short)0);
    		    
    		    row.createCell((short)0).setCellValue( new HSSFRichTextString ( "Test Name"));
    		    row.createCell((short)1).setCellValue( new HSSFRichTextString ( "Correct Sample Results"));
    		    row.createCell((short)2).setCellValue( new HSSFRichTextString ( " False Sample "));
    		    
    		  //  HSSFSheet sheet2 = wb.createSheet("second sheet");
    		    for (int i = 0; i < resultListVertices.size(); i++) {
    		    // Create a row and put some cells in it. Rows are 0 based.
    		    row = sheet.createRow((short)i+1);
    		    
    	
    		    row.createCell((short)0).setCellValue( new HSSFRichTextString (resultListVertices.get(i).TestName));
    		  //  row.createCell((short)1).setCellValue( resultListVertices.get(i).correctSamples *100);
    		  //  row.createCell((short)2).setCellValue(resultListVertices.get(i).falseSamples *100);
    		    
    		 
    		    }
    		    
    		    
    		    //------------------------------------now categoryies 
    		    if(!StoreOnlyFinalSheet){
    		    for (int i = 0; i < resultListVertices.size(); i++) {
    		    	//for each test do the following ------------
    		    	   HSSFSheet sheet2 = wb.createSheet( resultListVertices.get(i).TestName+" "+i);
    	    		  HSSFRow row2 = sheet2.createRow((short)0);
    	    		  
    	    		row2.createCell((short)0).setCellValue( new HSSFRichTextString ( "Sample "));
    	    		row2.createCell((short)1).setCellValue( new HSSFRichTextString ( "Category Name "));
    	  		    row2.createCell((short)2).setCellValue( new HSSFRichTextString ( " Error "));
    			    row2.createCell((short)3).setCellValue( new HSSFRichTextString ( " Stroke points "));
    			    row2.createCell((short)4).setCellValue( new HSSFRichTextString ( " Vertix points  "));
    			    row2.createCell((short)5).setCellValue( new HSSFRichTextString ( " Segments count "));;
    			    
    			    //now get the result of each category 
    			    HashMap<String, ArrayList<VertixTestResultNode>> catresult = resultListVertices.get(i).getCategoryResults();
    			   // catresult.keySet();
    			    Set<String> categryNames = catresult.keySet();
    			    int count=0;
    			    for (Iterator iterator = categryNames.iterator(); iterator   .hasNext();) {
    			    	
    			    	String sname = (String) iterator.next();
    			    	ArrayList<VertixTestResultNode> vertixresluts = catresult.get(sname);
    			    	for (int j = 0; j <  vertixresluts.size(); j++) {
    			    		  HSSFRow row3 = sheet2.createRow((short)count+1);
    			    		  count++;
    			    		// now i need to create a row for each reslut 
    			    		 row3.createCell((short)0).setCellValue( new HSSFRichTextString ( "Sample "+count));
    			    		  row3.createCell((short)1).setCellValue(new HSSFRichTextString (sname));
    			    		  row3.createCell((short)2).setCellValue(vertixresluts.get(j).getError());
    			    		  row3.createCell((short)3).setCellValue(vertixresluts.get(j).getStrokePointsCount());
    			    		  row3.createCell((short)4).setCellValue(vertixresluts.get(j).getVertixCount());
    			    		  row3.createCell((short)5).setCellValue(vertixresluts.get(j).getCountOfSegments());
    			    		  
						}
    			    	
    			    	
    			    }
    		    	
    		    	
    		    	
    		    }
    		    
    		    }
    
    		    
    		    HSSFWorkbook wb2 = new HSSFWorkbook();
    		    
    		    AnalyizeVerticesResult(wb2);
    		    
    		    
    		    FileOutputStream fileOut;
    			try {
    				fileOut = new FileOutputStream(filename);
    				 wb.write(fileOut);
    				    fileOut.close();
    				    
    				    
    				    
    					FileOutputStream fileOut2 = new FileOutputStream(filename.replace(".xls","_VertixAnalysis.xls"));
       				 wb2.write(fileOut2);
       				    fileOut2.close();
    				    
    			} catch (FileNotFoundException e) {
    				logger.error(e.getMessage(), e);
    				//e.printStackTrace();
    			} catch (IOException e) {
    				logger.error(e.getMessage(), e);
    				//e.printStackTrace();
    			}
    		   
    		
    		
    	}

    
    
    public void AnalyizeVerticesResult( HSSFWorkbook wb){

    	//get result for the tests
    	if (resultListVertices.size()==0)
    		return ;
    	
    	
    	for (int i = 0; i < resultListVertices.size(); i++) {
    		resultListVertices.get(i). AnalysisVertixResults();
    		  if (!StoreOnlyFinalSheet){
    		  HSSFSheet sheet2 =wb.createSheet(resultListVertices.get(i).TestName+i);
    		
    		  resultListVertices.get(i).getCatAnalysis().WriteToxls(sheet2);
    		  }
		}
    
    	//get cat names 
    	ArrayList<String> cat=resultListVertices.get(0).getCatAnalysis().Category;
    	
    	  HSSFSheet sheet = wb.createSheet("Test_Summary");
		    HSSFRow row = sheet.createRow((short)0);
		      row.createCell((short)(0)).setCellValue( new HSSFRichTextString ("   "));
				 
		    for (int i = 0; i < resultListVertices.size(); i++) {
//		    	   HSSFRow row2=sheet.createRow((short)(i+1));
		        row.createCell((short)(i+1)).setCellValue( new HSSFRichTextString (resultListVertices.get(i).TestName));
		        
		    }
		   
		    
		    HSSFRow   rowE = sheet.createRow((short)1);
		    HSSFRow   rowE2 = sheet.createRow((short)2);
		    HSSFRow   rowE3 = sheet.createRow((short)3);
		    HSSFRow   rowE4 = sheet.createRow((short)4);
		    
		    HSSFRow   rowP = sheet.createRow((short)5);
		    HSSFRow   rowS = sheet.createRow((short)6);
		    HSSFRow   rowV = sheet.createRow((short)7);
		     HSSFRow   rowV2 = sheet.createRow((short)8);
		    HSSFRow   rowV3 = sheet.createRow((short)9);
		    HSSFRow   rowV4 = sheet.createRow((short)10);
		    
		    
		    rowE.createCell((short)(0)).setCellValue( new HSSFRichTextString ( " Error AV"));
		    rowE2.createCell((short)(0)).setCellValue( new HSSFRichTextString ( " Error MAx"));
		    rowE3.createCell((short)(0)).setCellValue( new HSSFRichTextString ( " Error Min"));
		    rowE4.createCell((short)(0)).setCellValue( new HSSFRichTextString ( " Error STD"));
		    rowP.createCell((short)(0)).setCellValue( new HSSFRichTextString ( " Points "));
		    rowS.createCell((short)(0)).setCellValue( new HSSFRichTextString ( " Segments Count "));
		    rowV.createCell((short)(0)).setCellValue( new HSSFRichTextString ( " Vertix Count AV"));
		    rowV2.createCell((short)(0)).setCellValue( new HSSFRichTextString ( " Vertix Count  Max"));
		    rowV3.createCell((short)(0)).setCellValue( new HSSFRichTextString ( " Vertix Count Min"));
		    rowV4.createCell((short)(0)).setCellValue( new HSSFRichTextString ( " Vertix Count STD"));
		    
		    
		    
		    for (int i = 0; i < resultListVertices.size(); i++) {
		    	
		    	VectorResultAnalysis Catan = resultListVertices.get(i).getCatAnalysis();
		    	
		    	rowE.createCell((short)(i+1)).setCellValue(Catan .ErrorStat.Average);
				
		        rowE2.createCell((short)(i+1)).setCellValue(Catan .ErrorStat.Max );
			    rowE3.createCell((short)(i+1)).setCellValue( Catan .ErrorStat.Min );
			    rowE4.createCell((short)(i+1)).setCellValue(  Catan .ErrorStat.Std);
			    rowP.createCell((short)(i+1)).setCellValue( Catan .PointsStat.Average  );
			    rowS.createCell((short)(i+1)).setCellValue(Catan . SegStat.Average  );
			    rowV.createCell((short)(i+1)).setCellValue(   Catan .VerStat.Average  );
			    rowV2.createCell((short)(i+1)).setCellValue(    Catan .VerStat.Max);
			    rowV3.createCell((short)(i+1)).setCellValue(    Catan .VerStat.Min);
			    rowV4.createCell((short)(i+1)).setCellValue(    Catan .VerStat.Std);
		    	
		    	
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

	private  TestSketchSetting GetDefaultSettings(){
 TestSketchSetting set;
		set=new TestSketchSetting(); // initalize a new set.
		set.setRecognizierType( TestSketchSetting.SVM_RECOGNIZER);
		// swarm algorithm settings ageents, swarm max iteration. 
		set.setSwarmSettings(10,90);
		// resample  minn segment (for segmentations) and Max iterpolation length then min stroke pixel 
		set.setStrokeLengthSettings(false, -1,-1, -1);
		// all, no swarm,algorithms ( 1 2 3 ), circle.  
		set.setAlgorithmRunnings(false, false,false, false, true, false);
		// choose on of the polygon adjust. 
		set.setSettingsForPolygon(polygonSolution.POLYGON_ADJUST_NONE);
		// bezire error, curve test, hybired tol( no use), solution error, solution intial tol, second pass
		set.SetErrorThresholds( -1.0,-1.0,-1,-1,-1,false);
		//swarm cettings c1, c2, w, vmax
		set.swarmSettings(1.8,1.8,2,8);
		// now for features. 
	// those are the defaults 
//  primitive,count, count relations, centroid, convex hul, raions, area, logstatics, density, rubine, zenerik
		//true,false,true,false,false,false,false,false,true,false,true 
		
	
		set.SymbolFeaturesSettings(true,true,true,false,false,false,true,false,true,false,true );
		set.setSymbolFeaturesSettingsCOUNTCURVE(true);
		// now for other options . 
		//(150 iteration and 15 agent) with 3 training files and 2 test file
		set.setTestName("Alg3");
		set.setDataSetType(set.DATA_SET_TYPE_XML);
		//set.addDataSetFileName("/windows/E/sketch/other systems/source/hhreco/data/data/user1.sml");
//		set.addDataSetFileName("/windows/E/sketch/other systems/source/hhreco/data/data/user7.sml");
//		set.addDataSetFileName("/windows/E/sketch/other systems/source/hhreco/data/data/user3.sml");
//		set.addDataSetFileName("/windows/E/sketch/other systems/source/hhreco/data/data/user4.sml");
//		set.addDataSetFileName("/windows/E/sketch/other systems/source/hhreco/data/data/user5.sml");
//		set.addDataSetFileName("/windows/E/sketch/other systems/source/hhreco/data/data/user1.sml");
//		set.addDataSetFileName("/windows/E/sketch/other systems/source/hhreco/data/data/user9.sml");
		set.setMaxCatSize(-1);  // all data 
		set.addDataSetFileName("F:\\sketch\\other systems\\source\\hhreco\\data\\data\\user1.sml");
		set.addDataSetFileName("F:\\sketch\\other systems\\source\\hhreco\\data\\data\\user5.sml");
		
		set.addDataSetFileName("F:\\sketch\\other systems\\source\\hhreco\\data\\data\\user7.sml");
		
		set.addDataSetFileName("F:\\sketch\\other systems\\source\\hhreco\\data\\data\\user8.sml");
		
		set.addDataSetFileName("F:\\sketch\\other systems\\source\\hhreco\\data\\data\\user9.sml");

		set.addDataSetFileName("F:\\sketch\\other systems\\source\\hhreco\\data\\data\\user10.sml");
		set.addDataSetFileName("F:\\sketch\\other systems\\source\\hhreco\\data\\data\\user11.sml");
		set.addDataSetFileName("F:\\sketch\\other systems\\source\\hhreco\\data\\data\\user12.sml");
		
		//set.addDataSetFileName("/windows/E/sketch/other systems/source/hhreco/data/data/user2.sml");
	//	set.addDataSetFileName("F:\\sketch\\other systems\\source\\hhreco\\data\\data\\user5.sml");
	// added 
		
//		set.addDataSetFileName("F:\\sketch\\other systems\\source\\hhreco\\data\\data\\user2.sml");
//		set.addDataSetFileName("F:\\sketch\\other systems\\source\\hhreco\\data\\data\\user3.sml");
//		set.addDataSetFileName("F:\\sketch\\other systems\\source\\hhreco\\data\\data\\user4.sml");
//		set.addDataSetFileName("F:\\sketch\\other systems\\source\\hhreco\\data\\data\\user5.sml");
//		set.addDataSetFileName("F:\\sketch\\other systems\\source\\hhreco\\data\\data\\user7.sml");
//		
//		set.addDataSetFileName("F:\\sketch\\other systems\\source\\hhreco\\data\\data\\user9.sml");
	
		
		
		set.setDoTrain(true);
//		set.addDataSetFileName("/windows/E/sketch/other systems/source/hhreco/data/data/user9.sml");
//		set.addDataSetFileName("/windows/E/sketch/other systems/source/hhreco/data/data/user10.sml");
//		set.addDataSetFileName("/windows/E/sketch/other systems/source/hhreco/data/data/user11.sml");
//		set.addDataSetFileName("/windows/E/sketch/other systems/source/hhreco/data/data/user12.sml");
//		set.addDataSetFileName("/windows/E/sketch/other systems/source/hhreco/data/data/user13.sml");

		//	set.addDataSetFileName("F:\\sketch\\other systems\\source\\hhreco\\data\\data\\user1.sml");
	//	set.addDataSetFileName("F:\\sketch\\other systems\\source\\hhreco\\data\\data\\user5.sml");
		//set.addDTestSetFileName( "F:\\sketch\\other systems\\source\\hhreco\\data\\data\\user5.sml");
		//set.addDTestSetFileName("/windows/E/sketch/other systems/source/hhreco/data/data/user10.sml");
//	set.addDTestSetFileName("/windows/E/sketch/other systems/source/hhreco/data/data/user1.sml");
//	set.addDTestSetFileName("/windows/E/sketch/other systems/source/hhreco/data/data/user3.sml");
//	set.addDTestSetFileName("/windows/E/sketch/other systems/source/hhreco/data/data/user4.sml");
//		set.addDTestSetFileName("/windows/E/sketch/other systems/source/hhreco/data/data/user11.sml");
		set.addDTestSetFileName( "F:\\sketch\\other systems\\source\\hhreco\\data\\data\\user2.sml");
		set.addDTestSetFileName( "F:\\sketch\\other systems\\source\\hhreco\\data\\data\\user3.sml");
		set.addDTestSetFileName( "F:\\sketch\\other systems\\source\\hhreco\\data\\data\\user13.sml");
		set.addDTestSetFileName( "F:\\sketch\\other systems\\source\\hhreco\\data\\data\\user14.sml");
		set.addDTestSetFileName( "F:\\sketch\\other systems\\source\\hhreco\\data\\data\\user4.sml");
		set.setTestSize(5);
		set.setOSLinux(false);
		// change setttings 
		//set.getCurrentRecognizierOperation();
		
	    return set;
	}
	private void GeneralTest1(ArrayList<TestSketchSetting>  testList,TestSketchSetting set){
		
		TestSketchSetting set2;
		//------------------now start testing other things 
		set.SymbolFeaturesSettings(false,true,true,false,false,false,true,false,true,false,true );
		set.setSymbolFeaturesSettingsCOUNTCURVE(false);
		//------------------------------------Max iteraiton----------------------------------	
		// get same as previous test 
		 set2=(TestSketchSetting) set.clone();
		// change the main settings
		set2.setSwarmSettings(10,10);
		set2.setAlgorithmRunnings(false, false, true, false, false, false);
		
		//change its name 
		set2.setTestName("Only Alg 1 with 10 max iteration");
		/// now add it to list 
		testList.add(set2);  
		
		
		//-------------------------------------------------------------------------------
		// get same as previous test 
		 set2=(TestSketchSetting) set.clone();
		// change the main settings
		set2.setSwarmSettings(10,20);
		set2.setAlgorithmRunnings(false, false, true, false, false, false);
		//change its name 
		set2.setTestName("Only Alg 1 with 20 max iteration");
		/// now add it to list 
		testList.add(set2);  
		
		
		//-------------------------------------------------------------------------------
		// get same as previous test 
		 set2=(TestSketchSetting) set.clone();
		// change the main settings
		set2.setSwarmSettings(10,40);
		set2.setAlgorithmRunnings(false, false, true, false, false, false);
		//change its name 
		set2.setTestName("Only Alg 1 with 40 max iteration");
		/// now add it to list 
		testList.add(set2);  
		
		
		
		//-------------------------------------------------------------------------------
		// get same as previous test 
		 set2=(TestSketchSetting) set.clone();
		// change the main settings
		set2.setSwarmSettings(10,80);
		set2.setAlgorithmRunnings(false, false, true, false, false, false);
		//change its name 
		set2.setTestName("Only Alg 1 with 80 max iteration");
		/// now add it to list 
		testList.add(set2);  
	}
	
	
private void AlgorithmsTestVariations2(ArrayList<TestSketchSetting>  testList,TestSketchSetting set, String name){
		
		
		TestSketchSetting set2;
		

	
		//---------------------------------------------------------------------
		// get same as previous test 
		 set2=(TestSketchSetting) set.clone();
		// change the main settings
		set2.setAlgorithmRunnings(false, false, true, false, false, true);

		set2.setTestName(name+"AlgS1_EFit");
		/// now add it to list 
		testList.add(set2);    // add to list 
	
		
		//-------------------------------------------------------------------------------
		// get same as previous test 
		 set2=(TestSketchSetting) set.clone();
		// change the main settings
		set2.setAlgorithmRunnings(false, false, true, false, false, false);
	//	set2.setSettingsForPolygon(polygonSolution.POLYGON_ADJUST_BOTH);
	//	set2.FeatureAdjust(true);
		//set2.setSymbolFeaturesSettingsCOUNTCURVE(true);
		//set2.setSymbolFeaturesSettingsCOUNTCURVE(false);
	//	set2.setSymbolFeaturesSettingsCOUNTCURVE(false);
		//change its name 
		set2.setTestName(name+"AlgS1");
		/// now add it to list 
		testList.add(set2);    // add to list 
		//---------------------------------------------------------------------
		

		 set2=(TestSketchSetting) set.clone();
	//	 set2.FeatureAdjust(false);
		 
		//	set2.setSymbolFeaturesSettingsCOUNTCURVE(true);
		// change the main settings
		set2.setAlgorithmRunnings(false, false, false, true, false, true);
		//set2.setSettingsForPolygon(polygonSolution.POLYGON_ADJUST_BOTH);
		//change its name 
		set2.setTestName(name+"AlgS2_EFit");
		/// now add it to list 
		testList.add(set2);    // add to list 
		
		//-------------------------------------------------------------------------------
		// get same as previous test 
		 set2=(TestSketchSetting) set.clone();
		// set2.FeatureAdjust(false);
		// change the main settings
		set2.setAlgorithmRunnings(false, false, false, true, false,false);
		//set2.setSettingsForPolygon(polygonSolution.POLYGON_ADJUST_BOTH);
		//change its name 
		set2.setTestName(name+"AlgS2");
		/// now add it to list 
		testList.add(set2);    // add to list 

	
		//-------------------------------------------------------------------------------
		// get same as previous test 
		 set2=(TestSketchSetting) set.clone();
		// set2.FeatureAdjust(false);
		//set2.setSymbolFeaturesSettingsCOUNTCURVE(true);
		// change the main settings
		set2.setAlgorithmRunnings(false, false, true, true, false, true);
		//set2.setSettingsForPolygon(polygonSolution.POLYGON_ADJUST_BOTH);
		//change its name 
		set2.setTestName(name+"M_Er(A1,A2)_EF");
		/// now add it to list 
		testList.add(set2);  
		
		//-------------------------------------------------------------------------------
		// get same as previous test 
		 set2=(TestSketchSetting) set.clone();
		// set2.FeatureAdjust(false);
		// change the main settings
		set2.setAlgorithmRunnings(false, false, true, true, false, false);
		//set2.setSettingsForPolygon(polygonSolution.POLYGON_ADJUST_BOTH);
		
		//set2.FeatureAdjust(false);
		//change its name 
		set2.setTestName(name+"M_Er(A1,A2)");
		/// now add it to list 
		testList.add(set2);  

		
		//---------------------------------------------------------------------
			
			// get same as previous test 
			 set2=(TestSketchSetting) set.clone();
			 
			// change the main settings
			set2.setAlgorithmRunnings(false, false, false, false, true, true);
			 
			//change its name 
			set2.setTestName(name+"Alg3_EFit");
			/// now add it to list 
			testList.add(set2);   
			//---------------------------------------------------------------------
			
			// get same as previous test 
			 set2=(TestSketchSetting) set.clone();
			// change the main settings
			set2.setAlgorithmRunnings(false, false, false, false, true, false);
		 
			//set2.setSymbolFeaturesSettingsCOUNTCURVE(false);
			//change its name 
			set2.setTestName(name+"Alg3");
			/// now add it to list 
			testList.add(set2);    // add to list 
		
	
	}  
	
	
	
	
	private void AlgorithmsTestVariations(ArrayList<TestSketchSetting>  testList,TestSketchSetting set){
		
		
		TestSketchSetting set2;
		
		//---------------------------------------------------------------------
		
		// get same as previous test 
		 set2=(TestSketchSetting) set.clone();
		// change the main settings
		set2.setAlgorithmRunnings(false, false, false, false, true, false);
		set2.FeatureAdjust(false);
		set2.setSymbolFeaturesSettingsCOUNTCURVE(true);
		//set2.setSymbolFeaturesSettingsCOUNTCURVE(false);
		//change its name 
		set2.setTestName("Alg3");
		/// now add it to list 
		testList.add(set2);    // add to list 
		
		
		//---------------------------------------------------------------------
		
		// get same as previous test 
		 set2=(TestSketchSetting) set.clone();
		 set2.FeatureAdjust(false);
		// change the main settings
		set2.setAlgorithmRunnings(false, false, false, false, true, true);
		set2.setSymbolFeaturesSettingsCOUNTCURVE(true);
		//change its name 
		set2.setTestName("Alg3 + Ellipse");
		/// now add it to list 
		testList.add(set2);    // add to list 
	
		
		
		
		
		
//		//-------------------------------------------------------------------------------
//		// get same as previous test 
//		 set2=(TestSketchSetting) set.clone();
//		// change the main settings
//		set2.setAlgorithmRunnings(false, false, true,false, false, false);
//		set2.SymbolFeaturesSettings(false,true,true,false,false,false,true,false,true,false,true );
//		set2.setSymbolFeaturesSettingsCOUNTCURVE(false);
//		//change its name 
//		set2.setTestName("Only Alg1");
//		/// now add it to list 
//		testList.add(set2);    // add to list 
		
		
		//-------------------------------------------------------------------------------
		// get same as previous test 
		 set2=(TestSketchSetting) set.clone();
		// change the main settings
		set2.setAlgorithmRunnings(false, false, true, false, false, true);
		//set2.setSymbolFeaturesSettingsCOUNTCURVE(false);
		set2.FeatureAdjust(true);
		//set2.setSymbolFeaturesSettingsCOUNTCURVE(true);
//		set.setSwarmSettings(30,180);
//
////		//swarm cettings c1, c2, w, vmax
//		set.swarmSettings(1.6,1.8,2,10);
		set2.setSettingsForPolygon(polygonSolution.POLYGON_ADJUST_BOTH);
		//change its name 
		set2.setTestName("Alg1  + Ellipse");
		/// now add it to list 
		testList.add(set2);    // add to list 
		
		
		//-------------------------------------------------------------------------------
		// get same as previous test 
		 set2=(TestSketchSetting) set.clone();
		// change the main settings
		set2.setAlgorithmRunnings(false, false, true, false, false, false);
		set2.setSettingsForPolygon(polygonSolution.POLYGON_ADJUST_BOTH);
		set2.FeatureAdjust(true);
		//set2.setSymbolFeaturesSettingsCOUNTCURVE(true);
		//set2.setSymbolFeaturesSettingsCOUNTCURVE(false);
	//	set2.setSymbolFeaturesSettingsCOUNTCURVE(false);
		//change its name 
		set2.setTestName("Alg1");
		/// now add it to list 
		testList.add(set2);    // add to list 
		
		
		//-------------------------------------------------------------------------------
//		// get same as previous test 
//		 set2=(TestSketchSetting) set.clone();
//		// change the main settings
//		set2.setAlgorithmRunnings(false, false, false, true, false, false);
//		//change its name 
//		set2.setTestName("Only Alg2");
//		/// now add it to list 
//		testList.add(set2);    // add to list 
		//-------------------------------------------------------------------------------
		// get same as previous test 
		 set2=(TestSketchSetting) set.clone();
		 set2.FeatureAdjust(false);
		 
			set2.setSymbolFeaturesSettingsCOUNTCURVE(true);
		// change the main settings
		set2.setAlgorithmRunnings(false, false, false, true, false, true);
		set2.setSettingsForPolygon(polygonSolution.POLYGON_ADJUST_BOTH);
		//change its name 
		set2.setTestName("Alg2 + Ellipse");
		/// now add it to list 
		testList.add(set2);    // add to list 
		
		//-------------------------------------------------------------------------------
		// get same as previous test 
		 set2=(TestSketchSetting) set.clone();
		 set2.FeatureAdjust(false);
		// change the main settings
		set2.setAlgorithmRunnings(false, false, false, true, false,false);
		set2.setSettingsForPolygon(polygonSolution.POLYGON_ADJUST_BOTH);
		//change its name 
		set2.setTestName("Alg2");
		/// now add it to list 
		testList.add(set2);    // add to list 
		
		
		
//		//-------------------------------------------------------------------------------
//		// get same as previous test 
//		 set2=(TestSketchSetting) set.clone();
//		// change the main settings
//		set2.setAlgorithmRunnings(false, false, true, true, false, false);
//		//change its name 
//		set2.setTestName("Alg1 and Alg 2 ");
//		/// now add it to list 
//		testList.add(set2);  
		
		
		//-------------------------------------------------------------------------------
		// get same as previous test 
		 set2=(TestSketchSetting) set.clone();
		 set2.FeatureAdjust(false);
			set2.setSymbolFeaturesSettingsCOUNTCURVE(true);
		// change the main settings
		set2.setAlgorithmRunnings(false, false, true, true, false, true);
		set2.setSettingsForPolygon(polygonSolution.POLYGON_ADJUST_BOTH);
		//change its name 
		set2.setTestName("min Er(Alg1, Alg2)+ Ellipse");
		/// now add it to list 
		testList.add(set2);  
		
		//-------------------------------------------------------------------------------
		// get same as previous test 
		 set2=(TestSketchSetting) set.clone();
		 set2.FeatureAdjust(false);
		// change the main settings
		set2.setAlgorithmRunnings(false, false, true, true, false, false);
		set2.setSettingsForPolygon(polygonSolution.POLYGON_ADJUST_BOTH);
		
		//set2.FeatureAdjust(false);
		//change its name 
		set2.setTestName("min Er(Alg1, Alg2)");
		/// now add it to list 
		testList.add(set2);  
//		//-------------------------------------------------------------------------------
//		// get same as previous test 
//		 set2=(TestSketchSetting) set.clone();
//		// change the main settings
//		set2.setAlgorithmRunnings(false, false, true, true, true, false);
//		//change its name 
//		set2.setTestName(" All algorithms without circle detection");
//		/// now add it to list 
//		testList.add(set2);  
//		
//		//-------------------------------------------------------------------------------
//		// get same as previous test 
//		 set2=(TestSketchSetting) set.clone();
//		// change the main settings
//		set2.setAlgorithmRunnings(false, false, true, true, true, true);
//		//change its name 
//		set2.setTestName(" All algorithms  with circle detection");
//		/// now add it to list 
//		testList.add(set2);  
		
		
	
		
	
		
		//-------------------------------------------------------------------------------	
	/*	// get same as previous test 
		 set2=(TestSketchSetting) set.clone();
		// change the main settings
		set2.setSwarmSettings(10,160*2);
		set2.setAlgorithmRunnings(false, false, true, false, false, false);
		//change its name 
		set2.setTestName("Only Alg 1 with 320 max iteration");
		/// now add it to list 
		testList.add(set2);  
		
		//-------------------------Error threshold----------------
		//============================
		// get same as previous test 
		 set2=(TestSketchSetting) set.clone();
		// change the main settings
		set2.setAlgorithmRunnings(false, false, true,false, false, false);
		// bezire error, curve test, hybired tol( no use), solution error, solution intial tol, second pass
		set2.SetErrorThresholds( -1.0,-1.0,-1,1e1,-1,false);
		//change its name 
		set2.setTestName("Only Alg1 error 1e1");
		/// now add it to list 
		testList.add(set2);    // add to list 
		
		//============================
		// get same as previous test 
		 set2=(TestSketchSetting) set.clone();
		// change the main settings
		set2.setAlgorithmRunnings(false, false, true,false, false, false);
		// bezire error, curve test, hybired tol( no use), solution error, solution intial tol, second pass
		set2.SetErrorThresholds( -1.0,-1.0,-1,1e2,-1,false);
		//change its name 
		set2.setTestName("Only Alg1 error 1e2");
		/// now add it to list 
		testList.add(set2);    // add to list 
		
		
		
		
		//============================
		// get same as previous test 
		 set2=(TestSketchSetting) set.clone();
		// change the main settings
		set2.setAlgorithmRunnings(false, false, true,false, false, false);
		// bezire error, curve test, hybired tol( no use), solution error, solution intial tol, second pass
		set2.SetErrorThresholds( -1.0,-1.0,-1,1e3,-1,false);
		//change its name 
		set2.setTestName("Only Alg1 error 1e3");
		/// now add it to list 
		testList.add(set2);    // add to list 
		
		
		
		
		//============================
		// get same as previous test 
		 set2=(TestSketchSetting) set.clone();
		// change the main settings
		set2.setAlgorithmRunnings(false, false, true,false, false, false);
		// bezire error, curve test, hybired tol( no use), solution error, solution intial tol, second pass
		set2.SetErrorThresholds( -1.0,-1.0,-1,1e4,-1,false);
		//change its name 
		set2.setTestName("Only Alg1 error 1e4");
		/// now add it to list 
		testList.add(set2);    // add to list 
		
		
		
		
		
		//============================
		// get same as previous test 
		 set2=(TestSketchSetting) set.clone();
		// change the main settings
		set2.setAlgorithmRunnings(false, false, true,false, false, false);
		// bezire error, curve test, hybired tol( no use), solution error, solution intial tol, second pass
		set2.SetErrorThresholds( -1.0,-1.0,-1,1e7,-1,false);
		//change its name 
		set2.setTestName("Only Alg1 error 1e7");
		/// now add it to list 
		testList.add(set2);    // add to list 
	*/
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
	
	HashMap<String, Integer> catCounts;
	private boolean UseLeaveOneOut=false;
	private boolean UseSeventyPercent=true; 
	private boolean  UseFifthOne=false;
	
private void	AddCountToCat(String cat, int count){
	if ( catCounts!=null){
		  if (	  catCounts.containsKey(cat))
		  {
			  int c= catCounts.get(cat);
		 
			  catCounts.put(cat,c+count);
		  }
		  else{
			  
			  
			  catCounts.put(cat, new Integer (count));
			  
		  }
		
	}
}
	private String CatHashToString(){
		 String  s="----------------------------------------";
		String newline = System.getProperty("line.separator");
		s+=newline;
		int totalcount=0;
		if (catCounts!=null){
		for (Iterator iterator =  catCounts.keySet().iterator(); iterator.hasNext();) {
			String cat = (String) iterator.next();
			 totalcount =catCounts.get(cat);
			s+=" Category  "+cat+"  has "+ totalcount;
			s+=newline;
		}
		
		}
		return s;
	}
	private String doStateOnFile(String file ){
		HandleDataSetEvents dataSet=new HandleDataSetEvents();
		 String  s="";
	     s+=" File  "+file;
		String newline = System.getProperty("line.separator");
		s+=newline;
		dataSet.readXML(file);
		ArrayList<String> Cat = dataSet.getCategoriesList();
		for (int i = 0; i < Cat.size(); i++) {
		 
		  ArrayList<ArrayList<Stroke>> Examples = dataSet.getExampleForCat(Cat.get(i));
		  // 
		  s+=" Cat "+i+" "+Cat.get(i)+"  "+  Examples.size();
		  AddCountToCat(Cat.get(i),Examples.size());
		  
			s+=newline;
		  
		}
		return s;
	}
	private void doStateOnAllDataBase(String StoreFile){
		 String path =getDataBaseMainPath();
		 String extention=getDataBaseExtention();
	     String  s="";
		   String filename;
			String newline = System.getProperty("line.separator");
			
			 catCounts=new HashMap<String, Integer>();
		  for (int i = 1; i <  CurrentFilesMaxNumber; i++) {
			   filename=path+"user"+i+extention;
				s+=doStateOnFile(filename);
				s+=newline;
		  }
			s+=newline;
		  s+=CatHashToString();
			s+=newline;
		  Writer output = null;
		    
		    File file = new File(StoreFile);
		    try {
				output = new BufferedWriter(new FileWriter(file));
		
		    output.write(s);
		    output.close();
			} catch (IOException e) {
				 
				e.printStackTrace();
			}
		  
		
	}
	private void doStateOnTrainAndTest(String StoreFile){
		 
	     String  s="";
	     catCounts=new HashMap<String, Integer>();
		String newline = System.getProperty("line.separator");
		s+=newline;
		for (int i = 0; i < TrainFilenames.size(); i++) {
			s+=doStateOnFile(TrainFilenames.get(i));
			s+=newline;
		}
		s+=newline;
		  s+=CatHashToString();
			s+=newline;
			  catCounts=new HashMap<String, Integer>();
			s+=newline;
			s+="------------------Test samples --------------------------------------";
			s+=newline;
		for (int i = 0; i < TestFilenames.size(); i++) {
			s+= doStateOnFile( TestFilenames.get(i));
			s+=newline;
		}
		
		s+=newline;
		  s+=CatHashToString();
			s+=newline;
		  Writer output = null;
		    
		    File file = new File(StoreFile);
		    try {
				output = new BufferedWriter(new FileWriter(file));
		
		    output.write(s);
		    output.close();
			} catch (IOException e) {
				 
				e.printStackTrace();
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
    private int getDataBaseMaxCount(){
    	
    	 int path=20;
 		if (DataBase==DATA_HSE )
 		  path =20;
 		else if (DataBase==DATA_MINE_ELECTERICAL)
 			//F:\sketch\Data Sets\Mine\elect
 			path =16;
 		else if (DataBase==DATA_MINE_DIGITAL)
 			//F:\sketch\Data Sets\Mine\logic
 			path =9;
		else if (DataBase==DATA_MINE_ELECTERICAL_R)
			//F:\sketch\Data Sets\Mine\elect
			path =11;   // 8 file last is skew
		else if (DataBase==DATA_MINE_DIGITAL_R)
			//F:\sketch\Data Sets\Mine\logic
			path =11;  //7 file last is sketw 
		else if (DataBase==DATA_MINE_SWARM_STROKES)
			//F:\sketch\Data Sets\Mine\logic
			path =3;  //7 file last is sketw 
 		
    	return path;
    }	 
    private void  GenerateRandomAllTestSet(){
    
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
			 
				 TrainFilenames.add( AllFiles.get(i));
				 if (i%3==0){
				  TestFilenames.add( AllFiles.get(i));
				 }
		 
			// logger.trace(" Randomize  "+i+" =   " +  AllFiles.get(i));
			
		}
		   
		 
    	
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
	private void GenerateSameFileTestSet(){
		 TestFilenames=new ArrayList<String>();
		   TrainFilenames=new ArrayList<String>();
		 String path =getDataBaseMainPath();
		 String extention=getDataBaseExtention();
		   String filename;
		   if (DataBase!=DATA_MINE_SWARM_STROKES)
		   filename=path+"user"+5+extention;
		   else 
			   filename=path+"user"+1+extention;
		TestFilenames.add( filename);
		TrainFilenames.add( filename);
//		  filename=path+"user"+2+extention;
//			TestFilenames.add(  filename);
//			TrainFilenames.add(  filename);
		 // filename=path+"user"+1+extention;
		
	}
	
	
	
	private TestSketchSetting OnlyF1Feat() {
		 TestSketchSetting set;
			set=new TestSketchSetting(); // initalize a new set.
			set.GetStoredSetting();
			set.setRecognizierType( TestSketchSetting.SVM_RECOGNIZER);
			// all, no swarm,algorithms ( 1 2 3 ), circle.  
			set.setAlgorithmRunnings(false, false,true, true,false, true);
			//(150 iteration and 15 agent) with 3 training files and 2false test file
			set.setTestName("AllAlgFS1_");
			// all, no swarm,algorithms ( 1 2 3 ), circle.  
			//set.setAlgorithmRunnings(false, false,false, false, true, false);
			set.setDataSetType(set.DATA_SET_TYPE_XML);
			
			set.setMaxCatSize(-1);  // all data 
			
			for (int i = 0; i < TrainFilenames.size(); i++) {
				set.addDataSetFileName(new String (TrainFilenames.get(i)));
			}
			
//			set.setSwarmSettings(10,80);
//			// resample  minn segment (for segmentations) and Max iterpolation length then min stroke pixel 
//			set.setStrokeLengthSettings(false, -1,-1, -1);
//			set.setSettingsForPolygon(polygonSolution.POLYGON_ADJUST_NONE);
//			// bezire error, curve test, hybired tol( no use), solution error, solution intial tol, second pass
//			set.SetErrorThresholds( -1.0,-1.0,-1,-1,-1,false);
//			//swarm cettings c1, c2, w, vmax
//			set.swarmSettings(1.8,1.8,2,8);
//			// now for features. 
//			// those are the defaults 
//		//  primitive,count, count relations, centroid, convex hul, raions, area, logstatics, density, rubine, zenerik
//				//true,false,true,false,false,false,false,false,true,false,true 
				set.ClearFeat();
				set.setFeatures(1);
				set.setOSLinux(false);
			for (int i = 0; i < TestFilenames.size(); i++) {
				set.addDTestSetFileName(new String (TestFilenames.get(i)));
			}
			set.setTestSize(TestFilenames.size());
	 
         //set.ResetAllSettings();
		    return set;
	}

	private TestSketchSetting AllAllFeatMyAlg() {
		 TestSketchSetting set;
			set=new TestSketchSetting(); // initalize a new set.
			set.GetStoredSetting();
			set.setRecognizierType( TestSketchSetting.SVM_RECOGNIZER);
			// all, no swarm,algorithms ( 1 2 3 ), circle.  
			set.setAlgorithmRunnings(false, false,true, true,false, true);
			//(150 iteration and 15 agent) with 3 training files and 2 test file
			set.setTestName("AllFeat_");
			// all, no swarm,algorithms ( 1 2 3 ), circle.  
			//set.setAlgorithmRunnings(false, false,false, false, true, false);
			set.setDataSetType(set.DATA_SET_TYPE_XML);
			
			set.setMaxCatSize(-1);  // all data 
			
			for (int i = 0; i < TrainFilenames.size(); i++) {
				set.addDataSetFileName(new String (TrainFilenames.get(i)));
			}
			set.setSwarmSettings(15,60);
			set.swarmSettings(2.5,2.5,1.5,4);
//			set.setSwarmSettings(10,80);
//			// resample  minn segment (for segmentations) and Max iterpolation length then min stroke pixel 
//			set.setStrokeLengthSettings(false, -1,-1, -1);
//			set.setSettingsForPolygon(polygonSolution.POLYGON_ADJUST_NONE);
//			// bezire error, curve test, hybired tol( no use), solution error, solution intial tol, second pass
//			set.SetErrorThresholds( -1.0,-1.0,-1,-1,-1,false);
//			//swarm cettings c1, c2, w, vmax
//			set.swarmSettings(1.8,1.8,2,8);
//			// now for features. 
//			// those are the defaults 
//		//  primitive,count, count relations, centroid, convex hul, raions, area, logstatics, density, rubine, zenerik
//				//true,false,true,false,false,false,false,false,true,false,true 
				set.SymbolFeaturesSettings(true,true,true,true,true,true,true,true,true,true,true );
				set.setSymbolFeaturesSettingsCOUNTCURVE(true);
			set.setOSLinux(false);
			for (int i = 0; i < TestFilenames.size(); i++) {
				set.addDTestSetFileName(new String(TestFilenames.get(i)));
			}
			set.setTestSize(TestFilenames.size());
	 
           //set.ResetAllSettings();
		    return set;
	}
	private TestSketchSetting AllAllFeaAlg2() {
		 TestSketchSetting set;
			set=new TestSketchSetting(); // initalize a new set.
			set.GetStoredSetting();
			set.setRecognizierType( TestSketchSetting.SVM_RECOGNIZER);
			// all, no swarm,algorithms ( 1 2 3 ), circle.  
			set.setAlgorithmRunnings(false, false,false,true,false, true);
			//(150 iteration and 15 agent) with 3 training files and 2 test file
			set.setTestName("Syst_");
			// all, no swarm,algorithms ( 1 2 3 ), circle.  
			//set.setAlgorithmRunnings(false, false,false, false, true, false);
			set.setDataSetType(set.DATA_SET_TYPE_XML);
			set.setMaxCatSize(-1);  // all data 
			for (int i = 0; i < TrainFilenames.size(); i++) {
				set.addDataSetFileName(TrainFilenames.get(i));
			}		
			set.setSwarmSettings(15,60);
			set.swarmSettings(2.5,2.5,1.5,4);
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
			set.setOSLinux(false);
			for (int i = 0; i < TestFilenames.size(); i++) {
				set.addDTestSetFileName(TestFilenames.get(i));
			}
			set.setTestSize(TestFilenames.size());

	     //set.ResetAllSettings();
		    return set;
	}
private void FeatureTestVariations(ArrayList<TestSketchSetting>  testList,TestSketchSetting set, String name){
		
		
		TestSketchSetting set2;
		//---------------------------------------------------------------------
		
		// get same as previous test 
		 set2=(TestSketchSetting) set.clone();
		// change the main settings
		 set2.ClearFeat();
	     set2.setFeatures(1);
		//set2.setSymbolFeaturesSettingsCOUNTCURVE(false);
		//change its name 
		set2.setTestName(name+"FS1_");
		/// now add it to list 
		testList.add(set2);    // add to list 
		//---------------------------------------------------------------------
		//---------------------------------------------------------------------
		
		// get same as previous test 
		 set2=(TestSketchSetting) set.clone();
		// change the main settings
		 set2.ClearFeat();
		//set2.setAlgorithmRunnings(false, false, false, false, true, false);
	     set2.setFeatures(2);
		//set2.setSymbolFeaturesSettingsCOUNTCURVE(false);
		//change its name 
		set2.setTestName(name+"FS2_");
		/// now add it to list 
		testList.add(set2);    // add to list 
		//---------------------------------------------------------------------
		//---------------------------------------------------------------------
		
		// get same as previous test 
		 set2=(TestSketchSetting) set.clone();
		// change the main settings
		//set2.setAlgorithmRunnings(false, false, false, false, true, false);
		 set2.ClearFeat();
	     set2.setFeatures(3);
		//set2.setSymbolFeaturesSettingsCOUNTCURVE(false);
		//change its name 
		set2.setTestName(name+"FS3_n10_");
		set2.setMomemtsOrder(10);
		/// now add it to list 
		testList.add(set2);    // add to list 
		//---------------------------------------------------------------------
		//---------------------------------------------------------------------
		
		// get same as previous test 
		 set2=(TestSketchSetting) set.clone();
		// change the main settings
		//set2.setAlgorithmRunnings(false, false, false, false, true, false);
		 set2.ClearFeat();
	     set2.setFeatures(3);
		//set2.setSymbolFeaturesSettingsCOUNTCURVE(false);
		//change its name 
		set2.setTestName(name+"FS3_n20_");
		set2.setMomemtsOrder(20);
		/// now add it to list 
		testList.add(set2);    // add to list 
		//---------------------------------------------------------------------
		
		// get same as previous test 
		 set2=(TestSketchSetting) set.clone();
		// change the main settings
		//set2.setAlgorithmRunnings(false, false, false, false, true, false);
		 set2.ClearFeat();
	     set2.setFeatures(3);
		//set2.setSymbolFeaturesSettingsCOUNTCURVE(false);
		//change its name 
		set2.setTestName(name+"FS3_n40_");
		set2.setMomemtsOrder(40);
		/// now add it to list 
		testList.add(set2);    // add to list 
		
		//-------------------------------------------------
		// get same as previous test 
		 set2=(TestSketchSetting) set.clone();
		// change the main settings
		//set2.setAlgorithmRunnings(false, false, false, false, true, false);
		 set2.ClearFeat();
	     set2.setFeatures(4);
		//set2.setSymbolFeaturesSettingsCOUNTCURVE(false);
		//change its name 
		set2.setTestName(name+"FS4_");
		
		/// now add it to list 
		testList.add(set2);    // add to list 
		//---------------------------------------------------------------------
		
		//---------------------------------------------------------------------
		
		// get same as previous test 
		 set2=(TestSketchSetting) set.clone();
		// change the main settings
		//set2.setAlgorithmRunnings(false, false, false, false, true, false);
		 set2.ClearFeat();
	     set2.setFeatures(1);
	     set2.setFeatures(2);
		//set2.setSymbolFeaturesSettingsCOUNTCURVE(false);
		//change its name 
		set2.setTestName(name+"FS1_FS2");
		/// now add it to list 
		testList.add(set2);    // add to list 
		//---------------------------------------------------------------------
		//---------------------------------------------------------------------
		
		// get same as previous test 
		 set2=(TestSketchSetting) set.clone();
		// change the main settings
		//set2.setAlgorithmRunnings(false, false, false, false, true, false);
		 set2.ClearFeat();
	     set2.setFeatures(1);
	     set2.setFeatures(3);
		//set2.setSymbolFeaturesSettingsCOUNTCURVE(false);
		//change its name 
		set2.setTestName(name+"FS1_FS3");
		/// now add it to list 
		testList.add(set2);    // add to list 
		//---------------------------------------------------------------------
		
		// get same as previous test 
		 set2=(TestSketchSetting) set.clone();
		// change the main settings
		//set2.setAlgorithmRunnings(false, false, false, false, true, false);
		 set2.ClearFeat();
	     set2.setFeatures(1);
	     set2.setFeatures(4);
		//set2.setSymbolFeaturesSettingsCOUNTCURVE(false);
		//change its name 
		set2.setTestName(name+"FS1_FS4");
		/// now add it to list 
		testList.add(set2);    // add to list 
		//---------------------------------------------------------------------
		
		// get same as previous test 
		 set2=(TestSketchSetting) set.clone();
		// change the main settings
		//set2.setAlgorithmRunnings(false, false, false, false, true, false);
		 set2.ClearFeat();
	     set2.setFeatures(1);
	     set2.setFeatures(2);
	     set2.setFeatures(3);
		//set2.setSymbolFeaturesSettingsCOUNTCURVE(false);
		//change its name 
		set2.setTestName(name+"FS1FS2FS3");
		/// now add it to list 
		testList.add(set2);    // add to list 
		//---------------------------------------------------------------------
		// get same as previous test 
		 set2=(TestSketchSetting) set.clone();
		// change the main settings
		//set2.setAlgorithmRunnings(false, false, false, false, true, false);
		 set2.ClearFeat();
	     set2.setFeatures(1);
	     set2.setFeatures(2);
	     set2.setFeatures(4);
		//set2.setSymbolFeaturesSettingsCOUNTCURVE(false);
		//change its name 
		set2.setTestName(name+"FS1FS2FS4");
		/// now add it to list 
		testList.add(set2);    // add to list 
		//---------------------------------------------------------------------
		// get same as previous test 
		 set2=(TestSketchSetting) set.clone();
		// change the main settings
		//set2.setAlgorithmRunnings(false, false, false, false, true, false);
		 set2.ClearFeat();
	     set2.setFeatures(2);
	     set2.setFeatures(3);
	     set2.setFeatures(4);
		//set2.setSymbolFeaturesSettingsCOUNTCURVE(false);
		//change its name 
		set2.setTestName(name+"FS2FS3FS4");
		/// now add it to list 
		testList.add(set2);    // add to list 
		
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
			set.setOSLinux(false);
			for (int i = 0; i < TestFilenames.size(); i++) {
				set.addDTestSetFileName(TestFilenames.get(i));
			}
			set.setTestSize(TestFilenames.size());
	 
            //set.ResetAllSettings();
		    return set;
	}
    
	
	//19-4
    @Deprecated
	private TestSketchSetting Alg1SettingGeneralSVM() {
		 TestSketchSetting set;
			set=new TestSketchSetting(); // initalize a new set.
			set.GetStoredSetting();
			set.setRecognizierType( TestSketchSetting.SVM_RECOGNIZER);
			// all, no swarm,algorithms ( 1 2 3 ), circle.  
			set.setAlgorithmRunnings(false, false,true, false,false, false);
			//(150 iteration and 15 agent) with 3 training files and 2 test file
			set.setTestName("Only Alg1");
			// all, no swarm,algorithms ( 1 2 3 ), circle.  
			//set.setAlgorithmRunnings(false, false,false, false, true, false);
			set.setDataSetType(set.DATA_SET_TYPE_XML);
			
			set.setMaxCatSize(-1);  // all data 
			
			for (int i = 0; i < TrainFilenames.size(); i++) {
				set.addDataSetFileName(TrainFilenames.get(i));
			}
			
//			set.setSwarmSettings(10,80);
//			// resample  minn segment (for segmentations) and Max iterpolation length then min stroke pixel 
//			set.setStrokeLengthSettings(false, -1,-1, -1);
//			set.setSettingsForPolygon(polygonSolution.POLYGON_ADJUST_NONE);
//			// bezire error, curve test, hybired tol( no use), solution error, solution intial tol, second pass
//			set.SetErrorThresholds( -1.0,-1.0,-1,-1,-1,false);
//			//swarm cettings c1, c2, w, vmax
//			set.swarmSettings(1.8,1.8,2,8);
//			// now for features. 
//			// those are the defaults 
//		//  primitive,count, count relations, centroid, convex hul, raions, area, logstatics, density, rubine, zenerik
//				//true,false,true,false,false,false,false,false,true,false,true 
//				set.SymbolFeaturesSettings(true,true,true,false,false,false,true,false,true,false,true );
//				set.setSymbolFeaturesSettingsCOUNTCURVE(true);
			set.setOSLinux(false);
			for (int i = 0; i < TestFilenames.size(); i++) {
				set.addDTestSetFileName(TestFilenames.get(i));
			}
			set.setTestSize(TestFilenames.size());
	 
             //set.ResetAllSettings();
		    return set;
	}

	

    private TestSketchSetting swarmTesting(){
    	
    	
    	 TestSketchSetting set;
			set=new TestSketchSetting(); // initalize a new set.
			set.GetStoredSetting();
			set.setRecognizierType( TestSketchSetting.SVM_RECOGNIZER);
			// all, no swarm,algorithms ( 1 2 3 ), circle.  
			set.setAlgorithmRunnings(true, false,true, true,false,true);
			//(150 iteration and 15 agent) with 3 training files and 2 test file
			set.setTestName("All_alg_");
			// all, no swarm,algorithms ( 1 2 3 ), circle.  
			//set.setAlgorithmRunnings(false, false,false, false, true, false);
			set.setDataSetType(set.DATA_SET_TYPE_XML);
			
			set.setMaxCatSize(-1);  // all data 
			
			for (int i = 0; i < TrainFilenames.size(); i++) {
				set.addDataSetFileName(TrainFilenames.get(i));
			}
			
//			set.setSwarmSettings(10,80);
//			// resample  minn segment (for segmentations) and Max iterpolation length then min stroke pixel 
//			set.setStrokeLengthSettings(false, -1,-1, -1);
//			set.setSettingsForPolygon(polygonSolution.POLYGON_ADJUST_NONE);
//			// bezire error, curve test, hybired tol( no use), solution error, solution intial tol, second pass
//			set.SetErrorThresholds( -1.0,-1.0,-1,-1,-1,false);
//			//swarm cettings c1, c2, w, vmax
//			set.swarmSettings(1.8,1.8,2,8);
//			// now for features. 
//			// those are the defaults 
//		//  primitive,count, count relations, centroid, convex hul, raions, area, logstatics, density, rubine, zenerik
//				//true,false,true,false,false,false,false,false,true,false,true 
//				set.SymbolFeaturesSettings(true,true,true,false,false,false,true,false,true,false,true );
//				set.setSymbolFeaturesSettingsCOUNTCURVE(true);
			set.setOSLinux(false);
			for (int i = 0; i < TestFilenames.size(); i++) {
				set.addDTestSetFileName(TestFilenames.get(i));
			}
			set.setTestSize(TestFilenames.size());
	 
          //set.ResetAllSettings();
		    return set;
   	
    	
    }
    ////19-4
    ////@Deprecated
    
    private TestSketchSetting swarmTestingAlg1(){
    	
    	
   	 TestSketchSetting set;
			set=new TestSketchSetting(); // initalize a new set.
			set.GetStoredSetting();
			set.setRecognizierType( TestSketchSetting.SVM_RECOGNIZER);
			// all, no swarm,algorithms ( 1 2 3 ), circle.  
			set.setAlgorithmRunnings(false, false,true, false,false,true);
			//(150 iteration and 15 agent) with 3 training files and 2 test file
			set.setTestName("Alg1_");
			// all, no swarm,algorithms ( 1 2 3 ), circle.  
			//set.setAlgorithmRunnings(false, false,false, false, true, false);
			set.setDataSetType(set.DATA_SET_TYPE_XML);
			
			set.setMaxCatSize(-1);  // all data 
			
			for (int i = 0; i < TrainFilenames.size(); i++) {
				set.addDataSetFileName(TrainFilenames.get(i));
			}
			
			set.setSwarmSettings(15,80);
//			// resample  minn segment (for segmentations) and Max iterpolation length then min stroke pixel 
//			set.setStrokeLengthSettings(false, -1,-1, -1);
			set.setSettingsForPolygon(polygonSolution.POLYGON_ADJUST_NONE);
//			// bezire error, curve test, hybired tol( no use), solution error, solution intial tol, second pass
//			set.SetErrorThresholds( -1.0,-1.0,-1,-1,-1,false);
//			//swarm cettings c1, c2, w, vmax
//			set.swarmSettings(1.8,1.8,2,8);
//			// now for features. 
//			// those are the defaults 
//		//  primitive,count, count relations, centroid, convex hul, raions, area, logstatics, density, rubine, zenerik
//				//true,false,true,false,false,false,false,false,true,false,true 
//				set.SymbolFeaturesSettings(true,true,true,false,false,false,true,false,true,false,true );
//				set.setSymbolFeaturesSettingsCOUNTCURVE(true);
			set.setOSLinux(false);
			for (int i = 0; i < TestFilenames.size(); i++) {
				set.addDTestSetFileName(TestFilenames.get(i));
			}
			set.setTestSize(TestFilenames.size());
	 
         //set.ResetAllSettings();
		    return set;
  	
   	
   }
   //19-4
 
    private TestSketchSetting swarmTestingAlg2(){
    	
    	
      	 TestSketchSetting set;
   			set=new TestSketchSetting(); // initalize a new set.
   			set.GetStoredSetting();
   			set.setRecognizierType( TestSketchSetting.SVM_RECOGNIZER);
   			// all, no swarm,algorithms ( 1 2 3 ), circle.  
   			set.setAlgorithmRunnings(false, false,false, true,false,true);
   			//(150 iteration and 15 agent) with 3 training files and 2 test file
   			set.setTestName("Alg2_");
   			// all, no swarm,algorithms ( 1 2 3 ), circle.  
   			//set.setAlgorithmRunnings(false, false,false, false, true, false);
   			set.setDataSetType(set.DATA_SET_TYPE_XML);
   			
   			set.setMaxCatSize(-1);  // all data 
   			
   			for (int i = 0; i < TrainFilenames.size(); i++) {
   				set.addDataSetFileName(TrainFilenames.get(i));
   			}
   			
   			set.setSwarmSettings(15,80);
   			set.setSettingsForPolygon(polygonSolution.POLYGON_ADJUST_NONE);

   			set.setOSLinux(false);
   			for (int i = 0; i < TestFilenames.size(); i++) {
   				set.addDTestSetFileName(TestFilenames.get(i));
   			}
   			set.setTestSize(TestFilenames.size());
   	 
            //set.ResetAllSettings();
   		    return set;
     	
      	
      }
    
    
private void AlgSwarmTestVariationsMax(ArrayList<TestSketchSetting>  testList,TestSketchSetting set, String test, int agents){
		
		
		TestSketchSetting set2;
		
		
	//---------------------------------------------------------------------
	for (int i = 2; i < 10; i+=2) {
		// get same as previous test 
		 set2=(TestSketchSetting) set.clone();
			set2.setSwarmSettings(agents,i);

//			//swarm cettings c1, c2, w, vmax
			//set2.swarmSettings(1.6,1.8,2,10);
	 
		set2.setTestName(test+"swarm"+i);
		/// now add it to list 
		testList.add(set2);    // add to list 
	//---------------------------------------------------
	}
	
	
	
	
	
	//---------------------------------------------------------------------
	for (int i = 10; i < 800; i+=30) {
		// get same as previous test 
		 set2=(TestSketchSetting) set.clone();
			set2.setSwarmSettings(agents,i);

//			//swarm cettings c1, c2, w, vmax
			//set2.swarmSettings(1.6,1.8,2,10);
	 
		set2.setTestName(test+"swarm"+i);
		/// now add it to list 
		testList.add(set2);    // add to list 
	//---------------------------------------------------
	}
	/*
	//---------------------------------------------------------------------
	for (int i = 100; i < 400; i+=25) {
		// get same as previous test 
		 set2=(TestSketchSetting) set.clone();
			set2.setSwarmSettings(agents,i);

//			//swarm cettings c1, c2, w, vmax
			//set2.swarmSettings(1.6,1.8,2,10);
	 
		set2.setTestName(test+"swarm"+i);
		/// now add it to list 
		testList.add(set2);    // add to list 
	//---------------------------------------------------
	}
	//---------------------------------------------------------------------
	for (int i = 400; i < 800; i+=50) {
		// get same as previous test 
		 set2=(TestSketchSetting) set.clone();
			set2.setSwarmSettings(agents,i);

//			//swarm cettings c1, c2, w, vmax
			//set2.swarmSettings(1.6,1.8,2,10);
	 
		set2.setTestName(test+"swarm"+i);
		/// now add it to list 
		testList.add(set2);    // add to list 
	//---------------------------------------------------
	}
	*/
	
	
	
	
	/*
	//------------------
		
		// get same as previous test 
		 set2=(TestSketchSetting) set.clone();
			set2.setSwarmSettings(agents,2);

//			//swarm cettings c1, c2, w, vmax
			//set2.swarmSettings(1.6,1.8,2,10);
	 
		set2.setTestName(test+"swarm2");
		/// now add it to list 
		testList.add(set2);    // add to list 
		
	//---------------------------------------------------------------------
	//---------------------------------------------------------------------
		
		// get same as previous test 
		 set2=(TestSketchSetting) set.clone();
			set2.setSwarmSettings(agents,3);

//			//swarm cettings c1, c2, w, vmax
			//set2.swarmSettings(1.6,1.8,2,10);
	 
		set2.setTestName(test+"swarm3");
		/// now add it to list 
		testList.add(set2);    // add to list 
	//----------------------------------------------------------
	//---------------------------------------------------------------------
		
		// get same as previous test 
		 set2=(TestSketchSetting) set.clone();
			set2.setSwarmSettings(agents,4);

//			//swarm cettings c1, c2, w, vmax
			//set2.swarmSettings(1.6,1.8,2,10);
	 
		set2.setTestName(test+"swarm4");
		/// now add it to list 
		testList.add(set2);    // add to list 
	//---------------------------------------------------------------------
		
		// get same as previous test 
		 set2=(TestSketchSetting) set.clone();
			set2.setSwarmSettings(agents,5);

//			//swarm cettings c1, c2, w, vmax
			//set2.swarmSettings(1.6,1.8,2,10);
	 
		set2.setTestName(test+"swarm5");
		/// now add it to list 
		testList.add(set2);    // add to list 
	//---------------------------------------------------------------------
		
		// get same as previous test 
		 set2=(TestSketchSetting) set.clone();
			set2.setSwarmSettings(agents,6);

//			//swarm cettings c1, c2, w, vmax
			//set2.swarmSettings(1.6,1.8,2,10);
	 
		set2.setTestName(test+"swarm6");
		/// now add it to list 
		testList.add(set2);    // add to list 
	
		// get same as previous test 
		 set2=(TestSketchSetting) set.clone();
			set2.setSwarmSettings(agents,5);

//			//swarm cettings c1, c2, w, vmax
			//set2.swarmSettings(1.6,1.8,2,10);
	 
		set2.setTestName(test+"swarm5");
		/// now add it to list 
		testList.add(set2);    // add to list 
		
		//---------------------------------------------------------------------
		
		// get same as previous test 
		 set2=(TestSketchSetting) set.clone();
			set2.setSwarmSettings(agents,10);

//			//swarm cettings c1, c2, w, vmax
			//set2.swarmSettings(1.6,1.8,2,10);
	 
		set2.setTestName(test+"swarm10");
		/// now add it to list 
		testList.add(set2);    // add to list 
		

		//---------------------------------------------------------------------
		
		// get same as previous test 
		 set2=(TestSketchSetting) set.clone();
			set2.setSwarmSettings(agents,20);

//			//swarm cettings c1, c2, w, vmax
			//set2.swarmSettings(1.6,1.8,2,10);
	 
		set2.setTestName(test+"swarm20");
		/// now add it to list 
		testList.add(set2);    // add to list 
		
		
	//---------------------------------------------------------------------
		
		// get same as previous test 
		 set2=(TestSketchSetting) set.clone();
			set2.setSwarmSettings(agents,40);

//			//swarm cettings c1, c2, w, vmax
			//set2.swarmSettings(1.6,1.8,2,10);
	 
		set2.setTestName(test+"swarm40");
		/// now add it to list 
		testList.add(set2);    // add to list 
	
	//---------------------------------------------------------------------
		
		// get same as previous test 
		 set2=(TestSketchSetting) set.clone();
			set2.setSwarmSettings(agents,50);

//			//swarm cettings c1, c2, w, vmax
			//set2.swarmSettings(1.6,1.8,2,10);
	 
		set2.setTestName(test+"swarm50");
		/// now add it to list 
		testList.add(set2);    // add to list 
		
//------------------------------------------------------------------------------------		
		// get same as previous test 
		 set2=(TestSketchSetting) set.clone();
			set2.setSwarmSettings(agents,80);

//			//swarm cettings c1, c2, w, vmax
			//set2.swarmSettings(1.6,1.8,2,10);
	 
		set2.setTestName(test+"swarm80");
		/// now add it to list 
		testList.add(set2);    // add to list 

		
		//---------------------------------------------------------------------
		//------------------------------------------------------------------------------------		
		// get same as previous test 
		 set2=(TestSketchSetting) set.clone();
			set2.setSwarmSettings(agents,100);

//			//swarm cettings c1, c2, w, vmax
			//set2.swarmSettings(1.6,1.8,2,10);
	 
		set2.setTestName(test+"swarm100");
		/// now add it to list 
		testList.add(set2);    // add to list 

		
		//---------------------------------------------------------------------
		//------------------------------------------------------------------------------------		
		// get same as previous test 
		 set2=(TestSketchSetting) set.clone();
			set2.setSwarmSettings(agents,130);

//			//swarm cettings c1, c2, w, vmax
			//set2.swarmSettings(1.6,1.8,2,10);
	 
		set2.setTestName(test+"swarm130");
		/// now add it to list 
		testList.add(set2);    // add to list 

		
		//---------------------------------------------------------------------
		//------------------------------------------------------------------------------------		
		// get same as previous test 
		 set2=(TestSketchSetting) set.clone();
			set2.setSwarmSettings(agents,160);

//			//swarm cettings c1, c2, w, vmax
			//set2.swarmSettings(1.6,1.8,2,10);
	 
		set2.setTestName(test+"swarm160");
		/// now add it to list 
		testList.add(set2);    // add to list 

		
		//---------------------------------------------------------------------
		//------------------------------------------------------------------------------------		
		// get same as previous test 
		 set2=(TestSketchSetting) set.clone();
			set2.setSwarmSettings(agents,180);

//			//swarm cettings c1, c2, w, vmax
			//set2.swarmSettings(1.6,1.8,2,10);
	 
		set2.setTestName(test+"swarm180");
		/// now add it to list 
		testList.add(set2);    // add to list 

		
		//---------------------------------------------------------------------
		//------------------------------------------------------------------------------------		
		// get same as previous test 
		 set2=(TestSketchSetting) set.clone();
			set2.setSwarmSettings(agents,200);

//			//swarm cettings c1, c2, w, vmax
			//set2.swarmSettings(1.6,1.8,2,10);
	 
		set2.setTestName(test+"swarm200");
		/// now add it to list 
		testList.add(set2);    // add to list 

		
		//---------------------------------------------------------------------
		//------------------------------------------------------------------------------------		
		// get same as previous test 
		 set2=(TestSketchSetting) set.clone();
			set2.setSwarmSettings(agents,220);

//			//swarm cettings c1, c2, w, vmax
			//set2.swarmSettings(1.6,1.8,2,10);
	 
		set2.setTestName(test+"swarm220");
		/// now add it to list 
		testList.add(set2);    // add to list 

		
		//---------------------------------------------------------------------
		//------------------------------------------------------------------------------------		
		// get same as previous test 
		 set2=(TestSketchSetting) set.clone();
			set2.setSwarmSettings(agents,250);

//			//swarm cettings c1, c2, w, vmax
			//set2.swarmSettings(1.6,1.8,2,10);
	 
		set2.setTestName(test+"swarm250");
		/// now add it to list 
		testList.add(set2);    // add to list 

		
		//---------------------------------------------------------------------
		//------------------------------------------------------------------------------------		
		// get same as previous test 
		 set2=(TestSketchSetting) set.clone();
			set2.setSwarmSettings(agents,270);

//			//swarm cettings c1, c2, w, vmax
			//set2.swarmSettings(1.6,1.8,2,10);
	 
		set2.setTestName(test+"swarm270");
		/// now add it to list 
		testList.add(set2);    // add to list 

		
		//---------------------------------------------------------------------		//------------------------------------------------------------------------------------		
		// get same as previous test 
		 set2=(TestSketchSetting) set.clone();
			set2.setSwarmSettings(agents,300);

//			//swarm cettings c1, c2, w, vmax
			//set2.swarmSettings(1.6,1.8,2,10);
	 
		set2.setTestName(test+"swarm300");
		/// now add it to list 
		testList.add(set2);    // add to list 

		
		//---------------------------------------------------------------------
		//---------------------------------------------------------------------		//------------------------------------------------------------------------------------		
		// get same as previous test 
		 set2=(TestSketchSetting) set.clone();
			set2.setSwarmSettings(agents,300);

//			//swarm cettings c1, c2, w, vmax
			//set2.swarmSettings(1.6,1.8,2,10);
	 
		set2.setTestName(test+"swarm350");
		/// now add it to list 
		testList.add(set2);    // add to list 

		
		//---------------------------------------------------------------------
		//------------------------------------------------------------------------------------		
		// get same as previous test 
		 set2=(TestSketchSetting) set.clone();
			set2.setSwarmSettings(agents,400);

//			//swarm cettings c1, c2, w, vmax
			//set2.swarmSettings(1.6,1.8,2,10);
	 
		set2.setTestName(test+"swarm400");
		/// now add it to list 
		testList.add(set2);    // add to list 

		
		//---------------------------------------------------------------------
		//---------------------------------------------------------------------		//------------------------------------------------------------------------------------		
		// get same as previous test 
		 set2=(TestSketchSetting) set.clone();
			set2.setSwarmSettings(agents,550);

//			//swarm cettings c1, c2, w, vmax
			//set2.swarmSettings(1.6,1.8,2,10);
	 
		set2.setTestName(test+"swarm550");
		/// now add it to list 
		testList.add(set2);    // add to list 

		
		//---------------------------------------------------------------------	
		//------------------------------------------------------------------------------------		
		// get same as previous test 
		 set2=(TestSketchSetting) set.clone();
			set2.setSwarmSettings(agents,800);

//			//swarm cettings c1, c2, w, vmax
			//set2.swarmSettings(1.6,1.8,2,10);
	 
		set2.setTestName(test+"swarm800");
		/// now add it to list 
		testList.add(set2);    // add to list 

		
		//---------------------------------------------------------------------
	
		
	//---------------------------------------------------------------------
		
		// get same as previous test 
		 set2=(TestSketchSetting) set.clone();
			set2.setSwarmSettings(agents,60);

//			//swarm cettings c1, c2, w, vmax
			//set2.swarmSettings(1.6,1.8,2,10);
	 
		set2.setTestName(test+"swarm60");
		/// now add it to list 
		testList.add(set2);    // add to list 
	//---------------------------------------------------------------------
		
		// get same as previous test 
		 set2=(TestSketchSetting) set.clone();
			set2.setSwarmSettings(agents,80);

//			//swarm cettings c1, c2, w, vmax
			//set2.swarmSettings(1.6,1.8,2,10);
	 
		set2.setTestName(test+"swarm80");
		/// now add it to list 
		testList.add(set2);    // add to list 

		
		//---------------------------------------------------------------------
		
		//---------------------------------------------------------------------
			
			// get same as previous test 
			 set2=(TestSketchSetting) set.clone();
				set2.setSwarmSettings(agents,100);

//				//swarm cettings c1, c2, w, vmax
				//set2.swarmSettings(1.6,1.8,2,10);
		 
			set2.setTestName(test+"swarm100");
			/// now add it to list 
			testList.add(set2);    // add to list 
			
	//---------------------------------------------------------------------
		
		// get same as previous test 
		 set2=(TestSketchSetting) set.clone();
			set2.setSwarmSettings(agents,120);

//			//swarm cettings c1, c2, w, vmax
			//set2.swarmSettings(1.6,1.8,2,10);
	 
		set2.setTestName(test+"swarm120");
		/// now add it to list 
		testList.add(set2);    // add to list 
		//-------------------------------------------------------
		
		//---------------------------------------------------------------------
			
			// get same as previous test 
			 set2=(TestSketchSetting) set.clone();
				set2.setSwarmSettings(agents,140);

//				//swarm cettings c1, c2, w, vmax
				//set2.swarmSettings(1.6,1.8,2,10);
		 
			set2.setTestName(test+"swarm140");
			/// now add it to list 
			testList.add(set2);    // add to list 
			
		//	//---------------------------------------------------------------------
		
		// get same as previous test 
		 set2=(TestSketchSetting) set.clone();
			set2.setSwarmSettings(agents,160);

//			//swarm cettings c1, c2, w, vmax
			//set2.swarmSettings(1.6,1.8,2,10);
	 
		set2.setTestName(test+"swarm160");
		/// now add it to list 
		testList.add(set2);    // add to list 
		
		
	//---------------------------------------------------------------------
		
//		// get same as previous test 
//		 set2=(TestSketchSetting) set.clone();
//			set2.setSwarmSettings(agents,180);
//
////			//swarm cettings c1, c2, w, vmax
//			//set2.swarmSettings(1.6,1.8,2,10);
//	 
//		set2.setTestName(test+"swarm180");
//		/// now add it to list 
//		testList.add(set2);    // add to list 
	//---------------------------------------------------------------------
		
		
//		
//		// get same as previous test 
//		 set2=(TestSketchSetting) set.clone();
//			//set2.setSwarmSettings(30,190);
//
////			//swarm cettings c1, c2, w, vmax
//			 set2.swarmSettings(1.1,1.1,1.3,6);
//	 
//		set2.setTestName(test+"swarm1.1,1.1,1.3,6");
//		/// now add it to list 
//		testList.add(set2);    // add to list 
//		//---------------------------------------------------------
//		// get same as previous test 
//		 set2=(TestSketchSetting) set.clone();
//			//set2.setSwarmSettings(30,190);
//
////			//swarm cettings c1, c2, w, vmax
//			 set2.swarmSettings(1.6,1.8,2,10);
//	 
//		set2.setTestName(test+"swarm1.6,1.8,2,10");
//		/// now add it to list 
//		testList.add(set2);    // add to list 
//		//-----------------------------------------------------------------
//		
//	//---------------------------------------------------------------------
//		
//		// get same as previous test 
//		 set2=(TestSketchSetting) set.clone();
//			//set2.setSwarmSettings(30,190);
//
////			//swarm cettings c1, c2, w, vmax
//			 set2.swarmSettings(1.7,1.9,2.2,10);
//	 
//		set2.setTestName(test+"swarm1.7,1.9,2.2,10");
//		/// now add it to list 
//		testList.add(set2);    // add to list 
//		//-----------------------------------------------------------------
//		
//		///------------------------------------------------------------------
//		
//		// get same as previous test 
//		 set2=(TestSketchSetting) set.clone();
//			//set2.setSwarmSettings(30,190);
//
////			//swarm cettings c1, c2, w, vmax
//			 set2.swarmSettings(2,2,1.5,6);
//	 
//		set2.setTestName(test+"swarm2,2,1.5,6");
//		/// now add it to list 
//		testList.add(set2);    // add to list 
//		//--------------------------------------------------------------------
//		// get same as previous test 
//		 set2=(TestSketchSetting) set.clone();
//			//set2.setSwarmSettings(30,190);
//
////			//swarm cettings c1, c2, w, vmax
//			 set2.swarmSettings(3,3,3,26);
//	 
//		set2.setTestName(test+"swarm3,3,3,26");
//		/// now add it to list 
//		testList.add(set2);    // add to list 
//
//		
//		
//		
////		//-----------------------------------------
////		// get same as previous test 
////		 set2=(TestSketchSetting) set.clone();
////			//set2.setSwarmSettings(30,190);
////
//////			//swarm cettings c1, c2, w, vmax
////			// set2.swarmSettings(1.1,1.1,1.3,6);
////		 //set2.SetErrorThresholds(FilesMaxNumber, FilesMaxNumber, FilesMaxNumber, averageRunNo, FilesMaxNumber, false)
////	 
////		set2.setTestName("Alg1 swarm 1.1,1.1,1.3,6");
////		/// now add it to list 
////		testList.add(set2);    // add to list 
	
		
		*/
	}


private void AlgSwarmTestVariations(ArrayList<TestSketchSetting>  testList,TestSketchSetting set, String test){
	
	
	TestSketchSetting set2;

//---------------------------------------------------------------------
	
	
	
	// get same as previous test 
	 set2=(TestSketchSetting) set.clone();
		//set2.setSwarmSettings(30,190);

//		//swarm cettings c1, c2, w, vmax
		 set2.swarmSettings(1.1,-1,-1,-1);
 
	set2.setTestName(test+"c1.1");
	/// now add it to list 
	testList.add(set2);    // add to list 
	//---------------------------------------------------------
	// get same as previous test 
	 set2=(TestSketchSetting) set.clone();
		//set2.setSwarmSettings(30,190);

//		//swarm cettings c1, c2, w, vmax
	 set2.swarmSettings(1.6,-1,-1,-1);
 
	set2.setTestName(test+"c1.6");
	/// now add it to list 
	testList.add(set2);    // add to list 
	//-----------------------------------------------------------------
	
//---------------------------------------------------------------------
	
	// get same as previous test 
	 set2=(TestSketchSetting) set.clone();
		//set2.setSwarmSettings(30,190);

//		//swarm cettings c1, c2, w, vmax
	 set2.swarmSettings(1.8,-1,-1,-1);
 
	set2.setTestName(test+"c1.8");
	/// now add it to list 
	testList.add(set2);    // add to list 
	//-----------------------------------------------------------------
//---------------------------------------------------------------------
	
	// get same as previous test 
	 set2=(TestSketchSetting) set.clone();
		//set2.setSwarmSettings(30,190);

//		//swarm cettings c1, c2, w, vmax
	 set2.swarmSettings(2.1,-1,-1,-1);
 
	set2.setTestName(test+"c1.8");
	/// now add it to list 
	testList.add(set2);    // add to list 
	//-----------------------------------------------------------------
//---------------------------------------------------------------------
	
	// get same as previous test 
	 set2=(TestSketchSetting) set.clone();
		//set2.setSwarmSettings(30,190);

//		//swarm cettings c1, c2, w, vmax
	 set2.swarmSettings(2.5,-1,-1,-1);
 
	set2.setTestName(test+"c2.5");
	/// now add it to list 
	testList.add(set2);    // add to list 
	//-----------------------------------------------------------------
//---------------------------------------------------------------------
	
	// get same as previous test 
	 set2=(TestSketchSetting) set.clone();
		//set2.setSwarmSettings(30,190);

//		//swarm cettings c1, c2, w, vmax
	 set2.swarmSettings(3,-1,-1,-1);
 
	set2.setTestName(test+"c3");
	/// now add it to list 
	testList.add(set2);    // add to list 
	//-----------------------------------------------------------------
	
	///------------------------------------------------------------------
//---------------------------------------------------------------------
	
	// get same as previous test 
	 set2=(TestSketchSetting) set.clone();
		//set2.setSwarmSettings(30,190);

//		//swarm cettings c1, c2, w, vmax
	 set2.swarmSettings(1.8,1.8,0.8,-1);
 
	set2.setTestName(test+"w0.8");
	/// now add it to list 
	testList.add(set2);    // add to list 
	//-----------------------------------------------------------------
	
	// get same as previous test 
	 set2=(TestSketchSetting) set.clone();
		//set2.setSwarmSettings(30,190);

//		//swarm cettings c1, c2, w, vmax
	 set2.swarmSettings(1.8,1.8,1.1,-1);
 
	set2.setTestName(test+"w1.1");
	/// now add it to list 
	testList.add(set2);    // add to list 
//-----------------------------------------------------------------
	
	// get same as previous test 
	 set2=(TestSketchSetting) set.clone();
		//set2.setSwarmSettings(30,190);

//		//swarm cettings c1, c2, w, vmax
	 set2.swarmSettings(1.8,1.8,1.5,-1);
 
	set2.setTestName(test+"w1.5");
	/// now add it to list 
	testList.add(set2);    // add to list 
	 
//-----------------------------------------------------------------
	
	// get same as previous test 
	 set2=(TestSketchSetting) set.clone();
		//set2.setSwarmSettings(30,190);

//		//swarm cettings c1, c2, w, vmax
	 set2.swarmSettings(1.8,1.8,2.0,-1);
 
	set2.setTestName(test+"w2.0");
	/// now add it to list 
	testList.add(set2);    // add to list 
//-----------------------------------------------------------------
	
	// get same as previous test 
	 set2=(TestSketchSetting) set.clone();
		//set2.setSwarmSettings(30,190);

//		//swarm cettings c1, c2, w, vmax
	 set2.swarmSettings(1.8,1.8,3.0,-1);
 
	set2.setTestName(test+"w3.0");
	/// now add it to list 
	testList.add(set2);    // add to list 
//-----------------------------------------------------------------
	
	// get same as previous test 
	 set2=(TestSketchSetting) set.clone();
		//set2.setSwarmSettings(30,190);

//		//swarm cettings c1, c2, w, vmax
	 set2.swarmSettings(1.8,1.8,4.0,-1);
 
	set2.setTestName(test+"w4.0");
	/// now add it to list 
	testList.add(set2);    // add to list 
//-----------------------------------------------------------------
	
	// get same as previous test 
	 set2=(TestSketchSetting) set.clone();
		//set2.setSwarmSettings(30,190);

//		//swarm cettings c1, c2, w, vmax
	 set2.swarmSettings(1.8,1.8,2,1);
 
	set2.setTestName(test+"Vmax 1");
	/// now add it to list 
	testList.add(set2);    // add to list 
//-----------------------------------------------------------------
	
	// get same as previous test 
	 set2=(TestSketchSetting) set.clone();
		//set2.setSwarmSettings(30,190);

//		//swarm cettings c1, c2, w, vmax
	 set2.swarmSettings(1.8,1.8,2,2);
 
	set2.setTestName(test+"Vmax 2");
	/// now add it to list 
	testList.add(set2);    // add to list 
	 
//-----------------------------------------------------------------
	
	// get same as previous test 
	 set2=(TestSketchSetting) set.clone();
		//set2.setSwarmSettings(30,190);

//		//swarm cettings c1, c2, w, vmax
	 set2.swarmSettings(1.8,1.8,2,4);
 
	set2.setTestName(test+"Vmax 4");
	/// now add it to list 
	testList.add(set2);    // add to list 
	 
//-----------------------------------------------------------------
	
	// get same as previous test 
	 set2=(TestSketchSetting) set.clone();
		//set2.setSwarmSettings(30,190);

//		//swarm cettings c1, c2, w, vmax
	 set2.swarmSettings(1.8,1.8,2,6);
 
	set2.setTestName(test+"Vmax 6");
	/// now add it to list 
	testList.add(set2);    // add to list 
	 
//-----------------------------------------------------------------
	
	// get same as previous test 
	 set2=(TestSketchSetting) set.clone();
		//set2.setSwarmSettings(30,190);

//		//swarm cettings c1, c2, w, vmax
	 set2.swarmSettings(1.8,1.8,2,8);
 
	set2.setTestName(test+"Vmax 8");
	/// now add it to list 
	testList.add(set2);    // add to list 
	 
//-----------------------------------------------------------------
	
	// get same as previous test 
	 set2=(TestSketchSetting) set.clone();
		//set2.setSwarmSettings(30,190);

//		//swarm cettings c1, c2, w, vmax
	 set2.swarmSettings(1.8,1.8,2,12);
 
	set2.setTestName(test+"Vmax 12");
	/// now add it to list 
	testList.add(set2);    // add to list 
	 
	//-----------------------------------------------------------------
		
		// get same as previous test 
		 set2=(TestSketchSetting) set.clone();
			//set2.setSwarmSettings(30,190);

//			//swarm cettings c1, c2, w, vmax
		 set2.swarmSettings(1.8,1.8,2,20);
	 
		set2.setTestName(test+"Vmax 20");
		/// now add it to list 
		testList.add(set2);    // add to list 
		 
	 
	 
	 
}

private void AlgSwarmTestVariationsAgents(ArrayList<TestSketchSetting>  testList,TestSketchSetting set, String test,int max){
	
	
	TestSketchSetting set2;
//---------------------------------------------------------------------
	
	// get same as previous test 
	 set2=(TestSketchSetting) set.clone();
		set2.setSwarmSettings(2,max);

//		//swarm cettings c1, c2, w, vmax
		//set2.swarmSettings(1.6,1.8,2,10);
 
	set2.setTestName(test+"A2");
	/// now add it to list 
	testList.add(set2);    // add to list 
	
	
//---------------------------------------------------------------------
	
	// get same as previous test 
	 set2=(TestSketchSetting) set.clone();
		set2.setSwarmSettings(5,max);

//		//swarm cettings c1, c2, w, vmax
		//set2.swarmSettings(1.6,1.8,2,10);
 
	set2.setTestName(test+"A5");
	/// now add it to list 
	testList.add(set2);    // add to list 
	
	//---------------------------------------------------------------------
	
	// get same as previous test 
	 set2=(TestSketchSetting) set.clone();
		set2.setSwarmSettings(10,max);

//		//swarm cettings c1, c2, w, vmax
		//set2.swarmSettings(1.6,1.8,2,10);
 
	set2.setTestName(test+"A10");
	/// now add it to list 
	testList.add(set2);    // add to list 
	

	//---------------------------------------------------------------------
	
	// get same as previous test 
	 set2=(TestSketchSetting) set.clone();
		set2.setSwarmSettings(15,max);

//		//swarm cettings c1, c2, w, vmax
		//set2.swarmSettings(1.6,1.8,2,10);
 
	set2.setTestName(test+"A15");
	/// now add it to list 
	testList.add(set2);    // add to list 
	
	
//---------------------------------------------------------------------
	
	// get same as previous test 
	 set2=(TestSketchSetting) set.clone();
		set2.setSwarmSettings(20,max);

//		//swarm cettings c1, c2, w, vmax
		//set2.swarmSettings(1.6,1.8,2,10);
 
	set2.setTestName(test+"A20");
	/// now add it to list 
	testList.add(set2);    // add to list 

//---------------------------------------------------------------------
	
	// get same as previous test 
	 set2=(TestSketchSetting) set.clone();
		set2.setSwarmSettings(25,max);

//		//swarm cettings c1, c2, w, vmax
		//set2.swarmSettings(1.6,1.8,2,10);
 
	set2.setTestName(test+"A25");
	/// now add it to list 
	testList.add(set2);    // add to list 
	
	
//---------------------------------------------------------------------
	
	// get same as previous test 
	 set2=(TestSketchSetting) set.clone();
		set2.setSwarmSettings(30,max);

//		//swarm cettings c1, c2, w, vmax
		//set2.swarmSettings(1.6,1.8,2,10);
 
	set2.setTestName(test+"A30");
	/// now add it to list 
	testList.add(set2);    // add to list 
//---------------------------------------------------------------------
	
	// get same as previous test 
	 set2=(TestSketchSetting) set.clone();
		set2.setSwarmSettings(35,max);

//		//swarm cettings c1, c2, w, vmax
		//set2.swarmSettings(1.6,1.8,2,10);
 
	set2.setTestName(test+"A35");
	/// now add it to list 
	testList.add(set2);    // add to list 
 
//---------------------------------------------------------------------
	
	// get same as previous test 
	 set2=(TestSketchSetting) set.clone();
		set2.setSwarmSettings(40,max);

//		//swarm cettings c1, c2, w, vmax
		//set2.swarmSettings(1.6,1.8,2,10);
 
	set2.setTestName(test+"A40");
	/// now add it to list 
	testList.add(set2);    // add to list 
//---------------------------------------------------------------------
	
	// get same as previous test 
	 set2=(TestSketchSetting) set.clone();
		set2.setSwarmSettings(50,max);

//		//swarm cettings c1, c2, w, vmax
		//set2.swarmSettings(1.6,1.8,2,10);
 
	set2.setTestName(test+"A50");
	/// now add it to list 
	testList.add(set2);    // add to list 
 
	
}

private void runFeaturesTest(String name){
	
	TestSketchSetting set;
	if (OperatonCode==1||OperatonCode==4||OperatonCode==-1||OperatonCode==7){
	set=AllAllFeatMyAlg();
	testList.add(set);
	FeatureTestVariations (testList, set,name);
	}
	
	
//	
	if (OperatonCode==2||OperatonCode==5||OperatonCode==-1||OperatonCode==7){
		set=this.OnlyF1Feat();
		testList.add(set);
		AlgorithmsTestVariations2(testList, set,name+"OF1_");
	}
}
private void runZernikeMomentTest(String name){
	TestSketchSetting set;
	set=AllAllFeaAlg2();
	testList.add(set);
	FeatureTestVariationsMoment(testList, set,name);
	
}


private void FeatureTestVariationsMoment(ArrayList<TestSketchSetting>  testList,TestSketchSetting set, String name){
	TestSketchSetting set2;
	//---------------------------------------------------------------------
	//---------------------------------------------------------------------
	
	// get same as previous test 
	 set2=(TestSketchSetting) set.clone();
	// change the main settings
	//set2.setAlgorithmRunnings(false, false, false, false, true, false);
	 set2.ClearFeat();
     set2.setFeatures(3);
	//set2.setSymbolFeaturesSettingsCOUNTCURVE(false);
	//change its name 
	set2.setTestName(name+"FS3_n2_");
	set2.setMomemtsOrder(2);
	/// now add it to list 
	testList.add(set2);    // add to list 
	//---------------------------------------------------------------------
	//---------------------------------------------------------------------
	
	// get same as previous test 
	 set2=(TestSketchSetting) set.clone();
	// change the main settings
	//set2.setAlgorithmRunnings(false, false, false, false, true, false);
	 set2.ClearFeat();
     set2.setFeatures(3);
	//set2.setSymbolFeaturesSettingsCOUNTCURVE(false);
	//change its name 
	set2.setTestName(name+"FS3_n3_");
	set2.setMomemtsOrder(3);
	/// now add it to list 
	testList.add(set2);    // add to list 
	//---------------------------------------------------------------------
	//---------------------------------------------------------------------
	
	// get same as previous test 
	 set2=(TestSketchSetting) set.clone();
	// change the main settings
	//set2.setAlgorithmRunnings(false, false, false, false, true, false);
	 set2.ClearFeat();
     set2.setFeatures(3);
	//set2.setSymbolFeaturesSettingsCOUNTCURVE(false);
	//change its name 
	set2.setTestName(name+"FS3_n4_");
	set2.setMomemtsOrder(4);
	/// now add it to list 
	testList.add(set2);    // add to list 
	//---------------------------------------------------------------------
	//---------------------------------------------------------------------
	
	// get same as previous test 
	 set2=(TestSketchSetting) set.clone();
	// change the main settings
	//set2.setAlgorithmRunnings(false, false, false, false, true, false);
	 set2.ClearFeat();
     set2.setFeatures(3);
	//set2.setSymbolFeaturesSettingsCOUNTCURVE(false);
	//change its name 
	set2.setTestName(name+"FS3_n5_");
	set2.setMomemtsOrder(5);
	/// now add it to list 
	testList.add(set2);    // add to list 
	//---------------------------------------------------------------------
	//---------------------------------------------------------------------
	
	// get same as previous test 
	 set2=(TestSketchSetting) set.clone();
	// change the main settings
	//set2.setAlgorithmRunnings(false, false, false, false, true, false);
	 set2.ClearFeat();
     set2.setFeatures(3);
	//set2.setSymbolFeaturesSettingsCOUNTCURVE(false);
	//change its name 
	set2.setTestName(name+"FS3_n8_");
	set2.setMomemtsOrder(8);
	/// now add it to list 
	testList.add(set2);    // add to list 
	//---------------------------------------------------------------------
	//---------------------------------------------------------------------
	
	// get same as previous test 
	 set2=(TestSketchSetting) set.clone();
	// change the main settings
	//set2.setAlgorithmRunnings(false, false, false, false, true, false);
	 set2.ClearFeat();
     set2.setFeatures(3);
	//set2.setSymbolFeaturesSettingsCOUNTCURVE(false);
	//change its name 
	set2.setTestName(name+"FS3_n10_");
	set2.setMomemtsOrder(10);
	/// now add it to list 
	testList.add(set2);    // add to list 
	//---------------------------------------------------------------------
	// get same as previous test 
	 set2=(TestSketchSetting) set.clone();
	// change the main settings
	//set2.setAlgorithmRunnings(false, false, false, false, true, false);
	 set2.ClearFeat();
    set2.setFeatures(3);
	//set2.setSymbolFeaturesSettingsCOUNTCURVE(false);
	//change its name 
	set2.setTestName(name+"FS3_n11_");
	set2.setMomemtsOrder(11);
	/// now add it to list 
	testList.add(set2);    // add to list 
	//---------------------------------------------------------------------
	//---------------------------------------------------------------------
	
	// get same as previous test 
	 set2=(TestSketchSetting) set.clone();
	// change the main settings
	//set2.setAlgorithmRunnings(false, false, false, false, true, false);
	 set2.ClearFeat();
     set2.setFeatures(3);
	//set2.setSymbolFeaturesSettingsCOUNTCURVE(false);
	//change its name 
	set2.setTestName(name+"FS3_n12_");
	set2.setMomemtsOrder(12);
	/// now add it to list 
	testList.add(set2);    // add to list 
	//---------------------------------------------------------------------
	//---------------------------------------------------------------------
	
	// get same as previous test 
	 set2=(TestSketchSetting) set.clone();
	// change the main settings
	//set2.setAlgorithmRunnings(false, false, false, false, true, false);
	 set2.ClearFeat();
     set2.setFeatures(3);
	//set2.setSymbolFeaturesSettingsCOUNTCURVE(false);
	//change its name 
	set2.setTestName(name+"FS3_n13_");
	set2.setMomemtsOrder(13);
	/// now add it to list 
	testList.add(set2);    // add to list 
	//---------------------------------------------------------------------
	//---------------------------------------------------------------------
	
	// get same as previous test 
	 set2=(TestSketchSetting) set.clone();
	// change the main settings
	//set2.setAlgorithmRunnings(false, false, false, false, true, false);
	 set2.ClearFeat();
     set2.setFeatures(3);
	//set2.setSymbolFeaturesSettingsCOUNTCURVE(false);
	//change its name 
	set2.setTestName(name+"FS3_n14_");
	set2.setMomemtsOrder(14);
	/// now add it to list 
	testList.add(set2);    // add to list
	
	// get same as previous test 
	 set2=(TestSketchSetting) set.clone();
	// change the main settings
	//set2.setAlgorithmRunnings(false, false, false, false, true, false);
	 set2.ClearFeat();
     set2.setFeatures(3);
	//set2.setSymbolFeaturesSettingsCOUNTCURVE(false);
	//change its name 
	set2.setTestName(name+"FS3_n15_");
	set2.setMomemtsOrder(15);
	/// now add it to list 
	testList.add(set2);    // add to list
//---------------------------------------------------------------------
//---------------------------------------------------------------------
	
	// get same as previous test 
	 set2=(TestSketchSetting) set.clone();
	// change the main settings
	//set2.setAlgorithmRunnings(false, false, false, false, true, false);
	 set2.ClearFeat();
     set2.setFeatures(3);
	//set2.setSymbolFeaturesSettingsCOUNTCURVE(false);
	//change its name 
	set2.setTestName(name+"FS3_n16_");
	set2.setMomemtsOrder(16);
	/// now add it to list 
	testList.add(set2);    // add to list
//---------------------------------------------------------------------
	// get same as previous test 
	 set2=(TestSketchSetting) set.clone();
	// change the main settings
	//set2.setAlgorithmRunnings(false, false, false, false, true, false);
	 set2.ClearFeat();
    set2.setFeatures(3);
	//set2.setSymbolFeaturesSettingsCOUNTCURVE(false);
	//change its name 
	set2.setTestName(name+"FS3_n17_");
	set2.setMomemtsOrder(17);
	/// now add it to list 
	testList.add(set2);    // add to list
//---------------------------------------------------------------------
	
	// get same as previous test 
	 set2=(TestSketchSetting) set.clone();
	// change the main settings
	//set2.setAlgorithmRunnings(false, false, false, false, true, false);
	 set2.ClearFeat();
     set2.setFeatures(3);
	//set2.setSymbolFeaturesSettingsCOUNTCURVE(false);
	//change its name 
	set2.setTestName(name+"FS3_n18_");
	set2.setMomemtsOrder(18);
	/// now add it to list 
	testList.add(set2);    // add to list
//---------------------------------------------------------------------

	// get same as previous test 
	 set2=(TestSketchSetting) set.clone();
	// change the main settings
	//set2.setAlgorithmRunnings(false, false, false, false, true, false);
	 set2.ClearFeat();
    set2.setFeatures(3);
	//set2.setSymbolFeaturesSettingsCOUNTCURVE(false);
	//change its name 
	set2.setTestName(name+"FS3_n20_");
	set2.setMomemtsOrder(20);
	/// now add it to list 
	testList.add(set2);    // add to list
//---------------------------------------------------------------------
	// get same as previous test 
	 set2=(TestSketchSetting) set.clone();
	// change the main settings
	//set2.setAlgorithmRunnings(false, false, false, false, true, false);
	 set2.ClearFeat();
     set2.setFeatures(3);
	//set2.setSymbolFeaturesSettingsCOUNTCURVE(false);
	//change its name 
	set2.setTestName(name+"FS3_n22_");
	set2.setMomemtsOrder(22);
	/// now add it to list 
	testList.add(set2);    // add to list
	///-------------------------------------------------------------------
	// get same as previous test 
	 set2=(TestSketchSetting) set.clone();
	// change the main settings
	//set2.setAlgorithmRunnings(false, false, false, false, true, false);
	 set2.ClearFeat();
   set2.setFeatures(3);
	//set2.setSymbolFeaturesSettingsCOUNTCURVE(false);
	//change its name 
	set2.setTestName(name+"FS3_n28_");
	set2.setMomemtsOrder(28);
	/// now add it to list 
	testList.add(set2);    // add to list
//---------------------------------------------------------------------
}

private void runAlgorithmsTest(String name){
	
	//set=this.Alg1SettingGeneralSVM();
	//set= swarmTesting();
	TestSketchSetting set=this.AllAlgAllFeat();
	testList.add(set);
	AlgorithmsTestVariations2(testList, set,name);
	
}
private void runSwarmVerticesTest(){
	TestSketchSetting Alg1set = swarmTestingAlg1();
	//testList.add(Alg1set);/// th c variation 
	//AlgSwarmTestVariations(testList, Alg1set,"_Alg1_");
	
	TestSketchSetting Alg2set = swarmTestingAlg2();
	//testList.add(Alg2set);
	//AlgSwarmTestVariations(testList, Alg2set,"_Alg2_");

	
//	AlgSwarmTestVariationsAgents(testList, Alg1set,"Alg_1_m60_",60);
//	
//	AlgSwarmTestVariationsAgents(testList, Alg1set,"Alg_1_m80_",80);
//	
//	
//	
//	AlgSwarmTestVariationsAgents(testList, Alg2set,"Alg_2_m60_",60);
//	
//	AlgSwarmTestVariationsAgents(testList, Alg2set,"Alg_2_m80_",80);
	
	
	AlgSwarmTestVariationsMax(testList, Alg1set,"Alg_1_a3_",3);
	AlgSwarmTestVariationsMax(testList, Alg1set,"Alg_1_a3_",7);
	AlgSwarmTestVariationsMax(testList, Alg1set,"Alg_1_a10_",10);
	AlgSwarmTestVariationsMax(testList, Alg1set,"Alg_1_a15_",15);
	AlgSwarmTestVariationsMax(testList, Alg1set,"Alg_1_a20_",20);
	AlgSwarmTestVariationsMax(testList, Alg1set,"Alg_1_a25_",25);
	
	
/*	AlgSwarmTestVariationsMax(testList, Alg1set,"Alg_1_a3_",3);
AlgSwarmTestVariationsMax(testList, Alg1set,"Alg_1_a5_",5);
	//AlgSwarmTestVariationsMax(testList, Alg1set,"Alg_1_a7_",7);
	AlgSwarmTestVariationsMax(testList, Alg1set,"Alg_1_a10_",10);
	//AlgSwarmTestVariationsMax(testList, Alg1set,"Alg_1_a13_",13);
	AlgSwarmTestVariationsMax(testList, Alg1set,"Alg_1_a15_",15);
	AlgSwarmTestVariationsMax(testList, Alg1set,"Alg_1_a20_",20);
	AlgSwarmTestVariationsMax(testList, Alg1set,"Alg_1_a25_",25);
	*/
	/*
	
	AlgSwarmTestVariationsMax(testList, Alg2set,"Alg_2_a2_",2);
	AlgSwarmTestVariationsMax(testList, Alg2set,"Alg_2_a3_",3);
	AlgSwarmTestVariationsMax(testList, Alg2set,"Alg_2_a5_",5);
	AlgSwarmTestVariationsMax(testList, Alg2set,"Alg_2_a7_",7);
	AlgSwarmTestVariationsMax(testList, Alg2set,"Alg_2_a10_",10);
	//AlgSwarmTestVariationsMax(testList, Alg2set,"Alg_2_a13_",13);
	AlgSwarmTestVariationsMax(testList, Alg2set,"Alg_2_a15_",15);
	//AlgSwarmTestVariationsMax(testList, Alg2set,"Alg_2_a20_",20);
	AlgSwarmTestVariationsMax(testList, Alg2set,"Alg_2_a25_",25);
*/
}
private void intializeTestSymbolsCount() {
    System.out.println("inside the symbols count test ... "+" (" + this.getClass().getSimpleName()
			+ "    " + (new Throwable()).getStackTrace()[0].getLineNumber()
			+ "  )  ");
	//testList=new ArrayList<TestSketchSetting>();
	TestSketchSetting set;
	doStateOnAllDataBase("StateonAllDB_.txt");
	int increment=3;
	if (OperatonCode==6)
	 increment=1;
	else 
		increment=2;
	

	 // get percentage of max number of files... ..
	 TotalFileMaxNumber=getDataBaseMaxCount();
	
	 
	 

	for(int j=3;j<=TotalFileMaxNumber;j+=increment){
		if (j>TotalFileMaxNumber)
			j=TotalFileMaxNumber;
		
		//System.out.println( " number of j ="+j+" repeat i "+i);
		 CurrentFilesMaxNumber=j;
		 
			for(int i=0;i<averageRunNo;i++){
		// System.out.println(" CurrentFilesMaxNumber "+ CurrentFilesMaxNumber);
	 GenerateRandomTestSet();
	 
	// this set 
		
		for (int k = 0; k < TrainFilenames.size(); k++) {
			logger.error(" Training for this set = " + TrainFilenames.get(k));
		}
		for (int k = 0; k < TestFilenames.size(); k++) {
			logger.error(" Test for this set = " + TestFilenames.get(k));
		}
		doStateOnTrainAndTest("StateOFfiles"+j+"db"+DataBase+".txt");
	//default settings
	//set=GetDefaultSettings();
	//testList.add(set);
		
		//System.out.println("TF      "+TrainFilenames.size());

//	 GeneralTest(testList, set);
	 
	// set= GetDefaultSettings_Symbol();
	// testList.add(set);
	 
	// GeneralTest(testList, set);
   if(OperatonCode==6){
	  // logger.error("setting information for the TF running using onl algs1 and algs2");
	   RunDifferentSampleCountTest("TF"+TrainFilenames.size()+"TS"+TestFilenames.size()+"db"+DataBase);
   }
    
	 if (OperatonCode==3||OperatonCode==7)
	runAlgorithmsTest("TF"+TrainFilenames.size()+"TS"+TestFilenames.size()+"db"+DataBase);
 if(OperatonCode==4||OperatonCode==5||OperatonCode==7) 
	 {
		 runFeaturesTest("TF"+TrainFilenames.size()+"TS"+TestFilenames.size()+"db"+DataBase);
	 }	 


	}//for loop of tests 


	/*	
testList.add(set);
		
GeneralTest(testList, set);
		 
		 
	set=this.GetDefaultSettings3();
			
	testList.add(set);
			
	 GeneralTest(testList, set);
			 
	set=this.GetDefaultSettings4();
				
	testList.add(set);
				
		 GeneralTest(testList, set);
		
		 
	 */
	 
	//	 GeneralTest1(testList, set);
	}
	System.out.println("Now starting the testings  "+testList.size()+" (" + this.getClass().getSimpleName()
			+ "    " + (new Throwable()).getStackTrace()[0].getLineNumber()
			+ "  )  ");
	
	if (logger.isInfoEnabled()){
		
		logger.info("####################################Testing########################################################");
		logger.info("#####################            number of test is = "+testList.size()+"              #############");
		
	}
	else {
		logger.error("##############################################################################################");
		logger.error("#####################            number of test is = "+testList.size()+"              #############");
	}
	
}


private void RunDifferentSampleCountTest(String name) {
	// TODO Auto-generated method stub
	//set= swarmTesting();
	TestSketchSetting set=this.AlgS2System(name);
	testList.add(set);
	set=this.AlgS1System(name);
	testList.add(set);
	
	//AlgorithmsTestVariations2(testList, set,name);
}
private TestSketchSetting AlgS2System(String name) {
	 TestSketchSetting set;
		set=new TestSketchSetting(); // initalize a new set.
		set.GetStoredSetting();
		set.setRecognizierType( TestSketchSetting.SVM_RECOGNIZER);
		// all, no swarm,algorithms ( 1 2 3 ), circle.  
		set.setAlgorithmRunnings(false, false,false, true,false, true);
		//(150 iteration and 15 agent) with 3 training files and 2 test file
		set.setTestName("AlgS2_"+name);
		// all, no swarm,algorithms ( 1 2 3 ), circle.  
		//set.setAlgorithmRunnings(false, false,false, false, true, false);
		set.setDataSetType(set.DATA_SET_TYPE_XML);
		
		set.setMaxCatSize(-1);  // all data 
		
		for (int i = 0; i < TrainFilenames.size(); i++) {
			set.addDataSetFileName(TrainFilenames.get(i));
		}
		
		set.setSwarmSettings(15,80);
		set.swarmSettings(2.5,2.5,1.5,4);
//		// resample  minn segment (for segmentations) and Max iterpolation length then min stroke pixel 
//		set.setStrokeLengthSettings(false, -1,-1, -1);
//		set.setSettingsForPolygon(polygonSolution.POLYGON_ADJUST_NONE);
//		// bezire error, curve test, hybired tol( no use), solution error, solution intial tol, second pass
//		set.SetErrorThresholds( -1.0,-1.0,-1,-1,-1,false);
//		//swarm cettings c1, c2, w, vmax
		
//		// now for features. 
//		// those are the defaults 
//	//  primitive,count, count relations, centroid, convex hul, raions, area, logstatics, density, rubine, zenerik
//			//true,false,true,false,false,false,false,false,true,false,true 
			set.SymbolFeaturesSettings(true,true,true,true,true,true,true,true,true,true,true );
			set.setSymbolFeaturesSettingsCOUNTCURVE(true);
		set.setOSLinux(false);
		for (int i = 0; i < TestFilenames.size(); i++) {
			set.addDTestSetFileName(TestFilenames.get(i));
		}
		set.setTestSize(TestFilenames.size());

     //set.ResetAllSettings();
	    return set;
}
private TestSketchSetting AlgS1System(String name) {
	 TestSketchSetting set;
		set=new TestSketchSetting(); // initalize a new set.
		set.GetStoredSetting();
		set.setRecognizierType( TestSketchSetting.SVM_RECOGNIZER);
		// all, no swarm,algorithms ( 1 2 3 ), circle.  
		set.setAlgorithmRunnings(false,false, true, false,false, true);
		//(150 iteration and 15 agent) with 3 training files and 2 test file
		set.setTestName("AlgS1_"+name);
		// all, no swarm,algorithms ( 1 2 3 ), circle.  
		//set.setAlgorithmRunnings(false, false,false, false, true, false);
		set.setDataSetType(set.DATA_SET_TYPE_XML);
		
		set.setMaxCatSize(-1);  // all data 
		
		for (int i = 0; i < TrainFilenames.size(); i++) {
			set.addDataSetFileName(TrainFilenames.get(i));
		}
		
		set.setSwarmSettings(15,80);
		set.swarmSettings(2.5,2.5,1.5,4);
//		// resample  minn segment (for segmentations) and Max iterpolation length then min stroke pixel 
//		set.setStrokeLengthSettings(false, -1,-1, -1);
//		set.setSettingsForPolygon(polygonSolution.POLYGON_ADJUST_NONE);
//		// bezire error, curve test, hybired tol( no use), solution error, solution intial tol, second pass
//		set.SetErrorThresholds( -1.0,-1.0,-1,-1,-1,false);
//		//swarm cettings c1, c2, w, vmax
		
//		// now for features. 
//		// those are the defaults 
//	//  primitive,count, count relations, centroid, convex hul, raions, area, logstatics, density, rubine, zenerik
//			//true,false,true,false,false,false,false,false,true,false,true 
			set.SymbolFeaturesSettings(true,true,true,true,true,true,true,true,true,true,true );
			set.setSymbolFeaturesSettingsCOUNTCURVE(true);
		set.setOSLinux(false);
		for (int i = 0; i < TestFilenames.size(); i++) {
			set.addDTestSetFileName(TestFilenames.get(i));
		}
		set.setTestSize(TestFilenames.size());

    //set.ResetAllSettings();
	    return set;
}
private void intializeTestVertices() {
	TestSketchSetting set;
	 doStateOnAllDataBase("StateonAllDB"+"DB"+DataBase+".txt");
	for(int j=0;j<averageRunNo;j++){
	 
		 GenerateSameFileTestSet();
	 
	 
	// this set 
		
		for (int i = 0; i < TrainFilenames.size(); i++) {
			logger.error(" Training for this set = " + TrainFilenames.get(i));
		}
			runSwarmVerticesTest();

	}//for loop of tests 


	/*	
testList.add(set);
		
GeneralTest(testList, set);
		 
		 
	set=this.GetDefaultSettings3();
			
	testList.add(set);
			
	 GeneralTest(testList, set);
			 
	set=this.GetDefaultSettings4();
				
	testList.add(set);
				
		 GeneralTest(testList, set);
		
		 
	 */
	 
	//	 GeneralTest1(testList, set);
	
	System.out.println("Now starting the testings  "+testList.size()+" (" + this.getClass().getSimpleName()
			+ "    " + (new Throwable()).getStackTrace()[0].getLineNumber()
			+ "  )  ");
	
	if (logger.isInfoEnabled()){
		
		logger.info("####################################Testing########################################################");
		logger.info("#####################            number of test is = "+testList.size()+"              #############");
		
	}
	else {
		logger.error("##############################################################################################");
		logger.error("#####################            number of test is = "+testList.size()+"              #############");
	}
	
	
}
private void intializeTrainAll(){
	// init set
	//testList=new ArrayList<TestSketchSetting>();
	TestSketchSetting set;
	 doStateOnAllDataBase("StateonAllDB"+"DB"+DataBase+".txt");
	//for(int j=0;j<averageRunNo;j++){
//	if (testVertices)
//	{
//		 GenerateSameFileTestSet();
//	}
//	else {
	 GenerateAllTestSet();
	//}
	// this set 
		
		for (int i = 0; i < TrainFilenames.size(); i++) {
			logger.error(" Training for this set = " + TrainFilenames.get(i));
		}
		for (int i = 0; i < TestFilenames.size(); i++) {
			logger.error(" Test for this set = " + TestFilenames.get(i));
		}
		doStateOnTrainAndTest("StateOFRunNo"+"Op"+OperatonCode+"DB"+DataBase+".txt");
	//default settings
	//set=GetDefaultSettings();
	//testList.add(set);

//	 GeneralTest(testList, set);
	 
	// set= GetDefaultSettings_Symbol();
	// testList.add(set);
	 
	// GeneralTest(testList, set);

 
	
//	if (OperatonCode==11||OperatonCode==12){
		runAlgorithmsTest("Op"+OperatonCode+"db"+DataBase);
	//	}
//		if (OperatonCode==2||OperatonCode==1||OperatonCode==-1){
//		runFeaturesTest("op"+OperatonCode+"db"+DataBase);
//		}
//		if (OperatonCode==-2){
//			
//		runZernikeMomentTest( "op"+OperatonCode+"db"+DataBase);
//			
//		}
 



	//}//for loop of tests 
 
 
	/*	
testList.add(set);
		
 GeneralTest(testList, set);
		 
		 
	set=this.GetDefaultSettings3();
			
	testList.add(set);
			
	 GeneralTest(testList, set);
			 
	set=this.GetDefaultSettings4();
				
	testList.add(set);
				
		 GeneralTest(testList, set);
		
		 
	 */
	 
	//	 GeneralTest1(testList, set);
	
	System.out.println("Now starting the testings  "+testList.size()+" (" + this.getClass().getSimpleName()
			+ "    " + (new Throwable()).getStackTrace()[0].getLineNumber()
			+ "  )  ");
	
	if (logger.isInfoEnabled()){
		
		logger.info("####################################Testing########################################################");
		logger.info("#####################            number of test is = "+testList.size()+"              #############");
		
	}
	else {
		logger.error("##############################################################################################");
		logger.error("#####################            number of test is = "+testList.size()+"              #############");
	}
	
	
	
}
private void intializeTest() {
	// init set
	//testList=new ArrayList<TestSketchSetting>();
	TestSketchSetting set;
	 doStateOnAllDataBase("StateonAllDB"+"DB"+DataBase+".txt");
	for(int j=0;j<averageRunNo;j++){
	if (testVertices)
	{
		 GenerateSameFileTestSet();
	}
	else {
	 GenerateRandomTestSet();
	}
	// this set 
		
		for (int i = 0; i < TrainFilenames.size(); i++) {
			logger.error(" Training for this set = " + TrainFilenames.get(i));
		}
		for (int i = 0; i < TestFilenames.size(); i++) {
			logger.error(" Test for this set = " + TestFilenames.get(i));
		}
		doStateOnTrainAndTest("StateOFRunNo"+j+"Op"+OperatonCode+"DB"+DataBase+".txt");
	//default settings
	//set=GetDefaultSettings();
	//testList.add(set);

//	 GeneralTest(testList, set);
	 
	// set= GetDefaultSettings_Symbol();
	// testList.add(set);
	 
	// GeneralTest(testList, set);

if (testVertices){

			///runSwarmVerticesTest();
	}
else {
	
	if (OperatonCode==0||OperatonCode==-1){
		runAlgorithmsTest("op"+OperatonCode+"db"+DataBase);
		}
		if (OperatonCode==2||OperatonCode==1||OperatonCode==-1){
		runFeaturesTest("op"+OperatonCode+"db"+DataBase);
		}
		if (OperatonCode==-2){
			
			runZernikeMomentTest( "op"+OperatonCode+"db"+DataBase);
			
		}
	}



	}//for loop of tests 
 
 
	/*	
testList.add(set);
		
 GeneralTest(testList, set);
		 
		 
	set=this.GetDefaultSettings3();
			
	testList.add(set);
			
	 GeneralTest(testList, set);
			 
	set=this.GetDefaultSettings4();
				
	testList.add(set);
				
		 GeneralTest(testList, set);
		
		 
	 */
	 
	//	 GeneralTest1(testList, set);
	
	System.out.println("Now starting the testings  "+testList.size()+" (" + this.getClass().getSimpleName()
			+ "    " + (new Throwable()).getStackTrace()[0].getLineNumber()
			+ "  )  ");
	
	if (logger.isInfoEnabled()){
		
		logger.info("####################################Testing########################################################");
		logger.info("#####################            number of test is = "+testList.size()+"              #############");
		
	}
	else {
		logger.error("##############################################################################################");
		logger.error("#####################            number of test is = "+testList.size()+"              #############");
	}
	
}

 public void InitErrorCorrectTest(){
	 
	// init set
	//	testList=new ArrayList<TestSketchSetting>();
		TestSketchSetting set;
		StoreOnlyFinalSheet=false;
		 doStateOnAllDataBase("StateonAllDB"+"DB"+DataBase+".txt");
		for(int j=0;j<averageRunNo;j++){
		if (testVertices)
		{
			 GenerateSameFileTestSet();
		}
		else {
		 GenerateRandomTestSet();
		}
		// this set 
			
			for (int i = 0; i < TrainFilenames.size(); i++) {
				logger.error(" Training for this set = " + TrainFilenames.get(i));
			}
			for (int i = 0; i < TestFilenames.size(); i++) {
				logger.error(" Test for this set = " + TestFilenames.get(i));
			}
			doStateOnTrainAndTest("StateOFRunNo"+j+"Op"+OperatonCode+"DB"+DataBase+".txt");
	 

	 
			RunSingleTest("Op"+OperatonCode+"DB"+DataBase+"ET");


		}//for loop of tests 
	 
	 
	
		
		System.out.println("Now starting the testings  "+testList.size()+" (" + this.getClass().getSimpleName()
				+ "    " + (new Throwable()).getStackTrace()[0].getLineNumber()
				+ "  )  ");
		
		if (logger.isInfoEnabled()){
			
			logger.info("####################################Testing########################################################");
			logger.info("#####################            number of test is = "+testList.size()+"              #############");
			
		}
		else {
			logger.error("##############################################################################################");
			logger.error("#####################            number of test is = "+testList.size()+"              #############");
		}
		
	 
	 
 }
 
 
 
 private void  RunStoreAllData(){
	 
	// init set
		//testList=new ArrayList<TestSketchSetting>();
	
		 doStateOnAllDataBase("StateonAllDB"+"DB"+DataBase+".txt");
	//	for(int j=0;j<averageRunNo;j++){

		 GenerateRandomAllTestSet();
		
		// this set 
			
			for (int i = 0; i < TrainFilenames.size(); i++) {
				logger.error(" Training for this set = " + TrainFilenames.get(i));
			}
	
			doStateOnTrainAndTest("StateOF"+"Op"+OperatonCode+"DB"+DataBase+".txt");
		//default settings
		//set=GetDefaultSettings();
		//testList.add(set);

//		 GeneralTest(testList, set);
		 
		// set= GetDefaultSettings_Symbol();
		// testList.add(set);
		 
		// GeneralTest(testList, set);


			TestSketchSetting set=this.AllAlgAllFeat();
			set.setTestName( "SySDb"+DataBase);
			testList.add(set);
			
	
		//	runAlgorithmsTest("op"+OperatonCode+"db"+DataBase);
		




		//}//for loop of tests 
	 
	 
		/*	
	testList.add(set);
			
	 GeneralTest(testList, set);
			 
			 
		set=this.GetDefaultSettings3();
				
		testList.add(set);
				
		 GeneralTest(testList, set);
				 
		set=this.GetDefaultSettings4();
					
		testList.add(set);
					
			 GeneralTest(testList, set);
			
			 
		 */
		 
		//	 GeneralTest1(testList, set);
		
		System.out.println("Now starting the testings  "+testList.size()+" (" + this.getClass().getSimpleName()
				+ "    " + (new Throwable()).getStackTrace()[0].getLineNumber()
				+ "  )  ");
		
		if (logger.isInfoEnabled()){
			
			logger.info("####################################Testing########################################################");
			logger.info("#####################            number of test is = "+testList.size()+"              #############");
			
		}
		else {
			logger.error("##############################################################################################");
			logger.error("#####################            number of test is = "+testList.size()+"              #############");
		}
		
	 
 }
private void RunSingleTest(String name) {
	// TODO Auto-generated method stub
	TestSketchSetting set=this.AlgS2System(name);
	testList.add(set);
	//set=this.AlgS1System(name);
	//testList.add(set);
	
}
}
