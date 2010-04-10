/**
 * 
 */
package settings;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import test.ConfusionMatrix;

/**
 * @author Maha
 * 
 */
public class TestResults implements Serializable {
	// this will class will be resposible for savign the tes reults in a meaning
	// form to compare it
	// in graphs , table, sheets and files.ame
	
	 ConfusionMatrix confusion=new ConfusionMatrix();
		
		 
		long taskTimeMsFeatures ; //= System.nanoTime( )- startTimeMs;
		
	
		long taskTimeRec;
	
		long taskTimeSeg;
		int SegCount=0;
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String FinalString="";
		FinalString+=" Result of "+TestName;
		FinalString+=" and test id="+nTestId;
		FinalString+="  With "+nSamples+"  Samples ";
		FinalString+="  And "+nCategory+" Category ";
		FinalString+=" is "+correctSamples*100+"% Correct sample percent";
		FinalString+=" and "+falseSamples*100+"% false sample percent";
		
		return FinalString;
	}
	public String CategorySamplesString ="";
	public String CategoriesString(){
		
		CategorySamplesString="";
		if (categorySamples!=null)
		{
			for (Iterator iter = categorySamples.entrySet().iterator(); iter.hasNext();) {
				Entry<String, ArrayList<Integer>> Category = (Entry<String, ArrayList<Integer>>) iter.next();
				CategorySamplesString += " Symbol  "+ Category.getKey();
				CategorySamplesString += " has a recognition rate of ";
			    double correct=0.0;
			    correct=((double)Category.getValue().get(1)/(double)Category.getValue().get(0))*100.0;
			    double falsenumber=100.0*((double)Category.getValue().get(2)/(double)Category.getValue().get(0));
			    CategorySamplesString += "Correct ="+correct+"  % ";
			    CategorySamplesString += " false sample  ="+falsenumber+" % ";
			    CategorySamplesString +=" \n " ;
			    
			}
		}
		//CategorySamplesString
		
		return CategorySamplesString;
	}
	String TestName="";
	double correctSamples=0.0;
	int nCorrectSamples=0;//number of correct samples
	double falseSamples=0.0;
	int nFalseSample=0;//number of false samples. 
	int nSamples=0;  //number of Samples 
	int nCategory=0;  //number of categories 
	int nFiles=0;  //number of test fiels. 
	int nFeatures=0;//number of feartures of the test 
	int nTrainFiles=0; //number fo files used to train system.
	int nTestId=0;  // id of the test.
	HashMap<String, ArrayList<Integer>>  categorySamples;
	
	HashMap<String, Accuracy>  categoryAccuracies;//=new HashMap<String, Accuracy>();
 	/**
	 * @return the testName
	 */
	public String getTestName() {
		return TestName;
	}
	/**
	 * @param testName the testName to set
	 */
	public void setTestName(String testName) {
		TestName = testName;
	}
	/**
	 * @return the correctSamples
	 */
	public double getCorrectSamples() {
		return correctSamples;
	}
	/**
	 * @param correctSamples the correctSamples to set
	 */
	public void setCorrectSamples(double correctSamples) {
		this.correctSamples = correctSamples;
	}
	/**
	 * @return the falseSamples
	 */
	public double getFalseSamples() {
		return falseSamples;
	}
	/**
	 * @param falseSamples the falseSamples to set
	 */
	public void setFalseSamples(double falseSamples) {
		this.falseSamples = falseSamples;
	}
	/**
	 * @return the nSamples
	 */
	public int getNSamples() {
		return nSamples;
	}
	/**
	 * @param samples the nSamples to set
	 */
	public void setNSamples(int samples) {
		nSamples = samples;
	}
	/**
	 * @return the nCategory
	 */
	public int getNCategory() {
		return nCategory;
	}
	/**
	 * @param category the nCategory to set
	 */
	public void setNCategory(int category) {
		nCategory = category;
	}
	/**
	 * @return the nFiles
	 */
	public int getNFiles() {
		return nFiles;
	}
	/**
	 * @param files the nFiles to set
	 */
	public void setNFiles(int files) {
		nFiles = files;
	}
	public void setTestID(int i) {
	nTestId=i;
		
	}
	public void setCategoryResults(
			HashMap<String, ArrayList<Integer>> categorySamples) {
		
		this.categorySamples=categorySamples;
	}
	/**
	 * @return the categoryAccuracies
	 */
	public HashMap<String, Accuracy> getCategoryAccuracies() {
		return categoryAccuracies;
	}
	/**
	 * @param categoryAccuracies the categoryAccuracies to set
	 */
	public void setCategoryAccuracies(HashMap<String, Accuracy> categoryAccuracies) {
		this.categoryAccuracies = categoryAccuracies;
		///computeAccuacy();
		
	}
	private void computeAccuacy()
	{
		Collection<Accuracy> acc= this.categoryAccuracies.values();
		for (Iterator iterator = acc.iterator(); iterator.hasNext();) {
			Accuracy accitem= (Accuracy) iterator.next();
			 accitem.computeStat();
			
			
		}
	 
		
		
	}
	/**
	 * @return the confusion
	 */
	public ConfusionMatrix getConfusion() {
		return confusion;
	}
	/**
	 * @param confusion the confusion to set
	 */
	public void setConfusion(ConfusionMatrix confusion) {
		this.confusion = confusion;
	}
	public long getTaskTimeMsFeatures() {
		return taskTimeMsFeatures;
	}
	public void setTaskTimeMsFeatures(long taskTimeMsFeatures) {
		this.taskTimeMsFeatures = taskTimeMsFeatures;
	}
	public long getTaskTimeRec() {
		return taskTimeRec;
	}
	public void setTaskTimeRec(long taskTimeRec) {
		this.taskTimeRec = taskTimeRec;
	}
	public long getTaskTimeSeg() {
		return taskTimeSeg;
	}
	public void setTaskTimeSeg(long taskTimeSeg) {
		this.taskTimeSeg = taskTimeSeg;
	}
	public int getSegCount() {
		return SegCount;
	}
	public void setSegCount(int segCount) {
		SegCount = segCount;
	}
	int ExampleCount;
	public void setExampleTimeCount(int exampleCount) {
		ExampleCount=exampleCount;
	}
	public int getExampleCount() {
		  
		return ExampleCount;
	}
	
}
