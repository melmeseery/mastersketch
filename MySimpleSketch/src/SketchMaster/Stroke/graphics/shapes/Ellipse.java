package SketchMaster.Stroke.graphics.shapes;
import java.awt.Graphics;
import java.util.ArrayList;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import SketchMaster.Stroke.StrokeData.PointData;
import SketchMaster.lib.ComputationsGeometry;

public class Ellipse extends GeometricPrimitive implements GuiShape {
	double x, y, xc, yc, a, b;
	

	public void paint(Graphics2D g) {
		g.drawOval((int) x, (int) y, (int) a, (int) b);
		super.paint(g);
	}

	@Deprecated
	public void setParam(ArrayList Param) {

		// this.param = param;
	}

	
	public double DifferanceFromPoint(PointData point){
		
		//x y is the intersection and xx,yy is the point 
		////x = a*b*xx/sqrt((b*xx)2 + (a*yy)2)
		///y = a*b*yy/sqrt((b*xx)2 + (a*yy)2)|
		//distance betweeoin point and center suptracted from teh radius. 
		
		//double distance=ComputationsGeometry.computeLength(point,   centerpoint);
		
		double temp=0;
		
	//	temp=Math.sqrt(  (distance-r)* (distance-r)   );
		
	     return temp;	
	
	}
	
	public void setEllipseParam(double x0, double y0, double ellipse_a,
			double ellipse_b) {
		xc = x0;
		yc = y0;
		a = ellipse_a;
		b = ellipse_b;
		x = xc - a;
		y = yc - b;
	}

	public double  getLargestRadius() {
		// TODO Auto-generated method stub
		if (a>=b)
			return a;
		else return b;
	}

}
