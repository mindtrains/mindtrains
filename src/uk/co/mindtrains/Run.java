/*
 * Copyright (c) 2006 Andy Wood
 */
package uk.me.wouldbe.train;

import java.util.HashSet;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Run extends Thread
{
	private Layout layout;
	
	public Run( Layout layout )
	{
		this.layout = layout;
		start();
	}

	public void run()
	{
		JLabel train = new JLabel( new ImageIcon( "docs/train.png" ) );
		train.setSize( 16, 8 );
		layout.add( train, 0 );
		train.setVisible( true );
		
		System.out.println( layout.getMain() );
		Set done = new HashSet();
		Piece.Label now = layout.getMain();

		Piece.Label next;
		do
		{
			train.setLocation( now.getLocation() );
			layout.repaint();
			next = null;
			for ( int i = 0; i < layout.getComponentCount(); i++ )
			{
				if ( layout.getComponent( i ) instanceof Piece.Label )
				{
					Piece.Label piece = (Piece.Label)layout.getComponent( i );
					if ( piece != now && !done.contains( piece ) &&
						 piece.getPiece().connected( now.getPiece() ) )
					{
						next = piece;
						System.out.println( piece );
						continue;
					}
				}
			}
			done.add( now );
			now = next;
			try { Thread.sleep( 250 ); } catch ( InterruptedException e ) {}
		}
		while ( now != null );
		
		layout.remove( train );
		layout.repaint();
	}
}
