package SketchMaster.Stroke.StrokeData;

import org.apache.log4j.Logger;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Point2D.Double;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;



import SketchMaster.Stroke.graphics.shapes.GeometricObject;
import SketchMaster.Stroke.graphics.shapes.PolygonShape;
//import SketchMaster.Stroke.graphics.shapes.Polygon;
import SketchMaster.swarm.polygonApproximations.PolygonSolution;

/**
 * @author Mahi
 */
public class PointData extends Point2D.Double implements Serializable,GeometricObject, Comparable<PointData> {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(PointData.class);

	public PointData() {
		super();
        magComputed=false;
	}

	public PointData(double arg0, double arg1) {
		super(arg0, arg1);
        magComputed=false;
	}
	
	public PointData(double x,double y , long t, double p){
		this(x,y);
		this.setTime(t);
		this.setPresureValue(p);
		
		
		
	}

	public PointData(Point2D f) {
		super(f.getX(),f.getY());
        magComputed=false;
	}

	@Override
	public Object clone() {
		PointData temp = null;
		temp = (PointData) super.clone();
		temp.time = time;
		temp.startPoint = this.startPoint;
		temp.endPoint = this.endPoint;
		temp.CompulativeLength = CompulativeLength;
		temp.presureValue = this.presureValue;
		temp.controlPoint=this.controlPoint;
		temp.magComputed=false;

		// tempSolution.polygonVertices=(ArrayList<Point2D>)this.polygonVertices.clone();
		return temp;

	}
	
	private void writeObject(ObjectOutputStream os) throws IOException {
		try {
			os.defaultWriteObject();
		
			
//			os.writeBoolean(this.endPoint);
//			os.writeBoolean( this.startPoint);
//			os.writeBoolean(this.controlPoint);
//			os.writeLong(this.time);
			os.writeDouble(this.x);
			os.writeDouble(this.y);
//			os.writeDouble(this.presureValue);
//			os.writeDouble(this.CompulativeLength);
		
	
			
		
		} catch (Exception e) {
			logger.error("writeObject(ObjectOutputStream)", e); //$NON-NLS-1$
		}
	}
 
