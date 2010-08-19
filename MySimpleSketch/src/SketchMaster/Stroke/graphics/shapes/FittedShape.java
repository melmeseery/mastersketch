/**
 * 
 */
package SketchMaster.Stroke.graphics.shapes;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import SketchMaster.Stroke.StrokeData.DominatePointStructure;
import SketchMaster.Stroke.StrokeData.InkInterface;
import SketchMaster.Stroke.StrokeData.InkPart;
import SketchMaster.Stroke.StrokeData.PointData;
import SketchMaster.Stroke.StrokeData.Segment;
import SketchMaster.Stroke.StrokeData.Stroke;
import SketchMaster.lib.ComputationsGeometry;
import SketchMaster.lib.CurveFitData;
import SketchMaster.system.SystemSettings;
import SketchMaster.system.segmentation.SketchSegmentors;

/**
 * @author TOSHIBA
 *
 */
public class FittedShape implements GuiShape,SegmentedShape{


	private static final Logger logger = Logger.getLogger(FittedShape.class);
	GeometricPrimitive  shape;
	double Error;
	boolean accepted;
	int type;
	
	private InkInterface Originalink;

	Segment seg=null;
	
	
	public final static int TYPE_LINE=0;  
	public final static int TYPE_CIRCLE=1; 
	public final static int TYPE_ELLIPSE=2;  
	public final static int TYPE_POLYLINE=3;
	public final static int TYPE_HELIX=4;
	public final static int TYPE_SPRIAL=5;
	public final static int TYPE_ARC=6;
	
//	public	double slope;
//	public double  intercept;
	
	
	public void paint(Graphics2D g) {
		shape.paint(g);
		
	}


	public GeometricPrimitive getShape() {
		return shape;
	}


	public void setShape(GeometricPrimitive shape) {
		this.shape = shape;
	}


	public double getError() {
		return Error;
	}


	public void setError(double error) {
		Error = error;
	}


	public boolean isAccepted() {
		return accepted;
	}


	public void setAccepted(boolean accepted) {
		this.accepted = accepted;
	}


	public int getType() {
		return type;
	}


	public void setType(int type) {
		this.type = type;
	}


	public FittedShape(GeometricPrimitive shape,double error, boolean accepted ) {
		super();
		Error = error;
		this.accepted = accepted;
		this.shape = shape;
	}
	public FittedShape() {
		 
	}


	public Segment getSegmentOfIndex(int i) {
 
		
		if (seg==null){
		 
		// then addd the vertix
      seg=new Segment();
      seg.setPoints(Originalink.getPoints());
      
 
		seg.setSegmentType(this.shape);
		
		}
		return seg;
	}


	public int getSegmentsCount() {
		 
		return 1;
	}
	
	@Override
	public String toString() {
		String st="";
		st+=" Shape is "+this.shape+" of type "+type;
		if  (!accepted)
			st+=" not ";
		st+=" accepted ";
		st+=" with error "+this.Error;
		return  st;
	}

	  
}
