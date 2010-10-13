package SketchMaster.gui;

import org.apache.log4j.Logger;

import SketchMaster.Stroke.StrokeData.PointData;
import SketchMaster.Stroke.StrokeData.Stroke;
import SketchMaster.Stroke.graphics.layers.SketchLayer;
import SketchMaster.gui.Events.HandleFinishCluster;
import SketchMaster.gui.Events.HandleStroke;
import SketchMaster.gui.Events.NewClusterEvent;
import SketchMaster.gui.Events.NewStrokeEvent;
import SketchMaster.io.log.FileLog;
import SketchMaster.system.SystemSettings;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.EventListenerList;
import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;


/**
 * @author Mahi
 */
public class DrawingSheet extends JScrollPane {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(DrawingSheet.class);

	GraphWatched watched = new GraphWatched();
	// -----------------------------------parameters
	// private SketchLayer currentLayer;
	private Stroke currentStroke = null;

	private boolean flagStrokeOn = false;
	
	private boolean flagClusterStart=false;
	private int DragStrokeCount=0;

	private long currentStrokeTime = 0;

	private long currentStrokeStartTime = 0;

	private long PrevcurrentStrokeTime = 0;
	
	private boolean NoNotify=false;
	private boolean PaintToImage=false;
	private boolean Moving=false;

	protected int oldx,oldy;
	protected int x=0,y=0; //location of cursor.. 
	private JPanel panel = new JPanel();

	/**
	 * @directed
	 */
	public DrawingSheet() throws HeadlessException {
		super();
		try {
			jbInit();
			// logger.info("finish the drawing ");
		} catch (Exception ex) {
			logger.error("DrawingSheet()", ex); //$NON-NLS-1$
		}
	}

	// public DrawingSheet(GraphicsConfiguration gc) {
	// super(gc);
	// }
	//
	// public DrawingSheet(String title) throws HeadlessException {
	// super(title);
	// }
	//
	// public DrawingSheet(String title, GraphicsConfiguration gc) {
	// super(title, gc);
	// }

	/**
	 * @throws Exception
	 */
	private void jbInit() throws Exception {

		// panel.setSize(1000, 1000);
		// panel.setLocation(20, 20);
		//
		// // Get the default scrollbar policy
		// int hpolicy = this.getHorizontalScrollBarPolicy();
		// // JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED;
		//	        
		// int vpolicy = this.getVerticalScrollBarPolicy();
		// // JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED;

		// Make the scrollbars never appear
		this
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		this.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

		this.setLayout(null);
     //     this.setComponentPopupMenu(getJPopupMenuSave());
		MouseEvetLisnter me = new MouseEvetLisnter();

		this.addMouseListener(me);
		this.addMouseMotionListener(me);
		this.setBackground(Color.white);
		// this.setContentPane(panel);

		this.setLayout(null);
		// this.add(panel);

	}

	public void initSheetPanel(int x, int y, int width, int hight, Color color) {
		this.setSize(width, hight);
		this.setLocation(x, y);
		// drawSheet.setBounds(40,40,300,300);
		this.setBackground(color);
	}

	/**
	 * Repaint stroke
	 */
	public void repaintStroke() {
//		if (SystemSettings.DEBUG_MODE){
//			Color temp=getGraphics().getColor();
//			getGraphics().setColor(Color.LIGHT_GRAY);
//			
//			// now draw a   ---  e.getX(); from y=0 to y = size 
//			getGraphics().drawLine(x, 0,x, getSize().height);
//			getGraphics().drawLine(0, y,  getSize().width,y);
//			
//			getGraphics().drawString(x+" , " +y,x+2, y+3);
//			getGraphics().setColor(temp);
//	}
		currentStroke.drawStroke(this.getGraphics());

		// this.firePropertyChange()

	}

	protected EventListenerList listenerDrawingList = new EventListenerList();
	protected EventListenerList listenerClusterList = new EventListenerList();
	private JPopupMenu jPopupMenuSave = null;
	private JMenuItem jMenuItemSaveImage = null;
	private JMenuItem jMenuItemClear = null;

	public void addstrokeListener(HandleStroke l) {
		listenerDrawingList.add(HandleStroke.class, l);
	}

