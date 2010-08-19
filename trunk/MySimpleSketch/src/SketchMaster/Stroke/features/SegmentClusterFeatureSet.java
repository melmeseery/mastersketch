/**
 * 
 */
package SketchMaster.Stroke.features;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import SketchMaster.Stroke.StrokeData.InkInterface;
import SketchMaster.Stroke.StrokeData.PointData;
import SketchMaster.Stroke.StrokeData.Segment;
import SketchMaster.Stroke.StrokeData.SegmentCluster;
import SketchMaster.Stroke.StrokeData.SimpleInkObject;
import SketchMaster.Stroke.StrokeData.Stroke;
import SketchMaster.Stroke.graphics.shapes.BezireCurve;
import SketchMaster.Stroke.graphics.shapes.Circle;
import SketchMaster.Stroke.graphics.shapes.Ellipse;
import SketchMaster.Stroke.graphics.shapes.GeometricPrimitive;
import SketchMaster.Stroke.graphics.shapes.Line;
import SketchMaster.lib.ComputationsGeometry;
import SketchMaster.lib.ZernikeFE;
import SketchMaster.system.SketchSheet;
import SketchMaster.system.SystemSettings;

/**
 * @author maha
 * 
 */
public class SegmentClusterFeatureSet implements InkFeatureSet, Serializable {
	transient ArrayList<SegmentClusterFeature> Features = null;
	private static final  Logger logE=Logger.getLogger("ExampleLogging");;
	private static final Logger logger = Logger.getLogger(SegmentClusterFeatureSet.class);
	SegmentCluster inkCluster;
	static double ZERO_REPLACMENT=-1;

	/***************************************************************************
	 * Features 1. number of primitives ---------- done 2. number of lines ,
	 * ---------------done (number of strokes ) 3. number of curves - arcs ,
	 * -----------done 4. number of ellipses ----------------done 5. number of
	 * parralles lines. ----------------50 % done 6. number of perpendicular
	 * lines ----------------50 % done 7. number of intersecitons at t , x , l
	 * seciotn start end mid point (divide it to line , line , (line curve ))
	 * ----------------50 % done 8. bounding box 9. center fo gravity (average
	 * ox , average y 10.larger triangle. 11. convex hull------------done 12.
	 * ink density 13. area 14. string ontaing order primitve tempatel lelele
	 **************************************************************************/

	/**
	 * @author maha the feature f1 : convex hull
	 */
	/***************************************************************************
	 * the feature f1 : number of primitives
	 */

	public void computeFeatures() {
		if (Features == null) {
			initAll();
		}
		for (int i = 0; i < Features.size(); i++) {
			Features.get(i).getValues();
		//	Features.get(i).getValue();
		}
		
		
	}
	public SVMFeatureSet computeSVMFeatures(){
		//TODO:  call this method with the feature now to add this set to the features et. 
		if (Features == null) {
			initAll();
		}
		int countFeatures=0;
		for (int i = 0; i < Features.size(); i++) {
			Features.get(i).getValues();
			countFeatures+=Features.get(i).getValues().length;
		//	Features.get(i).getValues();
		//	Features.get(i).getValue();
			
			
		}
		
		double tempValues[]=new double[countFeatures];
		String names[]=new String [countFeatures];
		int j=0;
		
		for (int i = 0; i < Features.size(); i++) {
//		if (Features.get(i).NoOfValues()>1)
//		{
		 
			double[] tempV = Features.get(i).getValues();
			String nameF=Features.get(i).getName();
			String[] namesfl=Features.get(i).getNames();
			
		//	logger.error("Now in feature s  "+nameF);
			for (int k = 0; k < tempV.length; k++) {
				names[j+k]=namesfl[k];//+" "+k;
				tempValues[j+k]=tempV[k];
	
				if (Double.isNaN(tempV[k]))
				{
					
					logger.fatal("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFf  i= "+i+"  ");
				}
			
		//	}
		
			
			
		}
			j+= Features.get(i).getValues().length;
//		else{
//			tempValues[j]=Features.get(i).getValue();
//			names[j]=Features.get(i).getName();
//			j++;
//		}
		
		
		}
		
		
		SVMFeatureSet tempFeatureSet=new SVMFeatureSet(tempValues,names);
		return tempFeatureSet;
		
			
	}

	public void computeFeatures(InkInterface ink) {
		this.computeFeatures();
	}

	public ArrayList<SegmentClusterFeature> getFeatures() {

		return Features;
	}

