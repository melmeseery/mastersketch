/**
 * 
 */
package SketchMaster.Stroke.graphics.shapes;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import SketchMaster.Stroke.StrokeData.DominatePointStructure;
import SketchMaster.Stroke.StrokeData.InkInterface;
import SketchMaster.Stroke.StrokeData.InkPart;
import SketchMaster.Stroke.StrokeData.PointData;
import SketchMaster.Stroke.StrokeData.Stroke;
import SketchMaster.lib.ComputationsGeometry;
import SketchMaster.lib.CurveFitData;
import SketchMaster.system.SystemSettings;
import SketchMaster.system.segmentation.SketchSegmentors;

/**
 * @author TOSHIBA
 *
 */
public class FittedShape implements GuiShape{
	private static final Logger logger = Logger.getLogger(FittedShape.class);
	GeometricPrimitive  shape;
	double Error;
	boolean accepted;
	
	int type;
	
	public final static int TYPE_LINE=0;  
	public final static int TYPE_CIRCLE=1;  
	public final static int TYPE_ELLIPSE=2;  
	public final static int TYPE_POLYLINE=3;
	public final static int TYPE_HELIX=4;
	public final static int TYPE_SPRIAL=5;
	public final static int TYPE_ARC=6;
	
	public	double slope;
	public double  intercept;
	
	
	public void paint(Graphics2D g) {
		shape.paint(g);
		
	}


	public FittedShape(GeometricPrimitive shape,double error, boolean accepted ) {
		super();
		Error = error;
		this.accepted = accepted;
		this.shape = shape;
	}
	public FittedShape() {
		 
	}
	private void fitLine(CurveFitData sum) {
		  double dem=(sum.N* sum.Exx )- sum.Ex_2;
			
		  slope =((sum.N*sum.Exy)-(sum.Ey*sum.Ex))/(dem);
		
		  intercept=(((sum.Ey*sum.Exx)-(sum.Exy*sum.Ex))/(dem));
		
	}


	private void fitLine(Stroke stroke){
		
		CurveFitData sum = stroke.Sums();
		  double dem=(sum.N* sum.Exx )- sum.Ex_2;
			
			  slope =((sum.N*sum.Exy)-(sum.Ey*sum.Ex))/(dem);
			
			  intercept=(((sum.Ey*sum.Exx)-(sum.Exy*sum.Ex))/(dem));
	}
	
	  public static void fitLine(double [] parameters, double [] x, double [] y,
              double [] sigmaY ){

double s=0.0,sx=0.0,sy=0.0,sxx=0.0,sxy=0.0,del;
int numPoints=x.length;
// Null sigmaY implies a constant error which drops
// out of the divisions of the sums.
if( sigmaY != null){
for(int i=0; i < numPoints; i++){

  s   += 1.0/(sigmaY[i]*sigmaY[i]);
  sx  += x[i]/(sigmaY[i]*sigmaY[i]);
  sy  += y[i]/(sigmaY[i]*sigmaY[i]);
  sxx += (x[i]*x[i])/(sigmaY[i]*sigmaY[i]);
  sxy += (x[i]*y[i])/(sigmaY[i]*sigmaY[i]);
}
}else{
s = numPoints;
for(int i=0; i < numPoints; i++){
  sx  += x[i];
  sy  += y[i];
  sxx += x[i]*x[i];
  sxy += x[i]*y[i];
}
}

del = s*sxx - sx*sx;

// Intercept
parameters[0] = (sxx*sy -sx*sxy)/del;
// Slope
parameters[1] = (s*sxy -sx*sy)/del;


// Errors (sd**2) on the:
// intercept
parameters[2] = sxx/del;
// and slope
parameters[3] = s/del;

}
	    
 
	  public FittedShape LineTest( InkInterface ink ){
		  if (ink instanceof Stroke) {
			Stroke st = (Stroke) ink;
			return LineTestStroke(st);
			
		}
		  else if (ink instanceof  InkPart  ){
			  InkPart  inkp=(InkPart) ink;
			  
			  return LineTestPart(inkp);
		  }
		  
		  else {
			  InkPart temp=new InkPart();
			  temp.setPoints(ink.getPoints(), 0, ink.getPointsCount());
			  return LineTestPart(temp);
			  
		  }
		  
	  }
	  