	public void removestrokeListener(HandleStroke l) {
		listenerDrawingList.remove(HandleStroke.class, l);
	}
	
	
	public void addClusterListener(HandleFinishCluster l) {
		listenerClusterList.add(HandleFinishCluster.class,l);
	}

	public void removestrokeListener(HandleFinishCluster l) {
		listenerDrawingList.remove(HandleFinishCluster.class, l);
	}

	public void fireStrokeEvents(Stroke temp){
		currentStroke=temp;
		fireStrokeEvent();
	}
	private void fireFinishEvent(){
		if (flagClusterStart){
		
		
		Object[] listeners = listenerClusterList.getListenerList();

		NewClusterEvent strokeEvent = null;

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == HandleFinishCluster.class) {
				// Lazily create the event:
				if (strokeEvent == null)
					strokeEvent = new NewClusterEvent(this);
				((HandleFinishCluster) listeners[i + 1]). HandleFinishedCluster(strokeEvent);
			}
		}
		flagClusterStart=false;
		}
		
	}
	private void fireStrokeEvent() {
//		//  logger.trace("********************FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFf****************88");
//		//  logger.trace(" fir a new stroke   ");
		Object[] listeners = listenerDrawingList.getListenerList();

		NewStrokeEvent strokeEvent = null;

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == HandleStroke.class) {
				// Lazily create the event:
				if (strokeEvent == null)
					strokeEvent = new NewStrokeEvent(this, currentStroke);
				((HandleStroke) listeners[i + 1])
						.HandleNewStroke(strokeEvent);
			}
		}
	}

	// /---------------------------------Mouse Listener and
	// events-------------------------------------
	// the frame which any thing can be drawn
	/**
	 * @author Mahi Mouse lisnter
	 */
	private class MouseEvetLisnter implements MouseListener,
			MouseMotionListener {
		

		MouseEvetLisnter() {
		}
  
	

		public void mouseClicked(MouseEvent e) {
			NoNotify=false;
			
			if (e.getButton() == MouseEvent.BUTTON1) { 

				; //the first button (left?) 

				} else if (e.getButton() == MouseEvent.BUTTON2) { 

				; //the second button (center?) 

				} else if (e.getButton() == MouseEvent.BUTTON3) { 

			        //  logger.info(" in the right buton click .....");
					fireFinishEvent();
				} 
			
			
		}

		public void mouseEntered(MouseEvent e) {

		}

		public void mouseExited(MouseEvent e) {

		}

		public void mousePressed(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON1) { 
          
			NoNotify=false;
			flagStrokeOn = true;
			flagClusterStart=true;
			 DragStrokeCount=0;
			currentStroke = new Stroke();
			PointData pd = new PointData();
			pd.setPointLocation(e.getPoint());
			pd.setTime(System.currentTimeMillis());
			// currentStrokeStartTime=pd.getTime();
			currentStroke.addPoint(pd);
			currentStroke.setStartPoint(pd);
			// updateLables();

			// saySomething("Mouse pressed; # of clicks: "
			// + e.getClickCount(), e);
			}else if (e.getButton() == MouseEvent.BUTTON3) { 

		        // logger.info(" in the right buton click ..... and no strooookeeeeee" );
		         //no 
				fireFinishEvent();
			} 
		}

		public void mouseReleased(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON1) { 
			flagStrokeOn = false;

			PointData pd = new PointData();
			pd.setPointLocation(e.getPoint());

			pd.setTime(System.currentTimeMillis());
			currentStroke.addPoint(pd);
			currentStroke.setEndPoint(pd);
			// logger.info("releasing ");
			// logger.info("start point
			// "+currentStroke.getStartPoint().toString()+" end point =
			// "+pd.toString()+" The stroke time at release = " +
			// currentStroke.getStrokeTime());

			// PrevcurrentStrokeTime=currentStroke.getStrokeTime();

			// may create a new stroke
			//
			if ( DragStrokeCount > SystemSettings.MIN_STROKE_PIXEL ){
			fireStrokeEvent();
			}
			repaintStroke();
			// repaint();
			}

		}

		void saySomething(String eventDescription, MouseEvent e) {
			//  logger.trace(eventDescription + " detected on "	+ e.getComponent().getClass().getName() + ".");

		}

		public void mouseDragged(MouseEvent e) {

			if (flagStrokeOn) {
				PointData pd = new PointData();
				pd.setTime(System.currentTimeMillis());
				pd.setPointLocation(e.getPoint());
				currentStroke.addPoint(pd);
				 DragStrokeCount++;
				
				x=e.getX();
				y=e.getY();
				Moving=false;
				// saySomething("this is drag x= "+e.getX()+" y = "+e.getY(),e);
				repaintStroke();
//				oldx=x;
//				oldy=y;
				// updateLables();
			}

		}

		public void mouseMoved(MouseEvent e) {
        
			
			if (SystemSettings.DEBUG_MODE){
		
				x=e.getX();
			    y=e.getY();
			    Moving=true;
// 
//	
//					
//					oldx=x;
//					oldy=y;
		 
					
					repaint();
			}
			// saySomething("this is moving x= "+e.getX()+" y = "+e.getY(),e);
		}
	}

	@Override
	public void paint(Graphics g) {
		// logger.info("PAAAAAAAAAAAAAAAAAAAA");
		super.paint(g);
		if (SystemSettings.DEBUG_MODE && Moving){
			
			Color temp=getGraphics().getColor();
			
			g.setColor(Color.BLUE);
			// now draw a   ---  e.getX(); from y=0 to y = size 
			g.drawLine(x, 0,x, getSize().height);
			g.drawLine(0, y,  getSize().width,y);
			
			g.drawString(x+" , " +y,x+2, y+3);
			g.setColor(temp);
 
//					
			}
		
		if (!NoNotify){
		watched.setG((Graphics2D) g);

		// watched.notifyObservers();

		watched.notifyObservers((Graphics2D) g);
	
		}
		

		
		if (PaintToImage){
			// save to an saved image 
			PaintToImage=false;
		}
		

	}

	public void addDataDisplay(Observer o) {
		watched.addObserver(o);
		// watched.counter(10);
	}

	public void removeDataDisplay(Observer arg0) {
		watched.deleteObserver(arg0);
	}

	/**
	 * This method initializes jPopupMenuSave	
	 * 	
	 * @return javax.swing.JPopupMenu	
	 */
	private JPopupMenu getJPopupMenuSave() {
		if (jPopupMenuSave == null) {
			jPopupMenuSave = new JPopupMenu();
			jPopupMenuSave.add(getJMenuItemSaveImage());  // Generated
			jPopupMenuSave.add(getJMenuItemClear());  // Generated
		}
		return jPopupMenuSave;
	}

	/**
	 * This method initializes jMenuItemSaveImage	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getJMenuItemSaveImage() {
		if (jMenuItemSaveImage == null) {
			jMenuItemSaveImage = new JMenuItem();
			jMenuItemSaveImage.setText("Save image");
			jMenuItemSaveImage.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					//logger.info("actionPerformed()");  
					final JFileChooser fc = new JFileChooser(new File("."));
					// In response to a button click:
					if (fc.showSaveDialog(getParent()) == fc.APPROVE_OPTION) { // .showOpenDialog(this);
						// logger.info("actionPerformed open ()");

						// now i need to draw to the file i want to save 
						PaintToImage=true;
					}

					// logger.info("actionPerformed()");

				
					
				}
			});
		}
		return jMenuItemSaveImage;
	}

	/**
	 * This method initializes jMenuItemClear	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getJMenuItemClear() {
		if (jMenuItemClear == null) {
			jMenuItemClear = new JMenuItem();
			jMenuItemClear.setText("Clear");  // Generated
			jMenuItemClear.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					//logger.info("actionPerformed()  clear 339 drawing sheet");  
					
					// i need to clear every thing in the graph and start all coounters 
					 currentStroke = null;

					flagStrokeOn = false;
					 DragStrokeCount=0;

				currentStrokeTime = 0;
				currentStrokeStartTime = 0;

					PrevcurrentStrokeTime = 0;
					NoNotify=true;
					repaint();
				}
			});
		}
		return jMenuItemClear;
	}

}
