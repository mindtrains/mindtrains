/*
 * Copyright (c) 2006 Andy Wood
 */
package uk.co.mindtrains;

import javax.swing.Icon;

import com.l2fprod.common.beans.editor.ComboBoxPropertyEditor;
import com.l2fprod.common.propertysheet.Property;
import com.l2fprod.common.propertysheet.PropertyEditorRegistry;

public class Increment extends Piece
{
	public static class By implements Cloneable, CustomEditor
	{
		public static class Editor extends ComboBoxPropertyEditor
		{
			public Editor()
			{
				super();	    
			    setAvailableValues( new String[] { "++", "--" } );
			}
		}

		String operator = "++";

		public String getOperator()
		{
			return operator;
		}

		public void setOperator( String operation )
		{
			this.operator = operation;
		}
		
		public boolean increment()
		{
			return operator.equals( "++" );
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

		public void registerEditors( PropertyEditorRegistry registry, Property[] properties )
		{
	    	registry.registerEditor( properties[ 0 ], Editor.class );
		}
	}
	
	
	By by = new By();
	
	public Increment( String id, Icon icon, Connector[] connectors )
	{
		super( id, icon, connectors );
	}
	
	public Connector travel( Train train, Connector entry, Console console )
	{
		Carriages carriages = (Carriages)train.getLoad();
		carriages.setCarriageA( by.increment() ? carriages.getCarriageA() + 1 : carriages.getCarriageA() - 1 );
		return super.travel( train, entry, console );
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
