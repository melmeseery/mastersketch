package SketchMaster.Stroke.StrokeData;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;

import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Ellipse2D.Double;
import java.io.Serializable;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.sun.org.apache.xerces.internal.impl.dv.xs.FullDVFactory;


import SketchMaster.Stroke.features.FeatureFunction;
import SketchMaster.Stroke.features.StrokeFeatures;
import SketchMaster.io.log.FileLog;
import SketchMaster.lib.ArrayLibrary;
import SketchMaster.lib.ComputationsGeometry;
import SketchMaster.lib.CurveFitData;
import SketchMaster.swarm.curvefit.StrokeCurveSolution;
import SketchMaster.swarm.polygonApproximations.polygonSolution;
import SketchMaster.system.SystemSettings;
import SketchMaster.system.Recogniziers.SimpleSymbolRecognizier;

/**
 * @author Mahi (strokestatisticaldata 118)
 */
public class StrokeStatisticalData implements Serializable {
	private static final Logger logger = Logger.getLogger(StrokeStatisticalData.class);
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8072182009963948465L;

	/**
	 * @uml.property name="box"
	 */
	private Rectangle2D box = null;

	private double Area = 0.0;

	protected InkInterface stroke;

	private ArrayList<DominatePointStructure> controlPoints = null;
	 //CurveFitData  sums;
	 
	 
	// think to how represent easier
	// private ArrayList<PointData> DominatePoints = null;
	// private ArrayList<Integer> DominatePointsIndeces = null;
	// Todo add the use for them
	// private ArrayList<Integer> DominatePointsSource = null;

	// pneedede
	transient protected FeatureFunction distance = null;


	transient private ArrayList<FeatureFunction> functions;
	
	private int deltaCurvature = -1;

	private FeatureFunction CurvatureRotation;
	
	private String cornerFunctionName = "Corner detection vs. distance using FD algorithm";
//
//	private boolean sumsComputed=false;
//
//	private double rotation;
//	boolean rotationComputed=false;

	public void initAll() {
		functions = new ArrayList<FeatureFunction>();
		
		initDistance();
		if (SystemSettings.USE_SPEED)
		initSpeed();
		if (SystemSettings.USE_DIREC)
		initDirection();
		if (SystemSettings.USE_TIME_DIFF)
		initTimeDiff();
		initCurvature();
	}

	public void initSpeed() {
		speedCalcuationFeature speedFun = new speedCalcuationFeature();
		speedFun.setName("Velocity of storke Vs. distance");
		FeatureFunction Speed = new FeatureFunction();
		Speed.setFunc(speedFun);
		Speed.setName("Velocity of storke Vs. distance");

		// FeatureFunction temp = Speed;
		Speed.setXName("  Distance ");
		Speed.setYName(" speed ");
		functions.add(Speed);
	}

	public void initDirection() {
		directionCalculateionFeature Fun = new directionCalculateionFeature();
		FeatureFunction Direction = new FeatureFunction();
		Direction.setFunc(Fun);
		Direction.setName("Direction of storke Vs. distance");

		Direction.setXName("  Distance ");
		Direction.setYName(" direction  ");

		functions.add(Direction);
		
		
		slopeDirectionCalculateionFeature Fun2 = new slopeDirectionCalculateionFeature();
		FeatureFunction Direction2 = new FeatureFunction();
		Direction2.setFunc(Fun2);
		Direction2.setName("Slope of storke Vs. distance");

		Direction2.setXName("  Slope ");
		Direction2.setYName(" direction  ");

		functions.add(Direction2);
		
	}

	public void initTimeDiff() {
		timeDiffCalcuationFeature Fun = new timeDiffCalcuationFeature();
		FeatureFunction TimeDiff = new FeatureFunction();
		TimeDiff.setFunc(Fun);
		TimeDiff.setName("Time difference of storke Vs. distance");

		// FeatureFunction temp = TimeDiff;

		TimeDiff.setXName("  Distance ");
		TimeDiff.setYName(" time diff  ");
		functions.add(TimeDiff);
	}

 

	public void initCurvature() {
		if (SystemSettings.CURVEVATURE_ESTIMATION_1) {
			initCurvatureE1();
		}
		if (SystemSettings.CURVEVATURE_ESTIMATION_2) {
			initCurvatureE2();
		}
		if (SystemSettings.CURVEVATURE_ESTIMATION_3) {
			initCurvatureE3();
		}
          if (SystemSettings.CURVEVATURE_ESTIMATION_4){
        	  
        	  initCurvatureE4();
        	  
          }
		
		
		if (deltaCurvature == -1) {
			deltaCurvature = functions.size() - 1;
		}
	}

	private void initCurvatureE3() {
		  
		changeInRotation fun = new changeInRotation();
		FeatureFunction Curvature = new FeatureFunction();
		Curvature.setFunc(fun);
		Curvature.setName("Curvature of storke Vs. distance Change in rotation");
		// FeatureFunction temp = Curvature;
		Curvature.setXName("  Distance ");
		Curvature.setYName(" Curvature3 ");
		CurvatureRotation=Curvature;
		functions.add(Curvature);
	}

	public void initCurvatureE1() {
		curvatureCalculateionFeature Fun = new curvatureCalculateionFeature();

		FeatureFunction Curvature = new FeatureFunction();
		Curvature.setFunc(Fun);
		Curvature
				.setName("Curvature of storke Vs. distance Using Algorithm FD");
		// FeatureFunction temp = Curvature;
		Curvature.setName(Curvature.getName());
		Curvature.setXName("  Distance ");
		Curvature.setYName(" Curvature ");
		functions.add(Curvature);
		deltaCurvature = functions.size() - 1;
	}

