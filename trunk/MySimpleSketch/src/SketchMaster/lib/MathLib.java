package SketchMaster.lib;

import java.awt.geom.Point2D;

import org.apache.log4j.Logger;

import JSci.maths.matrices.DoubleSquareMatrix;
import SketchMaster.Stroke.StrokeData.PointData;
import SketchMaster.system.SystemSettings;
import SketchMaster.system.Recogniziers.SimpleSymbolRecognizier;

public class MathLib {
	
	private static final Logger logger = Logger.getLogger(MathLib.class);
	
	
	public static Point2D solveQuadratic(double a, double b, double c){
		Point2D result=new Point2D.Double();
		  // Solve the discriminant (SQRT (b^2 - 4ac)
	    double  discr = Math.sqrt((b * b) - (4.0 * a * c));
	    double root1=0,root2=0;
	    // Test for NaN
	     if(Double.isNaN(discr)){
	        logger.trace("Equation has no roots");
	        return null;
	     }
	     
	     if(discr > 0)
	     {
	    	 logger.trace("Equation has 2 roots");
	        root1 = (-b + discr)/(2.0 * a);
	        root2 = (-b - discr)/(2.0 * a);
	        logger.trace("First root = " + root1);
	        logger.trace("Second roor = " + root2);

		       result.setLocation(root1, root2);
		 
	      }

	      if(discr == 0)
	      {
	    	  logger.trace("Equation has 1 root");
	        root1 = (-b + discr)/(2.0 * a);
	        root2 = (-b + discr)/(2.0 * a);
	        System.out.println("Root = " + root1);

		       result.setLocation(root1, root2);
		       
	      }

	       if(discr < 0){
	         //System.out.println("Equation has imaginary roots");
	         return null;
	       }
		
	       
	       
		return result;
	} 
	
	public static double MeanSquareError(double estimated, double Ideal){
		double temp=estimated-Ideal;
		return Math.sqrt( (temp*temp));
		
	}
	public static double MeanPercentSquareError(double estimated , double Ideal){
		
		double mean=MeanSquareError(estimated, Ideal);
		return mean/Ideal;
	}
 public static boolean containNan(DoubleSquareMatrix mat){
	 for (int i = 0; i < mat.rows(); i++) {
		  for (int j = 0; j < mat.columns(); j++) {
			if (Double.isNaN(mat.getElement(i, j)))
			{
			//	NanError=true;
				return true;
			}
		}
//		  if (NanError)
//			  break;
		
	}
	 return false;
 }	
	
  public static DoubleSquareMatrix inverse(DoubleSquareMatrix mat){
	  
	  
	  
	  DoubleSquareMatrix tempInverse=(DoubleSquareMatrix) mat.inverse();
	  boolean NanError=false;
	  // check if the temp inverse has a nan 
	  
	  
	  /////////i
	  if ( containNan(tempInverse))
	  {
		  
	
		//  tempInverse=new DoubleSquareMatrix(mat.rows());
		  
		   double tempDet=mat.det();
		  //first check if 
		  if (tempDet==0)
		  {
			 return inverseAlg(mat);
		  }
		  if (Double.isNaN(tempDet)){
			  
			  double x;
			  // now loop on the mat to check if any elment is nan. 
			  DoubleSquareMatrix tempMat = new  DoubleSquareMatrix(mat.rows());
			  for (int i = 0; i < mat.rows(); i++) {
				for (int j = 0; j < mat.rows(); j++) {
					 x=mat.getElement(i, j);
					 if (Double.isNaN(x))
						 x=SystemSettings.ZERO;
					 if (Double.isInfinite(x))
						 x=Double.MAX_VALUE/2.0;
					tempMat.setElement(i, j, x);
				}
			}
			  tempInverse=(DoubleSquareMatrix)tempMat.inverse();
			  if (containNan(tempInverse))
			  {
				// compute my inverse   
				  return inverseAlg(tempMat);
			  }
			  else
			  {
				  logger.info("  now the inverse has been corrected "+tempMat.det() +   "     "+ tempInverse.det());
					
				  return tempInverse;
			  }// else 
			  
		  }
		  else {
			  
			  //calcuate my inverse 
			  return inverseAlg(mat);
		  }//ekse 
	  }/// if contain nane 
	  
	  
	  
	  return tempInverse;
  }


  public static  DoubleSquareMatrix inverseAlg(DoubleSquareMatrix mat){
	  logger.info(" IIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII ");
	  return mat;
  }
}
