package SketchMaster.classifier.Symbol;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import JSci.maths.matrices.DoubleSquareMatrix;
import SketchMaster.Stroke.StrokeData.InkInterface;
import SketchMaster.Stroke.StrokeData.SegmentCluster;
import SketchMaster.Stroke.features.InkFeatureSet;
import SketchMaster.Stroke.features.SegmentClusterFeature;
import SketchMaster.Stroke.features.SegmentClusterFeatureSet;
import SketchMaster.Stroke.features.StrokeRubineFeatureSet;
import SketchMaster.Stroke.features.StrokeStatisticalFeatures;
import SketchMaster.classifier.Category;
import SketchMaster.classifier.rubine.RubineCategory;
import SketchMaster.io.log.FileLog;
import SketchMaster.system.SystemSettings;

public class SymbolCategory extends RubineCategory implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8901636817539474407L;

	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(SymbolCategory.class);

	private static final double ZERO =SystemSettings.ZERO;
	// private ArrayList<SegmentClusterFeatureSet> inksFeatures;

	
	private transient double[]  FC_Values=null;
	private transient boolean FC_Values_OK=false;
	@Override
	public void init() {
//		if (logger.isDebugEnabled()) {
//			//  logger.debug("init() - start"); //$NON-NLS-1$
//		}

		FC = new java.util.HashMap<String, Double>();
		inksFeatures = new ArrayList<InkFeatureSet>();

//		if (logger.isDebugEnabled()) {
//			//  logger.debug("init() - end"); //$NON-NLS-1$
//		}
	}

	public void addExample(SegmentCluster cluster) {
//		if (logger.isDebugEnabled()) {
//			//  logger.debug("addExample(SegmentCluster) - start"); //$NON-NLS-1$
//		}

		SegmentClusterFeatureSet example = new SegmentClusterFeatureSet();
		example.setSegmentCluster(cluster);
		example.initAll();
		example.computeFeatures();
		inksFeatures.add(example);
		if (FeaturesCount == 0)// now yet initalized
			FeaturesCount =FeaturesCounts(example);
		covOk = false;
		valuesOk = false;

//		if (logger.isDebugEnabled()) {
//			//  logger.debug("addExample(SegmentCluster) - end"); //$NON-NLS-1$
//		}
	}

	@Override
	protected void ComputeFeatures() {
//		if (logger.isDebugEnabled()) {
//			//  logger.debug("ComputeFeatures() - start"); //$NON-NLS-1$
//		}

		valuesOk = false;
		covOk = false;
		int E;
		int countF = 0;

		if (FC == null)
			FC = new HashMap<String, Double>();

		if (inksFeatures != null) {
			// total size of the examples.
			E = inksFeatures.size();
//			//  logger.debug(" L("  
//					+ (new Throwable()).getStackTrace()[0].getLineNumber() + ")"+" - "
//					+"  E =inksFeatures.size()= "+E);
			// temp valuable
			double[] Fci = null;
			String[] FeatureName = null;
			//int FeatureCount = 0;
			SegmentClusterFeatureSet example;
			// loop on each example. of the category. (a gesture)
			for (int e = 0; e < inksFeatures.size(); e++) {
//				//  logger.debug(" L("  
//						+ (new Throwable()).getStackTrace()[0].getLineNumber() + ")"+" - "
//						+"  e == "+e);
				// get the current example
				example = (SegmentClusterFeatureSet) inksFeatures.get(e);
				// compute all the features
				example.computeFeatures();
				// to get the count of the feature array i want to create of
				// number
				// FeatureCount = example.getFeatures().size();
				this.FeaturesCount = FeaturesCounts(example);

				if (e == 0) // first example calculated
				{
					// now create the array of features
					Fci = new double[FeaturesCount];
					// temp variable
					FeatureName = new String[FeaturesCount];
					int index = 0;
					// init all element of the array with zero to be used in
					// summation in calcuation
					countF = example.getFeatures().size();
					for (int f = 0; f < countF; f++) {
			
			
					//	if (example.getFeatures().get(f).NoOfValues() > 1) {
							if (example.getFeatures().get(f).isValueOk()) {
								double[] valuesTemp = example.getFeatures()
										.get(f).getValues();
								for (int h = 0; h < example.getFeatures().get(f).Values().length ; h++) {

									Fci[index + h] = valuesTemp[h];
									FeatureName[index + h] = example.getFeatures().get(f).getName()+" "+ h;
								}
							}
							index += example.getFeatures().get(f).Values().length;

						//} 
						
//						else {
//							if (example.getFeatures().get(f).isValueOk()) {
//
//								Fci[index] = example.getFeatures().get(f)
//										.getValue();
//							} else {
//								Fci[index] = 0.0;
//							}
//							FeatureName[index] = example.getFeatures().get(f)
//									.getName();
//							index++;
//						}

					}
					
//					for (int i = 0; i < Fci.length; i++) {
//						if (Double.isNaN(Fci[i])){
//							
//							//  logger.debug("  MMMMMMMMMMMMMMMMMMMmm   "+i+"   = "+Fci[i]+"    name "+FeatureName[i]);
//							
//						}
//					}
					
					
				} else {
					countF = example.getFeatures().size();
					int ind = 0;
					for (int i = 0; i < countF; i++) {
						if (Fci != null) {
			//				if (example.getFeatures().get(i).NoOfValues() > 1) {
								
								if (example.getFeatures().get(i).isValueOk()) {
									String fname=example.getFeatures().get(i).getName();
									double[] valuesTemp = example.getFeatures()
											.get(i).getValues();
									for (int h = 0; h < example.getFeatures().get(i).Values().length ; h++) {

										Fci[ind + h] += valuesTemp[h];
										;
									}// for 
								}// if values isok  
								ind += example.getFeatures().get(i).Values().length;

			//				}// if multivalues 
//							else {
//								if (example.getFeatures().get(i).isValueOk()) {
//									Fci[ind] += example.getFeatures().get(i)
//											.getValue();
//								}
//								ind++;
//
//							}//else
						} // if fci not null
					}  // for feature count 
				}// else not first example 
//				for (int i = 0; i < Fci.length; i++) {
//					if (Double.isNaN(Fci[i])){
//						
//						//  logger.debug("  NNNNNNNNNNNNNN   "+i+"   = "+Fci[i]+"    name "+FeatureName[i]);
//						
//					}
//				}
			}// for all example 

			// / now after computing all the features i need to add it to the
			// map
			for (int i = 0; i < FeaturesCount; i++) {
				Fci[i] = Fci[i] * (1.0 / (double) E);
				FC.put(FeatureName[i], Fci[i]);
			}
			valuesOk = true;

		}


	}

	
	public int getFeaturesCount() {

		
		int temp= super.getFeaturesCount();
		if (temp==0)
		{
			SegmentClusterFeatureSet example=null;
			if (inksFeatures!=null&&inksFeatures.size()>0){
				example =	(SegmentClusterFeatureSet) this.inksFeatures.get(0);
				example.initAll();
			}
			else {
				 example = new SegmentClusterFeatureSet();
				example.setSegmentCluster(new SegmentCluster());
				example.initAll();
				
			}
			FeaturesCount=FeaturesCounts(example);
			return FeaturesCount;
		}
		else 
		{
			FeaturesCount=temp;

			return FeaturesCount;
			}
	}

	private int FeaturesCounts(SegmentClusterFeatureSet example) {

	
		int c = 0;

		// example.getFeatures()
		for (int i = 0; i < example.getFeatures().size(); i++) {
			c += example.getFeatures().get(i).NoOfValues();
		}
              this.FeaturesCount=c;

//		if (logger.isDebugEnabled()) {
//			//  logger.debug("FeaturesCounts(SegmentClusterFeatureSet) - end"); //$NON-NLS-1$
//		}
		return c;
	}

