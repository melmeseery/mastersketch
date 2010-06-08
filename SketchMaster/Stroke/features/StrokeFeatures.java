/**
 * 
 */
package SketchMaster.Stroke.features;
import java.awt.geom.Point2D;
import java.io.Serializable;

import SketchMaster.Stroke.StrokeData.PointData;
import SketchMaster.Stroke.StrokeData.Segment;

/**
 * @author Mahi
 */
public abstract class StrokeFeatures implements Serializable {
	public static final int THERSHOLD_AVERAGE = 0;
	public static final int THERSHOLD_ABS_AVERAGE = 1;
	public static final int THERSHOLD_STATIC = 2;
	public static final int THERSHOLD_CUSTOM = 3;
	public static final int THERSHOLD_NO = -1;
	protected PointData prev;
	protected PointData next;
	protected PointData current;
	protected String Name;

	abstract public Point2D f(PointData p);

	abstract public Point2D f();

	abstract public boolean isInFunction(double y, double Threshold);

	abstract public int getThresholdType();

	protected double minValue = 0.0;

	public double MinValue() {
		return minValue;
	}

	protected double maxValue = Double.MAX_VALUE;

	public double MaxValue() {
		return maxValue;
	}

	public double ScaledFunction(double factor) {
		double returndouble = f().getY() * factor;
		return returndouble;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getName() {
		return Name;
	}

}
