/**
 * 
 */
package SketchMaster.lib;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import SketchMaster.Stroke.StrokeData.PointData;
import SketchMaster.Stroke.StrokeData.Stroke;
import SketchMaster.Stroke.graphics.shapes.Line;
import SketchMaster.gui.DrawingDebugUtils;
import SketchMaster.system.SystemSettings;

import java.lang.*;

import org.apache.log4j.Logger;

/**
 * @author Mahi
 * 
 */

/**
 * 
 */

/*
 * (C) 2004 - Geotechnical Software Services
 * 
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This code is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 */
// package no.geosoft.cc.geometry;
/**
 * Collection of geometry utility methods. All methods are static.
 * 
 * @author <a href="mailto:jacob.dreyer@geosoft.no">Jacob Dreyer</a>
 */
public final class ComputationsGeometry {

	private static boolean debug=true; 
	private static final Logger logger = Logger.getLogger(ComputationsGeometry.class);
	public ComputationsGeometry() {

	}

	/**
	 * Return true if c is between a and b.
	 */
//	@SuppressWarnings("unused")
//	private static boolean isBetween(int a, int b, int c) {
//		return b > a ? c >= a && c <= b : c >= b && c <= a;
//	}

	/**
	 * Return true if c is between a and b.
	 */
	private static boolean isBetween(double a, double b, double c) {
		return b > a ? c >= a && c <= b : c >= b && c <= a;
	}

	/**
	 * Check if two double precision numbers are "equal", i.e. close enough to a
	 * given limit.
	 * 
	 * @param a
	 *            First number to check
	 * @param b
	 *            Second number to check
	 * @param limit
	 *            The definition of "equal".
	 * @return True if the twho numbers are "equal", false otherwise
	 */
	private static boolean equals(double a, double b, double limit) {
		return Math.abs(a - b) < limit;
	}

	/**
	 * Check if two double precision numbers are "equal", i.e. close enough to a
	 * prespecified limit.
	 * 
	 * @param a
	 *            First number to check
	 * @param b
	 *            Second number to check
	 * @return True if the twho numbers are "equal", false otherwise
	 */
	private static boolean equals(double a, double b) {
		return equals(a, b, SystemSettings.EQUAL_LIMIT);
	}

	/**
	 * Return smallest of four numbers.
	 * 
	 * @param a
	 *            First number to find smallest among.
	 * @param b
	 *            Second number to find smallest among.
	 * @param c
	 *            Third number to find smallest among.
	 * @param d
	 *            Fourth number to find smallest among.
	 * @return Smallest of a, b, c and d.
	 */
	private static double min(double a, double b, double c, double d) {
		return Math.min(Math.min(a, b), Math.min(c, d));
	}

	/**
	 * Return largest of four numbers.
	 * 
	 * @param a
	 *            First number to find largest among.
	 * @param b
	 *            Second number to find largest among.
	 * @param c
	 *            Third number to find largest among.
	 * @param d
	 *            Fourth number to find largest among.
	 * @return Largest of a, b, c and d.
	 */
	private static double max(double a, double b, double c, double d) {
		return Math.max(Math.max(a, b), Math.max(c, d));
	}

	/**
	 * Check if a specified point is inside a specified rectangle.
	 * 
	 * @param x0,
	 *            y0, x1, y1 Upper left and lower right corner of rectangle
	 *            (inclusive)
	 * @param x,y
	 *            Point to check.
	 * @return True if the point is inside the rectangle, false otherwise.
	 */
	public static boolean isPointInsideRectangle(int x0, int y0, int x1,
			int y1, int x, int y) {
		return x >= x0 && x < x1 && y >= y0 && y < y1;
	}

	/**
	 * Check if a given point is inside a given (complex) polygon.
	 * 
	 * @param x,
	 *            y Polygon.
	 * @param pointX,
	 *            pointY Point to check.
	 * @return True if the given point is inside the polygon, false otherwise.
	 */
	public static boolean isPointInsidePolygon(double[] x, double[] y,
			double pointX, double pointY) {
		boolean isInside = false;
		int nPoints = x.length;

		int j = 0;
		for (int i = 0; i < nPoints; i++) {
			j++;
			if (j == nPoints)
				j = 0;

			if (y[i] < pointY && y[j] >= pointY || y[j] < pointY
					&& y[i] >= pointY) {
				if (x[i] + (pointY - y[i]) / (y[j] - y[i]) * (x[j] - x[i]) < pointX) {
					isInside = !isInside;
				}
			}
		}

		return isInside;
	}

	/**
	 * Check if a given point is inside a given polygon. Integer domain.
	 * 
	 * @param x,
	 *            y Polygon.
	 * @param pointX,
	 *            pointY Point to check.
	 * @return True if the given point is inside the polygon, false otherwise.
	 */
	public static boolean isPointInsidePolygon(int[] x, int[] y, int pointX,
			int pointY) {
		boolean isInside = false;
		int nPoints = x.length;

		int j = 0;
		for (int i = 0; i < nPoints; i++) {
			j++;
			if (j == nPoints)
				j = 0;

			if (y[i] < pointY && y[j] >= pointY || y[j] < pointY
					&& y[i] >= pointY) {
				if (x[i] + (double) (pointY - y[i]) / (double) (y[j] - y[i])
						* (x[j] - x[i]) < pointX) {
					isInside = !isInside;
				}
			}
		}

		return isInside;
	}

	/**
	 * Find the point on the line p0,p1 [x,y,z] a given fraction from p0.
	 * Fraction of 0.0 whould give back p0, 1.0 give back p1, 0.5 returns
	 * midpoint of line p0,p1 and so on. F raction can be >1 and it can be
	 * negative to return any point on the line specified by p0,p1.
	 * 
	 * @param p0
	 *            First coordinale of line [x,y,z].
	 * @param p0
	 *            Second coordinale of line [x,y,z].
	 * @param fractionFromP0
	 *            Point we are looking for coordinates of
	 * @param p
	 *            Coordinate of point we are looking for
	 */
	public static double[] computePointOnLine(double[] p0, double[] p1,
			double fractionFromP0) {
		double[] p = new double[3];

		p[0] = p0[0] + fractionFromP0 * (p1[0] - p0[0]);
		p[1] = p0[1] + fractionFromP0 * (p1[1] - p0[1]);
		p[2] = p0[2] + fractionFromP0 * (p1[2] - p0[2]);

		return p;
	}

	/**
	 * Find the point on the line defined by x0,y0,x1,y1 a given fraction from
	 * x0,y0. 2D version of method above..
	 * 
	 * @param x0,
	 *            y0 First point defining the line
	 * @param x1,
	 *            y1 Second point defining the line
	 * @param fractionFrom0
	 *            Distance from (x0,y0)
	 * @return x, y Coordinate of point we are looking for
	 */
	public static double[] computePointOnLine(double x0, double y0, double x1,
			double y1, double fractionFrom0) {
		double[] p0 = { x0, y0, 0.0 };
		double[] p1 = { x1, y1, 0.0 };

		double[] p = ComputationsGeometry.computePointOnLine(p0, p1,
				fractionFrom0);

		double[] r = { p[0], p[1] };
		return r;
	}

	/**
	 * Extend a given line segment to a specified length.
	 * 
	 * @param p0,
	 *            p1 Line segment to extend [x,y,z].
	 * @param toLength
	 *            Length of new line segment.
	 * @param anchor
	 *            Specifies the fixed point during extension. If anchor is 0.0,
	 *            p0 is fixed and p1 is adjusted. If anchor is 1.0, p1 is fixed
	 *            and p0 is adjusted. If anchor is 0.5, the line is adjusted
	 *            equally in each direction and so on.
	 */
	public static void extendLine(double[] p0, double[] p1, double toLength,
			double anchor) {
		double[] p = ComputationsGeometry.computePointOnLine(p0, p1, anchor);

		double length0 = toLength * anchor;
		double length1 = toLength * (1.0 - anchor);

		ComputationsGeometry.extendLine(p, p0, length0);
		ComputationsGeometry.extendLine(p, p1, length1);
	}

	/**
	 * Extend a given line segment to a given length and holding the first point
	 * of the line as fixed.
	 * 
	 * @param p0,
	 *            p1 Line segment to extend. p0 is fixed during extension
	 * @param length
	 *            Length of new line segment.
	 */
	public static void extendLine(double[] p0, double[] p1, double toLength) {
		double oldLength = ComputationsGeometry.length(p0, p1);
		double lengthFraction = oldLength != 0.0 ? toLength / oldLength : 0.0;

		p1[0] = p0[0] + (p1[0] - p0[0]) * lengthFraction;
		p1[1] = p0[1] + (p1[1] - p0[1]) * lengthFraction;
		p1[2] = p0[2] + (p1[2] - p0[2]) * lengthFraction;
	}

	/**
	 * Return the length of a vector.
	 * 
	 * @param v
	 *            Vector to compute length of [x,y,z].
	 * @return Length of vector.
	 */
	public static double length(double[] v) {
		return Math.sqrt(v[0] * v[0] + v[1] * v[1] + v[2] * v[2]);
	}

	/**
	 * Compute distance between two points.
	 * 
	 * @param p0,
	 *            p1 Points to compute distance between [x,y,z].
	 * @return Distance between points.
	 */
	public static double length(double[] p0, double[] p1) {
		double[] v = ComputationsGeometry.createVector(p0, p1);
		return length(v);
	}

	/**
	 * Compute the length of the line from (x0,y0) to (x1,y1)
	 * 
	 * @param x0,
	 *            y0 First line end point.
	 * @param x1,
	 *            y1 Second line end point.
	 * @return Length of line from (x0,y0) to (x1,y1).
	 */
	public static double length(int x0, int y0, int x1, int y1) {
		return ComputationsGeometry.length((double) x0, (double) y0,
				(double) x1, (double) y1);
	}

	/**
	 * Compute the length of the line from (x0,y0) to (x1,y1)
	 * 
	 * @param x0,
	 *            y0 First line end point.
	 * @param x1,
	 *            y1 Second line end point.
	 * @return Length of line from (x0,y0) to (x1,y1).
	 */
	public static double length(double x0, double y0, double x1, double y1) {
		double dx = x1 - x0;
		double dy = y1 - y0;

		return Math.sqrt(dx * dx + dy * dy);
	}

