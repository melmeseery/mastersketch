package SketchMaster.system.segmentation;

import org.apache.log4j.Logger;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import SketchMaster.system.SystemSettings;

import SketchMaster.Stroke.StrokeData.*;
import SketchMaster.Stroke.features.FeatureFunction;
import SketchMaster.Stroke.features.StrokeFeatures;
import SketchMaster.collection.NumericalComparator;
import SketchMaster.collection.SortedValueMap;
import SketchMaster.gui.DrawingDebugUtils;
import SketchMaster.system.SystemSettings;

public class SezginSegmentor  implements Cloneable {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(SezginSegmentor.class);

	private static double ErrorThreshold = SystemSettings.SEGZIN_ERROR_THRESHOLD;
	private SortedValueMap HybirdFitList;
	private SortedValueMap segmentsCurvaturePoints;
	private SortedValueMap segmentsVelocityPoints;
	

	@Override
	public Object clone() {

		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {

			logger.error("clone()", e); //$NON-NLS-1$
		}
		return this;
	}

	public static SortedValueMap createVertices(Stroke ink, SortedValueMap segmentsPoints,
			int type) {
		if (segmentsPoints==null)
			segmentsPoints=new SortedValueMap(new NumericalComparator());
		
		int curvatureCount;
		double max = Double.MIN_VALUE, min = Double.MAX_VALUE;
		String functionName = "";
		if (type == 0)
			// functionName="Direction";
			functionName = SystemSettings.CURVATURENAME;
		// functionName="Curvature of storke Vs. distance";
		 if (type == 1)
			functionName = "Velocity of storke Vs. distance";

		 if (type==2)
			functionName="Direction";
		 if (type==3)
			 functionName="Time";
		// Direction
		// //Done : add the correct implemenation for finding the critcal points
		// which include finding local maximum not the points > average.
		// +http://download.eclipse.org/tools/ve/downloads/
		// / logger.info("// doneTODO : add the correct implemenation for
		// finding the critcal points "+" (" +this.getClass().getSimpleName()+"
		// "+(new Throwable()).getStackTrace()[0].getLineNumber()+" ) ");
		// logger.info(" which include finding local maximum not the
		// points > threshold. (sezginsegmentor 52 ) ");
		ArrayList<FeatureFunction> features = ink.getStatisticalInfo()
				.getFunctions();

		for (int i = 0; i < features.size(); i++) {

			if (features.get(i).getName().startsWith(functionName)) {

				ArrayList<Integer> indeces = ink.getStatisticalInfo()
						.getIndeces(features.get(i));
				// logger.info(" function "+features.get(i).getName()+" =
				// "+indeces.size()+" ("
				// + this.getClass().getSimpleName() + " "
				// + (new Throwable()).getStackTrace()[0].getLineNumber()
				// + " ) ");
				for (int j = 0; j < indeces.size(); j++) {

					DominatePointStructure dominatepoint = new DominatePointStructure();

					dominatepoint.setPoint(ink.getPoints().get(
							(Integer) indeces.get(j)));
					dominatepoint.setIndexInInk((Integer) indeces.get(j));

					dominatepoint.addFunction(type);

					if (type == 0)// curvature certianty
					{
						int indexToget = (Integer) indeces.get(j);
									//  get the data of the curvature stats from the features 
						// i will use this data to get the 
						ArrayList<PointData> empData = features.get(i).getData();
						if (empData.size() > indexToget) {
							dominatepoint
									.setCertainty(computeCurvatureCertainty(
											indexToget, features.get(i)));
						}
						else {
						dominatepoint.setCertainty(1.0);
						}
					}
					if (type == 1)
					//
					{
						int indexToget = (Integer)indeces.get(j);

						ArrayList<PointData> empData = features.get(i).getData();
						if (empData.size() > indexToget) {
							Point2D point = empData.get(indexToget);
							dominatepoint
									.setCertainty(computeVelocityCertainty(
											point, features.get(i).getMax()));
						} else {
							dominatepoint.setCertainty(1.0);
						}
					}
					if (type>1)
					{
						
						int indexToget = (Integer)indeces.get(j);

						ArrayList<PointData> empData = features.get(i).getData();
						if (empData.size() > indexToget) {
							Point2D point = empData.get(indexToget);
							dominatepoint
									.setCertainty(computeMainCertainty(
											point, features.get(i).getMax()));
						} else {
							dominatepoint.setCertainty(1.0);
						}
						
					}
					if (j==0){
						max=dominatepoint.getCertainty();
						min = dominatepoint.getCertainty();
					}
					if (dominatepoint.getCertainty() > max)
						max = dominatepoint.getCertainty();
					if (dominatepoint.getCertainty() < min)
						min = dominatepoint.getCertainty();

					// logger.info("---------------------------------put
					// "+type);
					segmentsPoints.put(dominatepoint, dominatepoint
							.getCertainty());

				}
				break;

			}

		}
		


		if (logger.isDebugEnabled()) {
			//  logger.debug("createVertices(Stroke, SortedValueMap, int) -   segmentsPoints     " + functionName + "        " + segmentsPoints.size() + " (" + this.getClass().getSimpleName() + "    " + (new Throwable()).getStackTrace()[0].getLineNumber() + "  )  "); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		}
		if (logger.isDebugEnabled()) {
			//  logger.debug("createVertices(Stroke, SortedValueMap, int) - ************************finish get verticees *******************************"); //$NON-NLS-1$
		}
		// / Velocity of storke Vs. distance
		// Curvature of storke Vs. distance
		// Second Estimation of Curvature of storke Vs. distance
		return normalizeCertaintyValues(segmentsPoints, max, min);
	}

	private static double computeMainCertainty(Point2D pi, double max) {
		double vc = 1.0 - (pi.getY() / max);
		return vc;
	}

	@SuppressWarnings("unchecked")
	private static SortedValueMap normalizeCertaintyValues(SortedValueMap segmentsPoints,
			double max, double min) {
//        logger.info( "  In next i will print certinety with for each function  "+" (" + this.getClass().getSimpleName()
//				+ "    " + (new Throwable()).getStackTrace()[0].getLineNumber()
//				+ "  )  ");
		Set AllElements = segmentsPoints.entrySet();
		
		SortedValueMap newSegmentPoints = new SortedValueMap(
				new NumericalComparator());

		double v;
		double normailizeV;
		if (logger.isDebugEnabled()) {
			//  logger.debug("normalizeCertaintyValues(SortedValueMap, double, double) -  max   = " + max); //$NON-NLS-1$
		}
		for (Iterator iter = AllElements.iterator(); iter.hasNext();) {
			Map.Entry element = (Map.Entry) iter.next();

			v = (Double) element.getValue();

			normailizeV = v / max;
			
			element.setValue(new Double(normailizeV));
			newSegmentPoints.put(element.getKey(), element.getValue());
//			 logger.info( "   old value  "+v+"   the new value is" +
//			 		"  "+element.getValue()  +  "   for the  point "+ element.getKey()+"   of the function  ");
		}
	return 	newSegmentPoints;
    
	}

	/**
	 * CURVATURE SET metric for a curvature candidate vertex vi is the scaled
	 * magnitude of the curvature in a local neighborhood around the
	 * point.computed as |di−k − di+k|/l. Here l is the curve length between
	 * points Si−k, Si+k and k is a small integer defining the neighborhood
	 * size around vi.
	 * 
	 * _______________-i understand this as i have vertix vi and its certianigny
	 * depend on the nieighbours value of curvaute
	 * 
	 * for this i have to create a function whihc will use the function
	 * istatiscial data (curvature) this curncitn will have compute the
	 * certianiy for each point
	 * 
	 */

	private static double computeCurvatureCertainty(Integer i, FeatureFunction function) {

		int k = SystemSettings.STROKE_CONSTANT_CURVATURE_NEIGHBOURS;
		double dik = 0.0, di_k = 0.0, l = 1.0;
		Point2D si_k = null, sik = null,

		ci = function.getData().get(i);
		if ((i - k) > 0)   //not on the edge
			si_k = function.getData().get(i - k);
		if (function.getData().size() > (i + k))
			sik = function.getData().get(i + k);
		if (si_k != null) {
			l = ci.getX() - si_k.getX();
			di_k = si_k.getY();
		}

		if (sik != null) {
			if (l==0.0||l==1.0)
				l = sik.getX() - ci.getX();
			dik = sik.getY();
		}
		if (l==0.0)
			l=1.0;
           
		double cer = (di_k - dik) / l;
//		 logger.info("cer   =  "+cer+" (" + this.getClass().getSimpleName()
//					+ "    "
//					+ (new Throwable()).getStackTrace()[0].getLineNumber()
//					+ "  )  ");
		return cer;
	}

	/***************************************************************************
	 * VELOCITY SET
	 * 
	 * measure of the pen slowdown at the point, 1 − vi/vmax, where vmax is
	 * the maximum pen speed in the stroke. The certainty values are normalized
	 * to [0, 1]
	 * 
	 * 
	 * 
	 */

	private static double computeVelocityCertainty(Point2D vi, double vmax) {

		double vc = 1.0 - (vi.getY() / vmax);
		return vc;
	}

	/***************************************************************************
	 * SCALE
	 * 
	 * scales. As the metrics are used only for ordering within each set, they
	 * need not be numerically comparable across sets. Candidate vertices are
	 * sorted by certainty within each fit.
	 * 
	 * 
	 **************************************************************************/

	/***************************************************************************
	 * 
	 * The initial hybrid fit H0 is the intersection of Fd and Fs. A succession
	 * of additional fits is then generated by appending to Hi the highest
	 * scoring curvature and speed candidates not already in Hi.
	 * 
	 * 
	 * To do this, on each cycle we create two new fits: H''i = Hi+vs (i.e., Hi
	 * augmented with the best remaining speed fit candidate) and H'' i = Hi +
	 * vd (i.e., Hi augmented with the best remaining curvature candidate). We
	 * use least squares error as a metric of the goodness of a fit: the error
	 * εi is
	 * 
	 * 
	 * 
	 **************************************************************************/
	private HybirdFitSolution generateFirstSet(Stroke stroke) {
		// Set pointsCur = segmentsCurvaturePoints.keySet();
		// Set pointVel=segmentsVelocityPoints.keySet();
		HybirdFitSolution solution = new HybirdFitSolution(stroke);
		solution.clearVertices();
		boolean commonsFound=false;		
//		
//		}
		DominatePointStructure  temp=null;
//		DominatePointStructure firstinkv= (DominatePointStructure) segmentsVelocityPoints.getFirstKey();
//		
//		DominatePointStructure firstinkc=  (DominatePointStructure) segmentsCurvaturePoints.getFirstKey();
//		
		if (segmentsCurvaturePoints.getSortedList()==null||segmentsVelocityPoints.getSortedList()==null){
			if (segmentsCurvaturePoints.getSortedList()==null){
				

					 temp = (DominatePointStructure) segmentsVelocityPoints.getFirstKey();
					 
					 solution.addVertix(temp.getIndexInInk());
					 segmentsVelocityPoints.remove(temp);
			}
			else if (segmentsVelocityPoints.getSortedList()==null){
			
				// i have not found any common ground and i will build the polygon fro the first two point on each list.
				 temp = (DominatePointStructure) segmentsCurvaturePoints.getFirstKey();
					solution.addVertix(temp.getIndexInInk());
					segmentsCurvaturePoints.remove(temp);

					
			}
		solution.refineSolution();
		solution.calculateSolutionParameters();
		ErrorThreshold = solution.getStrokeBasedThreshold();
		return solution;
			
		}
		
	//	logger.info(    "<><><><><><<><><><><><><><><<><><><><><><><><<><><><><><><><><><><><><><><>" );
	
		//if (segmentsCurvaturePoints.getSortedList()!=null)
		
		for (Iterator iter = segmentsCurvaturePoints.getSortedList().iterator(); iter
				.hasNext();) {
		//	DominatePointStructure cur = (DominatePointStructure)segmentsCurvaturePoints.getFirstKey();
			DominatePointStructure cur = (DominatePointStructure) ((Map.Entry)iter.next()).getKey();
		
	//	if (segmentsVelocityPoints.getSortedList()!=null)
			for (Iterator iterator = segmentsVelocityPoints.getSortedList().iterator(); iterator
					.hasNext();) {

				DominatePointStructure vel = (DominatePointStructure) ((Map.Entry)iterator.next()).getKey();

			//	logger.info( vel  );
				if ((cur.getIndexInInk() -vel.getIndexInInk())<SystemSettings.STROKE_CONSTANT_NEIGHBOURS) {
				
				//if (cur.getIndexInInk() == vel.getIndexInInk()) {
					// logger.info("remvoing the index of
					// "+vel.getIndexInInk());
					commonsFound=true;
					if (logger.isDebugEnabled()) {
						//  logger.debug("generateFirstSet(Stroke) -    simlar values now addd vertix number " + cur.getIndexInInk() + " (" + this.getClass().getSimpleName() + "    " + (new Throwable()).getStackTrace()[0].getLineNumber() + "  )  "); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
					}
					solution.addVertix(cur.getIndexInInk());
					// this is a dominate poitn in both funcitns add it to the
					// first hybird and remove from sorted maps
					try {
						// logger.info( " the segmetns points are "+
						// pointsCur.size()+" "+pointVel.size()+" (" +
						// this.getClass().getSimpleName()
						// + " " + (new
						// Throwable()).getStackTrace()[0].getLineNumber()
						// + " ) ");
						// if (iter.hasNext())
						segmentsCurvaturePoints.remove(cur);
						segmentsVelocityPoints.remove(vel);
						iter.remove();
						// if (iterator.hasNext())
						iterator.remove();
						break;

					} catch (Exception ex) {
						logger.error("generateFirstSet(Stroke)", ex); //$NON-NLS-1$
					}
					// segmentsVelocityPoints.keySet().iterator().remove();
					// segmentsCurvaturePoints.keySet().iterator().remove();
					// segmentsCurvaturePoints.remove(cur);
					// segmentsVelocityPoints.remove(vel);

				}

			}

		}
		if (commonsFound==false){
			// i have not found any common ground and i will build the polygon fro the first two point on each list.
		temp = (DominatePointStructure) segmentsCurvaturePoints.getFirstKey();
			if (temp!=null){
				solution.addVertix(temp.getIndexInInk());
				segmentsCurvaturePoints.remove(temp);
			}
			else 
			{
				solution.addVertix(0);
			}
				 temp = (DominatePointStructure) segmentsVelocityPoints.getFirstKey();
					if (temp!=null){
				 solution.addVertix(temp.getIndexInInk());
				 segmentsVelocityPoints.remove(temp);
					}
					else 
					{
						solution.addVertix(0);
					}
					
		}
	
		
		solution.refineSolution();
		solution.calculateSolutionParameters();
		if (solution.getPolygonVertices().size()<4)
		{
			addTopVertics(solution);
			solution.calculateSolutionParameters();
		}
		ErrorThreshold = solution.getStrokeBasedThreshold();
		return solution;
	}

	private void addTopVertics(HybirdFitSolution hc) {
		
		DominatePointStructure pc = (DominatePointStructure) segmentsCurvaturePoints
		.getFirstKey();

DominatePointStructure pV = (DominatePointStructure) segmentsVelocityPoints
		.getFirstKey();

if ((pc == null) && (pV == null)) {
	return ;
}

if (pc != null) {

	hc.addVertix(pc.getIndexInInk());
	segmentsCurvaturePoints.remove(pc);
	
	
}

if (pV != null) {

	
	hc.addVertix(pV.getIndexInInk());
	segmentsVelocityPoints.remove(pV);
	
}
	}

	/***************************************************************************
	 * 
	 * Alogrithm in steps 1. compute curvature and speed data for each poin
	 * along the stroke 2. get vertics from the averge filtering procedure. 3.
	 * for each vertic compute the certiainty of them using next procedure then
	 * normalize them between o,1 a) vertix certainty ==> 1 − vi/vmax b)
	 * Curvature certainity ===> |di−k − di+k|/l. 4. normalize the
	 * certianity between 0,1 5. sort vetices according to there certaingiy 6)
	 * creat list ho which have higher certainty vertices in both sets of fs and
	 * fd. 7) loop on remaiing vertics till finish and do the following a)
	 * create his = hi+ next vetix in fs b) create hid = hi+ next vetix in fd c)
	 * compute the distance (orthcognal from each poin on the stroke to
	 * resutlant polygon of his and hif) d)lowes is the next hi
	 * 
	 * 8) we now have a set of best fit hi we just need to get the min error in
	 * them then 9) shoose the one with the less thena error with minimum number
	 * of vertices
	 * 
	 * 
	 **************************************************************************/

	public HybirdFitSolution runAlogrithm(Stroke stroke) {
		//segmentsCurvaturePoints ;
		;
		//DrawingDebugUtils.DisplayChartsFrames(stroke);
		segmentsCurvaturePoints=	createVertices(stroke,null , 0);
		segmentsVelocityPoints=createVertices(stroke, null, 1);
		boolean loop = true;
		HybirdFitSolution hi = generateFirstSet(stroke);
		HybirdFitSolution hc, hv;
		HybirdFitList = new SortedValueMap(new NumericalComparator());
		HybirdFitList.put(hi, hi.error());
		double testc = 0, testv = 0;

		for (int i = 0; loop; i++) {

			hc = (HybirdFitSolution) hi.clone();
			hv = (HybirdFitSolution) hi.clone();
			// logger.info("i = "+i+" size ==
			// "+segmentsCurvaturePoints.size());
			DominatePointStructure pc = (DominatePointStructure) segmentsCurvaturePoints
					.getFirstKey();

			DominatePointStructure pV = (DominatePointStructure) segmentsVelocityPoints
					.getFirstKey();

			if ((pc == null) && (pV == null)) {
				break;
			}
			
			if (pc != null) {
			
				hc.addVertix(pc.getIndexInInk());
				hc.calculateSolutionParameters();
				testc = hc.error();
			}

			if (pV != null) {

			
				hv.addVertix(pV.getIndexInInk());
				hv.calculateSolutionParameters();
				testv = hv.error();

			}	
			
//                got the info i need now i will need to           //////////////////////////////////////////////
			
			if ((pc != null) && (pV != null)) {
				if (testc > testv) {
					// hv is the one
					segmentsVelocityPoints.remove(pV);
					hi = (HybirdFitSolution) hv.clone();
					HybirdFitList.put(hv, testv);

				} else if (testv > testc) { // hc is the smallest
					segmentsCurvaturePoints.remove(pc);
					hi = hc;
					HybirdFitList.put(hc, testc);

				} else {
					segmentsVelocityPoints.remove(pV);
					segmentsCurvaturePoints.remove(pc);
					hi = (HybirdFitSolution) hc.clone();
					HybirdFitList.put(hv, testv);
					HybirdFitList.put(hc, testc);
				}
			}
	 else {  // either pc or pv is null       

				if (pc != null) {
					segmentsCurvaturePoints.remove(pc);
					hi = (HybirdFitSolution) hc.clone();
					HybirdFitList.put(hc, testc);
				}

				if (pV != null) {
					segmentsVelocityPoints.remove(pV);
					hi = (HybirdFitSolution) hv.clone();
					HybirdFitList.put(hv, testv);

				}

			}

		}

		HybirdFitSolution Sol = getBestFitFromHybirdList();

		Sol = this.runAlogrithmCurve(Sol);
		Sol.CheckLineSlopes();
		return Sol;
		// double error=first.getError();
	}

	private HybirdFitSolution getBestFitFromHybirdList() {

		int minV = Integer.MAX_VALUE;
		int vertixcount = 0;
		HybirdFitSolution minsol = (HybirdFitSolution) HybirdFitList
				.getFirstKey();

	//	int count = HybirdFitList.values().size();
		HybirdFitSolution testEntry;
		
		// we want to get e0 and e_all
		
		 Set list = HybirdFitList.keySet();
	int maxv=0,minv=Integer.MAX_VALUE;
	double e_all=0,e_0=0;
	double mean=0;
	
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			HybirdFitSolution element = (HybirdFitSolution) iter.next();
			int countv=element.getPolygonVertices().size();
			mean+=element.error();
			if (countv>maxv)
			{
				maxv=countv;
				e_all=element.error();
				
			}
			if (countv<minv)
			{
				minv=countv;
				e_0=element.error();
				
			}
			
			
			
		}
		mean/=list.size();
		
		
		//.1*(e0-eall) + eall 

		double error_T=0.1*(e_0+e_all) + e_all; 
		//double emye=0.5*(mean+ErrorThreshold)+e_all;
	
		ErrorThreshold=  (error_T);
		//ErrorThreshold=  (emye+error_T)/2.0;
		

		if (logger.isDebugEnabled()) {
			//  logger.debug("getBestFitFromHybirdList() - error_t " + error_T + " my eerrror " + emye + "  ErrorThreshold  " + ErrorThreshold + "  (" + this.getClass().getSimpleName() + "    " + (new Throwable()).getStackTrace()[0].getLineNumber() + "  )  "); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
		}
		
		
		while ((testEntry = (HybirdFitSolution) HybirdFitList.getFirstKey()) != null) {
			HybirdFitList.remove(testEntry);
			double temp = testEntry.error();
			
			
			if (temp < ErrorThreshold) {

				// it under trshold now get the min value of vertixcest
				vertixcount = testEntry.getPolygonVertices().size();
				
				

				if (logger.isDebugEnabled()) {
					//  logger.debug("getBestFitFromHybirdList() -  Vertix count is equalll  " + vertixcount + "  the eroor was " + temp + " (" + this.getClass().getSimpleName() + "    " + (new Throwable()).getStackTrace()[0].getLineNumber() + "  )  "); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
				}
				if (vertixcount < minV) {
					minsol = testEntry;
					minV = vertixcount;

					// logger.info("Changing the min with "+minV+" error
					// is = "+temp);
				}

			}
			else {
				// it under trshold now get the min value of vertixcest
				vertixcount = testEntry.getPolygonVertices().size();
				
//				
				if (logger.isDebugEnabled()) {
					//  logger.debug("getBestFitFromHybirdList() -  Vertix  " + vertixcount + "  the eroor was " + temp + " (" + this.getClass().getSimpleName() + "    " + (new Throwable()).getStackTrace()[0].getLineNumber() + "  )  "); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
				}
			}

		}

		// get first element

		// check its vertices size
		// 
		if (logger.isDebugEnabled()) {
			//  logger.debug("getBestFitFromHybirdList() - With error tolerance =  " + ErrorThreshold + " Vertix count is equalll  " + minsol.getPolygonVertices().size() + "  the eroor was " + minsol.error() + " (" + this.getClass().getSimpleName() + "    " + (new Throwable()).getStackTrace()[0].getLineNumber() + "  )  "); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
		}
		if (logger.isDebugEnabled()) {
			//  logger.debug("getBestFitFromHybirdList() - ######################################################"); //$NON-NLS-1$
		}
		return minsol;

	}