	public void initCurvatureE2() {
		anotherCurvatureCalculateionFeature fun2 = new anotherCurvatureCalculateionFeature();
		FeatureFunction Curvature2 = new FeatureFunction();

		Curvature2.setFunc(fun2);
		Curvature2
				.setName("Curvature of storke Vs. distance Using Algorithm HKAlg");
		Curvature2.setXName("  Distance ");
		Curvature2.setYName(" Curvature 2  ");
		// Curvature2.setName("Second Estimation of Curvature of storke Vs.
		// distance");
		functions.add(Curvature2);
		// mainCurvature=functions.size()-1;
	}
	
	
	public void initCurvatureE4() {
		CurvatursDirectionCalculateionFeature fun2 = new CurvatursDirectionCalculateionFeature();
		FeatureFunction Curvature4 = new FeatureFunction();

		Curvature4.setFunc(fun2);
		Curvature4.setName("Curvature of storke Vs. distance Using change in direction");
		Curvature4.setXName("  Distance ");
		Curvature4.setYName(" Curvature 4 ");
		// Curvature2.setName("Second Estimation of Curvature of storke Vs.
		// distance");
		functions.add(Curvature4);
		// mainCurvature=functions.size()-1;
	}
	
	


	public void initDistance() {
		//logger.info("//TODO  try using the feild distance  pointdata class to calcate the distance  instead of this function  (strokestatisticaldata 118)");
		distanceCalculationFeature Fun = new distanceCalculationFeature();
		this.distance = new FeatureFunction();
		this.distance.setFunc(Fun);
		this.distance.setName("Distance");
		this.distance.setXName(" points ");
		this.distance.setYName("  x  ");

		// i will not add distance and lenght of stroke to funciton becuase is
		// not alcuated.
		// FeatureFunction temp=distance;
		// functions.add(temp);
	}

	public void updateFunctions(PointData point, InkInterface stroke) {
		//sumsComputed=false;
		// logger.info(" i am here ");
		FeatureFunction temp;
		this.stroke = stroke;
		// first update distance
		if (this.stroke.getPoints().size() >= 2)
			distance.updateFunctionWithPoint(point,this.stroke, SystemSettings.STROKE_CONSTANT_NEIGHBOURS);  // change this
		
		for (int i = 0; i < functions.size(); i++) {
			temp = functions.get(i);
			if (this.stroke.getPoints().size() == 1) {
				temp.updateFunctionWithPoint(point, this.stroke, SystemSettings.STROKE_CONSTANT_NEIGHBOURS);
			}
			if (this.stroke.getPoints().size() >  SystemSettings.STROKE_CONSTANT_NEIGHBOURS) {
				if (temp.getName().startsWith( SystemSettings.CURVATURENAME))
					temp.updateFunctionWithPoint(point, this.stroke, SystemSettings.STROKE_CONSTANT_CURVATURE_NEIGHBOURS);
				else 
					temp.updateFunctionWithPoint(point, this.stroke, SystemSettings.STROKE_CONSTANT_NEIGHBOURS);
				
				
				// data.getCurvature().updateFunctionWithPoint(point, this);

			}
		}
	}

	public void updateFunctionsAndBox(InkInterface stroke) {
		// this.stroke=stroke;
		PointData point = stroke.getPoint(0);
		box = new Rectangle2D.Double(point.getPointLocation().getX(), point
				.getPointLocation().getY(), 0, 0);

		ArrayList points = stroke.getPoints();
		PointData prev, cur, next;
		FeatureFunction tempx;
		for (int i = 0; i < points.size() - SystemSettings.STROKE_CONSTANT_NEIGHBOURS; i++) {
			this.stroke = stroke.createSubInkObject(0, i);
			cur = point = (PointData) points.get(i);
			box.add(point.getPointLocation());
			if (this.stroke.getPoints().size() > (SystemSettings.STROKE_CONSTANT_NEIGHBOURS*2)) {
		
				prev = (PointData) points.get(i - SystemSettings.STROKE_CONSTANT_NEIGHBOURS);
				next = (PointData) points.get(i + SystemSettings.STROKE_CONSTANT_NEIGHBOURS);
				((distanceCalculationFeature) distance.getFunc()).setNoPointInStroke(i);
				distance.updateFunctionWithPoint(prev, cur, next, i);

				for (int j = 0; j < functions.size(); j++) {
					tempx = functions.get(j);
					if (tempx.getName().startsWith( SystemSettings.CURVATURENAME)){
					if ((points.size() > (SystemSettings.STROKE_CONSTANT_CURVATURE_NEIGHBOURS*2))&&
							i<(points.size() - SystemSettings.STROKE_CONSTANT_CURVATURE_NEIGHBOURS)
							){
						prev = (PointData) points.get(i - SystemSettings.STROKE_CONSTANT_CURVATURE_NEIGHBOURS);
						next = (PointData) points.get(i + SystemSettings.STROKE_CONSTANT_CURVATURE_NEIGHBOURS);
						}
		
							}
					else {
						prev = (PointData) points.get(i - SystemSettings.STROKE_CONSTANT_NEIGHBOURS);
						next = (PointData) points.get(i + SystemSettings.STROKE_CONSTANT_NEIGHBOURS);
	
					}
					
					
					tempx.updateFunctionWithPoint(prev, cur, next, i);
					
					
				}
			}

		}

		this.stroke = stroke;
	}

	public void updateBatchFunctions() {
		// updateCurvature3function();

		calculateCornerFunction();
		// FeatureFunction tempx

		// logger.info("compute the update batch funciton implement it. (
		// stroke statistical data 226 ) ");
	}

	private void calculateCornerFunction() {

		// create a feature function
		/*
		 * // create a corner feature
		 * 
		 * CornerDetectionFeature feat=new CornerDetectionFeature();
		 * feat.setNeededData(functions.get(deltaCurvature));
		 * feat.setName(cornerFunctionName); feat.thestroke=stroke; ArrayList<Point2D>
		 * cornerData = feat.computeFeature();
		 * 
		 * FeatureFunction func=new FeatureFunction();
		 * func.setFunctionType(FeatureFunction.BATCH_TYPE);
		 * func.setName(feat.getName())
		 * ; func.setFunc(feat);
		 * 
		 * func.setAbsaverage(feat.AbsAverage);
		 * func.setData(cornerData);
		 * func.setPointIndex(functions.get(deltaCurvature).getPointIndex());
		 * functions.add(func);
		 * 
		 */

	}

