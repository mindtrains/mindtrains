/*
 * Copyright (c) 2006 Andy Wood
 */
package uk.co.mindtrains;

import javax.swing.Icon;

import com.l2fprod.common.beans.editor.ComboBoxPropertyEditor;

public class If extends Piece
{
	public static class Limit implements Cloneable
	{
		public static class Editor extends ComboBoxPropertyEditor
		{
			public Editor()
			{
				super();	    
			    setAvailableValues( new String[] { ">", "==", "<" } );
			}
		}

		String comparison = "<";
		int value;

		public String getComparison()
		{
			return comparison;
		}

		public void setComparison( String test )
		{
			this.comparison = test;
		}

		public int getValue()
		{
			return value;
		}

		public void setValue( int maximum )
		{
			this.value = maximum;
		}

		public boolean compare( int v )
		{
			if ( comparison.equals( ">" ) )
				return v > value;
			else if (comparison.equals( "<" ) )
				return v < value;
			else
				return v == value;
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
	
	Limit limit = new Limit();
	
	/**
	 * Really important that first connector [0] is in with [1] & [2] as out
	 */
	public If( String id, Icon icon, Connector[] connectors )
	{
		super( id, icon, connectors );
	}

	public void reset()
	{
	}
	
	public Connector travel( Train train, Connector entry )
	{
		if ( entry == connectors[ 0 ] )
		{
			if ( limit.compare( ( (Carriages)train.getLoad() ).getCarriageA() ) )
			{
				return connectors[ 1 ];
			}
			else
				return connectors[ 2 ];
		}
		else
			return connectors[ 0 ];
	}
	
	public Object getProperties()
	{
		return limit;
	}

	protected void cloneProperties( Piece piece )
	{
		limit = (Limit)( (If)piece ).limit.clone();
	}
}
