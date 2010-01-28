// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Direction.java

package SketchMaster.Stroke.graphics.shapes;
class Direction
{
    Direction()
    {
    }

    public static String toString(int relation)
    {
        switch(relation)
        {
        case 0: // '\0'
            return "left_right";

        case 1: // '\001'
            return "above_below";
        }
        return "unknown relation";
    }

    public static final int LEFT_RIGHT = 0;
    public static final int ABOVE_BELOW = 1;
}
