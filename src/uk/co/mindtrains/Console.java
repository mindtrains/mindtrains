/*
 * Copyright (c) 2006 Andy Wood
 */
package uk.co.mindtrains;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;

import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class Console extends JInternalFrame
{
	private static final long serialVersionUID = 1L;

	PipedInputStream piOut;
    PipedOutputStream poOut;
    JTextArea textArea = new JTextArea();

    public Console( String title ) throws IOException
    {
    	super( title );
    	
        // Set up System.out
        piOut = new PipedInputStream();
        poOut = new PipedOutputStream(piOut);
        System.setOut(new PrintStream(poOut, true));

        // Add a scrolling text area
        textArea.setEditable(false);
        textArea.setRows(20);
        textArea.setColumns(50);
        getContentPane().add(new JScrollPane(textArea), BorderLayout.CENTER);
        pack();

        // Create reader thread
        new ReaderThread(piOut).start();
        
        setFocusable( true );
        addKeyListener( new KeyAdapter() {
			public void keyTyped( KeyEvent event )
			{
				System.err.println( "key: " + (int)event.getKeyChar() );
				textArea.append( "" + event.getKeyChar() );
			}
		} );
    }

    class ReaderThread extends Thread
    {
        PipedInputStream pi;

        ReaderThread(PipedInputStream pi)
        {
            this.pi = pi;
        }

        public void run()
        {
            final byte[] buf = new byte[1024];
            try
            {
                while (true)
                {
                    final int len = pi.read(buf);
                    if (len == -1)
                    {
                        break;
                    }
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run()
                        {
                            textArea.append(new String(buf, 0, len));

                            // Make sure the last line is always visible
                            textArea.setCaretPosition(textArea.getDocument().getLength());

                            // Keep the text area down to a certain character size
                            int idealSize = 1000;
                            int maxExcess = 500;
                            int excess = textArea.getDocument().getLength() - idealSize;
                            if (excess >= maxExcess)
                            {
                                textArea.replaceRange("", 0, excess);
                            }
                        }
                    });
                }
            }
            catch (IOException e)
            {
            }
        }
    }
    
    public void clear()
    {
    	textArea.setText( "" );
        textArea.setCaretPosition( 0 );
    }
}
