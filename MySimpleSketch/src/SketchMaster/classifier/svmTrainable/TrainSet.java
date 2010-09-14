/**
 * 
 */
package SketchMaster.classifier.svmTrainable;

import java.awt.List;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.log4j.Logger;

import SketchMaster.Stroke.StrokeData.PointData;
import SketchMaster.Stroke.StrokeData.SegmentCluster;
import SketchMaster.Stroke.StrokeData.Stroke;

import SketchMaster.Stroke.features.SegmentClusterFeatureSet;
import SketchMaster.io.xml.XmlManager;

/**
 * @author Maha
 *
 */
public class TrainSet {

	/**
	 * 
	 */
	private static final Logger logger = Logger.getLogger(TrainingSet.class);
	
	

	 
	   /* Internal constant for the array slot of negative examples.
	     */
	    private static final int NEGATIVE = 0;

	    /**
	     * Internal constant for the array slot of positive examples.
	     */
	    private static final int POSITIVE = 1;

	    /**
	     * Store a mapping from types to their positive/negative examples.
	     */
	    private HashMap _map;

	    /**
	     * Construct an empty training set.
	     */
	    public TrainSet() {
	        _map = new HashMap();
	       
	    }

	    /**
	     * Add a negative example to this training set for the given type.
	     */
	    public final void addNegativeExample(String t,ArrayList<Stroke> s) {
	        addExample(t, s, NEGATIVE);
	    }

	    /**
	     * Add a positive example to this training set for the given type.
	     */
	    public final void addPositiveExample(String t, ArrayList<Stroke> s) {
	        addExample(t, s,POSITIVE);
	    }
  
	   
	  
	    /**
	     * Add a example to this training set for the given type
	     * (either positive or negative, denoted by the "which" argument).
	     */
	    public final void addExample(String t, ArrayList<Stroke> s, int which) {
	    	
	    	
	    	 ArrayList< ArrayList<Stroke>>[] l = ( ArrayList< ArrayList<Stroke>>[])_map.get(t);
	        if(l == null) {
	            l = new ArrayList[2];
	            l[0] = new  ArrayList< ArrayList<Stroke>>();//positive exmpale 
	            l[1] = new ArrayList< ArrayList<Stroke>>();//negtive examplessssss
	            _map.put(t, l);
	        }
	        l[which].add(s);
	    }
	    
	    /**
	     * Return true if the training type with the specified name is
	     * in the set, or false otherwise.
	     */
	    public final boolean containsType(String t){
	        return _map.get(t) != null;
	    }

	    
	    
	    /**
	     * An internal method to get the example count for a particular type
	     * (either positive or negative, denoted by the "which" argument).
	     */
	    private final int getExampleCount(String t, int which) {
	    	 ArrayList< ArrayList<Stroke>>[] l = ( ArrayList< ArrayList<Stroke>>[])_map.get(t);
	        if(l == null) {
	            return 0;
	        }
	        else {
	            return l[which].size();
	        }
	    }

	    public  ArrayList< ArrayList<Stroke>> getExamples(String t){
	    	   ArrayList< ArrayList<Stroke>>[] l = (ArrayList<ArrayList<Stroke>>[])_map.get(t);
		        if(l == null) {
		            return new   ArrayList< ArrayList<Stroke>>();
		        }
		        else {
		        	  ArrayList< ArrayList<Stroke>> temp=new ArrayList<ArrayList<Stroke>>();
		        	  
		        	  
		        	  temp.addAll(l[0]);
		        	  temp.addAll(l[1]);
		        	  return temp;
		        	  
		        }
		        //return l[which].iterator();
	    }
	    /**
	     * An internal method to get the examples for a particular type
	     * (either positive or negative, denoted by the "which" argument).
	     */
	    private final Iterator getExamples(String t, int which) {
	    	   ArrayList< ArrayList<Stroke>>[] l = (ArrayList<ArrayList<Stroke>>[])_map.get(t);
	        if(l == null) {
	            return ((java.util.List) new List()).iterator();
	        }
	        return l[which].iterator();
	    }
	   

	    /**
	     * Return how many types are contained in this training set.
	     */
	    public final int getTypeCount() {
	        return _map.size();
	    }
	    
	    /**
	     * Return the number of negative examples for the given type.
	     */
	    public final int negativeExampleCount(String t) {
	        return getExampleCount(t, NEGATIVE);
	    }

