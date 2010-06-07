package SketchMaster.Stroke.StrokeData;

import org.apache.log4j.Logger;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.ArrayList;




import SketchMaster.system.SystemSettings;
import SketchMaster.Stroke.features.FeatureFunction;
import SketchMaster.Stroke.graphics.shapes.GuiShape;
import SketchMaster.Stroke.graphics.shapes.Line;
import SketchMaster.Stroke.graphics.shapes.SegmentedShape;
import SketchMaster.gui.DrawingDebugUtils;
import SketchMaster.lib.ComputationsGeometry;
import SketchMaster.system.SystemSettings;

/**
 * @author Mahi
 */
public class Stroke extends SimpleInkObject implements Serializable, GuiShape {
	
	class pointChange{
		final static int  TYPE_INC=0;
		 final static  	int TYPE_DEC=1;
		 final static 	int TYPE_ULTERNATING=2;
		 final static int 	TURN_X=0;
		 final static int 	TURN_y=1;
		 final static int 	TURN_M=2;
		 

		  final static int 	TURN_YM=3;
		 final static int 	TURN_XM=4;
	
	   final static int 	TURN_XY=5; 
	   
		 final static int 	TURN_ALL=6;
		 
		double inX;
		double inY;
		double inMag;
		
		int typeX;
		int typeY;
		int typeMag;
		int turnType=0;
		int index;
		boolean turn=false;
		public void add(double x, double y, double mag){
			inX=x;
			if (x>0){
				typeX=TYPE_INC;
			}
			else {
				typeX=TYPE_DEC;
			}
			
			
			
			
			
			inY=y;
			if (y>0){
				typeY=TYPE_INC;
			}
			else {
				typeY=TYPE_DEC;
			}
			
			inMag=mag;
			if (mag>0){
				typeMag=TYPE_INC;
			}
			else {
				typeMag=TYPE_DEC;
			}
			
			
			
			
		}
		public void testTurn(pointChange temp) {
		if (temp.turn)
			return ;
			
			if (typeX!=temp.typeX){
				turn=true;
				turnType=TURN_X;
			}
		
			if (typeY!=temp.typeY){
				// if prevvv.
				if (turn){
					
					turnType=TURN_XY;
				}
				else {
					turnType= TURN_y;
				}
				
				turn=true;
			}
			
			if (typeMag!=temp.typeMag){
				
				if(turn){
				
					// get the current turn .. 
					if (turnType==TURN_XY){
						
						turnType=TURN_ALL;
						
					}
					else {
						
						if (turnType==TURN_X){
							turnType=TURN_XM;
						}
						else {
							turnType=TURN_YM;
						}
						
					}
					
				}
				else {
					turnType=TURN_M;
				}
				
				turn=true;
			}
		}
		
		
		
	}
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(Stroke.class);
    private static boolean debug=false;
	// private PointData[] points;

	public static double RectangleLoopThreshold = 50;
	private transient static boolean onLine = SystemSettings.OnLineComputations;
    private boolean interpolated=false;
//		if(!rotationComputed){
//		ArrayList<PointData> points;
//		if (this.stroke != null) {
//
//			points = stroke.getPoints();
//			if (points.size() > 3) {
//			 rotation = 0.0;
//				for (int i = 0; i < points.size() - 2; i++) {
//					PointData p1 = points.get(i);
//					PointData p2 = points.get(i + 1);
//					PointData p3 = points.get(i + 2);
//					rotation +=  ComputationsGeometry
//							.computeChangeRotation(p1, p2, p3);
//
//				}
// 
//	}
//		}
//		rotationComputed=true;
//		}
//return rotation;
//}
	/**
	 * 
	 */
    ArrayList<pointChange>  changes=null;
    ArrayList<Integer>  SortedXIndex=null;
    ArrayList<Integer>  SortedYIndex=null;
	int OverTraceHyposes=0;
	ArrayList<Integer> OverTracePoints=null;
    
    ArrayList<Integer>  turnsIndex=null;
      ArrayList<Integer> SortedPointIndex=null;
    ArrayList<Double>  DistantFromStart=null;
    ArrayList<Rectangle2D>  boxes=null;
    double NDDE;
	double DCR;
	double LongestChordtoLengthRatio;
	double EndPointtoLengthRation;
	double LongestDistanceBetweenPointsInStroke;
		private static final int ORIENTATION_VERTICAL = 0;
	final int  ORIENTATION_HORIZONATAL=1;
	int Orientation=ORIENTATION_HORIZONATAL;
	boolean OverTraced=false;
	boolean TailRemoved=false;
	ArrayList<Integer> TailPartIndex=null;
   
	private static final long serialVersionUID = -4866211701068294061L;
	private static final int MaxChangeIndex = 10;
	private static final int	DEFAULT_NEIGHBOURS	= 3;
	


	private PointData StartPoint = null;

	private PointData EndPoint = null;

	private boolean dirty = true;// dirty mean not finish editing.

	private transient StrokeStatisticalData StatisticalInfo = null; // stroke


	private PointData MaxDirection;
	private PointData MinDirection;
	
	
	// computed
																	// StatisticalInfo

	public Stroke() {
		StatisticalInfo = new StrokeStatisticalData();
		StatisticalInfo.initAll();
		points = new ArrayList<PointData>();
		if (SystemSettings.USE_NEW_CHANGES){
		   SortedXIndex=new ArrayList<Integer>();
		      SortedYIndex=new ArrayList<Integer>();
		      SortedPointIndex=new ArrayList<Integer>();
		}
		      DistantFromStart=new ArrayList<Double>();
	}

	public   Stroke(SimpleInkObject ink) {
		// get all info from theh simple ink to the stroke 
		
		this.points=new ArrayList<PointData>();
		int noPoints=ink.getPointsCount();
		StatisticalInfo = null;
		this.setStartPoint((PointData)points.get(0).clone());
		this.setEndPoint((PointData)ink.getPoint(noPoints-1).clone());
		//this.addPoint( (PointData)ink.getPoint(i).clone());
		for (int i = 0; i < noPoints; i++) {
		//	points.add((PointData)ink.getPoint(i).clone());
	     this.addPoint( (PointData)ink.getPoint(i).clone());		
			
		}
		
	
		//StatisticalInfo.initAll();
		
		
	}

	public Stroke InterpolatePoints(){
		if (this.points.size()>0){
		Stroke NewInterploated=new Stroke();
		  ArrayList pointsa = new ArrayList<PointData>();
		 
	        PointData prev =this.points.get(0);
	        PointData point=null;
	        NewInterploated.addPoint(prev);
	        NewInterploated.setStartPoint(prev);
	        
	        if (debug)
	        {
	        	if (DrawingDebugUtils.DEBUG_GRAPHICALLY){
	        		
	        		DrawingDebugUtils.drawPointPath(DrawingDebugUtils.getGraphics(), DrawingDebugUtils.InkColor, DrawingDebugUtils.PointsColor, this.points);
	        		
	        	}
	        }
	        
	        
	     //   pointsa.add(prev);
	        for(int i = 1; i < points.size(); i++)
	        {
	             point = points.get(i);
	            double dist = point.distance(prev);
	            if(dist > SystemSettings.MaxInterplotionLength)
	            {
	                int n = (int)Math.floor(dist / SystemSettings.MaxInterplotionLength);
	                double theta = prev.getAngle(point)  ;
	                long tStep = (point.getTime() - prev.getTime()) / (long)n;
	                double pStep = (point.getPresureValue() - prev.getPresureValue()) / n;
	                for(int j = 1; j < n + 1; j++)
	                {
	                    PointData p = new PointData(prev.getX() + (double)j * SystemSettings.MaxInterplotionLength * Math.cos(theta),
	                    		prev.getY() + (double)j * SystemSettings.MaxInterplotionLength * Math.sin(theta),
	                    		prev.getTime() + tStep * (long)j,
	                    		(int)((double)prev.getPresureValue() + pStep * (double)j));
	                    NewInterploated.addPoint(p);
	                  //  pointsa.add(p);
	                }
	                prev = point;
	  		            //NewInterploated.addPoint(point);

	            }
	            else{  // point is less than the interploaiton predected 
	            	// skip this point 
	            	//prev = point;
		      //      NewInterploated.addPoint(point);
	            }
	            
	        }
	        if (point!=null)
	        	NewInterploated.setEndPoint(point );
	        NewInterploated.setInterpolated(true);
	        if (debug)
	        {
	        	if (DrawingDebugUtils.DEBUG_GRAPHICALLY){
	        		
	        		DrawingDebugUtils.drawPointPath(DrawingDebugUtils.getGraphics(), Color.cyan, Color.MAGENTA,NewInterploated.points);
	        		
	        	}
	        }
	        return   NewInterploated;
		
		}
		else{
			
			return this;
		}
		
		
	}
	
	
	
