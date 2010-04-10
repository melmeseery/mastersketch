/**
 * 
 */
package settings;

/**
 * @author Maha
 *
 */
public class Accuracy {
	//here is the table to define the persion 
	/****
	 *                             The Required result
	 *                    | Current class (True)(Sick) | others(False)(healthy)|
	 * Test outcome            
	 *    (sick) Positive |    True Positive           | False Positive        |  Positive predictive value
	 * (healthy)  Negtive |    False Negative          | True Negative         |  Negative predictive value       
	 *                           Sensitivity           |  Specificity           | Accuracy
	 *                       
True positive: Sick people correctly diagnosed as sick
False positive: Healthy people wrongly identified as sick
True negative: Healthy people correctly identified as healthy
False negative: Sick people wrongly identified as healthy
	 * **/
	
	
public double TruePositive; //  TP it is supposed of this class and recognized as is  
	
	public double TrueNegative; // TN it is supposed not of this class and recognized as not
	
	public double  FalseNegative ;  // FN it is supposed of this class and recognized as not
	
	public double FalsePositive; // FP it is supposed not of this class and recognized as is
	
	String ClassName= "Class one ";
	String OtherClassName="Class two";

	int SamplesClass1;
	int SamplesOthers;
	
	double FalsePositiveRate; // (alpha) = FP / (FP + TN) =   1 - specificity
	double FalseNegativeRate;//B  = FN / (TP + FN) = 1 - sensitivity
	double specificity; // TN / (FP + TN)
	double sensitivity ;//TP / (TP + FN)
	 double Positivepredictivevalue;// TP / (TP + FP)
	 
	 
	 double Negativepredictivevalue ;//TN / (TN + FN)
	double Accuracy; //  ((TP+TN)/ TotalCount)
	 double Power; // power = sensitivity = 1 - B
	 
	 int TotalCount;   //  SamplesClass1+SamplesOthers ==> count of all samples in the test.
	 
	 //In information retrieval positive predictive value is called precision, and sensitivity is called recall.
		double Recall; //sensitivity ;//TP / (TP + FN)  
		double Percision;// Positivepredictivevalue=TP / (TP + FP)
		
		double Fmeasure;//F-measure=2*(Percision*recall)/(Percision+recall)
		
		public void computeStat(){
			
			 TotalCount=SamplesClass1+ SamplesOthers;
			FalsePositiveRate=((double)FalsePositive)/(double)(FalsePositive+TrueNegative);
			
			FalseNegativeRate=((double)FalseNegative)/(double)(TruePositive+FalseNegative);
			
			specificity=((double)TrueNegative)/(double)(FalsePositive+TrueNegative);
			
			sensitivity=((double)TruePositive)/(double)(TruePositive+FalseNegative);
			Positivepredictivevalue=((double)TruePositive)/(double)(TruePositive+FalsePositive);
			
			
			Negativepredictivevalue=((double)TrueNegative)/(double)(FalseNegative+TrueNegative);
			
			Accuracy=((double)(TrueNegative+TruePositive))/(double)(FalseNegative+TrueNegative+TruePositive+FalsePositive);
			
			Power=sensitivity;
			Recall=sensitivity;
			Percision=Positivepredictivevalue;
			
			Fmeasure=(double)2*((double)(Recall*Percision))/((double)(Recall+Percision));
			
			
			
			
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
		String s="";
		String newline = System.getProperty("line.separator");
		
		 s+="-------------------------------"+this.ClassName+"--------------------------------------";
			s+=newline;	
		s+="  TruePositive  = "+TruePositive;
		s+="  FalsePositive  = "+FalsePositive;
		
						
			s+="  TrueNegative = "+TrueNegative;
			s+="  FalseNegative=  "+FalseNegative;
			 
			s+=newline;	
			s+="  FalsePositiveRate= = "+FalsePositiveRate;
			 
			s+=" FalseNegativeRate=  "+ FalseNegativeRate;
			s+=newline;	
			s+="  specificity=  "+  specificity;
			s+="  sensitivity=  "+ sensitivity;
			s+=" power "+  Power;
			s+=newline;	
			s+=" Positivepredictivevalue=  "+Positivepredictivevalue ;
			s+="  Negativepredictivevalue=  "+ Negativepredictivevalue;
			s+=newline;	
			s+="  Accuracy= "+ Accuracy ;
			s+="  Recall= "+Recall ;	
			s+="  Percision= " + Percision;	
			s+="  Fmeasure = " +Fmeasure ;	
			s+=newline;	
		
			s+="  Accuracy= "+ Accuracy ;
			s+=newline;	
			s+="  Recall= "+Recall ;
			s+=newline;	
			s+="  Percision= " + Percision;
			s+=newline;	
			s+="  Fmeasure = " +Fmeasure ;	
			s+=newline;	
		 s+="---------------------------------------------------------------------";
			s+=newline;	
			return s;
		}

