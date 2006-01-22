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

import javax.swing.DesktopManager;
import javax.swing.Icon;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Layout class manages the list of track pieces as the user is
 * putting it together.
 */
public class Layout extends JPanel
{
	private Component piece;
	private Point offset;
	
	public Layout(final DesktopManager manager)
	{
		setLayout( null );
		addMouseListener( new MouseAdapter()
		{
			public void mousePressed( MouseEvent e )
			{
				piece = findComponentAt( e.getPoint() );
				if ( piece != null && piece instanceof Piece )
				{
					offset = new Point( e.getX() - piece.getX(), e.getY() - piece.getY() );
					remove( piece );
					add( piece, 0 );
					repaint();
				}
			}
		} );

		addMouseMotionListener( new MouseMotionAdapter()
		{
			public void mouseDragged( MouseEvent e )
			{
				if ( piece != null && piece instanceof Piece )
					piece.setLocation( e.getX() - (int)offset.getX(), e.getY() - (int)offset.getY() );
			}
		} );
		
		DropTarget drop = new DropTarget(this,
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
								Icon icon = (Icon)dtde.getTransferable().getTransferData( IconTransferHandler.NATIVE_FLAVOR );
					        	JLabel piece = new JLabel( icon );
					        	Point location = new Point( dtde.getLocation() );
					        	location.translate( -icon.getIconWidth(), -icon.getIconHeight() );
					        	piece.setLocation( location );
					        	piece.setSize( icon.getIconWidth(), icon.getIconHeight() );
								piece.setOpaque( false );
								add( piece, 0 );
								repaint();
								dtde.dropComplete(true);
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
		               	true);
	}
	
}
