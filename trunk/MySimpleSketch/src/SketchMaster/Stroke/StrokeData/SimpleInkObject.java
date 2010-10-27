/**
 * 
 */
package SketchMaster.Stroke.StrokeData;

 

import org.apache.log4j.Logger;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.util.ArrayList;
import java.util.Collections;
import java.io.Serializable;
import java.lang.reflect.Array;

import SketchMaster.Stroke.graphics.shapes.GuiShape;
import SketchMaster.Stroke.graphics.shapes.Line;
import SketchMaster.gui.DrawingDebugUtils;
import SketchMaster.lib.ComputationsGeometry;
import SketchMaster.lib.CurveFitData;
import SketchMaster.system.SystemSettings;

/**
 * @author melmeseery
 * 
 */
public class SimpleInkObject implements Serializable, InkInterface {
	/**
	 * Logger for this class
	 */
	
	private static final double DivideStrokePercent = SystemSettings.DivideStrokePercent;
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
	private boolean sumsComputed=false;
	 CurveFitData  sums;
		private double rotation;
		boolean rotationComputed=false;
		double revolution;
		double minX,minY, maxY,maxX;
	public double getMinX() {
			return minX;
		}

		public double getMinY() {
			return minY;
		}

		public double getMaxY() {
			return maxY;
		}

		public double getMaxX() {
			return maxX;
		}

	/**
	 * @return the closed
	 */
	public boolean isClosed() {
		return closed;
	}
	protected double getBoxArea() {
		return getBox().getHeight()*getBox().getWidth();
		//return 0;
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
			minX=box.getMinX();
			minY=box.getMinY();
			maxX=box.getMaxX();
			maxY=box.getMaxY();
			
			
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
	public CurveFitData Sums(){
		if (!sumsComputed){
			sums=new CurveFitData();
			sums.computeInitalDat(getPoints());
			
		}
		return sums;
		
	}
	public double TotalRotation(){
		if(!rotationComputed){
			rotation=0;
		ArrayList<PointData> points;
		if (getPoints() != null) {
        double rot;
			points =  getPoints();
			if (points.size() > 3) {
			 rotation = 0.0;
				for (int i = 0; i < points.size() - 2; i++) {
					PointData p1 = points.get(i);
					PointData p2 = points.get(i + 1);
					PointData p3 = points.get(i + 2);
					rot= ComputationsGeometry
					.computeChangeRotation(p1, p2, p3);
					rotation +=  ComputationsGeometry
							.computeChangeRotation(p1, p2, p3);

				}
 
	}
		}
		revolution=rotation/(2.0*Math.PI);
		rotationComputed=true;
		}
		
		
	
return rotation;
}

	public ArrayList<PointData> IntersectionPoints(Line l2) {
		
			 ArrayList<Line> lines = toLines();
	 
			  
		 logger.info(" intesrsection of line "+l2+"  with the storkes that are respresented by lines"+lines);
//		// there may be more than two intersection.... so save all..  
             ArrayList< PointData>  intersections=new   ArrayList< PointData> ();
		 for (int i = 0; i < lines.size(); i++) {
//		 
			 if (l2.isIntersect(lines.get(i)))
			 {
//				 // the intersection .... 
				 PointData inter = l2.getIntersection(lines.get(i));
//				 
//				 // 
			 if (inter!=null){
					 logger.info(" the  sub intersections .. is  "+inter);
//				 if (this.getBox().contains(inter))
					 	intersections.add(inter);
			 }
				 
			 }
//			 
		}
		 
		 
		 
		 
//			if (DrawingDebugUtils.DEBUG_GRAPHICALLY ){
//				logger.info(" inside the debug graphically system...............");
//				 Graphics2D g = DrawingDebugUtils.getGraphics();
//	 
////					 //DrawingDebugUtils.getGraphics().setColor(Color.GREEN);
////				 for (int i = 0; i < lines.size(); i++) {
////					 DrawingDebugUtils.drawThickLine(3, g, lines.get(i).getStartPoint().getX(),lines.get(i).getStartPoint().getY(),
////							 lines.get(i).getEndPoint().getX(),lines.get(i).getEndPoint().getY());
////				 }
//				 DrawingDebugUtils.PointsSize=10;
//				// g.setColor(Color.ORANGE);
//				 for (int i = 0; i < intersections.size(); i++) {
//					 DrawingDebugUtils.drawPoint( g, Color.ORANGE,intersections.get(i));
//				}
////					 DrawingDebugUtils.drawLine(g, Color.CYAN, l);
////					 DrawingDebugUtils.drawLine(g, Color.MAGENTA, l2);
////					 
//			 		}
		 

	  
		return  intersections;
	}

	public ArrayList<InkInterface> divideDirection() {

		return null;
	}

	
	public ArrayList<Line> toLines(){
		ArrayList<Line> stLines=new 	ArrayList<Line>();
		double segmentNo= DivideStrokePercent *points.size();
		
		int step=(int) Math.round(points.size()/segmentNo);
		if (step==0){
			step=2;
		}
		logger.info("  after computation of segments number "+segmentNo + "   and  step is     "+step);
		for (int i = 0; i < this.points.size()-step; i+=step) {
			Line  temp=new Line(points.get(i),points.get(i+step ));
			stLines.add(temp);
		}
		
		return stLines;
	}
}
