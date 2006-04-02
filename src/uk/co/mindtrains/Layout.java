/*
 * Copyright (c) 2006 Andy Wood
 */
package uk.co.mindtrains;

import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
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

import javax.swing.JDesktopPane;
import javax.swing.JLayeredPane;

import uk.co.mindtrains.Piece.Label;

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
	private Rectangle draggingRect = new Rectangle();
	
	public Layout( Point start )
	{
		setLayout( null );
		
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
					dragging = null;
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
							add( dragging, JLayeredPane.DRAG_LAYER );
							Point location = new Point( dtde.getLocation() );
				        	location.translate( -dragging.getIcon().getIconWidth(), -dragging.getIcon().getIconHeight() );
							dragging.setLocation( location );
							snap( dragging );
							draggingRect.setRect( dragging.getLocation().x, dragging.getLocation().y,
							                      dragging.getIcon().getIconWidth(), dragging.getIcon().getIconHeight() );
							paintImmediately( draggingRect );
						}

						public void dragOver( DropTargetDragEvent dtde )
						{
							dtde.acceptDrag(DnDConstants.ACTION_COPY);
							Point location = new Point( dtde.getLocation() );
				        	location.translate( -dragging.getIcon().getIconWidth(), -dragging.getIcon().getIconHeight() );
							dragging.setLocation( location );
							snap( dragging );
							Rectangle newRect = new Rectangle( dragging.getLocation().x, dragging.getLocation().y,
							                                   dragging.getIcon().getIconWidth(), dragging.getIcon().getIconHeight() );
							draggingRect.add( newRect );
							paintImmediately( draggingRect );
							draggingRect = newRect;
						}

						public void dropActionChanged( DropTargetDragEvent dtde )
						{
							 dtde.acceptDrag(DnDConstants.ACTION_COPY);
						}

						public void dragExit( DropTargetEvent dte )
						{
							remove( dragging );
							paintImmediately( draggingRect );
						}

						public void drop( DropTargetDropEvent dtde )
						{
							dtde.acceptDrop( DnDConstants.ACTION_COPY );
							remove( dragging );
							paintImmediately( draggingRect );
							try
							{
								Piece piece = (Piece)dtde.getTransferable().getTransferData( IconTransferHandler.NATIVE_FLAVOR );
					        	Point location = new Point( dtde.getLocation() );
					        	location.translate( -piece.getIcon().getIconWidth(), -piece.getIcon().getIconHeight() );
								add( piece, location, true );
								repaint();
								dtde.dropComplete( true );
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

		IconTransferHandler.setLayout( this );
	}
	
	public void setDragging( Piece.Label dragging )
	{
		this.dragging = dragging;
	}

	public Piece.Label add( Piece piece, Point location, boolean snap )
	{
		Piece.Label label = piece.new Label();
    	label.setLocation( location );
    	if ( snap )
    		snap( label );
		add( label, 0 );
		return label;
	}

	public void printProgram()
	{
		new Run( this );
	}

	public void setMain( Label main )
	{
		this.main = main;
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

	public void save()
	{
		try
		{
			Saver saver = new Saver( this );
			saver.save( "docs/saved.xml" );
		}
		catch ( IOException e )
		{
			e.printStackTrace( System.err );
		}
	}

}
