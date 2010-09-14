package SketchMaster.system;

import org.apache.log4j.Logger;

import SketchMaster.Stroke.StrokeData.Stroke;
import SketchMaster.Stroke.features.FeatureFunction;
import SketchMaster.Stroke.graphics.layers.SketchLayer;
import SketchMaster.Stroke.graphics.layers.SketchStrokesLayer;
import SketchMaster.Stroke.graphics.shapes.GuiShape;
import SketchMaster.gui.DrawingSheet;
import SketchMaster.gui.GraphWatched;
import SketchMaster.gui.Events.HandleFinishCluster;
import SketchMaster.gui.Events.HandleStroke;
import SketchMaster.gui.Events.NewClusterEvent;
import SketchMaster.gui.Events.NewStrokeEvent; //import SketchMaster.gui.GraphFrame;
//import SketchMaster.gui.SketchFrame;
import SketchMaster.io.StrokeReaderWriter;
import SketchMaster.io.log.FileLog;
import SketchMaster.system.Recogniziers.RecognizierSystem;
import SketchMaster.system.Recogniziers.RubineRecognizier;
import SketchMaster.system.Recogniziers.SVMRecognizier;
import SketchMaster.system.Recogniziers.SimpleSymbolRecognizier;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * this is the class that control the system at initalization this is the class
 * that create all frames it is initalized from the main class it handle all
 * passing of decisions it act as the outer program it directly call frame ,
 * drawing drawSheet and sketch drawSheet
 */
public class SketchRecognitionSystem implements Observer, HandleFinishCluster {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(SketchRecognitionSystem.class);

	int StrokeNumber = 1;
	// SketchFrame sketchframe;

	SketchSheet sheetdata;

	GraphDrawing ftest;

	DrawingSheet drawSheet = null;

	RecognizierSystem recognizier;

	private static int Classifier; // classifier used

	private static int SystemMode; // training 0 or classifaying 1 or sampling

	public void clearAllSketch() {

		sheetdata.sketch.clearAll();
		sheetdata.clearAllSketch();
		 //recognizier.Clear();
		drawSheet.repaint();
	}

	public SketchRecognitionSystem() {
		// to check which mode i am in
		getSystemProperties();
		// now create the data store
		sheetdata = new SketchSheet();

		// init the sketch data and sketchs layer
		sheetdata.initSketch();
		sheetdata.initClustringAlgorithm();
		sheetdata.addObserver(this);

	}



	private void getSystemProperties() {

	}

	/**
	 * @author maha
	 */
	public class GraphDrawing implements HandleStroke {
		// GraphFrame frame = null;
		Stroke stroke = null;

		public void HandleNewStroke(NewStrokeEvent Evt) {
			//  logger.trace("\n----------------------------------------------");
			//  logger.trace("\nNew Stroke  " + StrokeNumber);
			stroke = Evt.getEventStroke();
			showAllGraphs();

		}

		public void showAllGraphs() {
		}

		
	}
	public void HandleFinishedCluster(NewClusterEvent Evt) {
		
	   CreateCluster();	
	}
	public String[] getLayers() {
		return sheetdata.getLayersNames();
	}

	public boolean[] getLayerSelections() {
		return sheetdata.getLayerSelections();
	}

	public void setActiveLayer(int layer, boolean active) {
		sheetdata.attachView(drawSheet);
		sheetdata.setActiveLayer(layer, active);

	}

	public void ViewAll(boolean active) {

		sheetdata.attachView(drawSheet);
		sheetdata.activateAllViews(active);

		// logger.info("i will make all layer active");
		// sheetdata.sketch.paint((Graphics2D)drawSheet.getGraphics());

	}

	public void saveStrokes(String FileName) {

		// get the layer ffor the strokes
		int storkeLayer = -1;
		for (int i = 0; i < sheetdata.sketch.getLayersCount(); i++) {
			if (sheetdata.sketch.getLayer(i) instanceof SketchStrokesLayer) {
				if (sheetdata.sketch.getLayer(i).getLayerName().equals(
						SystemSettings.StrokeLayerName))
					storkeLayer = i;
			}
		}
		ArrayList Strokes = null;
		if (storkeLayer != -1) {
			Strokes = ((SketchStrokesLayer) sheetdata.sketch
					.getLayer(storkeLayer)).getStrokes();
		}
		StrokeReaderWriter write = new StrokeReaderWriter();
		write.createFile(FileName);
             write.writeStrokeCounts(sheetdata.getClusterStrokeCounts());
		if (Strokes != null) {
			for (int i = 0; i < Strokes.size(); i++) {
				if (logger.isDebugEnabled()) {
					//  logger.debug("saveStrokes(String) - stroke info " + Strokes.get(i) + "  ( sketch recognition system   166 )   "); //$NON-NLS-1$ //$NON-NLS-2$
				}
				write.WriteStroke((Stroke) Strokes.get(i));
			}
		}
		write.closeWrite();
		if (logger.isDebugEnabled()) {
			//  logger.debug("saveStrokes(String) - ---------------Finished --------------   ( sketch recognition system   171 )  "); //$NON-NLS-1$
		}

	}

