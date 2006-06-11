/*
 * Copyright (c) 2006 Andy Wood
 */
package uk.co.mindtrains;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Console extends JInternalFrame
{
	private static final long serialVersionUID = 1L;

    private JTextArea textArea = new JTextArea();

    public Console( String title ) throws IOException
    {
    	super( title );
    	
        textArea.setEditable( false );
        textArea.setRows( 20 );
        textArea.setColumns( 50 );
        getContentPane().add( new JScrollPane( textArea ), BorderLayout.CENTER );
        pack();

        setFocusable( true );
    }

    public void println( String string )
    {    
    	textArea.append( string + "\n" );
    	textArea.setCaretPosition( textArea.getDocument().getLength() );
    }
    
    public String readln()
    {    
		requestFocusInWindow();
		final StringBuffer read = new StringBuffer();
		final Object enter = new Object();
		
		KeyListener listener = new KeyAdapter() {
			public void keyTyped( KeyEvent event )
			{
				synchronized ( enter )
				{
					System.err.println( "key: " + (int)event.getKeyChar() );
					textArea.append( "" + event.getKeyChar() + "\n" );
					read.append( event.getKeyChar() );
					enter.notify();
				}
			}
		};

		synchronized ( enter )
		{
			addKeyListener( listener );
			try
			{
				enter.wait();
			}
			catch ( InterruptedException e )
			{
				e.printStackTrace();
			}
			removeKeyListener( listener );
		}
		return read.toString();
    }

    public void clear()
    {
    	textArea.setText( "" );
        textArea.setCaretPosition( 0 );
    }
}