	public InkInterface getInk() {
		InkInterface returnInkInterface = this.inkCluster.getInkPath();
		return returnInkInterface;

	}

	
	
	
	public void initAll() {
		//logger.error(" IN THE INIT ALLLLLLLLLLLLLLLLLLLLLLL");
		
		//logger.error( SystemSettings.SYMBOL_FEATURES_PRIMITIVES);
		// logger.info("Tod do init all ");
		Features = new ArrayList<SegmentClusterFeature>();

		SegmentClusterFeature temp;
		if(SystemSettings.SYMBOL_FEATURES_PRIMITIVES)
		{
			// 0
		temp = new PrimitiveCount();
		temp.setData(this.inkCluster);
	//	logger.error(inkCluster);
		temp.setName("primitive count");
		Features.add(temp);
		}
//		// 8
		if (SystemSettings.SYMBOL_FEATURES_PRIMITIVE_COUNT){
	
			
			if (SystemSettings.SYMBOL_FEATURES_PRIMITIVE_COUNT_LINE)
			{		// 1
		temp = new LineCount();
		temp.setName("Line count");
		temp.setData(this.inkCluster);
		Features.add(temp);
			}
		if (SystemSettings.SYMBOL_FEATURES_PRIMITIVE_COUNT_CURVE)
		{// 2
		temp = new CurvesCount();
		temp.setData(this.inkCluster);
		temp.setName("Curves count");
		Features.add(temp);
		}
		if (SystemSettings.SYMBOL_FEATURES_PRIMITIVE_COUNT_ELLIPSE)
		{// 4
		temp = new ellipseCount();
		temp.setData(this.inkCluster);
		temp.setName("Ellipse count");
		Features.add(temp);
		}
		}
	if (SystemSettings.SYMBOL_FEATURES_CONNECTIONS_COUNT)
	{
		if(SystemSettings.SYMBOL_FEATURES_CONNECTIONS_COUNT_TYPE) {
		//8
		temp = new IntersectionLTypeCount();
		temp.setName(" Intersection type  count");
		temp.setData(this.inkCluster);
		Features.add(temp);
		}
		// 5
		temp = new ParallelCount();
		temp.setName("Parallel count");
		temp.setData(this.inkCluster);
		Features.add(temp);
		// 6
		temp = new PerPendicularCount();
		temp.setName("Perpendicular  count");
		temp.setData(this.inkCluster);
		Features.add(temp);
		// 7
		temp = new IntersectionLinesCount();
		temp.setName("Intersections count");
		temp.setData(this.inkCluster);
		Features.add(temp);
		
		
		// 8
		temp = new MinMaxRadius();
		temp.setName("Min Max Radius count");
		temp.setData(this.inkCluster);
		Features.add(temp);
		
		
	}

	if (SystemSettings.SYMBOL_FEATURES_CENTROID)
	{
//		// 9
		temp = new Centroid();
		temp.setName("Center of gravity ");
		if (this.inkCluster.getInkPath()!=null)
		temp.setData(this.inkCluster.getInkPath().getPoints());
		Features.add(temp);
	}
	if (SystemSettings.SYMBOL_FEATURES_CONVEXHULL)
	{
		// 10
		temp = new ConvexHull();
		temp.setName("Convex hull Features");
		
		if (this.inkCluster.getInkPath()!=null)
		{
			ArrayList tempa=new ArrayList();
			tempa.add(this.inkCluster.getInkPath().getPoints());
			tempa.add(new Double (this.inkCluster.getArea()));
			temp.setData(tempa);
		}
		Features.add(temp);
	}
	if (SystemSettings.SYMBOL_FEATURES_AREA)
	{
		
//		 11
		temp = new Area();
		temp.setName("Area");
		if (this.inkCluster.getInkPath()!=null)
		temp.setData(this.inkCluster.getInkPath());
		Features.add(temp);
	
	}
	if (SystemSettings.SYMBOL_FEATURES_RATIOS)
	{
		 
		
		// feature 12 
		temp = new Ratio();
		temp.setName(" Ration betweeen w / h ");
		if (this.inkCluster.getInkPath()!=null)
		temp.setData(this.inkCluster.getInkPath());
		Features.add(temp);
		
		 //features again for 
		 temp= new RatioLog();
		 temp.setName(" Aspect ration log");
			if (this.inkCluster.getInkPath()!=null)
				temp.setData(this.inkCluster.getInkPath());
		 Features.add(temp);
		 
	}
		// feature 13 
		if(SystemSettings.SYMBOL_FEATURES_ZENERIK_MOMEMENTS){
		temp=new ZenerikMomentFeature();
		temp.setName("zernike moments ");
		 // now create a 
		ArrayList<Stroke> listStroke = inkCluster.getStrokeInSymbol();
		//logger.info( "  the live stroke is ");
	//logger.info(listStroke.size());
		Stroke[] arrayStroke=new Stroke[listStroke.size()];
		for (int i = 0; i < arrayStroke.length; i++) {
			 arrayStroke[i]=listStroke.get(i);
		}
		temp.setData(arrayStroke);
		Features.add(temp);
		}
		
		// feature 14- 26 == rubine features 
		if (SystemSettings.SYMBOL_FEATURES_RUBINE_FEATURES){
		 StrokeRubineFeatureSet  rubinesFeatures=new StrokeRubineFeatureSet(inkCluster.getInkPath());
		 rubinesFeatures.initAll();
		 temp= new RubineFeatures();
		 temp.setName("rubine features ");
		 temp.setData(rubinesFeatures);
		 Features.add(temp);
		}
//		 

//		 
		if (SystemSettings.SYMBOL_FEATURES_LOGSAT)
		{	 
	
		 
		 temp= new AreaComputedLog();
		 temp.setName(" Area computed from ink log ");
			if (this.inkCluster.getInkPath()!=null)
			temp.setData(this.inkCluster.getInkPath());
		 Features.add(temp);
		 

			
		 temp= new areaEachStroke();
		 temp.setName(" Area of each stroke with log");
			
			temp.setData(new Double (this.inkCluster.getArea()));
		 Features.add(temp);
		}
//		 
		if (SystemSettings.SYMBOL_FEATURES_DENSITY)
		{
		 temp= new Density();
		 temp.setName(" Ink density (length / size)");
		 if (this.inkCluster.getInkPath()!=null)
				temp.setData(this.inkCluster.getInkPath());
		 Features.add(temp);
		}
		
		
		if (SystemSettings.SYMBOL_FEATURES_COMPOSITE){
			
			temp = new CentroidalRadius();
			temp.setName("Centroid Radius Functions ");
			if (this.inkCluster.getInkPath()!=null)
			temp.setData(this.inkCluster.getInkPath().getPoints());
			Features.add(temp);
		}
		
		
		
		
	}
/**
 * 
 * The features we use in the experiments
Features Descriptions Characteristics
Rubine features [6] Eleven geometric features and --------------
2 dynamical features
Perform well in gesture recognition
Curvature [7] Cosine of the angle formed by --------------
three consecutive points
Typical Feature used in stroke segmentation
Pen speed [17] Pen moving speed The same as above---------------
Normalized curvature [18] A reformative type of curvature Invariant with respect to the three factors
of geometric distortion
Modified turning function [3] A function of the length of the
curves composing a symbol
Irrelevant to polygon’s size, position, and
rotation direction. We choose 20 points that
uniformly distribute on the sketchy strokes
Centroidal radius Distance between the centroid and
each point in the point sequence
Employed to the online drawing recognition
based on neural network
Compositional features See in Table 2 Adaptive to SVM and HMM methods for
some rather simple strokes




Table 2
The constitution of compositional features
Features Feature descriptions Remarks
F1 Mean of centroidal radius (1) Cross-speed means the ratio of the distance between the current
point and the previous point to the drawing time used
(2) Cross-curvature means the cosine of the angle formed by the
previous point, current point and the following one
(3) Pen direction indicates the slope of the dashed line between the
end point of the previous stroke and the start point of the current stroke
F2 Standard variance of centroidal radius 
F3 Means of cross-speed
F4 Standard variances of cross-speed
F5 Means of cross-curvature
F6 Standard variances of cross-curvature
F7 Pen direction
1140 L. Zhang, Z. Sun / Applied Mathematics and Computation 185 (2007) 1138–1148
 * 
 * ***/
	public boolean IsIndexRepeated(ArrayList<Point> list, int i1, int i2) {
		//
		// logger.info("Checking the repreted index "+" (" +
		// this.getClass().getSimpleName()
		// + " " + (new Throwable()).getStackTrace()[0].getLineNumber()
		// + " ) ");

		for (int i = 0; i < list.size(); i++) {

			// logger.info(list.get(i));

			if (list.get(i).x == i1) // if any stored index is similar to the
			// first
			{
				if (list.get(i).y == i2) // chceck the other index
					// if same then return true for found .
					return true;
				// else contnue to test,
			}
			if (list.get(i).y == i1) {
				if (list.get(i).x == i2)
					return true;
			}
			if (list.get(i).x == i2) {
				if (list.get(i).y == i1)
					return true;
			}
			if (list.get(i).y == i2) {
				if (list.get(i).x == i1)
					return true;
			}
		}
		return false;
	}

	// /***********************************************************************************************************
	/***************************************************************************
	 * Features calculations
	 **************************************************************************/
	// /***********************************************************************************************************
	/**
	 * *Feature 1 : Primitive count = count the number of primitve in a cluster.
	 * 
	 * @author maha
	 * 
	 */
	class PrimitiveCount extends SegmentClusterFeature {

		ArrayList<Segment> segments = null;

