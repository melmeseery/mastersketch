package SketchMaster.Stroke;
import java.awt.*;
import java.util.*;

import SketchMaster.Stroke.StrokeData.Stroke;
import SketchMaster.Stroke.graphics.layers.SketchLayer;
import SketchMaster.Stroke.graphics.layers.SketchStrokesLayer;
import SketchMaster.system.SystemSettings;

/**
 * @author Mahi
 */
public class SketchLayerArray {
	public void paint(Graphics2D arg0) {
		for (int i = 0; i < layers.size(); i++) {

			layers.get(i).paint(arg0);

		}
	}

	public void clearAll() {
		for (int i = 0; i < layers.size(); i++) {
			layers.get(i).ClearAll();

		}
	}

	/**
	 * @uml.property name="layerSelected"
	 */
	private int layerSelected = 0;
	// SketchStrokesLayer strokes;

	// SketchSegmentionLayer polygonsLayer;
	/**
	 * @uml.property name="layers"
	 * @uml.associationEnd multiplicity="(0 -1)"
	 */
	ArrayList<SketchLayer> layers;

//{{ 
	public void addObjectToLayer(Object object, int layer) {
		layers.get(layer).addObject(object);
	}

	public SketchLayerArray() {
		layerSelected = 0;

		layers = new ArrayList<SketchLayer>();

	}

	public int getDomain() {
		return 0;
	}
//}}
	public void addLayer(SketchLayer strokes) {
		// strokes=new SketchStrokesLayer();
		layers.add(strokes);
	}

	
	
	//{{ public SketchStrokesLayer getStrokesLayer() {
	// return strokes;
	// }
	//
	//
	// // public void setStrokes(SketchStrokesLayer strokes) {
	// // this.strokes = strokes;
	// // }
	//
	//
	// public SketchSegmentionLayer getDividesLayer() {
	// return polygonsLayer;
	// }

	// public void setSymbols(SketchSegmentionLayer polygonsLayer) {
	// this.symbols = polygonsLayer;
	// }

	// public void clearAllStroke() {
	// strokes.clearAllStroke();
	//		
	//}} }

	/**
	 * @return the layerSelected
	 * @uml.property name="layerSelected"
	 */
	public int getLayerSelected() {
		return layerSelected;
	}

	/**
	 * @param layerSelected
	 *            the layerSelected to set
	 * @uml.property name="layerSelected"
	 */
	public void setLayerSelected(int layerSelected) {
		if (layerSelected >= layers.size())
			this.layerSelected = 0;
		
		
		
		
		else
			this.layerSelected = layerSelected;
	}

	public SketchLayer getLayerSelect() {
		SketchLayer returnSketchLayer = layers.get(layerSelected);
		return returnSketchLayer;
	}

	public SketchLayer getLayer(int layerSelected2) {
		if (layerSelected2 >= layers.size())
			layerSelected2 = 0;
		// logger.info("setting layer "+layerSelected2);

		SketchLayer returnSketchLayer = layers.get(layerSelected2);
		return returnSketchLayer;
	}

	public int getLayersCount() {
		// 
		int returnint = layers.size();
		return returnint;
	}

	public void addNewStroke(Stroke stroke) {
		// 
		// get the stroke layer with the tyep sketch layer
		for (int i = 0; i < layers.size(); i++) {

			if (layers.get(i) instanceof SketchStrokesLayer) {
				SketchStrokesLayer strokes = (SketchStrokesLayer) layers.get(i);
				if (strokes.getLayerName().equalsIgnoreCase(
						SystemSettings.StrokeLayerName)) {
					strokes.addObject(stroke);
				}
			}
		}
	}

}
