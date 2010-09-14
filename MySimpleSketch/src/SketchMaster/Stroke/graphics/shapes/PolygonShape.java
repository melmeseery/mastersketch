// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Polygon.java

package SketchMaster.Stroke.graphics.shapes;

import org.apache.log4j.Logger;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.PrintStream;
import java.io.Serializable;





import SketchMaster.Stroke.StrokeData.PointData;

// Referenced classes of package edu.mit.sketch.geom:
//            Range, Point, Rectangle, Line, 
//            Vertex, GeometricObject, GeometryUtil

public class PolygonShape extends java.awt.Polygon
    implements GeometricObject, Serializable
{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(PolygonShape.class);

    public void setIndices(int indices[])
    {
//        this.indices = indices;
//        ranges = new Range[indices.length - 1];
//        for(int i = 0; i < ranges.length; i++)
//            ranges[i] = getRange(i);

    }

//    public Range[] getRanges()
//    {
//        return ranges;
//    }
//
//    public Range getRange(int i)
//    {
//        if(i + 1 < indices.length)
//            return new Range(indices[i], indices[i + 1]);
//        else
//            return null;
//    }

    public PolygonShape()
    {
    }

    public PolygonShape(java.awt.Polygon p)
    {
        npoints = p.npoints;
        xpoints = new int[p.npoints];
        ypoints = new int[p.npoints];
        for(int i = 0; i < npoints; i++)
        {
            xpoints[i] = p.xpoints[i];
            ypoints[i] = p.ypoints[i];
        }

    }

    public PolygonShape(PointData points[])
    {
        npoints = points.length;
        xpoints = new int[points.length];
        ypoints = new int[points.length];
        for(int i = 0; i < npoints; i++)
        {
            xpoints[i] = (int)points[i].x;
            ypoints[i] = (int)points[i].y;
        }

    }

    public PolygonShape(int xpoints[], int ypoints[], int npoints)
    {
        super(xpoints, ypoints, npoints);
    }

    public PolygonShape(PolygonShape p)
    {
        npoints = p.npoints;
        xpoints = new int[npoints];
        ypoints = new int[npoints];
        for(int i = 0; i < npoints; i++)
        {
            xpoints[i] = p.xpoints[i];
            ypoints[i] = p.ypoints[i];
        }

        points = p.getDataPoints();
        m_vertices = p.getOriginalVertices();
        setGraphicsContext(p.graphics);
        setTimeStamp(p.time_stamp);
    }

    public void copyVerticesFrom(PolygonShape p)
    {
        for(int i = 0; i < npoints; i++)
        {
            xpoints[i] = p.xpoints[i];
            ypoints[i] = p.ypoints[i];
        }

        points = p.getDataPoints();
        m_vertices = p.getOriginalVertices();
    }

    PolygonShape(Line line)
    {
        npoints = 2;
        xpoints = new int[2];
        ypoints = new int[2];
        xpoints[0] = (int)line.x1;
        ypoints[0] = (int)line.y1;
        xpoints[1] = (int)line.x2;
        ypoints[1] = (int)line.y2;
        points = line.getDataPoints();
        m_vertices = line.getOriginalVertices();
   //     setGraphicsContext(line.graphics);
        setTimeStamp(line.time_stamp);
    }

    public String getType()
    {
        return "polygon";
    }

    /**
     * @deprecated Method addPointDouble is deprecated
     */

    public void addPointDouble(double x, double y)
    {
		logger.error("addPointDouble(double, double) - Warning: casting to int: Polygon.addPointDouble()", null); //$NON-NLS-1$
        addPoint((int)x, (int)y);
    }

    public String toString()
    {
        StringBuilder description = (new StringBuilder()).append("Polygon with ").append(npoints).append(" vertices ");
        for(int i = 0; i < npoints; i++)
            description .append(description).append("( ").append(xpoints[i]).append(", ").append(ypoints[i]).append(" )--").toString();

        return description.toString();
    }

    public void paint()
    {
        if(graphics == null)
            return;
        graphics.setColor(Color.black);
        paint(graphics);
        for(int i = 0; i < npoints - 1; i++)
            GraphicsUtil.drawThickLine(1, graphics, xpoints[i], ypoints[i], xpoints[i + 1], ypoints[i + 1]);
    }

    public void paintOriginal(Graphics g)
    {
        points.paint(g);
    }

    public void paint(Graphics g)
    {
        for(int i = 0; i < npoints - 1; i++)
            GraphicsUtil.drawThickLine(1, g, xpoints[i], ypoints[i], xpoints[i + 1], ypoints[i + 1]);
    }

    public double getDistanceTo(PointData p)
    {
        double minDist = 1.7976931348623157E+308D;
        for(int i = 0; i < npoints - 1; i++)
        {
            double dist = Line.ptSegDist(xpoints[i], ypoints[i], xpoints[i + 1], ypoints[i + 1], p.x, p.y);
            if(dist < minDist)
                minDist = dist;
        }
        return minDist;
    }

    public boolean pointIsOn(PointData p, double radius)
    {
        for(int i = 0; i < npoints - 1; i++)
            if(Line.ptSegDist(xpoints[i], ypoints[i], xpoints[i + 1], ypoints[i + 1], p.x, p.y) < radius)
                return true;
        return false;
    }

    public boolean pointIsOnOriginal(PointData p, double radius)
    {
		boolean returnboolean = points.pointIsOn(p, radius);
        return returnboolean;
    }

    public void translate(double x, double y)
    {
        for(int i = 0; i < npoints; i++)
        {
            xpoints[i] += (int)x;
            ypoints[i] += (int)y;
        }

        if(points != null)
            points.translate(x, y);
    }

    public int closestNeighborIndex(PointData p)
    {
        int min = (int)p.distance(xpoints[0], ypoints[0]);
        int min_index = 0;
        for(int i = 0; i < npoints; i++)
        {
            int distance = (int)p.distance(xpoints[i], ypoints[i]);
            if(distance < min)
            {
                min = distance;
                min_index = i;
            }
        }
        return min_index;
    }

    public PointData pointAt(int i)
    {
		PointData returnPointData = new PointData(xpoints[i], ypoints[i]);
        return returnPointData;
    }

    public void drawAngleAt(int i, PointData reference_point)
    {
        if(graphics == null || i >= npoints - 1 || i < 1)
            return;
        PointData current = pointAt(i);
        PointData next = pointAt(i + 1);
        PointData previous = pointAt(i - 1);
        int theta_start = (int)((Math.atan2(current.y - previous.y, current.x - previous.x) * 180D) / 3.1415926535897931D);
        if((double)theta_start < 0.0D)
            theta_start += 360;
        int x[] = new int[3];
        int y[] = new int[3];
        x[0] = (int)previous.x;
        y[0] = (int)previous.y;
        x[1] = (int)current.x;
        y[1] = (int)current.y;
        x[2] = (int)next.x;
        y[2] = (int)next.y;
        PolygonShape p = new PolygonShape(x, y, 3);
        boolean clockwise = true;
        if(GeometryUtil.relativeCCW(previous, current, next) == -1)
            clockwise = false;
        int arc_angle = (int)((GeometryUtil.cosTheoremAngle(previous, current, next) / 3.1415926535897931D) * 180D);
        if(clockwise)
            arc_angle = -arc_angle;
        if(!p.contains(reference_point.x, reference_point.y))
            arc_angle -= 360;
        graphics.setColor(Color.red);
        graphics.fillArc((int)current.x - 15, (int)current.y - 15, 30, 30, 180 - theta_start, arc_angle);
    }

    public void setGraphicsContext(Graphics g)
    {
        graphics = g;
    }

    public boolean touches(GeometricObject object)
    {
        PolygonShape p = object.getPolygonalBounds();
        int next_i = 0;
        int next_j = 0;
        for(int i = 0; i < npoints - 1; i++)
        {
            next_i = i + 1;
            for(int j = 0; j < p.npoints - 1; j++)
            {
                next_j = j + 1;
                if(Line.linesIntersect(xpoints[i], ypoints[i], xpoints[next_i], ypoints[next_i], p.xpoints[j], p.ypoints[j], p.xpoints[next_j], p.ypoints[next_j]))
                    return true;
            }

        }
        return false;
    }

    public Rectangle getHorizontalBounds()
    {
		Rectangle returnRectangle = super.getBounds();
        return returnRectangle;
    }

    public SketchMaster.Stroke.graphics.shapes.Rectangle getRectangularBounds()
    {
		SketchMaster.Stroke.graphics.shapes.Rectangle returnRectangle = getRectangularBounds(72);
        return returnRectangle;
    }
  
    public boolean containsGeometricObject(GeometricObject object)
    {
        if(touches(object))
            return false;
        PolygonShape p = object.getPolygonalBounds();
        for(int i = 0; i < p.npoints; i++)
            if(!contains(p.xpoints[i], p.ypoints[i]))
                return false;
        return true;
    }

    public SketchMaster.Stroke.graphics.shapes.Rectangle getRectangularBounds(int steps)
    {
        double step_angle = 3.1415926535897931D / (double)(2 * steps);
        PolygonShape copy = new PolygonShape(this);
        Rectangle bounds = getHorizontalBounds();
        int min_index = 0;
        int min_sum = bounds.width + bounds.height;
   SketchMaster.Stroke.graphics.shapes.Rectangle result;// = new SketchMaster.Stroke.graphics.shapes.Rectangle(getHorizontalBounds());
        for(int i = 1; i < steps; i++)
        {
            copy.rotate(-step_angle);
            bounds = copy.getHorizontalBounds();
            if(bounds.width + bounds.height < min_sum)
            {
                min_sum = bounds.width + bounds.height;
                min_index = i;
            }
        }

        copy = new PolygonShape(this);
        copy.rotate(-step_angle * (double)min_index);
        bounds = copy.getHorizontalBounds();
        result = new SketchMaster.Stroke.graphics.shapes.Rectangle(bounds);
        result.rotateAbout(new PointData(xpoints[0], ypoints[0]), (double)min_index * step_angle);
        return result;
    }

//    public Line[] getLines()
//    {
//        Line edges[] = new Line[npoints - 1];
//        for(int i = 0; i < npoints - 1; i++)
//        {
//            edges[i] = new Line(xpoints[i], ypoints[i], xpoints[i + 1], ypoints[i + 1]);
//            if(ranges == null)
//                continue;
//            Vertex line_vertices[] = new Vertex[(ranges[i].max - ranges[i].min) + 1];
//            for(int j = ranges[i].min; j <= ranges[i].max; j++)
//                line_vertices[j - ranges[i].min] = m_vertices[j];
//
//            edges[i].setOriginalVertices(line_vertices);
//        }
//
//        return edges;
//    }

//    public Line getEdge(int n)
//    {
//        return new Line(xpoints[n], ypoints[n], xpoints[n + 1], ypoints[n + 1]);
//    }

    public double getAngle(int n)
    {
		double returndouble = GeometryUtil.cosTheoremAngle(new PointData(xpoints[n], ypoints[n]), new PointData(xpoints[(n + 1) % npoints], ypoints[(n + 1) % npoints]), new PointData(xpoints[(n + 2) % npoints], ypoints[(n + 2) % npoints]));
        return returndouble;
    }

    public PolygonShape getPolygonalBounds()
    {
        return this;
    }

    public void rotateAboutOrigin(double theta)
    {
        PointData p = new PointData();
        double radius = 0.0D;
        double angle = 0.0D;
        for(int i = 0; i < npoints; i++)
        {
            p.x = xpoints[i];
            p.y = ypoints[i];
            radius = Math.sqrt(p.x * p.x + p.y * p.y);
            if(p.x != 0.0D || p.y != 0.0D)
            {
                angle = Math.atan2(p.y, p.x) + theta;
                xpoints[i] = (int)(Math.cos(angle) * radius);
                ypoints[i] = (int)(Math.sin(angle) * radius);
            }
        }
    }

    public void rotateAboutCOM(double theta)
    {
        double center_x = 0.0D;
        double center_y = 0.0D;
        for(int i = 0; i < npoints; i++)
        {
            center_x += xpoints[0];
            center_y += xpoints[0];
        }

        center_x /= npoints;
        center_y /= npoints;
        double radius = 0.0D;
        double angle = 0.0D;
        PointData p = new PointData();
        translate(-center_x, -center_y);
        for(int i = 0; i < npoints; i++)
        {
            p.x = xpoints[i];
            p.y = ypoints[i];
            radius = Math.sqrt(p.x * p.x + p.y * p.y);
            if(p.x != 0.0D || p.y != 0.0D)
            {
                angle = Math.atan2(p.y, p.x);
                xpoints[i] = (int)(Math.cos(angle + theta) * radius);
                ypoints[i] = (int)(Math.sin(angle + theta) * radius);
            }
        }

        translate(center_x, center_y);
    }

    public void rotate(double theta)
    {
        if(npoints < 1)
            return;
        int oX = xpoints[0];
        int oY = ypoints[0];
        translate(-(double)oX, -(double)oY);
        double cost = Math.cos(theta);
        double sint = Math.sin(theta);
        for(int i = 0; i < npoints; i++)
        {
            int px = xpoints[i];
            int py = ypoints[i];
            xpoints[i] = (int)Math.round((double)px * cost - (double)py * sint);
            ypoints[i] = (int)Math.round((double)px * sint + (double)py * cost);
        }

        translate(oX, oY);
    }

    public int spatialRelation(GeometricObject object)
    {
		int returnint = getRectangularBounds().spatialRelation(object);
        return returnint;
    }

    public void setTimeStamp(long time_stamp)
    {
        this.time_stamp = time_stamp;
    }

    public long getTimeStamp()
    {
        return time_stamp;
    }

    public void setDataPoints(PolygonShape points)
    {
        this.points = points;
    }

    public void setOriginalVertices(Vertex pts[])
    {
        m_vertices = new Vertex[pts.length];
        for(int i = 0; i < pts.length; i++)
            m_vertices[i] = pts[i];
    }

    public PolygonShape getDataPoints()
    {
        return points;
    }

    public Vertex[] getOriginalVertices()
    {
        if(m_vertices == null)
            return null;
        Vertex ret[] = new Vertex[m_vertices.length];
        for(int i = 0; i < m_vertices.length; i++)
            ret[i] = m_vertices[i];
        return ret;
    }

    public boolean containsGeometricObjects(GeometricObject objects[])
    {
        for(int i = 0; i < objects.length; i++)
            if(!containsGeometricObject(objects[i]))
                return false;
        return true;
    }

    public double[] getAbsoluteAngles()
    {
        double angles[] = new double[npoints - 1];
        for(int i = 0; i < npoints - 1; i++)
            angles[i] = Math.atan2(ypoints[i + 1] - ypoints[i], xpoints[i + 1] - xpoints[i]);
        return angles;
    }

    public double getPolygonLength()
    {
        double arc_length = 0.0D;
        for(int i = 0; i < npoints - 1; i++)
            arc_length += Math.sqrt((ypoints[i + 1] - ypoints[i]) * (ypoints[i + 1] - ypoints[i]) + (xpoints[i + 1] - xpoints[i]) * (xpoints[i + 1] - xpoints[i]));
        return arc_length;
    }

    public double[] getAbsolutePositiveAngles()
    {
        double angles[] = getAbsoluteAngles();
        for(int i = 0; i < angles.length; i++)
            if(angles[i] < 0.0D)
                angles[i] += 3.1415926535897931D;
        return angles;
    }

    public boolean tryCombining(Object o, int tolerance)
    {
        if(o instanceof Line)
            return tryCombining(new PolygonShape((Line)o), tolerance);
        if(o instanceof PolygonShape)
        {
            PolygonShape polygon = (PolygonShape)o;
            PolygonShape tmp_points = new PolygonShape();
            if(PointData.distance(xpoints[npoints - 1], ypoints[npoints - 1], polygon.xpoints[0], polygon.ypoints[0]) < (double)tolerance)
            {
                int tmp_xpoints[] = new int[(npoints + polygon.npoints) - 1];
                int tmp_ypoints[] = new int[(npoints + polygon.npoints) - 1];
                for(int i = 0; i < npoints; i++)
                {
                    tmp_xpoints[i] = xpoints[i];
                    tmp_ypoints[i] = ypoints[i];
                }

                for(int i = 1; i < polygon.npoints; i++)
                {
                    tmp_xpoints[(npoints + i) - 1] = polygon.xpoints[i];
                    tmp_ypoints[(npoints + i) - 1] = polygon.ypoints[i];
                }

                if(points != null && polygon.points != null)
                {
                    for(int i = 0; i < polygon.points.npoints; i++)
                        points.addPoint(polygon.points.xpoints[i], polygon.points.ypoints[i]);

                }
                npoints += polygon.npoints - 1;
                xpoints = tmp_xpoints;
                ypoints = tmp_ypoints;
                return true;
            }
            if(PointData.distance(xpoints[0], ypoints[0], polygon.xpoints[polygon.npoints - 1], polygon.ypoints[polygon.npoints - 1]) < (double)tolerance)
            {
                int tmp_xpoints[] = new int[(npoints + polygon.npoints) - 1];
                int tmp_ypoints[] = new int[(npoints + polygon.npoints) - 1];
                for(int i = 0; i < polygon.npoints; i++)
                {
                    tmp_xpoints[i] = polygon.xpoints[i];
                    tmp_ypoints[i] = polygon.ypoints[i];
                }

                for(int i = 1; i < npoints; i++)
                {
                    tmp_xpoints[(polygon.npoints + i) - 1] = xpoints[i];
                    tmp_ypoints[(polygon.npoints + i) - 1] = ypoints[i];
                }

                if(points != null && polygon.points != null)
                {
                    for(int i = 0; i < polygon.points.npoints; i++)
                        tmp_points.addPoint(polygon.points.xpoints[i], polygon.points.ypoints[i]);

                    for(int i = 0; i < points.npoints; i++)
                        tmp_points.addPoint(points.xpoints[i], points.ypoints[i]);

                    points = tmp_points;
                }
                npoints += polygon.npoints - 1;
                xpoints = tmp_xpoints;
                ypoints = tmp_ypoints;
                return true;
            }
            if(PointData.distance(xpoints[npoints - 1], ypoints[npoints - 1], polygon.xpoints[polygon.npoints - 1], polygon.ypoints[polygon.npoints - 1]) < (double)tolerance)
            {
                int tmp_xpoints[] = new int[(npoints + polygon.npoints) - 1];
                int tmp_ypoints[] = new int[(npoints + polygon.npoints) - 1];
                for(int i = 0; i < polygon.npoints - 1; i++)
                {
                    tmp_xpoints[i] = polygon.xpoints[i];
                    tmp_ypoints[i] = polygon.ypoints[i];
                }

                for(int i = 0; i < npoints; i++)
                {
                    tmp_xpoints[(polygon.npoints + i) - 1] = xpoints[npoints - i - 1];
                    tmp_ypoints[(polygon.npoints + i) - 1] = ypoints[npoints - i - 1];
                }

                if(points != null && polygon.points != null)
                {
                    for(int i = 0; i < points.npoints; i++)
                        tmp_points.addPoint(points.xpoints[i], points.ypoints[i]);

                    for(int i = polygon.points.npoints - 1; i >= 0; i--)
                        tmp_points.addPoint(polygon.points.xpoints[i], polygon.points.ypoints[i]);

                    points = tmp_points;
                }
                npoints += polygon.npoints - 1;
                xpoints = tmp_xpoints;
                ypoints = tmp_ypoints;
                return true;
            }
            if(PointData.distance(xpoints[0], ypoints[0], polygon.xpoints[0], polygon.ypoints[0]) < (double)tolerance)
            {
                int tmp_xpoints[] = new int[(npoints + polygon.npoints) - 1];
                int tmp_ypoints[] = new int[(npoints + polygon.npoints) - 1];
                for(int i = 0; i < npoints - 1; i++)
                {
                    tmp_xpoints[i] = xpoints[npoints - i - 1];
                    tmp_ypoints[i] = ypoints[npoints - i - 1];
                }

                for(int i = 0; i < polygon.npoints; i++)
                {
                    tmp_xpoints[(npoints + i) - 1] = polygon.xpoints[i];
                    tmp_ypoints[(npoints + i) - 1] = polygon.ypoints[i];
                }

                if(points != null && polygon.points != null)
                {
                    for(int i = points.npoints - 1; i >= 0; i--)
                        tmp_points.addPoint(points.xpoints[i], points.ypoints[i]);

                    for(int i = 0; i < polygon.points.npoints; i++)
                        tmp_points.addPoint(polygon.points.xpoints[i], polygon.points.ypoints[i]);

                    points = tmp_points;
                }
                npoints += polygon.npoints - 1;
                xpoints = tmp_xpoints;
                ypoints = tmp_ypoints;
                return true;
            }
        }
        return false;
    }

    public void combineEndPoints(double error)
    {
        if(npoints < 3)
            return;
        int l = npoints - 1;
        if((double)((xpoints[0] - xpoints[l]) * (xpoints[0] - xpoints[l]) + (ypoints[0] - ypoints[l]) * (ypoints[0] - ypoints[l])) < error * error)
        {
            xpoints[l] = xpoints[0];
            ypoints[l] = ypoints[0];
        }
    }

    public double[] getMajorAngles()
    {
        double angles[] = getAbsolutePositiveAngles();
        double window_size = 500;//Tablet.window_width; 
        double dx = 0.017453292519943295D;
        double wrapped_angles[] = new double[angles.length * 2];
        int j = 0;
        for(j = 0; j < angles.length; j++)
            wrapped_angles[j] = angles[j];

        for(j = angles.length; j < wrapped_angles.length; j++)
            wrapped_angles[j] = angles[j % angles.length] - 3.1415926535897931D;

        int histogram[] = getSlidingWindowHistogram(wrapped_angles, -window_size, 3.1415926535897931D + window_size, dx, window_size);
        double new_angles[] = new double[npoints];
        boolean incremented = true;
        int index = 0;
        int n = 0;
        int distinct_angles = 0;
        for(int i = 0; i < histogram.length; i++)
        {
            if(histogram[i] == 0 && !incremented)
            {
                new_angles[index] /= n;
//                if(Tablet.debug)
//                    logger.info((new StringBuilder()).append("index = ").append(index).append("\t").append(" final value  ").append(new_angles[index]).toString());
                index++;
                incremented = true;
                n = 0;
                distinct_angles++;
            }
            if(histogram[i] == 0)
                continue;
            n += histogram[i];
            new_angles[index] += (dx * (double)i - window_size) * (double)histogram[i];
//            if(Tablet.debug)
//                logger.info((new StringBuilder()).append("index = ").append(index).append("\t").append(" adding ").append(dx * (double)i).toString());
            incremented = false;
        }
//
//        if(Tablet.debug)
//            logger.info("Final angles : ");
        double angle_set[] = new double[distinct_angles];
        for(int i = 0; i < distinct_angles; i++)
        {
//            if(Tablet.debug)
//                logger.info((new StringBuilder()).append(" i = ").append(i).append(" angle = ").append(new_angles[i]).toString());
            angle_set[i] = new_angles[i];
        }
        return angle_set;
    }

    public void normalize(double angle_set[])
    {
        int new_xpoints[] = new int[npoints];
        int new_ypoints[] = new int[npoints];
        int last_processed_vertex_index = 0;
        double closest_angle = 0.0D;
        double current_point_x = xpoints[0];
        double current_point_y = ypoints[0];
        for(int i = 0; i < npoints - 2; i++)
        {
            Line edge = getEdge(i);
            Line next_edge = getEdge(i + 1);
            closest_angle = edge.chooseApproximateAngle(angle_set);
            Line line1 = new Line(current_point_x - Math.cos(closest_angle) * 1000D, current_point_y - Math.sin(closest_angle) * 1000D, current_point_x + Math.cos(closest_angle) * 1000D, current_point_y + Math.sin(closest_angle) * 1000D);
            current_point_x = (next_edge.x1 + next_edge.x2) / 2D;
            current_point_y = (next_edge.y1 + next_edge.y2) / 2D;
            closest_angle = next_edge.chooseApproximateAngle(angle_set);
            Line line2 = new Line(current_point_x - Math.cos(closest_angle) * 1000D, current_point_y - Math.sin(closest_angle) * 1000D, current_point_x + Math.cos(closest_angle) * 1000D, current_point_y + Math.sin(closest_angle) * 1000D);
            PointData intersection;
            try
            {
                intersection = line1.getIntersection(line2);
            }
            catch(Exception e)
            {
                last_processed_vertex_index++;
                new_xpoints[last_processed_vertex_index] = (int)edge.x2;
                new_ypoints[last_processed_vertex_index] = (int)edge.y2;
				logger.error("normalize(double[]) - error in finding intersection in Polygon.normalize()", e); //$NON-NLS-1$
                continue;
            }
          //  Tablet.debug_graphics.setColor(Color.red);
            //intersection.paint(Tablet.debug_graphics);
            last_processed_vertex_index++;
            new_xpoints[last_processed_vertex_index] = (int)intersection.x;
            new_ypoints[last_processed_vertex_index] = (int)intersection.y;
        }

        new_xpoints[0] = xpoints[0];
        new_ypoints[0] = ypoints[0];
        new_xpoints[npoints - 1] = xpoints[npoints - 1];
        new_ypoints[npoints - 1] = ypoints[npoints - 1];
        xpoints = new_xpoints;
        ypoints = new_ypoints;
    }

    public final GeometricObject copy()
    {
        PolygonShape polygon = new PolygonShape(this);
        polygon.time_stamp = time_stamp;
        if(points != null)
            polygon.points = (PolygonShape)points.copy();
        return polygon;
    }

    public int getIntType()
    {
        return 0;
    }

    private PolygonShape points;
    private Vertex m_vertices[];
    public long time_stamp;
    public transient Graphics graphics;
    static final long serialVersionUID = 0x19933fe1720280d4L;
    private int indices[];
   // private Range ranges[];
	public long getTime() {
		
		return time_stamp;
	}
	 public Line getEdge(int n)
	    {
		Line returnLine = new Line(xpoints[n], ypoints[n], xpoints[n + 1], ypoints[n + 1]);
	        return returnLine;
	    }
	public void setTime(long l) {
		time_stamp=l;
		
	}
	
	
	public static int[] getSlidingWindowHistogram(double data[], double min, double max, double dx, double window_width)
    {
        int max_height = 0;
        int data_points = 0;
        int histogram[] = new int[(int)((max - min) / dx)];
        for(int i = 0; i < histogram.length; i++)
        {
            for(int j = 0; j < data.length; j++)
                if(data[j] > (min + dx * (double)i) - window_width && data[j] < min + dx * (double)i + window_width)
                    data_points++;

            histogram[i] = data_points;
            if(max_height < data_points)
                max_height = data_points;
            data_points = 0;
        }
        return histogram;
    }

	public void paint(Graphics2D g) {
		  
		
	}
}
