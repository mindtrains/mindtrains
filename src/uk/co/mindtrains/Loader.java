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
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
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
	private List land = new ArrayList();
	
	public void parse( String filename ) throws Exception
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
		String id = node.getAttributes().getNamedItem( "id" ).getNodeValue();
		String classname = node.getAttributes().getNamedItem( "class" ).getNodeValue();
		List connectors = new ArrayList();
		Icon image = null;
		
		System.out.println( id + " " + classname );
		
		Node child = node.getFirstChild();
		while ( child != null )
    	{
    		if ( child.getNodeType() == Node.ELEMENT_NODE )
    		{
    			if ( child.getNodeName().equalsIgnoreCase( "image" ) )
    				image = parseImage( child );
    			else if ( child.getNodeName().equalsIgnoreCase( "connector" ) )
					connectors.add( parseConnector( child ) );
    			// TODO default properties
    			else
    				throw new Exception( "Unexpected <" + child.getNodeName() + "> element" );
    		}
    		child = child.getNextSibling();
    	}
		
		Class clazz = Class.forName( classname );
		
		Constructor constructor = clazz.getConstructor( new Class[] { String.class, Icon.class, Connector[].class } );
		Piece piece = (Piece)constructor.newInstance( new Object[] { id, image, (Connector[])connectors.toArray( new Connector[ 0 ] ) } );		
		set.put( id, piece );
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
    			{
    				Piece.Representation label = parsePieceRef( child ).new Representation();
    				label.setTransferHandler( new IconTransferHandler( label.getIcon() ) );
    				MouseListener ml = new MouseAdapter() {
    				    public void mousePressed(MouseEvent e) {
    				        JComponent c = (JComponent)e.getSource();
    				        TransferHandler th = c.getTransferHandler();
    				        th.exportAsDrag(c, e, TransferHandler.COPY);
    				    }
    				};
    				label.addMouseListener(ml);
    				content.add( label );
    			}
    			else
    				throw new Exception( "Unexpected <" + child.getNodeName() + "> element" );
    		}
    		child = child.getNextSibling();
    	}
		
		tabs.addTab( name, content );
	}

	protected Piece parsePieceRef( Node node )
	{
		System.out.println( "\t\t\tpiece (ref)" );
		NamedNodeMap attributes = node.getAttributes();
		String ref = attributes.getNamedItem( "ref" ).getNodeValue();
		return (Piece)set.get( ref );
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
    				land.add( parsePieceRefPoint( child ) );
    			else
    				throw new Exception( "Unexpected <" + child.getNodeName() + "> element" );
    		}
    		child = child.getNextSibling();
    	}
	}
	
	protected static class PiecePoint
	{
		protected Piece piece;
		protected Point point;
	}
	
	protected PiecePoint parsePieceRefPoint( Node node ) throws Exception
	{
		PiecePoint piecePoint = new PiecePoint();
		NamedNodeMap attributes = node.getAttributes();
		int x = Integer.parseInt( attributes.getNamedItem( "x" ).getNodeValue() );
		int y = Integer.parseInt( attributes.getNamedItem( "y" ).getNodeValue() );
		// boolean fixed = Boolean.valueOf( attributes.getNamedItem( "fixed" ).getNodeValue() ).booleanValue();
		piecePoint.point = new Point( x, y );
		piecePoint.piece = (Piece)parsePieceRef( node ).clone();;

		Node child = node.getFirstChild();
		while ( child != null )
    	{
    		if ( child.getNodeType() == Node.ELEMENT_NODE )
    		{
    			if ( child.getNodeName().equalsIgnoreCase( "property" ) )
    				parseProperties( child, piecePoint.piece );
    			else
    				throw new Exception( "Unexpected <" + child.getNodeName() + "> element" );
    		}
    		child = child.getNextSibling();
    	}
		
		return piecePoint;
	}

	protected void parseProperties( Node node, Piece piece )
	{
		System.out.println( "\t\t\t\tproperty" );
		NamedNodeMap attributes = node.getAttributes();
		String name = attributes.getNamedItem( "name" ).getNodeValue();
		Object properties = piece.getProperties();
		if ( properties != null )
		{
			try
			{
				PropertyDescriptor[] descriptors = Introspector.getBeanInfo( properties.getClass(), Object.class ).getPropertyDescriptors();
				for ( int j = 0; j < descriptors.length; j++ )
				{
					if ( name.equalsIgnoreCase( descriptors[ j ].getName() ) )
					{
						Method method = descriptors[ j ].getWriteMethod();
						Class clazz = method.getParameterTypes()[ 0 ];
						Object value = null;

						if ( clazz == Integer.TYPE )
							value = Integer.decode( attributes.getNamedItem( "value" ).getNodeValue() );
						else if ( clazz == String.class )
							value = attributes.getNamedItem( "value" ).getNodeValue();

						System.out.println( name + "=" + value + " (type=" + clazz.getName() + ")" );
						method.invoke( properties, new Object[] { value } );
					}
				}
			}
			catch ( Exception e )
			{
				e.printStackTrace( System.err );
			}
		}
		else
			System.err.println( "Property element, but piece doesn't have any" );
	}

	public void addTabs( Container contentPane )
	{
		contentPane.add( tabs );
	}

	public void addLand( Layout layout )
	{
		for ( int i = 0; i < land.size(); i++ )
		{
			PiecePoint piecePoint = (PiecePoint)land.get( i );
			Piece.Representation label = layout.add( piecePoint.piece, piecePoint.point, false );
			if ( i == 0 )
				layout.setMain( label );
		}		
	}
}