	/**
	 * Compute the length of a polyline.
	 * 
	 * @param x,
	 *            y Arrays of x,y coordinates
	 * @param nPoints
	 *            Number of elements in the above.
	 * @param isClosed
	 *            True if this is a closed polygon, false otherwise
	 * @return Length of polyline defined by x, y and nPoints.
	 */
	public static double length(int[] x, int[] y, boolean isClosed) {
		double length = 0.0;

		int nPoints = x.length;
		for (int i = 0; i < nPoints - 1; i++)
			length += ComputationsGeometry.length(x[i], y[i], x[i + 1],
					y[i + 1]);

		// Add last leg if this is a polygon
		if (isClosed && nPoints > 1)
			length += ComputationsGeometry.length(x[nPoints - 1],
					y[nPoints - 1], x[0], y[0]);

		return length;
	}

	/**
	 * Return distance bwetween the line defined by (x0,y0) and (x1,y1) and the
	 * point (x,y). Ref:
	 * http://astronomy.swin.edu.au/pbourke/geometry/pointline/ The 3D case
	 * should be similar.
	 * 
	 * @param x0,
	 *            y0 First point of line.
	 * @param x1,
	 *            y1 Second point of line.
	 * @param x,
	 *            y, Point to consider.
	 * @return Distance from x,y down to the (extended) line defined by x0, y0,
	 *         x1, y1.
	 */
	public static double distance(int x0, int y0, int x1, int y1, int x, int y) {
		// If x0,y0,x1,y1 is same point, we return distance to that point
		double length = ComputationsGeometry.length(x0, y0, x1, y1);
		if (length == 0.0)
			return ComputationsGeometry.length(x0, y0, x, y);

		// If u is [0,1] then (xp,yp) is on the line segment (x0,y0),(x1,y1).
		double u = ((x - x0) * (x1 - x0) + (y - y0) * (y1 - y0))
				/ (length * length);

		// This is the intersection point of the normal.
		// TO DO: Might consider returning this as well.
		double xp = x0 + u * (x1 - x0);
		double yp = y0 + u * (y1 - y0);

		length = ComputationsGeometry.length(xp, yp, x, y);
		return length;
	}

	/**
	 * This is a copy of the original library distance funciton just i changed
	 * it to make it take a point data and then return an array with interection
	 * point .
	 * 
	 * Return distance bwetween the line defined by (x0,y0) and (x1,y1) and the
	 * point (x,y). Ref:
	 * http://astronomy.swin.edu.au/pbourke/geometry/pointline/ The 3D case
	 * should be similar.
	 * 
	 * @param x0,
	 *            y0 First point of line.
	 * @param x1,
	 *            y1 Second point of line.
	 * @param x,
	 *            y, Point to consider.
	 * @return An array first element is Distance from x,y down to the
	 *         (extended) line defined by x0, y0, x1, y1. The second two element
	 *         is the xp , yp coordinate of the intersection point.
	 */
	public static double[] distance2(PointData p1, PointData p2, PointData p3) {

		double x0 = p1.x;
		double y0 = p1.y;
		double x1 = p2.x;
		double y1 = p2.y;
		double x = p3.x;
		double y = p3.y;
		double[] toReturn = new double[3];

		// If x0,y0,x1,y1 is same point, we return distance to that point
		double length = ComputationsGeometry.length(x0, y0, x1, y1);
		if (length == 0.0) {
			length = ComputationsGeometry.length(x0, y0, x, y);
			toReturn[0] = length;
			toReturn[1] = x0;
			toReturn[2] = y0;
			return toReturn;
		}

		// If u is [0,1] then (xp,yp) is on the line segment (x0,y0),(x1,y1).
		double u = ((x - x0) * (x1 - x0) + (y - y0) * (y1 - y0))
				/ (length * length);

		// This is the intersection point of the normal.

		double xp = x0 + u * (x1 - x0);
		double yp = y0 + u * (y1 - y0);

		length = ComputationsGeometry.length(xp, yp, x, y);
		toReturn[0] = length;
		toReturn[1] = xp;
		toReturn[2] = yp;

		return toReturn;
	}

	/**
	 * Find the angle between twree points. P0 is center point
	 * 
	 * @param p0,
	 *            p1, p2 Three points finding angle between [x,y,z].
	 * @return Angle (in radians) between given points.
	 */
	public static double computeAngle(double[] p0, double[] p1, double[] p2) {
		double[] v0 = ComputationsGeometry.createVector(p0, p1);
		double[] v1 = ComputationsGeometry.createVector(p0, p2);

		double dotProduct = ComputationsGeometry.computeDotProduct(v0, v1);

		double length1 = ComputationsGeometry.length(v0);
		double length2 = ComputationsGeometry.length(v1);

		double denominator = length1 * length2;

		double product = denominator != 0.0 ? dotProduct / denominator : 0.0;

		double angle = Math.acos(product);

		return angle;
	}

	/**
	 * Compute the dot product (a scalar) between two vectors.
	 * 
	 * @param v0,
	 *            v1 Vectors to compute dot product between [x,y,z].
	 * @return Dot product of given vectors.
	 */
	public static double computeDotProduct(double[] v0, double[] v1) {
		return v0[0] * v1[0] + v0[1] * v1[1] + v0[2] * v1[2];
	}

	/**
	 * Compute the cross product (a vector) of two vectors.
	 * 
	 * @param v0,
	 *            v1 Vectors to compute cross product between [x,y,z].
	 * @param crossProduct
	 *            Cross product of specified vectors [x,y,z].
	 */
	public static double[] computeCrossProduct(double[] v0, double[] v1) {
		double crossProduct[] = new double[3];

		crossProduct[0] = v0[1] * v1[2] - v0[2] * v1[1];
		crossProduct[1] = v0[2] * v1[0] - v0[0] * v1[2];
		crossProduct[2] = v0[0] * v1[1] - v0[1] * v1[0];

		return crossProduct;
	}

	/**
	 * Construct the vector specified by two points.
	 * 
	 * @param p0,
	 *            p1 Points the construct vector between [x,y,z].
	 * @return v Vector from p0 to p1 [x,y,z].
	 */
	public static double[] createVector(double[] p0, double[] p1) {
		double v[] = { p1[0] - p0[0], p1[1] - p0[1], p1[2] - p0[2] };
		return v;
	}

	/**
	 * Check if two points are on the same side of a given line. Algorithm from
	 * Sedgewick page 350.
	 * 
	 * @param x0,
	 *            y0, x1, y1 The line.
	 * @param px0,
	 *            py0 First point.
	 * @param px1,
	 *            py1 Second point.
	 * @return <0 if points on opposite sides. =0 if one of the points is
	 *         exactly on the line >0 if points on same side.
	 */
	private static int sameSide(double x0, double y0, double x1, double y1,
			double px0, double py0, double px1, double py1) {
		int sameSide = 0;

		double dx = x1 - x0;
		double dy = y1 - y0;
		double dx1 = px0 - x0;
		double dy1 = py0 - y0;
		double dx2 = px1 - x1;
		double dy2 = py1 - y1;

		// Cross product of the vector from the endpoint of the line to the
		// point
		double c1 = dx * dy1 - dy * dx1;
		double c2 = dx * dy2 - dy * dx2;

		if (c1 != 0 && c2 != 0)
			sameSide = c1 < 0 != c2 < 0 ? -1 : 1;
		else if (dx == 0 && dx1 == 0 && dx2 == 0)
			sameSide = !isBetween(y0, y1, py0) && !isBetween(y0, y1, py1) ? 1
					: 0;
		else if (dy == 0 && dy1 == 0 && dy2 == 0)
			sameSide = !isBetween(x0, x1, px0) && !isBetween(x0, x1, px1) ? 1
					: 0;

		return sameSide;
	}

	/**
	 * Check if two points are on the same side of a given line. Integer domain.
	 * 
	 * @param x0,
	 *            y0, x1, y1 The line.
	 * @param px0,
	 *            py0 First point.
	 * @param px1,
	 *            py1 Second point.
	 * @return <0 if points on opposite sides. =0 if one of the points is
	 *         exactly on the line >0 if points on same side.
	 */
	private static int sameSide(int x0, int y0, int x1, int y1, int px0,
			int py0, int px1, int py1) {
		return sameSide((double) x0, (double) y0, (double) x1, (double) y1,
				(double) px0, (double) py0, (double) px1, (double) py1);
	}

	/**
	 * Check if two line segments intersects. Integer domain.
	 * 
	 * @param x0,
	 *            y0, x1, y1 End points of first line to check.
	 * @param x2,
	 *            yy, x3, y3 End points of second line to check.
	 * @return True if the two lines intersects.
	 */
	public static boolean isLineIntersectingLine(int x0, int y0, int x1,
			int y1, int x2, int y2, int x3, int y3) {
		int s1 = ComputationsGeometry.sameSide(x0, y0, x1, y1, x2, y2, x3, y3);
		int s2 = ComputationsGeometry.sameSide(x2, y2, x3, y3, x0, y0, x1, y1);

		return s1 <= 0 && s2 <= 0;
	}

	/**
	 * Check if a specified line intersects a specified rectangle. Integer
	 * domain.
	 * 
	 * @param lx0,
	 *            ly0 1st end point of line
	 * @param ly1,
	 *            ly1 2nd end point of line
	 * @param x0,
	 *            y0, x1, y1 Upper left and lower right corner of rectangle
	 *            (inclusive).
	 * @return True if the line intersects the rectangle, false otherwise.
	 */
	public static boolean isLineIntersectingRectangle(int lx0, int ly0,
			int lx1, int ly1, int x0, int y0, int x1, int y1) {
		// Is one of the line endpoints inside the rectangle
		if (ComputationsGeometry.isPointInsideRectangle(x0, y0, x1, y1, lx0,
				ly0)
				|| ComputationsGeometry.isPointInsideRectangle(x0, y0, x1, y1,
						lx1, ly1))
			return true;

		// If it intersects it goes through. Need to check three sides only.

		// Check against top rectangle line
		if (ComputationsGeometry.isLineIntersectingLine(lx0, ly0, lx1, ly1, x0,
				y0, x1, y0))
			return true;

		// Check against left rectangle line
		if (ComputationsGeometry.isLineIntersectingLine(lx0, ly0, lx1, ly1, x0,
				y0, x0, y1))
			return true;

		// Check against bottom rectangle line
		if (ComputationsGeometry.isLineIntersectingLine(lx0, ly0, lx1, ly1, x0,
				y1, x1, y1))
			return true;

		return false;
	}

	/**
	 * Check if a specified polyline intersects a specified rectangle. Integer
	 * domain.
	 * 
	 * @param x,
	 *            y Polyline to check.
	 * @param x0,
	 *            y0, x1, y1 Upper left and lower left corner of rectangle
	 *            (inclusive).
	 * @return True if the polyline intersects the rectangle, false otherwise.
	 */
	public static boolean isPolylineIntersectingRectangle(int[] x, int[] y,
			int x0, int y0, int x1, int y1) {
		if (x.length == 0)
			return false;

		if (ComputationsGeometry.isPointInsideRectangle(x[0], y[0], x0, y0, x1,
				y1))
			return true;

		else if (x.length == 1)
			return false;

		for (int i = 1; i < x.length; i++) {
			if (x[i - 1] != x[i] || y[i - 1] != y[i])
				if (ComputationsGeometry.isLineIntersectingRectangle(x[i - 1],
						y[i - 1], x[i], y[i], x0, y0, x1, y1))
					return true;
		}

		return false;
	}

