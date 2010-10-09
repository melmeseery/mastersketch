/**
 * 
 */
package SketchMaster.Stroke.graphics.shapes;
import java.awt.BasicStroke;
import java.awt.Color;

import java.awt.Stroke;
import java.util.ArrayList;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import org.apache.log4j.Logger;







import SketchMaster.Stroke.StrokeData.PointData;
import SketchMaster.lib.ComputationsGeometry;
import SketchMaster.system.SystemSettings;
import SketchMaster.system.segmentation.SketchSegmentors;

/**
 * @author maha
 * 
 */
public class Line extends GeometricPrimitive {
	/**
	 * 
	 */
	private static final Logger logger = Logger.getLogger(Line.class);
	private static final long serialVersionUID = -4548396929621205142L;
	public static final int T_INTERSECTION = 0;
	public static final int L_INTERSECTION = 1;
	public static final int X_INTERSECTION = 2;
	public static final int NO_INTERSECTION = 3;
	public static final double Tolerance = SystemSettings.EQUAL_LIMIT;
	double x2, y2, x1, y1;
	//private PointData p1,p2;
	public int type;
	transient static Stroke bs = new BasicStroke(3);
	double slope = Double.NaN;

	public static final int PARALLEL_TO_X = 1; // / line is like this
												// ---------- (horizontal)
	public static final int PARALLEL_TO_Y = 2;// / line is like this |
												// (vertical)
	public static final int NORMAL = 3;// any other line
	private static final double ThetaTolerance = 15.0;
	//private static final String SystemSetttings = null;
	int LineTYPE = NORMAL;
	  private PolygonShape points;
	    private Vertex m_vertices[];
	    public long time_stamp;
		private double intercept;
		private boolean SlopeComputed=false;
		private boolean interceptComputed=false;
 public Line (PointData data, double m){
	  
		x1 = data.getX();
		y1 = data.getY();
		SlopeComputed=true;
		slope=m;
		x2=0;
		
		intercept=-slope*x1+y1;
		
		interceptComputed=true;
		
		y2=solveY(x1);
		  PointData p1=new PointData(x1,y1);
		  PointData  p2=new PointData(x2,y2);
			setStartPoint(p1);
			setEndPoint(p2);
		
 }
	  public Line(PointData data,PointData data2)
	    {
		  
			x1 = data.getX();
			y1 = data.getY();
			x2 = data2.getX();
			y2 = data2.getY();
			SlopeComputed=false;
			interceptComputed=false;
			slope = Slope();
 
			setStartPoint((PointData) data.clone());
			setEndPoint((PointData) data2.clone());
 
		 
		 intercept= Intercept();
		 
		 logger.trace("  the strart of point is "+StartPoint+"  end is "+EndPoint+"   slope is  "+slope+"   intercept "+intercept);
 
	    }
	
	public  Line(double m, double intercept,PointData data,PointData data2){
			// 
	
		interceptComputed=true;
			//slope=m;
			this.intercept=intercept;
			x1=data.getX();
			
			y1 = (x1 * m) + intercept;
			x2=data2.getX();
			y2 = (x2 * m) + intercept;
				SlopeComputed=false;
			Slope();
		
			  PointData p1=new PointData(x1,y1);
			  PointData  p2=new PointData(x2,y2);
				setStartPoint(p1);
				setEndPoint(p2);
				
				logger.info("  slope is  = "+slope+ "   m  "+ m);
				slope=m;
			if (Math.abs(slope-m)>SystemSettings.ZERO_COMPARE)
			{
				logger.error(" ERROR COMPUTING SLOPE CHECK   m="+m+"  slope is ="+slope);
			}
			// equation is y= x*m+b;
			
			 logger.info("  the strart of point is "+StartPoint+"  end is "+EndPoint+"   slope is  "+slope+"   intercept "+intercept);
	  }
	  
