/**
 * 
 */
package SketchMaster.Stroke.graphics.shapes;
import java.awt.Graphics2D;
import java.io.Serializable;
import java.util.ArrayList;

import SketchMaster.Stroke.StrokeData.PointData;

/**
 * @author maha
 * 
 */
public class ComplexPolygon extends GeometricPrimitive {
	public void paint(Graphics2D g) {
           super.paint(g);
	}

	public void setParam(ArrayList Param) {

		// this.param = param;
	}

	@Override
	public double DifferanceFromPoint(PointData point) {
		  
		return 0;
	}
}
