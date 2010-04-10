/**
 * 
 */
package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.log4j.Logger;

import settings.PatchTestSet;

/**
 * @author Maha
 *
 */
public class RunCommands implements Runnable {

	private static transient final Logger logger = Logger.getLogger(RunCommands.class);
	/**
	 * 
	 */
	ArrayList<TaskSet> tasks=new 	ArrayList<TaskSet> ();
	public RunCommands() {
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		org.apache.log4j.PropertyConfigurator.configure("log4j.properties");
		 RunCommands runApp = new RunCommands();
		Thread th = new Thread(runApp);
		th.run();

	}

	public void run() {
	
		RunCommnadsFromDisk();
	}

	private void RunCommnadsFromDisk() {
		 
		
 
  	  
  	  ReadCommands();
  	  for (int i = 0; i < tasks.size(); i++) {
			
  		  TaskSet t=tasks.get(i);
  		  logger.info(" running -------------- operation..  " +t.operation+"  it is number "+i+"  of "+tasks.size());
  		 
		 
				
				PatchTestSet temp=new PatchTestSet();
				temp.setOperationSettings(t.operation, t.DB, t.MaxFiles, t.repeatTimes);
				temp.run();
				
				
			 
	     

		 
  		  
  		  
		}
  	  
    }
	
	public void RunAlgCommpare(){
		
		
	}
	
	public void ReadCommands() {
		try {
			System.out.println("reading the file................ wait");
			File afile = new File("commands.txt");
			Scanner input = new Scanner(new BufferedReader(
					new FileReader(afile)));
int t;

TaskSet test=null;
			while (input.hasNext()) {
				// first
				String inputString = input.nextLine();
				//run my program with commands
				logger.trace(" Reading command "+inputString);
				if (inputString.trim().startsWith("task")) {
					
					test=new TaskSet();
					//default settings 
					test.DB=1;
					test.operation=1;
					test.MaxFiles=2;
					test.repeatTimes=1;
                    tasks.add(test);
				}
				else if (inputString.trim().startsWith("op")){
					t = input.nextInt();
					test.operation=t;
				}
				else if (inputString.trim().startsWith("db")){
					t = input.nextInt();
					test.DB=t;
				}
			
				else if (inputString.trim().startsWith("max")){
					t = input.nextInt();
					test.MaxFiles=t;
				}
				else if (inputString.trim().startsWith("re")){
					t = input.nextInt();
					test.repeatTimes=t;
				}
				

			}

			input.close();

		} catch (FileNotFoundException e) {
			 
			e.printStackTrace();
		} catch (IOException e) {
			 
			e.printStackTrace();
		}

	}
	
	class TaskSet {
		final static int OPERATION_ALGComp=0;
		final static int OPERATION_FSetsComp=1;
		final static int OPERATION_F1Comp=2;
		final static int OPERATION_SymbolCountComp=3;
		
		
		int operation;
		int DB; 
		int MaxFiles;
		//int Functions;
		int repeatTimes;
		
	}

}