	    /**
	     * An iterator over the negative examples for the given type.
	     */
	    public final Iterator negativeExamples(String t) {
	        return getExamples(t, NEGATIVE);
	    }

	    /**
	     * Return the number of positive examples for the given type.
	     */
	    public final int positiveExampleCount(String t) {
	        return getExampleCount(t, POSITIVE);
	    }
	    
	    /**
	     * An iterator over the positive examples for the given type.
	     */
	    public final Iterator positiveExamples(String t) {
	        return getExamples(t, POSITIVE);
	    }

	    /**
	     * Remove the specified type from this training set.  This method
	     * cannot be called while iterating over the types, otherwise a
	     * ConcurrentModificationException will be thrown.
	     */
	    public final void removeType(String t){
	        _map.remove(t);
	    }
	    public int typesSize(){
	    	 return _map.keySet().size();
	    }

	    public int examplesSize(String t){
	    	
	    	 ArrayList[] l = (ArrayList[])_map.get(t);
		        if(l != null) {
		           return l[0].size()+l[1].size();
		        }
		        else 
		        	return 0;
	    }
	    /**
	     * An iterator over the types contained in this training set.
	     */
	    public Iterator types() {
	        return _map.keySet().iterator();
	    }

	    public String toString(){
	    	
	    	 StringBuffer buf = new StringBuffer();
	         for(Iterator iter = types(); iter.hasNext();){
	             String type = (String)iter.next();
	             int num = positiveExampleCount(type);
	             buf.append(num + "\t" + type + "s\n");
	         }
	         return buf.toString();

}
     public int getExamplesCount(){
     	int totalCount=0;
      for (Iterator iterator = 	_map.keySet().iterator(); iterator.hasNext();) {
			String type = (String) iterator.next();
			totalCount+=examplesSize(type);
			//totalCount+=getExampleCount(type, 1);
			
			
		}
     	
     	return totalCount;
     
     
     	
     	
     } 
     
     public void saveXML(String filename){
    	 
    	 XmlManager  xml=new XmlManager();
    	  xml.openParser();
    	 
    	   for (Iterator iterator = 	_map.keySet().iterator(); iterator.hasNext();) {
   			String type = (String) iterator.next();
   		
   		 ArrayList<ArrayList<Stroke>>[] examples = (ArrayList<ArrayList<Stroke>>[])_map.get(type);
   			
   		 
   		 ArrayList<ArrayList<Stroke>> total=new ArrayList<ArrayList<Stroke>>();
   		 
   		 ArrayList<ArrayList<Stroke>> neg=  examples[NEGATIVE];
   		 ArrayList<Integer> Labels=new ArrayList<Integer>();
   		 total.addAll(neg);
   		 for (int i = 0; i < neg.size(); i++) {
   			Labels.add(new Integer(-1));
		}
   		 
   		 ArrayList<ArrayList<Stroke>> pos=  examples[POSITIVE];
   		 
   		 total.addAll(pos);
   		 
   		 for (int i = 0; i < pos.size(); i++) {
    			Labels.add(Integer.valueOf(1));
 		}
   		 
   		 xml.addType(type,  total, Labels);
   		 
   		 
   		 
   		//	totalCount+=examplesSize(type);
   			//totalCount+=getExampleCount(type, 1);
   			
   			
   		}
    	   
    	   xml.writeFile(filename);
    	 
    	 
    	 
     }
     
		public void clear() {
			_map.clear();
			
		}

		public void readXML(String filename) {
		 
		 	 XmlManager  xml=new XmlManager();
	    	  xml.readXML(filename);
	    	  
	    		ArrayList<String> Cat = xml.getCategoriesList();
	    		for (int j = 0; j < Cat.size(); j++) {
	    			  ArrayList<ArrayList<Stroke>> TestSamples = xml.getPosExampleForCat(Cat.get(j));
	    			  for (int i = 0; i < TestSamples.size(); i++) {
	    					this.addPositiveExample(Cat.get(j), TestSamples.get(i));
					}
	    			  
	    			 TestSamples = xml.getNegExampleForCat(Cat.get(j));
	    			  for (int i = 0; i < TestSamples.size(); i++) {
	    					this.addNegativeExample(Cat.get(j), TestSamples.get(i));
					}
	    		
	    			  
	    			  
	    			  
	  	    		
				}
	    		
	    		
	    		
		}
		
	

}
