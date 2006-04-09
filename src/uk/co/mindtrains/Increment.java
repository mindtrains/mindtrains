/*
 * Copyright (c) 2006 Andy Wood
 */
package uk.co.mindtrains;

import javax.swing.Icon;

public class Increment extends Piece
{
	public static class By implements Cloneable
	{
		int value;

		public int getValue()
		{
			return value;
		}

		public void setValue( int maximum )
		{
			this.value = maximum;
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
	
	By by = new By();
	
	public Increment( String id, Icon icon, Connector[] connectors )
	{
		super( id, icon, connectors );
	}
	
	public Connector travel( Train train, Connector entry )
	{
		// TODO increment train load
		return super.travel( train, entry );
	}

	public Object getProperties()
	{
		return by;
	}

	protected void cloneProperties( Piece piece )
	{
		by = (By)( (Increment)piece ).by.clone();
	}
}
