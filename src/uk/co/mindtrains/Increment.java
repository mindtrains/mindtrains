/*
 * Copyright (c) 2006 Andy Wood
 */
package uk.co.mindtrains;

import javax.swing.Icon;

import com.l2fprod.common.beans.editor.ComboBoxPropertyEditor;

public class Increment extends Piece
{
	public static class By implements Cloneable
	{
		public static class Editor extends ComboBoxPropertyEditor
		{
			public Editor()
			{
				super();	    
			    setAvailableValues( new String[] { "++", "--" } );
			}
		}

		String value = "++";

		public String getValue()
		{
			return value;
		}

		public void setValue( String operation )
		{
			this.value = operation;
		}
		
		public boolean increment()
		{
			return value.equals( "++" );
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
	
	
	By by = new By();
	
	public Increment( String id, Icon icon, Connector[] connectors )
	{
		super( id, icon, connectors );
	}
	
	public Connector travel( Train train, Connector entry )
	{
		Carriages carriages = (Carriages)train.getLoad();
		carriages.setCarriageA( by.increment() ? carriages.getCarriageA() + 1 : carriages.getCarriageA() - 1 );
		return super.travel( train, entry );
	}

	public Object getProperties()
	{
		return by;
	}

	protected void cloneProperties( Piece piece )
	{
		by = (By)( (Increment)piece ).by.clone();
	}
}
