/**
 * 
 */
package SketchMaster.system.segmentation;

import org.apache.log4j.Logger;
import org.omg.CORBA.DATA_CONVERSION;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import SketchMaster.system.SystemSettings;
import SketchMaster.Stroke.StrokeData.DominatePointStructure;
import SketchMaster.Stroke.StrokeData.InkInterface;
import SketchMaster.Stroke.StrokeData.PointData;
import SketchMaster.Stroke.StrokeData.Stroke;
import SketchMaster.Stroke.graphics.layers.SketchSegmentionLayer;
import SketchMaster.Stroke.graphics.shapes.Ellipse;
import SketchMaster.Stroke.graphics.shapes.FittedShape;
import SketchMaster.Stroke.graphics.shapes.GuiShape;
import SketchMaster.Stroke.graphics.shapes.Line;
import SketchMaster.Stroke.graphics.shapes.ShapeRecognizier;
import SketchMaster.collection.NumericalComparator;
import SketchMaster.collection.SortedValueMap;
import SketchMaster.gui.Events.HandleStroke;
import SketchMaster.gui.Events.NewStrokeEvent;
import SketchMaster.io.log.FileLog;
import SketchMaster.lib.ComputationsGeometry;
import SketchMaster.lib.CurveFitData;
import SketchMaster.swarm.Solution;
import SketchMaster.swarm.SwarmSystem;
import SketchMaster.swarm.curvefit.StrokeCurveAgent;
import SketchMaster.swarm.curvefit.StrokeCurveSolution;
import SketchMaster.swarm.polygonApproximations.DigitalCurveDivideAgent;
import SketchMaster.swarm.polygonApproximations.DigitalCurveDivideSolution;
import SketchMaster.swarm.polygonApproximations.PolygonAgent;
import SketchMaster.swarm.polygonApproximations.PolygonSolution;
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
		// logger.info(" line error = "+Finalsol.eval()+" (sketch
		// segmentors 85 ) ");

		//  logger.trace("number of points in stroke "+ stroke.getPointsCount());
		//  logger.trace("Type = " + Finalsol.getType());
		//  logger.trace(" the parameters are ");
		for (int i = 0; i < Finalsol.getParticlesPoints().length; i++) {
			//  logger.trace(" param " + i + "  =  "	+ Finalsol.getParticlesPoints()[i]);
		}

		Finalsol.calculateSolutionParameters();
		// logger.info("the a = "+Finalsol.getCirclea());
		// logger.info("the b = "+Finalsol.getCircleb());
		// logger.info("the r = "+Finalsol.getCircleR());
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
		// // logger.info("The fit type of final solution is
		// "+Finalsol.getType());
		// temp.setFitType(Finalsol.getType());
		// //add the parameter of search
		// for (int i = 0; i < Finalsol.getParticlesPoints().length; i++) {
		// // if (i==4)
		// // logger.info("a== "+ Finalsol.getParticlesPoints()[i]);
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
//		// logger.info("the a = "+Finalsol.getCirclea());
//		// logger.info("the b = "+Finalsol.getCircleb());
//		// logger.info("the r = "+Finalsol.getCircleR());
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
//		// // logger.info("The fit type of final solution is
//		// "+Finalsol.getType());
//		// temp.setFitType(Finalsol.getType());
//		// //add the parameter of search
//		// for (int i = 0; i < Finalsol.getParticlesPoints().length; i++) {
//		// // if (i==4)
//		// // logger.info("a== "+ Finalsol.getParticlesPoints()[i]);
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
		int asize=AGENT_SIZE/2;
		// now create a list of 20 agents to solve the problem
		if (asize<3)
		{
			asize=3;
		}
		StrokeCurveAgent[] agents = new StrokeCurveAgent[asize];

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
   //logger.error( "  there is  nubmer of agnets is "+asize);
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
		// logger.info("the a = "+Finalsol.getCirclea());
		// logger.info("the b = "+Finalsol.getCircleb());
		// logger.info("the r = "+Finalsol.getCircleR());
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
		// // logger.info("The fit type of final solution is
		// "+Finalsol.getType());
		// temp.setFitType(Finalsol.getType());
		// //add the parameter of search
		// for (int i = 0; i < Finalsol.getParticlesPoints().length; i++) {
		// // if (i==4)
		// // logger.info("a== "+ Finalsol.getParticlesPoints()[i]);
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
	
	public PolygonSolution divideStroke(Stroke stroke) {
		//  logger.trace(" Start divide Stroke");
		// now i will start the swarm system and initaize ti for the current
		// stroke
		// let is solve it then display the approximatin.
		SwarmSystem swarm = new SwarmSystem();
		// now create a list of 20 agents to solve the problem
		PolygonAgent[] agents = new PolygonAgent[AGENT_SIZE];

		PolygonSolution sol;
		//  logger.trace("number of points in stroke "	+ stroke.getPointsCount());
		 SortedValueMap  pointmap = computeVerticesForPolygon(stroke);
		for (int i = 0; i < agents.length; i++) {
			agents[i] = new PolygonAgent();
			sol = new PolygonSolution(stroke);
			sol.setDominatePointMap(pointmap);
			sol.refineSolution();
			sol.calculateSolutionParameters();
			agents[i].setCurrentSolution(sol);
			agents[i].setLocalbest(sol);

		}

		// now intalize an global solution as a
		sol = new PolygonSolution(stroke);
		sol.setDominatePointMap(pointmap);
		sol.refineSolution();
		sol.calculateSolutionParameters();
		

		//  logger.trace(" ----------------------------------------------------------");
		//  logger.trace(" Start ");
		// now run swarm
		PolygonSolution Finalsol = (PolygonSolution) swarm
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
		
//		logger.info(" RRRRRRRRRRRRRRRRRRRRRRRRRRRRr "+" (" + this.getClass().getSimpleName() + "    "
//				+ (new Throwable()).getStackTrace()[0].getLineNumber() + "  )  ");
//		for (int i = 0; i < Finalsol.getParticlePoints().length; i++) {
//			System.out.print("   "+Finalsol.getParticlePoints()[i]);
//		}
		for (int i = 0; i < Finalsol.getPolygonVertices().size(); i++) {
			//  logger.trace(Finalsol.getPolygonVertices().get(i).toString());
		}
		// stroke.
		//  logger.trace("  the error in polygon  " + Finalsol.error());
		// logger.info(" the error in polygon "+Finalsol.error());
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
		// logger.info(" the error in divide curve "+Finalsol.error());
		// change this global solution with the computed speed pont and curvaute
		// change this global solution with the computed speed pont and curvaute

		Point tpoint;
		PointData p;

		return Finalsol;

	}
	
	public GuiShape PreRecognizeStroke(Stroke stroke){
		logger.info("PreRecognizeStroke  //TODO: IMPLEMENT THIS FUNCTION 28 JAN _ now test 3rd of may "  );
		//TODO: IMPLEMENT THIS FUNCTION 28 JAN
		ShapeRecognizier test=new ShapeRecognizier();
		FittedShape  lineFit=	test.LineTest(stroke);
	 logger.info("  the fitted line ....    "+lineFit);
	 if (lineFit.isAccepted() ){
		 
		 return lineFit;
	 }
		FittedShape  ellipseFit= test.ellipseTest(stroke);
		FittedShape  circleTest=test.circleTest(stroke);
		if (ellipseFit!=null  ){
			logger.info(ellipseFit);
		if (ellipseFit.isAccepted() ){
			logger.info("  accepted ellipse fit..... ");
			
			if (circleTest!=null){
			if(circleTest.isAccepted()){
				
				// check which is min 
				if (circleTest.getError()<=ellipseFit.getError()){
					return circleTest;
				}
				else {
					return ellipseFit;
				}
			}
			}
			 
				return ellipseFit;
		}
		}
		
		FittedShape ployTest=test.polylineTest(stroke);  /// directly go the ALGS1 only ..... 
		//boolean 
	if (ployTest!=null)	
		logger.info( "  poly test is "+ployTest.isAccepted()+"  because error is "+ployTest.getError());
		if (ployTest.isAccepted())
			return ployTest;
		
		FittedShape  arcTest=test.arcTest(stroke);
		if (arcTest!=null)
		if (arcTest.isAccepted()){
			return arcTest;
		}
		
	
		
		FittedShape  sprialHelixTest=test.sprialHelixTest(stroke);
		if (sprialHelixTest!=null)
		if (sprialHelixTest.isAccepted()){
			return sprialHelixTest;
		}
		
	FittedShape  curveTest=test.curveTest(stroke);
	if (curveTest!=null)
	if ( curveTest.isAccepted()){
		return  curveTest;
	}
	
	//FittedShape   curveTest=test.complexTest(stroke);
		
		// 
		return null;
	}