		public PrimitiveCount (){
			
			Names=new String [2];
			Names[0]="Number of primitives";
			Names[1]="Number of segments";
			
		}
		public double Value() {
			if (segments != null) {
				int lineCount = 0;
				int biezierCount = 0;
				int curveCount = 0;
				int ellipseCount = 0;
				int primitveCount = (int)ZERO_REPLACMENT;
				for (int i = 0; i < segments.size(); i++) {
					// / now if count of each of the primitive type
			//		logger.info(segments.get(i).getSegmentType());
					if (segments.get(i).getSegmentType() instanceof Line) {
					  
						lineCount++;
						primitveCount=0;
					   //  logger.info(" line count = "+ lineCount);

					}
					if (segments.get(i).getSegmentType() instanceof Ellipse) {

						ellipseCount++;
						primitveCount=0;
					   //  logger.info("  ellipseCount  = "+ ellipseCount);

					}
					if (segments.get(i).getSegmentType() instanceof BezireCurve) {

						biezierCount++;
						primitveCount=0;
						//   logger.info("  biezierCount  = "+ biezierCount);

					}
					else if (segments.get(i).getSegmentType() instanceof Circle) {

						curveCount++;
						primitveCount=0;
					}
				

				}

				// after each type is counted i count how many differnt
				// primitive
				if (lineCount > 0)
					primitveCount++;
				if (ellipseCount > 0)
					primitveCount++;
				if (biezierCount > 0)
					primitveCount++;
				if (curveCount > 0)
					primitveCount++;
      //        logger.info("  primitive count = "+primitveCount);
				// / now save the count as the the value
				this.Value = primitveCount;
				this.ValueOk = true;
				this.computed = true;
//	if (Double.isNaN(Value)||this.Value==0){
//					
//					logger.fatal("Error while calcuation.. Primitive................."+Value);
//					
//				}
				//logger.fatal("Va")
				return Value;
	
			}
			logger.fatal(" did not compute the .. Primitive................."+Value);
			this.Value = ZERO_REPLACMENT;
			this.ValueOk = false;
			this.computed = false;
			return 0;
		}

		@Override
		void setData(Object data) {
			this.ValueOk = false;
			this.computed = false;
			if (data instanceof ArrayList) {
				segments = (ArrayList<Segment>) data;
				//logger.info(segments.get(0).getSegmentType());
			}
		}

		@Override
		public double[] Values() {
			int segsize=0;
			if (segments != null) {
		 
				int primitveCount = (int)ZERO_REPLACMENT;
				segsize=segments.size();
				}
			
			
			Values = new double[2];
			Values[0] = this.Value();
			Values[1]= segsize;
			return Values;
		}

	}

	/** ------------------------------------------------------------------------------------ */
	/** *------------------------------------------------------------------** */
	/**
	 * Feature 2 : Line count = count the number of lines in a cluster. *
	 * 
	 * @author maha
	 * 
	 */
	class LineCount extends SegmentClusterFeature {

		public LineCount(){
			
			Names=new String [1];
			Names[0]="Line count";
		}
		ArrayList<Segment> segments = null;


		public double Value() {
			if (segments != null) {
				int lineCount = (int)ZERO_REPLACMENT;

				for (int i = 0; i < segments.size(); i++) {
					// / now if count of each of the primitive type

					if (segments.get(i).getSegmentType() instanceof Line) {
                              if(lineCount==ZERO_REPLACMENT)
                            	  lineCount=0;
						lineCount++;

					}

				}

				// / now save the count as the the value
				this.Value = lineCount;
//				if (this.Value==0)
//					Value=ZERO_REPLACMENT;
				this.ValueOk = true;
				this.computed = true;
				return Value;

			}

			this.Value = ZERO_REPLACMENT;
			this.ValueOk = false;
			this.computed = false;
			return 0;
		}

		@Override
		void setData(Object data) {
			this.ValueOk = false;
			this.computed = false;
			if (data instanceof ArrayList) {
				segments = (ArrayList<Segment>) data;

			}
		}

		@Override
		public double[] Values() {
			Values = new double[1];
			Values[0] = this.Value();
			return Values;
		}

	}

	/** ------------------------------------------------------------------------------------ */
	/** *------------------------------------------------------------------** */
	/**
	 * Feature 3 : Curve count = count the number of curves or arcs in a
	 * cluster.
	 * 
	 * @author maha
	 * 
	 */
	class CurvesCount extends SegmentClusterFeature {

		ArrayList<Segment> segments = null;

	    public CurvesCount(){
	    	Names=new String [1];
	    	Names[0]="Curves Count";
	    	
	    }
		public double Value() {
			if (segments != null) {

				int biezierCount = (int)ZERO_REPLACMENT;
				int curveCount = (int)ZERO_REPLACMENT;
				int EllCount= (int)ZERO_REPLACMENT;
			

				for (int i = 0; i < segments.size(); i++) {
					// / now if count of each of the primitive type

					if (segments.get(i).getSegmentType() instanceof BezireCurve) {

						if (biezierCount==ZERO_REPLACMENT)
							biezierCount=0;
						biezierCount++;

					}
					else if (segments.get(i).getSegmentType() instanceof Circle) {

						if (curveCount==ZERO_REPLACMENT)
						curveCount=0;
						curveCount++;
					}
					else if (segments.get(i).getSegmentType() instanceof Ellipse) {

						if (EllCount==ZERO_REPLACMENT)
						EllCount=0;
						EllCount++;
					}

				}

				if (curveCount>0||biezierCount>0||EllCount>0)
				{
					if (curveCount== ZERO_REPLACMENT)
						curveCount=0;
					if (biezierCount== ZERO_REPLACMENT)
						biezierCount=0;
					if (EllCount== ZERO_REPLACMENT)
						EllCount=0;
				}
				
				///if (curveCount=)
				// / now save the count as the the value
				this.Value = curveCount + biezierCount+EllCount;
//				if (Value==0)
//					Value=ZERO_REPLACMENT;
				
				
				this.ValueOk = true;
				this.computed = true;
				
//				if (Double.isNaN(Value)){
//					
//					logger.fatal("Error while calcuation....Curve..............."+Value);
//					
//				}
				return Value;

			}
			logger.fatal(" did not compute the .. Primitive................."+Value);
			this.Value = ZERO_REPLACMENT;
			this.ValueOk = false;
			this.computed = false;
			return 0;
		}

		@Override
		void setData(Object data) {
			this.ValueOk = false;
			this.computed = false;
			if (data instanceof ArrayList) {
				segments = (ArrayList<Segment>) data;

			}
		}

		@Override
		public double[] Values() {
			Values = new double[1];
			Values[0] = this.Value();
			return Values;
		}

	}

	/** ------------------------------------------------------------------------------------ */
	/** *------------------------------------------------------------------** */
	/**
	 * Feature 4 : ellipse count = count the number of ellipses or circles in a
	 * cluster.
	 * 
	 * @author maha
	 * 
	 */
	class ellipseCount extends SegmentClusterFeature {

		ArrayList<Segment> segments = null;
	    public ellipseCount(){
	    	Names=new String [1];
	    	Names[0]="Ellipse Count";
	    	
	    }

		public double Value() {
			if (segments != null) {

				int ellipseCount = (int) ZERO_REPLACMENT;

				for (int i = 0; i < segments.size(); i++) {
					// / now if count of each of the primitive type

					if (segments.get(i).getSegmentType() instanceof Ellipse) {

						if (	ellipseCount==	ZERO_REPLACMENT)
							ellipseCount=0;
						ellipseCount++;

					}

				}

				// / now save the count as the the value
				this.Value = ellipseCount;
//				if (Value==0)
//					Value=ZERO_REPLACMENT;
				this.ValueOk = true;
				this.computed = true;
				return Value;

			}

			this.Value = ZERO_REPLACMENT;
			this.ValueOk = false;
			this.computed = false;
			return 0;
		}

