/*
 * Copyright (c) 2006 Andy Wood
 */
package uk.co.mindtrains;

import java.awt.Point;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Train extends JLabel
{
	private static final long serialVersionUID = 1L;
	private Object load;

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
	
	public Object getLoad()
	{
		return load;
	}

	public void setLoad( Object load )
	{
		this.load = load;
	}
}
