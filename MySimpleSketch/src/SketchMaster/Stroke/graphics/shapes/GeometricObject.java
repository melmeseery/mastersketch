// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   GeometricObject.java

package SketchMaster.Stroke.graphics.shapes;




// Referenced classes of package edu.mit.sketch.geom:
//            Translatable, Polygon, Rectangle, Vertex

public interface GeometricObject
    extends Terminal, Translatable, GuiShape
{

    public abstract int getIntType();

    public abstract PolygonShape getPolygonalBounds();

    public abstract boolean touches(GeometricObject geometricobject);

    public abstract int spatialRelation(GeometricObject geometricobject);

    public abstract  SketchMaster.Stroke.graphics.shapes.Rectangle getRectangularBounds();

    public abstract java.awt.Rectangle getBounds();

    public abstract boolean containsGeometricObject(GeometricObject geometricobject);

    public abstract boolean containsGeometricObjects(GeometricObject ageometricobject[]);

  //  public abstract void setDataPoints(Polygon polygon);

   // public abstract Polygon getDataPoints();

 //   public abstract Vertex[] getOriginalVertices();

 //   public abstract void setOriginalVertices(Vertex avertex[]);

    public abstract GeometricObject copy();
}