		@Override
		void setData(Object data) {
			this.ValueOk = false;
			this.computed = false;
			if (data instanceof ArrayList) {
				segments = (ArrayList<Segment>) data;

			}
		}

		@Override
		public double[] Values() {
			Values = new double[1];
			Values[0] = this.Value();
			return Values;
		}

	}

	/** ------------------------------------------------------------------------------------ */
	/** *------------------------------------------------------------------** */
	/**
	 * Feature 5 : Parallel count = count the number of parallel line in a
	 * cluster.
	 * 
	 * @author maha
	 * 
	 */
	class ParallelCount extends SegmentClusterFeature {

		ArrayList<Segment> segments = null;

	    public ParallelCount(){
	    	Names=new String [1];
	    	Names[0]="Parallel Count";
	    	
	    }
		public double Value() {
			if (segments != null) {
				ArrayList<Point> list = new ArrayList<Point>();

				Line l1, l2;

				int parallelCount = (int) ZERO_REPLACMENT;
				for (int i = 0; i < segments.size(); i++) {

					if (segments.get(i).getSegmentType() instanceof Line) {

						l1 = (Line) segments.get(i).getSegmentType();

						for (int j = i + 1; j < segments.size(); j++) {

							if (segments.get(j).getSegmentType() instanceof Line) {
								
								l2 = (Line) segments.get(j).getSegmentType();
								
								if (!l1.equals(l2))
									if (l1.isParallel(l2)) {
									//	if (!l1.isIntersectedLine(l2)) {  // changed from 
											//go 
										if (SystemSettings.DEBUG_MODE){
											logE.info(" "+l1+"  is parallel to "+l2);
											             // logE.info(")
											}
												if (!l1.isIntersect(l2)) {  
											if (!IsIndexRepeated(list, i, j)) {
												if (parallelCount==ZERO_REPLACMENT)
													parallelCount=0;
												
												
												parallelCount++;

												Point tempindex = new Point(i,j);
												list.add(tempindex);
											}
										}
									}

							}

						}

					}

				}

				// / now save the count as the the value
				this.Value = parallelCount;
//				if (Value==0)
//					Value=ZERO_REPLACMENT;
				this.ValueOk = true;
				this.computed = true;
				return Value;

			}

			this.Value = ZERO_REPLACMENT;
			this.ValueOk = false;
			this.computed = false;
			return 0;
		}

		@Override
		void setData(Object data) {
			this.ValueOk = false;
			this.computed = false;
			if (data instanceof ArrayList) {
				segments = (ArrayList<Segment>) data;

			}
		}

		@Override
		public double[] Values() {
			Values = new double[1];
			Values[0] = this.Value();
			return Values;
		}

	}

	/** *------------------------------------------------------------------** */
	/** ------------------------------------------------------------------------------------ */
	/**
	 * Feature 6 : Perpendicular count = count the number of perpendicular lines
	 * in a cluster.
	 * 
	 * @author maha
	 * 
	 */
	class PerPendicularCount extends SegmentClusterFeature {

		ArrayList<Segment> segments = null;
	    public PerPendicularCount(){
	    	Names=new String [1];
	    	Names[0]="PerPendicular Count";
	    	
	    }
		
		public double Value() {
			if (segments != null) {

				Line l1, l2;
				ArrayList<Point> list = new ArrayList<Point>();
				int perCount = (int) ZERO_REPLACMENT;
				for (int i = 0; i < segments.size(); i++) {

					if (segments.get(i).getSegmentType() instanceof Line) {

						l1 = (Line) segments.get(i).getSegmentType();

						for (int j = i + 1; j < segments.size(); j++) {

							if (segments.get(j).getSegmentType() instanceof Line) {
								l2 = (Line) segments.get(j).getSegmentType();
								if (!l1.equals(l2))
									if (!IsIndexRepeated(list, i, j)) {
									if (l1.isOrthogonal(l2)) {
										if (l1.isIntersect(l2)) {
										//if (l1.isIntersectedSegments(l2)) {
											if (SystemSettings.DEBUG_MODE){
												logE.info(" "+l1+"  is Orthogonal to "+l2);
												             // logE.info(")
												}
										
												if (perCount==ZERO_REPLACMENT)
													perCount=0;
												perCount++;

												Point tempindex = new Point(i,
														j);
												list.add(tempindex);
											}
										}
									}

							}

						}

					}

				}

				// / now save the count as the the value
				this.Value = perCount;
				this.ValueOk = true;
				this.computed = true;
				return Value;

			}

			this.Value = ZERO_REPLACMENT;
			this.ValueOk = false;
			this.computed = false;
			return 0;
		}

		@Override
		void setData(Object data) {
			this.ValueOk = false;
			this.computed = false;
			if (data instanceof ArrayList) {
				segments = (ArrayList<Segment>) data;

			}
		}

		@Override
		public double[] Values() {
			Values = new double[1];
			Values[0] = this.Value();
			return Values;
		}

	}

	/** ------------------------------------------------------------------------------------ */

	/** *------------------------------------------------------------------** */
	/**
	 * Feature 7 : intersections count = count the number of intersected lines
	 * lines in a cluster.
	 * 
	 * @author maha
	 * 
	 */
	class IntersectionLinesCount extends SegmentClusterFeature {

		ArrayList<Segment> segments = null;

	    public IntersectionLinesCount(){
	    	Names=new String [1];
	    	Names[0]="Intersection Lines Count";
	    	
	    }
		

		public double Value() {
			if (segments != null) {

				Line l1, l2;
				ArrayList<Point> list = new ArrayList<Point>();
				int intCount = (int)0;
				logger.info("segment size is  = "+segments.size());
				for (int i = 0; i < segments.size(); i++) {

					if (segments.get(i).getSegmentType() instanceof Line) {

						l1 = (Line) segments.get(i).getSegmentType();

						for (int j = i + 1; j < segments.size(); j++) {

							if (segments.get(j).getSegmentType() instanceof Line) {
								l2 = (Line) segments.get(j).getSegmentType();
								if (!l1.equals(l2))
									
									if (SystemSettings.DEBUG_MODE){
										logE.info(" "+l1+"  intersects "+l2);
										             // logE.info(")
										}
										if (!IsIndexRepeated(list, i, j)) {
											if (l1.isIntersect(l2)) {
											if (intCount ==ZERO_REPLACMENT)
												intCount = 0;
											intCount++;
//											logger.info(" Intersection between "+i+"  and "+j);
//											logger.info(l1);
//											logger.info(l2);

											Point tempindex = new Point(i, j);
											list.add(tempindex);
										}

									}

							}

						}

					}

				}

				// / now save the count as the the value
				this.Value = intCount;
				this.ValueOk = true;
				this.computed = true;
				return Value;

			}

			this.Value = ZERO_REPLACMENT;
			this.ValueOk = false;
			this.computed = false;
			return 0;
		}

		@Override
		void setData(Object data) {
			this.ValueOk = false;
			this.computed = false;
			if (data instanceof ArrayList) {
				segments = (ArrayList<Segment>) data;

			}
		}

		@Override
		public double[] Values() {
			Values = new double[1];
			Values[0] = this.Value();
			return Values;
		}

	}
class MinMaxRadius extends SegmentClusterFeature{

	ArrayList<Segment> segments = null;

