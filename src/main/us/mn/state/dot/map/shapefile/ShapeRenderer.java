/*
 * IRIS -- Intelligent Roadway Information System
 * Copyright (C) 2000  Minnesota Department of Transportation
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
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package us.mn.state.dot.shape.shapefile;

import java.awt.*;
import java.awt.geom.*;
import us.mn.state.dot.shape.LayerRenderer;
import us.mn.state.dot.shape.MapTip;
import us.mn.state.dot.shape.Symbol;
import us.mn.state.dot.shape.MapObject;


/**
 * Base class for all renderers used for shape files.
 *
 * @author <a href="mailto:erik.engstrom@dot.state.mn.us">Erik Engstrom</a>
 * @version $Revision: 1.3 $ $Date: 2001/08/15 16:08:51 $ 
 */
public abstract class ShapeRenderer implements LayerRenderer {
    
	/** MapTip to display. */
	MapTip mapTip = null;
	
	/** Symbol to paint with */
	Symbol symbol = null;
	
	/** Name of renderer */
	private String name = "";
	
	/**
	 * Set the field for this renderer.
	 */
    public abstract void setField( String field );
	
	/**
	 * Get the field used by this renderer.
	 */
    public abstract String getField();
	
	/**
	 * Set the MapTip used by this renderer.
	 */
    public void setTip( MapTip m ) {
        mapTip = m;
    }

	/**
	 * Get the symbol used by this renderer.
	 */
    public Symbol getSymbol(){
        return symbol;
    }

	/**
	 * Set the symbol used by this renderer.
	 */
    public void setSymbol( Symbol s ){
        symbol = s;
    }

	/**
	 * Get the Tip for the given map object.
	 * @param layer, Layer that contains the map object.
	 * @param i, index of the object.
	 */
    public String getTip( MapObject object ){
        String result = null;
        if ( mapTip != null ){
            result = mapTip.getTip( object );
        }
    	return result;
    }
	
	/**
	 * Get the Symbols used by this renderer.
	 */
	public Symbol[] getSymbols() {
		Symbol[] result = { symbol };
		return result;
	}
	
	/**
	 * Set the name of this renderer.
	 */
	public void setName( String s ) {
		name = s;
	}
	
	/** 
	 * Overrides Object.toString() 
	 */
	public final String toString(){
		String result = null;
		if ( name == null ) {
			result = super.toString();
		} else {
			result = name;
		}
		return result;
	}
	
	public boolean isVisible( int index ) {
		return true;
	}
	
}