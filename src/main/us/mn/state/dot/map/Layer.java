
//Title:        SignClient
//Version:
//Copyright:    Copyright (c) 1999
//Author:       Erik Engstrom
//Company:      MnDOT
//Description:  Your description

package us.mn.state.dot.shape;

import java.awt.*;
import java.util.Vector;
import java.awt.geom.*;

public interface Layer {
    public Rectangle2D getExtent();
    public void paint(Graphics2D g);
    public Vector hit(Point2D p);
    public String getTip(Point2D p);
    public boolean isVisible();
    public void setVisible(boolean b);
    public String getName();
    public void addLayerListener(LayerListener l);
    public void updateLayer(Layer l);
    public void repaintLayer(Layer l);
}