	public void updateBoundingBox(PointData point, InkInterface stroke) {
		// if this is the first point then set it with the rectangle both
		// top left and right low corners
		if (stroke.getPoints().size() == 1) {
			// Point p=new
			// Point((int)point.getPointLocation().getX(),(int)point.getPointLocation().getY())
			// ;

			Rectangle2D r = new Rectangle2D.Double(point.getPointLocation()
					.getX(), point.getPointLocation().getY(), 0, 0);
			// r.add();
			// if (this.box!=null)
			// box=r;
			// else
			box = r;

			// logger.info("first 00000000000000000 box x "+box.getX()+"
			// y "+box.getY());
		}
		// else this is just a new point i have to check
		else if (stroke.getPoints().size() > 1) {

			// if out
			// add this point to the rectangle to create a larger one
			box.add(point.getPointLocation());

		}

		// logger.info("adding the point "+point);
		// logger.info("box x "+box.x+" y "+box.y);
	}

	/**
	 * @return the box
	 * @uml.property name="box"
	 */
	public Rectangle2D getBox() {
		return box;
	}

	/**
	 * @param box
	 *            the box to set
	 * @uml.property name="box"
	 */
	public void setBox(Rectangle2D box) {
		this.box = box;
	}

	public StrokeStatisticalData(InkInterface ink) {
		super();
		this.stroke = ink;

	}

	public StrokeStatisticalData() {
		super();
	}

	// /**
	// * @return the curvaturalPoints
	// * @uml.property name="curvaturalPoints"
	// */
	// public ArrayList getDominatePoints() {
	// return controlPoints;
	// }

	/**
	 * @param curvaturalPoints
	 *            the curvaturalPoints to set
	 * @uml.property name="curvaturalPoints"
	 */
	// public void setDominatePoints(ArrayList curvaturalPoints) {
	// controlPoints = curvaturalPoints;
	// }

	public FeatureFunction getDistance() {
		return distance;
	}

	public void setDistance(FeatureFunction distance) {
		this.distance = distance;
	}

	class speedCalcuationFeature extends StrokeFeatures implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		int neighbours = SystemSettings.STROKE_CONSTANT_NEIGHBOURS;

		@Override
		public Point2D f(PointData p) {
			return null;
		}

		@Override
		public Point2D f() {
			double TotalDistance = distance.getSumUpNow();
			double speed = 0, dist = 0;
			if (neighbours == 1) {

			//	dist = ComputationsGeometry.computeLength(current, next);
				speed = ComputationsGeometry.computeVelocity(current, next);
			}
			if (neighbours >= 2) {
			//	dist = ComputationsGeometry.computeLength(prev, next);
				speed = ComputationsGeometry.computeVelocity(prev, next);
			}
			Point2D result = new Point2D.Double();
			result.setLocation( TotalDistance, speed);
			return result;
		}

		// @Override
		// public int getFunctionThersholdType() {
		// 
		// return 0;
		// }
		// @Override
		// public void setNeededData(Object data) {
		// 
		//			
		// // there is no needed data at this point
		//			
		// }

		@Override
		public boolean isInFunction(double y, double Threshold) {
			if (y < Threshold)
				return true;
			else
				return false;
		}

