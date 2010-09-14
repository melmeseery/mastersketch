package SketchMaster.classifier.Symbol;

import org.apache.log4j.Logger;

import java.util.Iterator;

import SketchMaster.Stroke.StrokeData.InkInterface;
import SketchMaster.Stroke.StrokeData.SegmentCluster;
import SketchMaster.Stroke.features.SegmentClusterFeatureSet;
import SketchMaster.Stroke.features.StrokeRubineFeatureSet;
import SketchMaster.classifier.Classification;
import SketchMaster.classifier.rubine.RubineCategory;
import SketchMaster.classifier.rubine.RubineClassifier;
import SketchMaster.classifier.rubine.RubineTrainingSet;

public class SymbolClassifier extends RubineClassifier {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(SymbolClassifier.class);

	private SymbolTrainingSet trainSetS;

	public Classification Classify(SegmentCluster cluster) {
		if (logger.isDebugEnabled()) {
			//  logger.debug("Classify(SegmentCluster) - start"); //$NON-NLS-1$
		}

		// the new ink if not trained then train.

		this.train();

		Classification inkclass = new Classification();
		// first i need to get the featus of the commint ink

		// StrokeRubineFeatureSet featurs=new StrokeRubineFeatureSet(ink);
		SegmentClusterFeatureSet featurs = new SegmentClusterFeatureSet();
		featurs.setSegmentCluster(cluster);

		featurs.initAll();
		featurs.computeFeatures();
		int fCout =featurs.getFeatures().size();
		
		
//		for (int i = 0; i < featurs.getFeatures().size(); i++) {
//			fCout+=featurs.getFeatures().get(i).NoOfValues();
//		}
		// now i have all the features computed
		double temp = 0;
		// get the categories
		double FeatValue;
		//int l=0;
		int index;
		// trainSet
		for (Iterator set = trainSetS.iterator(); set.hasNext();) {
			SymbolCategory Category = (SymbolCategory) set.next();
		
			index=0;
			for (int i = 0; i < fCout; i++) {
//				if (featurs.getFeatures().get(i).NoOfValues()==1)
//				{
//					FeatValue=featurs.getFeatures().get(i).getValue();
//					temp += Category.getCategoryWeight()[index]
//					             						* FeatValue;
//					index++;
//				}
				//else if (featurs.getFeatures().get(i).NoOfValues()>1){
					for (int k = 0; k < featurs.getFeatures().get(i).getValues().length; k++) {
						FeatValue=featurs.getFeatures().get(i).getValues()[k];
						temp += Category.getCategoryWeight()[index+k]* FeatValue;
					
					}
					index+=featurs.getFeatures().get(i).getValues().length;
				
			//	}
				
				
			}
			temp += Category.getWi0();
			//l++;
			// now i need to classifiy

			// this can more t
			inkclass.put(Category, temp);
			temp = 0.0;

		}

		if (logger.isDebugEnabled()) {
			//  logger.debug("Classify(SegmentCluster) - end"); //$NON-NLS-1$
		}
		return inkclass;

		// return null;

	}

	public void train() {
		if (logger.isDebugEnabled()) {
			//  logger.debug("train() - start"); //$NON-NLS-1$
		}

		if (trainSetS != null) {
			if (!trainSetS.getTrainingState()) {
				// logger.info("trianig ");
				trainSetS.trainAll();

			}
		}

		if (logger.isDebugEnabled()) {
			//  logger.debug("train() - end"); //$NON-NLS-1$
		}
	}

	@Override
	public void init(RubineTrainingSet trainSet) {
//		if (logger.isDebugEnabled()) {
//			//  logger.debug("init(RubineTrainingSet) - start"); //$NON-NLS-1$
//		}

		// init using this traing set.
if (trainSet instanceof SymbolTrainingSet) {
		this.trainSetS = (SymbolTrainingSet) trainSet;
	
}
		// logger.info("triang set is ");
		//this.trainSetS = (SymbolTrainingSet) trainSet;
		//

//		if (logger.isDebugEnabled()) {
//			//  logger.debug("init(RubineTrainingSet) - end"); //$NON-NLS-1$
//		}
	}

}