    public  MinMaxRadius(){
    	Names=new String [2];
    	Names[0]="Min Radius";
    	Names[1]="Max Radius";
    	
    }
	

	public double Value() {
	
		return 0;
	}

	@Override
	void setData(Object data) {
		this.ValueOk = false;
		this.computed = false;
		if (data instanceof ArrayList) {
			segments = (ArrayList<Segment>) data;

		}
	}

	@Override
	public double[] Values() {
		if (segments != null) {
              double Minr=Double.MAX_VALUE,Maxr=0.0;
              double tempr=0.0;
			int ellipseCount = (int) ZERO_REPLACMENT;

			for (int i = 0; i < segments.size(); i++) {
				// / now if count of each of the primitive type
                 GeometricPrimitive seg = segments.get(i).getSegmentType();
				if (seg instanceof Ellipse) {

				if (seg instanceof Ellipse) {
					Ellipse eseg = (Ellipse) seg;
  					tempr=eseg.getLargestRadius();
					
					
				}

				}else if (seg  instanceof BezireCurve) {
                   if (seg instanceof BezireCurve) {
					BezireCurve bseg = (BezireCurve) seg;
					tempr=bseg.getRadius();
				}
					 

				}
				else if (seg instanceof Circle) {
                   
					if (seg instanceof Circle) {
						Circle Cseg = (Circle) seg;
						tempr=Cseg.getRadius();
					}
					
				}
                 if (tempr>Maxr){
                	 Maxr=tempr;
                	 
                	 
                 }
                 if (Minr<tempr){
                	 Minr=tempr;
                 }
                 
			}

			// / now save the count as the the value
			 
			Values = new double[2];
			
			Values[0] = Minr;
			if (Values[0]==0)
				Values[0]=ZERO_REPLACMENT;
			Values[1] = Maxr;
			if (Values[1]==0)
				Values[1]=ZERO_REPLACMENT;
  
 
			// / now save the count as the the value

			// this.Value=intsCount;
			this.ValueOk = true;
			this.computed = true;
			return Values;

		}

		Values = new double[3];
		Values[0] = ZERO_REPLACMENT;
		Values[1] = ZERO_REPLACMENT;
	 
		this.ValueOk = false;
		this.computed = false;
		return this.Values;
	}

	
}
	/** ------------------------------------------------------------------------------------ */
	/** *------------------------------------------------------------------** */
	/**
	 * Feature 8 : intersections type count = count the number of types of
	 * intersection in a cluster means count number of T, L , X type
	 * intersections
	 * 
	 * @author maha
	 * 
	 */
	class IntersectionLTypeCount extends SegmentClusterFeature {

		ArrayList<Segment> segments = null;
	 

	    public IntersectionLTypeCount(){
	    	Names=new String [3];
	    	Names[0]="Intersection T type";
	    	Names[1]="Intersection L type";
	    	Names[2]="Intersection X type";
	    	
	    	
	    }
		@Override
		public double[] Values() {
			if (segments != null) {

				Line l1, l2;

				int intCount = 0;
				int intTCount = 0;
				int intLCount = 0;
				int intXCount = 0;
				for (int i = 0; i < segments.size(); i++) {

					if (segments.get(i).getSegmentType() instanceof Line) {

						l1 = (Line) segments.get(i).getSegmentType();

						for (int j = i + 1; j < segments.size(); j++) {

							if (segments.get(j).getSegmentType() instanceof Line) {
								l2 = (Line) segments.get(j).getSegmentType();

								if (!l1.equals(l2)) {
									int type = l1.getIntersectionType(l2);
									
									
									
									if (type == Line.T_INTERSECTION) {
										
										if ((j-i) <=1)
										{
											intLCount++;
										}
										else 
										intTCount++;
//									if (type == Line.L_INTERSECTION)
//									
//									}
									}
									if (type == Line.X_INTERSECTION)
										intXCount++;
								}
							}

						}

					}

				}

				Values = new double[3];
				
				Values[0] = intTCount;
				if (Values[0]==0)
					Values[0]=ZERO_REPLACMENT;
				Values[1] = intLCount;
				if (Values[1]==0)
					Values[1]=ZERO_REPLACMENT;
				Values[2] = intXCount;
				if (Values[2]==0)
					Values[2]=ZERO_REPLACMENT;
				// / now save the count as the the value

				// this.Value=intsCount;
				this.ValueOk = true;
				this.computed = true;
				return Values;

			}

			Values = new double[3];
			Values[0] = ZERO_REPLACMENT;
			Values[1] = ZERO_REPLACMENT;
			Values[2] = ZERO_REPLACMENT;
			this.ValueOk = false;
			this.computed = false;
			return this.Values;
		}

		@Override
		void setData(Object data) {
			this.ValueOk = false;
			this.computed = false;
			if (data instanceof ArrayList) {
				segments = (ArrayList<Segment>) data;

			}
		}

//		@Override
//		public double Value() {
//			return 0.0;
//		}

		@Override
		public int NoOfValues() {
			  
			return 3;
		}

	}

	/** ------------------------------------------------------------------------------------ */
	/**
	 * Feature 9 : Centroid of the shape : the values of the x ,y , t corrdinate
	 * of the center of graavity of the gemetory sahpae ,.
	 * 
	 * @author maha
	 * 
	 */
	class Centroid extends SegmentClusterFeature {

		
	    public Centroid(){
	    	Names=new String [4];
	    	Names[0]="Centroid x";
	    	Names[1]="Centroid y";
	    	Names[2]="Centroid time";
	    	Names[3]="Centroid time difference";
	    	
	    }
		
		ArrayList ink = null;
		

		@Override
		public double[] Values() {
			if (ink != null) {

				Line l1, l2;
                
                long inittime=0;
                Rectangle2D reg=null;
               
            	for (int i = 0; i < ink.size(); i++) {

					if (ink.get(i) instanceof PointData) {
						PointData p = (PointData) ink.get(i);	
						if (reg==null)
						{
							 reg = new Rectangle2D.Double(p.x,p.y,1,1);							
						}
						reg.add(p);
					}
					}
                
                
				double cx = 0, cy = 0, ct = 0, ctDiff=0;
				 double bx=0,by=0;
				 if (reg!=null){
					 bx=reg.getX();
					 by=reg.getY();
				 }
				for (int i = 0; i < ink.size(); i++) {

					if (ink.get(i) instanceof PointData) {
						
						PointData p = (PointData) ink.get(i);
						if (i==0)
						{
							inittime=p.getTime();
						}
						cx += p.getX()-bx;
						cy += p.getY()-by;
						ct += p.getTime();
                        ctDiff+=p.getTime()-inittime;
					}

				}
				cx /= ((double) ink.size());

				cy /= ((double) ink.size());
				ct /= ((double) ink.size());
				ctDiff/=((double) ink.size());
            	if (ink.size()==0){
						cx=cy=ct=ctDiff=0;
						
				}
				

				Values = new double[4];
				Values[0] = cx;
				Values[1] = cy;
				Values[2] = ct;
				Values[3]=ctDiff;
				// / now save the count as the the value

				// this.Value=intsCount;
				this.ValueOk = true;
				this.computed = true;
				return Values;

			}

			Values = new double[4];
			Values[0] =ZERO_REPLACMENT;
			Values[1] = ZERO_REPLACMENT;
			Values[2] = ZERO_REPLACMENT;
			Values[3] = ZERO_REPLACMENT;
			
			this.ValueOk = false;
			this.computed = false;
			return this.Values;
		}

