/**
 * 
 */
package SketchMaster.Stroke.StrokeData;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.apache.log4j.Logger;
 

import SketchMaster.Stroke.graphics.shapes.FittedShape;
import SketchMaster.Stroke.graphics.shapes.Line;
import SketchMaster.lib.ComputationsGeometry;
import SketchMaster.lib.CurveFitData;
import SketchMaster.system.SystemSettings;

/**
 * @author maha
 *
 */
public class InkPart implements InkInterface {
	
	
	private static final Logger logger = Logger.getLogger(InkPart.class);
	private static final double DivideStrokePercent = SystemSettings.DivideStrokePercent;
	int start, end;
	boolean OnlyPart;
	CurveFitData sums;
	private boolean sumsComputed=false;
	private Rectangle2D box;
	private boolean box_valid=false; 
	double length;
	boolean lengthComputed=false;
	double revolution;
	protected ArrayList<PointData> points = null;
	protected boolean closed=false,open=true;
	private double rotation;
	boolean rotationComputed=false;
	
	/* (non-Javadoc)
	 * @see SketchMaster.Stroke.StrokeData.InkInterface#addPoint(SketchMaster.Stroke.StrokeData.PointData)
	 */
	public void addPoint(PointData point) {
	 
	}

	/**
	 * @return the start
	 */
	public int getStart() {
		return start;
	}

	/**
	 * @param start the start to set
	 */
	public void setStart(int start) {
		this.start = start;
	}

	/**
	 * @return the end
	 */
	public int getEnd() {
		return end;
	}

	/**
	 * @param end the end to set
	 */
	public void setEnd(int end) {
		this.end = end;
	}

	/**
	 * @return the sums
	 */
	public CurveFitData getSums() {
		return sums;
	}