	  public Line(double x1, double y1, double x2, double y2)
	    {
			SlopeComputed=false;
			interceptComputed=false;
		  this.x1=x1;
		  this.y1=y1;
		  this.x2=x2;
		  this.y2=y2;

		  Slope();
		  PointData p1=new PointData(x1,y1);
		  PointData  p2=new PointData(x2,y2);
			setStartPoint(p1);
			setEndPoint(p2);
 
		 
		 intercept= Intercept();
//	       logger.info(" adddeddd for mit "+" (" + this.getClass().getSimpleName()
//				+ "    " + (new Throwable()).getStackTrace()[0].getLineNumber()
//				+ "  )  ");
		 
		 logger.info("  the strart of point is "+StartPoint+"  end is "+EndPoint+"   slope is  "+slope+"   intercept "+intercept);
	    }
	public void paint(Graphics2D g) {
		// logger.info("draw line ");
		// g.setColor(Color.getHSBColor((float)Math.random(), (float)0.9,
		// (float)0.5));
		Stroke s = g.getStroke();
		g.setStroke(bs);
		g.setColor(Color.BLACK);
		// logger.info("draw line "+ " x = " +x +" y "+ y+ " x1 " +x1+ "
		// y1 " +y1+ " k "+k + " c "+c);
		// 
		// if
		// (Double.isNaN(x)||Double.isNaN(y)||Double.isNaN(x1)||Double.isNaN(y1))
		// {
		 //logger.info("ttttttttttttttttttttttttttttttttttttttttttttttttttt");
		// }
	//	g.drawString(this.toString(), (int) x1, (int) y1);
		g.drawLine( (int) x1, (int) y1,(int) x2, (int) y2);
		g.setStroke(s);
		super.paint(g);
	}

	public void setParam(ArrayList Param) {
		// 
		// this.param = param;
	}


	
//	public double slope() {
//		//if (Double.isNaN(slope) && LineTYPE != PARALLEL_TO_X) {
//			slope = Slope();
//	//	}
//		return slope;
//	}
	  private double  Intercept() {
			 if (!interceptComputed){
				
				 intercept=y1-(x1*Slope());
				 
				  
				 interceptComputed=true;
				 return intercept;
			 }
			 else{
				 return intercept;
			 }
				
			}
	public double Slope() {
		if(!SlopeComputed){
		double s = Double.POSITIVE_INFINITY;

		double dx = x2 - x1;
		double dy = y2 - y1;
		if (Math.abs(dx) <=SystemSettings.ZERO) {
			// logger.info("this line is vertical " +dx);
			LineTYPE = PARALLEL_TO_Y;
			//return Double.NaN;
			slope=Double.NaN;
		}

		s = dy / dx;
		if (Math.abs(s) <= SystemSettings.ZERO_SLOPE) {
			// logger.info("this line is horizontal "+s);
			LineTYPE = PARALLEL_TO_X;
			 s=0.0;

		} else {
			//logger.info("this line is normal with slope =  "+s);
			LineTYPE = NORMAL;
			
		}
		SlopeComputed=true;
		slope=s;
		return s;
		}else 
		{
			return slope;
		}
	}

	@Deprecated
	public void setLineParams(double k2, double c2, PointData data,
			PointData data2) {
//		// 
//		k = k2;
//		c = c2;
//		x1 = data.getX();
//		y1 = data.getY();
//
//		// now get a new line value
//		x2 = data2.getX();
//		y2 = (x1 * k) + c;

		setLineParams(data, data2);
	}

	public void setLineParams(PointData data, PointData data2) {
		x1 = data.getX();
		y1 = data.getY();
		x2 = data2.getX();
		y2 = data2.getY();
		// logger.info(data);
		// logger.info("data 2 "+data2);
		slope = Slope();
//		logger.info("Set the line parameter in the lien classsssssssssssssss"+" (" + this.getClass().getSimpleName()
//				+ "    " + (new Throwable()).getStackTrace()[0].getLineNumber()
//				+ "  )  ");
		setStartPoint((PointData) data.clone());
		setEndPoint((PointData) data2.clone());
	}

