/**
 * 
 */
package SketchMaster.Stroke.graphics.shapes;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import SketchMaster.Stroke.StrokeData.DominatePointStructure;
import SketchMaster.Stroke.StrokeData.InkInterface;
import SketchMaster.Stroke.StrokeData.InkPart;
import SketchMaster.Stroke.StrokeData.PointData;
import SketchMaster.Stroke.StrokeData.Stroke;
import SketchMaster.lib.CurveFitData;
import SketchMaster.system.SystemSettings;

/**
 * @author TOSHIBA
 * 
 */
public class ShapeRecognizier {
	private static final Logger logger = Logger
			.getLogger(ShapeRecognizier.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {

	}

	public FittedShape LineTest(InkInterface ink) {
		if (ink instanceof Stroke) {
			Stroke st = (Stroke) ink;
			return LineTestStroke(st);

		} else if (ink instanceof InkPart) {
			InkPart inkp = (InkPart) ink;

			return LineTestPart(inkp);
		}

		else {
			InkPart temp = new InkPart();
			temp.setPoints(ink.getPoints(), 0, ink.getPointsCount());
			return LineTestPart(temp);

		}

	}

	private FittedShape LineTestStroke(Stroke stroke) {

		FittedShape shape = new FittedShape();
		PointData p1, p2;

		// try if to create line using the fist and last point of the storke...
		Line line = new Line(stroke.getStartPoint(), stroke.getEndPoint());
		// line ortognal distance from
		double ErrorOrthognal = line.OrthognalError(stroke.getPoints());
		ErrorOrthognal = ErrorOrthognal / stroke.getLength();
		logger.info("  the simple line orthognal error is  " + ErrorOrthognal);
		if (ErrorOrthognal < SystemSettings.THERSHOLD_PRE_RECOGNITION_LINE_FIT_ERROR) {

			CurveFitData sum = stroke.Sums();
			double dem = (sum.N * sum.Exx) - sum.Ex_2;

			double slope = ((sum.N * sum.Exy) - (sum.Ey * sum.Ex)) / (dem);

			double intercept = (((sum.Ey * sum.Exx) - (sum.Exy * sum.Ex)) / (dem));

			double Error;
			line = new Line(slope, intercept, stroke.getStartPoint(), stroke
					.getEndPoint());
			// now compute the error and feature area....
			ErrorOrthognal = line.OrthognalError(stroke.getPoints());
			ErrorOrthognal = ErrorOrthognal / stroke.getLength();
			logger.info(" orthigonal error is " + ErrorOrthognal);
			if (ErrorOrthognal < SystemSettings.THERSHOLD_RECOGNITION_LINE_FIT_ERROR) {
				shape = new FittedShape(line, ErrorOrthognal, true);
			} else {
				shape = new FittedShape(line, ErrorOrthognal, false);
			}
			return shape;

		} else {
			shape = new FittedShape(line, ErrorOrthognal, false);

			return shape;
		}
	}

	private FittedShape LineTestPart(InkPart stroke) {
		FittedShape shape = new FittedShape();
		PointData p1, p2;

		int s, e;
		s = stroke.getStart();
		e = stroke.getEnd();

		// shape.fitLine(stroke.Sums());

		CurveFitData sum = stroke.Sums();
		double dem = (sum.N * sum.Exx) - sum.Ex_2;

		double slope = ((sum.N * sum.Exy) - (sum.Ey * sum.Ex)) / (dem);

		double intercept = (((sum.Ey * sum.Exx) - (sum.Exy * sum.Ex)) / (dem));

		//		

		double Error;
		Line line = new Line(slope, intercept, stroke.getPoint(stroke
				.getStart()), stroke.getPoint(stroke.getEnd()));
		// now compute the error and feature area....
		double ErrorOrthognal = line.OrthognalError(stroke.getPoints(), stroke
				.getStart(), stroke.getEnd());
		ErrorOrthognal = ErrorOrthognal / stroke.getLength();
		logger.info(" orthigonal error is " + ErrorOrthognal);
		if (ErrorOrthognal < SystemSettings.THERSHOLD_RECOGNITION_LINE_FIT_ERROR) {
			shape = new FittedShape(line, ErrorOrthognal, true);
		} else {
			shape = new FittedShape(line, ErrorOrthognal, false);
		}
		//logger.info("  fitted shape is  "+shape);
		
		
		return shape;

	}

	public FittedShape circleTest(InkInterface ink) {
		if (ink instanceof Stroke) {
			Stroke st = (Stroke) ink;
			return circleTestStroke(st);

		}
		// else if (ink instanceof InkPart ){
		// InkPart inkp=(InkPart) ink;
		//			  
		// return circleTestPart(inkp);
		// }
		//		  
		// else {
		// InkPart temp=new InkPart();
		// temp.setPoints(ink.getPoints(), 0, ink.getPointsCount());
		// return circleTestPart(temp);
		//			  
		// }
		return null;
	}

	private FittedShape circleTestStroke(Stroke stroke) {

		double cx, cy;
		// the average point is the center of ellispe ..
		cx = stroke.Sums().cx;
		cy = stroke.Sums().cy;

		double radius = stroke.Sums().mcr;

		PointData ps, pe;
		// the two points that are furthesst away from each other...
		ps = stroke.getLargestChordStart();
		pe = stroke.getLargestChordEnd();
		Line l = new Line(ps, pe); // this is the major axis. ...

		PointData mid = l.getMidpoint();

		// the perpendicular bisector is the ellipse minor axis
		Line l2 = l.getBisector();
		ArrayList<PointData> Pintersect = intersections(l2, stroke);
		if (Pintersect != null) {
			if (Pintersect.size() > 1) {
				l2.setStartPoint(Pintersect.get(0));
				l2.setEndPoint(Pintersect.get(Pintersect.size() - 1));
			}
		}

		double ratio = l.length() / l2.length(); // major / minor axis must be nearly one. 

		// this ration must be nearly == 1

     	FittedShape shape ;
		double ratio2 = l.length() / (radius*2.0);  // axis/diameter
		
	   Circle c = new Circle(radius, cx, cy);
		// compute
		double ErrorOrthognal=c.fitError(stroke.getPoints());
		// now compute the are
            if (checkRatio(ratio2) & checkRatio(ratio)){
		// 
            	
            	if (ErrorOrthognal<SystemSettings.THERSHOLD_RECOGNITION_CIRCLE_FIT_ERROR)
            	{ 
            		shape = new FittedShape(c, ErrorOrthognal, true);
	             
	             
	             return shape;
            	}
            }
            
            
            
            shape = new FittedShape(c, ErrorOrthognal, false);
            return shape;
		// now compute the min / max axis
		// the radius. is

	 
	}
    private boolean checkRatio(double ratio){
    	logger.warn("  \\ To Do:  checkRatio (check if ration is near 1 )   ");
    	return false;
    }
	public FittedShape ellipseTest(InkInterface ink) {
		if (ink instanceof Stroke) {
			Stroke st = (Stroke) ink;
			return ellispeTestStroke(st);

		} else if (ink instanceof InkPart) {
			InkPart inkp = (InkPart) ink;

			return ellipseTestPart(inkp);
		}

		else {
			InkPart temp = new InkPart();
			temp.setPoints(ink.getPoints(), 0, ink.getPointsCount());
			return ellipseTestPart(temp);

		}

	}

	private FittedShape ellipseTestPart(InkPart inkp) {
		logger.trace("   //  ellipseTestPart TODO Auto-generated method stub");
		// TODO Auto-ellipseTestPart generated method stub
		return null;
	}

	public ArrayList<PointData> intersections(Line l, InkInterface stroke) {

		// check if it intersect the box or not...

		return stroke.IntersectionPoints(l);

	}

	private FittedShape ellispeTestStroke(Stroke stroke) {
		// NDDR ndde must be high ,
		// larges chord to length must be low Only if not overtraced..
		// if overtrace // cut at 2 pi
		// must be closed.
		// feature area... vs. area of ideal ellipse (see my code )
		FittedShape shape = new FittedShape();

		// intially.. get max axis as longest chord then get the
		PointData ps, pe;
		// the two points that are furthesst away from each other...
		ps = stroke.getLargestChordStart();
		pe = stroke.getLargestChordEnd();
		Line l = new Line(ps, pe); // this is the major axis. ...

		PointData mid = l.getMidpoint();

		// the perpendicular bisector is the ellipse minor axis
		Line l2 = l.getBisector();

		double cx, cy;
		// the average point is the center of ellispe ..
		// cx=stroke.Sums().Ex/(double)stroke.Sums().N;
		// cy=stroke.Sums().Ey/(double)stroke.Sums().N;
		cx = stroke.Sums().cx;
		cy = stroke.Sums().cy;
		// center is the avearge of the point sumx/n and sumy/n
		PointData center = new PointData(cx, cy);

		// now this is the center, largest chort for the a, now i want to get
		// the shortest chord length....
		// i have to get the intersection with the stroke......

		ArrayList<PointData> Pintersect = intersections(l2, stroke);
		if (Pintersect != null) {
			if (Pintersect.size() > 1) {
				l2.setStartPoint(Pintersect.get(0));
				l2.setEndPoint(Pintersect.get(Pintersect.size() - 1));
			}
			// now get the lenght of the radius...

			// logger.info
			logger.info(l2);

			logger
					.info("  length of small bisection si ....    "
							+ l2.length());
			// line perpendicular to it.
			Ellipse e = new Ellipse(cx, cy, l, l2);
			// e.setEllipseParam(cx, cy,l.length()/2.0, l2.lepoints2ngth()/2.0);
			// now get the error to the ideall...

			double error = e.fitError(stroke.getPoints());

			double ErrorOrthognal = error;

			if (ErrorOrthognal < SystemSettings.THERSHOLD_RECOGNITION_ELISPSE_FIT_ERROR) {
				shape = new FittedShape(e, ErrorOrthognal, true);
			} else {
				shape = new FittedShape(e, ErrorOrthognal, false);
			}

			return shape;
		}
		Ellipse e2 = new Ellipse(cx, cy, l, l2);
		
		shape = new FittedShape(e2, Double.POSITIVE_INFINITY, false);
		return shape;
	}

	public boolean polylineTest2(Stroke stroke) {

		ArrayList<DominatePointStructure> pd = stroke.getStatisticalInfo()
				.getDominatePointsIndeces();

		boolean poly = true;
		// get the start and end of each sub
		for (int i = 0; i < pd.size(); i++) {
			DominatePointStructure temp = pd.get(i);

			int start = temp.getIndexInInk();

			int end = pd.get(i + 1).getIndexInInk();

			if (start == end) {
				continue;
			}

			InkInterface ts = stroke.createSubInkObject(start, end);

			// try if to create line using the fist and last point of the
			// storke...
			Line line = new Line(ts.getPoint(0), ts.getPoint(ts.getPoints()
					.size() - 1));
			// line ortognal distance from
			double ErrorOrthognal = line.OrthognalError(stroke.getPoints(),
					start, end);
			ErrorOrthognal = ErrorOrthognal / stroke.getLength();
			logger.info("  the simple line orthognal error is  "
					+ ErrorOrthognal);
			if (ErrorOrthognal > SystemSettings.THERSHOLD_PRE_RECOGNITION_LINE_FIT_ERROR) {

				return false;
			}

		}
		// if all is correct then

		return true;
	}

	public FittedShape polylineTest(Stroke stroke) {

		FittedShape shape;
		double e = 0;
		boolean acc = true;
		ArrayList<DominatePointStructure> pd = stroke.getStatisticalInfo()
				.getControlPointsIndeces();

		boolean poly = true;
		// get the start and end of each sub
		for (int i = 0; i < pd.size()-1; i++) {
			DominatePointStructure temp = pd.get(i);

			int start = temp.getIndexInInk();

			int end = pd.get(i + 1).getIndexInInk();

			if (start == end  || end<start) {
				logger.info(" this is skippped "+i);
				continue;
			}
logger.info("segment  "+i+" start at "+start+"   end "+end);
			InkInterface ts = stroke.createSubInkObject(start, end);
			
			//logger.info(ts);
           
			// try if to create line using the fist and last point of the
			// storke...
			Line line = new Line(ts.getPoint(0), ts.getPoint(ts.getPoints()
					.size() - 1));
			// line ortognal distance from
			double ErrorOrthognal = line.OrthognalError(stroke.getPoints(),
					start, end);
			ErrorOrthognal = ErrorOrthognal / stroke.getLength();
			e += ErrorOrthognal;
			logger.info("  the simple line orthognal error is  "
					+ ErrorOrthognal);
			if (ErrorOrthognal > SystemSettings.THERSHOLD_PRE_RECOGNITION_POLY_LINE_FIT_ERROR) {

				// return false;
				acc = false;
				break;

			}

		}
		
		if (e> SystemSettings.THERSHOLD_RECOGNITION_POLY_FIT_ERROR){
			acc=false;
		}

		// if all is correct then

		shape = new FittedShape(null, e, acc);
		shape.type = FittedShape.TYPE_POLYLINE;

		return shape;
	}

	public FittedShape curveTest(InkInterface ink) {
		if (ink instanceof Stroke) {
			Stroke st = (Stroke) ink;
			return curveTestStroke(st);

		}
		// else if (ink instanceof InkPart ){
		// InkPart inkp=(InkPart) ink;
		//			  
		// return curveTestPart(inkp);
		// }
		//		  
		// else {
		// InkPart temp=new InkPart();
		// temp.setPoints(ink.getPoints(), 0, ink.getPointsCount());
		// return curveTestPart(temp);
		//			  
		// }
		return null;
	}

	private FittedShape curveTestStroke(Stroke st) {
		// TODO       Auto-generated method stub
		logger.trace("   //  curveTestStroke TODO Auto-generated method stub");
		
		return null;
	}

	public FittedShape arcTest(Stroke stroke) {
		// TODO Auto-generated method stub
		logger.trace("   //  curveTestStroke TODO Auto-generated method stub");
		return null;
	}

//	public FittedShape complexTest(Stroke stroke) {
//		// TODO Auto-generated method stub
//		return null;
//	}

	public FittedShape sprialHelixTest(Stroke stroke) {
		// TODO Auto-generated method stub
		logger.trace("   //  curveTestStroke TODO Auto-generated method stub");
		return null;
	}

}
