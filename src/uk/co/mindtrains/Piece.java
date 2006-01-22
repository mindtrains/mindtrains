/*
 * Copyright (c) 2006 Andy Wood
 */
package uk.me.wouldbe.train;

import javax.swing.Icon;
import javax.swing.JLabel;

public class Piece implements Cloneable
{
	private static final long serialVersionUID = 1L;
	private Icon icon;
	private Connector[] connectors;

	public Piece( Icon icon, Connector[] connectors )
	{
		this.icon = icon;
		this.connectors = connectors;
	}
	
	public Object clone()
	{
		try
		{
			return super.clone();
		}
		catch( CloneNotSupportedException e )
		{
			return null;
		}
	}
	
	public class Label extends JLabel
	{
		public Label()
		{
			super( icon );
		}
		
		Piece getPiece()
		{
			return Piece.this;
		}
	}

	public Icon getIcon()
	{
		return icon;
	}
}
