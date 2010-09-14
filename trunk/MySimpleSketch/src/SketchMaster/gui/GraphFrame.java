package SketchMaster.gui;


//import jahuwaldt.plot.CircleSymbol;
//import jahuwaldt.plot.Plot2D;
//import jahuwaldt.plot.PlotPanel;
//import jahuwaldt.plot.PlotRun;
//import jahuwaldt.plot.PlotRunList;
//import jahuwaldt.plot.SimplePlotXY;
//import jahuwaldt.plot.SquareSymbol;


import SketchMaster.Stroke.features.FeatureFunction;
import SketchMaster.lib.Vector;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;

import java.awt.Rectangle;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.geom.Point2D;

import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;

import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;

import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;


/**
 * @author  Mahi
 */
public class GraphFrame extends JFrame implements FocusListener {

	// PlotPanel plotpanel=null;

	/**
	 * 
	 */
	private static final long serialVersionUID = 8471682471495961363L;

	/**
	 * @uml.property  name="points"
	 */
	ArrayList points;

	// Plot2D aPlot;
	double StartX;
	// Plot2D aPlot;
	double StartY;

	XYSeriesCollection dataset;

	/**
	 * @uml.property  name="xAxisName"
	 */
	String xAxisName;
	/**
	 * @uml.property  name="yAxisName"
	 */
	String yAxisName;
	/**
	 * @uml.property  name="graphName"
	 */
	String graphName;

	private JPanel panel;

	JFreeChart chart;

	private ChartPanel panel1;

	private XYSeries dSeries;

	public GraphFrame() throws HeadlessException {
		super();
		 
	}

	public GraphFrame(GraphicsConfiguration arg0) {
		super(arg0);
		 
	}

	public GraphFrame(String arg0) throws HeadlessException {
		super(arg0);
		 
	}

	public GraphFrame(String arg0, GraphicsConfiguration arg1) {
		super(arg0, arg1);
		 
	}

