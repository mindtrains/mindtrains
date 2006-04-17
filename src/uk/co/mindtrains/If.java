/*
 * Copyright (c) 2006 Andy Wood
 */
package uk.co.mindtrains;

import javax.swing.Icon;

public class If extends Piece
{
	public static class Limit implements Cloneable
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

		public Object clone()
		{
			try
			{
				return super.clone();
			}
			catch ( CloneNotSupportedException e )
			{
				return null;
			}
		}
	}
	
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
	}
	
	public Connector travel( Train train, Connector entry )
	{
		if ( entry == connectors[ 0 ] )
		{
			if ( ( (Integer)train.getLoad() ).intValue() < limit.getMaximum() )
			{
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

	protected void cloneProperties( Piece piece )
	{
		limit = (Limit)( (If)piece ).limit.clone();
	}
}
