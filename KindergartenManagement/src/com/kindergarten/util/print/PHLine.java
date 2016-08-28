package com.kindergarten.util.print;

import org.eclipse.swt.SWT;

/**
 * A horzontal line
 * 
 * @author Friederich Kupzog
 */
public class PHLine extends PBox
{
	protected double thickness;
	protected int color;

	/**
	 * Creates a horizontal line with the given thickness and color.
	 */
	public PHLine(PContainer parent, double thickness, int color)
	{
		super(parent, POS_BELOW, 1.0, 0.0);
		this.thickness = thickness;
		boxStyle.lines = new double[]
		{ 0.0, 0.0, 0.0, 0.0 };
		boxStyle.backColor = color;
	}

	/**
	 * Creates a black thin horizontal line.
	 */
	public PHLine(PContainer parent)
	{
		super(parent, POS_BELOW, 1.0, 0.0);
		this.thickness = 0.01;
		boxStyle.lines = new double[]
		{ 0.0, 0.0, 0.0, 0.0 };
		boxStyle.backColor = SWT.COLOR_BLACK;
	}

	protected int getHeight()
	{
		if (forcedHeight > 0)
			return forcedHeight;

		int erg = PBox.pixelY(thickness);
		if (erg == 0)
			return 1;
		return erg;
	}

}