	public boolean isParallel(Line l2) {
		// logger.info(" check parallel of the library in interseciotn .
		// ??? "+" (" + this.getClass().getSimpleName()
		// + " " + (new Throwable()).getStackTrace()[0].getLineNumber()
		// + " ) ");
		/*
		 * 
		 * double[] intersection=new double [2]; int
		 * parallel=ComputationsGeometry.findLineSegmentIntersection(x,y,x1,y1,l2.x,l2.y,l2.x1,l2.y1,intersection); //
		 * TODO Is parrel check between two lines if
		 * (parallel==-1||parallel==-2) return true ; else return false;
		 * 
		 */
		// / here is another implemnation of the parallel lines

		double s1 = Slope();
		double s2 = l2.Slope();
		// first check if one of them is nan
		// both are parallel to y
		// logger.info("Slope 1= "+s1+" slope 2 = "+s2+" (" +
		// this.getClass().getSimpleName()
		// + " " + (new Throwable()).getStackTrace()[0].getLineNumber()
		// + " ) ");
	
		if (Double.isNaN(s1) && Double.isNaN(s2))
			return true;

		// only one of them is parallel to y then they are not parallel
		else if (Double.isNaN(s1) || Double.isNaN(s2))
		{
			if (this.LineTYPE !=NORMAL)
			if (this.LineTYPE ==l2.LineTYPE)
			return true;
		  if (Double.isNaN(s1)){
			  
			  
			  // check if x's of l2 are not nearly 
			  double deltax=l2.x2-l2.x1;
			  if (Math.abs(deltax)<SystemSettings.DISTANCE_LIMIT)
				  return true;
			  else 
				  return false;
			  
			  
		  }	
		  else if (Double.isNaN(s2)){
			  
			  // check if x's of l2 are not nearly 
			  double deltax=x2-x1;
			  if (Math.abs(deltax)<SystemSettings.DISTANCE_LIMIT)
				  return true;
			  else 
				  return false;
			  
			  
		  }
			
			return false;
		}
		// if both zeros
		else {
			double sub = Math.abs(Math.abs(s2) - Math.abs(s1));
			if (sub < Tolerance) // difference of slope less than tolerance
									// then they have same slope
			{
				// logger.info(" nearly paralllllllllellllllllll ");
				return true; // they are parallel
			} else {// else difference slope then they are difference.
				return false;
			}

		}

		// logger.info("
		//		
		// return false;
	}

	public boolean isOrthogonal(Line l2) {
		double m1, m2, mm;
		m1 = this.Slope();
		m2 = l2.Slope();
		mm = m1 * m2;

		if (mm == -1) // if slope1*slope2 = -1 then perpendicular exactly;
			return true;

		else {
			// first check if either of them has a nan slope
			if (this.LineTYPE == PARALLEL_TO_X) // l1 horzinal
			{

				if (l2.LineTYPE == PARALLEL_TO_Y)
					return true;
				else
					return false;
			} else if (this.LineTYPE == PARALLEL_TO_Y) { // l1 vertical

				if (l2.LineTYPE == PARALLEL_TO_X)
					return true;
				else
					return false;
			}

			else if (l2.LineTYPE == PARALLEL_TO_X) { // l2 horizontal

				if (LineTYPE == PARALLEL_TO_Y)
					return true;
				else
					return false;
			}

			else if (l2.LineTYPE == PARALLEL_TO_Y) { // l2 vertical

				if (LineTYPE == PARALLEL_TO_X)
					return true;
				else
					return false;
			}

			else { // other both has slope not nan or 0

				// logger.info(" slope of l1 = "+slope+ " slope of l2
				// ="+l2.slope() );
				// calcuate theta from this equations
				// tan 0 = |m2+m1/1+m1m2|

				double m1_m2 = m2 - m1;
				// get the angle between lines
				double theta = Math.atan(Math.abs(m1_m2 / (1.0 + mm)));

				// convert to degreee
				double degree = RadToDegree(theta);
				if (degree > 90.0) {
					// degree-=180;
					// logger.info(degree);
				}
				//			

				double test = Math.abs(90.0 - degree);
				// logger.info(degree+" degree the value "+test +" is the
				// difference in degrees (" + this.getClass().getSimpleName()
				// + " "
				// + (new Throwable()).getStackTrace()[0].getLineNumber()
				// + " ) ");
				//			

				if (test < ThetaTolerance) {
					// logger.info(degree+" degree the value "+test +" is
					// the difference in degrees (" +
					// this.getClass().getSimpleName()
					// + " "
					// + (new Throwable()).getStackTrace()[0].getLineNumber()
					// + " ) ");
					// logger.info(" -----------------------------------
					// nearlyyyyyyyyyyyyyyyyyy orthegonal ");
					return true;
				} else {

					// logger.info("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&"+degree+"
					// degree the value "+test +" is the difference in degrees
					// (" + this.getClass().getSimpleName()
					// + " "
					// + (new Throwable()).getStackTrace()[0].getLineNumber()
					// + " ) ");
					return false;
				}

			}

		}
 
		// return false;
	}