	/**
	 * Check if a specified polygon intersects a specified rectangle. Integer
	 * domain.
	 * 
	 * @param x
	 *            X coordinates of polyline.
	 * @param y
	 *            Y coordinates of polyline.
	 * @param x0
	 *            X of upper left corner of rectangle.
	 * @param y0
	 *            Y of upper left corner of rectangle.
	 * @param x1
	 *            X of lower right corner of rectangle.
	 * @param y1
	 *            Y of lower right corner of rectangle.
	 * @return True if the polyline intersects the rectangle, false otherwise.
	 */
	public static boolean isPolygonIntersectingRectangle(int[] x, int[] y,
			int x0, int y0, int x1, int y1) {
		int n = x.length;

		if (n == 0)
			return false;

		if (n == 1)
			return ComputationsGeometry.isPointInsideRectangle(x0, y0, x1, y1,
					x[0], y[0]);

		//
		// If the polyline constituting the polygon intersects the rectangle
		// the polygon does too.
		//
		if (ComputationsGeometry.isPolylineIntersectingRectangle(x, y, x0, y0,
				x1, y1))
			return true;

		// Check last leg as well
		if (ComputationsGeometry.isLineIntersectingRectangle(x[n - 2],
				y[n - 2], x[n - 1], y[n - 1], x0, y0, x1, y1))
			return true;

		//
		// The rectangle and polygon are now completely including each other
		// or separate.
		//
		if (ComputationsGeometry.isPointInsidePolygon(x, y, x0, y0)
				|| ComputationsGeometry.isPointInsideRectangle(x0, y0, x1, y1,
						x[0], y[0]))
			return true;

		// Separate
		return false;
	}

	/**
	 * Compute the area of the specfied polygon.
	 * 
	 * @param x
	 *            X coordinates of polygon.
	 * @param y
	 *            Y coordinates of polygon.
	 * @return Area of specified polygon.
	 */
	public static double computePolygonArea(double[] x, double[] y) {
		int n = x.length;

		double area = 0.0;
		for (int i = 0; i < n - 1; i++)
			area += (x[i] * y[i + 1]) - (x[i + 1] * y[i]);
		area += (x[n - 1] * y[0]) - (x[0] * y[n - 1]);

		area *= 0.5;

		return area;
	}

	/**
	 * Compute the area of the specfied polygon.
	 * 
	 * @param xy
	 *            ComputationsGeometry of polygon [x,y,...]
	 * @return Area of specified polygon.
	 */
	public static double computePolygonArea(double[] xy) {
		int n = xy.length;

		double area = 0.0;
		for (int i = 0; i < n - 2; i += 2)
			area += (xy[i] * xy[i + 3]) - (xy[i + 2] * xy[i + 1]);
		area += (xy[xy.length - 2] * xy[1]) - (xy[0] * xy[xy.length - 1]);

		area *= 0.5;

		return area;
	}

	/**
	 * Compute centorid (center of gravity) of specified polygon.
	 * 
	 * @param x
	 *            X coordinates of polygon.
	 * @param y
	 *            Y coordinates of polygon.
	 * @return Centroid [x,y] of specified polygon.
	 */
	public static double[] computePolygonCentroid(double[] x, double[] y) {
		double cx = 0.0;
		double cy = 0.0;

		int n = x.length;
		for (int i = 0; i < n - 1; i++) {
			double a = x[i] * y[i + 1] - x[i + 1] * y[i];
			cx += (x[i] + x[i + 1]) * a;
			cy += (y[i] + y[i + 1]) * a;
		}
		double a = x[n - 1] * y[0] - x[0] * y[n - 1];
		cx += (x[n - 1] + x[0]) * a;
		cy += (y[n - 1] + y[0]) * a;

		double area = ComputationsGeometry.computePolygonArea(x, y);

		cx /= 6 * area;
		cy /= 6 * area;

		return new double[] { cx, cy };
	}

	/**
	 * Find the 3D extent of a polyline.
	 * 
	 * @param x
	 *            X coordinates of polyline.
	 * @param y
	 *            Y coordinates of polyline.
	 * @param z
	 *            Z coordinates of polyline. May be null if this is a 2D case.
	 * @param xExtent
	 *            Will upon return contain [xMin,xMax].
	 * @param yExtent
	 *            Will upon return contain [xMin,xMax].
	 * @param zExtent
	 *            Will upon return contain [xMin,xMax]. Unused (may be set to
	 *            null) if z is null.
	 */
	public static void findPolygonExtent(double[] x, double[] y, double[] z,
			double[] xExtent, double[] yExtent, double[] zExtent) {

		double xMin = +Double.MAX_VALUE;
		double xMax = -Double.MAX_VALUE;

		double yMin = +Double.MAX_VALUE;
		double yMax = -Double.MAX_VALUE;

		double zMin = +Double.MAX_VALUE;
		double zMax = -java.lang.Double.MAX_VALUE;

		for (int i = 0; i < x.length; i++) {
			if (x[i] < xMin)
				xMin = x[i];
			if (x[i] > xMax)
				xMax = x[i];

			if (y[i] < yMin)
				yMin = y[i];
			if (y[i] > yMax)
				yMax = y[i];

			if (z != null) {
				if (z[i] < zMin)
					zMin = z[i];
				if (z[i] > zMax)
					zMax = z[i];
			}
		}

		xExtent[0] = xMin;
		xExtent[1] = xMax;

		yExtent[0] = yMin;
		yExtent[1] = yMax;

		if (z != null) {
			zExtent[0] = zMin;
			zExtent[1] = zMax;
		}
	}

	/**
	 * Find the extent of a polygon.
	 * 
	 * @param x
	 *            X coordinates of polygon.
	 * @param y
	 *            Y coordinates of polygon.
	 * @param xExtent
	 *            Will upon return contain [xMin, xMax]
	 * @param yExtent
	 *            Will upon return contain [yMin, yMax]
	 */
	public static void findPolygonExtent(int[] x, int[] y, int[] xExtent, // xMin,
			// xMax
			int[] yExtent) // yMin, yMax
	{
		int xMin = +Integer.MAX_VALUE;
		int xMax = -Integer.MAX_VALUE;

		int yMin = +Integer.MAX_VALUE;
		int yMax = -Integer.MAX_VALUE;

		for (int i = 0; i < x.length; i++) {
			if (x[i] < xMin)
				xMin = x[i];
			if (x[i] > xMax)
				xMax = x[i];

			if (y[i] < yMin)
				yMin = y[i];
			if (y[i] > yMax)
				yMax = y[i];
		}

		xExtent[0] = xMin;
		xExtent[1] = xMax;

		yExtent[0] = yMin;
		yExtent[1] = yMax;
	}

	
	
	/**
	 * Compute the intersection between two line segments, or two lines of
	 * infinite length.
	 * 
	 * @param x0
	 *            X coordinate first end point first line segment.
	 * @param y0
	 *            Y coordinate first end point first line segment.
	 * @param x1
	 *            X coordinate second end point first line segment.
	 * @param y1
	 *            Y coordinate second end point first line segment.
	 * @param x2
	 *            X coordinate first end point second line segment.
	 * @param y2
	 *            Y coordinate first end point second line segment.
	 * @param x3
	 *            X coordinate second end point second line segment.
	 * @param y3
	 *            Y coordinate second end point second line segment.
	 * @param intersection[2]
	 *            Preallocated by caller to double[2]
	 * @return -1 if lines are parallel (x,y unset), -2 if lines are parallel
	 *         and overlapping (x, y center) 0 if intesrection outside segments
	 *         (x,y set) +1 if segments intersect (x,y set)
	 */
	public static int findLineSegmentIntersection(double x0, double y0,
			double x1, double y1, double x2, double y2, double x3, double y3,
			double[] intersection) {
		// TODO: Make limit depend on input domain
		final double LIMIT = SystemSettings.EQUAL_LIMIT;
		final double INFINITY = 1e10;

		double x, y;

		//
		// Convert the lines to the form y = ax + b
		//

		// Slope of the two lines
		double a0 = ComputationsGeometry.equals(x0, x1, LIMIT) ? INFINITY
				: (y0 - y1) / (x0 - x1);
		double a1 = ComputationsGeometry.equals(x2, x3, LIMIT) ? INFINITY
				: (y2 - y3) / (x2 - x3);

		double b0 = y0 - a0 * x0;
		double b1 = y2 - a1 * x2;

		// Check if lines are parallel
		if (ComputationsGeometry.equals(a0, a1)) {
			//logger.info("-----------------");
			
			if (!ComputationsGeometry.equals(b0, b1))
				return -1; // Parallell non-overlapping

			else {
				if (ComputationsGeometry.equals(x0, x1)) {
					if (Math.min(y0, y1) < Math.max(y2, y3)
							|| Math.max(y0, y1) > Math.min(y2, y3)) {
						double twoMiddle = y0 + y1 + y2 + y3
								- ComputationsGeometry.min(y0, y1, y2, y3)
								- ComputationsGeometry.max(y0, y1, y2, y3);
						y = (twoMiddle) / 2.0;
						x = (y - b0) / a0;
					} else
						return -1; // Parallell non-overlapping
				} else {
					if (Math.min(x0, x1) < Math.max(x2, x3)
							|| Math.max(x0, x1) > Math.min(x2, x3)) {
						double twoMiddle = x0 + x1 + x2 + x3
								- ComputationsGeometry.min(x0, x1, x2, x3)
								- ComputationsGeometry.max(x0, x1, x2, x3);
						x = (twoMiddle) / 2.0;
						y = a0 * x + b0;
					} else
						return -1;
				}

				intersection[0] = x;
				intersection[1] = y;
				return -2;
			}
		}

		// Find correct intersection point
		if (ComputationsGeometry.equals(a0, INFINITY)) {
			x = x0;
			y = a1 * x + b1;
		} else if (ComputationsGeometry.equals(a1, INFINITY)) {
			x = x2;
			y = a0 * x + b0;
		} else {
			x = -(b0 - b1) / (a0 - a1);
			y = a0 * x + b0;
		}

		intersection[0] = x;
		intersection[1] = y;

		// Then check if intersection is within line segments
		double distanceFrom1;
		if (ComputationsGeometry.equals(x0, x1)) {
			if (y0 < y1)
				distanceFrom1 = y < y0 ? ComputationsGeometry.length(x, y, x0,
						y0) : y > y1 ? ComputationsGeometry
						.length(x, y, x1, y1) : 0.0;
			else
				distanceFrom1 = y < y1 ? ComputationsGeometry.length(x, y, x1,
						y1) : y > y0 ? ComputationsGeometry
						.length(x, y, x0, y0) : 0.0;
		} else {
			if (x0 < x1)
				distanceFrom1 = x < x0 ? ComputationsGeometry.length(x, y, x0,
						y0) : x > x1 ? ComputationsGeometry
						.length(x, y, x1, y1) : 0.0;
			else
				distanceFrom1 = x < x1 ? ComputationsGeometry.length(x, y, x1,
						y1) : x > x0 ? ComputationsGeometry
						.length(x, y, x0, y0) : 0.0;
		}

		double distanceFrom2;
		if (ComputationsGeometry.equals(x2, x3)) {
			if (y2 < y3)
				distanceFrom2 = y < y2 ? ComputationsGeometry.length(x, y, x2,
						y2) : y > y3 ? ComputationsGeometry
						.length(x, y, x3, y3) : 0.0;
			else
				distanceFrom2 = y < y3 ? ComputationsGeometry.length(x, y, x3,
						y3) : y > y2 ? ComputationsGeometry
						.length(x, y, x2, y2) : 0.0;
		} else {
			if (x2 < x3)
				distanceFrom2 = x < x2 ? ComputationsGeometry.length(x, y, x2,
						y2) : x > x3 ? ComputationsGeometry
						.length(x, y, x3, y3) : 0.0;
			else
				distanceFrom2 = x < x3 ? ComputationsGeometry.length(x, y, x3,
						y3) : x > x2 ? ComputationsGeometry
						.length(x, y, x2, y2) : 0.0;
		}

		return ComputationsGeometry.equals(distanceFrom1, 0.0)
				&& ComputationsGeometry.equals(distanceFrom2, 0.0) ? 1 : 0;
	}