		@Override
		void setData(Object data) {
			this.ValueOk = false;
			this.computed = false;
			if (data instanceof ArrayList) {
				ink = (ArrayList<Segment>) data;

			}
		}

//		@Override
//		public double Value() {
//			return 0.0;
//		}

		@Override
		public int NoOfValues() {

			return 4;
		}

	}

	/** ------------------------------------------------------------------------------------ */
	/**
	 * Feature 10 : ConvexHull The convex hull of the ink inn 2d space .
	 * 
	 * @author maha
	 * 
	 */
	class ConvexHull extends SegmentClusterFeature {

	    public  ConvexHull(){
	    	Names=new String [5];
	    	Names[0]="area of convexhull";
	    	Names[1]="area of convexhull/area of symbol";
	    	Names[2]="N.Points ConvexHull/ N. points symbol";
	    	Names[3]="Convext Perimeter/symbol perimeter";
	    	Names[4]=" percentage of convex area to bounding box area";
	    	
	    	
	    }
		ArrayList ink = null;
		double area=1.0;

		@Override
		public double[] Values() {
			if (ink != null) {

				Line l1, l2;

				double cx = 0, cy = 0, ct = 0;

				ArrayList<PointData> hull = ComputationsGeometry
						.computeConvexHull((ArrayList<PointData>) ink);
				// logger.info("determine what to take as features of the
				// convex hull data -----869 of segmentsclusterset.");
		
				
				
				double areaConvex = ComputationsGeometry.computeArea(hull);
				double noOfPoints = hull.size();
				double premiter = ComputationsGeometry.computeTotalLength(hull);
                 double permiterInk=ComputationsGeometry.computeTotalLength(ink);
				Values = new double[5];
				Rectangle2D box=ComputationsGeometry.computeBoundingBox(ink);
				double boundingBoxArea=box.getHeight()*box.getWidth();
						if (SystemSettings.DEBUG_MODE){
					logE.info(" The convex hull    "+hull);
					logE.info( " Area of convex hull =  "+areaConvex+"   preimiter of hull "+premiter);
					             // logE.info(")
					}
				
				Values[0] = Math.abs(areaConvex);  // area of convexhull 
//				 (area of convex /area of cluster) 
//				 peremiter of area of convex hull / perimieter of area of  cluster
				Values[1]= Math.abs(areaConvex/area);
				Values[4]=( Math.abs(Math.abs(areaConvex)- Math.abs(boundingBoxArea))/ Math.abs(boundingBoxArea))*100;
				if (area==0)
					Values[1]=ZERO_REPLACMENT;
				
				if ( boundingBoxArea==0)
					Values[4]=ZERO_REPLACMENT;
				
				
				Values[2] = noOfPoints/ink.size();
				if (ink.size()==0)
					Values[2]=0.0;
				Values[3] = Math.abs(premiter/permiterInk);  
				if (permiterInk==0)
					Values[3]=ZERO_REPLACMENT;
				
				
				// / now save the count as the the value

				// this.Value=intsCount;
				this.ValueOk = true;
				this.computed = true;
				return Values;

			}

			Values = new double[5];
			Values[0] = ZERO_REPLACMENT;
			Values[1] =ZERO_REPLACMENT;
			Values[2] = ZERO_REPLACMENT;
			Values[3] =ZERO_REPLACMENT ;
			Values[4]=ZERO_REPLACMENT ;
			this.ValueOk = false;
			this.computed = false;
			return this.Values;
		}

		@Override
		void setData(Object data) {
			this.ValueOk = false;
			this.computed = false;
			if (data instanceof ArrayList) {
		
				ArrayList temp = ((ArrayList) data);
				if (temp.get(0) instanceof ArrayList) {
					ink = (ArrayList) temp.get(0);
					
				}
				if (temp.get(1) instanceof Double) {
					area = (Double) temp.get(1);
					
				}

			}
		}

//		@Override
//		public double Value() {
//			return 0.0;
//		}

		@Override
		public int NoOfValues() {
		
			return 5;
		}

	}

	/** ------------------------------------------------------------------------------------ */
	/** ------------------------------------------------------------------------------------ */
	/**
	 * Feature 11 : area of the ink data.
	 * 
	 * @author maha
	 * 
	 */
	class Area extends SegmentClusterFeature {

	    public  Area(){
	    	Names=new String [2];
	    	Names[0]="area ";
	    	Names[1]="diff area of symbol to area of bounding box";
	    	
	    }
		
		SimpleInkObject ink = null;

		@Override
		public double[] Values() {
			
			if (ink != null) {

				Line l1, l2;

				double cx = 0, cy = 0, ct = 0;

				Value = Math.abs(ComputationsGeometry.computeArea(ink.getPoints()));
				// logger.info("determine what to take as features of the
				// convex hull data -----869 of segmentsclusterset.");
             
				// / now save the count as the the value
               double areaBoundingBox= Math.abs(ink.getBox().getHeight()*ink.getBox().getWidth());
               
               double diff=(Math.abs(Value-areaBoundingBox)/areaBoundingBox)*100;
               
				Values = new double[2];
				Values[0]=Value;
				Values[1]=diff;
				// this.Value=intsCount;
				this.ValueOk = true;
				this.computed = true;
				return Values;

			}

			Value = 0;

			this.ValueOk = false;
			this.computed = false;
		//	return this.Value;
			
			
			Values = new double[2];
			Values[0] = 0;
			Values[1]=0;
			return Values;
		}

		@Override
		void setData(Object data) {
			this.ValueOk = false;
			this.computed = false;
			if (data instanceof SimpleInkObject) {
				ink = (SimpleInkObject) data;

			}
		}

		@Override
		public int NoOfValues() {
		
			return 2;
		}
		public double Value() {
			if (ink != null) {

				Line l1, l2;

				double cx = 0, cy = 0, ct = 0;

				Value = ComputationsGeometry.computeArea(ink.getPoints());
				// logger.info("determine what to take as features of the
				// convex hull data -----869 of segmentsclusterset.");

				// / now save the count as the the value

				// this.Value=intsCount;
				this.ValueOk = true;
				this.computed = true;
				return Value;

			}

			Value = 0;

			this.ValueOk = false;
			this.computed = false;
			return this.Value;
		}

	}

	/** ------------------------------------------------------------------------------------ */
	
	
	/** ------------------------------------------------------------------------------------ */
	/**
	 * Feature 12 : Ratio of inks 
	 * 
	 * @author maha
	 * 
	 */
	class Ratio extends SegmentClusterFeature {

	    public  Ratio(){
	    	Names=new String [1];
	    	Names[0]=" Ration between w/h ";
	    	//Names[1]="diff area of symbol to area of bounding box";
	    	
	    }
		SimpleInkObject   ink=null;

		@Override
		public double[] Values() {
			Values = new double[1];
			Values[0] = this.Value();
			return Values;
		}

		@Override
		void setData(Object data) {
			this.ValueOk = false;
			this.computed = false;
			if (data instanceof SimpleInkObject) {
				ink = (SimpleInkObject) data;

			}
		}