//	private FittedShape LineTest(Stroke stroke){
//		
//		 FittedShape  shape=new FittedShape();
//		PointData p1,p2;
//		
//		// try if to create line using the fist and last point of the storke...
//		Line line=new Line(stroke.getStartPoint(),stroke.getEndPoint());		
//		// line ortognal distance from 
//		double ErrorOrthognal=line.OrthognalError(stroke.getPoints());	
//		 ErrorOrthognal =  ErrorOrthognal/stroke.getLength();
//		logger.info( "  the simple line orthognal error is  "+ ErrorOrthognal);		
//		if (ErrorOrthognal<SystemSettings.THERSHOLD_PRE_RECOGNITION_LINE_FIT_ERROR){
//		
//		shape.fitLine(stroke);
//		//data.fitLine(stroke);
//		double slope=shape.slope;
//		double  intercept=shape.intercept;
//	  
//		double Error;
//		line=new Line(slope,intercept,stroke.getStartPoint(),stroke.getEndPoint());
//		// now compute the error and feature area.... 	
//	 ErrorOrthognal=line.OrthognalError(stroke.getPoints()); 
//	 ErrorOrthognal =  ErrorOrthognal/stroke.getLength();
//		logger.info(" orthigonal error is "+ErrorOrthognal);
//		if (ErrorOrthognal<SystemSettings.THERSHOLD_RECOGNITION_LINE_FIT_ERROR){
//		  shape=new 	 FittedShape  (line,ErrorOrthognal,true);
//		}
//		else {
//			  shape=new 	 FittedShape  (line,ErrorOrthognal,false);
//		}
//		return shape;
//		
//		}
//		else {
//		  shape=new 	 FittedShape  (line,ErrorOrthognal,false);
//			
//			return shape;private FittedShape  circleTest(Stroke stroke){
//				//
////			    private boolean polylineTest(Stroke stroke){
//
//		}
//	}
	 