	/**
	 * Find the intersections between a polygon and a straight line.
	 * 
	 * NOTE: This method is only guaranteed to work if the polygon is first
	 * preprocessed so that "unneccesary" vertices are removed (i.e vertices on
	 * the straight line between its neighbours).
	 * 
	 * @param x
	 *            X coordinates of polygon.
	 * @param y
	 *            Y coordinates of polygon.
	 * @param x0
	 *            X first end point of line.
	 * @param x0
	 *            Y first end point of line.
	 * @param x0
	 *            X second end point of line.
	 * @param x0
	 *            Y second end point of line.
	 * @return Intersections [x,y,x,y...].
	 */
	public static double[] findLinePolygonIntersections(double[] x, double[] y,
			double x0, double y0, double x1, double y1) {
		double x2, y2, x3, y3;
		// double xi, yi;
		int nPoints = x.length;

		int nIntersections = 0;
		double[] intersections = new double[24]; // Result vector x,y,x,y,...
		double[] intersection = new double[2]; // Any given intersection x,y

		for (int i = 0; i < nPoints; i++) {
			int next = i == nPoints - 1 ? 0 : i + 1;

			// The line segment of the polyline to check
			x2 = x[i];
			y2 = y[i];
			x3 = x[next];
			y3 = y[next];

			boolean isIntersecting = false;

			// Ignore segments of zero length
			if (ComputationsGeometry.equals(x2, x3)
					&& ComputationsGeometry.equals(y2, y3))
				continue;

			int type = ComputationsGeometry.findLineSegmentIntersection(x0, y0,
					x1, y1, x2, y2, x3, y3, intersection);

			if (type == -2) { // Overlapping
				int p1 = i == 0 ? nPoints - 1 : i - 1;
				int p2 = next == nPoints - 1 ? 0 : next + 1;

				int side = ComputationsGeometry.sameSide(x0, y0, x1, y1, x[p1],
						y[p1], x[p2], y[p2]);

				if (side < 0)
					isIntersecting = true;
			} else if (type == 1)
				isIntersecting = true;

			// Add the intersection point
			if (isIntersecting) {

				// Reallocate if necessary
				if (nIntersections << 1 == intersections.length) {
					double[] newArray = new double[nIntersections << 2];
					System.arraycopy(intersections, 0, newArray, 0,
							intersections.length);
					intersections = newArray;
				}

				// Then add
				intersections[nIntersections << 1 + 0] = intersection[0];
				intersections[nIntersections << 1 + 1] = intersection[1];

				nIntersections++;
			}
		}

		if (nIntersections == 0)
			return null;

		// Reallocate result so array match number of intersections
		double[] finalArray = new double[nIntersections << 2];
		System.arraycopy(intersections, 0, finalArray, 0, finalArray.length);

		return finalArray;
	}

	/**
	 * Return the geometry of an ellipse based on its four top points. Integer
	 * domain. The method use the generic createEllipse() method for the main
	 * task, and then transforms this according to any rotation or skew defined
	 * by the given top points.
	 * 
	 * @param x
	 *            X array of four top points of ellipse.
	 * @param y
	 *            Y array of four top points of ellipse.
	 * @return ComputationsGeometry of ellipse [x,y,x,y...].
	 */
	public static int[] createEllipse(int[] x, int[] y) {
		// Center of ellipse
		int x0 = (x[0] + x[2]) / 2;
		int y0 = (y[0] + y[2]) / 2;

		// Angle between axis define skew
		double[] p0 = { (double) x0, (double) y0, 0.0 };
		double[] p1 = { (double) x[0], (double) y[0], 0.0 };
		double[] p2 = { (double) x[1], (double) y[1], 0.0 };

		double axisAngle = ComputationsGeometry.computeAngle(p0, p1, p2);

		// dx / dy
		double dx = ComputationsGeometry.length(x0, y0, x[1], y[1]);
		double dy = ComputationsGeometry.length(x0, y0, x[0], y[0])
				* Math.sin(axisAngle);

		// Create geometry for unrotated / unsheared ellipse
		int[] ellipse = createEllipse(x0, y0, (int) Math.round(dx), (int) Math
				.round(dy));
		int nPoints = ellipse.length / 2;

		// Shear if neccessary. If angle is close to 90 there is no shear.
		// If angle is close to 0 or 180 shear is infinite, and we set
		// it to zero as well.
		if (!ComputationsGeometry.equals(axisAngle, Math.PI / 2.0, 0.1)
				&& !ComputationsGeometry.equals(axisAngle, Math.PI, 0.1)
				&& !ComputationsGeometry.equals(axisAngle, 0.0, 0.1)) {
			double xShear = 1.0 / Math.tan(axisAngle);
			for (int i = 0; i < nPoints; i++)
				ellipse[i * 2 + 0] += Math.round((ellipse[i * 2 + 1] - y0)
						* xShear);
		}

		// Rotate
		int ddx = x[1] - x0;
		int ddy = y0 - y[1];

		double angle;
		if (ddx == 0 && ddy == 0)
			angle = 0.0;
		else if (ddx == 0)
			angle = Math.PI / 2.0;
		else
			angle = Math.atan((double) ddy / (double) ddx);

		double cosAngle = Math.cos(angle);
		double sinAngle = Math.sin(angle);

		for (int i = 0; i < nPoints; i++) {
			int xr = (int) Math.round(x0 + (ellipse[i * 2 + 0] - x0) * cosAngle
					- (ellipse[i * 2 + 1] - y0) * sinAngle);
			int yr = (int) Math.round(y0 - (ellipse[i * 2 + 1] - y0) * cosAngle
					- (ellipse[i * 2 + 0] - x0) * sinAngle);

			ellipse[i * 2 + 0] = xr;
			ellipse[i * 2 + 1] = yr;
		}

		return ellipse;
	}

	/**
	 * Create the geometry for an unrotated, unskewed ellipse. Integer domain.
	 * 
	 * @param x0
	 *            X center of ellipse.
	 * @param y0
	 *            Y center of ellipse.
	 * @param dx
	 *            X ellipse radius.
	 * @param dy
	 *            Y ellipse radius.
	 * @return Ellipse geometry [x,y,x,y,...].
	 */
	public static int[] createEllipse(int x0, int y0, int dx, int dy) {
		// Make sure deltas are positive
		dx = Math.abs(dx);
		dy = Math.abs(dy);

		// This is an approximate number of points we need to make a smooth
		// surface on a quater of the ellipse
		int nPoints = dx > dy ? dx : dy;
		nPoints /= 2;
		if (nPoints < 1)
			nPoints = 1;

		// Allocate arrays for holding the complete set of vertices around
		// the ellipse. Note that this is a complete ellipse: First and last
		// point coincide.
		int[] ellipse = new int[nPoints * 8 + 2];

		// Compute some intermediate results to save time in the inner loop
		int dxdy = dx * dy;
		int dx2 = dx * dx;
		int dy2 = dy * dy;

		// Handcode the entries in the four "corner" points of the ellipse,
		// i.e. at point 0, 90, 180, 270 and 360 degrees
		ellipse[nPoints * 0 + 0] = x0 + dx;
		ellipse[nPoints * 0 + 1] = y0;

		ellipse[nPoints * 8 + 0] = x0 + dx;
		ellipse[nPoints * 8 + 1] = y0;

		ellipse[nPoints * 2 + 0] = x0;
		ellipse[nPoints * 2 + 1] = y0 - dy;

		ellipse[nPoints * 4 + 0] = x0 - dx;
		ellipse[nPoints * 4 + 1] = y0;

		ellipse[nPoints * 6 + 0] = x0;
		ellipse[nPoints * 6 + 1] = y0 + dy;

		// Find the angle step
		double angleStep = nPoints > 0 ? Math.PI / 2.0 / nPoints : 0.0;

		// Loop over angles from 0 to 90. The rest of the ellipse can be
		// derrived
		// from this first quadrant. For each angle set the four corresponding
		// ellipse points.
		double a = 0.0;
		for (int i = 1; i < nPoints; i++) {
			a += angleStep;

			double t = Math.tan(a);

			double x = (double) dxdy / Math.sqrt(t * t * dx2 + dy2);
			double y = x * t;

			int xi = (int) (x + 0.5);
			int yi = (int) (y + 0.5);

			ellipse[(nPoints * 0 + i) * 2 + 0] = x0 + xi;
			ellipse[(nPoints * 2 - i) * 2 + 0] = x0 - xi;
			ellipse[(nPoints * 2 + i) * 2 + 0] = x0 - xi;
			ellipse[(nPoints * 4 - i) * 2 + 0] = x0 + xi;

			ellipse[(nPoints * 0 + i) * 2 + 1] = y0 - yi;
			ellipse[(nPoints * 2 - i) * 2 + 1] = y0 - yi;
			ellipse[(nPoints * 2 + i) * 2 + 1] = y0 + yi;
			ellipse[(nPoints * 4 - i) * 2 + 1] = y0 + yi;
		}

		return ellipse;
	}

