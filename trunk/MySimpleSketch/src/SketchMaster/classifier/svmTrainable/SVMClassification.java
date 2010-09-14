package SketchMaster.classifier.svmTrainable;

import java.util.Arrays;

import SketchMaster.classifier.Classification;

public class SVMClassification  extends Classification  {

	
	   private String types[];
	    private double confidences[];
	    private String highestConfidenceType;
	    private double highestConfidence;
	    
	    @Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + Arrays.hashCode(confidences);
			long temp;
			temp = Double.doubleToLongBits(highestConfidence);
			result = prime * result + (int) (temp ^ (temp >>> 32));
			result = prime
					* result
					+ ((highestConfidenceType == null) ? 0
							: highestConfidenceType.hashCode());
			result = prime * result + Arrays.hashCode(types);
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (!super.equals(obj))
				return false;
			if (getClass() != obj.getClass())
				return false;
			SVMClassification other = (SVMClassification) obj;
			if (!Arrays.equals(confidences, other.confidences))
				return false;
			if (Double.doubleToLongBits(highestConfidence) != Double
					.doubleToLongBits(other.highestConfidence))
				return false;
			if (highestConfidenceType == null) {
				if (other.highestConfidenceType != null)
					return false;
			} else if (!highestConfidenceType
					.equals(other.highestConfidenceType))
				return false;
			if (!Arrays.equals(types, other.types))
				return false;
			return true;
		}

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
	        StringBuffer buf = new StringBuffer();
               buf.append(s);
	        for(int i=0; i<getTypeCount(); i++){
	        	buf.append( " [");
	        	buf.append(  getType(i));
	        	buf.append(",  ");
	        	buf.append( getConfidence(i));
	        	buf.append( "] ");
	            
	        }
	        s = buf.append(" ) ").toString();
	        return s;
	    }
}