		/**
		 * @return the className
		 */
		public String getClassName() {
			return ClassName;
		}

		/**
		 * @param className the className to set
		 */
		public void setClassName(String className) {
			ClassName = className;
		}

		/**
		 * @return the otherClassName
		 */
		public String getOtherClassName() {
			return OtherClassName;
		}

		/**
		 * @param otherClassName the otherClassName to set
		 */
		public void setOtherClassName(String otherClassName) {
			OtherClassName = otherClassName;
		}

		/**
		 * @return the truePositive
		 */
		public double getTruePositive() {
			return TruePositive;
		}

		/**
		 * @param truePositive the truePositive to set
		 */
		public void setTruePositive(double truePositive) {
			TruePositive = truePositive;
		}

		/**
		 * @return the trueNegative
		 */
		public double getTrueNegative() {
			return TrueNegative;
		}

		/**
		 * @param trueNegative the trueNegative to set
		 */
		public void setTrueNegative(double trueNegative) {
			TrueNegative = trueNegative;
		}

		/**
		 * @return the falseNegative
		 */
		public double getFalseNegative() {
			return FalseNegative;
		}

		/**
		 * @param falseNegative the falseNegative to set
		 */
		public void setFalseNegative(double falseNegative) {
			FalseNegative = falseNegative;
		}

		/**
		 * @return the falsePositive
		 */
		public double getFalsePositive() {
			return FalsePositive;
		}

		/**
		 * @param falsePositive the falsePositive to set
		 */
		public void setFalsePositive(double falsePositive) {
			FalsePositive = falsePositive;
		}

		/**
		 * @return the samplesClass1
		 */
		public int getSamplesClass1() {
			return SamplesClass1;
		}

		/**
		 * @param samplesClass1 the samplesClass1 to set
		 */
		public void setSamplesClass1(int samplesClass1) {
			SamplesClass1 = samplesClass1;
		}

		/**
		 * @return the samplesOthers
		 */
		public int getSamplesOthers() {
			return SamplesOthers;
		}

		/**
		 * @param samplesOthers the samplesOthers to set
		 */
		public void setSamplesOthers(int samplesOthers) {
			SamplesOthers = samplesOthers;
		}

		/**
		 * @return the totalCount
		 */
		public int getTotalCount() {
			return TotalCount;
		}

		/**
		 * @param totalCount the totalCount to set
		 */
		public void setTotalCount(int totalCount) {
			TotalCount = totalCount;
		}

		/**
		 * @return the falsePositiveRate
		 */
		public double getFalsePositiveRate() {
			return FalsePositiveRate;
		}

		/**
		 * @return the falseNegativeRate
		 */
		public double getFalseNegativeRate() {
			return FalseNegativeRate;
		}

		/**
		 * @return the specificity
		 */
		public double getSpecificity() {
			return specificity;
		}

		/**
		 * @return the sensitivity
		 */
		public double getSensitivity() {
			return sensitivity;
		}

		/**
		 * @return the positivepredictivevalue
		 */
		public double getPositivepredictivevalue() {
			return Positivepredictivevalue;
		}

		/**
		 * @return the negativepredictivevalue
		 */
		public double getNegativepredictivevalue() {
			return Negativepredictivevalue;
		}

		/**
		 * @return the accuracy
		 */
		public double getAccuracy() {
			return Accuracy;
		}

		/**
		 * @return the power
		 */
		public double getPower() {
			return Power;
		}

		/**
		 * @return the recall
		 */
		public double getRecall() {
			return Recall;
		}

		/**
		 * @return the percision
		 */
		public double getPercision() {
			return Percision;
		}

		/**
		 * @return the fmeasure
		 */
		public double getFmeasure() {
			return Fmeasure;
		}
		
}
