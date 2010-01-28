/*
 * $Id: Util.java,v 1.8 2003/11/21 00:24:47 hwawen Exp $
 *
 * Copyright (c) 2003 The Regents of the University of California.
 * All rights reserved. See the file COPYRIGHT for details.
 */
package SketchMaster.lib;


import java.awt.geom.Rectangle2D;
import java.awt.geom.AffineTransform;

import SketchMaster.Stroke.StrokeData.Stroke;

/**
 * General utilities.
 *
 * @author Heloise Hse (hwawen@eecs.berkeley.edu)
 */
public class Util {
    /**
     * Store the stroke points in array forms.  This will become
     * unnecessary once I do some rewriting of TimedStroke class.
     * xvals, yvals and npoints are pre-allocated arrays and will be
     * filled in by this method.  xvals[strokeIndex][pointIndex]
     */
    public static void strokesToArrays(Stroke[] strokes, double[][] xvals,double[][] yvals, int[] npoints){
        for(int i=0; i<strokes.length; i++){
          Stroke s=strokes[i];
          int noPoints=s.getPointsCount();
            double x[]=new double[noPoints];
            double y[]=new double[noPoints];
            for(int j=0; j<noPoints; j++){
                x[j]=s.getPoint(j).getX();
                y[j]=s.getPoint(j).getY();
            }
            xvals[i]=x;
            yvals[i]=y;
            npoints[i]=noPoints;
        }
    }

    
    /**
     * Return the bounding box enclosing the set of strokes.
     */
    public static Rectangle2D getBounds(Stroke[] s) {
        Rectangle2D bbox = s[0].getStatisticalInfo().getBox();
        for(int i=1; i<s.length; i++){
            bbox.add(s[i].getStatisticalInfo().getBox());
        }
        return bbox;
    }

    
    /**
     * Scale the set of strokes uniformly (maintaining the aspect
     * ratio).  The scaling factor is dependent on the longer side of
     * the bounding box that encloses the strokes.
     *
     * If the height of the bounding box is bigger than the width, the
     * scale factor would be height/box.height.  Otherwise, it is
     * width/box.width.
     *
     * The strokes are also translated such that the upper left point
     * of the bounding box is at the origin.
     *
     * Return the transform performed on the set of input strokes.
     */
    public static AffineTransform normScaling(Stroke[] strokes,double height, double width){
        double xmin = Double.MAX_VALUE;
        double ymin = Double.MAX_VALUE;
        double xmax = Double.MIN_VALUE;
        double ymax = Double.MIN_VALUE;
        for(int i=0; i<strokes.length; i++){
            Rectangle2D bounds = strokes[i].getStatisticalInfo().getBox();
            xmin=Math.min(xmin,bounds.getMinX());
            xmax=Math.max(xmax,bounds.getMaxX());
            ymin=Math.min(ymin,bounds.getMinY());
            ymax=Math.max(ymax,bounds.getMaxY());
        }
        //scale to height,width
        double ow = xmax-xmin;
        double oh = ymax-ymin;
        double r = 0;
        if(oh>ow){
            r=height/oh; //scaling factor
        }
        else{
            r=width/ow; //scaling factor
        }
        AffineTransform tx = new AffineTransform();        
        tx.scale(r,r);//scale by r
        tx.translate(-xmin,-ymin);//translate upper left point to (0,0)
        for(int i=0; i<strokes.length; i++){
            strokes[i].transform(tx);
        }
        return tx;
    }

    /**
     * Return the Euclidean distance between two points, (x1, y1) and
     * (x2, y2).
     */
    public static double distance(double x1, double y1, double x2, double y2){
        return Math.sqrt(Math.pow(x2-x1, 2)+Math.pow(y2-y1, 2));
    }

    /**
     * Return the total pathlength by summing over the distance of
     * consecutive points in the array.
     */
    public static double pathLength(double xvals[], double yvals[], int num){
        double len = 0;
        double v,dx,dy;
        for(int i=1; i<num; i++){
            dx=xvals[i]-xvals[i-1];
            dy=yvals[i]-yvals[i-1];            
            v=Math.sqrt(dx*dx+dy*dy);
            len += v;
        }
        return len;
    }

    /**
     * calculate the path length starting from the startIndex to the
     * endIndex (inclusive) of the points in the array.
     */
    public static double pathLength(double xvals[], double yvals[], int startIndex, int endIndex){
        double len = 0;
        for(int i=startIndex+1; i<=endIndex; i++){
            len += distance(xvals[i], yvals[i], xvals[i-1], yvals[i-1]);
        }
        return len;
    }

    /**
     * Return the path length of a stroke by enumerating over the
     * points in the stroke and summing up the distances between every
     * two consecutive points.
     */
    public static double pathLength(Stroke s) {
        double len = 0;
        int num = s.getPointsCount();
        if(num>1){
            double p1x = s.getPoint(0).getX();
            double p1y = s.getPoint(0).getY();
            double p2x, p2y;
            for(int i = 1; i< num; i++){
                p2x = s.getPoint(i).getX();
                p2y = s.getPoint(i).getY();
                len += distance(p1x, p1y, p2x, p2y);
                p1x = p2x;
                p1y = p2y;
            }
        }
        return len;
    }
}









