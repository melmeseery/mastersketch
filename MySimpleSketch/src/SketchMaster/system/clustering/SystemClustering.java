/**
 * 
 */
package SketchMaster.system.clustering;

import org.apache.log4j.Logger;

import java.util.ArrayList;

import SketchMaster.Stroke.StrokeData.Segment;
import SketchMaster.Stroke.StrokeData.SegmentCluster;
import SketchMaster.Stroke.StrokeData.Stroke;
import SketchMaster.Stroke.graphics.layers.SketchLayer;
import SketchMaster.Stroke.graphics.shapes.GuiShape;
import SketchMaster.Stroke.graphics.shapes.SegmentedShape;

/**
 * @author maha
 * 
 */
public class SystemClustering {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(SystemClustering.class);

	// i will have a list of strokes not clustered ==> list of segments

	ArrayList<GuiShape> UnClusteredsegments = new ArrayList<GuiShape>();
	ArrayList<Stroke> UnClusteredStrokes = new ArrayList<Stroke>();
	ArrayList<SegmentCluster> symbols = new ArrayList<SegmentCluster>();
	double cluserArea=0.0;
	

	// i will have a list of clustered strokes

	private SketchLayer CurrentSymbolLayer;

	public void setSymbolLayer(SketchLayer layer) {

		// System.out.println("''''''''''''''''''''''''''''''''''''''''''");
		CurrentSymbolLayer = layer;
	}

	// this the main class will implement the clustring algoirthms
	// here is the steps
	/***************************************************************************
	 * The simple algorithm, an event from user add the previosu strokes into a
	 * new cluster.
	 * 
	 * 
	 * 
	 **************************************************************************/

	public void addStrokeSegmentation(Stroke stroke, GuiShape segmenation) {
		// add this stroke with its segmetnation to the list of unclustered
		// strokes
		if (UnClusteredsegments != null) {
			UnClusteredsegments.add(segmenation);
			UnClusteredStrokes.add(stroke);
			cluserArea+=stroke.getStatisticalInfo().getArea();

		}
	}

	public SegmentCluster CreateSymbol() {
		// create a new cluster and starts its recognition from the set of
		// unclustered strokes.
		SegmentCluster symbol = new SegmentCluster();

		// for each segmented stroke not part of a symbol do the follwing
		for (int i = 0; i < UnClusteredsegments.size(); i++) {
			// is the cluster a segmented shape
			if (UnClusteredsegments.get(i) instanceof SegmentedShape) {
				// //
				// //now divide the shape into segments to be used in the
				// cluster/symbol
				SegmentedShape shape = (SegmentedShape) UnClusteredsegments
						.get(i);
				
			
			//	logger.info("Shape is  "+shape);
				ArrayList<Segment> SegmentsOfStroke = UnClusteredStrokes.get(i).createSubSegments(shape);
				// add all segments to the cluster.
//				  logger.info("  this i stroke number "+i);
//             logger.info("     Segment of stroke size =   "+SegmentsOfStroke.size() );
           
				symbol.addAll(UnClusteredStrokes.get(i), SegmentsOfStroke);
				

			}

		}
		symbol.setArea(cluserArea);
		symbol.buildSymbol();
		// symbol.Recognize();
		symbols.add(symbol);
		if (CurrentSymbolLayer != null) {
			CurrentSymbolLayer.addObject(symbol);
			// System.out.println("adding a nd object to
			// theclustersssingssssssssssssss ")
			// ;
		}
		// now create a new segments to collect info for the next symbol
		UnClusteredsegments = new ArrayList<GuiShape>();
		UnClusteredStrokes = new ArrayList<Stroke>();
		cluserArea=0.0;  // TODO:: this area may be computed wrong because some strokes may overlap so it cannot be simple addded 
	//	if (logger.isDebugEnabled()) {
			logger.info("CreateSymbol() - -----------------------------{finish symbol}----------------------------------------          ( 88  system clusetering )"); //$NON-NLS-1$
		//}
		return symbol;
	}

	public void clearAll() {
 UnClusteredsegments.clear();
		 UnClusteredStrokes.clear();
		symbols.clear();
		CurrentSymbolLayer.ClearAll();
		
	}

	/**
	 * @return the unClusteredStrokes
	 */
	public ArrayList<Stroke> getUnClusteredStrokes() {
		return UnClusteredStrokes;
	}

	/**
	 * @return the symbols
	 */
	public ArrayList<SegmentCluster> getSymbols() {
		return symbols;
	}

	/***************************************************************************
	 * Complex 1. Add stroke to the stroke layers "Stroke list" 2. use the
	 * segmentors class to segment the stroke. ==> return a ArrayList of
	 * segments or fit solutions 3. check the "NonRecogniziedClustersList" a) if
	 * empty create a new cluster and add all the segments to a new cluster then
	 * move to (4) b) if not check the list clusters for each cluster i) check
	 * the bounding box of cluster with the new stroke if overlap or intersect.
	 * ii) add it to cluster but with low certainty -- count number of add in
	 * clusters c) if segment is add to more than one cluster then mark them all
	 * witha a flag. 4. try to recognize changed cluster with the classifiers 5.
	 * remove the recognized cluster for the list into the symbols list. 6.
	 * clean up. if a marked cluster is recognizied i have to revise the list to
	 * remove the repeated segments.
	 * 
	 **************************************************************************/

}
