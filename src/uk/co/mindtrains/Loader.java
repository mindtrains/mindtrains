/*
 * Copyright (c) 2006 Andy Wood
 */
package uk.co.mindtrains;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.TransferHandler;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class Loader
{
	private String dir;
	private Map set = new HashMap();
	private JTabbedPane tabs = new JTabbedPane();
	
	public Loader( String filename ) throws Exception
	{
	    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	    dbf.setNamespaceAware( true );
	    DocumentBuilder db = dbf.newDocumentBuilder();
	    File file = new File( filename );
	    dir = file.getParent();
	    Document doc = db.parse( file );

	    Node node = doc.getDocumentElement();
		if ( node.getNodeName().equalsIgnoreCase( "mindtrains" ) )
			parseMindTrains( node );
		else
			throw new Exception( "Expected <mindtrains> as document element" );
		
		System.out.println( "set: " + set.size() );
	}
	
	protected void parseMindTrains( Node node ) throws Exception
	{
		System.out.println( "mindtrains" );
		Node child = node.getFirstChild();
		while ( child != null )
    	{
    		if ( child.getNodeType() == Node.ELEMENT_NODE )
    		{
    			if ( child.getNodeName().equalsIgnoreCase( "set" ) )
    				parseSet( child );
    			else if ( child.getNodeName().equalsIgnoreCase( "palette" ) )
    				parsePalette( child );
    			else if ( child.getNodeName().equalsIgnoreCase( "land" ) )
					parseLand( child );
    			else
    				throw new Exception( "Unexpected <" + child.getNodeName() + "> element" );
    		}
    		child = child.getNextSibling();
    	}
	}

	protected void parseSet( Node node ) throws Exception
	{
		System.out.println( "\tset" );
		Node child = node.getFirstChild();
		while ( child != null )
    	{
    		if ( child.getNodeType() == Node.ELEMENT_NODE )
    		{
    			if ( child.getNodeName().equalsIgnoreCase( "piece" ) )
    				parsePiece( child );
    			else
    				throw new Exception( "Unexpected <" + child.getNodeName() + "> element" );
    		}
    		child = child.getNextSibling();
    	}		
	}

	protected void parsePiece( Node node ) throws Exception
	{
		System.out.println( "\t\tpiece" );
		String name = node.getAttributes().getNamedItem( "name" ).getNodeValue();
		String clazz = node.getAttributes().getNamedItem( "class" ).getNodeValue();
		List connectors = new ArrayList();
		Icon image = null;
		
		System.out.println( name + " " + clazz );
		
		Node child = node.getFirstChild();
		while ( child != null )
    	{
    		if ( child.getNodeType() == Node.ELEMENT_NODE )
    		{
    			if ( child.getNodeName().equalsIgnoreCase( "image" ) )
    				image = parseImage( child );
    			else if ( child.getNodeName().equalsIgnoreCase( "connector" ) )
					connectors.add( parseConnector( child ) );
    			else
    				throw new Exception( "Unexpected <" + child.getNodeName() + "> element" );
    		}
    		child = child.getNextSibling();
    	}	
		
		Piece.Label piece = new Piece( image, (Connector[])connectors.toArray( new Connector[ 0 ] ) ).new Label();
		piece.setTransferHandler( new IconTransferHandler( ( (ImageIcon)image ).getImage() ));
		MouseListener ml = new MouseAdapter() {
		    public void mousePressed(MouseEvent e) {
		        JComponent c = (JComponent)e.getSource();
		        TransferHandler th = c.getTransferHandler();
		        th.exportAsDrag(c, e, TransferHandler.COPY);
		    }
		};
		piece.addMouseListener(ml);
		set.put( name, piece );
	}
	
	protected Icon parseImage( Node node )
	{
		System.out.println( "\t\t\timage" );
		NamedNodeMap attributes = node.getAttributes();
		String filename = dir + File.separator + attributes.getNamedItem( "file" ).getNodeValue();
		boolean hflip = Boolean.valueOf( attributes.getNamedItem( "hflip" ).getNodeValue() ).booleanValue();
		boolean vflip = Boolean.valueOf( attributes.getNamedItem( "vflip" ).getNodeValue() ).booleanValue();
		ImageIcon image = new ImageIcon( filename );
		return hflip || vflip ? flipImage( image, hflip, vflip ) : image;
	}
	
	public ImageIcon flipImage( ImageIcon image, boolean hflip, boolean vflip )
	{
	    int iw = image.getIconWidth();
	    int ih = image.getIconHeight();
	    BufferedImage flipped = new BufferedImage(iw, ih, BufferedImage.TYPE_INT_ARGB);
	    flipped.createGraphics().drawImage( image.getImage(), hflip ? iw : 0, vflip ? ih : 0, hflip ? 0 : iw, vflip ? 0 : ih, 0, 0, iw, ih, null );
	    return new ImageIcon( flipped );
	}

	protected Connector parseConnector( Node node ) throws Exception
	{
		System.out.println( "\t\t\tconnector" );
		NamedNodeMap attributes = node.getAttributes();
		int x = Integer.parseInt( attributes.getNamedItem( "x" ).getNodeValue() );
		int y = Integer.parseInt( attributes.getNamedItem( "y" ).getNodeValue() );
		short orientation = parseOrientation( attributes.getNamedItem( "orientation" ).getNodeValue() );
		return new Connector( new Point( x, y ), orientation );
	}

	protected short parseOrientation( String orientation ) throws Exception
	{
		orientation = orientation.toUpperCase();
		
		switch ( orientation.charAt( 0 ) )
		{
			case 'N':
				if ( orientation.length() == 1 )
					return Connector.N;
				else if ( orientation.charAt( 1 ) == 'E' ) 
					return Connector.NE;
				else if ( orientation.charAt( 1 ) == 'W' )
					return Connector.NW;
				break;
			case 'S':
				if ( orientation.length() == 1 )
					return Connector.S;
				else if ( orientation.charAt( 1 ) == 'E' ) 
					return Connector.SE;
				else if ( orientation.charAt( 1 ) == 'W' )
					return Connector.SW;
			case 'E':
				return Connector.E;
			case 'W':
				return Connector.W;		
		}
		
		throw new Exception( "Unkown orientation \"" + orientation + "\"" );
	}

	protected void parsePalette( Node node ) throws Exception
	{
		System.out.println( "\tpalette" );
		Node child = node.getFirstChild();
		while ( child != null )
    	{
    		if ( child.getNodeType() == Node.ELEMENT_NODE )
    		{
    			if ( child.getNodeName().equalsIgnoreCase( "tab" ) )
    				parseTab( child );
    			else
    				throw new Exception( "Unexpected <" + child.getNodeName() + "> element" );
    		}
    		child = child.getNextSibling();
    	}		
	}

	protected void parseTab( Node node ) throws Exception
	{
		System.out.println( "\t\ttab" );
		NamedNodeMap attributes = node.getAttributes();
		String name = attributes.getNamedItem( "name" ).getNodeValue();

		Container content = new JPanel();
		content.setLayout( new GridLayout( 0, 4 ) );

		Node child = node.getFirstChild();
		while ( child != null )
    	{
    		if ( child.getNodeType() == Node.ELEMENT_NODE )
    		{
    			if ( child.getNodeName().equalsIgnoreCase( "piece" ) )
    				content.add( parsePieceRef( child ) );
    			else
    				throw new Exception( "Unexpected <" + child.getNodeName() + "> element" );
    		}
    		child = child.getNextSibling();
    	}
		
		tabs.addTab( name, content );
	}

	protected Piece.Label parsePieceRef( Node node )
	{
		System.out.println( "\t\t\tpiece (ref)" );
		NamedNodeMap attributes = node.getAttributes();
		String ref = attributes.getNamedItem( "ref" ).getNodeValue();
		return (Piece.Label)set.get( ref );
	}

	protected void parseLand( Node node ) throws Exception
	{
		System.out.println( "\tland" );

		Node child = node.getFirstChild();
		while ( child != null )
    	{
    		if ( child.getNodeType() == Node.ELEMENT_NODE )
    		{
    			if ( child.getNodeName().equalsIgnoreCase( "piece" ) )
    				parsePieceRef( child );
    			else
    				throw new Exception( "Unexpected <" + child.getNodeName() + "> element" );
    		}
    		child = child.getNextSibling();
    	}
	}
	
	public JTabbedPane getTabs()
	{
		return tabs;
	}

	protected static void printContent( Node child )
	{
		while ( child != null )
    	{
    		if ( child.getNodeType() == Node.ELEMENT_NODE )
    		{
    			System.out.print( child.getNodeName() );
    		
    			NamedNodeMap attributes = child.getAttributes();
    			for ( int i = 0; i < attributes.getLength(); i++ )
    			{
    				Node attribute = attributes.item( i );
    				System.out.print(  " " + attribute.getNodeName() + "=" + attribute.getNodeValue() );
    			}
    			
    			System.out.println();
    			printContent( child.getFirstChild() );
    		}
    		child = child.getNextSibling();
    	}
	}
}
