/*
 * Copyright (c) 2006 Andy Wood
 */
package uk.co.mindtrains;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;

import javax.swing.ImageIcon;
import javax.swing.JComponent;

public class Train extends JComponent
{
	private static final long serialVersionUID = 1L;
	private Object load;
	private double orientation;
	private ImageIcon train;

	public Train()
	{
		train = new ImageIcon( "docs/train.png" );
		setSize( 32, 32 );
		setVisible( true );
	}

	public void setLocation( Point location )
	{
		Point point = new Point( location );
		point.translate( -( 32 / 2 ), -( 32 / 2 ) );
		super.setLocation( point );
		getParent().repaint();
	}	
	
	public void setOrientation( double orientation )
	{
		this.orientation = orientation;
	}	

	public Object getLoad()
	{
		return load;
	}

	public void setLoad( Object load )
	{
		this.load = load;
	}

	protected void paintComponent( Graphics g )
	{
		//g.setColor( Color.RED );
		//g.fillRect( 0, 0, 32, 32 );
		g.translate( ( 32 / 2 ) - ( 16 / 2 ), ( 32 / 2 ) - ( 8 / 2 ) );
		( (Graphics2D)g ).drawImage( train.getImage(), AffineTransform.getRotateInstance( orientation, train.getIconWidth() / 2, train.getIconHeight() / 2 ), this );
	}
	
	
}