	/**
	 * @param endPoint
	 *            the endPoint to set
	 * @uml.property name="endPoint"
	 */
	public void setEndPoint(PointData EndPoint) {
		this.dirty = false;
		this.EndPoint = EndPoint;
          
		if (SystemSettings.USE_NEW_CHANGES){
		logger.info( "  now all point are intered i need to display the sort list of x an y ");
		logger.info( " points   "+points);
		logger.info(" $%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%5");
		logger.info("SortedXIndex    " + SortedXIndex);
		logger.info(" $%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%5");
		logger.info(" SortedYIndex   "+ SortedYIndex);
		//setLoopingEnd(this.StartPoint, this.EndPoint);

		}
		if (!onLine) {
			updateStatiscal();
		}

		getStatisticalInfo().updateBatchFunctions();
		
	}

	private void updateOtherFeatures() {
		
		//get the maximum hightest direction value,
		
		ArrayList<FeatureFunction> function = getStatisticalInfo().getFunctions();
		FeatureFunction direction=null;
		for (int i = 0; i <function.size(); i++) {
			//get the direction 
			if (function.get(i).getName().startsWith("Direction")){  // direction is better. 
			//if (function.get(i).getName().startsWith("Slope")){
				direction=function.get(i);
			}
		}
		
	// change try using 
	//	direction=getStatisticalInfo().getCurvatureRotation();
			//NDDE
		if (direction!=null){
			
			int max=direction.getMaxLocation();
			int min=direction.getMinLocation();
			// compute dcr 
	 
			DCR=direction.getMax()/direction.getAverage();
			
			logger.info("  the dcr is  =  "+DCR);
			//double revolution=TotalRotation()/(2.0*Math.PI);
			logger.info("  the total rotation of the  = " + TotalRotation());
			logger.info( " The sum of the  drection graph is  "+direction.getSumUpNow() );
			
			logger.info("  revolution is   "+revolution);
			MaxDirection=points.get(max);
			MinDirection=points.get(min);
			
		double distance=points.get(min).distance(points.get(max));
		
		logger.info(" the point of max direction is "+points.get(max)+" and point of min direction is "+points.get(min));
		logger.info(" distance between them is  "+distance);
		
		NDDE=Math.abs(distance/this.getLength());
		
		logger.info(" NDDE is = "+NDDE);
		
		}
		
		
	
		
	}

	@Deprecated
	public void setLoopingEnd(PointData start, PointData end) {
		if (start==null)
			if (points!=null)
				{
				if (points.size()>0)
					start=points.get(0);
		
			else 
				return ;
				}
			else return;
		
		Rectangle2D r = new Rectangle2D.Double(start.getPointLocation().getX(),
				start.getPointLocation().getY(), 0, 0);
		r.add(end.getPointLocation());
		double area = r.getWidth() * r.getHeight();

		if (area < RectangleLoopThreshold) {
			// add the start point to last point in the points array
			points.add((PointData) start.clone());
			this.EndPoint = start;

		}
	}

	/**
	 * @param startPoint
	 *            the startPoint to set
	 * @uml.property name="startPoint"
	 */
	public void setStartPoint(PointData StartPoint) {
		this.StartPoint = StartPoint;
		this.dirty = true;
	}

	public void addPoint(PointData point) {
		// logger.info(point);
		// / adding point to the array
		this.points.add(point);
		// after add point chek that this point 
		if (SystemSettings.USE_NEW_CHANGES){
		addPointToSortedLists(point);
		logger.info(" this is poing number "+points.size());
		addPointToIncDec(point);
		}
		addPointDistance(point);
		if (onLine) {
			// updateBoundingBox(point);
			// if (onLine)
			// updateStatiscalData(point);
			updateStatiscal(point);
		}
	}
	private void addPointToIncDec(PointData point) {
		int neighbours=DEFAULT_NEIGHBOURS;
		if (	this.points.size()==1){
			changes=new ArrayList<pointChange>();
			
		}
		else {
			// check the point right before this
			//
			if (points.size()>neighbours){
			// get first points  
			PointData prev=points.get(points.size()-neighbours);
			
			
			// now get difference between new and old 
			
			pointChange temp=new pointChange();
			temp.index=points.size()-1;
			
			double inX=point.getX()-prev.getX();
			double inY=point.getY()-prev.getY();
			double m=point.magnitude()-prev.magnitude();
		 temp.add(inX, inY, m);
		 
		 if (changes.size()>0){
			 // get the laast change element ot check if differnt than this... 
			 pointChange t=changes.get(changes.size()-1);// last element 
	  
			 temp.testTurn(t);
			 if (temp.turn){
				 if (turnsIndex==null){
					 turnsIndex=new ArrayList<Integer>();
					 
				 }
				 turnsIndex.add(   changes.size());
				 
				//   if (Math.abs(temp.index-t.index )>=neighbours){
				 logger.info( "   this is a turning point    of index    "+this.points.size() + "  turn type is "+temp.turnType);
			// }
			 }
		 }
	    	 changes.add(temp);
			
			}/// size > neighbours 
		}
		
	}

	private void addPointDistance(PointData point){
		Double dis;
		
		if (StartPoint==null)
			return;
		 dis=StartPoint.distance(point);
		this.DistantFromStart.add(dis);
	}
	
	
	private int addToList(  ArrayList<Integer>  list, double value, int type ){
		
		if (	this.points.size()==1){
			// this is the first point... 
			// do the follwoing 
			list.add(0);
		}
		
	 
		int newIndexpoint=BinarySearch(list,value, type);
		 //  logger.info( "  the index found by binary search is "+newIndexpoint);
			if (newIndexpoint==-1){
				// add at the begining 
				list.add(0, this.points.size()-1);
			}
			else if (newIndexpoint==-2){
				list.add(this.points.size()-1);
			}
			else {
				list.add(newIndexpoint, this.points.size()-1);
			}
			
		   return newIndexpoint;
		
		
	}
	private boolean testProximity(PointData point, int insertLoc, ArrayList<Integer> list){
		
		if (insertLoc>0){// this is insert is in the middle then i need to look for the
			//surronding pixels and check that are not ins the same locatinos... 
	int 		window=SystemSettings.WINDOW_SCAN_SIZE;
			// get the list from windo to window 
			//first make sure that the size is ok 
			int b=insertLoc-window;
			int e=insertLoc+window;
			// now make sure that b >0 and e < size 
			if (b<0)  b=0;			
			if (e>list.size()) e=list.size();
			int pointIndex=points.size()-1;
			int in;
			for (int i = b; i < e; i++) {
				in=list.get(i);
				if (pointIndex-in<window){
					continue;
				}
				// get the index of the
				PointData tempPoint = points.get(in);
			
				if (tempPoint.isNearPoint(point, SystemSettings.LOCATION_RANGE))
				{
						logger.info( " NEEEARRRRRRRRRRRRR" );
								logger.info(" the distance between points of index of  "+(this.points.size()-1)+" and is  "+point);
						logger.info("  with tempoint has index of   "+ list.get(i)+"     and is" +tempPoint);
						logger.info("  the distance is "+point.distance(tempPoint));
						
						return true;
				}
				
				
			}
			
			
			
			
		}
		
		return false;
	}

	private void addPointToSortedLists(PointData point){
		
		double x,y;
		x=point.x;
		y=point.y;
		double loc=point.magnitude();

//	if (testProximity(point, insertLoc, SortedXIndex)	 ){
//		
//	
//		OverTraceHyposes++;
//		 if (OverTracePoints==null){
//			 OverTracePoints=new ArrayList<Integer>();
//		 }
//		 OverTracePoints.add(points.size()-1);
//	}
		 		 int insertLocx=addToList( SortedXIndex,x, 0);
		 		 
				 boolean testx=testProximity(point, insertLocx, SortedXIndex)	;
		 int insertLocy= addToList(SortedYIndex, y, 1);
		 
		 
		 boolean testy=testProximity(point, insertLocy, SortedYIndex)	;
		 
		 int insertLocloc= addToList (SortedPointIndex,loc, 2);
		 
		 boolean testmag=testProximity(point, insertLocloc, SortedPointIndex)	;
		 
		 if (testx&testy || testy&testmag || testmag &testx ){
				logger.info( " test correct...........   ."+points.size());
				
				OverTraceHyposes++;
				 if (OverTracePoints==null){
					 OverTracePoints=new ArrayList<Integer>();
				 }
				 OverTracePoints.add(points.size()-1);
			}
		 
		
//		if (	this.points.size()==1){
//			// this is the first point... 
//			// do the follwoing 
//			SortedXIndex.add(0);
//			SortedYIndex.add(0);
//			SortedPointIndex.add(0);
//			
//			
//		}
//		else{
//			// this point is added to list of point at the end
//			double x,y;
//			x=point.x;
//			y=point.y;
//			double loc=point.magnitude();
//			
////			String str="";
////			for (int i = 0; i < points.size(); i++) {
////				str+=" X ("+i+" )= "+points.get(i).x+" "; 
////			}
////			
////			 logger.info( "  the x is "+x);
////			 
////			 logger.info( str );
//			 
//			int newIndexpoint=BinarySearch(SortedXIndex,x, 0);
//		 //  logger.info( "  the index found by binary search is "+newIndexpoint);
//			if (newIndexpoint==-1){
//				// add at the begining 
//				SortedXIndex.add(0, this.points.size()-1);
//			}
//			else if (newIndexpoint==-2){
//				SortedXIndex.add(this.points.size()-1);
//			}
//			else {
//				SortedXIndex.add(newIndexpoint, this.points.size()-1);
//			}
//			
//		   
//			newIndexpoint=BinarySearch(SortedYIndex,y, 1);
//			if (newIndexpoint==-1){
//				// add at the begining 
//				SortedYIndex.add(0, this.points.size()-1);
//			}
//			else if (newIndexpoint==-2){//at the end 
//				SortedYIndex.add(this.points.size()-1);
//			}else {
//				SortedYIndex.add(newIndexpoint, this.points.size()-1);
//				
//			}
//			
//			
//			
//			newIndexpoint=BinarySearch(SortedPointIndex,loc, 1);
//			if (newIndexpoint==-1){
//				// add at the begining 
//				SortedPointIndex.add(0, this.points.size()-1);
//			}
//			else if (newIndexpoint==-2){//at the end 
//				SortedPointIndex.add(this.points.size()-1);
//			}else {
//				SortedPointIndex.add(newIndexpoint, this.points.size()-1);
//				
//			}
//			
//			
//			
////			logger.info(" $%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%5");
////			logger.info("SortedXIndex    " + SortedXIndex);
////			logger.info(" $%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%5");
////			logger.info(" SortedYIndex   "+ SortedYIndex);
//		}
		 
	}

