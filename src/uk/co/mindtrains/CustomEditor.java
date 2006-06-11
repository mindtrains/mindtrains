/*
 * Copyright (c) 2006 Andy Wood
 */
package uk.co.mindtrains;

import com.l2fprod.common.propertysheet.Property;
import com.l2fprod.common.propertysheet.PropertyEditorRegistry;

public interface CustomEditor
{
	public void registerEditors( PropertyEditorRegistry registry, Property[] properties );
}
