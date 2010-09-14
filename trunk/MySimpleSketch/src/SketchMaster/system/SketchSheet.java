package SketchMaster.system;

import org.apache.log4j.Logger;

import SketchMaster.Stroke.StrokeData.DominatePointStructure;
import SketchMaster.Stroke.StrokeData.PointData;
import SketchMaster.Stroke.StrokeData.SegmentCluster;
import SketchMaster.Stroke.StrokeData.Stroke;
import SketchMaster.Stroke.StrokeData.StrokeStatisticalData;
import SketchMaster.Stroke.features.FeatureFunction;

import SketchMaster.Stroke.graphics.layers.SketchLayer;

import SketchMaster.Stroke.graphics.layers.SketchSegmentionLayer;

import SketchMaster.Stroke.graphics.layers.SketchStrokesLayer;
import SketchMaster.Stroke.graphics.shapes.FittedShape;
import SketchMaster.Stroke.graphics.shapes.GuiShape; //import SketchMaster.Stroke.graphics.shapes.StrokeFitObject;
import SketchMaster.Stroke.SketchLayerArray;
import SketchMaster.system.SystemSettings;

import SketchMaster.gui.DrawingDebugUtils;
import SketchMaster.gui.DrawingSheet;
import SketchMaster.gui.Events.HandleStroke;
import SketchMaster.gui.Events.NewStrokeEvent;
import SketchMaster.io.log.FileLog;
import SketchMaster.swarm.Solution;
import SketchMaster.swarm.SwarmSystem;
import SketchMaster.swarm.curvefit.StrokeCurveAgent;
import SketchMaster.swarm.curvefit.StrokeCurveSolution;
import SketchMaster.swarm.polygonApproximations.DigitalCurveDivideSolution;
import SketchMaster.swarm.polygonApproximations.PolygonAgent;
import SketchMaster.swarm.polygonApproximations.PolygonSolution;
import SketchMaster.system.clustering.SystemClustering;
import SketchMaster.system.segmentation.SezginSegmentor;
import SketchMaster.system.segmentation.SketchSegmentors;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Observable;

/**
 * this is the class that contian all info about what are the strokes it handle
 * the new storkes recognizers and interperter for this sheets the sheet also
 * handle the it directly calls sketch , recongizer , interperter.
 * 
 * @author Mahi (sketchsheet.java l.44)
 */
public class SketchSheet extends Observable implements HandleStroke {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(SketchSheet.class);
	
	private static final  Logger logE=Logger.getLogger("ExampleLogging");;
	private static final String polygonName = "Polygon Layer";
	private static final String ellipseFitname = "Ellipse Fit Layer";
	private static final String LineFitname = "Line fit layer";

	// private Interperter interperter;

	/**
	 * @directed
	 */
	// private Symbol lnkSymbol;
	SketchLayerArray sketch;

	SystemClustering clusturing;

	private Rectangle Sketchrectangle;
	private boolean LayerChanged = true;
	private String multiCircleName = "Muliti circle line";
	private String SezignName = "Sezgin layer";

	public SketchLayer getCurrentLayer() {
		return sketch.getLayerSelect();

	}

	public void initSketch() {

		sketch = new SketchLayerArray();
		SketchStrokesLayer strokeslayer = new SketchStrokesLayer();
		strokeslayer.setLayerName(SystemSettings.StrokeLayerName);
		strokeslayer.setActive(true);
		sketch.addLayer(strokeslayer);

		sketch.setLayerSelected(0);
		SketchSegmentionLayer polygonglayer = new SketchSegmentionLayer();
		polygonglayer.setLayerName("Polygon Layer");
		polygonglayer.setActive(SystemSettings.Polygon_ACTIV_DEFAULT);
		sketch.addLayer(polygonglayer);

		SketchSegmentionLayer templayer;
		// templayer= new SketchSegmentionLayer();
		// templayer.setLayerName(LineFitname);
		// templayer.setActive(false);
		// sketch.addLayer(templayer);

		templayer = new SketchSegmentionLayer();
		templayer.setLayerName(ellipseFitname);
		templayer.setActive(true);
		sketch.addLayer(templayer);

		templayer = new SketchSegmentionLayer();
		templayer.setLayerName(multiCircleName);
		templayer.setActive(SystemSettings.Curve_ACTIV_DEFAULT);
		sketch.addLayer(templayer);

		templayer = new SketchSegmentionLayer();
		templayer.setLayerName(SezignName);
		templayer.setActive(SystemSettings.Sezgin_ACTIV_DEFAULT);
		sketch.addLayer(templayer);

		SketchSegmentionLayer symbollayer = new SketchSegmentionLayer();
		symbollayer.setLayerName(SystemSettings.SymbolLayerName);
		symbollayer.setActive(SystemSettings.Symbol_ACTIV_DEFAULT);
		sketch.addLayer(symbollayer);

	}