	/***************************************************************************
	 * 
	 * 
	 * The polyline approximation Hf generated in the process described above
	 * provides a natural foundation for detecting areas of curvature: we
	 * compare the Euclidean distance l1 between each pair of consecutive
	 * vertices in Hf to the accumulated arc length l2 between those vertices in
	 * the input S. The ratio l2/l1 is very close to 1 in the linear regions of
	 * S, and significantly higher than 1 in curved regions.
	 * 
	 *  // this mean in the final line if " lenght of arc / lenght of line > 1
	 * then it is moslty a curve "
	 * 
	 * 
	 */

	/***************************************************************************
	 * v=first point i , u =second point j c1 = kˆt1 + v ==> first control c2 =
	 * kˆt2 + u ====> second control k =1/3*(Sum(|Sk − Sk+1|))
	 * 
	 * 
	 * where ˆt1 and ˆt2 are the unit length tangent vectors pointing
	 * 
	 * inwards at the curve segment to be approximated. The 1/3 factor in k
	 * controls how much we scale ˆ t1 and ˆ t2 in order to reach the control
	 * points; the summation is simply the length of the chord between Si and Sj
	 * 
	 * 1) get the sum of points from si to sj 2) get the unit vector of tangent
	 * from pont v 3) get the univ vecto rof tange tpoin of vector 4) compute
	 * the control points 5) compute the approximateion
	 * pcicewise=================================> need to find how 6) compute
	 * errror from approxlimation to origingla 7) if under error bound split the
	 * curve in midle point and repeat from one
	 * 
	 * 
	 * 
	 * We discretize the B´ezier curve using a piecewise linear curve and
	 * compute the error for that curve. This error computation is O(n) because
	 * we impose a finite upper bound on the number of segments used in the
	 * piecewise approximation.
	 * 
	 * 
	 * 
	 * If the error for the B´ezier approximation is higher than our maximum
	 * error tolerance, the curve is recursively subdivided in the middle, where
	 * middle is defined as the data point in the original stroke whose index is
	 * midway between the indices of the two endpoints of the original B´ezier
	 * curve. New control points are computed for each half of the curve, and
	 * the process continues until the desired precision is achieved.
	 * 
	 * 
	 * 
	 **************************************************************************/
	public HybirdFitSolution runAlogrithmCurve(HybirdFitSolution sol) {

		/***********************************************************************
		 * 
		 * 
		 * The polyline approximation Hf generated in the process described
		 * above provides a natural foundation for detecting areas of curvature:
		 * we compare the Euclidean distance l1 between each pair of consecutive
		 * vertices in Hf to the accumulated arc length l2 between those
		 * vertices in the input S. The ratio l2/l1 is very close to 1 in the
		 * linear regions of S, and significantly higher than 1 in curved
		 * regions.
		 * 
		 *  // this mean in the final line if " lenght of arc / lenght of line >
		 * 1 then it is moslty a curve "
		 * 
		 * 
		 */
		sol.InitSegments();
		int oldCount=sol.getPolygonVertices().size();
		int nexti=0;
//        logger.info(" coutn of vertiss "+sol.getPolygonVertices().size()+" (" + this.getClass().getSimpleName()
//				+ "    " + (new Throwable()).getStackTrace()[0].getLineNumber()
//				+ "  )  ");
		for (int i = 0; i < sol.getParticlePoints().length; i++) {
			if (sol.getParticlePoints()[i] == 1) {
				
				if (sol.IsCurveSegment(i)) {
				
//					logger.info("Create curve  "+i+" ("
//							+ this.getClass().getSimpleName()
//							+ "    "
//							+ (new Throwable()).getStackTrace()[0]
//									.getLineNumber() + "  )  ");
					sol.CreateBezireSegment(i);
					// check if i have divided and annd some point 
					
					if (sol.getPolygonVertices().size()>oldCount)
					{
						// i added vertices by dividng the curve. 
						//now get the last vertix that was vertix 
					       i=sol.getSegmentLastVertix()-1;	
						
						
						
						
					}
					
					
				} else {
					
//					logger.info("Create  line "+i+" ("
//							+ this.getClass().getSimpleName()
//							+ "    "
//							+ (new Throwable()).getStackTrace()[0]
//									.getLineNumber() + "  )  ");
					sol.CreateLineSegment(i);

				}

			}

		}// for
		
		sol.computeSegmentError();
		
//		logger.info("_______________________________________ segments count  =  " + sol.getSegmentsCount()
//				+ "   (" + this.getClass().getSimpleName() + "    "
//				+ (new Throwable()).getStackTrace()[0].getLineNumber()
//				+ "  )  ");

		//if (logger.isDebugEnabled()) {
			logger.warn("runAlogrithmCurve(HybirdFitSolution) -  // TODO check if i can merge two consective segments into one line example if they have same slope .    (" + this.getClass().getSimpleName() + "    " + (new Throwable()).getStackTrace()[0].getLineNumber() + "  )  "); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		//}

		return sol;

	}

}
