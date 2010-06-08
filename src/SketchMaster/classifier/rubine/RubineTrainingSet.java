/**
 * 
 */
package SketchMaster.classifier.rubine;

import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;

import JSci.maths.matrices.AbstractDoubleSquareMatrix;
import JSci.maths.matrices.DoubleMatrix;
import JSci.maths.matrices.DoubleSquareMatrix;
import SketchMaster.Stroke.StrokeData.InkInterface;
import SketchMaster.Stroke.features.InkFeatureSet;
import SketchMaster.Stroke.features.StrokeRubineFeatureSet;
import SketchMaster.classifier.Symbol.SymbolCategory;
import SketchMaster.io.log.FileLog;

/**
 * @author maha
 * 
 */
public class RubineTrainingSet extends LinkedHashSet<RubineCategory> implements
		Serializable {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(RubineTrainingSet.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 8858155913902859033L;
	protected boolean CovOk = false;
	// i need a coveraicne matirx
	transient protected DoubleSquareMatrix CovaricnceMatrix = null;
	protected boolean Wok = false;

	protected double[][] CovrMat;

	/*
	 * *
	 
	 */
	public RubineTrainingSet() {

	}

	/**
	 * @param arg0
	 */
	public RubineTrainingSet(int arg0) {
		super(arg0);

	}

	/**
	 * @param arg0
	 */
	public RubineTrainingSet(Collection arg0) {
		super(arg0);

	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public RubineTrainingSet(int arg0, float arg1) {
		super(arg0, arg1);

	}

	protected void computeCoverianceMatrix() {
//		if (logger.isDebugEnabled()) {
//			//  logger.debug("computeCoverianceMatrix() - start"); //$NON-NLS-1$
//		}

		//  logger.trace("al coveraince ");
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
			RubineCategory Cat = (RubineCategory) iter.next();
			if (l == 0)
				nF = Cat.getFeaturesCount();

			// loop on the cateogry to get the bottom which is the Count of
			// example in each.
			// equation => B=-C+ Sum(E)
			Buttom += Cat.getExampleCount();

		}
		// //////////////////

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

					RubineCategory Cat = (RubineCategory) iter.next();
					// compute top of sum(Ec) where Ec is the element of cov
					// matrix of category.
					// logger.info("will compute coveriance matrix of
					// "+Cat.getCategoryName());
					top += Cat.getCovaricnceMatrix().getElement(i, j);

				}

				E[i][j] = top / Buttom;
				top = 0.0;

			}
		}

		CovaricnceMatrix = new DoubleSquareMatrix(E);
		//  logger.trace("=================Combined  coveraicne Matrix  is ================ ");
		//  logger.trace(CovaricnceMatrix.toString());
		CovOk = true;