//	 protected double[] getFeatureValues(SegmentClusterFeature feature){
//		 
//		 
//			for (int i = 0; i <  FCount ; i++) {
//				
//				
//				logger.info("-%%%%%------------------------------------------compute multi value of coverance ");
//				fi = example.getFeatures().get(i).getValue();
//				fci = FC.get(example.getFeatures().get(i).getName());  // central of this features
//				
//				
//				for (int j = 0; j < FCount; j++) {
//					// fi,fj
//					fj = example.getFeatures().get(j).getValue();
//				String	name=example.getFeatures().get(j).getName();
//				
//				
//					logger.info(name + "   value is  "+ fj + "  and i am in loop "+j +" ("
//							+ this.getClass().getSimpleName()
//							+ "    "
//							+ (new Throwable()).getStackTrace()[0]
//									.getLineNumber() + "  )  ");
//					
//					
//					System.out.flush();
//					
//				         try {
//						fcj = FC.get(name);
//						Ecij[i][j] += (fi - fci) * (fj - fcj);
//				         }
//				         catch (Exception ex ){
//				        	 System.err.println("  i am in the exception of  "+j+"  "+ex.getMessage()+"   ("
//									+ this.getClass().getSimpleName()
//									+ "    "
//									+ (new Throwable()).getStackTrace()[0]
//											.getLineNumber() + "  )  ");
//				        	 System.err.flush();
//				        	// ex.printStackTrace();
//				         }
//			
//				}
//			}
//		 return  null;
//	 }
	protected double[]  getFeaturesArray(SegmentClusterFeatureSet example){


		 int FCount=example.getFeatures().size();
		 double[] values=new double[this.FeaturesCount];
		 for (int i = 0; i < values.length; i++) {
			values[i]=0;
		}
        int ind=0;
		for (int i = 0; i <  FCount ; i++) {
			
			SegmentClusterFeature feat = example.getFeatures().get(i);
					
			// check if this feature is multivalue 
		//	if (feat.NoOfValues()>1){
				// yes this featues is more than one value
			      for (int j = 0; j < feat.getValues().length; j++) {
			    	  values[ind+j] =feat.getValues()[j];
			    	  
				}	
			      ind+=feat.getValues().length;
				
		//	}
//			else{
//					values[ind] =feat.getValue();
//	                   ind++;
//			}
			
			
		}

		return values;
	}
	protected double[]  getCentralsArray(SegmentClusterFeatureSet example){


		 int FCount=example.getFeatures().size();
		 double[] values=new double[this.FeaturesCount];
		 String fname="";
		 for (int i = 0; i < values.length; i++) {
			values[i]=0;
		}
        int ind=0;

		for (int i = 0; i <  FCount ; i++) {
			
			SegmentClusterFeature feat = example.getFeatures().get(i);
			
			fname = feat.getName();
				
			// check if this feature is multivalue 
		//	if (feat.getValues().length>1){
				// get function name 
			
				
				// yes this featues is more than one value
			      for (int j = 0; j < feat.getValues().length; j++) {
					
			    	  
			    	  if (Double.isNaN( FC.get(fname+" "+j) ))
			    	   logger.error("  VVVVVVVVVVVv "+ ( fname+" "+j  )+"  == "+ FC.get(fname+" "+j)   );
			    	  else   values[ind+j] =FC.get(fname+" "+j);
			    	  
				}	
			      ind+=feat.NoOfValues();
				
		//	}
//			else{
//			 	  if (Double.isNaN(FC.get(fname)))
//				  //  logger.debug(" L(" + (new Throwable()).getStackTrace()[0].getLineNumber() + ")"+" - "	+"  iiiiiiiiiiiiiiiiiiiiiiiiii   "+   FC.get(fname)   );
//					values[ind] =FC.get(fname);
//	                   ind++;
//			}
			
			
		}


		return values;
	}
	@Override
	protected void computeCoverianceMatrix() {
//		if (logger.isDebugEnabled()) {
//			//  logger.debug("computeCoverianceMatrix() - start"); //$NON-NLS-1$
//		}

		//  logger.trace("----------------------- catgory  coveriance matrix ----------------");
		int FCount=0 ;
		covOk = false;
		if (!valuesOk)
			ComputeFeatures();
		////  logger.trace("Compute the coveriance of category " + CategoryName);
		// counts of example
		//int E;
		// now get count of features
		

		if (inksFeatures != null) {

			// total size of the examples.
			//E = inksFeatures.size();

			double[][] Ecij = null;
			// temp valuable
//			double[] Fci = null;
//			String[] FeatureName = null;
//			double fi, fj, fci, fcj;
			SegmentClusterFeatureSet example;
			
			// loop on each example. of the category. (a gesture)
			
			double[] fciArray =null;
			double[] fcjArray = null;
			
			for (int e = 0; e < inksFeatures.size(); e++) {
				////  logger.trace("will compute  " + e + "  Example from   "+ E + "   examples ");
				// get the current example
				example = (SegmentClusterFeatureSet) inksFeatures.get(e);
				// compute all the features
				example.computeFeatures();
				if (e == 0) {
					// to get the count of the feature array i want to create of
					// number
				 FCount = example.getFeatures().size();
					// now create the array of features
					Ecij = new double[FeaturesCount][FeaturesCount];
					 fciArray = getCentralsArray(example );
					 fcjArray = getCentralsArray(example);
					// init all element of the array with zero to be used in
					// summation in calcuation
					for (int f = 0; f < FeaturesCount; f++) {
						for (int l = 0; l < FeaturesCount; l++) {
							Ecij[f][l] = 0.0;
						}

					}
				}
			
				// double ECij=0.0;
			
				// now i am goint to calcuate teh ecij 
		
				// print this to go on tracing the nan error in coveriancee matrix. 
				double[] fiArray = getFeaturesArray(example);
			
				double[] fjArray = getFeaturesArray(example);
		
//				for (int i = 0; i < fjArray.length; i++) {
//					  if (Double.isNaN(fiArray[i])||Double.isNaN(fciArray[i]))
//						  //  logger.trace("  ffffffffffffffff   i = " + i+"   fiArray  "+fiArray[i]+"    central array is   "+fciArray[i]);
//				}
				
				for (int i = 0; i <fiArray.length; i++) {
					for (int j = 0; j < fjArray.length; j++) {
						 
						if (fcjArray[j]==0||fciArray[i]==0){
							if (fcjArray[j]==0)
							Ecij[i][j] += (fiArray[i] - fciArray[i]) * (ZERO - fcjArray[j]);
							if (fciArray[i]==0)
							{
								Ecij[i][j] += (ZERO  - fciArray[i]) * (fjArray[j] - fcjArray[j]);
							}
						}
						else
							Ecij[i][j] += (fiArray[i] - fciArray[i]) * (fjArray[j] - fcjArray[j]);
						
						
//						  if (Double.isNaN(Ecij[i][j]))
//								  //  logger.debug("   NANANANANANANNANANANAANANANANNANANA   "+  Ecij[i][j]);
						
					}
				}
				
				
					
//					logger.info("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
//					System.out.flush();
//					System.err.flush();
			}

			this.CovaricnceMatrix = new DoubleSquareMatrix(Ecij);
			////  logger.trace(this.CovaricnceMatrix.toString());
			covOk = true;
		}

//		if (logger.isDebugEnabled()) {
//			//  logger.debug("computeCoverianceMatrix() - end"); //$NON-NLS-1$
//		}
	}