	  public  FittedShape LineTestStroke(Stroke stroke){
			
			 FittedShape  shape=new FittedShape();
			PointData p1,p2;
			
			// try if to create line using the fist and last point of the storke...
			Line line=new Line(stroke.getStartPoint(),stroke.getEndPoint());		
			// line ortognal distance from 
			double ErrorOrthognal=line.OrthognalError(stroke.getPoints());	
			 ErrorOrthognal =  ErrorOrthognal/stroke.getLength();
			logger.info( "  the simple line orthognal error is  "+ ErrorOrthognal);		
			if (ErrorOrthognal<SystemSettings.THERSHOLD_PRE_RECOGNITION_LINE_FIT_ERROR){
			
			shape.fitLine(stroke);
			//data.fitLine(stroke);
			double slope=shape.slope;
			double  intercept=shape.intercept;
		  
			double Error;
			line=new Line(slope,intercept,stroke.getStartPoint(),stroke.getEndPoint());
			// now compute the error and feature area.... 	
		 ErrorOrthognal=line.OrthognalError(stroke.getPoints()); 
		 ErrorOrthognal =  ErrorOrthognal/stroke.getLength();
			logger.info(" orthigonal error is "+ErrorOrthognal);
			if (ErrorOrthognal<SystemSettings.THERSHOLD_RECOGNITION_LINE_FIT_ERROR){
			  shape=new 	 FittedShape  (line,ErrorOrthognal,true);
			}
			else {
				  shape=new 	 FittedShape  (line,ErrorOrthognal,false);
			}
			return shape;
			
			}
			else {
			  shape=new 	 FittedShape  (line,ErrorOrthognal,false);
				
				return shape;
			}
		}

	  

	  private FittedShape LineTestPart(InkPart stroke) {
			 FittedShape  shape=new FittedShape();
				PointData p1,p2;
				

		       int s,e;
		       s=stroke.getStart();
		       e=stroke.getEnd();
				
				shape.fitLine(stroke.Sums());
				//data.fitLine(stroke);
				double slope=shape.slope;
				double  intercept=shape.intercept;
			  
				double Error;
				Line line = new Line(slope,intercept,stroke.getPoint( stroke.getStart()),stroke.getPoint( stroke.getEnd()));
				// now compute the error and feature area.... 	
			 double ErrorOrthognal = line.OrthognalError(stroke.getPoints(), stroke.getStart(), stroke.getEnd()); 
			 ErrorOrthognal =  ErrorOrthognal/stroke.getLength();
				logger.info(" orthigonal error is "+ErrorOrthognal);
				if (ErrorOrthognal<SystemSettings.THERSHOLD_RECOGNITION_LINE_FIT_ERROR){
				  shape=new 	 FittedShape  (line,ErrorOrthognal,true);
				}
				else {
					  shape=new 	 FittedShape  (line,ErrorOrthognal,false);
				}
				return shape;
				
	
	}

	  public FittedShape   circleTest( InkInterface ink ){
		  if (ink instanceof Stroke) {
			Stroke st = (Stroke) ink;
			return   circleTestStroke(st);
			
		}
		  else if (ink instanceof  InkPart  ){
			  InkPart  inkp=(InkPart) ink;
			  
			  return   circleTestPart(inkp);
		  }
		  
		  else {
			  InkPart temp=new InkPart();
			  temp.setPoints(ink.getPoints(), 0, ink.getPointsCount());
			  return   circleTestPart(temp);
			  
		  }
		  
	  }
	  

	  private FittedShape   circleTestPart(InkPart inkp) {
		// TODO Auto-generated method stub
		return null;
	}
	  
	  public ArrayList<PointData>  intersections(Line l, InkInterface stroke){
		  
		  // check if it intersect the box or not... 
		  
                        return  stroke.IntersectionPoints(l);
		  
		
		  
	  }
	  
