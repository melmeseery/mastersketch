/**
 * 
 */
package SketchMaster.classifier.rubine;

import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.Iterator;

import SketchMaster.Stroke.StrokeData.InkInterface;
import SketchMaster.Stroke.features.StrokeRubineFeatureSet;
import SketchMaster.classifier.Classification;
import SketchMaster.classifier.Classifier;

/**
 * @author maha
 * 
 */
public class RubineClassifier extends Classifier implements Serializable {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(RubineClassifier.class);

	protected boolean trained = false;
	private RubineTrainingSet trainSet;

	/**
	 * 
	 */
	public RubineClassifier() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see SketchMaster.classifier.Classifier#Classify(SketchMaster.Stroke.StrokeData.InkObject)
	 */
	@Override
	public Classification Classify(InkInterface ink) {
		if (logger.isDebugEnabled()) {
			//  logger.debug("Classify(InkInterface) - start"); //$NON-NLS-1$
		}

		// the new ink if not trained then train.

		this.train();

		Classification inkclass = new Classification();
		// first i need to get the featus of the commint ink

		StrokeRubineFeatureSet featurs = new StrokeRubineFeatureSet(ink);
		featurs.initAll();
		featurs.computeFeatures();
		// now i have all the features computed
		double temp = 0;
		// get the categories
		// trainSet
		for (Iterator set = trainSet.iterator(); set.hasNext();) {
			RubineCategory Category = (RubineCategory) set.next();
			for (int i = 0; i < featurs.getFeatures().size(); i++) {
				temp += Category.getCategoryWeight()[i]
						* featurs.getFeatures().get(i).getValue();
			}
			temp += Category.getWi0();
			// now i need to classifiy

			// this can more t
			inkclass.put(Category, temp);
			temp = 0.0;

		}

		if (logger.isDebugEnabled()) {
			//  logger.debug("Classify(InkInterface) - end"); //$NON-NLS-1$
		}
		return inkclass;

		// return null;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see SketchMaster.classifier.Classifier#train()
	 */
	@Override
	public void train() {
		if (logger.isDebugEnabled()) {
			//  logger.debug("train() - start"); //$NON-NLS-1$
		}

		if (trainSet != null) {
			if (!trainSet.getTrainingState()) {
				// logger.info("trianig ");
				trainSet.trainAll();

			}
		}

		if (logger.isDebugEnabled()) {
			//  logger.debug("train() - end"); //$NON-NLS-1$
		}
	}

	public void init() {

		// trained=false;

	}

	public void init(RubineTrainingSet trainSet) {

		// init using this traing set.

		// logger.info("triang set is ");
		this.trainSet = trainSet;
		//
	}

}
