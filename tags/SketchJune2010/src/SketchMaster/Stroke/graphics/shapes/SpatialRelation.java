// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SpatialRelation.java

package SketchMaster.Stroke.graphics.shapes;
public class SpatialRelation
{
    public SpatialRelation()
    {
    }

    public static String toString(int relation)
    {
        switch(relation)
        {
        case 0: // '\0'
            return "upper_left";

        case 1: // '\001'
            return "above";

        case 2: // '\002'
            return "upper_right";

        case 3: // '\003'
            return "left";

        case 4: // '\004'
            return "cocentric";

        case 5: // '\005'
            return "right";

        case 6: // '\006'
            return "lower_left";

        case 7: // '\007'
            return "below";

        case 8: // '\b'
            return "lower_right";
        }
        return "unknown relation";
    }

    public static int cartesian2screen(int relation)
    {
        switch(relation)
        {
        case 0: // '\0'
            return 6;

        case 1: // '\001'
            return 7;

        case 2: // '\002'
            return 8;

        case 3: // '\003'
            return 3;

        case 4: // '\004'
            return 4;

        case 5: // '\005'
            return 5;

        case 6: // '\006'
            return 0;

        case 7: // '\007'
            return 1;

        case 8: // '\b'
            return 2;
        }
        return relation;
    }

    public static int screen2cartesian(int relation)
    {
        switch(relation)
        {
        case 0: // '\0'
            return 6;

        case 1: // '\001'
            return 7;

        case 2: // '\002'
            return 8;

        case 3: // '\003'
            return 3;

        case 4: // '\004'
            return 4;

        case 5: // '\005'
            return 5;

        case 6: // '\006'
            return 0;

        case 7: // '\007'
            return 1;

        case 8: // '\b'
            return 2;
        }
        return relation;
    }

    public static int getComplement(int relation)
    {
        switch(relation)
        {
        case 0: // '\0'
            return 8;

        case 1: // '\001'
            return 7;

        case 2: // '\002'
            return 6;

        case 3: // '\003'
            return 5;

        case 4: // '\004'
            return 4;

        case 5: // '\005'
            return 3;

        case 6: // '\006'
            return 2;

        case 7: // '\007'
            return 1;

        case 8: // '\b'
            return 0;
        }
        return relation;
    }

    public static final int UPPER_LEFT = 0;
    public static final int ABOVE = 1;
    public static final int UPPER_RIGHT = 2;
    public static final int LEFT = 3;
    public static final int COCENTRIC = 4;
    public static final int RIGHT = 5;
    public static final int LOWER_LEFT = 6;
    public static final int BELOW = 7;
    public static final int LOWER_RIGHT = 8;
}
