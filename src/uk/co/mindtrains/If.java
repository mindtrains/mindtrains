/*
 * Copyright (c) 2006 Andy Wood
 */
package uk.co.mindtrains;

import javax.swing.Icon;

public class If extends Piece
{
	public static class Limit
	{
		int maximum;

		public int getMaximum()
		{
			return maximum;
		}

		public void setMaximum( int maximum )
		{
			this.maximum = maximum;
		}	
	}
	
	int travelled;
	Limit limit = new Limit();
	
	/**
	 * Really important that first connector is in with 2 & 3 as out
	 */
	public If( String id, Icon icon, Connector[] connectors )
	{
		super( id, icon, connectors );
	}

	public void reset()
	{
		travelled = 0;
	}
	
	public Connector travel( Train train, Connector entry )
	{
		if ( entry == connectors[ 0 ] )
		{
			if ( travelled < limit.getMaximum() )
			{
				travelled++;
				return connectors[ 1 ];
			}
			else
				return connectors[ 2 ];
		}
		else
			return connectors[ 0 ];
	}
	
	public Object getProperties()
	{
		return limit;
	}
}
