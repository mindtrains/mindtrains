/*
 * Copyright (c) 2006 Andy Wood
 */
package uk.co.mindtrains;

import javax.swing.Icon;
import javax.swing.InputVerifier;
import javax.swing.JComboBox;
import javax.swing.JComponent;

import com.l2fprod.common.beans.editor.ComboBoxPropertyEditor;
import com.l2fprod.common.propertysheet.Property;
import com.l2fprod.common.propertysheet.PropertyEditorRegistry;

public class If extends Piece
{
	public static class Limit implements Cloneable, CustomEditor
	{
		public static class Editor extends ComboBoxPropertyEditor
		{
			public Editor()
			{
				super();	    
			    setAvailableValues( new String[] { "==", "!=", ">", "<" } );
			    ( (JComboBox)editor ).setEditable( true );
			}
		}

		public static class OperandEditor extends ComboBoxPropertyEditor
		{
			public OperandEditor()
			{
				super();	    
			    setAvailableValues( new String[] { "A", "B" } );
			    ( (JComboBox)editor ).setEditable( true );
			    ( (JComboBox)editor ).setInputVerifier( new InputVerifier() {
					public boolean verify( JComponent arg0 )
					{
						return true;
					}} );
			}
		}

		String comparison = "==";
		String operand1 = "A";
		String operand2 = "0";

		public String getComparison()
		{
			return comparison;
		}

		public void setComparison( String test )
		{
			this.comparison = test;
		}

		public String getOperand1()
		{
			return operand1;
		}

		public void setOperand1( String operand1 )
		{
			this.operand1 = operand1;
		}

		public String getOperand2()
		{
			return operand2;
		}

		public void setOperand2( String operand2 )
		{
			this.operand2 = operand2;
		}

		public boolean compare( Carriages carriages )
		{
			int value1 = value( carriages, operand1 );
			int value2 = value( carriages, operand2 );
			if ( comparison.equals( ">" ) )
				return value1 > value2;
			else if ( comparison.equals( "<" ) )
				return value1 < value2;
			else if ( comparison.equals( "!=" ) )
				return value1 != value2;
			else
				return value1 == value2;
		}

		protected int value( Carriages carriages, String operand )
		{
			if ( operand.equalsIgnoreCase( "A" ) )
				return carriages.getCarriageA();
			else if ( operand.equalsIgnoreCase( "B" ) )
				return carriages.getCarriageB();
			else
				return Integer.parseInt( operand );
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
    		registry.registerEditor( properties[ 1 ], OperandEditor.class );
    		registry.registerEditor( properties[ 2 ], OperandEditor.class );
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
	
	public Connector travel( Train train, Connector entry, Console console )
	{
		if ( entry == connectors[ 0 ] )
		{
			if ( limit.compare( (Carriages)train.getLoad() ) )
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
