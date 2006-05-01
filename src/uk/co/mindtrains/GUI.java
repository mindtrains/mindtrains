/*
 * Copyright (c) 2006 Andy Wood
 */
package uk.co.mindtrains;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.beans.IntrospectionException;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLayeredPane;
import javax.swing.JToolBar;

import com.l2fprod.common.propertysheet.PropertyEditorRegistry;
import com.l2fprod.common.propertysheet.PropertySheetTable;

public class GUI extends JFrame
{
	private static final long serialVersionUID = 1L;
		
	public GUI() throws Exception
	{
		super( "MindTrains" );
		setDefaultCloseOperation( EXIT_ON_CLOSE );
		
		Loader loader = new Loader();
		loader.parse( "docs/pieces.xml" );
		loader.parse( "docs/saved.xml" );
		
		PropertySheetTable table = new PropertySheetTable();
		PropertyEditorRegistry registry = new PropertyEditorRegistry();
		table.setEditorFactory( registry );
    	//table.setFont( new Font( "Tahoma", Font.PLAIN, 16 ) );

		final PropertySheetTable carriages = new PropertySheetTable();

		final Layout layout = new Layout( new Point( 50, 50 ), table.getSheetModel(), registry );
		loader.addLand( layout );
		
		JInternalFrame palette = createTrackPalette();
		loader.addTabs( palette.getContentPane() );
		layout.add( palette, JLayeredPane.PALETTE_LAYER );
		layout.add( createPropertiesPane( table ), JLayeredPane.PALETTE_LAYER );
		final JInternalFrame carriagesPalette = createCarriagesPane( carriages );
		layout.add( carriagesPalette, JLayeredPane.PALETTE_LAYER );
		
		final Console console = createConsole();
		layout.add( console );
		
		JToolBar toolBar = createToolBar();
		layout.add( toolBar, BorderLayout.NORTH );
		
		toolBar.add( new AbstractAction( "Save", new ImageIcon( "docs/save.png" ) ) {
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent arg0)
			{
				( (Layout)layout ).save();
			}
		} );

		toolBar.addSeparator();
		
		toolBar.add( new AbstractAction( "Run", new ImageIcon( "docs/run.png" ) ) {
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent arg0)
			{
				( (Layout)layout ).run( carriagesPalette, carriages.getSheetModel(), console );
			}
		} );

		setContentPane( layout );
		setSize( 1022, 730 );
		setVisible( true );
	}

	private JToolBar createToolBar()
	{
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable( false );
		toolBar.setSize( 1022, 32 );
		return toolBar;
	}
			
    protected JInternalFrame createTrackPalette() throws Exception
	{
    	JInternalFrame palette = new JInternalFrame();
    	palette.putClientProperty( "JInternalFrame.isPalette", Boolean.TRUE );
    	palette.setSize( 218, 400 );
    	palette.setLocation( 804, 36 );
    	palette.setVisible( true );
		return palette;
	}
	
    protected JInternalFrame createPropertiesPane( PropertySheetTable table ) throws IntrospectionException
    {
    	JInternalFrame properties = new JInternalFrame( "Track Properties" );
    	properties.putClientProperty( "JInternalFrame.isPalette", Boolean.TRUE );
    	properties.setSize( 218, 100 );
    	properties.setLocation( 804, 436 );
    	properties.setVisible( true );
    	properties.getContentPane().add( table );
		return properties;    	
    }
    
    protected JInternalFrame createCarriagesPane( PropertySheetTable table ) throws IntrospectionException
    {
    	JInternalFrame carriages = new JInternalFrame( "Train Properties" );
    	carriages.putClientProperty( "JInternalFrame.isPalette", Boolean.TRUE );
    	carriages.setSize( 218, 100 );
    	carriages.setLocation( 804, 536 );
    	carriages.setVisible( false );
    	carriages.getContentPane().add( table );
		return carriages;    	
    }

    protected Console createConsole() throws IOException
    {
    	Console console = new Console( "Console" );
    	console.putClientProperty( "JInternalFrame.isPalette", Boolean.TRUE );
    	console.setSize( 418, 200 );
    	console.setLocation( 380, 436 );
    	console.setVisible( false );
    	return console;
    }

    protected static JFrame createSplashScreen()
    {
    	JFrame splash = new JFrame();
    	splash.setSize( 300, 300 );
    	return splash;
	}

    public static void main( String[] args ) throws Exception
    {
    	//System.out.println( "drag: " + DragSource.isDragImageSupported() );
    	//UIManager.setLookAndFeel( new MetalLookAndFeel() );
    	JFrame splash = createSplashScreen();
    	splash.setVisible( true );
    	new GUI();
    	splash.setVisible( false );
    	splash = null;
	}
}
