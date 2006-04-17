/*
 * Copyright (c) 2006 Andy Wood
 */
package uk.co.mindtrains;

import javax.swing.Icon;

public class Main extends Piece
{
	public static class Args implements Cloneable
	{
		int argument1;

		public int getArgument1()
		{
			return argument1;
		}

		public void setArgument1( int argument1 )
		{
			this.argument1 = argument1;
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
	
	Args args = new Args();
	
	public Main( String id, Icon icon, Connector[] connectors )
	{
		super( id, icon, connectors );
	}
	
	public Connector travel( Train train, Connector entry )
	{
		train.setLoad( new Integer( args.getArgument1() ) );
		return connectors[ 0 ];
	}
	
	public Object getProperties()
	{
		return args;
	}

	protected void cloneProperties( Piece piece )
	{
		args = (Args)( (Main)piece ).args.clone();
	}
}
