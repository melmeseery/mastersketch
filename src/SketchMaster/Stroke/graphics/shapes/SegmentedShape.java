/**
 * 
 */
package SketchMaster.Stroke.graphics.shapes;

import SketchMaster.Stroke.StrokeData.Segment;

/**
 * @author maha
 * 
 */
public interface SegmentedShape extends GuiShape {

	public int getSegmentsCount();

	public Segment getSegmentOfIndex(int i);
}
