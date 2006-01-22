/*
 * Copyright (c) 2006 Andy Wood
 */
package uk.me.wouldbe.train;

import java.awt.Point;

/**
 * Represent a track connection point - consists of a pixel offset on
 * this piece, its location (both from top left, in the Java-stylee)
 * plus an orientation (0 degrees = up).
 */
public class Connector
{
	public final static short N = 0;
	public final static short NE = 45;
	public final static short E = 90;
	public final static short SE = 135;
	public final static short S = 180;
	public final static short SW = 225;
	public final static short W = 270;
	public final static short NW = 315;
	
	private final static int WIDTH = 8; // Distance between one side of the track and the other
	private final static int NEAR = 8; // Used in snapping
	private final static int CONNECTED = 1;  // Tolerence when deciding connectedness

	private Point offset;
	private double orientation; // radians
	private Point location;
	
	public Connector( Point offset, short orientation ) // degrees
	{
		this.offset = offset;
		this.orientation = (double)orientation / 180.0 * Math.PI;
	}

	public void setLocation( Point location )
	{
		this.location = location;
	}
	
	public Point getLocation()
	{
		return location;
	}

	public boolean connected( Connector connector )
	{
		return compatible( connector ) && ( midlocation().distance( connector.midlocation() ) < CONNECTED );
	}
	
	public Point snap( Connector connector )
	{
		if ( compatible( connector ) && near( connector ) )
		{
			Point midlocation = midlocation();
			Point midoffset = connector.midoffset();
			return new Point( (int)( midlocation.getX() - midoffset.getX() ),
			                  (int)( midlocation.getY() - midoffset.getY() ) );
		}
		else
			return null;
	}
	
	protected boolean compatible( Connector connector )
	{
		return Math.abs( connector.orientation - orientation ) == Math.PI;
	}

	protected Point midlocation()
	{
		Point midoffset = midoffset();
		return new Point( (int)( location.getX() + midoffset.getX() ),
		                  (int)( location.getY() + midoffset.getY() ) );
	}
	
	protected Point midoffset()
	{
		return new Point( (int)( offset.getX() + ( Math.sin( orientation ) * WIDTH / 2 ) ),
		                  (int)( offset.getY() + ( Math.cos( orientation ) * WIDTH / 2 ) ) );
	}

	protected boolean near( Connector connector )
	{
		return midlocation().distance( connector.midlocation() ) <= NEAR;
	}
}