	public void readStrokes(String FileName) {

		Stroke temp;
		ArrayList strokes = new ArrayList<Stroke>();
		StrokeReaderWriter reader = new StrokeReaderWriter();
		int[] countStrokes;
		// reader.readFile( FileName);
		if (reader.readFile(FileName)) {
			countStrokes=reader.readStrokeCounts();
			// no error in reading file
			while ((temp = reader.ReadStroke()) != null) {
				// temp.calculateStrokeData();
				strokes.add(temp);
			}

			// get the layer ffor the strokes
			int storkeLayer = -1;
			for (int i = 0; i < sheetdata.sketch.getLayersCount(); i++) {
				if (sheetdata.sketch.getLayer(i) instanceof SketchStrokesLayer) {
					storkeLayer = i;
				}
			}
			// ArrayList Strokes=null;
			if (storkeLayer != -1) {
				((SketchStrokesLayer) sheetdata.sketch.getLayer(storkeLayer))
						.setStrokes(strokes);
			}
			reader.closeRead();

			if (logger.isDebugEnabled()) {
				//  logger.debug("readStrokes(String) - ---------------Finished --------------" + strokes.size() + "  ( sketch recognition system  203  ) "); //$NON-NLS-1$ //$NON-NLS-2$
			}
			
			int SymbolsStrokeCount=0,j=0,strokeIndex=1;
			if (countStrokes!=null&&countStrokes.length>0)
				SymbolsStrokeCount=countStrokes[0];
			for (int i = 0; i < strokes.size(); i++,strokeIndex++) {
			
				
				if (logger.isDebugEnabled()) {
					//  logger.debug("readStrokes(String) - ----------Stroke  " + i + "  ---------   ( sketch recognition system   205 ) "); //$NON-NLS-1$ //$NON-NLS-2$
				}
				Stroke s=(Stroke )strokes.get(i);
				Stroke stroke=sheetdata.PreProcessStroke(s);
				GuiShape segmentation = sheetdata.segmentStroke(stroke);
				sheetdata.addStrokeToCluster(stroke, segmentation);
				if (countStrokes!=null)
				{
					if (strokeIndex==SymbolsStrokeCount)// end of symbol 
						
					{
				//		logger.info("sssssssssssssssssssssssssssssssss stroke "+i+"  symbol   "+j+"  of "+countStrokes.length);
						
						sheetdata.CreateNewSymbol();
						j++;
						if (j < countStrokes.length){
						
							SymbolsStrokeCount=countStrokes[j];
					//		logger.info("    the next one will have the follwing "+SymbolsStrokeCount);
							strokeIndex=0;
						}
					}
				}
				
				
			}
			// logger.info("Finished dividing now fitting ");
			// for (int i = 0; i < strokes.size(); i++) {
			// sheetdata.curveFit((Stroke)strokes.get(i));
			// }

		}
		drawSheet.repaint();

	}

	public void addDrawingSheet(DrawingSheet drawSheet2) {

		this.drawSheet = drawSheet2;

		// hadling a new storke using recognizer
		drawSheet.addstrokeListener(sheetdata);
		drawSheet.addstrokeListener(recognizier);
		drawSheet.addClusterListener(this);
		drawSheet.addDataDisplay(this);
		// ArrayList<SketchLayer> layers=sheetdata.getAllLayers();
		sheetdata.attachView(drawSheet);

		// graphing a new storke speed and other statisticals
		// ftest = new graphDrawing();
		// drawSheet.addstrokeListener(ftest);

	}

	public void update(Observable ob, Object g) {
		// logger.info("HHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHh");
		if (ob instanceof GraphWatched) {
			GraphWatched gr = (GraphWatched) ob;
			// logger.info("iiiiiiiiiiiiiiiiii update ");
		}
		if (ob instanceof SketchSheet) {
			SketchSheet data = (SketchSheet) ob;

			// this need to be repainted.
			drawSheet.repaint();

		}

		// is am in observalble

		// logger.info(" i am in observable");

	}

	public void getSystemOptions() {

	}

	public RecognizierSystem getRubineRecognizier() {
		if (recognizier == null || !(recognizier instanceof RubineRecognizier)) {
			recognizier = new RubineRecognizier();
			recognizier.init();
		}
		return recognizier;
	}

	public RecognizierSystem getSymbolRecognizier() {

		if (recognizier == null
				|| !(recognizier instanceof SimpleSymbolRecognizier)) {
			recognizier = new SimpleSymbolRecognizier();
			recognizier.init();
		}

		// logger.info("");

		return recognizier;
	}
	
	public   RecognizierSystem  getSVMRecognizier(){
		
		if (recognizier == null
				|| !(recognizier instanceof SVMRecognizier)) {
			recognizier = new SVMRecognizier();
			recognizier.init();
		}

		// logger.info("");

		return recognizier;
		
	}

	public void CreateCluster() {

		this.sheetdata.CreateNewSymbol();

	}
}
