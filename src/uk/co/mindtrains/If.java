/*
 * Copyright (c) 2006 Andy Wood
 */
package uk.co.mindtrains;

import javax.swing.Icon;

public class If extends Piece
{
	boolean travelled = false;
	
	/**
	 * Really important that first connector is in with 2 & 3 as out
	 */
	public If( String id, Icon icon, Connector[] connectors )
	{
		super( id, icon, connectors );
	}

	public Connector travel( Train train, Connector entry )
	{
		if ( entry == connectors[ 0 ] )
		{
			if ( !travelled )
			{
				travelled = true;
				return connectors[ 1 ];
			}
			else
				return connectors[ 2 ];
		}
		else
			return connectors[ 0 ];
	}
}
