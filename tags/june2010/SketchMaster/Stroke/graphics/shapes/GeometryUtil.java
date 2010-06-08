// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   GeometryUtil.java

package SketchMaster.Stroke.graphics.shapes;

import org.apache.log4j.Logger;

import java.io.PrintStream;

import SketchMaster.Stroke.StrokeData.PointData;

// Referenced classes of package edu.mit.sketch.geom:
//            Line, Point, Vertex

public class GeometryUtil
{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(GeometryUtil.class);

    public GeometryUtil()
    {
    }

    public static double[] roundAngles(double angles[], double dx)
    {
        double result[] = new double[angles.length];
        for(int i = 0; i < angles.length; i++)
            if(Math.abs(angles[i] - (double)(int)(angles[i] / dx) * dx) < Math.abs(angles[i] - (double)((int)(angles[i] / dx) + 1) * dx))
                result[i] = (double)(int)(angles[i] / dx) * dx;
            else
                result[i] = (double)((int)(angles[i] / dx) + 1) * dx;
        return result;
    }

    public static boolean equalDoubles(double a, double b, double tolerance)
    {
		boolean returnboolean = Math.abs(a - b) < tolerance;
        return returnboolean;
    }

    public static boolean linesParallel(Line a, Line b, double t)
    {
        double angle_a = Math.atan2(a.y2 - a.y1, a.x2 - a.x1);
        double angle_b = Math.atan2(b.y2 - b.y1, b.x2 - b.x1);
		boolean returnboolean = parallelAngles(angle_a, angle_b, t);
        return returnboolean;
    }

    public static boolean parallelAngles(double angle_a, double angle_b, double t)
    {
		if (logger.isDebugEnabled()) {
			//  logger.debug("parallelAngles(double, double, double) - GeometryUtil : parallelAngles()"); //$NON-NLS-1$
		}
		if (logger.isDebugEnabled()) {
			//  logger.debug("parallelAngles(double, double, double) - " + (new StringBuilder()).append("angle_a").append(radian2degree(angle_a)).append(" angle_b").append(radian2degree(angle_b)).toString()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		boolean returnboolean = equalDoubles(angle_a, angle_b, t) || equalDoubles(angle_a + 3.1415926535897931D, angle_b, t) || equalDoubles(angle_a + 6.2831853071795862D, angle_b, t) || equalDoubles(angle_a, angle_b + 3.1415926535897931D, t) || equalDoubles(angle_a, angle_b + 6.2831853071795862D, t) || equalDoubles(angle_a + 3.1415926535897931D, angle_b + 3.1415926535897931D, t) || equalDoubles(angle_a + 3.1415926535897931D, angle_b + 6.2831853071795862D, t) || equalDoubles(angle_a + 6.2831853071795862D, angle_b + 3.1415926535897931D, t) || equalDoubles(angle_a + 6.2831853071795862D, angle_b + 6.2831853071795862D, t);
        return returnboolean;
    }

    public static double radian2degree(double angle)
    {
		double returndouble = (angle / 3.1415926535897931D) * 180D;
        return returndouble;
    }

    public static double degree2radian(double angle)
    {
		double returndouble = (angle * 3.1415926535897931D) / 180D;
        return returndouble;
    }

    public static double distance(PointData p1, PointData p2)
    {
		double returndouble = p1.distance(p2);
        return returndouble;
    }

    public static double cosTheoremAngle(PointData a, PointData b, PointData c)
    {
        double d1 = distance(a, b);
        double d2 = distance(b, c);
        double d3 = distance(a, c);
		double returndouble = Math.acos(((d1 * d1 + d2 * d2) - d3 * d3) / (2D * d1 * d2));
        return returndouble;
    }

    public static double ptSegDist(PointData p1, PointData p2, PointData p)
    {
		double returndouble = Math.sqrt(ptSegDistSq(p1.x, p1.y, p2.x, p2.y, p.x, p.y));
        return returndouble;
    }

    public static double ptSegDistSq(PointData p1, PointData p2, PointData p)
    {
		double returndouble = ptSegDistSq(p1.x, p1.y, p2.x, p2.y, p.x, p.y);
        return returndouble;
    }

    public static double ptSegDistSq(double X1, double Y1, double X2, double Y2, 
            double PX, double PY)
    {
        X2 -= X1;
        Y2 -= Y1;
        PX -= X1;
        PY -= Y1;
        double dotprod = PX * X2 + PY * Y2;
        double projlenSq;
        if(dotprod <= 0.0D)
        {
            projlenSq = 0.0D;
        } else
        {
            PX = X2 - PX;
            PY = Y2 - PY;
            dotprod = PX * X2 + PY * Y2;
            if(dotprod <= 0.0D)
                projlenSq = 0.0D;
            else
                projlenSq = (dotprod * dotprod) / (X2 * X2 + Y2 * Y2);
        }
		double returndouble = (PX * PX + PY * PY) - projlenSq;
        return returndouble;
    }

    public static int relativeCCW(PointData x1, PointData x2, PointData p)
    {
		int returnint = relativeCCW(x1.x, x1.y, x2.x, x2.y, p.x, p.y);
        return returnint;
    }

    public static int relativeCCW(double X1, double Y1, double X2, double Y2, 
            double PX, double PY)
    {
        X2 -= X1;
        Y2 -= Y1;
        PX -= X1;
        PY -= Y1;
        double ccw = PX * Y2 - PY * X2;
        if(ccw == 0.0D)
        {
            ccw = PX * X2 + PY * Y2;
            if(ccw > 0.0D)
            {
                PX -= X2;
                PY -= Y2;
                ccw = PX * X2 + PY * Y2;
                if(ccw < 0.0D)
                    ccw = 0.0D;
            }
        }
		int returnint = ccw >= 0.0D ? ((int) (ccw <= 0.0D ? 0 : 1)) : -1;
        return returnint;
    }

    public static double[] getIntermediateAngles(Vertex input_vertices[])
    {
        double intermediate_angles[] = new double[input_vertices.length - 1];
        for(int i = 0; i < intermediate_angles.length; i++)
        {
            double dx = input_vertices[i + 1].x - input_vertices[i].x;
            double dy = input_vertices[i + 1].y - input_vertices[i].y;
            intermediate_angles[i] = Math.atan2(dy, dx);
        }
        return intermediate_angles;
    }

    public static void continualizeDirection(double direction[])
    {
label0:
        for(int i = 1; i < direction.length; i++)
        {
            if(Math.abs(direction[i] - direction[i - 1]) <= 1.5707963267948966D)
                continue;
            int j = -10;
            do
            {
                if(j >= 11)
                    continue label0;
                if(Math.abs((direction[i] - direction[i - 1]) + (double)j * 3.1415926535897931D) < 1.5707963267948966D)
                {
                    direction[i] += (double)j * 3.1415926535897931D;
                    continue label0;
                }
                j++;
            } while(true);
        }
    }

    public static double segmentLength(PointData PointDatas[], int begin_index, int end_index)
    {
        double length = 0.0D;
        for(int i = begin_index; i < end_index && i < PointDatas.length - 1; i++)
            length += PointDatas[i].distance(PointDatas[i + 1]);
        return length;
    }
}
