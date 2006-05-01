/*
 * Copyright (c) 2006 Andy Wood
 */
package uk.co.mindtrains;

import javax.swing.Icon;

public class Input extends Piece
{
	public static class Message implements Cloneable
	{
		String message = "";

		public String getMessage()
		{
			return message;
		}

		public void setMessage( String message )
		{
			this.message = message;
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
	
	private Message message = new Message();
	
	public Input( String id, Icon icon, Connector[] connectors )
	{
		super( id, icon, connectors );
	}
	
	public Connector travel( Train train, Connector entry, Console console )
	{
		Carriages carriages = (Carriages)train.getLoad();
		String input = console.readln();
		carriages.setCarriageA( Integer.parseInt( input ) );
		return super.travel( train, entry, console );
	}

	public Object getProperties()
	{
		return message;
	}

	protected void cloneProperties( Piece piece )
	{
		message = (Message)( (Input)piece ).message.clone();
	}
}
