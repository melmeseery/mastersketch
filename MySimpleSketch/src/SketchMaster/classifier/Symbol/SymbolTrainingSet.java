package SketchMaster.classifier.Symbol;

import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;

import JSci.maths.matrices.DoubleSquareMatrix;
import SketchMaster.Stroke.StrokeData.InkInterface;
import SketchMaster.Stroke.StrokeData.SegmentCluster;
import SketchMaster.Stroke.features.InkFeatureSet;
import SketchMaster.Stroke.features.SegmentClusterFeatureSet;
import SketchMaster.Stroke.features.StrokeRubineFeatureSet;

import SketchMaster.classifier.rubine.RubineTrainingSet;
import SketchMaster.io.log.FileLog;
import SketchMaster.lib.MathLib;

public class SymbolTrainingSet extends RubineTrainingSet implements
		Serializable {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(SymbolTrainingSet.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = -53743440296758632L;

	@Override
	protected void computeCoverianceMatrix() {
//		if (logger.isDebugEnabled()) {
//			//  logger.debug("computeCoverianceMatrix() - start"); //$NON-NLS-1$
//		}

		//  logger.trace(" al coveraince ");
		CovOk = false;
		// first compute all the cateories average ssets

		// then set up the get number of features , number of eample, number of
		// guesturs
		int nF = 0, nE = 0, nCat = 0;
		// number of categories C
		nCat = size();

		// temp vairable for calcuation
		double E[][] = null;
		double top = 0, Buttom = 0;

		// initalize buttom with -C
		Buttom = -nCat;

		int l = 0;
		// getn number of features ... they supossed to be constant for all
		// categories.
		for (Iterator iter = this.iterator(); iter.hasNext(); l++) {
			SymbolCategory Cat = (SymbolCategory) iter.next();
			if (l == 0)
				nF = Cat.getFeaturesCount();

			// loop on the cateogry to get the bottom which is the Count of
			// example in each.
			// equation => B=-C+ Sum(E)
			Buttom += Cat.getExampleCount();

		}
		// //////////////////
//		if (logger.isDebugEnabled()) {
//			//  logger.debug("computeCoverianceMatrix() -  The features count is equal to .  " + nF + " (" + this.getClass().getSimpleName() + "    " + (new Throwable()).getStackTrace()[0].getLineNumber() + "  )  "); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		}
		// ////////////now create the E matrixx
		E = new double[nF][nF];
		for (int j = 0; j < E.length; j++) {
			for (int k = 0; k < E[j].length; k++) {
				E[j][k] = 0.0;
			}
		}

		// / llopp on i j featrus to get the covericande matrix.

		for (int i = 0; i < nF; i++) {
			for (int j = 0; j < nF; j++) {

				// loop on all the categries i have in the matrix
				for (Iterator iter = this.iterator(); iter.hasNext();) {

					SymbolCategory Cat = (SymbolCategory) iter.next();
					// compute top of sum(Ec) where Ec is the element of cov
					// matrix of category.
					// logger.info("will compute coveriance matrix of
					// "+Cat.getCategoryName());
//					if (Double.isNaN(Cat.getCovaricnceMatrix().getElement(i, j)))
//					 //  logger.trace( "NNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN   i=  "+i+"  j "+j+"   "+Cat.getCovaricnceMatrix().getElement(i, j));
					top += Cat.getCovaricnceMatrix().getElement(i, j);

				}
                 ////  logger.trace( "tttttttttttttttttttttt/BBBBBBBBBBBBBBBbb"+top+"           "+Buttom);
				E[i][j] = top / Buttom;
				top = 0.0;

			}
		}

//		if (logger.isDebugEnabled()) {
//			//  logger.debug("computeCoverianceMatrix() - displaying the e  (" + this.getClass().getSimpleName() + "    " + (new Throwable()).getStackTrace()[0].getLineNumber() + "  )  "); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
//		}
		for (int i = 0; i < E.length; i++) {
//			if (logger.isDebugEnabled()) {
//				//  logger.debug("computeCoverianceMatrix()"); //$NON-NLS-1$
//			}
			for (int j = 0; j < E[i].length; j++) {
//				if (logger.isDebugEnabled()) {
//					if (Double.isNaN(E[i][j]))
//					//  logger.debug("computeCoverianceMatrix() -    "+ "  i  "+ i + " j  "+ j +"   "+ E[i][j]); //$NON-NLS-1$
//				}
				
			}
		}
		
		CovaricnceMatrix = new DoubleSquareMatrix(E);
//		if (logger.isDebugEnabled()) {
//			//  logger.debug("computeCoverianceMatrix() - Now Compute the matrix (" + this.getClass().getSimpleName() + "    " + (new Throwable()).getStackTrace()[0].getLineNumber() + "  )  " + CovaricnceMatrix); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
//		}
		//  logger.trace("=================Combined  coveraicne Matrix  is ================ ");
		////  logger.trace(CovaricnceMatrix.toString());
		CovOk = true;


	}

	@Override
	protected void ComputeWeightVector() {
//		if (logger.isDebugEnabled()) {
//			//  logger.debug("ComputeWeightVector() - start"); //$NON-NLS-1$
//		}

	//	//  logger.trace("wwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww");
		Wok = false;
//		if (logger.isDebugEnabled()) {
//			//  logger.debug("ComputeWeightVector() -  (" + this.getClass().getSimpleName() + "    " + (new Throwable()).getStackTrace()[0].getLineNumber() + "  )  " + getCommonCovaricnceMatrix()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
//		}
	//	double temp=((DoubleSquareMatrix) getCommonCovaricnceMatrix()).det();
	//	logger.info("CCCCCCCCCCCCCCCCCCCCCCCCCCcc   "+getCommonCovaricnceMatrix().toString()+      "     det     "+temp);
		
//		if ((temp==0)||(Double.isNaN(temp)))
//			logger.error(" EEEEEEEEEEEEEEEE 00000000000000000000000  " +" matrix determing  ===000000000000000000" );
//		
		DoubleSquareMatrix E_1 = MathLib.inverse(getCommonCovaricnceMatrix());
	
	//	logger.info("EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEe"+E_1.toString());
		
		int l = 0;
		int nF = 0;
		double[] w;
		double wcj = 0.0; // temp variable used in additon
		// getn number of features ... they supossed to be constant for all
		// categories.
		for (Iterator iter = this.iterator(); iter.hasNext(); l++) {
			SymbolCategory Cat = (SymbolCategory) iter.next();
			//  logger.trace("will compute weight matrix of "	+ Cat.getCategoryName());
			if (l == 0) {
				nF = Cat.getFeaturesCount();
			}
			w = new double[nF];
			for (int i = 0; i < w.length; i++) {
				w[i] = 0;
			}
              //  //  logger.trace("      0000000000000000   "+nF);
			// now after wight matrix is created
			for (int j = 0; j < nF; j++) {
			//	//  logger.trace("wcj == "+wcj);
				
				for (int i = 0; i < nF; i++) {
					if (Double.isNaN(Cat.getAverageFeatures(i))||Double.isNaN(E_1.getElement(i, j)))
						{
						logger.error(" NNNNaN    "+Cat.getAverageFeatures(i));
						//	//  logger.trace(" ###########  E "+E_1.getElement(i, j)+  "   average cat  "+Cat.getAverageFeatures(i));
						logger.error(" ###########  E "+E_1.getElement(i, j));
						}
					
					wcj += E_1.getElement(i, j) * Cat.getAverageFeatures(i);

				}
				////  logger.trace("wcj == "+wcj+"  wj "+ w[j]);
				if (Double.isNaN(wcj)){
					logger.error(" Error in wcj =  "+wcj);
				}
				w[j] = wcj;
				wcj = 0.0;

			}

			// / now i have to get this array of wigh to the class
			Cat.setCategoryWeight(w);

		}

		Wok = true;

//		if (logger.isDebugEnabled()) {
//			//  logger.debug("ComputeWeightVector() - end"); //$NON-NLS-1$
//		}
	}

	public void addCategoryExample(int i, SegmentCluster cluster) {
		if (logger.isDebugEnabled()) {
			//  logger.debug("  ---------------->  addCategoryExample(int, SegmentCluster) - start"); //$NON-NLS-1$
		}

		int j = 0;
		for (Iterator iter = this.iterator(); iter.hasNext();) {
			SymbolCategory element = (SymbolCategory) iter.next();
			if (j == i)

			{

				// logger.info("164 symboltrainingset. adding a new
				// example to "+element.getCategoryName());
				element.addExample(cluster);
				Wok = false;
				CovOk = false;

//				if (logger.isDebugEnabled()) {
//					//  logger.debug("addCategoryExample(int, SegmentCluster) - end"); //$NON-NLS-1$
//				}
				return;

			}
			j++;

		}

//		if (logger.isDebugEnabled()) {
//			//  logger.debug("addCategoryExample(int, SegmentCluster) - end"); //$NON-NLS-1$
//		}
	}

	public void addCategoryExample(String name, SegmentCluster cluster) {
//		if (logger.isDebugEnabled()) {
//			//  logger.debug("addCategoryExample(String, SegmentCluster) - start"); //$NON-NLS-1$
//		}

		for (Iterator iter = this.iterator(); iter.hasNext();) {
			SymbolCategory element = (SymbolCategory) iter.next();
			if (element.getCategoryName().equalsIgnoreCase(name))

			{

				element.addExample(cluster);
				Wok = false;
				CovOk = false;

//				if (logger.isDebugEnabled()) {
//					//  logger.debug("addCategoryExample(String, SegmentCluster) - end"); //$NON-NLS-1$
//				}
				return;

			}

		}

//		if (logger.isDebugEnabled()) {
//			//  logger.debug("addCategoryExample(String, SegmentCluster) - end"); //$NON-NLS-1$
//		}
	}

	public ArrayList getCategoryExamples(String name) {
//		if (logger.isDebugEnabled()) {
//			//  logger.debug("getCategoryExamples(String) - start"); //$NON-NLS-1$
//		}

		for (Iterator iter = this.iterator(); iter.hasNext();) {
			SymbolCategory element = (SymbolCategory) iter.next();
			if (element.getCategoryName().equalsIgnoreCase(name))

			{

				ArrayList returnArrayList = element.getInksFeatures();
//				if (logger.isDebugEnabled()) {
//					//  logger.debug("getCategoryExamples(String) - end"); //$NON-NLS-1$
//				}
				return returnArrayList;

			}

		}

//		if (logger.isDebugEnabled()) {
//			//  logger.debug("getCategoryExamples(String) - end"); //$NON-NLS-1$
//		}
		return null;

	}

	public ArrayList getCategoryExamplesInk(String name) {
//		if (logger.isDebugEnabled()) {
//			//  logger.debug("getCategoryExamplesInk(String) - start"); //$NON-NLS-1$
//		}

		for (Iterator iter = this.iterator(); iter.hasNext();) {
			SymbolCategory element = (SymbolCategory) iter.next();
			if (element.getCategoryName().equalsIgnoreCase(name))

			{

				ArrayList<InkFeatureSet> t = element.getInksFeatures();
				ArrayList<SegmentCluster> inks = new ArrayList<SegmentCluster>();
				for (int i = 0; i < t.size(); i++) {
					inks.add(((SegmentClusterFeatureSet) t.get(i))
							.getSegmentCluster());

				}

//				if (logger.isDebugEnabled()) {
//					//  logger.debug("getCategoryExamplesInk(String) - end"); //$NON-NLS-1$
//				}
				return inks;

			}

		}

//		if (logger.isDebugEnabled()) {
//			//  logger.debug("getCategoryExamplesInk(String) - end"); //$NON-NLS-1$
//		}
		return null;

	}

	public ArrayList getCategoryExamples(int i) {
//		if (logger.isDebugEnabled()) {
//			//  logger.debug("getCategoryExamples(int) - start"); //$NON-NLS-1$
//		}

		int j = 0;
		for (Iterator iter = this.iterator(); iter.hasNext();) {
			SymbolCategory element = (SymbolCategory) iter.next();
			if (j == i)

			{
				// SymbolCategory element = (SymbolCategory) iter.next();

				ArrayList returnArrayList = element.getInksFeatures();
//				if (logger.isDebugEnabled()) {
//					//  logger.debug("getCategoryExamples(int) - end"); //$NON-NLS-1$
//				}
				return returnArrayList;

			}
			j++;

		}

//		if (logger.isDebugEnabled()) {
//			//  logger.debug("getCategoryExamples(int) - end"); //$NON-NLS-1$
//		}
		return null;

	}

	public SymbolCategory getCategory(String name) {
//		if (logger.isDebugEnabled()) {
//			//  logger.debug("getCategory(String) - start"); //$NON-NLS-1$
//		}

		for (Iterator iter = this.iterator(); iter.hasNext();) {
			SymbolCategory element = (SymbolCategory) iter.next();
			if (element.getCategoryName().equalsIgnoreCase(name))

			{

//				if (logger.isDebugEnabled()) {
//					//  logger.debug("getCategory(String) - end"); //$NON-NLS-1$
//				}
				return element;

			}

		}

//		if (logger.isDebugEnabled()) {
//			//  logger.debug("getCategory(String) - end"); //$NON-NLS-1$
//		}
		return null;
	}

	public void deleteCategory(String name) {
//		if (logger.isDebugEnabled()) {
//			//  logger.debug("deleteCategory(String) - start"); //$NON-NLS-1$
//		}

		for (Iterator iter = this.iterator(); iter.hasNext();) {
			SymbolCategory element = (SymbolCategory) iter.next();
			if (element.getCategoryName().equalsIgnoreCase(name)) {

				this.remove(element);
				Wok = false;
				CovOk = false;

//				if (logger.isDebugEnabled()) {
//					//  logger.debug("deleteCategory(String) - end"); //$NON-NLS-1$
//				}
				return;
			}

		}

//		if (logger.isDebugEnabled()) {
//			//  logger.debug("deleteCategory(String) - end"); //$NON-NLS-1$
//		}
	}

	public int deleteCategoryExample(String name, int i) {
//		if (logger.isDebugEnabled()) {
//			//  logger.debug("deleteCategoryExample(String, int) - start"); //$NON-NLS-1$
//		}

		for (Iterator iter = this.iterator(); iter.hasNext();) {
			SymbolCategory element = (SymbolCategory) iter.next();
			if (element.getCategoryName().equalsIgnoreCase(name)) {

				element.deleteExample(i);
				Wok = false;
				CovOk = false;
				int returnint = element.getExampleCount();
//				if (logger.isDebugEnabled()) {
//					//  logger.debug("deleteCategoryExample(String, int) - end"); //$NON-NLS-1$
//				}
				return returnint;
			}

		}

//		if (logger.isDebugEnabled()) {
//			//  logger.debug("deleteCategoryExample(String, int) - end"); //$NON-NLS-1$
//		}
		return 0;
	}

	public int getCategoryID(String name) {
//		if (logger.isDebugEnabled()) {
//			//  logger.debug("getCategoryID(String) - start"); //$NON-NLS-1$
//		}

		int i=0;
		for (Iterator iter = this.iterator(); iter.hasNext();) {
			SymbolCategory element = (SymbolCategory) iter.next();
			
			if (element.getCategoryName().equalsIgnoreCase(name))

			{
//				if (logger.isDebugEnabled()) {
//					//  logger.debug("getCategoryID(String) - end"); //$NON-NLS-1$
//				}
				return i;
			}
			i++;
		}
		int returnint = -1;
//		if (logger.isDebugEnabled()) {
//			//  logger.debug("getCategoryID(String) - end"); //$NON-NLS-1$
//		}
		return returnint;
	}

}