	public void init() {
		try {
			jbInit();
			// SketchSystem system = new SketchSystem(this);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	private void jbInit() throws Exception {
		  
		this.setSize(600, 600);
		this.setLocation(600, 100);
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		// this.setVisible(true);
//		this.setTitle(" The speed graph  ");
//		graphName = "speed graph";
//		xAxisName = "distance";
//		yAxisName = "velcoity";

		panel = new JPanel();
		panel.setBounds(10, 10, 500, 500);
		// Add the series to your data set
		dataset = new XYSeriesCollection();

		// /// the initail plots
		// double[] xArr = {1., 2., 10, 20, 30, 40, 50};
		// double[] yArr = {0.06, 0.01, -0.01, -0.02, -0.03, -0.05, -0.075};
		// double[] yArrAlt = {0.08, 0.03, 0.01, 0.0, -0.01, -0.03, -0.045};
		// setPointsToPlot();
		// plotpanel = new PlotPanel(aPlot);
		// plotpanel .setBackground( Color.white );
		// this.add(plotpanel );

		// plotpanel = new PlotPanel();
		// plotpanel.setBounds(50, 40, 200, 200);
		// / plotpanel.setBackground(Color.WHITE);
		// this.getContentPane().add(plotpanel);
		// XYDataset xyDataset = new XYSeriesCollection(series);
//		chart = ChartFactory.createXYLineChart(graphName, // Title
//				xAxisName, // X-Axis label
//				yAxisName, // Y-Axis label
//				dataset, PlotOrientation.VERTICAL, // Dataset
//				true, true, true// Show legend
//
//				);
//
//		panel1 = new ChartPanel(chart);
//
//		// panel.addFocusListener(this);
//		this.getContentPane().add(panel1);

	}

	private void plotFunction(Graphics g, ArrayList l) {
		points = l;
		// plotFunction(g);

	}

	private void plotFunction(ArrayList l) {
		//Graphics g = this.getGraphics();
		points = l;
		// plotFunction(g);
	}

	/**
	 * @return  the points
	 * @uml.property  name="points"
	 */
	public ArrayList getPoints() {
		return points;
	}

	/**
	 * @param points  the points to set
	 * @uml.property  name="points"
	 */
	public void setPoints(ArrayList points) {
		this.points = points;
		// setPointsToPlot();

		// repaint();
	}

	private void setPointsToPlot() {

		// /to delete if not neeeded in two weeks from 28 july
		// double [] xArr,yArr;
		// xArr=new double [points.size()];
		// yArr=new double [points.size()];
		//		
		// for (int i = 0; i < points.size(); i++) {
		// p=(Vector)points.get(i);
		// xArr[i]=p.getX();
		// yArr[i]=p.getY();
		// }

		// series.add(20.0, 10.0);
		// series.add(40.0, 20.0);
		// series.add(70.0, 50.0);
		// if (plotpanel!=null)
		// //{
		// aPlot=(Plot2D)this.plotpanel.getPlot(); // Create a 2nd run and add
		// it to the plot.
		// //PlotRunList runs = aPlot.getRuns();
		// //runs.add(new PlotRun(xArr, yArr, true, new SquareSymbol()));
		// //}
		// //else {
		// // PlotRunList runs = aPlot.getRuns();
		// // runs.add(new PlotRun(xArr, yArr, true, new SquareSymbol()));
		// aPlot = new SimplePlotXY(xArr, yArr, "Velicity Graph", "Distance
		// Axis", "Speed Axis",
		// null, null, new CircleSymbString sGraphName ol());
		// }

	}
	public void addChartFunction( int type, FeatureFunction function)
	{
		String sGraphName=function.getName();
		XYSeries series = new XYSeries(sGraphName);
		
		ArrayList<Point2D> points=function.getDataForPloting();
		Point2D p;
		if (points==null)
			return ;
		for (int i = 0; i < points.size(); i++) {
			p = (Point2D) points.get(i);
			series.add(p.getX(), p.getY());
			// yArr[i]=p.getY();
		}
		
		dataset.addSeries(series);
		xAxisName=function.getXName();
		yAxisName=function.getYName();
		this.setTitle(function.getName());
		// XYDataset xyDataset = new XYSeriesCollection(series);
		chart = ChartFactory.createXYLineChart(graphName, // Title
				function.getXName(), // X-Axis label
				function.getYName(), // Y-Axis label
				dataset, PlotOrientation.VERTICAL, // Dataset
				true, true, false // Show legend
				);
		if (type == 0) {

		} else if (type == 1) {

			XYItemRenderer rend = chart.getXYPlot().getRenderer();
			Rectangle rect = new Rectangle();
			// rect.setBounds(0, 0, 2, 2);
			rect.setSize(2, 2);
			rend.setSeriesShape(dataset.getSeriesCount() - 1, rect);
			// StandardXYItemRenderer rr = (StandardXYItemRenderer)rend;
			// rr.setPlotLines(true);

		}
		
		
	}
	public XYSeries  addPointsToSeries(String Name,ArrayList<Point2D> points){
		XYSeries series = new XYSeries(Name);
		Point2D p;
		if (points==null)
			return series ;
		for (int i = 0; i < points.size(); i++) {
			p = (Point2D) points.get(i);
			series.add(p.getX(),p.getY());
			//series.add(p.getY(),p.getX());
			// yArr[i]=p.getY();
		}
		return series;
		
	}
	

	public void DisplayChartFunction(String sGraphName, int type, ArrayList apoints) {
		setPoints(apoints);
		Vector p;

		XYSeries series = new XYSeries(sGraphName);
		for (int i = 0; i < points.size(); i++) {
			p = (Vector) points.get(i);
			series.add(p.getX(), p.getY());
			// yArr[i]=p.getY();
		}

		dataset.addSeries(series);
		// XYDataset xyDataset = new XYSeriesCollection(series);
		chart = ChartFactory.createXYLineChart(graphName, // Title
				xAxisName, // X-Axis label
				yAxisName, // Y-Axis label
				dataset, PlotOrientation.VERTICAL, // Dataset
				true, true, false // Show legend
				);
		if (type == 0) {

		} else if (type == 1) {

			XYItemRenderer rend = chart.getXYPlot().getRenderer();
			Rectangle rect = new Rectangle();
			// rect.setBounds(0, 0, 2, 2);
			rect.setSize(2, 2);
			rend.setSeriesShape(dataset.getSeriesCount() - 1, rect);
			// StandardXYItemRenderer rr = (StandardXYItemRenderer)rend;
			// rr.setPlotLines(true);

		}

		// this.repaint();// repaint();

	}
	public void DisplayFunctionProcessing(FeatureFunction function){
	//	String sGraphName=function.getName();
//		XYSeries series = new XYSeries(sGraphName);
		
		ArrayList<Point2D> points=function.getDataForPloting();
		
		
		
		
		
		dataset.addSeries(addPointsToSeries("Orginal  data ",points));
		
		 points=function.localMaxMinDataForPlot();
		dataset.addSeries(addPointsToSeries("after the drivetiv ",points));
		
		
		xAxisName=function.getXName();
		yAxisName=function.getYName();
		this.setTitle(function.getName());
		// XYDataset xyDataset = new XYSeriesCollection(series);
		chart = ChartFactory.createXYLineChart(graphName, // Title
				function.getXName(), // X-Axis label
				function.getYName(), // Y-Axis label
				dataset, PlotOrientation.VERTICAL, // Dataset
				true, true, false // Show legend
				);
		
			XYItemRenderer r = chart.getXYPlot().getRenderer();
		   
		        if (r instanceof XYLineAndShapeRenderer) {
		            XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) r;
		            renderer.setBaseShapesVisible(true);
		            renderer.setBaseShapesFilled(true);
		        }
		        
		        
//			Rectangle rect = new Rectangle();
//			// rect.setBounds(0, 0, 2, 2);
//			rect.setSize(2, 2);
//			rend.setSeriesShape(dataset.getSeriesCount() - 1, rect);
//			rend.setShape(rect);
//			
			
			// StandardXYItemRenderer rr = (StandardXYItemRenderer)rend;
			// rr.setPlotLines(true);

			//XYItemRenderer rend = chart.getXYPlot().getRenderer();
//			StandardXYItemRenderer rr = (StandardXYItemRenderer)rend;
//			rr.setPlotImages(true);
//			rr.setShape(rect, true);
	}

	

