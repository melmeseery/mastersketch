// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Translatable.java

package SketchMaster.Stroke.graphics.shapes;

import SketchMaster.Stroke.StrokeData.PointData;


// Referenced classes of package edu.mit.sketch.geom:
//            Point

public interface Translatable
{

    public abstract void translate(double d, double d1);

    public abstract boolean pointIsOn(PointData point, double d);

    public abstract boolean pointIsOnOriginal(PointData point, double d);
}
