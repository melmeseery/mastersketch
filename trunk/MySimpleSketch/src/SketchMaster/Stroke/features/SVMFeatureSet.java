/**
 * 
 */
package SketchMaster.Stroke.features;

import org.apache.log4j.Logger;

import SketchMaster.classifier.svmTrainable.SVMClassifier;

/**
 * @author Maha
 *
 */
public class SVMFeatureSet {
	private static transient final Logger logger = Logger.getLogger(SVMFeatureSet.class);
	private static  int numFeatures=0;
	  /** The feature vector. */
    private double _features[]=null;
    private String featuresName[]=null;
	/**
	 * 
	 */
	
	public SVMFeatureSet(int count){
		initFeature(count);
	}
	
public SVMFeatureSet(double[] vals) {
	 _features=vals.clone();
	}

public SVMFeatureSet(double[] vals,String[] names) {

	 _features=vals.clone();
	 featuresName=new String[names.length];
	 for (int i = 0; i < names.length; i++) {
		 featuresName[i]=names[i];
	}
	 
	//featuresName=names.clone();
	 
//		for (int i = 0; i < vals.length; i++) {
//			logger.info("   features "+featuresName[i]+"  = "+ _features[i]);
//		}
//		logger.info("--------------------------------------Features------------------------------------");
	}
public SVMFeatureSet(SVMFeatureSet s) {
	 _features=s._features.clone();
	 featuresName=new String[s.featuresName.length];
	 for (int i = 0; i < s.featuresName.length; i++) {
		 featuresName[i]=s.featuresName[i];
	}
}

public static int getFeatureMaxCount(){
	return numFeatures;
}
public int getFeatureCount() {// this feature set with no zero values 

		return  _features.length;
	}

public int getFeatureNoZeroCount(){
	
	int count=0;
	for (int i = 0; i < _features.length; i++) {
		if (_features[i]!=0)
			count++;
	}
	return count;
}

public double getFeature(int i) { // get the value of feature of value i 
	 return _features[i];
	//return 0;
}
public String getFeatureName(int i) {
	
	if (featuresName!=null)
		return featuresName[i];
	return "";
}

public void setFeature(int i, double val, String name){
	_features[i]=val;
	featuresName[i]=name;
}
public void setFeature(int i, double val){
	_features[i]=val;
}

public  void initFeature(int count){
	_features=new double[count];
	featuresName=new String[count];
}

public String getFullString() {

StringBuilder   t=new StringBuilder("");
String newline = System.getProperty("line.separator");

t.append(" Features of Stroke are :");
t.append(newline);
for (int i = 0; i <_features.length; i++) {
	
	t.append(" Feature ");
	t.append(featuresName[i]);
	t.append(" = ");
	t.append(_features[i]);
	t.append(" ");
	if (i%3==0){
		t.append(newline);
	}
	
}
	return t.toString();
}

}
