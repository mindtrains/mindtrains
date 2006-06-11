/*
 * Copyright (c) 2006 Andy Wood
 */
package uk.co.mindtrains;

import java.awt.Component;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Saver
{
	private Layout layout;
	
	public Saver( Layout layout )
	{
		this.layout = layout;
	}

	public void save( String filename ) throws IOException
	{
		PrintWriter writer = new PrintWriter( new FileWriter( filename ) );
		
		writer.println( "<mindtrains>" );
		writer.println( "  <land>" );
		for ( int i = 0; i < layout.getComponentCount(); i++ )
		{
			Component component = layout.getComponent( i );
			
			if ( component instanceof Piece.Representation && component != layout.getMain() )
			{
				Piece.Representation piece = (Piece.Representation)component;
				writer.print( "    <piece ref=\"" + piece.getPiece().getId() +
				                "\" x=\"" + piece.getLocation().x +
				                "\" y=\"" + piece.getLocation().y + "\"" );
				Object properties = piece.getPiece().getProperties();
				
				if ( properties == null )
					writer.println( " />" );
				else
				{
					writer.println( ">" );
		    		try
					{
						PropertyDescriptor[] descriptors = Introspector.getBeanInfo( properties.getClass(), Object.class ).getPropertyDescriptors();
						for ( int j = 0; j < descriptors.length; j++ )
							writer.println( "      <property name=\"" + descriptors[ j ].getName() + "\" value=\"" +
							                escape( descriptors[ j ].getReadMethod().invoke( properties, null ) ) + "\" />" );
					}
					catch ( Exception e )
					{
						e.printStackTrace( System.err );
					}
					writer.println( "    </piece>" );
				}
			}
		}
		writer.println( "  </land>" );
		writer.println( "</mindtrains>" );
		writer.close();
	}

	private String escape( Object object )
	{
		String string = object.toString();
		if ( string.equals( "<" ) )
			return "&lt;";
		else if ( string.equals( ">" ) )
			return "&gt;";
		else
			return string;
	}
}
