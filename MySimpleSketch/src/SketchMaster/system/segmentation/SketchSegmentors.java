/**
 * 
 */
package SketchMaster.system.segmentation;

import org.apache.log4j.Logger;
import org.omg.CORBA.DATA_CONVERSION;

import java.awt.Point;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import SketchMaster.system.SystemSettings;
import SketchMaster.Stroke.StrokeData.DominatePointStructure;
import SketchMaster.Stroke.StrokeData.PointData;
import SketchMaster.Stroke.StrokeData.Stroke;
import SketchMaster.Stroke.graphics.layers.SketchSegmentionLayer;
import SketchMaster.Stroke.graphics.shapes.FittedShape;
import SketchMaster.Stroke.graphics.shapes.GuiShape;
import SketchMaster.Stroke.graphics.shapes.Line;
import SketchMaster.collection.NumericalComparator;
import SketchMaster.collection.SortedValueMap;
import SketchMaster.gui.Events.HandleStroke;
import SketchMaster.gui.Events.NewStrokeEvent;
import SketchMaster.io.log.FileLog;
import SketchMaster.lib.CurveFitData;
import SketchMaster.swarm.Solution;
import SketchMaster.swarm.SwarmSystem;
import SketchMaster.swarm.curvefit.StrokeCurveAgent;
import SketchMaster.swarm.curvefit.StrokeCurveSolution;
import SketchMaster.swarm.polygonApproximations.DigitalCurveDivideAgent;
import SketchMaster.swarm.polygonApproximations.DigitalCurveDivideSolution;
import SketchMaster.swarm.polygonApproximations.polygonAgent;
import SketchMaster.swarm.polygonApproximations.polygonSolution;
import SketchMaster.system.SystemSettings;

/**
 * @author maha
 * 
 */
public class SketchSegmentors {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(SketchSegmentors.class);

	private static final int AGENT_SIZE = SystemSettings.AGENT_SIZE;

	// handle command on stroke
	public void generateDominatePoints(Stroke stroke) {

		// System.out
		// .println("---------------Compute critical point-----------------");
		//  logger.trace	("---------------get  critical point-----------------");
		stroke.generateAllDominatePoints();
		stroke.computeRemainigStatists();

	}