	/* (non-Javadoc)
	 * @see SketchMaster.Stroke.StrokeData.InkInterface#canIntersect(SketchMaster.Stroke.StrokeData.InkInterface)
	 */
	public boolean canIntersect(InkInterface end) {
		if (end instanceof SimpleInkObject) {
			SimpleInkObject new_e = (SimpleInkObject) end;
			 return this.box.intersects(new_e.getBox() );
			
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
				return box.intersects(boxe);
				
			}
			
			
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see SketchMaster.Stroke.StrokeData.InkInterface#createSubInkObject(int, int)
	 */
	public InkInterface createSubInkObject(int start, int end) {
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
	}

	/* (non-Javadoc)
	 * @see SketchMaster.Stroke.StrokeData.InkInterface#deletePoint(int)
	 */
	public void deletePoint(int index) {
		 // this function will do nothing as i do not wan to delete any point... 
		//this.points.remove(index);
	}

	/* (non-Javadoc)
	 * @see SketchMaster.Stroke.StrokeData.InkInterface#getPoint(int)
	 */
	public PointData getPoint(int index) {
		 
		if (points.size()>index && points.size()>0)
		{
			if (end>index && index>start)
			{
			PointData returnPointData = (PointData) points.get(index);
		
		return returnPointData;
		}
		}
	  return null;
	}

	/* (non-Javadoc)
	 * @see SketchMaster.Stroke.StrokeData.InkInterface#getPoints()
	 */
	public ArrayList<PointData> getPoints() {
	 
		return points;
	}

	/* (non-Javadoc)
	 * @see SketchMaster.Stroke.StrokeData.InkInterface#getPointsCount()
	 */
	public int getPointsCount() {
		 
		return end-start;
	}
	 public double getLength() {
			if (lengthComputed)
				return length;
			else {
				if (points!=null)
				{
					length=0;
					for (int i = start; i < end-1; i++) {
						length+=points.get(i).distance(points.get(i+1));
					}
					lengthComputed=true;
					return length;
				}
			
			 
			return 0;
			}
		}
	/* (non-Javadoc)
	 * @see SketchMaster.Stroke.StrokeData.InkInterface#setPoints(java.util.ArrayList)
	 */
	public void setPoints(ArrayList<PointData> po) {
		this.points = po;
		box_valid=false;
	}
	public void setPoints(ArrayList<PointData> po, int start, int end) {
		this.points = po;
		this.start=start;
		this.end=end;
		box_valid=false;
	}
	/* (non-Javadoc)
	 * @see SketchMaster.Stroke.StrokeData.InkInterface#setPoints(int, SketchMaster.Stroke.StrokeData.PointData)
	 */
	public void setPoints(int index, PointData point) {
		this.points.set(index, point);
		box_valid=false;
	}

	/* (non-Javadoc)
	 * @see SketchMaster.Stroke.graphics.shapes.GuiShape#paint(java.awt.Graphics2D)
	 */
	public void paint(Graphics2D g) {
		if (points != null)
			for (int i = start; i <end- 1; i++) {
				g.drawLine((int) ((PointData) points.get(i)).getX(),
						(int) ((PointData) points.get(i)).getY(),
						(int) ((PointData) points.get(i + 1)).getX(),
						(int) ((PointData) points.get(i + 1)).getY());
			}// for
	}
	
	public CurveFitData Sums(){
		if (!sumsComputed){
			sums=new CurveFitData();
			sums.computeInitalDat( getPoints(), start, end);
			
		}
		return sums;
		
	}
	public double TotalRotation(){
		if(!rotationComputed){
			rotation=0;
		ArrayList<PointData> points;
		double rot;
		if ( getPoints() != null) {

			points = getPoints();
			if (points.size() > 3) {
			 rotation = 0.0;
				for (int i = start; i < points.size() - 2 && i<end; i++) {
					PointData p1 = points.get(i);
					PointData p2 = points.get(i + 1);
					PointData p3 = points.get(i + 2);
					rot= ComputationsGeometry
							.computeChangeRotation(p1, p2, p3);
					
					rotation += rot;

				}
 
	}
		}	
		revolution=rotation/(2.0*Math.PI);
		rotationComputed=true;
		}
		
	
return rotation;
}
	
	
	public ArrayList<Line> toLines(){
		ArrayList<Line> stLines=new 	ArrayList<Line>();
		double segmentNo= DivideStrokePercent *points.size();
		
		int step=(int) Math.round(points.size()/segmentNo);
		if (step==0){
			step=2;
		}
		logger.info("  after computation of segments number "+segmentNo + "   and  step is     "+step);
		for (int i = start; i < this.points.size()-step && i<end ; i+=step) {
			Line  temp=new Line(points.get(i),points.get(i+step ));
			stLines.add(temp);
		}
		
		return stLines;
	}

	public  ArrayList<PointData> IntersectionPoints(Line l2) {
		//first divide the stroke into set lines (mainly based on length... 
		 ArrayList<Line> lines = toLines();
		// there may be more than two intersection.... so save all..  
              ArrayList< PointData>  intersections=new   ArrayList< PointData> ();
		 for (int i = 0; i < lines.size(); i++) {
			
			 if (l2.isIntersect(lines.get(i)))
			 {
				 // the intersection .... 
				 PointData inter = l2.getIntersection(lines.get(i));
				 
				 // 
				 intersections.add(inter);
				 
			 }
			 
		}
		 logger.info("  there are ..   "+intersections.size()+"     which are  "+intersections);
		 // get the farthest interection points  to dertermine the extreeimes of the line.. 
//		 double maxlengthLeft=0,	 maxlengthRight=0;
//		int  firstpointindex=-1, secondpointindex=-1;
	 // on for left o fthe line and 
		Collections.sort( intersections );
		
		
		// get the first and last only 
		
	//	 for (int i = 0; i < intersections.size(); i++) {
				
		 // sort by 

		 
		 
	  
		return intersections;
	}

	public ArrayList<InkInterface> divideDirection() {
 
		logger.warn("	// TODO Auto-generated method stub  divide directions ");
		return null;
	}
}
