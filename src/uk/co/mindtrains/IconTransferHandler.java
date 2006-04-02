/*
 * Copyright (c) 2006 Andy Wood
 */
package uk.co.mindtrains;

import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragGestureRecognizer;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceContext;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.TransferHandler;

public class IconTransferHandler extends TransferHandler
{
	private static final long serialVersionUID = 1L;
	public static final DataFlavor NATIVE_FLAVOR = new DataFlavor( Piece.class, DataFlavor.javaJVMLocalObjectMimeType );
	private Icon icon;
    private static SwingDragGestureRecognizer recognizer = null;
    private static Layout layout;
    
	public IconTransferHandler( Icon icon )
	{
		this.icon = icon;
	}
	
	public static void setLayout( Layout layout )
	{
		IconTransferHandler.layout = layout;
	}
	
	public Point getOffset()
	{
		return new Point( -icon.getIconWidth(), -icon.getIconHeight() );
	}

	/**
	 * Create empty drag image so we can see ours below it, and avoid the silly sized
	 * outline border provided automatically.
	 */
	public Image getImage()
	{
		return new BufferedImage( icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB_PRE );
	}
	
    public void exportAsDrag(JComponent comp, InputEvent e, int action) {
        int srcActions = getSourceActions(comp);
	int dragAction = srcActions & action;
	if (! (e instanceof MouseEvent)) {
	    // only mouse events supported for drag operations
	    dragAction = NONE;
	}
	if (dragAction != NONE && !GraphicsEnvironment.isHeadless()) {
	    if (recognizer == null) {
		recognizer = new SwingDragGestureRecognizer(new DragHandler());
	    }
	    recognizer.gestured(comp, (MouseEvent)e, srcActions, dragAction);
	} else {
            exportDone(comp, null, NONE);
        }
    }

	public Icon getVisualRepresentation( Transferable arg0 )
	{
		return icon;
	}

	public int getSourceActions( JComponent c )
	{
		return TransferHandler.COPY;
	}

	protected Transferable createTransferable( JComponent c )
	{
		final Piece piece = (Piece)( (Piece.Label)c ).getPiece().clone();
		layout.setDragging( piece.new Label() );
		return new Transferable()
		{
			public DataFlavor[] getTransferDataFlavors()
			{
				return new DataFlavor[] { NATIVE_FLAVOR };
			}

			public boolean isDataFlavorSupported( DataFlavor flavor )
			{
				return flavor.equals( NATIVE_FLAVOR );
			}

			public Object getTransferData( DataFlavor flavor ) throws UnsupportedFlavorException, IOException
			{
				return piece;
			}
		};
	}


    private static class SwingDragGestureRecognizer extends DragGestureRecognizer
    {
		private static final long serialVersionUID = 1L;

		SwingDragGestureRecognizer(DragGestureListener dgl) {
    	    super(DragSource.getDefaultDragSource(), null, NONE, dgl);
    	}

    	void gestured(JComponent c, MouseEvent e, int srcActions, int action) {
    	    setComponent(c);
                setSourceActions(srcActions);
    	    appendEvent(e);
    	    fireDragGestureRecognized(action, e.getPoint());
    	}

    	/**
    	 * register this DragGestureRecognizer's Listeners with the Component
    	 */
            protected void registerListeners() {
    	}

    	/**
    	 * unregister this DragGestureRecognizer's Listeners with the Component
    	 *
    	 * subclasses must override this method
    	 */
            protected void unregisterListeners() {
    	}

        }

	
	
    private static class DragHandler implements DragGestureListener, DragSourceListener {
        
        private boolean scrolls;

	// --- DragGestureListener methods -----------------------------------

	/**
	 * a Drag gesture has been recognized
	 */
        public void dragGestureRecognized(DragGestureEvent dge) {
	    JComponent c = (JComponent) dge.getComponent();
	    IconTransferHandler th = (IconTransferHandler)c.getTransferHandler();
	    Transferable t = th.createTransferable(c);
	    if (t != null) {
                scrolls = c.getAutoscrolls();
                c.setAutoscrolls(false);
                try {
                    dge.startDrag( null, th.getImage(), th.getOffset(), t, this );
                    return;
                } catch (RuntimeException re) {
                    c.setAutoscrolls(scrolls);
                }
	    }
            
            th.exportDone(c, null, NONE);
	}

	// --- DragSourceListener methods -----------------------------------

	/**
	 * as the hotspot enters a platform dependent drop site
	 */
        public void dragEnter(DragSourceDragEvent dsde) {
	}
  
	/**
	 * as the hotspot moves over a platform dependent drop site
	 */
        public void dragOver(DragSourceDragEvent dsde) {
	}
  
	/**
	 * as the hotspot exits a platform dependent drop site
	 */
        public void dragExit(DragSourceEvent dsde) {
	}
  
	/**
	 * as the operation completes
	 */
        public void dragDropEnd(DragSourceDropEvent dsde) {
            DragSourceContext dsc = dsde.getDragSourceContext();
            JComponent c = (JComponent)dsc.getComponent();
	    if (dsde.getDropSuccess()) {
               ( (IconTransferHandler)c.getTransferHandler() ).exportDone(c, dsc.getTransferable(), dsde.getDropAction());
	    } else {
	    	( (IconTransferHandler)c.getTransferHandler() ).exportDone(c, null, NONE);
            }
            c.setAutoscrolls(scrolls);
	}
  
        public void dropActionChanged(DragSourceDragEvent dsde) {
	}
    }
}
