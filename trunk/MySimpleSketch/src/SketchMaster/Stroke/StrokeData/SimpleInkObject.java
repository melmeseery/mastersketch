/**
 * 
 */
package SketchMaster.Stroke.StrokeData;

import org.apache.log4j.Logger;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.util.ArrayList;
import java.io.Serializable;
import java.lang.reflect.Array;

import SketchMaster.Stroke.graphics.shapes.GuiShape;

/**
 * @author melmeseery
 * 
 */
public class SimpleInkObject implements Serializable, InkInterface {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(SimpleInkObject.class);

	/**
	 * @uml.property name="points"
	 */
	protected ArrayList<PointData> points = null;
	protected boolean closed=false,open=true;
	private Rectangle2D box;
	private boolean box_valid=false; 
	double length;
	boolean lengthComputed=false;

	/**
	 * @return the closed
	 */
	public boolean isClosed() {
		return closed;
	}

	/**
	 * @param closed the closed to set
	 */
	public void setClosed(boolean closed) {
		this.closed = closed;
	}

	/**
	 * @return the open
	 */
	public boolean isOpen() {
		return open;
	}

	/**
	 * @param open the open to set
	 */
	public void setOpen(boolean open) {
		this.open = open;
	}

	/**
	 * 
	 */
	public SimpleInkObject() {
	
	 points=new ArrayList<PointData>();
	}

	public ArrayList<PointData> getPoints() {
		return points;
	}

	/**
	 * getPointsCount
	 */
	public int getPointsCount() {
		int returnint = points.size();
		return returnint;
	}

	public void addPoint(PointData point) {
		// / adding point to the array
		points.add(point);
		box_valid=false;
	}

	public void paint(Graphics2D g) {
		// 
		if (points != null)
			for (int i = 0; i < points.size() - 1; i++) {
				g.drawLine((int) ((PointData) points.get(i)).getX(),
						(int) ((PointData) points.get(i)).getY(),
						(int) ((PointData) points.get(i + 1)).getX(),
						(int) ((PointData) points.get(i + 1)).getY());
			}// for
	}

	public InkInterface createSubInkObject(int start, int end) {
		//			
		//			
		//			
		InkInterface ink = new SimpleInkObject();
		ArrayList<PointData> po = new ArrayList<PointData>();
		if (this.points != null) {
			for (int i = start; (i < this.points.size()) && (i < end); i++) {
				po.add(this.points.get(i));
			}

		}
		ink.setPoints(po);
		// return a segment from this stroke that will contain the points of the
		// stroke.
		return ink;
		//		
	}

	public void deletePoint(int index) {
		this.points.remove(index);
	}

	public PointData getPoint(int index) {
		if (points.size()>index && points.size()>0)
		{
			PointData returnPointData = (PointData) points.get(index);
		
		return returnPointData;
		}
		else return null;
	}

	public void setPoints(int index, PointData point) {
		this.points.set(index, point);
		box_valid=false;
	}

	/**
	 * @param points
	 *            the points to set
	 * @uml.property name="points"
	 */
	public void setPoints(ArrayList<PointData> points) {
		this.points = points;
		box_valid=false;
	}

	public void setParam(ArrayList Param) {
		// 

	}

	public Rectangle2D getBox() {
		if (box_valid)
			return box;
		else {
		if (points!=null){
			if (points.size()>0){
			PointData point = points.get(0);
			box = new Rectangle2D.Double(point.getPointLocation().getX(), point
					.getPointLocation().getY(), 0, 0);
			for (int i = 1; i < points.size() - 1; i++) {
				
				box.add(points.get(i).getPointLocation());
				}
			box_valid=true;
			return box;
			}
		}
			Rectangle2D returnRectangle2D = box = new Rectangle2D.Double(0, 0, 0, 0);
		return 	returnRectangle2D;
		}
	}
    public void transform (AffineTransform at) {
    	  
		if (logger.isDebugEnabled()) {
			//  logger.debug("transform(AffineTransform) -   transform using the tx  (" + this.getClass().getSimpleName() + "    " + (new Throwable()).getStackTrace()[0].getLineNumber() + "  )  "); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
        int n =points.size()*2;
        double[] coords = new double[n];
        int k=0;
        for(int i=0; i<points.size(); i++){
            coords[k++]=points.get(i).getX();
            coords[k++]=points.get(i).getY();
        }
        at.transform(coords,0,coords,0,points.size());
        k=0;
        for(int i=0; i<points.size(); i++){
        	double x=coords[k++];
        	double y=coords[k++];
        	points.get(i).setPointLocation(new Point2D.Double(x,y));
//            [i]=coords[k++];
//            _yvals[i]=;
        }
    }
    public double getLength() {
		if (lengthComputed)
			return length;
		else {
			if (points!=null)
			{
				length=0;
				for (int i = 0; i < points.size()-1; i++) {
					length+=points.get(i).distance(points.get(i+1));
				}
				lengthComputed=true;
				return length;
			}
		
		 
		return 0;
		}
	}
    /**
     * Translate the polyline with the given distance.
     */
    public void translate (double x, double y) {
        for (int i=0; i<points.size(); i++) {
        	double xp=points.get(i).getX()+x;
        	double yp=points.get(i).getY()+y;
        	
        	points.get(i).setPointLocation(new Point2D.Double(xp,yp));
        	
         
        }
    }

	public boolean canIntersect(InkInterface end) {
		if (end instanceof SimpleInkObject) {
			SimpleInkObject new_e = (SimpleInkObject) end;
			 return this.getBox().intersects(new_e.getBox() );
			
		}
		else {
			// get the box for end 
			if (end.getPoints().size()>0){
				PointData point = end.getPoints().get(0);
				Double boxe = new Rectangle2D.Double(point.getPointLocation().getX(), point
						.getPointLocation().getY(), 0, 0);
				for (int i = 1; i < end.getPoints().size() - 1; i++) {
					
					boxe.add(end.getPoints().get(i).getPointLocation());
					}
				return getBox().intersects(boxe);
				
			}
			
			
		}	
		return false;
	}
}