	/**
	 * Create the geometry for an unrotated, unskewed ellipse. Floating point
	 * domain.
	 * 
	 * @param x0
	 *            X center of ellipse.
	 * @param y0
	 *            Y center of ellipse.
	 * @param dx
	 *            X ellipse radius.
	 * @param dy
	 *            Y ellipse radius.
	 * @return Ellipse geometry [x,y,x,y,...].
	 */
	public static double[] createEllipse(double x0, double y0, double dx,
			double dy) {
		// Make sure deltas are positive
		dx = Math.abs(dx);
		dy = Math.abs(dy);

		// As we don't know the resolution of the appliance of the ellipse
		// we create one vertex per 2nd degree. The nPoints variable holds
		// number of points in a quater of the ellipse.
		int nPoints = 45;

		// Allocate arrays for holding the complete set of vertices around
		// the ellipse. Note that this is a complete ellipse: First and last
		// point coincide.
		double[] ellipse = new double[nPoints * 8 + 2];

		// Compute some intermediate results to save time in the inner loop
		double dxdy = dx * dy;
		double dx2 = dx * dx;
		double dy2 = dy * dy;

		// Handcode the entries in the four "corner" points of the ellipse,
		// i.e. at point 0, 90, 180, 270 and 360 degrees
		ellipse[nPoints * 0 + 0] = x0 + dx;
		ellipse[nPoints * 0 + 1] = y0;

		ellipse[nPoints * 8 + 0] = x0 + dx;
		ellipse[nPoints * 8 + 1] = y0;

		ellipse[nPoints * 2 + 0] = x0;
		ellipse[nPoints * 2 + 1] = y0 - dy;

		ellipse[nPoints * 4 + 0] = x0 - dx;
		ellipse[nPoints * 4 + 1] = y0;

		ellipse[nPoints * 6 + 0] = x0;
		ellipse[nPoints * 6 + 1] = y0 + dy;

		// Find the angle step
		double angleStep = nPoints > 0 ? Math.PI / 2.0 / nPoints : 0.0;

		// Loop over angles from 0 to 90. The rest of the ellipse can be
		// derrived
		// from this first quadrant. For each angle set the four corresponding
		// ellipse points.
		double a = 0.0;
		for (int i = 1; i < nPoints; i++) {
			a += angleStep;

			double t = Math.tan(a);

			double x = (double) dxdy / Math.sqrt(t * t * dx2 + dy2);
			double y = x * t + 0.5;

			ellipse[(nPoints * 0 + i) * 2 + 0] = x0 + x;
			ellipse[(nPoints * 2 - i) * 2 + 0] = x0 - x;
			ellipse[(nPoints * 2 + i) * 2 + 0] = x0 - x;
			ellipse[(nPoints * 4 - i) * 2 + 0] = x0 + x;

			ellipse[(nPoints * 0 + i) * 2 + 1] = y0 - y;
			ellipse[(nPoints * 2 - i) * 2 + 1] = y0 - y;
			ellipse[(nPoints * 2 + i) * 2 + 1] = y0 + y;
			ellipse[(nPoints * 4 - i) * 2 + 1] = y0 + y;
		}

		return ellipse;
	}

	/**
	 * Create geometry for a circle. Integer domain.
	 * 
	 * @param x0
	 *            X center of circle.
	 * @param y0
	 *            Y center of circle.
	 * @param radius
	 *            Radius of circle.
	 * @return ComputationsGeometry of circle [x,y,...]
	 */
	public static int[] createCircle(int x0, int y0, int radius) {
		return createEllipse(x0, y0, radius, radius);
	}

	/**
	 * Create geometry for a circle. Floating point domain.
	 * 
	 * @param x0
	 *            X center of circle.
	 * @param y0
	 *            Y center of circle.
	 * @param radius
	 *            Radius of circle.
	 * @return ComputationsGeometry of circle [x,y,...]
	 */
	public static double[] createCircle(double x0, double y0, double radius) {
		return createEllipse(x0, y0, radius, radius);
	}

	/**
	 * Create the geometry of a sector of an ellipse.
	 * 
	 * @param x0
	 *            X coordinate of center of ellipse.
	 * @param y0
	 *            Y coordinate of center of ellipse.
	 * @param dx
	 *            X radius of ellipse.
	 * @param dy
	 *            Y radius of ellipse.
	 * @param angle0
	 *            First angle of sector (in radians).
	 * @param angle1
	 *            Second angle of sector (in radians).
	 * @return ComputationsGeometry of secor [x,y,...]
	 */
	public static int[] createSector(int x0, int y0, int dx, int dy,
			double angle0, double angle1) {
		// Determine a sensible number of points for arc
		double angleSpan = Math.abs(angle1 - angle0);
		double arcDistance = Math.max(dx, dy) * angleSpan;
		int nPoints = (int) Math.round(arcDistance / 15);
		double angleStep = angleSpan / (nPoints - 1);

		int[] xy = new int[nPoints * 2 + 4];

		int index = 0;
		for (int i = 0; i < nPoints; i++) {
			double angle = angle0 + angleStep * i;
			double x = dx * Math.cos(angle);
			double y = dy * Math.sin(angle);

			xy[index + 0] = x0 + (int) Math.round(x);
			xy[index + 1] = y0 - (int) Math.round(y);

			index += 2;
		}

		// Start and end geometry at center of ellipse to make it a closed
		// polygon
		xy[nPoints * 2 + 0] = x0;
		xy[nPoints * 2 + 1] = y0;
		xy[nPoints * 2 + 2] = xy[0];
		xy[nPoints * 2 + 3] = xy[1];

		return xy;
	}

	/**
	 * Create the geometry of a sector of a circle.
	 * 
	 * @param x0
	 *            X coordinate of center of ellipse.
	 * @param y0
	 *            Y coordinate of center of ellipse.
	 * @param dx
	 *            X radius of ellipse.
	 * @param dy
	 *            Y radius of ellipse.
	 * @param angle0
	 *            First angle of sector (in radians).
	 * @param angle1
	 *            Second angle of sector (in radians).
	 * @return ComputationsGeometry of secor [x,y,...]
	 */
	public static int[] createSector(int x0, int y0, int radius, double angle0,
			double angle1) {
		return createSector(x0, y0, radius, radius, angle0, angle1);
	}

	/**
	 * Create the geometry of an arrow. The arrow is positioned at the end (last
	 * point) of the specified polyline, as follows:
	 * 
	 * 0,4--, \ --, \ --, \ --, \ --, -------------------------3-----------1 /
	 * --' / --' / --' / --' 2--'
	 * 
	 * @param x
	 *            X coordinates of polyline of where arrow is positioned in the
	 *            end. Must contain at least two points.
	 * @param y
	 *            Y coordinates of polyline of where arrow is positioned in the
	 *            end.
	 * @param length
	 *            Length along the main axis from point 1 to the projection of
	 *            point 0.
	 * @param angle
	 *            Angle between the main axis and the line 1,0 (and 1,2) in
	 *            radians.
	 * @param inset
	 *            Specification of point 3 [0.0-1.0], 1.0 will put point 3 at
	 *            distance length from 1, 0.0 will put it at point 1.
	 * @return Array of the five coordinates [x,y,...].
	 */
	public static int[] createArrow(int[] x, int[] y, double length,
			double angle, double inset) {
		int[] arrow = new int[10];

		int x0 = x[x.length - 1];
		int y0 = y[y.length - 1];

		arrow[2] = x0;
		arrow[3] = y0;

		// Find position of interior of the arrow along the polyline
		int[] pos1 = new int[2];
		ComputationsGeometry.findPolygonPosition(x, y, length, pos1);

		// Angles
		double dx = x0 - pos1[0];
		double dy = y0 - pos1[1];

		// Polyline angle
		double v = dx == 0.0 ? Math.PI / 2.0 : Math.atan(Math.abs(dy / dx));

		v = dx > 0.0 && dy <= 0.0 ? Math.PI + v
				: dx > 0.0 && dy >= 0.0 ? Math.PI - v
						: dx <= 0.0 && dy < 0.0 ? -v
								: dx <= 0.0 && dy > 0.0 ? +v : 0.0;

		double v0 = v + angle;
		double v1 = v - angle;

		double edgeLength = length / Math.cos(angle);

		arrow[0] = x0 + (int) Math.round(edgeLength * Math.cos(v0));
		arrow[1] = y0 - (int) Math.round(edgeLength * Math.sin(v0));

		arrow[4] = x0 + (int) Math.round(edgeLength * Math.cos(v1));
		arrow[5] = y0 - (int) Math.round(edgeLength * Math.sin(v1));

		double c1 = inset * length;

		arrow[6] = x0 + (int) Math.round(c1 * Math.cos(v));
		arrow[7] = y0 - (int) Math.round(c1 * Math.sin(v));

		// Close polygon
		arrow[8] = arrow[0];
		arrow[9] = arrow[1];

		return arrow;
	}

	/**
	 * Create geometry for an arrow along the specified line and with tip at
	 * x1,y1. See general method above.
	 * 
	 * @param x0
	 *            X first end point of line.
	 * @param y0
	 *            Y first end point of line.
	 * @param x1
	 *            X second end point of line.
	 * @param y1
	 *            Y second end point of line.
	 * @param length
	 *            Length along the main axis from point 1 to the projection of
	 *            point 0.
	 * @param angle
	 *            Angle between the main axis and the line 1,0 (and 1.2)
	 * @param inset
	 *            Specification of point 3 [0.0-1.0], 1.0 will put point 3 at
	 *            distance length from 1, 0.0 will put it at point 1.
	 * @return Array of the four coordinates [x,y,...].
	 */
	public static int[] createArrow(int x0, int y0, int x1, int y1,
			double length, double angle, double inset) {
		int[] x = { x0, x1 };
		int[] y = { y0, y1 };

		return createArrow(x, y, length, angle, inset);
	}

	/**
	 * Create geometry for a rectangle. Returns a closed polygon; first and last
	 * points matches. Integer domain.
	 * 
	 * @param x0
	 *            X corner of rectangle.
	 * @param y0
	 *            Y corner of rectangle.
	 * @param width
	 *            Width (may be negative to indicate leftwards direction)
	 * @param height
	 *            Height (may be negative to indicaten upwards direction)
	 */
	public static int[] createRectangle(int x0, int y0, int width, int height) {
		return new int[] { x0, y0, x0 + (width - 1), y0, x0 + (width - 1),
				y0 + (height - 1), x0, y0 + (height - 1), x0, y0 };
	}

