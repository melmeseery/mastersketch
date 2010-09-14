/**
 *
 * 
 * 
 * 
 */
package SketchMaster.Stroke.features;

import org.apache.log4j.Logger;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.ArrayList;

import SketchMaster.system.SystemSettings;
import SketchMaster.Stroke.StrokeData.InkInterface;
import SketchMaster.Stroke.StrokeData.PointData;
import SketchMaster.Stroke.StrokeData.Stroke;

/**
 * @author Mahi
 */
public class FeatureFunction implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8911153265345792426L;

	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(FeatureFunction.class);

	private StrokeFeatures func = null;
	private ArrayList<PointData> data = null;
	private static double ZERO = 10e-3;
	private double max = Double.MIN_VALUE;
	private double min = Double.MAX_VALUE;
	private String Name = "";
	private String xName = " X - axis ";
	private String yName = " Y - axis ";
	private int maxi = 0;
	private int mini = 0;
	private double maxX = 0.0;
	private double minX = 0.0;
	private double average = 0.0;
	private double absaverage = 0.0;
	private ArrayList pointIndex = null;
	private double sumAbs = 0.0;
	private double sumUpNow = 0.0;
	private double DataThreshold = 0.0;
	private int functionType = ONLINE_TYPE;
	public static final int BATCH_TYPE = 0;
	public static final int ONLINE_TYPE = 1;

	private   transient ArrayList<Point2D> ImportantList = new ArrayList<Point2D>();

	/**
	 * @return the data
	 * @uml.property name="data"
	 */
	public ArrayList<PointData> getData() {

		return data;
	}

	public StrokeFeatures getFunc() {
		return func;
	}

	/**
	 * @param func
	 *            the func to set
	 * @uml.property name="func"
	 */
	public void setFunc(StrokeFeatures func) {
		this.func = func;
	}
 
	@Deprecated
	public void updateFunctionWithPoint(PointData point, InkInterface stroke) {
		if (func != null) {
			ArrayList points = stroke.getPoints();
			if ((points.size() == 1) || data == null) {
				data = new ArrayList<PointData>();
				pointIndex = new ArrayList();
				sumUpNow = 0.0;
				return;
			}
			PointData temp;
			if (points.size() > 3) {

				// PointData point;
				// point=(PointData)points.get(0);

				// double total=0.0;
				// sumUpNow
				// retrieve the data
				func.prev = (PointData) points.get(points.size() - 3);// the
																		// ppoint
																		// before
																		// before
																		// last
				func.next = point;// the last point is thee addded one
				func.current = (PointData) points.get(points.size() - 2);// the
																			// point
																			// before
																			// last

				// now compute the calcuations
				temp = new PointData(func.f());
				// save calculation in data
				data.add(temp); // it i-1 because loop start from 1
				pointIndex.add(points.size() - 1);
				if (!(Double.isInfinite(temp.getY()) || Double.isNaN(temp
						.getY()))) {
					// now save some statistics global min and global max and
					// other
					if (temp.getY() > max) {
						max = temp.getY();
						maxi = points.size() - 1;
						maxX = temp.getX();
					}
					if (temp.getY() < min) {
						min = temp.getY();
						mini = points.size() - 1;
						minX = temp.getX();
					}
					//
					sumUpNow += temp.getY();
					sumAbs += Math.abs(temp.getY());

				}
				average = sumUpNow / (double) data.size();
				absaverage = sumAbs / (double) data.size();
			}
		}
	}
	public void updateFunctionWithPoint(PointData point, InkInterface stroke, int neigh) {
		if (func != null) {
			ArrayList points = stroke.getPoints();
			if ((points.size() == 1) || data == null) {
				data = new ArrayList<PointData>();
				pointIndex = new ArrayList();
				sumUpNow = 0.0;
				return;
			}
			PointData temp;
			if (points.size() > (neigh*2)) {

				// PointData point;
				// point=(PointData)points.get(0);

				// double total=0.0;
				// sumUpNow
				// retrieve the data
				func.prev = (PointData) points.get(points.size() - (neigh*2)-1);// the
																		// ppoint
																		// before
																		// before
																		// last
				func.next = point;// the last point is thee addded one
				func.current = (PointData) points.get(points.size() - neigh-1);// the
																			// point
																			// before
																			// last

				// now compute the calcuations
				temp = new PointData(func.f());
				// save calculation in data
				data.add(temp); // it i-1 because loop start from 1
				pointIndex.add(points.size() - neigh);
				if (!(Double.isInfinite(temp.getY()) || Double.isNaN(temp
						.getY()))) {
					// now save some statistics global min and global max and
					// other
					if (temp.getY() > max) {
						max = temp.getY();
						maxi = points.size() - neigh;
						maxX = temp.getX();
					}
					if (temp.getY() < min) {
						min = temp.getY();
						mini = points.size() - neigh;
						minX = temp.getX();
					}
					//
					sumUpNow += temp.getY();
					sumAbs += Math.abs(temp.getY());

				}
				average = sumUpNow / (double) data.size();
				absaverage = sumAbs / (double) data.size();
			}
		}
	}
	public void updateFunctionWithPoint(PointData prev, PointData curr,
			PointData next, int index) {
		if (func != null) {

			if (data == null) {
				data = new ArrayList<PointData>();
				pointIndex = new ArrayList();
				sumUpNow = 0.0;
				sumAbs = 0.0;

			}

			PointData temp;

			// PointData point;
			// point=(PointData)points.get(0);

			// double total=0.0;
			// sumUpNow
			// retrieve the data
			func.prev = prev;// the ppoint before before last
			func.next = next;// the last point is thee addded one
			func.current = curr;// the point before last

			// now compute the calcuations
			temp = new PointData(func.f());
			// save calculation in data
			data.add(temp); // it i-1 because loop start from 1
			
			
			pointIndex.add(index);
	//		logger.info(" --------------"+this.func.getName()+"--------------------------i am working on index "+index);
			if (!(Double.isInfinite(temp.getY()) || Double.isNaN(temp.getY()))) {
				// now save some statistics global min and global max and other
				if (temp.getY() > max) {
					max = temp.getY();
					maxi = index;
					maxX = temp.getX();
				}
				if (temp.getY() < min) {
					min = temp.getY();
					mini = index;
					minX = temp.getX();
				}
				//
				
				
				sumUpNow += temp.getY();
			//logger.info("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^"+this.func.getName()+"    sum "+sumUpNow );
				sumAbs += Math.abs(temp.getY());

				average = sumUpNow / (double) data.size();
				absaverage = sumAbs / (double) data.size();
			}

		}
	}

	@Deprecated
	public void calculatFunctionWithNoNanInfin(Stroke stroke) {
		if (logger.isDebugEnabled()) {
			//  logger.debug("calculatFunctionWithNoNanInfin(Stroke) -     This function is deprecated      (" + this.getClass().getSimpleName() + "    " + (new Throwable()).getStackTrace()[0].getLineNumber() + "  )  "); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
	}

	/**
	 * @return the max
	 * @uml.property name="max"
	 */
	public double getMax() {
		return max;
	}

	// /**
	// * @param max the max to set
	// */
	// public void setMax(double max) {
	// this.max = max;
	// }
	/**
	 * @return the min
	 * @uml.property name="min"
	 */
	public double getMin() {
		return min;
	}

	// /**
	// * @param min the min to set
	// */
	// public void setMin(double min) {
	// this.min = min;
	// }
	/**
	 * @return the name
	 * @uml.property name="name"
	 */
	public String getName() {
		if (func != null)
			if (func.getName() != null)
				return func.getName();
		return Name;
	}

	/**
	 * @param name
	 *            the name to set
	 * @uml.property name="name"
	 */
	public void setName(String name) {
		Name = name;
	}

	@Deprecated
	private ArrayList getImportantData() {
		if ((data != null) && (pointIndex != null)) {
			ArrayList temp = new ArrayList();
			Point2D point;
			double x, y;
			// logger.info("Thre threshold i s"+average);
			for (int i = 0; i < data.size(); i++) {
				point = data.get(i);

				if (point != null) {
					x = point.getX();
					y = point.getY();
					// logger.info("i= "+i+" x = "+x+" y= "+y);

					if (!(Double.isInfinite(x) || Double.isNaN(x)
							|| Double.isInfinite(y) || Double.isNaN(y))) {
						if (func.isInFunction(y, DataThreshold)) {
							temp.add(i); // index of point in stroke that
											// have larger than
							ImportantList.add(point);
						}
					}

				}
			}
			return temp;

		}
		return null;
	}

	public ArrayList calcuateLocalPoints() {
		// logger.info("This fuction must be changed so i can detect the
		// local and max min "+" (" + this.getClass().getSimpleName()
		// + " " + (new Throwable()).getStackTrace()[0].getLineNumber()
		// + " ) ");
		//Point2D point;
	//	ArrayList index = getImportantData();
		// get a new list that contains the indexces of this
		// now calculate the local min values
		ArrayList returnArrayList = locateIndeces(getRegionMax(localFunctionRegions(data)));
		return returnArrayList;

		// return getImportantData();

	}

	public ArrayList<Point2D> localMaxMinDataForPlot() {
		Point2D point;
		//ArrayList index = getImportantData();
		// get a new list that contains the indexces of this
		ArrayList<Point2D> returnArrayList = getRegionMax(localFunctionRegions(data));
		return returnArrayList;
		  
		// now calculate the local min values
	//	return localMaxMin(ImportantList);

		
	}

	private  ArrayList<Point2D> getRegionMax(ArrayList<Point> regions){
		if (regions==null)
			return null;
		
		int s,e;
		double maxRegion,x,y;
		int maxRegionIndex;
		
		Point2D point ;
		
		ArrayList<Point2D> RegionMaximums = new ArrayList<Point2D>();
		
		/// loop on all reagions 
		for (int i = 0; i < regions.size(); i++) {
			
			// geth the region start and end index's  
			s=regions.get(i).x;
			e=regions.get(i).y;
			maxRegion=0.0;
			// the maximum of this region. 
			maxRegionIndex=-1;
//			logger.info("region from "+s+"   to the point "+e+" ("
//					+ this.getClass().getSimpleName() + "    "
//					+ (new Throwable()).getStackTrace()[0].getLineNumber()
//					+ "  )  ");
//			
			
			if (s==e)  // i  finish looping or this reiogn is only one element. 
			{
				maxRegionIndex=s; //the first element is the region max 
			     if (maxRegionIndex>=0)			
	            		RegionMaximums.add(data.get(maxRegionIndex));
			     continue;
			}
			for (int j = s; j < e && j<data.size(); j++) {
			
				
		
				
			 point = data.get(j);

				if (point != null) {
					x = point.getX();
					y = point.getY();
					
					// logger.info("i= "+i+" x = "+x+" y= "+y);

					if (!(Double.isInfinite(x) || Double.isNaN(x)
							|| Double.isInfinite(y) || Double.isNaN(y))) {
						
						if (j==s){  // first element in region make it max 
							maxRegion=point.getY();
							maxRegionIndex=s;
						}
						
						if (func.isInFunction(y, DataThreshold)) {
							
							if (func.isInFunction(y, maxRegion))
							{
								maxRegion=y;
								maxRegionIndex=j;
								
							}
							
							
						}
						}
					}
					
				
				
				
			}
            if (maxRegionIndex>=0)			
            		RegionMaximums.add(data.get(maxRegionIndex));
		}
		return RegionMaximums;
	}
	
	private ArrayList<Point> localFunctionRegions(ArrayList<PointData> data){
		// this list carry the start and end of each reiong x=start , y = end 
		ArrayList<Point>  regions=new ArrayList<Point>();
		
		//tmep variabl e
		
		int s=0, e=0,count=0;
		boolean founds=false,founde=false,InRegion=false;

		if ((data != null) && (pointIndex != null)) {
			ArrayList temp = new ArrayList();
			Point2D point;
			double x, y;
			// logger.info("Thre threshold i s"+average);
			for (int i = 0; i < data.size(); i++) {
				point = data.get(i);

				if (point != null) {
					x = point.getX();
					y = point.getY();
					// logger.info("i= "+i+" x = "+x+" y= "+y);

					if (!(Double.isInfinite(x) || Double.isNaN(x)
							|| Double.isInfinite(y) || Double.isNaN(y))) {
						if (func.isInFunction(y, DataThreshold)) {
							
							if (InRegion==false)
							{
							//	logger.info(" ------------------enter the region no  In region no   "+count);
								InRegion=true;
								founds=true;
							
								s=i;
								
							}
							else {
								e=i;
								
							}
							//// in the region i want 
							
//							logger.info("  In region no     "+count+"  ("
//									+ this.getClass().getSimpleName()
//									+ "    "
//									+ (new Throwable()).getStackTrace()[0]
//											.getLineNumber() + "  )  ");
							temp.add(i); // index of point in stroke that
											// have larger than
							ImportantList.add(point);
						}
						else {
							if (InRegion==true){
								// i was in region no i am out 
								InRegion=false;
								founde=true;
							
								e=i;
								count++;
								regions.add(new Point(s,e));
							}
							// the point i do not want 
//							logger.info("I am now out of region "+" ("
//									+ this.getClass().getSimpleName()
//									+ "    "
//									+ (new Throwable()).getStackTrace()[0]
//											.getLineNumber() + "  )  ");
						}
						
					}

				}
			}
			
			// add final region if exist 
			if (InRegion==true){
				e=data.size()-1;
				count++;
				regions.add(new Point(s,e));
			}
			return regions;

		}
		return null;
		
		//return null;
		
	} 
	
	private ArrayList<Point2D> localMaxMin(ArrayList<Point2D> data) {
		ArrayList<Point2D> finals = new ArrayList<Point2D>();
		// now i have to do is locate local min and max
		// get the local min or max by first difference
		double dx, dy = 0.0, delta;
		Point2D p1, p2, d1;
		// logger.info("-----------------------------------------------------------------------------"+"
		// (" + this.getClass().getSimpleName()
		// + " " + (new Throwable()).getStackTrace()[0].getLineNumber()
		// + " ) ");

	//	ArrayList<Point2D> firstDriv = new ArrayList<Point2D>();
		for (int i = 0; i < data.size() - 1; i++) {

			p1 = new PointData();
			p1.setLocation(data.get(i));
			p2 = data.get(i + 1);
			dx = p2.getX() - p1.getX();
			dy = p2.getY() - p1.getY();

			delta = dy / dx;
			if (Math.abs(delta) < ZERO) {
				// logger.info( "function "+getName()+" "+p1.getY()+ "
				// delta ="+delta+" (" + this.getClass().getSimpleName()
				// + " "
				// + (new Throwable()).getStackTrace()[0].getLineNumber()
				// + " ) ");
				// d1=(new Point2D.Double());
				// d1.setLocation(p1.getX(),delta);
				// firstDriv.add(d1);

				// logger.info("This function can be more complex like
				// check window for min or remove added ..."+" (" +
				// this.getClass().getSimpleName()
				// + " "
				// + (new Throwable()).getStackTrace()[0].getLineNumber()
				// + " ) ");
				if (func.isInFunction(p1.getY(), DataThreshold)) {

					finals.add(p1);

				}

			}

		}
		return finals;
	}

	private ArrayList<Integer> locateIndeces(ArrayList<Point2D> locals) {
		Point2D point, point2;
		ArrayList temp = new ArrayList();
		if (locals != null) {

			for (int i = 0; i < locals.size(); i++) {
				point = locals.get(i);
				// search for this x in the main data list to get the indeces of
				// the point
				for (int j = 0; j < data.size(); j++) {

					point2 = data.get(j);

					if (point2 != null) {
						// the same data point from the locals and the main data
						// / then this index is what i need to find
						if (point2.getX() == point.getX()
								&& point2.getY() == point.getY()) {

							// now save this index
							temp.add(j);
							break;
						}

					}
				}

			}
		}
		return temp;
	}

	@Deprecated
	public ArrayList calcuateLocalMinPoints() {
		if (logger.isDebugEnabled()) {
			//  logger.debug("calcuateLocalMinPoints() -     This function is deprecated      (" + this.getClass().getSimpleName() + "    " + (new Throwable()).getStackTrace()[0].getLineNumber() + "  )  "); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		return null;
	}

	@Deprecated
	public ArrayList calcuateLocalMaxPoints() {
		if (logger.isDebugEnabled()) {
			//  logger.debug("calcuateLocalMaxPoints() -     This function is deprecated      (" + this.getClass().getSimpleName() + "    " + (new Throwable()).getStackTrace()[0].getLineNumber() + "  )  "); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		return null;
	}

	@Deprecated
	public ArrayList calcuateLocalAbsolutePoints(int type) {
		if (logger.isDebugEnabled()) {
			//  logger.debug("calcuateLocalAbsolutePoints(int) -     This function is deprecated      (" + this.getClass().getSimpleName() + "    " + (new Throwable()).getStackTrace()[0].getLineNumber() + "  )  "); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		return null;

	}

	
	
	// public Point2D
	public double getSumUpNow() {
		return sumUpNow;
	}

	public ArrayList<Point2D> getDataForPloting() {
		// search the arrry list if there is any points with null, nan infinty
		// and remove it. from the list.

		if (data != null) {
			ArrayList<Point2D> temp = new ArrayList<Point2D>();
			Point2D point;
			double x, y;
			for (int i = 0; i < data.size(); i++) {
				point = data.get(i);

				if (point != null) {
					x = point.getX();
					y = point.getY();

					if (!(Double.isInfinite(x) || Double.isNaN(x)
							|| Double.isInfinite(y) || Double.isNaN(y))) {
						// logger.info(point+" ("
						// + this.getClass().getSimpleName()
						// + " "
						// + (new Throwable()).getStackTrace()[0]
						// .getLineNumber() + " ) ");
						temp.add(point);

					} else {
						// logger.info(" Nan or init "+point.getY()+" ("
						// + this.getClass().getSimpleName()
						// + " "
						// + (new Throwable()).getStackTrace()[0]
						// .getLineNumber() + " ) ");
					}

				}
			}
			return temp;

		}
		return null;
	}

	public String getXName() {
		return xName;
	}

	public void setXName(String name) {
		xName = name;
	}

	public String getYName() {
		return yName;
	}

	public void setYName(String name) {
		yName = name;
	}

	public double getAverage() {
		return average;
	}

	public double getDataThreshold() {
		return DataThreshold;
	}

	public void setDataThreshold(double dataThreshold) {
		DataThreshold = dataThreshold;
	}

	public double getAbsaverage() {
		return absaverage;
	}

	@Deprecated
	public int getFunctionType() {
		if (logger.isDebugEnabled()) {
			//  logger.debug("getFunctionType() -     This function is deprecated    with calculateCornerFunction in strokesttistical data   (" + this.getClass().getSimpleName() + "    " + (new Throwable()).getStackTrace()[0].getLineNumber() + "  )  "); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		return functionType;
	}

	@Deprecated
	public void setFunctionType(int functionType) {
		if (logger.isDebugEnabled()) {
			//  logger.debug("setFunctionType(int) -     This function is deprecated    with calculateCornerFunction in strokesttistical data   (" + this.getClass().getSimpleName() + "    " + (new Throwable()).getStackTrace()[0].getLineNumber() + "  )  "); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		this.functionType = functionType;
	}

	@Deprecated
	public void setData(ArrayList<PointData> cornerData) {
		if (logger.isDebugEnabled()) {
			//  logger.debug("setData(ArrayList<PointData>) -     This function is deprecated    with calculateCornerFunction in strokesttistical data   (" + this.getClass().getSimpleName() + "    " + (new Throwable()).getStackTrace()[0].getLineNumber() + "  )  "); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
	
		if (functionType == BATCH_TYPE) {
			this.data = cornerData;
		}
	}

	@Deprecated
	public void setPointIndex(ArrayList pointIndex) {
		if (logger.isDebugEnabled()) {
			//  logger.debug("setPointIndex(ArrayList) -     This function is deprecated    with calculateCornerFunction in strokesttistical data   (" + this.getClass().getSimpleName() + "    " + (new Throwable()).getStackTrace()[0].getLineNumber() + "  )  "); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		if (functionType == BATCH_TYPE) {
		this.pointIndex = pointIndex;
		}
	}

	@Deprecated 
	public void setAbsaverage(double absaverage) {
		if (logger.isDebugEnabled()) {
			//  logger.debug("setAbsaverage(double) -     This function is deprecated    with calculateCornerFunction in strokesttistical data   (" + this.getClass().getSimpleName() + "    " + (new Throwable()).getStackTrace()[0].getLineNumber() + "  )  "); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		if (functionType == BATCH_TYPE)
			this.absaverage = absaverage;
	}

	public int getMaxLocation() {
		 
		return maxi;
	}

	public int getMinLocation() {
	 
		return mini;
	}
	

}
