/**
 * 
 */
package SketchMaster.Stroke.graphics.shapes;

import java.awt.Graphics2D;
import java.util.ArrayList;

import SketchMaster.Stroke.StrokeData.PointData;
import SketchMaster.Stroke.StrokeData.Stroke;
import SketchMaster.lib.CurveFitData;

/**
 * @author TOSHIBA
 *
 */
public class FittedShape implements GuiShape{

	GeometricPrimitive  shape;
	double Error;
	boolean accepted;
	public	double slope;
	public double  intercept;
	
	public void paint(Graphics2D g) {
		shape.paint(g);
		
	}


	public FittedShape(GeometricPrimitive shape,double error, boolean accepted ) {
		super();
		Error = error;
		this.accepted = accepted;
		this.shape = shape;
	}
	public FittedShape() {
		 
	}
	public void fitLine(CurveFitData sum) {
		  double dem=(sum.N* sum.Exx )- sum.Ex_2;
			
		  slope =((sum.N*sum.Exy)-(sum.Ey*sum.Ex))/(dem);
		
		  intercept=(((sum.Ey*sum.Exx)-(sum.Exy*sum.Ex))/(dem));
		
	}


	public void fitLine(Stroke stroke){
		
		CurveFitData sum = stroke.getStatisticalInfo().Sums();
		  double dem=(sum.N* sum.Exx )- sum.Ex_2;
			
			  slope =((sum.N*sum.Exy)-(sum.Ey*sum.Ex))/(dem);
			
			  intercept=(((sum.Ey*sum.Exx)-(sum.Exy*sum.Ex))/(dem));
	}
	
	  public static void fitLine(double [] parameters, double [] x, double [] y,
              double [] sigmaY ){

double s=0.0,sx=0.0,sy=0.0,sxx=0.0,sxy=0.0,del;
int numPoints=x.length;
// Null sigmaY implies a constant error which drops
// out of the divisions of the sums.
if( sigmaY != null){
for(int i=0; i < numPoints; i++){

  s   += 1.0/(sigmaY[i]*sigmaY[i]);
  sx  += x[i]/(sigmaY[i]*sigmaY[i]);
  sy  += y[i]/(sigmaY[i]*sigmaY[i]);
  sxx += (x[i]*x[i])/(sigmaY[i]*sigmaY[i]);
  sxy += (x[i]*y[i])/(sigmaY[i]*sigmaY[i]);
}
}else{
s = numPoints;
for(int i=0; i < numPoints; i++){
  sx  += x[i];
  sy  += y[i];
  sxx += x[i]*x[i];
  sxy += x[i]*y[i];
}
}

del = s*sxx - sx*sx;

// Intercept
parameters[0] = (sxx*sy -sx*sxy)/del;
// Slope
parameters[1] = (s*sxy -sx*sy)/del;


// Errors (sd**2) on the:
// intercept
parameters[2] = sxx/del;
// and slope
parameters[3] = s/del;

}


	
}