	public double RadToDegree(double theta) {
		double returndouble = ((theta * 180.0) / (Math.PI));
		return returndouble;

	}

	public double DegreeToRad(double theta) {
		double returndouble = ((theta * Math.PI) / (180.0));
		return returndouble;
	}

	@Deprecated 
	public boolean isIntersectedSegments(Line l2) {
		
		
		double[] intersection = new double[2];
		int inters = ComputationsGeometry.findLineSegmentIntersection(x1, y1, x2,
				y2, l2.x1, l2.y1, l2.x2, l2.y2, intersection);

		if (inters > 0)
			return true;
		else
			// // TODO implement if intersected between two lines
			// logger.info(" // TODO implement if intersected between two
			// lines ");
			/**
			 * 
			 * 
			 * http://www.ahristov.com/tutorial/geometry-games/intersection-lines.html
			 * public Point intersection(// line intersection 0018 int x1,int
			 * y1,int x2,int y2, 0019 int x3, int y3, int x4,int y4 0020 ) {
			 * 0021 int d = (x1-x2)*(y3-y4) - (y1-y2)*(x3-x4); 0022 if (d == 0)
			 * return null; 0023 0024 int xi =
			 * ((x3-x4)*(x1*y2-y1*x2)-(x1-x2)*(x3*y4-y3*x4))/d; 0025 int yi =
			 * ((y3-y4)*(x1*y2-y1*x2)-(y1-y2)*(x3*y4-y3*x4))/d; 0026 0027 return
			 * new Point(xi,yi); 0028 }
			 * 
			 * ///////////////////////////// >0013 public Point intersection( //
			 * segment interseciont 0014 int x1,int y1,int x2,int y2, 0015 int
			 * x3, int y3, int x4,int y4 0016 ) { 0017 int d = (x1-x2)*(y3-y4) -
			 * (y1-y2)*(x3-x4); 0018 if (d == 0) return null; 0019 0020 int xi =
			 * ((x3-x4)*(x1*y2-y1*x2)-(x1-x2)*(x3*y4-y3*x4))/d; 0021 int yi =
			 * ((y3-y4)*(x1*y2-y1*x2)-(y1-y2)*(x3*y4-y3*x4))/d; 0022 0023 Point
			 * p = new Point(xi,yi); 0024 if (xi < Math.min(x1,x2) || xi >
			 * Math.max(x1,x2)) return null; 0025 if (xi < Math.min(x3,x4) || xi >
			 * Math.max(x3,x4)) return null; 0026 return p; 0027 }
			 * 
			 * 
			 * 
			 */

			return false;
	}

