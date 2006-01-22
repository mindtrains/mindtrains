/*
 * Created on Jan 8, 2006
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package uk.me.wouldbe.train;


import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.TransferHandler;

public class GUI extends JFrame
{
	private static final long serialVersionUID = 1L;
		
		public GUI()
		{
			super( "ProgrammingTrain" );
			setDefaultCloseOperation( EXIT_ON_CLOSE );
			JDesktopPane desktop = new JDesktopPane();
			desktop.add( createTrackPalette(), JLayeredPane.PALETTE_LAYER );
			desktop.add( createToolBar(), BorderLayout.NORTH );
			JInternalFrame layout = createLayoutFrame( desktop );
			desktop.add( layout );
			setContentPane( desktop );
			setSize( 876, 634 );
			setVisible( true );
			desktop.getDesktopManager().activateFrame( layout );
		}

		private JToolBar createToolBar()
		{
			JToolBar toolBar = new JToolBar();
			toolBar.setFloatable( false );
			toolBar.setSize( 876, 32 );
			return toolBar;
		}
		
		protected JInternalFrame createLayoutFrame( JDesktopPane desktop )
		{
			final JInternalFrame frame = new JInternalFrame( "Track" );
			frame.setSize( 720, 576 );
			frame.setLocation( 2, 36 );
			frame.setVisible( true );
			frame.setContentPane( new Layout( desktop.getDesktopManager() ) );
			return frame;
		}
		
		protected JInternalFrame createTrackPalette()
		{
		  JInternalFrame palette = new JInternalFrame();
		  palette.putClientProperty( "JInternalFrame.isPalette", Boolean.TRUE );
		  palette.setSize( 150, 576 );
		  palette.setLocation( 724, 36 );
		  palette.setVisible( true );
		  
		  Container content = palette.getContentPane();
		  
		  content.setLayout( new GridLayout( 0, 2 ) );
		  content.add( createTrack( "curve", new ImageIcon( "docs/curve45.png" ), 
		                            connectors( 0, 0, Connector.N, 1, 1, Connector.S ) ) );
		  content.add( createTrack( "curve", new ImageIcon( "docs/curve45-2.png" ), 
		                            connectors( 0, 0, Connector.N, 1, 1, Connector.S ) ) );
		  content.add( createTrack( "curve", flipIcon( new ImageIcon( "docs/curve45.png" ), true, false ), 
		                            connectors( 0, 0, Connector.N, 1, 1, Connector.S ) ) );
		  content.add( createTrack( "curve", flipIcon( new ImageIcon( "docs/curve45-2.png" ), true, false ), 
		                            connectors( 0, 0, Connector.N, 1, 1, Connector.S ) ) );
		  content.add( createTrack( "curve", flipIcon( new ImageIcon( "docs/curve45.png" ), false, true ), 
		                            connectors( 0, 0, Connector.N, 1, 1, Connector.S ) ) );
		  content.add( createTrack( "curve", flipIcon( new ImageIcon( "docs/curve45-2.png" ), false, true ), 
		                            connectors( 0, 0, Connector.N, 1, 1, Connector.S ) ) );
		  content.add( createTrack( "curve", flipIcon( new ImageIcon( "docs/curve45.png" ), true, true ), 
		                            connectors( 0, 0, Connector.N, 1, 1, Connector.S ) ) );
		  content.add( createTrack( "curve", flipIcon( new ImageIcon( "docs/curve45-2.png" ), true, true ), 
		                            connectors( 0, 0, Connector.N, 1, 1, Connector.S ) ) );
		  content.add( createTrack( "straight", new ImageIcon( "docs/shorthorizstraight.png" ), 
		                            connectors( 0, 0, Connector.N, 1, 1, Connector.S ) ) );
		  content.add( createTrack( "straight", new ImageIcon( "docs/straighthorizlong.png" ), 
		                            connectors( 0, 0, Connector.N, 1, 1, Connector.S ) ) );
		  content.add( createTrack( "straight", new ImageIcon( "docs/shortvertstraight.png" ), 
		                            connectors( 0, 0, Connector.N, 1, 1, Connector.S ) ) );
		  content.add( createTrack( "straight", new ImageIcon( "docs/straightvertlong.png" ), 
		                            connectors( 0, 0, Connector.N, 1, 1, Connector.S ) ) );
		  content.add( createTrack( "diagonal", new ImageIcon( "docs/diagonalshort.png" ), 
		                            connectors( 0, 0, Connector.N, 1, 1, Connector.S ) ) );
		  content.add( createTrack( "diagonal", new ImageIcon( "docs/diagonallong.png" ), 
		                            connectors( 0, 0, Connector.N, 1, 1, Connector.S ) ) );
		  content.add( createTrack( "diagonal", flipIcon( new ImageIcon( "docs/diagonalshort.png" ), true, false ), 
		                            connectors( 0, 0, Connector.N, 1, 1, Connector.S ) ) );
		  content.add( createTrack( "diagonal", flipIcon( new ImageIcon( "docs/diagonallong.png" ), true, false ), 
		                            connectors( 0, 0, Connector.N, 1, 1, Connector.S ) ) );
		  
		  return palette;
		}

		private Connector[] connectors( int offx1, int offy1, short orie1, int offx2, int offy2, short orie2 )
		{
			return new Connector[] { new Connector( new Point( offx1, offy1 ), orie1 ),
									 new Connector( new Point( offx2, offy2 ), orie2 ) };
		}

		public ImageIcon flipIcon( ImageIcon icon, boolean lr, boolean tb )
		{
			    int iw = icon.getIconWidth();
			    int ih = icon.getIconHeight();
			    BufferedImage flipped = new BufferedImage(iw, ih, BufferedImage.TYPE_INT_ARGB);
			    flipped.createGraphics().drawImage(icon.getImage(), lr ? iw : 0, tb ? ih : 0, lr ? 0 : iw, tb ? 0 : ih, 0, 0, iw, ih, this);
			    return new ImageIcon( flipped );
		}
		
		protected static JFrame createSplashScreen()
		{
		  JFrame splash = new JFrame();
		  splash.setSize( 300, 300 );
		  return splash;
		}

		protected Piece createTrack( String text, Icon icon, Connector[] connectors  )
		{
			Piece componentType = new Piece( icon, connectors );
			componentType.setTransferHandler( new IconTransferHandler( ( (ImageIcon)icon ).getImage() ));
			MouseListener ml = new MouseAdapter() {
			    public void mousePressed(MouseEvent e) {
			        JComponent c = (JComponent)e.getSource();
			        TransferHandler th = c.getTransferHandler();
			        th.exportAsDrag(c, e, TransferHandler.COPY);
			    }
			};
			componentType.addMouseListener(ml);
			return componentType;
		}
		
		public static void main( String[] args ) throws Exception
		{
			//System.out.println( "drag: " + DragSource.isDragImageSupported() );
			//UIManager.setLookAndFeel( new MetalLookAndFeel() );
			JFrame splash = createSplashScreen();
			splash.setVisible( true );
			GUI frame = new GUI();
			splash.setVisible( false );
			splash = null;
		}		
}