	public void initInterperters() {
	}

	public void interpertSketch() {

	}

	public void updateSketchInterpertation() {

	}

	// public void handleNewStrokeEvent() {
	// // either segment
	// // merge split
	// // classifiy
	//
	// }

	public void clearAllSketch() {
		this.sketch.getLayer(this.sketch.getLayerSelected()).ClearAll();
		clusturing.clearAll();
	}

	public void attachView(DrawingSheet drawSheet) {

		if (LayerChanged) {

			// drawSheet.removeAllDataDisplay();
			for (int i = 0; i < sketch.getLayersCount(); i++) {
				drawSheet.addDataDisplay(sketch.getLayer(i));

				// logger.info(" i am attaching the
				// layer"+sketch.getLayer(i).getLayerName());
			}
			LayerChanged = false;

		}
	}

	public void activateAllViews(boolean active) {

		for (int i = 0; i < sketch.getLayersCount(); i++) {
			sketch.getLayer(i).setActive(active);
			// logger.info("setting the layer "+i+" active "+active);
		}

	}

	public String[] getLayersNames() {

		String[] Names = new String[sketch.getLayersCount()];

		for (int i = 0; i < Names.length; i++) {
			Names[i] = sketch.getLayer(i).getLayerName();
		}

		return Names;
	}

