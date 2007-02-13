/*
 * IRIS -- Intelligent Roadway Information System
 * Copyright (C) 2000-2007  Minnesota Department of Transportation
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

import javax.swing.JLabel;
import javax.swing.JMenu;

/**
 * JMenu for displaying the Legend for a LayerRenderer.
 *
 * @author Erik Engstrom
 * @author Douglas Lau
 */
public class LegendMenu extends JMenu {

  	/** Create a new LegendMenu */
 	public LegendMenu(String name, LayerRenderer r) {
		super(name);
		setMapRenderer(r);
	}

	/** Set the LayerRenderer that this menu displays */
	public void setMapRenderer(LayerRenderer r) {
		removeAll();
		for(Symbol s: r.getSymbols()) {
			JLabel l = new JLabel(s.getLabel());
			l.setIcon(s.getLegend());
			add(l);
		}
	}
}