	public GuiShape lineFit(Stroke stroke) {

		//  logger.trace(" ----------------------------------------------------------");
		//  logger.trace(" Start line fit");
		// now i will start the swarm system and initaize ti for the current
		// stroke
		// let is solve it then display the approximatin.
		SwarmSystem swarm = new SwarmSystem();
		// now create a list of 20 agents to solve the problem
		StrokeCurveAgent[] agents = new StrokeCurveAgent[AGENT_SIZE];

		StrokeCurveSolution sol;
		//  logger.trace("number of points in stroke "+ stroke.getPointsCount());

		for (int i = 0; i < agents.length; i++) {
			agents[i] = new StrokeCurveAgent();
			sol = new StrokeCurveSolution(stroke);
			sol.setProblemStroke(stroke);
			sol.setType(SystemSettings.STROKE_LINE);
			sol.calculateDmax();
			agents[i].setCurrentSolution(sol);
			agents[i].setLocalbest(sol);

		}

		// now intalize an global solution as a
		sol = new StrokeCurveSolution();
		sol.setProblemStroke(stroke);
		//  logger.trace(" ----------------------------------------------------------");
		//  logger.trace("Start ");
		// now run swarm
		StrokeCurveSolution Finalsol = (StrokeCurveSolution) swarm.solve(
				agents, sol, 0,-1);

		// display the solution
		// //  logger.trace("circle error = "+Finalsol.eval());

		//  logger.trace("fit error = " + Finalsol.eval());
		// System.out.println(" line error = "+Finalsol.eval()+" (sketch
		// segmentors 85 ) ");

		//  logger.trace("number of points in stroke "+ stroke.getPointsCount());
		//  logger.trace("Type = " + Finalsol.getType());
		//  logger.trace(" the parameters are ");
		for (int i = 0; i < Finalsol.getParticlesPoints().length; i++) {
			//  logger.trace(" param " + i + "  =  "	+ Finalsol.getParticlesPoints()[i]);
		}

		Finalsol.calculateSolutionParameters();
		// System.out.println("the a = "+Finalsol.getCirclea());
		// System.out.println("the b = "+Finalsol.getCircleb());
		// System.out.println("the r = "+Finalsol.getCircleR());
		// stroke.
		stroke.getStatisticalInfo().setLine(Finalsol);

		// StrokeFitObject temp=new StrokeFitObject();
		// temp.setOriginalStorke(stroke);
		// // parameterof fit to objet
		// Finalsol.eval();
		// temp.addParam(Finalsol.getCirclea());
		// temp.addParam(Finalsol.getCircleb());
		// temp.addParam(Finalsol.getCircleR());
		//	
		// // System.out.println("The fit type of final solution is
		// "+Finalsol.getType());
		// temp.setFitType(Finalsol.getType());
		// //add the parameter of search
		// for (int i = 0; i < Finalsol.getParticlesPoints().length; i++) {
		// // if (i==4)
		// // System.out.println("a== "+ Finalsol.getParticlesPoints()[i]);
		// temp.addParam(Finalsol.getParticlesPoints()[i]);
		// }

		return Finalsol;

	}
//    public  Solution runBothSwarm(Stroke stroke){
//    //  logger.trace(" ----------------------------------------------------------");
//		//  logger.trace(" Start curve fit");
//		// now i will start the swarm system and initaize ti for the current
//		// stroke
//		// let is solve it then display the approximatin.
//		SwarmSystem swarm = new SwarmSystem();
//		// now create a list of 20 agents to solve the problem
//		StrokeCurveAgent[] agents = new StrokeCurveAgent[AGENT_SIZE/2];
//
//		StrokeCurveSolution sol;
//		//  logger.trace("number of points in stroke "	+ stroke.getPointsCount());
//
//		for (int i = 0; i < agents.length; i++) {
//			agents[i] = new StrokeCurveAgent();
//			sol = new StrokeCurveSolution(stroke);
//			sol.setProblemStroke(stroke);
//			sol.calculateDmax();
//			agents[i].setCurrentSolution(sol);
//			agents[i].setLocalbest(sol);
//
//		}
//
//		// now intalize an global solution as a
//		sol = new StrokeCurveSolution();
//		sol.setProblemStroke(stroke);
//		//  logger.trace(" ----------------------------------------------------------");
//		//  logger.trace("Start ");
//		// now run swarm
//		StrokeCurveSolution Finalsol = (StrokeCurveSolution) swarm.solve(	agents, sol, 0, SystemSettings.SWARM_SYSTEM_MAX_CIRCLE_ITERATION);
//
//		// display the solution
//		// //  logger.trace("circle error = "+Finalsol.eval());
//
//		//  logger.trace("fit error = " + Finalsol.eval());
//
//		//  logger.trace("area error is  = " + Finalsol.getErrorFromArea());
//		//  logger.trace(" is in middle " + Finalsol.isCenterInMiddle());
//		//  logger.trace("  getEllipseCertainty()  "+ Finalsol.getEllipseCertainty());
//
//		//  logger.trace("number of points in stroke "	+ stroke.getPointsCount());
//		//  logger.trace("Type = " + Finalsol.getType());
//		//  logger.trace(" the parameters are ");
//		for (int i = 0; i < Finalsol.getParticlesPoints().length; i++) {
//			//  logger.trace(" param " + i + "  =  "	+ Finalsol.getParticlesPoints()[i]);
//		}
//
//		Finalsol.calculateSolutionParameters();
//		// System.out.println("the a = "+Finalsol.getCirclea());
//		// System.out.println("the b = "+Finalsol.getCircleb());
//		// System.out.println("the r = "+Finalsol.getCircleR());
//		// stroke.
//
//		stroke.getStatisticalInfo().setEllipse(Finalsol);
//
//		// StrokeFitObject temp=new StrokeFitObject();
//		// temp.setOriginalStorke(stroke);
//		// // parameterof fit to objet
//		// Finalsol.eval();
//		// temp.addParam(Finalsol.getCirclea());
//		// temp.addParam(Finalsol.getCircleb());
//		// temp.addParam(Finalsol.getCircleR());
//		//	
//		// // System.out.println("The fit type of final solution is
//		// "+Finalsol.getType());
//		// temp.setFitType(Finalsol.getType());
//		// //add the parameter of search
//		// for (int i = 0; i < Finalsol.getParticlesPoints().length; i++) {
//		// // if (i==4)
//		// // System.out.println("a== "+ Finalsol.getParticlesPoints()[i]);
//		// temp.addParam(Finalsol.getParticlesPoints()[i]);
//		// }
//		//		
//		// int fitlayer=-1;
//		// for (int i = 0; i < sketch.getLayersCount(); i++) {
//		//			
//		// if
//		// (sketch.getLayer(i).getLayerName().equalsIgnoreCase(ellipseFitname))
//		// {
//		// fitlayer=i;
//		// break;
//		// }
//		//	 
//		// // if (sketch.getLayer(i) instanceof SketchSegmentionLayer) {
//		// // fitlayer=i;
//		// // break;
//		// //
//		// // }
//		//			
//		// }
//		// if (fitlayer!=-1)
//		// sketch.getLayer(fitlayer).addObject(Finalsol);
//		// else
//		// {
//		// SketchSegmentionLayer templayer= new SketchSegmentionLayer();
//		// templayer.setLayerName(ellipseFitname);
//		// templayer.addObject(Finalsol);
//		// sketch.addLayer(templayer);
//		// }
//
//		return Finalsol;
//
//		// change this global solution with the computed speed pont and curvaute
//    	
//	 }
	public StrokeCurveSolution curveFit(Stroke stroke) {
		//  logger.trace(" ----------------------------------------------------------");
		//  logger.trace(" Start curve fit");
		// now i will start the swarm system and initaize ti for the current
		// stroke
		// let is solve it then display the approximatin.
		SwarmSystem swarm = new SwarmSystem();
		// now create a list of 20 agents to solve the problem
		StrokeCurveAgent[] agents = new StrokeCurveAgent[AGENT_SIZE/2];

		StrokeCurveSolution sol;
		//  logger.trace("number of points in stroke "	+ stroke.getPointsCount());

		for (int i = 0; i < agents.length; i++) {
			agents[i] = new StrokeCurveAgent();
			sol = new StrokeCurveSolution(stroke);
			sol.setProblemStroke(stroke);
			sol.calculateDmax();
			agents[i].setCurrentSolution(sol);
			agents[i].setLocalbest(sol);

		}

		// now intalize an global solution as a
		sol = new StrokeCurveSolution();
		sol.setProblemStroke(stroke);
		//  logger.trace(" ----------------------------------------------------------");
		//  logger.trace("Start ");
		// now run swarm
		StrokeCurveSolution Finalsol = (StrokeCurveSolution) swarm.solve(	agents, sol, 0, SystemSettings.SWARM_SYSTEM_MAX_CIRCLE_ITERATION);

		// display the solution
		// //  logger.trace("circle error = "+Finalsol.eval());

		//  logger.trace("fit error = " + Finalsol.eval());

		//  logger.trace("area error is  = " + Finalsol.getErrorFromArea());
		//  logger.trace(" is in middle " + Finalsol.isCenterInMiddle());
		//  logger.trace("  getEllipseCertainty()  "+ Finalsol.getEllipseCertainty());

		//  logger.trace("number of points in stroke "	+ stroke.getPointsCount());
		//  logger.trace("Type = " + Finalsol.getType());
		//  logger.trace(" the parameters are ");
		for (int i = 0; i < Finalsol.getParticlesPoints().length; i++) {
			//  logger.trace(" param " + i + "  =  "	+ Finalsol.getParticlesPoints()[i]);
		}

		Finalsol.calculateSolutionParameters();
		// System.out.println("the a = "+Finalsol.getCirclea());
		// System.out.println("the b = "+Finalsol.getCircleb());
		// System.out.println("the r = "+Finalsol.getCircleR());
		// stroke.

		stroke.getStatisticalInfo().setEllipse(Finalsol);

		// StrokeFitObject temp=new StrokeFitObject();
		// temp.setOriginalStorke(stroke);
		// // parameterof fit to objet
		// Finalsol.eval();
		// temp.addParam(Finalsol.getCirclea());
		// temp.addParam(Finalsol.getCircleb());
		// temp.addParam(Finalsol.getCircleR());
		//	
		// // System.out.println("The fit type of final solution is
		// "+Finalsol.getType());
		// temp.setFitType(Finalsol.getType());
		// //add the parameter of search
		// for (int i = 0; i < Finalsol.getParticlesPoints().length; i++) {
		// // if (i==4)
		// // System.out.println("a== "+ Finalsol.getParticlesPoints()[i]);
		// temp.addParam(Finalsol.getParticlesPoints()[i]);
		// }
		//		
		// int fitlayer=-1;
		// for (int i = 0; i < sketch.getLayersCount(); i++) {
		//			
		// if
		// (sketch.getLayer(i).getLayerName().equalsIgnoreCase(ellipseFitname))
		// {
		// fitlayer=i;
		// break;
		// }
		//	 
		// // if (sketch.getLayer(i) instanceof SketchSegmentionLayer) {
		// // fitlayer=i;
		// // break;
		// //
		// // }
		//			
		// }
		// if (fitlayer!=-1)
		// sketch.getLayer(fitlayer).addObject(Finalsol);
		// else
		// {
		// SketchSegmentionLayer templayer= new SketchSegmentionLayer();
		// templayer.setLayerName(ellipseFitname);
		// templayer.addObject(Finalsol);
		// sketch.addLayer(templayer);
		// }

		return Finalsol;

		// change this global solution with the computed speed pont and curvaute

	}

