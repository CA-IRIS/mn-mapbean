
/**
  * ShapeLayer is a class for drawing ESRI shape files.
  *
  * @author Douglas Lau
  */

package us.mn.state.dot.shape;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import java.io.*;
import us.mn.state.dot.shape.DbaseReader.*;

public class ShapeLayer implements Layer {
	private Vector layerListeners = new Vector();

	private boolean visible = true;
	private String name = null;

	public String getName(){
		return name;
	}

	/** Bounding box */
	private Rectangle2D.Double extent;

	public Rectangle2D getExtent(){
		return extent;
	}

	private DbaseReader dbFile;

	/** Set symbol to paint layer with */
	public void setSymbol(Symbol s){
		painter.setSymbol(s);
	}

	/** Renderer for this layer */
	private ShapeRenderer painter = null;

	/** Set the Renderer for the layer */
	public void setRenderer( ShapeRenderer p ) {
		painter = p;
	}

	public ShapeRenderer getRenderer(){
		return painter;
	}

	/** Array to hold all shape information */
	protected final GeneralPath [] paths;

	public Field [] getFields(){
		return dbFile.getFields();
	}

	public Field getField(String name){
		Field [] fields = dbFile.getFields();
		Field result = null;
		for (int i = 0; i < fields.length; i++) {
			if ((fields[i].getName()).equalsIgnoreCase(name)) {
				result = fields[i];
				break;
			}
		}
		return result;
	}

	public ShapeLayer(String fileName, String layerName) throws IOException {
		name = layerName;
		dbFile = new DbaseReader( fileName + ".dbf" );
		ShapeFile file = new ShapeFile( fileName + ".shp" );
		ArrayList list = file.getShapeList();
		paths = new GeneralPath [ list.size() ];
		for ( int i = 0; i < list.size(); i++ ) {
			GeneralPath path = new GeneralPath();
			path.append( (PathIterator)list.get( i ), false );
			paths[ i ] = path;
		}
		painter = new DefaultRenderer();
		extent = new Rectangle2D.Double(file.getXmin(), file.getYmin(),
			(file.getXmax() - file.getXmin()),
			(file.getYmax() - file.getYmin()));
		switch( file.getShapeType() ) {
		case ShapeTypes.POINT:
			painter.setSymbol(new PointSymbol());
			break;
		case ShapeTypes.POLYLINE:
			painter.setSymbol(new LineSymbol());
			break;
		case ShapeTypes.POLYGON:
			painter.setSymbol(new FillSymbol());
			break;
		}
	}

	/** Create a new Layer from a ShapeFile */
	public ShapeLayer( String fileName ) throws IOException {
		 this(fileName, fileName);
	}

	/** Paint this Layer */
	public void paint( Graphics2D g ) {
		if (visible) {
			for (int i = 0; i < paths.length; i++) {
				painter.paint(g, paths[i], i);
			}
		}
	}

	public Vector hit(Point2D p){
		Vector result = new Vector();
		for ( int i = 0; i < paths.length; i++ ) {
			Rectangle2D r = paths[i].getBounds2D();
			if (r.contains(p)) {
				result.add(paths[i]);
			}
		}
		return result;
	}

	public void setVisible(boolean b) {
		visible = b;
		repaintLayer(this);
	}

	public boolean isVisible() {
		return visible;
	}

	public String toString(){
		return name;
	}

	public boolean writeToFile(){
		return true;
	}

	public String getTip(Point2D p){
		String result = null;
		Rectangle2D searchZone = new Rectangle2D.Double((p.getX() - 250),
			(p.getY() - 250), 500, 500);
		for ( int i = 0; i < paths.length; i++ ) {
			Rectangle2D r = paths[i].getBounds2D();
			if ((r.getWidth() == 0) | (r.getHeight() == 0)) {
				Point2D q = paths[i].getCurrentPoint();
				if (searchZone.contains(q)) {
					result = painter.getTip(this, i);
					break;
				}
			} else {
				if (searchZone.contains(r) | r.contains(searchZone) |
						r.intersects(searchZone)) {
					result = painter.getTip(this, i);
					break;
				}
			}
		}
		return result;
	}

	public void addLayerListener(LayerListener l) {
		if (!layerListeners.contains(l)) {
			layerListeners.add(l);
		}
	}

	public void updateLayer(Layer l) {
		ListIterator it = layerListeners.listIterator();
		while (it.hasNext()){
			((LayerListener) it.next()).updateLayer(l);
		}
	}

	public void repaintLayer(Layer l) {
		ListIterator it = layerListeners.listIterator();
		while (it.hasNext()){
			((LayerListener) it.next()).repaintLayer(l);
		}
	}
}
