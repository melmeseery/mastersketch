/**
 * 
 */
package SketchMaster.classifier.rubine;

import org.apache.log4j.Logger;

import JSci.maths.matrices.DoubleSquareMatrix;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import JSci.maths.matrices.DoubleMatrix;
import SketchMaster.Stroke.StrokeData.InkInterface;
import SketchMaster.Stroke.features.InkFeatureSet;
import SketchMaster.Stroke.features.StrokeRubineFeatureSet;
import SketchMaster.Stroke.features.StrokeStatisticalFeatures;
import SketchMaster.classifier.Category;
import SketchMaster.io.log.FileLog;

/**
 * @author maha
 * 
 */
public class RubineCategory extends Category implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6300822404766696586L;

	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(RubineCategory.class);

	// i need a coveraicne matirx
	protected transient DoubleSquareMatrix CovaricnceMatrix = null;
	protected static int FeaturesCount =0;
	protected boolean covOk = false;
	protected double[] W = null;
	protected double wi0 = 0.0;// intial weith of class
	protected double[][] CovrMat;
	/**
	 * 
	 */

	protected boolean valuesOk = false;

	protected java.util.HashMap<String, Double> FC;
	


	protected ArrayList<InkFeatureSet> inksFeatures;

	// this will contain maiy features

	//
	public RubineCategory() {

	}

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

	public void addExample(InkInterface ink) {
//		if (logger.isDebugEnabled()) {
//			//  logger.debug("addExample(InkInterface) - start"); //$NON-NLS-1$
//		}

		StrokeRubineFeatureSet example = new StrokeRubineFeatureSet(ink);
		example.initAll();
		example.computeFeatures();
		inksFeatures.add(example);
		if (FeaturesCount == 0)// now yet initalized
			FeaturesCount = example.getFeatures().size();
		covOk = false;
		valuesOk = false;

//		if (logger.isDebugEnabled()) {
//			//  logger.debug("addExample(InkInterface) - end"); //$NON-NLS-1$
//		}
	}

	public void removeExample(int i) {
//		if (logger.isDebugEnabled()) {
//			//  logger.debug("removeExample(int) - start"); //$NON-NLS-1$
//		}

		covOk = false;
		valuesOk = false;
		if (inksFeatures != null)
			inksFeatures.remove(i);

//		if (logger.isDebugEnabled()) {
//			//  logger.debug("removeExample(int) - end"); //$NON-NLS-1$
//		}
	}

	public ArrayList<Double> getAverageFeaturesValues() {
//		if (logger.isDebugEnabled()) {
//			//  logger.debug("getAverageFeaturesValues() - start"); //$NON-NLS-1$
//		}

		// new

		if (valuesOk)
			return (ArrayList<Double>) FC.values();
		else {
			ComputeFeatures();
			ArrayList<Double> returnArrayList = (ArrayList<Double>) FC.values();
//			if (logger.isDebugEnabled()) {
//				//  logger.debug("getAverageFeaturesValues() - end"); //$NON-NLS-1$
//			}
			return returnArrayList;
		}
		// return null;
	}

	public double getAverageFeatures(String FeatureName) {
//		if (logger.isDebugEnabled()) {
//			//  logger.debug("getAverageFeatures(String) - start"); //$NON-NLS-1$
//		}

		if (valuesOk) {
			if (FC != null)
				return FC.get(FeatureName);
		} else {
			ComputeFeatures();
			double returndouble = FC.get(FeatureName);
//			if (logger.isDebugEnabled()) {
//				//  logger.debug("getAverageFeatures(String) - end"); //$NON-NLS-1$
//			}
			return returndouble;
		}

//		if (logger.isDebugEnabled()) {
//			//  logger.debug("getAverageFeatures(String) - end"); //$NON-NLS-1$
//		}
		return 0.0;

	}

	protected void ComputeFeatures() {


		valuesOk = false;
		covOk = false;
		int E;

		if (FC == null)
			FC = new HashMap<String, Double>();

		if (inksFeatures != null) {
			// total size of the examples.
			E = inksFeatures.size();
			// temp valuable
			double[] Fci = null;
			String[] FeatureName = null;
			int FeatureCount = 0;
			StrokeRubineFeatureSet example;
			// loop on each example. of the category. (a gesture)
			for (int e = 0; e < inksFeatures.size(); e++) {

				// get the current example
				example = (StrokeRubineFeatureSet) inksFeatures.get(e);
				// compute all the features
				example.computeFeatures();
				// to get the count of the feature array i want to create of
				// number
				FeatureCount = example.getFeatures().size();

				if (e == 0) // first example calculated
				{

					// now create the array of features
					Fci = new double[FeatureCount];
					// temp variable
					FeatureName = new String[FeatureCount];
					// init all element of the array with zero to be used in
					// summation in calcuation
					for (int f = 0; f < Fci.length; f++) {
						if (example.getFeatures().get(f).isValueOk())
							Fci[f] = example.getFeatures().get(f).getValue();
						else
							Fci[f] = 0.0;
						FeatureName[f] = example.getFeatures().get(f).getName();
					}
				} else {

					for (int i = 0; i < FeatureCount; i++) {
						if (Fci != null)
							if (example.getFeatures().get(i).isValueOk())
								Fci[i] += example.getFeatures().get(i)
										.getValue();
					}
				}

			}

			// / now after computing all the features i need to add it to the
			// map
			for (int i = 0; i < FeatureCount; i++) {
				Fci[i] = Fci[i] * (1.0 / (double) E);
				FC.put(FeatureName[i], Fci[i]);
			}
			valuesOk = true;

		}

//		if (logger.isDebugEnabled()) {
//			//  logger.debug("ComputeFeatures() - end"); //$NON-NLS-1$
//		}
	}

	public java.util.HashMap<String, Double> getFC() {
//		if (logger.isDebugEnabled()) {
//			//  logger.debug("getFC() - start"); //$NON-NLS-1$
//		}

		if (!valuesOk)
			ComputeFeatures();

//		if (logger.isDebugEnabled()) {
//			//  logger.debug("getFC() - end"); //$NON-NLS-1$
//		}
		return FC;
	}

	public void setInksFeatures(ArrayList<InkFeatureSet> inksFeatures) {
		this.inksFeatures = inksFeatures;
	}

	public int getExampleCount() {
//		if (logger.isDebugEnabled()) {
//			//  logger.debug("getExampleCount() - start"); //$NON-NLS-1$
//		}

		if (inksFeatures != null)
			return inksFeatures.size();

//		if (logger.isDebugEnabled()) {
//			//  logger.debug("getExampleCount() - end"); //$NON-NLS-1$
//		}
		return 0;
	}

	protected void computeCoverianceMatrix() {
//		if (logger.isDebugEnabled()) {
//			//  logger.debug("computeCoverianceMatrix() - start"); //$NON-NLS-1$
//		}

		//  logger.trace("----------------------- catgory  coveriance matrix ----------------");

		covOk = false;
		if (!valuesOk)
			ComputeFeatures();
	
		// counts of example
		//int E;
		// now get count of features
		int FeatureCount = 0;
		if (inksFeatures!= null){

			// total size of the examples.
			//E = inksFeatures.size();

			double[][] Ecij = null;
			// temp valuable
			//double[] Fci = null;
			//String[] FeatureName = null;
			double fi, fj, fci, fcj;
			StrokeRubineFeatureSet example;
			// loop on each example. of the category. (a gesture)
			for (int e = 0; e < inksFeatures.size(); e++) {
				//  logger.trace("will compute  " + e + "  Example from   "+ E + "   examples ");
				// get the current example
				example = (StrokeRubineFeatureSet) inksFeatures.get(e);
				// compute all the features
				example.computeFeatures();
				if (e == 0) {
					// to get the count of the feature array i want to create of
					// number
					FeatureCount = example.getFeatures().size();
					// now create the array of features
					Ecij = new double[FeatureCount][FeatureCount];

					// init all element of the array with zero to be used in
					// summation in calcuation
					for (int f = 0; f < FeatureCount; f++) {
						for (int l = 0; l < FeatureCount; l++) {
							Ecij[f][l] = 0.0;
						}

					}
				}
				// double ECij=0.0;
				for (int i = 0; i < FeatureCount; i++) {

					fi = example.getFeatures().get(i).getValue();
					fci = FC.get(example.getFeatures().get(i).getName());
				

					for (int j = 0; j < FeatureCount; j++) {
						// fi,fj
						fj = example.getFeatures().get(j).getValue();
						fcj = FC.get(example.getFeatures().get(j).getName());
						//  logger.trace(" XXXXXXXXXXXXXXXXXX   Category Matrix calcuations  of   "+i+" name =  "+example.getFeatures().get(i).getName()+ " fi - fci  "+(fi - fci)+"   fj - fcj    "+ (fj - fcj));
						Ecij[i][j] += (fi - fci) * (fj - fcj);
					}
				}

			}

			// // total size of the examples.
			// E = inksFeatures.size();
			//			 
			// double[][] Ecij=null ;
			// // temp valuable
			// double[] Fci = null;
			// String[] FeatureName = null;
			// double fi,fj,fci,fcj;
			// StrokeRubineComputations example;
			// // loop on each example. of the category. (a gesture)
			// for (int e = 0; e < inksFeatures.size(); e++) {
			//				
			// // get the current example
			// example = inksFeatures.get(e);
			// // compute all the features
			// example.computeFeatures();
			// if (e==0){
			// // to get the count of the feature array i want to create of
			// // number
			// FeatureCount = example.getFeatures().size();
			// // now create the array of features
			// Ecij = new double[FeatureCount][FeatureCount];
			//		
			// // init all element of the array with zero to be used in
			// // summation in calcuation
			// for (int f = 0; f < FeatureCount; f++) {
			// for (int l = 0; l < FeatureCount; l++) {
			// Ecij[f][l] = 0.0;
			// }
			//							
			// }
			// }
			// //double ECij=0.0;
			// for (int i = 0; i <FeatureCount; i++) {
			//				
			// fi=example.getFeatures().get(i).getValue();
			// fci=FC.get(example.getFeatures().get(i).getName());
			//					
			// for (int j = 0; j < FeatureCount; j++) {
			// //fi,fj
			// fj=example.getFeatures().get(j).getValue();
			// fcj=FC.get(example.getFeatures().get(j).getName());
			// Ecij[i][j]+=(fi-fci)*(fj-fcj);
			// }
			// }

			// }
			// logger.info("Ecij is "+Ecij);
			CovaricnceMatrix = new DoubleSquareMatrix(Ecij);
			//  logger.trace(CovaricnceMatrix.toString());
			covOk = true;
		}

//		if (logger.isDebugEnabled()) {
//			//  logger.debug("computeCoverianceMatrix() - end"); //$NON-NLS-1$
//		}
	}

	/**
	 * @return the covaricnceMatrix
	 */
	public DoubleSquareMatrix getCovaricnceMatrix() {
//		if (logger.isDebugEnabled()) {
//			//  logger.debug("getCovaricnceMatrix() - start"); //$NON-NLS-1$
//		}

		if (!covOk)
			computeCoverianceMatrix();

//		if (logger.isDebugEnabled()) {
//			//  logger.debug("getCovaricnceMatrix() - end"); //$NON-NLS-1$
//		}
		return CovaricnceMatrix;
	}

	/**
	 * @return the featuresCount
	 */
	public int getFeaturesCount() {

		return FeaturesCount;
	}

	public double getAverageFeatures(int i) {
//		if (logger.isDebugEnabled()) {
//			//  logger.debug("getAverageFeatures(int) - start"); //$NON-NLS-1$
//		}

		String FeatureName = "";
		// / get the featurs name
		if (inksFeatures != null) {
			ArrayList<StrokeStatisticalFeatures> featurs = inksFeatures.get(0)
					.getFeatures();

			FeatureName = featurs.get(i).getName();
		}

		if (valuesOk) {
			if (FC != null)
				return FC.get(FeatureName);
		} else {
			ComputeFeatures();
			double returndouble = FC.get(FeatureName);
//			if (logger.isDebugEnabled()) {
//				//  logger.debug("getAverageFeatures(int) - end"); //$NON-NLS-1$
//			}
			return returndouble;
		}

//		if (logger.isDebugEnabled()) {
//			//  logger.debug("getAverageFeatures(int) - end"); //$NON-NLS-1$
//		}
		return 0.0;

	}

	public void setCategoryWeight(double[] w) {
//		if (logger.isDebugEnabled()) {
//			//  logger.debug("setCategoryWeight(double[]) - start"); //$NON-NLS-1$
//		}

		//  logger.trace("Calcuating weight of " + CategoryName);
//		for (int i = 0; i < w.length; i++) {
//			//  logger.trace("wwww"+w[i]);
//		}
		W = w;

		// after setting the weight cateory now reclac`uate the initail weight.
		double temp = 0.0;
		for (int i = 0; i < W.length; i++) {
			temp += W[i] * getAverageFeatures(i);
			////  logger.trace("averge feature "+ getAverageFeatures(i));
			//  logger.trace("w [" + i + " ]= " + W[i]);
		}
		wi0 = -0.5 * temp;
		//  logger.trace("wi0");

//		if (logger.isDebugEnabled()) {
//			//  logger.debug("setCategoryWeight(double[]) - end"); //$NON-NLS-1$
//		}
	}

	public double[] getCategoryWeight() {
		return W;
	}

	/**
	 * @return the wi0
	 */
	public double getWi0() {
		return wi0;
	}

	public int compareTo(Category o) throws ClassCastException {
//		if (logger.isDebugEnabled()) {
//			//  logger.debug("compareTo(Category) - start"); //$NON-NLS-1$
//		}

		if (!(o instanceof RubineCategory))
			throw new ClassCastException("Rubine category expected");
		RubineCategory temp = ((RubineCategory) o);

		int returnint = this.categoryName.compareTo(temp.categoryName);
//		if (logger.isDebugEnabled()) {
//			//  logger.debug("compareTo(Category) - end"); //$NON-NLS-1$
//		}
		return returnint;

	}

	/**
	 * @return the inksFeatures
	 */
	public ArrayList<InkFeatureSet> getInksFeatures() {
		return inksFeatures;
	}

	public void SaveData() {
//		if (logger.isDebugEnabled()) {
//			//  logger.debug("SaveData() - start"); //$NON-NLS-1$
//		}

		if (CovaricnceMatrix != null) {

			CovrMat = new double[CovaricnceMatrix.rows()][CovaricnceMatrix
					.columns()];

			for (int i = 0; i < CovaricnceMatrix.rows(); i++) {
				for (int j = 0; j < CovaricnceMatrix.columns(); j++) {
					CovrMat[i][j] = CovaricnceMatrix.getElement(i, j);
				}
			}

		}

//		if (logger.isDebugEnabled()) {
//			//  logger.debug("SaveData() - end"); //$NON-NLS-1$
//		}
	}

	public void ReadData() {
//		if (logger.isDebugEnabled()) {
//			//  logger.debug("ReadData() - start"); //$NON-NLS-1$
//		}

		if (CovrMat != null) {

			CovaricnceMatrix = new DoubleSquareMatrix(CovrMat.length);

			for (int i = 0; i < CovaricnceMatrix.rows(); i++) {
				for (int j = 0; j < CovaricnceMatrix.columns(); j++) {

					CovaricnceMatrix.setElement(i, j, CovrMat[i][j]);
				}
			}
		} else {
			this.covOk = false;

		}

//		if (logger.isDebugEnabled()) {
//			//  logger.debug("ReadData() - end"); //$NON-NLS-1$
//		}
	}

	public void deleteExample(int i) {
//		if (logger.isDebugEnabled()) {
//			//  logger.debug("deleteExample(int) - start"); //$NON-NLS-1$
//		}

		inksFeatures.remove(i);
		valuesOk = false;
		covOk = false;

//		if (logger.isDebugEnabled()) {
//			//  logger.debug("deleteExample(int) - end"); //$NON-NLS-1$
//		}
	}

}
