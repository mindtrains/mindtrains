/*
 * Copyright (c) 2006 Andy Wood
 */
package uk.co.mindtrains;

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

import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;

/**
 * Layout class manages the list of track pieces as the user is
 * putting it together.
 */
public class Layout extends JDesktopPane
{
	private static final long serialVersionUID = 1L;
	private Piece.Label dragging;
	private Piece.Label main;
	private Point dragOffset;
	
	public Layout( Point start )
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
					snap( dragging );
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
					        	Piece.Label dropped = piece.new Label();
					        	Point location = new Point( dtde.getLocation() );
					        	location.translate( -piece.getIcon().getIconWidth(), -piece.getIcon().getIconHeight() );
					        	dropped.setLocation( location );
								snap( dropped );
								dropped.setSize( piece.getIcon().getIconWidth(), piece.getIcon().getIconHeight() );
								dropped.setOpaque( false );
								add( dropped, 0 );
								repaint();
								dtde.dropComplete( true );
								//getDesktopManager().activateFrame( (JInternalFrame)getParent().getParent().getParent() );
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
		new Run( this );
	}

	public Piece.Label getMain()
	{
		return main;
	}

	protected void snap( Piece.Label piece )
	{
		Point snap = null;
		for ( int i = 0; i < getComponentCount(); i++ )
		{
			Component component = getComponent( i );
			if ( component != piece && component instanceof Piece.Label )
				snap = Piece.closest( ( (Piece.Label)component ).getPiece().snap( piece.getPiece() ), snap, piece.getLocation() );
		}
		if ( snap != null )
			piece.setLocation( snap );
	}
}
