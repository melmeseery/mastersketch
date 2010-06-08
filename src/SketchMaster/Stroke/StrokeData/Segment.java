package SketchMaster.Stroke.StrokeData;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.Serializable;
import java.util.ArrayList;

import SketchMaster.Stroke.graphics.shapes.GeometricPrimitive;
import SketchMaster.Stroke.graphics.shapes.GuiShape;

public class Segment extends SimpleInkObject implements Serializable {
	public Segment() {
		super();
		
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 8230908701712079078L;
	GeometricPrimitive SegmentType;
	private PointData StartPoint = null;

	private PointData EndPoint = null;

	// public Stroke getStroke(){
	//	
	// return stroke;
	// }
	//
	// public void setStroke(Stroke stroke){
	// this.stroke= stroke;
	// }

	public Segment(InkInterface object) {
		this.setPoints(object.getPoints());

	}

	public void setParam(ArrayList Param) {
		// 

	}

	// private Stroke stroke; //orginating stroke

	public void paint(Graphics2D g) {
		// 
		this.getSegmentType().paint(g);
	}

	public void setEndPoint(PointData endPoint) {
		EndPoint = endPoint;
	}

	public void setSegmentType(GeometricPrimitive segmentType) {
		SegmentType = segmentType;
	}

	public void setStartPoint(PointData startPoint) {
		StartPoint = startPoint;
	}

	@Override
	public InkInterface createSubInkObject(int start, int end) {
		Segment ink = new Segment();

		ArrayList<PointData> po = new ArrayList<PointData>();
		if (this.points != null) {
			for (int i = start; (i < this.points.size()) && (i < end); i++) {
				po.add(this.points.get(i));
			}

		}
		ink.setSegmentType(this.SegmentType);
		ink.setPoints(po);
		// return a segment from this stroke that will contain the points of the
		// stroke.
		return ink;
		// return null;
	}

	public GeometricPrimitive getSegmentType() {
		return SegmentType;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
	
		return "Segment  "+this.SegmentType.getClass().getSimpleName();
	}

}