	private double getTestValue (ArrayList<Integer> arr, int index, int type ){
		
	     double testValue=0;
		 if (type==0){
	            testValue=this.points.get( arr.get(index)).x;
	        }
	            else if (type==1){ 
	                testValue=this.points.get( arr.get(index)).y;
	            }
	            else if (type==2){ 
	                testValue=this.points.get( arr.get(index)).magnitude();
	            }
	            else if (type==3){
	            	testValue= arr.get(index);
	            }
		 
		 return testValue;
		
	}
    /**
     * Binary search finds item in sorted array.
     * And returns index (zero based) of item
     * If item is not found returns -1
     * Based on C++ example at
     * http://en.wikibooks.org/wiki/Algorithm_implementation/Search/Binary_search#C.2B.2B_.28common_Algorithm.29
     **/
   private int BinarySearch(ArrayList<Integer> arr, double value, int type)
    {       
        int low = 0, high = arr.size()- 1, midpoint = 0;
        double testValue=0,tpointb,tpointa;
        testValue=getTestValue(arr, high, type);
        if (value>testValue){
        	return -2;
        	}

        testValue=getTestValue(arr, low, type);
        
        
        if (value<testValue){
        	return 0;
        	}
       
        
        
        while (low <= high)
        {
        	//logger.info( "  low is "+low+"  hight is "+high);
            midpoint = (low + high) / 2;
                 testValue=getTestValue(arr, midpoint, type);
             if (midpoint>1&&midpoint<arr.size()-1){

            	  tpointb=getTestValue(arr, midpoint-1, type);
            	  tpointa=getTestValue(arr, midpoint+1, type);
   
             }
             else {
                tpointa=testValue;
            	 tpointb=testValue;
             }

             
            // check to see if value is equal to item in array
            if (value == testValue)
            {                    
                return midpoint;
            }
            else if (value>=tpointb && value<=testValue){
            	return midpoint;
            }
            else if (value>=testValue && value<=tpointa){
            	return midpoint+1;
            }
            else if (value < testValue)
                high = midpoint - 1;
            else
                low = midpoint + 1;
        }

        // item was not found
        return -1;
    }

	private void updateStatiscal(PointData point) {
		StatisticalInfo.updateFunctions(point, this);
		StatisticalInfo.updateBoundingBox(point, this);
	}

	private void updateStatiscal() {
		StatisticalInfo.updateFunctionsAndBox(this);
	}

	// @Deprecated
	// private void updateStatiscalData(PointData point){
	//		
	//	  
	// if (points.size()==1)
	// {
	// distanceCalculationFeature function
	// =(distanceCalculationFeature)StatisticalInfo.getDistance().getFunc();
	// function.setNoPointInStroke(points.size());
	// // initalize the functions
	// StatisticalInfo.getDistance().updateFunctionWithPoint(point, this);
	// StatisticalInfo.getSpeed().updateFunctionWithPoint(point, this);
	// StatisticalInfo.getTimeDiff().updateFunctionWithPoint(point, this);
	// StatisticalInfo.getDirection().updateFunctionWithPoint(point, this);
	// curvatureCalculateionFeature funct =
	// (curvatureCalculateionFeature)StatisticalInfo.getCurvature().getFunc();
	// funct.setStroke(this);
	// StatisticalInfo.getCurvature().updateFunctionWithPoint(point, this);
	// }
	// if (points.size()>=3)
	// {
	// distanceCalculationFeature function
	// =(distanceCalculationFeature)StatisticalInfo.getDistance().getFunc();
	// function.setNoPointInStroke(points.size());
	// // start calcuating
	// StatisticalInfo.getDistance().updateFunctionWithPoint(point, this);
	//			
	// StatisticalInfo.getSpeed().updateFunctionWithPoint(point, this);
	// StatisticalInfo.getTimeDiff().updateFunctionWithPoint(point, this);
	// StatisticalInfo.getDirection().updateFunctionWithPoint(point, this);
	// curvatureCalculateionFeature funct =
	// (curvatureCalculateionFeature)StatisticalInfo.getCurvature().getFunc();
	// funct.setStroke(this);
	// StatisticalInfo.getCurvature().updateFunctionWithPoint(point, this);
	// //StatisticalInfo.getCurvature().updateFunctionWithPoint(point, this);
	//			
	// // now every function is updataed
	// }
	//		
	// }
	// private void updateBoundingBox(PointData point){
	//		
	// //if this is the first point then set it with the rectangle both
	// //top left and right low corners
	// if (points.size()==1)
	// {
	// Rectangle r=new Rectangle();
	// r.add(point.getPointLocation());
	// if (StatisticalInfo.getBox()!=null)
	// StatisticalInfo.getBox().setBounds(r);
	// else
	// StatisticalInfo.setBox(r);
	// }
	// // else this is just a new point i have to check
	// else if (points.size()>1){
	// Rectangle currentrectangle= StatisticalInfo.getBox();
	//				
	// //1 )if it is out of the current rectangle or in
	// if(currentrectangle.contains(point.getPointLocation()))
	// {
	// //it is contain
	// // if in do no action
	// }
	// else
	// {
	// // if out
	// //add this point to the rectangle to create a larger one
	// currentrectangle.add(point.getPointLocation());
	//					
	// }
	//			
	//			
	//			
	//		
	// }
	//		
	//		
	// }
	public void calculateStrokeData() {
		// / if the points exist without using the
		// add functions .
		// first compute the bounding box
		//
		
		if (StatisticalInfo == null){
		//	logger.info("genereating the statistical data at firest");
			StatisticalInfo = StrokeStatisticalData.BuildStorkeData(this);
		}
		
		computeRemainigStatists();
		generateAllDominatePoints();
		// StatisticalInfo.getData();
		// for (int i = 0; i < points.size(); i++) {
		//		
		// }
	}

	public void clearAllPoints() {
		this.points.clear();
		StatisticalInfo.clear();
	}

	public String toString() {
		String s = "";

		s = " Start Point " + points.get(0).toString() + "   points are  =  "
				+ points.toString();

		return s;

	}

	public void drawStroke(Graphics g) {
		// for (int i = 0; i < points.size() - 1; i++) {
		// g.drawLine((int) ((PointData) points.get(i)).getX(),
		// (int) ((PointData) points.get(i)).getY(),
		// (int) ((PointData) points.get(i + 1)).getX(),
		// (int) ((PointData) points.get(i + 1)).getY());
		// }
		
		drawStroke(g, Color.BLUE, Color.RED);
//		if (DrawingDebugUtils.DEBUG_GRAPHICALLY)
//			drawStroke(DrawingDebugUtils.getGraphics(),Color.BLUE, Color.RED);
	}

	public void drawStroke(Graphics g, Color color) {
		g.setColor(color);

		for (int i = 0; i < points.size() - 1; i++) {
			g.drawLine((int) ((PointData) points.get(i)).getX(),
					(int) ((PointData) points.get(i)).getY(),
					(int) ((PointData) points.get(i + 1)).getX(),
					(int) ((PointData) points.get(i + 1)).getY());
		}// for
	}// draw stroke