		public double Value() {
			if (ink != null) {



				

				Value = ink.getBox().getWidth()/ink.getBox().getHeight();
				// logger.info("determine what to take as features of the
				// convex hull data -----869 of segmentsclusterset.");

				// / now save the count as the the value

				if (Double.isNaN(Value))
					Value=ZERO_REPLACMENT;
				// this.Value=intsCount;
				this.ValueOk = true;
				this.computed = true;
				return Value;

			}

			Value = 0;

			this.ValueOk = false;
			this.computed = false;
			return this.Value;
		}

	}

	/** ------------------------------------------------------------------------------------ */
	/**
	 * Feature 13 : zenerik moment features 
	 * 
	 * @author maha
	 * 
	 */
	class ZenerikMomentFeature extends SegmentClusterFeature{

		private Stroke[] strokes=null;
		ZernikeFE moments=null;

	    public  ZenerikMomentFeature (){
	    	//int size=(SystemSettings.DEFAULT_ZERNIKE_ORDER*SystemSettings.DEFAULT_ZERNIKE_ORDER-1)/2;
	    	int size= NoOfValues();
	    	Names=new String [size];
	    	for (int i = 0; i < Names.length; i++) {
				Names[i]="Zernike moments "+i/2;
			}
	    	//Names[0]=" Ration between w/h ";
	    	
	    	//Names[1]="diff area of symbol to area of bounding box";
	    	
	    }

		@Override
		public double[] Values() {
			if (strokes!=null){
				
				if (moments==null)
						moments=new ZernikeFE();
				
			Values=  moments.apply(strokes);
			//return null;
			
			// this.Value=intsCount;
			this.ValueOk = true;
			this.computed = true;
			return Values;

		}
           
			// i need the correct number of strokes so i will create a dump strok eand 
			// then apply on it the algorithms 
			
			Stroke s=new Stroke ();
			PointData p=new PointData(10,10,10,10);
			s.addPoint(new PointData(10,10,10,10));
			s.addPoint(new PointData(20,20,20,20));
			s.addPoint(new PointData(30,30,30,30));
			s.addPoint(new PointData(50,50,50,50));
			s.addPoint(new PointData(40,40,40,40));
			s.addPoint(p);
			s.setStartPoint(p);
			s.setEndPoint(p);
				moments=new ZernikeFE();
				 Stroke[] strokes=new Stroke[1];
				 strokes[0]=s;
			Values=  moments.apply(strokes);
	

		this.ValueOk = false;
		this.computed = false;
		return this.Values;
		}

		@Override
		void setData(Object data) {
			this.ValueOk = false;
			this.computed = false;
			if (data instanceof Stroke[]) {
				strokes = (Stroke[]) data;
				
			}
		}
		@Override
		public int NoOfValues() {
			if (ValueOk)
				return Values.length;
			else {
				Stroke s=new Stroke ();
				PointData p=new PointData(10,100,10,10);
				s.addPoint(new PointData(10,10,10,10));
				s.addPoint(new PointData(20,20,20,20));
				s.addPoint(new PointData(30,300,30,30));
				s.addPoint(new PointData(50,50,50,50));
				s.addPoint(new PointData(40,40,40,40));
				s.addPoint(p);
				s.setStartPoint(p);
				s.setEndPoint(p);
				Stroke s1=new Stroke ();
				 p=new PointData(20,20,20,20);
				s1.addPoint(new PointData(200,20,20,10));
				s1.addPoint(new PointData(20,20,20,20));
				s1.addPoint(new PointData(40,40,400,30));
				s1.addPoint(new PointData(50,50,50,50));
				s1.addPoint(new PointData(70,70,70,70));
				s1.addPoint(p);
				s1.setStartPoint(p);
				s1.setEndPoint(p);
			
				moments=new ZernikeFE();
				 //Stroke[] strokes=new Stroke[1];
				 Stroke[] strokes=new Stroke[2];
				 strokes[0]=s;
				 strokes[1]=s1;
				Values=  moments.apply(strokes);
				return Values.length;
			}
				
		}
		
	}
	
	/**
	 * Feature 13 : zenerik moment features 
	 * 
	 * @author maha
	 * 
	 */
	class RubineFeatures extends SegmentClusterFeature{

		StrokeRubineFeatureSet rubine;

	    public  RubineFeatures (){
	    	Names=new String [13];
	    	for (int i = 0; i < Names.length; i++) {
				Names[i]="Rubine "+i;
			}
	    	//Names[0]=" Ration between w/h ";
	    	//Names[1]="diff area of symbol to area of bounding box";
	    	
	    }

		@Override
		public double[] Values() {
            if(rubine!=null)
            {
			//return null;
			rubine.computeFeatures();
			int valuesCount=rubine.getFeatures().size();
			Values=new double[valuesCount];
			for (int i = 0; i < Values.length; i++) {
				Values[i]=rubine.getFeatures().get(i).getValue();
			}
			// this.Value=intsCount;
			this.ValueOk = true;
			this.computed = true;
			return Values;

		}

		Values = new double[12];
		for (int i = 0; i < Values.length; i++) {
			Values[i]=ZERO_REPLACMENT;
		}

		this.ValueOk = false;
		this.computed = false;
		return this.Values;
		}

		@Override
		void setData(Object data) {
			this.ValueOk = false;
			this.computed = false;
			if (data instanceof StrokeRubineFeatureSet) {
				rubine = (StrokeRubineFeatureSet) data;
				
			}
		}
		@Override
		public int NoOfValues() {
			if (ValueOk)
				return Values.length;
			else 
				return 12;
		}
		
	}
	
	/**
	 * Feature 15x : Density  (length/size)
	 * 
	 * @author maha
	 * 
	 */

	class Density extends SegmentClusterFeature {

	    public  Density (){
	    	Names=new String [1];

	    Names[0]=" Density(length/size) ";
	    	//Names[1]="diff area of symbol to area of bounding box";
	    	
	    }
		SimpleInkObject   ink=null;

		@Override
		public double[] Values() {
			Values = new double[1];
			Values[0] = this.Value();
			return Values;
		}

		@Override
		void setData(Object data) {
			this.ValueOk = false;
			this.computed = false;
			if (data instanceof SimpleInkObject) {
				ink = (SimpleInkObject) data;

			}
		}


		public double Value() {
			if (ink != null) {

                double length=0.0;
                
//					for (int i = 0; i < ink.getPoints().size()-1; i++) {
//						length+=ink.getPoint(i).distance(ink.getPoint(i+1));
//					}
                   length=ink.getLength();
			
                    // double size=ink.getBox().
                     PointData pmin, pmax;
     				pmin = new PointData();
     				pmax = new PointData();
     				pmin.setLocation(ink.getBox().getMinX(), ink.getBox().getMinY());
     				pmax.setLocation(ink.getBox().getMaxX(), ink.getBox().getMaxY());
     				double size = ComputationsGeometry.computeLength(pmin, pmax);
                
     				if (size==0)
     					size=1;
                     
				Value = length/size;
				
				// logger.info("determine what to take as features of the
				// convex hull data -----869 of segmentsclusterset.");

				// / now save the count as the the value

				// this.Value=intsCount;
				this.ValueOk = true;
				this.computed = true;
				return Value;

			}

			Value = 0;

			this.ValueOk = false;
			this.computed = false;
			return this.Value;
		}

	}
	
	
	
	
	/**
	 * Feature 16x :  Area Computed Log of the ink data.
	 * 

	 * @author maha
	 * 
	 */
	class AreaComputedLog extends SegmentClusterFeature {