		@Override
		public int getThresholdType() {

			return THERSHOLD_AVERAGE;
		}

	}

	class timeDiffCalcuationFeature extends StrokeFeatures implements
			Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		int neighbours = SystemSettings.STROKE_CONSTANT_NEIGHBOURS;

		@Override
		public Point2D f(PointData p) {
			return null;
		}

		@Override
		public Point2D f() {
			double TotalDistance = distance.getSumUpNow();
			double timeDiff = 0, dist = 0;
			if (neighbours == 1) {

				//dist = ComputationsGeometry.computeLength(current, next);
				timeDiff = ComputationsGeometry.computeDeltaT(current, next);
			}
			if (neighbours >= 2) {
				//dist = ComputationsGeometry.computeLength(prev, next);
				timeDiff = ComputationsGeometry.computeDeltaT(prev, next);
			}
			
			
			Point2D result = new Point2D.Double();
			result.setLocation(TotalDistance, timeDiff);
			return result;
		}

		// @Override
		public void setNeededData(Object data) {

			// there is no needed data at this point

		}

		// @Override
		// public int getFunctionThersholdType() {
		// 
		// return 0;
		// }

		@Override
		public boolean isInFunction(double y, double Threshold) {
			if (y > Threshold)
				return true;
			else
				return false;
		}

		@Override
		public int getThresholdType() {

			return THERSHOLD_AVERAGE;
		}

	}

	class directionCalculateionFeature extends StrokeFeatures implements
			Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = -813239045951000051L;

		// Direction
		int neighbours = SystemSettings.STROKE_CONSTANT_NEIGHBOURS;

		@Override
		public Point2D f(PointData p) {
			return null;
		}

		@Override
		public Point2D f() {
			double TotalDistance = distance.getSumUpNow();
			double direc = 0, dist = 0;
			if (neighbours == 1) {

				//dist = ComputationsGeometry.computeLength(current, next);
//				direc = ComputationsGeometry.computeDirection(prev, current,
//						next);
				direc = ComputationsGeometry.computeDirectionXaxis(prev, current,
						next);
				current.setDirection(direc);
			}
			if (neighbours >= 2) {
				
				// i will change this to direction of the curve vs. 
				
				
			///	dist = ComputationsGeometry.computeLength(prev, next);
//				direc = ComputationsGeometry.computeDirection(prev, current,
//						next);
				
				direc = ComputationsGeometry.computeDirectionXaxis(prev, current,
						next);
				current.setDirection(direc);
				
			}
			// if (neighbours > 2) {
			// // do something else
			// }
			Point2D result = new Point2D.Double();
			result.setLocation(TotalDistance, direc);
			return result;
		}

		// @Override
		// public int getFunctionThersholdType() {
		// 
		// return 0;
		// }

		@Override
		public boolean isInFunction(double y, double Threshold) {
			if (Math.abs(y) > Threshold)
				return true;
			else
				return false;
		}

		@Override
		public int getThresholdType() {

			return THERSHOLD_AVERAGE;
		}

	}
	class slopeDirectionCalculateionFeature extends StrokeFeatures implements
	Serializable {
/**
 * 
 */
private static final long serialVersionUID = -813239045951000051L;

// Direction
int neighbours = SystemSettings.STROKE_CONSTANT_NEIGHBOURS;

@Override
public Point2D f(PointData p) {
	return null;
}

@Override
public Point2D f() {
	double TotalDistance = distance.getSumUpNow();
	double direc = 0, dist = 0;
	if (neighbours == 1) {

		//dist = ComputationsGeometry.computeLength(current, next);
//		direc = ComputationsGeometry.computeDirection(prev, current,
//				next);
		direc = ComputationsGeometry.computeDeltaYoverX(prev, current,
				next);
		current.setDirection(direc);
	}
	if (neighbours >= 2) {
		
		// i will change this to direction of the curve vs. 
		
		
	///	dist = ComputationsGeometry.computeLength(prev, next);
//		direc = ComputationsGeometry.computeDirection(prev, current,
//				next);
		
		direc = ComputationsGeometry.computeDeltaYoverX(prev, current,
				next);
		current.setDirection(direc);
		
	}
	// if (neighbours > 2) {
	// // do something else
	// }
	Point2D result = new Point2D.Double();
	result.setLocation(TotalDistance, direc);
	return result;
}

// @Override
// public int getFunctionThersholdType() {
// 
// return 0;
// }

@Override
public boolean isInFunction(double y, double Threshold) {
	if (Math.abs(y) > Threshold)
		return true;
	else
		return false;
}

@Override
public int getThresholdType() {

	return THERSHOLD_AVERAGE;
}

}
	class CurvatursDirectionCalculateionFeature extends StrokeFeatures implements
	
	Serializable {
/**
		 * 
		 */
		private static final long serialVersionUID = -1252506366439192641L;
/**
 * 
 */


// Direction
//int neighbours = SystemSettings.STROKE_CONSTANT_NEIGHBOURS;

@Override
public Point2D f(PointData p) {
	return null;
}

@Override
public Point2D f() {
	double TotalDistance = distance.getSumUpNow();
	double direc = 0, dist = 0,curv=0;
//	if (neighbours == 1) {
//
//		//dist = ComputationsGeometry.computeLength(current, next);
//		direc = ComputationsGeometry.computeDirection(prev, current,
//				next);
//	}
//	if (neighbours >= 2) {
//	
	if (stroke.getPointsCount()>(SystemSettings.STROKE_CONSTANT_NEIGHBOURS*2)+2){
	///	dist = ComputationsGeometry.computeLength(prev, next);
		//direc = ComputationsGeometry.computeDirection(prev, current,next);
//	logger.info("  current =  "+  current.fullString());
//	logger.info("  prev =  "+prev.fullString());
		direc=current.getDirection()-prev.getDirection();
		double deltaS=current.getCompulativeLength()-prev.getCompulativeLength();
		
		if (deltaS!=0)
		{
		curv= direc/deltaS;
	//	logger.info("-----------------------  deltaS  =  "+deltaS+"   dir "+direc+"                  the curvatures is                   "+curv);
		}
		else 
			curv=direc/SystemSettings.ZERO;
	}
//	}
	
	Point2D result = new Point2D.Double();
	result.setLocation(TotalDistance, curv);
	return result;
}

// @Override
// public int getFunctionThersholdType() {
// 
// return 0;
// }

@Override
public boolean isInFunction(double y, double Threshold) {
	if (Math.abs(y) > Math.abs(Threshold))
		return true;
	else
		return false;
}

@Override
public int getThresholdType() {

	return THERSHOLD_ABS_AVERAGE;
}

}


	class curvatureCalculateionFeature extends StrokeFeatures implements
			Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = -5681710442422250863L;

		// Direction
		int neighbours = SystemSettings.STROKE_CONSTANT_NEIGHBOURS;

		// Stroke thestroke = null;

		@Override
		public Point2D f(PointData p) {
			return null;
		}

		@Override
		public Point2D f() {
			// thestroke = stroke;
			double TotalDistance = distance.getSumUpNow();
			double curv = 0, dist = 0;
			if (neighbours == 1) {

				dist = ComputationsGeometry.computeLength(current, next);
				curv = ComputationsGeometry.computeCurvature(prev, current,
						next);
			}
			if (neighbours == 2) {

				dist = ComputationsGeometry.computeLength(prev, next);
				curv = ComputationsGeometry.computeCurvature(prev, current,
						next);
			}
			if (neighbours > 2) {
				if (stroke != null) {
					// get the count of points in the storke

					if (((neighbours * 2) + 2) < stroke.getPoints().size()) {

						// now i need to get pi which is count-neighbours-1.
//						int pi_1_index = stroke.getPoints().size() - neighbours- 2;
//						int pi_index = stroke.getPoints().size() - neighbours- 1;
//						int pi1_index = stroke.getPoints().size() - neighbours;
//						ArrayList points = stroke.getPoints();
//						
//					logger.info("  the curvature index "+ pi_index +"is from "+ (pi_1_index-neighbours)+"  to  "+(pi1_index+neighbours));
//						curv = ComputationsGeometry
//								.computeCurvatureAlgFD((PointData) points.get(pi_1_index), (PointData) points.get(pi1_index), (PointData) points.get(pi_1_index - neighbours),
//										(PointData) points.get(pi_index+neighbours));  // changed with debug to correct calcuation 
//						
						curv = ComputationsGeometry.computeCurvatureAlgFD(current,current, prev,
								next);  // changed with debug to correct calcuation 
						
						// and the distance between pi and pi-1
						dist = current.getCompulativeLength();

					} else {
						dist = 0.0;
						curv = 0.0; //was nana
					}

				}
				//				

				// do something else
			}
			Point2D result = new Point2D.Double();
			result.setLocation(dist, curv);
			return result;
		}

		// @Override
		public void setNeededData(Object data) {

			// there is no needed data at this point

		}

		// @Override
		// public int getFunctionThersholdType() {
		// 
		// return 0;
		// }

		@Override
		public boolean isInFunction(double y, double Threshold) {
			if (Math.abs(y) > Math.abs(Threshold))
				return true;
			else
				return false;
		}

		@Override
		public int getThresholdType() {

			return THERSHOLD_ABS_AVERAGE;
		}

	}