//		if (logger.isDebugEnabled()) {
//			//  logger.debug("computeCoverianceMatrix() - end"); //$NON-NLS-1$
//		}
	}

	/**
	 * @return the covaricnceMatrix
	 */
	public DoubleSquareMatrix getCommonCovaricnceMatrix() {
//		if (logger.isDebugEnabled()) {
//			//  logger.debug("getCommonCovaricnceMatrix() - start"); //$NON-NLS-1$
//		}

		if (!CovOk)
			computeCoverianceMatrix();
		

//		if (logger.isDebugEnabled()) {
//			//  logger.debug("getCommonCovaricnceMatrix() - end"); //$NON-NLS-1$
//		}
		return CovaricnceMatrix;
	}

	protected void ComputeWeightVector() {
//		if (logger.isDebugEnabled()) {
//			//  logger.debug("ComputeWeightVector() - start"); //$NON-NLS-1$
//		}

		////  logger.trace("wwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww");
		Wok = false;
		////  logger.trace("  before    "+getCommonCovaricnceMatrix());
		DoubleSquareMatrix E_1 = (DoubleSquareMatrix) getCommonCovaricnceMatrix().inverse();
       // //  logger.trace("   iiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii       "+E_1);
		int l = 0;
		int nF = 0;
		double[] w;
		double wcj = 0; // temp variable used in additon
		// getn number of features ... they supossed to be constant for all
		// categories.
		for (Iterator iter = this.iterator(); iter.hasNext(); l++) {
			RubineCategory Cat = (RubineCategory) iter.next();
			//  logger.trace("will compute weight matrix of "	+ Cat.getCategoryName());
			if (l == 0) {
				nF = Cat.getFeaturesCount();
			}
			w = new double[nF];
			for (int i = 0; i < w.length; i++) {
				w[i] = 0;
			}
           //  logger.trace(" ----------------------------nF   "+nF);
			// now after wight matrix is created
			for (int j = 0; j < nF; j++) {
				for (int i = 0; i < nF; i++) {
					if (Double.isNaN(Cat.getAverageFeatures(i)))
						//  logger.trace(" NNNNNNNNNNNNNNNNNNNNNNNN NaN    "+Cat.getAverageFeatures(i));
					wcj += E_1.getElement(i, j) * Cat.getAverageFeatures(i);

				}
			//	//  logger.trace("wcj == "+wcj+"  wj "+ w[j]);
				w[j] = wcj;
				wcj = 0.0;

			}

			// / now i have to get this array of wigh to the class
			Cat.setCategoryWeight(w);
		//	//  logger.trace("wcj == "+w);
		}

		Wok = true;

//		if (logger.isDebugEnabled()) {
//			//  logger.debug("ComputeWeightVector() - end"); //$NON-NLS-1$
//		}
	}

	public void trainAll() {
//		if (logger.isDebugEnabled()) {
//			//  logger.debug("trainAll() - start"); //$NON-NLS-1$
//		}
             
		this.computeCoverianceMatrix();
		this.ComputeWeightVector();
        if (!logger.isInfoEnabled())
        {
        	logger.error("  the coveriance matrix is  "+this.CovaricnceMatrix);
        	//logger.error("  the weigh vector is ");
        }
//		if (logger.isDebugEnabled()) {
//			//  logger.debug("trainAll() - end"); //$NON-NLS-1$
//		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.HashSet#add(java.lang.Object)
	 */
	@Override
	public boolean add(RubineCategory o) {
//		if (logger.isDebugEnabled()) {
//			//  logger.debug("add(RubineCategory) - start"); //$NON-NLS-1$
//		}

		if (super.add(o)) {
			//
			// logger.info("adding the "+o.getCategoryName());
			Wok = false;
			CovOk = false;

//			if (logger.isDebugEnabled()) {
//				//  logger.debug("add(RubineCategory) - end"); //$NON-NLS-1$
//			}
			return true;
		} else {

//			if (logger.isDebugEnabled()) {
//				//  logger.debug("add(RubineCategory) - end"); //$NON-NLS-1$
//			}
			return false;
		}
	}

	public void addCategoryExample(int i, InkInterface ink) {
//		if (logger.isDebugEnabled()) {
//			//  logger.debug("addCategoryExample(int, InkInterface) - start"); //$NON-NLS-1$
//		}

		int j = 0;
		for (Iterator iter = this.iterator(); iter.hasNext();) {
			RubineCategory element = (RubineCategory) iter.next();
			if (j == i)

			{

				// logger.info("adding a new example to
				// "+element.getCategoryName());
				element.addExample(ink);
				Wok = false;
				CovOk = false;

//				if (logger.isDebugEnabled()) {
//					//  logger.debug("addCategoryExample(int, InkInterface) - end"); //$NON-NLS-1$
//				}
				return;

			}
			j++;

		}

//		if (logger.isDebugEnabled()) {
//			//  logger.debug("addCategoryExample(int, InkInterface) - end"); //$NON-NLS-1$
//		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.HashSet#remove(java.lang.Object)
	 */
	@Override
	public boolean remove(Object o) {
//		if (logger.isDebugEnabled()) {
//			//  logger.debug("remove(Object) - start"); //$NON-NLS-1$
//		}

		Wok = false;

		CovOk = false;

		boolean returnboolean = super.remove(o);
//		if (logger.isDebugEnabled()) {
//			//  logger.debug("remove(Object) - end"); //$NON-NLS-1$
//		}
		return returnboolean;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.AbstractSet#removeAll(java.util.Collection)
	 */
	@Override
	public boolean removeAll(Collection<?> c) {
//		if (logger.isDebugEnabled()) {
//			//  logger.debug("removeAll(Collection<?>) - start"); //$NON-NLS-1$
//		}

		Wok = false;
		CovOk = false;

		boolean returnboolean = super.removeAll(c);
//		if (logger.isDebugEnabled()) {
//			//  logger.debug("removeAll(Collection<?>) - end"); //$NON-NLS-1$
//		}
		return returnboolean;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.AbstractCollection#addAll(java.util.Collection)
	 */
	@Override
	public boolean addAll(Collection<? extends RubineCategory> c) {
//		if (logger.isDebugEnabled()) {
//			//  logger.debug("addAll(Collection<? extends RubineCategory>) - start"); //$NON-NLS-1$
//		}

		Wok = false;
		CovOk = false;

		boolean returnboolean = super.addAll(c);
//		if (logger.isDebugEnabled()) {
//			//  logger.debug("addAll(Collection<? extends RubineCategory>) - end"); //$NON-NLS-1$
//		}
		return returnboolean;
	}

	public void init() {
//		if (logger.isDebugEnabled()) {
//			//  logger.debug("init() - start"); //$NON-NLS-1$
//		}

		Wok = false;
		CovOk = false;

//		if (logger.isDebugEnabled()) {
//			//  logger.debug("init() - end"); //$NON-NLS-1$
//		}
	}

	public boolean getTrainingState() {
//		if (logger.isDebugEnabled()) {
//			//  logger.debug("getTrainingState() - start"); //$NON-NLS-1$
//		}

		boolean returnboolean = (Wok && CovOk);
//		if (logger.isDebugEnabled()) {
//			//  logger.debug("getTrainingState() - end"); //$NON-NLS-1$
//		}
		return returnboolean;
	}

	public String[] getCategoryNames() {
//		if (logger.isDebugEnabled()) {
//			//  logger.debug("getCategoryNames() - start"); //$NON-NLS-1$
//		}

		String[] names = new String[size()];
		int i = 0;
		for (Iterator iter = this.iterator(); iter.hasNext();) {
			RubineCategory element = (RubineCategory) iter.next();
			names[i] = element.getCategoryName();
			i++;

		}

//		if (logger.isDebugEnabled()) {
//			//  logger.debug("getCategoryNames() - end"); //$NON-NLS-1$
//		}
		return names;
	}
	public boolean CategoryExist(String name){
//		if (logger.isDebugEnabled()) {
//			//  logger.debug("CategoryExist(String) - start"); //$NON-NLS-1$
//		}

		for (Iterator iter = this.iterator(); iter.hasNext();) {
			RubineCategory element = (RubineCategory) iter.next();
			 if (element.getCategoryName().equalsIgnoreCase(name))
				 return true;
			

		}

//		if (logger.isDebugEnabled()) {
//			//  logger.debug("CategoryExist(String) - end"); //$NON-NLS-1$
//		}
		return false;
	}

	public void addCategoryExample(String name, InkInterface ink) {
//		if (logger.isDebugEnabled()) {
//			//  logger.debug("addCategoryExample(String, InkInterface) - start"); //$NON-NLS-1$
//		}

		for (Iterator iter = this.iterator(); iter.hasNext();) {
			RubineCategory element = (RubineCategory) iter.next();
			if (element.getCategoryName().equalsIgnoreCase(name))

			{

				element.addExample(ink);
				Wok = false;
				CovOk = false;

//				if (logger.isDebugEnabled()) {
//					//  logger.debug("addCategoryExample(String, InkInterface) - end"); //$NON-NLS-1$
//				}
				return;

			}

		}

//		if (logger.isDebugEnabled()) {
//			//  logger.debug("addCategoryExample(String, InkInterface) - end"); //$NON-NLS-1$
//		}
	}

	public ArrayList getCategoryExamples(String name) {
//		if (logger.isDebugEnabled()) {
//			//  logger.debug("getCategoryExamples(String) - start"); //$NON-NLS-1$
//		}

		for (Iterator iter = this.iterator(); iter.hasNext();) {
			RubineCategory element = (RubineCategory) iter.next();
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
			RubineCategory element = (RubineCategory) iter.next();
			if (element.getCategoryName().equalsIgnoreCase(name))

			{

				ArrayList<InkFeatureSet> t = element.getInksFeatures();
				ArrayList<InkInterface> inks = new ArrayList<InkInterface>();
				for (int i = 0; i < t.size(); i++) {
					inks.add(t.get(i).getInk());

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
			RubineCategory element = (RubineCategory) iter.next();
			if (j == i)

			{
				// RubineCategory element = (RubineCategory) iter.next();

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

	public RubineCategory getCategory(String name) {
//		if (logger.isDebugEnabled()) {
//			//  logger.debug("getCategory(String) - start"); //$NON-NLS-1$
//		}

		for (Iterator iter = this.iterator(); iter.hasNext();) {
			RubineCategory element = (RubineCategory) iter.next();
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

		for (Iterator iter = this.iterator(); iter.hasNext();) {
			RubineCategory element = (RubineCategory) iter.next();
			element.SaveData();
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
			// logger.info(CovaricnceMatrix);
		} else {
			this.CovOk = false;
			this.Wok = false;
		}

		for (Iterator iter = this.iterator(); iter.hasNext();) {
			RubineCategory element = (RubineCategory) iter.next();
			element.ReadData();
		}

//		if (logger.isDebugEnabled()) {
//			//  logger.debug("ReadData() - end"); //$NON-NLS-1$
//		}
	}

	public void deleteCategory(String name) {
//		if (logger.isDebugEnabled()) {
//			//  logger.debug("deleteCategory(String) - start"); //$NON-NLS-1$
//		}

		for (Iterator iter = this.iterator(); iter.hasNext();) {
			RubineCategory element = (RubineCategory) iter.next();
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
			RubineCategory element = (RubineCategory) iter.next();
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
