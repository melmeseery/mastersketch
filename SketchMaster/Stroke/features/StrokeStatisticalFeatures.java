/**
 * 
 */
package SketchMaster.Stroke.features;
import java.awt.geom.Point2D;
import java.io.Serializable;

import SketchMaster.Stroke.StrokeData.InkInterface;
import SketchMaster.Stroke.StrokeData.PointData;
import SketchMaster.Stroke.StrokeData.Segment;

/**
 * @author maha
 * 
 */
public abstract class StrokeStatisticalFeatures extends StrokeFeatures {
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
	public String toString() {
		String string = "Function " + Name + "  and Value = " + Value
				+ " with Status " + ValueOk;

		return string;
	}

	protected InkInterface subStroke = null;

	protected boolean ValueOk = false;
	protected boolean computed = false;
	protected Object data;
	protected double Value = 0.0;

	public double getValue() {
		if (!computed) {
			computed = true;
			double returndouble = Value();
			return returndouble;
		} else {
			return Value;
		}
		// return this.MinValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see SketchMaster.Stroke.features.StrokeFeatures#f(SketchMaster.Stroke.StrokeData.PointData)
	 */
	@Override
	public Point2D f(PointData p) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see SketchMaster.Stroke.features.StrokeFeatures#f()
	 */
	// @Override
	// public Point2D f() {
	//		
	// return null;
	// }
	abstract public double Value();

	// {
	// return 0.0;
	// }
	/*
	 * (non-Javadoc)
	 * 
	 * @see SketchMaster.Stroke.features.StrokeFeatures#getThresholdType()
	 */
	// @Override
	// public int getThresholdType() {
	//		
	// return 0;
	// }
	/*
	 * (non-Javadoc)
	 * 
	 * @see SketchMaster.Stroke.features.StrokeFeatures#isInFunction(double,
	 *      double)
	 */
	// @Override
	// public boolean isInFunction(double y, double Threshold) {
	//		
	// return false;
	// }
	/*
	 * (non-Javadoc)
	 * 
	 * @see SketchMaster.Stroke.features.StrokeFeatures#setNeededData(java.lang.Object)
	 */
	// @Override
	public void setNeededData(Object data) {

	}

	public boolean isValueOk() {
		return ValueOk;
	}

	public void setSubStroke(InkInterface subStroke) {
		computed = false;
		ValueOk = false;
		this.subStroke = subStroke;
	}

}