	/**
	 * Create geometry for a rectangle. Returns a closed polygon; first and last
	 * points matches. Floating point domain.
	 * 
	 * @param x0
	 *            X corner of rectangle.
	 * @param y0
	 *            Y corner of rectangle.
	 * @param width
	 *            Width (may be negative to indicate leftwards direction)
	 * @param height
	 *            Height (may be negative to indicaten upwards direction)
	 */
	public static double[] createRectangle(double x0, double y0, double width,
			double height) {
		return new double[] { x0, y0, x0 + width, y0, x0 + width, y0 + height,
				x0, y0 + height, x0, y0 };
	}

	/**
	 * Create geometry of a star. Integer domain.
	 * 
	 * @param x0
	 *            X center of star.
	 * @param y0
	 *            Y center of star.
	 * @param innerRadius
	 *            Inner radis of arms.
	 * @param outerRadius
	 *            Outer radius of arms.
	 * @param nArms
	 *            Number of arms.
	 * @return ComputationsGeometry of star [x,y,x,y,...].
	 */
	public static int[] createStar(int x0, int y0, int innerRadius,
			int outerRadius, int nArms) {
		int nPoints = nArms * 2 + 1;

		int[] xy = new int[nPoints * 2];

		double angleStep = 2.0 * Math.PI / nArms / 2.0;

		for (int i = 0; i < nArms * 2; i++) {
			double angle = i * angleStep;
			double radius = (i % 2) == 0 ? innerRadius : outerRadius;

			double x = x0 + radius * Math.cos(angle);
			double y = y0 + radius * Math.sin(angle);

			xy[i * 2 + 0] = (int) Math.round(x);
			xy[i * 2 + 1] = (int) Math.round(y);
		}

		// Close polygon
		xy[nPoints * 2 - 2] = xy[0];
		xy[nPoints * 2 - 1] = xy[1];

		return xy;
	}

	/**
	 * Create geometry of a star. Floating point domain.
	 * 
	 * @param x0
	 *            X center of star.
	 * @param y0
	 *            Y center of star.
	 * @param innerRadius
	 *            Inner radis of arms.
	 * @param outerRadius
	 *            Outer radius of arms.
	 * @param nArms
	 *            Number of arms.
	 * @return ComputationsGeometry of star [x,y,x,y,...].
	 */
	public static double[] createStar(double x0, double y0, double innerRadius,
			double outerRadius, int nArms) {
		int nPoints = nArms * 2 + 1;

		double[] xy = new double[nPoints * 2];

		double angleStep = 2.0 * Math.PI / nArms / 2.0;

		for (int i = 0; i < nArms * 2; i++) {
			double angle = i * angleStep;
			double radius = (i % 2) == 0 ? innerRadius : outerRadius;

			xy[i * 2 + 0] = x0 + radius * Math.cos(angle);
			xy[i * 2 + 1] = y0 + radius * Math.sin(angle);
		}

		// Close polygon
		xy[nPoints * 2 - 2] = xy[0];
		xy[nPoints * 2 - 1] = xy[1];

		return xy;
	}

	/**
	 * Return the x,y position at distance "length" into the given polyline.
	 * 
	 * @param x
	 *            X coordinates of polyline
	 * @param y
	 *            Y coordinates of polyline
	 * @param length
	 *            Requested position
	 * @param position
	 *            Preallocated to int[2]
	 * @return True if point is within polyline, false otherwise
	 */
	public static boolean findPolygonPosition(int[] x, int[] y, double length,
			int[] position) {
		if (length < 0)
			return false;

		double accumulatedLength = 0.0;
		for (int i = 1; i < x.length; i++) {
			double legLength = ComputationsGeometry.length(x[i - 1], y[i - 1],
					x[i], y[i]);
			if (legLength + accumulatedLength >= length) {
				double part = length - accumulatedLength;
				double fraction = part / legLength;
				position[0] = (int) Math.round(x[i - 1] + fraction
						* (x[i] - x[i - 1]));
				position[1] = (int) Math.round(y[i - 1] + fraction
						* (y[i] - y[i - 1]));
				return true;
			}

			accumulatedLength += legLength;
		}

		// Length is longer than polyline
		return false;
	}

	public static double[] getEllipseElements(double[] E) {
		double[] param = new double[5];
		double a, b, c, d, f, g;
		a = E[0];
		b = E[1] / 2.0;
		c = E[2];
		d = E[3] / 2.0;
		f = E[4] / 2.0;
		g = E[5];

		double A = 0, B = 0, x0 = 0, y0 = 0, theta = 0;

		double term, term1, term2, term3, term4, term5, term6, term7;

		x0 = (c * d - b * f) / (b * b - a * c);
		y0 = (a * f - b * d) / (b * b - a * c);
		theta = 0.5 * Math.atan((2.0 * b) / (c - a));

		//
		term = a * f * f + c * d * d + g * b * b;
		term1 = -1 * 2.0 * b * d * f + (-1 * a * c * g);
		term2 = (term + term1) * 2.0;

		term3 = b * b - a * c;
		term4 = a + c;
		term5 = (4 * b * b) / ((a - c) * (a - c));// (a-c)^2;
		term6 = Math.sqrt(1 + term5);
		//
		A = Math.sqrt(term2 / (term3 * ((term6 * (c - a)) - term4)));

		B = Math.sqrt(term2 / (term3 * ((term6 * (a - c)) - term4)));

		param[0] = A;
		param[1] = B;
		param[2] = x0;
		param[3] = y0;
		param[4] = theta;
		return param;
	}

	public static ArrayList<PointData> computeConvexHull(
			ArrayList<PointData> ink) {
	 
		// logger.info("
		// the ink drawn ");
		return quickHull(ink);
	}

	public static double computeArea(ArrayList ink) {
		// calculateSolutionParameters();
		double area = 0.0;
		double temperror;
		double a = 0.0;

		Point2D pi, pj;
		int vj = 0;
//		if (DrawingDebugUtils.DEBUG_GRAPHICALLY){
//		colorValue+=0.1;
//		 SolutionColor=Color.getHSBColor(0.4f, (float)colorValue, 0.5f);
//		// DrawingDebugUtils.getGraphics().setColor(SolutionColor);
	// paint(	DrawingDebugUtils.getGraphics() );
//		 DrawingDebugUtils.drawThickPointPath( 	DrawingDebugUtils.getGraphics()  , SolutionColor, SolutionColor, polygonVertices);
//
//}
		
		
		
		int v1index = 0, v2index = 0, pjindex = 0, vcount = 0;

		for (int i = 0; i < ink.size() - 1; i++) {
			// now i
			pi = (Point2D) ink.get(i);
			// {
			//pj = (Point2D) ink.get(i + 1);
			pj = (Point2D) ink.get(i+1 );
		
			
			
			a = (pi.getX() * pj.getY()) - (pj.getX() * pi.getY());
			area+=a;
//			if (debug){
//				if (DrawingDebugUtils.DEBUG_GRAPHICALLY){
//					
//					DrawingDebugUtils.drawLine(DrawingDebugUtils.getGraphics(),Color.MAGENTA, (PointData)ink.get(i),(PointData)ink.get(i+1 ));
//				}
////				logger.debug(" this loop  "+i+" a = "+a);
////				logger.debug(" total area is =  "+area);
//				
//			}
	

		}
		if (ink.size()>0){
			pi = (Point2D) ink.get(ink.size() - 1);
			// {
			//pj = (Point2D) ink.get(i + 1);
			pj = (Point2D) ink.get(0);
		
			
			a = (pi.getX() * pj.getY()) - (pj.getX() * pi.getY());
			area+=a;
		}
		// the final two points for close polygon 
		
//		if (debug)
//
//		logger.debug(" total area is =  "+area);
		area = area / 2.0;
		// TODO implements the area of ink gestures "it may not be a polygon )
		// logger.info("// TODO implements the area of ink gestures it
		// may not be a polygon )------------computation gemotry 1745");
		return area;
	}

	/*
	 * Copyright (c) 2007 Alexander Hristov. http://www.ahristov.com
	 * 
	 * Feel free to use this code as you wish, as long as you keep this
	 * copyright notice. The only limitation on use is that this code cannot be
	 * republished on other web sites.
	 * 
	 * As usual, this code comes with no warranties of any kind.
	 * 
	 * 
	 */

	static public ArrayList<PointData> quickHull(ArrayList<PointData> points) {
		ArrayList<PointData> convexHull = new ArrayList<PointData>();
		if (points.size() < 3)
			return (ArrayList) points.clone();
		// find extremals
		int minPointData = -1, maxPointData = -1;
		int minX = Integer.MAX_VALUE;
		int maxX = Integer.MIN_VALUE;
		for (int i = 0; i < points.size(); i++) {
			if (points.get(i).x < minX) {
				minX = (int) points.get(i).x;
				minPointData = i;
			}
			if (points.get(i).x > maxX) {
				maxX = (int) points.get(i).x;
				maxPointData = i;
			}
		}
		PointData A = points.get(minPointData);
		PointData B = points.get(maxPointData);
		convexHull.add(A);
		convexHull.add(B);
		points.remove(A);
		points.remove(B);

		ArrayList<PointData> leftSet = new ArrayList<PointData>();
		ArrayList<PointData> rightSet = new ArrayList<PointData>();

		for (int i = 0; i < points.size(); i++) {
			PointData p = points.get(i);
			if (pointLocation(A, B, p) == -1)
				leftSet.add(p);
			else
				rightSet.add(p);
		}
		hullSet(A, B, rightSet, convexHull);
		hullSet(B, A, leftSet, convexHull);

		return convexHull;
	}

	/*
	 * Computes the square of the distance of point C to the segment defined by
	 * points AB
	 */
	static public int distance(PointData A, PointData B, PointData C) {
		double ABx = B.x - A.x;
		double ABy = B.y - A.y;
		double num = ABx * (A.y - C.y) - ABy * (A.x - C.x);
		if (num < 0)
			num = -num;
		return (int) num;
	}