	public void AddFunctionToChart(ArrayList<Point2D> points, String Title ){
//		ArrayList<Point2D> points=function.getDataForPloting();
	
		
			dataset.addSeries(addPointsToSeries(Title,points));
			
		//	 points=function.getLocalMaxMinDataForPlot();
		//	dataset.addSeries(addPointsToSeries("after the drivetiv ",points));
		
	}
	
	
	
	
	public void finishChart(String Title,String  xAxisname,String yAxisname){
		xAxisName=xAxisname;
		yAxisName=yAxisname;
		this.setTitle(Title);
		
		
		
		// XYDataset xyDataset = new XYSeriesCollection(series);
		chart = ChartFactory.createXYLineChart(graphName, // Title
				xAxisname, // X-Axis label
				yAxisname, // Y-Axis label
				dataset, PlotOrientation.VERTICAL, // Dataset
				true, 
				true,
				true // Show legend
				);

	
	        XYPlot plot = (XYPlot) chart.getPlot();
	       
	    
//	        
//	        
//	        plot.setBackgroundPaint(Color.lightGray);
//	        plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
//	        plot.setDomainGridlinePaint(Color.white);
//	        plot.setRangeGridlinePaint(Color.white);
	        
		
 
		XYItemRenderer r =plot.getRenderer();
		   
        if (r instanceof XYLineAndShapeRenderer) {
        	//r.setBaseSeriesVisible(true);
            XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) r;
            
            renderer.setBaseShapesVisible(true);
            renderer.setBaseShapesFilled(true);
            renderer.setBaseShapesVisible(true);
            renderer.setBaseShapesFilled(true);
            renderer.setShapesFilled(true);
            
       //     renderer.setDrawShapes(true);
            renderer.setSeriesStroke(
            0,
            new BasicStroke(
            2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
            1.0f, new float[] {10.0f, 6.0f}, 4.0f
            )
            );
            
            
//            renderer.setSeriesStroke(
//            1,
//            new BasicStroke(
//            2.0f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_BEVEL,
//            1.0f, new float[] {6.0f, 6.0f}, 0.0f));
            renderer.setSeriesLinesVisible(2, false);
            Rectangle reg=new Rectangle(14,14);
            renderer.setSeriesShape(2,reg);
            //renderer.setSeriesFillPaint(2, Color.black);
            renderer.setSeriesShapesVisible(2, true);
//            renderer.setSeriesStroke(
//    	            2,
//    	            new BasicStroke(
//    	            2.0f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND,
//    	            1.0f, new float[] {6.0f, 16.0f}, 4.0f));
  
            
         //   renderer.setSeriesVisible(true);
//            logger.info(" In the render   "+" ("
//					+ this.getClass().getSimpleName() + "    "
//					+ (new Throwable()).getStackTrace()[0].getLineNumber()
//					+ "  )  ");
            
            
     
    	         //   renderer.setSeriesVisible(true);
    	            
        }
	        
//	      change the auto tick unit selection to integer units only...
//	        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
//	        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
//	        
	        
	        
				panel1 = new ChartPanel(chart);
	        
