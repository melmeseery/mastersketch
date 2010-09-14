// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Vertex.java

package SketchMaster.Stroke.graphics.shapes;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.*;

import SketchMaster.Stroke.StrokeData.PointData;

// Referenced classes of package edu.mit.sketch.geom:
//            Point, Polygon, GeometricObject

public class Vertex extends PointData
    implements Serializable, Comparator
{
    public Vertex()
    {
        certainty = 0.0D;
    }

    public Vertex(Point point)
    {
        super(point);
    }

    public Vertex(Point2D point)
    {
        super(point);
        if(point instanceof Vertex)
        {
            index = ((Vertex)point).index;
            certainty = ((Vertex)point).certainty;
            time= ((Vertex)point).getTime();
        } else
        if(point instanceof PointData)
            time = ((PointData)point).getTime();
        
        else
            certainty = 0.0D;
    }

    public Vertex(double x, double y)
    {
        super(x, y);
        certainty = 0.0D;
    }

    public Vertex(double certainty)
    {
        this.certainty = certainty;
    }

    public Vertex(Point point, double certainty)
    {
        super(point);
        this.certainty = certainty;
    }

    public Vertex(Point2D point, double certainty)
    {
        super(point);
        this.certainty = certainty;
        if(point instanceof Vertex)
            index = ((Vertex)point).index;
    }

    public Vertex(double x, double y, double certainty)
    {
        super(x, y);
        this.certainty = certainty;
    }

    public Vertex(Vertex vertex)
    {
        this(vertex.x, vertex.y, vertex.certainty);
        index = vertex.index;
        time = vertex.getTime();
    }

    public void setIndex(int index)
    {
        this.index = index;
    }

    public String toString()
    {
        return (new StringBuilder()).append("Vertex ( ").append(x).append(", ").append(y).append(" ) c = ").append((new StringBuilder()).append(certainty).append("              ").toString().substring(0, 5)).append(", i = ").append(index).append(" t = ").append(time).toString();
    }

    public int compare(Object o1, Object o2)
    {
        if(((Vertex)o1).certainty - ((Vertex)o2).certainty < 0.0D)
            return -1;
		int returnint = ((Vertex) o1).certainty - ((Vertex) o2).certainty <= 0.0D ? 0 : 1;
        return returnint;
    }

    public boolean equals(Object o1, Object o2)
    {
		boolean returnboolean = ((Vertex) o1).certainty == ((Vertex) o2).certainty;
        return returnboolean;
    }

    public static Vertex[] removeDuplicateVertices(Vertex vertices[])
    {
   //     Vertex copy[] = new Vertex[vertices.length];
        ArrayList list = new ArrayList();
        for(int i = 0; i < vertices.length - 1; i++)
            if(vertices[i].x != vertices[i + 1].x || vertices[i].y != vertices[i + 1].y)
                list.add(vertices[i]);

        list.add(vertices[vertices.length - 1]);
		Vertex[] returnVertexArray = arrayListToArray(list);
        return returnVertexArray;
    }

    public static Vertex[] cloneVertices(Vertex vertices[])
    {
        Vertex copy[] = new Vertex[vertices.length];
        for(int i = 0; i < vertices.length; i++)
        {
            copy[i] = new Vertex(vertices[i]);
            copy[i].setTime(vertices[i].getTime());
        }
        return copy;
    }

    public static Vertex[] appendVertices(Vertex vertices[], Vertex vertex)
    {
        Vertex result[] = new Vertex[vertices.length + 1];
        for(int i = 0; i < vertices.length; i++)
            result[i] = vertices[i];

        result[result.length - 1] = vertex;
        return result;
    }

    public static Vertex[] arrayListToArray(ArrayList vertices)
    {
        Vertex result[] = new Vertex[vertices.size()];
        for(int i = 0; i < result.length; i++)
            result[i] = (Vertex)(Vertex)vertices.get(i);
        return result;
    }

    public static Vertex[] vectorToArray(Vector vertices)
    {
        Vertex result[] = new Vertex[vertices.size()];
        for(int i = 0; i < result.length; i++)
            result[i] = (Vertex)(Vertex)vertices.elementAt(i);
        return result;
    }

//    public final GeometricObject copy()
//    {
//        Vertex vertex = new Vertex(this);
//        if(getDataPoints() != null)
//            vertex.setDataPoints((Polygon)getDataPoints().copy());
//        return vertex;
//    }

    public int index;
    public double certainty;
    static final long serialVersionUID = 0x81f2432e05b47975L;
}
