package SketchMaster.lib;

import java.io.Serializable;

import JSci.maths.vectors.DoubleVector;

/**
 * this is warper for the vector for jsci library
 * 
 * @author Mahi
 * 
 */
public class Vector implements Serializable {

	DoubleVector data = null;

	/**
	 * 
	 */
	public Vector() {

		super();

	}

	// //////////////////////////////Constructors

	public Vector(double x, double y, double z) {

		data = new DoubleVector(3);
		data.setComponent(0, x);
		data.setComponent(1, y);
		data.setComponent(2, z);

	}

	public Vector(double x, double y) {

		data = new DoubleVector(2);
		data.setComponent(0, x);
		data.setComponent(1, y);

	}

	public Vector(double[] d) {
		data = new DoubleVector(d);

	}

	// /////////////////////////////////////functions

	// ////////////////////////////////setters and getters
	public double getX() {
		if (data != null)
			if (data.dimension() > 0)
				return data.getComponent(0);
		return Double.NaN;
	}

	public double getY() {
		if (data != null)
			if (data.dimension() > 1)
				return data.getComponent(1);
		return Double.NaN;
	}

	public double getZ() {
		if (data != null)
			if (data.dimension() > 2)
				return data.getComponent(2);
		return Double.NaN;
	}

	double getElement(int i) {
		if (data != null)
			if (data.dimension() > i)
				return data.getComponent(i);
		return Double.NaN;
	}
}
