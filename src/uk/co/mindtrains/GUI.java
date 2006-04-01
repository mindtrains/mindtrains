/*
 * Created on Jan 8, 2006
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package uk.me.wouldbe.train;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
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
			JToolBar toolBar = createToolBar();
			desktop.add( toolBar, BorderLayout.NORTH );
			final JInternalFrame layout = createLayoutFrame( desktop );
			
			toolBar.add( new AbstractAction() {
				public void actionPerformed(ActionEvent arg0) {
					( (Layout)layout.getContentPane() ).printProgram();
				}} );
			
			desktop.add( layout );
			setContentPane( desktop );
			setSize( 1022, 730 );
			setVisible( true );
			desktop.getDesktopManager().activateFrame( layout );
		}

		private JToolBar createToolBar()
		{
			JToolBar toolBar = new JToolBar();
			toolBar.setFloatable( false );
			toolBar.setSize( 1022, 32 );
			return toolBar;
		}
		
		protected JInternalFrame createLayoutFrame( JDesktopPane desktop )
		{
			final JInternalFrame frame = new JInternalFrame( "Track" );
			frame.setSize( 800, 600 );
			frame.setLocation( 2, 36 );
			frame.setVisible( true );
			frame.setContentPane( new Layout( desktop.getDesktopManager(), new Point( 50, 50 ) ) );
			return frame;
		}
		
		protected JInternalFrame createTrackPalette()
		{
		  JInternalFrame palette = new JInternalFrame();
		  palette.putClientProperty( "JInternalFrame.isPalette", Boolean.TRUE );
		  palette.setSize( 218, 600 );
		  palette.setLocation( 804, 36 );
		  palette.setVisible( true );
		  
		  Container content = new JPanel();
		  content.setLayout( new GridLayout( 0, 4 ) );
		  content.add( createTrack( "curve", new ImageIcon( "docs/curve45.png" ), 
		                            connectors( 1, 19, Connector.SE, 46, 8, Connector.N ) ) );
		  content.add( createTrack( "curve", new ImageIcon( "docs/curve45-2.png" ), 
		                            connectors( 8, 46, Connector.W, 24, 6, Connector.NW ) ) );
		  content.add( createTrack( "curve", flipIcon( new ImageIcon( "docs/curve45.png" ), true, false ), 
		                            connectors( 0, 0, Connector.S, 40, 24, Connector.NE ) ) );
		  content.add( createTrack( "curve", flipIcon( new ImageIcon( "docs/curve45-2.png" ), true, false ), 
		                            connectors( 25, 46, Connector.W, 6, 1, Connector.SW ) ) );
		  content.add( createTrack( "curve", flipIcon( new ImageIcon( "docs/curve45.png" ), false, true ), 
		                            connectors( 6, 1, Connector.SW, 46, 25, Connector.N ) ) );
		  content.add( createTrack( "curve", flipIcon( new ImageIcon( "docs/curve45-2.png" ), false, true ), 
		                            connectors( 0, 0, Connector.E, 19, 45, Connector.NE ) ) );
		  content.add( createTrack( "curve", flipIcon( new ImageIcon( "docs/curve45.png" ), true, true ), 
		                            connectors( 0, 17, Connector.S, 45, 6, Connector.NW ) ) );
		  content.add( createTrack( "curve", flipIcon( new ImageIcon( "docs/curve45-2.png" ), true, true ), 
		                            connectors( 17, 0, Connector.E, 1, 40, Connector.SE ) ) );
		  content.add( createTrack( "straight", new ImageIcon( "docs/shorthorizstraight.png" ), 
		                            connectors( 0, 0, Connector.S, 32, 8, Connector.N ) ) );
		  content.add( createTrack( "straight", new ImageIcon( "docs/straighthorizlong.png" ), 
		                            connectors( 0, 0, Connector.S, 48, 8, Connector.N ) ) );
		  content.add( createTrack( "straight", new ImageIcon( "docs/shortvertstraight.png" ), 
		                            connectors( 0, 0, Connector.E, 8, 32, Connector.W ) ) );
		  content.add( createTrack( "straight", new ImageIcon( "docs/straightvertlong.png" ), 
		                            connectors( 0, 0, Connector.E, 8, 48, Connector.W ) ) );
		  content.add( createTrack( "diagonal", new ImageIcon( "docs/diagonalshort.png" ), 
		                            connectors( 28, 5, Connector.NW, 0, 23, Connector.SE ) ) );
		  content.add( createTrack( "diagonal", new ImageIcon( "docs/diagonallong.png" ), 
		                            connectors( 39, 5, Connector.NW, 0, 34, Connector.SE ) ) );
		  content.add( createTrack( "diagonal", flipIcon( new ImageIcon( "docs/diagonalshort.png" ), true, false ), 
		                            connectors( 5, 0, Connector.SW, 23, 28, Connector.NE ) ) );
		  content.add( createTrack( "diagonal", flipIcon( new ImageIcon( "docs/diagonallong.png" ), true, false ), 
		                            connectors( 5, 0, Connector.SW, 34, 39, Connector.NE ) ) );

		  Container switches = new JPanel();
		  switches.setLayout( new GridLayout( 0, 2 ) );
		  switches.add( createTrack( "if", new ImageIcon( "docs/if.png" ), 
		                             connectors( 0, 17, Connector.S, 45, 6, Connector.NW, 48, 25, Connector.N ) ) );
		  switches.add( createTrack( "if", flipIcon( new ImageIcon( "docs/if.png" ), true, false ), 
		                             connectors( 8, 1, Connector.SW, 48, 25, Connector.N, 0, 17, Connector.S ) ) );
		  
		  JTabbedPane tabs = new JTabbedPane();
		  tabs.addTab( "track", content );
		  tabs.addTab( "switches", switches );
		  palette.getContentPane().add( tabs );

		  return palette;
		}

		private Connector[] connectors( int offx1, int offy1, short orie1, int offx2, int offy2, short orie2 )
		{
			return new Connector[] { new Connector( new Point( offx1, offy1 ), orie1 ),
									 new Connector( new Point( offx2, offy2 ), orie2 ) };
		}

		private Connector[] connectors( int offx1, int offy1, short orie1, int offx2, int offy2, short orie2, int offx3, int offy3, short orie3 )
		{
			return new Connector[] { new Connector( new Point( offx1, offy1 ), orie1 ),
									 new Connector( new Point( offx2, offy2 ), orie2 ),
									 new Connector( new Point( offx3, offy3 ), orie3 )};
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

		protected Piece.Label createTrack( String text, Icon icon, Connector[] connectors  )
		{
			Piece.Label componentType = new Piece( icon, connectors ).new Label();
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
			new GUI();
			splash.setVisible( false );
			splash = null;
			
//		    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//		    dbf.setNamespaceAware(true);
//		    DocumentBuilder db = dbf.newDocumentBuilder();
//		    Document doc = db.parse(new File(filename));

		}		
}
