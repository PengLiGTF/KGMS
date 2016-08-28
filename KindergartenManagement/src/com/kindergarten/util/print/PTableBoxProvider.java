package com.kindergarten.util.print;

import org.eclipse.swt.SWT;

/**
 * Used by PTable to print KTables. Comparable to KTableCellRenderer. Creates a
 * PBox for a Table cell. It gets width and height information from the model
 * which are pixel values fro screen view. It may use these values as a
 * guideline.
 * 
 * @author Friederich Kupzog
 */
public class PTableBoxProvider
{
	public PBox createBox(PContainer parent, int style, int col, int row, int widthFromModel, int heightFromModel, boolean fixed, Object content)
	{
		// create a text box
		PLittleTextBox box = new PLittleTextBox(parent, style, 0, widthFromModel * 0.03);
		// set its border lines
		PStyle boxStyle = PStyle.getDefaultStyle();
		boxStyle.lines = new double[]
		{ 0.005, 0.01, 0.005, 0.0 };
		if (row == 0)
			boxStyle.lines[0] = 0.02;
		if (col == 0)
			boxStyle.lines[3] = 0.01;
		box.setBoxStyle(boxStyle);

		// set the font
		PTextStyle textStyle = PTextStyle.getDefaultStyle();
		if (fixed) // Header row / column
		{
			textStyle.setMarginLeft(0.08);
			textStyle.setMarginRight(0.08);
			textStyle.setMarginTop(0.1);
			textStyle.setMarginBottom(0.1);
			textStyle.fontSize = 9;
			textStyle.fontStyle = SWT.BOLD;
			textStyle.textAlign = PTextStyle.ALIGN_LEFT;
		} else
		// normal cell
		{
			textStyle.setMarginLeft(0.08);
			textStyle.setMarginRight(0.08);
			textStyle.setMarginTop(0.1);
			textStyle.setMarginBottom(0.1);
			textStyle.fontSize = 9;
			textStyle.textAlign = PTextStyle.ALIGN_LEFT;
		}
		box.setTextStyle(textStyle);

		// set the text
		box.setText(content.toString());

		// ready
		return box;
	}

}
