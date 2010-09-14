/**
  
 
 */
package SketchMaster.swarm.curvefit;

import org.apache.log4j.Logger;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import SketchMaster.system.SystemSettings;
import SketchMaster.Stroke.StrokeData.PointData;
import SketchMaster.Stroke.StrokeData.Segment;
import SketchMaster.Stroke.StrokeData.Stroke;
import SketchMaster.Stroke.graphics.shapes.Ellipse;
import SketchMaster.Stroke.graphics.shapes.GuiShape;
import SketchMaster.Stroke.graphics.shapes.Line;
import SketchMaster.Stroke.graphics.shapes.SegmentedShape;
import SketchMaster.io.log.FileLog;
import SketchMaster.lib.ComputationsGeometry;
import SketchMaster.swarm.Solution;
import SketchMaster.swarm.polygonApproximations.PolygonSolution;

/**
 * @author maha
 */
public class StrokeCurveSolution extends PolygonSolution implements
		SegmentedShape {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(StrokeCurveSolution.class);

	private static final boolean ERROR_CONSTANT = true;
	private static int PARAM_SIZE = 19;// size of paramter of the particle
	private double circlea, circleb, circleR;

	// final static double T=0.9;
	// final static double eTolerance=200;
	private transient double A, B, C, D, a, b, c, d, e, f, m, line_b;
	private transient double x0, y0, ellipse_a, ellipse_b;

	private double dmax, Amax, Bmax, Cmax, Dmax;

	private double aC, bC, r;

	/**
	 * @param problemStroke
	 */
	public StrokeCurveSolution(Stroke stroke) {

		super(PARAM_SIZE);
		ParticlesPoints = new double[PARAM_SIZE];// 
		this.problemStroke = stroke;
		Random  r1Random;
		r1Random=new Random(PARAM_SIZE);
		// points== is (A B C D) , points ( a, b , c, d, e, f )==.ellipse
		// , ac,ab,r for circle polar

		// now itialize the values with random parameter

		for (int i = 0; i < ParticlesPoints.length; i++) {
			ParticlesPoints[i] = r1Random.nextDouble();
		}

		// ParticlesPoints[0]= 0.0;
		// ParticlesPoints[1]=
		// ParticlesPoints[2]=
		// ParticlesPoints[3]=
		// ParticlesPoints[4]=
		// ParticlesPoints[5]=
		// ParticlesPoints[6]=
		// ParticlesPoints[7]=
		// ParticlesPoints[8]=
		// ParticlesPoints[9]=
	//	if (problemStroke.getStatisticalInfo().getBox()!=null){
		ParticlesPoints[10] = problemStroke.getStatisticalInfo().getBox().getCenterX();
		ParticlesPoints[11] = problemStroke.getStatisticalInfo().getBox()
				.getCenterY();
		ParticlesPoints[12] = problemStroke.getStatisticalInfo().getBox()
				.getWidth() / 2.0;

		ParticlesPoints[15] = problemStroke.getStatisticalInfo().getBox()
				.getCenterX();
		ParticlesPoints[16] = problemStroke.getStatisticalInfo().getBox()
				.getCenterY();
		ParticlesPoints[17] = problemStroke.getStatisticalInfo().getBox()
				.getWidth() / 2.0;
		ParticlesPoints[18] = problemStroke.getStatisticalInfo().getBox()
				.getWidth() / 2.0;
	/*	}
		else 
		{
			for (int i = 10; i < ParticlesPoints.length; i++) {
				ParticlesPoints[i]=Math.random();
			}
		//	ParticlesPoints[10] = problemStroke.getStatisticalInfo().getBox().getCenterX();
			
		}*/
		init();
	}

	/**
	 * 
	 */
	public StrokeCurveSolution() {

		super(PARAM_SIZE);

		ParticlesPoints = new double[PARAM_SIZE];// 
		Random  r1Random;
		r1Random=new Random(PARAM_SIZE);
		// points== is (A B C D) , points ( a, b , c, d, e, f )==.ellipse

		// now itialize the values with random parameter

		// now itialize the values with random parameter

		for (int i = 0; i < ParticlesPoints.length; i++) {
			ParticlesPoints[i] = r1Random.nextDouble();
		}
		init();

	}

	/**
	 * @param size
	 */
	public StrokeCurveSolution(int size) {
		super(PARAM_SIZE);

		ParticlesPoints = new double[PARAM_SIZE];// 
		Random  r1Random;
		r1Random=new Random(PARAM_SIZE);
		// points== is (A B C D) , points ( a, b , c, d, e, f )==.ellipse

		// now itialize the values with random parameter

		for (int i = 0; i < ParticlesPoints.length; i++) {
			ParticlesPoints[i] = r1Random.nextDouble();
		}

		init();
	}

	private void init() {
		// type=SystemSettings.STROKE_CURVE;
		// type=SystemSettings.STROKE_CIRCLE;
		// type= SystemSettings.STROKE_LINE;
		type = SystemSettings.STROKE_ELLIPSE2;
		// type=SystemSettings.STROKE_LINE;
		// type= SystemSettings.STROKE_ELLIPSE;

		eTolerance = 1e5;
		dmax = 0;
	}

	public void calculateDmax() {

		double max = Double.MIN_VALUE;
		double tempd = 0.0;
		PointData pi, pj;

		double term1, term2;

		if (problemStroke != null) {
            int nPoints=problemStroke.getPointsCount();
			for (int i = 0; i < nPoints; i++) {
				pi = (PointData) problemStroke.getPoints().get(i);
				for (int j = 0; j < nPoints; j++) {

					pj = (PointData) problemStroke.getPoints().get(j);
					term1 = (pi.getX() - pj.getX()) * (pi.getX() - pj.getX());
					term2 = (pi.getY() - pj.getY()) * (pi.getY() - pj.getY());
					tempd = Math.sqrt(term1 + term2);
					if (max < tempd) {
						max = tempd;
					}

				}
			}

			dmax = max;
		}
		// logger.info("dmax = "+dmax);
	}

	private void getData() {

		// / type cuvrve
		A = ParticlesPoints[0];
		B = ParticlesPoints[1];
		C = ParticlesPoints[2];
		D = ParticlesPoints[3];

		// type ellispe
		a = ParticlesPoints[4];
		b = ParticlesPoints[5];
		c = ParticlesPoints[6];
		d = ParticlesPoints[7];
		e = ParticlesPoints[8];
		f = ParticlesPoints[9];
		// / type circle
		aC = ParticlesPoints[10];
		bC = ParticlesPoints[11];
		r = ParticlesPoints[12];
		// type line
		m = ParticlesPoints[13];
		line_b = ParticlesPoints[14];
		// type ellipse 2
		x0 = ParticlesPoints[15];
		y0 = ParticlesPoints[16];
		ellipse_a = ParticlesPoints[17];
		ellipse_b = ParticlesPoints[18];

	}

	public boolean checkSolution() {
		getData();
		if (type == SystemSettings.STROKE_CURVE) {
			double term = B * B + C * C - (4.0 * A * D);
			if (Math.abs(term) == 1)
				return true;
			else
				return true;
		} else if (type == SystemSettings.STROKE_ELLIPSE) {

			if ((a + c) == 1)
				return true;
			else
				return true;
		} else if (type == SystemSettings.STROKE_LINE) {
			// if (problemStroke.getData().getBox().contains(aC,bC))
			// {
			// // logger.info( aC+ " b "+bC);
			// return true;
			// }
			// else
			// {
			// // logger.info(" I am Heree "+ aC+ " b "+bC);
			// return true;
			// }

			return true;
		}
		return true;
	}

	public void refineSolution() {
		// get the solution to yeild to better reeults
		if (type == SystemSettings.STROKE_CIRCLE) {

			// for (int i = 10; i < ParticlesPoints.length; i++) {
			// if (ParticlesPoints[i]<0)
			// ParticlesPoints[i]=-ParticlesPoints[i];
			// }
			if (ParticlesPoints[12] < 0)
				ParticlesPoints[12] = -ParticlesPoints[12];
			;
			// getData();
			// if (!problemStroke.getData().getBox().contains(aC, bC)){
			//					 
			// ParticlesPoints[10]=problemStroke.getData().getBox().getCenterX();
			// ParticlesPoints[11]=problemStroke.getData().getBox().getCenterY();
			//					
			// }

		}
		if (type == SystemSettings.STROKE_CURVE) {
			getData();
			double term = B * B + C * C - (4.0 * A * D);
			if (term != 1) {
				// change d to apply to condition
				// b^2+C^2/4*A
				if (A != 0.0) {
					D = (-1 + (B * B) + (C * C)) / (4.0 * A);
				} else {
					A = (-1 + (B * B) + (C * C)) / (4.0 * D);
				}

			}

			ParticlesPoints[0] = A;
			ParticlesPoints[1] = B;
			ParticlesPoints[2] = C;
			ParticlesPoints[3] = D;
		}
		if (type == SystemSettings.STROKE_ELLIPSE) {
			getData();
			double term = b * b;
			double term2 = 4.0 * a * c;
			if ((term2 - term - 1) != 0) {
				a = (1 + term) / (4.0 * c);
			}

			ParticlesPoints[4] = a;
			ParticlesPoints[5] = b;
			ParticlesPoints[6] = c;

		}

		if (type == SystemSettings.STROKE_ELLIPSE2) {
			getData();

			// if (!problemStroke.getData().getBox().contains(x0, y0)){
			//				 
			// ParticlesPoints[15]=problemStroke.getData().getBox().getCenterX();
			// ParticlesPoints[16]=problemStroke.getData().getBox().getCenterY();
			//				
			// }

		}

	}

	public void calculateSolutionParameters() {
		getData();
		if (type == SystemSettings.STROKE_CURVE) {
			circlea = -B / (2.0 * A);
			circleb = -C / (2.0 * A);
			circleR = 1.0 / (2.0 * Math.abs(A));
		}

		if (type == SystemSettings.STROKE_CIRCLE) {
			circlea = aC;
			circleb = bC;
			circleR = r;
		}

		// super.calculateSolutionParameters();
	}

	@Override
	public double eval() {
		getData();

		return error();
	}

	@Override
	public double error() {

		// // there supose to be an error check to check which
		// set get the min error and set the type according to it

		if (ERROR_CONSTANT) // means if true then use the errror of current
							// type.
		{
			Error = selectError();
			return Error;
		} else // if false select the type basedn on the minimum error.
		{
			Error = AllError();
			return Error;
		}

		// logger.info("error is "+error);
		// double errorE=getEllipseError();
		// type=SystemSettings.STROKE_ELLIPSE;
		// return errorE;
		// return errorE;
		// if ( error>errorE)
		// {
		// type=SystemSettings.STROKE_ELLIPSE;
		// return errorE;
		// }
		// else{
		// type=SystemSettings.STROKE_CIRCLE;
		// return error;
		// }
	}

	private double selectError() {
		double error = 0.0;
		//		
		//		
		//		
		if ((type == SystemSettings.STROKE_CIRCLE)
				|| (type == SystemSettings.STROKE_CURVE))
			error = getCircleError();

		if ((type == SystemSettings.STROKE_ELLIPSE)
				|| (type == SystemSettings.STROKE_ELLIPSE2))
			error = getEllipseError();

		if (type == SystemSettings.STROKE_LINE)
			error = getLineError();
		// //return
		// //type=SystemSettings.STROKE_CIRCLE;
		return error;

	}

	private double AllError() {
		double errorC, errorE, errorL;

		type = SystemSettings.STROKE_CIRCLE;

		errorC = getCircleError();
		type = SystemSettings.STROKE_ELLIPSE2;
		errorE = getEllipseError();
		type = SystemSettings.STROKE_LINE;
		errorL = getLineError();

		double min = 0;

		if (errorC > errorE) {
			min = errorE;
			type = SystemSettings.STROKE_ELLIPSE2;
		} else {
			min = errorC;
			type = SystemSettings.STROKE_CIRCLE;
		}

		if (min > errorL) {
			min = errorL;
			type = SystemSettings.STROKE_LINE;
		}

		return min;
	}

	private double getLineError() {
		double error = 0.0;
		// logger.info("------------------------------------------------------------------------------------");

		for (int i = 0; i < problemStroke.getPointsCount(); i++) {
			error += getLi(i);
		}
		return Math.sqrt(error);
	}

	private double getLi(int index) {
		PointData pointk = (PointData) problemStroke.getPoints().get(index);
		double D = 0;
		double x, y, E;
		// x2; xy; y2; x; y; 1]
		// a b c d e f
		x = pointk.getX();
		y = pointk.getY();

		D = m * x + line_b - y;

		return D * D;
	}

	private double getEllipseError() {
		double error = 0.0;
		// logger.info("------------------------------------------------------------------------------------");
          int npoints= problemStroke.getPointsCount();
		for (int i = 0; i < npoints; i++) {
			if (type == SystemSettings.STROKE_ELLIPSE2)
				error += getPi2(i);
			if (type == SystemSettings.STROKE_ELLIPSE)
				error += getPi(i);
		}
		return Math.sqrt(error);
	}

	private double getPi(int index) {
		PointData pointk = (PointData) problemStroke.getPoints().get(index);
		double D = 0;
		double x, y, xx, yy, xy;
		// x2; xy; y2; x; y; 1]
		// a b c d e f
		x = pointk.getX();
		y = pointk.getY();
		xx = x * x;
		yy = y * y;
		xy = x * y;

		D = (xx * a) + (xy * b) + (c * yy) + (d * x) + (e * y) + f;

		return D * D;
	}

	private double getPi2(int index) {
		PointData pointk = (PointData) problemStroke.getPoints().get(index);
		double D = 0.0;
		double x, y, xx, yy, xy;
		// x2; xy; y2; x; y; 1]
		// a b c d e f
		x = pointk.getX();
		y = pointk.getY();
		xx = (x - x0) * (x - x0);
		yy = (y - y0) * (y - y0);

		D = (xx / (ellipse_a * ellipse_a)) + (yy / (ellipse_b * ellipse_b)) - 1;

		return (D * D);
	}

	private double getCircleError() {
		// if (Math.abs(A)>((double)problemStroke.getPointsCount()/(2.0*dmax)))
		// circleR=1.0/(2.0*Math.abs(A));
		//		
		// if ( circleR< (dmax/(double)problemStroke.getPointsCount()))
		// {
		// // logger.info("A ="+A+" ant the other dmax= "+dmax+ " cond= "
		// +((double)problemStroke.getPointsCount()/(2.0*dmax)));
		// //logger.info("in error solution");
		// return Double.POSITIVE_INFINITY;
		//			
		// }
		double error = 0.0;
		int npoints=problemStroke.getPointsCount();
		// logger.info("------------------------------------------------------------------------------------");
		for (int i = 0; i < npoints; i++) {
			if (type == SystemSettings.STROKE_CIRCLE) // circle of a,b,r
			{
				error += getDi(i);
			} else if (type == SystemSettings.STROKE_CURVE) {
				error += getdi(i); // circle or line
				// logger.info("eroro til now is "+error);
			}
		}
		return Math.sqrt(error);
	}

	private double getdi(int index) {

		PointData pointk = (PointData) problemStroke.getPoints().get(index);
		double di, pi;

		double term1 = A
				* ((pointk.getX() * pointk.getX()) + (pointk.getY() * pointk
						.getY()));
		double term2 = (pointk.getX() * B) + (pointk.getY() * C) + D;
		pi = term1 + term2;
		double term3 = (1.0 + (4.0 * A * pi));
		if (term3 < 0)
			term3 = -term3;

		di = 2.0 * ((pi) / (1.0 + Math.sqrt(term3)));

		// logger.info(" Pi = "+pi+"d "+index+" = "+ di);

		return di * di;
	}

	public double getErrorFromArea() {

		// try equlidaina distance
		double polyerr = getEllipsearea();
		double serror = Math.abs(problemStroke.getStatisticalInfo().getArea());
		//  logger.trace("   polygon area  = " + serror);

		// logger.info("p="+polyerr);
		double error = 0.0;

		error = (serror - polyerr) * (serror - polyerr);

		return Math.sqrt(error);
	}

	public double getErrorFromPriemeter(){
		
		double prieEllipse = perimeter();
		double serror = problemStroke.getLength();
		//  logger.trace("   polygon area  = " + serror);

		// logger.info("p="+polyerr);
		double perror = 0.0;

		perror = ( prieEllipse-serror) * ( prieEllipse-serror);
		return Math.sqrt(perror);

	} 
	private double getPrimeterPercent(){
		
		double prieEllipse = perimeter();
		double serror = problemStroke.getLength();
		//  logger.trace("   polygon area  = " + serror);

		// logger.info("p="+polyerr);
		double perror =  serror/ prieEllipse ;

	//	perror = ( prieEllipse-serror) * ( prieEllipse-serror);
		return perror; 
	}
	private double getEllipsearea() {
		getData();
		
		// i will change this line to correct error caclculation from 
		//		double area = Math.PI * (this.ellipse_a / 2.0) * (this.ellipse_b / 2.0);
		double area = Math.PI * (this.ellipse_a ) * (this.ellipse_b );
		//  logger.trace("   ellipse area =    " + area);
		return area;
	}

	public boolean isCenterInMiddle() {
		// problemStroke.getStatisticalInfo().getBox().getCenterX();
		// problemStroke.getStatisticalInfo().getBox().getCenterY();
		getData();
		// xo,yo
		if (problemStroke.getStatisticalInfo().getBox()!=null)
		return problemStroke.getStatisticalInfo().getBox().contains(this.x0,
				this.y0);
		else {
			
			return true;
		}

	}

	public double getEllipseCertainty() {

		
		double e=this.eval();
		//double p=this.getErrorFromPriemeter();
		double perc =getPrimeterPercent();
		//"  perimeter error is "+p+"
		//logger.info("  perimeter error is "+p);
		logger.trace("distances error iis   "+e+ "  the percent is "+perc);
		double c=(perc)/(e);
		double c2=(perc)/(e*2.0);
		logger.trace("   c = "+c);
		logger.trace("   c2 = "+c2);
		if (perc<0.5)
		        return c2;  // change fore  better perforance see papers and theiss for orginal may be c
			if(perc>=0.5)
				
				return c2;
				
		return c;
		
//		boolean inmiddle = isCenterInMiddle();
//		if (inmiddle) {
//
//			double e = this.eval();
//			//  logger.trace("the last thing iam  doign was check ellipse certainty  581 ub stroke curve solution s");
//
//			return 10 - e;
//
//			// double e= this.eval();
//			// double aree=this.getErrorFromArea();
//			// if (e>=2.5)
//			// {
//			// // logger.info("error divide area "+e/aree);
//			// if ((aree)<1e3)
//			// return 1.0;
//			//    
//			// return 0.5;
//			// }
//			// else //if (e<2.5)
//			// {
//			//    		
//			// if (e<2)
//			// if (aree<1e4)
//			// return 1.0;
//			//    	
//			//    	
//			//    		
//			//    		
//			// logger.info("-------------------------------");
//			// if ( (this.getEllipsearea()>aree)&&(aree<1e3))
//			// return 1.0;
//			//    		
//			// return 0.5;
//			//    
//			// }
//
//		}
//
//		else
//			return 0.0;

	}

	private double getDi(int index) {

		PointData pointk = (PointData) problemStroke.getPoints().get(index);
		double di, pi;

		double term1 = (pointk.getX() - aC);
		term1 = term1 * term1;

		double term2 = (pointk.getY() - bC);

		term2 = term2 * term2;
		pi = Math.sqrt(term1 + term2);

		di = pi - r;

		// logger.info(" Pi = "+pi+"d "+index+" = "+ di);

		return di * di;
	}

	public int compareTo(Object o) {

		return 0;
	}

	/**
	 * @uml.property name="SolutionType" multiplicity="(0 -1)" dimension="0"
	 */
	private int type;

	/**
	 * @uml.property name="ParticlesPoints" multiplicity="(0 -1)" dimension="1"
	 */
	private double[] ParticlesPoints;

	/**
	 * Getter of the property <tt>ParticlesPoints</tt>
	 * 
	 * @return Returns the particlesPoints.
	 * @uml.property name="ParticlesPoints"
	 */
	public double[] getParticlesPoints() {

		return ParticlesPoints;
	}

	/**
	 * Setter of the property <tt>ParticlesPoints</tt>
	 * 
	 * @param ParticlesPoints
	 *            The particlesPoints to set.
	 * @uml.property name="ParticlesPoints"
	 */
	public void setParticlesPoints(double[] particlePoints) {

		ParticlesPoints = particlePoints;
	}

	public int getType() {
		if (type == SystemSettings.STROKE_CURVE)
			return SystemSettings.STROKE_CIRCLE;
		else
			return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	/**
	 * @return the circlea
	 */
	public double getCirclea() {
		return circlea;
	}

	/**
	 * @return the circleb
	 */
	public double getCircleb() {
		return circleb;
	}

	/**
	 * @return the circleR
	 */
	public double getCircleR() {
		return circleR;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + Arrays.hashCode(ParticlesPoints);
		result = PRIME * result + type;
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final StrokeCurveSolution other = (StrokeCurveSolution) obj;
		if (!Arrays.equals(ParticlesPoints, other.ParticlesPoints))
			return false;
		if (type != other.type)
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() {
		StrokeCurveSolution tempSolution = null;
		tempSolution = (StrokeCurveSolution) super.clone();
		tempSolution.ParticlesPoints = (double[]) ParticlesPoints.clone();
		tempSolution.velocities = velocities.clone();
		tempSolution.problemStroke = this.problemStroke;
		tempSolution.type = this.type;

		return tempSolution;
	}

	// public void radomizeSolution()
	// {
	//		
	// velocities=new double[13];
	// ParticlesPoints=new double[13];//
	//		
	// //points== is (A B C D) , points ( a, b , c, d, e, f )==.ellipse
	//		
	//		
	// //now itialize the values with random parameter
	//		
	// for (int i = 0; i < ParticlesPoints.length; i++) {
	// ParticlesPoints[i]=Math.random();
	// velocities[i]=Math.random();
	// }
	//         
	// // type= SystemSettings.STROKE_CIRCLE;

	// /// eTolerance=900000;
	// // dmax=0;
	//         
	// }

	public void paint(Graphics2D g) {

		getData();
		calculateSolutionParameters();
		// logger.info("------------The fit type is "+FitType);
		// Graphics2D g= (Graphics2D) g1;

		if (type == SystemSettings.STROKE_CIRCLE) {

			calculateSolutionParameters();
			// logger.info("draw circle");
			// aC , bC,r

			double r = circleR;
			double xr, yr;
			double x, y;
			x = circlea;
			y = circleb;

			xr = x - r;
			yr = y - r;

			g.setColor(Color.BLUE);
			g.drawOval((int) xr, (int) yr, (int) (2 * r), (int) (2 * r));
			// g.setColor(Color.lightGray);
			// g.fillOval((int)xr, (int)yr,(int) (2*r), (int)(2*r));

			g.setColor(Color.BLACK);
			g.drawRect((int) x, (int) y, 5, 5);
			g.fillRect((int) x, (int) y, 5, 5);

			// }

		}
		if (type == SystemSettings.STROKE_ELLIPSE) {
			// logger.info("draw elllipse");

			double[] ellip = new double[7];
			for (int i = 0; i < ellip.length; i++) {

				ellip[i] = ParticlesPoints[4 + i];

				// logger.info(" a "+ellip[i] );
			}
			double[] para = ComputationsGeometry.getEllipseElements(ellip);

			double A = para[0];
			double B = para[1];
			double x0 = para[2];
			double y0 = para[3];
			//double theta = para[4];

			if (A != 0.0) {
				double x = x0 - A;
				double y = y0 - B;

				g.setColor(Color.BLACK);
				g.drawRect((int) x, (int) y, 5, 5);
				g.fillRect((int) x, (int) y, 5, 5);
				g.drawOval((int) x, (int) y, (int) A, (int) B);
				// g.rotate(theta);

			}
		}
		if (type == SystemSettings.STROKE_LINE) {
			// logger.info("drawing line ");
			g.setColor(Color.darkGray);
			int x1 = 0, y1 = 0, x2 = 0, y2 = 0;
			double x = problemStroke.getStartPoint().getX();
			double y = m * x + line_b;
			x1 = (int) x;

			y1 = (int) y;

			x = problemStroke.getEndPoint().getX();
			y = m * x + line_b;
			x2 = (int) x;

			y2 = (int) y;

			g.drawLine(x1, y1, x2, y2);

		}
		if (type == SystemSettings.STROKE_ELLIPSE2) {

			// logger.info("draw elllipse 2 ");

			// if (A!=0.0){

			// x0,y0,ellipse_a,ellipse_b
			double x = this.x0;
			double y = this.y0;
			g.setColor(Color.BLACK);
			g.drawRect((int) x, (int) y, 5, 5);
			g.fillRect((int) x, (int) y, 5, 5);
			x = x - this.ellipse_a;
			y = y - this.ellipse_b;

			// / i want draw only the par in stroke

			g.drawOval((int) x, (int) y, (int) (this.ellipse_a * 2),
					(int) (this.ellipse_b * 2));
			// g.rotate(theta);

			// }
		}

		// draw the item

	}

	public Segment getSegmentOfIndex(int i) {

		if (logger.isDebugEnabled()) {
			//  logger.debug("getSegmentOfIndex(int) - To do wirete teh parameter list fo ellipse in stroke curve solution (  " + this.getClass().getSimpleName() + "    " + (new Throwable()).getStackTrace()[0].getLineNumber() + "  )  "); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		int vi = 0;
		int vj = problemStroke.getPointsCount()-1;

		Segment temp = new Segment(problemStroke.createSubInkObject(vi, vj));

		// then addd the vertix

		Ellipse elips = new Ellipse();
		
		PointData p = (PointData) problemStroke.getPoint(vi).clone();
		temp.setStartPoint(p);
		
		
		elips.setStartPoint(p);
		elips.setIStart(vi);
		
		p = (PointData) problemStroke.getPoint(vj).clone();
		temp.setEndPoint(p);
        elips.setEndPoint(p);
	    elips.setIEnd(vj);

		//ArrayList param = new ArrayList();
		elips.setEllipseParam(this.x0, this.y0, this.ellipse_a, this.ellipse_b);
		


		// linPrim.setLineParams(problemStroke.getPoint(vi),
		// problemStroke.getPoint(vj));
		// Segpoints.add();
		temp.setSegmentType(elips);

		return temp;

	}

	public int getSegmentsCount() {

		return 1;
	}

	
	public double getComparableError(){
		
		return error();
	}
	public double  perimeter(){
		
		
		double r=	ellipse_a ;
		double s=	ellipse_b ;
		
		
		double approx= Math.PI*((3*(r+s)-Math.sqrt((3*r+s)*(r+3*s)) ));
		//logger.info("  The approximate of primeter is  "+approx);
//		// now trying to get the max axis is r and min is s . 
///*		if (	ellipse_a <	ellipse_b)
//		{
//			 r=	ellipse_b ;
//		 s=	ellipse_a ;
//		}*/
//		logger.info("  r  "+r+"   s = "+s);
//		double ecen=Math.sqrt( Math.abs(r*r-s*s) )/r;
//		logger.info("ecent =  "+ecen);
//		double p=0;
//		double expand=0;
//		double term1,term2,term3,term4,term5;
//		for (int i = 1; i < 30; i++) {
//			term1=factorial(2*i);
//			//term2=;
//			term3= Math.pow(2, i)- factorial(i);
//			term4=Math.pow(term3,4);
//			
////			logger.info("  term1 "+term1);
////			
//	//		logger.info("  term 3 = "+term3+"  term 4= "+term4);
//		expand+= ((term1*term1)/term4)*(Math.pow(ecen, 2.0*i)/((2.0*i)-1.0));
//		logger.info(" expand term = "+expand);
//		
//		}
//		
//		p=2*Math.PI*r*(1-expand);
//		logger.info(" p = "+p);
		return approx;
	}
	
	public double factorial(int i){
		
		if (i==1)
			return 1;
		else if (i==0)
			return 0;
		else {
			
			return (factorial(i-1)*i);
		}
		
	}

	public String getEllipseString() {
		String str=" ";
		str+=" a = "+ellipse_a+" ,b = "+ellipse_b +" , Center( "+x0+",  "+y0+" )   with Error =  " +this.eval()+ " , Percent ="+getPrimeterPercent();
		return  str;
	}
}
