package SketchMaster.classifier.svmTrainable;

import SketchMaster.classifier.Classification;

public class SVMClassification  extends Classification  {

	
	   private String types[];
	    private double confidences[];
	    private String highestConfidenceType;
	    private double highestConfidence;
	    
	    /**
	     * Construct a classification with no types.
	     */
	    public  SVMClassification (String[] stypes, double[]dconfidences) {
	        types = stypes;
	        confidences = dconfidences;

	        int maxIndex = 0;
	        double maxConfidence = confidences[maxIndex];
	        for(int i=0; i<types.length; i++){
	            if(confidences[i]>confidences[maxIndex]){
	                maxIndex = i;
	                maxConfidence = confidences[maxIndex];
	            }
	        }
	        highestConfidenceType = types[maxIndex];
	        highestConfidence = maxConfidence;
	    }

	    /**
	     * Return the highest confidence value.
	     */
	    public double getHighestConfidence(){
	        return highestConfidence;
	    }

	    /**
	     * Return the highest confidence type.
	     */
	    public String getHighestConfidenceType(){
	        return highestConfidenceType;
	    }
	    
	    /**
	     * Return the number of types in this classification.
	     */
	    public int getTypeCount () {
	        return (types == null) ? 0 :types.length;
	    }

	    /**
	     * Return the i'th type.
	     */
	    public String getType (int i) {
	        return types[i];
	    }

	    /**
	     * Return the i'th confidence.
	     */
	    public double getConfidence (int i) {
	        return confidences[i];
	    }

	    /**
	     * Return a string representation of this classification
	     * consisting of type and confidence pairs.
	     */
	    public String toString(){
	        String s = getTypeCount() + " types: (";
	        for(int i=0; i<getTypeCount(); i++){
	            s = s + "[" + getType(i) + ", " + getConfidence(i) + "] ";
	        }
	        s = s + ")";
	        return s;
	    }
}
