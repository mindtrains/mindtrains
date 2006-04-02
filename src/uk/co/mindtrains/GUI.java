/*
 * Created on Jan 8, 2006
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package uk.co.mindtrains;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLayeredPane;
import javax.swing.JToolBar;

public class GUI extends JFrame
{
	private static final long serialVersionUID = 1L;
		
	public GUI() throws Exception
	{
		super( "MindTrains" );
		setDefaultCloseOperation( EXIT_ON_CLOSE );
		
		Loader loader = new Loader();
		loader.parse( "docs/pieces.xml" );
		
		final Layout layout = new Layout( new Point( 50, 50 ) );
		loader.addLand( layout );
		
		JInternalFrame palette = createTrackPalette();
		loader.addTabs( palette.getContentPane() );
		layout.add( palette, JLayeredPane.PALETTE_LAYER );
		
		JToolBar toolBar = createToolBar();
		layout.add( toolBar, BorderLayout.NORTH );
		
		toolBar.add( new AbstractAction() {
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent arg0)
			{
				( (Layout)layout ).printProgram();
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
    	palette.setSize( 218, 600 );
    	palette.setLocation( 804, 36 );
    	palette.setVisible( true );
		return palette;
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
