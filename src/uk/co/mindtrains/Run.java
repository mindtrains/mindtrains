/*
 * Copyright (c) 2006 Andy Wood
 */
package uk.co.mindtrains;

import javax.swing.JInternalFrame;

import com.l2fprod.common.propertysheet.PropertySheetTableModel;

public class Run extends Thread
{
	private Layout layout;
	private JInternalFrame palette;
	private PropertySheetTableModel model;
	private Console console;
	
	public Run( Layout layout, JInternalFrame palette, PropertySheetTableModel model, Console console )
	{
		this.layout = layout;
		this.palette = palette;
		this.model = model;
		this.console = console;
		start();
	}

	public void run()
	{
		for ( int i = 0; i < layout.getComponentCount(); i++ )
			if ( layout.getComponent( i ) instanceof Piece.Label )
				( (Piece.Label)layout.getComponent( i ) ).getPiece().reset();

		console.clear();
		console.setVisible( true );
		console.requestFocusInWindow();
		palette.setVisible( true );
		Train train = new Train();
		layout.add( train, 0 );
		
		Piece.Label now = layout.getMain();
		Connector entry = null;
		do
		{
			Connector exit = now.getPiece().travel( train, entry, console );
			model.setProperties( Layout.createProperties( train.getLoad(), null ) );
			train.setOrientation( exit.getOrientation() );
			train.setLocation( exit.midlocation() );
			for ( int i = 0; i < layout.getComponentCount(); i++ )
			{
				if ( layout.getComponent( i ) instanceof Piece.Label )
				{
					Piece.Label piece = (Piece.Label)layout.getComponent( i );
					if ( piece != now  )
					{
						entry = piece.getPiece().connected( exit );
						if ( entry != null )
						{
							now = piece;
							break;
						}
					}
				}
			}
			try { Thread.sleep( 250 ); } catch ( InterruptedException e ) {}
		}
		while ( entry != null );

		layout.remove( train );
		layout.repaint();
		palette.setVisible( false );
		console.setVisible( false );
	}
}
