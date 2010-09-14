/**
 * 
 */
package SketchMaster.Stroke.graphics.layers;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import SketchMaster.Stroke.StrokeData.Stroke;
import SketchMaster.Stroke.graphics.shapes.GuiShape;

// import SketchMaster.Stroke.graphics.shapes.StrokeFitObject;

/**
 * @author Mahi
 */
public class SketchStrokesLayer extends SketchLayer {
	/**
	 * 
	 */
	public SketchStrokesLayer() {
		super();

	}

	/**
	 * @return the strokes
	 * @uml.property name="strokes"
	 */
	public ArrayList getStrokes() {
		return Strokes;
	}

	public void addStroke(SketchMaster.Stroke.StrokeData.Stroke stroke) {
		Strokes.add(stroke);
	}

	private void clearAllStroke() {
		for (int i = 0; i < Strokes.size(); i++) {
			((Stroke) (Strokes.get(i))).clearAllPoints();
		}
		Strokes.clear();
	}

	/**
	 * @param strokes
	 *            the strokes to set
	 * @uml.property name="strokes"
	 */
	public void setStrokes(ArrayList Strokes) {
		this.Strokes = Strokes;
	}

	public void drawStrokes(Graphics2D g) {
		// logger.info("i am on the draw");

		int max = Strokes.size();

		for (int i = 0; i < Strokes.size(); i++) {
			// logger.info("stroke "+ i);
			// g.setColor();
			Color c = Color.getHSBColor((float) i / (float) max, (float) 0.7,
					(float) 0.5);
			if (Math.abs(i) % 2 == 1)
				((Stroke) (Strokes.get(i)))
						.drawStroke(g, Color.BLUE, Color.RED);
			else
				((Stroke) (Strokes.get(i)))
						.drawStroke(g, Color.red, Color.black);

		}
	}

	/** @link dependency */
	/* # Stroke lnkStroke; */

	/**
	 * @uml.property name="strokes"
	 */
	private ArrayList<GuiShape> Strokes = new ArrayList();

	@Override
	public void paint(Graphics2D g) {
		// this.drawStroke(g);
		for (int i = 0; i < Strokes.size(); i++) {
			Strokes.get(i).paint(g);
		}
	}

	@Override
	public void addObject(Object object) {
		if (object instanceof Stroke) {
			Stroke temp = (Stroke) object;
			Strokes.add(temp);

		}
	}

	@Override
	public void ClearAll() {
		clearAllStroke();
	}

}
