/*
 * Copyright (c) 2006 Andy Wood
 */
package uk.co.mindtrains;

import java.awt.Component;
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
			
			if ( component instanceof Piece.Label && component != layout.getMain() )
			{
				Piece.Label piece = (Piece.Label)component;
				writer.println( "    <piece ref=\"" + piece.getPiece().getName() +
				                "\" x=\"" + piece.getLocation().x +
				                "\" y=\"" + piece.getLocation().y + "\" />");
			}
		}
		writer.println( "  </land>" );
		writer.println( "</mindtrains>" );
		writer.close();
	}
}
