/**
 * 
 */
package SketchMaster.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

import SketchMaster.system.SystemSettings;

/**
 * @author Maha
 *
 */
public class DrawingSheetTesting  extends DrawingSheet{

	/**
	 * 
	 */
	public DrawingSheetTesting() {
		DrawingDebugUtils. DEBUG_GRAPHICALLY=true;
		
	}

	/* (non-Javadoc)
	 * @see SketchMaster.gui.DrawingSheet#paint(java.awt.Graphics)
	 */
	@Override
	public void paint(Graphics g) {
	
		super.paint(g);
		
		/// draw grid lines for 
		
		DrawingDebugUtils.DrawGridOnGraph((Graphics2D)g,this.getSize());
		if (SystemSettings.DEBUG_MODE){
			//		updateUI();
					Point e = getMousePosition();
//					if (e!=null)
//					{
//				//	logger.info(" in the paint........"+e);
//					// draw the axis from the mouse location ..
//					Color temp=getGraphics().getColor();
//					getGraphics().setColor(Color.LIGHT_GRAY);
//					
//					// now draw a   ---  e.getX(); from y=0 to y = size 
//					getGraphics().drawLine((int)e.getX(), 0,(int) e.getX(), getSize().height);
//					getGraphics().drawLine(0, (int)e.getY(),  getSize().width,(int) e.getY());
//					
//					getGraphics().drawString(e.getX()+" , " +e.getY(),(int)e.getX()+2, (int)e.getY()+3);
//					getGraphics().setColor(temp);
//					
//					}
					
				}
	}

}
