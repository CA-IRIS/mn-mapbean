/*
 * IRIS -- Intelligent Roadway Information System
 * Copyright (C) 2007-2016  Minnesota Department of Transportation
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */
package us.mn.state.dot.map;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import javax.swing.Icon;

/**
 * A vector symbol is draws a Shape with a specific Style.
 *
 * @author Douglas Lau
 */
public class VectorSymbol implements Symbol {

	/** Transparent white */
	static private final Color TRANS_WHITE = new Color(1, 1, 1, 0.4f);

	/** Transparent white */
	static private final Color TRANSPARENT = new Color(1, 1, 1, 0.75f);

	/** Create an ellipse around the given shape */
	static private Shape createEllipse(Shape s) {
		Rectangle2D r = s.getBounds2D();
		return new Ellipse2D.Double(
			r.getCenterX() - r.getWidth(),
			r.getCenterY() - r.getHeight(),
			r.getWidth() * 2,
			r.getHeight() * 2
		);
	}

	/** Shape to draw legend */
	private final Shape lshape;

	/** Size of legend icon */
	private final int lsize;

	/** Create a new vector symbol */
	public VectorSymbol(Shape shp, int sz) {
		lshape = shp;
		lsize = sz;
	}

	/** Draw the symbol */
	@Override
	public void draw(Graphics2D g, MapObject mo, float scale, Style sty) {
		if (sty != null) {
			AffineTransform trans = mo.getTransform();
			if (trans != null)
				g.transform(trans);
			draw(g, mo.getShape(), mo.getOutlineShape(), scale,sty);
		}
	}

	/** Draw the symbol */
	private void draw(Graphics2D g, Shape shp, Shape o_shp, float scale,
		Style sty)
	{
		if (shp != null && sty.fill_color != null) {
			g.setColor(sty.fill_color);
			g.fill(shp);
		}
		if (o_shp != null && sty.outline != null) {
			g.setColor(sty.outline.color);
			g.setStroke(sty.outline.getStroke(scale));
			g.draw(o_shp);
		}
	}

	/** Draw a selected symbol */
	@Override
	public void drawSelected(Graphics2D g, MapObject mo, float scale,
		Style sty)
	{
		if (sty != null) {
			Shape shp = mo.getShape();
			if (shp != null)
				drawSelected(g, mo, shp, scale, sty);
		}
	}

	/** Draw a selected symbol */
	private void drawSelected(Graphics2D g, MapObject mo, Shape shp,
		float scale, Style sty)
	{
		g.transform(mo.getTransform());
		g.setColor(TRANS_WHITE);
		g.fill(shp);
		Outline outline = Outline.createSolid(TRANSPARENT, 4);
		g.setColor(TRANSPARENT);
		g.setStroke(outline.getStroke(scale));
		g.draw(createEllipse(shp));
	}

	/** Get the legend icon */
	@Override
	public Icon getLegend(Style sty) {
		return new LegendIcon(sty);
	}

	/** Inner class for icon displayed on the legend */
	private class LegendIcon implements Icon {

		/** Legend style */
		private final Style sty;

		/** Transform to draw the legend */
		private final AffineTransform transform;

		/** Create a new legend icon */
		protected LegendIcon(Style s) {
			sty = s;
			Rectangle2D b = lshape.getBounds2D();
			double x = b.getX() + b.getWidth() / 2;
			double y = b.getY() + b.getHeight() / 2;
			double scale = (lsize - 2) /
				Math.max(b.getWidth(), b.getHeight());
			transform = new AffineTransform();
			transform.translate(lsize / 2, lsize / 2);
			transform.scale(scale, -scale);
			transform.translate(-x, -y);
		}	

		/** Paint the icon onto the given component */
		@Override
		public void paintIcon(Component c, Graphics g, int x, int y) {
			Graphics2D g2 = (Graphics2D) g;
			AffineTransform t = g2.getTransform();
			g2.translate(x, y);
			g2.transform(transform);
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
			draw(g2, lshape, lshape, 1, sty);
			g2.setTransform(t);
		}

		/** Get the icon width */
		@Override
		public int getIconWidth() {
			return lsize;
		}

		/** Get the icon height */
		@Override
		public int getIconHeight() {
			return lsize;
		}
	}
}
