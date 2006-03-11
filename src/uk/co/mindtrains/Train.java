/*
 * Copyright (c) 2006 Andy Wood
 */
package uk.me.wouldbe.train;

import java.awt.Point;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Train extends JLabel
{
	private static final long serialVersionUID = 1L;

	public Train()
	{
		super( new ImageIcon( "docs/train.png" ) );
		setSize( 16, 8 );
		setVisible( true );
	}

	public void setLocation( Point location )
	{
		super.setLocation( location );
		getParent().repaint();
	}	
}