	public int getIntersectionType(Line l2) {
		
		PointData a,b,c,d;
    	a=new PointData(x1,y1);
    	b=new PointData(x2,y2);
    	
    	c=new PointData(l2.x1,l2.y1);
    	d=new PointData(l2.x2,l2.y2);
    	
    	
		
		if (ComputationsGeometry.Intersect(a, b, c, d))
		{
			
			if (ComputationsGeometry.IntersectProp(a, b, c, d))
			return   X_INTERSECTION;
			else 
				return T_INTERSECTION;
			
			
		}
		else return NO_INTERSECTION ;
		
//		System.out
//				.println("//TODO implements the intersection type fo between ---------line.java 322");
//		System.out
		
	//	if (isIntersectedSegments)isIntersectedSegments(l2)
//				.println("return either t or l or x or no intersections--------------------line.java  323");
		//return 0;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + LineTYPE;
		long temp;
//		temp = Double.doubleToLongBits(c);
//		result = PRIME * result + (int) (temp ^ (temp >>> 32));
//		temp = Double.doubleToLongBits(k);
//		result = PRIME * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(slope);
		result = PRIME * result + (int) (temp ^ (temp >>> 32));
		result = PRIME * result + type;
		temp = Double.doubleToLongBits(x1);
		result = PRIME * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(x2);
		result = PRIME * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y1);
		result = PRIME * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y2);
		result = PRIME * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Line other = (Line) obj;
		if (LineTYPE != other.LineTYPE)
			return false;
//		if (Double.doubleToLongBits(c) != Double.doubleToLongBits(other.c))
//			return false;
//		if (Double.doubleToLongBits(k) != Double.doubleToLongBits(other.k))
//			return false;
		if (Double.doubleToLongBits(slope) != Double
				.doubleToLongBits(other.slope))
			return false;
		if (type != other.type)
			return false;
		if (Double.doubleToLongBits(x1) != Double.doubleToLongBits(other.x1))
			return false;
		if (Double.doubleToLongBits(x2) != Double.doubleToLongBits(other.x2))
			return false;
		if (Double.doubleToLongBits(y1) != Double.doubleToLongBits(other.y1))
			return false;
		if (Double.doubleToLongBits(y2) != Double.doubleToLongBits(other.y2))
			return false;
		return true;
	}