	public void drawStroke(Graphics g, Color linecolor, Color pointColor) {
		g.setColor(linecolor);
		
		// This draw the storke... 
		for (int i = 0; i < points.size() - 1; i++) {
			g.drawLine((int) ((PointData) points.get(i)).getX(),
					(int) ((PointData) points.get(i)).getY(),
					(int) ((PointData) points.get(i + 1)).getX(),
					(int) ((PointData) points.get(i + 1)).getY());
			
			
			if (SystemSettings.DrawPoints)
			{
				g.setColor(Color.BLUE);
				g.drawRect((int) ((PointData) points.get(i)).getX(),
						(int) ((PointData) points.get(i)).getY(),
						2,2);
				g.fillRect((int) ((PointData) points.get(i)).getX(),
						(int) ((PointData) points.get(i)).getY(),
						2,2);
			}
			
			
			
		}
		

		if (SystemSettings.USE_NEW_CHANGES){
			// i want to draw the places of turns....
			 if (turnsIndex!=null){
				 
				 for (int i = 0; i <  turnsIndex.size(); i++) {
					
					 pointChange temp = changes.get(turnsIndex.get(i));
					 
					 
					 // now i need to draw a somting 
			//		 if (temp.turn){
						 
						  int ind =temp.index;
						  
						  if (temp.turnType<=temp.TURN_M){
						  // draw around this poing . 
						  g.setColor(Color.PINK);
						  }
//						  else if (temp.turnType==temp.TURN_ALL){
//							    g.setColor(Color.black);
//						  }
						  else {
							  g.setColor(Color.red);
						  }
							g.drawRect((int) ((PointData) points.get(ind)).getX(),
									(int) ((PointData) points.get(ind)).getY(),
									4,4);
							g.fillRect((int) ((PointData) points.get(ind)).getX(),
									(int) ((PointData) points.get(ind)).getY(),
									3,3);
						 
				//	 }
					 
					 
					 
					 
				}
				 
			 }
			 
			 
			 if (boxes!=null){
				  g.setColor(Color.GRAY);
				 for (int i = 0; i < boxes.size(); i++) {
				
					 g.drawRect((int)boxes.get(i).getX(), (int)boxes.get(i).getY(),(int) boxes.get(i).getWidth(),(int) boxes.get(i).getHeight());
					 
				}
				 
				 
			 }
			 if (OverTracePoints!=null){
				   g.setColor(Color.ORANGE);
				 for (int i = 0; i <OverTracePoints.size(); i++) {
					  int ind =OverTracePoints.get(i);
					 
						g.drawRect((int) ((PointData) points.get(ind)).getX(),
								(int) ((PointData) points.get(ind)).getY(),
								4,4);
						g.fillRect((int) ((PointData) points.get(ind)).getX(),
								(int) ((PointData) points.get(ind)).getY(),
								3,3);
					 
					 
				}
				 
			 }
			 
			
			
		}
		
		
		PointData temp;
		if (SystemSettings.DrawChords&&SmallestX!=null){
			g.setColor(Color.GREEN);
			
			g.drawLine((int) (SmallestX).getX(),
					(int) (SmallestX).getY(),
					(int) (LargestX).getX(),
					(int) (LargestX).getY());
			
			g.drawRect((int) (SmallestX).getX(),
					(int) (SmallestX).getY(),
					4,4);
			g.drawRect((int) (LargestX).getX(),
					(int) (LargestX).getY(),
					4,4);
			g.setColor(Color.ORANGE);
			g.drawLine((int) (SmallestY).getX(),
					(int) (SmallestY).getY(),
					(int) (LargestY).getX(),
					(int) (LargestY).getY());
			
			g.drawRect((int) (SmallestY).getX(),
					(int) (SmallestY).getY(),
					4,4);
			g.drawRect((int) (LargestY).getX(),
					(int) (LargestY).getY(),
					4,4);
		}
		
		if (MaxDirection!=null){
			g.setColor(Color.BLACK);
				g.drawRect((int) (MaxDirection).getX(),
						(int) (MaxDirection).getY(),
						4,4);
			g.fillRect((int) (MaxDirection).getX(),
					(int) (MaxDirection).getY(),
					4,4);
			
			
			
			g.drawRect((int) (MinDirection).getX(),
					(int) (MinDirection).getY(),
					4,4);
		g.fillRect((int) (MinDirection).getX(),
				(int) (MinDirection).getY(),
				4,4);
			
		}
		// g.setColor(Color.green);
//		if (StatisticalInfo != null) {
//			
//		}// if of teh StatisticalInfo check

		if (StatisticalInfo != null) {

			// now draw the rectangle of bounds
			if (StatisticalInfo.getBox() != null && (!dirty)) {

				if (SystemSettings.STATISTICAL_POINTS_DRAW)
					StatisticalInfo.paint(g);
				// logger.info( " x "+StatisticalInfo.getBox().getX()+" y
				// "+StatisticalInfo.getBox().getY());
				g.setColor(Color.GRAY);
				g.drawRect((int) StatisticalInfo.getBox().getX()-1,
						(int) StatisticalInfo.getBox().getY()+1,
						(int) StatisticalInfo.getBox().getWidth()+1,
						(int) StatisticalInfo.getBox().getHeight()+1);

			}
		}
	}// close of paint

	/**
	 * @return the startPoint
	 * @uml.property name="startPoint"
	 */
	public PointData getStartPoint() {
		return StartPoint;
	}

	/**
	 * @return the endPoint
	 * @uml.property name="endPoint"
	 */
	public PointData getEndPoint() {

		return EndPoint;
	}

	public long getStrokeTime() {
		long returnlong = (EndPoint.getTime() - StartPoint.getTime());
		return returnlong;
	}

	/**
	 * @return the StatisticalInfo
	 * @uml.property name="StatisticalInfo"
	 */
	public StrokeStatisticalData getStatisticalInfo() {
		if (StatisticalInfo == null) {
			calculateStrokeData();

		}
		return StatisticalInfo;
	}

	/**
	 * @param StatisticalInfo
	 *            the StatisticalInfo to set
	 * @uml.property name="StatisticalInfo"
	 */
	public void setStatisticalInfo(StrokeStatisticalData data) {
		this.StatisticalInfo = data;
	}

	public void computeAreaOfStroke() {
		// first are then try to get the centroid
		double area = 0.0;

		double a = 0.0;

		PointData pi, pj;

		// logger.info("------------------------------Error---------
		// -------------------");
		// logger.info(this.toString());
		// logger.info("nubmer of polygong in this
		// solution"+polygonVertices.size());
		// for (int i = 0; i < points.size()-1; i++) {
		// //now i
		// pi=points.get(i);
		// // {
		// pj=points.get(i+1);
		// a = (pi.getX()*pj.getY())-(pj.getX()*pi.getY());
		// area+=a;
		//        
		//		
		// }
		// area*=0.5;
		area = ComputationsGeometry.computeArea(points);
		getStatisticalInfo().setArea(area);
	}

	public void computeRemainigStatists() {
		computeAreaOfStroke();
	}

	public void generateAllDominatePoints() {
		getStatisticalInfo().generateAllDominatePoints();

		// //System.out
		// // .println("---------------Compute critical
		// point-----------------");
		// FileLog
		// .addString("---------------get critical point-----------------");
		// // corners,bounding box ....
		// // StrokeLib.computeSpeedCriticalPoints(Evt.getEventStroke());
		// // StrokeLib.computeCurvatureCriticalPoints(Evt.getEventStroke());
		//			
		// //for sped StatisticalInfo get min of a certian threhold.
		// // for speed make it the average of spped as threshold
		// ArrayList indeces=null;
		// // StrokeStatisticalData tempData=getData();
		// ArrayList tempD=new ArrayList();
		//			
		// FeatureFunction func=getStatisticalInfo().getSpeed();
		// //
		// //
		//			
		//		
		//	
		// func.setDataThreshold(func.getAverage());
		//			
		// tempD=new ArrayList();
		// //
		// indeces=func.calcuateLocalMinPoints();
		//			
		// getFunctionDominatePoints(indeces, null, tempD);
		// //////////////////////finish speed now get time
		//					
		// func=StatisticalInfo.getTimeDiff();
		//		
		//
		// func.setDataThreshold(func.getAverage());
		// ArrayList indeces2=func.calcuateLocalMaxPoints();
		//			
		// //
		// getFunctionDominatePoints(indeces2, indeces, tempD);
		//			
		//
		//			
		// ////////////////now the direction
		// func=StatisticalInfo.getDirection();
		// func.setDataThreshold(func.getAverage());
		//
		// ArrayList indeces3=func.calcuateLocalMaxPoints();
		// getFunctionDominatePoints(indeces3, indeces, tempD);
		//			
		//			
		// ///////////////////now checck curvature
		//			
		// func=StatisticalInfo.getCurvature();
		// func.setDataThreshold(func.getAbsaverage());
		// logger.info("the avergaeof curvature is "+func.getAverage());
		// logger.info("the threshold average is "
		// +func.getAbsaverage());
		// ArrayList tempindeces4=func.calcuateLocalAbsolutePoints(1);
		// ArrayList indeces4= new ArrayList();
		// int orignal=0,newi;
		// for (int i = 0; i < tempindeces4.size(); i++) {
		// orignal=(Integer)tempindeces4.get(i);
		// newi=orignal+SystemSettings.STROKE_CONSTANT_NEIGHBOURS;
		// if (newi>=points.size())
		// indeces4.add(0);
		// else
		// indeces4.add(newi);
		//				
		// }
		// getFunctionDominatePoints(indeces4,indeces, tempD);
		//			
		//
		// StatisticalInfo.setDominatePoints(tempD);
		//			
		// logger.info("number of dominat points is "+tempD.size());
	}