	static public void hullSet(PointData A, PointData B,
			ArrayList<PointData> set, ArrayList<PointData> hull) {
		int insertPosition = hull.indexOf(B);
		if (set.size() == 0)
			return;
		if (set.size() == 1) {
			PointData p = set.get(0);
			set.remove(p);
			hull.add(insertPosition, p);
			return;
		}
		int dist = Integer.MIN_VALUE;
		int furthestPointData = -1;
		for (int i = 0; i < set.size(); i++) {
			PointData p = set.get(i);
			int distance = distance(A, B, p);
			if (distance > dist) {
				dist = distance;
				furthestPointData = i;
			}
		}
		PointData P = set.get(furthestPointData);
		set.remove(furthestPointData);
		hull.add(insertPosition, P);

		// Determine who's to the left of AP
		ArrayList<PointData> leftSetAP = new ArrayList<PointData>();
		for (int i = 0; i < set.size(); i++) {
			PointData M = set.get(i);
			if (pointLocation(A, P, M) == 1) {
				leftSetAP.add(M);
			}
		}

		// Determine who's to the left of PB
		ArrayList<PointData> leftSetPB = new ArrayList<PointData>();
		for (int i = 0; i < set.size(); i++) {
			PointData M = set.get(i);
			if (pointLocation(P, B, M) == 1) {
				leftSetPB.add(M);
			}
		}
		hullSet(A, P, leftSetAP, hull);
		hullSet(P, B, leftSetPB, hull);

	}

	static public int pointLocation(PointData A, PointData B, PointData P) {
		double cp1 = (B.x - A.x) * (P.y - A.y) - (B.y - A.y) * (P.x - A.x);
		return (cp1 > 0) ? 1 : -1;
	}

	public static double computeTotalLength(ArrayList<PointData> hull) {

		double length = 0.0;

		for (int i = 0; i < hull.size() - 1; i++) {
			length += ComputationsGeometry.computeLength(hull.get(i), hull
					.get(i + 1));
		}
		return length;
	}

	public static double computeDeltaT(PointData p1, PointData p2) {

		double dt = p2.getTime() - p1.getTime();

		return dt;
	}

	@Deprecated
	public static double computeCurvature(PointData p1, PointData p2,
			PointData p3)
	// get the curvature equaiton
	{

		return 0.0;

	}

	public static double computeCurvatureHKAlg(PointData pb, PointData pi,
			PointData pf) {

		double lb = ComputationsGeometry.computeLength(pi, pb);
		double lf = ComputationsGeometry.computeLength(pi, pf);
		// compute angle in this funciton
		double thetab = 0.0;
		// thetab=computeAngle(pi, pb);
		double thetaf = 0.0;

		double dxb = Math.abs(pb.getX() - pi.getX());
		double dyb = Math.abs(pb.getY() - pi.getY());

//		if (dxb == 0) {
//			thetab = Math.atan(dxb / dyb);
//		} else {
//			thetab = Math.atan(dxb / dyb);
//		}
		
		// changed after the find bugs in 14 sep. 2010 also i used to match atan2 is better than atan
		if (dxb!=0 && dyb!=0)
			thetab=Math.atan2(dyb, dxb);
			else 
	thetab=0;
		// thetaf=computeAngle(pi, pf);

		double dxf = Math.abs(pf.getX() - pi.getX());
		double dyf = Math.abs(pf.getY() - pi.getY());

//		if (dxf == 0) {
//			thetaf = Math.atan(dxf / dyf);
//
//		} else {
//			thetaf = Math.atan(dxf / dyf);
//		}

		
		if (dxf!=0 && dyf!=0)
		thetaf=Math.atan2(dyf, dxf);
		else 
			thetaf=0;
		double thetai = thetab / 2.0 + thetaf / 2.0;
		double df = Math.abs(thetaf - thetai);
		double db = Math.abs(thetab - thetai);

		double e = ((df) / (2 * lf)) + ((db) / (2 * lb));

		return e;
	}

	public static double computeCurvature(PointData[] p) {

		// if the nighbours size = 5 then the array size is 11 +2 =13
		int k = ((p.length - 2) / 2);
		// now i can get the size k=0
		//PointData pi = p[k + 1];
		Point2D bp;//,fb
	//	fp = ComputationsGeometry.computeVector(p[k + 1], p[k + 1 + k]);
		bp = ComputationsGeometry.computeVector(p[k + 1], p[1]);

		//double thetai = ComputationsGeometry.computeTheta(bp);

		bp = ComputationsGeometry.computeVector(p[k + 2], p[p.length - 1]);
		double thetai1 = ComputationsGeometry.computeTheta(bp);// i+1
		bp = ComputationsGeometry.computeVector(p[k], p[0]);
		double thetai_1 = ComputationsGeometry.computeTheta(bp);// i-1
		double cur = thetai1 - thetai_1;

		// get the middle now
		return cur;
	}

	/**
	 * this is using tehe algorith fd 1977 in the ccurvatue estimation
	 * presentation
	 * 
	 * @param pi_1
	 * @param pi1
	 * @param p_1_k
	 * @param pi1_k
	 * @return
	 */
	public static double computeCurvatureAlgFD(PointData pi_1, PointData pi1,
			PointData p_1_k, PointData pi1_k) {

		// if the nighbours size = 5 then the array size is 11 +2 =13

		Point2D bp;
		// fp=computeVector(p[k+1],p[k+1+k]);

		// p i-1 pi-1-k
		bp = ComputationsGeometry.computeVector(pi1, pi1_k);
		double thetai1 = ComputationsGeometry.computeTheta(bp);// i+1

		bp = ComputationsGeometry.computeVector( p_1_k,pi_1);  //chaned after debug to correct sign. 
		double thetai_1 = ComputationsGeometry.computeTheta(bp);// i-1

		double cur = thetai1 - thetai_1;

		// get the middle now
		return cur;
	}

	/**
	 * @param p1
	 * @param p2
	 * @return
	 */
	public static double computeAngleVectors(Point2D p1, Point2D p2) {

		// get the the dot product of the two vectors
		double dotprod = ComputationsGeometry.computeDotProductVector(p1, p2);
		// get norms of both points or vectors
		double normp1, normp2;
		normp1 = ComputationsGeometry.normVector(p1);
		normp2 = ComputationsGeometry.normVector(p2);
		// not get the arcos of the given angle

		// cos theta == a.b/||a||*||b||

		return Math.acos(dotprod / (normp1 * normp2)); // check if rad or
														// degree
		// if

		// return ;

	}

	public static double computeAngle(Point2D p1, Point2D p2) {
		double slope = ComputationsGeometry.computeSlope(p1, p2);
		double angle = Math.atan(slope);
		return angle;
	}

	public static double computeAngle(PointData p1, PointData p2) {
		double slope = ComputationsGeometry.computeSlope(p1.getPointLocation(),
				p2.getPointLocation());
		double angle = Math.atan(slope);
		return angle;
	}

	public static double computeChangeRotation(PointData p1, PointData p2,
			PointData p3) {

		double dx, dy, dx_1, dy_1;

		dx = p3.getX() - p2.getX(); // delta xp = x(p+1) - xp
		dy = p3.getY() - p2.getY();

		dx_1 = p2.getX() - p1.getX(); // delta x(p-1) = x(p) - x(p-1)
		dy_1 = p2.getY() - p1.getY();

		double term1 = (dx * dy_1) - (dx_1 * dy);
		double term2 = (dx * dx_1) + (dy * dy_1);

		// tan 0 = sin0 /cos0 if 0 = tan -1(sin0 /cos0 ) // cos 0= 0

		if (term2 == 0)
			return Math.acos(term2);
		return Math.atan(term1 / term2);
		// return 0;
	}

	/**
	 * compute the length between two points
	 * 
	 * @param p1
	 *            first point
	 * @param p2
	 *            second point
	 * @return
	 */
	public static double computeLength(Point2D p1, Point2D p2) {

		// calculating length sqrt(y2 -x2)
		double deltax = p2.getX() - p1.getX();
		double deltay = p2.getY() - p1.getY();
		double distance = Math.sqrt((deltax * deltax) + (deltay * deltay));

		return distance;
	}

	/**
	 * compute the length between two points
	 * 
	 * @param p1
	 *            first point
	 * @param p2
	 *            second point
	 * @return
	 */
	public static double computeLength(PointData p1, PointData p2) {

		// calculating length sqrt(y2 -x2)
		double deltax = p2.getX() - p1.getX();
		double deltay = p2.getY() - p1.getY();
		double distance = Math.sqrt((deltax * deltax) + (deltay * deltay));

		return distance;
	}
	public static double computenorm(Point2D p1) {

		// calculating length sqrt(y2 -x2)
		double dx = p1.getX() * p1.getX();
		double dy = p1.getY() *p1.getY();
		double distance = Math.sqrt(dx+dy);

		return distance;
	}

	public static double computeVelocity(PointData p1, PointData p2) {
		double length = computeLength(p1, p2);
		double deltaT = computeDeltaT(p1, p2);
		double speed = length / deltaT;
		return speed;
	}
	public static double computeDirectionXaxis(PointData p1, PointData p2,
			PointData p3) {
		Point2D p1p2 = computeVector(p1, p2);
		PointData xorgin=new PointData(0,p2.y);
        Point2D  xaxis=computeVector(p2, xorgin);
		//Point2D p2p3 = computeVector(p2, p3);
		double angle = computeAngleVectors(p1p2, xaxis);

		// / now compute the angle between this two lines

		return angle;
		 
	}
	public static double computeDirection(PointData p1, PointData p2,
			PointData p3)
	// get the direction equation

	{
		Point2D p1p2 = computeVector(p1, p2);

		Point2D p2p3 = computeVector(p2, p3);
		double angle = computeAngleVectors(p1p2, p2p3);

		// / now compute the angle between this two lines

		return angle;
		// get first the vecter betwwwn p2and pi

	}

	public static Point2D computeVector(PointData p1, PointData p2) {
	//	PointData p = new PointData();
		double x = p2.getX() - p1.getX();
		double y = p2.getY() - p1.getY();
		return new Point2D.Double(x, y);

		// return null;
	}

	public static double computeTheta(Point2D v) {
		// this is modefied due to this page in the web 
		
		//http://en.wikipedia.org/wiki/Atan2
		// which define the state of theta and the type of states in it. 
		
		if (v.getX()==0 && v.getY()==0)
			return 0;
		else 
		return Math.atan2(v.getY(),v.getX());
//		if (Math.abs(v.getX()) > Math.abs(v.getY())) {
//			return Math.atan(v.getY() / v.getX());
//		} else {
//			return Math.atan(v.getX() / v.getY());
//		}
	}

	// public static double compute
	/**
	 * @param p1
	 * @param p2
	 * @return
	 */
	public static double computeSlope(PointData p1, PointData p2) {
		// calcuating slop delta y /delta x
		double deltax = p2.getX() - p1.getX();
		double deltay = p2.getY() - p1.getY();
		return deltay / deltax;
	}

	/**
	 * @param p1
	 * @param p2
	 * @return
	 */
	public static double computeSlope(Point2D p1, Point2D p2) {
		double deltax = p2.getX() - p1.getX();
		double deltay = p2.getY() - p1.getY();
		return deltay / deltax;
	}

