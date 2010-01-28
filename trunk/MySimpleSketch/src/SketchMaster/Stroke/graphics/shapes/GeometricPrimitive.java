/**
 * 
 */
package SketchMaster.Stroke.graphics.shapes;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.ArrayList;

import SketchMaster.Stroke.StrokeData.PointData;

/**
 * @author maha
 * 
 */
public abstract class GeometricPrimitive implements GuiShape, Serializable {
	private double ErrorComputed=0.0;
	
	PointData StartPoint,EndPoint;
	int iStart,iEnd;
	public  void paint(Graphics2D g){
		Point2D pointS = getStartPoint();
		Point2D pointe=	getEndPoint();
		if (	pointS!=null){
		g.setColor(Color.BLUE);
		g.drawRect((int)pointS.getX(),(int)pointS.getY(),4,4);
		g.fillRect((int)pointS.getX(),(int)pointS.getY(),3,3);
		}
		if (pointe!=null)
		{
		g.setColor(Color.RED);
		g.drawRect((int)pointe.getX(),(int)pointe.getY(),6,6);
		g.fillRect((int)pointe.getX(),(int)pointe.getY(),5,5);
		}
	}

	public abstract void setParam(ArrayList Param);
//
//	abstract public Point2D getStartPoint();
//
//	abstract public Point2D getEndPoint();

	public abstract double DifferanceFromPoint(PointData point);
	
	public Point2D getEndPoint() {
		
		return EndPoint;
	}

	public Point2D getStartPoint() {
		
		return StartPoint;
	}

	
	public void setStartPoint(PointData p) {
		
//		System.out.println("setting the start point of the class "+" (" + this.getClass().getSimpleName()
//				+ "    " + (new Throwable()).getStackTrace()[0].getLineNumber()
//				+ "  )  ");
			StartPoint=p;
	}

	public void setEndPoint(PointData p) {
//	        System.out.println(" setting the end point  "+" (" + this.getClass().getSimpleName()
//					+ "    "
//					+ (new Throwable()).getStackTrace()[0].getLineNumber()
//					+ "  )  ");
		EndPoint=p;
		
	}

	/**
	 * @return the errorComputed
	 */
	public double getErrorComputed() {
		return ErrorComputed;
	}

	/**
	 * @param errorComputed the errorComputed to set
	 */
	public void setErrorComputed(double errorComputed) {
		ErrorComputed = errorComputed;
	}

	/**
	 * @return the iStart
	 */
	public int getIStart() {
		return iStart;
	}

	/**
	 * @param start the iStart to set
	 */
	public void setIStart(int start) {
		iStart = start;
	}

	/**
	 * @return the iEnd
	 */
	public int getIEnd() {
		return iEnd;
	}

	/**
	 * @param end the iEnd to set
	 */
	public void setIEnd(int end) {
		iEnd = end;
	}

}