	public FittedShape  circleTestStroke(Stroke stroke){
					 // NDDR  ndde must be high ,
			//larges chord to length must be low Only if not overtraced..
			//if overtrace // cut at 2 pi
			// must be closed. 
			// feature area... vs. area of ideal ellipse (see my code )	
			 FittedShape  shape=new FittedShape();		

			
			//intially.. get max axis as longest chord then get the 	
			 PointData ps ,pe;
			 // the two points that are furthesst away from each other... 
			  ps = stroke.getLargestChordStart();
			  pe=stroke.getLargestChordEnd();
			  Line l=new Line(ps,pe);			  // this is the major axis. ...

			  PointData mid = l.getMidpoint();
 
			  // the perpendicular  bisector is the ellipse minor axis
	             Line l2=l.getBisector();
			
	             double cx,cy;
	             // the average point is the  center of ellispe .. 
	             cx=stroke.Sums().Ex/(double)stroke.Sums().N;
	             cy=stroke.Sums().Ey/(double)stroke.Sums().N;
			//center is the avearge of the point sumx/n  and sumy/n
			PointData center=new PointData(cx,cy);
			
			
			
			// now this is the center, largest chort for the a, now i want to get the shortest chord length.... 
			// i have to get the intersection with the stroke...... 
			
			ArrayList<PointData> Pintersect = intersections(l2 , stroke);
			 if (Pintersect!=null)
			 {
				 if (Pintersect.size()>1){
					l2.setStartPoint( Pintersect.get(0)); 
					l2.setEndPoint(  Pintersect.get(Pintersect.size()-1));
				 }
					// now get the lenght of the radius...
					
			 
					// logger.info
					logger.info(l2);
					
					logger.info( "  length of small bisection si ....    "  + l2.length());
					// line perpendicular to it. 
					 Ellipse e=new Ellipse(cx,cy,l,l2);
					//e.setEllipseParam(cx, cy,l.length()/2.0, l2.lepoints2ngth()/2.0);
					// now get the error to the ideall... 
					 
					double error=e.fitError(stroke.getPoints());
					
					
					
					double ErrorOrthognal=error;
					
					
					if (ErrorOrthognal<SystemSettings.THERSHOLD_RECOGNITION_ELISPSE_FIT_ERROR){
						  shape=new 	 FittedShape  (e,ErrorOrthognal,true);
						}
						else {
							  shape=new 	 FittedShape  (e,ErrorOrthognal,false);
						}
					
					
					
					return shape;
			 }
			
			return null;
		}

	    public boolean polylineTest2(Stroke stroke){
	    	
	    	ArrayList<DominatePointStructure> pd = stroke.getStatisticalInfo().getDominatePointsIndeces();
	    
	    	boolean poly=true;
	    	 // get the start and end of each sub 
	    	for (int i = 0; i < pd.size(); i++) {
	    		DominatePointStructure temp = pd.get(i);
	    		
				int start = temp.getIndexInInk();
				
				int end = pd.get(i+1).getIndexInInk();
				
				if (start==end )
				{
					continue;
				}			
				
				InkInterface ts = stroke.createSubInkObject(start, end);
			 
				
				// try if to create line using the fist and last point of the storke...
				Line line=new Line(ts.getPoint(0),ts.getPoint( ts.getPoints().size()-1));		
				// line ortognal distance from 
				double ErrorOrthognal=line.OrthognalError(stroke.getPoints(), start, end);	
			 ErrorOrthognal =  ErrorOrthognal/stroke.getLength();
	    		logger.info( "  the simple line orthognal error is  "+ ErrorOrthognal);		
	    		if (ErrorOrthognal>SystemSettings.THERSHOLD_PRE_RECOGNITION_LINE_FIT_ERROR){
	            
	    			return false;
	    		}
	    		
	    	}
	    	//  if all is correct then 
	    	
	    	
	    	return true;
	    }
	    
   public FittedShape polylineTest(Stroke stroke){
	    	
	   FittedShape shape;
	   double e=0;
	   boolean acc=true;
	    	ArrayList<DominatePointStructure> pd = stroke.getStatisticalInfo().getDominatePointsIndeces();
	    
	    	boolean poly=true;
	    	 // get the start and end of each sub 
	    	for (int i = 0; i < pd.size(); i++) {
	    		DominatePointStructure temp = pd.get(i);
	    		
				int start = temp.getIndexInInk();
				
				int end = pd.get(i+1).getIndexInInk();
				
				if (start==end )
				{
					continue;
				}			
				
				InkInterface ts = stroke.createSubInkObject(start, end);
			 
				
				// try if to create line using the fist and last point of the storke...
				Line line=new Line(ts.getPoint(0),ts.getPoint( ts.getPoints().size()-1));		
				// line ortognal distance from 
				double ErrorOrthognal=line.OrthognalError(stroke.getPoints(), start, end);	
			 ErrorOrthognal =  ErrorOrthognal/stroke.getLength();
			 e+=ErrorOrthognal;
	    		logger.info( "  the simple line orthognal error is  "+ ErrorOrthognal);		
	    		if (ErrorOrthognal>SystemSettings.THERSHOLD_PRE_RECOGNITION_LINE_FIT_ERROR){
	            
	    			//return false;
	    			acc=false;
	    			break;
	    			
	    		}
	    		
	    	}
	    
	    	//  if all is correct then 
	    	
	    	shape=new FittedShape(null, e, acc);
	    	shape.type=TYPE_POLYLINE;
	    	
	    	
	    	return shape;
	    }

	    
	    
}
