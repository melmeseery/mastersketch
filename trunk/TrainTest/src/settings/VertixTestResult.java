/**
 * 
 */
package settings;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Maha
 *
 */
public class VertixTestResult {
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
	//ArrayList<VertixTestResultNode>
	HashMap<String, ArrayList<VertixTestResultNode>> categoryResults=new  HashMap<String, ArrayList<VertixTestResultNode>>();
	 VectorResultAnalysis CatAnalysis=null;
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
	 * @return the nCorrectSamples
	 */
	public int getNCorrectSamples() {
		return nCorrectSamples;
	}
	/**
	 * @param correctSamples the nCorrectSamples to set
	 */
	public void setNCorrectSamples(int correctSamples) {
		nCorrectSamples = correctSamples;
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
	 * @return the nFalseSample
	 */
	public int getNFalseSample() {
		return nFalseSample;
	}
	/**
	 * @param falseSample the nFalseSample to set
	 */
	public void setNFalseSample(int falseSample) {
		nFalseSample = falseSample;
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
	/**
	 * @return the nFeatures
	 */
	public int getNFeatures() {
		return nFeatures;
	}
	/**
	 * @param features the nFeatures to set
	 */
	public void setNFeatures(int features) {
		nFeatures = features;
	}
	/**
	 * @return the nTrainFiles
	 */
	public int getNTrainFiles() {
		return nTrainFiles;
	}
	/**
	 * @param trainFiles the nTrainFiles to set
	 */
	public void setNTrainFiles(int trainFiles) {
		nTrainFiles = trainFiles;
	}
	/**
	 * @return the nTestId
	 */
	public int getNTestId() {
		return nTestId;
	}
	/**
	 * @param testId the nTestId to set
	 */
	public void setNTestId(int testId) {
		nTestId = testId;
	}
	/**
	 * @return the categoryResults
	 */
	public HashMap<String, ArrayList<VertixTestResultNode>> getCategoryResults() {
		return categoryResults;
	}
	
	
	/**
	 * @param categoryResults the categoryResults to set
	 */
	public void setCategoryResults(
			HashMap<String, ArrayList<VertixTestResultNode>> categoryResults) {
		this.categoryResults = categoryResults;
		
	}
	public void AnalysisVertixResults(){
		CatAnalysis=new VectorResultAnalysis();
		CatAnalysis.setCategoryResults(this.categoryResults);
	}
	public String toString() {
		String FinalString="";
		FinalString+=" Result of "+TestName;
		FinalString+=" and test id="+nTestId;
		FinalString+="  With "+nSamples+"  Samples ";
		FinalString+="  And "+nCategory+" Category ";
		FinalString+=" is "+correctSamples*100+" % Correct sample percent";
		FinalString+=" and "+falseSamples*100+"  % false sample percent";
		
		return FinalString;
	}
	
	public void AddCategoryResult(String cat, VertixTestResultNode result){
		
		
		if (this.categoryResults!=null){
			  if (	  categoryResults.containsKey(cat))
			  {
				  ArrayList<VertixTestResultNode> tempSamples= categoryResults.get(cat);
				  tempSamples.add(result);
				  categoryResults.put(cat, tempSamples);
			  }
			  else {
				  
				  ArrayList<VertixTestResultNode> tempSamples= new ArrayList<VertixTestResultNode>();
				  tempSamples.add(result);
				  categoryResults.put(cat, tempSamples);
				  
			  }
			
			
			
		}
		
	}
	public void setTestID(int i) {
		nTestId=i;
			
		}
	/**
	 * @return the catAnalysis
	 */
	public VectorResultAnalysis getCatAnalysis() {
		return CatAnalysis;
	}
	
	
	
}
