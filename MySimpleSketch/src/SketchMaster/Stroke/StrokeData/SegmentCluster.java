/**
 * 
 */
package SketchMaster.Stroke.StrokeData;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import SketchMaster.Stroke.features.SegmentClusterFeatureSet;
import SketchMaster.Stroke.graphics.shapes.GuiShape;
import SketchMaster.Stroke.graphics.shapes.Line;
import SketchMaster.Stroke.graphics.shapes.SegmentedShape;
import SketchMaster.gui.DrawingDebugUtils;
import SketchMaster.system.SystemSettings;

/**
 * @author maha a coollection of segment that will be used to get a sembol
 */
public class SegmentCluster extends ArrayList<Segment> implements InkInterface,
		Serializable {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(SegmentCluster.class);
	private static final  Logger logE=Logger.getLogger("ExampleLogging");;
	private Rectangle2D box;
	private boolean box_valid=false; 
	// it must compute features

	// the full ink path of the colleection
	transient InkInterface FullinkPath;

	// strokes of a symbol
	ArrayList<Stroke> strokeInSymbol = new ArrayList<Stroke>();

	int strokeCount = 0; // number of strokes consist the segmets in the
							// cluster
	int segmentCount = 0;
	double area=0.0;
	transient SegmentClusterFeatureSet FeatureSet = new SegmentClusterFeatureSet();

	private String curString;

	

	/**
	 * 
	 */
	private static final long serialVersionUID = 7346329178510124243L;

	public static final int DRAW_RAW_INK = 0;

	public static final int DRAW_CLEAN_SEGMENTS = 1;
	public static final int	DRAW_BOTH_RAW_SEGMENT=2;
	public static final int DRAW_ORIGINAL=3;
	
	
	private void writeObject(ObjectOutputStream os) throws IOException {
		try {
			os.defaultWriteObject();
		
//			if (name instanceof type) {
//				type new_name = (type) name;
//				
//			}FullinkPath
//			os.writeBoolean(this.endPoint);
//			os.writeBoolean( this.startPoint);
//			os.writeBoolean(this.controlPoint);
//			os.writeLong(this.time);
			
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
			generateInkPath();
			
//			point.presureValue=is.readDouble();
//			point.CompulativeLength=is.readDouble();
		} catch (Exception e) {
			logger.error("readObject(ObjectInputStream)", e); //$NON-NLS-1$
		}
	}

	
	public void paint(Graphics2D g) {
		
	//	logger.trace("   inside the cluster paint ");
		Color temp = g.getColor();
		java.awt.Stroke  tempStroke=g.getStroke();
		
		if (SystemSettings.DRAW_SYMBOL_OPTION ==DRAW_ORIGINAL){
			
			if (FullinkPath != null) {
				//full ink path is not null 
				//logger.trace("full ink path is not nullll         ");
                         int noPointsF=FullinkPath.getPointsCount();
				for (int i = 0; i < noPointsF; i++) {

					if (FullinkPath.getPoint(i).isStartPoint()
							|| FullinkPath.getPoint(i).isEndPoint()) {
						// do nothing 

					}

					else if ((i + 1) <  noPointsF) {
						g.setColor(DrawingDebugUtils.InkColor);
						g.drawLine((int) (FullinkPath.getPoint(i).x),
								(int) (FullinkPath.getPoint(i).y),
								(int) (FullinkPath.getPoint(i + 1).x),
								(int) (FullinkPath.getPoint(i + 1).y));
					}

				}
			}
			
		}
		// logger.info("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
		// logger.info("VVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVV");
		if (SystemSettings.DRAW_SYMBOL_OPTION == DRAW_RAW_INK||SystemSettings.DRAW_SYMBOL_OPTION == DRAW_BOTH_RAW_SEGMENT) {

			if (FullinkPath != null) {
				//full ink path is not null 
				//logger.trace("full ink path is not nullll         ");
                         int noPointsF=FullinkPath.getPointsCount();
				for (int i = 0; i < noPointsF; i++) {

					if (FullinkPath.getPoint(i).isStartPoint()
							|| FullinkPath.getPoint(i).isEndPoint()) {
						g.setColor(DrawingDebugUtils.PointsColor);
						g.drawRect((int) (FullinkPath.getPoint(i).x),
								(int) (FullinkPath.getPoint(i).y),
								DrawingDebugUtils.PointsSize,
								DrawingDebugUtils.PointsSize);
						g.fillRect((int) (FullinkPath.getPoint(i).x),
								(int) (FullinkPath.getPoint(i).y),
								DrawingDebugUtils.PointsSize,
								DrawingDebugUtils.PointsSize);

					}

					else if ((i + 1) <  noPointsF) {
						g.setColor(DrawingDebugUtils.InkColor);
						g.drawLine((int) (FullinkPath.getPoint(i).x),
								(int) (FullinkPath.getPoint(i).y),
								(int) (FullinkPath.getPoint(i + 1).x),
								(int) (FullinkPath.getPoint(i + 1).y));
					}

				}
			}

		}
		if (SystemSettings.DRAW_SYMBOL_OPTION == DRAW_CLEAN_SEGMENTS||SystemSettings.DRAW_SYMBOL_OPTION == DRAW_BOTH_RAW_SEGMENT) {
			logger.trace(" in draw each segment ... ");
			g.setColor(DrawingDebugUtils.segmeColor);
			java.awt.BasicStroke bold=new BasicStroke(3);
			
			g.setStroke(bold);
			int i=0;
			for (Iterator iter = iterator(); iter.hasNext();) {
			
				i++;
				Segment element = (Segment) iter.next();
				element.paint(g);
				logger.trace(" drawing the  element no. "+i+" "+element);
			}
		}
		g.setColor(temp);
		// logger.info(" add paint implementaiton of the clusters.");
	}

	public void setParam(ArrayList Param) {

	}

	/**
	 * add the stroke and all its segments teo the cluster
	 * 
	 * @param stroke
	 */
	public void addStroke(Stroke stroke) {
		 
		//if (logger.isDebugEnabled()) {
		//	logger.warn("addStroke(Stroke) - 	// TODO add stroke implementation of the stroke        segment cluseter 104"); //$NON-NLS-1$
		//}
	}

	/**
	 * add all a segments of the specific stroke to the cluster.
	 * 
	 * @param stroke
	 * @param segmentsOfStroke
	 */
	public void addAll(Stroke stroke, ArrayList<Segment> segmentsOfStroke) {
		if (segmentsOfStroke != null) {
			// add count of strokes
			// add count of segments
			logger.trace( " addint strokes to segments "+strokeCount);
			strokeCount++;
			segmentCount += segmentsOfStroke.size();
			this.addAll(segmentsOfStroke);
			this.strokeInSymbol.add(stroke);
		}
		else{
			
		
			this.strokeInSymbol.add(stroke);
			logger.trace( " addint strokes to segments "+this.strokeInSymbol.size());
		}
	}

	private void generateInkPath() {
		this.FullinkPath = new SimpleInkObject();
		ArrayList<PointData> allPoints = new ArrayList<PointData>();
		// FullinkPath.setPoints(new ArrayList<PointData>());

		if (this.segmentCount > 0) {
			for (Iterator iter = this.iterator(); iter.hasNext();) {
				Segment element = (Segment) iter.next();
                    if (element!=null){
				ArrayList<PointData> temp = element.getPoints();
				temp.get(0).setStartPoint(true);
				temp.get(temp.size() - 1).setEndPoint(true);
				allPoints.addAll(temp);
                    }
                    else 
                    	iter.remove();

			}
		}
		FullinkPath.setPoints(allPoints);
		// Implement the general ink path of the cluster
		// logger.info(" Implement the general ink path of the cluster");
		// logger.info("use point strart point and endpoint attribute to
		// connect an disconnect segments ");
	}

	public void Recognize() {
		// TODO implement the recognizer of symbol.
		//if (logger.isWarnEnabled()) {
			logger.warn("Recognize() - // TODO implement the recognizer of  symbol.   segment cluster 149 "); //$NON-NLS-1$
		//}
	}

	public void buildSymbol() {
		// logger.setLevel(Level.ERROR);
			if (SystemSettings.DEBUG_MODE){
				logE.info(" strokes to segments "+this.strokeInSymbol.size());
				logE.info(" there are  "+this.size()+"  segments in this symbol");
				int s=0;
				for (Iterator iter = this.iterator(); iter.hasNext();) {
					Segment element = (Segment) iter.next();
					logE.info("   Segment S"+s+"  =  "+element);
					
				}
				s++;
				logE.info("----------------------------------------------------------------");
	            // logE.info(")
				}
		generateInkPath();
		computeFeatures();
	}

	private void computeFeatures() {
		
		
//		if (logger.isInfoEnabled()) {
//			logger.info("computeFeatures() - ///////
		/////here is featture computed///////////////////  (" + getClass().getSimpleName() + "    " + (new Throwable()).getStackTrace()[0].getLineNumber() + "  )  "); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
//		}
		
		curString="";
		 
		FeatureSet.setSegmentCluster(this);
		FeatureSet.initAll();
		FeatureSet.computeFeatures();
		// call compuste feateure of set.
		// TODO implement compute the symbol features
		//if (logger.isDebugEnabled()) {
			logger.warn("computeFeatures() - 	// TODO implement compute the symbol features-------------30% done  segmet cluser 165 "); //$NON-NLS-1$
		//}


//
//		if (logger.isInfoEnabled()) {
//			logger.info("computeFeatures() - segments size  == " + this.segmentCount); //$NON-NLS-1$
//		}	
		
	if (logger.isDebugEnabled()||SystemSettings.DEBUG_MODE) {
		for (int i = 0; i < FeatureSet.getFeatures().size(); i++) {
		 
		
//				if (FeatureSet.getFeatures().get(i).getValues().length==1)
//				{ 
//					logger.debug(" ComputeFeatures() - " + FeatureSet.getFeatures().get(i)); //$NON-NLS-1$
//				}
//				else {
					
					 double[] feat = FeatureSet.getFeatures().get(i).getValues();
					 String[] names=FeatureSet.getFeatures().get(i).getNames();
					 for (int j = 0; j < feat.length; j++) {
						 //logger.debug(" ComputeFeatures() -  "+FeatureSet.getFeatures().get(i).getName());
						 logger.debug(" ComputeFeatures() - "+ names[j]+" "+feat[j]);
						 curString+="  Feature "+ names[j]+" "+feat[j]+"   ,  ";
					}
					
				 	 //$NON-NLS-1$
				//}
				//}
			}
		}
//
//		if (logger.isDebugEnabled()) {
//			//  logger.debug("computeFeatures() - ////////////here is featture computed///////////////////                 segmet cluser   174 "); //$NON-NLS-1$
//		}
		
	//	if(logger.isTraceEnabled()){
//			logger.info(" L("
//					+ (new Throwable()).getStackTrace()[0].getLineNumber()
//					+ ")" + " - " + "-------------------finished the features");
	//	}
	}

	public SimpleInkObject getInkPath() {
		SimpleInkObject returnSimpleInkObject = (SimpleInkObject) this.FullinkPath;
		return returnSimpleInkObject;
	}

	public void addPoint(PointData point) {

	}

	public InkInterface createSubInkObject(int start, int end) {
		InkInterface returnInkInterface = this.FullinkPath.createSubInkObject(start, end);
		return returnInkInterface;
		// return null;
	}

	public void deletePoint(int index) {
		this.FullinkPath.deletePoint(index);
	}

	public PointData getPoint(int index) {
		PointData returnPointData = FullinkPath.getPoint(index);
		return returnPointData;
	}

	public ArrayList<PointData> getPoints() {
		ArrayList<PointData> returnArrayList = this.FullinkPath.getPoints();
		return returnArrayList;

	}

	public int getPointsCount() {
		int returnint = FullinkPath.getPointsCount();
		return returnint;
	}

	public void setPoints(ArrayList<PointData> po) {
		FullinkPath.setPoints(po);
	}

	public void setPoints(int index, PointData point) {
		FullinkPath.setPoints(index, point);
	}

	public void setArea(double cluserArea) {
		
		area=cluserArea;
	}

	public double getArea() {
		
		return area;
	}

	/**
	 * @return the strokeInSymbol
	 */
	public ArrayList<Stroke> getStrokeInSymbol() {
		return strokeInSymbol;
	}

	@Override
	public String toString() {
		  
		return curString;
	}
	public Rectangle2D getBox() {
		if (box_valid)
			return box;
		else {
		if (getPoints()!=null){
			if (getPoints().size()>0){
			PointData point = getPoints().get(0);
			box = new Rectangle2D.Double(point.getPointLocation().getX(), point
					.getPointLocation().getY(), 0, 0);
			for (int i = 1; i < getPoints().size() - 1; i++) {
				
				box.add(getPoints().get(i).getPointLocation());
				}
			box_valid=true;
			return box;
			}
		}
			Rectangle2D returnRectangle2D = box = new Rectangle2D.Double(0, 0, 0, 0);
		return 	returnRectangle2D;
		}
	}
	public boolean canIntersect(InkInterface end) {
		if (end instanceof  SegmentCluster ) {
			 SegmentCluster  new_e = ( SegmentCluster ) end;
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

	public ArrayList<PointData> IntersectionPoints(Line l) {
 
		logger.warn(" // TODO Auto-generated method stub IntersectionPoint ");
		return null;
	}

	public ArrayList<InkInterface> divideDirection() {
	 
		logger.warn(" // TODO Auto-generated method  divide directions  ");
		return null;
	}

	public ArrayList<Line> toLines() {
		// TODO Auto-generated method stub
		logger.warn(" // TODO Auto-generated method stub  to lines  ");
		return null;
	}

}