	@Deprecated
	private void addSorted(ArrayList list, int index) {
		int temp;
		for (int i = 0; i < list.size(); i++) {
			temp = (Integer) list.get(i);

			if (temp > index) {
				list.add(i + 1, index);
				return;
			}
		}
		// upp till now it is the largest
		// add it now
		list.add(index);
	}

	@Deprecated
	private boolean indexExist(ArrayList list, int index) {
		int temp;
		for (int i = 0; i < list.size(); i++) {
			temp = (Integer) list.get(i);
			if (temp == index)
				return true;
			if (temp > index)
				return false;
		}
		return false;
	}

	@Deprecated
	private ArrayList getFunctionDominatePoints(ArrayList indeces3,
			ArrayList except, ArrayList tempD) {
		PointData point;

		// ArrayList tempD1;

		// tempD1=new ArrayList();
		if ((indeces3 != null)) {
			// logger.info("frome time "+indeces2.size());
			if (except != null) {

				for (int i = 0; i < indeces3.size(); i++) {
					if (indexExist(except, (Integer) indeces3.get(i))) {
						addSorted(except, (Integer) indeces3.get(i));
						point = points.get((Integer) indeces3.get(i));
						tempD.add(point);
					}

				}
			} else {
				for (int i = 0; i < indeces3.size(); i++) {

					point = points.get((Integer) indeces3.get(i));
					tempD.add(point);

				}

			}
		}
		return tempD;

	}

	/** @link dependency */
	/* # BasicImageStructure lnkBasicImageStructure; */

	public void paint(Graphics2D g) {
		drawStroke(g);
	}

	public void setParam(ArrayList Param) {

	}

	// public InkObject createSubInkObject(int start, int end){
	//		
	// // return a segment from this stroke that will contain the points of the
	// stroke.
	// return createSubSegment(start,end);
	//	
	// }
	@Deprecated
	public Segment createSubSegment(int start, int end) {
		// return a segment from this stroke that will contain the points of the
		// stroke.
		return null;

	}

	@Deprecated
	public SegmentCluster createSubSegments() {
		// from fit or dividtion
		return null;
	}

	@Deprecated
	public SegmentCluster createSubSegments(int count) {
		// each count points create a new segment and add then to the stroke
		return null;
	}

	@Deprecated
	public SegmentCluster createSubSegments(ArrayList vertices) {
		return null;
	}

