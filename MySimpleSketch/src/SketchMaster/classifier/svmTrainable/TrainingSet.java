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
import SketchMaster.Stroke.features.SVMFeatureSet;
import SketchMaster.Stroke.features.SegmentClusterFeatureSet;
 
import SketchMaster.system.Recogniziers.SVMRecognizier;
 

public class TrainingSet {

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
	    public TrainingSet() {
	        _map = new HashMap();
	       
	    }

	    /**
	     * Add a negative example to this training set for the given type.
	     */
	    public final void addNegativeExample(String t,SVMFeatureSet  s) {
	        addExample(t, s, NEGATIVE);
	    }

	    /**
	     * Add a positive example to this training set for the given type.
	     */
	    public final void addPositiveExample(String t, SVMFeatureSet  s) {
	        addExample(t, s, POSITIVE);
	    }
     
	    public boolean[]  getUselessFeatures(){
	    	ArrayList<DataStat> FeatureStates=null;
	        for(Iterator iter = types(); iter.hasNext();){
	             String type = (String)iter.next();
	     		//logger.error(" Type  ."+type);
	             ArrayList<SVMFeatureSet>[] l = (ArrayList<SVMFeatureSet>[])_map.get(type);
	             FeatureStates=AddFeatState(l[0],FeatureStates);
	             FeatureStates= AddFeatState(l[1],FeatureStates);
	             
	             
	        }
	        
	        boolean[] useless=null;
	        int countUseless=0;
	        int featcount=0;
	        
	        if (FeatureStates!=null){
	        	useless=new boolean[ FeatureStates.size()];
	        	featcount= FeatureStates.size();
	        	for (int i = 0; i < FeatureStates.size(); i++) {
					FeatureStates.get(i).ComputeState();
				    double var,std;
				    var=FeatureStates.get(i).getVariance();
				    std=FeatureStates.get(i).getStd();
						
					///check the variance if variance less than 0.1 then this is use less.
					//and need remove. 
					if (Double.isNaN(std)){
						logger.error(" Feature "+i+" ("+FeatureStates.get(i).getName()+") with  "+"  variance is "+var+"  std "+std+" is uselesss" );
						
						useless[i]=true;
						countUseless++;
					}
					else {
						useless[i]=false;
					}
					
					
				}
	        }
	       // logger.info(" Number of useless features is  "+countUseless+" from total of "+featcount);

	        logger.error(" Number of useless features is  "+countUseless+" from total of "+featcount);
	        return useless;
	    	
	    }
	    private ArrayList<DataStat>  AddFeatState(ArrayList<SVMFeatureSet> list,ArrayList<DataStat> FeatureStates){
	    	SVMFeatureSet feats ;
	    	if (FeatureStates==null)
	    	{
	    	
	    	//	logger.error(" creating the features setates....");
	    		if (list.size()>0)
	    		{
	    			FeatureStates=new ArrayList<DataStat>();
	    			feats=	list.get(0);
	    			
	    			for (int i = 0; i < feats.getFeatureCount(); i++) {
	    				DataStat  st=new DataStat();
	    				st.AddData(  feats.getFeature(i))  ;
	    				
	    				FeatureStates.add(st);
					}
	    			
	    		}
	    	}
	    	
	    	for (int i = 0; i < list.size(); i++) {
				feats = list.get(i);
				  
				for (int j = 0; j < feats.getFeatureCount(); j++) {
					FeatureStates.get(j).setName(feats.getFeatureName(j));
    			    FeatureStates.get(j).AddData(feats.getFeature(j) );
				}
				
				
			}
	    	return FeatureStates;
	    }
	    /**
	     * Add a example to this training set for the given type
	     * (either positive or negative, denoted by the "which" argument).
	     */
	    public final void addExample(String t, SVMFeatureSet  s, int which) {
	    	
	    	
	        ArrayList<SVMFeatureSet>[] l = (ArrayList<SVMFeatureSet>[])_map.get(t);
	        if(l == null) {
	            l = new ArrayList[2];
	            l[0] = new ArrayList<SVMFeatureSet>();//positive exmpale 
	            l[1] = new ArrayList<SVMFeatureSet>();//negtive examplessssss
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
	        ArrayList<SVMFeatureSet>[] l = (ArrayList<SVMFeatureSet>[])_map.get(t);
	        if(l == null) {
	            return 0;
	        }
	        else {
	            return l[which].size();
	        }
	    }

	    /**
	     * An internal method to get the examples for a particular type
	     * (either positive or negative, denoted by the "which" argument).
	     */
	    private final Iterator getExamples(String t, int which) {
	        ArrayList<SVMFeatureSet>[] l = (ArrayList<SVMFeatureSet>[])_map.get(t);
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
		public void clear() {
			_map.clear();
			
		}
		public void SaveToArrffFile(String filename){
			
			
			/*******
			 * 
			 * 
			 * @RELATION  DigitsClassifier

	@attribute 'Class' numeric  
	@attribute '1' numeric  
	@attribute '2' numeric  
	@attribute '3' numeric  
	@attribute '4' numeric  
	@attribute '5' numeric  
	@attribute '6' numeric  
	@attribute '7' numeric  
	@attribute '8' numeric  
	@attribute '9' numeric  
	@attribute '10' numeric  

	@data
	1   , 26 , 0 , 1 , 0 , 0 , 5 , 0 , 0 , 13.6667 , 6
	1   , 80 , 0 , 1 , 1 , 0 , 7 , 0 , 0 , 6 , 9.66667
	1   , 129 , 0 , 1 , 1 , 0 , 6 , 0 , 0 , 7.33333 , 14.666
			 * 
			 * ****/
			logger.debug("  In the same file add ng th arff extentions.... ");
			  FileOutputStream file; 
	          PrintStream out; // declare a print stream object
	          try {
	           // Create a new file output stream
	          file = new FileOutputStream(filename+".arff");
                    
	          
	          logger.error("  writhing this  "+this.toString());
	          
	                  // Connect print stream to the output stream
	                 out = new PrintStream(file);
	                  out.println(" @RELATION  Sketch_Recongizier " );
	                  out.println("");
	                 writeFeatureSet2(out);
	           

	                  
	                 StringBuffer  Classes=new StringBuffer("");
	                  Classes.append(" {" );
	                  for(Iterator iter = types(); iter.hasNext();){
	     	             String type = (String)iter.next();
						
						  Classes.append(type);
						
						 if ( iter.hasNext()){
	               		  Classes.append("," );
	               		  
	               	  }
	               	  else {
	               		  Classes.append("}" );
	               	  }
	               	
						
						
					}
	                   
	                  if ( Classes.equals(new StringBuffer(""))){
	                	  
	                	  Classes.append(" numeric ");
	                  }
	                  
	                  
	                  out.println(" @attribute 'class'  "+Classes.toString());
	                  
	                  
	                  
	                  out.println("");
	                  out.println("@data");
	                 // out.println ( this.NumOfSamples +" "+ (this.NumOfFeatures+1));
	                  double dataw; 
	                //  for (int i = 0; i <getTypeCount(); i++) {
	                	
	                	  for(Iterator iter = types(); iter.hasNext();){
	 	     	             String type = (String)iter.next();
							
	 	     	      	 ArrayList<SVMFeatureSet>[] l = (ArrayList<SVMFeatureSet>[])_map.get(type);
							
						 //poistiove examples 
	 	     	      
	 	     	      	writeFeaturesForExamples(l[0],out,type);
	 	     	    	writeFeaturesForExamples(l[1],out,type);
	                	 
	                	 
						
				//	}
	                  }
	      
	                  
	          }
	          catch (Exception e){
	                  System.err.println ("Error in writing to file");
	                  logger.error(e.getMessage(),e.getCause());
	          }
			
			
			
		}

		private void writeFeatureSet(PrintStream out) {
			//get a dummy type 
			

				Stroke s=new Stroke();
				s.addPoint(new PointData(0,0));
				 SegmentClusterFeatureSet temp=new 	 SegmentClusterFeatureSet ();
				 SegmentCluster sc=new SegmentCluster();
				 sc.addStroke(s);
				// temp.setSegmentCluster();
				temp.setSegmentCluster(sc);
				temp.initAll();

      
               for (int i = 0; i < temp.getFeatures().size(); i++) {
					
            	   
            	   for (int j = 0; j < temp.getFeatures().get(i).Values().length; j++) {
            		   out.println(" @attribute   '"+ temp.getFeatures().get(i).getName()+"'  numeric  ");
            	   }
					
				
             	
             	   
             	  
				}
		}
		private void writeFeatureSet2(PrintStream out) {
			//get a dummy type 
			

			ArrayList<SVMFeatureSet>[]temp =(ArrayList<SVMFeatureSet>[]) _map.values().iterator().next();

              SVMFeatureSet tempFeat=null;
			if (temp[0].size()>0)
            	  tempFeat=temp[0].get(0);
              if (temp[1].size()>0)
            	  tempFeat=temp[1].get(0);
              
              
           
               for (int i = 0; i <       tempFeat.getFeatureCount()   ; i++) {
					
            	  
            		   out.println(" @attribute   '"+ tempFeat.getFeatureName(i)+"'  numeric  ");
      
				
             	
             	   
             	  
				}
		}
		private void writeFeaturesForExamples(ArrayList<SVMFeatureSet> list,
				PrintStream out,String type) {
			 
			if (list.size()==0)
				return;
 
			for(int j=0; j< list.size() ;j++){
	             
             	SVMFeatureSet featSet = list.get(j);
             	
             	for (int k = 0; k < featSet.getFeatureCount(); k++) {
             		double data=featSet.getFeature(k);
             		double dataw=data;
				 if ( Double.isNaN( data)  )
     			  {
     				 dataw= 0.0;
     			  }
     			  else 
     				  dataw= data;
       		  out.print(dataw);	
       		  
       		  
       		  out.print(",");
       		  out.print("  ");
       		  
				}
             	
       	
       	
       		//  }
       	
       
       		  out.print( type);
       	  out.println("");
       	  }
       	 
     
       	  
       	 
       		  
       		  out.flush();
       		  logger.info(" writing samples number of type  "+type);
      
			
			
			
		}
		
}
