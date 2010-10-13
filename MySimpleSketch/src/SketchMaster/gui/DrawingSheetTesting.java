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

				}
	}

}
