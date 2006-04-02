/*
 * Copyright (c) 2006 Andy Wood
 */
package uk.co.mindtrains;

import java.awt.Point;
import java.util.Random;

import javax.swing.Icon;
import javax.swing.JLabel;

public class Piece implements Cloneable
{
	private static final long serialVersionUID = 1L;
	private static final Random random = new Random( System.currentTimeMillis() );

	private String name;
	private Icon icon;
	private Connector[] connectors;
	private Point location;

	public Piece( String name, Icon icon, Connector[] connectors )
	{
		this.name = name;
		this.icon = icon;
		this.connectors = connectors;
	}
	
	public String getName()
	{
		return name;
	}

	/**
	 * Create a deeper copy as we don't want to share connectors or location.
	 */
	public Object clone()
	{
		try
		{
			Piece piece = (Piece)super.clone();
			piece.connectors = new Connector[ connectors.length ];
			for ( int i = 0; i < connectors.length; i++ )
				piece.connectors[ i ] = (Connector)connectors[ i ].clone();
			if ( location != null )
				piece.location = (Point)location.clone();
			return piece;
		}
		catch( CloneNotSupportedException e )
		{
			return null;
		}
	}
	
	public Point snap( Piece piece )
	{
		Point snap = null;
		
		for ( int i = 0; i < connectors.length; i++ )
		{
			for ( int j = 0; j < piece.connectors.length; j++ )
			{
				Point point = connectors[ i ].snap( piece.connectors[ j ] );
				snap = closest( point, snap, piece.getLocation() );
			}
		}

		return snap;
	}
	
	public static Point closest( Point a, Point b, Point to )
	{
		if ( b == null )
			return a;
		else if ( a == null )
			return b;
		else if ( a.distance( to ) <= b.distance( to ) )
			return a;
		else
			return b;
	}
	
	public Connector connected( Connector connector )
	{
		for ( int i = 0; i < connectors.length; i++ )
		{
			if ( connectors[ i ].connected( connector ) )
				return connectors[ i ];
		}

		return null;
	}

	public class Label extends JLabel
	{
		private static final long serialVersionUID = 1L;

		public Label()
		{
			super( icon );
	    	setSize( icon.getIconWidth(), icon.getIconHeight() );
	    	setOpaque( false );
		}
		
		Piece getPiece()
		{
			return Piece.this;
		}

		public void setLocation( int x, int y )
		{
			super.setLocation( x, y );
			Piece.this.setLocation( new Point( x, y ) );
		}

		public void setLocation( Point p )
		{
			super.setLocation( p );
			Piece.this.setLocation( p );
		}
	}

	public Connector travel( Train train, Connector entry )
	{
		System.err.println( "connectors: " + connectors.length + " " + entry );
		if ( connectors.length == 1 )
		{
			train.setLocation( connectors[ 0 ].midlocation() );
			return connectors[ 0 ];
		}
		else
		{
			train.setLocation( entry.midlocation() );
	    	int i;
	    	do
	    		i = random.nextInt( connectors.length );
	    	while ( connectors[ i ] == entry );
	    	return connectors[ i ];
		}
	}
	
	public void setLocation( Point location )
	{
		this.location = location;
		for ( int i = 0; i < connectors.length; i++ )
			connectors[ i ].setLocation( location );
	}
	
	public Point getLocation()
	{
		return location;
	}

	public Icon getIcon()
	{
		return icon;
	}
}