	/**
	 * @param p1
	 * @param p2
	 * @return
	 */
//	private static double computeDotProductVector(Point p1, Point p2) {
//		return p1.x * p2.x + p1.y * p2.y;
//
//	}

	public static double computeDotProductVector(Point2D p1, Point2D p2) {
		return p1.getX() * p2.getX() + p1.getY() * p2.getY();

	}

//	private static double normVector(Point p) {
//		return Math.sqrt(p.x * p.x + p.y * p.y);
//
//	}

	public static double normVector(Point2D p) {
		return Math.sqrt(p.getX() * p.getX() + p.getY() * p.getY());

	}

	@Deprecated
	public static Vector[] computeStrokeFirstDiravative(Stroke stroke) {
		double dx, dy;

		PointData p1, p2;
		// get x derivative and y derivative and save them in points
		Vector p;
      int noPoints=stroke.getPointsCount();
		// Vector data;
		Vector[] V = new Vector[noPoints - 1];

		for (int i = 0; i < noPoints - 1; i++) {
			p1 = stroke.getPoint(i);
			p2 = stroke.getPoint(i + 1);
			dx = p2.getX() - p1.getX();
			dy = p2.getY() - p1.getY();

			// p=
			V[i] = new Vector(dx, dy, i);

		}// for
		return V;
	}

	@Deprecated
	public static Vector[] computeStrokeSecondDiravative(Vector[] V) {

		double dx, dy;
		Vector[] V2 = new Vector[V.length - 1];
		for (int i = 0; i < V.length - 1; i++) {
			dx = V[i + 1].getX() - V[i].getX();
			dy = V[i + 1].getY() - V[i].getY();
			V2[i] = new Vector(dx, dy, V[i].getZ());
		}
		return V2;

	}

	public static double cos(PointData data, PointData data2) {

		double x0 = data.getX();
		double y0 = data.getY();
		double x2 = data2.getX();
		double y2 = data2.getY();

		return (x2 - x0)
				/ Math.sqrt(((y2 - y0) * (y2 - y0)) + ((x2 - x0) * (x2 - x0)));

	}

	public static double sin(PointData data, PointData data2) {
		double x0 = data.getX();
		double y0 = data.getY();
		double x2 = data2.getX();
		double y2 = data2.getY();

		return (y2 - y0)
				/ Math.sqrt(((y2 - y0) * (y2 - y0)) + ((x2 - x0) * (x2 - x0)));
	}

	public static double computeSpeed(PointData p1, PointData p2) {

		double dx = p2.getX() - p1.getX(); // delta xp = x(p+1) - xp
		double dy = p2.getY() - p1.getY();
		double dt = p2.getTime() - p1.getTime();
		return ((dx * dx) + (dy * dy)) / (dt * dt);
		// return 0;
	}

	public static PointData computeTangent(PointData point, PointData point2,
			PointData pointLength) {
		//logger.info("	
		
		// TODO  Compute how get the tangent point ---------check it i implemented need to be tested. --sketch libe 405 ");
	
		// get the slope

		// slope
		double dx = point2.x - point.x;
		double dy = point2.y - point.y;
		double y;
		double x;
		double m=0;

		if (dx == 0) {
//			logger.info("condition 1 dx=0  so "+" (" 
//					+ "    "
//					+ (new Throwable()).getStackTrace()[0].getLineNumber()
//					+ "  )  ");
			// slope is nan then the line is horizontal
			// parralel to x y = pint.y
                 
			y = pointLength.y;
			x = point.x;
		} else if (dy == 0) {
//			logger.info("condition 2 dy=0"+" (" 
//					+ "    "
//					+ (new Throwable()).getStackTrace()[0].getLineNumber()
//					+ "  )  ");
			
			y = point.y;
			x = pointLength.x;
		} else {

		m = dy / dx;

			
//			logger.info("third condiotin m= "+m+" (" + 
//					 "    "
//					+ (new Throwable()).getStackTrace()[0].getLineNumber()
//					+ "  )  ");
			// logger.info(" slope of tangent = "+m);
			// get point in the line of slope
			y = (m * (pointLength.x - point.x)) + point.y;
			x = pointLength.x;

		}

		PointData p = new PointData();
		p.setLocation(x, y);

		double l = computeLength(p, point);
		 //logger.info(" l=    "+l);

		if (l == 0.0) {
			if (!Double.isInfinite(m))
			{
                      /// subsitute with x=0  and find the point 
			y = m*(-point.x)+point.y;
			x = 0;
			}
			else {
				y = 0;
				x = point.x;
			}
			
			p = new PointData();
			p.setLocation(x, y);
			l = computeLength(point, p);

		}
//		logger.info(" point  "+point+"    p = "+p+" (" 
//				+ "    " + (new Throwable()).getStackTrace()[0].getLineNumber()
//				+ "  )  ");

		Point2D v = computeVector(point, p);
//          logger.info( "V"+v+" (" 
//				+ "    " + (new Throwable()).getStackTrace()[0].getLineNumber()
//				+ "  )  ");
		PointData u = new PointData();
		u.setLocation(v.getX() / l, v.getY() / l);
		//logger.info(" u "+u);

		return u;
	}
 
	
	public static Rectangle2D computeBoundingBox(ArrayList<PointData>  points){
		Rectangle2D box;
		if (points!=null){
			if (points.size()>0){
			PointData point = points.get(0);
			box = new Rectangle2D.Double(point.getPointLocation().getX(), point
					.getPointLocation().getY(), 0, 0);
			for (int i = 1; i < points.size() - 1; i++) {
				
				box.add(points.get(i).getPointLocation());
				}
	 
			return box;
			}
		}
			Rectangle2D returnRectangle2D = box = new Rectangle2D.Double(0, 0, 0, 0);
		return 	returnRectangle2D;
		}

   public static double DistancePointLine(Point2D point, Point2D x1, Point2D x2){
	   
		double distance = x1.distance(x2);
	return	 DistancePointLine( point,  x1,x2,distance);
   }
	public static double DistancePointLine(Point2D point, Point2D x1, Point2D x2,
			double LineMag)
	// XYZ *Point, XYZ *LineStart, XYZ *LineEnd, float *Distance )
	{

	 
		//
		// double LineMag;
		double U;

		// XYZ Intersection;

		// LineMag = Magnitude( LineEnd, LineStart );

		if (LineMag==0){
			LineMag=1;
			// the points x1 and x2 are nearly over each other or they may be over each other
		}
		
		U = (double) (((point.getX() - x1.getX()) * (x2.getX() - x1.getX())) + ((point
				.getY() - x1.getY()) * (x2.getY() - x1.getY())))
				/ (LineMag * LineMag);

//		if (U < 0.0f || U > 1.0f) {
//			//logger.info("-----------------------------------------------problem");
//			return 0.0; // closest point does not fall within the line
//								// segment
//
//		}
         
		
		double X = (double) x1.getX() + U * (x2.getX() - x1.getX());
		double Y = (double) x1.getY() + U * (x2.getY() - x1.getY());
		Point2D intersection = new Point2D.Double();
		intersection.setLocation(X, Y);
		// Intersection.Z = LineStart->Z + U * ( LineEnd->Z - LineStart->Z );

		
		double Distance = point.distance(intersection);
		// double Distance = Magnitude( point, intersection );
	

		// logger.info("the line lenthg is "+LineMag+" the final distance
		// is"+Distance);
		return Distance;
	}

	
	 
	private double Magnitude(Point x1, Point x2)
	// ( XYZ *Point1, XYZ *Point2 )
	{
		// XYZ Vector;

		double X = x2.getX() - x1.getX();
		double Y = x2.getY() - x1.getY();
		// double Z = Point2->Z - Point1->Z;

		return Math.sqrt(X * X + Y * Y);
	}
	
	
	private static double Area2(PointData a, PointData b,PointData c ){
		
		double area= (b.x-a.x)*(c.y-a.y)-(c.x-a.x)*(b.y-a.y);
		return area;
		
	} 
//	private static double Area2(Point a, Point b,Point c ){
//		
//		double area= (b.x-a.x)*(c.y-a.y)-(c.x-a.x)*(b.y-a.y);
//		return area;
//		
//	} 
	public static boolean Left(PointData a, PointData b,PointData c){
		
	  	
		return Area2(a,b,c)>=0; 
	}
//	public static boolean Left(Point a, Point b,Point c){
//		
//	  	
//		return Area2(a,b,c)>=0; 
//	}
	public static boolean Collinear (PointData a, PointData b,PointData c){
		
		double range=SystemSettings. collinearRange;
		double test=Math.abs(Area2(a,b,c));
		//logger.info("  colinear test  = "+test);
		return test<range;
		//return Area2(a,b,c)==0; 
	} 
	public static boolean Collinear (PointData a, PointData b,PointData c,double range){
		

		return Math.abs(Area2(a,b,c))<range;
		//return Area2(a,b,c)==0; 
	} 
	public static  boolean IntersectProp (PointData a, PointData b,PointData c,PointData d){
		
		/**
		 * eliminate improper case 
		 * **/
		
		if (Collinear(a, b, c)||Collinear(a, b, d)||Collinear(c, d, a)||Collinear(c, d, b)){
			
			return false;
		}
		
		return xor( Left(a, b, c),Left(a, b, d)) &&xor( Left(c, d, a),Left(c, d, b));
		
	} 
	
	
	private static boolean xor( boolean x, boolean y ){
		
       return !x ^ !y;		
	}
	
	
	private static boolean Between(PointData a, PointData b,PointData c){
		PointData ba,ca;
		
		if (!Collinear(a, b, c))
			return false;
		
		if (a.x!=b.x){
			
			
		return ((a.x <=c.x)&&(c.x<=b.x))||((a.x>=c.x)&&(c.x>=b.x));	
		}
		else 
		{
			
			return ((a.y <=c.y)&&(c.y<=b.y))||((a.y>=c.y)&&(c.y>=b.y));	
		}
	}
//	public static boolean Intersect (Line l1, Line l2){
//		
//		return Intersect(l1.getStartPoint(), l1.getEndPoint(), l2.getStartPoint(), l2.getEndPoint());
//	}
	
	public static boolean Intersect (PointData a, PointData b,PointData c,PointData d ){
		
		if (IntersectProp(a, b, c, d))
		return true;
		else 
		{
			if (Between(a, b, c)||Between(a, b, d)|| Between(c, d, a)||Between(c, d, b))
				return true;
		}
		 
		return false;
	}
	 
	public static double computeDeltaYoverX(PointData prev, PointData current,
			PointData next) {
		 
		return computeSlope(prev,next);
	}


	
}
