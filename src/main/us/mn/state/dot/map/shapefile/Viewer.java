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

package us.mn.state.dot.shape;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import us.mn.state.dot.shape.shapefile.ShapeFileFilter;
import us.mn.state.dot.shape.shapefile.ShapeLayer;

/**
 *
 * @author <a href="mailto:erik.engstrom@dot.state.mn.us">Erik Engstrom</a>
 * @version $Revision: 1.4 $ $Date: 2003/05/07 15:28:57 $ 
 */
public class Viewer extends JFrame {

	private final MapBean map = new MapBean();
		
	/** Creates new Viewer */
    public Viewer() {
		super( "Shapefile Viewer" );
		this.setJMenuBar( buildMenus() );
		this.getContentPane().setLayout( new BorderLayout() );
		this.getContentPane().add( map, BorderLayout.CENTER );
		NavigationBar toolbar = new NavigationBar( map );
		this.getContentPane().add( toolbar, BorderLayout.NORTH );
    }
	
	private JMenuBar buildMenus() {
		JMenuBar menuBar = new JMenuBar();
		menuBar.add( createFileMenu() );
		menuBar.add( createViewMenu() );
		return menuBar;
	}
	
	private JMenu createFileMenu(){
		JMenu file = new JMenu( "File" );
		JMenuItem newItem = new JMenuItem( "New" );
		newItem.setEnabled( false );
		file.add( newItem );
		JMenuItem openItem = new JMenuItem( "Open" );
		openItem.setEnabled( false );
		file.add( openItem );
		JMenuItem saveItem = new JMenuItem( "Save" );
		saveItem.setEnabled( false );
		file.add( saveItem );
		file.addSeparator();
		JMenuItem exitItem = new JMenuItem( "Exit" );
		file.add( exitItem );
		return file;
	}
	
	private JMenu createViewMenu() {
		JMenu viewMenu = new JMenu( "View" );
		JMenuItem addLayer = new JMenuItem( "Add Layer" );
		final JFrame frame = this;
		addLayer.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent event ) {
				JFileChooser dialog = new JFileChooser();
				dialog.setDialogTitle( "Load ShapeFile" );
				dialog.setDialogType( JFileChooser.OPEN_DIALOG );
				javax.swing.filechooser.FileFilter shp = new ShapeFileFilter( );
				dialog.setFileFilter( shp );
				int returnVal = dialog.showOpenDialog( frame );
				if ( returnVal == JFileChooser.APPROVE_OPTION ) {
					File file = dialog.getSelectedFile();
					addLayer( file );
				}
			}
		});
		viewMenu.add( addLayer );
		JMenuItem removeLayer = new JMenuItem( "Remove Layer" );
		viewMenu.add( removeLayer );
		return viewMenu;
	}
	
	private void addLayer( File file ) {
		try {
			Layer layer = new ShapeLayer( file.toURL(), getName( file ) );
			map.addTheme( layer.getTheme() );
		} catch ( IOException ioe ) {
			ioe.printStackTrace();
		}
	}
	
	private String getName( File file ) {
		String fileName = file.getName();
		int index = fileName.lastIndexOf( '.' );
		return fileName.substring( 0, index );
	}

	public static void main( String[] args ) {
		Viewer viewer = new Viewer();
		viewer.setSize( 100, 100 );
		viewer.setVisible( true );
	}	
}