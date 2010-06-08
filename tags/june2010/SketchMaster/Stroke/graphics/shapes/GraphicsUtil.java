// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   GraphicsUtil.java

package SketchMaster.Stroke.graphics.shapes;
import java.awt.*;

public class GraphicsUtil
{
    public GraphicsUtil()
    {
    }

    public static void drawThickLine(int r, Graphics g, int x1, int y1, int x2, int y2)
    {
        if(r == 1)
        {
            g.drawLine(x1, y1, x2, y2);
            return;
        }
        int iterations = (int)Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
        for(int i = 0; i < iterations; i++)
            g.fillOval((x1 * i + x2 * (iterations - i)) / iterations, (y1 * i + y2 * (iterations - i)) / iterations, r, r);
    }

    public static void drawThickLine(double r, Graphics g, double x1, double y1, double x2, double y2)
    {
        if(r == 1.0D)
        {
            g.drawLine((int)x1, (int)y1, (int)x2, (int)y2);
            return;
        }
        int iterations = (int)Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
        for(int i = 0; i < iterations; i++)
            g.fillOval((int)((x1 * (double)i + x2 * (double)(iterations - i)) / (double)iterations), (int)((y1 * (double)i + y2 * (double)(iterations - i)) / (double)iterations), (int)r, (int)r);
    }

    public static void drawThickOval(int r, Graphics g, int x, int y, int r1, int r2)
    {
        if(r == 1)
        {
            g.drawOval(x, y, r1, r2);
            return;
        }
        for(int i = 0; i < r; i++)
            g.drawOval(x + i, y + i, r1 - i * 2, r2 - i * 2);
    }

    public static void setConstraints(GridBagLayout layout, Component component, int x, int y, int column_span, int row_span, double weightx, 
            double weighty, int fill, int anchor, Insets insets)
    {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = column_span;
        gbc.gridheight = row_span;
        gbc.weightx = weightx;
        gbc.weighty = weighty;
        gbc.fill = fill;
        gbc.anchor = anchor;
        gbc.insets = insets;
        layout.setConstraints(component, gbc);
    }

    public static void clearComponent(Component component)
    {
        Graphics g = component.getGraphics();
        g.setColor(Color.white);
        g.fillRect(0, 0, component.getWidth(), component.getHeight());
    }
}
