/**
 * 
 */
package SketchMaster.Stroke.features;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import SketchMaster.Stroke.StrokeData.InkInterface;
import SketchMaster.Stroke.StrokeData.PointData;
import SketchMaster.Stroke.StrokeData.SimpleInkObject;
import SketchMaster.Stroke.StrokeData.Stroke;
import SketchMaster.Stroke.StrokeData.StrokeStatisticalData;
import SketchMaster.classifier.Symbol.SymbolCategory;
import SketchMaster.lib.ComputationsGeometry;
import SketchMaster.system.SystemSettings;

/**
 * @author maha
 * 
 */
public class StrokeRubineFeatureSet  implements
		Serializable, InkFeatureSet {
	
	private static final Logger logger = Logger.getLogger(StrokeRubineFeatureSet.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 566823144662422035L;
         SimpleInkObject stroke;
	/**
	 * @return the stroke
	 */
	public InkInterface getStroke() {
		return stroke;
	}

	public InkInterface getInk() {
		return stroke;
	}

	transient ArrayList<StrokeStatisticalFeatures> Features = null;

	/**
	 * 
	 */
	public StrokeRubineFeatureSet() {
		super();
	}

	/**
	 * 
	 */
	public StrokeRubineFeatureSet(InkInterface ink) {
		 this.stroke=(SimpleInkObject) ink;
		//super(ink);
	}


	public void initAll() {
		Features = new ArrayList<StrokeStatisticalFeatures>();

		// init the f1
		StrokeStatisticalFeatures temp = new cosineStartingPoint();
		temp.setSubStroke(stroke);
		Features.add(temp);

		// f2
		temp = new sineStartingPoint();
		temp.setSubStroke(stroke);
		Features.add(temp);
		// f3
		temp = new lengthDiagonal();
		temp.setSubStroke(stroke);
		Features.add(temp);
		// f4
		temp = new AngleOfDiagonal();
		temp.setSubStroke(stroke);
		Features.add(temp);

		// f5
		temp = new DistanceStartEnd();
		temp.setSubStroke(stroke);
		Features.add(temp);

		// f6

		temp = new cosineEnding();
		temp.setSubStroke(stroke);
		Features.add(temp);

		// f7

		temp = new sineEndAngle();
		temp.setSubStroke(stroke);
		Features.add(temp);

		// f8

		temp = new LenghtOfSegment();
		temp.setSubStroke(stroke);
		Features.add(temp);

		// f9
		temp = new TotalChangeInRotation();
		temp.setSubStroke(stroke);
		Features.add(temp);

		// f10
		temp = new AbsoluteRotation();
		temp.setSubStroke(stroke);
		Features.add(temp);

		// f11
		temp = new SquareRotation();
		temp.setSubStroke(stroke);
		Features.add(temp);

		// f12

		temp = new MaxmiumSpeed();
		temp.setSubStroke(stroke);
		Features.add(temp);

		// f13
		temp = new TotalTime();
		temp.setSubStroke(stroke);
		Features.add(temp);
		for (int i = 0; i < Features.size(); i++) {
			Features.get(i).setName("");
		}

		// now construct the featue s

		// super.initAll();
	}

	public void computeFeatures() {
		if (Features == null) {
			initAll();
		}
		for (int i = 0; i < Features.size(); i++) {
			Features.get(i).getValue();
			// logger.info(Features.get(i).toString());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder temp = new StringBuilder("  ");
		
		for (int i = 0; i < Features.size(); i++) {
			temp.append( Features.get(i).getName());
			temp.append(" =  ");
			temp.append(Features.get(i).getValue());
			temp.append( "  ");// +"\n ";
		}

		return temp.toString();
	}

	public void computeFeatures(InkInterface ink) {
		this.stroke =   (SimpleInkObject) ink;
		for (int i = 0; i < Features.size(); i++) {
			Features.get(i).setSubStroke(ink);
			Features.get(i).getValue();
		}
	}

	/**
	 * @author maha the feature f1 : cosine of the starting point.
	 */
	class cosineStartingPoint extends StrokeStatisticalFeatures {

		@Override
		public double Value() {
			computed = true;

			ArrayList<PointData> points;
			if (this.subStroke != null) {
				points = this.subStroke.getPoints();
				if (points.size() > 3) {
					double value = ComputationsGeometry.cos(points.get(0),
							points.get(2));
					Value = value;
					ValueOk = true;
					return value;
				}

			}
			ValueOk = false;
			return this.minValue;
		}

		@Override
		public Point2D f() {

			return null;
		}

		@Override
		public int getThresholdType() {

			return 0;
		}

		@Override
		public boolean isInFunction(double y, double Threshold) {
			return false;
		}

		@Override
		public void setName(String name) {

			Name = "Cosine Starting Point";
		}

	}

	/**
	 * @author maha the feature f2 : sine of the starting point.
	 */
	class sineStartingPoint extends StrokeStatisticalFeatures {
		@Override
		public double Value() {
			computed = true;

			ArrayList<PointData> points;
			if (this.subStroke != null) {
				points = this.subStroke.getPoints();
				if (points.size() > 3) {
					double value = ComputationsGeometry.sin(points.get(0),
							points.get(2));
					Value = value;
					ValueOk = true;
					return value;
				}

			}
			ValueOk = false;
			return this.minValue;
		}

		@Override
		public Point2D f() {

			return null;
		}

		@Override
		public int getThresholdType() {

			return 0;
		}

		@Override
		public boolean isInFunction(double y, double Threshold) {
			return false;
		}

		@Override
		public void setName(String name) {

			Name = "Sine Starting point";
		}

	}

	/**
	 * @author maha the feature f3 : lenght of the diagonal of the bounding box.
	 */
	class lengthDiagonal extends StrokeStatisticalFeatures {
		@Override
		public double Value() {
			computed = true;

			ArrayList<PointData> points;
			if (this.subStroke != null) {
				points = this.subStroke.getPoints();
				Rectangle box = new Rectangle();

				for (int i = 0; i < points.size(); i++) {
					box.add(points.get(i).getPointLocation());
				}
				// after adding all points to the rectangle.
				// then compute the lenght fo max and min
				Point2D pmin, pmax;
				pmin = new Point.Double();
				pmax = new Point.Double();
				pmin.setLocation(box.getMinX(), box.getMinY());
				pmax.setLocation(box.getMaxX(), box.getMaxY());
				double value = ComputationsGeometry.computeLength(pmin, pmax);
				Value = value;
				ValueOk = true;
				return Value;

			}
			ValueOk = false;
			return this.minValue;
		}

		@Override
		public Point2D f() {

			return null;
		}

		@Override
		public int getThresholdType() {

			return 0;
		}

		@Override
		public boolean isInFunction(double y, double Threshold) {
			return false;
		}

		@Override
		public void setName(String name) {

			Name = "Lenght of diagonal";
		}
	}

	/**
	 * @author maha the feature f4 : angle of the diagonal of the boundong box
	 */
	class AngleOfDiagonal extends StrokeStatisticalFeatures {

		@Override
		public double Value() {
			computed = true;

			ArrayList<PointData> points;
			if (this.subStroke != null) {
				points = this.subStroke.getPoints();
				Rectangle box = new Rectangle();

				for (int i = 0; i < points.size(); i++) {
					box.add(points.get(i).getPointLocation());
				}
				// after adding all points to the rectangle.
				// then compute the lenght fo max and min
				Point2D pmin, pmax;
				pmin = new Point.Double();
				pmax = new Point.Double();
				pmin.setLocation(box.getMinX(), box.getMinY());
				pmax.setLocation(box.getMaxX(), box.getMaxY());
				Value = ComputationsGeometry.computeAngle(pmin, pmax);

				ValueOk = true;
				return Value;

			}
			ValueOk = false;
			return this.minValue;
		}

		@Override
		public Point2D f() {

			return null;
		}

		@Override
		public int getThresholdType() {

			return 0;
		}

		@Override
		public boolean isInFunction(double y, double Threshold) {
			return false;
		}

		@Override
		public void setName(String name) {

			Name = "Angle of Diagonal";
		}
	}

	/**
	 * @author maha the feature f5 : lenght from the start to the end.
	 */
	class DistanceStartEnd extends StrokeStatisticalFeatures {

		@Override
		public double Value() {
			computed = true;

			ArrayList<PointData> points;
			if (this.subStroke != null) {
				points = this.subStroke.getPoints();
				if (points.size() > 1) {
					Value = ComputationsGeometry.computeLength(points.get(0),
							points.get(points.size() - 1));// (points.get(0),points.get(2));
					//  logger.trace("DDDDDDDDDDDDDDDDDDDDDDDddd     "+Value);
					ValueOk = true;
					return Value;
				}

			}
			else {
				logger.error("  L(  "
						+ (new Throwable()).getStackTrace()[0].getLineNumber()
						+ ")" + " - " + " subStroke  equal num  an value is   "+this.minValue );
			}
			ValueOk = false;
			return this.minValue;
		}

		@Override
		public Point2D f() {

			return null;
		}

		@Override
		public int getThresholdType() {

			return 0;
		}

		@Override
		public boolean isInFunction(double y, double Threshold) {
			return false;
		}

		public void setSubStroke(InkInterface subStroke) {

			this.subStroke = subStroke;
		}

		@Override
		public void setName(String name) {

			Name = "Distance from start to end";
		}

	}

	/**
	 * @author maha the feature f6 : cosine of the ending angle
	 */

	class cosineEnding extends StrokeStatisticalFeatures {

		@Override
		public double Value() {
			computed = true;

			ArrayList<PointData> points;
			if (this.subStroke != null) {
				DistanceStartEnd f5 = new DistanceStartEnd();
				f5.setSubStroke(this.subStroke);
				points = this.subStroke.getPoints();
				if (points.size() > 1) {
					double f5value = f5.Value();
					if (f5value==0){
						f5value=SystemSettings.ZERO;
					}
					
					if (f5.isValueOk()) {
						Value = (points.get(points.size() - 1).getX() - points
								.get(0).getX())
								/ f5value;
						if(Double.isNaN(Value))
							Value=SystemSettings.ZERO;
						
						
						ValueOk = true;
						return Value;
					}
					// ;//(points.get(0),points.get(2));

				}

			}
			ValueOk = false;
			return this.minValue;

		}

		@Override
		public Point2D f() {

			return null;
		}

		@Override
		public int getThresholdType() {

			return 0;
		}

		@Override
		public boolean isInFunction(double y, double Threshold) {
			return false;
		}

		@Override
		public void setName(String name) {

			Name = "Cosine of end angle";
		}

	}

	/**
	 * @author maha the feature f7 : sine of the ending angle
	 * 
	 * 
	 */
	class sineEndAngle extends StrokeStatisticalFeatures {
		@Override
		public double Value() {
			computed = true;

			ArrayList<PointData> points;
			if (this.subStroke != null) {
				DistanceStartEnd f5 = new DistanceStartEnd();
				f5.setSubStroke(this.subStroke);
				points = this.subStroke.getPoints();
				if (points.size() > 1) {
					double f5value = f5.Value();
					if (f5value==0){
						f5value=SystemSettings.ZERO;
					}
					
					if (f5.isValueOk()) {
						Value = (points.get(points.size() - 1).getY() - points
								.get(0).getY())
								/ f5value;
						if(Double.isNaN(Value))
							Value=SystemSettings.ZERO;
						ValueOk = true;
						return Value;
					}
					// ;//(points.get(0),points.get(2));

				}

			}
			ValueOk = false;
			return this.minValue;

		}

		@Override
		public Point2D f() {

			return null;
		}

		@Override
		public int getThresholdType() {

			return 0;
		}

		@Override
		public boolean isInFunction(double y, double Threshold) {
			return false;
		}

		@Override
		public void setName(String name) {

			Name = "Sine of end angle";
		}
	}

	/**
	 * @author maha the feature f8 : lenght of the whole stroke // the distance
	 *         is computed using
	 */
	class LenghtOfSegment extends StrokeStatisticalFeatures {
		@Override
		public double Value() {
			computed = true;

			ArrayList<PointData> points;
			if (this.subStroke != null) {

				points = this.subStroke.getPoints();
				if (points.size() > 2) {
					double length = 0.0;
					for (int i = 0; i < points.size() - 1; i++) {
						PointData p1 = points.get(i);
						PointData p2 = points.get(i + 1);
						length += ComputationsGeometry.computeLength(p1, p2);

					}

					Value = length;
					ValueOk = true;
					return length;

					// ;//(points.get(0),points.get(2));

				}

			}
			ValueOk = false;
			return this.minValue;

		}

		@Override
		public Point2D f() {

			return null;
		}

		@Override
		public int getThresholdType() {

			return 0;
		}

		@Override
		public boolean isInFunction(double y, double Threshold) {
			return false;
		}

		@Override
		public void setName(String name) {

			Name = "Length of end angle";
		}
	}

	/**
	 * @author maha the feature f9: total change in rotation // the distance is
	 *         computed using
	 */
	class TotalChangeInRotation extends StrokeStatisticalFeatures {
		@Override
		public double Value() {
			computed = true;

			ArrayList<PointData> points;
			if (this.subStroke != null) {

				points = this.subStroke.getPoints();
				if (points.size() > 3) {
					double rotation = 0.0;
					for (int i = 0; i < points.size() - 2; i++) {
						PointData p1 = points.get(i);
						PointData p2 = points.get(i + 1);
						PointData p3 = points.get(i + 2);
						rotation += ComputationsGeometry.computeChangeRotation(
								p1, p2, p3);
						// logger.info("at the rotation "+i+" this value
						// is "+rotation);

					}

					Value = rotation;
					ValueOk = true;
					return rotation;

					// ;//(points.get(0),points.get(2));

				}

			}
			ValueOk = false;
			return this.minValue;

		}

		@Override
		public Point2D f() {

			return null;
		}

		@Override
		public int getThresholdType() {

			return 0;
		}

		@Override
		public boolean isInFunction(double y, double Threshold) {
			return false;
		}

		@Override
		public void setName(String name) {

			Name = "Total change of rotation";
		}
	}

	/**
	 * @author maha the feature f10: absolute Rotation means how much it move
	 *         around // the distance is computed using
	 */
	class AbsoluteRotation extends StrokeStatisticalFeatures {
		@Override
		public double Value() {
			computed = true;

			ArrayList<PointData> points;
			if (this.subStroke != null) {

				points = this.subStroke.getPoints();
				if (points.size() > 3) {
					double rotation = 0.0;
					for (int i = 0; i < points.size() - 2; i++) {
						PointData p1 = points.get(i);
						PointData p2 = points.get(i + 1);
						PointData p3 = points.get(i + 2);
						rotation += Math.abs(ComputationsGeometry
								.computeChangeRotation(p1, p2, p3));

					}

					Value = rotation;
					ValueOk = true;
					return rotation;

					// ;//(points.get(0),points.get(2));

				}

			}
			ValueOk = false;
			return this.minValue;

		}

		@Override
		public Point2D f() {

			return null;
		}

		@Override
		public int getThresholdType() {

			return 0;
		}

		@Override
		public boolean isInFunction(double y, double Threshold) {
			return false;
		}

		@Override
		public void setName(String name) {

			Name = "Absolute rotation ";
		}
	}

	/**
	 * @author maha the feature f11: Rotation squared means how smoth is the
	 *         curve // the distance is computed using
	 */
	class SquareRotation extends StrokeStatisticalFeatures {
		@Override
		public double Value() {
			computed = true;

			ArrayList<PointData> points;
			if (this.subStroke != null) {

				points = this.subStroke.getPoints();
				if (points.size() > 3) {
					double rotation = 0.0;
					for (int i = 0; i < points.size() - 2; i++) {
						PointData p1 = points.get(i);
						PointData p2 = points.get(i + 1);
						PointData p3 = points.get(i + 2);
						rotation += Math.pow(ComputationsGeometry
								.computeChangeRotation(p1, p2, p3), 2);

					}

					Value = rotation;
					ValueOk = true;
					return rotation;

					// ;//(points.get(0),points.get(2));

				}

			}

			ValueOk = false;
			return this.minValue;

		}

		@Override
		public Point2D f() {

			return null;
		}

		@Override
		public int getThresholdType() {

			return 0;
		}

		@Override
		public boolean isInFunction(double y, double Threshold) {
			return false;
		}

		@Override
		public void setName(String name) {

			Name = "Square of rotation ";
		}
	}

	/**
	 * @author maha the feature f12: Maximum speed reached. // the distance is
	 *         computed using
	 */
	class MaxmiumSpeed extends StrokeStatisticalFeatures {
		@Override
		public double Value() {
			computed = true;

			ArrayList<PointData> points;
			if (this.subStroke != null) {

				points = this.subStroke.getPoints();
				if (points.size() > 3) {
					double max = Double.MIN_VALUE;
					double currSpeed = 0.0;
					for (int i = 0; i < points.size() - 2; i++) {
						PointData p1 = points.get(i);
						PointData p2 = points.get(i + 1);

						currSpeed = ComputationsGeometry.computeSpeed(p1, p2);
						if (!Double.isInfinite(currSpeed)
								&& !Double.isNaN(currSpeed)) {

							// some time time is nearly the same its different
							// is 0 it preduce infite or zero we remove this
							// data from our calcuations
							if (currSpeed > max) {
								// logger.info( "not finite or nan and
								// max = "+max+" currspeed "+currSpeed);
								max = currSpeed;
							}
						}
					}

					Value = max;
					ValueOk = true;
					return max;

					// ;//(points.get(0),points.get(2));

				}

			}
			ValueOk = false;
			return this.minValue;

		}

		@Override
		public Point2D f() {

			return null;
		}

		@Override
		public int getThresholdType() {

			return 0;
		}

		@Override
		public boolean isInFunction(double y, double Threshold) {
			return false;
		}

		@Override
		public void setName(String name) {

			Name = "Maximum speed in the stroke";
		}
	}

	/**
	 * @author maha the feature f13: total time of stroke speed . // the
	 *         distance is computed using
	 */
	class TotalTime extends StrokeStatisticalFeatures {
		@Override
		public double Value() {
			computed = true;

			ArrayList<PointData> points;
			if (this.subStroke != null) {

				points = this.subStroke.getPoints();
				if (points.size() > 3) {
					double time = Double.MIN_VALUE;

					time = points.get(points.size() - 1).getTime()
							- points.get(0).getTime();

					Value = time;
					ValueOk = true;
					return time;

					// ;//(points.get(0),points.get(2));

				}

			}
			ValueOk = false;
			return this.minValue;

		}

		@Override
		public Point2D f() {

			return null;
		}

		@Override
		public int getThresholdType() {

			return 0;
		}

		@Override
		public boolean isInFunction(double y, double Threshold) {
			return false;
		}

		@Override
		public void setName(String name) {

			Name = "Total Time of Storke";
		}
	}

	public ArrayList<StrokeStatisticalFeatures> getFeatures() {
		if (Features == null) {
			initAll();
		}
		return Features;
	}

}
