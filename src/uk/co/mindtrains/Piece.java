/*
 * Copyright (c) 2006 Andy Wood
 */
package uk.me.wouldbe.train;

import javax.swing.Icon;
import javax.swing.JLabel;

public class Piece extends JLabel
{
	private static final long serialVersionUID = 1L;
	private Connector[] connectors;

	public Piece( Icon icon, Connector[] connectors )
	{
		super( icon );
		this.connectors = connectors;
	}
	
}
