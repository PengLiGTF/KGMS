package com.kindergarten.util.print;

/**
 * Vertical whitspace.
 * 
 * @author Friederich Kupzog
 */
public class PVSpace extends PBox
{

	private double cm;

	/**
	 * Creates a new Space
	 */
	public PVSpace(PContainer parent, double cm)
	{
		super(parent);
		this.cm = cm;
		// getBoxStyle().backColor = SWT.COLOR_CYAN;
	}

	/*
	 * overridden from superclass
	 */
	protected int getWidth()
	{
		// return 1;
		return 0;
	}

	/*
	 * overridden from superclass
	 */
	protected int getHeight()
	{
		if (forcedHeight > 0)
			return forcedHeight;
		return PBox.pixelY(cm);
	}
}
