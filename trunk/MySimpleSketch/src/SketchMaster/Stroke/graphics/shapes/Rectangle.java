// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Rectangle.java

package SketchMaster.Stroke.graphics.shapes;

import org.apache.log4j.Logger;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Dimension2D;
import java.io.PrintStream;
import java.io.Serializable;

import SketchMaster.Stroke.StrokeData.PointData;
import SketchMaster.system.SystemSettings;

// Referenced classes of package edu.mit.sketch.geom:
//            Polygon, Line, Point, Vertex, 
//            GeometricObject, Rotatable, GeometryUtil, Direction, 
//            SpatialRelation

public class Rectangle
    implements GeometricObject,  Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 20359727367889206L;

	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(Rectangle.class);

    private PolygonShape points;
    private Vertex m_vertices[];
    private double angle;
    private double width_x;
    private double width_y;
    private double height_x;
    private double height_y;
    private double width;
    private double height;
    public double x;
    public double y;
    public long time_stamp;
    public transient Graphics graphics;
    public Rectangle(java.awt.Rectangle rectangle)
    {
        this(rectangle.x, rectangle.y, rectangle.getWidth(), 0.0D, rectangle.getHeight());
    }

    public Rectangle(PointData upper_left_point, Dimension dimension)
    {
        this(upper_left_point.x, upper_left_point.y, dimension.width, 0.0D, dimension.height);
    }

    public Rectangle(PointData upper_left_point, Dimension2D dimension)
    {
        this(upper_left_point.x, upper_left_point.y, dimension.getWidth(), 0.0D, dimension.getHeight());
    }

    public Rectangle(PointData upper_left_point, Dimension dimension, double angle)
    {
        this.angle = Math.atan2(Math.sin(angle), Math.cos(angle));
        x = upper_left_point.x;
        y = upper_left_point.y;
        width_x = (double)dimension.width * Math.cos(angle);
        width_y = (double)dimension.width * Math.sin(angle);
        height_x = (double)dimension.height * Math.cos(angle + 1.5707963267948966D);
        height_y = (double)dimension.height * Math.sin(angle + 1.5707963267948966D);
        width = dimension.width;
        height = dimension.height;
    }

    public Rectangle(PointData upper_left_point, Dimension2D dimension, double angle)
    {
        double dWidth = dimension.getWidth();
        double dHeight = dimension.getHeight();
        this.angle = Math.atan2(Math.sin(angle), Math.cos(angle));
        x = upper_left_point.x;
        y = upper_left_point.y;
        width_x = dWidth * Math.cos(angle);
        width_y = dWidth * Math.sin(angle);
        height_x = dHeight * Math.cos(angle + 1.5707963267948966D);
        height_y = dHeight * Math.sin(angle + 1.5707963267948966D);
        width = dWidth;
        height = dHeight;
    }

    public Rectangle(double x, double y,PointData width_vector, double height)
    {
        this(x, y, width_vector.x, width_vector.y, height);
    }

    public Rectangle(double x, double y, double width_x, double width_y, double height)
    {
        this.x = x;
        this.y = y;
        this.width_x = width_x;
        this.width_y = width_y;
        angle = getAngle();
        height_x = height * Math.cos(angle + 1.5707963267948966D);
        height_y = height * Math.sin(angle + 1.5707963267948966D);
        width = PointData.distance(0.0D, 0.0D, width_x, width_y);
        this.height = height;
    }

    public Rectangle(Rectangle r)
    {
        setRectangle(r);
    }

    public Rectangle(double x, double y, double width, double height)
    {
        this(x, y, width, 0.0D, height);
    }

    public void setRectangle(Rectangle r)
    {
        x = r.x;
        y = r.y;
        width_x = r.width_x;
        width_y = r.width_y;
        angle = r.angle;
        height_x = r.height_x;
        height_y = r.height_y;
        width = r.width;
        height = r.height;
    }

    public void scaleAboutCenter(double scale)
    {
        x = x - (scale - 1.0D) * (width_x + height_x);
        y = y - (scale - 1.0D) * (width_y + height_y);
        width_x *= 1.0D + (scale - 1.0D) * 2D;
        width_y *= 1.0D + (scale - 1.0D) * 2D;
        height_x *= 1.0D + (scale - 1.0D) * 2D;
        height_y *= 1.0D + (scale - 1.0D) * 2D;
        width *= 1.0D + (scale - 1.0D) * 2D;
        height *= 1.0D + (scale - 1.0D) * 2D;
    }

    public String getType()
    {
        if(Math.abs(width - height) < SystemSettings.ZERO_COMPARE )
            return "square";
        else
            return "rectangle";
    }

    public String toString()
    {
        return (new StringBuilder()).append("Rectangle ( ").append(x).append(", ").append(y).append(" ) w = ").append(width).append(" h = ").append(height).append(" angle = ").append(angle).toString();
    }

    public void paint()
    {
        if(graphics == null)
        {
            return;
        } else
        {
            graphics.setColor(Color.black);
            paint(graphics);
            return;
        }
    }

    public void paintOriginal(Graphics g)
    {
        points.paint(g);
    }

    public void paint(Graphics g, int thickness)
    {
        int x1 = (int)width_x;
        int y1 = (int)width_y;
        int x2 = (int)height_x;
        int y2 = (int)height_y;
        GraphicsUtil.drawThickLine(thickness, g, x, y, x + (double)x1, y + (double)y1);
        GraphicsUtil.drawThickLine(thickness, g, x, y, x + (double)x2, y + (double)y2);
        GraphicsUtil.drawThickLine(thickness, g, x + (double)x1, y + (double)y1, x + (double)x1 + (double)x2, y + (double)y1 + (double)y2);
        GraphicsUtil.drawThickLine(thickness, g, x + (double)x2, y + (double)y2, x + (double)x1 + (double)x2, y + (double)y1 + (double)y2);
    }

    public void shade(Graphics g, int shades, int thickness, int offset)
    {
        int x1 = (int)width_x;
        int y1 = (int)width_y;
        int x2 = (int)height_x;
        int y2 = (int)height_y;
        if(height > width)
        {
            x1 = (int)height_x;
            y1 = (int)height_y;
            x2 = (int)width_x;
            y2 = (int)width_y;
        }
        for(int i = 0; i < shades; i++)
            GraphicsUtil.drawThickLine(thickness, g, x + (double)(x1 * i) / (double)shades, y + (double)(y1 * i) / (double)shades, x + (double)(x1 * (i + offset)) / (double)shades + (double)x2, y + (double)(y1 * (i + offset)) / (double)shades + (double)y2);

    }

    public void paint(Graphics g)
    {
        paint(g, 1);
    }

    public void shade(Graphics g, int number_of_shades)
    {
        shade(g, number_of_shades, 1, 0);
    }

    public boolean pointIsOn(PointData point, double radius)
    {
        return getPolygonalBounds().pointIsOn(point, radius);
    }

    public boolean pointIsOnOriginal(PointData p, double radius)
    {
        if(points == null)
            return false;
        else
            return points.pointIsOn(p, radius);
    }

    public void setGraphicsContext(Graphics g)
    {
        graphics = g;
    }

    public boolean touches(GeometricObject object)
    {
        return getPolygonalBounds().touches(object.getPolygonalBounds());
    }

    public Rectangle getRectangularBounds()
    {
        return getPolygonalBounds().getRectangularBounds();
    }

    public java.awt.Rectangle getBounds()
    {
        return getPolygonalBounds().getBounds();
    }

    public boolean containsGeometricObject(GeometricObject object)
    {
        return getPolygonalBounds().containsGeometricObject(object);
    }

    public PolygonShape getPolygonalBounds()
    {
        PolygonShape result = new PolygonShape();
        result.addPointDouble(x, y);
        result.addPointDouble(x + width_x, y + width_y);
        result.addPointDouble(x + width_x + height_x, y + width_y + height_y);
        result.addPointDouble(x + height_x, y + height_y);
        result.addPointDouble(x, y);
        return result;
    }

    public int spatialRelation(GeometricObject object, double scale)
    {
        int origin_index = 0;
        int diagonal_index = 0;
        int diagonal_x = 0;
        int diagonal_y = 0;
        int origin_x = 0;
        int origin_y = 0;
        int horizontal_vector_x = 0;
        int horizontal_vector_y = 0;
        int vertical_vector_x = 0;
        int vertical_vector_y = 0;
        Rectangle copy = new Rectangle(this);
        copy.scaleAboutCenter(scale);
        PolygonShape bounds = copy.getPolygonalBounds();
        origin_x = bounds.xpoints[0];
        origin_y = bounds.ypoints[0];
        diagonal_x = bounds.xpoints[0];
        diagonal_y = bounds.ypoints[0];
        for(int i = 1; i < bounds.npoints; i++)
        {
            if(bounds.ypoints[i] <= origin_y && (bounds.ypoints[i] != origin_y || bounds.xpoints[i] <= origin_x))
            {
                origin_x = bounds.xpoints[i];
                origin_y = bounds.ypoints[i];
                origin_index = i;
            }
            if(bounds.ypoints[i] >= diagonal_y && (bounds.ypoints[i] != diagonal_y || bounds.xpoints[i] >= diagonal_x))
            {
                diagonal_x = bounds.xpoints[i];
                diagonal_y = bounds.ypoints[i];
                diagonal_index = i;
            }
        }

label0:
        for(int i = 0; i < bounds.npoints; i++)
        {
            int j = 0;
            do
            {
                if(j >= bounds.npoints)
                    continue label0;
                if(i != origin_index && i != diagonal_index && j != origin_index && j != diagonal_index && i != j)
                {
                    double angle_i = Math.atan2(Math.abs(bounds.ypoints[i] - origin_y), Math.abs(bounds.xpoints[i] - origin_x));
                    double angle_j = Math.atan2(Math.abs(bounds.ypoints[j] - origin_y), Math.abs(bounds.xpoints[j] - origin_x));
					if (logger.isDebugEnabled()) {
						//  logger.debug("spatialRelation(GeometricObject, double) - " + (new StringBuilder()).append("i ").append(i).append(" j ").append(j).append(" ").append("angle_i ").append(GeometryUtil.radian2degree(angle_i)).append(" ").append("angle_j ").append(GeometryUtil.radian2degree(angle_j)).toString()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
					}
                    if(angle_i > angle_j)
                    {
                        vertical_vector_x = bounds.xpoints[i];
                        vertical_vector_y = bounds.ypoints[i];
                        horizontal_vector_x = bounds.xpoints[j];
                        horizontal_vector_y = bounds.ypoints[j];
                        i = bounds.npoints;
                    } else
                    {
                        vertical_vector_x = bounds.xpoints[j];
                        vertical_vector_y = bounds.ypoints[j];
                        horizontal_vector_x = bounds.xpoints[i];
                        horizontal_vector_y = bounds.ypoints[i];
                        i = bounds.npoints;
                    }
                    continue label0;
                }
                j++;
            } while(true);
        }

        diagonal_x -= origin_x;
        diagonal_y -= origin_y;
        vertical_vector_x -= origin_x;
        vertical_vector_y -= origin_y;
        horizontal_vector_x -= origin_x;
        horizontal_vector_y -= origin_y;
//        if(Tablet.debug)
//        {
//            logger.info((new StringBuilder()).append("origin_index       = ").append(origin_index).toString());
//            logger.info((new StringBuilder()).append("diagonal_index     = ").append(diagonal_index).toString());
//            logger.info((new StringBuilder()).append("origin_x           = ").append(origin_x).toString());
//            logger.info((new StringBuilder()).append("origin_y           = ").append(origin_y).toString());
//            logger.info((new StringBuilder()).append("diagonal_x         = ").append(diagonal_x).toString());
//            logger.info((new StringBuilder()).append("diagonal_y         = ").append(diagonal_y).toString());
//            logger.info((new StringBuilder()).append("vertical_vector_x  = ").append(vertical_vector_x).toString());
//            logger.info((new StringBuilder()).append("vertical_vector_y  = ").append(vertical_vector_y).toString());
//            logger.info((new StringBuilder()).append("horizontal_vector_x= ").append(horizontal_vector_x).toString());
//            logger.info((new StringBuilder()).append("horizontal_vector_y= ").append(horizontal_vector_y).toString());
//        }
        Line horizontal_up = new Line(origin_x - horizontal_vector_x, origin_y - horizontal_vector_y, origin_x + horizontal_vector_x * 2, origin_y + horizontal_vector_y * 2);
        Line horizontal_down = new Line((origin_x - horizontal_vector_x) + vertical_vector_x, (origin_y - horizontal_vector_y) + vertical_vector_y, origin_x + horizontal_vector_x * 2 + vertical_vector_x, origin_y + horizontal_vector_y * 2 + vertical_vector_y);
        Line left = new Line(origin_x - vertical_vector_x, origin_y - vertical_vector_y, origin_x + vertical_vector_x * 2, origin_y + vertical_vector_y * 2);
        Line right = new Line((origin_x - vertical_vector_x) + horizontal_vector_x, (origin_y - vertical_vector_y) + horizontal_vector_y, origin_x + vertical_vector_x * 2 + horizontal_vector_x, origin_y + vertical_vector_y * 2 + horizontal_vector_y);
        if(left.x1 > right.x1)
        {
            Line tmp = left;
            left = right;
            right = tmp;
        }
        horizontal_up.pointRight();
        horizontal_down.pointRight();
        right.pointUp();
        left.pointUp();
        Graphics g =this.graphics;
        g.setColor(Color.red);
        horizontal_up.paint((Graphics2D)g);
        g.setColor(Color.green);
        horizontal_down.paint((Graphics2D)g);
        g.setColor(Color.blue);
        left.paint((Graphics2D) g);
        g.setColor(Color.orange);
        right.paint((Graphics2D)g);
        int relation = -1;
       PointData center = object.getRectangularBounds().getCenter();
		if (logger.isDebugEnabled()) {
			//  logger.debug("spatialRelation(GeometricObject, double) - " + center); //$NON-NLS-1$
		}
        g.setColor(Color.cyan);
        center.paint(g);
        int direction1 = horizontal_up.relativeCCW(center.x, center.y);
        int direction2 = horizontal_down.relativeCCW(center.x, center.y);
        int direction3 = left.relativeCCW(center.x, center.y);
        int direction4 = right.relativeCCW(center.x, center.y);
		if (logger.isDebugEnabled()) {
			//  logger.debug("spatialRelation(GeometricObject, double) - " + (new StringBuilder()).append("direction1 ").append(direction1).toString()); //$NON-NLS-1$ //$NON-NLS-2$
		}
		if (logger.isDebugEnabled()) {
			//  logger.debug("spatialRelation(GeometricObject, double) - " + (new StringBuilder()).append("direction2 ").append(direction2).toString()); //$NON-NLS-1$ //$NON-NLS-2$
		}
		if (logger.isDebugEnabled()) {
			//  logger.debug("spatialRelation(GeometricObject, double) - " + (new StringBuilder()).append("direction3 ").append(direction3).toString()); //$NON-NLS-1$ //$NON-NLS-2$
		}
		if (logger.isDebugEnabled()) {
			//  logger.debug("spatialRelation(GeometricObject, double) - " + (new StringBuilder()).append("direction4 ").append(direction4).toString()); //$NON-NLS-1$ //$NON-NLS-2$
		}
        if(direction1 == 0)
            direction1 = 1;
        if(direction2 == 0)
            direction3 = 1;
        if(direction3 == 0)
            direction3 = 1;
        if(direction4 == 0)
            direction4 = 1;
        switch(direction1)
        {
        case 1: // '\001'
            switch(direction3)
            {
            case -1: 
                return 0;

            case 1: // '\001'
                switch(direction4)
                {
                case 1: // '\001'
                    return 2;

                case -1: 
                    return 1;
                }
                return -1;
            }
            // fall through

        case -1: 
            switch(direction2)
            {
            case 1: // '\001'
                switch(direction3)
                {
                case -1: 
                    return 3;

                case 1: // '\001'
                    switch(direction4)
                    {
                    case -1: 
                        return 4;

                    case 1: // '\001'
                        return 5;
                    }
                    return -1;
                }
                // fall through

            case -1: 
                switch(direction3)
                {
                case -1: 
                    return 6;

                case 1: // '\001'
                    switch(direction4)
                    {
                    case -1: 
                        return 7;

                    case 1: // '\001'
                        return 8;
                    }
                    return -1;
                }
                return -1;

            default:
                return 0;
            }

        default:
            return -1;
        }
    }

    public int spatialRelation(GeometricObject object)
    {
        return spatialRelation(object, 1.0D);
    }

    public boolean hasOnMajorAxisDirection(GeometricObject object)
    {
        int direction = getMajorAxisDirection();
        int spatial_relation = spatialRelation(object);
		if (logger.isDebugEnabled()) {
			//  logger.debug("hasOnMajorAxisDirection(GeometricObject) - " + (new StringBuilder()).append("Direction ").append(Direction.toString(direction)).toString()); //$NON-NLS-1$ //$NON-NLS-2$
		}
		if (logger.isDebugEnabled()) {
			//  logger.debug("hasOnMajorAxisDirection(GeometricObject) - " + (new StringBuilder()).append("Relation  ").append(SpatialRelation.toString(spatial_relation)).toString()); //$NON-NLS-1$ //$NON-NLS-2$
		}
        switch(direction)
        {
        case 0: // '\0'
            return spatial_relation == 3 || spatial_relation == 5;

        case 1: // '\001'
            return spatial_relation == 1 || spatial_relation == 7;
        }
        return false;
    }

    public int spatialRelationCartesian(GeometricObject object)
    {
        return SpatialRelation.screen2cartesian(spatialRelation(object, 1.0D));
    }

    public double getWidth()
    {
        return width;
    }

    public double getHeight()
    {
        return height;
    }

    public double getWidthX()
    {
        return width_x;
    }

    public double getWidthY()
    {
        return width_y;
    }

    public double getAngle()
    {
        return Math.atan2(width_y, width_x);
    }

    public double getMajorAxisLength()
    {
        return width <= height ? height : width;
    }

    public double getMinorAxisLength()
    {
        return width <= height ? width : height;
    }

    public void rotateAbout(PointData p, double theta)
    {
        translate(-p.x, -p.y);
        double oldX = x;
        double oldY = y;
        double oldWX = width_x;
        double oldWY = width_y;
        double oldHX = height_x;
        double oldHY = height_y;
        x = oldX * Math.cos(theta) - oldY * Math.sin(theta);
        y = oldX * Math.sin(theta) + oldY * Math.cos(theta);
        width_x = oldWX * Math.cos(theta) - oldWY * Math.sin(theta);
        width_y = oldWX * Math.sin(theta) + oldWY * Math.cos(theta);
        height_x = oldHX * Math.cos(theta) - oldHY * Math.sin(theta);
        height_y = oldHX * Math.sin(theta) + oldHY * Math.cos(theta);
        translate(p.x, p.y);
    }

    public PointData  getCenter()
    {
        return new PointData(x + (width_x + height_x) / 2D, y + (width_y + height_y) / 2D);
    }

    public static boolean isRectangle(PolygonShape p)
    {
        if(p.npoints != 5)
            return false;
        Rectangle bounds = p.getRectangularBounds();
       PointData center = bounds.getCenter();
        double half_diagonal = PointData.distance(0.0D, 0.0D, bounds.width, bounds.height) / 2D;
        for(int i = 0; i < 4; i++)
        {
            double distance = center.distance(p.xpoints[i], p.ypoints[i]);
            if(!GeometryUtil.equalDoubles(half_diagonal, distance, half_diagonal * 0.25D))
                return false;
        }

        return true;
    }

    public void setTimeStamp(long time_stamp)
    {
        this.time_stamp = time_stamp;
    }

    public long getTimeStamp()
    {
        return time_stamp;
    }

    public double getMinorAxisAngle()
    {
        if(width < height)
            return Math.atan2(width_y, width_x);
        else
            return Math.atan2(height_y, height_x);
    }

    public double getMajorAxisAngle()
    {
        if(width > height)
            return Math.atan2(width_y, width_x);
        else
            return Math.atan2(height_y, height_x);
    }

    public int getMajorAxisDirection()
    {
        if(width >= height)
            return Math.abs(width_x) <= Math.abs(width_y) ? 1 : 0;
        return Math.abs(height_x) <= Math.abs(height_y) ? 1 : 0;
    }

    public void setDataPoints(PolygonShape points)
    {
        this.points = points;
    }

    public void setOriginalVertices(Vertex pts[])
    {
        setDataPoints(new PolygonShape(pts));
        m_vertices = new Vertex[pts.length];
        for(int i = 0; i < pts.length; i++)
            m_vertices[i] = pts[i];

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

    public PolygonShape getDataPoints()
    {
        return points;
    }

    public double area()
    {
        return Math.abs(width * height);
    }

    public Rectangle union(Rectangle rectangle)
    {
        PolygonShape polygonal_bounds1 = getPolygonalBounds();
        PolygonShape polygonal_bounds2 = rectangle.getPolygonalBounds();
        for(int i = 0; i < polygonal_bounds1.npoints; i++)
            polygonal_bounds2.addPoint(polygonal_bounds1.xpoints[i], polygonal_bounds1.ypoints[i]);

        return polygonal_bounds2.getRectangularBounds();
    }

    public void translate(double x, double y)
    {
        this.x += x;
        this.y += y;
        if(points != null)
            points.translate(x, y);
    }

    public boolean containsGeometricObjects(GeometricObject objects[])
    {
        for(int i = 0; i < objects.length; i++)
            if(!containsGeometricObject(objects[i]))
                return false;

        return true;
    }

    public final GeometricObject copy()
    {
        Rectangle rectangle = new Rectangle(this);
        if(points != null)
            rectangle.points = (PolygonShape)points.copy();
        rectangle.time_stamp = time_stamp;
        return rectangle;
    }

    public PointData[] getCorners()
    {
       PointData corners[] = new  PointData[4];
        corners[0] = new PointData((int)x, (int)y);
        corners[1] = new  PointData((int)(x + width_x), (int)(y + width_y));
        corners[2] = new  PointData((int)(x + width_x + height_x), (int)(y + width_y + height_y));
        corners[3] = new  PointData((int)(x + height_x), (int)(y + height_y));
        return corners;
    }

    public int getIntType()
    {
        return 0;
    }

	public long getTime() {
		  
		return time_stamp;
	}

	public void setTime(long l) {
		  
		time_stamp=l;
	}

	public void paint(Graphics2D g) {
		  
		
	}

}