	private SortedValueMap computeVerticesForPolygon(Stroke stroke){
//		SortedValueMap segmentsVelocityPoints = SezginSegmentor.createVertices(stroke,null , 1);
//		SortedValueMap segmentsDirectionPoints = SezginSegmentor.createVertices(stroke,null ,2);
//		SortedValueMap segmentsTimePoints = SezginSegmentor.createVertices(stroke,null , 3)
		SortedValueMap segmentsPoints;
		SortedValueMap  AllSegments=new SortedValueMap(new NumericalComparator());
		
		for (int i = 0; i < 4; i++) {
			
	
		 segmentsPoints = SezginSegmentor.createVertices(stroke,null , i);
		
		// logger.info( "  i "+i);
	
		if (segmentsPoints.getSortedList()!=null){
		
			 for (Iterator iter = segmentsPoints.getSortedList().iterator(); iter.hasNext();) {
				
				 Map.Entry  entry= ((Map.Entry)iter.next());
			//	 DominatePointStructure point =(DominatePointStructure) ((Map.Entry)iter.next()).getKey();
//				 if 
//				 (AllSegments.containsKey(point)){
//					 
//					 // do something else 
//				 }
//			   
//				 else 
				 AllSegments.put(entry.getKey(), entry.getValue());
			}
		}
		
		}
		
		return AllSegments;
	}
	