	private void readObject(ObjectInputStream is) throws IOException,
			ClassNotFoundException {
		try {
			is.defaultReadObject();
//			PointData point=new PointData();
//			point.endPoint=is.readBoolean();
//			point.startPoint=is.readBoolean( );
//			point.controlPoint=is.readBoolean();
//			point.time=is.readLong();
			this.x=is.readDouble();
			this.y=is.readDouble();
	        magComputed=false;
//			point.presureValue=is.readDouble();
//			point.CompulativeLength=is.readDouble();
		} catch (Exception e) {
			logger.error("readObject(ObjectInputStream)", e); //$NON-NLS-1$
		}
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 364867548436996057L;
	/**
	 * @uml.property name="time"
	 */
	protected long time = 0;
	/**
	 * @uml.property name="pointLocation"
	 */
	// private Point2D pointLocation=null;
	/**
	 * @uml.property name="startPoint
	 * 
	 */

	private boolean startPoint = false;
	/**
	 * @uml.property name="endPoint"
	 */
	private boolean endPoint = false;
	private boolean controlPoint = false;
	/**
	 * @uml.property name="presureValue"
	 */
	private double presureValue = 0.0;
	private double CompulativeLength = 0.0;
    private double Direction=0;
    private double mag;
    
    private boolean magComputed=false;
	private boolean	overTrace=false;
	private boolean	Deleted=false;
    
    
    
	/**
	 * @return the time
	 * @uml.property name="time"
	 */
	public long getTime() {
		return time;
	}

	/**
	 * @param time
	 *            the time to set
	 * @uml.property name="time"
	 */
	public void setTime(long time) {
		this.time = time;

	}

	/**
	 * @return the pointLocation
	 * @uml.property name="pointLocation"
	 */
	public Point2D getPointLocation() {
		return this;
	}

	/**
	 * @param pointLocation
	 *            the pointLocation to set
	 * @uml.property name="pointLocation"
	 */
	public void setPointLocation(Point2D pointLocation) {
		this.setLocation(pointLocation.getX(), pointLocation.getY());
	}

	/**
	 * @return the startPoint
	 * @uml.property name="startPoint"
	 */
	public boolean isStartPoint() {
		return startPoint;
	}

	/**
	 * @return the endPoint
	 * @uml.property name="endPoint"
	 */
	public boolean isEndPoint() {
		return endPoint;
	}

	/**
	 * @return the presureValue
	 * @uml.property name="presureValue"
	 */
	public double getPresureValue() {
		return presureValue;
	}

	/**
	 * @param presureValue
	 *            the presureValue to set
	 * @uml.property name="presureValue"
	 */
	public void setPresureValue(double presureValue) {
		this.presureValue = presureValue;
	}

	public String toString() {

		String s = " X = " + getX() + "   Y = " + getY() + "  Time = " + time;
		return s;

	}

	/**
	 * @return the criticalPoint
	 * @uml.property name="criticalPoint"
	 */
	public boolean isControlPoint() {
		return controlPoint;
	}

	/**
	 * @param criticalPoint
	 *            the criticalPoint to set
	 * @uml.property name="criticalPoint"
	 */
	public void setControlPoint(boolean ControlPoint) {
		this.controlPoint = ControlPoint;
	}

	/**
	 * @return the compulativeLength
	 * @uml.property name="compulativeLength"
	 */
	public double getCompulativeLength() {
		return CompulativeLength;
	}

	/**
	 * @param compulativeLength
	 *            the compulativeLength to set
	 * @uml.property name="compulativeLength"
	 */
	public void setCompulativeLength(double compulativeLength) {
		CompulativeLength = compulativeLength;
	}

	/**
	 * @param endPoint
	 *            the endPoint to set
	 * @uml.property name="endPoint"
	 */
	public void setEndPoint(boolean endPoint) {
		this.endPoint = endPoint;
	}

	/**
	 * @param startPoint
	 *            the startPoint to set
	 * @uml.property name="startPoint"
	 */
	public void setStartPoint(boolean startPoint) {
		this.startPoint = startPoint;
	}

	public boolean isSamePoint(Point p) {
		if (this.getX() == p.getX() && this.getY() == p.getY())
			return true;
		return false;
	}
	public boolean isSamePoint(PointData p) {
		if (this.getX() == p.getX() && this.getY() == p.getY())
			return true;
		return false;
	}

	public boolean isNearPoint(Point p, double range) {
		if (isSamePoint(p))// check if have same cordinates,

			return true; // then return true both points are in range
		else {
			// not the same point
			// compare the distance between two points with the range given
			// if they lay inside this range then they are near point.
			if (this.distance(p) < range) {
				return true;
			}
		}
		return false;
	}

	public void addPoint(PointData p) {
		this.x += p.getX();
		this.y += p.getY();
        magComputed=false;
	}

	public void mulScaler(double s) {
		this.x *= s;
		this.y *= s;
        magComputed=false;
	}
	  public double getAngle(Point2D p)
	    {
	        double ydiff = p.getY() - getY();
	        double xdiff = p.getX() - getX();
		double returndouble = Math.atan2(ydiff, xdiff);
	        return returndouble;
	    }

	//   private Polygon points;

//	  public Polygon getDataPoints()
//	    {
//	        return points;
//	    }

	    public void rotate(double radians)
	    {
	        double radius = Math.sqrt(x * x + y * y);
	        double angle = Math.atan2(y, x) + radians;
	        x = (int)(Math.cos(angle) * radius);
	        y = (int)(Math.sin(angle) * radius);
	        
	        magComputed=false;
	    }

//	    public void translate(double dx, double dy)
//	    {
//	        x += dx;
//	        y += dy;
//	        if(points != null)
//	            points.translate(dx, dy);
//	    }

	 

	    public void scale(double scale)
	    {
	        x *= scale;
	        y *= scale;
	        magComputed=false;
	    }

	    public double magnitude()
	    {
	    	if (magComputed)
	    		return mag;
	    	else {
	           mag = Math.sqrt(x * x + y * y);
	           magComputed=true;
	    	}
	        return mag;
	    }

	    public double distance(Point p)
	    {
		double returndouble = Math.sqrt((x - p.x) * (x - p.x) + (y - p.y) * (y - p.y));
	        return returndouble;
	    }

	    public static void paint(Point points[], Graphics g)
	    {
	        for(int i = 0; i < points.length - 1; i++)
	            g.drawLine((int)points[i].x, (int)points[i].y, (int)points[i + 1].x, (int)points[i + 1].y);
	    }

	    public static Point[] arrayListToPoints(ArrayList list)
	    {
	        Point result[] = new Point[list.size()];
	        for(int i = 0; i < result.length; i++)
	            result[i] = (Point)list.get(i);
	        return result;
	    }

//	    public GeometricObject copy()
//	    {
//	        PointData point = new PointData(x, y);
//	        if(points != null)
//	            point.points = (Polygon)points.copy();
//	        point.time = time;
//	        point.presureValue = presureValue;
//	        point.startPoint =startPoint;
//	        return point;
//	    }

	    public boolean pointIsOn(Point point, double radius)
	    {
		boolean returnboolean = distance(x, y) < radius;
	        return returnboolean;
	    }

//	    public boolean pointIsOnOriginal(PointData p, double radius)
//	    {
//	        return points.pointIsOn(p, radius);
//	    }
//	    public boolean touches(GeometricObject object)
//	    {
//	        return getPolygonalBounds().touches(object.getPolygonalBounds());
//	    }

	    public double getAngle(PointData p)
	    {
	        double ydiff = p.getY() - getY();
	        double xdiff = p.getX() - getX();
		double returndouble = Math.atan2(ydiff, xdiff);
	        return returndouble;
	    }

//	    public boolean containsGeometricObject(GeometricObject object)
//	    {
//	        return getPolygonalBounds().containsGeometricObject(object);
//	    }
//
//	    public Rectangle2D getRectangularBounds()
//	    {
//	        return new Rectangle2D.Double(x - 1.0D, y - 1.0D, 2D, 2D);
//	    }

	    public java.awt.Rectangle getBounds()
	    {
		java.awt.Rectangle returnRectangle = new java.awt.Rectangle((int) x - 1, (int) y - 1, 2, 2);
	        return returnRectangle;
	    }

	    
	    
	    
	    
	    
	    
public boolean containsGeometricObject(GeometricObject geometricobject) {
	  
	return false;
}

public boolean containsGeometricObjects(GeometricObject[] ageometricobject) {
	  
	return false;
}

public GeometricObject copy() {
	  
	return null;
}

public int getIntType() {
	  
	return 0;
}

public SketchMaster.Stroke.graphics.shapes.Rectangle getRectangularBounds() {
	  
	return null;
}

public int spatialRelation(GeometricObject geometricobject) {
	  
	return 0;
}

public boolean touches(GeometricObject geometricobject) {
	  
	return false;
}

public boolean pointIsOn(PointData point, double d) {
	  
	return false;
}

public boolean pointIsOnOriginal(PointData point, double d) {
	  
	return false;
}

public void translate(double d, double d1) {
	  
	
}

public void paint(Graphics g) {
		if (logger.isDebugEnabled()) {
			//  logger.debug("paint(Graphics) -     (" + this.getClass().getSimpleName() + "    " + (new Throwable()).getStackTrace()[0].getLineNumber() + "  )  "); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
}

public PolygonShape getPolygonalBounds() {
	  
	return null;
}

public void paint(Graphics2D g) {
	  
	
}

/**
 * @return the direction
 */
public double getDirection() {
	return Direction;
}

/**
 * @param direction the direction to set
 */
public void setDirection(double direction) {
	Direction = direction;
}

public String fullString() {

	String s = " X = " + getX() + " Y = " + getY() + " Time = " + time+" Length= "+CompulativeLength+" directions= "+Direction;
	return s;
}

//public int compareTo(PointData o) { 
//	//java.lang.Double  result = (java.lang.Double getX()).compareTo(y) ;
//	// also the length from the orign can be used to sort...
//	double s=x;
//	java.lang.Double temp=new	java.lang.Double ( x);
//	int result =temp.compareTo( o.x);
//	 temp=new	java.lang.Double ( y);
//	int result2= temp.compareTo( o.y);
//	 
//	return result == 0 ? result2:result; 
//	
//	//return 0;
//}


public int compareTo(PointData o) { 
	//java.lang.Double  result = (java.lang.Double getX()).compareTo(y) ;
	// also the length from the orign can be used to sort...
 

 
 return (new java.lang.Double(magnitude())).compareTo( o.magnitude());
 
}
//public int compareTo (PointData point){
//	int result = x.compareTo((point).x);
//	return result == 0 ? y.compareTo((point).y):result;
//	}

public boolean isNearPoint(PointData p, double range) {
	if (isSamePoint( p))// check if have same cordinates,

		return true; // then return true both points are in range
	else {
		// not the same point
		// compare the distance between two points with the range given
		// if they lay inside this range then they are near point.
		if (this.distance(p) < range) {
			return true;
		}
	}
	return false;
}



public void setOvertrace(boolean b) {
	
	// TODO Auto-generated method stub
	overTrace=b;
}
public void setDeleted(boolean b) {
	
	// TODO Auto-generated method stub
	Deleted=b;
}
//	    public Rectangle2D getBounds2D()
//	    {
//	        return new java.awt.geom.Rectangle2D.Double(x - 1.0D, y - 1.0D, 2D, 2D);
//	    }
//
//	    public Polygon getPolygonalBounds()
//	    {
//	        Polygon result = new Polygon();
//	        result.addPointDouble(x - 1.0D, y - 1.0D);
//	        result.addPointDouble(x + 1.0D, y - 1.0D);
//	        result.addPointDouble(x + 1.0D, y + 1.0D);
//	        result.addPointDouble(x - 1.0D, y + 1.0D);
//	        return result;
//	    }

public boolean isDeleted() {
	
	return Deleted;
}

//	    public int spatialRelation(GeometricObject object)
//	    {
//	        return getRectangularBounds().spatialRelation(object);
//	    }
}
