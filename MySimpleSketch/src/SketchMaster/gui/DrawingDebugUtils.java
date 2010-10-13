/**
 * 
 */
package SketchMaster.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import javax.swing.JFrame;
import SketchMaster.Stroke.StrokeData.PointData;
import SketchMaster.Stroke.StrokeData.Stroke;
import SketchMaster.Stroke.features.FeatureFunction;
import SketchMaster.Stroke.features.StrokeFeatures;
import SketchMaster.Stroke.graphics.shapes.Line;
import SketchMaster.gui.*;

/**
 * @author melmeseery
 *
 */
public class DrawingDebugUtils {

	
//	if (DrawingDebugUtils.DEBUG_GRAPHICALLY){
//		colorValue+=0.1;
//		 SolutionColor=Color.getHSBColor(0.4f, (float)colorValue, 0.5f);
//		// DrawingDebugUtils.getGraphics().setColor(SolutionColor);
	// paint(	DrawingDebugUtils.getGraphics() );
//		 DrawingDebugUtils.drawThickPointPath( 	DrawingDebugUtils.getGraphics()  , SolutionColor, SolutionColor, polygonVertices);
//
//}
	public static final Color InkColor = Color.BLUE;
	public static final Color PointsColor = Color.RED;
	public static  int PointsSize = 3;
	public static final Color segmeColor = Color.BLACK;
    
	
	public static Graphics2D G_DEBUG=null;
	private static stickFrame DebugFrame=null;
	public static boolean DEBUG_GRAPHICALLY=true; 
	public static boolean StateFrames=false;
	
	public static Graphics2D getGraphics(){
		if (G_DEBUG!=null)
			return G_DEBUG;
		else {
		         return (Graphics2D) getDebugFrame().getGraphics();
			
		}
		
	}
	
 
	public static JFrame getDebugFrame() {
			if (	DebugFrame ==null)
			{
		
				DebugFrame =		 (new DrawingDebugUtils()).new stickFrame(" Debug Frame ");
//				Font font =new Font("Serif", Font.ITALIC, 20);
//				DebugFrame.setFont(font);
			//	DebugFrame.setBackground(Color.white);
				DebugFrame.setBounds(650, 200,600, 600);

				//DebugFrame.setVisible(true);
				DebugFrame.setVisible(true);
				DebugFrame.repaint();
				
				
			}
			
		return DebugFrame;
	}

	public static void showFrame(){
		 getDebugFrame().setVisible(true);
	}
	public static void FrameDelay(){
		 
	}
	
	 public static void drawThickLine(double r, Graphics g, double x1, double y1, double x2, double y2)
	    {
		 g.setColor(InkColor);
	        if(r == 1.0D)
	        {
	            g.drawLine((int)x1, (int)y1, (int)x2, (int)y2);
	            return;
	        }
	        int iterations = (int)Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
	        for(int i = 0; i < iterations; i++)
	            g.fillOval((int)((x1 * (double)i + x2 * (double)(iterations - i)) / (double)iterations), (int)((y1 * (double)i + y2 * (double)(iterations - i)) / (double)iterations), (int)r, (int)r);
	    }
    public static void clearComponent(Component component)
    {
        Graphics g = component.getGraphics();
        g.setColor(Color.white);
        g.fillRect(0, 0, component.getWidth(), component.getHeight());
    }
    public static void drawLine( Graphics2D g,Color c, PointData p1,  PointData p2){
		Color temp=g.getColor();
    	g.setColor(c);
    	g.drawLine((int)p1.getX(), (int)p1.getY(),(int)p2.getX(), (int)p2.getY());
    	
    	g.setColor(temp);
    }
    
