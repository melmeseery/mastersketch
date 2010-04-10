package settings;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import test.TestVertices;
import test.TrainTest;

public class PatchTestSetFromFiles  
	 implements Serializable ,Runnable{
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
			HashMap<String, Integer> catCounts;
			private boolean UseLeaveOneOut=false;
			private boolean UseSeventyPercent=true; 
			private boolean  UseFifthOne=false;
			Random  r1Random=new Random(600);
			
			
			
			public  PatchTestSetFromFiles () {
	            logger.setLevel(Level.ALL);
			   t=new TrainTest();
			   v=new TestVertices();
			   
			   readFromFile("settings.txt");
			 
			   
			   TotalFileMaxNumber=getDataBaseMaxCount();
		}
		public void readFromFile(String filename){
			
			  DataBase=  DATA_MINE_DIGITAL_R;
			   OperatonCode=3;
			   averageRunNo =1;
			   CurrentFilesMaxNumber=getDataBaseMaxCount();
			
			try{
			File afile = new File(filename);
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
		
		public static void main(String[] args) {
			if (logger.isDebugEnabled()) {
				//  logger.debug("main(String[]) - start"); //$NON-NLS-1$
				
				
			}
//			File temp=new File("");
//			System.out.println(temp.getAbsolutePath());

			  
			
			
			org.apache.log4j.PropertyConfigurator.configure("log4j.properties");
			
			 PatchTestSetFromFiles  temp=new  PatchTestSetFromFiles ();
			
			temp.run();
		 
		 
			//------------------------------------------to run a patch test train 
			
		

		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			
		}

}