	        		// panel.addFocusListener(this);
	        		this.getContentPane().add(panel1);
	        
//			XYItemRenderer rend = chart.getXYPlot().getRenderer();
//			Rectangle rect = new Rectangle();
//			// rect.setBounds(0, 0, 2, 2);
//			rect.setSize(2, 2);
//			rend.setSeriesShape(dataset.getSeriesCount() - 1, rect);
//			rend.setShape(rect);
			// StandardXYItemRenderer rr = (StandardXYItemRenderer)rend;
			// rr.setPlotLines(true);

			//XYItemRenderer rend = chart.getXYPlot().getRenderer();
//			StandardXYItemRenderer rr = (StandardXYItemRenderer)rend;
//			rr.setPlotImages(true);
//			rr.setShape(rect, true);
	       
	}
	public void showChart() {
		panel1.setChart(chart);
	}

	/**
	 * @return  the graphName
	 * @uml.property  name="graphName"
	 */
	public String getGraphName() {
		return graphName;
	}

	/**
	 * @param graphName  the graphName to set
	 * @uml.property  name="graphName"
	 */
	public void setGraphName(String graphName) {
		this.graphName = graphName;
	}

	/**
	 * @return  the xAxisName
	 * @uml.property  name="xAxisName"
	 */
	public String getXAxisName() {
		return xAxisName;
	}

	/**
	 * @param xAxisName  the xAxisName to set
	 * @uml.property  name="xAxisName"
	 */
	public void setXAxisName(String axisName) {
		xAxisName = axisName;
	}

	/**
	 * @return  the yAxisName
	 * @uml.property  name="yAxisName"
	 */
	public String getYAxisName() {
		return yAxisName;
	}

	/**
	 * @param yAxisName  the yAxisName to set
	 * @uml.property  name="yAxisName"
	 */
	public void setYAxisName(String axisName) {
		yAxisName = axisName;
	}

	@Override
	public void paint(Graphics g) {

		super.paint(g);

		// Rectangle2D rect;
		//	 
		// rect = new Rectangle(panel.getVisibleRect());
		//
		// chart.draw((Graphics2D)g, rect);
		// g.drawImage(chart.(this.getWidth()-10,this.getHeight()-10),10,10,Color.WHITE,this);
		// super.paint(g);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Container#paintComponents(java.awt.Graphics)
	 */
	@Override
	public void paintComponents(Graphics g) {
		  
		super.paintComponents(g);

		// Rectangle2D rect = new Rectangle(panel.getSize());
		//
		// chart.draw((Graphics2D)g, rect);
	}

	public void focusGained(FocusEvent arg0) {
		// this.repaint();
		  

	}

	public void focusLost(FocusEvent arg0) {
		  
		// this.repaint();
	}

}