//	private FittedShape  circleTest(Stroke stroke){
//		
//		 FittedShape  shape=new FittedShape();		
//		 // NDDR  ndde must be high ,
//		//larges chord to length must be low Only if not overtraced..
//		//if overtrace // cut at 2 pi
//		// must be closed. 
//		// feature area... vs. area of ideal ellipse (see my code )
//		
//		//intially.. get max axis as longest chord then get the 
//	
//		 PointData ps ,pe;
//		  ps = stroke.getLargestChordStart();
//		  pe=stroke.getLargestChordEnd();
//		  Line l=new Line(ps,pe);
//		  PointData mid = l.getMidpoint();
//// 
////		  
////		  double slopeOfline=-1.0/(l.Slope());
//		  
//             Line l2=l.getBisector();
//		
//             double cx,cy;
//             cx=stroke.getStatisticalInfo().Sums().Ex/(double)stroke.getStatisticalInfo().Sums().N;
//             cy=stroke.getStatisticalInfo().Sums().Ey/(double)stroke.getStatisticalInfo().Sums().N;
//		//center is the avearge of the point sumx/n  and sumy/n
//		PointData center=new PointData(cx,cy);
//		
//		
//		
//		// now this is the center, largest chort for the a, now i want to get the shortest chord length.... 
//		// i have to get the intersection with the stroke...... 
//		
//		//first divide the stroke into set lines (mainly based on length... 
//		 ArrayList<Line> lines = stroke.toLines();
//		// there may be more than two intersection.... so save all..  
//               ArrayList< PointData>  intersections=new   ArrayList< PointData> ();
//		 for (int i = 0; i < lines.size(); i++) {
//			
//			 if (l2.isIntersect(lines.get(i)))
//			 {
//				 // the intersection .... 
//				 PointData inter = l2.getIntersection(lines.get(i));
//				 
//				 // 
//				 intersections.add(inter);
//				 
//			 }
//			 
//		}
//		 logger.info("  there are ..   "+intersections.size()+"     which are  "+intersections);
//		 // get the farthest interection points  to dertermine the extreeimes of the line.. 
//		 double maxlengthLeft=0,	 maxlengthRight=0;
//		int  firstpointindex=-1, secondpointindex=-1;
//	 // on for left o fthe line and 
//		 
//		 for (int i = 0; i < intersections.size(); i++) {
//				
//		 
//	double dis=mid.distance(intersections.get(i)) ;
//		 
//		 if (ComputationsGeometry.Left((PointData)l.getStartPoint(), (PointData)l.getEndPoint(), intersections.get(i))){
//			 
//			 // get check the distance from mid point... 
//			if (maxlengthLeft<dis){
//				maxlengthLeft=dis;
//				
//				firstpointindex=i;
//			}
//			 
//			 
//		 }
//		 else {  	 // on on right .. 
//			 // as i am sure they are not collinear.... 
//			 // get check the distance from mid point... 
//				if (maxlengthRight<dis) {
//					maxlengthRight=dis;
//					
//				 secondpointindex=i;
//				}
//			 
//			 }
//		 }
//		 if ((firstpointindex!=-1 )&& (secondpointindex!=-1))
//		 {
//				l2.setStartPoint(  intersections.get(  firstpointindex)); 
//				l2.setEndPoint(  intersections.get( secondpointindex));
//				
//				// now get the lenght of the radius...
//				
//				
//				// logger.info
//				logger.info(l2);
//				
//				logger.info( "  length of small bisection si ....    "  + l2.length());
//				// line perpendicular to it. 
//				 Ellipse e=new Ellipse(cx,cy,l,l2);
//				e.setEllipseParam(cx, cy,l.length()/2.0, l2.length()/2.0);
//				
//				
//		 }
//		
//		return null;
//	}
//
//    private boolean polylineTest(Stroke stroke){
//    	
//    	ArrayList<DominatePointStructure> pd = stroke.getStatisticalInfo().getDominatePointsIndeces();
//    
//    	boolean poly=true;
//    	 // get the start and end of each sub 
//    	for (int i = 0; i < pd.size(); i++) {
//    		DominatePointStructure temp = pd.get(i);
//    		
//			int start = temp.getIndexInInk();
//			
//			int end = pd.get(i+1).getIndexInInk();
//			
//			if (start==end )
//			{
//				continue;
//			}			
//			
//			InkInterface ts = stroke.createSubInkObject(start, end);
//		 
//			// try if to create line using the fist and last point of the storke...
//			Line line=new Line(ts.getPoint(0),ts.getPoint( ts.getPoints().size()-1));		
//			// line ortognal distance from 
//			double ErrorOrthognal=line.OrthognalError(ts.getPoints());	
//		 ErrorOrthognal =  ErrorOrthognal/stroke.getLength();
//    		logger.info( "  the simple line orthognal error is  "+ ErrorOrthognal);		
//    		if (ErrorOrthognal>SystemSettings.THERSHOLD_PRE_RECOGNITION_LINE_FIT_ERROR){
//            
//    			return false;
//    		}
//    		
//    	}
//    	//  if all is correct then 
//    	
//    	
//    	return true;
//    }

}
