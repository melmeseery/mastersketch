package chartTest;
//import jahuwaldt.plot.Plot2D;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import SketchMaster.Stroke.StrokeData.Stroke;
import SketchMaster.Stroke.features.FeatureFunction;
import SketchMaster.Stroke.features.StrokeFeatures;
import SketchMaster.gui.DrawingSheet;
import SketchMaster.gui.Events.HandleStroke;
import SketchMaster.gui.Events.NewStrokeEvent;
import SketchMaster.system.Recogniziers.SVMRecognizier;

import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import javax.swing.JButton;
import java.awt.Dimension;

public class LocalMaxFrame extends JFrame implements HandleStroke {

	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null;

	private DrawingSheet MydrawingSheet = null;
	
	private SVMRecognizier reg;

	private JButton jButton = null;

	/**
	 * This is the default constructor
	 */
	public LocalMaxFrame() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(528, 500);
		this.setContentPane(getJContentPane());
		this.setTitle("Thhe test For local MAx");
		initReg();
	}

	private void initReg() {
		  
		this.reg=new SVMRecognizier();
	
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.add(getMydrawingSheet(), null);
			jContentPane.add(getJButton(), null);  // Generated
		}
		return jContentPane;
	}

	/**
	 * This method initializes MydrawingSheet	
	 * 	
	 * @return SketchMaster.gui.DrawingSheet	
	 */
	private DrawingSheet getMydrawingSheet() {
		if (MydrawingSheet == null) {
			MydrawingSheet = new DrawingSheet();
			MydrawingSheet.setBounds(new Rectangle(9, 24, 352, 434));
			MydrawingSheet.addstrokeListener(this);
		}
		return MydrawingSheet;
	}

	public void HandleNewStroke(NewStrokeEvent Evt) {
		  System.out.println(  " Handline stroke  "+" (" + this.getClass().getSimpleName()
					+ "    " + (new Throwable()).getStackTrace()[0].getLineNumber()
					+ "  )  ");
		Stroke stroke = Evt.getEventStroke();//.InterpolatePoints()
		stroke.getStatisticalInfo().generateAllDominatePoints();
		DisplayMergedFramePacking(stroke);
//		for (int i = 0; i <  funcs.size(); i++) {
////			displayFrame(funcs.get(i));
////			displayFrame2(funcs.get(i));
//		}
		
		
		
	}
	
   public void DisplayMergedFramePacking(Stroke stroke){
	   
	 
	
		System.out.println("  number of points in the stroke is "+stroke.getPointsCount()+" (" + this.getClass().getSimpleName()
				+ "    " + (new Throwable()).getStackTrace()[0].getLineNumber()
				+ "  )  ");
		
		ArrayList<FeatureFunction> funcs = stroke.getStatisticalInfo().getFunctions();
		stroke.getStatisticalInfo().setThresholds();
		DisplayMergedFrames(funcs);
	   
	   
   }
	
	
	/*
	 * 
	 * 
	 * 		//set scatter points from the graph
				//first create a arry list of vector 
				ArrayList temp=new ArrayList();
				int index=0;
				for (int i = 0; i < Evt.getEventStroke().getData().getCriticalPoints().size(); i++) {
					index= (int)((Point)Evt.getEventStroke().getData().getCriticalPoints().get(i)).getX();
					temp.add(Evt.getEventStroke().getData().getSpeedHistogram().get(index));
				}
				//add vector 
				
				
				
				
				frame.addChartFunction("critical ",1,temp);
				//frame.addChartFunction("stroke ",0,Evt.getEventStroke().getData().getSpeedHistogram());
				
				
				
				frame.repaint();
	 * 
	 * ***///
	
	
	public void displayFrame(FeatureFunction temp){
		GraphFrame frame=new GraphFrame();;
		
		frame.init();
		frame.DisplayFunctionProcessing(temp);

		frame.setVisible(true);
		frame.repaint();
		
	}
	public void displayFrame2(FeatureFunction temp){
		GraphFrame frame=new GraphFrame();;
		
		frame.init();
		frame.addChartFunction(1,temp);

		frame.setVisible(true);
		frame.repaint();
		
	}
	public void DisplayMergedFrames(	ArrayList<FeatureFunction>  funcs){
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
				    
				  
				   sizebefore=points.size();
				   
				frame.AddFunctionToChart(points, temp.getName()+" Information.(" + sizebefore+"points)");
				ArrayList<Point2D> ps=new ArrayList<Point2D>();
			   Point2D point;
			   point=new Point2D.Double();
			   
			  
//			   System.out.println(points+ "   size  = "+" (" + this.getClass().getSimpleName()
//					+ "    "
//					+ (new Throwable()).getStackTrace()[0].getLineNumber()
//					+ "  )  ");
//		//   point.setLocation(points.get(0));
//			   System.out.println("   temp "+temp+" (" + this.getClass().getSimpleName()
//					+ "    "
//					+ (new Throwable()).getStackTrace()[0].getLineNumber()
//					+ "  )  ");
			   point.setLocation(points.get(0).getX(), temp.getDataThreshold());

			   ps.add(point);
				
			   point=new Point2D.Double();
				//   point.setLocation(points.get(0));
					   point.setLocation(points.get(points.size()-1).getX(), temp.getDataThreshold());
					   
					   ps.add(point);
					   
					   
					   
						frame.AddFunctionToChart(ps,"  Threshold  ");
						
						ArrayList<Point2D> points2 = temp.localMaxMinDataForPlot();
						sizeAfter=points2.size();
						
				frame.AddFunctionToChart(points2, temp.getName()+"   "+sizeAfter+" Possible Dominate points");
						
						
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
						
	
//				   System.out.println("Function  "+temp.getName()+"  the count of data  "+points.size()+"  aftermin max  "+points2.size()+"  thershold  "+ temp.getDataThreshold() +" (" + this.getClass().getSimpleName()
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
	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton() {
		if (jButton == null) {
			jButton = new JButton();
			jButton.setBounds(new Rectangle(377, 28, 124, 43));  // Generated
			jButton.setText("Finish symbol");  // Generated
		}
		return jButton;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				LocalMaxFrame thisClass = new LocalMaxFrame ();
				thisClass.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				thisClass.setVisible(true);
			}
		});
	}	/**
	 * @param args
	 */


}  //  @jve:decl-index=0:visual-constraint="10,10"
