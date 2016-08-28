package com.kindergarten.util.print;

import java.io.InputStream;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

/**
 * This Class is intended for a cebtral access to all icons used throughout the
 * package.
 * 
 * Alter the getImage() function, if you use other techniques of image access.
 */
public class IconSource
{
	/** Returns the requested Image */
	public static Image getImage(String name)
	{
		return loadImageResource(Display.getCurrent(), "/images/" + name + ".gif");
	}

	/**
	 * reads an Image as ressource (either from file system or from a jar)
	 */
	public static Image loadImageResource(Display d, String name)
	{
		try
		{
			Image ret = null;
			Class<?> clazz = new Object().getClass();
			InputStream is = clazz.getResourceAsStream(name);
			if (is != null)
			{
				ret = new Image(d, is);
				is.close();
			}
			if (ret == null)
				System.out.println("Error loading bitmap:\n" + name);
			return ret;
		} catch (Exception e1)
		{
			System.out.println("Error loading bitmap:\n" + name);
			return null;
		}
	}
}