	public polygonSolution divideStroke(Stroke stroke) {
		//  logger.trace(" Start divide Stroke");
		// now i will start the swarm system and initaize ti for the current
		// stroke
		// let is solve it then display the approximatin.
		SwarmSystem swarm = new SwarmSystem();
		// now create a list of 20 agents to solve the problem
		polygonAgent[] agents = new polygonAgent[AGENT_SIZE];

		polygonSolution sol;
		//  logger.trace("number of points in stroke "	+ stroke.getPointsCount());
		 SortedValueMap  pointmap = computeVerticesForPolygon(stroke);
		for (int i = 0; i < agents.length; i++) {
			agents[i] = new polygonAgent();
			sol = new polygonSolution(stroke);
			sol.setDominatePointMap(pointmap);
			sol.refineSolution();
			sol.calculateSolutionParameters();
			agents[i].setCurrentSolution(sol);
			agents[i].setLocalbest(sol);

		}

		// now intalize an global solution as a
		sol = new polygonSolution(stroke);
		sol.setDominatePointMap(pointmap);
		sol.refineSolution();
		sol.calculateSolutionParameters();
		

		//  logger.trace(" ----------------------------------------------------------");
		//  logger.trace(" Start ");
		// now run swarm
		polygonSolution Finalsol = (polygonSolution) swarm
				.solve(agents, sol, 1,-1);

		Finalsol.CheckLineSlopes();
		
		stroke.getStatisticalInfo().setPolyline(Finalsol);
		// display the solution
		//  logger.trace("solution vertices = "		+ Finalsol.getPolygonVertices().size());
		//  logger.trace("number of points in stroke "	+ stroke.getPointsCount());
		//  logger.trace("solution vertices =  "+ Finalsol.getPolygonVertices().size());
		if (logger.isDebugEnabled()) {
			//  logger.debug("divideStroke(Stroke) - final solution errror   " + Finalsol.getError() + "  vertices count  " + Finalsol.getPolygonVertices().size() + " (" + this.getClass().getSimpleName() + "    " + (new Throwable()).getStackTrace()[0].getLineNumber() + "  )  "); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		}
		// the solution points 
		
//		System.out.println(" RRRRRRRRRRRRRRRRRRRRRRRRRRRRr "+" (" + this.getClass().getSimpleName() + "    "
//				+ (new Throwable()).getStackTrace()[0].getLineNumber() + "  )  ");
//		for (int i = 0; i < Finalsol.getParticlePoints().length; i++) {
//			System.out.print("   "+Finalsol.getParticlePoints()[i]);
//		}
		for (int i = 0; i < Finalsol.getPolygonVertices().size(); i++) {
			//  logger.trace(Finalsol.getPolygonVertices().get(i).toString());
		}
		// stroke.
		//  logger.trace("  the error in polygon  " + Finalsol.error());
		// System.out.println(" the error in polygon "+Finalsol.error());
		// change this global solution with the computed speed pont and curvaute
		// change this global solution with the computed speed pont and curvaute

		Point tpoint;
		PointData p;
		// StrokeFitObject temp=new StrokeFitObject();
		//		
		// for (int i = 0; i < Finalsol.getParticlePoints().length; i++) {
		// if (Finalsol.getParticlePoints()[i]==1){
		// temp.addVertix(stroke.getPoints(i));
		// }
		// }

		// int polylayer=-1;
		// for (int i = 0; i < sketch.getLayersCount(); i++) {
		//			
		// if (sketch.getLayer(i).getLayerName().equalsIgnoreCase(polygonName))
		// {
		// polylayer=i;
		// break;
		// }
		// //
		// // if (sketch.getLayer(i) instanceof SketchSegmentionLayer) {
		// // polylayer=i;
		// // break;
		// //
		// // }
		//			
		// }
		// if (polylayer!=-1)
		// sketch.getLayer(polylayer).addObject(Finalsol);

		return Finalsol;
	}

	public DigitalCurveDivideSolution divideStrokeCurves(Stroke stroke) {

		//  //  //  logger.trace(" Start divide curve Stroke");
		// now i will start the swarm system and initaize ti for the current
		// stroke
		// let is solve it then display the approximatin.
		SwarmSystem swarm = new SwarmSystem();
		// now create a list of 20 agents to solve the problem
		DigitalCurveDivideAgent[] agents = new DigitalCurveDivideAgent[AGENT_SIZE];

		DigitalCurveDivideSolution sol;
		  //  logger.trace("number of points in stroke "	+ stroke.getPointsCount());

		for (int i = 0; i < agents.length; i++) {
			agents[i] = new DigitalCurveDivideAgent();
			sol = new DigitalCurveDivideSolution(stroke);
			agents[i].setCurrentSolution(sol);
			agents[i].setLocalbest(sol);

		}

		// now intalize an globa\l solution as a
		sol = new DigitalCurveDivideSolution(stroke);
	//  logger.trace(" ----------------------------------------------------------");
		//  //  logger.trace(" Start ");
		// now run swarm
		DigitalCurveDivideSolution Finalsol = (DigitalCurveDivideSolution) swarm
				.solve(agents, sol, 1,-1);
		Finalsol.setStoreShape(true);
		Finalsol.errorWithStore();
		
		Finalsol.CheckLineSlopes();
		
	
		// stroke.getStatisticalInfo().setPolyline(Finalsol);
		// display the solution
		// //  //  logger.trace("solution vertices =
		// "+Finalsol.getPolygonVertices().size());
		// //  //  logger.trace("number of points in stroke
		// "+stroke.getPointsCount());
		// //  //  logger.trace("solution vertices =
		// "+Finalsol.getPolygonVertices().size());
		// for (int i = 0; i < Finalsol.getPolygonVertices().size(); i++) {
		// //  //  logger.trace(Finalsol.getPolygonVertices().get(i).toString());
		// }
		// //stroke.
		//  //  logger.trace("  the error in polygon  " + Finalsol.error());
		// System.out.println(" the error in divide curve "+Finalsol.error());
		// change this global solution with the computed speed pont and curvaute
		// change this global solution with the computed speed pont and curvaute

		Point tpoint;
		PointData p;

		return Finalsol;

	}
	
	public GuiShape PreRecognizeStroke(Stroke stroke){
		logger.info("PreRecognizeStroke  //TODO: IMPLEMENT THIS FUNCTION 28 JAN"  );
		//TODO: IMPLEMENT THIS FUNCTION 28 JAN
		
		FittedShape  lineFit=	LineTest(stroke);
		
		
		
		// 
		return null;
	}
	private FittedShape LineTest(Stroke stroke){
		
		
		PointData p1,p2;
		
		// try if to create line using the fist and last point of the storke...
		Line line=new Line(stroke.getStartPoint(),stroke.getEndPoint());		
		// line ortognal distance from 
		double ErrorOrthognal=line.OrthognalError(stroke.getPoints());	
		 ErrorOrthognal =  ErrorOrthognal/stroke.getLength();
		logger.info( "  the simple line orthognal error is  "+ ErrorOrthognal);		
		// TRY TO MAKE THIS THERSHOLD % RESPECT TO LENGTH OF STROKE.....
		if (ErrorOrthognal<SystemSettings.THERSHOLD_PRE_RECOGNITION_LINE_FIT_ERROR){
		// This is least square error..........
		// now l is an approximate of line .. 
		CurveFitData data=new CurveFitData();
		
		data.computeInitalDat(stroke.getPoints());
		
		data.fitLine(stroke);
		double slope=data.slope;
		double  intercept=data.intercept;

	
		
		
	  
		double Error;
		//    // Errors (sd**2) on the:
	    // error of slope 
	  //Error = data. N/dem;
	  
	  
//	    // and slope
//	    parameters[3] = s/del;
//		
		
	 line=new Line(slope,intercept,stroke.getStartPoint(),stroke.getEndPoint());
		// now compute the error and feature area.... 
		
		
	 ErrorOrthognal=line.OrthognalError(stroke.getPoints());
		// check the feature area
//		logger.info( "  the  fit app  error is  "+ Error);
	 ErrorOrthognal =  ErrorOrthognal/stroke.getLength();
		logger.info(" orthigonal error is "+ErrorOrthognal);
		
//		fitError=0;
//		for (int i = 0; i <stroke.getPointsCount(); i++) {
//			x=stroke.getPoint(i).getX();
//			y=l2.solveY(x);
//			 
//			if (!Double.isNaN(y))
//				{
//				dif=y-stroke.getPoint(i).getY();
//				fitError+=Math.sqrt((dif)*(dif));
//				}
//		}
//		logger.info( "   he  fit error is "+fitError);
		 
		
		 FittedShape  shape;
		if (ErrorOrthognal<SystemSettings.THERSHOLD_RECOGNITION_LINE_FIT_ERROR){
		  shape=new 	 FittedShape  (line,ErrorOrthognal,true);
		}
		else {
			  shape=new 	 FittedShape  (line,ErrorOrthognal,false);
		}
		return shape;
		
		}
		else {
			 FittedShape  shape=new 	 FittedShape  (line,ErrorOrthognal,false);
			
			return shape;
		}
	}
	
//	private FittedShape  circleTest(Stroke stroke){
//		
//	}
}
