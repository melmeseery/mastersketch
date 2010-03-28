/**
 * 
 */
package SketchMaster.Stroke.graphics.layers;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import SketchMaster.Stroke.graphics.shapes.GuiShape;
import SketchMaster.gui.GraphWatched;

/**
 * @author Mahi
 * 
 */
public class SketchLayer implements Observer {
	String LayerName = "layer ";

	public void addObject(Object object) {
		if (object instanceof GuiShape) {
			GuiShape NewGuiShape = (GuiShape) object;
			GuiItems.add(NewGuiShape);
		}
	}

	/**
	 * 
	 */

	protected ArrayList<GuiShape> GuiItems = new ArrayList<GuiShape>();

	// public SketchLayer() {
	// super();

	// }
	public void paint(Graphics2D arg0) {
		for (int i = 0; i < GuiItems.size(); i++) {
			GuiItems.get(i).paint(arg0);
		}
	}

	public void ClearAll() {
		GuiItems.clear();
	};

	protected boolean Active = false; // if true then this layer will be
										// displayed

	public boolean isActive() {
		return Active;
	}

	public void setActive(boolean active) {
		Active = active;
	}

	public String getLayerName() {
		return LayerName;
	}

	public void setLayerName(String layerName) {
		LayerName = layerName;
	}

	public void update(Observable ob, Object arg1) {
		if (ob instanceof GraphWatched) {
			GraphWatched gr = (GraphWatched) ob;

			if (this.Active)

			{
				// /logger.info("iiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii");
				paint(gr.getG());
			}
			// else
			// logger.info("in update but not active");
		}
	}

}