//	class CornerDetectionFeature extends StrokeFeatures implements Serializable {
//		/**
//		 * 
//		 */
//		private static final long serialVersionUID = -5681710442422567863L;
//
//		double AbsAverage = 0;
//		// Direction
//		int neighbours = SystemSettings.STROKE_CONSTANT_NEIGHBOURS;
//		FeatureFunction curvature;
//		InkInterface thestroke = null;
//		private double t1 = 15.0;
//		private double t2 = 30.0;
//
//		private double triangle = 1;
//
//		// curvatureCalculateionFeature Feature;
//		@Override
//		public Point2D f(PointData p) {
//			return null;
//		}
//
//		private void computeParameters() {
//			triangle = Math.atan(1.0 / (neighbours - 1.0));
//		}
//
//		public ArrayList<Point2D> computeFeature() {
//			  
//			computeParameters();
//			ArrayList<Point2D> temp = new ArrayList<Point2D>();
//			ArrayList<PointData> curvatdata = curvature.getData();
//			// for all point in the curvature features
//			PointData point;
//			double delta;
//			for (int i = 0; i < curvatdata.size() - neighbours; i++) {
//				point = new PointData();
//				point.x = curvatdata.get(i).getX();
//				delta = 0.0;
//				int s = 1, s1 = 1;
//
//				// s >1 and less than t
//				// t is max s such that it is between -d and +d
//				double templ = 0;
//				int index;
//				for (int j = 0; j < neighbours; j++) {
//					index = i - s;
//
//					if (index > 0 && index < curvatdata.size()) {
//						s++;
//						if (curvatdata.get(index) != null) {
//							if (Math.abs(curvatdata.get(index).getY()) > triangle) {
//								// logger.info("change the t1 ");
//								t1 = s;
//							}
//						}
//					}
//					// s=1;
//					index = i + s1 + neighbours;
//					if (index > 0 && index < curvatdata.size()) {
//						s1++;
//						if (curvatdata.get(index) != null) {
//							if (Math.abs(curvatdata.get(index).getY()) > triangle) {
//								t2 = s1;
//							}
//						}
//					}
//
//					delta += curvatdata.get(i + j).getY();
//
//				}
//				double k = Math.log(t1) * Math.log(t2) * delta;
//				// logger.info("t1 "+t1+ " t2 "+ t2+" trinangle =
//				// "+triangle);
//				// logger.info(" the function is now at x = "+ point.x +"
//				// and y = "+k+ " a nd the delta was=
//				// "+curvatdata.get(i).getY());
//				point.y = k;
//				if (!Double.isNaN(k) || !(Double.isInfinite(k))) {
//
//					AbsAverage += k;
//				}
//				temp.add(point);
//
//			}
//
//			AbsAverage /= (double) temp.size();
//
//			// thestroke = stroke;
//			// double TotalDistance = distance.getSumUpNow();
//			// double curv = 0, dist = 0;
//
//			// ArrayList<Point2D> delta = curvature.getData();
//
//			// Point2D result = new Point2D.Double();
//			// result.setLocation(dist + TotalDistance, curv);
//			return temp;
//		}
//
//		@Override
//		public Point2D f() {
//
//			return null;
//		}
//
//		// @Override
//		public void setNeededData(Object data) {
//			if (data instanceof FeatureFunction) {
//				curvature = (FeatureFunction) data;
//
//			}
//		}
//
//		public void setStroke(Stroke stroke) {
//			this.thestroke = stroke;
//		}
//
//		// @Override
//		// public int getFunctionThersholdType() {
//		// 
//		// return 0;
//		// }
//
//		@Override
//		public boolean isInFunction(double y, double Threshold) {
//			if (Math.abs(y) > Threshold)
//				return true;
//			else
//				return false;
//		}
//
//		@Override
//		public int getThresholdType() {
//
//			return THERSHOLD_CUSTOM;
//		}
//
//	}

	class changeInRotation extends StrokeFeatures implements Serializable {

		// Direction
		int neighbours = SystemSettings.STROKE_CONSTANT_NEIGHBOURS;

		@Override
		public Point2D f(PointData p) {
			  
			return null;
		}

		@Override
		public Point2D f() {
			// thestroke = stroke;
			double TotalDistance = distance.getSumUpNow();
			double curv = 0, dist = 0;
			if (neighbours == 1) {

				dist = ComputationsGeometry.computeLength(current, next);
				curv = ComputationsGeometry.computeChangeRotation(prev, next,
						current);
			}

			if (neighbours >= 2) {

//				if (stroke != null) {
//					// get the count of points in the storke
//
//			if (((neighbours * 2) + 2) < stroke.getPoints().size()) {
//
//						PointData pi, pb, pf;
//						pi = stroke.getPoint(stroke.getPoints().size()
//								- neighbours - 1);
//						pf = stroke.getPoint(stroke.getPoints().size() - 1);
//						pb = stroke.getPoint(stroke.getPoints().size()
//								- (neighbours * 2) - 1);
//
//						curv = ComputationsGeometry.computeChangeRotation(pb,
//								pi, pf);
				curv=ComputationsGeometry.computeChangeRotation(prev,current, next);
//						// and the distance between pi and pi-1
						//dist = ComputationsGeometry.computeLength(pi, pf);
//
//					} else {
//						dist = 0.0;
//						curv = Double.NaN;
//					}
//
				//}

			}

			Point2D result = new Point2D.Double();
			result.setLocation(TotalDistance, curv);
			return result;
		}

		@Override
		public boolean isInFunction(double y, double Threshold) {
			if (Math.abs(y) > Math.abs(Threshold))
				return true;
			else
				return false;
		}

		@Override
		public int getThresholdType() {

			return THERSHOLD_ABS_AVERAGE;
		}
		// StrokeLib.computeChangeRotation(p1, p2,p3);

	}

	/**
	 * @author maha
	 * 
	 */

	class anotherCurvatureCalculateionFeature extends StrokeFeatures implements
			Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		// Direction
		int neighbours = SystemSettings.STROKE_CONSTANT_NEIGHBOURS;

		Stroke thestroke = null;

		@Override
		public Point2D f(PointData p) {
			return null;
		}

		@Override
		public Point2D f() {
			// thestroke = stroke;
			double TotalDistance = distance.getSumUpNow();
			double curv = 0, dist = 0;
			if (neighbours == 1) {

				dist = ComputationsGeometry.computeLength(current, next);
				curv = ComputationsGeometry.computeCurvatureHKAlg(prev, next,
						current);
			}

			if (neighbours >= 2) {

//				if (stroke != null) {
//					// get the count of points in the storke
//
//					if (((neighbours * 2) + 2) < stroke.getPoints().size()) {
//
//						PointData pi, pb, pf;
//						pi = stroke.getPoint(stroke.getPoints().size()
//								- neighbours - 1);
//						pf = stroke.getPoint(stroke.getPoints().size() - 1);
//						pb = stroke.getPoint(stroke.getPoints().size()- (neighbours * 2) - 1);

						curv = ComputationsGeometry.computeCurvatureHKAlg(prev,
								current, next);
						// and the distance between pi and pi-1
						dist =current.getCompulativeLength();

//					} else {
//						dist = 0.0;
//						curv = Double.NaN;
//					}
//
				//}
			}
			Point2D result = new Point2D.Double();
			result.setLocation(dist, curv);
			return result;
		}

		public void setStroke(Stroke stroke) {
			this.thestroke = stroke;
		}

		@Override
		public boolean isInFunction(double y, double Threshold) {
			if (Math.abs(y) > Threshold)
				return true;
			else
				return false;
		}

		@Override
		public int getThresholdType() {

			return THERSHOLD_ABS_AVERAGE;
		}

	}

	/**
	 * @author maha feature to calcuate length of stroke. 
	 */
	class distanceCalculationFeature extends StrokeFeatures implements
			Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		double TotalDistance;
		// Direction
		int neighbours = SystemSettings.STROKE_CONSTANT_NEIGHBOURS;

		int noPointInStroke = 0;

		@Override
		public Point2D f(PointData p) {
			return null;
		}

		@Override
		public Point2D f() {
			//TotalDistance = distance.getSumUpNow();
			// noPointInStroke = stroke.getPoints().size();

			// logger.info(" total distance = "+TotalDistance +" (" +
			// this.getClass().getSimpleName()
			// + " "
			// + (new Throwable()).getStackTrace()[0].getLineNumber()
			// + " ) ");
			double direc = 0, dist = 0;
			if (neighbours == 1) {

				dist = ComputationsGeometry.computeLength(prev, current);
				// TotalDistance+=dist;
				// current.setCompulativeLength(TotalDistance);
				next.setCompulativeLength(prev.getCompulativeLength()+dist);

			}
			if (neighbours >= 2) {

				
				dist = ComputationsGeometry.computeLength(prev, current);
				//logger.info("************************************************************"+dist+"    sum up now is "+distance.getSumUpNow());
				//logger.info("*************   "+prev.fullString());
             				
			//	if (stroke.getPointsCount()<neighbours)
		//		prev.setCompulativeLength(distance.getSumUpNow());
				current.setCompulativeLength(dist+prev.getCompulativeLength());
				
				
				//logger.info(" **************  "+current.fullString());
				// TotalDistance+=dist;
				// next.setCompulativeLength(TotalDistance);

			}
			// if (neighbours > 2) {
			//
			// dist = ComputationsGeometry.computeLength(prev, next);
			// TotalDistance+=dist;
			// }
			// logger.info(" dist = "+dist+" (" +
			// this.getClass().getSimpleName()
			// + " "
			// + (new Throwable()).getStackTrace()[0].getLineNumber()
			// + " ) ");
			Point2D result = new Point2D.Double();

			result.setLocation(noPointInStroke, dist);
			return result;
		}

		public void setNoPointInStroke(int noPointInStroke) {
			this.noPointInStroke = noPointInStroke;
		}

		@Override
		public boolean isInFunction(double y, double Threshold) {
			return false;
		}

		@Override
		public int getThresholdType() {

			return THERSHOLD_NO;
		}

	}

	public double getArea() {
		return Area;
	}

	public void setArea(double area) {
		Area = area;
	}

	public void clear() {

	}

	public void generateAllDominatePoints() {
		// logger.info("i am here in dominate4");
		//  logger.trace("--------------calculating  critical point-----------------");
		ArrayList indeces = null;
		ArrayList ListFinal = null;

		FeatureFunction temp;

		for (int i = 0; i < functions.size(); i++) {
			temp = functions.get(i);

			// calcuate teh local points of the fucntions.
			indeces = getIndeces(temp);
			// logger.info(" line 911 statisicsofstorke i am computing
			// "+temp.getName()+" and it "+indeces);
			if (indeces != null) {
				// indeces
				ListFinal = ArrayLibrary.MergeSortedListNonRepeating(ListFinal,
						indeces);
			}

		}

		if (ListFinal != null) {
			controlPoints = new ArrayList<DominatePointStructure>();
			// DominatePoints = new ArrayList<PointData>();
			// now i have all the indeces that will be in the domiante points.
			for (int i = 0; i < ListFinal.size(); i++) {
				// add the point with index in list final
				//
				// logger.info((Integer) ListFinal.get(i));
				// logger.info("this stroke point count
				// "+this.stroke.getPointsCount());
				if ((Integer) ListFinal.get(i) < this.stroke.getPoints().size()) {
					DominatePointStructure dominatepoint = new DominatePointStructure();
					dominatepoint.setPoint((PointData) this.stroke.getPoints()
							.get((Integer) ListFinal.get(i)));
					dominatepoint.setIndexInInk((Integer) ListFinal.get(i));
					dominatepoint.addFunction(1);
					controlPoints.add(dominatepoint);
					// DominatePoints.add((PointData)
					// this.stroke.getPoints().get(
					// (Integer) ListFinal.get(i)));
				} else {
					// remove hhisindex
					ListFinal.remove(i);
				}
			}
			// DominatePointsIndeces=ListFinal;
		}

		// add them to dmoninate poins.

		// ///////////////////now checck curvature
		//			
		// func=data.getCurvature();
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
		// data.setDominatePoints(tempD);
		//			
		// logger.info("number of dominat points is "+tempD.size());
	}

	public void setThresholds() {
		
//		switch (temp.getFunc().getThresholdType()) {
//		case StrokeFeatures.THERSHOLD_ABS_AVERAGE:
//			temp.setDataThreshold(temp.getAbsaverage());
//			break;
//		case StrokeFeatures.THERSHOLD_CUSTOM:
//			if (temp.getName().equals(cornerFunctionName)) {
//				temp.setDataThreshold(1.0);// get to be computed );
//			}
//			break;
//		default:
//			temp.setDataThreshold(temp.getAverage());
//			break;
//		}
//		
		
		for (int i = 0; i < functions.size(); i++) {
			FeatureFunction temp = functions.get(i);
			switch (temp.getFunc().getThresholdType()) {
			case StrokeFeatures.THERSHOLD_ABS_AVERAGE:
				temp.setDataThreshold(temp.getAbsaverage());
				break;
			case StrokeFeatures.THERSHOLD_CUSTOM:
				if (temp.getName().equals(cornerFunctionName)) {
					temp.setDataThreshold(1.0);// get to be computed );
				}
				break;
			default:
				temp.setDataThreshold(temp.getAverage());
				break;
			}

		}
	}

	public ArrayList<Integer> getIndeces(FeatureFunction temp) {
	          this.setThresholds();
		// logger.info(temp.getData());
		// calcuate teh local points of the fucntions.
		ArrayList<Integer> returnArrayList = temp.calcuateLocalPoints();
		return returnArrayList;
	}

	public static StrokeStatisticalData BuildStorkeData(InkInterface stroke) {
		StrokeStatisticalData temp = new StrokeStatisticalData();
		temp.initAll();
		// temp.stroke = stroke;

		Rectangle2D r = new Rectangle2D.Double(stroke.getPoint(0)
				.getPointLocation().getX(), stroke.getPoint(0)
				.getPointLocation().getY(), 0, 0);

		// r.add(stroke.getPoint(0).getPointLocation());
		ArrayList points = stroke.getPoints();
		PointData point, prev, cur, next;
		FeatureFunction tempx;
		temp.stroke = stroke;
		temp.updateFunctionsAndBox(stroke);
		temp.updateBatchFunctions();

		// logger.info(" points.size() "+points.size());
		// for (int i = 1; i < points.size() - 1; i++) {
		// cur = point = (PointData) points.get(i);
		//
		// prev = (PointData) points.get(i - 1);
		//
		// r.add(point.getPointLocation());
		// if (points.size() > 3) {
		// next = (PointData) points.get(i + 1);
		// // now compute funcitons
		// temp.stroke=stroke.createSubInkObject(0, i);
		// ((distanceCalculationFeature)temp.distance.getFunc()).setNoPointInStroke(i);
		// temp.distance.updateFunctionWithPoint(prev, cur, next, i);
		//
		// for (int j = 0; j < temp.functions.size(); j++) {
		// tempx = temp.functions.get(j);
		//
		// // tempx.updateFunctionWithPoint(prev,cur,next,i);
		//
		// tempx.updateFunctionWithPoint(prev, cur, next, i);
		// // data.getCurvature().updateFunctionWithPoint(point, this);
		//
		// }
		// }
		//
		// }

		// temp.setBox(r);
		return temp;
	}

	public void paint(Graphics g) {
		// draw dominate points.
		drawDominateFunctionsPoints((Graphics2D) g);
	}

	public void drawDominateFunctionsPoints(Graphics2D g) {
		

		if (SystemSettings.STROKE_DOMINATE_COLORING == 4) {
			
			g.setColor(Color.RED);

			if (controlPoints != null) {
				// now draw the critical points
				for (int i = 0; i < controlPoints.size(); i++) {
					PointData P = (controlPoints.get(i).getPoint());
					// the point currently represent the index of point in
					// stroke
					g.drawRect((int) P.getX(), (int) P.getY(), 5, 5);
					g.fillRect((int) P.getX(), (int) P.getY(), 5, 5);
				}// /for
			}// if of cirtical points

			// draw each funciton with color
			// firstly loop on functions
			PointData P;
			int k=functions.size()+1;
			
			for (int i =0 ; i < functions.size(); i++,k--) {
				
				// now i have the funcitns
				ArrayList<Integer> indeces = getIndeces(functions.get(i));
				String functionName;
				functionName=functions.get(i).getName();//.substring(0, 10);
				if (i==0){
					g.setColor(Color.cyan);
	                 
	                  g.drawString(functionName, (int) (i+10), (int) (i*10+10));
					if (indeces != null) {
						for (int j = 0; j < indeces.size(); j++) {
							P = this.stroke.getPoint(indeces.get(j));
							g.drawRect((int) P.getX(), (int) P.getY(), 2*k, 2*k);
							g.fillRect((int) P.getX(), (int) P.getY(), 2*k, 2*k);
						}
							
				}
				}
				else if (i==1){
					g.setColor(Color.GRAY);
	               
	                  g.drawString(functionName, (int) (i+10), (int) (i*10+10));
					if (indeces != null) {
						for (int j = 0; j < indeces.size(); j++) {
							P = this.stroke.getPoint(indeces.get(j));
							g.drawOval((int) P.getX(), (int) P.getY(), 2*k, 2*k);
							g.fillOval((int) P.getX(), (int) P.getY(), 2*k, 2*k);
						}
				}
				}
				else if (i==2){
					g.setColor(Color.yellow);
		               
	                  g.drawString(functionName, (int) (i+10), (int) (i*10+10));
					if (indeces != null) {
						for (int j = 0; j < indeces.size(); j++) {
							P = this.stroke.getPoint(indeces.get(j));
							g.drawRect((int) P.getX(), (int) P.getY(), 2*k, 2*k);
							g.fillRect((int) P.getX(), (int) P.getY(), 2*k, 2*k);
						}
				}
				}
				else if (i==3){
					
					g.setColor(Color.orange);
					Rectangle2D c1=new Rectangle2D.Double();
					Rectangle2D c2=new Rectangle2D.Double();
					Rectangle2D  cross=new Rectangle2D.Double();
				        // Creates the first leaf by filling the intersection of two Area objects created from an ellipse.
				     
				        
				        
	                  g.drawString(functionName, (int) (i+10), (int) (i*10+10));
					if (indeces != null) {
						for (int j = 0; j < indeces.size(); j++) {
							P = this.stroke.getPoint(indeces.get(j));
							 
							
//							    c1.setFrame(P.getX(), P.getY()+2, 4, 15);//|
//							    c2.setFrame(P.getX()-2, P.getY(), 15, 4);//--
//							    cross.setFrame(P.getX(), P.getY(), 2, 2);
//							  c1.union(c1,c2, cross);
//							g.draw( cross);
							g.drawOval((int) P.getX(), (int) P.getY(), 2*k, 2*k);
							g.fillOval((int) P.getX(), (int) P.getY(), 2*k, 2*k);
//							g.drawRect((int) P.getX(), (int) P.getY(), 2*k, 2*k);
//							g.fillRect((int) P.getX(), (int) P.getY(), 2*k, 2*k);
						}
				}
				
				
					}
				
else if (i==4){
					
					g.setColor(Color.BLACK);
					Rectangle2D c1=new Rectangle2D.Double();
					Ellipse2D c2=new Ellipse2D.Double();
					Area cross=new Area();
				        // Creates the first leaf by filling the intersection of two Area objects created from an ellipse.
				     
				        
				        
	                  g.drawString(functionName, (int) (i+10), (int) (i*10+10));
					if (indeces != null) {
						for (int j = 0; j < indeces.size(); j++) {
							P = this.stroke.getPoint(indeces.get(j));
							 
							g.drawOval((int) P.getX(), (int) P.getY(), 2*k, 2*k);
							g.fillOval((int) P.getX(), (int) P.getY(), 2*k, 2*k);			
//							    c1.setRect(P.getX(), P.getY()+2, 5, 2);// /
//							    c2.setFrame(P.getX(), P.getY(), 3, 2);
//							     //(P.getX()-2, P.getY(), 2, 2);//--
//							 c2.intersects(c1);
//							g.fill( c2);
//							g.drawRect((int) P.getX(), (int) P.getY(), 2*k, 2*k);
//							g.fillRect((int) P.getX(), (int) P.getY(), 2*k, 2*k);
						}
				}
				
				
					}
					// logger.info(" i am computing
					// "+functions.get(i).getName()+" and itc color is
					// "+g.getColor().toString());
				
				
			}
			
		}
				
		if (SystemSettings.STROKE_DOMINATE_COLORING == 1) {
			g.setColor(Color.BLACK);

			if (controlPoints != null) {
				// now draw the critical points
				for (int i = 0; i < controlPoints.size(); i++) {
					PointData P = (controlPoints.get(i).getPoint());
					// the point currently represent the index of point in
					// stroke
					g.drawOval((int) P.getX(), (int) P.getY(), 4, 4);
					g.fillOval((int) P.getX(), (int) P.getY(), 4, 4);
				}// /for
			}// if of cirtical points
		} else if (SystemSettings.STROKE_DOMINATE_COLORING == 2) {

			// draw each funciton with color
			// firstly loop on functions
			PointData P;
			for (int i = 0; i < functions.size(); i++) {
				if (functions.get(i).getName().equals(cornerFunctionName)
						|| i == deltaCurvature) {
					// logger.info(" name of function
					// "+functions.get(i).getName());
					// now i have the funcitns

					ArrayList<Integer> indeces = getIndeces(functions.get(i));

					int size = 15;
					if ((float) functions.size() > size)// to display
														// distinctive colors
					{
						size *= 2;

					}
					g.setColor(Color.getHSBColor((float) (i * 2) / size,
							(float) 0.7, (float) 0.8));
					if (indeces != null)
						for (int j = 0; j < indeces.size(); j++) {
							// logger.info("stroke.getPoint
							// "+stroke.getPoints().size());
							P = this.stroke.getPoint(indeces.get(j));
							g.drawRect((int) P.getX(), (int) P.getY(), 3, 3);
							g.fillRect((int) P.getX(), (int) P.getY(), 3, 3);
						}
				}
			}

		} // if for drawing
		else if (SystemSettings.STROKE_DOMINATE_COLORING == 3) {

			g.setColor(Color.RED);

			if (controlPoints != null) {
				// now draw the critical points
				for (int i = 0; i < controlPoints.size(); i++) {
					PointData P = (controlPoints.get(i).getPoint());
					// the point currently represent the index of point in
					// stroke
					g.drawRect((int) P.getX(), (int) P.getY(), 5, 5);
					g.fillRect((int) P.getX(), (int) P.getY(), 5, 5);
				}// /for
			}// if of cirtical points

			// draw each funciton with color
			// firstly loop on functions
			PointData P;
			int k=functions.size()+1;
			
			for (int i =0 ; i < functions.size(); i++,k--) {
				
				// now i have the funcitns
				ArrayList<Integer> indeces = getIndeces(functions.get(i));
				g.setColor(Color.getHSBColor((float) (i)
						/ (float) functions.size(), (float) 0.7, (float) 0.8));
                  String functionName=functions.get(i).getName();//.substring(0, 10);
                  g.drawString(functionName, (int) (i+10), (int) (i*10+10));
				if (indeces != null) {
					for (int j = 0; j < indeces.size(); j++) {
						P = this.stroke.getPoint(indeces.get(j));
						g.drawRect((int) P.getX(), (int) P.getY(), 2*k, 2*k);
						g.fillRect((int) P.getX(), (int) P.getY(), 2*k, 2*k);
						
					}
					// logger.info(" i am computing
					// "+functions.get(i).getName()+" and itc color is
					// "+g.getColor().toString());
				
				}
			}

		}
	}

	public void setLine(StrokeCurveSolution finalsol) {

	}

	public void setEllipse(StrokeCurveSolution finalsol) {

	}

	public void setPolyline(polygonSolution finalsol) {

	}

	public ArrayList<DominatePointStructure> getDominatePointsIndeces() {

		return controlPoints;
	}

	public ArrayList<FeatureFunction> getFunctions() {
		return functions;
	}

//	public CurveFitData Sums(){
//		if (!sumsComputed){
//			sums=new CurveFitData();
//			sums.computeInitalDat(stroke.getPoints());
//			
//		}
//		return sums;
//		
//	}
//	public double TotalRotation(){
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

	public FeatureFunction getCurvatureRotation() {
		return CurvatureRotation;
	}

	public ArrayList<DominatePointStructure> getControlPointsIndeces() {
	 // this is a control point 
		logger.warn( "  TO DO:  add a control points different than dominate point (simpler) and make sure not repeated.... ") ;
		return getDominatePointsIndeces();
	}
	}
