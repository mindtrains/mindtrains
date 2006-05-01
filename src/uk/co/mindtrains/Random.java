/*
 * Copyright (c) 2006 Andy Wood
 */
package uk.co.mindtrains;

import javax.swing.Icon;

public class Random extends Piece
{
	public static class UpTo implements Cloneable
	{
		int maximum = 10;

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
	
	
	private static java.util.Random random = new java.util.Random( System.currentTimeMillis() );
	private UpTo upto = new UpTo();
	
	public Random( String id, Icon icon, Connector[] connectors )
	{
		super( id, icon, connectors );
	}
	
	public Connector travel( Train train, Connector entry, Console console )
	{
		Carriages carriages = (Carriages)train.getLoad();
		carriages.setCarriageB( random.nextInt( upto.getMaximum() ) );
		return super.travel( train, entry, console );
	}

	public Object getProperties()
	{
		return upto;
	}

	protected void cloneProperties( Piece piece )
	{
		upto = (UpTo)( (Random)piece ).upto.clone();
	}
}
