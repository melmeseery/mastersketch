package SketchMaster.classifier.svmTrainable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import SketchMaster.Stroke.StrokeData.InkInterface;
import SketchMaster.Stroke.features.SVMFeatureSet;
import SketchMaster.classifier.Classification;
import SketchMaster.classifier.Classifier;
import SketchMaster.classifier.rubine.RubineTrainingSet;
import SketchMaster.system.SystemSettings;
import libsvm.*;
public class SVMClassifier extends Classifier   implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8499387813697892864L;
	private double[] _minFeatureVals;
    private double[] _maxFeatureVals;
	private static transient final Logger logger = Logger.getLogger(SVMClassifier.class);
	private boolean _normalizeScale =true;
	   private 	transient svm_parameter _svmParam=null;
	   private 	transient svm_model _svmModel;
	private int numFeatures;
	//transient SVMFeatureSet  features;
	 transient TrainingSet  trainSet;
	   private HashMap _labelToType = new HashMap();
	  private  String ModelFileName="model.svt";
	private boolean[] FeatureToRemove;
	
	public SVMClassifier() {
		
		super();
		logger.setLevel(Level.INFO);
		  _svmParam = defaultSVMParam();
	}
	private svm_parameter defaultSVMParam() {
	    svm_parameter p ;//= new svm_parameter();
        //default values for now
        p = new svm_parameter();
        p.svm_type = svm_parameter.C_SVC;
       p.kernel_type = svm_parameter.LINEAR;
//        p.degree = 0;
//        p.gamma = 0.0;
//        p.coef0 = 0;
//        p.nu = 0.0;
//        p.cache_size = 1;
//        p.C = 1;
//        p.eps = 0;
//        p.p = 0.0;
//        p.shrinking = 1;
//        p.nr_weight = 0;
        
        p.degree = 13;
        p.gamma =SystemSettings.SVM_GAMMA;//1.499;50.127877237851656//1.51;49.61636828644501//1.45;49.87212276214834//1.5;50.38363171355499 //0.9625;50.127877237851656//0.0462;  46.03580562659847//0.3625;=>47.57// 0.3125; ==>46
        p.C =SystemSettings.SVM_C;//300:47.570332480818415// 200;47.570332480818415 //260; 47.5703324//245==>47.57;//240;==> 46
        p.coef0 = 0;
        p.nu = 0.5;
        p.cache_size = 60;
    
        p.eps = 1e-4;
        p.p = 0.1;
        p.shrinking = 1;
        p.nr_weight = 0;
       
        p.weight_label = null;
        p.weight = null;
        return p;
	}
	   /* Classify the given example using a single-class classifier.
	     */
	    private SVMClassification classifySingle(SVMFeatureSet fs) {
	        if(_normalizeScale){
	          fs = scale(fs);
	        }
	        //set the feature to enter the svm 
	        svm_node[] ex = new svm_node[fs.getFeatureCount()];
	        for(int j=0; j<fs.getFeatureCount(); j++){
	            ex[j]=new svm_node();
	            ex[j].index=j;// the features number j 
	            ex[j].value=fs.getFeature(j);  // value of feature 
	        }
	        // do the predition model and return the values 
	        double val = svm.svm_predict(_svmModel, ex);
	        
	        
	        String[] types = new String[1];
	        double[] confidences = new double[1];
	        //get types 
	        types[0] = (String)_labelToType.values().iterator().next();
	        confidences[0] = val;
	        return new SVMClassification(types,confidences);
	    }
	    
	  /* Scale the given feature vector based on the scale obtained from
	     * the training set.  This method is called by classify to scale
	     * the test example.
	     */

	
	   public boolean isIncremental(){
	        return false;
	    }

	public Classification Classify(InkInterface ink) {
		//TODO :  you could call the svm classify for proof 
		return null;
	}
	
	 public SVMClassification classify(SVMFeatureSet fs)  {
	        if(_svmParam.svm_type == svm_parameter.ONE_CLASS){
	            //  logger.debug("ONE_CLASS");
	            return classifySingle(fs);
	        }
	        else{
	            if(_normalizeScale){
	            	//logger.info("now normalizing ");
	             fs = scale(fs);
	            }
	            
	            int countFeatures=0;
	 	       for (int i = 0; i < FeatureToRemove.length; i++) {
	 			if (!FeatureToRemove[i])
	 				 countFeatures++;
	 		}
	 	       
	 	       
	 	       //    svm_node[] ex = new svm_node[fs.getFeatureCount()];
	 	       int exj=0;
	            svm_node[] ex = new svm_node[countFeatures];
	            for(int j=0; j<fs.getFeatureCount()&& exj<countFeatures; j++){
	            	
	            	if (!FeatureToRemove[j]){
	            
	                ex[exj]=new svm_node();
	                ex[exj].index=exj;
	                ex[exj].value=fs.getFeature(j);
	                exj++;
	            	}
	            }
	         //   logger.info(_svmModel);
	          	 int label = (int)svm.svm_predict(_svmModel, ex);
	          	 
	          	 
	          //	 logger.info(" label is  "+label);

	            String type =(String)_labelToType.get(Integer.valueOf(label));
	            String[] types = {type};
	            double[] values = {1.0};
	            return new SVMClassification(types,values);
	            /*
	            svm.SVMResult result = svm.svm_predict_hh(_svmModel, ex);
	            int[] labels = result.classPrediction();
	            ArrayList pairs = new ArrayList();
	            for(int i=0; i<labels.length; i++){
	                String type =(String)_labelToType.get(new Integer(labels[i]));
	                double[] decFuncValues = result.values(labels[i]);
	                for(int j=0; j<decFuncValues.length; j++){
	                    pairs.add(new Pair(type,decFuncValues[j]));
	                }
	            }
	            String[] types = new String[pairs.size()];
	            double[] values = new double[pairs.size()];
	            int i=0;
	            for(Iterator iter=pairs.iterator(); iter.hasNext();){
	                Pair p = (Pair)iter.next();
	                types[i]=p.type;
	                values[i]=p.value;
	                i++;
	            }
	            return new Classification(types,values);
	             */
	        }
	    }
	
	 
	 public void train(TrainingSet tset, int pnumFeatures){
		 numFeatures =  pnumFeatures;
		 trainSet=tset;
		 train();
	 }
	 @Override
	public void train() {
		
		  logger.info("  info the trainig ");
	        if(_normalizeScale){
	        	logger.info(" scale of  IN TRAIN ");
	            trainSet = scale(trainSet,-1,1);//scale values to be in the range of 0 and 1
	        }
	        ArrayList list = new ArrayList();  //create a new array lis 
	        int label=1; //start adding labels for the set 
	        int featcount=0;
	        
	       FeatureToRemove= trainSet.getUselessFeatures();
	       int countFeatures=0;
	       if (FeatureToRemove!=null)
	       for (int i = 0; i < FeatureToRemove.length; i++) {
			if (!FeatureToRemove[i])
				 countFeatures++;
		}
	       int countS=0;
	        /// get all types and category of the training set 
	        for(Iterator types =  trainSet.types(); types.hasNext();){ 
	            String type = (String)types.next();//for each label 
	            _labelToType.put(Integer.valueOf(label),type); // put the label with its type in a map
	            
	            logger.info("  adding the type "+type + "  with  "+ trainSet.examplesSize(type) );
	          
	      
	            for(Iterator examples = trainSet.positiveExamples(type); examples.hasNext();){
	            	SVMFeatureSet fs = (SVMFeatureSet)examples.next(); // get features for this
	                // now we will have to create nodes (featureindex, feature value )
	            	//svm_node [] ex = new svm_node[fs.getFeatureCount()];
	            	
	             svm_node [] ex = new svm_node[countFeatures];
	             featcount=fs.getFeatureCount();
	             int exj=0;
	             //   svm_node[] ex = new svm_node[fs.getFeatureNoZeroCount()];
	             //   int k=0;
	                for(int j=0; j<fs.getFeatureCount()&&exj<countFeatures; j++){
	                	if (!FeatureToRemove[j]){
	             //   	if (fs.getFeature(j)!=0){
		                    ex[exj]=new svm_node();
		                    ex[exj].index=exj;
		                    
		                    ex[exj].value=fs.getFeature(j);
		                    exj++;
	                	}
//		                    if (Double.isNaN(fs.getFeature(j))){
//		                    	
//		                    	 logger.fatal("[ERROR] feature "+j+" fo the item  =  " +fs.getFeature(j)+" the name = "+fs.getFeatureName(j));
//		                    
//		                    }
		                    
		       //             k++;
	               // 	}
	                }
	                countS++;
	                list.add(new Example(label,ex));// add the example ot list
	            }
	            label++;            
	        }
	        logger.info(" number of samples in trainig. is   "+countS);
	        logger.info( "  NO of features in this Training is = "+featcount);
	        logger.info(  "   number of categories in this train  = "  +_labelToType.size());
	        
	     // create the proble 
	        svm_problem prob = new svm_problem();
	        // for the list of exampel 
	        int num = list.size();
	        //get number of samples 
	        prob.l = num;
	        //create the label array y 
	        double[] y = new double[num];
	        //nnow create the two dimentional array of nodes
	        svm_node[][] x = new svm_node[num][];
	        for(int i=0; i<num; i++){
	            Example ex=(Example)list.get(i);
	            y[i]=ex.label;
	            x[i]=ex.nodes;
	        }
	        prob.y=y;
	        prob.x=x;
	        // get svm paramter 
	        if(_svmParam==null){
	            _svmParam = defaultSVMParam();
	        }
	        logger.info(" The pefore cross validate  ");
	        _svmParam=CrossValidate(prob);
	        logger.info("training started ");
	        // now train model 
	        _svmModel = svm.svm_train(prob,_svmParam);
	        
	         logger.info("training end");
		
		
//		
		
		
	}
	 
	    /**
	     * Scale the given feature vector based on the scale obtained from
	     * the training set.  This method is called by classify to scale
	     * the test example.
	     */
	    public SVMFeatureSet scale( SVMFeatureSet fvals){
	        double[] normVals = new double[fvals.getFeatureCount()];
	        for(int i=0; i<fvals.getFeatureCount(); i++){
	            normVals[i] = (fvals.getFeature(i)-_minFeatureVals[i])/(_maxFeatureVals[i]-_minFeatureVals[i]);
	        }
	        return new SVMFeatureSet(normVals);
	     }

	    
	    /**
	     * Scale the feature values in the training set to be in the
	     * range of [min,max].
	     */
	    public TrainingSet scale(TrainingSet tset, int min, int max){
	        _minFeatureVals = new double[numFeatures];
	        _maxFeatureVals = new double[numFeatures];
	        for(int i=0; i<numFeatures; i++){
	            _minFeatureVals[i]=Double.POSITIVE_INFINITY;
	            _maxFeatureVals[i]=Double.NEGATIVE_INFINITY;
	        }
	        //figure out the minimum and maximum values of each feature
	        for(Iterator types = tset.types(); types.hasNext();){
	            String type = (String)types.next();
	            for(Iterator examples = tset.positiveExamples(type); examples.hasNext();){
	            	SVMFeatureSet fs = (SVMFeatureSet)examples.next();
	                for(int i=0; i<fs.getFeatureCount(); i++){
	                    _minFeatureVals[i]=Math.min(_minFeatureVals[i],fs.getFeature(i));
	                    _maxFeatureVals[i]=Math.max(_maxFeatureVals[i],fs.getFeature(i));
	                }
	            }
	            for(Iterator examples = tset.negativeExamples(type); examples.hasNext();){
	            	SVMFeatureSet fs = (SVMFeatureSet)examples.next();
	                for(int i=0; i<fs.getFeatureCount(); i++){
	                    _minFeatureVals[i]=Math.min(_minFeatureVals[i],fs.getFeature(i));
	                    _maxFeatureVals[i]=Math.max(_maxFeatureVals[i],fs.getFeature(i));
	                }
	            }
	        }
	        //scale feature values to be in the range of [0,1]
	        TrainingSet normalizedSet = new TrainingSet();
	        for(Iterator types = tset.types(); types.hasNext();){
	            String type = (String)types.next();
	            for(Iterator examples = tset.positiveExamples(type); examples.hasNext();){
	            	SVMFeatureSet fs = (SVMFeatureSet)examples.next();
	                double[] vals = new double[numFeatures];
	                for(int i=0; i<fs.getFeatureCount(); i++){
	                    vals[i] = (fs.getFeature(i)-_minFeatureVals[i])/(_maxFeatureVals[i]-_minFeatureVals[i]);
	                }
	                normalizedSet.addPositiveExample(type, new SVMFeatureSet(vals));
	            }
	            for(Iterator examples = tset.negativeExamples(type); examples.hasNext();){
	            	SVMFeatureSet fs = (SVMFeatureSet)examples.next();
	                double[] vals = new double[numFeatures];
	                for(int i=0; i<fs.getFeatureCount(); i++){
	                    vals[i] = (fs.getFeature(i)-_minFeatureVals[i])/(_maxFeatureVals[i]-_minFeatureVals[i]);
	                }
	                normalizedSet.addNegativeExample(type, new  SVMFeatureSet(vals));
	            }
	        }
	        return normalizedSet;
	    }

	 public   svm_parameter CrossValidate(svm_problem prob){
		 
	 svm_parameter svmParam=defaultSVMParam();
	 int gridtime=3;
	 int loopTimes=3;
	 // now i need to change generate list of c  
	double  cStart,cEnd,cStep;
	double gStart,gEnd,gStep=0;
	 
	double max=Double.MIN_VALUE,maxg=0,maxc=0;
//	 cStart=-100;
//	  cEnd=1000;
	
	  
	  svmParam.C=SystemSettings.SVM_C;
		svmParam.gamma=SystemSettings.SVM_GAMMA;
		
		if (SystemSettings.CROSS_VALIDATE){
			
			
			
		
	/*
	  svmParam.C=136.32;
		svmParam.gamma=0.49199;
	 */
			//double factor =Math.random();
		
		 cStart=svmParam.C/(100.0*Math.random());
		 cEnd=svmParam.C*(100.0*Math.random());
//		  gStart=-10;
//			 gEnd=10;
		  gStart=svmParam.gamma/(10.0*Math.random());
		  gEnd=svmParam.gamma*10.0*Math.random();
	
		 double accurracy =RunCrossValidateJob(prob,  svmParam);

			 logger.info("  Default  c = "+svmParam.C+" g ="+svmParam.gamma +  "  with accuraray   "+accurracy*100);
			 max=accurracy;
			 maxc=svmParam.C;
			 maxg=svmParam.gamma;

			 cStep=(double)(cEnd-cStart)/ (double)loopTimes;
			  gStep=(double)(gEnd-gStart)/ (double)loopTimes;
//			  cStart= maxc -cStep;
//				cEnd=maxc+cStep;
//				 
//				gStart= maxg -gStep;
//				gEnd=maxg+gStep;
				 
			 
			 
		 boolean maxchanged=false;
		 

	 for (int i = 0; i <  gridtime; i++) {
		 
		 /// ------------------------
		 
//		 cStart=10;
//		  cEnd=1000;
		 cStep=(double)(cEnd-cStart)/ (double)loopTimes;
		 
		for (double c=cStart; c < cEnd; c+=cStep) {
//			if (c==136)
//				c=140;
	 //logger.error( "zzzzzzzzzzzz"+c);
//			 gStart=10;
//			 gEnd=1000;
			
			if (svmParam.kernel_type==svmParam.LINEAR){
				svmParam.C=c+(Math.random()*10.0);
				 accurracy =RunCrossValidateJob(prob,  svmParam);
					
					//  logger.error( "---- i = " + i + " c = "+c+"  g= "+g+"   ACu"+ accurracy);
					 if (accurracy>max)
					 {
						 maxchanged=true;
						 logger.info( "**************"+accurracy*100+"  ********************now i have the changed into  c = "+c+  "  with accuraray   "+accurracy*100+"  in loop "+i);
						 max=accurracy;
						 maxc=c;
					 
					 }
				
			}else{
			 gStep=(double)(gEnd-gStart)/ (double)loopTimes;
			for (double g = gStart; g < gEnd; g+=gStep) {
				
	            			//now try to get the cross validation 
		        
				svmParam.C=c+(Math.random()*10.0);
				svmParam.gamma=g+(Math.random());
//				if (c==136 && g==-10.5)
//					continue;
				 accurracy =RunCrossValidateJob(prob,  svmParam);
				//  logger.error( "---- i = " + i + " c = "+c+"  g= "+g+"   ACu"+ accurracy);
				 if (accurracy>max)
				 {
					 maxchanged=true;
					 logger.info( "**************"+accurracy*100+"  ********************now i have the changed into  c = "+c+" g ="+g +  "  with accuraray   "+accurracy*100+"  in loop "+i);
					 max=accurracy;
					 maxc=c;
					 maxg=g;
				 }
					
			}//for the g
			}// if linear 
			
		}// for the c 
//		 if (i>0){
//		if (maxchanged==false)
//		{
//			break;
//		}
//		else {
//			maxchanged=false;
//		}
//		 }
		 // i need to change start and end of c and g to start a new loop 
		
		cStart= maxc - cStep;
//		if (cStart==136)
//			cStart--;
		cEnd=maxc+cStep;
		 
		
		gStart= maxg -gStep;
		gEnd=maxg+gStep;
		 
		 
	}
	 
// now i have the maximum 
	//	svmParam= defaultSVMParam();
		svmParam.C=maxc;
		svmParam.gamma=maxg;
		 logger.info(" After cross refrence better is  c = "+maxc+" g ="+maxg + " with accuracy "+max*100 );
	       // logger.error( "accuracy   = "+ accuracy *100);
		}
	        return svmParam;
	      //  svm
	       //  logger.trace("training started ");
	        // now train model 
	 } 
	 
	 public    double RunCrossValidateJob(svm_problem prob ,  svm_parameter svmParam){
		 

	        
	        double[] result=new double [prob.l];
	        // now i will runt the grid 
	        // for each  loop value on  c  from 10 till 1000  with increment of 100 
	        //Figure 2: Loose grid search on C = 2−5 , 2−3 , . . . , 215 
	        //and γ = 2−15 , 2−13 , . . . , 23 .
	        // now test wich values is maximum and 
	         /// 
	        //logger.error( svmParam);
	        
	        double accuracy=0;
	        
	        
	        svm.svm_cross_validation(prob,svmParam, 10, result);
	        for (int i = 0; i < result.length; i++) {
	           if (prob.y[i]==result[i])
	        	   accuracy++; 
				//logger.error( "subosed to be "+	prob.y[i]+ " target  "+i+"  =  "+ result[i]);
			}
	        accuracy/=(double)prob.y.length;
	        return accuracy;
		 
	 }
	 
	   /* An internal data structure representing an example.
	     */
	    private static class Example{
	        public int label;
	        public svm_node[] nodes;
	        public Example(int l, svm_node[] n){
	            label=l;
	            nodes=n;
	        }

	        public String toString(){
	            StringBuffer sb = new StringBuffer();
	            sb.append(label+" ");
	            for(int i=0; i<nodes.length; i++){
	                sb.append(nodes[i].index+":"+nodes[i].value+" ");
	            }
	            return sb.toString();
	        }
	    }
	    private void writeObject(ObjectOutputStream os) throws IOException {
			try {
				
				logger.info( "labels size is "+ this._labelToType.size());
				logger.info(" normilized  "+this._normalizeScale);
				logger.info( "  minFeatureVals "+this._minFeatureVals.length);
				logger.info(this.toString());
				
				os.defaultWriteObject();
				if (_svmParam==null)
					_svmParam=this.defaultSVMParam();
				// now write the paramter 
				os.writeInt( _svmParam.svm_type);
			    os.writeInt( _svmParam.kernel_type); 
			    os.writeInt(_svmParam.degree);
		        os.writeDouble(_svmParam.gamma );
		        os.writeDouble(_svmParam.coef0 );//;= 0;
		        os.writeDouble(_svmParam.nu);// = 0.5;
		        os.writeDouble(_svmParam.cache_size);// = 40;
		        os.writeDouble(_svmParam.C );//= 100;
		        os.writeDouble(_svmParam.eps );//= 1e-3;
		        os.writeDouble(_svmParam.p );// 0.1;
		        os.writeInt(_svmParam.shrinking); //= 1;
		        os.writeInt(_svmParam.nr_weight);// = 0;
		        os.writeObject( _svmParam.weight_label);
		        os.writeObject( _svmParam.weight );
		        logger.info(" wrtitng the  "+_svmParam);
		        logger.info(" svm type   "+_svmParam.svm_type);
		        logger.info("  svm C  "+_svmParam.C);
		        logger.info(" SVMC has code  "+ _svmParam.hashCode());
		        
		        logger.info("  this is has code "+this.hashCode());
		        //write the trained model 
		        svm.svm_save_model(ModelFileName, _svmModel);
		      logger.info(" has code of svm "+ _svmModel.hashCode());
		      logger.info(" wrting th model file name "+ModelFileName);
			// now write the svm parameter ;
				
//				os.writeBoolean(this.endPoint);
//				os.writeBoolean( this.startPoint);
//				os.writeBoolean(this.controlPoint);
//				os.writeLong(this.time);
//				os.writeDouble(this.x);
//				os.writeDouble(this.y);
//				os.writeDouble(this.presureValue);
//				os.writeDouble(this.CompulativeLength);
			//	private double[] _minFeatureVals;
			 //   private double[] _maxFeatureVals;
			//	private static final Logger logger = Logger.getLogger(SVMClassifier.class);
			//	private boolean _normalizeScale = true;
//				   private svm_parameter _svmParam=null;
//				   private svm_model _svmModel;
//				private int numFeatures;
				//SVMFeatureSet features;
				// TrainingSet  trainSet;
			
				
			
			} catch (Exception e) {
				logger.error("writeObject(ObjectOutputStream)", e); //$NON-NLS-1$
			}
		}
	 
		private void readObject(ObjectInputStream is)  {
		//	try {
				try {
					is.defaultReadObject();
				
				logger.info( "labels size is "+ this._labelToType.size());
				logger.info(" normilized  "+this._normalizeScale);
				logger.info( "  minFeatureVals "+this._minFeatureVals.length);
				logger.info(this.toString());
				_svmParam=new svm_parameter();
				

				_svmParam.svm_type = is.readInt();
				_svmParam.kernel_type = is.readInt();
				_svmParam.degree = is.readInt();
				_svmParam.gamma =  is.readDouble();
				_svmParam.coef0 =  is.readDouble();
				_svmParam.nu =  is.readDouble();
				_svmParam.cache_size =  is.readDouble();
				_svmParam.C =  is.readDouble();
				_svmParam.eps =  is.readDouble();
				_svmParam.p =  is.readDouble();
				_svmParam.shrinking= is.readInt();
				_svmParam.nr_weight =  is.readInt();
				_svmParam.weight_label =  (int[]) is.readObject();
				_svmParam.weight =  (double[]) is.readObject();
				
				 logger.info(_svmParam);
				  logger.info(" SVMC has code  "+ _svmParam.hashCode());
			        logger.info(" svm type   "+_svmParam.svm_type);
			        logger.info("  svm C  "+_svmParam.C);
		        //write the trained model 
			        _svmModel=    svm.svm_load_model(ModelFileName);//(ModelFileName, _svmModel);
				      logger.info(" has code of svm "+ _svmModel.hashCode());
				      logger.info(" reading  th model file name "+ModelFileName);
				      logger.info("  this is has code "+this.hashCode());
//				PointData point=new PointData();
//				point.endPoint=is.readBoolean();
//				point.startPoint=is.readBoolean( );
//				point.controlPoint=is.readBoolean();
//				point.time=is.readLong();
//				this.x=is.readDouble();
//				this.y=is.readDouble();
				
//				point.presureValue=is.readDouble();
//				point.CompulativeLength=is.readDouble();
		//	}
//			} catch{
//			//	logger.error("readObject(ObjectInputStream)", e); //$NON-NLS-1$
//				
//				
//			}
				      
				} catch (IOException e) {
					 
					e.printStackTrace();
					logger.error("readObject(ObjectInputStream)", e); //$NON-NLS-1$
				} catch (ClassNotFoundException e) {
					logger.error("readObject(ObjectInputStream)", e); //$NON-NLS-1$
					e.printStackTrace();
				}
		}
		public String getModelFileName() {
			return ModelFileName;
		}
		public void setModelFileName(String modelFileName) {
			ModelFileName = modelFileName;
		}
		public void setTrainSet(TrainingSet trainSet) {
			this.trainSet = trainSet;
		}
	public HashMap getLabels() {
			
			return _labelToType;
		}
		
}