//	@Override
//	public double getAverageFeatures(int i) {
//		String FeatureName = "";
//		// / get the featurs name
//		if (inksFeatures != null) {
//			ArrayList<SegmentClusterFeature> featurs = inksFeatures.get(0)
//					.getFeatures();
//
//			FeatureName = featurs.get(i).getName();
//		}
//
//		if (valuesOk) {
//			if (FC != null)
//				return FC.get(FeatureName);
//		} else {
//			ComputeFeatures();
//			return FC.get(FeatureName);
//		}
//
//		return 0.0;
//
//	}
	private void getFC_Values( ){
//		if (logger.isDebugEnabled()) {
//			//  logger.debug("getFC_Values() - start"); //$NON-NLS-1$
//		}
		
		if (!valuesOk) {
		
			ComputeFeatures();
		
		}
		
			FC_Values=new double [this.FeaturesCount];
			int ind=0;
			inksFeatures.get(0).initAll();
			ArrayList<SegmentClusterFeature> feat= inksFeatures.get(0).getFeatures();
			for (int i = 0; i <feat.size(); i++) {
				String fname=feat.get(i).getName();
				//if (feat.get(i).geg)
				//{
					for (int j = 0; j < feat.get(i).getValues().length; j++) {
							
						FC_Values[ind+j]=FC.get(fname+" "+j);
					
					}
				
					
					//  logger.trace(" L("+ (new Throwable()).getStackTrace()[0]	.getLineNumber() + ")" + " - " +ind+ " FC.get(fname)  "+ fname+"  to "+(ind+feat.get(i).NoOfValues()));
					ind+=feat.get(i).NoOfValues();
				//}
//				else {
//				
//					
//					try {
//						//  logger.trace(" L("	+ (new Throwable()).getStackTrace()[0].getLineNumber() + ")" + " - " +ind+ " FC.get(fname)  "+ fname);
//						FC_Values[ind]=FC.get(fname);
//						ind++;
//					} catch (Exception e) {
//						
//						logger.error(" L("
//								+ (new Throwable()).getStackTrace()[0]
//										.getLineNumber() + ")" + " - " +"searcing for "+fname+ "  FC is"+FC,
//								e);
//						//e.printStackTrace();
//					}
//				}
			}
			
			
		

	
		
		FC_Values_OK=true;

//		if (logger.isDebugEnabled()) {
//			//  logger.debug("getFC_Values() - end"); //$NON-NLS-1$
//		}
	}
	
	public double getAverageFeatures(int i) {
//		if (logger.isDebugEnabled()) {
//			//  logger.debug("getAverageFeatures(int) - start"); //$NON-NLS-1$
//		}

if (FC_Values_OK){
			double returndouble = FC_Values[i];
//			if (logger.isDebugEnabled()) {
//				//  logger.debug("getAverageFeatures(int) - end"); //$NON-NLS-1$
//			}
	          return returndouble;		
		}
		else {
			getFC_Values();
			double returndouble = FC_Values[i];
//			if (logger.isDebugEnabled()) {
//				//  logger.debug("getAverageFeatures(int) - end"); //$NON-NLS-1$
//			}
			return returndouble;		
		}
		//String FeatureName = "";
		// / get the featurs name
//		if (inksFeatures != null) {
//			ArrayList<StrokeStatisticalFeatures> featurs = inksFeatures.get(0)
//					.getFeatures();
//
//			FeatureName = featurs.get(i).getName();
//		}

	

	}

	public int compareTo(Category o) throws ClassCastException {
//		if (logger.isDebugEnabled()) {
//			//  logger.debug("compareTo(Category) - start"); //$NON-NLS-1$
//		}

		if (!(o instanceof SymbolCategory))
			throw new ClassCastException("Symbol category  expected");
		SymbolCategory temp = ((SymbolCategory) o);

		int returnint = this.categoryName.compareTo(temp.categoryName);
//		if (logger.isDebugEnabled()) {
//			//  logger.debug("compareTo(Category) - end"); //$NON-NLS-1$
//		}
		return returnint;

	}

}