		SimpleInkObject ink = null;

	    public  AreaComputedLog (){
	    	Names=new String [1];

	    Names[0]=" Log of computed area";
	    	//Names[1]="diff area of symbol to area of bounding box";
	    	
	    }
		@Override
		public double[] Values() {
			Values = new double[1];
			Values[0] = this.Value();
			return Values;
		}

		@Override
		void setData(Object data) {
			this.ValueOk = false;
			this.computed = false;
			if (data instanceof SimpleInkObject) {
				ink = (SimpleInkObject) data;

			}
		}


		public double Value() {
			if (ink != null) {

				Line l1, l2;

				double cx = 0, cy = 0, ct = 0;

				Value = Math.log10(Math.abs(ComputationsGeometry.computeArea(ink.getPoints())));
				// logger.info("determine what to take as features of the
				// convex hull data -----869 of segmentsclusterset.");

				// / now save the count as the the value

				// this.Value=intsCount;
				this.ValueOk = true;
				this.computed = true;
				return Value;

			}

			Value = 0;

			this.ValueOk = false;
			this.computed = false;
			return this.Value;
		}

	}
	
	/**
	 * Feature 17  : area each stroke 
	 * 
	 * @author maha
	 * 
	 */
	class areaEachStroke extends SegmentClusterFeature {
		

	
		double area=-1.0;

		   public  areaEachStroke (){
		    	Names=new String [2];

		    Names[0]=" abs of area";
		    Names[1]="log of  abs of area";
		//    Names[0]=" abs of area";
		    	//Names[1]="diff area of symbol to area of bounding box";
		    	
		    }
		@Override
		public double[] Values() {
			if (area!= -1.0) {

			

		
               
				Values = new double[2];
				Values[0] = Math.abs(area);
//				 (area of convex /area of cluster) 
//				 peremiter of area of convex hull / perimieter of area of  cluster
				Values[1]= Math.log10(Math.abs(area));
				
				
				
			

				// this.Value=intsCount;
				this.ValueOk = true;
				this.computed = true;
				return Values;

			}

			Values = new double[2];
			Values[0] = ZERO_REPLACMENT;
			Values[1] = ZERO_REPLACMENT;
		
			this.ValueOk = false;
			this.computed = false;
			return this.Values;
		}

		@Override
		void setData(Object data) {
			this.ValueOk = false;
			this.computed = false;
			if (data instanceof Double) {
		
			
				
			
					area = (Double)data;
					
			

			}
		}
//
//		@Override
//		public double Value() {
//			return 0.0;
//		}

		@Override
		public int NoOfValues() {
		
			return 2;
		}

	}
	
	/**
	 * Feature  : log Ratio of inks 
	 * 
	 * @author maha
	 * 
	 */
	class RatioLog extends SegmentClusterFeature {

	    public RatioLog (){
	    	Names=new String [1];

	    Names[0]=" Log of ration between hight and width";
	    	//Names[1]="diff area of symbol to area of bounding box";
	    	
	    }
		SimpleInkObject   ink=null;

		@Override
		public double[] Values() {
			Values = new double[1];
			Values[0] = this.Value();
			return Values;
		}

		@Override
		void setData(Object data) {
			this.ValueOk = false;
			this.computed = false;
			if (data instanceof SimpleInkObject) {
				ink = (SimpleInkObject) data;

			}
		}


		public double Value() {
			if (ink != null) {



				

				Value = Math.log10(Math.abs(ink.getBox().getWidth()/ink.getBox().getHeight()));
				// logger.info("determine what to take as features of the
				// convex hull data -----869 of segmentsclusterset.");

				// / now save the count as the the value

				// this.Value=intsCount;
				this.ValueOk = true;
				this.computed = true;
				return Value;

			}

			Value = 0;

			this.ValueOk = false;
			this.computed = false;
			return this.Value;
		}

	}
	/** ------------------------------------------------------------------------------------ */
	class CentroidalRadius extends  SegmentClusterFeature{
		
	    public CentroidalRadius  (){
	    	Names=new String [5];

	    Names[0]="Mean CentroidalRadius x";
	    Names[1]="Mean CentroidalRadius y";
	    Names[2]="Mean t";
	    Names[3]="Mean time difference";
	    Names[4]="Mean Radius";
	    	//Names[1]="diff area of symbol to area of bounding box";
	    	
	    }
		ArrayList ink = null;

		@Override
		public double[] Values() {
			if (ink != null) {

				Line l1, l2;

				double cx = 0, cy = 0, ct = 0;
				double deltaT=0;
				for (int i = 0; i < ink.size(); i++) {

					if (ink.get(i) instanceof PointData) {
						PointData p = (PointData) ink.get(i);

						cx += p.getX();
						cy += p.getY();
						ct += p.getTime();
						if (i+1<ink.size())
						{
							PointData p2 = (PointData) ink.get(i+1);
						deltaT+=  p2.getTime()-p.getTime();
						}

					}

				}
				
				
				
				cx /= ((double) ink.size());

				cy /= ((double) ink.size());
				ct /= ((double) ink.size());
				deltaT/=((double) ink.size());
	
				if (ink.size()==0){
						cx=cy=ct=0;
						deltaT=0;
						
				}
				
				double meanr=0;
				for (int i = 0; i < ink.size(); i++) {

					if (ink.get(i) instanceof PointData) {
						PointData p = (PointData) ink.get(i);
						meanr+=ComputationsGeometry.length(cx, cy, p.getX(),p.getY());
					  

					}

				}
				meanr/= ((double) ink.size());
				// now after i compute the centroid 
				//i want to compute the mean of the centroid 
				
				
				Values = new double[5];
				Values[0] = cx;
				Values[1] = cy;
				Values[2] = ct;
			    Values[3]=deltaT;
			    Values[4]=meanr;
				// / now save the count as the the value

				// this.Value=intsCount;
				this.ValueOk = true;
				this.computed = true;
				return Values;

			}

			Values = new double[5];
			Values[0] =ZERO_REPLACMENT;
			Values[1] = ZERO_REPLACMENT;
			Values[2] = ZERO_REPLACMENT;
			Values[3] = ZERO_REPLACMENT;
			Values[4] = ZERO_REPLACMENT;
			this.ValueOk = false;
			this.computed = false;
			return this.Values;
		}

		@Override
		void setData(Object data) {
			this.ValueOk = false;
			this.computed = false;
			if (data instanceof ArrayList) {
				ink = (ArrayList<Segment>) data;

			}
		}

//		@Override
//		public double Value() {
//			return 0.0;
//		}

		@Override
		public int NoOfValues() {

			return 5;
		}
		
	}
	
	/** *------------------------------------------------------------------** */

	// /***********************************************************************************************************
	/***************************************************************************
	 * Features calculations
	 **************************************************************************/
	// /***********************************************************************************************************
	public void setSegmentCluster(SegmentCluster cluster) {
		this.inkCluster = cluster;

	}

	public SegmentCluster getSegmentCluster() {
		return this.inkCluster;
	}
	public int getCountOfFeatures() {
	
		int countFeatures=0;
		for (int i = 0; i < Features.size(); i++) {
			countFeatures+=Features.get(i).getValues().length;
		}
		return countFeatures;
	}

}
