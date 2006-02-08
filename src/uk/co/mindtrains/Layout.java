/*
 * Copyright (c) 2006 Andy Wood
 */
package uk.me.wouldbe.train;

import java.awt.Component;
import java.awt.Point;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.swing.DesktopManager;
import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;

/**
 * Layout class manages the list of track pieces as the user is
 * putting it together.
 */
public class Layout extends JPanel
{
	private static final long serialVersionUID = 1L;
	private Piece.Label dragging;
	private Piece.Label main;
	private Point dragOffset;
	
	public Layout(final DesktopManager manager, Point start )
	{
		setLayout( null );
		
		main = new Piece( new ImageIcon( "docs/tunnel.png" ),
						  new Connector[] { new Connector( new Point( 16, 14 ), Connector.N ) } ).new Label();
		main.setSize( 16, 20 );
		main.setLocation( start );
		add( main, 0 );
		
		addMouseListener( new MouseAdapter()
		{
			public void mousePressed( MouseEvent e )
			{
				Component piece = findComponentAt( e.getPoint() );
				if ( piece != null && piece instanceof Piece.Label )
				{
					dragging = (Piece.Label)piece;
					dragOffset = new Point( e.getX() - piece.getX(), e.getY() - piece.getY() );
					remove( piece );
					add( piece, 0 );
					repaint();
				}
				else
					piece = null;
			}
		} );

		addMouseMotionListener( new MouseMotionAdapter()
		{
			public void mouseDragged( MouseEvent e )
			{
				if ( dragging != null )
				{
					dragging.setLocation( e.getX() - (int)dragOffset.getX(), e.getY() - (int)dragOffset.getY() );
					Point snap = null;
					for ( int i = 0; i < getComponentCount(); i++ )
					{
						Component component = getComponent( i );
						if ( component != dragging )
							snap = Piece.closest( ( (Piece.Label)component ).getPiece().snap( dragging.getPiece() ), snap, dragging.getLocation() );
					}
					if ( snap != null )
						dragging.setLocation( snap );
				}
			}
		} );
		
		new DropTarget( this,
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
							 dtde.acceptDrop( DnDConstants.ACTION_COPY );
							 try
							{
								Piece piece = (Piece)dtde.getTransferable().getTransferData( IconTransferHandler.NATIVE_FLAVOR );
					        	Piece.Label label = piece.new Label();
					        	Point location = new Point( dtde.getLocation() );
					        	location.translate( -piece.getIcon().getIconWidth(), -piece.getIcon().getIconHeight() );
					        	label.setLocation( location );
					        	label.setSize( piece.getIcon().getIconWidth(), piece.getIcon().getIconHeight() );
								label.setOpaque( false );
								add( label, 0 );
								repaint();
								dtde.dropComplete( true );
								manager.activateFrame( (JInternalFrame)getParent().getParent().getParent() );
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
		               true );
	}
	
	public void printProgram()
	{
		System.out.println( main );
		Set done = new HashSet();
		Piece.Label now = main;
		Piece.Label next;
		do
		{
			next = null;
			for ( int i = 0; i < getComponentCount(); i++ )
			{
				Piece.Label piece = (Piece.Label)getComponent( i );
				if ( piece != now && !done.contains( piece ) &&
					 piece.getPiece().connected( now.getPiece() ) )
				{
					next = piece;
					System.out.println( piece );
					continue;
				}
			}
			done.add( now );
			now = next;
		}
		while ( now != null );
	}
}
