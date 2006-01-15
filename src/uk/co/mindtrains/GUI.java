/*
 * Created on Jan 8, 2006
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package uk.me.wouldbe.train;


import java.awt.BorderLayout;
import java.awt.Component;
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
import javax.swing.JToolBar;
import javax.swing.TransferHandler;

public class GUI extends JFrame
{
	private static final long serialVersionUID = 1L;
	private JInternalFrame layout;
	private Component piece;
	protected Point offset;
		
		public GUI()
		{
		  super( "ProgrammingTrain" );
		  setDefaultCloseOperation( EXIT_ON_CLOSE );
		  JDesktopPane desktop = new JDesktopPane();
		  desktop.add( createTrackPalette(), JLayeredPane.PALETTE_LAYER );
		  desktop.add( createToolBar(), BorderLayout.NORTH );
		  layout = createLayoutFrame();
		  desktop.add( layout );
		  setContentPane( desktop );
		  setSize( 876, 634 );
		}

		private JToolBar createToolBar()
		{
			JToolBar toolBar = new JToolBar();
			toolBar.setFloatable( false );
			toolBar.setSize( 876, 32 );
			return toolBar;
		}
		
		protected JInternalFrame createLayoutFrame()
		{
			final JInternalFrame frame = new JInternalFrame( "Track" );
			frame.setSize( 720, 576 );
			frame.setLocation( 2, 36 );
			frame.setVisible( true );
			//frame.getContentPane().setDropTarget()
			frame.getContentPane().setLayout( null );
			frame.getContentPane().addMouseListener( new MouseAdapter()
			{
				public void mousePressed( MouseEvent e )
				{
					piece = frame.getContentPane().findComponentAt( e.getPoint() );
					if ( piece != null && piece instanceof JLabel )
					{
						offset = new Point( e.getX() - piece.getX(), e.getY() - piece.getY() );
						frame.getContentPane().remove( piece );
						frame.getContentPane().add( piece, 0 );
						frame.getContentPane().repaint();
					}
				}
			} );

			frame.getContentPane().addMouseMotionListener( new MouseMotionAdapter()
			{
				public void mouseDragged( MouseEvent e )
				{
					if ( piece != null && piece instanceof JLabel )
						piece.setLocation( e.getX() - (int)offset.getX(), e.getY() - (int)offset.getY() );
				}
			} );
			
			DropTarget drop = new DropTarget(frame.getContentPane(),
			                                 DnDConstants.ACTION_COPY,
			                                 new DropTargetListener()
			               {

							public void dragEnter( DropTargetDragEvent dtde )
							{
								 dtde.acceptDrag(DnDConstants.ACTION_COPY);
							}

							public void dragOver( DropTargetDragEvent dtde )
							{
								 dtde.acceptDrag(DnDConstants.ACTION_COPY);
							}

							public void dropActionChanged( DropTargetDragEvent dtde )
							{
								 dtde.acceptDrag(DnDConstants.ACTION_COPY);
							}

							public void dragExit( DropTargetEvent dte )
							{
							}

							public void drop( DropTargetDropEvent dtde )
							{
								 dtde.acceptDrop(DnDConstants.ACTION_COPY);
								 try
								{
									Icon icon = (Icon)dtde.getTransferable().getTransferData( IconTransferHandler.NATIVE_FLAVOR );
						        	JLabel piece = new JLabel( icon );
						        	Point location = new Point( dtde.getLocation() );
						        	location.translate( -icon.getIconWidth(), -icon.getIconHeight() );
						        	piece.setLocation( location );
						        	piece.setSize( icon.getIconWidth(), icon.getIconHeight() );
									piece.setOpaque( false );
									layout.getContentPane().add( piece, 0 );
									layout.getContentPane().repaint();
									 dtde.dropComplete(true);
										( (JDesktopPane) GUI.this.getContentPane() ).getDesktopManager().activateFrame( layout );

								}
								catch ( UnsupportedFlavorException e )
								{
									e.printStackTrace();
								}
								catch ( IOException e )
								{
									e.printStackTrace();
								}
							}
				
			               },
			               	true);
			
			return frame;
		}
		
		protected JInternalFrame createTrackPalette()
		{
		  JInternalFrame palette = new JInternalFrame();
		  palette.putClientProperty( "JInternalFrame.isPalette", Boolean.TRUE );
		  palette.setSize( 150, 576 );
		  palette.setLocation( 724, 36 );
		  palette.setVisible( true );
		  
		  palette.getContentPane().setLayout( new GridLayout( 0, 2 ) );
		  palette.getContentPane().add( createTrack( "curve", new ImageIcon( "docs/curve45.png" ) ) );
		  palette.getContentPane().add( createTrack( "curve", new ImageIcon( "docs/curve45-2.png" ) ) );
		  palette.getContentPane().add( createTrack( "curve", flipIcon( new ImageIcon( "docs/curve45.png" ), true, false ) ) );
		  palette.getContentPane().add( createTrack( "curve", flipIcon( new ImageIcon( "docs/curve45-2.png" ), true, false ) ) );
		  palette.getContentPane().add( createTrack( "curve", flipIcon( new ImageIcon( "docs/curve45.png" ), false, true ) ) );
		  palette.getContentPane().add( createTrack( "curve", flipIcon( new ImageIcon( "docs/curve45-2.png" ), false, true ) ) );
		  palette.getContentPane().add( createTrack( "curve", flipIcon( new ImageIcon( "docs/curve45.png" ), true, true ) ) );
		  palette.getContentPane().add( createTrack( "curve", flipIcon( new ImageIcon( "docs/curve45-2.png" ), true, true ) ) );
		  palette.getContentPane().add( createTrack( "straight", new ImageIcon( "docs/shorthorizstraight.png" ) ) );
		  palette.getContentPane().add( createTrack( "straight", new ImageIcon( "docs/straighthorizlong.png" ) ) );
		  palette.getContentPane().add( createTrack( "straight", new ImageIcon( "docs/shortvertstraight.png" ) ) );
		  palette.getContentPane().add( createTrack( "straight", new ImageIcon( "docs/straightvertlong.png" ) ) );
		  palette.getContentPane().add( createTrack( "diagonal", new ImageIcon( "docs/diagonalshort.png" ) ) );
		  palette.getContentPane().add( createTrack( "diagonal", new ImageIcon( "docs/diagonallong.png" ) ) );
		  palette.getContentPane().add( createTrack( "diagonal", flipIcon( new ImageIcon( "docs/diagonalshort.png" ), true, false ) ) );
		  palette.getContentPane().add( createTrack( "diagonal", flipIcon( new ImageIcon( "docs/diagonallong.png" ), true, false ) ) );
		  
		  return palette;
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
		
		
		
		protected JLabel createTrack( String text, final Icon icon )
		{
			JLabel componentType = new JLabel( icon );
			componentType.setTransferHandler(new IconTransferHandler("text", ( (ImageIcon)icon ).getImage() ));
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
		
		protected JButton createButton( String text, final Icon icon )
		{
			return new JButton( new AbstractAction( text, icon )
			{
		        public void actionPerformed( ActionEvent e)
		        {
		        	JLabel piece = new JLabel( icon );
		        	piece.setLocation( 0, 0 );
		        	piece.setSize( icon.getIconWidth(), icon.getIconHeight() );
					piece.setOpaque( false );
					layout.getContentPane().add( piece, 0 );
					layout.getContentPane().validate();	
					layout.getContentPane().repaint();
		        }
			} );
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
			frame.setVisible( true );
			( (JDesktopPane)frame.getContentPane() ).getDesktopManager().activateFrame( frame.layout );

		}		
}