    public static void drawLine(Graphics2D g, Color c, Line l) {
    	Color temp=g.getColor();
    	g.setColor(c);
    	drawLine(g,c,l.getStartPoint(),l.getEndPoint());
    	
    	g.setColor(temp);
	}
    public static void drawLine( Graphics2D g,Color c, Point2D p1,  Point2D p2){
    	g.setColor(c);
    	g.drawLine((int)p1.getX(), (int)p1.getY(),(int)p2.getX(), (int)p2.getY());
    	
    }
    public static void drawLine( Graphics2D g, Point2D p1,  Point2D p2){
    	g.setColor(InkColor);
    	g.drawLine((int)p1.getX(), (int)p1.getY(),(int)p2.getX(), (int)p2.getY());
    	
    }
    public static void drawPoint(Graphics2D g, Color c, Point2D p1){
    	g.setColor(c);
    	//g.drawLine((int)p1.x, (int)p1.y,(int)(p1.x+1), (int)(p1.y+1));
    	 g.fillOval((int)p1.getX(), (int)p1.getY(),PointsSize,PointsSize);
    }
	public static void drawPoint(Graphics2D g, double cx, double cy) {
	 
		g.setColor(PointsColor);
  
    	 g.fillOval((int)cx, (int)cy,PointsSize,PointsSize);
	}
    public static void drawPoint(Graphics2D g, Point2D p1){
    	g.setColor(PointsColor);
    	//g.drawLine((int)p1.x, (int)p1.y,(int)(p1.x+1), (int)(p1.y+1));
    	 g.fillOval((int)p1.getX(), (int)p1.getY(),PointsSize,PointsSize);
    }
    public static void drawPointPath(Graphics2D g, Color linColor, Color pointColor , ArrayList polygonVertices){
    	 Point2D p1,p2 ;
    		Color temp=g.getColor();
        	
    	for (int i = 0; i < polygonVertices.size()-1; i++) {
			 p1 = (Point2D) polygonVertices.get(i);
			 p2=(Point2D) polygonVertices.get(i+1);
			 g.setColor(linColor);
    		g.drawLine((int)p1.getX(), (int)p1.getY(),(int)p2.getX(), (int)p2.getY());
    		g.setColor(pointColor);
    		g.fillOval((int)p1.getX(), (int)p1.getY(), PointsSize,PointsSize);
    		g.fillOval((int)p2.getX(), (int)p2.getY(), PointsSize,PointsSize);
    
		}
    	
		g.setColor(temp);
		
    }
    
    
    public static void drawThickPointPath(Graphics2D g, Color linColor, Color pointColor , ArrayList polygonVertices){
   	 Point2D p1,p2 ;
		Color temp=g.getColor();
		
		java.awt.Stroke s = g.getStroke();
		java.awt.BasicStroke bs=new BasicStroke(3);
		g.setStroke(bs);
	for (int i = 0; i < polygonVertices.size()-1; i++) {
		 p1 = (Point2D) polygonVertices.get(i);
		 p2=(Point2D) polygonVertices.get(i+1);
		 g.setColor(linColor);
			g.drawLine((int)p1.getX(), (int)p1.getY(),(int)p2.getX(), (int)p2.getY());
    		g.setColor(pointColor);
    		g.fillOval((int)p1.getX(), (int)p1.getY(), PointsSize*2,PointsSize*2);
    		g.fillOval((int)p2.getX(), (int)p2.getY(), PointsSize*2,PointsSize*2);
    		
	

		
	}
	g.setStroke(s);
	g.setColor(temp);
    	
    }
  
    