	public ArrayList<Segment> createSubSegments(SegmentedShape segmentShape) {
		// set subSegments......

		subSegments = new ArrayList<Segment>();
		Segment temp;
		// get segments count
		int Segmentscount = segmentShape.getSegmentsCount();
		if (logger.isDebugEnabled()) {
			//  logger.debug("createSubSegments(SegmentedShape) - @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@----  segment `count is " + Segmentscount + " (" + this.getClass().getSimpleName() + "    " + (new Throwable()).getStackTrace()[0].getLineNumber() + "  )  "); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}
		// for each shape
		for (int i = 0; i < Segmentscount; i++) {

			temp = segmentShape.getSegmentOfIndex(i);

			subSegments.add(temp);

		}
		return subSegments;
	}

	private ArrayList<Segment> subSegments;
	private boolean RepeatRemoved;
	private PointData SmallestX=null;
	private PointData SmallestY=null;
	private PointData LargestX=null;
	private PointData LargestY=null;
	private boolean	SelfIntersect;

	@Override
	public InkInterface createSubInkObject(int start, int end) {
		// {
		//	
		//	
		//	
		Stroke ink = new Stroke();
		ArrayList<PointData> po = new ArrayList<PointData>();
		if (this.points != null) {
//			logger.info("   number of ponit in this stroke = "+this.points.size()+" (" + this.getClass().getSimpleName()
//					+ "    "
//					+ (new Throwable()).getStackTrace()[0].getLineNumber()
//					+ "  )  ");
			for (int i = start; (i < this.points.size()) && (i < end); i++) {
				
				po.add(this.points.get(i));
			}

		}
		ink.setPoints(po);
		// return a segment from this stroke that will contain the points of the
		// stroke.
		return ink;
		//
		// }

	}

	/**
	 * @return the interpolated
	 */
	public boolean isInterpolated() {
		return interpolated;
	}

	/**
	 * @param interpolated the interpolated to set
	 */
	public void setInterpolated(boolean interpolated) {
		this.interpolated = interpolated;
	}

	public void wirte(Logger log) {
		String str="Stroke has "+ points.size() +" points = [";
		 for (int i = 0; i < this.points.size(); i++) {
		str+="P("+points.get(i).x+","+points.get(i).y+"),";
	}
		 log.info(str);
		 log.info(" Bonding box is  = Corner P("+ this.getBox().getX()+", "+this.getBox().getY()+"), w= "+ this.getBox().getWidth()+", h=  "+this.getBox().getHeight());
		 
	}
	public Stroke RemoveRepeatedPoints(){
		if (this.points.size()>0){
			logger.info(" the number of points before removal is "+this.points.size());
		double	thershold=SystemSettings.ThresholdDistancePoint*this.getLength();
			Stroke NewInterploated=new Stroke();
			  ArrayList pointsa = new ArrayList<PointData>();
			 
		        PointData prev =this.points.get(0);
		        PointData point=null;
		        NewInterploated.addPoint(prev);
		        NewInterploated.setStartPoint(prev);
		        
		        if (debug)
		        {
		        	if (DrawingDebugUtils.DEBUG_GRAPHICALLY){
		        		
		        		DrawingDebugUtils.drawPointPath(DrawingDebugUtils.getGraphics(), DrawingDebugUtils.InkColor, DrawingDebugUtils.PointsColor, this.points);
		        		
		        	}
		        }
		        
		        
		     //   pointsa.add(prev);
		        for(int i = 1; i < points.size(); i++)
		        {
		             point = points.get(i);
	//	            double dist = point.distance(prev);
		             // check the x and y and time of 
		             
		             if (point.x!=prev.x && point.time!=prev.time && point.y!=prev.y  )
		             {  // no x or y or time  
		            	 
		            	 prev=point;
		            	 NewInterploated.addPoint(point);
		             }
		             else {
		            	 logger.info("the point has same in one dimention... "+prev +"prev   point "+point);
		            	 if (point.x==prev.x && point.time==prev.time)
			             {// same x 
			            	 //skip 
			             }
		            	 else if (point.y==prev.y && point.time==prev.time){
			            	 // same y 
			            	// skip  
			             } 
		            	 else {
		            		 // only one is the same...
		            		 
		            		 // check if distance is larger than thershol 
		            	  double dist = point.distance(prev); 
		            		  // Maybe not same x or same y but same time... 
		            		 if (point.time==prev.time){
		            			 //same time 
		            			 if (dist>thershold){
		            				 prev=point;
					            	 NewInterploated.addPoint(point);
		            			 }
		            			 ////////////
		            			// 
		            	 }
		            	  else {/// either x 
		            	//	 logger.info(" in state where not equal time but equal either x or y");
		            		 // x may = x but we not in same time.
		            		 /// y may = prev y but not in same time 
		            			 prev=point;
				            	 NewInterploated.addPoint(point);
		            			 
//		            		 long diffTime=point.time-prev.time;
//		            		 if (diffTime>SystemSettings.ThresholdTimeADD){
//		            			 
//		            			 prev=point;
//				            	 NewInterploated.addPoint(point);
//		            		 }
//		            		 else {
//		            			 
//		            			 
//		            		 }
		            		 // check time. 
		            		 
		            	 }
		            	 }
		            	
		             }
		             

		            
		        }
		        if (point!=null)
		        	NewInterploated.setEndPoint(point );
		        
		        
		        NewInterploated.setRepeatedRemoved(true);
		        logger.info("number of ponits after removal of repeated "+NewInterploated.getPointsCount());
		        
		        if (debug)
		        {
		        	if (DrawingDebugUtils.DEBUG_GRAPHICALLY){
		        		
		        		DrawingDebugUtils.drawPointPath(DrawingDebugUtils.getGraphics(), Color.green, Color.ORANGE,NewInterploated.points);
		        		
		        	}
		        }
		        
		        
		        
		        return   NewInterploated;
			
			}
			else{
				
				return this;
			}
		
	}

	private void setRepeatedRemoved(boolean b) {
	 RepeatRemoved=b;
		
	}
	public PointData getLargestChordStart(){
		if ( Orientation==ORIENTATION_HORIZONATAL)
		{
			return LargestX;
		}
		else {
			return LargestY;
		}
	}
public PointData getLargestChordEnd(){
	if ( Orientation==ORIENTATION_HORIZONATAL)
	{
		return SmallestX;
	}
	else {
		return  SmallestY;
	}
	}
private void computeLongestDistance(){
		LongestDistanceBetweenPointsInStroke =0;
	 
		 if (points==null)
			 return ;
		 
		 SmallestX=points.get(SortedXIndex.get(0));
		 SmallestY=points.get(SortedYIndex.get(0));
		 LargestX=points.get(SortedXIndex.get(SortedXIndex.size()-1));
		 LargestY=points.get(SortedYIndex.get(SortedYIndex.size()-1));
		
		 double dis1=LargestX.distance(SmallestX);
		 double dis2=LargestY.distance(SmallestY);
		 double dis3=this.EndPoint.distance(StartPoint);
		 
		 logger.info(" The distance from last x to first x is "+dis1);
		 logger.info(" The distance from last y to first y is "+dis2);
		 logger.info(" The distance from end point to start   "+dis3);
		 
		 if (dis1>dis2){
			 LongestDistanceBetweenPointsInStroke=dis1;
			 Orientation=ORIENTATION_HORIZONATAL; //|
			 
				 
		 }
		 else {
			 LongestDistanceBetweenPointsInStroke=dis2;
			 Orientation=ORIENTATION_VERTICAL;
			
		 }
		 logger.info("the length is  "+getLength());
		 EndPointtoLengthRation=dis3/getLength();
		 logger.info("  EndPointtoLengthRation  "+ EndPointtoLengthRation);
		 
		 LongestChordtoLengthRatio=LongestDistanceBetweenPointsInStroke/getLength();
		
		 logger.info(" 	 LongestChordtoLengthRatio "+	 LongestChordtoLengthRatio );
		 
		 
		 
}
private void checkClosedShape(){
 
}


@Deprecated
private ArrayList<zone> processIndex( ArrayList<Integer>   sorted){
	 
	int window = SystemSettings.WINDOW_SCAN_SIZE;
	if(sorted!=null){
		ArrayList<Double> 	slopedSortedXL1=new ArrayList<Double>();
	 
		for (int i = 0; i < sorted.size()-1; i++) {
			slopedSortedXL1.add(new Double (sorted.get(i+1)-sorted.get(i)));
		}// for all sortex to compute the slopel...
		 
		double [] tempX;
 
		
		// current window 
		ArrayList<zone> blocksX=new ArrayList<zone>();
	 
		zone temp;
		
		for (int i = 0; i < (slopedSortedXL1.size()-window); i+=window/2) {
			tempX=new double [window];
	 
			for (int j = 0; j < tempX.length; j++) {
				tempX[j]=slopedSortedXL1.get(i+j);
			}
			
			temp=DetectedZone (tempX);
			if (temp!=null){
				temp.start=i;
				temp.end=i+window;
			  blocksX.add( temp);
			}
		
			// now i am creating a window to check the array... 
			// create a zone for this window..
		}// while the loops of sorted x.... 
		logger.info("  BlocksX =   "+ blocksX );
		ArrayList<zone> finalBlocksX= mergeSimilarZones(  blocksX,window,slopedSortedXL1);
			logger.info("   finalBlocksX =   "+ finalBlocksX );
		// now return the number of zone with only type 2 
	 	ArrayList<zone>  returnBlocks=new ArrayList<zone>( );
	 	for (int j = 0; j < finalBlocksX.size(); j++) {
			if (finalBlocksX.get(j).type==zone.TYPE_ULTERNATING){
				returnBlocks.add(finalBlocksX.get(j));
			}
		}
	 	
		return  returnBlocks;
	}//if sortex x 
	return null;
}
@Deprecated
private 	ArrayList<zone> processIndexArray(){
	int start=0;
	int end=0; 
	int window = SystemSettings.WINDOW_SCAN_SIZE;
	if(SortedXIndex!=null){
		ArrayList<Double> 	slopedSortedXL1=new ArrayList<Double>();
		ArrayList<Double> 	slopedSortedYL1=new ArrayList<Double>();
		ArrayList<Double>  slopedSortedPoint=new ArrayList<Double>();
		for (int i = 0; i < SortedXIndex.size()-1; i++) {
			slopedSortedXL1.add(new Double (SortedXIndex.get(i+1)-SortedXIndex.get(i)));
			slopedSortedYL1.add( new Double (SortedYIndex.get(i+1)-SortedYIndex.get(i)) );
			slopedSortedPoint.add( new Double (SortedPointIndex.get(i+1)-SortedPointIndex.get(i)) );
			
		}// for all sortex to compute the slopel...
		 
		double [] tempX;
		double [] tempY;
			double [] tempXY;	
		
		// current window 
		ArrayList<zone> blocksX=new ArrayList<zone>();
		ArrayList<zone> blocksY=new ArrayList<zone>();
		ArrayList<zone> blocksXY=new ArrayList<zone>();
		zone temp;
		
		for (int i = 0; i < (slopedSortedXL1.size()-window); i+=window/2) {
			tempX=new double [window];
			tempY=new double [window];
			tempXY=new double [window];
			for (int j = 0; j < tempX.length; j++) {
				tempX[j]=slopedSortedXL1.get(i+j);
			}
			
			temp=DetectedZone (tempX);
			if (temp!=null){
				temp.start=i;
				temp.end=i+window;
			  blocksX.add( temp);
			
			}
			// do the same for y 
			for (int j = 0; j < tempY.length; j++) {
				tempY[j]=slopedSortedYL1.get(i+j);
			}
			temp=DetectedZone (tempY);
			if (temp!=null){
				temp.start=i;
				temp.end=i+window;
			  blocksY.add( temp);
			}
		
			
			
			
			/// do the same for magnitude 
			// do the same for y 
			for (int j = 0; j < tempXY.length; j++) {
				tempXY[j]=slopedSortedPoint.get(i+j);
			}
			temp=DetectedZone (tempXY);
			if (temp!=null){
				temp.start=i;
				temp.end=i+window;
			  blocksXY.add( temp);
			}
			
			
			// now i am creating a window to check the array... 
			// create a zone for this window..
		}// while the loops of sorted x.... 
		
		logger.info("  BlocksX =   "+ blocksX );
		logger.info("    BlocksY =   "+ blocksY );
		logger.info("    BlocksXY =   "+ blocksXY );
		ArrayList<zone> finalBlocksX= mergeSimilarZones(  blocksX,window,slopedSortedXL1);
		
 	ArrayList<zone> finalBlocksY= mergeSimilarZones(  blocksY,window,slopedSortedYL1);
	 	ArrayList<zone> finalBlocksXY= mergeSimilarZones(  blocksXY,window,slopedSortedPoint);	
			logger.info("   finalBlocksX =   "+ finalBlocksX );
		logger.info("    finalBlocksY =   "+ finalBlocksY );
		logger.info("    finalBlocksY =   "+ finalBlocksXY );
		
 
		
		// now return the number of zone with only type 2 
		
	 	ArrayList<zone>  returnBlocks=new ArrayList<zone>( );
	 	for (int j = 0; j < finalBlocksX.size(); j++) {
			if (finalBlocksX.get(j).type==zone.TYPE_ULTERNATING){
				returnBlocks.add(finalBlocksX.get(j));
			}
		}
	 	
		return  returnBlocks;
	}//if sortex x 
	return null;
	
}
//  now i need 



private zone DetectedZone(double[]  array){
	// this arrary of all 
	
	int countN=0;
	int countP=0;
	int countZ=0;
	int Sum=0;
	int type=zone.TYPE_INC;
	for (int i = 0; i < array.length; i++) {
		
		Sum+=array[i];
		if (array[i]>=0){
			countP++;
			if (array[i]==0)
				countZ++;
			
		}
		else {
			countN++;
		}
		
	}
	
	
	int countNumberOfDifferentSigns=Math.abs(countN-countP);

	if ((countP-countN)>zone.signTolerance){
			type=zone.TYPE_INC;
		}
		else if ((countN-countP)>zone.signTolerance){
			type=zone.TYPE_DEC;
		}
		
		else {
			// number of signs are nearly the sam... 
			// so check the sum... 
			type=zone.TYPE_ULTERNATING;
			
//				if(Math.abs(Sum)<zone.SumTolerance){
//		// check the nubmer of countsss. 
//		  
//	}
			
		}

	 
	
	
	
		zone  t =new zone();
	
	t.countOfNeg=countN;
	t.countOfPos=countP;
	t.countOfSigns=countNumberOfDifferentSigns;
	t.size=array.length;
	t.sum=Sum;
	t.type=type;
	
	
	
	
	
	return t;
}
private ArrayList<zone> mergeSimilarZones(ArrayList<zone>  blocksX, int window, ArrayList<Double> slopedSorted){
	//int window = SystemSettings.WINDOW_SCAN_SIZE;
	ArrayList<zone> finalBlocksX=new ArrayList<zone>();
	zone tempz ;
	int type;
	int typej;
	// now check to merge zoness....
	for (int i = 0; i < blocksX.size(); i++) {
	
		// look for the fist block that has different type...
		type=blocksX.get(i).type;
		int lastsimilar=i;
		for (int j = i+1; j <blocksX.size(); j++) {
			typej=blocksX.get(j).type;
					
			if (type!=typej){
				
		
				break;
			}
			lastsimilar=j;
		}
		
		if (lastsimilar==i){
			// no change then add the block as it is 
			finalBlocksX.add(blocksX.get(i));
		}
		else {
			logger.info("  Merging the blocks  "+i+"   till  "+lastsimilar);
					tempz = mergeSimilarBlocks(blocksX,i,lastsimilar, slopedSorted);
					if (tempz!=null){
					
					finalBlocksX.add(tempz);
					// and jump next one 
					
						}
					i=lastsimilar;
		}
		

		
	}
	return finalBlocksX;
}


private ArrayList<zone> mergeDifferentZones(ArrayList<zone>  blocksX, int window, ArrayList<Double> slopedSorted){
	//int window = SystemSettings.WINDOW_SCAN_SIZE;
	ArrayList<zone> finalBlocksX=new ArrayList<zone>();
	zone tempz ;
	int type;
	int typej;
	// now check to merge zoness....
	for (int i = 0; i < blocksX.size(); i++) {
	
		// look for the fist block that has different type...
		type=blocksX.get(i).type;
		int lastsimilar=i;
		for (int j = i+1; j <blocksX.size(); j++) {
			typej=blocksX.get(j).type;
					
			if (type!=typej){
				// i am changing check if the two cann be mergezed 
				if (type==zone.TYPE_ULTERNATING || typej==zone.TYPE_ULTERNATING){
					
					
				}
		
				break;
			}
			lastsimilar=j;
		}
		
		if (lastsimilar==i){
			// no change then add the block as it is 
			finalBlocksX.add(blocksX.get(i));
		}
		else {
			logger.info("  Merging the blocks  "+i+"   till  "+lastsimilar);
					tempz = mergeSimilarBlocks(blocksX,i,lastsimilar, slopedSorted);
					if (tempz!=null){
					
					finalBlocksX.add(tempz);
					// and jump next one 
					
						}
					i=lastsimilar;
		}
		

		
	}
	return finalBlocksX;
}
private zone mergeSimilarBlocks(ArrayList<zone> blocksX, int i, int j, ArrayList<Double> slopedSorted) {
	 
	// look for zone starting from i to j if 
	if (blocksX!=null){
	
	int prevType=0;
	boolean merge=true;
	int countN=0;
	int countP=0;
	int countZ=0;
	double Sum=0;
	int start=i;
	int end=j;
	int size=0;
	int type=zone.TYPE_INC;
	for (int k = i; k < blocksX.size() && k<=j; k++) {
	
		if (k==i){
			prevType=blocksX.get(k).type;
			 countN=blocksX.get(k).countOfNeg;
			 	 countP=blocksX.get(k).countOfPos;		 
			 	 Sum = blocksX.get(k).sum;
			 	 start=blocksX.get(k).start;
			 	 size=blocksX.get(k).size;
		}
		
		else {
			if (prevType!=blocksX.get(k).type){ //merge together.....
				
				merge=false;
				
			}
		 	 countN+=blocksX.get(k).countOfNeg;
		 	 countP+=blocksX.get(k).countOfPos;		 
		 	 Sum+= blocksX.get(k).sum; 
			 end=blocksX.get(k).end;
			 size+=blocksX.get(k).size;
		}
		
		
	}// after the locks i am looking into...
	 
	int nendSize=end-start+1;
	
	double[]  arr=new double [nendSize];
	
	for (int k =0; k < arr.length ; k++) {
		arr[k]=slopedSorted.get(  start+k);
	}
	
zone temp= DetectedZone(arr);
 
		zone  t =new zone();
	t.countOfNeg=temp.countOfNeg;
	t.countOfPos=temp.countOfPos;		 
	 	t .sum=temp.sum;
	 	t.start=start;
	 	t.end=end;
		t.countOfSigns=Math.abs(t.countOfNeg-t.countOfPos);
	 	t.type=prevType;
	 	t.size=temp.size;
           return t;		

	}
	return null;
 
}
private zone mergeDifferentZonesBlocks(ArrayList<zone> blocksX, int i, int j, ArrayList<Double> slopedSorted) {
	 
	// look for zone starting from i to j if 
	if (blocksX!=null){
	
	int prevType=0;
	boolean merge=true;
	int countN=0;
	int countP=0;
	int countZ=0;
	double Sum=0;
	int start=i;
	int end=j;
	int size=0;
	int type=zone.TYPE_INC;
	for (int k = i; k < blocksX.size() && k<j; k++) {
	
		if (k==i){
			prevType=blocksX.get(k).type;
			 countN=blocksX.get(k).countOfNeg;
			 	 countP=blocksX.get(k).countOfPos;		 
			 	 Sum = blocksX.get(k).sum;
			 	 start=blocksX.get(k).start;
			 	 size=blocksX.get(k).size;
		}
		
		else {
			if (prevType!=blocksX.get(k).type){ //merge together.....
				
				merge=false;
				
			}
		 	 countN+=blocksX.get(k).countOfNeg;
		 	 countP+=blocksX.get(k).countOfPos;		 
		 	 Sum+= blocksX.get(k).sum; 
			 end=blocksX.get(k).end;
			 size+=blocksX.get(k).size;
		}
		
		
	}// after the locks i am looking into...
	
	
	if (merge){
		zone  t =new zone();
	t.countOfNeg=countN;
	t.countOfPos=countP;		 
	 	t .sum=Sum;
	 	t.start=start;
	 	t.end=end;
		t.countOfSigns=Math.abs(t.countOfNeg-t.countOfPos);
	 	t.type=prevType;
	 	t.size=size;
           return t;		
	}
	
	else {
		
		double[]  arr=new double [end-start+1];
		
		for (int k =0; k < arr.length ; k++) {
			arr[k]=slopedSorted.get(  start+k);
		}
		
		return DetectedZone(arr);
		
		
	}
	
	}
	
	
	
 
	// first rulles 
	// if all blocks has same type then merge...
	
	// o.w use the detect zone to detect the orginal zones of the 
	// if  the blocks has pattern like  ( I D U , DU I,) ===> merge to U
	// 
	
	
	return null;
}

@Deprecated
private void procesSortedX(){
	
	
//	self intersectin, overtrace, 
//	process sorted x values to region
//	either 
//	1. decreasing 
//	2. increasing 
//	3. chanign (rapid index changing )
//
//	in decreasing and increasing is straight line or curve... non intersecting , non 
//	in change section (found by first different for example 1 2 3  200 4 203 5 6  )  then "3 200 4 203 5 " is a change region..(also could include 2,6).
//	There can be three explanation...
//	normal drawing  (check the the y values of  the points and if they are noot near (threshold controled by stroke length and max length between points in stroke  ) then it is normal ....
//
//
//	2. stroke is self intersecting.... (check if the segment interstion (create line of the segments 
//	in previous example ... 3 4 5 6 is a segment 200 203 is another ==> create lines and check if theses lines are intersecting or paralle 
//	if parallel then ==> overtrace....
//	if intersecton then ==> self intersection...
//
//	3. the stroke is overtraced.
	int zone=0;  // 0 inc, 1 dec, 2 changing
	boolean zoneChanged=false;
	 
	int start=0;
	int end=0; 
	int prevx=0,x;
	int dif;
	int pervZone;
	int count=0;
	int countDec=0, countInc=0,countChange=0;
	int[] decIncPattern=new int[SortedXIndex.size()-1];
	ArrayList<zone> zones=new ArrayList<zone>();
	if(SortedXIndex!=null){
		ArrayList<Double> 	slopedSortedXL1=new ArrayList<Double>();
		ArrayList<Double> 	slopedSortedXL2=new ArrayList<Double>();
		for (int i = 0; i < SortedXIndex.size()-1; i++) {
			slopedSortedXL1.add(new Double (SortedXIndex.get(i)-SortedXIndex.get(i+1)));
		}
		for (int i = 0; i < slopedSortedXL1.size()-1; i++) {
			slopedSortedXL2.add(new Double ( slopedSortedXL1.get(i)- slopedSortedXL1.get(i+1)));
			
		}
		
		for (int i = 0; i < SortedXIndex.size(); i++) {
			zoneChanged=false;
			if(i==0){
				start=i;
				prevx=SortedXIndex.get(i) ;
				continue;
			}
			if (i==1){
				
				x=SortedXIndex.get(i);
				if (prevx>x){
					
					zone=1;//dec
				
				}
				else if (x>prevx)
				{
					
					zone=0;//inc
				}
				decIncPattern[i-1]=zone;
				prevx=x;
				continue;
			}
			/////////////////////////this is just for itinitalize..
			x=SortedXIndex.get(i);

			// check if less move in same pattern...
			// make different
			dif=x-prevx;
			if(dif>MaxChangeIndex){  // then x > prevx 
				decIncPattern[i-1]=0;

				if (zone==0){// was inc and now inc
					prevx=x;
				}
				else if (zone==1){/// was dec but now it is inc
					end=i;
					count=end-start;
					// the new start;;;..
					start=i;
					zone=0; // the new zone...
					prevx=x;
					zoneChanged=true;
					countChange++;
				}

			}// dif>0
			else if (dif<-MaxChangeIndex){  //dec range ...
				
				decIncPattern[i-1]=1;
				
				if (zone==0){// was inc and now dec
					end=i;
					count=end-start;
					// the new start;;;..
					start=i;
					zone=1; // the new zone...
					prevx=x;
					zoneChanged=true;
				
				}
				else if (zone==1){/// was dec but now it is inc
					prevx=x;
				}
			}//else dec
			
			if(zoneChanged){
				zone z=new zone();
				z.start=end-count;
				z.size =count;
				z.end=end;
				if (zone==1){
					countInc++;
					z.type=0;
				}
				else{
					z.type=1;
					countDec++;
				}
				zones.add(z);
			
				//create a zone and add it...
				
			}
			
		}//for loop ...
		logger.info("  the derivative is     ");
		logger.info("  the first     "+slopedSortedXL1);
		logger.info("  the  second    "+slopedSortedXL2);
		logger.info(" orignal zones "+zones);
	 logger.info("  number of increase zones. "+countInc+"  count of dec"+countDec);
		zone z;
		int prevtype=0;
		ArrayList<zone> finalzones=new ArrayList<zone>();
		zone temp=null;
		int countz=0;
		// now looop on zones to if zone coutn <2 then merge into 
		for (int j = 0; j < zones.size(); j++) {
			if (j==0){
			prevtype=zones.get(j).type;
			temp=null;
			}
			
			z=zones.get(j);
			
			if(z.size<4){
				if(temp==null){
				// he number ofregion is smakk...
			//now i need to merge this zone to next 	
				temp=new zone();
				temp.start=z.start;
				temp.type=2;
				temp.size=z.size;
				temp.end=z.end;
				}
				else {
					temp.size=temp.size+z.size;
					temp.end=z.end;
				}
			}
			else {
				if (temp==null) //normal zone...
				{
				finalzones.add(z);
				}//
				else{// the previous zones was a changing,,, ones.
					countz++;
					finalzones.add(temp);
					finalzones.add(z);// the current zone...
					temp=null;
					
				}
				
			}
			
			
			
		
		}
		logger.info(" Finalzone...  "+finalzones);
			
			logger.info(" number of changing zone. is  "+countz);
			
			int counti=0;
			int countd=0;
			for (int i = 0; i < finalzones.size(); i++) {
				if (finalzones.get(i).type==0)
					counti++;
				else if (finalzones.get(i).type==1){
					countd++;
				}
				else {
					// the changing values....
					
				}
			}
			
			logger.info("  final  ince of "+counti+"  regions and dec of "+countd);
	}
}
@Deprecated
class zone {
	public static final int	signTolerance=2;
	public static final int	SumTolerance	= 10;
	final static int  TYPE_INC=0;
 final static  	int TYPE_DEC=1;
 final static 	int TYPE_ULTERNATING=2;
	@Override
	public String toString() {
		 String st=" type "+type+"@ start "+start+" end "+end+" count "+countOfSigns;
		return st;
	}
	int type;
	int start;
	int end;
	int size;
	int countOfSigns;  // number of differrent signs..
	int countOfPos;
	int countOfNeg;
	double sum;//sum of values 
}

@Deprecated
private void processZoneList(	ArrayList<zone> list, ArrayList<Integer>   sorted ){
	// this is only the lis of zone that has ulgernating order 
	 if (list!=null){
		 
		 boxes=new ArrayList<Rectangle2D>();
		 for (int i = 0; i <list.size(); i++) {
			// for the first zone do the follwoing 
			 // get the lines in the stroke... 
			 
			 zone temp = list.get(i);
			 
			 int s=temp.start;
			 int e=temp.end;
			 
			 // create a new list of order of this zone... 
			 
			 ArrayList<Integer>   order=new ArrayList<Integer>();
			 for (int j = s; j < e; j++) {	
				 int value=sorted.get(j);
				 if (order.size()==0){
					 order.add(value);
					 continue;
				 }
				// add sorted in order... 
		
				 
					int newIndexpoint=BinarySearch( order,value, 3);
					 //  logger.info( "  the index found by binary search is "+newIndexpoint);
						if (newIndexpoint==-1){
							// add at the begining 
							 order.add(0, value);
						}
						else if (newIndexpoint==-2){
							 order.add(value);
						}
						else {
							 order.add(newIndexpoint, value);
						}
						
			}
			 
			 if (order!=null){
			 /// after this  i have an order zone of values 
			 // ineed to divide it into sections..... 		
				 // or create boxes of it.
			 Rectangle2D	 box=new Rectangle2D.Double(points.get(order.get(0)).getX(),points.get(order.get(0)).getY(),0,0);
			 
			 for (int j = 0; j <order.size(); j++) {
				box.add(points.get(order.get(0)) );
			}
			 
			 boxes.add(box);
			 // divide it into sections and lines 
			 ArrayList<ArrayList<PointData>> linesss=divideList  (order);
			 logger.info( linesss);
		 
		 }
		 }
	 }
}
@Deprecated


private ArrayList<ArrayList<PointData>> divideList(ArrayList<Integer> order) {
	if (order!=null)
	{
		logger.info("  the order list is "+order);
		 ArrayList<ArrayList<PointData>> list=new ArrayList<ArrayList<PointData>>();
		 int lastindex=0;
	for (int i = 0; i < order.size()-1; i++) {
		if (order.get(i+1)-order.get(i)> SystemSettings.WINDOW_SCAN_SIZE){// do the following 
			ArrayList<PointData> l=new ArrayList<PointData>();
			for (int j = lastindex; j < i; j++) {
					l.add( points.get(order.get(j)));
			}
		  list.add(l);
			// create a new array of list of point s
			lastindex=i+1;
		}
	}
	
	return list;
	}
	return null;
}

private void checkOverTraceAndSelfIntersect(){
	// proces teh stork x 
	//procesSortedX();
	ArrayList<zone> list=processIndex(SortedXIndex);
	processZoneList(list, SortedXIndex);
	// ArrayList<zone> list=processIndexArray();
	 if (list!=null){
	 for (int i = 0; i <list.size(); i++) {
		if (isOverTraced(list.get(i))){
			
			this.OverTraced=true;
			break;
		}
	}//all the ulternating list
	 
	 
	 for (int i = 0; i <list.size(); i++) {
			if (isSelfIntersect(list.get(i))){
				
				this.SelfIntersect=true;
				break;
			}
		}//all the ulternating list
		 
	 
	 }	 
}
private boolean isOverTraced(zone x){
	
	//TODO: Commplet the over traceb by finishing this function ( check if zone is an overtraced by detecting is neear enough and if parallel )
	return false;
} 
private boolean isSelfIntersect(zone x){
	//TODO: Commplet the  self intersecting by finishing this function ( check if zone is an overtraced by detecting is neear enough and if intersect )

	return false;
} 
private void checkTails(){
	// check if thre is tails in the 
	//create a parts with the last 10% and first 10 of the stroke...
	int part=(int) (Math.ceil((0.1)*this.points.size()));
	
	if (part<3){
		part=3;
	}
InkInterface start = this.createSubInkObject(0,part);
	InkInterface end = this.createSubInkObject(this.points.size()-part-1,this.points.size()-1);
	 
	if (start.canIntersect(end)){
		
		//TODO: now i need to detect the intersections... 
		
	}
	
	
}

	public void PreProcess() {
	  	//TODO: IMPLEMENT THIS FUNCTION 28 JAN
		 logger.info(" PreProcess	//TODO: IMPLEMENT THIS FUNCTION 28 JAN  ");
		 updateOtherFeatures();
		 logger.info("  OverTraceHyposes  = "+OverTraceHyposes+"  number of points is "+points.size());
		 logger.info( "   if overtrace/points  "+( (double)OverTraceHyposes/(double)points.size() )+
				 "  if  overtrace/(size/2) " +((double)OverTraceHyposes/((double)points.size()/2.0)));
		 
		 logger.info(  "  and location of point is   "+   OverTracePoints);
		 
		 
		computeLongestDistance();
	

		checkOverTraceAndSelfIntersect();
				checkTails();
			checkClosedShape();
	}


	
	
    /**
     * Transform the polyline with the given transform.
     */


}