@Deprecated 
	public boolean isIntersectedLine(Line l2) {
		double[] intersection = new double[2];
		int inters = ComputationsGeometry.findLineSegmentIntersection(x1, y1, x2,
				y2, l2.x1, l2.y1, l2.x2, l2.y2, intersection);

		if (inters >= 0)
			return true;
		else
			return false;
	}

    public double chooseApproximateAngle(double angle_set[])
    {
        int smallest_index = 0;
        double smallest_error = Math.abs(getAngle() - angle_set[0]);
        for(int i = 0; i < angle_set.length; i++)
            if(smallest_error > Math.abs(getAngle() - angle_set[i]))
            {
                smallest_error = Math.abs(getAngle() - angle_set[i]);
                smallest_index = i;
            }

		double returndouble = angle_set[smallest_index];
        return returndouble;
    }

    public boolean containsGeometricObjects(GeometricObject objects[])
    {
        for(int i = 0; i < objects.length; i++)
            if(!containsGeometricObject(objects[i]))
                return false;
        return true;
    }
    public boolean containsGeometricObject(GeometricObject object)
    {
		boolean returnboolean = getPolygonalBounds().containsGeometricObject(object);
        return returnboolean;
    }

    public PolygonShape getPolygonalBounds()
    {
        PolygonShape result = new PolygonShape();
        int x_1 = (int)x1;
        int x_2 = (int)x2;
        int y_1 = (int)y1;
        int y_2 = (int)y2;
        int left_x;
        int right_x;
        int left_y;
        int right_y;
        if(x_1 == x_2)
        {
            left_x = x_1;
            right_x = x_2;
            if(y1 < y2)
            {
                left_y = y_1;
                right_y = y_2;
            } else
            {
                left_y = y_2;
                right_y = y_1;
            }
            result.addPoint(left_x - 1, left_y - 1);
            result.addPoint(right_x - 1, right_y + 1);
            result.addPoint(right_x + 1, right_y + 1);
            result.addPoint(left_x + 1, left_y - 1);
            return result;
        }
        if(x_1 < x_2)
        {
            left_x = x_1;
            left_y = y_1;
            right_x = x_2;
            right_y = y_2;
        } else
        {
            left_x = x_2;
            left_y = y_2;
            right_x = x_1;
            right_y = y_1;
        }
        result.addPoint(left_x - 1, left_y + 1);
        result.addPoint(right_x + 1, right_y + 1);
        result.addPoint(right_x + 1, right_y - 1);
        result.addPoint(left_x - 1, left_y - 1);
        return result;
    }
    public double getAngle()
    {
		double returndouble = Math.atan2(y2 - y1, x2 - x1);
        return returndouble;
    }
    public Vertex[] getOriginalVertices()
    {
        if(m_vertices == null)
            return null;
        Vertex ret[] = new Vertex[m_vertices.length];
        for(int i = 0; i < m_vertices.length; i++)
            ret[i] = m_vertices[i];
        return ret;
    }

    public PolygonShape getDataPoints()
    {
        return points;
    }

    public PolygonShape toPolygon()
    {
        PolygonShape result = new PolygonShape();
        result.addPoint((int)x1, (int)y1);
        result.addPoint((int)x2, (int)y2);
        result.setDataPoints(new PolygonShape(points));
        return result;
    }
    public double length()
    {
		double returndouble = PointData.distance(x1, y1, x2, y2);
        return returndouble;
    }

    public PointData center()
    {
		PointData returnPointData = new PointData((x1 + x2) / 2D, (y1 + y2) / 2D);
        return returnPointData;
    }
    public void setTimeStamp(long time_stamp)
    {
        this.time_stamp = time_stamp;
    }

    public long getTimeStamp()
    {
        return time_stamp;
    }

    public void swapPoints()
    {
        double tmp = x1;
        x1 = x2;
        x2 = tmp;
        tmp = y1;
        y1 = y2;
        y2 = tmp;
    }

    public void pointRight()
    {
        if(x1 > x2)
            swapPoints();
    }

    public void pointLeft()
    {
        if(x1 < x2)
            swapPoints();
    }

    public boolean isIntersect(Line l2){
    	
    	PointData a,b,c,d;
    	a=new PointData(x1,y1);
    	b=new PointData(x2,y2);
    	
    	c=new PointData(l2.x1,l2.y1);
    	d=new PointData(l2.x2,l2.y2);
    	
    	return ComputationsGeometry.Intersect(a,b,c,d);
    	//return true;
    	
    }
    
    public void pointUp()
    {
        if(y1 > y2)
            swapPoints();
    }
    public PointData getMidpoint()
    {
		PointData returnPointData = new PointData((x1 + x2) / 2D, (y1 + y2) / 2D);
        return returnPointData;
    }

    public PointData getIntersection(Line line)
       
    {
        double dx1 = x2 - x1;
        double dx2 = line.x2 - line.x1;
        if(dx1 == 0.0D && dx2 == 0.0D)
            return null;
        if(dx1 == 0.0D || dx2 == 0.0D)
            if(dx1 == 0.0D)
            {
                PointData result = new PointData(x1, line.y1 - ((line.y2 - line.y1) / (line.x2 - line.x1)) * (line.x1 - x1));
                return result;
            } else
            {
                PointData result = new PointData(line.x1, y1 - ((y2 - y1) / (x2 - x1)) * (x1 - line.x1));
                return result;
            }
        if(GeometryUtil.equalDoubles((y2 - y1) / (x2 - x1), (line.y2 - line.y1) / (line.x2 - line.x1), 0.001D))
        {
        	return null;
          //    throw new GeometricComputationException("Lines parallel");
        } else
        {
            double a = (y2 - y1) / (x2 - x1);
            double b = y1 - x1 * a;
            double c = (line.y2 - line.y1) / (line.x2 - line.x1);
            double d = line.y1 - line.x1 * c;
            double x = (d - b) / (a - c);
            PointData result = new PointData(x, a * x + b);
            return result;
        }
    }
	public int relativeCCW(double x, double y) {
		 java.awt.geom.Line2D.Double ltemp=new  Line2D.Double ();
		 ltemp.setLine(new PointData(x1,y1), new PointData(x2,y2));
		  
	//	Point2D.Double p=new Point2D.Double(x,y);
		int returnint = ltemp.relativeCCW(x, y);
	 return returnint;	
	}
	public static boolean linesIntersect(int i, int j, int l, int m, int n, int o, int p, int q) {
		boolean returnboolean = Line2D.linesIntersect(i, j, l, m, n, o, p, q);
	return returnboolean;
		//return false;
	}
	public static double ptSegDist(int i, int j, int l, int m, double x, double y) {
		  
		double returndouble = Line2D.ptSegDist(i, j, l, m, x, y);
		return returndouble;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		
		String s="Line from  "+this.iStart+" x= "+x1+"y="+y1+"  to "+this.iEnd+" x2= "+x2+"y2="+y2;
		return s;
	}
	public boolean isCollinear(Line l2) {
	 	PointData a,b,c,d;
		a=new PointData(x1,y1);
    	b=new PointData(x2,y2);
    	
    	c=new PointData(l2.x1,l2.y1);
    	d=new PointData(l2.x2,l2.y2);
    	
    	return ComputationsGeometry.Collinear(a,b,d)&&ComputationsGeometry.Collinear(a,b,c);
	}

	public double DifferanceFromPoint(PointData point){
		//distance betweeoin point and center suptracted from teh radius. 
		PointData  p1,p2;
		  p1=new PointData(x1,y1);
		  p2=new PointData(x2,y2);
			double distance=ComputationsGeometry.DistancePointLine(point, p1, p2);

//		double distance=ComputationsGeometry.DistancePointLine(point, this.getStartPoint(), this.getEndPoint());
		
		double temp=0;
//		logger.info("  distance from point "+distance+"   "+point+"   "+ this.getStartPoint()+"  "+ this.getEndPoint() + " ( " + this.getClass().getSimpleName()
//				+ "    " + (new Throwable()).getStackTrace()[0].getLineNumber()
//				+ "  )  ");
		temp=Math.sqrt( distance*distance  );
		
	     return temp;	
	
	}
	public double OrthognalError(ArrayList<PointData> points2) {
		double len;
		double error = 0.0;
		PointData pointk,p1,p2;
		  p1=new PointData(x1,y1);
		  p2=new PointData(x2,y2);
		  
		// firstly get the first point in curve.
		for (int i = 0; i < points2.size(); i++) {

			pointk = (PointData) points2.get(i);
					
			len = ComputationsGeometry.DistancePointLine(pointk.getPointLocation(), p1,p2,
					length());
	           //  logger.info("  lenght  = "+len);
			if (!(Double.isNaN(len))) {
				error += len;
				
			 
			}
			else {
			 
				
			}

		}
 
		return Math.sqrt(error*error);
		//return (error * error);
		 
	}
	
	public double OrthognalError(ArrayList<PointData> points2, int s, int e) {
		double len;
		double error = 0.0;
		PointData pointk,p1,p2;
		  p1=new PointData(x1,y1);
		  p2=new PointData(x2,y2);
		  
		// firstly get the first point in curve.
		for (int i = s; i <e; i++) {

			pointk = (PointData) points2.get(i);
					
			len = ComputationsGeometry.DistancePointLine(pointk.getPointLocation(), p1,p2,
					length());
	           //  logger.info("  lenght  = "+len);
			if (!(Double.isNaN(len))) {
				error += len;
				
			 
			}
			else {
			 
				
			}

		}
 
		return (error);
		//return (error * error);
		 
	}
	public double solveX(double y) {
		
	 
	 
	 if ( LineTYPE==PARALLEL_TO_Y)
		 return StartPoint.getX();
	 
 
	   double x=(y-Intercept())/Slope();
	 
		return x;
	}
	public double solveY(double x) {
		
		 
		 
		 if ( LineTYPE==PARALLEL_TO_X)
			 return StartPoint.getY();
		 
	 
		   double y=x*Slope()+Intercept();
		 
			return y;
		}
	public Line getBisector() {
		 
		  PointData mid = getMidpoint();
 
		  
		  double slopeOfline=-1.0/(Slope());
		  
             Line l2=new Line(mid,slopeOfline);
		return l2;
	}
    
}