    public static void DisplayChartsFrames(Stroke stroke){
 	   
   	 
//    	
//		logger.info("  number of points in the stroke is "+stroke.getPointsCount()+" (" + this.getClass().getSimpleName()
//				+ "    " + (new Throwable()).getStackTrace()[0].getLineNumber()
//				+ "  )  ");
		
		ArrayList<FeatureFunction> funcs = stroke.getStatisticalInfo().getFunctions();
		stroke.getStatisticalInfo().setThresholds();
		if (StateFrames){
		DisplayMergedFrames(funcs,stroke);
		}
	   
   }
    public static void DrawGridOnGraph(Graphics2D g, Dimension size){
    	
    	Dimension dim =size;
		//now save the drawing color
		Color c=g.getColor();
		
		g.setColor(Color.LIGHT_GRAY);
		for (int i = 0; i < dim.width; i+=25) {
			g.drawLine(i, 0, i, dim.height);


			if (i%2==0){	
				
				g.setColor(Color.black);
						g.fillRect(i, 0, 2, 10);
				g.setColor(Color.LIGHT_GRAY);
			g.drawString(""+i, i+5, 12);
			}
			
		}
		for (int i = 0; i < dim.height; i+=25) {
			
			g.drawLine(0, i, dim.width, i);
			if (i%2==0){
				
				g.setColor(Color.black);
				g.fillRect(0, i, 10, 2);
		g.setColor(Color.LIGHT_GRAY);
				g.drawString(""+i, 2, i-5);
				}
		}
		
		
		g.setColor(c);
    	
    }
	private static  void DisplayMergedFrames(	ArrayList<FeatureFunction>  funcs, Stroke stroke){
		int MaxFunction=1;
		
		 //ChartGraph  graph=new ChartGraph();
		for (int i = 0; i < funcs.size(); i+=MaxFunction) {
			GraphFrame frame=new GraphFrame();
			frame.init();
		
			//FeatureFunction temp = funcs.get(i);
			//Plot2D aPlot =graph.InitPlot(temp.getDataForPloting(),temp.getName(), temp.getXName(), temp.getYName());
			//graph.addFunctionToPlot(aPlot,temp.getLocalMaxMinDataForPlot());
			for (int j = 0; (j < MaxFunction)&& ((j+i) < funcs.size()); j++) {
				// temp = funcs.get(i+j);
				

				
			//	graph.addFunctionToPlot(aPlot,temp.getDataForPloting());
				//graph.addFunctionToPlot(aPlot,temp.getLocalMaxMinDataForPlot());
				
			 	//"Test SimpleXY Plot", "X Axis", "Y Axis",
				
				
				FeatureFunction temp = funcs.get(i+j);
				ArrayList<Point2D> points = temp.getDataForPloting();
				
				 
				   int sizebefore,sizeAfter;
				    
				  if (points==null)
					  return ;
				   sizebefore=points.size();
				   
				frame.AddFunctionToChart(points, temp.getName()+" Main Data  " + sizebefore+"  points of the actual size of points = "+stroke.getPointsCount());
				ArrayList<Point2D> ps=new ArrayList<Point2D>();
			   Point2D point;
			   point=new Point2D.Double();
			   
			  
//			   logger.info(points+ "   size  = "+" (" + this.getClass().getSimpleName()
//					+ "    "
//					+ (new Throwable()).getStackTrace()[0].getLineNumber()
//					+ "  )  ");
//		//   point.setLocation(points.get(0));
//			   logger.info("   temp "+temp+" (" + this.getClass().getSimpleName()
//					+ "    "
//					+ (new Throwable()).getStackTrace()[0].getLineNumber()
//					+ "  )  ");
			   point.setLocation(points.get(0).getX(), temp.getDataThreshold());

			   ps.add(point);
				
			   point=new Point2D.Double();
				//   point.setLocation(points.get(0));
					   point.setLocation(points.get(points.size()-1).getX(), temp.getDataThreshold());
					   
					   ps.add(point);
					   
					   
					   
						frame.AddFunctionToChart(ps, temp.getName()+"  Threshold  ");
						
						ArrayList<Point2D> points2 = temp.localMaxMinDataForPlot();
						sizeAfter=points2.size();
						
				frame.AddFunctionToChart(points2, temp.getName()+"  After Procesing  "+sizeAfter+"  points");
						
						
						if (temp.getFunc().getThresholdType()==StrokeFeatures.THERSHOLD_ABS_AVERAGE){
							 ps=new ArrayList<Point2D>();
								 point=new Point2D.Double();
								 point.setLocation(points.get(points.size()-1).getX(), -temp.getDataThreshold());
								
								  ps.add(point);
								  point=new Point2D.Double();
								  point.setLocation(points.get(0).getX(), -temp.getDataThreshold());
								  ps.add(point);
								
								frame.AddFunctionToChart(ps, temp.getName()+"  Threshold2  ");
						}
						
	
//				   logger.info("Function  "+temp.getName()+"  the count of data  "+points.size()+"  aftermin max  "+points2.size()+"  thershold  "+ temp.getDataThreshold() +" (" + this.getClass().getSimpleName()
//							+ "    "
//							+ (new Throwable()).getStackTrace()[0].getLineNumber()
//							+ "  )  ");
			}
			frame.finishChart("Functions"," x "," y ");
			
			//frame.addChartFunction(1,temp);

			frame.setVisible(true);
			frame.repaint();
		//	graph.DisplayPlot(aPlot);
			
		}
	}
    
	class  stickFrame extends JFrame{

		@Override
		public void paint(Graphics g) {
			
			//super.paint(g); removed to make the image stick on the frame and do not have to redraw. 
			DrawingDebugUtils.DrawGridOnGraph((Graphics2D)g,this.getSize());
		}

		/**
		 * 
		 */
		private static final long serialVersionUID = -7689205820551325844L;

		public stickFrame(String string) {
			super(string);
		}
    	
    }

	





}