	/**
	 * @param layer
	 * @param active
	 */
	public void setActiveLayer(int layer, boolean active) {
		// 
		sketch.getLayer(layer).setActive(active);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see SketchMaster.gui.Events.HandleStroke#HandleNewStroke(SketchMaster.gui.Events.NewStrokeEvent)
	 */
	public void HandleNewStroke(NewStrokeEvent Evt) {

		HandleStroke(Evt.getEventStroke());
		setChanged();
		notifyObservers();

		// now segment it and and divide it into segments.

		// check if is a gesture
		// check if it is symbol
	//	if (logger.isDebugEnabled()) {
			logger.trace("HandleNewStroke(NewStrokeEvent) - ------------------------------------------------------------------------    ( sketch sheet  221   ) "); //$NON-NLS-1$
	//	}

	}
	public Stroke PreProcessStroke(Stroke input){
	      
	

	// try remove repreats... 
	
	 // try to remove the tails 
	if (SystemSettings.USE_REMOVE_REPEAT){
		input=input.RemoveRepeatedPoints();
	}
	 
		if(SystemSettings.UseResampling){
		// try to interpolate first 
	     input=input.InterpolatePoints();
		}
		 if (SystemSettings.USE_PreProcess){
              input.PreProcess();
            //  input=input.getUnTracedStroke();
		 }
		 if (SystemSettings.REMOVE_OVER_TRACE){
			 
			 input=input.getUnTracedStroke();
		 }
		 return input;
	}
    
	public Stroke RemoveOverTraceStroke(Stroke input){
	      
	 
			 if (SystemSettings.REMOVE_OVER_TRACE){
				 
				 input=input.getUnTracedStroke();
			 }
			 return input;
		}
	private void HandleStroke(Stroke inputstroke){
		//  logger.trace("In the sketch sheet handle stroke");
		// logger.info("MY stroke listner");
		// logger.info("start point "
		// + Evt.getEventStroke().getStartPoint().toString()
		// + " end point = "
		// + Evt.getEventStroke().getEndPoint().toString()
		// + " The stroke time at release = "
		// + Evt.getEventStroke().getStrokeTime());

		// add the storke to the sheet
		
		Stroke stroke=PreProcessStroke(inputstroke);
		
		
		///use resample point for all next calcuation 
		sketch.addNewStroke(stroke);
		

		// / Now i have a new storke
		// calcualte pre processing data ==> stroke speed graph and
		// get best segmentation or hbyird fit.
		GuiShape segmentation = segmentStroke(stroke);
		//logger.info(" number of final segments "+segmentation)
		
		addStrokeToCluster(stroke, segmentation);
		
		
		
		if (DrawingDebugUtils.DEBUG_GRAPHICALLY)
			DrawingDebugUtils.DisplayChartsFrames(stroke);
		
		
		
		if (SystemSettings.DEBUG_MODE){
			
			logE.info("-----------------------------------------------------------------------------");
			//stroke.wirte(logE);
			// logE.info( segmentation.toString());
			 //ExampleLogging
			
		}
	
	}
	
	// now try the new algorithm I will be using for this 
	
	
	/**
	 * This function takes stroke and try to fit to cirecle or segment it to
	 * polygon or lins and arc then compare those segmentation to get the best
	 * fit between them.
	 * 
	 * @param stroke
	 *            stroke to segment
	 * @return the best segmentation from all the system.
	 */
	public GuiShape segmentStrokeWithoutPreRecognition(Stroke stroke) {
		SketchSegmentors segment = new SketchSegmentors();
		segment.generateDominatePoints(stroke);
		GuiShape sol = null;
		if (SystemSettings.DEBUG_MODE){
			
			logE.info("-----------------------------------------------------------------------------");
			//stroke.wirte(logE);
			// logE.info( segmentation.toString());
			 //ExampleLogging
			
		}

		if (SystemSettings.DEBUG_MODE){
			stroke.wirte(logE);
			logE.info(" number of point in this stroke is   "+stroke.getPointsCount());
		      logE.info(" number of  pdp  = "+stroke.getStatisticalInfo().getDominatePointsIndeces().size());
		    //  String s=" Pdp = [ ";
		      StringBuilder s=new StringBuilder (" Pdp = [ ");
		    ArrayList<DominatePointStructure> ind = stroke.getStatisticalInfo().getDominatePointsIndeces();
		    double x,y;
		      for (int i = 0; i < ind.size(); i++) {
		    	  x=stroke.getPoint(ind.get(i).getIndexInInk()).x;
		    	  y=stroke.getPoint(ind.get(i).getIndexInInk()).y;
				//s+=ind.get(i).getIndexInInk()+" ("+x+" , "+y+" ), ";
				s.append(ind.get(i).getIndexInInk());
				s.append(" (");
				s.append(x);
				s.append(" , ");
				s.append(y);
				s.append(" ), ");
			} 
		      s.append(" ]");
		      logE.info( s);
//		      s+=" ]";
//		      logE.info( s);
		      
		}
	      logger.info(" number of point in this stroke is   "+stroke.getPointsCount());
	      logger.info(" number of  pdp  = "+stroke.getStatisticalInfo().getDominatePointsIndeces().size());
	      
	      
	      
	      
	    //logger.info(" !!!!!!!!!!!!!!!!!!!!!!!!!!!!this is important need direct atttention !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"+" (" + this.getClass().getSimpleName() + "    "
//	    		+ (new Throwable()).getStackTrace()[0].getLineNumber() + "  )  ");
	    //logger.info(" !!!!!!!!!!!!!!!!!!!!!!!!!!!!this is important need direct atttention !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"+" (" + this.getClass().getSimpleName() + "    "
//	    		+ (new Throwable()).getStackTrace()[0].getLineNumber() + "  )  ");
	    //
	    //logger.info(" !!!!!!!!!!!!!!!!!!!!!!!!!!!!this is important need direct atttention !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"+" (" + this.getClass().getSimpleName() + "    "
//	    		+ (new Throwable()).getStackTrace()[0].getLineNumber() + "  )  ");
	    //
	    //logger.info(" !!!!!!!!!!!!!!!!!!!!!!!!!!!!this is important need direct atttention !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"+" (" + this.getClass().getSimpleName() + "    "
//	    		+ (new Throwable()).getStackTrace()[0].getLineNumber() + "  )  ");
	    //
	    //logger.info(" !!!!!!!!!!!!!!!!!!!!!!!!!!!!this is important need direct atttention !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"+" (" + this.getClass().getSimpleName() + "    "
//	    		+ (new Throwable()).getStackTrace()[0].getLineNumber() + "  )  ");
	    //
	    //
	    //logger.info(" !!!!!!!!!!!!!!!!!!!!!!!!!!!!this is important need direct atttention !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"+" (" + this.getClass().getSimpleName() + "    "
//	    		+ (new Throwable()).getStackTrace()[0].getLineNumber() + "  )  ");


	    		if (SystemSettings.FIT_LINE) {
	    			sol = segment.lineFit(stroke); // try to fit the line
	    			this.addFitToLayer(this.LineFitname, sol);
	    		}
	    		if (SystemSettings.FIT_CURVE) {
	    			if (stroke.getPoints().size()>0)
	    			sol = segment.curveFit(stroke); // try to fit the ellipse
	    			
	    		}
	    		// now i have the 
	    		
	    		if (sol instanceof StrokeCurveSolution) {
	    			StrokeCurveSolution curveApproximation = (StrokeCurveSolution) sol;
	    		 
	    			double cer=curveApproximation.getEllipseCertainty();
	    			
	    					if (SystemSettings.DEBUG_MODE){
	    		 //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	    			  logE.info( " The circle is "+curveApproximation.getEllipseString( ));
	    	          logE.info("segmentStoke(Stroke) - certainty  ===  " + cer + "  )  ");
	    				}
	    			if (logger.isDebugEnabled()) {
	    		
	    				
	    				logger.info("segmentStoke(Stroke) - certainty  ===  " + cer + " (" + this.getClass().getSimpleName() + "    " + (new Throwable()).getStackTrace()[0].getLineNumber() + "  )  "); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	    			}
	    			if (cer<0.25){
	    				logE.info(" Polygon divide...  "+sol);
	    				//diffently not a circle 
	    				return polygonizeStroke(stroke,segment);
	    			}
	    			if (cer>0.25){
	    				this.addFitToLayer(this.ellipseFitname, sol);
	    				if (SystemSettings.DEBUG_MODE){
	    					logE.info(" Ellipse detected...  "+sol); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

	    						}
	    				logger.info("  Single segment of circel    ");
	    				//diffently a circle 
	    				return sol;
	    			}
//	    			if (cer >8 && cer<8.5){ // it may be a circcle or a close for rectangle. 
//	    				
	    //
//	    				if (logger.isDebugEnabled()) {
//	    					logger.info("segmentStoke(Stroke) -  I need more info about both the circel and the     (" + this.getClass().getSimpleName() + "    " + (new Throwable()).getStackTrace()[0].getLineNumber() + "  )  "); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
//	    				}
//	    				GuiShape tempSol = polygonizeStroke(stroke,segment);
//	    				if (tempSol instanceof polygonSolution) {
//	    					polygonSolution polygon = (polygonSolution) tempSol;
//	    					double err=polygon.error();
//	    				
//	    					double errArea1=polygon.getErrorFromArea();
//	    					double cirArea = curveApproximation.getErrorFromArea();
//	    					
	    //
//	    					if (logger.isDebugEnabled()) {
//	    						logger.info("segmentStoke(Stroke) - polygon   error " + err + "   area error " + errArea1 + "  error main " + polygon.errorMain() + "   circle error  " + curveApproximation.getError() + "    are error  " + cirArea + " (" + this.getClass().getSimpleName() + "    " + (new Throwable()).getStackTrace()[0].getLineNumber() + "  )  "); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$
//	    					}
//	    				
//	    					if (cirArea>err){
//	    						// cirel more error 
//	    						return tempSol;
//	    					}
//	    					else 
//	    					{
//	    						this.addFitToLayer(this.ellipseFitname, sol);
//	    						return sol;
//	    					}
//	    				}
//	    				
//	    			}
	    			
	    			
	    		}
	    		
	    	
	    	return polygonizeStroke(stroke,segment);
	      
 

	}
	public GuiShape segmentStroke(Stroke stroke) {
		
		
		SketchSegmentors segment = new SketchSegmentors();
		segment.generateDominatePoints(stroke);
		GuiShape sol = null;
		if (SystemSettings.DEBUG_MODE){
			
			logE.info("-----------------------------------------------------------------------------");
			//stroke.wirte(logE);
			// logE.info( segmentation.toString());
			 //ExampleLogging
			
		}

		if (SystemSettings.DEBUG_MODE){
			stroke.wirte(logE);
			logE.info(" number of point in this stroke is   "+stroke.getPointsCount());
		      logE.info(" number of  pdp  = "+stroke.getStatisticalInfo().getDominatePointsIndeces().size());
		      StringBuilder s=new StringBuilder (" Pdp = [ ");
		    ArrayList<DominatePointStructure> ind = stroke.getStatisticalInfo().getDominatePointsIndeces();
		    double x,y;
		      for (int i = 0; i < ind.size(); i++) {
		    	  x=stroke.getPoint(ind.get(i).getIndexInInk()).x;
		    	  y=stroke.getPoint(ind.get(i).getIndexInInk()).y;
				s.append(ind.get(i).getIndexInInk());
				s.append(" (");
				s.append(x);
				s.append(" , ");
				s.append(y);
				s.append(" ), ");
			} 
		      s.append(" ]");
		      logE.info( s);
		      
		}
	      logger.info(" number of point in this stroke is   "+stroke.getPointsCount());
	      logger.info(" number of  pdp  = "+stroke.getStatisticalInfo().getDominatePointsIndeces().size());
	      
	      
	 
	      if (SystemSettings.USE_PRE_RECOGNIZIER){
	    	  
	      
		  	//TODO: IMPLEMENT THIS FUNCTION 28 JAN
	 	     logger.info(" segmentStroke PRE_RECOGNIZIER	//TODO: IMPLEMENT THIS FUNCTION 28 JAN  "); 
	 	     
	 	     
	      GuiShape Presol=   segment.PreRecognizeStroke(stroke);
	      
	      if( Presol==null){
	    	  // go the second alg 
	    		if (SystemSettings.FIT_DIVIDE_CURVE) {
	    			//		  
	    			if (stroke.getPoints().size()>0){
	    			DigitalCurveDivideSolution sol2 = segment.divideStrokeCurves(stroke);
	    			this.addFitToLayer(multiCircleName, (GuiShape) sol2);
	    			return sol;
	    			}
	    		}
	      }
	      else {
	   FittedShape fitShape=(   FittedShape)Presol; 
	 if (fitShape.getType()==FittedShape.TYPE_POLYLINE)
	 {
			if (SystemSettings.FIT_POLYGON) {
//				logger.info(" IIIIIIIIIIIIIIIIINNNNNNNNNNNNNNNN polygon "+" (" + this.getClass().getSimpleName()
//						+ "    "
//						+ (new Throwable()).getStackTrace()[0].getLineNumber()
//						+ "  )  ");
				PolygonSolution sol1 = segment.divideStroke(stroke); // try to fit the ellispe
				sol=(GuiShape) sol1;
				this.addFitToLayer(this.polygonName, (GuiShape) sol1);
				return sol;
			}
		 
	 }else {
		 
		 
		 return Presol;
	 }
	      }
	      
	      // if it has not returned till now th
	      return segmentStrokeWithoutPreRecognition(stroke) ; 
	 	     
	      }
	      else 
	    	  return segmentStrokeWithoutPreRecognition(stroke) ;
	


	}
 
	
	private GuiShape polygonizeStroke(Stroke stroke,	SketchSegmentors segment){
		
		PolygonSolution sol1=null,sol2=null,sol3=null;
		GuiShape sol = null;
		if (SystemSettings.FIT_POLYGON) {
//			logger.info(" IIIIIIIIIIIIIIIIINNNNNNNNNNNNNNNN polygon "+" (" + this.getClass().getSimpleName()
//					+ "    "
//					+ (new Throwable()).getStackTrace()[0].getLineNumber()
//					+ "  )  ");
			sol1 = segment.divideStroke(stroke); // try to fit the ellispe
			sol=(GuiShape) sol1;
			if (SystemSettings.SEGMENTATION_SWARM_SECOND_PASS)
			{
				Stroke Resultstroke = CreateStrokeFromPolySol(sol1);
				GuiShape Tempsol1 = segment.divideStroke(Resultstroke);
				// check error smaller then 
				// set it as sol 
				sol=Tempsol1;
				sol1=(PolygonSolution) (Tempsol1);
			}
			
			
			this.addFitToLayer(this.polygonName, (GuiShape) sol1);
			

			//if (logger.isDebugEnabled()) {
			//	logger.warn("polygonizeStroke(Stroke, SketchSegmentors) - try to find a way to determine if ellipse before getting into more swarm systems  (sketchsheet.java    246)"); //$NON-NLS-1$
			//}
//			if (logger.isDebugEnabled()) {
//				//  logger.debug("polygonizeStroke(Stroke, SketchSegmentors) -  try to calcuate the square  orthigonal distance from teh solution to stroke   (sketchsheet.java    247)"); //$NON-NLS-1$
//			}
		}
		if (SystemSettings.FIT_DIVIDE_CURVE) {
			//		  
			if (stroke.getPoints().size()>0){
			sol=(GuiShape) (sol2 = segment.divideStrokeCurves(stroke));
			this.addFitToLayer(multiCircleName, (GuiShape) sol2);
			}
		}

		if (SystemSettings.FIT_SEGIZEN) {
			SezginSegmentor segmentH = new SezginSegmentor();

			sol=(GuiShape) (sol3 = segmentH.runAlogrithm(stroke));
			this.addFitToLayer(SezignName, (GuiShape) sol3);
		}
		
		GuiShape BestSol = getBestSegmentationSol(sol1,sol2,sol3);
		 
		if (BestSol instanceof PolygonSolution) {
			PolygonSolution Bs = (PolygonSolution) BestSol;
			Bs.calculateSolutionParameters();
		
			logger.trace(" this is stroke has "+Bs.getPolygonVertices().size()+" vertices and  "+Bs.getSegmentsCount()+" segments");
		}
	          
		
		
		if (BestSol!=null)
		{
			
			
		return BestSol;
		}
	else {
		return sol;
	}
		
	}
	
	private Stroke CreateStrokeFromPolySol(PolygonSolution sol1) {
		sol1.calculateSolutionParameters();
		ArrayList<Point2D> tempPoints = sol1.getPolygonVertices();
		Stroke s=new Stroke();
	
		for (int i = 0; i < tempPoints.size(); i++) {
			if (i==0)
			s.setStartPoint((PointData) tempPoints.get(0));
			else if (i==(tempPoints.size()-1))
			{
				s.addPoint((PointData) tempPoints.get(tempPoints.size()-1));
				s.setEndPoint((PointData) tempPoints.get(tempPoints.size()-1));
			}
			else 
			s.addPoint((PointData) tempPoints.get(i));
		
		}
		if (SystemSettings.UseResampling)
		return s; // no need for resampling it will be usesless as run will  be same. 
		
		else {
			 //double temp=SystemSettings.MaxInterplotionLength;
			 
			return s.InterpolatePoints();
			 //return s;
		}
		
		
		
	//	return s;
	}

	private GuiShape getBestSegmentationSol(PolygonSolution sol1, PolygonSolution sol2, PolygonSolution sol3) {
	double e1=Double.MAX_VALUE,e2=Double.MAX_VALUE,e3=Double.MAX_VALUE,emin=Double.MAX_VALUE;
	if (sol1!=null){
		//if (sol1 instanceof PolygonSolution) {
		PolygonSolution temps = (PolygonSolution) sol1;
			if (SystemSettings.DEBUG_MODE){
				logE.info(" The particle "+temps);
			logE.info(" First Polygonization PSO = "+temps.getSegmentsString());
			}
			emin=e1=temps. getComparableError();
		}
	if (sol2!=null)
		if (sol2 instanceof DigitalCurveDivideSolution) {
			DigitalCurveDivideSolution temp = (DigitalCurveDivideSolution) sol2;
			emin=e2=temp. getComparableError();
			if (SystemSettings.DEBUG_MODE){
				logE.info(" The particle "+temp);
				logE.info(" second PSO =  "+temp.getSegmentsString());
				}
		}
	if (sol3!=null){
		//if (sol3 instanceof polygonSolution) {
			PolygonSolution temp = (PolygonSolution) sol3;
			emin=e3=temp. getComparableError();
			if (SystemSettings.DEBUG_MODE){
				logE.info("");
				}
		//}
	}
	if (SystemSettings.DEBUG_MODE){
		logE.info("  the error ALgS1 "+e1+"  Algs2 "+e2+"  Alg3 "+e3);
		}
	logger.info( "  the error s1 "+e1+"  s2 "+e2+"  s3 "+e3);
	if (e1<e2 && e1<e3)
	{
		logger.info(" the best segmenation is the first algorithm ");
		//e1 is min 
		return sol1;
	}
	else if (e2<e1 && e2<e3)
	{
		logger.info(" the best segmenation is the second algorithm ");
		//e1 is min 
		return sol2;
	}
	else if (e3<e2 && e3<e1)
	{
		logger.trace(" the best segmenation is the third algorithm ");
		//e1 is min 
		return sol3;
	}
		

		//if (logger.isDebugEnabled()) {
			//logger.warn("getBestSegmentationSol(GuiShape, GuiShape, GuiShape) - 
	//(sketchsheet.java    l.317)"); //$NON-NLS-1$
		//}
return null;
	}

	private void addFitToLayer(String layerName, GuiShape Finalsol) {
		int fitlayer = -1;
		for (int i = 0; i < sketch.getLayersCount(); i++) {
			if (sketch.getLayer(i).getLayerName().equalsIgnoreCase(layerName)) {
				fitlayer = i;
				break;
			}

		}

		if (fitlayer != -1) {
			sketch.getLayer(fitlayer).addObject(Finalsol);
			// sketch.getLayer(fitlayer).setActive(true);
		} else {
			LayerChanged = true;
			// logger.info("adding a new layer ");
			SketchSegmentionLayer templayer = new SketchSegmentionLayer();
			templayer.setLayerName(layerName);
			// templayer.setActive(true);
			templayer.addObject(Finalsol);
			sketch.addLayer(templayer);
		}
	}

	public boolean[] getLayerSelections() {
		boolean[] Names = new boolean[sketch.getLayersCount()];

		for (int i = 0; i < Names.length; i++) {
			Names[i] = sketch.getLayer(i).isActive();
		}

		return Names;
	}

	/**
	 * compute the best fit from the latel
 * @param sol1 y computed
	 * 
	 * @return a segmented fit
	 */
 

	/**
	 * Start the clustring algorithm inital object and set the sketch layer to
	 * it.
	 */
	public void initClustringAlgorithm() {

		clusturing = new SystemClustering();

		int fitlayer = -1;
		for (int i = 0; i < sketch.getLayersCount(); i++) {
			if (sketch.getLayer(i).getLayerName().equalsIgnoreCase(
					SystemSettings.SymbolLayerName)) {
				fitlayer = i;
				break;
			}

		}
		// logger.info("in the initalize fo clustersing algorithms ");
		if (fitlayer != -1)
			clusturing.setSymbolLayer(sketch.getLayer(fitlayer));

	}

	/**
	 * @param stroke
	 *            the stroke that is beign segmented.
	 * @param segmenation
	 *            the best segmentaion of the stroke to add it to the cluster
	 */
	public void addStrokeToCluster(Stroke stroke, GuiShape segmenation) {

		clusturing.addStrokeSegmentation(stroke, segmenation);

	}

	public int getNumberOfStrokeInCluster(){
		
		
		return clusturing.getUnClusteredStrokes().size();
	}
	
	/**
	 * 
	 */
	public SegmentCluster CreateNewSymbol() {

		SegmentCluster temp=clusturing.CreateSymbol();
            
        	if (SystemSettings.DEBUG_MODE){
        		logE.info("-----------------------------------------------------------------------------");
        		
    			logE.info(temp);
    			logE.info("-----------------------------------------------------------------------------");
    			
    			}
            
		setChanged();
		notifyObservers();
        
		return  temp;
	}

	public int[] getClusterStrokeCounts() {
		int [] count=null;
		int size=0;
		if (clusturing.getUnClusteredStrokes().size()>0)
		
			size=clusturing.getSymbols().size()+1;
		else 
			size=clusturing.getSymbols().size();
		count=new int[size];
		for (int j = 0; j < clusturing.getSymbols().size(); j++) {
		
			count[j]=clusturing.getSymbols().get(j).getStrokeInSymbol().size();
		}
		if (clusturing.getUnClusteredStrokes().size()>0)
		{
			count[count.length-1]=clusturing.getUnClusteredStrokes().size();
		}
		return count;
	}

	

}
